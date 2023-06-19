package com.example.luv;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 액티비티를 풀스크린으로 설정합니다.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);



        // 액티비티의 레이아웃을 설정합니다.
        setContentView(R.layout.splash);

        // 일정 시간이 지난 후 다음 화면으로 넘어가기 위한 핸들러를 만듭니다.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 다음 화면으로 넘어갑니다.
                Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                startActivity(intent);

                // 현재 액티비티를 종료합니다.
                finish();
            }
        }, 3000); // 3초
    }
}
