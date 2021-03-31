package com.njxzc.food.impl;

import com.njxzc.food.dao.HistoryFoodDao;
import com.njxzc.food.model.HistoryRateFoods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HistoryFoodDaoImpl implements HistoryFoodDao {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<HistoryRateFoods> getAll() {
        return mongoTemplate.findAll(HistoryRateFoods.class);
    }
}
