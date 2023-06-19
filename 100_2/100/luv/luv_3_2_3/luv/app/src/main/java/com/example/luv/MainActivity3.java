package com.example.luv;

import static android.content.ContentValues.TAG;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity3 extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        // Firebase 데이터베이스 초기화
        mDatabase = FirebaseDatabase.getInstance().getReference("notifications");

        // ...
        Intent intent = getIntent();
        if (intent.hasExtra("notification_data")) {
            String notificationData = intent.getStringExtra("notification_data");
            // 고유한 ID 생성
            String notificationId = mDatabase.push().getKey();
            Notifications notification = new Notifications(notificationId, notificationData);
            mDatabase.child(notificationId).setValue(notification);
        }

        // 공지사항 클래스의 알림 목록 가져오기
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // 알림 목록 초기화
                List<Notifications> notifications = new ArrayList<>();
                // 모든 알림 데이터 가져오기
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Notifications notification = dataSnapshot.getValue(Notifications.class);
                    if (notification != null) {
                        notifications.add(notification);
                    }
                }
                // 공지사항 클래스에 알림 목록 전달
                // 알림 목록을 이용하여 UI 업데이트 등 필요한 작업 수행
                updateNotifications(notifications);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void updateNotifications(List<Notifications> notifications) {
        // 공지사항 클래스의 UI 업데이트 등 필요한 작업 수행
        // 예시로 RecyclerView를 이용하여 알림 목록을 표시하는 경우
        RecyclerView recyclerView = findViewById(R.id.notification_recycler_view);
        NotificationAdapter adapter = new NotificationAdapter(notifications);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}