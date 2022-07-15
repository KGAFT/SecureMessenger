package com.kgaft.securemessengerappandroid.Activities.AuthorizeActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kgaft.securemessengerappandroid.Activities.MainActivity.MainActivity;
import com.kgaft.securemessengerappandroid.Database.AppPropertiesTable.AppPropertiesTable;
import com.kgaft.securemessengerappandroid.Database.AppPropertiesTable.AppProperty;
import com.kgaft.securemessengerappandroid.Network.SecureMessenger;
import com.kgaft.securemessengerappandroid.Network.UserResponse;
import com.kgaft.securemessengerappandroid.R;


public class RegisterFragment extends Fragment {
    private EditText serverAddressInput;
    private EditText nameInput;
    private EditText loginInput;
    private EditText passwordInput;
    private Button loginButton;
    private Button registerButton;
    private TextView errorOutput;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        serverAddressInput = view.findViewById(R.id.serverAddressInput);
        loginInput = view.findViewById(R.id.loginInput);
        passwordInput = view.findViewById(R.id.passwordInput);
        loginButton = view.findViewById(R.id.loginButton);
        nameInput = view.findViewById(R.id.nameInput);
        registerButton = view.findViewById(R.id.registerButton);
        errorOutput = view.findViewById(R.id.errorOutput);
        initButtons();
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initButtons(){
        registerButton.setOnClickListener(event->{
            try{
                SecureMessenger messenger = new SecureMessenger(serverAddressInput.getText().toString());
                if(messenger.register(loginInput.getText().toString(), passwordInput.getText().toString(), nameInput.getText().toString())){
                    AppPropertiesTable appProperties = new AppPropertiesTable(getContext(), null, AppProperty.class);
                    UserResponse user = messenger.authorize(loginInput.getText().toString(), passwordInput.getText().toString());
                    appProperties.insertAppProperties(new AppProperty(serverAddressInput.getText().toString(), user.getLogin(), passwordInput.getText().toString(), user.getAppId()));
                    startActivity(new Intent(getContext(), MainActivity.class));
                }
                else{
                    errorOutput.setText("Cannot create user with same login");
                }
            }catch (Exception e){
                errorOutput.setText("Error");
            }
        });
        loginButton.setOnClickListener(event->{
            getParentFragmentManager().beginTransaction().replace(R.id.fragmentsContainer, new LoginFragment()).commitNow();
        });
    }
}