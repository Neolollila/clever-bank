package com.devmandrik;

import com.devmandrik.dao.UserDao;
import com.devmandrik.entity.User;

import java.util.List;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        var userDao = new UserDao();
//        var user = userDao.findById(2L);
//        User userUpdate = user.get();
//        userUpdate.setName("Anonimus");
//        userDao.update(userUpdate);
//        var all = userDao.findAll();
//        System.out.println(all);
//        var delete = userDao.delete(10L);
//        System.out.println(delete);
//        var byId = userDao.findById(1L);
        var user = createUser();
        var returnUser = userDao.save(user);
        System.out.println(returnUser);
    }

    private static User createUser() {
        return User.builder()
                .name("Vova33")
                .firstName("Alexeevich33")
                .lastName("Alexeev33")
                .balance(1223.5F)
                .build();
    }
}