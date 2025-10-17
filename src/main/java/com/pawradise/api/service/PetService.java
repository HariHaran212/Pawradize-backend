package com.pawradise.api.service;

import com.pawradise.api.dto.PetDto;
import com.pawradise.api.models.Pet;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PetService {
    List<PetDto> getAllAvailablePets();
    PetDto getAvailablePetById(String id);
    List<PetDto> getAllPets();
    PetDto getPetById(String id);
    PetDto createPet(PetDto petDto, MultipartFile imageFile);
    PetDto updatePetById(String id, PetDto petDto, MultipartFile imageFile);
    void deletePetById(String id);
}
