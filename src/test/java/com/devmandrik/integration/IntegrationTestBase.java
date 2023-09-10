package com.devmandrik.integration;

import com.devmandrik.util.ConnectionManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.sql.SQLException;

public class IntegrationTestBase {

    private static final String CLEAN_SQL = """
            DELETE FROM users;
            DELETE FROM bank;
            DELETE FROM account;
            DELETE FROM transaction;
            """;
    private static final String CREATE_SQL = """
            CREATE TABLE IF NOT EXISTS users (
                id SERIAL PRIMARY KEY ,
                name VARCHAR(64) NOT NULL UNIQUE ,
                first_name VARCHAR(64) ,
                last_name VARCHAR(64) ,
                balance FLOAT
            );
            CREATE TABLE IF NOT EXISTS bank(
                id SERIAL PRIMARY KEY ,
                name VARCHAR(64) NOT NULL UNIQUE
            );
            
            CREATE TABLE IF NOT EXISTS transaction(
                id SERIAL PRIMARY KEY ,
                created_at TIMESTAMP ,
                transaction_type VARCHAR(64) ,
                currency VARCHAR(8) ,
                sum FLOAT ,
                fromBank_id INT REFERENCES bank (id)  ,
                toBank_id INT REFERENCES bank (id)  ,
                user_id INT REFERENCES users (id)
            );
            
            CREATE TABLE IF NOT EXISTS account(
                id SERIAL PRIMARY KEY ,
                created_at TIMESTAMP ,
                sum FLOAT ,
                user_id INT REFERENCES users (id) ,
                Bank_id INT REFERENCES bank (id)
            );
            
            CREATE TABLE IF NOT EXISTS users_bank
            (
                id SERIAL PRIMARY KEY ,
                user_id BIGINT NOT NULL REFERENCES users (id) ON DELETE CASCADE ,
                bank_id BIGINT NOT NULL REFERENCES bank (id) ON DELETE CASCADE ,
                UNIQUE (user_id, bank_id)
            );
            """;

    @BeforeAll
    static void prepareDatabase() throws SQLException {
        try (var connection = ConnectionManager.get();
             var statement = connection.createStatement()) {
            statement.execute(CREATE_SQL);
        }
    }

    @BeforeEach
    void cleanData() throws SQLException {
        try (var connection = ConnectionManager.get();
             var statement = connection.createStatement()) {
            statement.execute(CLEAN_SQL);
        }
    }
}
