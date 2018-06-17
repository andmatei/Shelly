package com.shelly.Models;

public class User {

    private String User_id;
    private String Username;
    private String Email;
    private String AccountType;

    public User() {

    }

    public User(String user_id, String username, String email, String accountType) {
        User_id = user_id;
        Username = username;
        Email = email;
        AccountType = accountType;
    }

    public String getUser_id() {
        return User_id;
    }

    public void setUser_id(String user_id) {
        User_id = user_id;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getAccountType() {
        return AccountType;
    }

    public void setAccountType(String accountType) {
        AccountType = accountType;
    }

    @Override
    public String toString() {
        return "User{" +
                "User_id='" + User_id + '\'' +
                ", Username='" + Username + '\'' +
                ", Email='" + Email + '\'' +
                ", AccountType='" + AccountType + '\'' +
                '}';
    }
}
