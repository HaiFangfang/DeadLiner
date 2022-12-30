package com.example.deadliner;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;
import com.haibin.calendarview.TrunkBranchAnnals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HomeFragment extends Fragment implements
        CalendarView.OnCalendarSelectListener,
        CalendarView.OnCalendarLongClickListener,
        CalendarView.OnMonthChangeListener,
        CalendarView.OnYearChangeListener,
        CalendarView.OnWeekChangeListener,
        CalendarView.OnViewChangeListener,
        CalendarView.OnCalendarInterceptListener,
        CalendarView.OnYearViewChangeListener,
        View.OnClickListener {
    TextView mTextMonthDay;
    RelativeLayout mRelativeTool;
    TextView mTextYear;

    TextView mTextLunar;

    TextView mTextCurrentDay;

    private int mYear;
    CalendarLayout mCalendarLayout;
    CalendarView mCalendarView;
    View home;



    public HomeFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

    }

    protected void initView() {
        mTextMonthDay = home.findViewById(R.id.tv_month_day);
        mTextYear = home.findViewById(R.id.tv_year);
        mTextLunar = home.findViewById(R.id.tv_lunar);

        mRelativeTool = home.findViewById(R.id.rl_tool);
        mCalendarView = home.findViewById(R.id.calendarView);

        //垂直滚动
        mCalendarView.getMonthViewPager().setOrientation(LinearLayout.VERTICAL);

        //mCalendarView.setRange(2018, 7, 1, 2019, 4, 28);
        mTextCurrentDay = home.findViewById(R.id.tv_current_day);
        mTextMonthDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mCalendarLayout.isExpand()) {
                    mCalendarLayout.expand();
                    return;
                }
//                mCalendarView.showYearSelectLayout(mYear);
                mTextLunar.setVisibility(View.GONE);
                mTextYear.setVisibility(View.GONE);
//                mTextMonthDay.setText(String.valueOf(mYear));
            }
        });


        mCalendarLayout = home.findViewById(R.id.calendarLayout);
        mCalendarView.setOnYearChangeListener(this);
        mCalendarView.setOnCalendarSelectListener(this);
        mCalendarView.setOnMonthChangeListener(this);
        mCalendarView.setOnCalendarLongClickListener(this, true);
        mCalendarView.setOnWeekChangeListener(this);
        mCalendarView.setOnYearViewChangeListener(this);

        //设置日期拦截事件，仅适用单选模式，当前无效
        mCalendarView.setOnCalendarInterceptListener(this);

        mCalendarView.setOnViewChangeListener(this);
        mTextYear.setText(String.valueOf(mCalendarView.getCurYear()));
        mYear = mCalendarView.getCurYear();
        mTextMonthDay.setText(mCalendarView.getCurMonth() + "月" + mCalendarView.getCurDay() + "日");
        mTextLunar.setText("今日");
        mTextCurrentDay.setText(String.valueOf(mCalendarView.getCurDay()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View home=inflater.inflate(R.layout.fragment_home, container, false);
        this.home=home;

        return home;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initData();
    }

    protected void initData() {

        final int year = mCalendarView.getCurYear();
        final int month = mCalendarView.getCurMonth();

        Map<String, Calendar> map = new HashMap<>();
            for (int y = 2022; y < 2023; y++) {
            for (int m = 1; m <= 12; m++) {
Calendar c=getSchemeCalendar(y, m, 1, 0xFF40db25, "假");
c.addScheme(0xFFe69138, "游");
                map.put(c.toString(),c);
//
//                map.put(getSchemeCalendar(y, m, 2, 0xFFe69138, "游").toString(),
//                        getSchemeCalendar(y, m, 2, 0xFFe69138, "游"));
//                map.put(getSchemeCalendar(y, m, 3, 0xFFdf1356, "事").toString(),
//                        getSchemeCalendar(y, m, 3, 0xFFdf1356, "事"));
//                map.put(getSchemeCalendar(y, m, 4, 0xFFaacc44, "车").toString(),
//                        getSchemeCalendar(y, m, 4, 0xFFaacc44, "车"));
//                map.put(getSchemeCalendar(y, m, 5, 0xFFbc13f0, "驾").toString(),
//                        getSchemeCalendar(y, m, 5, 0xFFbc13f0, "驾"));
//                map.put(getSchemeCalendar(y, m, 6, 0xFF542261, "记").toString(),
//                        getSchemeCalendar(y, m, 6, 0xFF542261, "记"));
//                map.put(getSchemeCalendar(y, m, 7, 0xFF4a4bd2, "会").toString(),
//                        getSchemeCalendar(y, m, 7, 0xFF4a4bd2, "会"));
//                map.put(getSchemeCalendar(y, m, 8, 0xFFe69138, "车").toString(),
//                        getSchemeCalendar(y, m, 8, 0xFFe69138, "车"));
//                map.put(getSchemeCalendar(y, m, 9, 0xFF542261, "考").toString(),
//                        getSchemeCalendar(y, m, 9, 0xFF542261, "考"));
//                map.put(getSchemeCalendar(y, m, 10, 0xFF87af5a, "记").toString(),
//                        getSchemeCalendar(y, m, 10, 0xFF87af5a, "记"));
//                map.put(getSchemeCalendar(y, m, 11, 0xFF40db25, "会").toString(),
//                        getSchemeCalendar(y, m, 11, 0xFF40db25, "会"));
//                map.put(getSchemeCalendar(y, m, 12, 0xFFcda1af, "游").toString(),
//                        getSchemeCalendar(y, m, 12, 0xFFcda1af, "游"));
//                map.put(getSchemeCalendar(y, m, 13, 0xFF95af1a, "事").toString(),
//                        getSchemeCalendar(y, m, 13, 0xFF95af1a, "事"));
//                map.put(getSchemeCalendar(y, m, 14, 0xFF33aadd, "学").toString(),
//                        getSchemeCalendar(y, m, 14, 0xFF33aadd, "学"));
//                map.put(getSchemeCalendar(y, m, 15, 0xFF1aff1a, "码").toString(),
//                        getSchemeCalendar(y, m, 15, 0xFF1aff1a, "码"));
//                map.put(getSchemeCalendar(y, m, 16, 0xFF22acaf, "驾").toString(),
//                        getSchemeCalendar(y, m, 16, 0xFF22acaf, "驾"));
//                map.put(getSchemeCalendar(y, m, 17, 0xFF99a6fa, "校").toString(),
//                        getSchemeCalendar(y, m, 17, 0xFF99a6fa, "校"));
//                map.put(getSchemeCalendar(y, m, 18, 0xFFe69138, "车").toString(),
//                        getSchemeCalendar(y, m, 18, 0xFFe69138, "车"));
//                map.put(getSchemeCalendar(y, m, 19, 0xFF40db25, "码").toString(),
//                        getSchemeCalendar(y, m, 19, 0xFF40db25, "码"));
//                map.put(getSchemeCalendar(y, m, 20, 0xFFe69138, "火").toString(),
//                        getSchemeCalendar(y, m, 20, 0xFFe69138, "火"));
//                map.put(getSchemeCalendar(y, m, 21, 0xFF40db25, "假").toString(),
//                        getSchemeCalendar(y, m, 21, 0xFF40db25, "假"));
//                map.put(getSchemeCalendar(y, m, 22, 0xFF99a6fa, "记").toString(),
//                        getSchemeCalendar(y, m, 22, 0xFF99a6fa, "记"));
//                map.put(getSchemeCalendar(y, m, 23, 0xFF33aadd, "假").toString(),
//                        getSchemeCalendar(y, m, 23, 0xFF33aadd, "假"));
//                map.put(getSchemeCalendar(y, m, 24, 0xFF40db25, "校").toString(),
//                        getSchemeCalendar(y, m, 24, 0xFF40db25, "校"));
//                map.put(getSchemeCalendar(y, m, 25, 0xFF1aff1a, "假").toString(),
//                        getSchemeCalendar(y, m, 25, 0xFF1aff1a, "假"));
//                map.put(getSchemeCalendar(y, m, 26, 0xFF40db25, "议").toString(),
//                        getSchemeCalendar(y, m, 26, 0xFF40db25, "议"));
//                map.put(getSchemeCalendar(y, m, 27, 0xFF95af1a, "假").toString(),
//                        getSchemeCalendar(y, m, 27, 0xFF95af1a, "假"));
//                map.put(getSchemeCalendar(y, m, 28, 0xFF40db25, "码").toString(),
//                        getSchemeCalendar(y, m, 28, 0xFF40db25, "码"));
//                map.put(getSchemeCalendar(y, m, 1, 0xFF40db25, "假").toString(),
//                        getSchemeCalendar(y, m, 1, 0xFF40db25, "假"));


            }
        }

        //28560 数据量增长不会影响UI响应速度，请使用这个API替换
        mCalendarView.setSchemeDate(map);

        //可自行测试性能差距
    }

    private Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
//        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
        calendar.addScheme(color,text);
        return calendar;
    }
    private Calendar addSchemeCalendar(Calendar calendar ,int color, String text) {

        calendar.addScheme(color,text);
        return calendar;
    }

    @Override
    public void onCalendarOutOfRange(Calendar calendar) {

    }

    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
        //Log.e("onDateSelected", "  -- " + calendar.getYear() + "  --  " + calendar.getMonth() + "  -- " + calendar.getDay());
        mTextLunar.setVisibility(View.VISIBLE);
        mTextYear.setVisibility(View.VISIBLE);
        mTextMonthDay.setText(calendar.getMonth() + "月" + calendar.getDay() + "日");
        mTextYear.setText(String.valueOf(calendar.getYear()));
        mTextLunar.setText(calendar.getLunar());
        mYear = calendar.getYear();
        if (isClick) {
            Toast.makeText(getContext(), getCalendarText(calendar), Toast.LENGTH_SHORT).show();
        }
//        Log.e("lunar "," --  " + calendar.getLunarCalendar().toString() + "\n" +
//        "  --  " + calendar.getLunarCalendar().getYear());
        Log.e("onDateSelected", "  -- " + calendar.getYear() +
                "  --  " + calendar.getMonth() +
                "  -- " + calendar.getDay() +
                "  --  " + isClick + "  --   " + calendar.getScheme());
        Log.e("onDateSelected", "  " + mCalendarView.getSelectedCalendar().getScheme() +
                "  --  " + mCalendarView.getSelectedCalendar().isCurrentDay());
        Log.e("干支年纪 ： ", " -- " + TrunkBranchAnnals.getTrunkBranchYear(calendar.getLunarCalendar().getYear()));
    }
    private static String getCalendarText(Calendar calendar) {
        return String.format("新历%s \n 农历%s \n 公历节日：%s \n 农历节日：%s \n 节气：%s \n 是否闰月：%s",
                calendar.getMonth() + "月" + calendar.getDay() + "日",
                calendar.getLunarCalendar().getMonth() + "月" + calendar.getLunarCalendar().getDay() + "日",
                TextUtils.isEmpty(calendar.getGregorianFestival()) ? "无" : calendar.getGregorianFestival(),
                TextUtils.isEmpty(calendar.getTraditionFestival()) ? "无" : calendar.getTraditionFestival(),
                TextUtils.isEmpty(calendar.getSolarTerm()) ? "无" : calendar.getSolarTerm(),
                calendar.getLeapMonth() == 0 ? "否" : String.format("闰%s月", calendar.getLeapMonth()));
    }
    private Calendar getSchemeCalendar(int year, int month, int day, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(Color.WHITE);
        calendar.setScheme(text);
        calendar.addScheme(0xFFa8b015, "rightTop");
        calendar.addScheme(0xFF423cb0, "leftTop");
        calendar.addScheme(0xFF643c8c, "bottom");

        return calendar;
    }
    @Override
    public void onClick(View view) {
        Toast.makeText(getContext(), "click", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCalendarIntercept(Calendar calendar) {
        return false;
    }

    @Override
    public void onCalendarInterceptClick(Calendar calendar, boolean isClick) {

    }

    @Override
    public void onCalendarLongClickOutOfRange(Calendar calendar) {

    }

    @Override
    public void onCalendarLongClick(Calendar calendar) {

    }

    @Override
    public void onMonthChange(int year, int month) {

    }

    @Override
    public void onViewChange(boolean isMonthView) {

    }

    @Override
    public void onWeekChange(List<Calendar> weekCalendars) {

    }

    @Override
    public void onYearChange(int year) {

    }

    @Override
    public void onYearViewChange(boolean isClose) {

    }
}