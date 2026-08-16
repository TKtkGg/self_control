package com.tktkgg.selfcontrol.service;

import java.util.Optional;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

import com.tktkgg.selfcontrol.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.tktkgg.selfcontrol.entity.User;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private void establishSession(User user, HttpServletRequest request, HttpServletResponse response) {
        UsernamePasswordAuthenticationToken authentication = 
            new UsernamePasswordAuthenticationToken(
                user.getId(),
                null,
                List.of()
            );

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        SecurityContextRepository repository = new HttpSessionSecurityContextRepository();
        repository.saveContext(context, request, response);
    }

    public void signUp(String username, String email, String password, String passwordConfirm, HttpServletRequest request, HttpServletResponse response) {
        if (!password.equals(passwordConfirm)) {
            throw new IllegalArgumentException("Password and password confirmation do not match");
        }

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already in use");
        }
        
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(password));
        userRepository.save(user);

        establishSession(user, request, response);
    }

    public void login(String email, String password, HttpServletRequest request, HttpServletResponse response) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        if (!passwordEncoder.matches(password, user.get().getPasswordHash())) {
            throw new IllegalArgumentException("Invalid password");
        }

        establishSession(user.get(), request, response);
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextHolder.clearContext();

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }
}
