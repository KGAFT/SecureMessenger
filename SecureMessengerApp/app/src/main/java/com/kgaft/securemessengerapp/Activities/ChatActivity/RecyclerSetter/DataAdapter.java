package com.kgaft.securemessengerapp.Activities.ChatActivity.RecyclerSetter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kgaft.securemessengerapp.Network.Entities.MessageEntity;
import com.kgaft.securemessengerapp.R;

import java.util.ArrayList;

public class DataAdapter extends RecyclerView.Adapter<ViewHolder> {

    private ArrayList<MessageEntity> messages;
    private LayoutInflater inflater;

    public DataAdapter(Context context, ArrayList<MessageEntity> messages) {
        this.messages = messages;
        inflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.fragment_message, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MessageEntity message = messages.get(position);

        holder.messageText.setText(message.getMessagetext());
        holder.senderName.setText(message.getSender());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

}
