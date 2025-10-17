package com.pawradise.api.service.impl;

import com.pawradise.api.dto.SystemSettingsDto;
import com.pawradise.api.models.SystemSettings;
import com.pawradise.api.repository.SystemSettingsRepository;
import com.pawradise.api.service.SystemSettingsService;
import org.springframework.stereotype.Service;

@Service
public class SystemSettingsServiceImpl implements SystemSettingsService {

    private final SystemSettingsRepository settingsRepository;

    public SystemSettingsServiceImpl(SystemSettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }

    @Override
    public SystemSettingsDto getSettings() {
        // Find the settings document or create it with defaults if it doesn't exist
        SystemSettings settings = settingsRepository.findById(SystemSettings.SINGLETON_ID)
                .orElseGet(() -> settingsRepository.save(new SystemSettings()));
        return toDto(settings);
    }

    @Override
    public SystemSettingsDto updateSettings(SystemSettingsDto settingsDto) {
        SystemSettings settings = settingsRepository.findById(SystemSettings.SINGLETON_ID)
                .orElseGet(SystemSettings::new);

        settings.setNewUserEmail(settingsDto.isNewUserEmail());
        settings.setNewOrderEmail(settingsDto.isNewOrderEmail());
        settings.setLowStockAlerts(settingsDto.isLowStockAlerts());

        settings.setStoreAddress(settingsDto.getStoreAddress());
        settings.setStoreHours(settingsDto.getStoreHours());
        settings.setStorePhone(settingsDto.getStorePhone());
        settings.setStoreEmail(settingsDto.getStoreEmail());

        SystemSettings updatedSettings = settingsRepository.save(settings);
        return toDto(updatedSettings);
    }

    private SystemSettingsDto toDto(SystemSettings settings) {
        SystemSettingsDto dto = new SystemSettingsDto();

        dto.setNewUserEmail(settings.isNewUserEmail());
        dto.setNewOrderEmail(settings.isNewOrderEmail());
        dto.setLowStockAlerts(settings.isLowStockAlerts());

        dto.setStoreAddress(settings.getStoreAddress());
        dto.setStoreHours(settings.getStoreHours());
        dto.setStorePhone(settings.getStorePhone());
        dto.setStoreEmail(settings.getStoreEmail());
        return dto;
    }
}