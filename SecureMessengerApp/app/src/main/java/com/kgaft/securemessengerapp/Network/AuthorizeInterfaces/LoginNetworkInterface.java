package com.kgaft.securemessengerapp.Network.AuthorizeInterfaces;

import com.kgaft.securemessengerapp.Network.Entities.UserEntity;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LoginNetworkInterface {
    @GET("authorizeClient")
    Call<UserEntity> getUser(@Query("login") String login, @Query("password") String password);
}
