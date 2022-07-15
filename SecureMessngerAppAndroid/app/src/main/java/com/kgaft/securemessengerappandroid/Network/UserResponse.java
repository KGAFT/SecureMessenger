package com.kgaft.securemessengerappandroid.Network;

import com.google.gson.JsonObject;

public class UserResponse {
    private long appId;
    private String name;
    private String login;

    public UserResponse() {
    }
    public UserResponse(JsonObject userJson){
        appId = userJson.get("appId").getAsLong();
        name = userJson.get("name").getAsString();
        login = userJson.get("login").getAsString();
    }
    public UserResponse(long appId, String name, String login) {
        this.appId = appId;
        this.name = name;
        this.login = login;
    }

    public long getAppId() {
        return appId;
    }

    public void setAppId(long appId) {
        this.appId = appId;
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
