package com.pawradise.api.models;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "visit_requests")
public class VisitRequest {

    @Id
    private String id;

    private String petId;
    private String petName; // Denormalized for easy display
    private String petSpecies;

    private String userId;
    private String userName; // Denormalized
    private String userEmail; // Denormalized
    private String userPhone; // Denormalized

    private String address;
    private String reason;

    private Status status;

    @CreatedDate
    private LocalDateTime requestedDate;

    public enum Status {
        PENDING,
        APPROVED,
        DECLINED,
        COMPLETED // For after the visit has happened
    }
}