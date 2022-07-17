package com.kgaft.securemessengerappandroid.Activities.AuthorizeActivity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;

import com.kgaft.securemessengerappandroid.Activities.MainActivity.MainActivity;
import com.kgaft.securemessengerappandroid.Database.AppPropertiesTable.AppPropertiesTable;
import com.kgaft.securemessengerappandroid.Database.AppPropertiesTable.AppProperty;
import com.kgaft.securemessengerappandroid.Network.SecureMessenger;
import com.kgaft.securemessengerappandroid.R;


public class AuthorizeActivity extends AppCompatActivity {
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorize);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            AppPropertiesTable appPropertiesTable = new AppPropertiesTable(getBaseContext(), null, AppProperty.class);
            AppProperty property = (AppProperty) appPropertiesTable.getProperties();
            SecureMessenger messenger = new SecureMessenger(property.getServerBaseUrl());
            if(messenger.checkConnection(property.getAppId())){
                startActivity(new Intent(this, MainActivity.class));
            }
        }catch (Exception ignored){

        }
        getSupportFragmentManager().beginTransaction().add(R.id.fragmentsContainer, new LoginFragment()).commitNow();
    }
}