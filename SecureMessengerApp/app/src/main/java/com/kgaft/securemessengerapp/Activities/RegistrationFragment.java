package com.kgaft.securemessengerapp.Activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.kgaft.securemessengerapp.R;

public class RegistrationFragment extends Fragment implements View.OnClickListener{
    private EditText nameInput;
    private EditText loginInput;
    private EditText passwordInput;
    private EditText passwordInputRepeat;
    private ImageButton registerButton;
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
        registerButton.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View v) {
        if(passwordInput.getText().equals(passwordInputRepeat.getText())){

        }
    }
}