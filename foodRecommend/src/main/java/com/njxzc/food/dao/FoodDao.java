package com.njxzc.food.dao;

import com.njxzc.food.model.Food;

import java.util.List;

public interface FoodDao {

    void save(Food food);
    void update(Food food);
    List<Food> getAll();
    Food findOne(Integer id);
    List<Food> findBy(Food food);
    int foodCounts();
    void deleteOne(Long id);
}
