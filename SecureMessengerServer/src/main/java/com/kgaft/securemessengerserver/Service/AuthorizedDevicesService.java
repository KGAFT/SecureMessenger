package com.kgaft.securemessengerserver.Service;

import com.kgaft.securemessengerserver.DataBase.Entities.UserEntity;

import java.util.ArrayList;
import java.util.HashMap;

public class AuthorizedDevicesService {
    private static HashMap<String, UserEntity> authorizedDevices = new HashMap<>();
    public static boolean authorize(String appId){
        return authorizedDevices.keySet().contains(appId);
    }
    public static void unAuthorize(String appId){
        authorizedDevices.remove(appId);
    }
    public static void addDevice(String appId, UserEntity user){
        authorizedDevices.put(appId, user);
    }
    public static UserEntity getUser(String appId){
        return authorizedDevices.get(appId);
    }
}
