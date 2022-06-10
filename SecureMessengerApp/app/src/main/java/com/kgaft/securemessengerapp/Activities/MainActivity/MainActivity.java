package com.kgaft.securemessengerapp.Activities.MainActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.kgaft.securemessengerapp.Activities.MainActivity.Fragments.CameraFragment;
import com.kgaft.securemessengerapp.Activities.MainActivity.Fragments.GenerateKeysFragment;
import com.kgaft.securemessengerapp.Activities.MainActivity.Fragments.MessagesFragment;
import com.kgaft.securemessengerapp.Activities.MainActivity.Fragments.SettingsFragment;
import com.kgaft.securemessengerapp.R;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {
    private MessagesFragment messagesFragment;
    private BottomNavigationView bottomNavigationView;
    private SettingsFragment settingsFragment;
    private GenerateKeysFragment generateKeysFragment;
    private CameraFragment cameraFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        messagesFragment = new MessagesFragment();
        settingsFragment = new SettingsFragment();
        cameraFragment = new CameraFragment();
        generateKeysFragment = new GenerateKeysFragment(this);
        getSupportFragmentManager().beginTransaction().add(R.id.mainActivityContainer, messagesFragment).commitNow();
        bottomNavigationView = findViewById(R.id.bottomNavigationBar);
        bottomNavigationView.setOnItemSelectedListener(this::onNavigationItemSelected);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.messagesButton:
                getBack();
                break;
            case R.id.settingsButton:
                clearActivity();
                getSupportFragmentManager().beginTransaction().add(R.id.mainActivityContainer,settingsFragment).commitNow();
                break;
        }
        return true;
    }
    @Override
    public void onBackPressed(){
        getBack();
    }
    public void ocShowGeneratingKeysFragment(View view) {
        getSupportFragmentManager().beginTransaction().remove(messagesFragment).commitNow();
        try{
            generateKeysFragment.getThread().stop();
        }catch (Exception e){

        }
        generateKeysFragment = new GenerateKeysFragment(this);
        getSupportFragmentManager().beginTransaction().add(R.id.mainActivityContainer, generateKeysFragment).commitNow();
    }
    public void clearActivity(){
        getSupportFragmentManager().beginTransaction().remove(settingsFragment).commitNow();
        getSupportFragmentManager().beginTransaction().remove(messagesFragment).commitNow();
        getSupportFragmentManager().beginTransaction().remove(generateKeysFragment).commitNow();
        getSupportFragmentManager().beginTransaction().remove(cameraFragment).commitNow();
    }
    public void getBack(){
       clearActivity();
        messagesFragment.refreshChats();
    }
    public void ocChangeToCamera(View view) {
        clearActivity();
        getSupportFragmentManager().beginTransaction().add(R.id.mainActivityContainer, cameraFragment).commitNow();
    }
}