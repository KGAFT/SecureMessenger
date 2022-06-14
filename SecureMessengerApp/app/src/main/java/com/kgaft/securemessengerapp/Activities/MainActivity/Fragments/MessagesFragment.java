package com.kgaft.securemessengerapp.Activities.MainActivity.Fragments;

import android.content.ContentValues;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kgaft.securemessengerapp.Activities.MainActivity.MainActivity;
import com.kgaft.securemessengerapp.Database.CurrentClientInfo;
import com.kgaft.securemessengerapp.Database.EncryptionKeys;
import com.kgaft.securemessengerapp.Network.Entities.MessageEntity;
import com.kgaft.securemessengerapp.Network.MessageUtility;
import com.kgaft.securemessengerapp.R;

import java.io.IOException;
import java.util.ArrayList;


public class MessagesFragment extends Fragment{
    private CurrentClientInfo currentClient;
    private EncryptionKeys keys;
    private ArrayList<ChatPreview> chatFragments;
    private MainActivity mainActivityInstance;
    private FloatingActionButton startNewChatButton;
    private MessageUtility messageUtility;
    public MessagesFragment(MainActivity context){
        mainActivityInstance = context;
    }
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
        keys = new EncryptionKeys(view.getContext(), null, null, 0);
        messageUtility = new MessageUtility(currentClient.getServerAddress(), currentClient.getAppData().getAsString("appId"));
    }

    public void refreshChats(){
        chatFragments = new ArrayList<>();
        String serverAddress = currentClient.getServerAddress();
        String appId = currentClient.getAppData().getAsString("appId");
            keys.getAllReceivers().forEach(element->{
                ChatPreview chat = new ChatPreview(mainActivityInstance, messageUtility.getReceiverName(element), null, element, this);
                chatFragments.add(chat);
                getParentFragmentManager().beginTransaction().add(R.id.chatContainer, chat).commitNow();
            });

    }


}