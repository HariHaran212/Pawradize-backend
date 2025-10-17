package com.pawradise.api.dto;

import com.pawradise.api.models.Role;
import lombok.Data;

@Data
public class UserProfileDto {
    private String name;
    private String email;
    private String phone;
    private String address;
    private String avatarUrl;
    private boolean emailPromotions;
    private boolean emailOrderUpdates;
    private boolean emailPetUpdates;
    private Role role;
}