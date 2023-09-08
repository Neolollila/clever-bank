package com.devmandrik.service;

import com.devmandrik.dao.TransactionDao;
import com.devmandrik.dao.UserDao;
import com.devmandrik.entity.Transaction;
import com.devmandrik.entity.User;

import java.util.List;
import java.util.Optional;

public class TransactionService {
    private final TransactionDao transactionDao = TransactionDao.getInstance();

    public boolean delete(Long id) {
        return transactionDao.delete(id);
    }

    public Transaction save(Transaction transaction) {
        return transactionDao.save(transaction);
    }

    public void update(Transaction transaction) {
        transactionDao.update(transaction);
    }

    public Optional<Transaction> findById(Long id) {
        return transactionDao.findById(id);
    }

    public List<Transaction> findAll() {
        return transactionDao.findAll();
    }
}
