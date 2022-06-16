package com.kgaft.securemessengerapp.Activities.MainActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.kgaft.securemessengerapp.Activities.MainActivity.Fragments.GenerateKeysFragment;
import com.kgaft.securemessengerapp.Activities.MainActivity.Fragments.ChatsFragment;
import com.kgaft.securemessengerapp.Activities.MainActivity.Fragments.SettingsFragment;
import com.kgaft.securemessengerapp.Database.EncryptionKeys;
import com.kgaft.securemessengerapp.R;
import com.kgaft.securemessengerapp.Services.MessageService;


public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {
    private ChatsFragment chatsFragment;
    private BottomNavigationView bottomNavigationView;
    private SettingsFragment settingsFragment;
    private GenerateKeysFragment generateKeysFragment;

    private RecyclerView recyclerView;
    private EncryptionKeys keys;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFields();
        startService(new Intent(this, MessageService.class));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.messagesButton:
                getBack();
                break;
            case R.id.settingsButton:
                clearActivity();
                getSupportFragmentManager().beginTransaction().add(R.id.mainActivityContainer, settingsFragment).commitNow();
                break;
        }
        return true;
    }

    @SuppressLint("WrongViewCast")
    private void initFields() {
        chatsFragment = new ChatsFragment(this);
        settingsFragment = new SettingsFragment();
        generateKeysFragment = new GenerateKeysFragment(this);
        keys = new EncryptionKeys(this, null, null, 0);
        getSupportFragmentManager().beginTransaction().add(R.id.mainActivityContainer, chatsFragment).commitNow();
        bottomNavigationView = findViewById(R.id.bottomNavigationBar);
        bottomNavigationView.setOnItemSelectedListener(this::onNavigationItemSelected);

    }

    @Override
    public void onBackPressed() {
        getBack();
    }

    public void ocShowGeneratingKeysFragment(View view) {
        getSupportFragmentManager().beginTransaction().remove(chatsFragment).commitNow();
        try {
            generateKeysFragment.getThread().stop();
        } catch (Exception e) {

        }
        generateKeysFragment = new GenerateKeysFragment(this);
        getSupportFragmentManager().beginTransaction().add(R.id.mainActivityContainer, generateKeysFragment).commitNow();
    }

    private void clearActivity() {
        getSupportFragmentManager().beginTransaction().remove(settingsFragment).commitNow();
        getSupportFragmentManager().beginTransaction().remove(chatsFragment).commitNow();
        getSupportFragmentManager().beginTransaction().remove(generateKeysFragment).commitNow();
    }

    public void getBack() {
        clearActivity();
        getSupportFragmentManager().beginTransaction().add(R.id.mainActivityContainer, chatsFragment).commitNow();
        chatsFragment.refreshChats();
    }

}