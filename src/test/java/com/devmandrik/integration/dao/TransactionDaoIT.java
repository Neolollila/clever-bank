package com.devmandrik.integration.dao;

import com.devmandrik.dao.BankDao;
import com.devmandrik.dao.TransactionDao;
import com.devmandrik.dao.UserDao;
import com.devmandrik.entity.*;
import com.devmandrik.integration.IntegrationTestBase;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static com.devmandrik.util.ConstantAndMethodUtil.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

class TransactionDaoIT extends IntegrationTestBase {
    private static final UserDao userDao = UserDao.getInstance();
    private static final BankDao bankDao = BankDao.getInstance();
    private static final TransactionDao transactionDao = TransactionDao.getInstance();

    @Test
    void delete() {
        var user = userDao.save(createUser(ALEX));
        var fromBank = bankDao.save(createBank(ALPHA));
        var toBank = bankDao.save(createBank(MTB));
        var transaction = createTransaction(fromBank, toBank, user);
        var saved = transactionDao.save(transaction);

        Boolean delete = transactionDao.delete(saved.getId());

        assertThat(delete).isTrue();
    }

    @Test
    void save() {
        var user = userDao.save(createUser(ALEX));
        var fromBank = bankDao.save(createBank(ALPHA));
        var toBank = bankDao.save(createBank(MTB));
        var transaction = createTransaction(fromBank, toBank, user);

        Transaction saved = transactionDao.save(transaction);

        assertNotNull(saved.getId());
    }

    @Test
    void update() {
        var user = userDao.save(createUser(ALEX));
        var fromBank = bankDao.save(createBank(ALPHA));
        var toBank = bankDao.save(createBank(MTB));
        var transaction = createTransaction(fromBank, toBank, user);
        var saved = transactionDao.save(transaction);
        saved.setSum(1111F);

        transactionDao.update(saved);
        Optional<Transaction> transactionById = transactionDao.findById(saved.getId());

        assertThat(transactionById).isPresent();
        assertThat(transactionById.get().getSum()).isEqualTo(saved.getSum());
    }

    @Test
    void findById() {
        var user = userDao.save(createUser(ALEX));
        var fromBank = bankDao.save(createBank(ALPHA));
        var toBank = bankDao.save(createBank(MTB));
        var transaction = createTransaction(fromBank, toBank, user);
        var saved = transactionDao.save(transaction);

        Optional<Transaction> transactionById = transactionDao.findById(saved.getId());

        assertThat(transactionById).isPresent();
        assertThat(transactionById.get().getId()).isEqualTo(saved.getId());
    }

    @Test
    void findAll() {
        var user = userDao.save(createUser(ALEX));
        var fromBank = bankDao.save(createBank(ALPHA));
        var toBank1 = bankDao.save(createBank(MTB));
        var toBank2 = bankDao.save(createBank(BELARUSBANK));
        var transaction1 = createTransaction(fromBank, toBank1, user);
        var transaction2 = createTransaction(fromBank, toBank2, user);
        var saved1 = transactionDao.save(transaction1);
        var saved2 = transactionDao.save(transaction2);

        List<Transaction> transactions = transactionDao.findAll();

        List<Long> ids = transactions.stream().map(Transaction::getId).toList();
        assertThat(ids).contains(transaction1.getId(), transaction2.getId());
    }
}