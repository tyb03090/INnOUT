package com.example.luv;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RentPeriodActivity extends AppCompatActivity {

    private DatabaseReference mDatabaseRef; // Firebase Realtime Database의 레퍼런스
    private TextView mLockerNumberTextView;
    private TextView mFontNumberTextView; // 사물함 번호를 띄울 TextView
    private int mTag; // 폰트 번호를 저장할 멤버 변수
    private int mLockerNumber; // 락커 번호를 저장할 멤버 변수

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_period);

        // Firebase Realtime Database의 레퍼런스를 가져옴
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        // Intent로부터 폰트 번호와 락커 번호를 가져옴
        mTag = getIntent().getIntExtra("fontNumber", -1);
        mLockerNumber = getIntent().getIntExtra("lockerNumber", -1);

        // 사물함 번호를 띄울 TextView를ㅌㅌㅌㅌㅌㅌㅌㅌㅌ 찾아서 설정
        mLockerNumberTextView = findViewById(R.id.textView5);
        mLockerNumberTextView.setText(String.valueOf(mLockerNumber));



        // Firebase Realtime Database에서 해당 폰트 번호에 대응하는 사물함 번호를 가져옴
        mDatabaseRef.child("lockers").child("font" + mTag).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String lockerNumber = dataSnapshot.getValue(String.class);
                    // 사물함 번호를 설정
                    mFontNumberTextView.setText(String.valueOf(mTag));
                    mLockerNumberTextView.setText(lockerNumber);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 오류 발생 시 처리
            }
        });

        // 단기 대여 버튼 클릭시 처리
        Button shortRentButton = findViewById(R.id.short_rent_button);
        shortRentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 단기 대여 화면으로 이동
                Intent intent = new Intent(RentPeriodActivity.this, ShortRentActivity.class);
                intent.putExtra("fontNumber", mTag);
                intent.putExtra("lockerNumber", mLockerNumber);

                startActivity(intent);
            }
        });

        // 장기 대여 버튼 클릭시 처리
        Button longRentButton = findViewById(R.id.long_rent_button);
        longRentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 장기 대여 화면으로 이동
                Intent intent = new Intent(RentPeriodActivity.this, LongRentActivity.class);
                intent.putExtra("fontNumber", mTag);
                intent.putExtra("lockerNumber", mLockerNumber);

                startActivity(intent);
            }
        });
    }
}
