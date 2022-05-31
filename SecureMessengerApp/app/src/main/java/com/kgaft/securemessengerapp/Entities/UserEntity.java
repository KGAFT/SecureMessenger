package com.kgaft.securemessengerapp.Entities;

public class UserEntity {
    private long userId;
    private String name;
    private String login;

    public UserEntity(long userId, String name, String login) {
        this.userId = userId;
        this.name = name;
        this.login = login;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
