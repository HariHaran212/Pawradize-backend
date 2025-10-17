package com.pawradise.api.controller;

import com.pawradise.api.dto.*;
import com.pawradise.api.response.ApiResponse;
import com.pawradise.api.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final UserService userService;

    public ProfileController(@Autowired UserService userService) {
        this.userService = userService;
    }

    /**
     * GET /api/profile/me : Gets the current logged-in user's profile.
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserProfileDto>> getCurrentUserProfile(@AuthenticationPrincipal UserDetails userDetails) {
        UserProfileDto userProfile = userService.getUserProfile(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success(userProfile, "Profile retrieved successfully"));
    }

    /**
     * PUT /api/profile/me : Updates the current user's profile.
     * Handles multipart data for the avatar image.
     */
    @PutMapping("/me")
    public ResponseEntity<ApiResponse<UserProfileDto>> updateUserProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestPart("profileData") UserProfileUpdateDto updateDto,
            @RequestPart(value = "avatarFile", required = false) MultipartFile avatarFile
    ) {
        UserProfileDto updatedProfile = userService.updateUserProfile(userDetails.getUsername(), updateDto, avatarFile);
        return ResponseEntity.ok(ApiResponse.success(updatedProfile, "Profile updated successfully"));
    }

    /**
     * POST /api/profile/send-otp : Sends an OTP to verify a new email or phone.
     */
    @PostMapping("/send-otp")
    public ResponseEntity<ApiResponse<Void>> sendOtp(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody OtpRequestDto otpRequest
    ) {
        // Pass the currently logged-in user's username to the service
        userService.sendVerificationOtp(userDetails.getUsername(), otpRequest.getField(), otpRequest.getValue());
        return ResponseEntity.ok(ApiResponse.success("OTP sent successfully"));
    }

    /**
     * POST /api/profile/verify-otp : Verifies the OTP and updates the user's email/phone.
     */
    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<Void>> verifyOtp(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody OtpVerifyDto otpVerifyDto
    ) {
        userService.verifyOtpAndUpdateUser(userDetails.getUsername(), otpVerifyDto.getField(), otpVerifyDto.getValue(), otpVerifyDto.getOtp());
        return ResponseEntity.ok(ApiResponse.success("Verification successful"));
    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody PasswordChangeDto passwordDto
    ) {
        userService.changePassword(userDetails.getUsername(), passwordDto);
        return ResponseEntity.ok(ApiResponse.success("Password changed successfully"));
    }

    @PutMapping("/settings")
    public ResponseEntity<ApiResponse<Void>> updateUserSettings(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody SettingsUpdateDto settingsDto
    ) {
        userService.updateUserSettings(userDetails.getUsername(), settingsDto);
        return ResponseEntity.ok(ApiResponse.success("Settings updated successfully"));
    }

    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse<Void>> deleteUserAccount(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        userService.deleteUserAccount(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success("Account deleted successfully"));
    }
}