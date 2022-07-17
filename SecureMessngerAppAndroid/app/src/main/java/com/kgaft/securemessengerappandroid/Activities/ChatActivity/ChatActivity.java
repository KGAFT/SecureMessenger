package com.kgaft.securemessengerappandroid.Activities.ChatActivity;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.kgaft.securemessengerappandroid.Database.AppPropertiesTable.AppPropertiesTable;
import com.kgaft.securemessengerappandroid.Database.AppPropertiesTable.AppProperty;
import com.kgaft.securemessengerappandroid.Database.EncryptionKeysTable.EncryptionKey;
import com.kgaft.securemessengerappandroid.Database.EncryptionKeysTable.EncryptionKeysTable;
import com.kgaft.securemessengerappandroid.Files.DownloadedFileManager;
import com.kgaft.securemessengerappandroid.Files.FilesEncryptedNativeCalls;
import com.kgaft.securemessengerappandroid.Files.FilesNativeCalls;
import com.kgaft.securemessengerappandroid.Files.IOUtil;
import com.kgaft.securemessengerappandroid.Network.SecureMessenger;
import com.kgaft.securemessengerappandroid.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private String receiverLogin;
    private TextView receiverLoginLayout;
    private TextView receiverName;
    private ImageView receiverIcon;
    private EditText messageTextInput;
    private ImageButton attachFileButton;
    private ImageButton sendMessageButton;
    private AppProperty currentUser;
    private byte[] currentEncryptionKey;
    private List<Long> filesToSend = new ArrayList<>();
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        receiverLogin = getIntent().getStringExtra("receiverLogin");
        receiverLoginLayout = findViewById(R.id.receiverLogin);
        receiverName = findViewById(R.id.receiverName);
        receiverIcon = findViewById(R.id.receiverIcon);
        messageTextInput = findViewById(R.id.messageTextInput);
        attachFileButton = findViewById(R.id.addFileButton);
        sendMessageButton = findViewById(R.id.sendButton);
        try {
            currentEncryptionKey =((EncryptionKey) new EncryptionKeysTable(getApplicationContext(), null, EncryptionKey.class).getKey(receiverLogin)).getEncryptionKey();
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        try {
            currentUser = (AppProperty) new AppPropertiesTable(getApplicationContext(), null, AppProperty.class).getProperties();
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        startAddInfoThread();
        attachFileButton.setOnClickListener(event->{
            mGetFile.launch("*/*");
        });
    }
    private ActivityResultLauncher mGetFile = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onActivityResult(Uri result) {
            long fileSize = IOUtil.getUriFileSize(result, getContentResolver());
            if((fileSize>0)&&(fileSize<1024*1024*1024)&&(currentEncryptionKey!=null)){
                FileToSendPreviewFragment filePreview = new FileToSendPreviewFragment();
                getSupportFragmentManager().beginTransaction().add(R.id.uploadedFilesContainer, filePreview).commitNow();
                filePreview.getFileName().setText(IOUtil.getUriFileName(result, getContentResolver()));
                new Thread(()->{
                    File fileToSend = IOUtil.transferUriToCacheDir(result, getContentResolver(), getCacheDir().getAbsolutePath());
                    FilesEncryptedNativeCalls fileSender = new FilesEncryptedNativeCalls(currentUser.getServerBaseUrl());
                    long fileId = fileSender.uploadFile(fileToSend, currentEncryptionKey, currentUser.getAppId());
                    if(fileId!=0){
                        runOnUiThread(()->{
                            filePreview.getUploadingProgressBar().setVisibility(View.INVISIBLE);
                            filePreview.getDeleteButton().setVisibility(View.VISIBLE);
                            filePreview.getDeleteButton().setOnClickListener(event->{
                                getSupportFragmentManager().beginTransaction().remove(filePreview).commitNow();
                            });
                            filesToSend.add(fileId);
                        });
                    }
                    else{
                        runOnUiThread(()->{
                            getSupportFragmentManager().beginTransaction().remove(filePreview).commitNow();
                        });
                    }
                }).start();
            }
        }
    });
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void startAddInfoThread() {
        new Thread(() -> {
            SecureMessenger messenger = new SecureMessenger(currentUser.getServerBaseUrl());
            try {
                String receiverName = messenger.getUserName(currentUser.getAppId(), receiverLogin);
                File receiverIcon = DownloadedFileManager.getFile(getApplicationContext(), receiverLogin, new FilesNativeCalls(currentUser.getServerBaseUrl()), getCacheDir().getAbsolutePath());
                runOnUiThread(() -> {
                    receiverLoginLayout.setText(receiverLogin);
                    this.receiverName.setText(receiverName);
                    if (receiverIcon != null) {
                        this.receiverIcon.setImageBitmap(BitmapFactory.decodeFile(receiverIcon.getAbsolutePath()));
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}