package com.kgaft.securemessengerappandroid.Activities.MainActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kgaft.securemessengerappandroid.R;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView fragmentsSwitcher;
    private ChatsFragment availableChats;
    private SettingsFragment appSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentsSwitcher = findViewById(R.id.fragmentsSwitcher);
        availableChats = new ChatsFragment();
        appSettings = new SettingsFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.mainActivityFragmentsContainer, availableChats).commitNow();
        fragmentsSwitcher.setOnItemSelectedListener(event->{
            switch (event.getItemId()){
                case R.id.chatsFragmentItem:
                    try{
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainActivityFragmentsContainer, availableChats).commitNow();
                        availableChats.refresh();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case R.id.settingsFragmentItem:
                    try{
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainActivityFragmentsContainer, appSettings).commitNow();
                    }catch (Exception ignored){

                    }
                    break;
            }
        return true;
        });
    }
}