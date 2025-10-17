package com.pawradise.api.controller;

import com.pawradise.api.dto.UserAdminViewDto;
import com.pawradise.api.models.Role;
import com.pawradise.api.response.ApiResponse;
import com.pawradise.api.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/users")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<UserAdminViewDto>>> getAllUsers(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Role role,
            Pageable pageable
    ) {
        Page<UserAdminViewDto> users = userService.findUsers(search, role, pageable);
        return ResponseEntity.ok(ApiResponse.success(users, "Users retrieved successfully"));
    }

    @PatchMapping("/{id}/role")
    public ResponseEntity<ApiResponse<UserAdminViewDto>> updateUserRole(
            @PathVariable String id,
            @RequestParam Role newRole
    ) {
        UserAdminViewDto updatedUser = userService.updateUserRole(id, newRole);
        return ResponseEntity.ok(ApiResponse.success(updatedUser, "User role updated"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success("User deleted successfully"));
    }
}