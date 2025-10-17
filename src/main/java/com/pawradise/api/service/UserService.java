package com.pawradise.api.service;

import com.pawradise.api.dto.*;
import com.pawradise.api.models.Role;
import com.pawradise.api.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    User processOAuthPostLogin(String email, String firstName, String lastName, String avatarUrl);
    UserProfileDto getUserProfile(String username);
    UserProfileDto updateUserProfile(String username, UserProfileUpdateDto updateDto, MultipartFile avatarFile);
    void sendVerificationOtp(String username, String field, String value);
    void verifyOtpAndUpdateUser(String username, String field, String value, String otp);
    void changePassword(String username, PasswordChangeDto passwordDto);
    /**
     * Finds and paginates users for the admin view, with optional search and role filters.
     * Maps results to a safe DTO.
     */
    Page<UserAdminViewDto> findUsers(String search, Role role, Pageable pageable);

    /**
     * Updates the role of a specific user.
     * @return The updated user profile as a DTO.
     */
    UserAdminViewDto updateUserRole(String userId, Role newRole);

    /**
     * Deletes a user by their ID.
     */
    void deleteUser(String userId);

    void updateUserSettings(String username, SettingsUpdateDto settingsDto);

    void deleteUserAccount(String username);
}
