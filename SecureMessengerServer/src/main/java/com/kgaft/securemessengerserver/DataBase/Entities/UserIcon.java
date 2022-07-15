package com.kgaft.securemessengerserver.DataBase.Entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="UserIcons")
public class UserIcon {
    @Id
    private long iconId;
    private String userLogin;

    public UserIcon() {
    }

    public UserIcon(long iconId, String userLogin) {
        this.iconId = iconId;
        this.userLogin = userLogin;
    }

    public long getIconId() {
        return iconId;
    }

    public void setIconId(long iconId) {
        this.iconId = iconId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }
}
