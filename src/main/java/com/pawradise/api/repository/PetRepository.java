package com.pawradise.api.repository;

import com.pawradise.api.models.Pet;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PetRepository extends MongoRepository<Pet, String> {
    List<Pet> findAllByStatus(Pet.PetStatus status);
    Optional<Pet> findByIdAndStatus(String id, Pet.PetStatus status);
}
