package com.njxzc.food.impl;

import com.njxzc.food.config.RedisConfig;
import com.njxzc.food.dao.RateDao;
import com.njxzc.food.model.Rates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

@Component
public class RateDaoImpl implements RateDao {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void save(Rates rates) {
        int l = (int) (System.currentTimeMillis() / 1000);
        rates.setTimestamp(l);
        mongoTemplate.save(rates);
    }

    @Override
    public Rates findRates(int fid, int uid) {
        Criteria criteria = new Criteria();
        criteria.and("fid").is(fid);
        criteria.and("uid").is(uid);
        Query query = new Query(criteria);
        return mongoTemplate.findOne(query, Rates.class);
    }

    @Override
    public void updateRedis(Rates rates) {
        Jedis jedis = new Jedis(RedisConfig.redisHost);
        if (jedis.exists("uid:" + rates.getUid()) && jedis.llen("uid:" + rates.getUid()) >= 40){
            jedis.rpop("uid:" + rates.getUid());
        }
        jedis.lpush("uid:" + rates.getFid(), rates.getFid() + ":" + rates.getScore());
    }
}
