CREATE TABLE IF NOT EXISTS users
(
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(64) NOT NULL UNIQUE,
    first_name VARCHAR(64),
    last_name  VARCHAR(64)
);

CREATE TABLE IF NOT EXISTS bank
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(64) NOT NULL UNIQUE
);


CREATE TABLE IF NOT EXISTS transaction
(
    id               SERIAL PRIMARY KEY,
    created_at       TIMESTAMP,
    transaction_type VARCHAR(64),
    currency         VARCHAR(8),
    sum              FLOAT,
    fromBank_id      INT REFERENCES bank (id),
    toBank_id        INT REFERENCES bank (id),
    user_id          INT REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS account
(
    id         SERIAL PRIMARY KEY,
    created_at TIMESTAMP,
    sum        FLOAT,
    currency   VARCHAR(8),
    user_id    INT REFERENCES users (id),
    Bank_id    INT REFERENCES bank (id)
);

CREATE TABLE IF NOT EXISTS users_bank
(
    id      SERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    bank_id BIGINT NOT NULL REFERENCES bank (id) ON DELETE CASCADE,
    UNIQUE (user_id, bank_id)
);