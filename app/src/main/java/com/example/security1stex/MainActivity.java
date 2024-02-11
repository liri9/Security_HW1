package com.example.security1stex;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.widget.EditText;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {
    private EditText password;
    private MaterialButton login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        initViews();
    }

    private void initViews() {
        login.setOnClickListener(v -> {
           // AppManager.getInstance().setUserName(username.getText().toString());
            //Log.d("hello","username:"+ AppManager.getInstance().getUserName());
            if (checkBattery(password.getText().toString()) && checkWifi() && checkFlashLight()){
                Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                startActivity(intent);            }

        });
    }

    private boolean checkBattery(String pass) {
        int battery = AppManager.getInstance().getBatteryPrecent(this);
        if(pass.contains(String.valueOf(battery)))
            return true;
        return false;
    }

    private boolean checkWifi() {
        return AppManager.getInstance().isConnectedToWifi(this,this);

        //return true;
    }

    private boolean checkFlashLight() {
        return AppManager.getInstance().isFlashlight(this);
    }

    private void findViews() {
        //username = findViewById(R.id.login_EDT_username);
        password = findViewById(R.id.login_EDT_password);
        login =findViewById(R.id.login_BTN_login) ;

    }

}