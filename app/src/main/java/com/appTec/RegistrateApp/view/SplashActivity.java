package com.appTec.RegistrateApp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.appTec.RegistrateApp.R;

public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        Intent i=new Intent( SplashActivity.this, LoginActivity.class);
        startActivity(i);
    }
}