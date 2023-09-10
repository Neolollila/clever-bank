package com.devmandrik.integration.dao;

import com.devmandrik.dao.UserDao;
import com.devmandrik.entity.User;
import com.devmandrik.integration.IntegrationTestBase;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

class UserDaoIT extends IntegrationTestBase {

    private static final UserDao userDao = UserDao.getInstance();

    @Test
    void delete() {
        var createdUser = createUser("dummy1");
        var savedUser = userDao.save(createdUser);
        System.out.println(savedUser);
        var delete = userDao.delete(savedUser.getId());

        assertThat(delete).isTrue();
    }

    @Test
    void save() {
        var createdUser = createUser("dummy2");

        var savedUser = userDao.save(createdUser);

        assertThat(createdUser.getName()).isEqualTo(savedUser.getName());
    }

    @Test
    void update() {
        var createdUser = createUser("dummy3");
        var savedUser = userDao.save(createdUser);

        savedUser.setName("Vasiliii");
        userDao.update(savedUser);
        Optional<User> findUser = userDao.findById(savedUser.getId());

        assertThat(findUser).isPresent();
        assertThat(findUser.get().getName()).isEqualTo("Vasiliii");
    }

    @Test
    void findById() {
        var createdUser = createUser("dummy4");
        var savedUser = userDao.save(createdUser);

        var findUser = userDao.findById(savedUser.getId());

        assertThat(findUser).isPresent();
        assertThat(savedUser).isEqualTo(findUser.get());
    }

    @Test
    void findAll() {
        var createdUser1 = createUser("dummy5");
        var createdUser2 = createUser("dummy6");
        var savedUser1 = userDao.save(createdUser1);
        var savedUser2 = userDao.save(createdUser2);

        var users = userDao.findAll();

        assertThat(users.size()).isEqualTo(2);
        assertAll(
                () -> assertThat(users).contains(savedUser1),
                () -> assertThat(users).contains(savedUser2)
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