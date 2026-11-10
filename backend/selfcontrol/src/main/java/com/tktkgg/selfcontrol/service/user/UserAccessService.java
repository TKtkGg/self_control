package com.tktkgg.selfcontrol.service.user;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.tktkgg.selfcontrol.entity.Setting;
import com.tktkgg.selfcontrol.exception.ApiException;
import com.tktkgg.selfcontrol.repository.SettingRepository;
import com.tktkgg.selfcontrol.service.auth.AuthService;

@Service 
public class UserAccessService {
    private final SettingRepository settingRepository;
    private final AuthService authService;

    public UserAccessService(SettingRepository settingRepository, AuthService authService) {
        this.settingRepository = settingRepository;
        this.authService = authService;
    }

    public void requireCanViewUser(UUID userId) {
        UUID currentUserId = authService.getCurrentUserId();

        if (currentUserId.equals(userId)) {
            return;
        }

        Setting setting = settingRepository.findByUserId(userId).orElseThrow(() -> 
            ApiException.notFound("SETTING_NOT_FOUND", "Setting is missing")
        );

        if (setting.getIsPublic() == false) {
            throw ApiException.forbidden("USER_FORBIDDEN", "CurrentUser is forbidden");
        }
    }
}
