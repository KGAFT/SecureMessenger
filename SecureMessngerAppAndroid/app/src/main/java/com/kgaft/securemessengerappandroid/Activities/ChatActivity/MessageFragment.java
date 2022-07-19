package com.kgaft.securemessengerappandroid.Activities.ChatActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kgaft.securemessengerappandroid.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MessageFragment extends Fragment {
    private TextView senderLogin;
    private TextView messageText;
    private String messageSender;
    private String messageTextString;
    private String serverBaseUrl;
    private long[] files;
    private byte[] encryptionKey;
    private long appId;
    private List<Long> addedFiles = new ArrayList<>();
    public MessageFragment(String messageSender, String messageTextString, String serverBaseUrl, long[] files, byte[] encryptionKey, long appId) {
        this.messageSender = messageSender;
        this.messageTextString = messageTextString;
        this.serverBaseUrl = serverBaseUrl;
        this.files = files;
        this.encryptionKey = encryptionKey;
        this.appId = appId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_message, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        senderLogin = view.findViewById(R.id.senderLogin);
        messageText = view.findViewById(R.id.messageText);
        senderLogin.setText(messageSender);
        messageText.setText(messageTextString);
        if(files[0]!=0){
            new Thread(()->{
                while (!checkIfAllFilesAdded()){
                    for (long file : files) {
                        if(file!=0){
                            try{
                                MessageFileFragment filePreview = new MessageFileFragment(file, encryptionKey, serverBaseUrl, appId);
                                getActivity().runOnUiThread(()->{
                                    getChildFragmentManager().beginTransaction().add(R.id.messageFileContainer, filePreview).commitNow();
                                });
                                addedFiles.add(file);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }).start();


        }

    }
    public boolean checkIfAllFilesAdded(){
        boolean added = true;
        for (long file : files) {
            if(!addedFiles.contains(file)){
                added = false;
            }
        }
        return added;
    }
}