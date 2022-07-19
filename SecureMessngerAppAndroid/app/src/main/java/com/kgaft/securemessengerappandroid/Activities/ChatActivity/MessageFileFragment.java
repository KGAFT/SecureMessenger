package com.kgaft.securemessengerappandroid.Activities.ChatActivity;

import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.kgaft.securemessengerappandroid.Files.FilesEncryptedNativeCalls;
import com.kgaft.securemessengerappandroid.R;

import java.io.File;


public class MessageFileFragment extends Fragment {
    private ImageButton downloadButton;
    private ImageView filePreview;
    private ProgressBar downloadProgress;
    private long fileId;
    private byte[] encryptionKey;
    private String serverBaseUrl;
    private long appId;

    private boolean downloaded = false;

    public MessageFileFragment(long fileId, byte[] encryptionKey, String serverBaseUrl, long appId) {
        this.fileId = fileId;
        this.encryptionKey = encryptionKey;
        this.serverBaseUrl = serverBaseUrl;
        this.appId = appId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_message_file, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        downloadButton = view.findViewById(R.id.downloadFileButton);
        filePreview = view.findViewById(R.id.filePreview);
        downloadProgress = view.findViewById(R.id.downloadProgressBar);
        downloadButton.setOnClickListener(event -> {
            if (!downloaded) {
                downloadButton.setVisibility(View.INVISIBLE);
                downloadProgress.setVisibility(View.VISIBLE);
                FilesEncryptedNativeCalls files = new FilesEncryptedNativeCalls(serverBaseUrl);
                new Thread(() -> {
                    File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    File downloadedFile = files.downloadAndDecryptFile(appId, fileId, encryptionKey, file.getAbsolutePath(), getContext().getCacheDir().getAbsolutePath());
                    if (downloadedFile != null) {
                        getActivity().runOnUiThread(() -> {
                            downloaded = true;
                            downloadProgress.setVisibility(View.INVISIBLE);
                            Toast toast = Toast.makeText(getContext(), "Download file to: " + downloadedFile.getAbsolutePath(), Toast.LENGTH_SHORT);
                            try {
                                toast.show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                filePreview.setImageBitmap(BitmapFactory.decodeFile(downloadedFile.getAbsolutePath()));
                            } catch (Exception e) {
                                filePreview.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.file_icon));
                            }
                        });
                    } else {
                        getActivity().runOnUiThread(() -> {
                            downloadProgress.setVisibility(View.INVISIBLE);
                            downloadButton.setVisibility(View.VISIBLE);
                        });
                    }
                }).start();
            }

        });
    }
}