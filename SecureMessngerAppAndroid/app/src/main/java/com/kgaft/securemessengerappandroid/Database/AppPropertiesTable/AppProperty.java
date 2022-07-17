package com.kgaft.securemessengerappandroid.Database.AppPropertiesTable;


import com.kgaft.securemessengerappandroid.Database.TableColumn;
import com.kgaft.securemessengerappandroid.Database.TableInterface;

import java.util.ArrayList;
import java.util.List;

public class AppProperty implements TableInterface {

    private String serverBaseUrl;
    private String login;
    private String password;
    private long appId;

    public AppProperty(String serverBaseUrl, String login, String password, long appId) {
        this.serverBaseUrl = serverBaseUrl;
        this.login = login;
        this.password = password;
        this.appId = appId;
    }

    public AppProperty() {
    }

    public String getServerBaseUrl() {
        return serverBaseUrl;
    }

    public void setServerBaseUrl(String serverBaseUrl) {
        this.serverBaseUrl = serverBaseUrl;
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

    @Override
    public List<TableColumn> getAllValues() {
        List<TableColumn> values = new ArrayList<>();
        values.add(new TableColumn("serverBaseUrl", "TEXT", serverBaseUrl));
        values.add(new TableColumn("login", "TEXT", login));
        values.add(new TableColumn("password", "TEXT", password));
        values.add(new TableColumn("appId", "BIGINT", String.valueOf(appId)));
        return values;
    }

    @Override
    public void initWithValues(List<TableColumn> values) {
        for (TableColumn value : values) {
            switch (value.getColumnName()){
                case "serverBaseUrl":
                    this.serverBaseUrl = value.getColumnValue();
                    break;
                case "login":
                    this.login = value.getColumnValue();
                    break;
                case "password":
                    this.password = value.getColumnValue();
                    break;
                case "appId":
                    this.appId = Long.parseLong(value.getColumnValue());
                    break;
            }
        }
    }

    @Override
    public TableColumn getIDField() {
        return null;
    }
}
