package com.kgaft.securemessengerapp.Activities.MainActivity.Fragments;


import android.hardware.Camera;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.kgaft.securemessengerapp.R;

import java.io.IOException;


public class CameraFragment extends Fragment {
    private SurfaceView cameraPreview;
    private Button takePictureButton;
    private Camera camera;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_camera, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cameraPreview = view.findViewById(R.id.cameraPreview);
        takePictureButton = view.findViewById(R.id.takePictureButton);
        camera = Camera.open(0);
        try {
            camera.setPreviewDisplay(cameraPreview.getHolder());
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}