package com.devmandrik.dao;

import com.devmandrik.entity.User;
import com.devmandrik.util.ConnectionManager;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDao implements Dao<Long, User> {

    private final Connection connection;

    public UserDao() {
        connection = ConnectionManager.get();
    }

    public UserDao(Connection conn) {
        connection = conn;
    }

    private static final UserDao INSTANCE = new UserDao();

    private static final String SAVE_SQL = """
            INSERT INTO users (name, first_name, last_name, balance)
            VALUES (?, ?, ?, ?);
            """;

    private static final String UPDATE_SQL = """
            UPDATE users
            SET name = ?,
                first_name = ?,
                last_name = ?,
                balance = ?
            WHERE id = ?
            """;
    private static final String FIND_ALL_SQL = """
            SELECT id, name, first_name, last_name, balance
            FROM users
            """;
    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            WHERE id = ?
            """;

    private static final String DELETE_SQL = """
            DELETE FROM users
            WHERE id = ?
            """;

    public static UserDao getInstance() {
        return INSTANCE;
    }

    @Override
    @SneakyThrows
    public boolean delete(Long id) {
        var preparedStatement = connection.prepareStatement(DELETE_SQL);
        preparedStatement.setLong(1, id);

        return preparedStatement.executeUpdate() > 0;
    }

    @Override
    @SneakyThrows
    public User save(User user) {
        var preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, user.getName());
        preparedStatement.setString(2, user.getFirstName());
        preparedStatement.setString(3, user.getLastName());
        preparedStatement.setFloat(4, user.getBalance());

        preparedStatement.executeUpdate();

        var generatedKeys = preparedStatement.getGeneratedKeys();
        if (generatedKeys.next()) {
            user.setId(generatedKeys.getLong("id"));
        }
        return user;
    }

    @Override
    @SneakyThrows
    public void update(User user) {
        var preparedStatement = connection.prepareStatement(UPDATE_SQL);
        preparedStatement.setString(1, user.getName());
        preparedStatement.setString(2, user.getFirstName());
        preparedStatement.setString(3, user.getLastName());
        preparedStatement.setFloat(4, user.getBalance());
        preparedStatement.setLong(5, user.getId());

        preparedStatement.executeUpdate();
    }

    @Override
    @SneakyThrows
    public Optional<User> findById(Long id) {
        var preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL);
        preparedStatement.setLong(1, id);

        var resultSet = preparedStatement.executeQuery();
        User user = null;
        if (resultSet.next()) {
            user = buildUser(resultSet);
        }

        return Optional.ofNullable(user);
    }

    @Override
    @SneakyThrows
    public List<User> findAll() {
        var preparedStatement = connection.prepareStatement(FIND_ALL_SQL);

        var resultSet = preparedStatement.executeQuery();
        List<User> users = new ArrayList<>();
        while (resultSet.next()) {
            users.add(buildUser(resultSet));
        }

        return users;
    }

    private User buildUser(ResultSet resultSet) throws SQLException {
        return User.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .firstName(resultSet.getString("first_name"))
                .lastName(resultSet.getString("last_name"))
                .balance(resultSet.getFloat("balance"))
                .build();
    }
}
