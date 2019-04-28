package com.app.pao.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;

import org.xclcharts.chart.DountChart;
import org.xclcharts.chart.PieData;
import org.xclcharts.event.click.ArcPosition;
import org.xclcharts.renderer.XChart;
import org.xclcharts.renderer.XEnum;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MyFriendHeartRateDountChartView extends DemoView {

    /* contains */
    private String TAG = "MyFriendHeartRateDountChartView";
    private DountChart chart = new DountChart();


    LinkedList<PieData> lPieData = new LinkedList<PieData>();

    int weight0_89;
    int weight90_108;
    int weight109_126;
    int weight127_144;
    int weight145_162;
    int weight163_180;


    public MyFriendHeartRateDountChartView(Context context) {
        super(context);

    }

    public MyFriendHeartRateDountChartView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public MyFriendHeartRateDountChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    public void initView(int weight0_89,
                         int weight90_108,
                         int weight109_126,
                         int weight127_144,
                         int weight145_162,
                         int weight163_180) {
        this.weight0_89 = weight0_89;
        this.weight90_108 = weight90_108;
        this.weight109_126 = weight109_126;
        this.weight127_144 = weight127_144;
        this.weight145_162 = weight145_162;
        this.weight163_180 = weight163_180;

        chartDataSet();
        chartRender();
    }

    private void chartDataSet() {
        //设置图表数据源
        //PieData(标签，百分比，在饼图中对应的颜色)
        lPieData.add(new PieData("", "", weight0_89, Color.rgb(193, 193, 193)));
        lPieData.add(new PieData("", "", weight90_108, Color.rgb(255, 229, 105)));
        lPieData.add(new PieData("", "", weight109_126, Color.rgb(255, 162, 0)));
        lPieData.add(new PieData("", "", weight127_144, Color.rgb(240, 116, 34)));
        lPieData.add(new PieData("", "", weight145_162, Color.rgb(238, 84, 49)));
        lPieData.add(new PieData("", "", weight163_180, Color.rgb(222, 33, 33)));
    }

    private void chartRender() {
        try {
            //设置绘图区默认缩进px值
            int[] ltrb = getPieDefaultSpadding();
            chart.setPadding(ltrb[0], ltrb[1], ltrb[2], ltrb[3]);

            //数据源
            chart.setDataSource(lPieData);

            //标题
            chart.disableScale();
            chart.disablePanMode();


            chart.getPlotTitle().setTitleAlign(XEnum.HorizontalAlign.CENTER);
//            chart.setTitle("环形图");
//            chart.addSubtitle("(XCL-Charts Demo)");
            //显示key
//            chart.getPlotLegend().show();
            //显示图例
//            PlotLegend legend = chart.getPlotLegend();
//            legend.show();
//            legend.setType(XEnum.LegendType.ROW);
//            legend.setHorizontalAlign(XEnum.HorizontalAlign.CENTER);
//            legend.setVerticalAlign(XEnum.VerticalAlign.BOTTOM);
//            legend.showBox();
//            legend.getBox().setBorderRectType(XEnum.RectType.RECT);

            //图背景色
//            chart.setApplyBackgroundColor(true);
//            chart.setBackgroundColor(Color.rgb(19, 163, 224));

            //内环背景色
//            chart.getInnerPaint().setColor(Color.rgb(19, 163, 224));

            //显示边框线，并设置其颜色
            chart.getArcBorderPaint().setColor(Color.TRANSPARENT);
            //可用这个修改环所占比例
            chart.setInnerRadius(0.7f);
            chart.setInitialAngle(100.f);

            //保存标签位置
//            chart.saveLabelsPosition(XEnum.LabelSaveType.ALL);

            //激活点击监听
            chart.ActiveListenItemClick();
            chart.showClikedFocus();

        } catch (Exception e) {
        }
    }

    @Override
    public List<XChart> bindChart() {
        List<XChart> lst = new ArrayList<XChart>();
        lst.add(chart);
        return lst;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 图所占范围大小
        chart.setChartRange(w, h);
    }

    @Override
    public void render(Canvas canvas) {
        try {
            chart.render(canvas);

        } catch (Exception e) {
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
  //      Log.v(TAG, "onTouchEvent");
        if (event.getAction() == MotionEvent.ACTION_UP) {
            triggerClick(event.getX(), event.getY());
        }
        return true;
    }

    //触发监听
    private void triggerClick(float x, float y) {
        if (!chart.getListenItemClickStatus()) return;
        ArcPosition record = chart.getPositionRecord(x, y);
        if (null == record) return;
    //    Log.v(TAG,"record id:" + record.getDataID());
//        PieData pData = lPieData.get(record.getDataID());
//
//        boolean isInvaldate = true;
//        for (int i = 0; i < lPieData.size(); i++) {
//            PieData cData = lPieData.get(i);
//            if (i == record.getDataID()) {
//                if (cData.getSelected()) {
//                    isInvaldate = false;
//                    break;
//                } else {
//                    cData.setSelected(true);
//                }
//            } else
//                cData.setSelected(false);
//        }
//
//        if (isInvaldate) this.invalidate();
    }

    public void tvClick(int dataId) {
        PieData pData = lPieData.get(dataId);
        boolean isInvaldate = true;
        for (int i = 0; i < lPieData.size(); i++) {
            PieData cData = lPieData.get(i);
            if (i == dataId) {
                if (cData.getSelected()) {
                    isInvaldate = false;
                    break;
                } else {
                    cData.setSelected(true);
                }
            } else
                cData.setSelected(false);
        }

        if (isInvaldate) this.invalidate();
    }
}
