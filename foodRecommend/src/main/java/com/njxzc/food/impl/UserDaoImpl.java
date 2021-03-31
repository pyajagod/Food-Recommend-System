package com.njxzc.food.impl;

import com.njxzc.food.dao.UserDao;
import com.njxzc.food.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class UserDaoImpl implements UserDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void save(User user) {
        int uid = userCounts() + 1;
        while (findOne(uid) != null){
            uid ++;
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String date = df.format(new Date());
        user.setUid(uid);
        user.setTime(date);
        mongoTemplate.save(user);
    }

    @Override
    public void update(User user) {
        Query query = new Query(Criteria.where("uid").is(user.getUid()));
        if (!"".equals(query)) {
            Update update = new Update();
            update.set("username", user.getUsername());
            update.set("phone", user.getPhone());
            update.set("email", user.getEmail());
            update.set("avatar", user.getAvatar());
            update.set("gender", user.getGender());
            update.set("favorite", user.getFavorite());
            mongoTemplate.updateFirst(query, update, User.class);
        }
    }
    @Override
    public int userCounts() {
        return mongoTemplate.findAll(User.class).size();
    }

    @Override
    public List<User> findAll() {
        return mongoTemplate.findAll(User.class);
    }

    @Override
    public User findOne(Integer id) {
        Query query = new Query(Criteria.where("uid").is(id));
        return mongoTemplate.findOne(query, User.class);
    }

    @Override
    public List<User> findBy(User user) {
        System.out.println(user);
        Criteria criteria = new Criteria();
        if (user.getUid() != 0){
//            System.out.println("1 " + user.getUid());
            if (user.getUid().toString().length() != 0) {
                criteria.and("uid").is(user.getUid());
            }
        }
        if (user.getUsername()!= null){
//            System.out.println("2");
            if (user.getUsername().length() != 0) {
                criteria.and("username").is(user.getUsername());
            }
        }
        if (user.getPassword() != null){
//            System.out.println("3");
            if (user.getPassword().length() != 0) {
                criteria.and("password").is(user.getPassword());
            }
        }

        if (user.getEmail() != null){
            System.out.println("4");
            if (user.getEmail().length() != 0) {
                criteria.and("email").is(user.getEmail());
            }
        }

        if (user.getGender() != null){
            System.out.println("5");
            if (user.getGender().length() != 0) {
                criteria.and("gender").is(user.getGender());
            }
        }
        Query query = new Query(criteria);
//        Field fields = query.fields();
//        fields.include("data");
//        query.with(new Sort(new Sort.Order(Sort.Direction.ASC, "uid")));
        return mongoTemplate.find(query, User.class);
    }

    @Override
    public void delete(Integer id) {
        Query query = new Query(Criteria.where("uid").is(id));
        if (query == null){
            System.out.println("该用户不存在");
        }else {
            mongoTemplate.remove(query, User.class);
        }
    }
}
