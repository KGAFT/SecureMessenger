package com.kgaft.securemessengerapp.Network.AuthorizeInterfaces;

import com.kgaft.securemessengerapp.Network.Entities.ServerResponse;
import com.kgaft.securemessengerapp.Network.Entities.UserEntity;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface StartChatInterface {
    @GET("startChat")
    Call<ServerResponse> getResponse(@Query("appId")String appId);
}
