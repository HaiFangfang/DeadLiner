package com.example.deadliner;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

public class LeanTextView extends androidx.appcompat.widget.AppCompatTextView {
    public int getmDegrees() {
        return mDegrees;
    }

    public void setmDegrees(int mDegrees) {
        this.mDegrees = mDegrees;
        invalidate();
    }

    private int mDegrees;

    public LeanTextView(Context context) {
        super(context, null);
    }

    public LeanTextView(Context context, AttributeSet attrs) {
        super(context, attrs, android.R.attr.textViewStyle);
        this.setGravity(Gravity.CENTER);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LeanTextView);
        mDegrees = a.getDimensionPixelSize(R.styleable.LeanTextView_degree, 0);
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(getCompoundPaddingLeft(), getExtendedPaddingTop());
        canvas.rotate(-20, this.getWidth() / 2f, this.getHeight() / 2f);
        canvas.translate(-10,-150);


//        String mText="已完成";
//        //绘制
//        Paint mPaint = new Paint();
//        mPaint.setColor(Color.WHITE);
//        mPaint.setTextSize(18);
//        float x = getWidth() / 2 - mPaint.measureText(mText) / 2;
//        Paint.FontMetricsInt fm = mPaint.getFontMetricsInt();
////        float y = getHeight() / 2 + (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
//        canvas.drawText("已完成", x, this.getHeight() / 2f, mPaint);

        super.onDraw(canvas);
        canvas.restore();
    }
}