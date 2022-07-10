package com.kgaft.SecureMessengerPCClient.BackEnd;

import java.util.HashMap;
import java.util.Map;

public class ServiceHandler {
    public static final int SERVICE_WORKING = 1;
    public static final int SERVICE_STOPPED = 0;
    public static final int SERVICE_PAUSED = 2;
    public static final int SERVICE_DOES_NOT_EXISTS = -1;
    private static HashMap<ServiceInterface, Integer> services = new HashMap<>();

    public static void insertService(ServiceInterface service) {
        services.put(service, SERVICE_STOPPED);
    }

    public static void startNotWorkingServices() {
        services.forEach((service, state) -> {
            if (state == SERVICE_STOPPED) {
                service.start();
                services.replace(service, SERVICE_WORKING);
            }
        });
    }
    public static int getServiceState(Class serviceClass){
        try{
            for (Map.Entry<ServiceInterface, Integer> entry : services.entrySet()) {
                if (entry.getKey().getClass() == serviceClass) {
                    return entry.getValue();
                }
            }
            return SERVICE_DOES_NOT_EXISTS;
        }catch (Exception e){
            return SERVICE_DOES_NOT_EXISTS;
        }
    }
    public static void start() {
        services.forEach((service, state) -> {
            service.start();
            services.replace(service, SERVICE_WORKING);

        });
    }

    public static void stop() {
        services.forEach((service, state) -> {
            if(state == SERVICE_WORKING){
                service.stop();
                services.replace(service, SERVICE_STOPPED);
            }
        });
    }

    public static void pause() {
        services.forEach((service, state) -> {
            if (state == SERVICE_WORKING) {
                service.pause();
                services.replace(service, SERVICE_PAUSED);
            }
        });
    }

    public static void resume() {
        services.forEach((service, state) -> {
            if (state == SERVICE_PAUSED) {
                service.resume();
                services.replace(service, SERVICE_WORKING);
            }

        });
    }
}
