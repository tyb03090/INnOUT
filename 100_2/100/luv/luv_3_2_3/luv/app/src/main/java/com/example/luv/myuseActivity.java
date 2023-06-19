package com.example.luv;

import static android.content.ContentValues.TAG;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class myuseActivity extends AppCompatActivity {

    private Switch notificationSwitch;
    private boolean notificationEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myuse);

        ImageView logoutButton = findViewById(R.id.imageView5);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 로그아웃 처리
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();

                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        ImageView withdrawButton = findViewById(R.id.djjdj);
        withdrawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 회원 탈퇴 처리
                showWithdrawDialog();
            }
        });

        ImageView emailButton = findViewById(R.id.aool);
        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 이메일 보내기
                sendEmail();
            }
        });

        ImageView changePasswordButton = findViewById(R.id.btnChangePassword);
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 비밀번호 변경
                showChangePasswordDialog();
            }
        });

        notificationSwitch = findViewById(R.id.toggle_switch);
        notificationEnabled = getNotificationState();
        notificationSwitch.setChecked(notificationEnabled);

        notificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            notificationEnabled = isChecked;
            setNotificationState(notificationEnabled);
            if (notificationEnabled) {
                showNotification();
            } else {
                hideNotification();
            }
        });
    }

    private boolean getNotificationState() {
        SharedPreferences sharedPreferences = getSharedPreferences("notification_prefs", MODE_PRIVATE);
        return sharedPreferences.getBoolean("notification_enabled", false);
    }

    private void setNotificationState(boolean enabled) {
        SharedPreferences sharedPreferences = getSharedPreferences("notification_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("notification_enabled", enabled);
        editor.apply();
    }

    private void showNotification() {
        startService(new Intent(this, MyFirebaseMessagingService.class));
    }

    private void hideNotification() {
        stopService(new Intent(this, MyFirebaseMessagingService.class));
    }


    private void showWithdrawDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("회원 탈퇴");
        builder.setMessage("정말로 회원을 탈퇴하시겠습니까?");
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 회원 탈퇴 처리
                withdrawUser();
            }
        });
        builder.setNegativeButton("아니오", null);
        builder.show();
    }

    private void withdrawUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Firebase Realtime Database에서 사용자 데이터 삭제
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
            userRef.removeValue()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            // 사용자 데이터 삭제 성공

                            // 사용자 프로필 삭제
                            user.delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            // 사용자 프로필 삭제 성공
                                            // 추가적인 작업이 필요하다면 여기에 작성하세요.

                                            // 로그아웃 처리
                                            FirebaseAuth.getInstance().signOut();

                                            // 회원 탈퇴 후 처리 작업을 진행하세요.
                                            // 예시: 회원 가입 창으로 이동
                                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // 사용자 프로필 삭제 실패
                                            // 추가적인 작업이 필요하다면 여기에 작성하세요.
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // 사용자 데이터 삭제 실패
                            // 추가적인 작업이 필요하다면 여기에 작성하세요.
                        }
                    });
        }
    }

    private void sendEmail() {
        final String EMAIL_ADDRESS = "audrhks1618@naver.com";
        final String EMAIL_SUBJECT = "문의사항";

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{EMAIL_ADDRESS});
        intent.putExtra(Intent.EXTRA_SUBJECT, EMAIL_SUBJECT);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(Intent.createChooser(intent, "이메일 앱 선택"));
        } else {
            Log.e(TAG, "이메일 앱이 설치되어 있지 않습니다.");
        }
    }

    private void showChangePasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("비밀번호 변경");
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_change_password, null);
        builder.setView(dialogView);

        final EditText etCurrentPassword = dialogView.findViewById(R.id.etCurrentPassword);
        final EditText etNewPassword = dialogView.findViewById(R.id.etNewPassword);

        builder.setPositiveButton("변경", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String currentPassword = etCurrentPassword.getText().toString().trim();
                String newPassword = etNewPassword.getText().toString().trim();

                if (TextUtils.isEmpty(currentPassword) || TextUtils.isEmpty(newPassword)) {
                    Toast.makeText(myuseActivity.this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    changePassword(currentPassword, newPassword);
                }
            }
        });

        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void changePassword(String currentPassword, final String newPassword) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // 현재 비밀번호를 사용하여 사용자 재인증
        user.reauthenticate(EmailAuthProvider.getCredential(user.getEmail(), currentPassword))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // 새로운 비밀번호로 비밀번호 변경
                        user.updatePassword(newPassword)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(myuseActivity.this, "비밀번호가 성공적으로 변경되었습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(myuseActivity.this, "비밀번호 변경에 실패했습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(myuseActivity.this, "비밀번호 재인증에 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
