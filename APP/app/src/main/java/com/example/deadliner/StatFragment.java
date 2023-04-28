package com.example.deadliner;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatFragment extends Fragment {

   TextView tv_timer;
    TextView img_start;
    TextView img_clear;
    TextView img_over;
    private boolean isStopCount = true;

    private boolean isPause = false;

    private Handler mHandler = new Handler();
    ArrayList<TaskTimes> rangeList=new ArrayList<>();
    long timer = 0;
    private String timeStr = "";
    RecyclerView taskRange;
    rangeAdapter rAdapter;

    public StatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StaticFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StatFragment newInstance(String param1, String param2) {
        StatFragment fragment = new StatFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_stat, container, false);
        tv_timer = v.findViewById(R.id.tv_timer);
        img_start =  v.findViewById(R.id.img_start);
        img_clear =  v.findViewById(R.id.img_clear);
        img_over= v.findViewById(R.id.img_over);
        taskRange=v.findViewById(R.id.time_range);
        rAdapter=new rangeAdapter();
        taskRange.setLayoutManager(new LinearLayoutManager(getContext()));
        taskRange.setAdapter(rAdapter);
        img_clear.setOnClickListener(view -> {
            timer=0;
            tv_timer.setText("00:00:00");
        });
        img_start.setOnClickListener(view -> {
            if(!isPause){
                isPause = true;
                isStopCount = false;
                img_start.setText("PAUSE");
//                img_start.setImageResource(R.drawable.btn_pause);
            } else{
                isPause = false;
                isStopCount = true;
                img_start.setText("START");
//                img_start.setImageResource(R.drawable.btn_start);
            }
        });
        img_over.setOnClickListener(view -> {
            if(timer!=0){
            isPause = false;
            isStopCount = true;
            img_start.setText("START");
            openTaskSelectPop();}
        });
        taskRange.addItemDecoration(getBlockDecoration());
        countTimer();
        refreshTaskRange();
        return v;
    }


    public void openTaskSelectPop(){
        TaskPickFragment dialog = new TaskPickFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("second",timer/1000);
        dialog.setArguments(bundle);
        dialog.show(getParentFragmentManager(), "dialog_fragment");
    }
    private Runnable TimerRunnable = new Runnable() {

        @Override
        public void run() {
            if(!isStopCount){
                timer += 1000;
                int totalSeconds = (int) (timer / 1000);
                int seconds = totalSeconds % 60;
                int minutes = (totalSeconds / 60) % 60;
                int hours = totalSeconds / 3600;
                if(hours<10){
                    timeStr="0"+String.valueOf(hours);
                }else{
                    timeStr=String.valueOf(hours);
                }
                timeStr+=":";
                if(minutes<10){
                    timeStr+="0"+String.valueOf(minutes);
                }else{
                    timeStr+=String.valueOf(minutes);
                }
                timeStr+=":";
                if(seconds<10){
                    timeStr+="0"+String.valueOf(seconds);
                }else{
                    timeStr+=String.valueOf(seconds);
                }
                tv_timer.setText(timeStr);
            }
            countTimer();
        }
    };

    private void countTimer(){
        mHandler.postDelayed(TimerRunnable, 1000);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(TimerRunnable);
    }

    public void refreshTaskRange(){
        rangeList.clear();
        Cursor c=MainActivity.getdb().getWritableDatabase().rawQuery("select sum(seconds) as tt,task from times group by task order by tt desc", null);
        int i=1;
            while(c.moveToNext()){

                TaskTimes tt=new TaskTimes();
                tt.setId(c.getString(1));
                Cursor nm=MainActivity.getdb().getWritableDatabase().query("task",new String[]{"name"},"id=?",new String[]{tt.getId()},null,null,null,null);
                nm.moveToFirst();
                tt.setTN(nm.getString(0));
                tt.setSeconds(c.getInt(0));
                rangeList.add(tt);

            }

        rAdapter.notifyDataSetChanged();

    }
    private RecyclerView.ItemDecoration getBlockDecoration() {
        return new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = 15;
                outRect.bottom=5;

            }
        };
    }
    public class rangeAdapter extends RecyclerView.Adapter<rangeViewHolder> {
        @NonNull
        @Override
        public rangeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View viewt = LayoutInflater.from(getContext()).inflate(R.layout.module_task_range, parent, false);

            return new rangeViewHolder(viewt);
        }


        @Override
        public void onBindViewHolder(@NonNull rangeViewHolder holder, int position) {
            if(position==0){
                holder.range.setTextSize(40);
            }
            if(position==1){
                holder.range.setTextSize(30);
            }
            if(position==2){
                holder.range.setTextSize(28);
            }
            holder.name.setText(rangeList.get(position).getTN());
            holder.time.setText(getTimeStr(rangeList.get(position).getSeconds()));
            holder.range.setText(String.valueOf(position+1));
        }


        @Override
        public int getItemCount() {
            return  rangeList.size();
        }

    }
    public static class rangeViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView range;
        TextView time;

        public rangeViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.task_range_name);
            range=itemView.findViewById(R.id.range_range);
            time=itemView.findViewById(R.id.seconds);

        }

    }
    public String getTimeStr(int totalSeconds){
        String timeStr="";
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        if(hours<10){
            timeStr="0"+String.valueOf(hours);
        }else{
            timeStr=String.valueOf(hours);
        }
        timeStr+=":";
        if(minutes<10){
            timeStr+="0"+String.valueOf(minutes);
        }else{
            timeStr+=String.valueOf(minutes);
        }
        timeStr+=":";
        if(seconds<10){
            timeStr+="0"+String.valueOf(seconds);
        }else{
            timeStr+=String.valueOf(seconds);
        }
        return timeStr;
    }

}