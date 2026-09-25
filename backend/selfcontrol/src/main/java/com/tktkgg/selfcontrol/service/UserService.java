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
import com.tktkgg.selfcontrol.exception.ApiException;

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

    public UsersResponse getUsers(int page, int size, String username) {
        if (page < 0 || size < 1 || size > 100) {
            throw ApiException.badRequest(
                "INVALID_PAGINATION",
                "page must be 0 or greater and size must be between 1 and 100."
            );
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<User> users;

        if (username == null || username.trim().isEmpty()) {
            users = userRepository.findAll(pageable);
        } else {
            users = userRepository.findByUsernameContainingIgnoreCase(
                username.trim(), 
                pageable
            );
        }
        
        List<UserResponse> userResponses = 
            users.stream().map(user -> 
                new UserResponse(user.getId(), user.getUsername())
            ).filter(user -> !user.id().equals(authService.getCurrentUserId())
            ).filter(user -> settingRepository.findByUserId(user.id()).
                                orElseThrow(() -> ApiException.internalServerError(
                                    "SETTING_NOT_FOUND",
                                    "User setting is missing."
                                ))
                                .getIsPublic() == true)
            .collect(Collectors.toList());

        return new UsersResponse(userResponses, page, size, users.hasNext());
    }

    public LikeCountResponse likeUser(UUID currentUserId, UUID targetUserId) {
        Optional<Like> existingLike = likeRepository.findByUserIdAndTargetUserId(currentUserId, targetUserId);
        if (existingLike.isPresent()) {
            throw ApiException.badRequest("ALREADY_LIKED", "The user is already liked.");
        }

        if (currentUserId.equals(targetUserId)) {
            throw ApiException.forbidden("SELF_LIKE_NOT_ALLOWED", "You cannot like yourself.");
        }

        User currentUser = userRepository.findById(currentUserId).orElseThrow(() ->
            ApiException.notFound("USER_NOT_FOUND", "User not found.")
        );
        User targetUser = userRepository.findById(targetUserId).orElseThrow(() ->
            ApiException.notFound("USER_NOT_FOUND", "User not found.")
        );

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
            throw ApiException.badRequest("LIKE_NOT_FOUND", "The user has not been liked.");
        }

        likeRepository.delete(existingLike.get());

        return new LikeCountResponse(likeRepository.countByTargetUserId(targetUserId), false);
    }
}
