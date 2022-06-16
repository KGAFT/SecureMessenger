package com.kgaft.securemessengerapp.Activities.MainActivity.Fragments;

import android.content.ContentValues;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.kgaft.securemessengerapp.Activities.MainActivity.MainActivity;
import com.kgaft.securemessengerapp.Database.CurrentClientInfo;
import com.kgaft.securemessengerapp.Database.EncryptionKeys;
import com.kgaft.securemessengerapp.Network.MessageUtility;
import com.kgaft.securemessengerapp.R;


public class ChatsFragment extends Fragment {
    private CurrentClientInfo currentClient;
    private EncryptionKeys keys;
    private MainActivity mainActivityInstance;
    private RecyclerView recyclerView;

    public ChatsFragment(MainActivity context) {
        mainActivityInstance = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chats, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        keys = new EncryptionKeys(view.getContext(), null, null, 0);
    }
    public void refreshChats(){
        keys.getAllReceivers().forEach(element->{
            ChatPreviewFragment chatPreview = new ChatPreviewFragment(element, element, mainActivityInstance,null);
            getParentFragmentManager().beginTransaction().add(R.id.chatsContainer, chatPreview).commit();
        });
    }

}