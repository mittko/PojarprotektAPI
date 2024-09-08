package com.example.demo.security;

import com.example.demo.models.User;

import javax.swing.plaf.PanelUI;

public class LoginRes {

    private User user;
    private String token;

    public LoginRes(User user, String token) {
        this.user = user;
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}