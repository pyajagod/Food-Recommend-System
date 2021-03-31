package com.njxzc.food.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "admin")

public class Admin {

    private Long id;
    private String adminName;
    private String password;
    private String phone;
    private String email;
    private String time;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Admin() {
    }

    public Admin(String adminName, String password, String phone, String email, String time) {
        this.adminName = adminName;
        this.password = password;
        this.phone = phone;
        this.email = email;
        this.time = time;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "id=" + id +
                ", adminName='" + adminName + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
