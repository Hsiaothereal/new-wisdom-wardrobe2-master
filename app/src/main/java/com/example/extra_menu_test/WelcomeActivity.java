package com.example.extra_menu_test;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;


public class WelcomeActivity extends AppCompatActivity {
    private static final int GOTO_MAIN_ACTIVITY = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_layout);
        getSupportActionBar().hide();
         @SuppressLint("HandlerLeak") Handler mHandler = new Handler() {
             public void handleMessage(android.os.Message msg) {

                 switch (msg.what) {
                     case GOTO_MAIN_ACTIVITY:
                         Intent intent = new Intent();
                         //將原本Activity的換成MainActivity
                         intent.setClass(WelcomeActivity.this, MainActivity.class);
                         startActivity(intent);
                         finish();
                         break;

                     default:
                         break;
                 }
             }
         };
        mHandler.sendEmptyMessageDelayed(GOTO_MAIN_ACTIVITY, 2000); //2秒跳轉
    }
}
