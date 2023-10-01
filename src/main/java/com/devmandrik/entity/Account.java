package com.devmandrik.entity;

import com.devmandrik.entity.enums.CurrencyType;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class Account {
    private Long id;
    private Timestamp createdAt;
    private Float sum;
    private CurrencyType currency;
    private User user;
    private Bank bank;
}
