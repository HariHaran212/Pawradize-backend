package com.pawradise.api.service.impl;

import com.pawradise.api.dto.VisitRequestDto;
import com.pawradise.api.exception.PetNotFoundException;
import com.pawradise.api.models.Pet;
import com.pawradise.api.models.User;
import com.pawradise.api.models.VisitRequest;
import com.pawradise.api.repository.PetRepository;
import com.pawradise.api.repository.UserRepository;
import com.pawradise.api.repository.VisitRequestRepository;
import com.pawradise.api.service.VisitRequestService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.pawradise.api.exception.IllegalStateException;

import java.util.List;
import java.util.Optional;

@Service
public class VisitRequestServiceImpl implements VisitRequestService {

    private final VisitRequestRepository visitRequestRepository;
    private final UserRepository userRepository;
    private final PetRepository petRepository;

    public VisitRequestServiceImpl(VisitRequestRepository visitRequestRepository,
                                   UserRepository userRepository,
                                   PetRepository petRepository) {
        this.visitRequestRepository = visitRequestRepository;
        this.userRepository = userRepository;
        this.petRepository = petRepository;
    }

    @Override
    public VisitRequest createRequest(String username, VisitRequestDto dto) {
        // Find the user making the request
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // Find the pet being requested
        Pet pet = petRepository.findById(dto.getPetId())
                .orElseThrow(() -> new PetNotFoundException("Pet not found with id: " + dto.getPetId()));

        List<VisitRequest.Status> activeStatuses = List.of(VisitRequest.Status.PENDING, VisitRequest.Status.APPROVED);

        Optional<VisitRequest> existingRequest = visitRequestRepository.findByUserIdAndPetIdAndStatusIn(user.getId(), pet.getId(), activeStatuses);

        if (existingRequest.isPresent()) {
            // If a request is found, throw an exception to stop the process
            throw new IllegalStateException("You have already submitted an application for this pet. Please wait for a response.");
        }

        // Create and populate the new request object
        VisitRequest request = getVisitRequest(dto, pet, user);

        return visitRequestRepository.save(request);
    }

    private static VisitRequest getVisitRequest(VisitRequestDto dto, Pet pet, User user) {
        VisitRequest request = new VisitRequest();
        request.setPetId(pet.getId());
        request.setPetName(pet.getName());
        request.setPetSpecies(pet.getSpecies());
        request.setUserId(user.getId());
        request.setUserName(dto.getFullName());
        request.setUserEmail(dto.getEmail());
        request.setUserPhone(dto.getPhone());
        request.setAddress(dto.getAddress());
        request.setReason(dto.getReason());
        request.setStatus(VisitRequest.Status.PENDING);
        return request;
    }

    @Override
    public List<VisitRequest> getAllRequests(VisitRequest.Status status) {
        if (status != null) {
            // If a status filter is provided, use the specific repository method
            return visitRequestRepository.findByStatus(status);
        } else {
            // Otherwise, return all requests
            return visitRequestRepository.findAll();
        }
    }

    @Override
    public List<VisitRequest> getRequestsForUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return visitRequestRepository.findByUserId(user.getId());
    }

    @Override
    public VisitRequest updateRequestStatus(String requestId, VisitRequest.Status newStatus) {
        // Find the existing request or throw an exception
        VisitRequest request = visitRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Visit request not found with id: " + requestId));

        // Update the status and save
        request.setStatus(newStatus);
        return visitRequestRepository.save(request);
    }

    @Override
    public VisitRequest cancelRequest(String requestId, String username) {
        // Find the user making the request
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Find the request to be cancelled
        VisitRequest request = visitRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Visit request not found with id: " + requestId));

        // 1. SECURITY CHECK: Ensure the request belongs to the current user
        if (!request.getUserId().equals(user.getId())) {
            throw new AccessDeniedException("You are not authorized to cancel this request.");
        }

        // 2. BUSINESS RULE CHECK: Only allow cancellation if the request is Pending or Approved
        if (request.getStatus() != VisitRequest.Status.PENDING && request.getStatus() != VisitRequest.Status.APPROVED) {
            throw new IllegalStateException("This request can no longer be cancelled.");
        }

        // Update the status and save
        request.setStatus(VisitRequest.Status.DECLINED); // Or you can create a CANCELLED status
        return visitRequestRepository.save(request);
    }
}