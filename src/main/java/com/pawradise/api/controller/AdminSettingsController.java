package com.pawradise.api.controller;

import com.pawradise.api.dto.SystemSettingsDto;
import com.pawradise.api.response.ApiResponse;
import com.pawradise.api.service.SystemSettingsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/settings")
public class AdminSettingsController {

    private final SystemSettingsService settingsService;

    public AdminSettingsController(SystemSettingsService settingsService) {
        this.settingsService = settingsService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<SystemSettingsDto>> getSettings() {
        return ResponseEntity.ok(ApiResponse.success(settingsService.getSettings(), "Settings retrieved"));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<SystemSettingsDto>> updateSettings(@RequestBody SystemSettingsDto settingsDto) {
        return ResponseEntity.ok(ApiResponse.success(settingsService.updateSettings(settingsDto), "Settings updated"));
    }
}