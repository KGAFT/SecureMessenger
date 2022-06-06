package com.kgaft.securemessengerapp.Activities.AuthorizeActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;

import com.kgaft.securemessengerapp.Activities.AuthorizeActivity.Fragments.LoginFragment;
import com.kgaft.securemessengerapp.Activities.AuthorizeActivity.Fragments.RegistrationFragment;
import com.kgaft.securemessengerapp.Activities.MainActivity.MainActivity;
import com.kgaft.securemessengerapp.Database.CurrentClientInfo;
import com.kgaft.securemessengerapp.Network.AuthorizeUtility;
import com.kgaft.securemessengerapp.R;

public class AuthorizeActivity extends AppCompatActivity {
    private LoginFragment loginFragment;
    private RegistrationFragment registrationFragment;
    private CurrentClientInfo currentClientInfo;
    private static AuthorizeActivity instance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy gfgPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(gfgPolicy);
        setContentView(R.layout.activity_authorize);
        instance = this;
        loginFragment = new LoginFragment();
        registrationFragment = new RegistrationFragment();
        currentClientInfo = new CurrentClientInfo(getApplicationContext(), null, null,  0);

        if(!checkUser()){
            getSupportFragmentManager().beginTransaction().add(R.id.authorizeContainer, loginFragment).commitNow();
        }

    }

    public void ocChangeToRegisterFragment(View view) {
        getSupportFragmentManager().beginTransaction().remove(loginFragment).commitNow();
        getSupportFragmentManager().beginTransaction().add(R.id.authorizeContainer, registrationFragment).commitNow();
    }

    public void ocChangeToLoginFragment(View view) {
        getSupportFragmentManager().beginTransaction().remove(registrationFragment).commitNow();
        getSupportFragmentManager().beginTransaction().add(R.id.authorizeContainer, loginFragment).commitNow();
    }
    public static boolean checkUser(){
        try{
            if(AuthorizeUtility.checkConnection(instance.currentClientInfo.getAppData().getAsString("appId"), instance.currentClientInfo.getAppData().getAsString("serverAddress"))){
                instance.startActivity(new Intent(instance, MainActivity.class));
                return true;
            }
            else{
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }


    public static AuthorizeActivity getInstance() {
        return instance;
    }
}