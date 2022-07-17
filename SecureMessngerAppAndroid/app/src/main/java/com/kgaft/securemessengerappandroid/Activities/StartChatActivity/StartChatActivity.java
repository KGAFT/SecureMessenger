package com.kgaft.securemessengerappandroid.Activities.StartChatActivity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kgaft.securemessengerappandroid.Activities.MainActivity.MainActivity;
import com.kgaft.securemessengerappandroid.Database.AppPropertiesTable.AppPropertiesTable;
import com.kgaft.securemessengerappandroid.Database.AppPropertiesTable.AppProperty;
import com.kgaft.securemessengerappandroid.Database.EncryptionKeysTable.EncryptionKey;
import com.kgaft.securemessengerappandroid.Database.EncryptionKeysTable.EncryptionKeysTable;
import com.kgaft.securemessengerappandroid.Network.SecureMessenger;
import com.kgaft.securemessengerappandroid.R;
import com.kgaft.securemessengerappandroid.Services.EncryptionUtil.EncryptionUtil;

import java.security.NoSuchAlgorithmException;

public class StartChatActivity extends AppCompatActivity {
    private Thread waitingToJoinThread;
    volatile boolean running = true;
    private EditText encryptionKeyInput;
    private TextView encryptionKeyOut;
    private Button saveEncryptionKey;
    private byte[] generatedKey;
    private AppProperty currentAppUser;
    private EncryptionKeysTable keys;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_chat);
        keys = new EncryptionKeysTable(getApplicationContext(),
                null, EncryptionKey.class);
        try {
            currentAppUser = ((AppProperty) new AppPropertiesTable(getApplicationContext(), null, AppProperty.class).getProperties());
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        try {
            generatedKey = EncryptionUtil.generateEncryptionKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        saveEncryptionKey = findViewById(R.id.saveButton);
        encryptionKeyOut = findViewById(R.id.encryptionKeyOut);
        encryptionKeyInput = findViewById(R.id.encryptionKeyInput);
        waitingToJoinThread = new Thread(() -> {

            SecureMessenger messenger = new SecureMessenger(currentAppUser.getServerBaseUrl());
            while (running) {
                try {
                    String receiver = messenger.startChat(currentAppUser.getAppId());
                    if (!receiver.equals("Error")) {
                        keys.insertKey(new EncryptionKey(receiver, generatedKey));
                        break;
                    }
                } catch (Exception e) {

                }
            }
        });
        waitingToJoinThread.start();
        encryptionKeyOut.setText(EncryptionUtil.byteArrayToString(generatedKey) + "/r" + currentAppUser.getLogin());
        saveEncryptionKey.setOnClickListener(event -> {
            String[] encryptionKeyUser = encryptionKeyInput.getText().toString().split("/r");
            byte[] receivedEncryptionKey = EncryptionUtil.encryptedStringToByteArray(encryptionKeyUser[0]);
            String receiver = encryptionKeyUser[1];
            try {
                SecureMessenger messenger = new SecureMessenger(currentAppUser.getServerBaseUrl());
                if (messenger.joinChat(currentAppUser.getAppId(), receiver)) {
                    keys.insertKey(new EncryptionKey(receiver, receivedEncryptionKey));
                    running = false;
                    waitingToJoinThread.stop();
                    startActivity(new Intent(this, MainActivity.class));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {

    }
}