package com.example.smokingcessationhelper;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Random;
import java.util.logging.Logger;

public class MainFragment extends Fragment {

    final String noSmokingStr[] = {
            "금연을 위한 글 1",
            "금연을 위한 글 2",
            "금연을 위한 글 3",
            "금연을 위한 글 4",
            "금연을 위한 글 5",
    };

    public MainFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ImageView ivSelectedImage = (ImageView) view.findViewById(R.id.MainFragment_ivSelectedImage);
        //TextView tvBlackBox = (TextView) view.findViewById(R.id.MainFragment_tvBlackBox);
        TextView tvNoSmokingStr = (TextView) view.findViewById(R.id.MainFragment_tvNoSmokingString);
        Random rand = new Random();
        int alpha = 90;
        Button btnLogout = view.findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d("s", "onClick: ");
                Toast.makeText(getActivity(), "로그아웃이 되었습니다.", Toast.LENGTH_SHORT).show();
                SignOut();

                getActivity().finish();

                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);

            }
        });

        //이미지 설정
        ivSelectedImage.setImageResource(R.drawable.no_smoking);
        //투명도 조절
        ivSelectedImage.setColorFilter(getOpaqueColorFilter(40));

        tvNoSmokingStr.setText(noSmokingStr[rand.nextInt(noSmokingStr.length)]);

        return view;
    }
    private ColorMatrixColorFilter getOpaqueColorFilter(int opacity) {
        float rate = 1 - ((float)opacity / 100);
        ColorMatrix cm = new ColorMatrix(new float[]{rate,0,0,0,0,0,rate,0,0,0,0,0,rate,0,0,0,0,0,1,0});
        return new ColorMatrixColorFilter(cm);
    }

    private void SignOut() {
        FirebaseAuth.getInstance().signOut();
    }

}