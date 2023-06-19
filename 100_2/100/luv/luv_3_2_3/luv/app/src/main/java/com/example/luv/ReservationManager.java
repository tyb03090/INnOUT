package com.example.luv;
import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Message;
import android.app.Activity;
import androidx.core.app.ActivityCompat;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ReservationManager {
    private DatabaseReference reservationRef;
    private Context context;

    public interface OnReservationDeleteListener {
        void onReservationDeleted();
        void onReservationDeleteFailed();
    }

    private OnReservationDeleteListener deleteListener;

    public ReservationManager(Context context, OnReservationDeleteListener listener) {
        this.context = context;
        this.deleteListener = listener;
        reservationRef = FirebaseDatabase.getInstance().getReference("reservations");
    }

    public void deleteExpiredReservations() {
        reservationRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot reservationSnapshot : snapshot.getChildren()) {
                    Reservation reservation = reservationSnapshot.getValue(Reservation.class);
                    if (reservation != null && reservation.isExpired()) {
                        reservationRef.child(reservationSnapshot.getKey()).removeValue()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        deleteListener.onReservationDeleted();  // 삭제 성공 시 콜백 호출
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        deleteListener.onReservationDeleteFailed();  // 삭제 실패 시 콜백 호출
                                    }
                                });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // 예외 처리 코드
            }
        });
    }

    public void checkExpiration() {
            DatabaseReference reservationsRef = FirebaseDatabase.getInstance().getReference("reservations");
            reservationsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot reservationSnapshot : snapshot.getChildren()) {
                        Reservation reservation = reservationSnapshot.getValue(Reservation.class);
                        if (reservation != null && isExpirationApproaching(reservation)) {
                            // 예약 종료일이 3일 남았을 때, 알림을 보내는 로직을 추가
                            sendNotification(reservation, context);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // 예외 처리 코드
                }
            });
        }






        public boolean isExpirationApproaching(Reservation reservation) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            try {
                String endDate = reservation.getEndDate();  // 예약 객체에서 종료일 가져오기
                if (endDate != null) {
                    Date endDateTime = sdf.parse(endDate);
                    Date currentDateTime = new Date();

                    long diffInMillis = endDateTime.getTime() - currentDateTime.getTime();
                    long diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMillis);

                    return diffInDays <= 3; // 예약 종료일이 3일 이내인 경우 true 반환
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return false;
        }








        private int notificationIdCounter = 1;  // 알림 고유 식별자 카운터
        private static final int REQUEST_CODE_PERMISSION_VIBRATE = 1001;

// ...


        public void sendNotification(Reservation reservation, Context context) {
            // 알림 채널을 생성합니다.
            createNotificationChannel(context);

            // 알림을 생성합니다.
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "reservation_channel")
                    .setSmallIcon(R.drawable.icon1)
                    .setContentTitle("Reservation Expiration")
                    .setContentText("Your reservation is expiring in 3 days.")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);

            // 알림을 표시하기 전에 알림 권한을 확인하고 처리합니다.
            String permission = Manifest.permission.VIBRATE;
            int permissionCheck = ContextCompat.checkSelfPermission(context, permission);
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                // 알림 권한이 허용된 경우 알림을 표시합니다.
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                notificationManager.notify(notificationIdCounter++, builder.build());
            } else {
                // 알림 권한이 허용되지 않은 경우 권한을 요청합니다.
                if (context instanceof Activity) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{permission}, REQUEST_CODE_PERMISSION_VIBRATE);
                }
                // 이후에 권한 요청 결과를 처리할 수 있는 콜백 메서드를 구현해야 합니다.
                // onRequestPermissionsResult() 메서드를 오버라이드하여 권한 요청 결과를 처리할 수 있습니다.
            }
        }




        private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 채널 이름과 설명을 지정합니다.
            CharSequence name = "Reservation Channel";
            String description = "Channel for reservation notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            // 알림 채널을 생성합니다.
            NotificationChannel channel = new NotificationChannel("reservation_channel", name, importance);
            channel.setDescription(description);
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);

            // 생성한 알림 채널을 시스템에 등록합니다.
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }





    }

