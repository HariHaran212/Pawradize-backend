package com.pawradise.api.dto;

import com.pawradise.api.models.Pet;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating a new Pet. This object carries the data from the client
 * to the server. The image file is handled separately in a multipart request.
 */
@Data
@NoArgsConstructor
public class PetDto {

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

    // The frontend should send the date as a "YYYY-MM-DD" string.
    // The backend will parse this into a LocalDate object.
    @NotBlank(message = "Date of birth cannot be empty")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Date of birth must be in YYYY-MM-DD format")
    private String dateOfBirth;

    private String age;

    @Size(max = 200, message = "Short description must be less than 200 characters")
    private String shortDescription;

    private String longDescription;

    private List<String> personalityTraits;

    @NotNull(message = "Price cannot be null")
    @PositiveOrZero(message = "Price must be a positive value or zero")
    private Double price;

    @NotNull(message = "Status cannot be empty")
    private Pet.PetStatus status;

    private String imageUrl;

}