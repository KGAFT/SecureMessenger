package com.kgaft.SecureMessengerPCClient;


import com.kgaft.SecureMessengerPCClient.BackEnd.AppProperties.AppProperties;
import com.kgaft.SecureMessengerPCClient.BackEnd.Authorization.AuthorizationNativeCalls;
import com.kgaft.SecureMessengerPCClient.BackEnd.CheckConnectionService.CheckConnectionService;
import com.kgaft.SecureMessengerPCClient.BackEnd.Files.FilesNativeCalls;
import com.kgaft.SecureMessengerPCClient.BackEnd.ServiceHandler;
import com.kgaft.SecureMessengerPCClient.UI.AuthorizationScreen.LoginScreen;
import com.kgaft.SecureMessengerPCClient.UI.AuthorizationScreen.RegistrationScreen;
import com.kgaft.SecureMessengerPCClient.UI.MainScreen.MainScreen;
import com.kgaft.SecureMessengerPCClient.UI.ScreenManager;

import java.io.File;


public class Main {

    public static void main(String[] args) throws Exception {
        ScreenManager.registerNewScreen(new LoginScreen());
        ScreenManager.registerNewScreen(new RegistrationScreen());
        ScreenManager.registerNewScreen(new MainScreen());
        if(AppProperties.getServerBaseUrl()!=null){
            ServiceHandler.insertService(new CheckConnectionService());
        }
        showMainScreen();
        ServiceHandler.start();

    }
    private static void showLoginScreen(){
        ScreenManager.getAllScreens().forEach(screen->{
            if(screen.getClass() == LoginScreen.class){
                screen.showScreen();
            }
        });
    }
    private static void showMainScreen() {
        ScreenManager.getAllScreens().forEach(screen -> {
            if (screen.getClass() == MainScreen.class) {
                screen.showScreen();
            }
        });
    }
}
