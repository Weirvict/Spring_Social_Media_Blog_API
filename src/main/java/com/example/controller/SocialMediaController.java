package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your
 * controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use
 * the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations.
 * You should
 * refer to prior mini-project labs and lecture materials for guidance on how a
 * controller may be built.
 * 
 * NOTE: This controller handles:
 * - Account Endpoints (Requirements #1 & #2)
 * - Message Endpoints (Requirements #3–8)
 * 
 * Referenced Lab: Rest Control
 * 
 */
@RestController
@RequestMapping // Defaults to root path "/"
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    // ACOUNT SECTION
    /**
     * Registers a new user.
     * Endpoint: POST /register
     *
     * @param account the account JSON payload (no ID)
     * @return 200 OK with account if success,
     *         409 CONFLICT if username exists,
     *         400 BAD REQUEST if input is invalid
     */
    @PostMapping("/register")
    public ResponseEntity registerUser(@RequestBody Account account) {
        try {
            Account addedAccount = accountService.register(account);
            if (addedAccount != null) {
                return ResponseEntity.status(200).body(addedAccount);
            } else {
                return ResponseEntity.status(400).body(null);
            }
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(409).body(null);
        }

    }

    /**
     * Authenticates a user login.
     * Endpoint: POST /login
     *
     * @param account the login JSON (username + password)
     * @return 200 OK with account if valid, 401 UNAUTHORIZED otherwise
     */
    @PostMapping("/login")
    public ResponseEntity loginUser(@RequestBody Account account) {
        Account matchingAccount = accountService.login(account);
        if (matchingAccount != null) {
            return ResponseEntity.status(200).body(matchingAccount);
        } else {
            return ResponseEntity.status(401).body(null);
        }
    }

    // MESSAGE SECTION

    /**
     * Creates a new message.
     * Requirement #3
     * 
     * @param message JSON with messageText and postedBy
     * @return 200 OK if successful, 400 if validation fails
     */
    @PostMapping("/messages")
    public ResponseEntity createMessage(@RequestBody Message message) {
        Message saved = messageService.createMessage(message);
        if (saved != null) {
            return ResponseEntity.status(200).body(saved);
        }
        return ResponseEntity.status(400).body(null);
    }

    /**
     * Retrieves all messages.
     * Requirement #4
     * 
     * @return List of all messages, empty list if none
     */
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = messageService.getAllMessages();
        return ResponseEntity.status(200).body(messages);
    }

    /**
     * Retrieves a message by its ID.
     * Requirement #5
     * 
     * @param messageId ID of the message to retrieve
     * @return 200 OK with the message or null body if not found
     */
    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable int messageId) {
        Message message = messageService.getMessageById(messageId);
        return ResponseEntity.status(200).body(message);
    }

    /**
     * Deletes a message by its ID.
     * Requirement #6
     * 
     * @param messageId ID of the message to delete
     * @return 200 OK with 1 if deleted, or empty body if not found
     */
    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity deleteMessage(@PathVariable int messageId) {
        boolean deleted = messageService.deleteMessage(messageId);
        if (deleted) {
            return ResponseEntity.status(200).body(1);
        }
        return ResponseEntity.status(200).body(null);
    }

    /**
     * Updates message text by its ID.
     * Requirement #7
     * 
     * @param messageId       ID of the message to update
     * @param updatedTextOnly JSON containing only new messageText
     * @return 200 OK with 1 if successful, 400 if invalid
     */
    @PatchMapping("/messages/{messageId}")
    public ResponseEntity updateMessage(@PathVariable int messageId, @RequestBody Message updatedTextOnly) {
        String newText = updatedTextOnly.getMessageText();
        boolean updated = messageService.updateMessageText(messageId, newText);
        if (updated) {
            return ResponseEntity.status(200).body(1);
        }
        return ResponseEntity.status(400).body(null);
    }

    /**
     * Retrieves all messages posted by a specific user.
     * Requirement #8
     * 
     * @param accountId the ID of the account
     * @return List of messages by that user, empty if none or invalid user
     */
    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getMessagesByUser(@PathVariable int accountId) {
        List<Message> messages = messageService.getMessagesByUserId(accountId);
        return ResponseEntity.status(200).body(messages);
    }
}
