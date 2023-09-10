package com.devmandrik.service;

import com.devmandrik.dao.UserDao;
import com.devmandrik.entity.User;

import java.util.List;
import java.util.Optional;

public class UserService {

    private final UserDao userDao = UserDao.getInstance();

    public boolean delete(Long id) {
        return userDao.delete(id);
    }

    public User save(User user) {
        return userDao.save(user);
    }

    public void update(User user) {
        userDao.update(user);
    }

    public Optional<User> findById(Long id) {
        return userDao.findById(id);
    }

    public List<User> findAll() {
        return userDao.findAll();
    }
}
