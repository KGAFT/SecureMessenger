package com.kgaft.securemessengerapp.Activities.ChatActivity.Fragments;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kgaft.securemessengerapp.R;



public class MessageSenderFragment extends MessageFragment {
    private String sender;
    private String messageText;
    private Bitmap imageAttachment;
    private TextView messageSenderView;
    private TextView messageTextView;
    private ImageView image;
    public MessageSenderFragment(String sender, String messageText, Bitmap imageAttachment) {

        this.sender = sender;
        this.messageText = messageText;
        this.imageAttachment = imageAttachment;
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
        messageSenderView = view.findViewById(R.id.messageSenderName);
        messageTextView = view.findViewById(R.id.messageText);
        image = view.findViewById(R.id.imagePreview);
        messageSenderView.setText(sender);
        messageTextView.setText(messageText);
        if(imageAttachment!=null){
            image.setImageBitmap(imageAttachment);
        }
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
        messageSenderView.setText(sender);
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
        messageTextView.setText(messageText);
    }

    public Bitmap getImageAttachment() {
        return imageAttachment;
    }

    public void setImageAttachment(Bitmap imageAttachment) {
        this.imageAttachment = imageAttachment;
        image.setImageBitmap(imageAttachment);
    }
}