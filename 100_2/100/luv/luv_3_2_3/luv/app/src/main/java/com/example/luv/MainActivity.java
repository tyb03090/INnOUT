package com.example.luv;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.EnumMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;


public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;
    private static final String TAG = "MainActivity";
    private FirebaseAuth mAuth;
    private byte[] key;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userId = user.getUid();
    String emailID;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageButton myImageButton7 = findViewById(R.id.btn_firstarrow);
        myImageButton7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LockerActivity.class);
                startActivity(intent);
            }
        });
        ImageButton myImageButton1 = findViewById(R.id.btn_thirdarrow);
        myImageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, myuseActivity.class);
                startActivity(intent);
            }
        });
        ImageButton myImageButton5 = findViewById(R.id.btn_fivearrow2);
        myImageButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity4.class);
                startActivity(intent);
            }
        });

        ImageButton myImageButton2 = findViewById(R.id.btn_fourtharrow);
        myImageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intent);
            }
        });

        ImageButton myImageButton3 = findViewById(R.id.btn_fivearrow);
        myImageButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity3.class);
                startActivity(intent);
            }
        });
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null) {
            // Firebase Realtime Database의 학번과 이름 저장 위치
            String databaseLocation = "users/" + firebaseUser.getUid();

            // Firebase Realtime Database의 학번과 이름 데이터 가져오기
            FirebaseDatabase.getInstance().getReference(databaseLocation)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // 데이터를 가져와서 QR 코드 생성하기
                            String studentNumber = dataSnapshot.child("studentId").getValue(String.class);
                            String name = dataSnapshot.child("name").getValue(String.class);

                            // 학번과 이름을 조합하여 문자열로 만들기
                            String qrData = studentNumber  + name;

                            // QR 코드 생성하기
                            generateQRCode(qrData);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // 에러 처리
                        }
                    });
        } else {
            // 로그인하지 않은 사용자 처리
        }

        mFirebaseAuth = FirebaseAuth.getInstance();
// 타이머 시작
        startKeyChangeTimer();

    }
    // QR 코드 생성하기
    private void generateQRCode(String qrData) {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(qrData, BarcodeFormat.QR_CODE, 200, 200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap qrBitmap = barcodeEncoder.createBitmap(bitMatrix);

            // 생성된 QR 코드를 ImageView에 설정하기
            ImageView qrImageView = findViewById(R.id.imageView2);
            qrImageView.setImageBitmap(qrBitmap);

            // 학번과 이름을 Log로 출력하여 확인하기
            String[] data = qrData.split(" ");  // 학번과 이름 구분
            if (data.length >= 2) {
                Log.d(TAG, "학번: " + data[0]);
                Log.d(TAG, "이름: " + data[1]);
            } else {
                Log.d(TAG, "잘못된 데이터 형식: " + qrData);
            }
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // AES 키 생성
    private void generateAESKey () {
        key = new byte[16];
        new SecureRandom().nextBytes(key);
    }

    // 키 변경 타이머 시작
    private void startKeyChangeTimer () {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                generateAESKey();
                // 새로운 AES 키로 QR 코드 업데이트
                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                if (firebaseUser != null) {
                    String databaseLocation = "users/" + firebaseUser.getUid();
                    FirebaseDatabase.getInstance().getReference(databaseLocation)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String studentNumber = dataSnapshot.child("studentId").getValue(String.class);
                                    String name = dataSnapshot.child("name").getValue(String.class);
                                    String qrData = studentNumber  + name;
                                    generateQRCode(qrData);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    // 에러 처리
                                }
                            });
                }
            }
        };
        timer.scheduleAtFixedRate(task, 0, 30000);  // 30초마다 실행
    }


}




