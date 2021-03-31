package com.njxzc.food.impl;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.njxzc.food.dao.UserRecsDao;
import com.njxzc.food.model.Score;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Component
public class UserRecsDaoImpl implements UserRecsDao {

    @Autowired
    private MongoClient client;



    @Override
    public List<Score> all(int id) {
        MongoCollection<Document> collection = client.getDatabase("foodSystem").getCollection("UserRecs");
        Document userRecs = collection.find(new Document("uid", id)).first();
        return parseRecs(userRecs);
    }

    @Override
    public List<Score> parseRecs(Document document) {
        ArrayList<Score> scores = new ArrayList<>();
        if (null == document || document.isEmpty()){
            return scores;
        }
        ArrayList<Document> recs = document.get("recs", ArrayList.class);
        for (Document recDoc: recs){
            scores.add(new Score(recDoc.getInteger("fid"),recDoc.getDouble("score")));
        }
        Collections.sort(scores, new Comparator<Score>() {
            @Override
            public int compare(Score o1, Score o2) {
                return o1.getScore() > o2.getScore() ? -1 : 1;
            }
        });
        return scores;
    }
}
