package com.pawradise.api.controller;

import com.pawradise.api.dto.VisitRequestDto;
import com.pawradise.api.models.VisitRequest;
import com.pawradise.api.response.ApiResponse;
import com.pawradise.api.service.VisitRequestService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class VisitRequestController {

    private final VisitRequestService visitRequestService;

    public VisitRequestController(VisitRequestService visitRequestService) {
        this.visitRequestService = visitRequestService;
    }

    // --- USER ENDPOINT ---
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/api/visit-requests")
    public ResponseEntity<ApiResponse<VisitRequest>> createVisitRequest(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody VisitRequestDto visitRequestDto
    ) {
        VisitRequest newRequest = visitRequestService.createRequest(userDetails.getUsername(), visitRequestDto);
        return new ResponseEntity<>(ApiResponse.success(newRequest, "Request submitted successfully"), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/api/visit-requests/my-requests")
    public ResponseEntity<ApiResponse<List<VisitRequest>>> getMyVisitRequests(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        List<VisitRequest> requests = visitRequestService.getRequestsForUser(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success(requests, "User's visit requests retrieved"));
    }

    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/api/visit-requests/{id}/cancel")
    public ResponseEntity<ApiResponse<VisitRequest>> cancelMyVisitRequest(
            @PathVariable String id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        VisitRequest cancelledRequest = visitRequestService.cancelRequest(id, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success(cancelledRequest, "Your request has been cancelled."));
    }

    // --- ADMIN ENDPOINTS ---
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADOPTION_COORDINATOR')")
    @GetMapping("/api/admin/visit-requests")
    public ResponseEntity<ApiResponse<List<VisitRequest>>> getAllVisitRequests(
            @RequestParam(required = false) VisitRequest.Status status
    ) {
        List<VisitRequest> requests = visitRequestService.getAllRequests(status);
        return ResponseEntity.ok(ApiResponse.success(requests, "Visit requests retrieved"));
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADOPTION_COORDINATOR')")
    @PatchMapping("/api/admin/visit-requests/{id}/status")
    public ResponseEntity<ApiResponse<VisitRequest>> updateVisitRequestStatus(
            @PathVariable String id,
            @RequestParam VisitRequest.Status newStatus
    ) {
        VisitRequest updatedRequest = visitRequestService.updateRequestStatus(id, newStatus);
        return ResponseEntity.ok(ApiResponse.success(updatedRequest, "Status updated successfully"));
    }
}