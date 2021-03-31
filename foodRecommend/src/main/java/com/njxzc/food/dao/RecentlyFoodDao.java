package com.njxzc.food.dao;

import com.njxzc.food.model.RecentlyRateFoods;

import java.util.List;

public interface RecentlyFoodDao {
    List<RecentlyRateFoods> getAll();
}
