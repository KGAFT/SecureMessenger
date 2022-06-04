package com.kgaft.securemessengerapp.Activities.MainActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.kgaft.securemessengerapp.Activities.MainActivity.Fragments.MessagesFragment;
import com.kgaft.securemessengerapp.Activities.MainActivity.Fragments.SettingsFragment;
import com.kgaft.securemessengerapp.R;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {
    private MessagesFragment messagesFragment;
    private BottomNavigationView bottomNavigationView;
    private SettingsFragment settingsFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        messagesFragment = new MessagesFragment();
        settingsFragment = new SettingsFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.mainActivityContainer, messagesFragment).commitNow();
        bottomNavigationView = findViewById(R.id.bottomNavigationBar);
        bottomNavigationView.setOnItemSelectedListener(this::onNavigationItemSelected);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.messagesButton:
                getSupportFragmentManager().beginTransaction().remove(settingsFragment).commitNow();
                getSupportFragmentManager().beginTransaction().remove(messagesFragment).commitNow();
                getSupportFragmentManager().beginTransaction().add(R.id.mainActivityContainer, messagesFragment).commitNow();
                break;
            case R.id.settingsButton:
                getSupportFragmentManager().beginTransaction().remove(messagesFragment).commitNow();
                getSupportFragmentManager().beginTransaction().remove(settingsFragment).commitNow();
                getSupportFragmentManager().beginTransaction().add(R.id.mainActivityContainer,settingsFragment).commitNow();
                break;
        }
        return true;
    }
}