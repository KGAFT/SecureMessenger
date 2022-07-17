package com.kgaft.securemessengerappandroid.Activities.ChatActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kgaft.securemessengerappandroid.R;

public class FileToSendPreviewFragment extends Fragment {
    private TextView fileName;
    private ImageButton deleteButton;
    private ProgressBar uploadingProgressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_file_to_send_preview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fileName = view.findViewById(R.id.fileName);
        deleteButton = view.findViewById(R.id.deleteButton);
        uploadingProgressBar = view.findViewById(R.id.progressBar);
    }

    public TextView getFileName() {
        return fileName;
    }

    public ImageButton getDeleteButton() {
        return deleteButton;
    }

    public ProgressBar getUploadingProgressBar() {
        return uploadingProgressBar;
    }
}