package com.pawradise.api.service;

import com.pawradise.api.dto.ContactMessageDto;
import com.pawradise.api.models.ContactMessage;
import java.util.List;

public interface ContactMessageService {
    ContactMessage saveMessage(ContactMessageDto messageDto);
    List<ContactMessage> findMessages(ContactMessage.Status status);
    ContactMessage updateMessageStatus(String id, ContactMessage.Status newStatus);
}