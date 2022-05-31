package com.kgaft.securemessengerapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.kgaft.securemessengerapp.R;

public class AuthorizeActivity extends AppCompatActivity {
    private LoginFragment loginFragment;
    private RegistrationFragment registrationFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorize);

        loginFragment = new LoginFragment();
        registrationFragment = new RegistrationFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.authorizeContainer, loginFragment).commitNow();
    }

    public void ocChangeToRegisterFragment(View view) {
        getSupportFragmentManager().beginTransaction().remove(loginFragment).commitNow();
        getSupportFragmentManager().beginTransaction().add(R.id.authorizeContainer, registrationFragment).commitNow();
    }

    public void ocChangeToLoginFragment(View view) {
        getSupportFragmentManager().beginTransaction().remove(registrationFragment).commitNow();
        getSupportFragmentManager().beginTransaction().add(R.id.authorizeContainer, loginFragment).commitNow();
    }
}