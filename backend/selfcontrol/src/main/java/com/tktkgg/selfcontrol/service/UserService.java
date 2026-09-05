package com.tktkgg.selfcontrol.service;

import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tktkgg.selfcontrol.repository.UserRepository;
import com.tktkgg.selfcontrol.entity.User;
import com.tktkgg.selfcontrol.entity.Like;
import com.tktkgg.selfcontrol.repository.LikeRepository;
import com.tktkgg.selfcontrol.repository.SettingRepository;
import com.tktkgg.selfcontrol.dto.response.UserResponse;
import com.tktkgg.selfcontrol.dto.response.UsersResponse;
import com.tktkgg.selfcontrol.dto.response.LikeCountResponse;

@Service
public class UserService {
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final SettingRepository settingRepository;
    private final AuthService authService;

    public UserService(UserRepository userRepository, LikeRepository likeRepository, SettingRepository settingRepository, AuthService authService) {
        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
        this.settingRepository = settingRepository;
        this.authService = authService;
    }

    public UsersResponse getUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = userRepository.findAll(pageable);
        List<UserResponse> userResponses = 
            users.stream().map(user -> 
                new UserResponse(user.getId(), user.getUsername())
            ).filter(user -> !user.id().equals(authService.getCurrentUserId())
            ).filter(user -> settingRepository.findByUserId(user.id()).
                                orElseThrow(() -> new RuntimeException("User's setting not found"))
                                .getIsPublic() == true)
            .collect(Collectors.toList());

        return new UsersResponse(userResponses, page, size, users.hasNext());
    }

    public LikeCountResponse likeUser(UUID currentUserId, UUID targetUserId) {
        Optional<Like> existingLike = likeRepository.findByUserIdAndTargetUserId(currentUserId, targetUserId);
        if (existingLike.isPresent()) {
            throw new RuntimeException("Already liked");
        }

        User currentUser = userRepository.findById(currentUserId).orElseThrow(() -> new RuntimeException("User not found"));
        User targetUser = userRepository.findById(targetUserId).orElseThrow(() -> new RuntimeException("User not found"));

        Like like = new Like();
        like.setUser(currentUser);
        like.setTargetUser(targetUser);
        like.setCreatedAt(LocalDateTime.now());
        likeRepository.save(like);

        return new LikeCountResponse(likeRepository.countByTargetUserId(targetUserId), true);
    }

    public LikeCountResponse unlikeUser(UUID currentUserId, UUID targetUserId) {
        Optional<Like> existingLike = likeRepository.findByUserIdAndTargetUserId(currentUserId, targetUserId);
        if (!existingLike.isPresent()) {
            throw new RuntimeException("Not liked");
        }

        likeRepository.delete(existingLike.get());

        return new LikeCountResponse(likeRepository.countByTargetUserId(targetUserId), false);
    }
}
