package com.devmandrik.service;

import com.devmandrik.dao.AccountDao;
import com.devmandrik.dao.TransactionDao;
import com.devmandrik.entity.*;
import com.devmandrik.entity.enums.CurrencyType;
import com.devmandrik.entity.enums.TransactionType;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor
public class AccountService {
    private static final AccountService INSTANCE = new AccountService();
    private final AccountDao accountDao = AccountDao.getInstance();
    public static AccountService getInstance() {
        return INSTANCE;
    }

    public boolean delete(Long id) {
        return accountDao.delete(id);
    }

    public Account save(Account account) {
        return accountDao.save(account);
    }

    public void update(Account account) {
        accountDao.update(account);
    }

    public Optional<Account> findById(Long id) {
        return accountDao.findById(id);
    }

    public List<Account> findAll() {
        return accountDao.findAll();
    }

    public Boolean transfer(Float sum, Account fromAccount, Account toAccount) {
        if (fromAccount.getSum() >= sum) {
            fromAccount.setSum(fromAccount.getSum() - sum);
            toAccount.setSum(toAccount.getSum() + sum);
            accountDao.update(fromAccount);
            accountDao.update(toAccount);
            return true;
        } else return false;
    }

    public Boolean deposit(Float sum, Account account) {
        account.setSum(account.getSum() + sum);
        accountDao.update(account);
        return true;
    }

    public Boolean withdraw(Float sum, Account account) {
        if (account.getSum() >= sum) {
            account.setSum(account.getSum() - sum);
            accountDao.update(account);
            return true;
        } else return false;
    }

    private Transaction createTransaction(TransactionType transactionType, CurrencyType currencyType, Float sum, Bank fromBank, Bank toBank, User user) {
        return Transaction.builder()
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .transactionType(transactionType)
                .currency(currencyType)
                .sum(sum)
                .fromBank(fromBank)
                .toBank(toBank)
                .user(user)
                .build();
    }
}
