package com.example.luv;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LongRentActivity extends AppCompatActivity {

    private int lockerNumber; // 선택한 사물함의 번호
    private int fontNumber;
    private static final int REQUEST_CODE_START_DATE_TIME = 1;
    private static final int REQUEST_CODE_END_DATE_TIME = 2;
    private TextView mLockerNumberTextView;
    private TextView mFontNumberTextView; // 사물함 번호를 띄울 TextView
    private int mTag; // 폰트 번호를 저장할 멤버 변수
    private int mLockerNumber; // 락커 번호를 저장할 멤버 변수

    private ImageView reserveButton;
    private ImageView calendarImageStart;
    private ImageView calendarImageEnd;
    private TextView selectedDateTimeStart;
    private TextView selectedDateTimeEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_long_rent);




        // 예약 버튼에 클릭 이벤트 등록하기

        // 이전 화면에서 넘겨받은 데이터를 가져옴
        // 이전 페이지에서 넘어온 Intent 객체를 받아옴
        Intent intent = getIntent();
        mTag = getIntent().getIntExtra("fontNumber", -1);
        mLockerNumber = getIntent().getIntExtra("lockerNumber", -1); // 정수형으로 값을 가져옴

        TextView  mLockerNumberTextView = findViewById(R.id.textview22);
        mLockerNumberTextView.setText(String.valueOf(mLockerNumber));


        calendarImageStart = findViewById(R.id.restartImgbtn1);
        selectedDateTimeStart = findViewById(R.id.restartbtn1);
        calendarImageEnd = findViewById(R.id.restartImgbtn2);
        selectedDateTimeEnd = findViewById(R.id.restartbtn2);
        reserveButton = findViewById(R.id.nextBtn);




        // 시작일 캘린더 이미지를 클릭하면 다음 화면으로 이동
        calendarImageStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LongRentActivity.this, DateTimeActivity3.class);
                startActivityForResult(intent, REQUEST_CODE_START_DATE_TIME);
                intent.putExtra("fontNumber", mTag);
                intent.putExtra("lockerNumber", mLockerNumber);
                setResult(RESULT_OK, intent);

            }
        });

        // 종료일 캘린더 이미지를 클릭하면 다음 화면으로 이동
        calendarImageEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LongRentActivity.this, DateTimeActivity4.class);
                startActivityForResult(intent, REQUEST_CODE_END_DATE_TIME);
                intent.putExtra("fontNumber", mTag);
                intent.putExtra("lockerNumber", mLockerNumber);
                setResult(RESULT_OK, intent);

            }
        });
        reserveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 예약하기 버튼 클릭 시 실행될 코드 작성
                // ...
                intent.putExtra("fontNumber", mTag);
                intent.putExtra("lockerNumber", mLockerNumber);
                setResult(RESULT_OK, intent);
                onClickReserve(v);
            }
        });

    }


    // 다음 화면에서 선택한 날짜와 시간을 받아서 TextView에 표시
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_START_DATE_TIME && resultCode == RESULT_OK && data != null) {
            int year = data.getIntExtra("year", 0);
            int month = data.getIntExtra("month", 0);
            int day = data.getIntExtra("day", 0);
            int hour = data.getIntExtra("hour", 0);
            int minute = data.getIntExtra("minute", 0);

            String dateTime = String.format("%04d-%02d-%02d %02d:%02d", year, month, day, hour, minute);
            selectedDateTimeStart.setText(dateTime);
        } else if (requestCode == REQUEST_CODE_END_DATE_TIME && resultCode == RESULT_OK && data != null) {
            int year = data.getIntExtra("year", 0);
            int month = data.getIntExtra("month", 0);
            int day = data.getIntExtra("day", 0);
            int hour = data.getIntExtra("hour", 0);
            int minute = data.getIntExtra("minute", 0);

            String dateTime = String.format("%04d-%02d-%02d %02d:%02d", year
                    , month, day, hour, minute);
            selectedDateTimeEnd.setText(dateTime);
        }

    }
    // 예약하기 버튼을 클릭하면 실행되는 메소드



    public void onClickReserve(View view) {
        // 선택한 시작일과 종료일 정보 가져오기
        String startDate = selectedDateTimeStart.getText().toString();
        String endDate = selectedDateTimeEnd.getText().toString();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue(String.class);
                String studentId = snapshot.child("studentId").getValue(String.class);

                // 선택한 포트 번호와 사물함 번호 가져오기
                int fontNumber = mTag; // 폰트 번호
                int lockerNumber = mLockerNumber; // 사물함 번호

                // 예약 정보 생성
                Reservation reservation = new Reservation(studentId, name, lockerNumber, fontNumber, startDate, endDate);

                // 파이어베이스에 예약 정보 저장
                DatabaseReference reservationRef = FirebaseDatabase.getInstance().getReference("reservations");
                reservationRef.push().setValue(reservation);

                // 예약 내역 화면으로 이동
                Intent intent = new Intent(LongRentActivity.this, ReservationsActivity.class);
                intent.putExtra("fontNumber", mTag);
                intent.putExtra("lockerNumber", mLockerNumber);
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // 예외 처리 코드
            }
        });
    }
}




