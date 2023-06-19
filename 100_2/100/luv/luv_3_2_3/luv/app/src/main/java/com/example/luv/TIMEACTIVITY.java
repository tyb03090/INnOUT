import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.luv.R;

public class TIMEACTIVITY extends AppCompatActivity {
    private static final int ROW_COUNT = 11; // 시간표의 행 개수
    private static final int COLUMN_COUNT = 6; // 시간표의 열 개수
    private static final String[] DAYS_OF_WEEK = {"월", "화", "수", "목", "금", "토"};

    private String[][] timetable = new String[ROW_COUNT][COLUMN_COUNT];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time1);

        // 시간표 데이터 초기화
        for (int i = 0; i < ROW_COUNT; i++) {
            for (int j = 0; j < COLUMN_COUNT; j++) {
                timetable[i][j] = "";
            }
        }
    }
}