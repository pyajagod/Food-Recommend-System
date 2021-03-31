package com.njxzc.food.impl;

import com.njxzc.food.dao.FoodDao;
import com.njxzc.food.model.Food;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FoodImpl implements FoodDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void save(Food food) {
        int fid = foodCounts() + 1;
        while (findOne(fid) != null){
            fid++;
        }
        food.setFid(Long.valueOf(fid));
//        System.out.println(food);
        mongoTemplate.save(food);
    }

    @Override
    public void update(Food food) {
        Query query = new Query(Criteria.where("fid").is(food.getFid()));
        if (!"".equals(query)){
            Update update = new Update();
            update.set("name", food.getName());
            update.set("describe", food.getDescribe());
            update.set("canteen", food.getCanteen());
            update.set("genres", food.getGenres());
            update.set("avatar", food.getAvatar());
            mongoTemplate.updateFirst(query, update, Food.class);
        }

    }

    @Override
    public List<Food> getAll() {
        return mongoTemplate.findAll(Food.class);
    }

    @Override
    public Food findOne(Integer id) {
        Query query = new Query(Criteria.where("fid").is(id));
        return mongoTemplate.findOne(query, Food.class);
    }

    @Override
    public List<Food> findBy(Food food) {
        Criteria criteria = new Criteria();
        if (food.getFid()!= null){
            if (food.getFid().toString().length() != 0){
                criteria.and("fid").is(food.getFid());
            }
        }

        if (food.getGenres() != null){
            if (food.getGenres().toString().length() != 0){
                criteria.and("genres").is(food.getGenres());
            }
        }
        if (food.getCanteen() != null){
            if (food.getCanteen().toString().length() != 0){
                criteria.and("canteen").is(food.getCanteen());
            }
        }

        if (food.getName() != null){
            if (food.getName().toString().length() != 0){
                criteria.and("name").is(food.getName());
            }
        }
        Query query = new Query(criteria);
        List<Food> foods = mongoTemplate.find(query, Food.class);
        return foods;
    }

    @Override
    public int foodCounts() {
        return mongoTemplate.findAll(Food.class).size();
    }

    @Override
    public void deleteOne(Long id) {
        Query query = new Query(Criteria.where("fid").is(id));
        if (query == null){
            System.out.println("该美食不存在");
        }else {
            mongoTemplate.remove(query, Food.class);
        }
    }
}
