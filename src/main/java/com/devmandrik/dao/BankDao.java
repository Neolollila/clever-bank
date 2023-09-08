package com.devmandrik.dao;

import com.devmandrik.entity.Bank;
import com.devmandrik.entity.User;
import com.devmandrik.util.ConnectionManager;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor
public class BankDao implements Dao<Long, Bank>{

    private static final BankDao INSTANCE = new BankDao();

    private static final String SAVE_SQL = """
            INSERT INTO bank (name)
            VALUES (?);
            """;

    private static final String UPDATE_SQL = """
            UPDATE bank
            SET name = ?
            WHERE id = ?
            """;
    private static final String FIND_ALL_SQL = """
            SELECT id, name
            FROM bank
            """;
    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            WHERE id = ?
            """;

    private static final String DELETE_SQL = """
            DELETE FROM bank
            WHERE id = ?
            """;

    public static BankDao getInstance() {
        return INSTANCE;
    }

    @Override
    @SneakyThrows
    public boolean delete(Long id) {
        var connection = ConnectionManager.get();
        var preparedStatement = connection.prepareStatement(DELETE_SQL);
        preparedStatement.setLong(1, id);

        return preparedStatement.executeUpdate() > 0;
    }

    @Override
    @SneakyThrows
    public Bank save(Bank bank) {
        var connection = ConnectionManager.get();
        var preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, bank.getName());

        preparedStatement.executeUpdate();

        var generatedKeys = preparedStatement.getGeneratedKeys();
        if (generatedKeys.next()) {
            bank.setId(generatedKeys.getLong("id"));
        }
        return bank;
    }

    @Override
    @SneakyThrows
    public void update(Bank bank) {
        var connection = ConnectionManager.get();
        var preparedStatement = connection.prepareStatement(UPDATE_SQL);
        preparedStatement.setString(1, bank.getName());
        preparedStatement.setLong(2, bank.getId());

        preparedStatement.executeUpdate();
    }

    @Override
    @SneakyThrows
    public Optional<Bank> findById(Long id) {
        var connection = ConnectionManager.get();
        var preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL);
        preparedStatement.setLong(1, id);

        var resultSet = preparedStatement.executeQuery();
        Bank bank = null;
        if (resultSet.next()) {
            bank = buildBank(resultSet);
        }

        return Optional.ofNullable(bank);
    }

    @Override
    @SneakyThrows
    public List<Bank> findAll() {
        var connection = ConnectionManager.get();
        var preparedStatement = connection.prepareStatement(FIND_ALL_SQL);

        var resultSet = preparedStatement.executeQuery();
        List<Bank> banks = new ArrayList<>();
        while (resultSet.next()) {
            banks.add(buildBank(resultSet));
        }

        return banks;
    }

    private Bank buildBank(ResultSet resultSet) throws SQLException {
        return Bank.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .build();
    }
}
