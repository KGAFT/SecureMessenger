package com.kgaft.securemessengerapp.Network;

import com.kgaft.securemessengerapp.Network.Entities.ServerResponse;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RegisterNetworkInterface {
    @POST("register")
    Call<ServerResponse> registerUser(@Query("login") String login, @Query("password") String password, @Query("name")String name);
}
