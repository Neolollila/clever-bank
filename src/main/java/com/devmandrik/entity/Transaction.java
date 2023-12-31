package com.devmandrik.entity;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class Transaction {
    private Long id;
    private Timestamp createdAt;
    private TransactionType transactionType;
    private CurrencyType currency;
    private Float sum;
    private Bank fromBank;
    private Bank toBank;
    private User user;
}
