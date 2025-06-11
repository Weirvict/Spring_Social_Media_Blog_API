package com.example.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.Account;

/**
 * AccountRepository provides CRUD operations for the Account entity.
 * It extends JpaRepository to automatically inherit methods for
 * saving, deleting, and retrieving Account records from the database.
 *
 * Spring will automatically generate an implementation of this interface
 * at runtime and inject it wherever needed.
 * 
 * Used Lab: Entity to reference.
 */
public interface AccountRepository extends JpaRepository<Account, Integer> {
    /**
     * Finds an account by its unique username.
     * Useful for login and registration validation.
     *
     * @param username the username to search
     * @return an Optional containing the Account if found
     */
    Optional<Account> findByUsername(String username);
}
