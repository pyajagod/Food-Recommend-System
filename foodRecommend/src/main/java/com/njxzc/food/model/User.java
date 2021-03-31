package com.njxzc.food.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user")
public class User {

//    @Id
    private long uid;

    private String username;
    private String password;
    private String avatar;
    private String phone;
    private String email;
    private String gender;
    private String time;
    private String favorite;


    public Long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFavorite() {
        return favorite;
    }

    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(long uid, String username, String password, String phone, String time) {
        this.uid = uid;
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.time = time;
    }

    public User(long uid, String username, String avatar, String phone, String email, String gender, String favorite) {
        this.uid = uid;
        this.username = username;
        this.avatar = avatar;
        this.phone = phone;
        this.email = email;
        this.gender = gender;
        this.favorite = favorite;
    }


    @Override
    public String toString() {
        return "User{" +
                "uid=" + uid +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", headPortrait='" + avatar + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                ", time=" + time +
                ", favorite='" + favorite + '\'' +
                '}';
    }
}
