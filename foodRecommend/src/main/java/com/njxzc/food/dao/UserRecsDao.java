package com.njxzc.food.dao;

import com.njxzc.food.model.Score;
import org.bson.Document;

import java.util.List;

public interface UserRecsDao {
    List<Score> all(int id);
    List<Score> parseRecs(Document document);
}
