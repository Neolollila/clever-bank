package com.devmandrik.integration.dao;

import com.devmandrik.dao.BankDao;
import com.devmandrik.entity.Bank;
import com.devmandrik.integration.IntegrationTestBase;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

public class BankDaoIT extends IntegrationTestBase {
    private static final BankDao bankDao = BankDao.getInstance();

    @Test
    void delete() {
        var bank = createBank("Alpha");
        var savedUser = bankDao.save(bank);

        Boolean delete = bankDao.delete(savedUser.getId());

        assertThat(delete).isTrue();
    }

    @Test
    void save() {
        var bank = createBank("Alpha");

        Bank savedBank = bankDao.save(bank);

        assertThat(savedBank.getName()).isEqualTo(bank.getName());
    }

    @Test
    void update() {
        var bank = createBank("Alpha");
        Bank savedBank = bankDao.save(bank);
        savedBank.setName("MTB");

        bankDao.update(savedBank);
        Optional<Bank> updatedBank = bankDao.findById(savedBank.getId());

        assertThat(updatedBank).isPresent();
        assertThat(updatedBank.get().getName()).isEqualTo(savedBank.getName());
    }

    @Test
    void findById() {
        var bank = createBank("Alpha");
        Bank savedBank = bankDao.save(bank);

        Optional<Bank> updatedBank = bankDao.findById(savedBank.getId());

        assertThat(updatedBank).isPresent();
        assertThat(updatedBank.get().getId()).isEqualTo(savedBank.getId());
    }

    @Test
    void findAll() {
        Bank savedBank1 = bankDao.save(createBank("MTB"));
        Bank savedBank2 = bankDao.save(createBank("Alpha"));
        Bank savedBank3 = bankDao.save(createBank("BelarusBank"));

        List<Bank> banks = bankDao.findAll();

        List<Long> ids = banks.stream()
                        .map(Bank::getId)
                        .toList();
        assertThat(banks).hasSize(3);
        assertThat(ids).contains(savedBank1.getId(), savedBank2.getId(), savedBank3.getId());
    }

    private Bank createBank(String name) {
        return Bank.builder()
                .name(name)
                .build();
    }
}
