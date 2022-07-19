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
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.kgaft.securemessengerappandroid.Activities.MainActivity.MainActivity;
import com.kgaft.securemessengerappandroid.Database.AppPropertiesTable.AppPropertiesTable;
import com.kgaft.securemessengerappandroid.Database.AppPropertiesTable.AppProperty;
import com.kgaft.securemessengerappandroid.Database.EncryptionKeysTable.EncryptionKey;
import com.kgaft.securemessengerappandroid.Database.EncryptionKeysTable.EncryptionKeysTable;
import com.kgaft.securemessengerappandroid.Database.MessagesTable.MessageEntity;
import com.kgaft.securemessengerappandroid.Database.MessagesTable.MessagesTable;
import com.kgaft.securemessengerappandroid.Files.DownloadedFileManager;
import com.kgaft.securemessengerappandroid.Files.FilesEncryptedNativeCalls;
import com.kgaft.securemessengerappandroid.Files.FilesNativeCalls;
import com.kgaft.securemessengerappandroid.Files.IOUtil;
import com.kgaft.securemessengerappandroid.Network.SecureMessenger;
import com.kgaft.securemessengerappandroid.R;
import com.kgaft.securemessengerappandroid.Services.EncryptionUtil.EncryptionUtil;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.crypto.NoSuchPaddingException;

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
    private MessagesTable messagesTable;
    private Thread messageAdderThread;
    private HashMap<Long, FileToSendPreviewFragment> filesToSend = new HashMap<>();
    private List<Long> addedMessages = new ArrayList<>();
    volatile boolean running = true;
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
        messagesTable = new MessagesTable(getApplicationContext(), null, MessageEntity.class);

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
        initButtons();
        initMessageAdderThread();
        messageAdderThread.start();
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initButtons(){
        attachFileButton.setOnClickListener(event->{
            mGetFile.launch("*/*");
        });
        sendMessageButton.setOnClickListener(event->{
            MessageEntity message = prepareMessageFromFields();
            try {
                message = (MessageEntity) EncryptionUtil.encrypt(message, currentEncryptionKey);
                SecureMessenger messenger = new SecureMessenger(currentUser.getServerBaseUrl());
                messenger.sendMessage(currentUser.getAppId(), message);
                filesToSend.values().forEach(file->{
                    getSupportFragmentManager().beginTransaction().remove(file).commitNow();
                });
                messageTextInput.setText("");
            } catch (NoSuchPaddingException | InvalidKeyException | NoSuchAlgorithmException | IOException e) {
                e.printStackTrace();
            }

        });
    }
    private MessageEntity prepareMessageFromFields(){
        MessageEntity message = new MessageEntity();
        message.setSender(currentUser.getLogin());
        message.setReceiver(receiverLogin);
        message.setMessageText(messageTextInput.getText().toString());
        try{
            Set<Long> contentIdsList = filesToSend.keySet();
            long[] content = new long[contentIdsList.size()];
            int counter = 0;
            if(contentIdsList.size()>0){
                for (Long contentId : contentIdsList) {
                    content[counter] =contentId;
                    counter++;
                }
                message.setContentId(content);
            }
            else{
                message.setContentId(new long[]{0});
            }
        }catch (Exception e){
            message.setContentId(new long[]{0});
        }
        return message;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initMessageAdderThread(){
        messageAdderThread = new Thread(()->{
            while(running){
                try {
                    messagesTable.getMessagesByReceiverOrSender(receiverLogin).forEach(messageLine->{
                        MessageEntity message = (MessageEntity) messageLine;
                        if(!addedMessages.contains(message.getMessageId())){
                            MessageFragment messageFragment = new MessageFragment(message.getSender(), message.getMessageText(), currentUser.getServerBaseUrl(), message.getContentId(), currentEncryptionKey, currentUser.getAppId());
                            runOnUiThread(()->{
                                getSupportFragmentManager().beginTransaction().add(R.id.messagesContainer, messageFragment).commitNow();
                            });
                            addedMessages.add(message.getMessageId());
                        }
                    });
                } catch (Exception e){
                    e.printStackTrace();
                }
            }

        });
    }

    private ActivityResultLauncher mGetFile = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onActivityResult(Uri result) {
            long fileSize = -2;
            try{
                fileSize = IOUtil.getUriFileSize(result, getContentResolver());
            }catch (Exception e){

            }
            if((fileSize>0)&&(fileSize<1024*1024*1024)&&(currentEncryptionKey!=null)){
                try{
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
                                filesToSend.put(fileId, filePreview);
                            });
                        }
                        else{
                            runOnUiThread(()->{
                                getSupportFragmentManager().beginTransaction().remove(filePreview).commitNow();
                            });
                        }
                    }).start();
                }catch (Exception e){

                }

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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onResume() {
        super.onResume();
        if(!running){
            running = true;
            initMessageAdderThread();
            messageAdderThread.start();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        running = false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        running = false;
        startActivity(new Intent(this, MainActivity.class));
    }
}