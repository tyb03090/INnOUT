package com.example.luv;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ReviewActivity extends AppCompatActivity {
    private EditText titleEditText, contentEditText, lockerNumberEditText;
    private RatingBar ratingBar;
    private Button addPhotoButton, submitReviewButton;
    private ImageView photoImageView;
    private int selectedScore = 0;

    private static final int REQUEST_CODE_ADD_PHOTO = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        titleEditText = findViewById(R.id.titleEditText);
        contentEditText = findViewById(R.id.contentEditText);
        lockerNumberEditText = findViewById(R.id.lockerNumberEditText);


        addPhotoButton = findViewById(R.id.addPhotoButton);
        submitReviewButton = findViewById(R.id.submitReviewButton);
        photoImageView = findViewById(R.id.photoImageView);

        submitReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleEditText.getText().toString();
                String content = contentEditText.getText().toString();
                String lockerNumber = lockerNumberEditText.getText().toString();

                Bitmap photo = ((BitmapDrawable) photoImageView.getDrawable()).getBitmap();
                File photoFile = savePhotoToFile(photo);
                // Convert bitmap to byte array
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                Intent resultIntent = new Intent();
                resultIntent.putExtra("title", title);
                resultIntent.putExtra("content", content);
                resultIntent.putExtra("lockerNumber", lockerNumber);
                resultIntent.putExtra("photo", byteArray); // Pass the byte array instead of the file path
                resultIntent.putExtra("score", selectedScore);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });


        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE_ADD_PHOTO);
            }
        });
        Button score1Button = findViewById(R.id.score1Button);
        Button score2Button = findViewById(R.id.score2Button);
        Button score3Button = findViewById(R.id.score3Button);
        Button score4Button = findViewById(R.id.score4Button);
        Button score5Button = findViewById(R.id.score5Button);

        score1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedScore = 1;
                updateScoreLabel();
            }
        });

        score2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedScore = 2;
                updateScoreLabel();

            }
        });

        score3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedScore = 3;
                updateScoreLabel();
            }
        });

        score4Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedScore = 4;
                updateScoreLabel();
            }
        });

        score5Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedScore = 5;
                updateScoreLabel();
            }
        });
    }

    // Update the score label
    private void updateScoreLabel() {
        TextView scoreLabel = findViewById(R.id.scoreLabel);
        scoreLabel.setText("점수: " + selectedScore + "/5");
    }

    // Save the photo to a file
    private File savePhotoToFile(Bitmap bitmap) {
        // Create a file in the app's internal storage directory
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File directory = contextWrapper.getDir("photos", Context.MODE_PRIVATE);
        File photoFile = new File(directory, "review_photo.png");

        // Compress and save the bitmap to the file
        try (FileOutputStream outputStream = new FileOutputStream(photoFile)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return photoFile;
    }


    // Handle the result of selecting a photo from the gallery

    // Handle the result of selecting a photo from the gallery
    // Handle the result of selecting a photo from the gallery
    // Handle the result of selecting a photo from the gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_PHOTO && resultCode == RESULT_OK) {
            // Get the selected photo from the gallery
            if (data != null && data.getData() != null) {
                // Set the image URI
                Uri imageUri = data.getData();
                photoImageView.setImageURI(imageUri);

                // Wait until the image view is rendered
                photoImageView.post(new Runnable() {
                    @Override
                    public void run() {
                        // Resize the bitmap to desired dimensions
                        Bitmap bitmap = null;
                        try {
                            bitmap = resizeBitmapFromUri(imageUri, 100, 100);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                        // Set the bitmap to the image view
                        photoImageView.setImageBitmap(bitmap);
                    }
                });
            }
        }
    }

    // Resize the bitmap to the desired dimensions
    private Bitmap resizeBitmapFromUri(Uri uri, int reqWidth, int reqHeight) throws IOException {
        InputStream inputStream = getContentResolver().openInputStream(uri);

        // Decode the bitmap with the desired dimensions
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(inputStream, null, options);
        inputStream.close();

        int imageWidth = options.outWidth;
        int imageHeight = options.outHeight;
        int scaleFactor = Math.min(imageWidth / reqWidth, imageHeight / reqHeight);

        options.inJustDecodeBounds = false;
        options.inSampleSize = scaleFactor;
        inputStream = getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
        inputStream.close();

        return bitmap;
    }

}
