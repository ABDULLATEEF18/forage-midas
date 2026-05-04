package com.jpmc.midascore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRepository;

@Service
public class TransactionIngestor {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRecordRepository transactionRecordRepository;

    @Autowired
    private TransactionValidationService validationService;

    @Autowired 
private IncentiveService incentiveService;

@Transactional
public void processTransaction(Transaction transaction) {

    if (!validationService.isValid(transaction)) {
        System.out.println("Transaction discarded: " + transaction);
        return;
    }

    UserRecord sender = userRepository.findById(transaction.getSenderId());
    UserRecord recipient = userRepository.findById(transaction.getRecipientId());

    sender.setBalance(sender.getBalance() - transaction.getAmount());
    recipient.setBalance(recipient.getBalance() + transaction.getAmount());

    // Call incentive API
    float incentiveAmount = incentiveService.getIncentive(transaction);
    if (incentiveAmount > 0) {
        System.out.println("INCENTIVE: " + incentiveAmount + 
            " added to " + recipient.getName());
        recipient.setBalance(recipient.getBalance() + incentiveAmount);
    }

    userRepository.save(sender);
    userRepository.save(recipient);

    transactionRecordRepository.save(
        new TransactionRecord(sender, recipient, transaction.getAmount(), incentiveAmount)
    );

    // Track wilbur
    UserRecord wilbur = userRepository.findByName("wilbur");
    if (wilbur != null) {
        System.out.println(">>> WILBUR CURRENT BALANCE: " + wilbur.getBalance());
    }
}
}