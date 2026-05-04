package com.jpmc.midascore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.UserRepository;

@Component
public class TransactionListener {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IncentiveService incentiveService;

    @KafkaListener(
        topics = "${general.kafka-topic}",
        groupId = "${spring.kafka.consumer.group-id}"
    )
    public void listen(Transaction transaction) {

        System.out.println("Received transaction: " + transaction);

        UserRecord sender = userRepository.findById(
            transaction.getSenderId()
        );

        UserRecord recipient = userRepository.findById(
            transaction.getRecipientId()
        );

        // Validate users exist
        if (sender == null || recipient == null) {
            return;
        }

        // Validate sender has enough balance
        if (sender.getBalance() < transaction.getAmount()) {
            return;
        }

        // Get incentive safely through service
        float incentiveAmount = incentiveService.getIncentive(transaction);

        // Deduct from sender
        sender.setBalance(
            sender.getBalance() - transaction.getAmount()
        );

        // Add to recipient + incentive
        recipient.setBalance(
            recipient.getBalance() +
            transaction.getAmount() +
            incentiveAmount
        );

        // Save updated balances
        userRepository.save(sender);
        userRepository.save(recipient);
    }
}