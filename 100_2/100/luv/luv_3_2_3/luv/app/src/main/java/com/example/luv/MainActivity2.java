package com.example.luv;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import java.io.File;


public class MainActivity2 extends AppCompatActivity {

    private PopupWindow popupWindow;
    private TextView personTextView;
    private View popupView;
    private TextView phoneTextView;
    private TextView emailTextView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView imageView2 = findViewById(R.id.embedded);
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2.this, PdfViewerActivity1.class);
                startActivity(intent);
            }
        });


        ImageView imageView10 = findViewById(R.id.room1814);
        imageView10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 엑셀 파일 이름 설정
                String pdfFileName = "c1814.pdf";

                // PDF 파일 경로 설정
                String pdfFilePath = "file:///android_asset/" + pdfFileName;

                Intent intent = new Intent(MainActivity2.this, PdfViewerActivity1.class);
                intent.putExtra("assets", "c1814.pdf");
                startActivity(intent);
            }
        });
        ImageView imageView11 = findViewById(R.id.room1813);
        imageView11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 엑셀 파일 이름 설정
                String pdfFileName = "c1813.pdf";

                // PDF 파일 경로 설정
                String pdfFilePath = "file:///android_asset/" + pdfFileName;

                Intent intent = new Intent(MainActivity2.this, PdfViewerActivity1.class);
                intent.putExtra("assets", "c1813.pdf");
                startActivity(intent);
            }
        });
        ImageView imageView22 = findViewById(R.id.room1817);
        imageView22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 엑셀 파일 이름 설정
                String pdfFileName = "c1817.pdf";

                // PDF 파일 경로 설정
                String pdfFilePath = "file:///android_asset/" + pdfFileName;

                Intent intent = new Intent(MainActivity2.this, PdfViewerActivity1.class);
                intent.putExtra("assets", "c1817.pdf");
                startActivity(intent);
            }
        });
        ImageView imageView25 = findViewById(R.id.room1818);
        imageView25.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 엑셀 파일 이름 설정
                String pdfFileName = "c1818.pdf";

                // PDF 파일 경로 설정
                String pdfFilePath = "file:///android_asset/" + pdfFileName;

                Intent intent = new Intent(MainActivity2.this, PdfViewerActivity1.class);
                intent.putExtra("assets", "c1818.pdf");
                startActivity(intent);
            }
        });
        ImageView imageView30 = findViewById(R.id.room1824);
        imageView30.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 엑셀 파일 이름 설정
                String pdfFileName = "c1824.pdf";

                // PDF 파일 경로 설정
                String pdfFilePath = "file:///android_asset/" + pdfFileName;

                Intent intent = new Intent(MainActivity2.this, PdfViewerActivity1.class);
                intent.putExtra("assets", "c1824.pdf");
                startActivity(intent);
            }
        });

        // 팝업 뷰를 만듭니다.
        popupView = LayoutInflater.from(this).inflate(R.layout.popup_view, null);
        popupView.findViewById(R.id.phoneTextView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 전화 걸기 앱 실행
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:010-5262-5862"));
                startActivity(intent);
                popupWindow.dismiss();
            }
        });
        popupView.findViewById(R.id.emailTextView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 이메일 보내기 앱 실행
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:example@example.com"));
                startActivity(intent);
                popupWindow.dismiss();
            }
        });


        ImageView imageView = findViewById(R.id.profKim);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 팝업 뷰를 인플레이트하여 생성합니다.
                LayoutInflater inflater = LayoutInflater.from(MainActivity2.this);
                View popupView = inflater.inflate(R.layout.popup_view, null);

                // 팝업 뷰의 UI 요소 찾기
                TextView phoneTextView = popupView.findViewById(R.id.phoneTextView);
                TextView emailTextView = popupView.findViewById(R.id.emailTextView);
                TextView personTextView = popupView.findViewById(R.id.phoneTextView4);

                // 전화번호, 이메일, 교수님 이름 설정
                phoneTextView.setText("010-xxxx-xxxx");
                emailTextView.setText("jhkim@kduniv.ac.kr");
                personTextView.setText("김종윤 교수님");

                // 팝업 창 생성 및 설정
                PopupWindow popupWindow = new PopupWindow(popupView, 500, 800, true);

                // 팝업 창을 나타낼 위치 계산
                int[] location = new int[2];
                v.getLocationOnScreen(location);
                int x = location[0] - popupWindow.getWidth();
                int y = location[1] - popupWindow.getHeight();

                // 팝업 창을 나타냅니다.
                popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, x, y);

                // 팝업 창 닫기 버튼 클릭 리스너 설정


            }
        });

        ImageView imageView35 = findViewById(R.id.profJong);
        imageView35.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 팝업 뷰를 인플레이트하여 생성합니다.
                LayoutInflater inflater = LayoutInflater.from(MainActivity2.this);
                View popupView = inflater.inflate(R.layout.popup_view, null);

                // 팝업 뷰의 UI 요소 찾기
                TextView phoneTextView = popupView.findViewById(R.id.phoneTextView);
                TextView emailTextView = popupView.findViewById(R.id.emailTextView);
                TextView personTextView = popupView.findViewById(R.id.phoneTextView4);

                // 전화번호, 이메일, 교수님 이름 설정
                phoneTextView.setText("010-xxxx-xxxx");
                emailTextView.setText("jhkim@kduniv.ac.kr");
                personTextView.setText("김종훈 교수님");

                // 팝업 창 생성 및 설정
                PopupWindow popupWindow = new PopupWindow(popupView, 500, 800, true);

                // 팝업 창을 나타낼 위치 계산
                int[] location = new int[2];
                v.getLocationOnScreen(location);
                int x = location[0] - popupWindow.getWidth();
                int y = location[1] - popupWindow.getHeight();

                // 팝업 창을 나타냅니다.
                popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, x, y);

                // 팝업 창 닫기 버튼 클릭 리스너 설정


            }
        });

        ImageView imageView1 = findViewById(R.id.profMin);
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 팝업 뷰를 인플레이트하여 생성합니다.
                LayoutInflater inflater = LayoutInflater.from(MainActivity2.this);
                View popupView = inflater.inflate(R.layout.popup_view, null);

                // 팝업 뷰의 UI 요소 찾기
                TextView phoneTextView = popupView.findViewById(R.id.phoneTextView);
                TextView emailTextView = popupView.findViewById(R.id.emailTextView);
                TextView personTextView = popupView.findViewById(R.id.phoneTextView4);

                // 전화번호, 이메일, 교수님 이름 설정
                phoneTextView.setText("010-xxxx-xxxx");
                emailTextView.setText("jhkim@kduniv.ac.kr");
                personTextView.setText("민준식 교수님");

                // 팝업 창 생성 및 설정
                PopupWindow popupWindow = new PopupWindow(popupView, 500, 800, true);

                // 팝업 창을 나타낼 위치 계산
                int[] location = new int[2];
                v.getLocationOnScreen(location);
                int x = location[0] - popupWindow.getWidth();
                int y = location[1] - popupWindow.getHeight();

                // 팝업 창을 나타냅니다.
                popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, x, y);

                // 팝업 창 닫기 버튼 클릭 리스너 설정


            }
        });
        ImageView imageView12 = findViewById(R.id.profLee);
        imageView12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 팝업 뷰를 인플레이트하여 생성합니다.
                LayoutInflater inflater = LayoutInflater.from(MainActivity2.this);
                View popupView = inflater.inflate(R.layout.popup_view, null);

                // 팝업 뷰의 UI 요소 찾기
                TextView phoneTextView = popupView.findViewById(R.id.phoneTextView);
                TextView emailTextView = popupView.findViewById(R.id.emailTextView);
                TextView personTextView = popupView.findViewById(R.id.phoneTextView4);

                // 전화번호, 이메일, 교수님 이름 설정
                phoneTextView.setText("010-xxxx-xxxx");
                emailTextView.setText("jhkim@kduniv.ac.kr");
                personTextView.setText("이용권 교수님");

                // 팝업 창 생성 및 설정
                PopupWindow popupWindow = new PopupWindow(popupView, 500, 800, true);

                // 팝업 창을 나타낼 위치 계산
                int[] location = new int[2];
                v.getLocationOnScreen(location);
                int x = location[0] - popupWindow.getWidth();
                int y = location[1] - popupWindow.getHeight();

                // 팝업 창을 나타냅니다.
                popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, x, y);

                // 팝업 창 닫기 버튼 클릭 리스너 설정


            }
        });
        ImageView imageView13 = findViewById(R.id.profNam);
        imageView13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 팝업 뷰를 인플레이트하여 생성합니다.
                LayoutInflater inflater = LayoutInflater.from(MainActivity2.this);
                View popupView = inflater.inflate(R.layout.popup_view, null);

                // 팝업 뷰의 UI 요소 찾기
                TextView phoneTextView = popupView.findViewById(R.id.phoneTextView);
                TextView emailTextView = popupView.findViewById(R.id.emailTextView);
                TextView personTextView = popupView.findViewById(R.id.phoneTextView4);

                // 전화번호, 이메일, 교수님 이름 설정
                phoneTextView.setText("010-xxxx-xxxx");
                emailTextView.setText("jhkim@kduniv.ac.kr");
                personTextView.setText("남상복 교수님");

                // 팝업 창 생성 및 설정
                PopupWindow popupWindow = new PopupWindow(popupView, 500, 800, true);

                // 팝업 창을 나타낼 위치 계산
                int[] location = new int[2];
                v.getLocationOnScreen(location);
                int x = location[0] - popupWindow.getWidth();
                int y = location[1] - popupWindow.getHeight();

                // 팝업 창을 나타냅니다.
                popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, x, y);

                // 팝업 창 닫기 버튼 클릭 리스너 설정


            }
        });
        ImageView imageView14 = findViewById(R.id.profYang);
        imageView14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 팝업 뷰를 인플레이트하여 생성합니다.
                LayoutInflater inflater = LayoutInflater.from(MainActivity2.this);
                View popupView = inflater.inflate(R.layout.popup_view, null);

                // 팝업 뷰의 UI 요소 찾기
                TextView phoneTextView = popupView.findViewById(R.id.phoneTextView);
                TextView emailTextView = popupView.findViewById(R.id.emailTextView);
                TextView personTextView = popupView.findViewById(R.id.phoneTextView4);

                // 전화번호, 이메일, 교수님 이름 설정
                phoneTextView.setText("010-xxxx-xxxx");
                emailTextView.setText("jhkim@kduniv.ac.kr");
                personTextView.setText("양일호 교수님");

                // 팝업 창 생성 및 설정
                PopupWindow popupWindow = new PopupWindow(popupView, 500, 800, true);

                // 팝업 창을 나타낼 위치 계산
                int[] location = new int[2];
                v.getLocationOnScreen(location);
                int x = location[0] - popupWindow.getWidth();
                int y = location[1] - popupWindow.getHeight();

                // 팝업 창을 나타냅니다.
                popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, x, y);

                // 팝업 창 닫기 버튼 클릭 리스너 설정


            }
        });
    }









}










