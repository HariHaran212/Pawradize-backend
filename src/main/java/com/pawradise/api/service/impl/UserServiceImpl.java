package com.pawradise.api.service.impl;
import com.pawradise.api.dto.*;
import com.pawradise.api.models.AccountStatus;
import com.pawradise.api.models.AuthProvider;
import com.pawradise.api.models.Role;
import com.pawradise.api.models.User;
import com.pawradise.api.repository.UserRepository;
import com.pawradise.api.service.CloudinaryService;
import com.pawradise.api.service.EmailService;
import com.pawradise.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,  CloudinaryService cloudinaryService,  EmailService emailService,  PasswordEncoder passwordEncoder, MongoTemplate mongoTemplate) {
        this.userRepository = userRepository;
        this.cloudinaryService = cloudinaryService;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public User processOAuthPostLogin(String email, String firstName, String lastName, String avatarUrl) {
        Optional<User> existUser = userRepository.findByEmail(email);

        if (existUser.isPresent()) {
            // If user exists, you might want to update their details
            User user = existUser.get();
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setAvatarUrl(avatarUrl);
            return userRepository.save(user);
        } else {
            // If user does not exist, create a new one
            User newUser = new User();

            // Generate a unique username
            String baseUsername = email.split("@")[0].replaceAll("[^a-zA-Z0-9]", ""); // Sanitize username
            String username = baseUsername;
            int counter = 1;
            while (userRepository.findByUsername(username).isPresent()) {
                username = baseUsername + counter++;
            }

            newUser.setUsername(username);
            newUser.setEmail(email);
            newUser.setFirstName(firstName);
            newUser.setLastName(lastName);
            newUser.setAvatarUrl(avatarUrl); // <-- Save the profile picture URL
            newUser.setRole(Role.USER); // Set a default role
            newUser.setProvider(AuthProvider.GOOGLE); // Mark as a Google user

            return userRepository.save(newUser);
        }
    }

    private String uploadAvatar(MultipartFile imageFile) {
        if (imageFile != null && !imageFile.isEmpty()) {
            return cloudinaryService.uploadFile(imageFile);
        }
        else throw new IllegalArgumentException("Image file is empty");
    }

    @Override
    public UserProfileDto getUserProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        UserProfileDto dto = new UserProfileDto();
        dto.setName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhoneNumber());
        dto.setAddress(user.getAddress());
        dto.setAvatarUrl(user.getAvatarUrl());
        dto.setEmailPromotions(user.isEmailPromotions());
        dto.setEmailOrderUpdates(user.isEmailOrderUpdates());
        dto.setEmailPromotions(user.isEmailPromotions());
        dto.setRole(user.getRole());

        return dto;
    }

    @Override
    public UserProfileDto updateUserProfile(String username, UserProfileUpdateDto updateDto, MultipartFile avatarFile) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Update basic fields
        user.setFirstName(updateDto.getName().split(" ")[0]); // Simple name splitting
        user.setLastName(updateDto.getName().split(" ").length > 1 ? updateDto.getName().split(" ")[1] : "");
        user.setAddress(updateDto.getAddress());

        // Handle avatar upload
        if (avatarFile != null && !avatarFile.isEmpty()) {
            String avatarUrl = this.uploadAvatar(avatarFile);
            user.setAvatarUrl(avatarUrl);
        }

        User updatedUser = userRepository.save(user);
        return getUserProfile(updatedUser.getUsername()); // Return the updated profile
    }

    @Override
    public void sendVerificationOtp(String username, String field, String value) {
        // Find the user who is making the request
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        // 1. Generate OTP
        String otp = String.format("%06d", new Random().nextInt(999999));

        // 2. Save the OTP and its expiry time to the user's record
        user.setVerificationOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(10)); // OTP is valid for 10 minutes
        userRepository.save(user);

        // 3. Send the OTP via email asynchronously
        if("email".equalsIgnoreCase(field)) {
            emailService.sendOtpEmail(value, otp);
        }
        else if("phone".equalsIgnoreCase(field)) {
            emailService.sendOtpEmail(user.getEmail(), otp);
        }
        else throw new IllegalArgumentException("Invalid verification field specified.");
    }

    @Override
    public void verifyOtpAndUpdateUser(String username, String field, String value, String otp) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (user.getVerificationOtp() == null ||
                !user.getVerificationOtp().equals(otp) ||
                user.getOtpExpiry().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Invalid or expired OTP.");
        }

        System.out.println(value+" "+field+" "+otp);

        if ("email".equalsIgnoreCase(field)) {
            user.setEmail(value);
        } else if ("phone".equalsIgnoreCase(field)) {
            user.setPhoneNumber(value);
            System.out.println(user.getPhoneNumber());
        } else {
            throw new IllegalArgumentException("Invalid verification field specified.");
        }

        user.setVerificationOtp(null);
        user.setOtpExpiry(null);

        userRepository.save(user);
    }

    @Override
    public void changePassword(String username, PasswordChangeDto passwordDto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // 1. Verify the old password
        if (!passwordEncoder.matches(passwordDto.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Incorrect old password.");
        }

        // 2. Encode and set the new password
        user.setPassword(passwordEncoder.encode(passwordDto.getNewPassword()));

        // 3. Save the updated user
        userRepository.save(user);
    }

    @Override
    public Page<UserAdminViewDto> findUsers(String search, Role role, Pageable pageable) {
        Query query = new Query().with(pageable);
        List<Criteria> criteria = new ArrayList<>();

        // Add role filter if provided
        if (role != null) {
            criteria.add(Criteria.where("role").is(role));
        }

        // Add search filter if provided
        if (search != null && !search.trim().isEmpty()) {
            Pattern searchPattern = Pattern.compile(search, Pattern.CASE_INSENSITIVE);
            criteria.add(new Criteria().orOperator(
                    Criteria.where("firstName").regex(searchPattern),
                    Criteria.where("lastName").regex(searchPattern),
                    Criteria.where("email").regex(searchPattern)
            ));
        }

        if (!criteria.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[0])));
        }

        // Fetch the user entities
        List<User> users = mongoTemplate.find(query, User.class);

        // Get the total count for pagination
        long count = mongoTemplate.count(Query.of(query).limit(-1).skip(-1), User.class);

        // Map the User entities to the safe UserAdminViewDto
        List<UserAdminViewDto> userDtos = users.stream()
                .map(UserAdminViewDto::fromUser)
                .collect(Collectors.toList());

        return new PageImpl<>(userDtos, pageable, count);
    }

    @Override
    public UserAdminViewDto updateUserRole(String userId, Role newRole) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));

        user.setRole(newRole);
        User updatedUser = userRepository.save(user);

        return UserAdminViewDto.fromUser(updatedUser);
    }

    @Override
    public void deleteUser(String userId) {
        // First check if the user exists to provide a clear error
        if (!userRepository.existsById(userId)) {
            throw new UsernameNotFoundException("User not found with id: " + userId);
        }

        // TODO: Add business logic here, e.g., prevent a user from deleting their own account,
        // or prevent the deletion of the last SUPER_ADMIN.

        userRepository.deleteById(userId);
    }

    @Override
    public void updateUserSettings(String username, SettingsUpdateDto settingsDto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.setEmailPromotions(settingsDto.isEmailPromotions());
        user.setEmailOrderUpdates(settingsDto.isEmailOrderUpdates());
        user.setEmailPetUpdates(settingsDto.isEmailPetUpdates());

        userRepository.save(user);
    }

    @Override
    public void deleteUserAccount(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.setStatus(AccountStatus.DEACTIVATED);
        user.setDeactivationDate(LocalDateTime.now());
        userRepository.save(user);

        emailService.sendAccountDeactivationNotice(user.getEmail(), user.getFirstName());
    }

    /**
     * Runs every day at 2:00 AM to find and anonymize deactivated accounts
     * that are older than 15 days.
     */
    @Scheduled(cron = "0 0 2 * * ?") // Cron for 2 AM daily
    @Transactional
    public void anonymizeDeactivatedAccounts() {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(15);

        // Find all accounts that are ready to be anonymized
        List<User> usersToAnonymize = userRepository.findByStatusAndDeactivationDateBefore(AccountStatus.DEACTIVATED, cutoffDate);

        for (User user : usersToAnonymize) {
            // Anonymize Personal Identifiable Information (PII)
            String anonymizedId = "deleted-" + user.getId();
            user.setFirstName("Deleted");
            user.setLastName("User");
            user.setEmail(anonymizedId + "@pawradise.com"); // Preserve email uniqueness
            user.setUsername(anonymizedId);
            user.setPhoneNumber(null);
            user.setAddress(null);
            user.setPassword(null);
            user.setAvatarUrl(null);
            user.setProvider(null);

            // Set final status
            user.setStatus(AccountStatus.ANONYMIZED);
            user.setDeactivationDate(null);

            userRepository.save(user);

            // TODO: You should also anonymize this user's data in other collections,
            // e.g., update the 'customerName' on their past Orders.

            // Send final deletion confirmation email
            emailService.sendAccountDeletionConfirmation(user.getEmail(), user.getFirstName());
        }
    }
}