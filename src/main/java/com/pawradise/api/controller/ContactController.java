package com.pawradise.api.controller;

import com.pawradise.api.dto.ContactMessageDto;
import com.pawradise.api.models.ContactMessage;
import com.pawradise.api.response.ApiResponse;
import com.pawradise.api.service.ContactMessageService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ContactController {

    private final ContactMessageService messageService;

    public ContactController(ContactMessageService messageService) {
        this.messageService = messageService;
    }

    // --- PUBLIC ENDPOINT for user submissions ---
    @PostMapping("/api/contact")
    public ResponseEntity<ApiResponse<Void>> submitContactForm(@Valid @RequestBody ContactMessageDto messageDto) {
        messageService.saveMessage(messageDto);
        return ResponseEntity.ok(ApiResponse.success("Message sent successfully!"));
    }

    // --- ADMIN ENDPOINTS for managing messages ---
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/api/admin/contact")
    public ResponseEntity<ApiResponse<List<ContactMessage>>> getContactMessages(
            @RequestParam(required = false) ContactMessage.Status status
    ) {
        List<ContactMessage> messages = messageService.findMessages(status);
        return ResponseEntity.ok(ApiResponse.success(messages, "Messages retrieved"));
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PatchMapping("/api/admin/contact/{id}/status")
    public ResponseEntity<ApiResponse<ContactMessage>> updateMessageStatus(
            @PathVariable String id,
            @RequestParam ContactMessage.Status newStatus
    ) {
        ContactMessage updatedMessage = messageService.updateMessageStatus(id, newStatus);
        return ResponseEntity.ok(ApiResponse.success(updatedMessage, "Status updated"));
    }
}