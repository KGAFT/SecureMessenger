package com.kgaft.securemessengerapp.Activities.AuthorizeActivity.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kgaft.securemessengerapp.Activities.AuthorizeActivity.AuthorizeActivity;
import com.kgaft.securemessengerapp.Database.CurrentClientInfo;
import com.kgaft.securemessengerapp.Network.Entities.UserEntity;
import com.kgaft.securemessengerapp.Network.AuthorizeUtility;
import com.kgaft.securemessengerapp.R;

public class RegistrationFragment extends Fragment implements View.OnClickListener{
    private EditText nameInput;
    private EditText loginInput;
    private EditText passwordInput;
    private EditText passwordInputRepeat;
    private ImageButton registerButton;
    private EditText serverAddress;
    private TextView testText;
    private CurrentClientInfo clientInfo;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registration, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nameInput = view.findViewById(R.id.nameInput);
        loginInput = view.findViewById(R.id.loginInput);
        passwordInput = view.findViewById(R.id.passwordInput);
        passwordInputRepeat = view.findViewById(R.id.repeatPasswordInput);
        registerButton = view.findViewById(R.id.registerButton);
        testText = view.findViewById(R.id.testText);
        registerButton.setOnClickListener(this::onClick);
        serverAddress = view.findViewById(R.id.serverAddressInput);
        clientInfo = new CurrentClientInfo(view.getContext(), null, null, 0);
    }

    @Override
    public void onClick(View v) {
        if(passwordInput.getText().toString().equals(passwordInputRepeat.getText().toString())){
            String responseText = AuthorizeUtility.registerUser(loginInput.getText().toString(), passwordInput.getText().toString(), nameInput.getText().toString(), serverAddress.getText().toString());
            if(responseText.equals("Success!")){
                UserEntity entity = AuthorizeUtility.loginUser(loginInput.getText().toString(), passwordInput.getText().toString(), serverAddress.getText().toString());
                clientInfo.insertClientData(entity.getUserId(), entity.getAppId(), entity.getLogin(), entity.getName(), serverAddress.getText().toString());
                startActivity(new Intent(v.getContext(), AuthorizeActivity.class));
            }
            else{
                testText.setText(responseText);
            }
        }
        else{
            testText.setText("Error: passwords does not same!");
        }

    }
}