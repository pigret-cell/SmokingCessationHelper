package com.example.smokingcessationhelper;

import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

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
        TextView tvNoSmokingStr = (TextView) view.findViewById(R.id.MainFragment_tvNoSmokingString);
        Random rand = new Random();
        int alpha = 90; // 투명도

        //이미지 설정
        ivSelectedImage.setImageResource(R.drawable.no_smoking);

        //투명도 조절, 40% 불투명하게
        ivSelectedImage.setColorFilter(getOpaqueColorFilter(40));

        tvNoSmokingStr.setText(noSmokingStr[rand.nextInt(noSmokingStr.length)]);

        return view;
    }

    private ColorMatrixColorFilter getOpaqueColorFilter(int opacity) {
        float rate = 1 - ((float)opacity / 100);
        ColorMatrix cm = new ColorMatrix(new float[]{rate,0,0,0,0,0,rate,0,0,0,0,0,rate,0,0,0,0,0,1,0});
        return new ColorMatrixColorFilter(cm);
    }

}