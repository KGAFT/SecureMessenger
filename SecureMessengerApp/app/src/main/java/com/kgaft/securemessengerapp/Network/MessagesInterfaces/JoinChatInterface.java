package com.kgaft.securemessengerapp.Network.MessagesInterfaces;

import com.kgaft.securemessengerapp.Network.Entities.MessageEntity;
import com.kgaft.securemessengerapp.Network.Entities.ServerResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface JoinChatInterface {
    @POST("/joinChat")
    Call<ServerResponse> getMessages(@Query("appId")String appId, @Query("receiver")String receiver);
}
