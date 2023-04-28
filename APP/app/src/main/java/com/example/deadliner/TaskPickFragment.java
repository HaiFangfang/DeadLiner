package com.example.deadliner;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class TaskPickFragment extends BottomSheetDialogFragment {

    private ArrayList<CharSequence> taskN = new ArrayList<>();
    private ArrayAdapter<CharSequence> taskA = null;
    private ArrayList<CharSequence> ids=new ArrayList<>();
    int posi;
    Spinner sp;
    Long time;
    /**
     * 顶部向下偏移量
     */
    private int topOffset = 300;
    private BottomSheetBehavior<FrameLayout> behavior;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        time=getArguments().getLong("second");
        Cursor c=MainActivity.getdb().getWritableDatabase().query("task",null,null,null,null,null,null);
        while(c.moveToNext()){
            taskN.add(c.getString(1));
            ids.add(c.getString(0));
        }
        return new BottomSheetDialog(getContext(), R.style.TransparentBottomSheetStyle);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View dialogView = inflater.inflate(R.layout.fragment_task_pick, container, false);

        ImageView conf=dialogView.findViewById(R.id.cata_name_confirm);

        conf.setOnClickListener(view -> {
            freshData();
            dismiss();
        });
        sp = dialogView.findViewById(R.id.spinner);
//将数据cityInfo填充到适配器adapterCity中
        taskA = new ArrayAdapter<CharSequence>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, taskN);
//设置下拉框的数据适配器adapterCity
        sp.setAdapter(taskA);
        sp.setOnItemSelectedListener (new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //这个方法里可以对点击事件进行处理
                //i指的是点击的位置,通过i可以取到相应的数据源
                posi=i;
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
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
    public void freshData(){
        MainActivity.getdb().getWritableDatabase().execSQL("insert into times(seconds,task) values (?,?)", new Object[] { time,ids.get(posi)});
        ((StatFragment)(((MainActivity)getActivity()).getFragmentList().get(2))).refreshTaskRange();
        ((StatFragment)(((MainActivity)getActivity()).getFragmentList().get(2))).timer=0;
        ((StatFragment)(((MainActivity)getActivity()).getFragmentList().get(2))).tv_timer.setText("00:00:00");
    }
}