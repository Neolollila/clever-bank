package com.devmandrik.service;

import com.devmandrik.dao.AccountDao;
import com.devmandrik.dao.BankDao;
import com.devmandrik.entity.Account;
import com.devmandrik.entity.Bank;

import java.util.List;
import java.util.Optional;

public class AccountService {
    private final AccountDao accountDao = AccountDao.getInstance();

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
}
