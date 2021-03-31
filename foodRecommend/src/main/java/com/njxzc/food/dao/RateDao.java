package com.njxzc.food.dao;

import com.njxzc.food.model.Rates;

public interface RateDao {
    void save(Rates rates);
    Rates findRates(int fid, int uid);
    void updateRedis(Rates rates);
}
