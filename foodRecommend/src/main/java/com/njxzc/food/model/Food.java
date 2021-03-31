package com.njxzc.food.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Foods")
public class Food {
    private Long fid;
    private String name;
    private String describe;
    private String genres;
    private String price;
    private String types;
    private String avatar;
    private String canteen;
    private Long checked;

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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Long getChecked() {
        return checked;
    }

    public void setChecked(Long checked) {
        this.checked = checked;
    }

    public String getCanteen() {
        return canteen;
    }

    public void setCanteen(String canteen) {
        this.canteen = canteen;
    }


    public Food() {
    }

    public Food(String name, String describe, String genres, String canteen) {
        this.name = name;
        this.describe = describe;
        this.genres = genres;
        this.canteen = canteen;
    }

    public Food(String name, String describe, String genres, String card, String canteen) {
        this.name = name;
        this.describe = describe;
        this.genres = genres;
        this.avatar = card;
        this.canteen = canteen;
    }

    public Food(String name, String describe, String genres, String price, String avatar, String canteen) {
        this.name = name;
        this.describe = describe;
        this.genres = genres;
        this.price = price;
        this.avatar = avatar;
        this.canteen = canteen;
    }

    public Food(String name, String describe, String genres, String price, String types, String avatar, String canteen, Long checked) {
        this.name = name;
        this.describe = describe;
        this.genres = genres;
        this.price = price;
        this.types = types;
        this.avatar = avatar;
        this.canteen = canteen;
        this.checked = checked;
    }

    public Food(Long fid, String name, String describe, String genres, String price, String types, String avatar, String canteen, Long checked) {
        this.fid = fid;
        this.name = name;
        this.describe = describe;
        this.genres = genres;
        this.price = price;
        this.types = types;
        this.avatar = avatar;
        this.canteen = canteen;
        this.checked = checked;
    }

    @Override
    public String toString() {
        return "Food{" +
                "fid=" + fid +
                ", name='" + name + '\'' +
                ", describe='" + describe + '\'' +
                ", genres='" + genres + '\'' +
                ", price='" + price + '\'' +
                ", types='" + types + '\'' +
                ", avatar='" + avatar + '\'' +
                ", canteen='" + canteen + '\'' +
                ", checked=" + checked +
                '}';
    }
}
