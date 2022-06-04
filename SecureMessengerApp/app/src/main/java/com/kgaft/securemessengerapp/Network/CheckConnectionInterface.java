package com.kgaft.securemessengerapp.Network;

import com.kgaft.securemessengerapp.Network.Entities.ServerResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CheckConnectionInterface {
    @GET("/checkConnection")
    Call<ServerResponse> checkConnection(@Query("appId") String appId);
}
