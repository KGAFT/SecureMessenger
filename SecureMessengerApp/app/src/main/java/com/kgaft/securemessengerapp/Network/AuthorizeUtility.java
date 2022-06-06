package com.kgaft.securemessengerapp.Network;

import com.kgaft.securemessengerapp.Network.AuthorizeInterfaces.CheckConnectionInterface;
import com.kgaft.securemessengerapp.Network.AuthorizeInterfaces.LoginNetworkInterface;
import com.kgaft.securemessengerapp.Network.AuthorizeInterfaces.RegisterNetworkInterface;
import com.kgaft.securemessengerapp.Network.Entities.UserEntity;

import java.io.IOException;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthorizeUtility {

    public static String registerUser(String login, String password, String name, String serverAddress){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(serverAddress).addConverterFactory(GsonConverterFactory.create()).build();
        RegisterNetworkInterface networkInterface = retrofit.create(RegisterNetworkInterface.class);
        try {
            String response = networkInterface.registerUser(login, password, name).execute().body().getResponse();
            return response;
        } catch (IOException e) {
            e.printStackTrace();
            return "Error cannot connect to the server";
        }
    }
    public static UserEntity loginUser(String login, String password, String serverAddress){

        Retrofit retrofit = new Retrofit.Builder().baseUrl(serverAddress).addConverterFactory(GsonConverterFactory.create()).build();
        LoginNetworkInterface networkInterface = retrofit.create(LoginNetworkInterface.class);
        try {
            return networkInterface.getUser(login, password).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static boolean checkConnection(String appId, String serverAddress) throws IOException {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(serverAddress).addConverterFactory(GsonConverterFactory.create()).build();
        CheckConnectionInterface checkConnection = retrofit.create(CheckConnectionInterface.class);
        String response = checkConnection.checkConnection(appId).execute().body().getResponse();
        return Boolean.parseBoolean(response);
    }
}
