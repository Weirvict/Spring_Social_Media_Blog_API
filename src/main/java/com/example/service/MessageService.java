package com.example.service;

import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * =============================================================================
 * NOTE: This service handles:
 * - Requirement #3: Create message
 * - Requirement #4: Get all messages
 * - Requirement #5: Get message by ID
 * - Requirement #6: Delete message by ID
 * - Requirement #7: Update message text
 * - Requirement #8: Get messages by user ID
 * =============================================================================
 */
@Service
public class MessageService {
    MessageRepository messageRepository;
    AccountRepository accountRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository) {
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    /**
     * Creates and saves a message if valid.
     * 
     * @param message the message to create
     * @return the saved message or null if invalid
     */
    public Message createMessage(Message message) {
        if (message.getMessageText() == null || message.getMessageText().trim().isEmpty()) {
            return null;
        }
        if (message.getMessageText().length() > 255) {
            return null;
        }
        if (!accountRepository.existsById(message.getPostedBy())) {
            return null;
        }
        return messageRepository.save(message);
    }

    /**
     * Gets all messages.
     * 
     * @return list of messages (empty if none)
     */
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    /**
     * Gets a message by ID.
     * 
     * @param id messageId
     * @return the message or null if not found
     */
    public Message getMessageById(int id) {
        Optional<Message> optionalMessage = messageRepository.findById(id);
        if (optionalMessage.isPresent()) {
            return optionalMessage.get();
        } else {
            return null;
        }
    }

    /**
     * Deletes a message by ID.
     * 
     * @param id messageId
     * @return true if deleted, false if not found
     */
    public boolean deleteMessage(int id) {
        if (messageRepository.existsById(id)) {
            messageRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Updates the message text for a given message ID.
     * 
     * @param id      messageId
     * @param newText the new message text
     * @return true if updated, false if invalid or not found
     */
    public boolean updateMessageText(int id, String newText) {
        if (newText == null || newText.trim().isEmpty() || newText.length() > 255) {
            return false;
        }
        Optional<Message> optionalMessage = messageRepository.findById(id);
        if (optionalMessage.isPresent()) {
            Message message = optionalMessage.get();
            message.setMessageText(newText);
            messageRepository.save(message);
            return true;
        }
        return false;
    }

    /**
     * Gets all messages by a specific accountId.
     * 
     * @param accountId the user ID
     * @return list of messages (empty if none or account doesn't exist)
     */
    public List<Message> getMessagesByUserId(int accountId) {
        if (!accountRepository.existsById(accountId)) {
            return List.of(); // empty list
        }
        return messageRepository.findByPostedBy(accountId);
    }

}
