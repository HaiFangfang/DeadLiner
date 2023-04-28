package com.example.deadliner;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.lang.reflect.Field;
import java.sql.Time;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class TaskDatePickerFragment extends BottomSheetDialogFragment {
    DatePicker dp;
    DatePicker dp_start;
    TimePicker tp;
    String ddl;
    String st;
    /**
     * 顶部向下偏移量
     */
    private int topOffset = 300;
    private BottomSheetBehavior<FrameLayout> behavior;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ddl=getArguments().getString("ddl");
        st=getArguments().getString("st");
        if (getContext() == null) {
            return super.onCreateDialog(savedInstanceState);
        }
        return new BottomSheetDialog(getContext(), R.style.TransparentBottomSheetStyle);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View dialogView = inflater.inflate(R.layout.module_picker_date_time, container, false);
        tp = dialogView.findViewById(R.id.DDL_time_picker);
        dp = dialogView.findViewById(R.id.DDL_date_picker) ;
        dp_start=dialogView.findViewById(R.id.start_date_picker);
        tp.setIs24HourView(true);
        resizePikcer(dp,300,130);
        resizePikcer(dp_start,300,130);
        resizePikcer(tp,300,100);
        SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-dd-HH-mm-ss");
        SimpleDateFormat forma = new SimpleDateFormat("YYYY-MM-dd HH:mm");
        SimpleDateFormat form = new SimpleDateFormat("YYYY-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());
//获取当前时间
        if(!ddl.equals("-")){
          String d[]=ddl.split(" ");
          String c[]=d[0].split("-");
          String t[]=d[1].split(":");
          dp.init(Integer.valueOf(c[0]),Integer.valueOf(c[1])-1,Integer.valueOf(c[2]),null);
            tp.setMinute(Integer.valueOf(t[0]));
            tp.setHour(Integer.valueOf(t[1]));
        }else{
            ddl="";
            String str = formatter.format(curDate);
            String[] strs=str.split("-");
            dp.init(Integer.valueOf(strs[0]),Integer.valueOf(strs[1])-1,Integer.valueOf(strs[2]),null);
            tp.setMinute(0);
            tp.setHour(0);
        }
        if(!st.equals("-")){
            String c[]=st.split("-");
            dp_start.init(Integer.valueOf(c[0]),Integer.valueOf(c[1])-1,Integer.valueOf(c[2]),null);
        }else{
            st="";
        }


        ImageView ret=dialogView.findViewById(R.id.date_picker_confirm);
        ret.setOnClickListener(view -> {
            int y=dp.getYear();
            int m=dp.getMonth()+1;
            int d=dp.getDayOfMonth();
            int h=tp.getHour();
            int M=tp.getMinute();
            int ys=dp_start.getYear();
            int ms=dp_start.getMonth()+1;
            int ds=dp_start.getDayOfMonth();
            String DDL=y+"-"+m+"-"+d;
            String Time;
            if(h<10){
                if(M<10){
                    Time="0"+h+":"+"0"+M;
                }else{
                    Time="0"+h+":"+M;
                }
            }else{
                if(M<10){
                    Time=h+":"+"0"+M;
                }else{
                    Time=h+":"+M;
                }
            }
            String ST=ys+"-"+ms+"-"+ds;
            ((TaskFragment)(((MainActivity)getActivity()).getFragmentList().get(1))).page.refreshTimes(DDL,Time,ST);

            dismiss();
        });
        return dialogView;
    }
    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams windowParams = window.getAttributes();
        windowParams.dimAmount = 0.1f;
        window.setAttributes(windowParams);

//        window.getAttributes().dimAmount=0;
//        window.getAttributes().flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
//        window.setAttributes(lp);

        // 设置软键盘不自动弹出
//        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
//        BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
//        FrameLayout bottomSheet = dialog.getDelegate().findViewById(android.support.design.R.id.design_bottom_sheet);
//        if (bottomSheet != null) {
//            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomSheet.getLayoutParams();
//            layoutParams.height = getHeight();
//            behavior = BottomSheetBehavior.from(bottomSheet);
//            // 初始为展开状态
//            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//        }
    }

    /**
     * 获取屏幕高度
     *
     * @return height
     */
    private int getHeight() {
        int height = 1920;
        if (getContext() != null) {
            WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
            Point point = new Point();
            if (wm != null) {
                // 使用Point已经减去了状态栏高度
                wm.getDefaultDisplay().getSize(point);
                height = point.y - getTopOffset();
            }
        }
        return height;
    }
    public int getTopOffset() {
        return topOffset;
    }
    public void setTopOffset(int topOffset) {
        this.topOffset = topOffset;
    }
    public BottomSheetBehavior<FrameLayout> getBehavior() {
        return behavior;
    }









    private void resizePikcer(FrameLayout tp,int h,int w) {
        List<NumberPicker> npList = findNumberPicker(tp);
        for (NumberPicker np : npList) {
            resizeNumberPicker(np,h,w);
        }
    }
    private List<NumberPicker> findNumberPicker(ViewGroup viewGroup) {
        List<NumberPicker> npList = new ArrayList<NumberPicker>();
        View child = null;
        if (null != viewGroup) {
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                child = viewGroup.getChildAt(i);
                if (child instanceof NumberPicker) {
                    npList.add((NumberPicker) child);
                } else if (child instanceof LinearLayout) {
                    List<NumberPicker> result = findNumberPicker((ViewGroup) child);
                    if (result.size() > 0) {
                        return result;
                    }
                }
            }
        }
        return npList;
    }
    private void resizeNumberPicker(NumberPicker np,int h,int w) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(w,h, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 0, 10, 0);
        np.setLayoutParams(params);
    }
    private void setDatePickerDividerHight(DatePicker datePicker){
        // Divider changing:

        // 获取 mSpinners
        LinearLayout llFirst= (LinearLayout) datePicker.getChildAt(0);

        // 获取 NumberPicker
        LinearLayout mSpinners= (LinearLayout) llFirst.getChildAt(0);
        for (int i = 0; i < mSpinners.getChildCount(); i++) {
            NumberPicker picker = (NumberPicker) mSpinners.getChildAt(i);

            Field[] pickerFields = NumberPicker.class.getDeclaredFields();
            for (Field pf : pickerFields) {
                if(pf.getName().equals("mSelectionDividersDistance")){
                    pf.setAccessible(true);
                    try {
//                       pf.set(picker.getHeight(), 20);
                        pf.set(picker,20);//按照需求在此处修改
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    }  catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }


}
