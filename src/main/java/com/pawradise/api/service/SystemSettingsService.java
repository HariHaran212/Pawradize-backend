package com.pawradise.api.service;

import com.pawradise.api.dto.SystemSettingsDto;

public interface SystemSettingsService {
    SystemSettingsDto getSettings();
    SystemSettingsDto updateSettings(SystemSettingsDto settingsDto);
}