package com.tktkgg.selfcontrol.service.user;

import org.springframework.stereotype.Service;

import com.tktkgg.selfcontrol.repository.UserRepository;
import com.tktkgg.selfcontrol.service.auth.AuthService;

import jakarta.transaction.Transactional;

import com.tktkgg.selfcontrol.repository.ProfileRepository;
import com.tktkgg.selfcontrol.entity.Profile;
import com.tktkgg.selfcontrol.entity.User;
import com.tktkgg.selfcontrol.dto.request.UpdateProfileRequest;
import com.tktkgg.selfcontrol.dto.response.ProfileResponse;
import com.tktkgg.selfcontrol.repository.LikeRepository;

import java.util.Objects;
import java.util.UUID;
import com.tktkgg.selfcontrol.exception.ApiException;

@Service
public class ProfileService {
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final LikeRepository likeRepository;
    private final AuthService authService;
    private final UserAccessService userAccessService;

    public ProfileService(
        UserRepository userRepository,
        ProfileRepository profileRepository, 
        LikeRepository likeRepository,
        AuthService authService,
        UserAccessService userAccessService

    ) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.likeRepository = likeRepository;
        this.authService = authService;
        this.userAccessService = userAccessService;
    }

    public ProfileResponse getMyProfile() {
        UUID currentUserId = authService.getCurrentUserId();

        return getProfile(currentUserId);
    }

    public ProfileResponse getUserProfile(UUID targetUserId) {
        return getProfile(targetUserId);
    }

    @Transactional 
    public ProfileResponse updateMyProfile(UpdateProfileRequest request) {
        UUID currentUserId = authService.getCurrentUserId();

        User user = userRepository.findById(currentUserId).orElseThrow(() ->
            ApiException.notFound("USER_NOT_FOUND", "User not found.")
        );

        if (!Objects.equals(user.getVersion(), request.expectedUserVersion())) {
            throw ApiException.conflict(
                "RESOURCE_VERSION_CONFLICT",
                "This user was changed. Reload it before updating."
            );
        }

        Profile profile = profileRepository.findByUserId(currentUserId).orElseThrow(() -> 
            ApiException.internalServerError("PROFILE_NOT_FOUND", "Profile is missing.")
        );

        if (!Objects.equals(profile.getVersion(), request.expectedProfileVersion())) {
            throw ApiException.conflict(
                "RESOURCE_VERSION_CONFLICT",
                "This profile was changed. Reload it before updating."
            );
        }

        user.setUsername(request.username());
        profile.setIcon(request.icon());
        profile.setSelfIntroduce(request.selfIntroduce());

        userRepository.save(user);
        profileRepository.saveAndFlush(profile);

        return new ProfileResponse(
            currentUserId,
            user.getUsername(), 
            profile.getIcon(), 
            profile.getSelfIntroduce(), 
            likeRepository.countByTargetUserId(currentUserId),
            null,
            user.getVersion(),
            profile.getVersion()
        );
    }

    private ProfileResponse getProfile(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
            ApiException.notFound("USER_NOT_FOUND", "User not found.")
        );

        Profile profile = profileRepository.findByUserId(userId).orElseThrow(() -> 
            ApiException.internalServerError("PROFILE_NOT_FOUND", "Profile is missing.")
        );

        userAccessService.requireCanViewUser(userId);
        
        Boolean isLiked = getIsLiked(userId);

        return new ProfileResponse(
            userId, 
            user.getUsername(), 
            profile.getIcon(), 
            profile.getSelfIntroduce(), 
            likeRepository.countByTargetUserId(userId),
            isLiked,
            user.getVersion(),
            profile.getVersion()
        );
    }

    private Boolean getIsLiked(UUID userId) {
        UUID currentUserId = authService.getCurrentUserId();

        if (currentUserId.equals(userId)) {
            return null;
        } else {
            return likeRepository.findByUserIdAndTargetUserId(currentUserId, userId).isPresent();
        }
    }
}
