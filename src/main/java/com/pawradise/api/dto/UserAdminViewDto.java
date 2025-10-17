package com.pawradise.api.dto;

import com.pawradise.api.models.Role;
import com.pawradise.api.models.User;
import lombok.Data;
import org.bson.types.ObjectId;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Data
public class UserAdminViewDto {
    private String id;
    private String avatarUrl;
    private String name;
    private String email;
    private Role role;
    private String joinedDate;

    // A helper method to convert a User entity to this safe DTO
    public static UserAdminViewDto fromUser(User user) {
        UserAdminViewDto dto = new UserAdminViewDto();
        dto.setId(user.getId());
        dto.setAvatarUrl(user.getAvatarUrl());
        dto.setName(user.getFirstName() + " " + user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());

        // Derive the joined date from the MongoDB ObjectId
        Instant instant = new ObjectId(user.getId()).getDate().toInstant();
        dto.setJoinedDate(DateTimeFormatter.ofPattern("yyyy-MM-dd")
                .withZone(ZoneId.systemDefault())
                .format(instant));

        return dto;
    }
}