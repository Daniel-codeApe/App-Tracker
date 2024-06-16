package com.example.addressbook;

import java.util.Dictionary;
import java.util.Hashtable;

public class User {
    private String username;
    private String password;
    private String email;
    private int id;

    public User(String username, String email, String password) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getEmail() {
        return this.email;
    }
}