package com.kgaft.securemessengerappandroid.Activities.AuthorizeActivity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;

import com.kgaft.securemessengerappandroid.Activities.MainActivity.MainActivity;
import com.kgaft.securemessengerappandroid.Database.AppPropertiesTable.AppPropertiesTable;
import com.kgaft.securemessengerappandroid.Database.AppPropertiesTable.AppProperty;
import com.kgaft.securemessengerappandroid.Network.SecureMessenger;
import com.kgaft.securemessengerappandroid.R;
import com.kgaft.securemessengerappandroid.Services.MessageService;


public class AuthorizeActivity extends AppCompatActivity {
    private static final int STORAGE_PERMISSION_CODE = 101;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startService(new Intent(getApplicationContext(), MessageService.class));
        setContentView(R.layout.activity_authorize);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        if (checkPermissionForWriteExtertalStorage()) {
            init();
        } else {
            requestPermissionForWriteExternalStorage();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void init() {
        try {
            AppPropertiesTable appPropertiesTable = new AppPropertiesTable(getBaseContext(), null, AppProperty.class);
            AppProperty property = (AppProperty) appPropertiesTable.getProperties();
            SecureMessenger messenger = new SecureMessenger(property.getServerBaseUrl());
            if (messenger.checkConnection(property.getAppId())) {
                startActivity(new Intent(this, MainActivity.class));
            }
        } catch (Exception ignored) {

        }
        getSupportFragmentManager().beginTransaction().add(R.id.fragmentsContainer, new LoginFragment()).commitNow();
    }

    private boolean checkPermissionForWriteExtertalStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = getApplicationContext().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    private void requestPermissionForWriteExternalStorage() {
        try {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    STORAGE_PERMISSION_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                init();
            } else {
                requestPermissionForWriteExternalStorage();
            }
        } else {
            requestPermissionForWriteExternalStorage();
        }
    }
}