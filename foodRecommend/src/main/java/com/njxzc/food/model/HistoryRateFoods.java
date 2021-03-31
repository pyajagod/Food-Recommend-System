package com.njxzc.food.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "HistoryRateFoods")
public class HistoryRateFoods {
    private Long fid;
    private String name;
    private String describe;
    private String genres;
    private String price;
    private String types;
    private String avatar;
    private String canteen;
    private Long count;

    public Long getFid() {
        return fid;
    }

    public void setFid(Long fid) {
        this.fid = fid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCanteen() {
        return canteen;
    }

    public void setCanteen(String canteen) {
        this.canteen = canteen;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public HistoryRateFoods() {
    }

    public HistoryRateFoods(Long fid, String name, String describe, String genres, String price, String types, String avatar, String canteen, Long count) {
        this.fid = fid;
        this.name = name;
        this.describe = describe;
        this.genres = genres;
        this.price = price;
        this.types = types;
        this.avatar = avatar;
        this.canteen = canteen;
        this.count = count;
    }

    @Override
    public String toString() {
        return "HistoryRateFoods{" +
                "fid=" + fid +
                ", name='" + name + '\'' +
                ", describe='" + describe + '\'' +
                ", genres='" + genres + '\'' +
                ", price='" + price + '\'' +
                ", types='" + types + '\'' +
                ", avatar='" + avatar + '\'' +
                ", canteen='" + canteen + '\'' +
                ", count=" + count +
                '}';
    }
}
