package com.devmandrik.integration.dao;

import com.devmandrik.dao.UserDao;
import com.devmandrik.entity.User;
import com.devmandrik.integration.IntegrationTestBase;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static com.devmandrik.util.ConstantAndMethodUtil.*;
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

        assertNotNull(createdUser.getId());
    }

    @Test
    void update() {
        var createdUser = createUser("dummy3");
        var savedUser = userDao.save(createdUser);

        savedUser.setName(ALEX);
        userDao.update(savedUser);
        Optional<User> findUser = userDao.findById(savedUser.getId());

        assertThat(findUser).isPresent();
        assertThat(findUser.get().getName()).isEqualTo(ALEX);
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

        List<Long> ids = users.stream()
                .map(User::getId)
                .toList();
        assertThat(users).hasSize(2);
        assertThat(ids).contains(savedUser1.getId(), savedUser2.getId());
    }
}