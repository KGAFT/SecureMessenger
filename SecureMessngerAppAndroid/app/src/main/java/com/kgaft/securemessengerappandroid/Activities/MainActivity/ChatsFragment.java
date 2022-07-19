package com.kgaft.securemessengerappandroid.Activities.MainActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kgaft.securemessengerappandroid.Activities.StartChatActivity.StartChatActivity;
import com.kgaft.securemessengerappandroid.Database.EncryptionKeysTable.EncryptionKey;
import com.kgaft.securemessengerappandroid.Database.EncryptionKeysTable.EncryptionKeysTable;
import com.kgaft.securemessengerappandroid.R;

import java.util.ArrayList;
import java.util.List;


public class ChatsFragment extends Fragment {
    private List<String> receiversAdded = new ArrayList<>();
    private Thread chatsAdderThread;
    private EncryptionKeysTable keysTable;
    private FloatingActionButton startChatButton;
    private LinearLayout chatsContainer;
    volatile boolean threadRunning = true;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chats, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        keysTable = new EncryptionKeysTable(getContext(), null, EncryptionKey.class);
        startChatButton = view.findViewById(R.id.startNewChatButton);
        chatsContainer = view.findViewById(R.id.chatsContainer);
        startChatButton.setOnClickListener(event->{
            startActivity(new Intent(getContext(), StartChatActivity.class));
        });
        initChatAdderThread();
        chatsContainer.removeAllViews();
        threadRunning = true;
        initChatAdderThread();
        chatsAdderThread.start();
    }
    public void addChatPreviewFragment(ChatPreviewFragment chat){
        getActivity().runOnUiThread(() -> {
            getParentFragmentManager().beginTransaction().add(R.id.chatsContainer, chat).commitNow();
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onResume() {
        super.onResume();
        if(!threadRunning){
            threadRunning = true;
            initChatAdderThread();
            chatsAdderThread.start();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        chatsContainer.removeAllViews();
        threadRunning = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        chatsContainer.removeAllViews();
        threadRunning = false;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initChatAdderThread(){
        chatsAdderThread = new Thread(()->{
            while(threadRunning){
                try{
                    keysTable.getAvailableReceivers().forEach(receiver->{
                        try{
                            if(!receiversAdded.contains(receiver)){
                                ChatPreviewFragment chat = new ChatPreviewFragment(receiver);
                                addChatPreviewFragment(chat);
                                receiversAdded.add(receiver);
                            }
                        }catch (Exception e){

                        }
                    });
                }catch (Exception e){

                }
            }
        });
    }

}