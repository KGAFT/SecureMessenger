package com.kgaft.securemessengerapp.Network.Entities;

public class UserEntity {
    private long userid;
    private String name;
    private String login;
    private long appId;

    public UserEntity(String userid, String name, String login, String appId) {
        this.userid = Long.parseLong(userid);
        this.name = name;
        this.login = login;
        this.appId = Long.parseLong(appId);
    }

    public long getUserId() {
        return userid;
    }

    public void setUserId(String userId) {
        this.userid = Long.parseLong(userId);
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

    public long getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = Long.parseLong(appId);
    }
}
