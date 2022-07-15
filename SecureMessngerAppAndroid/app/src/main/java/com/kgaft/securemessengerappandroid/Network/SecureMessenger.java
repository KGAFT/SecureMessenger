package com.kgaft.securemessengerappandroid.Network;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kgaft.securemessengerappandroid.Database.MessagesTable.MessageEntity;
import com.kgaft.securemessengerappandroid.Files.IOUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SecureMessenger {
    private final String authorizationUrl;
    private final String checkConnectionUrl;
    private final String unAuthorizeUrl;
    private final String registrationUrl;
    private final String joinChatUrl;
    private final String startChatUrl;
    private final String getAllMessagesUrl;
    private final String getMessagesByTimeUrl;
    private final String sendMessageUrl;
    private final String userInfoUrl;
    public SecureMessenger(String serverUrl){
        this.joinChatUrl = serverUrl+"joinChat";
        this.startChatUrl = serverUrl+"startChat";
        this.getAllMessagesUrl = serverUrl+"getMessages";
        this.getMessagesByTimeUrl = serverUrl+"getMessagesByTime";
        this.sendMessageUrl = serverUrl+"sendMessage";
        this.userInfoUrl = serverUrl+"getUsername";
        this.authorizationUrl = serverUrl+"authorizeClient";
        this.checkConnectionUrl = serverUrl+"checkConnection";
        this.registrationUrl = serverUrl+"register";
        this.unAuthorizeUrl = serverUrl+"unAuthorizeClient";
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
    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<MessageEntity> getAllMessages(long appId) throws IOException {
        HttpURLConnection messageConnection = (HttpURLConnection) new URL(getAllMessagesUrl+"?appId="+appId).openConnection();
        messageConnection.setDoInput(true);
        messageConnection.setRequestMethod("GET");
        messageConnection.connect();
        JsonArray messagesJson = new JsonParser().parse(IOUtil.inputStreamToString(messageConnection.getInputStream())).getAsJsonArray();
        return jsonArrayToMessages(messagesJson);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<MessageEntity> getLastMessagesByTime(long appId, long time) throws IOException{
        HttpURLConnection connection = (HttpURLConnection) new URL(getMessagesByTimeUrl+"?appId="+appId+"&timeInMilliseconds="+time).openConnection();
        connection.setDoInput(true);
        connection.setRequestMethod("GET");
        connection.connect();
        JsonArray messagesJson = new JsonParser().parse(IOUtil.inputStreamToString(connection.getInputStream())).getAsJsonArray();
        return jsonArrayToMessages(messagesJson);
    }
    public boolean sendMessage(long appId, MessageEntity message) throws IOException{
        HttpURLConnection connection = (HttpURLConnection) new URL(sendMessageUrl).openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        String messageInString = "appId="+appId+messageToString(message);
        IOUtil.writeArgumentsToOutputStream(messageInString, connection.getOutputStream());
        connection.getOutputStream().flush();
        connection.connect();
        return IOUtil.inputStreamToJsonObject(connection.getInputStream()).get("response").getAsBoolean();
    }
    public boolean joinChat(long appId, String receiver) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(joinChatUrl).openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestMethod("POST");
        IOUtil.writeArgumentsToOutputStream("appId="+appId+"&receiver="+receiver, connection.getOutputStream());
        connection.getOutputStream().flush();
        connection.connect();
        return IOUtil.inputStreamToJsonObject(connection.getInputStream()).get("response").getAsBoolean();
    }
    public String getUserName(long appId, String userLogin) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(userInfoUrl+"?appId="+appId+"&userLogin="+userLogin).openConnection();
        connection.setDoInput(true);
        connection.setRequestMethod("GET");
        connection.connect();
        return IOUtil.inputStreamToJsonObject(connection.getInputStream()).get("response").getAsString();
    }
    public String startChat(long appId) throws IOException{
        HttpURLConnection connection = (HttpURLConnection) new URL(startChatUrl+"?appId="+appId).openConnection();
        connection.setDoInput(true);
        connection.connect();
        return IOUtil.inputStreamToJsonObject(connection.getInputStream()).get("response").getAsString();
    }
    private String messageToString(MessageEntity message){
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append("&receiver="+message.getReceiver());
        messageBuilder.append("&text="+message.getMessageText());
        messageBuilder.append("&files=");
        for (long content : message.getContentId()) {
            messageBuilder.append(content+";");
        }
        return messageBuilder.toString();
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private List<MessageEntity> jsonArrayToMessages(JsonArray messagesJson){
        List<MessageEntity> messages = new ArrayList<>();
        messagesJson.forEach(messageElementJson->{
            MessageEntity message = new MessageEntity();
            JsonObject messageJson = messageElementJson.getAsJsonObject();
            message.setMessageId(messageJson.get("messageId").getAsLong());
            message.setMessageText(messageJson.get("messageText").getAsString());
            message.setReceiver(messageJson.get("receiver").getAsString());
            message.setSender(messageJson.get("sender").getAsString());
            message.setTime(messageJson.get("time").getAsLong());
            JsonArray contentsJson = messageJson.get("contentId").getAsJsonArray();
            long[] contentId = new long[contentsJson.size()];
            for(int contentCounter = 0; contentCounter<contentsJson.size(); contentCounter++){
                contentId[contentCounter] = contentsJson.get(contentCounter).getAsLong();
            }
            message.setContentId(contentId);
            messages.add(message);
        });
        return messages;
    }
}
