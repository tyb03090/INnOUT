package com.example.luv;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class PdfViewerActivity2 extends AppCompatActivity {

    private static final String PDF_FILE_NAME = "c1813.pdf"; // PDF 파일 이름
    private static final String PDF_ASSET_PATH = PDF_FILE_NAME;

    // PDF 파일의 애셋 경로

    private ImageView pdfImageView;
    private Button closeButton;

    private PdfRenderer pdfRenderer;
    private PdfRenderer.Page currentPage;
    private ParcelFileDescriptor parcelFileDescriptor;

    public static void start(Context context) {
        Intent intent = new Intent(context, PdfViewerActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);

        pdfImageView = findViewById(R.id.pdf_image_view);
        closeButton = findViewById(R.id.close_button);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // PDF 파일을 애셋에서 복사하여 임시 파일로 저장
        File pdfFile = copyPdfFromAsset();

        if (pdfFile != null) {
            // 임시 파일로부터 PDF 렌더러 생성
            createPdfRenderer(pdfFile);
            // 첫 번째 페이지 표시
            showPage(0);
        } else {
            Toast.makeText(this, "Failed to load PDF", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        closeRenderer();
        super.onDestroy();
    }

    private File copyPdfFromAsset() {
        try {
            // 임시 파일 생성
            File outputFile = new File(getCacheDir(), PDF_FILE_NAME);

            if (!outputFile.exists()) {
                // 애셋에서 PDF 파일 열기
                InputStream inputStream = getAssets().open(PDF_ASSET_PATH);
                OutputStream outputStream = new FileOutputStream(outputFile);

                // 파일 복사
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }

                // 자원 해제
                outputStream.flush();
                outputStream.close();
                inputStream.close();
            }

            return outputFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void createPdfRenderer(File file) {
        try {
            parcelFileDescriptor =
                    ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
            pdfRenderer = new PdfRenderer(parcelFileDescriptor);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showPage(int index) {
        if (pdfRenderer != null) {
            // 이전 페이지 닫기
            if (currentPage != null) {
                currentPage.close();
            }

            // 해당 인덱스의 페이지 열기
            currentPage = pdfRenderer.openPage(index);

            // 페이지를 비트맵으로 변환하여 ImageView에 표시
            int width = getResources().getDisplayMetrics().densityDpi / 72 * currentPage.getWidth();
            int height = getResources().getDisplayMetrics().densityDpi / 72 * currentPage.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            currentPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
            pdfImageView.setImageBitmap(bitmap);
        }
    }

    private void closeRenderer() {
        if (currentPage != null) {
            currentPage.close();
        }
        if (pdfRenderer != null) {
            pdfRenderer.close();
        }
        try {
            if (parcelFileDescriptor != null) {
                parcelFileDescriptor.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
