package com.kgaft.securemessengerapp.Activities.ChatActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;


import com.kgaft.securemessengerapp.Activities.ChatActivity.RecyclerSetter.DataAdapter;
import com.kgaft.securemessengerapp.Activities.MainActivity.MainActivity;
import com.kgaft.securemessengerapp.Database.CurrentClientInfo;
import com.kgaft.securemessengerapp.Database.EncryptionKeys;
import com.kgaft.securemessengerapp.Database.MessageDatabase;
import com.kgaft.securemessengerapp.Network.Entities.MessageEntity;
import com.kgaft.securemessengerapp.Network.MessageUtility;
import com.kgaft.securemessengerapp.R;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class ChatActivity extends AppCompatActivity {
    private String receiver;
    private TextView receiverName;
    private TextView receiverLogin;
    private CurrentClientInfo client;
    private MessageUtility messageUtility;
    private MessageDatabase messagesDb;
    private EditText messageText;
    private ImageButton sendButton;
    private RecyclerView messagesRecycler;
    private DataAdapter data;
    private EncryptionKeys keys;
    private Thread messageHandlerThread;
    private ArrayList<MessageEntity> messages = new ArrayList<>();

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        messagesDb = new MessageDatabase(getBaseContext(), null, null, 0);
        keys = new EncryptionKeys(getApplicationContext(), null, null, 0);
        client = new CurrentClientInfo(getApplicationContext(), null, null, 0);
        messageUtility = new MessageUtility(client.getServerAddress(), client.getAppData().getAsString("appId"));

        receiver = getIntent().getStringExtra("receiver");


        receiverName = findViewById(R.id.receiverName);
        receiverLogin = findViewById(R.id.receiverLogin);
        sendButton = findViewById(R.id.sendButton);
        messageText = findViewById(R.id.messageTextInput);
        messagesRecycler = findViewById(R.id.messagesRecycler);
        messagesRecycler.setLayoutManager(new LinearLayoutManager(this));

        receiverLogin.setText(receiver);
        receiverName.setText(messageUtility.getReceiverName(receiver));


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MessageEntity message = new MessageEntity();
                message.setReceiver(receiver);
                message.setMessagetext(messageText.getText().toString());
                try {
                    message.encrypt(keys.getKey(receiver));
                    messageUtility.sendMessage(message);
                    messageText.setText("");
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                }
            }
        });
        data = new DataAdapter(this, messages);
        messagesRecycler.setAdapter(data);
        messageHandlerThread = new Thread(new Runnable() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                        AtomicInteger changes = new AtomicInteger(0);
                        messagesDb.getMessagesByReceiverOrSender(receiver).forEach(element -> {
                            if (!messages.contains(element)) {
                                messages.add(element);
                                changes.getAndIncrement();
                            }
                        });
                        if (changes.get() > 0) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    data.notifyDataSetChanged();
                                    messagesRecycler.smoothScrollToPosition(messages.size());
                                }
                            });
                            changes.set(0);
                        }

                    } catch (Exception e) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                data.notifyDataSetChanged();
                                messagesRecycler.smoothScrollToPosition(messages.size());
                            }
                        });
                    }
                }

            }
        });
        messageHandlerThread.start();
    }

    public void refreshChat() {

    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            messageHandlerThread.stop();
        } catch (Exception e) {

        }

    }

    @Override
    public void onBackPressed() {
        try {
            messageHandlerThread.stop();
        } catch (Exception e) {
        }
        startActivity(new Intent(this, MainActivity.class));
    }

}