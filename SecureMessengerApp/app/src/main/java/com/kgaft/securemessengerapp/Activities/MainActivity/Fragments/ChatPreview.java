package com.kgaft.securemessengerapp.Activities.MainActivity.Fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.utils.widget.ImageFilterButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.kgaft.securemessengerapp.Activities.ChatActivity.ChatActivity;
import com.kgaft.securemessengerapp.Activities.MainActivity.MainActivity;
import com.kgaft.securemessengerapp.Database.EncryptionKeys;
import com.kgaft.securemessengerapp.Database.MessageDatabase;
import com.kgaft.securemessengerapp.R;


public class ChatPreview extends Fragment {
    private String chatName;
    private Bitmap imagePreview;
    private TextView chatNameView;
    private ImageView imageView;
    private ImageFilterButton actionButton;
    private ImageButton deleteButton;
    private MainActivity mainActivityInstance;
    private EncryptionKeys keys;
    private MessageDatabase messages;
    private String receiverLogin;
    private MessagesFragment instance;
    public ChatPreview(MainActivity context, String chatName, Bitmap imagePreview, String receiverLogin, @Nullable MessagesFragment instance){
        this.chatName = chatName;
        this.imagePreview = imagePreview;
        this.receiverLogin = receiverLogin;
        mainActivityInstance = context;
        this.instance = instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat_preview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        chatNameView=view.findViewById(R.id.chatName);
        imageView = view.findViewById(R.id.imagePreview);
        actionButton = view.findViewById(R.id.actionButton);
        deleteButton = view.findViewById(R.id.deleteChatButton);
        keys = new EncryptionKeys(view.getContext(), null, null, 0);
        messages = new MessageDatabase(view.getContext(), null, null, 0);
        imageView.setImageBitmap(imagePreview);
        chatNameView.setText(chatName);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(mainActivityInstance, ChatActivity.class);
               intent.putExtra("receiver", receiverLogin);
               startActivity(intent);
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keys.deleteKey(receiverLogin);
                messages.deleteChat(receiverLogin);
                if(instance!=null){
                    instance.refreshChats();
                }
            }
        });
    }
    public void setChatIcon(Bitmap image){
        imagePreview = image;
        imageView.setImageBitmap(imagePreview);
    }
    public void setChatName(String name){
        chatName = name;
        chatNameView.setText(chatName);
    }

    public String getChatName() {
        return chatName;
    }

    public Bitmap getImagePreview() {
        return imagePreview;
    }
}