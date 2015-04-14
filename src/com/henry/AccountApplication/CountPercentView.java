package com.henry.AccountApplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by love on 2015/4/8.
 */
public class CountPercentView extends View {

    private double[] arrPercent;

    public void setArrPercent(double[] arrPercent) {
        this.arrPercent = arrPercent;
    }

    public CountPercentView(Context context) {
        this(context, null);
    }

    public CountPercentView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CountPercentView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init(context, attrs);
    }

    /**
     * 画笔
     */
    private Paint paint;
    /**
     * 画弧度
     */
    private RectF arcRect;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float currentDegree = 0;
        float needDegree = 0;
        for (int i = 0; i < arrPercent.length; i++) {
            currentDegree += (float) (360 * arrPercent[i]);
            needDegree = (float) (360 * arrPercent[i]);
            switch (i) {
                case 0:
                    //android手机
                    paint.setColor(Color.GREEN);
                    canvas.drawArc(arcRect, currentDegree - needDegree, needDegree, true, paint);
                    break;
                case 1:
                    //iOS手机
                    paint.setColor(Color.RED);
                    canvas.drawArc(arcRect, currentDegree - needDegree, needDegree, true, paint);
                    break;
                case 2:
                    //黑莓手机
                    paint.setColor(Color.YELLOW);
                    canvas.drawArc(arcRect, currentDegree - needDegree, needDegree, true, paint);
                    break;
                case 3:
                    //other手机
                    paint.setColor(Color.BLUE);
                    canvas.drawArc(arcRect, currentDegree - needDegree, needDegree, true, paint);
                    break;
            }
        }

    }

    public void init(Context context, AttributeSet attrs) {

        paint = new Paint();
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        arcRect = new RectF(width/4, 0, width*3/4, width/4);
        arrPercent = new double[20];
        invalidate();
    }
}
