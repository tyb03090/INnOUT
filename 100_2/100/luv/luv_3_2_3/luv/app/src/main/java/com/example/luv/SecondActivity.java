package com.example.luv;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

public class SecondActivity extends AppCompatActivity {

    private TextView textView;
    private Workbook workbook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        textView = findViewById(R.id.textView);

        try {
            readTimetableFromExcel();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            throw new RuntimeException(e);
        }
    }

    private void readTimetableFromExcel() throws IOException, XmlPullParserException {
        // 엑셀 파일 로드
        InputStream inputStream = getAssets().open("1818.xlsx");
        workbook = new XSSFWorkbook(inputStream);

        // 원하는 시트 선택
        Sheet sheet = workbook.getSheetAt(0);

        // 시간표 데이터 읽기
        StringBuilder timetableData = new StringBuilder();
        for (Row row : sheet) {
            for (Cell cell : row) {
                CellType cellType = cell.getCellType();
                if (cellType == CellType.STRING) {
                    timetableData.append(cell.getStringCellValue()).append("\t");
                } else if (cellType == CellType.NUMERIC) {
                    timetableData.append(cell.getNumericCellValue()).append("\t");
                }
            }
            timetableData.append("\n");
        }

        // 시간표 데이터 표시
        textView.setText(timetableData.toString());

        // 엑셀 파일 닫기
        if (workbook != null) {
            workbook.close();
        }
    }
}