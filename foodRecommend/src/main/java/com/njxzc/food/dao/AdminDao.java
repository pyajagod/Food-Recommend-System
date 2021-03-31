package com.njxzc.food.dao;

import com.njxzc.food.model.Admin;

import java.util.List;

public interface AdminDao {

    int adminCounts();
    void save(Admin admin);
    void update(Admin admin);
    Admin findOne(int id);
    List<Admin> showBy(Admin admin);
    List<Admin> showAll();

    void delete(Integer id);
}
