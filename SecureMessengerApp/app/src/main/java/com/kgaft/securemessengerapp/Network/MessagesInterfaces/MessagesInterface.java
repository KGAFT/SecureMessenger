package com.kgaft.securemessengerapp.Network.MessagesInterfaces;

import com.kgaft.securemessengerapp.Network.Entities.MessageEntity;
import com.kgaft.securemessengerapp.Network.Entities.ServerResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface MessagesInterface {
    @GET("/getMessages")
    Call<ArrayList<MessageEntity>> getMessages(@Query("appId")String appId);
    @GET("/getMessagesMoreThanTimeStamp")
    Call<ArrayList<MessageEntity>> getMessagesByTimestamp(@Query("appId")String appId, @Query("timeInMilliseconds") long time);
    @GET("/getUserName")
    Call<ServerResponse> getUserName(@Query("appId") String appId, @Query("userLogin") String login);
    @POST("/sendMessage")
    Call<ServerResponse> sendMessage(@Query("appId") String appId, @Query("receiver") String receiver, @Query("text") String text, @Query("files") String files);
}
