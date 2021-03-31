package com.njxzc.food.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@Document(collection = "UserRecs")
public class UserRecs {
    private int uid;

//    private Score score;

    private List<Score> recs;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public List<Score> getRecs() {
        return recs;
    }

    public void setRecs(List<Score> recs) {
        this.recs = recs;
    }

    public UserRecs() {
    }

    public UserRecs(int uid, List<Score> recs) {
        this.uid = uid;
        this.recs = recs;
    }

    @Override
    public String toString() {
        return "UserRecs{" +
                "uid=" + uid +
                ", recs=" + recs +
                '}';
    }
}
