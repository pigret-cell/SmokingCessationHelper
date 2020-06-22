package com.example.smokingcessationhelper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity{
    private FirebaseAuth mAuth;
    private BackPressCloseHandler backPressCloseHandler;

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }

    public void destroyFragment() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager = findViewById(R.id.MainActivity_viewPager);
        final MainPagerAdapter mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        backPressCloseHandler = new BackPressCloseHandler(this);
        viewPager.setAdapter(mainPagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }
            @Override
            public void onPageSelected(int position) { }
            @Override
            public void onPageScrollStateChanged(int state) { }
        });
    }


}
