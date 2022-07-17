package com.kgaft.SecureMessengerPCClient.BackEnd.Authorization;

import com.kgaft.SecureMessengerPCClient.BackEnd.Authorization.AuthorizationManager.AuthorizationManager;
import com.kgaft.SecureMessengerPCClient.BackEnd.Authorization.AuthorizationManager.Entities.UserEntity;
import com.kgaft.SecureMessengerPCClient.BackEnd.Authorization.AuthorizationManager.Entities.UserResponse;
import com.kgaft.SecureMessengerPCClient.BackEnd.Database.Database;
import com.kgaft.SecureMessengerPCClient.BackEnd.Database.SQLTableEntityI;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AuthorizationNativeCalls {
    private AuthorizationManager serverAccess;

    public AuthorizationNativeCalls(String authorizationUrl, String checkConnectionUrl, String registrationUrl, String unAuthorizeUrl) {
        serverAccess = new AuthorizationManager(authorizationUrl, checkConnectionUrl, registrationUrl, unAuthorizeUrl);
        try {
            Database.createAndRegisterTableClass(new UserEntity());
        } catch (Exception e) {

        }
    }
    public AuthorizationNativeCalls(String serverUrl){
        serverAccess = new AuthorizationManager(serverUrl+"authorizeClient", serverUrl+"checkConnection", serverUrl+"register", serverUrl+"unAuthorizeClient");
        try {
            Database.createAndRegisterTableClass(new UserEntity());
        } catch (Exception e) {

        }
    }
    public boolean register(String login, String password, String name) {
        try {
            if (serverAccess.register(login, password, name)) {
                UserResponse userServerInfo = serverAccess.authorize(login, password);
                UserEntity user = new UserEntity();
                try {
                    Database.deleteQuery("DELETE FROM " + user.getTableName() + " WHERE cast(login AS CHAR(128))='" + userServerInfo.getLogin()+"'");
                }catch (SQLException e) {
                    e.printStackTrace();
                }
                user.setLogin(login);
                user.setName(name);
                user.setPassword(password);
                user.setAppId(userServerInfo.getAppId());
                Database.insertEntity(user);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean authorize(String login, String password) {
        try {
            UserResponse userServerInfo = serverAccess.authorize(login, password);
            UserEntity user = new UserEntity();
            try {
                Database.deleteQuery("DELETE FROM "+user.getTableName()+" WHERE CAST(login AS CHAR(128))='"+login+"'");
            }catch (Exception e){
                e.printStackTrace();
            }
            user.setAppId(userServerInfo.getAppId());
            user.setPassword(password);
            user.setLogin(login);
            user.setName(userServerInfo.getName());
            Database.insertEntity(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public boolean checkConnection(){
        try{
            return serverAccess.checkConnection(getCurrentAuthorizedUser().getAppId());
        }catch (Exception e){
            return false;
        }
    }
    public UserEntity getCurrentAuthorizedUser() throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        UserEntity authorizedUser = new UserEntity();
        List<SQLTableEntityI> resultsSelection = new ArrayList<>();
        Database.selectQuery("SELECT * FROM "+authorizedUser.getTableName()+"", resultsSelection, authorizedUser);
        authorizedUser = (UserEntity) resultsSelection.get(0);
        return authorizedUser;
    }
}
