package com.pawradise.api.repository;

import com.pawradise.api.models.VisitRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface VisitRequestRepository extends MongoRepository<VisitRequest, String> {
    List<VisitRequest> findByUserId(String userId);
    List<VisitRequest> findByStatus(VisitRequest.Status status);
    Optional<VisitRequest> findByUserIdAndPetIdAndStatusIn(String userId, String petId, List<VisitRequest.Status> statuses);
}