package com.example.smokingcessationhelper;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class CalendarFragment_Activity extends Activity{

   // private static final String TAG = "CalendarFragment_Activity";
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_calendar);
        DatePickerDialog dialog = new DatePickerDialog(this, listener, 2020, 4, 1);
        dialog.show();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
    }

    private final DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            Toast.makeText(getApplicationContext(), year + "년 " + (month + 1) + "월 " + dayOfMonth + "일 시작", Toast.LENGTH_SHORT).show();
            dateUpdate(year, month, dayOfMonth);
            finish();
        }
    };

    private void dateUpdate(int year, int month, int dayOfMonth) {
        String date = year + "년 " + (month+1) + "월 " + dayOfMonth + "일";
        String mUseremail = mUser.getEmail();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //수정하기
        SmokeDay smokeDay = new SmokeDay(date);
        db.collection(mUseremail).document("Date").set(smokeDay)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) { }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) { }
                });

    }
}
