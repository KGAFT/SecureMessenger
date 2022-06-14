package com.kgaft.securemessengerapp.Database;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.kgaft.securemessengerapp.Activities.MainActivity.MainActivity;
import com.kgaft.securemessengerapp.Network.Entities.MessageEntity;
import com.kgaft.securemessengerapp.R;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class MessageDatabase extends SQLiteOpenHelper {
    private static final String TABLE_NAME="Messages";
    private static final int TABLE_VERSION=1;
    private EncryptionKeys keys;
    private CurrentClientInfo client;
    private Context context;
    public MessageDatabase(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, TABLE_NAME, null, TABLE_VERSION);
        keys = new EncryptionKeys(context, null, null, 0);
        client = new CurrentClientInfo(context, null, null, 0);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE "+TABLE_NAME+"(" +
                "messageId BIGINT," +
                "sender TEXT,"+
                "receiver TEXT," +
                "messageText TEXT," +
                "files TEXT," +
                "time BIGINT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public void insertMessage(MessageEntity message){
        SQLiteDatabase db = getWritableDatabase();
        try {
            if(!isMessageExists(message.getMessageid())){
                try{
                    message.decrypt(keys.getKey(message.getReceiver()));
                }catch (Exception e){
                    message.decrypt(keys.getKey(message.getSender()));
                }
                sendNotification(message);
                ContentValues parsedMessage = new ContentValues();
                parsedMessage.put("messageId", message.getMessageid());
                parsedMessage.put("receiver", message.getReceiver());
                parsedMessage.put("messageText", message.getMessagetext());
                parsedMessage.put("sender", message.getSender());
                parsedMessage.put("time", message.getTime());
                String files = "";
                if(message.getContentid()!=null){
                    for (long l : message.getContentid()) {
                        files+=String.valueOf(l)+";";
                    }
                }
                parsedMessage.put("files", files);
                db.insert(TABLE_NAME, null, parsedMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @SuppressLint("Range")
    public ArrayList<MessageEntity> getMessagesByReceiverOrSender(String receiverOrSender){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<MessageEntity> messages = new ArrayList<>();
        Cursor data = db.query(TABLE_NAME, new String[]{"messageId", "sender","receiver", "messageText", "files", "time", "files"}, "sender = ? OR receiver = ?", new String[]{receiverOrSender, receiverOrSender}, null, null, "time ASC");
        if(data.moveToFirst()){
            do{
                MessageEntity message = new MessageEntity();
                message.setMessageid(data.getLong(data.getColumnIndex("messageId")));
                message.setMessagetext(data.getString(data.getColumnIndex("messageText")));
                message.setReceiver(data.getString(data.getColumnIndex("receiver")));
                message.setTime(data.getLong(data.getColumnIndex("time")));
                message.setSender(data.getString(data.getColumnIndex("sender")));
                String contentStr = data.getString(data.getColumnIndex("files"));
                long[] content = new long[contentStr.split(";").length];
                for(int counter = 0; counter<content.length; counter++){
                    content[counter] = Long.parseLong(contentStr.split(";")[counter]);
                }
                if(content[0]==0){
                    message.setContentid(null);
                }
                else{
                    message.setContentid(content);
                }
                messages.add(message);
            }while(data.moveToNext());
        }

        return messages;
    }
    public void deleteChat(String receiverLogin){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, "receiver = ?", new String[]{receiverLogin});
        db.delete(TABLE_NAME, "sender = ?", new String[]{receiverLogin});
    }
    @SuppressLint("Range")
    public boolean isMessageExists(long messageId){
        SQLiteDatabase db = getReadableDatabase();
        MessageEntity message = new MessageEntity();
        Cursor data = db.query(TABLE_NAME, new String[]{"messageId"}, "messageId = ?", new String[]{String.valueOf(messageId)}, null, null, null);
        data.moveToFirst();
        try{
            if(data.getLong(data.getColumnIndex("messageId"))!=0){
                return true;
            }
            else{
                return false;
            }
        }catch (Exception e){
            return false;
        }
    }
    private void sendNotification(MessageEntity message){
        if(!message.getSender().equals(client.getAppData().getAsString("userLogin"))){
            NotificationManager mNotificationManager;  //Creating notifier manager

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context, "messengerApp"); //Create builder of notifications
            Intent ii = new Intent(context, MainActivity.class); //Creating intent of mainActivity
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, ii, 0); //Creating pending intent for mainActivity intent

            NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
            bigText.bigText("New message from: "+message.getSender());
            bigText.setBigContentTitle("Message!");
            bigText.setSummaryText("New message from: "+message.getSender());

            mBuilder.setContentIntent(pendingIntent);
            mBuilder.setSmallIcon(R.drawable.messenger_icon);
            mBuilder.setContentTitle("Messenger App");
            mBuilder.setContentText(message.getMessagetext());
            mBuilder.setPriority(Notification.PRIORITY_MAX);
            mBuilder.setStyle(bigText);

            mNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel("messengerApp", "messengerApp", NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(channel);

            mNotificationManager.notify(new Random().nextInt(1000), mBuilder.build());
        }

    }
}
