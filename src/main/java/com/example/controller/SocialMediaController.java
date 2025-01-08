package com.example.controller;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;
import com.example.exception.UsernameAlreadyExistsException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
 @Controller
public class SocialMediaController {
    private AccountService accountService;
    private MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    @PostMapping("register")
    public @ResponseBody ResponseEntity<Account> register(@RequestBody Account registerRequest) {
        try {
            Account savedAccount = accountService.register(registerRequest);
            return ResponseEntity.status(200).body(savedAccount);
        } catch (UsernameAlreadyExistsException ex) {
            return ResponseEntity.status(409).body(null);
        } catch (Exception ex) {
            return ResponseEntity.status(400).body(null);
        } 
    }

    @PostMapping("login")
    public @ResponseBody ResponseEntity<Account> login(@RequestBody Account loginRequest) {
        try {
            return ResponseEntity.status(200).body(accountService.login(loginRequest));
        } catch (Exception ex) {
            return ResponseEntity.status(401).body(null);
        } 
    }

    @PostMapping("messages")
    public @ResponseBody ResponseEntity<Message> createMessage(@RequestBody Message messageRequest) {
        try {
            return ResponseEntity.status(200).body(messageService.createMessage(messageRequest));
        } catch (Exception ex) {
            return ResponseEntity.status(400).body(null);
        } 
    }

    @GetMapping("messages")
    public @ResponseBody ResponseEntity<List<Message>> getAllMessages() {
        return ResponseEntity.status(200).body(messageService.getAllMessages());
    }

    @GetMapping("messages/{messageId}")
    public @ResponseBody ResponseEntity<Message> getMessageById(@PathVariable Integer messageId) {
        return ResponseEntity.status(200).body(messageService.getMessageById(messageId));
    }
    
    @DeleteMapping("messages/{messageId}")
    public @ResponseBody ResponseEntity<Integer> removeMessageById(@PathVariable Integer messageId) {
        return ResponseEntity.status(200).body(messageService.removeMessageById(messageId));
    }
    
    @PatchMapping("messages/{messageId}")
    public @ResponseBody ResponseEntity<Integer> updateMessageById(@PathVariable Integer messageId, @RequestBody Message message) {
        try {
            return ResponseEntity.status(200).body(messageService.updateMessageById(messageId,message));
        } catch (Exception ex) {
            return ResponseEntity.status(400).body(null);
        } 
    }

    @GetMapping("accounts/{accountId}/messages")
    public @ResponseBody ResponseEntity<List<Message>> getAllMessagesByAccountId(@PathVariable Integer accountId) {
        return ResponseEntity.status(200).body(messageService.getAllMessagesByAccountId(accountId));
    }
}
