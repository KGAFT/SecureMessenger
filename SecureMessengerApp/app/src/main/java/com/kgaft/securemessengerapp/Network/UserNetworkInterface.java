package com.kgaft.securemessengerapp.Network;

import com.kgaft.securemessengerapp.Entities.UserEntity;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserNetworkInterface {
    @POST("localhost:8080/authorizeClient")
    Call<UserEntity> getUsers(@Query("login") String login, @Query("password") String password, @Query("appId") long appId);
}
