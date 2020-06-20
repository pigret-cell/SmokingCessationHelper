package com.example.smokingcessationhelper;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;


public class CalendarFragment extends Fragment{
    private static Context mContext = null;
    private Activity mActivity = null;
    private static MaterialCalendarView mcvNoSmokingCalendar = null;
    private static Button btNoSmokingStart = null;
    private HashSet<CalendarDay> successedDays = new HashSet<>();
    private HashSet<CalendarDay> failedDays = new HashSet<>();
    //private static boolean successed = true;

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

    //private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        mcvNoSmokingCalendar = view.findViewById(R.id.CalendarFragment_mcvNoSmokingCalendar);

        //추후 팝업 메뉴로 변경
        btNoSmokingStart = view.findViewById(R.id.CalendarFragment_btNoSmokingStart);
        view.findViewById(R.id.CalendarFragment_btStartDate).setOnClickListener(onClickListener);

        mContext = getContext();

        /*successedDays.add(CalendarDay.from(2020,4,1));
        failedDays.add(CalendarDay.from(2020,4,2));
        failedDays.add(CalendarDay.from(2020,4,3));
        successedDays.add(CalendarDay.from(2020,4,4));
        failedDays.add(CalendarDay.from(2020,4,5));*/

        /*
         * successedDates는 금연 성공 날짜, failedDates는 금연 실패 날짜이다.
         * updateDB() -> 사용자의 금연 성공/실패 이벤트가 발생 시 DB에 데이터를 저장한다.
         * updateCalendarData() -> 사용자의 금연 성공/실패 정보를 DB로부터 가져와 변수들에 저장한다.
         * updateView() -> 저장된 정보들을 Decorator를 통해 화면에 금연 성공/실패 여부를 보여준다.
         */

        /*MaterialCalendarView mcvNoSmokingCalendar = view.findViewById(R.id.CalendarFragment_mcvNoSmokingCalendar);
        view.findViewById(R.id.fragment_cal_bt).setOnClickListener(onClickListener);

        mcvNoSmokingCalendar.state().edit()
                //.setFirstDayOfWeek(Calendar.MONDAY)
                .setMinimumDate(CalendarDay.from(2020, 1, 1))
                .setMaximumDate(CalendarDay.from(2030, 1, 1))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();*/

        //  if (금연 중) btNoSmokingStart.setVisibility(View.INVISIBLE);
        /*updateCalendarData();
        updateView();*/

        /*btNoSmokingStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //DB에 금연시작 여부를 알 수 있는 데이터 저장
                btNoSmokingStart.setVisibility(View.INVISIBLE);
            }
        });*/

        updateCalendarData();
        updateView();

        return view;
    }

    private void updateDB(CalendarDay day, boolean successed) {
        /*Calendar date = Calendar.getInstance();
        date.add(Calendar.DATE,-1);
        String beforeDate = new SimpleDateFormat("yyyy년 MM월 dd일").format(date.getTime()); //어제 날짜*/

        // DB에 데이터 등록
        if (successed) {
            //successedDays.add(day); // <- 지워야함, 테스트용
        }
        else {
            //failedDays.add(day);
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
            //mcvNoSmokingCalendar.addDecorator(new DayBorderDecorator(mContext));
            mcvNoSmokingCalendar.addDecorator(new SuccessedDaysDecorator(mContext, successedDays));
            mcvNoSmokingCalendar.addDecorator(new FailedDaysDecorator(mContext, failedDays));
        } catch (Exception e) { e.printStackTrace();}
    }

    @Override
    public void onAttach(Context context) {
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

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.CalendarFragment_btStartDate:
                    startCalenderFragmentActivity();
                    break;
            }
        }
    };

    private void startCalenderFragmentActivity() {
        Intent intent = new Intent(getView().getContext(), CalendarFragment_Activity.class);
        startActivity(intent);
    }

}