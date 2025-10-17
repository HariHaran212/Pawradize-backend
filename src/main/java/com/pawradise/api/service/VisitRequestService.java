package com.pawradise.api.service;

import com.pawradise.api.dto.VisitRequestDto;
import com.pawradise.api.models.VisitRequest;
import java.util.List;

public interface VisitRequestService {

    /**
     * Creates a new visit request for a given user and pet.
     * @param username The username of the currently authenticated user.
     * @param visitRequestDto The data submitted from the application form.
     * @return The newly created VisitRequest object.
     */
    VisitRequest createRequest(String username, VisitRequestDto visitRequestDto);

    /**
     * Retrieves all visit requests, optionally filtered by status.
     * @param status The optional status to filter by (e.g., PENDING).
     * @return A list of visit requests.
     */
    List<VisitRequest> getAllRequests(VisitRequest.Status status);

    /**
     * Retrieves visit requests of the user.
     * @param username The field to filter the requests.
     * @return A list of visit requests that are filed by the user.
     */
    List<VisitRequest> getRequestsForUser(String username);

    /**
     * Updates the status of an existing visit request.
     * @param requestId The ID of the request to update.
     * @param newStatus The new status to set (e.g., APPROVED, DECLINED).
     * @return The updated VisitRequest object.
     */
    VisitRequest updateRequestStatus(String requestId, VisitRequest.Status newStatus);

    /**
     * Cancels an existing visit request.
     * @param requestId The ID of the request to cancel.
     * @param username The user requesting the cancellation of visit request.
     * @return The cancelled VisitRequest object.
     */
    VisitRequest cancelRequest(String requestId, String username);
}