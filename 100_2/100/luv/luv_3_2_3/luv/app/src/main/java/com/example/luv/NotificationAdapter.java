package com.example.luv;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private List<Notifications> mNotifications;

    public NotificationAdapter(List<Notifications> notifications) {
        mNotifications = notifications;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notifications notification = mNotifications.get(position);
        holder.messageTextView.setText(notification.getMessage());
    }

    @Override
    public int getItemCount() {
        return mNotifications.size();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {

        private TextView messageTextView;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.notification_message_text_view);
        }
    }
}

