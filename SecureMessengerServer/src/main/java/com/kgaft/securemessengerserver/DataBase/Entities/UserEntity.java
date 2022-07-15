package com.kgaft.securemessengerserver.DataBase.Entities;

import com.google.gson.Gson;

import javax.persistence.*;

@Entity
@Table(name = "userslogindata")
public class UserEntity implements IJsonObject{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long userId;
    private String name;

    private String login;
    private String password;

    public UserEntity(long userId, String name, String login, String password) {
        this.name = name;
        this.userId = userId;
        this.login = login;
        this.password = password;
    }

    public UserEntity() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getUserId() {
        return userId;
    }


    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserId(long userid) {
        this.userId = userid;
    }

    @Override
    public String toJson() {
        return new Gson().toJson(this);
    }

}
