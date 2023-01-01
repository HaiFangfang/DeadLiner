package com.example.deadliner;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatFragment extends Fragment {

    private TextView tv_timer;
    private ImageView img_start;
    private boolean isStopCount = false;

    private boolean isPause = true;

    private Handler mHandler = new Handler();

    private long timer = 0;
    private String timeStr = "";


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
        img_start.setOnClickListener(view -> {
            if(!isPause){
                isPause = true;
                isStopCount = false;
//                img_start.setImageResource(R.drawable.btn_pause);
            } else{
                isPause = false;
                isStopCount = true;
//                img_start.setImageResource(R.drawable.btn_start);
            }
        });
        countTimer();
        return v;
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

                timeStr = String.valueOf(hours)+String.valueOf(minutes)+String.valueOf(seconds);
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

}