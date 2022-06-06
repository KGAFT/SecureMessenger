package com.kgaft.securemessengerapp.Activities.MainActivity.Fragments;

import android.content.ContentValues;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kgaft.securemessengerapp.Database.CurrentClientInfo;
import com.kgaft.securemessengerapp.Network.Entities.MessageEntity;
import com.kgaft.securemessengerapp.Network.MessageUtility;
import com.kgaft.securemessengerapp.R;

import java.io.IOException;
import java.util.ArrayList;


public class MessagesFragment extends Fragment{
    private ArrayList<MessageEntity> messages;
    private CurrentClientInfo currentClient;
    private FloatingActionButton startNewChatButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_messages, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        currentClient = new CurrentClientInfo(view.getContext(), null, null, 0);
        ContentValues appData = currentClient.getAppData();
        try {
            messages = MessageUtility.getMessages(appData.getAsString("appId"), appData.getAsString("serverAddress"));
            messages.get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}