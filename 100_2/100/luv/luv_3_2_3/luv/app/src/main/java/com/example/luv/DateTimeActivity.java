package com.example.luv;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class DateTimeActivity extends AppCompatActivity {
    private DatePicker datePicker;
    private TimePicker timePicker;

    private TextView mLockerNumberTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_time);

        datePicker = findViewById(R.id.date_picker);
        timePicker = findViewById(R.id.time_picker);

        // 현재 날짜와 시간 가져오기
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);

        // 최소 날짜 설정: 현재 날짜
// 최소 날짜 설정: 현재 날짜
        datePicker.setMinDate(calendar.getTimeInMillis());

// 최대 날짜 설정: 현재 날짜로부터 4주(28일) 후
        calendar.add(Calendar.WEEK_OF_MONTH, 4);
        datePicker.setMaxDate(calendar.getTimeInMillis());


        // 현재 날짜와 시간으로 DatePicker와 TimePicker 초기화
        datePicker.init(currentYear, currentMonth, currentDay, null);
        timePicker.setCurrentHour(currentHour);
        timePicker.setCurrentMinute(currentMinute);

        // 확인 버튼을 클릭하면 선택한 날짜와 시간을 이전 화면으로 전달
        Button confirmButton = findViewById(R.id.reserve_button);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year = datePicker.getYear();
                int month = datePicker.getMonth() + 1;
                int day = datePicker.getDayOfMonth();
                int hour = timePicker.getCurrentHour();
                int minute = timePicker.getCurrentMinute();

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
