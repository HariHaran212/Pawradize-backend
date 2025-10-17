package com.pawradise.api.repository;

import com.pawradise.api.models.SystemSettings;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SystemSettingsRepository extends MongoRepository<SystemSettings, String> {}