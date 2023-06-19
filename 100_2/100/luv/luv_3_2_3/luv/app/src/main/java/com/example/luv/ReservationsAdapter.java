package com.example.luv;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ReservationsAdapter extends RecyclerView.Adapter<ReservationsAdapter.ViewHolder> {

    private List<Reservation> reservationList;

    public ReservationsAdapter(List<Reservation> reservationList) {
        this.reservationList = reservationList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_reservation, parent, false);
        return new ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Reservation reservation = reservationList.get(position);
        holder.studentIdTextView.setText(reservation.getStudentId());
        holder.nameTextView.setText(reservation.getName());
        holder.lockerNumberTextView.setText(reservation.getLockerNumber());
        holder.startDateTextView.setText(reservation.getStartDate());
        holder.endDateTextView.setText(reservation.getEndDate());
    }

    @Override
    public int getItemCount() {
        return reservationList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView studentIdTextView;
        private TextView nameTextView;
        private TextView lockerNumberTextView;
        private TextView startDateTextView;
        private TextView endDateTextView;

        public ViewHolder(View itemView) {
            super(itemView);

        }

    }

}
