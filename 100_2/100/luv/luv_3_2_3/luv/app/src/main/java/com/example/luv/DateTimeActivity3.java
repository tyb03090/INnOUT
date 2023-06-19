package com.example.luv;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class DateTimeActivity3 extends AppCompatActivity {

    private DatePicker datePicker;
    private TimePicker timePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_time3);

        datePicker = findViewById(R.id.date_picker3);
        timePicker = findViewById(R.id.time_picker3);

        // 현재 날짜 가져오기
        final Calendar calendar = Calendar.getInstance();
        final int currentYear = calendar.get(Calendar.YEAR);
        final int currentMonth = calendar.get(Calendar.MONTH);
        final int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        // 현재 날짜로 DatePicker 초기화
        datePicker.init(currentYear, currentMonth, currentDay, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, monthOfYear, dayOfMonth);

                // 선택된 날짜가 현재 날짜 이전인 경우, 선택을 비활성화
                if (selectedDate.before(calendar)) {
                    // 현재 날짜로 다시 초기화
                    datePicker.updateDate(currentYear, currentMonth, currentDay);
                } else {
                    // 선택 가능한 범위 내의 날짜인지 확인
                    Calendar minDate = Calendar.getInstance();
                    minDate.add(Calendar.MONTH, 1); // 현재 날짜로부터 한 달 이후
                    Calendar maxDate = Calendar.getInstance();
                    maxDate.add(Calendar.MONTH, 4); // 현재 날짜로부터 네 달 이전

                    if (selectedDate.before(minDate) || selectedDate.after(maxDate)) {
                        // 선택 불가능한 날짜인 경우, 현재 날짜로 다시 초기화
                        datePicker.updateDate(currentYear, currentMonth, currentDay);
                    } else {
                        // 선택 가능한 범위 내의 날짜
                        // 처리를 추가하거나 표시할 수 있습니다.
                    }
                }
            }
        });

        // 확인 버튼 클릭 이벤트
        Button confirmButton = findViewById(R.id.reserve_button3);
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