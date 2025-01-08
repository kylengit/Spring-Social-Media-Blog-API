package com.example.service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;
import com.example.exception.UsernameAlreadyExistsException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AccountService {
    AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account getAccountByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    public Account register(Account newAccount) {
        String username = newAccount.getUsername();
        String password = newAccount.getPassword();

        if (username == null || username.length() == 0) {
            throw new IllegalArgumentException("Username must not be empty.");
        }

        if (password == null || password.length() < 4) {
            throw new IllegalArgumentException("Password must be at least 4 characters long.");
        }

        Account existingAccount = accountRepository.findByUsername(username);
        if (existingAccount != null) {
            throw new UsernameAlreadyExistsException("Account with this username already exists.");
        }

        Account savedAccount = accountRepository.save(new Account(username,password));
        return savedAccount;
    }
    
    public Account login(Account newAccount) {
        String username = newAccount.getUsername();
        String password = newAccount.getPassword();

        Account account = accountRepository.findByUsernameAndPassword(username,password);
        if (account == null) {
            throw new IllegalArgumentException("Username or password is incorrect.");
        }

        return account;
    }
}
