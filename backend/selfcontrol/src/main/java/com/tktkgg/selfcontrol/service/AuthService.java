package com.tktkgg.selfcontrol.service;

import java.util.Optional;
import java.util.List;
import java.time.DayOfWeek;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AnonymousAuthenticationToken;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

import com.tktkgg.selfcontrol.entity.User;
import com.tktkgg.selfcontrol.entity.Schedule;
import com.tktkgg.selfcontrol.entity.Setting;
import com.tktkgg.selfcontrol.entity.Profile;
import com.tktkgg.selfcontrol.repository.ScheduleRepository;
import com.tktkgg.selfcontrol.repository.SettingRepository;
import com.tktkgg.selfcontrol.repository.UserRepository;
import com.tktkgg.selfcontrol.repository.ProfileRepository;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;
    private final ProfileRepository profileRepository;
    private final SettingRepository settingRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
        UserRepository userRepository, 
        ScheduleRepository scheduleRepository, 
        ProfileRepository profileRepository, 
        SettingRepository settingRepository,
        PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.scheduleRepository = scheduleRepository;
        this.profileRepository = profileRepository;
        this.settingRepository = settingRepository;
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

    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        return authentication != null 
            && authentication.isAuthenticated()
            && !(authentication instanceof AnonymousAuthenticationToken);
    }

    public UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            throw new IllegalStateException("Unauthorized");
        }
        return (UUID) authentication.getPrincipal();
    }

    @Transactional
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

        for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
            Schedule schedule = new Schedule();
            schedule.setUser(user);
            schedule.setDayOfWeek(dayOfWeek);
            schedule.setTitle("");
            scheduleRepository.save(schedule);
        }

        Profile profile = new Profile();
        profile.setUser(user);
        profile.setIcon(null);
        profile.setSelfIntroduce("");
        profileRepository.save(profile);

        Setting setting = new Setting();
        setting.setUser(user);
        setting.setIsPublic(true);
        setting.setIsAuthorizeNotification(true);
        settingRepository.save(setting);

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
