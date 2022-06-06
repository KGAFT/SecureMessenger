package com.kgaft.securemessengerapp.Activities.MainActivity.Fragments;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.kgaft.securemessengerapp.Database.CurrentClientInfo;
import com.kgaft.securemessengerapp.Database.EncryptionKeys;
import com.kgaft.securemessengerapp.R;
import com.kgaft.securemessengerapp.Utils.KeyUtil;

import java.security.NoSuchAlgorithmException;


public class GenerateKeysFragment extends Fragment {
    private EditText receiverInput;
    private ImageView qrCodeOutput;
    private EditText friendsCodeInput;
    private EncryptionKeys keys;
    private String encryptionKey;
    private CurrentClientInfo clientInfo;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_generate_keys, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        receiverInput = view.findViewById(R.id.receiverInput);
        qrCodeOutput = view.findViewById(R.id.qrCodeImage);
        friendsCodeInput = view.findViewById(R.id.cryptCodeInput);
        keys = new EncryptionKeys(view.getContext(), null, null, 0);
        clientInfo = new CurrentClientInfo(view.getContext(), null, null, 0);
        try {
            encryptionKey = KeyUtil.byteToString(KeyUtil.generateKey())+clientInfo.getAppData().getAsString("userLogin");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        MultiFormatWriter mfWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = mfWriter.encode(encryptionKey, BarcodeFormat.QR_CODE, 800, 800);
            Bitmap qrCode = Bitmap.createBitmap(800, 800, Bitmap.Config.RGB_565);
            for(int y = 0; y<800; y++){
                for(int x = 0; x<800; x++){
                    qrCode.setPixel(x, y, bitMatrix.get(x, y)? Color.BLACK: Color.WHITE);
                }
            }
            qrCodeOutput.setImageBitmap(qrCode);
        } catch (WriterException e) {
            e.printStackTrace();
        }

    }

    public void ocEndGeneratingKeyProcess(View view) {

    }
}