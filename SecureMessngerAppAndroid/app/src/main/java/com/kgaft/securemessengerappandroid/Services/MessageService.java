package com.kgaft.securemessengerappandroid.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.kgaft.securemessengerappandroid.Database.AppPropertiesTable.AppPropertiesTable;
import com.kgaft.securemessengerappandroid.Database.AppPropertiesTable.AppProperty;
import com.kgaft.securemessengerappandroid.Database.EncryptionKeysTable.EncryptionKey;
import com.kgaft.securemessengerappandroid.Database.EncryptionKeysTable.EncryptionKeysTable;
import com.kgaft.securemessengerappandroid.Database.MessagesTable.MessageEntity;
import com.kgaft.securemessengerappandroid.Database.MessagesTable.MessagesTable;
import com.kgaft.securemessengerappandroid.Activities.AuthorizeActivity.AuthorizeActivity;
import com.kgaft.securemessengerappandroid.Network.SecureMessenger;
import com.kgaft.securemessengerappandroid.R;
import com.kgaft.securemessengerappandroid.Services.EncryptionUtil.EncryptionUtil;

import java.util.List;
import java.util.Random;

public class MessageService extends Service {
    volatile boolean running = false;
    private Thread messageThread;

    public MessageService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();
        messageThread = new Thread(() -> {
            AppPropertiesTable propertiesTable = new AppPropertiesTable(getApplicationContext(), null, AppProperty.class);
            EncryptionKeysTable keys = new EncryptionKeysTable(getApplicationContext(), null, EncryptionKey.class);
            MessagesTable messagesTable = new MessagesTable(getApplicationContext(), null, MessageEntity.class);
            long lastTimeGettingMessages = 0;
            while (running) {
                try {
                    AppProperty property = (AppProperty) propertiesTable.getProperties();
                    SecureMessenger messenger = new SecureMessenger(property.getServerBaseUrl());
                    if (messenger.checkConnection(property.getAppId())) {
                        List<MessageEntity> newMessages;
                        if (lastTimeGettingMessages == 0) {
                            newMessages = messenger.getAllMessages(property.getAppId());
                        } else {
                            newMessages = messenger.getLastMessagesByTime(property.getAppId(), lastTimeGettingMessages-10000);
                        }
                        for (MessageEntity message : newMessages) {
                            EncryptionKey messageKey;
                            try {
                                messageKey = (EncryptionKey) keys.getKey(message.getReceiver());
                            } catch (Exception e) {
                                try {
                                    messageKey = (EncryptionKey) keys.getKey(message.getSender());
                                } catch (Exception ignored) {
                                    messageKey = null;
                                }
                            }
                            if (messageKey != null) {
                                try {
                                    MessageEntity messageToInsert = (MessageEntity) EncryptionUtil.decrypt(message, messageKey.getEncryptionKey());
                                    if (messagesTable.insertMessage(messageToInsert)) {
                                        sendNotify(messageToInsert.getMessageText());
                                    }
                                } catch (Exception ignored) {

                                }
                            }
                        }
                        lastTimeGettingMessages = System.currentTimeMillis();
                    }
                }
            catch(Exception ignored){

            }
        }
    });
}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        messageThread.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendNotify(String text) {
        String notification;
        NotificationManager mNotificationManager;  //Creating notifier manager

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getApplicationContext(), "notify_001"); //Create builder of notifications
        Intent ii = new Intent(getApplicationContext(), AuthorizeActivity.class); //Creating intent of mainActivity
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, ii, 0); //Creating pending intent for mainActivity intent

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText(text);
        bigText.setBigContentTitle("New message");
        bigText.setSummaryText(text);

        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher_round);
        mBuilder.setContentTitle(text);
        mBuilder.setContentText(text);
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setStyle(bigText);

        mNotificationManager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

// === Removed some obsoletes
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "MessagesChannel";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Secure messenger",
                    NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }
        Random rand = new Random();
        int id = rand.nextInt(1000);
        mNotificationManager.notify(id, mBuilder.build());
    }
}