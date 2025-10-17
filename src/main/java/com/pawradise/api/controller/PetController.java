package com.pawradise.api.controller;

import com.pawradise.api.dto.PetDto;
import com.pawradise.api.response.ApiResponse;
import com.pawradise.api.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/pets")
public class PetController {

    private final PetService petService;

    @Autowired
    public PetController(PetService petService) {
        this.petService = petService;
    }

    /**
     * Get a list of all pets.
     * HTTP GET at /api/pets
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<PetDto>>> getAllAvailablePets() {
        List<PetDto> pets = petService.getAllAvailablePets();
        return ResponseEntity.ok(ApiResponse.success(pets, "All pets retrieved successfully"));
    }

    /**
     * Get a single pet by its ID.
     * HTTP GET at /api/pets/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PetDto>> getAvailablePetById(@PathVariable String id) {
        PetDto pet = petService.getAvailablePetById(id);
        return ResponseEntity.ok(ApiResponse.success(pet, "Pet found successfully"));
    }
}
