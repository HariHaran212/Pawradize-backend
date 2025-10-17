package com.pawradise.api.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "pets")
@Data
@NoArgsConstructor
public class Pet {
    @Id
    private String id;

    @NotBlank(message = "Pet name cannot be empty")
    @Size(max = 50, message = "Pet name must be less than 50 characters")
    private String name;

    @NotBlank(message = "Species cannot be empty")
    private String species;

    @NotBlank(message = "Breed cannot be empty")
    private String breed;

    @NotBlank(message = "Gender cannot be empty")
    private String gender;

    @PastOrPresent(message = "Date of birth must be in the past or present")
    private LocalDate dateOfBirth;

    @Size(max = 200, message = "Short description must be less than 200 characters")
    private String shortDescription;

    private String longDescription;

    private List<String> personalityTraits;

    @Positive(message = "Price must be a positive value")
    private double price;

    @NotBlank(message = "Status cannot be empty")
    private PetStatus status;

    @NotBlank(message = "Image URL cannot be empty")
    @URL(message = "Must be a valid URL")
    private String imageUrl;

    // A full constructor is useful for testing and service layers
    public Pet(String name, String species, String breed, String gender, LocalDate dateOfBirth, String shortDescription, String longDescription, List<String> personalityTraits, double price, PetStatus status, String imageUrl) {
        this.name = name;
        this.species = species;
        this.breed = breed;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
        this.personalityTraits = personalityTraits;
        this.price = price;
        this.status = status;
        this.imageUrl = imageUrl;
    }

    public enum PetStatus{
        AVAILABLE,
        ADOPTED,
        NOT_AVAILABLE
    }
}