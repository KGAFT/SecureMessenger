package com.kgaft.securemessengerapp.Activities.MainActivity.Fragments;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.utils.widget.ImageFilterButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kgaft.securemessengerapp.R;


public class ChatPreview extends Fragment {
    private String chatName;
    private Bitmap imagePreview;
    private TextView chatNameView;
    private ImageView imageView;
    private ImageFilterButton actionButton;
    private String receiverName;
    public ChatPreview(String chatName, Bitmap imagePreview, String receiverName){
        this.chatName = chatName;
        this.imagePreview = imagePreview;
        this.receiverName = receiverName;
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
        imageView.setImageBitmap(imagePreview);
        chatNameView.setText(chatName);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Chat activated!");
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