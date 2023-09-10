package com.devmandrik.dao;

import com.devmandrik.entity.Bank;
import com.devmandrik.entity.CurrencyType;
import com.devmandrik.entity.Transaction;
import com.devmandrik.entity.TransactionType;
import com.devmandrik.util.ConnectionManager;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor
public class TransactionDao implements Dao<Long, Transaction>{

    private static final TransactionDao INSTANCE = new TransactionDao();
    private final UserDao userDao = UserDao.getInstance();
    private final BankDao bankDao = BankDao.getInstance();

    private static final String SAVE_SQL = """
            INSERT INTO transaction (created_at, transaction_type, currency, sum, frombank_id, tobank_id, user_id)
            VALUES (?, ?, ?, ?, ?, ?, ?);
            """;

    private static final String UPDATE_SQL = """
            UPDATE transaction
            SET transaction_type = ?, currency = ?, sum = ?, frombank_id = ?, tobank_id = ?, user_id = ?
            WHERE id = ?
            """;
    private static final String FIND_ALL_SQL = """
            SELECT id, created_at, transaction_type, currency, sum, frombank_id, tobank_id, user_id
            FROM transaction
            """;
    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            WHERE id = ?
            """;

    private static final String DELETE_SQL = """
            DELETE FROM transaction
            WHERE id = ?
            """;

    public static TransactionDao getInstance() {
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
    public Transaction save(Transaction transaction) {
        var connection = ConnectionManager.get();
        var preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS);
        prepareStatementForTransaction(transaction, preparedStatement);

        var generatedKeys = preparedStatement.getGeneratedKeys();
        if (generatedKeys.next()) {
            transaction.setId(generatedKeys.getLong("id"));
        }
        return transaction;
    }

    @Override
    @SneakyThrows
    public void update(Transaction transaction) {
        var connection = ConnectionManager.get();
        var preparedStatement = connection.prepareStatement(UPDATE_SQL);
        prepareStatementForTransaction(transaction, preparedStatement);
    }

    @Override
    @SneakyThrows
    public Optional<Transaction> findById(Long id) {
        var connection = ConnectionManager.get();
        var preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL);
        preparedStatement.setLong(1, id);

        var resultSet = preparedStatement.executeQuery();
        Transaction transaction = null;
        if (resultSet.next()) {
            transaction = buildTransaction(resultSet);
        }

        return Optional.ofNullable(transaction);
    }

    @Override
    @SneakyThrows
    public List<Transaction> findAll() {
        var connection = ConnectionManager.get();
        var preparedStatement = connection.prepareStatement(FIND_ALL_SQL);

        var resultSet = preparedStatement.executeQuery();
        List<Transaction> transactions = new ArrayList<>();
        while (resultSet.next()) {
            transactions.add(buildTransaction(resultSet));
        }

        return transactions;
    }

    private void prepareStatementForTransaction(Transaction transaction, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setTimestamp(1, transaction.getCreatedAt());
        preparedStatement.setString(2, String.valueOf(transaction.getTransactionType()));
        preparedStatement.setString(3, String.valueOf(transaction.getCurrency()));
        preparedStatement.setFloat(4, transaction.getSum());
        preparedStatement.setLong(5, transaction.getFromBank().getId());
        preparedStatement.setLong(6, transaction.getToBank().getId());
        preparedStatement.setLong(7, transaction.getUser().getId());

        preparedStatement.executeUpdate();
    }

    private Transaction buildTransaction(ResultSet resultSet) throws SQLException {
        return Transaction.builder()
                .id(resultSet.getLong("id"))
                .createdAt(resultSet.getTimestamp("created_at"))
                .transactionType(TransactionType.valueOf(resultSet.getString("transaction_type")))
                .currency(CurrencyType.valueOf(resultSet.getString("currency")))
                .sum(resultSet.getFloat("sum"))
                .fromBank(bankDao.findById(resultSet.getLong("frombank_id")).orElse(null))
                .toBank(bankDao.findById(resultSet.getLong("tobank_id")).orElse(null))
                .user(userDao.findById(resultSet.getLong("user_id")).orElse(null))
                .build();
    }
}
