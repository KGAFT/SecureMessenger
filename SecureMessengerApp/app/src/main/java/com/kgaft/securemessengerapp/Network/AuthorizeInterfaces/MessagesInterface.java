package com.kgaft.securemessengerapp.Network.AuthorizeInterfaces;

import com.kgaft.securemessengerapp.Network.Entities.MessageEntity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MessagesInterface {
    @GET("/getMessages")
    Call<ArrayList<MessageEntity>> getMessages(@Query("appId")String appId);
}
