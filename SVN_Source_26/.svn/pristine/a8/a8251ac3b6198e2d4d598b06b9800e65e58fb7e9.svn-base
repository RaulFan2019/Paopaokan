package com.app.pao.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;

import com.app.pao.utils.DeviceUtils;

import org.xclcharts.chart.DialChart;
import org.xclcharts.view.GraphicalView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raul on 2016/1/17.
 * 配速仪表盘
 */
public class PlayBackDialView extends GraphicalView {

    private static final String TAG = "PlayBackDialView";

    private DialChart chart = new DialChart();
    private float mPercentage = 0.1f;

    public PlayBackDialView(Context context) {
        super(context);
        initView();
    }

    public PlayBackDialView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public PlayBackDialView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView() {
        chartRender();
    }


    public void chartRender() {
        try {
            chart.setPadding(0,0,0,0);
//            //设置标题背景
            chart.setApplyBackgroundColor(true);
            chart.setBackgroundColor(Color.TRANSPARENT);
            //绘制边框
//            chart.showRoundBorder();
            //设置当前百分比
            chart.getPointer().setPercentage(mPercentage);
            //设置指针长度
            chart.getPointer().getPointerPaint().setColor(Color.WHITE);
            chart.getPointer().getBaseCirclePaint().setColor(Color.WHITE);
            chart.getPointer().setBaseRadius(DeviceUtils.dpToPixel(2.4f));
            chart.getPointer().setLength(0.6f);
            //增加轴
            addAxis();
            addPointer();
            //设置附加信息
            addAttrInfo();
        } catch (Exception e) {
        }

    }

    public void addAxis() {

        List<String> rlabels = new ArrayList<String>();
        rlabels.add("10'    ");
        rlabels.add("8'  ");
        rlabels.add("6'");
        rlabels.add("  4'");
        rlabels.add("    2'");

        chart.addOuterTicksAxis(0.7f, rlabels);

        //环形颜色轴
        List<Float> ringPercentage = new ArrayList<Float>();
        ringPercentage.add(0.33f);
        ringPercentage.add(0.33f);
        ringPercentage.add(1 - 2 * 0.33f);

//        List<Integer> rcolor = new ArrayList<Integer>();
//        rcolor.add(Color.rgb(133, 206, 130));
//        rcolor.add(Color.rgb(252, 210, 9));
//        rcolor.add(Color.rgb(229, 63, 56));
//        chart.addStrokeRingAxis(0.8f, 0.75f, ringPercentage, rcolor);


        chart.getPlotAxis().get(0).getTickLabelPaint().setColor(Color.WHITE);
        chart.getPlotAxis().get(0).getTickLabelPaint().setTextSize(DeviceUtils.dpToPixel(7));
        chart.getPlotAxis().get(0).hideAxisLine();
        chart.getPlotAxis().get(0).getTickMarksPaint().setColor(Color.TRANSPARENT);

//        chart.getPlotAxis().get(1).getTickLabelPaint().setColor(Color.WHITE);
//        chart.getPlotAxis().get(1).getTickLabelPaint().setTextSize(DeviceUtils.dpToPixel(7));
//        chart.getPlotAxis().get(1).hideAxisLabels();
//        chart.getPlotAxis().get(1).getFillAxisPaint().setColor(Color.TRANSPARENT);
//        chart.getPlotAxis().get(1).getFillAxisPaint().setColor(Color.rgb(28, 129, 243));
    }


    private void addAttrInfo() {


    }

    public void addPointer() {

    }

    public void setCurrentStatus(float percentage) {
        //清理
        chart.clearAll();

        mPercentage = percentage;
        //设置当前百分比
        chart.getPointer().setPercentage(mPercentage);
        addAxis();
        addPointer();
        addAttrInfo();
    }

    @Override
    public void render(Canvas canvas) {
        try {
            chart.render(canvas);
        } catch (Exception e) {
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        chart.setChartRange(w, h);
    }
}
