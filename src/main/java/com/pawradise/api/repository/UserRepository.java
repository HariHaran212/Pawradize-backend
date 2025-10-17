package com.pawradise.api.repository;

import com.pawradise.api.models.AccountStatus;
import com.pawradise.api.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    List<User> findByStatusAndDeactivationDateBefore(AccountStatus status, LocalDateTime cutoffDate);
}