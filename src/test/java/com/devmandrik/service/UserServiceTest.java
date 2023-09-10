package com.devmandrik.service;

import com.devmandrik.dao.UserDao;
import com.devmandrik.entity.User;
import com.devmandrik.util.ConnectionManager;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private static UserDao userDao;

    @BeforeAll
    @SneakyThrows
    static void init() {
        Connection conn = ConnectionManager.get();
        conn.setAutoCommit(false);
        userDao = new UserDao(conn);
    }

    @AfterAll
    @SneakyThrows
    static void teardown() {
        Connection conn = ConnectionManager.get();
        conn.setAutoCommit(true);
    }

    @Test
    void delete() {
        var createdUser = createUser("dummy1");
        var savedUser = userDao.save(createdUser);
        System.out.println(savedUser);
        var delete = userDao.delete(savedUser.getId());

        assertTrue(delete);
    }

    @Test
    void save() {
        var createdUser = createUser("dummy2");

        var savedUser = userDao.save(createdUser);

        assertEquals(createdUser.getName(), savedUser.getName());
    }

    @Test
    void update() {
        var createdUser = createUser("dummy3");
        var savedUser = userDao.save(createdUser);

        savedUser.setName("Vasiliii");
        userDao.update(savedUser);
        var findUser = userDao.findById(savedUser.getId()).orElse(null);

        assertEquals(findUser.getName(), "Vasiliii");
    }

    @Test
    void findById() {
        var createdUser = createUser("dummy4");
        var savedUser = userDao.save(createdUser);

        var findUser = userDao.findById(savedUser.getId()).orElse(null);

        assertEquals(savedUser, findUser);
    }

    @Test
    void findAll() {
        var createdUser1 = createUser("dummy5");
        var createdUser2 = createUser("dummy6");
        var savedUser1 = userDao.save(createdUser1);
        var savedUser2 = userDao.save(createdUser2);

        var users = userDao.findAll();

        assertAll(
                () -> assertTrue(users.contains(savedUser1)),
                () -> assertTrue(users.contains(savedUser2))
        );
    }

    private User createUser(String name) {
        return User.builder()
                .name(name)
                .firstName("Alexeevich33")
                .lastName("Alexeev33")
                .balance(1223.5F)
                .build();
    }
}