package com.pawradise.api.service.impl;

import com.pawradise.api.models.AccountStatus;
import com.pawradise.api.models.User;
import com.pawradise.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. Find the user from the database
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // 2. Check for ANONYMIZED status first
        if (user.getStatus() == AccountStatus.ANONYMIZED) {
            throw new UsernameNotFoundException("User account has been permanently deleted.");
        }

        // 3. Check and REACTIVATE the account if necessary
        if (user.getStatus() == AccountStatus.DEACTIVATED) {
            user.setStatus(AccountStatus.ACTIVE);
            user.setDeactivationDate(null);
            userRepository.save(user);
            // Optionally, send a "Welcome Back!" email here.
        }

        // 4. Return the user object for Spring Security to handle authentication
        return user;
    }
}