package com.kgaft.SecureMessengerPCClient;


import com.kgaft.SecureMessengerPCClient.BackEnd.AppProperties.AppProperties;
import com.kgaft.SecureMessengerPCClient.BackEnd.Authorization.AuthorizationNativeCalls;
import com.kgaft.SecureMessengerPCClient.BackEnd.CheckConnectionService.CheckConnectionService;
import com.kgaft.SecureMessengerPCClient.BackEnd.Files.FilesNativeCalls;
import com.kgaft.SecureMessengerPCClient.BackEnd.MessageService.MessageReceiverManager.MessageManager;
import com.kgaft.SecureMessengerPCClient.BackEnd.MessageService.MessageReceiverService;
import com.kgaft.SecureMessengerPCClient.BackEnd.ServiceHandler;
import com.kgaft.SecureMessengerPCClient.UI.AuthorizationScreen.LoginScreen;
import com.kgaft.SecureMessengerPCClient.UI.AuthorizationScreen.RegistrationScreen;
import com.kgaft.SecureMessengerPCClient.UI.MainScreen.MainScreen;
import com.kgaft.SecureMessengerPCClient.UI.ScreenManager;
import com.kgaft.SecureMessengerPCClient.UI.StartChatScreen.StartChatScreen;

import java.io.File;


public class Main {

    public static void main(String[] args) throws Exception {
        ScreenManager.registerNewScreen(new LoginScreen());
        ScreenManager.registerNewScreen(new RegistrationScreen());
        ScreenManager.registerNewScreen(new MainScreen());
        ScreenManager.registerNewScreen(new StartChatScreen());
        if(AppProperties.getServerBaseUrl()!=null){
            ServiceHandler.insertService(new CheckConnectionService());
            ServiceHandler.insertService(new MessageReceiverService(new MessageManager(AppProperties.getServerBaseUrl()), new AuthorizationNativeCalls(AppProperties.getServerBaseUrl())));
            showMainScreen();
            ServiceHandler.start();
        }
        else{
            showLoginScreen();
        }



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
