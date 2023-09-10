package com.devmandrik.service;

import com.devmandrik.dao.BankDao;
import com.devmandrik.dao.TransactionDao;
import com.devmandrik.entity.Bank;
import com.devmandrik.entity.Transaction;

import java.util.List;
import java.util.Optional;

public class BankService {
    private final BankDao bankDao = BankDao.getInstance();

    public boolean delete(Long id) {
        return bankDao.delete(id);
    }

    public Bank save(Bank bank) {
        return bankDao.save(bank);
    }

    public void update(Bank bank) {
        bankDao.update(bank);
    }

    public Optional<Bank> findById(Long id) {
        return bankDao.findById(id);
    }

    public List<Bank> findAll() {
        return bankDao.findAll();
    }
}
