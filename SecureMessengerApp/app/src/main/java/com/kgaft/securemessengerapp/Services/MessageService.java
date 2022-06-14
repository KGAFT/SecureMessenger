package com.kgaft.securemessengerapp.Services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.renderscript.RenderScript;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.kgaft.securemessengerapp.Database.CurrentClientInfo;
import com.kgaft.securemessengerapp.Database.EncryptionKeys;
import com.kgaft.securemessengerapp.Database.MessageDatabase;
import com.kgaft.securemessengerapp.Network.Entities.MessageEntity;
import com.kgaft.securemessengerapp.Network.MessageUtility;
import com.kgaft.securemessengerapp.R;

import java.util.ArrayList;
import java.util.Random;

public class MessageService extends Service {
    private MessageUtility messageUtility;
    private CurrentClientInfo client;
    private EncryptionKeys keys;
    private MessageDatabase messages;
    private long lastTimeOfGetMessages = 0;
    private Context context;
    private Thread thread;
    private String CHANEL = "MessengerApp";
    private ArrayList<MessageEntity> messagesToInsert = new ArrayList<>();


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        client = new CurrentClientInfo(context, null, null, 0);
        messageUtility = new MessageUtility(client.getServerAddress(), client.getAppData().getAsString("appId"));
        keys = new EncryptionKeys(context, null, null, 0);
        messages = new MessageDatabase(context, null, null, 0);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        if (lastTimeOfGetMessages == 0) {
                            ArrayList<String> receivers = keys.getAllReceivers();
                            messageUtility.getMessages().forEach(element -> {
                                if ((receivers.contains(element.getReceiver())) || (receivers.contains(element.getSender()))) {
                                    messages.insertMessage(element);
                                }
                            });
                            lastTimeOfGetMessages = System.currentTimeMillis();
                        } else {
                            ArrayList<String> receivers = keys.getAllReceivers();
                            messageUtility.getMessagesMoreThanTimeStamp(lastTimeOfGetMessages).forEach(element -> {
                                if ((receivers.contains(element.getReceiver())) || (receivers.contains(element.getSender()))) {
                                    messages.insertMessage(element);
                                }
                            });
                            lastTimeOfGetMessages = System.currentTimeMillis();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        thread.stop();
    }
}

