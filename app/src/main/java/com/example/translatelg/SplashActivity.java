package com.example.translatelg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 1000; // Độ trễ (ms) cho Splash Screen

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        // Tạo một Handler giao tiêp giao diên, postdelay tạo độ trễ,runnable đại diện tác vụ

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
            // thêm độ trễ chuyển trang
        }, SPLASH_TIME_OUT);
    }
}