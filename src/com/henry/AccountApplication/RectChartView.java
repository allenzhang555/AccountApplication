package com.henry.AccountApplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import java.util.HashMap;

/**
 * Created by love on 2015/4/10.
 */


public class RectChartView extends View {
    public RectChartView(Context context) {
        this(context, null);
    }

    public RectChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 作为最后一个参数
     */
    private TextPaint textPaint;

    private Paint linePaint;
    /**
     * 虚线的绘制样式
     */
    private Paint dashPaint;
    /**
     * x和y轴的标题
     */
    private String[] yTitles;
    private String[] xTitles;
    /**
     * 二维数组，第一维是0，去年1代表今年。第二维就是7个数据
     */
    private int[][] data;

    private Paint rectPaint;

    public RectChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    /**
     * 初始化数据
     *
     * @param context
     * @param attrs
     */
    private void init(Context context, AttributeSet attrs) {
        textPaint = new TextPaint();
        textPaint.setColor(Color.BLACK);
        //设置对其的位置（左右对其）,实际上就是从哪里开始画文本
        textPaint.setTextAlign(TextPaint.Align.RIGHT);
        textPaint.setTextAlign(Paint.Align.RIGHT);
        linePaint = new Paint();
        linePaint.setStrokeWidth(1);
        yTitles = new String[]{"80000", "60000", "40000", "20000", "0"};
        xTitles = new String[]{"1", "2", "3", "4", "5", "6", "7"};
        dashPaint = new Paint();
        dashPaint.setColor(Color.BLACK);
        //设置画出的线段为虚线
        // 因为数组代表的是虚线的长度信息，至少包含一个实现的宽度和一个空格的宽度
        //两个参数1：float数组代表虚线的样式，数组的个数是2的倍数长度必须》=2；
        //第二个参数代表从哪一个宽度开始绘制
        DashPathEffect effect = new DashPathEffect(new float[]{5, 2}, 0);
        dashPaint.setPathEffect(effect);

        //初始话数据
        data = new int[][]{
                {22000, 61000, 16000, 22000, 10000, 2000, 38650},//去年
                {25000, 71000, 26000, 32000, 50000, 8000, 58650}//今年
        };
        //设置矩形发热样式
        rectPaint = new Paint();
        //设置是否填充操作,设置的颜色进行填充
        rectPaint.setStyle(Paint.Style.FILL);
        //代表只画边框
//        rectPaint.setStyle(Paint.Style.STROKE);
        //即填充又绘制边框
//        rectPaint.setStyle(Paint.Style.FILL_AND_STROKE);


    }


    ///////////////////////////////////////

    //动态的设置数据

    /**
     * @param data
     */
    public void setData(int[][] data) {
        this.data = data;
        if (data != null) {
            //所有的控件都有的方法，进行刷新操作
//            invalidate();//主线程
            postInvalidate();//任何线程都可以
        }
    }
    //////////////////////////////////////////

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //todo 实现柱状图
        //1.绘制Y轴的标题drawText(String,x,y,paint)
        //2.获取设定的文字的行高getTextSize()单位是像素
        float textSize = textPaint.getTextSize();
        //3.获得文字的像素宽度getTextWidths(String,float[]ws);返回值不是宽度，而是字符个数
        //会检查字符串中每一个字符，会把字符的宽度放到float数组对应的位置中，因此ws的个数应该与text的字符个数相同，中文同样适用

        float[] widths = new float[5];
        float textWidth = textPaint.getTextWidths("80000", widths);
        float sum = 0;
        for (int i = 0; i < widths.length; i++) {
            sum += widths[i];
        }
        widths = null;//频繁执行时gc不管用
//        canvas.drawText("8000",sum,20,textPaint);
//        canvas.drawText("6000",sum,20+textSize,textPaint);
//        canvas.drawText("4000",sum,20+textSize*2,textPaint);
//        canvas.drawText("2000",sum,20+textSize*3,textPaint);
//        canvas.drawText("0",sum,20+textSize*4,textPaint);

        int bottomSpacing = 10;
        int yHeight = (int) (getHeight() - textSize - bottomSpacing);
        //测量字符的各项高度
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        //绘制Y轴
        canvas.drawLine(sum + 20, 0, sum + 20, yHeight, linePaint);
        //绘制x轴
        canvas.drawLine(sum + 20, yHeight, getWidth(), yHeight, linePaint);
        //有了y轴的高度，就能够计算出，y轴标题的位置
        int ySpacing = (yHeight - 20) / (yTitles.length - 1);//-1:0不参与平分高度
        for (int i = 0; i < yTitles.length; i++) {
            String title = yTitles[i];
            canvas.drawText(title, sum, 20 + (ySpacing * i) + fontMetrics.descent, textPaint);
            if (i < yTitles.length - 1) {
                //绘制文本之后，绘制水平线段
                canvas.drawLine(sum + 20, 20 + (ySpacing * i), getWidth(), 20 + (ySpacing * i), dashPaint);
            }
        }

        //绘制x轴的标题
        int xWidth = (int) (getWidth() - (sum + 20));
        int xSpacing = xWidth / (xTitles.length + 1);//x轴实际上平分了8分


//        float leading = fontMetrics.leading;//1与2间的距离
//        float top = fontMetrics.top;
//        float ascent = fontMetrics.ascent;//3与2的距离
        float descent = fontMetrics.descent;//4与3间的距离
//        float bottom = fontMetrics.bottom;
        //重用textPaint
        textPaint.setTextAlign(Paint.Align.CENTER);
        for (int i = 0; i < xTitles.length; i++) {
            String title = xTitles[i];
            float x = sum + 20 + xSpacing * (i + 1);
            float y = getHeight() - descent;
            canvas.drawText(title, x, y, textPaint);
        }

        //绘制虚线

        //绘制矩形的数据

        if (data != null) {


            //今年
            int[] thisYear = data[1];
            if (thisYear != null) {


                //设置今年的颜色10.76.ce，代码设置的颜色必须是8个数字必须包含透明度
//        rectPaint.setColor(0xff1076ce);
                rectPaint.setColor(Color.rgb(0x10, 0x76, 0xce));
                for (int i = 0; i < thisYear.length; i++) {
                    float x = sum + 20 + xSpacing * (i + 1);
                    //矩形计算top位置
                    int value = thisYear[i];
                    //实际矩形的高度
                    int rh = yHeight * value / 80000;
                    //top就是y轴的坐标
                    int top = yHeight - rh;
                    top = (int) (top + fontMetrics.descent);
                    int bottom = yHeight;
                    //left:x-10
                    //right:x+10
                    canvas.drawRect(x - 10, top, x + 10, bottom, rectPaint);//左上右下
                }

            }
            //绘制矩形的数据
            //去年
            int[] lastYear = data[0];
            if (lastYear != null) {


                //设置今年的颜色10.76.ce，代码设置的颜色必须是8个数字必须包含透明度
//        rectPaint.setColor(0xff1076ce);
                rectPaint.setColor(Color.rgb(0xff, 0x00, 0x00));
                for (int i = 0; i < lastYear.length; i++) {
                    float x = sum + 20 + xSpacing * (i + 1);
                    //矩形计算top位置
                    int value = lastYear[i];
                    //实际矩形的高度
                    int rh = yHeight * value / 80000;
                    //top就是y轴的坐标
                    int top = yHeight - rh;
                    top = (int) (top + fontMetrics.descent);
                    int bottom = yHeight;
                    //left:x-10
                    //right:x+10
                    canvas.drawRect(x - 10, top, x + 10, bottom, rectPaint);//左上右下
                }
            }
        }
    }
}
