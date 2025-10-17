package com.pawradise.api.controller;

import com.pawradise.api.dto.PetDto;
import com.pawradise.api.response.ApiResponse;
import com.pawradise.api.service.PetService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/admin/pets")
public class AdminPetController {

    private final PetService petService;

    @Autowired
    public AdminPetController(PetService petService) {
        this.petService = petService;
    }

    /**
     * Create a new pet.
     * HTTP POST at /api/pets
     */
    @PostMapping
    public ResponseEntity<ApiResponse<PetDto>> createPet(
            @Valid @RequestPart("petData") PetDto petDto, // Use the DTO for pet metadata
            @RequestPart("imageFile") MultipartFile imageFile
    ) {
        PetDto newPet = petService.createPet(petDto, imageFile);
        return new ResponseEntity<>(ApiResponse.success(newPet, "Pet created successfully"), HttpStatus.CREATED);
    }

    /**
     * Get a list of all pets.
     * HTTP GET at /api/pets
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<PetDto>>> getAllPets() {
        List<PetDto> pets = petService.getAllPets();
        return ResponseEntity.ok(ApiResponse.success(pets, "All pets retrieved successfully"));
    }

    /**
     * Get a single pet by its ID.
     * HTTP GET at /api/pets/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PetDto>> getPetById(@PathVariable String id) {
        PetDto pet = petService.getPetById(id);
        return ResponseEntity.ok(ApiResponse.success(pet, "Pet found successfully"));
    }

    /**
     * Update an existing pet.
     * HTTP PUT at /api/pets/{id}
     */
    @PutMapping(value = "/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<ApiResponse<PetDto>> updatePet(@PathVariable String id,
                                                         @Valid @RequestPart("petData") PetDto petDto,
                                                         @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {
        PetDto updatedPet = petService.updatePetById(id, petDto, imageFile);
        return ResponseEntity.ok(ApiResponse.success(updatedPet, "Pet updated successfully"));
    }

    /**
     * Delete a pet by its ID.
     * HTTP DELETE at /api/pets/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePet(@PathVariable String id) {
        petService.deletePetById(id);
        return ResponseEntity.ok(ApiResponse.success("Pet deleted successfully"));
    }
}