package com.kgaft.securemessengerapp.Activities.ChatActivity.RecyclerView;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kgaft.securemessengerapp.Network.Entities.MessageEntity;
import com.kgaft.securemessengerapp.R;
import com.kgaft.securemessengerapp.Utils.EncryptedFileManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.crypto.NoSuchPaddingException;

public class DataAdapter extends RecyclerView.Adapter<ViewHolder> {

    private ArrayList<MessageEntity> messages;
    private LayoutInflater inflater;
    private EncryptedFileManager filesManager;

    public DataAdapter(Context context, ArrayList<MessageEntity> messages, String downloadUrl, String uploadUrl, String infoUrl, String appId, byte[] encryptionKey, String baseDirectory) throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.messages = messages;
        inflater = LayoutInflater.from(context);
        filesManager = new EncryptedFileManager(downloadUrl, uploadUrl, infoUrl, appId, encryptionKey, baseDirectory);
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
        ArrayList<String> imagesLocations = new ArrayList<>();
        try {
            for (long l : messages.get(position).getContentId()) {
                try {
                    String location = filesManager.downloadFileAndDecrypt(l);
                    imagesLocations.add(location);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {

        }
        if (imagesLocations.size() > 0) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(new File(imagesLocations.get(0)));
                byte[] image = new byte[fis.available()];
                holder.messageImage.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }


}
