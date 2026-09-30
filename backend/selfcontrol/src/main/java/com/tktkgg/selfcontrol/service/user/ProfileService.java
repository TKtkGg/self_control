package com.tktkgg.selfcontrol.service.user;

import org.springframework.stereotype.Service;

import com.tktkgg.selfcontrol.repository.UserRepository;
import com.tktkgg.selfcontrol.service.auth.AuthService;
import com.tktkgg.selfcontrol.repository.ProfileRepository;
import com.tktkgg.selfcontrol.repository.SettingRepository;
import com.tktkgg.selfcontrol.entity.Profile;
import com.tktkgg.selfcontrol.entity.Setting;
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
    private final SettingRepository settingRepository;
    private final AuthService authService;

    public ProfileService(
        UserRepository userRepository,
        ProfileRepository profileRepository, 
        LikeRepository likeRepository,
        SettingRepository settingRepository,
        AuthService authService
    ) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.likeRepository = likeRepository;
        this.settingRepository = settingRepository;
        this.authService = authService;
    }

    public ProfileResponse getMyProfile() {
        UUID currentUserId = authService.getCurrentUserId();

        return getProfile(currentUserId);
    }

    public ProfileResponse getUserProfile(UUID targetUserId) {
        return getProfile(targetUserId);
    }

    public ProfileResponse updateMyProfile(UpdateProfileRequest request) {
        UUID currentUserId = authService.getCurrentUserId();

        User user = userRepository.findById(currentUserId).orElseThrow(() ->
            ApiException.notFound("USER_NOT_FOUND", "User not found.")
        );

        Profile profile = profileRepository.findByUserId(currentUserId).orElseThrow(() -> 
            ApiException.internalServerError("PROFILE_NOT_FOUND", "Profile is missing.")
        );

        user.setUsername(request.username());
        profile.setIcon(request.icon());
        profile.setSelfIntroduce(request.selfIntroduce());

        userRepository.save(user);
        profileRepository.save(profile);

        return new ProfileResponse(
            currentUserId,
            user.getUsername(), 
            profile.getIcon(), 
            profile.getSelfIntroduce(), 
            likeRepository.countByTargetUserId(currentUserId),
            null
        );
    }

    private ProfileResponse getProfile(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
            ApiException.notFound("USER_NOT_FOUND", "User not found.")
        );

        Profile profile = profileRepository.findByUserId(userId).orElseThrow(() -> 
            ApiException.internalServerError("PROFILE_NOT_FOUND", "Profile is missing.")
        );

        if (userId.equals(authService.getCurrentUserId())) {
            Setting setting = settingRepository.findByUserId(userId).orElseThrow(() -> 
                ApiException.notFound("SETTING_NOT_FOUND", "Setting is missing")
            );

            if (setting.getIsPublic() == false) {
                throw ApiException.forbidden("USER_FORBIDDEN", "CurrentUser is forbidden");
            }
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

    private Boolean getIsLiked(UUID userId) {
        UUID currentUserId = authService.getCurrentUserId();

        if (currentUserId.equals(userId)) {
            return null;
        } else {
            return likeRepository.findByUserIdAndTargetUserId(currentUserId, userId).isPresent();
        }
    }
}
