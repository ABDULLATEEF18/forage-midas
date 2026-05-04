package com.jpmc.midascore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.UserRepository;

//import java.util.Optional;

@Service
public class TransactionValidationService {

    @Autowired
    private UserRepository userRepository;

    public boolean isValid(Transaction transaction) {

        // Rule 1 — sender must exist
        UserRecord sender = userRepository.findById(transaction.getSenderId());
        if (sender == null) {
            System.out.println("Invalid: sender not found - " + 
                transaction.getSenderId());
            return false;
        }

        // Rule 2 — recipient must exist
        UserRecord recipient = userRepository.findById(transaction.getRecipientId());
        if (recipient == null) {
            System.out.println("Invalid: recipient not found - " + 
                transaction.getRecipientId());
            return false;
        }

        // Rule 3 — sender must have sufficient balance
        if (sender.getBalance() < transaction.getAmount()) {
            System.out.println("Invalid: insufficient funds. " +
                "Balance: " + sender.getBalance() + 
                ", Amount: " + transaction.getAmount());
            return false;
        }

        return true;
    }
}