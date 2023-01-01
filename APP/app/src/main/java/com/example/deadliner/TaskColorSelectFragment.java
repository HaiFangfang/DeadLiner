package com.example.deadliner;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class TaskColorSelectFragment extends BottomSheetDialogFragment {
    String id;
    private ColorBarView mCbv_color_pick;
    private TextView mTv_text_color_b;
    private View big;
    Button confirm;
    /**
     * 顶部向下偏移量
     */
    private int topOffset = 300;
    private BottomSheetBehavior<FrameLayout> behavior;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        id=getArguments().getString("id");
//        Toast.makeText(getContext(), id, Toast.LENGTH_SHORT).show();

        if (getContext() == null) {
            return super.onCreateDialog(savedInstanceState);
        }
        return new BottomSheetDialog(getContext(), R.style.TransparentBottomSheetStyle);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View dialogView = inflater.inflate(R.layout.module_color_select, container, false);

        big=dialogView;
        initView();
        initListener();
        confirm=dialogView.findViewById(R.id.color_confirm);
        confirm.setOnClickListener(view -> {
            ((TaskFragment)(((MainActivity)getActivity()).getFragmentList().get(1))).page.refreshColor(mCbv_color_pick.getCurrentColor());
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
    private void initView() {
        mCbv_color_pick = big.findViewById(R.id.cbv_color_pick);
        mTv_text_color_b = big.findViewById(R.id.tv_text_color_b);
    }

    private void initListener() {

        mTv_text_color_b.setOnClickListener(view -> {
            mCbv_color_pick.setCurrentColor(((ColorDrawable) mTv_text_color_b.getBackground()).getColor());
        });

        mCbv_color_pick.setOnColorChangerListener(new ColorBarView.OnColorChangeListener() {
            @Override
            public void onColorChange(int color) {
                GradientDrawable gd=new GradientDrawable();
                gd.setShape(GradientDrawable.RECTANGLE);
                gd.setCornerRadius(10);
                gd.setColor(color);
                mTv_text_color_b.setBackground(gd);
            }
        });
        mCbv_color_pick.setCurrentColor(-16711726);

    }

//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.tv_text_color_a:
//                mCbv_color_pick.setCurrentColor(((ColorDrawable) mTv_text_color_a.getBackground()).getColor());
//                break;
//            case R.id.tv_text_color_b:
//                mCbv_color_pick.setCurrentColor(((ColorDrawable) mTv_text_color_b.getBackground()).getColor());
//                break;
//            case R.id.tv_text_color_c:
//                mCbv_color_pick.setCurrentColor(((ColorDrawable) mTv_text_color_c.getBackground()).getColor());
//                break;
//        }
//    }
}