package com.devmandrik;

import com.devmandrik.dao.UserDao;
import com.devmandrik.entity.User;

import java.util.List;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        var userDao = UserDao.getInstance();
        var user = userDao.findById(2L);
        User userUpdate = user.get();
        userUpdate.setName("Anonimus");
        userDao.update(userUpdate);
//        var all = userDao.findAll();
//        System.out.println(all);
//        var delete = userDao.delete(1L);
//        var byId = userDao.findById(1L);
//        var returnUser = userDao.save(user);
//        createUser(userDao);
    }

    private User createUser(UserDao userDao) {
        return User.builder()
                .name("Alex3")
                .firstName("Alexeevich3")
                .lastName("Alexeev3")
                .balance(122.5F)
                .build();
    }
}