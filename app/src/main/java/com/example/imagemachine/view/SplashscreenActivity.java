package com.example.imagemachine.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.example.imagemachine.R;
import com.example.imagemachine.handler.DBHandler;

public class SplashscreenActivity extends AppCompatActivity {

    SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.parseColor("#14A0B4"));
        }

        prefs = getSharedPreferences("com.example.imagemachine", MODE_PRIVATE);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(prefs.getBoolean("firstrun", true)) {
                    prefs.edit().putBoolean("firstrun", false).apply();
                    startActivity(new Intent(SplashscreenActivity.this, NavigationActivity.class));
                    finish();
                }
                else{
                    startActivity(new Intent(SplashscreenActivity.this, NavigationActivity.class));
                    finish();
                }
            }
        }, 2000);
    }
}