package com.kgaft.SecureMessengerPCClient.BackEnd.MessageService.MessageReceiverManager;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kgaft.SecureMessengerPCClient.BackEnd.MessageService.MessageReceiverManager.Entities.MessageEntity;
import com.kgaft.SecureMessengerPCClient.BackEnd.Utils.IOUtil;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MessageManager {
    private String joinChatUrl;
    private String startChatUrl;
    private String getAllMessagesUrl;
    private String getMessagesByTimeUrl;
    private String sendMessageUrl;
    private String userInfoUrl;
    public MessageManager(String joinChatUrl, String startChatUrl, String getAllMessagesUrl, String getMessagesByTimeUrl, String sendMessageUrl, String userInfo) {
        this.joinChatUrl = joinChatUrl;
        this.startChatUrl = startChatUrl;
        this.getAllMessagesUrl = getAllMessagesUrl;
        this.getMessagesByTimeUrl = getMessagesByTimeUrl;
        this.sendMessageUrl = sendMessageUrl;
        this.userInfoUrl = userInfo;
    }
    public MessageManager(String serverUrl){
        this.joinChatUrl = serverUrl+"joinChat";
        this.startChatUrl = serverUrl+"startChat";
        this.getAllMessagesUrl = serverUrl+"getMessages";
        this.getMessagesByTimeUrl = serverUrl+"getMessagesByTime";
        this.sendMessageUrl = serverUrl+"sendMessage";
        this.userInfoUrl = serverUrl+"getUsername";
    }

    public List<MessageEntity> getAllMessages(long appId) throws IOException {
        HttpURLConnection messageConnection = (HttpURLConnection) new URL(getAllMessagesUrl+"?appId="+appId).openConnection();
        messageConnection.setDoInput(true);
        messageConnection.setRequestMethod("GET");
        messageConnection.connect();
        JsonArray messagesJson = new JsonParser().parse(IOUtil.inputStreamToString(messageConnection.getInputStream())).getAsJsonArray();
        return jsonArrayToMessages(messagesJson);
    }
    public List<MessageEntity> getLastMessagesByTime(long appId, long time) throws IOException{
        HttpURLConnection connection = (HttpURLConnection) new URL(getMessagesByTimeUrl+"?appId="+appId+"&time="+time).openConnection();
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
    public String getJoinChatUrl() {
        return joinChatUrl;
    }

    public void setJoinChatUrl(String joinChatUrl) {
        this.joinChatUrl = joinChatUrl;
    }

    public String getStartChatUrl() {
        return startChatUrl;
    }

    public void setStartChatUrl(String startChatUrl) {
        this.startChatUrl = startChatUrl;
    }

    public String getGetAllMessagesUrl() {
        return getAllMessagesUrl;
    }

    public void setGetAllMessagesUrl(String getAllMessagesUrl) {
        this.getAllMessagesUrl = getAllMessagesUrl;
    }

    public String getGetMessagesByTimeUrl() {
        return getMessagesByTimeUrl;
    }

    public void setGetMessagesByTimeUrl(String getMessagesByTimeUrl) {
        this.getMessagesByTimeUrl = getMessagesByTimeUrl;
    }

    public String getSendMessageUrl() {
        return sendMessageUrl;
    }

    public void setSendMessageUrl(String sendMessageUrl) {
        this.sendMessageUrl = sendMessageUrl;
    }
}
