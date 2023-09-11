package com.devmandrik.integration.dao;

import com.devmandrik.dao.AccountDao;
import com.devmandrik.dao.BankDao;
import com.devmandrik.dao.UserDao;
import com.devmandrik.entity.Account;
import com.devmandrik.integration.IntegrationTestBase;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static com.devmandrik.util.ConstantAndMethodUtil.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
class AccountDaoIT extends IntegrationTestBase {
    private static final AccountDao accountDao = AccountDao.getInstance();
    private static final UserDao userDao = UserDao.getInstance();
    private static final BankDao bankDao = BankDao.getInstance();


    @Test
    void delete() {
        var user = userDao.save(createUser(ALEX));
        var bank = bankDao.save(createBank(ALPHA));
        var account = createAccount(bank, user);


        Account savedAcc = accountDao.save(account);
        Boolean deleted = accountDao.delete(savedAcc.getId());

        assertThat(deleted).isTrue();
    }

    @Test
    void save() {
        var user = userDao.save(createUser(ALEX));
        var bank = bankDao.save(createBank(ALPHA));
        var account = createAccount(bank, user);

        Account savedAcc = accountDao.save(account);

        assertNotNull(savedAcc.getId());
    }

    @Test
    void update() {
        var user = userDao.save(createUser(ALEX));
        var bank = bankDao.save(createBank(ALPHA));
        var account = createAccount(bank, user);

        Account savedAcc = accountDao.save(account);
        savedAcc.setBank(bankDao.save(createBank(MTB)));
        accountDao.update(savedAcc);
        Optional<Account> accountById = accountDao.findById(savedAcc.getId());

        assertThat(accountById).isPresent();
        assertThat(accountById.get().getBank()).isEqualTo(savedAcc.getBank());
    }

    @Test
    void findById() {
        var user = userDao.save(createUser(ALEX));
        var bank = bankDao.save(createBank(ALPHA));
        var account = createAccount(bank, user);

        Account savedAcc = accountDao.save(account);
        Optional<Account> accountById = accountDao.findById(savedAcc.getId());

        assertThat(accountById).isPresent();
        assertThat(accountById.get()).isEqualTo(savedAcc);
    }

    @Test
    void findAll() {
        var user = userDao.save(createUser(ALEX));
        var bank1 = bankDao.save(createBank(ALPHA));
        var bank2 = bankDao.save(createBank(MTB));
        var account1 = createAccount(bank1, user);
        var account2 = createAccount(bank2, user);

        Account savedAcc1 = accountDao.save(account1);
        Account savedAcc2 = accountDao.save(account2);
        List<Account> accounts = accountDao.findAll();

        List<Long> ids = accounts.stream()
                .map(Account::getId)
                .toList();
        assertThat(accounts).hasSize(2);
        assertThat(ids).contains(savedAcc1.getId(), savedAcc2.getId());
    }
}