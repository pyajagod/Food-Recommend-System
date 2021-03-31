package com.njxzc.food.dao;

import com.njxzc.food.model.User;

import java.util.List;

public interface UserDao {

    void save(User user);
    void update(User user);
    List<User> findAll();
    User findOne(Integer id);
    List<User> findBy(User user);
    int userCounts();
    void delete(Integer id);
}
