package com.kgaft.securemessengerapp.Network;

import com.kgaft.securemessengerapp.Network.Entities.UserEntity;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LoginNetworkInterface {
    @POST("authorizeClient")
    Call<UserEntity> getUser(@Query("login") String login, @Query("password") String password);
}
