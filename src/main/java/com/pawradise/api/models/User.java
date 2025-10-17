package com.pawradise.api.models; // Your package name

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Data
@Document(collection = "users")
public class User implements UserDetails {

    @Id
    private String id;

    @Indexed(unique = true)
    private String username;
    private String firstName;
    private String lastName;

    private String phoneNumber;

    @Indexed(unique = true)
    private String email;
    private String password;
    private String address;
    private String avatarUrl;

    private Role role; // <-- Added for role-based security
    private AuthProvider provider; // To track if user signed up locally or via Google

    private String verificationOtp;
    private LocalDateTime otpExpiry;

    private boolean emailPromotions = true;
    private boolean emailOrderUpdates = true;
    private boolean emailPetUpdates = true;

    private AccountStatus status = AccountStatus.ACTIVE;
    private LocalDateTime deactivationDate;

    public String getFullName(){
        if(lastName == null) return firstName;
        return firstName + " " + lastName;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (role == null) {
            return Collections.emptyList();
        }
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        // You can add logic to expire accounts here.
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // You can add logic to lock accounts here.
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // You can add logic for password expiration here.
        return true;
    }

    @Override
    public boolean isEnabled() {
        // You can add logic to disable users here.
        return true;
    }
}