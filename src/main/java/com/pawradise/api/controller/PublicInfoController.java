package com.pawradise.api.controller;

import com.pawradise.api.dto.SystemSettingsDto;
import com.pawradise.api.response.ApiResponse;
import com.pawradise.api.service.SystemSettingsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/info")
public class PublicInfoController {

    private final SystemSettingsService settingsService;

    public PublicInfoController(SystemSettingsService settingsService) {
        this.settingsService = settingsService;
    }

    @GetMapping("/contact")
    public ResponseEntity<ApiResponse<SystemSettingsDto>> getPublicContactInfo() {
        return ResponseEntity.ok(ApiResponse.success(settingsService.getSettings(), "Contact info retrieved"));
    }
}