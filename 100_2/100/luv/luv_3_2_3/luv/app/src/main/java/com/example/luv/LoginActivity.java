package com.example.luv;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    // Firebase Authentication 인스턴스 생성
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    // View 변수 생성
    private EditText mEmailEditText, mPasswordEditText;
    private Button mLoginButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Firebase 인스턴스 초기화
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if(currentUser != null){ // 로그인된 사용자 정보가 있다면
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }



        // View 초기화
        mEmailEditText = findViewById(R.id.emailEditText);
        mPasswordEditText = findViewById(R.id.passwordEditText);
        mLoginButton = findViewById(R.id.button2);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);


            }
        });


        // 로그인 버튼 클릭 이벤트 설정
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }

    // ...

    private void loginUser() {
        // 이메일, 비밀번호 정보 가져오기
        String email = mEmailEditText.getText().toString().trim();
        String password = mPasswordEditText.getText().toString().trim();

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

        // Firebase Authentication으로 사용자 인증
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // 로그인 성공
                        currentUser = mAuth.getCurrentUser(); // 로그인된 사용자 정보 가져오기
                        if (isAdministrator(currentUser)) {
                            // 관리자 계정으로 로그인 성공, 관리자 페이지로 이동
                            Intent intent = new Intent(LoginActivity.this, AdministratorActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // 일반 사용자 계정으로 로그인 성공, 메인 페이지로 이동 또는 다른 처리 수행
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        // 로그인 실패
                        Toast.makeText(LoginActivity.this, "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

// ...

    private boolean isAdministrator(FirebaseUser user) {
        // 관리자 계정으로 인정할 조건을 설정하여 확인합니다.
        return user != null && user.getEmail().equals("audrhks1618@naver.com");
    }
// ...


    // 이메일 유효성 검사 메서드
    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // 비밀번호 유효성 검사 메서드
    private boolean isValidPassword(String password) {
        return !TextUtils.isEmpty(password) && password.length() >= 6;
    }

}
