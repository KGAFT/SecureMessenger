package com.kgaft.securemessengerapp.Activities.MainActivity.Fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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

public class ChatPreviewFragment extends Fragment {
    private TextView chatName;
    private ImageView receiverIcon;
    private ImageButton deleteButton;
    private ImageFilterButton startChatButton;
    private String chatNameText;
    private String receiverLogin;
    private Bitmap chatImage;
    private EncryptionKeys keys;
    private MessageDatabase messages;
    private MainActivity instance;
    private ChatsFragment layoutInstance;
    public ChatPreviewFragment(@NonNull String chatName, @NonNull String receiverLogin, @NonNull MainActivity instance, @Nullable Bitmap chatImage){
        this.chatNameText = chatName;
        this.receiverLogin = receiverLogin;
        this.chatImage = chatImage;
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
        initFields(view);
        setValues();
    }
    private void initFields(View view){
        chatName = view.findViewById(R.id.chatName);
        receiverIcon = view.findViewById(R.id.receiverImagePreview);
        deleteButton = view.findViewById(R.id.deleteChatButton);
        startChatButton = view.findViewById(R.id.actionButton);
        keys = new EncryptionKeys(view.getContext(), null, null, 0);
        messages = new MessageDatabase(view.getContext(), null, null, 0);
    }
    private void setValues(){
        chatName.setText(chatNameText);
        if(chatImage!=null){
            receiverIcon.setImageBitmap(chatImage);
        }
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keys.deleteKey(receiverLogin);
                messages.deleteChat(receiverLogin);
                instance.getBack();
            }
        });
        startChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(instance, ChatActivity.class);
                intent.putExtra("receiver", receiverLogin);
                startActivity(intent);
            }
        });
    }
}