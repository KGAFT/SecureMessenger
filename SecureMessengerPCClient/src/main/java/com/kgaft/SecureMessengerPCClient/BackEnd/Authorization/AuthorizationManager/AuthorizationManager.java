package com.kgaft.SecureMessengerPCClient.BackEnd.Authorization.AuthorizationManager;

import com.kgaft.SecureMessengerPCClient.BackEnd.Authorization.AuthorizationManager.Entities.UserResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.JsonObject;
import com.kgaft.SecureMessengerPCClient.BackEnd.Utils.IOUtil;

public class AuthorizationManager {
    private String authorizationUrl;
    private String checkConnectionUrl;
    private String unAuthorizeUrl;
    private String registrationUrl;
    public AuthorizationManager(String authorizationUrl, String checkConnectionUrl, String registrationUrl, String unAuthorizeUrl){
        this.authorizationUrl = authorizationUrl;
        this.checkConnectionUrl = checkConnectionUrl;
        this.unAuthorizeUrl = unAuthorizeUrl;
        this.registrationUrl = registrationUrl;
    }
    public UserResponse authorize(String login, String password) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(authorizationUrl+"?login="+login+"&password="+password).openConnection();
        connection.setRequestMethod("GET");
        connection.setDoInput(true);
        connection.connect();
        return new UserResponse(IOUtil.inputStreamToJsonObject(connection.getInputStream()));
    }
    public boolean register(String login, String password, String name) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(registrationUrl).openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setDoInput(true);
        OutputStream out = connection.getOutputStream();
        IOUtil.writeArgumentsToOutputStream("login="+login+"&password="+password+"&name="+name, out);
        out.flush();
        connection.connect();
        JsonObject response = IOUtil.inputStreamToJsonObject(connection.getInputStream());
        return response.get("response").getAsBoolean();
    }
    public void unAuthorize(String appId) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(unAuthorizeUrl).openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        OutputStream out = connection.getOutputStream();
        IOUtil.writeArgumentsToOutputStream("appId="+appId, out);
        out.flush();
        connection.connect();
    }
    public boolean checkConnection(long appId) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(checkConnectionUrl+"?appId="+appId).openConnection();
        connection.setDoInput(true);
        connection.connect();
        JsonObject response = IOUtil.inputStreamToJsonObject(connection.getInputStream());
        return response.get("response").getAsBoolean();
    }

}
