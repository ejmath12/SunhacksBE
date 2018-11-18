package com.sunhacks.models;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user")
public class User {
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String userId) {
        this.username = username;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    private String passWord;
}
