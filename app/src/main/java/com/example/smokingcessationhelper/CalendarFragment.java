package com.example.smokingcessationhelper;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.zip.Inflater;


public class CalendarFragment extends Fragment{
    private Context mContext = null;
    private Activity mActivity = null;
    private View mView = null;

    private MaterialCalendarView mcvNoSmokingCalendar = null;
    private HashSet<CalendarDay> successedDays = new HashSet<>();
    private HashSet<CalendarDay> failedDays = new HashSet<>();

    private ConstraintLayout clPanel;

    private FloatingActionButton fabExtend;

    private int[] extendedMenus = { R.id.CalendarFragment_fabStart, R.id.CalendarFragment_tvStart, R.id.CalendarFragment_fabShare, R.id.CalendarFragment_tvShare };

    private Animation fab_open, fab_close;
    private Boolean isFabExtended = false;

    //private FirebaseAuth mAuth;

    private BroadcastReceiver dateChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            CalendarDay today = CalendarDay.today();
            CalendarDay yesterday = CalendarDay.from(today.getYear(), today.getMonth(), today.getDay() - 1);

            updateDB(yesterday, true);
            updateCalendarData();
            updateView();
        }
    };

    public CalendarFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_calendar, container, false);
        fabClickListener fabHandler = new fabClickListener();

        mcvNoSmokingCalendar = mView.findViewById(R.id.CalendarFragment_mcvNoSmokingCalendar);

        mContext = getContext();

        clPanel = mView.findViewById(R.id.CalendarFragment_clPanel);

        fabExtend = mView.findViewById(R.id.CalendarFragment_fabExtend);

        fab_open = AnimationUtils.loadAnimation(mContext, R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(mContext, R.anim.fab_close);

        fabExtend.setOnClickListener(fabHandler);
        for (int item: extendedMenus) {
            (mView.findViewById(item)).setOnClickListener(fabHandler);
        }

        clPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clPanel.setVisibility(View.INVISIBLE);
                for (int item: extendedMenus) {
                    (mView.findViewById(item)).startAnimation(fab_close);
                    (mView.findViewById(item)).setClickable(false);
                }
                fabExtend.setImageDrawable(getResources().getDrawable(R.drawable.plus));
                isFabExtended = false;
            }
        });


 //       ibNoSmokingStart = view.findViewById(R.id.CalendarFragment_ibNoSmokingStart);

        /*
         * successedDates는 금연 성공 날짜, failedDates는 금연 실패 날짜이다.
         * updateDB() -> 사용자의 금연 성공/실패 이벤트가 발생 시 DB에 데이터를 저장한다.
         * updateCalendarData() -> 사용자의 금연 성공/실패 정보를 DB로부터 가져와 변수들에 저장한다.
         * updateView() -> 저장된 정보들을 Decorator를 통해 화면에 금연 성공/실패 여부를 보여준다.
         */

        updateCalendarData();
        updateView();

        return mView;
    }

    private void updateDB(CalendarDay day, boolean successed) {
        /* DB에 데이터 등록 */

        /*Calendar date = Calendar.getInstance();
        date.add(Calendar.DATE,-1);
        String beforeDate = new SimpleDateFormat("yyyy년 MM월 dd일").format(date.getTime()); //어제 날짜*/

        // DB에 데이터 등록
        if (successed) {
            /* 어제 금연 성공, 데이터 DB에 데이터 추가 */
        }
        else {
            /* 오늘 금연 실패, DB에 데이터 추가 */
        }
    }

    private void updateCalendarData()
    {
        /*
        successedDays.clear();
        failedDays.clear();
        DB에서 데이터 가져와서 successedDays와 failedDays에 저장
        successedDays.add(CalendarDay.from(2020,4,1));
        failedDays.add(CalendarDay.from(2020,4,2));
         */
    }

    private void updateView() {
        if (mcvNoSmokingCalendar == null) {
            return;
        }

        try {
            mcvNoSmokingCalendar.removeDecorators();
            mcvNoSmokingCalendar.invalidateDecorators();
            mcvNoSmokingCalendar.addDecorator(new SuccessedDaysDecorator(mContext, successedDays));
            mcvNoSmokingCalendar.addDecorator(new FailedDaysDecorator(mContext, failedDays));
        } catch (Exception e) { e.printStackTrace();}
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
        if (context instanceof MainActivity) {
            mActivity = (MainActivity) context;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_DATE_CHANGED);
        mActivity.registerReceiver(dateChangeReceiver, intentFilter);
        if (false) {
            /* 금연중임에도 불구하고 어제의 데이터가 존재하지 않으면 추가 (앱이 중지 혹은 종료된 경우) */
            CalendarDay today = CalendarDay.today();
            CalendarDay yesterday = CalendarDay.from(today.getYear(), today.getMonth(), today.getDay() - 1);
            updateDB(yesterday, true);
        }
        updateCalendarData();
        updateView();
    }

    @Override
    public void onPause() {
        super.onPause();
        mActivity.unregisterReceiver(dateChangeReceiver);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
    }

    class fabClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.CalendarFragment_fabExtend:
                    if (isFabExtended) {
                        clPanel.setVisibility(View.INVISIBLE);
                        for (int item: extendedMenus) {
                            (mView.findViewById(item)).startAnimation(fab_close);
                            (mView.findViewById(item)).setClickable(false);
                        }
                        fabExtend.setImageDrawable(getResources().getDrawable(R.drawable.plus));
                    } else {
                        clPanel.setVisibility(View.VISIBLE);
                        for (int item: extendedMenus) {
                            (mView.findViewById(item)).startAnimation(fab_open);
                            (mView.findViewById(item)).setClickable(true);
                        }
                        fabExtend.setImageDrawable(getResources().getDrawable(R.drawable.close));
                    }
                    isFabExtended = !isFabExtended;
                    break;

                case R.id.CalendarFragment_fabStart:
                    startCalenderFragmentActivity();
                    break;

                case R.id.CalendarFragment_fabShare:
                    // 추후 구현
                    break;
            }
        }
    };

    private void startCalenderFragmentActivity() {
        Intent intent = new Intent(mView.getContext(), CalendarFragment_Activity.class);
        startActivity(intent);
    }

}
