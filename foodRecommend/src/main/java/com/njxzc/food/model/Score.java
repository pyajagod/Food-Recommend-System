package com.njxzc.food.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document
public class Score implements Serializable {
    private int fid;
    private double score;

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public Score() {
    }

    public Score(int fid, double score) {
        this.fid = fid;
        this.score = score;
    }

    @Override
    public String toString() {
        return "Score{" +
                "fid=" + fid +
                ", score=" + score +
                '}';
    }
}
