package com.example.luv;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ViewHolder> {
    private List<Reservation> reservationList;

    public ReservationAdapter(List<Reservation> reservationList) {
        this.reservationList = reservationList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reservation_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Reservation reservation = reservationList.get(position);
        holder.studentIdTextView.setText(reservation.getStudentId());
        holder.nameTextView.setText(reservation.getName());
        holder.lockerNumberTextView.setText(String.valueOf(reservation.getLockerNumber()));
        holder.startDateTextView.setText(reservation.getStartDate());
        holder.endDateTextView.setText(reservation.getEndDate());
    }

    @Override
    public int getItemCount() {
        return reservationList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView studentIdTextView, nameTextView, lockerNumberTextView, startDateTextView, endDateTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            studentIdTextView = itemView.findViewById(R.id.studentIdTextView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            lockerNumberTextView = itemView.findViewById(R.id.lockerNumberTextView);
            startDateTextView = itemView.findViewById(R.id.startDateTextView);
            endDateTextView = itemView.findViewById(R.id.endDateTextView);
        }
    }

    public void updateData(List<Reservation> newData) {
        reservationList.clear();
        reservationList.addAll(newData);
        notifyDataSetChanged();
    }
}

