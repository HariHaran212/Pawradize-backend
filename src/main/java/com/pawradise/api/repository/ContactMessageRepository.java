package com.pawradise.api.repository;

import com.pawradise.api.models.ContactMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ContactMessageRepository extends MongoRepository<ContactMessage, String> {
    List<ContactMessage> findByStatus(ContactMessage.Status status);
}