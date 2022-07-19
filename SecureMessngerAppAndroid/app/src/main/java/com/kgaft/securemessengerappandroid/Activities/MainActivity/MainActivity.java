package com.kgaft.securemessengerappandroid.Activities.MainActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kgaft.securemessengerappandroid.R;
import com.kgaft.securemessengerappandroid.Services.MessageService;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView fragmentsSwitcher;
    private ChatsFragment availableChats;
    private SettingsFragment appSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentsSwitcher = findViewById(R.id.fragmentsSwitcher);
        appSettings = new SettingsFragment();
        availableChats = new ChatsFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.mainActivityFragmentsContainer, availableChats).commitNow();
        fragmentsSwitcher.setOnItemSelectedListener(event->{
            switch (event.getItemId()){
                case R.id.chatsFragmentItem:
                    try{
                        getSupportFragmentManager().beginTransaction().remove(appSettings).commitNow();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    availableChats = new ChatsFragment();
                    getSupportFragmentManager().beginTransaction().add(R.id.mainActivityFragmentsContainer, availableChats).commitNow();
                    break;
                case R.id.settingsFragmentItem:
                    try{
                        getSupportFragmentManager().beginTransaction().remove(availableChats).commitNow();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    appSettings = new SettingsFragment();
                    getSupportFragmentManager().beginTransaction().add(R.id.mainActivityFragmentsContainer, appSettings).commitNow();
                    break;
            }
        return true;
        });
    }

    @Override
    public void onBackPressed() {

    }
}