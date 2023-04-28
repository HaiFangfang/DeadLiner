package com.example.deadliner;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class stdDialog extends Dialog implements View.OnClickListener {

    private TextView mTitle, mContent;
    private TextView mConfirm, mCancel;
boolean cata;
    private Context mContext;
    private String content;
    private OncloseListener listener;
    private String positiveName;
    private String negativeName;
    private String title;

    public stdDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public stdDialog(@NonNull Context context, int themeResId, String content) {
        super(context, themeResId);
        this.mContext = context;
        this.content = content;
    }

    public stdDialog(@NonNull Context context, int themeResId, OncloseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.listener = listener;
    }

    public stdDialog(@NonNull Context context, int themeResId, String content,Boolean Cata, OncloseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.content = content;
        this.listener = listener;
        this.cata=Cata;
    }

    protected stdDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    /**
     * 设置弹框标题
     * @param title 标题内容
     * @return
     */
    public stdDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * 设置弹框的提示内容
     * @param content 弹框的提示内容
     * @return
     */
    public stdDialog setContent(String content) {
        this.content = content;
        return this;
    }

    /**
     * 设置弹框确认键的内容
     * @param name 确认键显示内容
     * @return
     */
    public stdDialog setPositiveButton(String name) {
        this.positiveName = name;
        return this;
    }

    /**
     * 设置弹框取消键的内容
     * @param name 取消键显示内容
     * @return
     */
    public stdDialog setNegativeButton(String name) {
        this.negativeName = name;
        return this;
    }
    public void setDialogWidthAndHeight(Dialog dialog,int widthDp,int heightDp){
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;// 屏幕宽度（像素）
        int height= dm.heightPixels; // 屏幕高度（像素）
        float density = dm.density;//屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = dm.densityDpi;//屏幕密度dpi（120 / 160 / 240）
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.width = (int) (widthDp*densityDpi/160);
        layoutParams.height = (int) (heightDp*densityDpi/160);
        dialog.getWindow().setAttributes(layoutParams);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(cata){
            setContentView(R.layout.module_pop_dialog);
        }else{
            setContentView(R.layout.module_pop_dialog_task);
        }

        setCanceledOnTouchOutside(true);
        mTitle = findViewById(R.id.dialog_title);
        mContent = findViewById(R.id.dialog_content);
        mConfirm = findViewById(R.id.confirm);
        mCancel = findViewById(R.id.cancel);
        mConfirm.setOnClickListener(this);
        mCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm:
//                MainActivity.getdb().getWritableDatabase().delete("cata","id=?",new String[]{content});
//                MainActivity.getdb().getWritableDatabase().delete("task","cata=?",new String[]{content});
                if (listener != null) {
                    listener.onClick(true);
                }
                this.dismiss();
                break;
            case R.id.cancel:
                this.dismiss();
                break;
        }
    }

    public interface OncloseListener {
        void onClick(boolean confirm);
    }
}
