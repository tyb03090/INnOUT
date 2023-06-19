package com.example.luv;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity4 extends AppCompatActivity {
    private AlertDialog alertDialog;
    private TextView studentNumberEditText;
    private TextView nameEditText;
    private TextView lockerTagEditText;
    private TextView startDateEditText;
    private TextView endDateEditText;
    private Button confirmButton;
    private Button cancelButton;
    private Button extendButton;
    private Button extendButton1;
    private DatabaseReference reservationsRef;
    private FirebaseUser currentUser;
    private String currentReservationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        studentNumberEditText = findViewById(R.id.studentNumberEditText1);
        nameEditText = findViewById(R.id.nameEditText1);
        lockerTagEditText = findViewById(R.id.lockerTagEditText1);
        startDateEditText = findViewById(R.id.startDateEditText1);
        endDateEditText = findViewById(R.id.endDateEditText);
        confirmButton = findViewById(R.id.confirmButton1);
        cancelButton = findViewById(R.id.confirmButton2);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        reservationsRef = firebaseDatabase.getReference("reservations");
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 확인 버튼을 눌렀을 때 처리할 로직을 작성합니다.
                // 예약 내역을 초기화하고 메인 액티비티로 이동합니다.
                clearReservationInfo();
                goToMainActivity();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelReservation();

            }
        });
        extendButton = findViewById(R.id.confirmButton3);

        extendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showExtendDialog();
            }
        });
        extendButton1 = findViewById(R.id.confirmButton4);

        extendButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRentOptionsDialog();
            }
        });
    }


    private void showRentOptionsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("어떤 대여를 하시겠습니까?")
                .setItems(new CharSequence[]{"단기대여", "장기대여"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                // 단기대여 선택 시 처리
                                showDateSelectionDialog();
                                break;
                            case 1:
                                // 장기대여 선택 시 처리
                                showDateSelectionDialog1();
                                break;
                        }
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 취소 버튼 클릭 시 처리
                        dialog.dismiss();
                    }
                });

        builder.create().show();
    }

    private void showDateSelectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_date_selection, null);
        builder.setView(dialogView);

        TextView startDateButton = dialogView.findViewById(R.id.startDateButton);
        TextView endDateButton = dialogView.findViewById(R.id.endDateButton);
        Button confirmButton = dialogView.findViewById(R.id.confirmButton5);
        Button cancelButton = dialogView.findViewById(R.id.cancelButton);

        // 시작일 버튼 클릭 시 DatePickerDialog 표시
        startDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePickerDialog(startDateButton);
            }
        });

        // 종료일 버튼 클릭 시 DatePickerDialog 표시
        endDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePickerDialog(endDateButton);
            }
        });

        // 확인 버튼 클릭 시 예약 내역의 시작일과 종료일 업데이트

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String startDate = startDateButton.getText().toString();
                String endDate = endDateButton.getText().toString();

                // 예약 내역의 시작일과 종료일 업데이트
                updateReservationDates(startDate, endDate);

                // 다이얼로그 닫기
                alertDialog.dismiss();

                // 예약 내역 화면으로 이동하여 업데이트된 예약 내역 표시
                showUpdatedReservation();
            }
        });

// 취소 버튼 클릭 시 다이얼로그 닫기
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });


        alertDialog = builder.create();
        alertDialog.show();
    }
    private void showDateSelectionDialog1() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_date_selection, null);
        builder.setView(dialogView);

        TextView startDateButton = dialogView.findViewById(R.id.startDateButton);
        TextView endDateButton = dialogView.findViewById(R.id.endDateButton);
        Button confirmButton = dialogView.findViewById(R.id.confirmButton5);
        Button cancelButton = dialogView.findViewById(R.id.cancelButton);

        // 시작일 버튼 클릭 시 DatePickerDialog 표시
        startDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePickerDialog1(startDateButton);
            }
        });

        // 종료일 버튼 클릭 시 DatePickerDialog 표시
        endDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePickerDialog1(endDateButton);
            }
        });

        // 확인 버튼 클릭 시 예약 내역의 시작일과 종료일 업데이트

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String startDate = startDateButton.getText().toString();
                String endDate = endDateButton.getText().toString();

                // 예약 내역의 시작일과 종료일 업데이트
                updateReservationDates(startDate, endDate);

                // 다이얼로그 닫기
                alertDialog.dismiss();

                // 예약 내역 화면으로 이동하여 업데이트된 예약 내역 표시
                showUpdatedReservation();
            }
        });

// 취소 버튼 클릭 시 다이얼로그 닫기
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });


        alertDialog = builder.create();
        alertDialog.show();
    }


    private void showDateTimePickerDialog(final TextView dateTimeButton) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity4.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar selectedDateTime = Calendar.getInstance();
                        selectedDateTime.set(Calendar.YEAR, year);
                        selectedDateTime.set(Calendar.MONTH, monthOfYear);
                        selectedDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        selectedDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        selectedDateTime.set(Calendar.MINUTE, minute);

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                        String dateTime = sdf.format(selectedDateTime.getTime());

                        // 선택한 날짜 및 시간을 버튼에 표시
                        dateTimeButton.setText(dateTime);
                    }
                }, hour, minute, false);
                timePickerDialog.show();
            }
        }, year, month, day);

        // 최대 날짜를 현재 날짜부터 1달 이내로 설정
        calendar.add(Calendar.MONTH, 1);
        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());

        datePickerDialog.show();
    }


    // 예약 내역의 시작일과 종료일 업데이트
    private void updateReservationDates(String startDate, String endDate) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String name = user.getDisplayName();
        DatabaseReference reservationRef = FirebaseDatabase.getInstance().getReference("reservations");

        reservationRef.orderByChild("name").equalTo(name).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot reservationSnapshot : snapshot.getChildren()) {
                    String reservationKey = reservationSnapshot.getKey();

                    // 예약 내역의 시작일과 종료일 업데이트
                    reservationRef.child(reservationKey).child("startDate").setValue(startDate);
                    reservationRef.child(reservationKey).child("endDate").setValue(endDate);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // 예외 처리 코드
            }
        });
    }
    private void showDateTimePickerDialog1(final TextView dateTimeButton) {
        Calendar calendar = Calendar.getInstance();
        final int currentYear = calendar.get(Calendar.YEAR);
        final int currentMonth = calendar.get(Calendar.MONTH);
        final int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        final int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        final int currentMinute = calendar.get(Calendar.MINUTE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity4.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar selectedDateTime = Calendar.getInstance();
                        selectedDateTime.set(Calendar.YEAR, year);
                        selectedDateTime.set(Calendar.MONTH, monthOfYear);
                        selectedDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        selectedDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        selectedDateTime.set(Calendar.MINUTE, minute);

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                        String dateTime = sdf.format(selectedDateTime.getTime());

                        // Set the selected date and time on the button
                        dateTimeButton.setText(dateTime);
                    }
                }, currentHour, currentMinute, false);
                timePickerDialog.show();
            }
        }, currentYear, currentMonth, currentDay);

        // Set the minimum and maximum selectable dates for DatePicker
        calendar.add(Calendar.MONTH, 1);
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        calendar.add(Calendar.MONTH, 2);
        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());

        // Disable dates before the current date
        datePickerDialog.getDatePicker().setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar selectedDateTime = Calendar.getInstance();
                selectedDateTime.set(Calendar.YEAR, year);
                selectedDateTime.set(Calendar.MONTH, monthOfYear);
                selectedDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                if (selectedDateTime.before(Calendar.getInstance())) {
                    // Disable dates before the current date
                    view.updateDate(currentYear, currentMonth, currentDay);
                }
            }
        });

        datePickerDialog.show();
    }







    // 업데이트된 예약 내역을 가져와서 화면에 표시
    private void showUpdatedReservation() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        DatabaseReference reservationRef = FirebaseDatabase.getInstance().getReference("reservations");

        reservationRef.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot reservationSnapshot : snapshot.getChildren()) {
                    // 업데이트된 예약 내역 가져오기
                    Reservation reservation = reservationSnapshot.getValue(Reservation.class);

                    // 화면에 예약 내역 표시
                    // 예약 내역을 적절한 방법으로 화면에 표시하는 코드 작성
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // 예외 처리 코드
            }
        });
    }





    private void showExtendDialog() {
        final CharSequence[] options = {"1일", "1주일", "1달", "3달"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("예약 연장");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selectedOption = options[which].toString();
                extendReservation(selectedOption);
            }
        });
        builder.show();
    }



    private void extendReservation(String selectedOption) {
        String endDateString = endDateEditText.getText().toString();
        if (TextUtils.isEmpty(endDateString)) {
            Toast.makeText(this, "예약 종료일을 가져올 수 없습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("예약 연장");
        builder.setMessage("연장 하시겠습니까?");
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                try {
                    Date endDate = dateTimeFormat.parse(endDateString);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(endDate);

                    if (selectedOption.equals("1일")) {
                        calendar.add(Calendar.DAY_OF_MONTH, 1);
                    } else if (selectedOption.equals("1주일")) {
                        calendar.add(Calendar.WEEK_OF_MONTH, 1);
                    } else if (selectedOption.equals("1달")) {
                        calendar.add(Calendar.MONTH, 1);
                    } else if (selectedOption.equals("3달")) {
                        calendar.add(Calendar.MONTH, 3);
                    }

                    Date extendedEndDate = calendar.getTime();
                    String extendedEndDateString = dateTimeFormat.format(extendedEndDate);
                    String extendedEndDateStringOnly = extendedEndDateString.substring(0, 10); // 날짜 부분만 추출

                    // 예약 내역 업데이트 작업 수행
                    updateReservationEndDate(extendedEndDateString);

                    endDateEditText.setText(extendedEndDateStringOnly);
                    Toast.makeText(MainActivity4.this, "예약이 연장되었습니다.", Toast.LENGTH_SHORT).show();

                    // 예약 정보를 가져와서 화면에 표시
                    retrieveReservationInfo();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity4.this, "예약 연장에 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 아니오 버튼 클릭 시 아무 작업도 수행하지 않음
            }
        });
        builder.show();
    }
    // 예약 정보를 가져와서 화면에 표시


    private void updateReservationEndDate(String extendedEndDate) {
        // Get the reference to the current reservation in the Firebase database
        DatabaseReference reservationRef = FirebaseDatabase.getInstance().getReference("reservations").child(currentReservationId);

        // Update the reservation's end date
        reservationRef.child("endDate").setValue(extendedEndDate)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity4.this, "예약 종료일이 연장되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity4.this, "예약 종료일 연장에 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void goToReservationPage() {
        Intent intent = new Intent(MainActivity4.this, MainActivity4.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        retrieveReservationInfo();
        retrieveLockerReservation();// 예약 정보를 가져와서 화면에 표시

    }

    private void retrieveReservationInfo() {
        String studentId = currentUser.getUid();
        String name = currentUser.getDisplayName();
        Log.d("MainActivity4", "Name: " + name);
        Log.d("MainActivity4", "학번: " + studentId);
        reservationsRef.orderByChild("name").equalTo(name).limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Reservation reservation = snapshot.getValue(Reservation.class);

                        if (reservation != null && reservation.getName().equals(name)) {
                            currentReservationId = snapshot.getKey();

                            studentNumberEditText.setText(reservation.getStudentId());
                            nameEditText.setText(reservation.getName());
                            lockerTagEditText.setText(String.valueOf(reservation.getLockerNumber()));
                            startDateEditText.setText(reservation.getStartDate());
                            endDateEditText.setText(reservation.getEndDate());

                            return; // 예약 내역을 찾았으므로 반환하여 추가적인 처리를 중단합니다.
                        }
                    }
                }

                // 예약 내역이 없거나 이름과 일치하는 예약 내역이 없는 경우 처리
                clearReservationInfo();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // 오류 처리
            }
        });

        retrieveLockerReservation(); // 예약한 사물함 내역 가져오기
    }

    private void cancelReservation() {
        if (currentReservationId != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            reservationsRef.child(currentReservationId).removeValue()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(MainActivity4.this, "예약이 취소되었습니다.", Toast.LENGTH_SHORT).show();
                                            clearReservationInfo();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(MainActivity4.this, "예약 취소에 실패했습니다.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    });
            builder.setNegativeButton("취소", null);
            builder.show();
        }
    }

    private void clearReservationInfo() {
        studentNumberEditText.setText("");
        nameEditText.setText("");
        lockerTagEditText.setText("");
        startDateEditText.setText("");
        endDateEditText.setText("");
    }

    private void goToMainActivity() {
        // 메인 액티비티로 이동하는 코드를 작성합니다.
        Intent intent = new Intent(MainActivity4.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    private void retrieveLockerReservation() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            String studentId = currentUser.getUid();
            String name = currentUser.getDisplayName();

            DatabaseReference reservationsRef = FirebaseDatabase.getInstance().getReference("reservations");
            reservationsRef.orderByChild("studentId").equalTo(studentId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Reservation reservation = snapshot.getValue(Reservation.class);
                            if (reservation.getName().equals(name)) {
                                // 예약 정보에 해당 사용자의 예약 정보가 있을 때 처리할 로직을 작성합니다.
                                // reservation.getLockerNumber(), reservation.getStartDate(), reservation.getEndDate() 등의 정보 활용 가능
                                studentNumberEditText.setText(reservation.getStudentId());
                                nameEditText.setText(reservation.getName());
                                lockerTagEditText.setText(String.valueOf(reservation.getLockerNumber()));
                                startDateEditText.setText(reservation.getStartDate());
                                endDateEditText.setText(reservation.getEndDate());
                                return;
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // 오류 처리
                    Log.e("MainActivity4", "Error retrieving locker reservation: " + databaseError.getMessage());
                }
            });
        } else {
            // 예약 정보가 없는 경우 처리
            clearLockerReservationInfo();
        }
    }



    private void clearLockerReservationInfo() {
        lockerTagEditText.setText("");
        startDateEditText.setText("");
        endDateEditText.setText("");
    }





}