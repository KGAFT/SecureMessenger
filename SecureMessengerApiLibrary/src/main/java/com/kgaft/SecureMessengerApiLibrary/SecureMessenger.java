package com.kgaft.SecureMessengerApiLibrary;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author KGAFT
 * @version 1.0
 * Class that helps connect to SecureMessengerServer
 */
public class SecureMessenger {
    private String baseUrl;
    private String login;
    private String password;
    private String name;
    private long appId;
    public SecureMessenger(String baseUrl){
        this.baseUrl = baseUrl;
    }

    /**
     * This method registering a new user, in case when user wil be created successfully, fields: appId, login, password, will be filled automatically
     * @param login
     * @param password
     * @param name
     */
    public boolean register(String login, String password, String name) throws IOException {
        HttpURLConnection connection = prepareConnection("/register","POST", "login="+login+"&password="+password+"&name="+name);
        connection.connect();
        String response = inputStreamToString(connection.getInputStream());
        if(response.contains("Success!")){
            this.login = login;
            this.password = password;
            this.name = name;
            return true;
        }
        return false;
    }
    public boolean register() throws IOException {
        HttpURLConnection connection = prepareConnection("/register","POST", "login="+login+"&password="+password+"&name="+name);
        connection.connect();
        String response = inputStreamToString(connection.getInputStream());
        return response.contains("Success!");
    }
    public boolean sendMessage(MessageEntity message) throws IOException {
        String files = "";
        for (long l : message.getContentId()) {
            files+=l+";";
        }
        HttpURLConnection connection = prepareConnection("/sendMessage", "POST"
                , "appId="+appId+"&receiver="+message.getReceiver()+"&text="+message.getMessagetext()+"&files="+files);
        connection.connect();
        String response = inputStreamToString(connection.getInputStream());
        return Boolean.parseBoolean(parseStringToJsonObject(response).get("response").getAsString());
    }
    public List<MessageEntity> getAllMessages() throws IOException {
        ArrayList<MessageEntity> messagesList = new ArrayList<>();
        HttpURLConnection connection = prepareConnection("/getMessages", "GET", "appId="+appId);
        connection.connect();
        String response = inputStreamToString(connection.getInputStream());
        JsonArray messages = parseStringToJsonArray(response).getAsJsonArray();
        for(int counter = 0; counter<messages.size(); counter++){
            messagesList.add(parseJsonObjectToMessageEntity(messages.get(counter).getAsJsonObject()));
        }
        return messagesList;
    }

    /**
     * This method authorizes user by setted fields: login, password
     * @return result of authorization
     */
    public boolean authorize() throws IOException {
        if((login!=null)&&(password!=null)){
            HttpURLConnection connection = prepareConnection("/authorizeClient", "GET", "login="+login+"&password="+password);
            connection.connect();
            String response = inputStreamToString(connection.getInputStream());
            if(!response.equals("Cannot find user, with this login")){
                JsonObject jsonResponse = parseStringToJsonObject(response);
                name = jsonResponse.get("name").getAsString();
                appId = jsonResponse.get("appId").getAsLong();
                return true;
            }
            return false;
        }
        return false;
    }

    /**
     * This method authorizes user by params that, you qualified
     * @param login
     * @param password
     * @return
     * @throws IOException
     */
    public boolean login(String login, String password) throws IOException {
        HttpURLConnection connection = prepareConnection("/authorizeClient", "GET", "login="+login+"&password="+password);
        connection.connect();
        String response = inputStreamToString(connection.getInputStream());
        if(!response.equals("Cannot find user, with this login")){
            JsonObject jsonResponse = parseStringToJsonObject(response);
            this.name = jsonResponse.get("name").getAsString();
            this.login = jsonResponse.get("login").getAsString();
            this.appId = jsonResponse.get("appId").getAsLong();
            this.password = password;
            return true;
        }
        return false;
    }
    private String inputStreamToString(InputStream stream) throws IOException {
        byte[] response = stream.readAllBytes();
        String stringResponse = new String(response);
        return stringResponse;
    }
    private HttpURLConnection prepareConnection(String page, String requestMethod, String params) throws IOException {
        if(requestMethod.equals("GET")){
            HttpURLConnection connection = (HttpURLConnection) new URL(baseUrl+page+((params==null)||(params.length()>0)?"?"+params:"")).openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod(requestMethod);
            return connection;
        }
        else{
            HttpURLConnection connection = (HttpURLConnection) new URL(baseUrl+page).openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod(requestMethod);
            OutputStream paramsStream = connection.getOutputStream();
            paramsStream.write(params.getBytes(StandardCharsets.UTF_8));
            paramsStream.flush();
            return connection;
        }

    }
    private MessageEntity parseJsonObjectToMessageEntity(JsonObject messageJson){
        MessageEntity message = new MessageEntity();
        message.setReceiver(messageJson.get("receiver").getAsString());
        message.setSender(messageJson.get("sender").getAsString());
        message.setContentId(messageJson.get("messageText").getAsString());
        message.setMessageid(messageJson.get("messageId").getAsLong());
        JsonArray content = messageJson.getAsJsonArray("contentId");
        String contentString = "";
        for(int counter = 0; counter<content.size(); counter++){
            contentString+=content.get(counter).getAsString()+";";
        }
        message.setContentId(contentString);
        return message;
    }
    private JsonObject parseStringToJsonObject(String jsonText){
        return new JsonParser().parse(jsonText).getAsJsonObject();
    }
    private JsonArray parseStringToJsonArray(String jsonText){
        return new JsonParser().parse(jsonText).getAsJsonArray();
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
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
}
