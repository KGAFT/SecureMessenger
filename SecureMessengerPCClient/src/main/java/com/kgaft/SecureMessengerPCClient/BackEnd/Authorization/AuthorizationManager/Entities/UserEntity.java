package com.kgaft.SecureMessengerPCClient.BackEnd.Authorization.AuthorizationManager.Entities;

import com.kgaft.SecureMessengerPCClient.BackEnd.Database.SQLTableEntityI;
import com.kgaft.SecureMessengerPCClient.BackEnd.Database.Value;

import java.util.ArrayList;
import java.util.List;

public class UserEntity implements SQLTableEntityI {
    private String login;
    private String password;
    private long appId;
    private String name;

    public UserEntity(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public UserEntity(String login, String password, String name) {
        this.login = login;
        this.password = password;
        this.name = name;
    }

    public UserEntity(String login, String password, long appId, String name) {
        this.login = login;
        this.password = password;
        this.appId = appId;
        this.name = name;
    }

    public UserEntity() {
    }

    @Override
    public List<Value> getAllValues() {
        List<Value> values = new ArrayList<>();
        values.add(new Value(String.class, login, "login"));
        values.add(new Value(String.class, password, "password"));
        values.add(new Value(Long.class, String.valueOf(appId), "appId"));
        values.add(new Value(String.class, name, "name"));
        return values;
    }

    @Override
    public void initWithValues(List<Value> values) {
        values.forEach(value->{
            switch(value.getValueName()){
                case "login":
                    login = value.getValue();
                    break;
                case "password":
                    password = value.getValue();
                    break;
                case "appId":
                    appId = Long.parseLong(value.getValue());
                    break;
                case "name":
                    name = value.getValue();
                    break;
            }
        });
    }

    @Override
    public String getTableName() {
        return "userinfo";
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
}
