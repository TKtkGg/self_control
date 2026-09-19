package com.tktkgg.selfcontrol.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.tktkgg.selfcontrol.dto.request.SettingRequest;
import com.tktkgg.selfcontrol.dto.response.SettingResponse;
import com.tktkgg.selfcontrol.repository.SettingRepository;
import com.tktkgg.selfcontrol.entity.Setting;
import com.tktkgg.selfcontrol.exception.ApiException;

@Service
public class SettingService {
    private final SettingRepository settingRepository;
    private final AuthService authService;

    public SettingService(SettingRepository settingRepository, AuthService authService) {
        this.authService = authService;
        this.settingRepository = settingRepository;
    }
    
    public SettingResponse getSetting() {
        UUID userId = authService.getCurrentUserId();
        Setting setting = settingRepository.findByUserId(userId)
            .orElseThrow(() -> ApiException.internalServerError(
                "SETTING_NOT_FOUND",
                "Setting is missing."
            ));
        return new SettingResponse(setting.getIsPublic(), setting.getIsAuthorizeNotification());
    }

    public SettingResponse updateSetting(SettingRequest request) {
        UUID userId = authService.getCurrentUserId();
        Setting setting = settingRepository.findByUserId(userId)
            .orElseThrow(() -> ApiException.internalServerError(
                "SETTING_NOT_FOUND",
                "Setting is missing."
            ));

        setting.setIsPublic(request.isPublic());
        setting.setIsAuthorizeNotification(request.isAuthorizeNotification());
        settingRepository.save(setting);

        return new SettingResponse(setting.getIsPublic(), setting.getIsAuthorizeNotification());
    }
}
