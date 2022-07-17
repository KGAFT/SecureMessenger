package com.kgaft.securemessengerappandroid.Activities.MainActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.kgaft.securemessengerappandroid.Activities.ChatActivity.ChatActivity;
import com.kgaft.securemessengerappandroid.Database.AppPropertiesTable.AppPropertiesTable;
import com.kgaft.securemessengerappandroid.Database.AppPropertiesTable.AppProperty;
import com.kgaft.securemessengerappandroid.Database.EncryptionKeysTable.EncryptionKey;
import com.kgaft.securemessengerappandroid.Database.EncryptionKeysTable.EncryptionKeysTable;
import com.kgaft.securemessengerappandroid.Database.MessagesTable.MessageEntity;
import com.kgaft.securemessengerappandroid.Database.MessagesTable.MessagesTable;
import com.kgaft.securemessengerappandroid.Files.DownloadedFileManager;
import com.kgaft.securemessengerappandroid.Files.FilesNativeCalls;
import com.kgaft.securemessengerappandroid.Network.SecureMessenger;
import com.kgaft.securemessengerappandroid.R;

import java.io.File;
import java.io.IOException;

public class ChatPreviewFragment extends Fragment {

    private ImageView receiverImage;
    private TextView receiverLogin;
    private TextView receiverName;
    private ImageButton actionButton;
    private String receiverLoginString;
    private ImageButton deleteButton;
    public ChatPreviewFragment(String receiverLogin){
        this.receiverLoginString = receiverLogin;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat_preview, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        receiverImage = view.findViewById(R.id.receiverIcon);
        receiverLogin = view.findViewById(R.id.receiverLogin);
        receiverName = view.findViewById(R.id.receiverName);
        actionButton = view.findViewById(R.id.actionButton);
        deleteButton = view.findViewById(R.id.deleteButton);
        try {
            initForReceiver();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initForReceiver() throws IllegalAccessException, java.lang.InstantiationException, IOException {
        AppProperty user = (AppProperty) new AppPropertiesTable(getContext(), null, AppProperty.class).getProperties();
        SecureMessenger messenger = new SecureMessenger(user.getServerBaseUrl());
        actionButton.setOnClickListener(event->{
            Intent intent = new Intent(getContext(), ChatActivity.class);
            intent.putExtra("receiverLogin", receiverLoginString);
            startActivity(intent);
        });
        this.receiverLogin.setText(receiverLoginString);
        deleteButton.setOnClickListener(event->{
            prepareDeleteAlertDialog().show();
        });
        new Thread(()->{
            try{
                File receiverIcon = DownloadedFileManager.getFile(getContext(), receiverLoginString, new FilesNativeCalls(user.getServerBaseUrl()), getContext().getCacheDir().getAbsolutePath());
                setIcon(BitmapFactory.decodeFile(receiverIcon.getAbsolutePath()));
                setReceiverName(messenger.getUserName(user.getAppId(), receiverLoginString));
            }catch (Exception e){
                e.printStackTrace();
            }
        }).start();

    }
    private void setIcon(Bitmap icon){
        getActivity().runOnUiThread(()->{
            this.receiverImage.setImageBitmap(icon);
        });
    }
    private void setReceiverName(String receiverName){
        getActivity().runOnUiThread(()->{
            this.receiverName.setText(receiverName);
        });
    }
    private void delete(){
        EncryptionKeysTable keys = new EncryptionKeysTable(getContext(), null, EncryptionKey.class);
        MessagesTable messages = new MessagesTable(getContext(), null, MessageEntity.class);
        keys.deleteKey(new EncryptionKey(receiverLoginString, null));
        messages.deleteMessagesByReceiverOrSender(receiverLoginString);
    }
    private AlertDialog prepareDeleteAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Do you wanna delete dialog with: "+receiverLoginString);
        builder.setPositiveButton("Yes", (dialogInterface, i) -> {
            delete();
            startActivity(new Intent(getContext(), MainActivity.class));
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        return builder.create();
    }
}