package com.devmandrik.util;

import com.devmandrik.entity.*;
import lombok.experimental.UtilityClass;

import java.sql.Timestamp;

@UtilityClass
public class ConstantAndMethodUtil {
    public static final String ALEX = "Alex";
    public static final String ALPHA = "Alpha";
    public static final String MTB = "MTB";
    public static final String BELARUSBANK = "BelarusBank";

    public User createUser(String name) {
        return User.builder()
                .name(name)
                .firstName(ALEX)
                .lastName(ALEX)
                .balance(1223.5F)
                .build();
    }

    public Bank createBank(String name) {
        return Bank.builder()
                .name(name)
                .build();
    }

    public Account createAccount(Bank bank, User user) {
        var timestamp = new Timestamp(System.currentTimeMillis());
        return Account.builder()
                .createdAt(timestamp)
                .sum(10F)
                .bank(bank)
                .user(user)
                .build();
    }

    public Transaction createTransaction(Bank fromBank, Bank toBank, User user) {
        var timestamp = new Timestamp(System.currentTimeMillis());
        return Transaction.builder()
                .createdAt(timestamp)
                .transactionType(TransactionType.WITHDRAW)
                .currency(CurrencyType.USD)
                .sum(13F)
                .fromBank(fromBank)
                .toBank(toBank)
                .user(user)
                .build();
    }
}
