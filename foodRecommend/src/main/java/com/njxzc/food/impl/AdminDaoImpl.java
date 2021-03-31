package com.njxzc.food.impl;

import com.njxzc.food.dao.AdminDao;
import com.njxzc.food.model.Admin;
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
public class AdminDaoImpl implements AdminDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public int adminCounts() {
        return mongoTemplate.findAll(Admin.class).size();
    }

    @Override
    public void save(Admin admin) {
        int id = adminCounts() + 1;
        while (findOne(id) != null){
            id ++;
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String date = df.format(new Date());
        admin.setId((long) id);
        admin.setTime(date);
//        System.out.println(admin);
        mongoTemplate.save(admin);
    }

    @Override
    public void update(Admin admin) {
        System.out.println(admin);
        Query query = new Query(Criteria.where("id").is(admin.getId()));
        if (!"".equals(query)) {
            Update update = new Update();
            update.set("adminName", admin.getAdminName());
            update.set("phone", admin.getPhone());
            update.set("email", admin.getEmail());
            mongoTemplate.updateFirst(query, update, Admin.class);
        }
    }

    public Admin findOne(int id) {
        Query query = new Query(Criteria.where("id").is(id));
        return mongoTemplate.findOne(query, Admin.class);
    }

    @Override
    public List<Admin> showBy(Admin admin) {
        Criteria criteria = new Criteria();
//        System.out.println(admin);
        if (admin.getId() != null){
            if (admin.getId().toString().length() != 0) {
//            System.out.println(".....");
                criteria.and("id").is(admin.getId());
            }
        }
        if (admin.getAdminName()!= null) {
//            System.out.println("aaaaaa");
            if (admin.getAdminName().length() != 0) {
                criteria.and("adminName").is(admin.getAdminName());
            }
        }
        if (admin.getPassword() != null){
//            System.out.println("sdsddsd");
            if (admin.getPassword().length() != 0) {
                criteria.and("password").is(admin.getPassword());
            }
        }
        if (admin.getPhone() != null) {
            if (admin.getPhone().length() != 0) {
                criteria.and("phone").is(admin.getPhone());
            }
        }
        if (admin.getEmail() != null){
            if (admin.getEmail().length() != 0) {
//                System.out.println("adsdadadascascasdasads");
                criteria.and("email").is(admin.getEmail());
            }
        }
        Query query = new Query(criteria);
//        Field fields = query.fields();
//        fields.include("data");
//        query.with(new Sort(new Sort.Order(Sort.Direction.ASC, "uid")));
        List<Admin> admins = mongoTemplate.find(query,Admin.class);
//        System.out.println(admins);
        return admins;
    }

    @Override
    public List<Admin> showAll() {
        return mongoTemplate.findAll(Admin.class);
    }

    @Override
    public void delete(Integer id) {
        Query query = new Query(Criteria.where("id").is(id));
        if (query == null){
            System.out.println("该管理员不存在");
        }else {
            mongoTemplate.remove(query, Admin.class);
        }
    }
}
