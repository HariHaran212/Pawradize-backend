package com.pawradise.api.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "contact_messages")
public class ContactMessage {

    @Id
    private String id;

    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotBlank

    private String message;

    private Status status = Status.NEW; // Default status

    @CreatedDate
    private LocalDateTime receivedAt;

    public enum Status {
        NEW,
        READ,
        ARCHIVED
    }
}