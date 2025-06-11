package com.example.service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * AccountService handles business logic related to user accounts,
 * including registration and login functionality.
 * 
 * Referenced Lab JPACRUD
 * NOTE: This service handles functionality for:
 * - Requirement #1: User registration (POST /register)
 * - Requirement #2: User login (POST /login)
 * 
 */
@Service // Marks this class as a Spring service for dependency injection
public class AccountService {
    private final AccountRepository accountRepository;

    /**
     * Constructor-based dependency injection of the AccountRepository.
     * Spring will automatically inject the bean at runtime.
     *
     * @param accountRepository the repository used to access account data
     */
    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Registers a new account if valid.
     * Requirements:
     * - Username is not blank
     * - Password is at least 4 characters long
     * - Username must not already exist
     *
     * @param account the account to register
     * @return the saved account entity
     */
    public Account register(Account account) {
        if (account.getUsername() == null || account.getUsername().trim().isEmpty()) {
            return null;
        }
        if (account.getPassword() == null || account.getPassword().length() < 4) {
            return null;
        }
        if (accountRepository.findByUsername(account.getUsername()).isPresent()) {
            throw new DataIntegrityViolationException("Username already exists.");
        }
        return accountRepository.save(account);
    }

    /**
     * Verifies login credentials.
     * Matches provided username and password with database.
     *
     * @param account the login attempt
     * @return the account if valid, or null
     */
    public Account login(Account account) {
        Optional<Account> existing = accountRepository.findByUsername(account.getUsername());
        if (existing.isPresent() && existing.get().getPassword().equals(account.getPassword())) {
            return existing.get();
        }
        return null;
    }

    /**
     * Finds an account by ID.
     * 
     * @param id the accountId to search for
     * @return the Account if found, or null
     */
    public Account getAccountById(int id) {
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if (optionalAccount.isPresent()) {
            return optionalAccount.get();
        } else {
            return null;
        }
    }

    /**
     * Deletes an account by ID.
     * 
     * @param id the accountId to delete
     */
    public void deleteAccount(int id) {
        accountRepository.deleteById(id);
    }

    /**
     * Updates an account's username and/or password by ID.
     * 
     * @param id          account ID to update
     * @param replacement Account object with new data
     * @return the updated account, or null if not found
     */
    public Account updateAccount(int id, Account replacement) {
        Optional<Account> optional = accountRepository.findById(id);
        if (optional.isPresent()) {
            Account existing = optional.get();
            existing.setUsername(replacement.getUsername());
            existing.setPassword(replacement.getPassword());
            return accountRepository.save(existing);
        }
        return null;
    }

}
