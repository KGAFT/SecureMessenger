package com.kgaft.securemessengerapp.Network;

import com.kgaft.securemessengerapp.Network.MessagesInterfaces.ChatStartInterface;
import com.kgaft.securemessengerapp.Network.MessagesInterfaces.MessagesInterface;
import com.kgaft.securemessengerapp.Network.Entities.MessageEntity;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MessageUtility {
    private Retrofit retrofit;
    private String appId;
    public MessageUtility(String serverAddress, String appId){
        this.appId = appId;
        retrofit = new Retrofit.Builder().baseUrl(serverAddress).addConverterFactory(GsonConverterFactory.create()).build();;
    }
    public ArrayList<MessageEntity> getMessages() throws IOException {
        MessagesInterface messageController = retrofit.create(MessagesInterface.class);
        return messageController.getAllMessages(appId).execute().body();
    }
    public ArrayList<MessageEntity> getMessagesMoreThanTimeStamp(long timeInMilliseconds) throws IOException {
        MessagesInterface messagesController = retrofit.create(MessagesInterface.class);
        return messagesController.getMessagesByTime(appId, timeInMilliseconds).execute().body();
    }
    public String startChat(){
        ChatStartInterface startChatInterface = retrofit.create(ChatStartInterface.class);

        try {
            return startChatInterface.startChat(appId).execute().body().getResponse();
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed";
        }
    }
    public boolean joinChat(String receiver) {
        ChatStartInterface joinChatController = retrofit.create(ChatStartInterface.class);
        try{
            return Boolean.parseBoolean(joinChatController.joinChat(appId, receiver).execute().body().getResponse());
        }catch (Exception e){
            return false;
        }

    }
    public String getReceiverName(String receiverLogin) {
        MessagesInterface messageController = retrofit.create(MessagesInterface.class);
        try {
            return messageController.getUserName(appId, receiverLogin).execute().body().getResponse();
        } catch (IOException e) {
            return "Error";
        }
    }
    public boolean sendMessage(MessageEntity message){
        MessagesInterface messageController = retrofit.create(MessagesInterface.class);
        String files = "";
        try{
            for (long l : message.getContentId()) {
                files+=String.valueOf(l)+";";
            }
        }catch (Exception e){
            files = "0;";
        }
        try {
            return Boolean.parseBoolean(messageController.sendMessage(appId, message.getReceiver(), message.getMessagetext(), files).execute().body().getResponse());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
