package com.kgaft.securemessengerapp.Activities.MainActivity.Fragments;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.kgaft.securemessengerapp.Activities.MainActivity.MainActivity;
import com.kgaft.securemessengerapp.Database.CurrentClientInfo;
import com.kgaft.securemessengerapp.Database.EncryptionKeys;
import com.kgaft.securemessengerapp.Network.MessageUtility;
import com.kgaft.securemessengerapp.R;
import com.kgaft.securemessengerapp.Utils.KeyUtil;

import java.security.NoSuchAlgorithmException;


public class GenerateKeysFragment extends Fragment implements Runnable{
    private EncryptionKeys keys;
    private String encryptionKey;
    private TextView encryptionKeyOutput;
    private EditText encryptionKeyInput;
    private Button startChatButton;
    private CurrentClientInfo clientInfo;
    private MainActivity activityInstance;
    private Button getBackButton;
    private Thread thread;
    private MessageUtility messageUtility;
    public GenerateKeysFragment(MainActivity instance){
        this.activityInstance = instance;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_generate_keys, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        keys = new EncryptionKeys(view.getContext(), null, null, 0);
        clientInfo = new CurrentClientInfo(view.getContext(), null, null, 0);
        getBackButton = view.findViewById(R.id.getBackButton);
        encryptionKeyOutput = view.findViewById(R.id.encryptionKeyOutput);
        encryptionKeyInput = view.findViewById(R.id.encryptionKeyInput);
        startChatButton = view.findViewById(R.id.startChatButton);
        messageUtility = new MessageUtility(clientInfo.getServerAddress(), clientInfo.getAppData().getAsString("appId"));
        getBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityInstance.getBack();
            }
        });
        try {
            encryptionKey = KeyUtil.byteToString(KeyUtil.generateKey())+"/r"+clientInfo.getAppData().getAsString("userLogin");
            encryptionKeyOutput.setText(encryptionKeyOutput.getText().toString()+encryptionKey);
            thread = new Thread(this::run);
            thread.start();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        startChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String encryptionKeyI = encryptionKeyInput.getText().toString();
                if(encryptionKeyI.split("/r").length>1){
                    if(messageUtility.joinChat(encryptionKeyI.split("/r")[1])){
                        keys.insertKey(encryptionKeyI.split("/r")[0], encryptionKeyI.split("/r")[1]);
                        activityInstance.getBack();
                    }
                }
            }
        });


    }


    public Thread getThread() {
        return thread;
    }

    @Override
    public void run() {
        ContentValues appData = clientInfo.getAppData();
        String receiver = messageUtility.startChat();
        while((receiver==null) || (receiver.equals("Failed"))||(receiver.equals("null"))){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            receiver = messageUtility.startChat();
        }
        keys.insertKey(encryptionKey.split("/r")[0], receiver);
    }


}