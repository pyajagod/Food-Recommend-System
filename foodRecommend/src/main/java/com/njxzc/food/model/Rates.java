package com.njxzc.food.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Ratings")
public class Rates {
    private int uid;
    private int fid;
    private Double score;
    private int timestamp;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public Rates() {
    }

    public Rates(int uid, int fid, Double score, int timestamp) {
        this.uid = uid;
        this.fid = fid;
        this.score = score;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Rates{" +
                "uid=" + uid +
                ", fid=" + fid +
                ", score=" + score +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
