package com.example.luv;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class  ReservationsActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private int mTag; // 폰트 번호를 저장할 멤버 변수
    private int mLockerNumber;
    private String mStudentId;
    private String mName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        // Intent로부터 폰트 번호, 락커 번호를 가져옴
        mTag = getIntent().getIntExtra("fontNumber", -1);
        mLockerNumber = getIntent().getIntExtra("lockerNumber", -1);

        EditText studentIdEditText = findViewById(R.id.studentNumberEditText);
        EditText nameEditText = findViewById(R.id.nameEditText);
        EditText lockerNumberEditText = findViewById(R.id.lockerTagEditText);
        EditText startDateEditText = findViewById(R.id.startDateEditText);
        EditText endDateEditText = findViewById(R.id.endDateEditText);

        // 현재 로그인한 사용자의 학번과 이름 가져오기
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mStudentId = dataSnapshot.child("studentId").getValue(String.class);
                    mName = dataSnapshot.child("name").getValue(String.class);
                    lockerNumberEditText.setText(String.valueOf(mLockerNumber));
                    studentIdEditText.setText(mStudentId);
                    nameEditText.setText(mName);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // 예외 처리
                }
            });
        }

        lockerNumberEditText.setText(String.valueOf(mLockerNumber));

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reservationRef = database.getReference("reservations");

        // 마지막 예약 정보만 가져와서 EditText에 예약 정보를 보여주는 코드
        reservationRef.limitToLast(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Reservation reservation = snapshot.getValue(Reservation.class);
                    // EditText에 예약 정보 설정
                    if (reservation != null) {
                        startDateEditText.setText(reservation.getStartDate());
                        endDateEditText.setText(reservation.getEndDate());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // 예외 처리
            }
        });

        // ...
        Button confirmButton = findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReservationsActivity.this, MainActivity.class);
                intent.putExtra("lockerNumber", mLockerNumber);
                intent.putExtra("studentId", mStudentId);
                intent.putExtra("name", mName);
                startActivity(intent);

                StringBuilder messageBuilder = new StringBuilder();
                messageBuilder.append("사물함 예약이 완료되었습니다.\n")
                        .append("학번: ").append(mStudentId).append("\n")
                        .append("이름: ").append(mName).append("\n")
                        .append("사물함 번호: ").append(mLockerNumber);

                // Create a notification channel
                String channelId = "reservation_channel";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    CharSequence channelName = "Reservation Channel";
                    int importance = NotificationManager.IMPORTANCE_DEFAULT;
                    NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
                    NotificationManager notificationManager = getSystemService(NotificationManager.class);

                    // Check if the permission is granted
                    if (ContextCompat.checkSelfPermission(ReservationsActivity.this, "android.permission.ACCESS_NOTIFICATION_POLICY")
                            == PackageManager.PERMISSION_GRANTED) {
                        // Permission is granted, create the notification channel
                        notificationManager.createNotificationChannel(channel);
                    } else {
                        // Permission is not granted, request the permission from the user
                        ActivityCompat.requestPermissions(ReservationsActivity.this,
                                new String[]{"android.permission.ACCESS_NOTIFICATION_POLICY"}, 1);
                        return;
                    }
                }

                // Build the notification
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(ReservationsActivity.this, channelId)
                        .setSmallIcon(R.drawable.icon1)
                        .setContentTitle("사물함 예약")
                        .setContentText(messageBuilder.toString())
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                // Show the notification
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(ReservationsActivity.this);
                notificationManager.notify(0, notificationBuilder.build());
            }
        });




// ...

    }
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "뒤로가기 버튼을 한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

}