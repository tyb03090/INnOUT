package com.example.luv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class TimeActivity1 extends AppCompatActivity {
    private Button addReviewButton;
    private String currentUser;
    private EditText titleEditText, contentEditText, lockerNumberEditText;
    private RatingBar ratingBar;
    private Button addPhotoButton, submitReviewButton;
    private List<Review> reviewList;
    private ReviewAdapter reviewAdapter;
    private RecyclerView reviewRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time1);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        currentUser= String.valueOf(firebaseAuth.getCurrentUser());
        String author = currentUser;

        // Initialize table layout
        addReviewButton = findViewById(R.id.addReviewButton);
        addReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TimeActivity1.this, ReviewActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        // Initialize the review list and adapter
        reviewList = new ArrayList<>();
        reviewAdapter = new ReviewAdapter(this, reviewList, currentUser);

        // Get the review recycler view
        reviewRecyclerView = findViewById(R.id.reviewRecyclerView);
        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        reviewRecyclerView.setAdapter(reviewAdapter);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String title = data.getStringExtra("title");
            String content = data.getStringExtra("content");
            String lockerNumber = data.getStringExtra("lockerNumber");

            byte[] photoByteArray = data.getByteArrayExtra("photo");
            Bitmap photo = BitmapFactory.decodeByteArray(photoByteArray, 0, photoByteArray.length);
            int score = data.getIntExtra("score", 0);

            Review review = new Review(title, content, score, lockerNumber, currentUser,photo);

            reviewList.add(review);
            reviewAdapter.notifyDataSetChanged();
        }
    }


}
