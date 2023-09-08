package com.devmandrik.dao;

import com.devmandrik.entity.Account;
import com.devmandrik.entity.Bank;
import com.devmandrik.entity.User;
import com.devmandrik.util.ConnectionManager;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor
public class AccountDao implements Dao<Long, Account>{

    private static final AccountDao INSTANCE = new AccountDao();
    private final UserDao userDao = UserDao.getInstance();
    private final BankDao bankDao = BankDao.getInstance();

    private static final String SAVE_SQL = """
            INSERT INTO account (created_at, sum, user_id, bank_id)
            VALUES (?, ?, ?, ?);
            """;

    private static final String UPDATE_SQL = """
            UPDATE account
            SET created_at = ?, sum = ?, user_id = ?, bank_id = ?
            WHERE id = ?
            """;
    private static final String FIND_ALL_SQL = """
            SELECT id, created_at, sum, user_id, bank_id
            FROM account
            """;
    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            WHERE id = ?
            """;

    private static final String DELETE_SQL = """
            DELETE FROM account
            WHERE id = ?
            """;

    public static AccountDao getInstance() {
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
    public Account save(Account account) {
        var connection = ConnectionManager.get();
        var preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setTimestamp(1, account.getCreatedAt());
        preparedStatement.setFloat(2, account.getSum());
        preparedStatement.setLong(3, account.getUser().getId());
        preparedStatement.setLong(4, account.getBank().getId());

        preparedStatement.executeUpdate();

        var generatedKeys = preparedStatement.getGeneratedKeys();
        if (generatedKeys.next()) {
            account.setId(generatedKeys.getLong("id"));
        }
        return account;
    }

    @Override
    @SneakyThrows
    public void update(Account account) {
        var connection = ConnectionManager.get();
        var preparedStatement = connection.prepareStatement(UPDATE_SQL);
        preparedStatement.setTimestamp(1, account.getCreatedAt());
        preparedStatement.setFloat(2, account.getSum());
        preparedStatement.setLong(3, account.getUser().getId());
        preparedStatement.setLong(4, account.getBank().getId());
        preparedStatement.setLong(5, account.getId());

        preparedStatement.executeUpdate();
    }

    @Override
    @SneakyThrows
    public Optional<Account> findById(Long id) {
        var connection = ConnectionManager.get();
        var preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL);
        preparedStatement.setLong(1, id);

        var resultSet = preparedStatement.executeQuery();
        Account account = null;
        if (resultSet.next()) {
            account = buildAccount(resultSet);
        }

        return Optional.ofNullable(account);
    }

    @Override
    @SneakyThrows
    public List<Account> findAll() {
        var connection = ConnectionManager.get();
        var preparedStatement = connection.prepareStatement(FIND_ALL_SQL);

        var resultSet = preparedStatement.executeQuery();
        List<Account> accounts = new ArrayList<>();
        while (resultSet.next()) {
            accounts.add(buildAccount(resultSet));
        }

        return accounts;
    }

    private Account buildAccount(ResultSet resultSet) throws SQLException {
        return Account.builder()
                .id(resultSet.getLong("id"))
                .createdAt(resultSet.getTimestamp("created_at"))
                .sum(resultSet.getFloat("sum"))
                .user(userDao.findById(resultSet.getLong("user_id")).orElse(null))
                .bank(bankDao.findById(resultSet.getLong("bank_id")).orElse(null))
                .build();
    }
}
