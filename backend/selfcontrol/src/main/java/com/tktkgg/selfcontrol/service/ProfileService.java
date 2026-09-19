package com.tktkgg.selfcontrol.service;

import org.springframework.stereotype.Service;

import com.tktkgg.selfcontrol.repository.UserRepository;
import com.tktkgg.selfcontrol.repository.ProfileRepository;
import com.tktkgg.selfcontrol.entity.Profile;
import com.tktkgg.selfcontrol.entity.User;
import com.tktkgg.selfcontrol.dto.request.UpdateProfileRequest;
import com.tktkgg.selfcontrol.dto.response.ProfileResponse;
import com.tktkgg.selfcontrol.repository.LikeRepository;

import java.util.UUID;
import com.tktkgg.selfcontrol.exception.ApiException;

@Service
public class ProfileService {
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final LikeRepository likeRepository;
    private final AuthService authService;

    public ProfileService(
        UserRepository userRepository,
        ProfileRepository profileRepository, 
        LikeRepository likeRepository,
        AuthService authService
    ) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.likeRepository = likeRepository;
        this.authService = authService;
    }

    public ProfileResponse getProfile(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
            ApiException.notFound("USER_NOT_FOUND", "User not found.")
        );
        Profile profile = profileRepository.findByUserId(userId);
        if (profile == null) {
            throw ApiException.internalServerError("PROFILE_NOT_FOUND", "Profile is missing.");
        }

        Boolean isLiked = getIsLiked(userId);

        return new ProfileResponse(
            userId, 
            user.getUsername(), 
            profile.getIcon(), 
            profile.getSelfIntroduce(), 
            likeRepository.countByTargetUserId(userId),
            isLiked
        );
    }

    public ProfileResponse updateProfile(UUID userId, UpdateProfileRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() ->
            ApiException.notFound("USER_NOT_FOUND", "User not found.")
        );
        Profile profile = profileRepository.findByUserId(userId);
        if (profile == null) {
            throw ApiException.internalServerError("PROFILE_NOT_FOUND", "Profile is missing.");
        }

        user.setUsername(request.username());
        profile.setIcon(request.icon());
        profile.setSelfIntroduce(request.selfIntroduce());

        userRepository.save(user);
        profileRepository.save(profile);

        return new ProfileResponse(userId,
            user.getUsername(), 
            profile.getIcon(), 
            profile.getSelfIntroduce(), 
            likeRepository.countByTargetUserId(userId),
            null
        );
    }

    private Boolean getIsLiked(UUID userId) {
        if (authService.getCurrentUserId().equals(userId)) {
            return null;
        } else {
            return likeRepository.findByUserIdAndTargetUserId(authService.getCurrentUserId(), userId).isPresent();
        }
    }
}
