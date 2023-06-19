package com.example.luv;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class C1814Activity extends AppCompatActivity {

    private GridLayout mGridLayout; // 사물함 이미지를 나열할 GridLayout

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c1814);

        mGridLayout = findViewById(R.id.gridview1814);

        for (int i = 0; i < mGridLayout.getChildCount(); i++) {
            final int position = i;
            View child = mGridLayout.getChildAt(i);
            if (child instanceof ImageView) { // ImageView일 경우에만 처리
                child.setTag(i); // 이미지뷰에 태그 값으로 i를 지정
                child.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 선택한 사물함의 정보를 가져옴
                        Object tagObj = v.getTag();
                        if (tagObj != null) {
                            int tag = (int) tagObj;

                            // 다음 화면으로 이동
                            Intent intent = new Intent(C1814Activity.this, RentPeriodActivity.class);
                            intent.putExtra("tag", tag);
                            startActivity(intent);
                        }
                    }
                });
            }
        }
        Button button4 = findViewById(R.id.button1804);
        // 버튼 클릭 이벤트 리스너
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 인텐트 생성
                Intent intent = new Intent(C1814Activity.this, LockerActivity.class);
                // 다른 액티비티로 이동
                startActivity(intent);
            }
        });
        Button button5 = findViewById(R.id.button1805);
        // 버튼 클릭 이벤트 리스너
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 인텐트 생성
                Intent intent = new Intent(C1814Activity.this,CimbadideActivity.class);
                // 다른 액티비티로 이동
                startActivity(intent);
            }
        });
        Button button6 = findViewById(R.id.button1806);
        // 버튼 클릭 이벤트 리스너
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 인텐트 생성
                Intent intent = new Intent(C1814Activity.this, C1817Activity.class);
                // 다른 액티비티로 이동
                startActivity(intent);
            }
        });
    }
}

