package com.example.luv;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class DateTimeActivity4 extends AppCompatActivity {
    private DatePicker datePicker;
    private TimePicker timePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_time4);

        datePicker = findViewById(R.id.date_picker4);
        timePicker = findViewById(R.id.time_picker4);

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

                // 현재 날짜와 동일한 경우 선택을 비활성화
                if (selectedDate.get(Calendar.YEAR) == currentYear &&
                        selectedDate.get(Calendar.MONTH) == currentMonth &&
                        selectedDate.get(Calendar.DAY_OF_MONTH) == currentDay) {
                    datePicker.updateDate(currentYear, currentMonth, currentDay);
                }

                // 선택된 날짜가 현재 날짜로부터 1달 이후부터 4달 이하인 경우 선택을 활성화
                Calendar minDate = Calendar.getInstance();
                minDate.add(Calendar.MONTH, 1);

                Calendar maxDate = Calendar.getInstance();
                maxDate.add(Calendar.MONTH, 4);

                if (selectedDate.after(minDate) && selectedDate.before(maxDate)) {
                    // 선택 가능한 범위 내의 날짜
                    // 처리를 추가하거나 표시할 수 있습니다.
                } else {
                    // 선택 불가능한 날짜
                    datePicker.updateDate(currentYear, currentMonth, currentDay);
                }
            }
        });

        // 확인 버튼 클릭 이벤트
        Button confirmButton = findViewById(R.id.reserve_button4);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year = datePicker.getYear();
                int month = datePicker.getMonth() + 1;
                int day = datePicker.getDayOfMonth();
                int hour, minute;

                // API level에 따라 시간 정보 가져오는 방법 변경
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    hour = timePicker.getHour();
                    minute = timePicker.getMinute();
                } else {
                    hour = timePicker.getCurrentHour();
                    minute = timePicker.getCurrentMinute();
                }

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
