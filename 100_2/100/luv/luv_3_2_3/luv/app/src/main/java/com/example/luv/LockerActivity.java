package com.example.luv;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LockerActivity extends AppCompatActivity {

    private GridLayout gridLayout;
    private DatabaseReference reservationsRef;
    private DatabaseReference usersRef;
    private FirebaseUser currentUser;

    private int[] fontNumbers = {37, 35, 33, 31, 29};
    private int[] lockerNumbers = {1, 2, 3, 4, 5};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locker);

        gridLayout = findViewById(R.id.grid_view);
        reservationsRef = FirebaseDatabase.getInstance().getReference("reservations");
        usersRef = FirebaseDatabase.getInstance().getReference("users");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        checkReservedLockers();
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            final int position = i;
            ImageView imageView = (ImageView) gridLayout.getChildAt(i);

            // 이미 예약한 사물함인 경우 클릭 불가능하도록 처리
            if (checkUserReservationStatus()) {
                imageView.setEnabled(false);
                imageView.setAlpha(0.5f);
                disableReservedImageViews();
            }

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int lockerNumber = lockerNumbers[position];
                    int fontNumber = fontNumbers[position];

                    if (checkUserReservationStatus()) {
                        Toast.makeText(LockerActivity.this, "이미 사물함을 예약했습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        isLockerReserved(lockerNumber, fontNumber, position);
                    }
                }
            });
        }
    }
    private void checkReservedLockers() {
        reservationsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Reservation reservation = snapshot.getValue(Reservation.class);
                    if (reservation != null) {
                        int lockerNumber = reservation.getLockerNumber();
                        int fontNumber = reservation.getFontNumber();
                        int position = getPosition(lockerNumber, fontNumber);

                        if (position >= 0) {
                            ImageView imageView = (ImageView) gridLayout.getChildAt(position);
                            imageView.setAlpha(0.5f);
                            imageView.setEnabled(false);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // 예약 정보 가져오기 실패
                Toast.makeText(LockerActivity.this, "예약 정보를 가져오는 데 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int getPosition(int lockerNumber, int fontNumber) {
        for (int i = 0; i < lockerNumbers.length; i++) {
            if (lockerNumbers[i] == lockerNumber && fontNumbers[i] == fontNumber) {
                return i;
            }
        }
        return -1;
    }
    private boolean checkUserReservationStatus() {
        String userName = currentUser.getDisplayName();

        Query query = reservationsRef.orderByChild("name").equalTo(userName);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean isUserReserved = false;

                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Reservation reservation = snapshot.getValue(Reservation.class);
                        if (reservation != null) {
                            int lockerNumber = reservation.getLockerNumber();
                            int fontNumber = reservation.getFontNumber();

                            disableImageView(lockerNumber, fontNumber);
                            isUserReserved = true;
                        }
                    }

                }

                if (isUserReserved) {
                    // 사용자가 예약한 사물함이 있으면 클릭 불가능하도록 처리
                    disableImageViews();


                } else {
                    // 사용자가 예약한 사물함이 없으면 클릭 가능하도록 처리
                    enableImageViews();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // 예약 정보 가져오기 실패
                Toast.makeText(LockerActivity.this, "예약 정보를 가져오는 데 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        return false;
    }

    private void disableImageViews() {
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            ImageView imageView = (ImageView) gridLayout.getChildAt(i);
            imageView.setEnabled(false);
        }
    }
    private void disableReservedImageViews() {
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            ImageView imageView = (ImageView) gridLayout.getChildAt(i);
            Reservation reservation = (Reservation) imageView.getTag();

            if (reservation != null) {
                // 예약된 사물함인 경우 불투명하게 처리
                imageView.setAlpha(0.5f);
                imageView.setEnabled(false);
            }
        }
    }



    private void enableImageViews() {
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            ImageView imageView = (ImageView) gridLayout.getChildAt(i);
            imageView.setEnabled(true);
        }
    }


    private void disableImageView(int lockerNumber, int fontNumber) {
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            ImageView imageView = (ImageView) gridLayout.getChildAt(i);
            Reservation reservation = (Reservation) imageView.getTag();

            if (reservation != null && reservation.getLockerNumber() == lockerNumber && reservation.getFontNumber() == fontNumber) {
                // 예약된 사물함인 경우 클릭 불가능하도록 처리 및 불투명하게 만들기
                imageView.setEnabled(false);
                imageView.setAlpha(0.5f); // 이미지를 불투명하게 설정
            }
        }
    }












    private void isLockerReserved(final int lockerNumber, final int fontNumber, final int position) {
        Query query = reservationsRef.orderByChild("lockerNumber").equalTo(lockerNumber);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean isReserved = false;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Reservation reservation = snapshot.getValue(Reservation.class);
                    if (reservation != null && reservation.getFontNumber() == fontNumber) {
                        isReserved = true;
                        break;
                    }
                }

                if (isReserved) {
                    // 예약된 사물함은 불투명하게 처리
                    ImageView imageView = (ImageView) gridLayout.getChildAt(position);
                    imageView.setAlpha(0.5f);
                    imageView.setEnabled(false);

                    Toast.makeText(LockerActivity.this, "해당 사물함은 이미 예약되었습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    checkUserAlreadyReserved(lockerNumber, fontNumber);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // 예약 정보 가져오기 실패
                Toast.makeText(LockerActivity.this, "예약 정보를 가져오는 데 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }




    private void checkUserAlreadyReserved(final int lockerNumber, final int fontNumber) {
        Query query = usersRef.orderByChild("name").equalTo(currentUser.getDisplayName());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean isUserReserved = false;

                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getChildren().iterator().next().getValue(User.class);
                    if (user != null && user.isReservedLocker()) {
                        isUserReserved = true;
                    }
                }

                if (isUserReserved) {
                    Toast.makeText(LockerActivity.this, "이미 사물함을 예약했습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(LockerActivity.this, RentPeriodActivity.class);
                    intent.putExtra("lockerNumber", lockerNumber);
                    intent.putExtra("fontNumber", fontNumber);
                    startActivity(intent);
                }
            }

            public void onCancelled(DatabaseError databaseError) {
                // 사용자 정보 가져오기 실패
                Toast.makeText(LockerActivity.this, "사용자 정보를 가져오는 데 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}



