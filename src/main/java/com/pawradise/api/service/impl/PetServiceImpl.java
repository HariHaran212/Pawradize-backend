package com.pawradise.api.service.impl;

import com.pawradise.api.exception.PetNotFoundException;
import com.pawradise.api.dto.PetDto;
import com.pawradise.api.models.Pet;
import com.pawradise.api.repository.PetRepository;
import com.pawradise.api.service.CloudinaryService;
import com.pawradise.api.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PetServiceImpl implements PetService {

    private final PetRepository petRepository;
    private final CloudinaryService cloudinaryService;

    public PetServiceImpl (@Autowired PetRepository petRepository, @Autowired CloudinaryService cloudinaryService) {
        this.petRepository = petRepository;
        this.cloudinaryService = cloudinaryService;
    }

    @Override
    public List<PetDto> getAllAvailablePets() {
        return petRepository.findAllByStatus(Pet.PetStatus.AVAILABLE).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public PetDto getAvailablePetById(String id) {
        Pet pet = petRepository.findByIdAndStatus(id, Pet.PetStatus.AVAILABLE)
                .orElseThrow(() -> new PetNotFoundException("Pet not found!"));
        return convertToDto(pet);
    }

    @Override
    public List<PetDto> getAllPets() {
        return petRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public PetDto getPetById(String id) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new PetNotFoundException("Pet not found!"));
        return convertToDto(pet);
    }

    @Override
    public PetDto createPet(PetDto petDto,  MultipartFile imageFile) {
        Pet pet = toEntity(petDto);

        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = cloudinaryService.uploadFile(imageFile);
            pet.setImageUrl(imageUrl);
        }

        return convertToDto(petRepository.save(pet));
    }

    @Override
    public PetDto updatePetById(String id, PetDto petDto, MultipartFile imageFile) {
        Pet _pet = petRepository.findById(id)
                .orElseThrow(() -> new PetNotFoundException("Pet not found!"));

        _pet.setName(petDto.getName());
        _pet.setSpecies(petDto.getSpecies());
        _pet.setGender(petDto.getGender());
        _pet.setBreed(petDto.getBreed());
        _pet.setDateOfBirth(LocalDate.parse(petDto.getDateOfBirth()));
        _pet.setShortDescription(petDto.getShortDescription());
        _pet.setLongDescription(petDto.getLongDescription());
        _pet.setPersonalityTraits(petDto.getPersonalityTraits());
        _pet.setPrice(petDto.getPrice());
        _pet.setStatus(petDto.getStatus());


        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = cloudinaryService.uploadFile(imageFile);
            _pet.setImageUrl(imageUrl);
        }

        return convertToDto(petRepository.save(_pet));
    }

    @Override
    public void deletePetById(String id) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new PetNotFoundException("Pet not found!"));

        petRepository.delete(pet);
    }

    private String calculateAge(LocalDate dateOfBirth) {
        Period age = Period.between(dateOfBirth, LocalDate.now());
        int years = age.getYears();
        int months = age.getMonths();

        if (years > 0) {
            return String.format("%d year(s), %d month(s)", years, months);
        } else {
            return String.format("%d month(s)", months);
        }
    }

    /**
     * A simple mapper method to convert Pet entity to a PetDto.
     * This can be placed in a separate mapper class for better organization.
     */
    private PetDto convertToDto(Pet pet) {
        PetDto dto = new PetDto();

        dto.setId(pet.getId());
        dto.setName(pet.getName());
        dto.setSpecies(pet.getSpecies());
        dto.setBreed(pet.getBreed());
        dto.setGender(pet.getGender());
        dto.setDateOfBirth(String.valueOf(pet.getDateOfBirth()));
        dto.setAge(calculateAge(pet.getDateOfBirth()));
        dto.setShortDescription(pet.getShortDescription());
        dto.setLongDescription(pet.getLongDescription());
        dto.setPersonalityTraits(pet.getPersonalityTraits());
        dto.setPrice(pet.getPrice());
        dto.setStatus(pet.getStatus());
        dto.setImageUrl(pet.getImageUrl());

        return dto;
    }

    /**
     * A simple mapper method to convert PetDto to a Pet entity.
     * This can be placed in a separate mapper class for better organization.
     */
    private Pet toEntity(PetDto dto) {
        Pet pet = new Pet();
        pet.setName(dto.getName());
        pet.setSpecies(dto.getSpecies());
        pet.setBreed(dto.getBreed());
        pet.setGender(dto.getGender());
        pet.setDateOfBirth(LocalDate.parse(dto.getDateOfBirth()));
        pet.setShortDescription(dto.getShortDescription());
        pet.setLongDescription(dto.getLongDescription());
        pet.setPersonalityTraits(dto.getPersonalityTraits());
        pet.setPrice(dto.getPrice());
        pet.setStatus(dto.getStatus());
        return pet;
    }

}