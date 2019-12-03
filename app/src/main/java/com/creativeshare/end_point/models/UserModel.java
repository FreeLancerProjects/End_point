package com.creativeshare.end_point.models;

import java.io.Serializable;
import java.util.List;

public class UserModel implements Serializable {



        private int id;
            private String name;
    private String phone;
    private String email;
    private String logo;
    private String twitter;
    private String facebook;
    private String google;
    private String instagram;
    private String address;
    private long latitude;
            private long longitude;
            private int block;
    private String password_forget_code;
            private int balance;
            private int is_login;
            private String logout_time;
    private String email_verified_at;
            private int is_deleted;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getLogo() {
        return logo;
    }

    public String getTwitter() {
        return twitter;
    }

    public String getFacebook() {
        return facebook;
    }

    public String getGoogle() {
        return google;
    }

    public String getInstagram() {
        return instagram;
    }

    public String getAddress() {
        return address;
    }

    public long getLatitude() {
        return latitude;
    }

    public long getLongitude() {
        return longitude;
    }

    public int getBlock() {
        return block;
    }

    public String getPassword_forget_code() {
        return password_forget_code;
    }

    public int getBalance() {
        return balance;
    }

    public int getIs_login() {
        return is_login;
    }

    public String getLogout_time() {
        return logout_time;
    }

    public String getEmail_verified_at() {
        return email_verified_at;
    }

    public int getIs_deleted() {
        return is_deleted;
    }
}
