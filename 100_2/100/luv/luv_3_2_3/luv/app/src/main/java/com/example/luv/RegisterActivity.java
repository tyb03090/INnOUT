package com.example.luv;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {

    // Firebase Authentication 인스턴스 생성
    private FirebaseAuth mAuth;

    // Realtime Database 인스턴스 생성
    private DatabaseReference mDatabase;

    // View 변수 생성
    private EditText mEmailEditText, mPasswordEditText, mNameEditText, mStudentIdEditText;
    private Button mRegisterButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Firebase 인스턴스 초기화
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // View 초기화
        mEmailEditText = findViewById(R.id.emailEditText);
        mPasswordEditText = findViewById(R.id.passwordEditText);
        mNameEditText = findViewById(R.id.nameEditText);
        mNameEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);

        mStudentIdEditText = findViewById(R.id.studentIdEditText);
        mRegisterButton = findViewById(R.id.button3);

        // 회원가입 버튼 클릭 이벤트 설정
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        // 이메일, 비밀번호, 이름, 학번 정보 가져오기
        String email = mEmailEditText.getText().toString().trim();
        String password = mPasswordEditText.getText().toString().trim();
        String name = mNameEditText.getText().toString().trim();

        mNameEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME);

        String studentId = mStudentIdEditText.getText().toString().trim();


        // 이메일과 비밀번호가 유효한지 확인
        if (!isValidEmail(email)) {
            mEmailEditText.setError("유효한 이메일을 입력하세요.");
            mEmailEditText.requestFocus();
            return;
        }

        if (!isValidPassword(password)) {
            mPasswordEditText.setError("6자리 이상의 비밀번호를 입력하세요.");
            mPasswordEditText.requestFocus();
            return;
        }

        mDatabase.child("users").orderByChild("studentId").equalTo(studentId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            // 중복된 학번이 이미 존재하는 경우
                            mStudentIdEditText.setError("이미 등록된 학번입니다.");
                            mStudentIdEditText.requestFocus();
                        } else {
                            // 중복된 학번이 없는 경우
                            // Firebase Authentication으로 새 사용자 등록
                            mAuth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            // 회원가입 성공
                                            String userId = mAuth.getCurrentUser().getUid();
                                            User newUser = new User(name, studentId);
                                            // Realtime Database에 사용자 정보 저장
                                            mDatabase.child("users").child(userId).setValue(newUser)
                                                    .addOnCompleteListener(databaseTask -> {
                                                        if (databaseTask.isSuccessful()) {
                                                            // 사용자 정보 저장 성공
                                                            // 사용자 프로필 업데이트
                                                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                                    .setDisplayName(name)
                                                                    .build();
                                                            mAuth.getCurrentUser().updateProfile(profileUpdates)
                                                                    .addOnCompleteListener(profileTask -> {
                                                                        if (profileTask.isSuccessful()) {
                                                                            // 프로필 업데이트 성공
                                                                            // 회원가입 완료 후 홈 화면으로 이동
                                                                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                                                            startActivity(intent);
                                                                            finish();
                                                                        } else {
                                                                            // 프로필 업데이트 실패
                                                                            Toast.makeText(RegisterActivity.this, "회원가입에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    });
                                                        } else {
                                                            // 사용자 정보 저장 실패
                                                            Toast.makeText(RegisterActivity.this, "회원가입에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        } else {
                                            // 회원가입 실패
                                            Toast.makeText(RegisterActivity.this, "회원가입에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // 조회 실패
                        Toast.makeText(RegisterActivity.this, "학번 중복 체크에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    // 이메일 유효성 검사 메서드
    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // 비밀번호 유효성 검사 메서드
    private boolean isValidPassword(String password) {
        return !TextUtils.isEmpty(password) && password.length() >= 6;
    }
}
