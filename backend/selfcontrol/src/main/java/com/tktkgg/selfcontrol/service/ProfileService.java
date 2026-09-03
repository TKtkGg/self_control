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

@Service
public class ProfileService {
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final LikeRepository likeRepository;

    public ProfileService(
        UserRepository userRepository,
        ProfileRepository profileRepository, 
        LikeRepository likeRepository
    ) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.likeRepository = likeRepository;
    }

    public ProfileResponse getProfile(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Profile profile = profileRepository.findByUserId(userId);

        return new ProfileResponse(
            userId, 
            user.getUsername(), 
            profile.getIcon(), 
            profile.getSelfIntroduce(), 
            likeRepository.countByTargetUserId(userId));
    }

    public ProfileResponse updateProfile(UUID userId, UpdateProfileRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Profile profile = profileRepository.findByUserId(userId);

        user.setUsername(request.getUsername());
        profile.setIcon(request.getIcon());
        profile.setSelfIntroduce(request.getSelfIntroduce());

        userRepository.save(user);
        profileRepository.save(profile);

        return new ProfileResponse(userId,
            user.getUsername(), 
            profile.getIcon(), 
            profile.getSelfIntroduce(), 
            likeRepository.countByTargetUserId(userId));
    }
}
