package com.example.service;

import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import javax.persistence.EntityNotFoundException;

@Service
@Transactional
public class MessageService {
    MessageRepository messageRepository;
    AccountRepository accountRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository) {
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    public Message createMessage(Message newMessage) {
        Integer postedBy = newMessage.getPostedBy();
        String messageText = newMessage.getMessageText();
        Long timePostedEpoch = newMessage.getTimePostedEpoch();
        
        if (messageText == null || messageText.length() == 0) {
            throw new IllegalArgumentException("Message must not be empty.");
        }

        if (messageText.length() > 255) {
            throw new IllegalArgumentException("Message must not be more than 255 characters long.");
        }

        if (!accountRepository.existsById(postedBy)) {
            throw new EntityNotFoundException("User does not exist.");
        }

        Message savedMessage = messageRepository.save(new Message(postedBy,messageText,timePostedEpoch));
        return savedMessage;
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Message getMessageById(Integer messageId) {
        return messageRepository.findById(messageId).orElse(null);
    }

    public Integer removeMessageById(Integer messageId) {
        if (messageRepository.existsById(messageId)) {
            messageRepository.deleteById(messageId);
            return 1;
        }
        return null;
    }

    public Integer updateMessageById(Integer messageId, Message message) {
        String messageText = message.getMessageText();

        if (messageText == null || messageText.length() == 0) {
            throw new IllegalArgumentException("Message must not be empty.");
        }

        if (messageText.length() > 255) {
            throw new IllegalArgumentException("Message must not be more than 255 characters long.");
        }

        Message existingMessage = messageRepository.getById(messageId);
        if (existingMessage == null) {
            throw new EntityNotFoundException("Message does not exist.");
        }

        existingMessage.setMessageText(messageText);
        messageRepository.save(existingMessage);
        return 1;
    }

    public List<Message> getAllMessagesByAccountId(Integer accountId) {
        return messageRepository.findByPostedBy(accountId);
    }
}
