package com.example.deadliner;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2015/9/16.
 */
public class MyProgressBar extends View {
    private int maxProgress=100;
    private int currentProgress;
    private int width;
    private int height;
    private Paint mPaintProgressMax;
    private Paint mPaintProgressCurrent;


    private static final int UPDATA_PROGRESS=0x28;
    private Paint mPaintText;

    public int getMaxProgress() {
        return maxProgress;
    }

    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
    }

    public int getCurrentProgress() {
        return currentProgress;
    }

    public void setCurrentProgress(int currentProgress) {
        this.currentProgress = currentProgress;
    }

    public MyProgressBar(Context context) {
        super(context);
    }
   public void setColor(int color){
       mPaintProgressCurrent.setColor(color);
   }
    public  MyProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaintProgressMax=new Paint();
        mPaintProgressMax.setColor(Color.WHITE);
        mPaintProgressMax.setAntiAlias(true);
        mPaintProgressCurrent=new Paint();
        mPaintProgressCurrent.setColor(Color.GREEN);
        mPaintProgressCurrent.setAntiAlias(true);
        mPaintText=new Paint();
        mPaintText.setTextSize(100);
        mPaintText.setColor(Color.RED);
        mPaintText.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(width/2,height/2,30,mPaintProgressMax);
        canvas.drawCircle(width/2,height/2,22,mPaintProgressCurrent);

    }

}