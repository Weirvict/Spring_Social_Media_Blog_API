package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.Message;

/**
 * MessageRepository provides CRUD operations for the Message entity.
 * It extends JpaRepository to automatically inherit methods for saving,
 * deleting, and retrieving Message records from the database.
 *
 * This repository also supports custom query methods for finding messages
 * by specific criteria, such as the user who posted them.
 * 
 * Used Lab: Entity to reference.
 */
public interface MessageRepository extends JpaRepository<Message, Integer> {
    /**
     * Finds all messages posted by a specific account.
     *
     * @param accountId the ID of the user who posted the messages
     * @return list of messages by that user
     */
    List<Message> findByPostedBy(int accountId);
}
