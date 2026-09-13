package com.tktkgg.selfcontrol.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tktkgg.selfcontrol.dto.response.SettingResponse;
import com.tktkgg.selfcontrol.dto.request.SettingRequest;
import com.tktkgg.selfcontrol.service.SettingService;

@RestController
@RequestMapping("/api/settings")
public class SettingController {
    private final SettingService settingService;

    public SettingController(SettingService settingService) {
        this.settingService = settingService;
    }

    @GetMapping("")
    public SettingResponse getSetting() {
        return settingService.getSetting();
    }

    @PatchMapping("")
    public SettingResponse updateSetting(@RequestBody SettingRequest request) {
        return settingService.updateSetting(request);
    }
}
