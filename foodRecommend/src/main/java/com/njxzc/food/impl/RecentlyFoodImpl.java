package com.njxzc.food.impl;

import com.njxzc.food.dao.RecentlyFoodDao;
import com.njxzc.food.model.RecentlyRateFoods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RecentlyFoodImpl implements RecentlyFoodDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<RecentlyRateFoods> getAll() {
        return mongoTemplate.findAll(RecentlyRateFoods.class);
    }
}
