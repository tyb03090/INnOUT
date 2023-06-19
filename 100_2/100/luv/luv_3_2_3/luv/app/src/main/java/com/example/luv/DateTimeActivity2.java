package com.example.luv;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class DateTimeActivity2 extends AppCompatActivity {
    private DatePicker datePicker;
    private TimePicker timePicker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_time2);

        datePicker = findViewById(R.id.date_picker2);
        timePicker = findViewById(R.id.time_picker2);

        // 현재 날짜 가져오기
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        // 최소 날짜 설정: 현재 날짜
        datePicker.setMinDate(calendar.getTimeInMillis());

        // 최대 날짜 설정: 현재 날짜로부터 2주(14일) 후
        calendar.add(Calendar.WEEK_OF_MONTH, 4);
        datePicker.setMaxDate(calendar.getTimeInMillis());

        // 확인 버튼 클릭 이벤트
        Button confirmButton = findViewById(R.id.reserve_button2);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year = datePicker.getYear();
                int month = datePicker.getMonth() + 1;
                int day = datePicker.getDayOfMonth();
                int hour = timePicker.getHour();
                int minute = timePicker.getMinute();

                // 선택한 날짜와 시간을 이전 화면으로 전달
                Intent intent = new Intent();
                intent.putExtra("year", year);
                intent.putExtra("month", month);
                intent.putExtra("day", day);
                intent.putExtra("hour", hour);
                intent.putExtra("minute", minute);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
