package com.appTec.RegistrateApp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.appTec.RegistrateApp.R;

public class SplashActivity extends AppCompatActivity {
    /**
     * Deprecated activity. It is don't used anymore.
     * @param savedInstanceState
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent( SplashActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        }, 2000);
    }
}