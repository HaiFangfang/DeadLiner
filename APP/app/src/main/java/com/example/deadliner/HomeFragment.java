package com.example.deadliner;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Rect;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;
import com.haibin.calendarview.TrunkBranchAnnals;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
    List<TaskBlock> TaskList =new ArrayList<>();
    List<TaskBlock> inSchList= new ArrayList<>();
    TextView mTextLunar;
    RecyclerView inSch;
    TextView mTextCurrentDay;
    inSchAdapter inschAdapter;
    private int mYear;
    CalendarLayout mCalendarLayout;
    CalendarView mCalendarView;
    View home;
    String daySelectd;



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
        inSch=home.findViewById(R.id.insche_view);
        inschAdapter=new inSchAdapter();
        inSch.setAdapter(inschAdapter);
        inSch.setLayoutManager(new LinearLayoutManager(getContext()));
        inSch.addItemDecoration(getBlockDecoration());
        return home;
    }
    private RecyclerView.ItemDecoration getBlockDecoration() {
        return new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = 15;
                outRect.bottom=5;
                outRect.left=20;
                outRect.right=20;

            }
        };
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initData();
        android.icu.util.Calendar c= android.icu.util.Calendar.getInstance();
        daySelectd=c.get(android.icu.util.Calendar.YEAR
        )+"-"+(c.get(android.icu.util.Calendar.MONTH)+1)+"-"+c.get(android.icu.util.Calendar.DATE);
//        Toast.makeText(getContext(), daySelectd, Toast.LENGTH_SHORT).show();
        taskClassify(daySelectd);
    }

    protected void initData() {
        TaskList.clear();
        final int year = mCalendarView.getCurYear();
        final int month = mCalendarView.getCurMonth();
        Map<String, Calendar> map = new HashMap<>();
        Cursor tasks=MainActivity.getdb().getWritableDatabase().query("task",null,null,null,null,null,null);
        while(tasks.moveToNext()){
            String id= tasks.getString(0);
            String name=tasks.getString(1);
            String sttime=tasks.getString(2);
            String ddl=tasks.getString(3);
            String ddlkey="";
            String stkey="";
            int color=tasks.getInt(5);
//            Toast.makeText(getContext(), String.valueOf(color), Toast.LENGTH_SHORT).show();
            if(!sttime.isEmpty()&&!ddl.isEmpty()){
                String strs[]=ddl.split(" ");
                String strtrs[]=strs[0].split("-");
                ddlkey=strtrs[0]+strtrs[1]+strtrs[2];
                String strs1[]=sttime.split(" ");
                String strtrs1[]=strs1[0].split("-");
                stkey=strtrs1[0]+strtrs1[1]+strtrs1[2];
                android.icu.util.Calendar ddlutil=android.icu.util.Calendar.getInstance();
                android.icu.util.Calendar stutil=android.icu.util.Calendar.getInstance();
                if(ddlkey.compareTo(stkey)==0){//the same day
                    ddlutil.set(Integer.valueOf(strtrs[0]),Integer.valueOf(strtrs[1])-1,Integer.valueOf(strtrs[2]),0,0);
                    stutil.set(Integer.valueOf(strtrs1[0]),Integer.valueOf(strtrs1[1])-1,Integer.valueOf(strtrs1[2]),0,0);
//                    Toast.makeText(getContext(), transToKey(ddlutil), Toast.LENGTH_SHORT).show();
                    Calendar c=map.get(transToKey(ddlutil));
                    if(c==null){
                        c=getSchemeCalendar(Integer.valueOf(strtrs[0]), Integer.valueOf(strtrs[1]), Integer.valueOf(strtrs[2]), color, "SE");
                        map.put(c.toString(),c);
                    }else{
                        c.addScheme(color, "SE");
                    }
                }else if(transToKey(ddlutil).compareTo(transToKey(stutil))<0){//ddl before starttime

                    ddlutil.set(Integer.valueOf(strtrs[0]),Integer.valueOf(strtrs[1])-1,Integer.valueOf(strtrs[2]),0,0);
                    stutil.set(Integer.valueOf(strtrs1[0]),Integer.valueOf(strtrs1[1])-1,Integer.valueOf(strtrs1[2]),0,0);
//                    Toast.makeText(getContext(), strtrs[0]+strtrs[1]+strtrs[2], Toast.LENGTH_SHORT).show();
                    Calendar c=map.get(transToKey(ddlutil));
                    if(c==null){
                        c=getSchemeCalendar(Integer.valueOf(strtrs[0]), Integer.valueOf(strtrs[1]), Integer.valueOf(strtrs[2]), color, "E");
                        map.put(c.toString(),c);
                    }else{
                        c.addScheme(color, "E");
                    }
                    ddlutil.add(5,1);
                    String st=String.valueOf(ddlutil.get(android.icu.util.Calendar.YEAR))+String.valueOf(ddlutil.get(android.icu.util.Calendar.MONTH))+String.valueOf(ddlutil.get(android.icu.util.Calendar.DATE));
                    String st2=String.valueOf(stutil.get(android.icu.util.Calendar.YEAR))+String.valueOf(stutil.get(android.icu.util.Calendar.MONTH))+String.valueOf(stutil.get(android.icu.util.Calendar.DATE));
//                    Toast.makeText(getContext(), st+" "+st2, Toast.LENGTH_SHORT).show();
//                    Log.e("123",String.valueOf(ddlutil.get(android.icu.util.Calendar.YEAR))+String.valueOf(ddlutil.get(android.icu.util.Calendar.MONTH))+String.valueOf(ddlutil.get(android.icu.util.Calendar.DATE)));
//                    Log.e("123",String.valueOf(stutil.get(android.icu.util.Calendar.YEAR))+String.valueOf(stutil.get(android.icu.util.Calendar.MONTH))+String.valueOf(stutil.get(android.icu.util.Calendar.DATE)));
                    while(ddlutil.before(stutil)){
//                        Log.e("123",String.valueOf(ddlutil.get(android.icu.util.Calendar.YEAR))+String.valueOf(ddlutil.get(android.icu.util.Calendar.MONTH))+String.valueOf(ddlutil.get(android.icu.util.Calendar.DATE)));
//                        Log.e("123",String.valueOf(stutil.get(android.icu.util.Calendar.YEAR))+String.valueOf(stutil.get(android.icu.util.Calendar.MONTH))+String.valueOf(stutil.get(android.icu.util.Calendar.DATE)));
                        c=map.get(transToKey(ddlutil));
                        if(c==null){
                            c=getSchemeCalendar(ddlutil.get(android.icu.util.Calendar.YEAR),ddlutil.get(android.icu.util.Calendar.MONTH)+1,ddlutil.get(android.icu.util.Calendar.DATE),color,"D");

                            map.put(c.toString(),c);
                        }else{
                            c.addScheme(color, "D");
                        }
                        ddlutil.add(5,1);
                    }
                    c=map.get(transToKey(stutil));
                    if(c==null){
                        c=getSchemeCalendar(Integer.valueOf(strtrs1[0]), Integer.valueOf(strtrs1[1]), Integer.valueOf(strtrs1[2]), color, "S");
                        map.put(c.toString(),c);
                    }else{
                        c.addScheme(color, "S");
                    }

                }else{//ddl after start

                    ddlutil.set(Integer.valueOf(strtrs[0]),Integer.valueOf(strtrs[1])-1,Integer.valueOf(strtrs[2]),0,0);
                    stutil.set(Integer.valueOf(strtrs1[0]),Integer.valueOf(strtrs1[1])-1,Integer.valueOf(strtrs1[2]),0,0);
//                    Toast.makeText(getContext(), strtrs[0]+strtrs[1]+strtrs[2], Toast.LENGTH_SHORT).show();
                    Calendar c=map.get(transToKey(stutil));
                    if(c==null){

//                        Toast.makeText(getContext(), stkey, Toast.LENGTH_SHORT).show();
                        c=getSchemeCalendar(Integer.valueOf(strtrs1[0]), Integer.valueOf(strtrs1[1]), Integer.valueOf(strtrs1[2]), color, "S");
                        map.put(c.toString(),c);
                    }else{
                        c.addScheme(color, "S");
                    }
                    Log.e("32112345",transToKey(stutil));
                    stutil.add(5,1);

//                    String st=String.valueOf(ddlutil.get(android.icu.util.Calendar.YEAR))+String.valueOf(ddlutil.get(android.icu.util.Calendar.MONTH))+String.valueOf(ddlutil.get(android.icu.util.Calendar.DATE));
//                    String st2=String.valueOf(stutil.get(android.icu.util.Calendar.YEAR))+String.valueOf(stutil.get(android.icu.util.Calendar.MONTH))+String.valueOf(stutil.get(android.icu.util.Calendar.DATE));
//                    Toast.makeText(getContext(), st+" "+st2, Toast.LENGTH_SHORT).show();
//                    Log.e("123",String.valueOf(ddlutil.get(android.icu.util.Calendar.YEAR))+String.valueOf(ddlutil.get(android.icu.util.Calendar.MONTH))+String.valueOf(ddlutil.get(android.icu.util.Calendar.DATE)));
//                    Log.e("123",String.valueOf(stutil.get(android.icu.util.Calendar.YEAR))+String.valueOf(stutil.get(android.icu.util.Calendar.MONTH))+String.valueOf(stutil.get(android.icu.util.Calendar.DATE)));
                    while(stutil.before(ddlutil)){
                        String key="";
                        key=transToKey(stutil);
                        Log.e("123",key);
                        c=map.get(key);
                        if(c==null){
                            c=getSchemeCalendar(stutil.get(android.icu.util.Calendar.YEAR),stutil.get(android.icu.util.Calendar.MONTH)+1,stutil.get(android.icu.util.Calendar.DATE),color,"D");
//                            Log.e("123",c.toString());
                            map.put(c.toString(),c);
                        }else{
                            c.addScheme(color, "D");
                        }
                        stutil.add(5,1);
                    }
                    c=map.get(transToKey(ddlutil));
                    if(c==null){
                        c=getSchemeCalendar(Integer.valueOf(strtrs[0]), Integer.valueOf(strtrs[1]), Integer.valueOf(strtrs[2]), color, "E");
                        map.put(c.toString(),c);
                    }else{
                        c.addScheme(color, "E");
                    }
                }


            }
            TaskBlock tb=new TaskBlock();
            tb.setSttime(sttime);
            tb.setDDL(ddl);
            tb.setTN(name);
            tb.setId(id);
            tb.setColor(color);
            TaskList.add(tb);
        }

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
        daySelectd=mYear+"-"+calendar.getMonth() +"-"+ calendar.getDay();
        if (isClick) {
            taskClassify(mYear+"-"+calendar.getMonth() +"-"+ calendar.getDay());
//            Toast.makeText(getContext(), getCalendarText(calendar), Toast.LENGTH_SHORT).show();
        }
//        Log.e("lunar "," --  " + calendar.getLunarCalendar().toString() + "\n" +
//        "  --  " + calendar.getLunarCalendar().getYear());

//        Log.e("onDateSelected", "  -- " + calendar.getYear() +
//                "  --  " + calendar.getMonth() +
//                "  -- " + calendar.getDay() +
//                "  --  " + isClick + "  --   " + calendar.getScheme());
//        Log.e("onDateSelected", "  " + mCalendarView.getSelectedCalendar().getScheme() +
//                "  --  " + mCalendarView.getSelectedCalendar().isCurrentDay());
//        Log.e("干支年纪 ： ", " -- " + TrunkBranchAnnals.getTrunkBranchYear(calendar.getLunarCalendar().getYear()));
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
//        Toast.makeText(getContext(), "click", Toast.LENGTH_SHORT).show();
    }
    public void taskClassify(String date){
//        Toast.makeText(getContext(), date, Toast.LENGTH_SHORT).show();
        inSchList.clear();
        String strs[]=date.split("-");
        for(int i = 0;i < TaskList.size(); i ++){
//            Toast.makeText(getContext(), String.valueOf(i), Toast.LENGTH_SHORT).show();
            TaskBlock tb=TaskList.get(i);
            String ddl= tb.getDDL();
            String st=tb.getSttime();
            if(!ddl.isEmpty()&&!st.isEmpty()){
                String ddl1[]=ddl.split(" ");
                String ddls[]=ddl1[0].split("-");
                String sts[]=st.split("-");
                android.icu.util.Calendar today= android.icu.util.Calendar.getInstance(); android.icu.util.Calendar stc= android.icu.util.Calendar.getInstance();
                today.set(Integer.valueOf(strs[0]),Integer.valueOf(strs[1])-1,Integer.valueOf(strs[2]),0,0);
                stc.set(Integer.valueOf(sts[0]),Integer.valueOf(sts[1])-1,Integer.valueOf(sts[2]),0,0);
                android.icu.util.Calendar ddlc= android.icu.util.Calendar.getInstance();
                ddlc.set(Integer.valueOf(ddls[0]),Integer.valueOf(ddls[1])-1,Integer.valueOf(ddls[2]),0,0);

                if(!today.after(ddlc)&&!today.before(stc)){
//                    Toast.makeText(getContext(),date+ddl+st, Toast.LENGTH_SHORT).show();

                    inSchList.add(tb);
                }else if(!today.before(ddlc)&&!today.after(stc)){
//                    inSchList.add(tb);
                }
            }
        }
        inschAdapter.notifyDataSetChanged();


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
    public void refreshData(){
        initData();
        taskClassify(daySelectd);

    }
    public class inSchAdapter extends RecyclerView.Adapter<inSchViewHolder> {
        //        private BlockClickListener BlockClickListener = null;
        @NonNull
        @Override
        public inSchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View viewt = LayoutInflater.from(getContext()).inflate(R.layout.module_task_introduce, parent, false);

                return new inSchViewHolder(viewt);
        }

        @Override
        public void onBindViewHolder(@NonNull inSchViewHolder holder, int position) {


            TaskBlock tb = inSchList.get(position);
            MyProgressBar dot=holder.itemView.findViewById(R.id.task_intro_color);
            dot.setColor(tb.getColor());
            Date date;
            if (tb.getDDL().isEmpty()) {
                holder.daysRemain.setText("余 -");
            } else {
                try {
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                    date = df.parse(tb.getDDL());
                    holder.daysRemain.setText(ShowTimeIntervalDDL(date));
                    if (String.valueOf(holder.daysRemain.getText()).indexOf("超时") != - 1){
                        holder.rmHinter.setText("TIMEOUT");
                        holder.daysRemain.setText(String.valueOf(holder.daysRemain.getText()).substring(3));
                    }else{
                        holder.rmHinter.setText("REMAINS");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if (tb.getDDL().isEmpty()) {
                holder.daysRemain.setText("余 -");
            } else {
                try {
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    date = df.parse(tb.getSttime());
                    holder.daysStarted.setText(ShowTimeIntervalST(date));
                    if(holder.daysStarted.getText().equals("NOT STARTED")){
                        holder.stHinter.setText("NOT STARTED");
                        holder.daysStarted.setText("");
                    }else{
                        holder.stHinter.setText("STARTED");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            holder.taskName.setText(tb.getTN());

            holder.DDL.setText(tb.getDDL().isEmpty() ? "-" : tb.getDDL());
//                ((TBlockViewHolder) holder).textView3.setText(String.valueOf(TaskBlockList.size()));
            holder.itemView.setOnClickListener(view -> {
                int pos = holder.getAbsoluteAdapterPosition();
                ((MainActivity)getActivity()).vpMain.setCurrentItem(1, true);
                ((TaskFragment)(((MainActivity)getActivity()).getFragmentList().get(1))).jump(inSchList.get(pos).getId());
            });
            holder.itemView.setOnLongClickListener(view -> {
                int pos = holder.getAbsoluteAdapterPosition();
                Vibrator v = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(30);
                return true;
            });
        }


        @Override
        public int getItemCount() {
            return  inSchList.size();
        }

    }
    public static class inSchViewHolder extends RecyclerView.ViewHolder {
        TextView taskName;
        TextView daysRemain;
        TextView daysStarted;
        TextView DDL;
        TextView stHinter;
        TextView rmHinter;
        ConstraintLayout constraintLayout;

        public inSchViewHolder(@NonNull View itemView) {
            super(itemView);
            taskName = itemView.findViewById(R.id.task_block_name);
            daysRemain = itemView.findViewById(R.id.task_block_days_remain);
            daysStarted=itemView.findViewById(R.id.task_block_sted);
            DDL = itemView.findViewById(R.id.task_block_ddl);
            stHinter = itemView.findViewById(R.id.st_hinter);
            rmHinter = itemView.findViewById(R.id.rm_hinter);
            constraintLayout = itemView.findViewById(R.id.task_block);
        }
    }
    public String transToKey( android.icu.util.Calendar util){
        String key="";
        if(util.get(android.icu.util.Calendar.MONTH)+1<10){
            if(util.get(android.icu.util.Calendar.DATE)<10){
                key=String.valueOf(util.get(android.icu.util.Calendar.YEAR))+"0"+String.valueOf(util.get(android.icu.util.Calendar.MONTH)+1)+"0"+String.valueOf(util.get(android.icu.util.Calendar.DATE));
            }else{
                key=String.valueOf(util.get(android.icu.util.Calendar.YEAR))+"0"+String.valueOf(util.get(android.icu.util.Calendar.MONTH)+1)+String.valueOf(util.get(android.icu.util.Calendar.DATE));
            }
        }else{
            if(util.get(android.icu.util.Calendar.DATE)<10){
                key=String.valueOf(util.get(android.icu.util.Calendar.YEAR))+String.valueOf(util.get(android.icu.util.Calendar.MONTH)+1)+"0"+String.valueOf(util.get(android.icu.util.Calendar.DATE));
            }else{
                key=String.valueOf(util.get(android.icu.util.Calendar.YEAR))+String.valueOf(util.get(android.icu.util.Calendar.MONTH)+1)+String.valueOf(util.get(android.icu.util.Calendar.DATE));
            }
        }
        return key;
    }
    public static String ShowTimeIntervalDDL(Date date) {
        long lDate1 = date.getTime();
        long lDate2 =  new Date(System.currentTimeMillis()).getTime();
        long diff = lDate1 - lDate2;
        long day = diff / (24 * 60 * 60 * 1000);
        long hour = diff / (60 * 60 * 1000) - day * 24;
        long min = diff / (60 * 1000) - day * 24 * 60 - hour * 60;
        long sec = diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60;
        if(day<0||hour<0||min<0||sec<0){
            return "超时 "+String.valueOf(-day)+"D "+String.valueOf(-hour)+"H";
        }
        return String.valueOf(day)+"D "+String.valueOf(hour)+"H";
    }
    public static String ShowTimeIntervalST(Date date) {
        long lDate1 = date.getTime();
        long lDate2 =  new Date(System.currentTimeMillis()).getTime();
        long diff = lDate2 - lDate1;
        long day = diff / (24 * 60 * 60 * 1000);
        long hour = diff / (60 * 60 * 1000) - day * 24;
        long min = diff / (60 * 1000) - day * 24 * 60 - hour * 60;
        long sec = diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60;
        if(day<0||hour<0||min<0||sec<0){
            return "NOT STARTED";
        }
        return String.valueOf(day)+"D";
    }
}