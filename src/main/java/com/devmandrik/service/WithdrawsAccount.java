package com.devmandrik.service;

import java.sql.Date;

public interface WithdrawsAccount {

    void withdraw(double amount, Date date);
}
