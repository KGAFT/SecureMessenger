package com.kgaft.securemessengerapp.Network;

import com.google.gson.GsonBuilder;
import com.kgaft.securemessengerapp.Network.AuthorizeInterfaces.MessagesInterface;
import com.kgaft.securemessengerapp.Network.Entities.MessageEntity;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MessageUtility {
    public static ArrayList<MessageEntity> getMessages(String appId, String serverAddress) throws IOException {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(serverAddress).addConverterFactory(GsonConverterFactory.create()).build();
        MessagesInterface messageController = retrofit.create(MessagesInterface.class);
        return messageController.getMessages(appId).execute().body();
    }
}
