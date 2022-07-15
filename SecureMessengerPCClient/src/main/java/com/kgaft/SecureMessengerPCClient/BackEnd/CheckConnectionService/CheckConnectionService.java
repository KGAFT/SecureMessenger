package com.kgaft.SecureMessengerPCClient.BackEnd.CheckConnectionService;

import com.kgaft.SecureMessengerPCClient.BackEnd.AppProperties.AppProperties;
import com.kgaft.SecureMessengerPCClient.BackEnd.Authorization.AuthorizationNativeCalls;
import com.kgaft.SecureMessengerPCClient.BackEnd.ServiceHandler;
import com.kgaft.SecureMessengerPCClient.BackEnd.ServiceInterface;
import com.kgaft.SecureMessengerPCClient.UI.AuthorizationScreen.LoginScreen;
import com.kgaft.SecureMessengerPCClient.UI.ScreenInterface;
import com.kgaft.SecureMessengerPCClient.UI.ScreenManager;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public class CheckConnectionService implements ServiceInterface {
    private AuthorizationNativeCalls authorization;
    private Thread thread;
    public CheckConnectionService() throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if(AppProperties.getServerBaseUrl()==null){
            authorization = new AuthorizationNativeCalls(AppProperties.getServerBaseUrl());
            hideAllScreens();
            showLoginScreen();
            ServiceHandler.pause();
        }

    }
    private void hideAllScreens(){
        ScreenManager.getAllScreens().forEach(ScreenInterface::hideScreen);
    }
    private void showLoginScreen(){
        ScreenManager.getAllScreens().forEach(screen->{
            if(screen.getClass() == LoginScreen.class){
                screen.showScreen();
            }
        });
    }
    @Override
    public void start() {
        thread = new Thread(() -> {
            while(true){
                try{
                    if(authorization==null){
                        authorization = new AuthorizationNativeCalls(AppProperties.getServerBaseUrl());
                    }
                    if(!authorization.checkConnection()){
                        hideAllScreens();
                        showLoginScreen();
                        ServiceHandler.pause();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    hideAllScreens();
                    showLoginScreen();
                    ServiceHandler.pause();
                }

            }
        });
        thread.start();
    }

    @Override
    public void stop() {
        thread.stop();
    }

    @Override
    public void pause() {
        thread.stop();
    }

    @Override
    public void resume() {
        thread.start();
    }
}
