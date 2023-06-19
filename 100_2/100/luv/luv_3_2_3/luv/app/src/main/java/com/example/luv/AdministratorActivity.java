package com.example.luv;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.ArrayList;
import java.util.List;

public class AdministratorActivity extends AppCompatActivity {
    private List<Reservations> reservationList;
    private List<ImageView> lockerImageViewList;
    private RecyclerView recyclerView;
    private ReservationAdapter reservationsAdapter;
    private DatabaseReference reservationsRef;
    private static final long KEY_UPDATE_INTERVAL = 30 * 1000; // 30 seconds

    private Handler handler;
    private Runnable keyUpdateRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator);
        handler = new Handler();

        Button logoutButton = findViewById(R.id.button4);
        // 로그아웃 버튼 클릭 이벤트 설정
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 로그아웃 처리
                FirebaseAuth mAuth = FirebaseAuth.getInstance(); // mAuth 객체 생성
                mAuth.signOut();
                // 스택 내에 쌓인 모든 액티비티 종료
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
        recyclerView = findViewById(R.id.recyclerView1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

// 파이어베이스에서 예약 내역 가져오기
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        reservationsRef =  firebaseDatabase.getReference("reservations");

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Reservation> reservationList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String studentId = snapshot.child("studentId").getValue(String.class);
                    String name = snapshot.child("name").getValue(String.class);
                    int lockerNumber = snapshot.child("lockerNumber").getValue(Integer.class);
                    int fontNumber = snapshot.child("fontNumber").getValue(Integer.class);
                    String startDate = snapshot.child("startDate").getValue(String.class);
                    String endDate = snapshot.child("endDate").getValue(String.class);

                    Reservation reservation = new Reservation(studentId, name, lockerNumber, fontNumber, startDate, endDate);
                    reservationList.add(reservation);
                }

                // 예약 내역을 기반으로 RecyclerView에 표시
                if (reservationsAdapter == null) {
                    reservationsAdapter = new ReservationAdapter(reservationList);
                    recyclerView.setAdapter(reservationsAdapter);
                } else {
                    reservationsAdapter.updateData(reservationList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 예외 처리
            }
        };

        reservationsRef.addValueEventListener(valueEventListener);






        lockerImageViewList = new ArrayList<>();
        lockerImageViewList.add(findViewById(R.id.imageView11));
        lockerImageViewList.add(findViewById(R.id.imageView12));
        lockerImageViewList.add(findViewById(R.id.imageView13));
        lockerImageViewList.add(findViewById(R.id.imageView14));
        lockerImageViewList.add(findViewById(R.id.imageView15));

        // 파이어베이스에서 예약 내역 가져오기
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reservationsRef = database.getReference("reservation");
        reservationsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                reservationList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // 예약 정보를 가져오는 방법에 따라 수정해주세요.
                    // 예시로 학번과 이름을 가져오는 방법을 제시하였습니다.
                    String studentId = snapshot.child("studentId").getValue(String.class);
                    String name = snapshot.child("name").getValue(String.class);
                    Reservations reservation = new Reservations(studentId, name);
                    reservationList.add(reservation);
                }

                // 예약 내역을 기반으로 QR 코드 생성
                generateQRCode();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 예외 처리
            }
        });
    }


// ...

    // QR 코드 생성 및 이미지뷰에 표시
// QR 코드 생성 및 이미지뷰에 표시
    private void generateQRCode() {
        int imageViewCount = lockerImageViewList.size();
        int reservationCount = reservationList.size();

        // 예약 내역과 이미지뷰 개수를 비교하여 QR 코드 생성 및 이미지뷰에 표시
        for (int i = 0; i < Math.min(imageViewCount, reservationCount); i++) {
            Reservations reservation = reservationList.get(i);
            ImageView lockerImageView = lockerImageViewList.get(i);

            String studentId = reservation.getStudentId();
            String name = reservation.getName();

            // 학번과 이름을 기반으로 QR 코드 데이터 생성
            String qrData = name + studentId;

            Bitmap qrBitmap = generateQRBitmap(qrData);

            if (qrBitmap != null) {
                lockerImageView.setImageBitmap(qrBitmap);

                // QR 이미지뷰를 클릭했을 때 크게 보여주는 기능 추가
                final int position = i;
                lockerImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showLargeImage(position);
                    }
                });

                // 학번과 이름 표시를 위한 TextView를 추가하고 설정해주세요.
                // 학번과 이름을 보이도록 설정
            }
        }

        handler = new Handler();
        keyUpdateRunnable = new Runnable() {
            @Override
            public void run() {
                // 예약 정보 업데이트 작업 수행
                updateReservationInfo();

                // 일정 시간마다 예약 정보 업데이트를 반복 실행
                handler.postDelayed(keyUpdateRunnable, KEY_UPDATE_INTERVAL);
            }
        };

        // 예약 정보 업데이트 작업을 최초 실행
        updateReservationInfo();
    }

    // 큰 이미지 보여주기
    private void showLargeImage(int position) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_large_image);

        ImageView imageView = dialog.findViewById(R.id.imageViewLarge);
        Button closeButton = dialog.findViewById(R.id.buttonClose);

        // Get the bitmap from the lockerImageViewList based on the position
        Bitmap imageBitmap = ((BitmapDrawable) lockerImageViewList.get(position).getDrawable()).getBitmap();
        imageView.setImageBitmap(imageBitmap);

        // Close button click listener to dismiss the dialog
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // Show the dialog
        dialog.show();
    }


    // 예약 정보 업데이트
    private void updateReservationInfo() {
        // 예약 정보를 업데이트하는 작업을 수행
        // 예시로 파이어베이스에서 예약 정보를 다시 가져오는 방식을 제시하였습니다.
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reservationsRef = database.getReference("reservation");
        reservationsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                reservationList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // 예약 정보를 가져오는 방법에 따라 수정해주세요.
                    // 예시로 학번과 이름을 가져오는 방법을 제시하였습니다.
                    String studentId = snapshot.child("studentId").getValue(String.class);
                    String name = snapshot.child("name").getValue(String.class);

                    Reservations reservation = new Reservations(studentId, name);
                    reservationList.add(reservation);
                }

                // 예약 내역을 기반으로 QR 코드 생성
                generateQRCode();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 예외 처리
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 여기에서 handler 사용
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 원하는 작업 수행
            }
        }, 1000); // 예시로 1초 후에 실행되도록 설정
    }


    @Override
    protected void onPause() {
        super.onPause();

        // 액티비티가 화면에서 사라질 때마다 예약 정보 업데이트 작업을 중지
        handler.removeCallbacks(keyUpdateRunnable);
    }



    // QR 코드 데이터 생성


    // QR 코드 비트맵 생성
    private Bitmap generateQRBitmap(String qrData) {
        try {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix bitMatrix = writer.encode(qrData, BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }

            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }

        return null;


    }


}
