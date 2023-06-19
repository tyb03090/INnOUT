package com.example.luv;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luv.R;
import com.example.luv.Review;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private List<Review> reviewList;
    private String currentUser;
    private LayoutInflater inflater;

    public ReviewAdapter(Context context, List<Review> reviewList, String currentUser) {
        this.reviewList = reviewList;
        this.currentUser = currentUser;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.review_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Review review = reviewList.get(position);

        holder.titleTextView.setText(review.getTitle());
        holder.contentTextView.setText(review.getContent());
        holder.ratingTextView.setText(String.valueOf(review.getScore()));
        holder.lockerNumberTextView.setText(review.getLockerNumber());

        // 작성자와 현재 사용자를 비교하여 삭제 버튼의 가시성 설정
        if (review.getAuthor().equals(currentUser)) {
            holder.deleteButton.setVisibility(View.VISIBLE);
        } else {
            holder.deleteButton.setVisibility(View.GONE);
        }

        // 사진(photo) 표시
        Bitmap photo = review.getPhoto();
        if (photo != null) {
            holder.photoImageView.setImageBitmap(photo);
            holder.photoImageView.setVisibility(View.VISIBLE);
        } else {
            holder.photoImageView.setVisibility(View.GONE);
        }

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    // 클릭한 리뷰 삭제
                    reviewList.remove(position);
                    notifyItemRemoved(position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView contentTextView;
        TextView ratingTextView;
        TextView lockerNumberTextView;
        ImageView photoImageView;
        Button deleteButton;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            contentTextView = itemView.findViewById(R.id.contentTextView);
            ratingTextView = itemView.findViewById(R.id.scoreLabel1);
            lockerNumberTextView = itemView.findViewById(R.id.lockerNumberTextView);
            photoImageView = itemView.findViewById(R.id.photoImageView);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
