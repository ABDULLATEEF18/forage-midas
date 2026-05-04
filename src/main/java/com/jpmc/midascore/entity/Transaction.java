package com.jpmc.midascore.entity;

public class Transaction {

    private UserRecord sender;
    private UserRecord receiver;
    private float amount;

    public Transaction() {}

    public UserRecord getSender() {
        return sender;
    }

    public void setSender(UserRecord sender) {
        this.sender = sender;
    }

    public UserRecord getReceiver() {
        return receiver;
    }

    public void setReceiver(UserRecord receiver) {
        this.receiver = receiver;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Transaction {senderId=" + sender.getId() +
               ", recipientId=" + receiver.getId() +
               ", amount=" + amount + "}";
    }
}