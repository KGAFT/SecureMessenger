package com.kgaft.securemessengerserver.DataBase.Entities;

import javax.persistence.*;

@Entity
@Table(name = "AuthorizedUsers")
public class AuthorizedUserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long appId;
    private long userId;

    public AuthorizedUserEntity(long appId, long userId) {
        this.appId = appId;
        this.userId = userId;
    }

    public AuthorizedUserEntity() {
    }

    public long getAppId() {
        return appId;
    }

    public void setAppId(long appId) {
        this.appId = appId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
