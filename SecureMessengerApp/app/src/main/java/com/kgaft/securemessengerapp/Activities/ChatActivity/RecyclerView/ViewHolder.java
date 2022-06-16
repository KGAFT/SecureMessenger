package com.kgaft.securemessengerapp.Activities.ChatActivity.RecyclerView;



import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.kgaft.securemessengerapp.R;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {
    TextView senderName;
    TextView messageText;
    ImageView messageImage;
    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        senderName = itemView.findViewById(R.id.messageSenderName);
        messageText = itemView.findViewById(R.id.messageText);
        messageImage = itemView.findViewById(R.id.messageImage);
    }
}
