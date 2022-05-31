package com.kgaft.securemessengerserver.DataBase.Entities;

import com.google.gson.Gson;

import javax.persistence.*;

@Entity
@Table(name = "userslogindata")
public class UserEntity implements IJsonObject{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long userid;
    private String name;

    private String login;
    private String password;

    public UserEntity(long userid, String name, String login, String password) {
        this.name = name;
        this.userid = userid;
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

    public long getUserid() {
        return userid;
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

    public void setUserid(long userid) {
        this.userid = userid;
    }

    @Override
    public String toJson() {
        return new Gson().toJson(this);
    }
}
