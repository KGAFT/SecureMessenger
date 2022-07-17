package com.kgaft.SecureMessengerPCClient.UI;

import java.util.ArrayList;
import java.util.List;

public class ScreenManager {
    private static ArrayList<ScreenInterface> screens = new ArrayList<>();
    public static void registerNewScreen(ScreenInterface screen){
        screens.add(screen);
    }
    public static List<ScreenInterface> getAllScreens(){
        return screens;
    }
    public static void hideAllScreens(){
        screens.forEach(ScreenInterface::hideScreen);
    }
    public static void closeAllScreens(){
        screens.forEach(ScreenInterface::closeScreen);
    }
}
