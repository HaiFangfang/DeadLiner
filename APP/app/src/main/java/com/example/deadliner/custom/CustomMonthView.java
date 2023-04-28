package com.example.deadliner.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.text.TextUtils;

import com.haibin.calendarview.BaseMonthView;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.MonthAnimHelper;
import com.haibin.calendarview.MonthView;

/**
 * 演示一个变态需求的月视图
 * Created by huanghaibin on 2018/2/9.
 */

public class CustomMonthView extends MonthView {

    private int mRadius;

    /**
     * 自定义标记的文本画笔
     */
    private Paint mTextPaint = new Paint();

    private Paint mRectPaint = new Paint();
    /**
     * 24节气画笔
     */
    private Paint mSolarTermTextPaint = new Paint();

    /**
     * 背景圆点
     */
//    private Paint mPointPaint = new Paint();

    /**
     * 今天的背景色
     */
    private Paint mCurrentDayPaint = new Paint();

    /**
     * 圆点半径
     */
    private float mPointRadius;

    private int mPadding;

    private float mCircleRadius;
    /**
     * 自定义魅族标记的圆形背景
     */
    private Paint mSchemeBasicPaint = new Paint();

    private float mSchemeBaseLine;

    public CustomMonthView(Context context) {
        super(context);

        mTextPaint.setTextSize(dipToPx(context, 8));
        PorterDuffXfermode mode=new PorterDuffXfermode(PorterDuff.Mode.DARKEN);
        mTextPaint.setXfermode(mode);
        mTextPaint.setColor(Color.GRAY);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setFakeBoldText(true);


        mSolarTermTextPaint.setColor(0xff489dff);
        mSolarTermTextPaint.setAntiAlias(true);
        mSolarTermTextPaint.setTextAlign(Paint.Align.CENTER);

        mSchemeBasicPaint.setAntiAlias(true);
        mSchemeBasicPaint.setStyle(Paint.Style.FILL);
        mSchemeBasicPaint.setTextAlign(Paint.Align.CENTER);
        mSchemeBasicPaint.setFakeBoldText(true);
        mSchemeBasicPaint.setColor(Color.WHITE);


        mCurrentDayPaint.setAntiAlias(true);
        mCurrentDayPaint.setStyle(Paint.Style.FILL);
        mCurrentDayPaint.setColor(0xFFeaeaea);

//        mPointPaint.setAntiAlias(true);
//        mPointPaint.setStyle(Paint.Style.FILL);
//        mPointPaint.setTextAlign(Paint.Align.CENTER);
//        mPointPaint.setColor(Color.RED);

        mCircleRadius = dipToPx(getContext(), 7);

        mPadding = dipToPx(getContext(), 3);

        mPointRadius = dipToPx(context, 2);

        Paint.FontMetrics metrics = mSchemeBasicPaint.getFontMetrics();
        mSchemeBaseLine = mCircleRadius - metrics.descent + (metrics.bottom - metrics.top) / 2 + dipToPx(getContext(), 1);
    }


    @Override
    protected void onPreviewHook() {
        mSolarTermTextPaint.setTextSize(mCurMonthLunarTextPaint.getTextSize());
        mRadius = Math.min(mItemWidth, mItemHeight) / 11 * 5+20;
    }


    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme) {
        int cx = x + mItemWidth / 2+3;
        int cy = y + mItemHeight / 2;
        PorterDuffXfermode mode=new PorterDuffXfermode(PorterDuff.Mode.DARKEN);
        mSelectedPaint.setColor(0x88BCE6EE);
        mSelectedPaint.setXfermode(mode);
        if (isTouchDown && mCurrentItem == mItems.indexOf(getIndex())) {
            //点击当前选中的item, 缩放效果提示
            canvas.drawCircle(cx, cy, mRadius - dipToPx(getContext(), 4), mSelectedPaint);
        } else {
//            canvas.drawRect(x,y,x + mItemWidth,y + mItemHeight, mSelectedPaint);
            canvas.drawCircle(cx, cy, mRadius, mSelectedPaint);
        }
        return true;
    }

    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x, int y) {

        boolean isSelected = isSelected(calendar);
//        if (isSelected) {
//            mPointPaint.setColor(Color.WHITE);
//        } else {
//            mPointPaint.setColor(Color.GRAY);
//        }
//
//        canvas.drawCircle(x + mItemWidth / 2, y + mItemHeight - 3 * mPadding, mPointRadius, mPointPaint);
    }

    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme, boolean isSelected) {
        int cx = x + mItemWidth / 2;
        int cy = y + mItemHeight / 2;
        int top = y - mItemHeight / 3;

        if (calendar.isCurrentDay() && !isSelected) {
            canvas.drawCircle(cx+2, cy, mRadius-5, mSelectedPaint);
        }

//        if (hasScheme) {
//            int oleft=x+20;
//            int oright=x+mItemWidth-20;
//            int end=y+mItemHeight-9;
//            int start=y+mItemHeight/4;
//
//            for(Calendar.Scheme tmp:calendar.getSchemes()){
//                int color=tmp.getShcemeColor();
//                float hsv[]=new float[3];
//                Color.colorToHSV(color,hsv);
//                float ratio=hsv[0]/360;
//                float spand=end-start;
//                float stripy=start+spand*ratio;
//
//                mTextPaint.setColor(tmp.getShcemeColor());
//                canvas.drawCircle(oleft,stripy,8,mTextPaint);
//                canvas.drawCircle(oright,stripy,8,mTextPaint);
//                if(tmp.getScheme().equals("D")){
//                    if(calendar.getWeek()==6){
//                        canvas.drawRect(x,stripy-8,oright,stripy+8,mTextPaint);
//                    }else if(calendar.getWeek()==0){
//                        canvas.drawRect(oleft,stripy-8,x+mItemWidth,stripy+8,mTextPaint);
//                    }else{
//                        canvas.drawRect(x,stripy-8,x+mItemWidth,stripy+8,mTextPaint);
//                    }
//
//                }else if(tmp.getScheme().equals("S")){
//                    if(calendar.getWeek()==6){
//                        canvas.drawRect(oleft,stripy-8,oright,stripy+8,mTextPaint);
//                    }else{
//                        canvas.drawRect(oleft,stripy-8,x+mItemWidth,stripy+8,mTextPaint);
//                    }
//                }else if(tmp.getScheme().equals("E")){
//                   if(calendar.getWeek()==0){
//                        canvas.drawRect(oleft,stripy-8,oright,stripy+8,mTextPaint);
//                    }else{
//                        canvas.drawRect(x,stripy-8,oright,stripy+8,mTextPaint);
//                    }
//                }
//                else{
//                    canvas.drawRect(oleft,stripy-8,oright,stripy+8,mTextPaint);
//                }
////
////                Toast.makeText(getContext(),tmp.toString(), Toast.LENGTH_SHORT).show();
////                canvas.drawText(tmp.getScheme(), x + mItemWidth - mPadding - mCircleRadius, y + mPadding + mSchemeBaseLine, mTextPaint);
//            }
//
//        }

        //当然可以换成其它对应的画笔就不麻烦，
        if (calendar.isWeekend() && calendar.isCurrentMonth()) {
            mCurMonthTextPaint.setColor(0xFF489dff);
            mCurMonthLunarTextPaint.setColor(0xFF489dff);
            mSchemeTextPaint.setColor(0xFF489dff);
            mSchemeLunarTextPaint.setColor(0xFF489dff);
            mOtherMonthLunarTextPaint.setColor(0xFF489dff);
            mOtherMonthTextPaint.setColor(0xFF489dff);
        } else {
            mCurMonthTextPaint.setColor(0x99333333);
            mCurMonthLunarTextPaint.setColor(0xffCFCFCF);
            mSchemeTextPaint.setColor(0x99333333);
            mSchemeLunarTextPaint.setColor(0xffCFCFCF);

            mOtherMonthTextPaint.setColor(0xFFe1e1e1);
            mOtherMonthLunarTextPaint.setColor(0xFFe1e1e1);
        }

        if (isSelected) {
            mSelectTextPaint.setColor(0xAA000000);
//            PorterDuffXfermode mode  = new PorterDuffXfermode(PorterDuff.Mode.DARKEN);
//            mSelectTextPaint.setXfermode(mode);
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
                    mSelectTextPaint);
//            canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + y + mItemHeight / 10, mSelectedLunarTextPaint);
        } else
        if (hasScheme) {
//
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
                    calendar.isCurrentMonth() ? mSchemeTextPaint : mOtherMonthTextPaint);

//            canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + y + mItemHeight / 10,
//                    !TextUtils.isEmpty(calendar.getSolarTerm()) ? mSolarTermTextPaint : mSchemeLunarTextPaint);
        } else {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() ? mCurMonthTextPaint : mOtherMonthTextPaint);

//            canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + y + mItemHeight / 10,
//                    calendar.isCurrentDay() ? mCurDayLunarTextPaint :
//                            calendar.isCurrentMonth() ? !TextUtils.isEmpty(calendar.getSolarTerm()) ? mSolarTermTextPaint  :
//                                    mCurMonthLunarTextPaint : mOtherMonthLunarTextPaint);
        }

        if (hasScheme) {
            int oleft=x+20;
            int oright=x+mItemWidth-20;
            int end=y+mItemHeight-9;
            int start=y+mItemHeight/4;

            for(Calendar.Scheme tmp:calendar.getSchemes()){
                int color=tmp.getShcemeColor();
                float hsv[]=new float[3];
                Color.colorToHSV(color,hsv);
                float ratio=hsv[0]/360;
                float spand=end-start;
                float stripy=start+spand*ratio;
                if(calendar.isCurrentMonth()){
                    mTextPaint.setColor(tmp.getShcemeColor());
                }else{
                    int c=0;
                    hsv[1]=hsv[1]/3f;
                    hsv[2]=hsv[2]-0.05f;
                    c=Color.HSVToColor(hsv);
                    mTextPaint.setColor(c);
                }
                canvas.drawCircle(oleft,stripy,8,mTextPaint);
                canvas.drawCircle(oright,stripy,8,mTextPaint);
                if(tmp.getScheme().equals("D")){
                    if(calendar.getWeek()==6){
                        canvas.drawRect(x,stripy-8,oright,stripy+8,mTextPaint);
                    }else if(calendar.getWeek()==0){
                        canvas.drawRect(oleft,stripy-8,x+mItemWidth,stripy+8,mTextPaint);
                    }else{
                        canvas.drawRect(x,stripy-8,x+mItemWidth,stripy+8,mTextPaint);
                    }

                }else if(tmp.getScheme().equals("S")){
                    if(calendar.getWeek()==6){
                        canvas.drawRect(oleft,stripy-8,oright,stripy+8,mTextPaint);
                    }else{
                        canvas.drawRect(oleft,stripy-8,x+mItemWidth,stripy+8,mTextPaint);
                    }
                }else if(tmp.getScheme().equals("E")){
                    if(calendar.getWeek()==0){
                        canvas.drawRect(oleft,stripy-8,oright,stripy+8,mTextPaint);
                    }else{
                        canvas.drawRect(x,stripy-8,oright,stripy+8,mTextPaint);
                    }
                }
                else{
                    canvas.drawRect(oleft,stripy-8,oright,stripy+8,mTextPaint);
                }
//
//                Toast.makeText(getContext(),tmp.toString(), Toast.LENGTH_SHORT).show();
//                canvas.drawText(tmp.getScheme(), x + mItemWidth - mPadding - mCircleRadius, y + mPadding + mSchemeBaseLine, mTextPaint);
            }

        }

        mRectPaint.setStyle(Paint.Style.STROKE);
        mRectPaint.setStrokeWidth(dipToPx(getContext(), 1.3f));
        mRectPaint.setColor(0x44039AC5);
        PorterDuffXfermode mode=new PorterDuffXfermode(PorterDuff.Mode.DARKEN);
        mRectPaint.setXfermode(mode);
        if(calendar.getWeek()==6){
            canvas.drawLine(x+2 , y-2, x+mItemWidth+2 , y-2, mRectPaint);
        }else{
            canvas.drawLine(x+mItemWidth+2, y-2, x +mItemWidth+2, y + mItemHeight-2, mRectPaint);
            canvas.drawLine(x+2 , y-2, x+mItemWidth+2 , y-2, mRectPaint);
        }
    }

    /**
     * dp转px
     *
     * @param context context
     * @param dpValue dp
     * @return px
     */
    private static int dipToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    @Override
    protected void onChangeItemTo(int from, int to) {
    }
}
