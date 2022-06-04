package com.kgaft.securemessengerapp.Activities.AuthorizeActivity.Fragments;

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

public class LoginFragment extends Fragment implements View.OnClickListener{
    private EditText loginInput;
    private EditText passwordInput;
    private ImageButton loginButton;
    private TextView testText;
    private EditText serverAddressInput;
    private CurrentClientInfo clientInfo;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        clientInfo = new CurrentClientInfo(view.getContext(), null, null, 0);
        loginInput = view.findViewById(R.id.loginInput);
        passwordInput = view.findViewById(R.id.passwordInput);
        loginButton = view.findViewById(R.id.loginButton);
        testText = view.findViewById(R.id.testText);
        loginButton.setOnClickListener(this::onClick);
        serverAddressInput = view.findViewById(R.id.serverAddressInput);
    }

    @Override
    public void onClick(View v) {

        UserEntity entity = AuthorizeUtility.loginUser(loginInput.getText().toString(), passwordInput.getText().toString(), serverAddressInput.getText().toString());
        if(entity == null){
            testText.setText("Cannot authorize");
        }
        else{
            clientInfo.insertClientData(entity.getUserId(), entity.getAppId(), entity.getLogin(), entity.getName(), serverAddressInput.getText().toString());
            AuthorizeActivity.checkUser();
        }

    }
}