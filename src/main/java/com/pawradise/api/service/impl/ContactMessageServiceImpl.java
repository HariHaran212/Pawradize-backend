package com.pawradise.api.service.impl;

import com.pawradise.api.dto.ContactMessageDto;
import com.pawradise.api.models.ContactMessage;
import com.pawradise.api.repository.ContactMessageRepository;
import com.pawradise.api.service.ContactMessageService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ContactMessageServiceImpl implements ContactMessageService {

    private final ContactMessageRepository repository;

    public ContactMessageServiceImpl(ContactMessageRepository repository) {
        this.repository = repository;
    }

    @Override
    public ContactMessage saveMessage(ContactMessageDto messageDto) {
        ContactMessage message = new ContactMessage();
        message.setName(messageDto.getName());
        message.setEmail(messageDto.getEmail());
        message.setMessage(messageDto.getMessage());
        return repository.save(message);
    }

    @Override
    public List<ContactMessage> findMessages(ContactMessage.Status status) {
        if (status != null) {
            return repository.findByStatus(status);
        }
        return repository.findAll();
    }

    @Override
    public ContactMessage updateMessageStatus(String id, ContactMessage.Status newStatus) {
        ContactMessage message = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found"));
        message.setStatus(newStatus);
        return repository.save(message);
    }
}