package com.app.pao.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.app.pao.entity.model.PieChartEntity;

import org.xclcharts.chart.DountChart;
import org.xclcharts.chart.PieData;
import org.xclcharts.event.click.ArcPosition;
import org.xclcharts.renderer.XChart;
import org.xclcharts.renderer.XEnum;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MyFriendHeartRateDountBigChartView extends DemoView {

    /* contains */
    private String TAG = "MyFriendHeartRateDountBigChartView";
    private DountChart chart = new DountChart();
    private PieListener mPieListener;

    LinkedList<PieData> lPieData = new LinkedList<PieData>();
    List<PieChartEntity> mDatas = new ArrayList<PieChartEntity>();


    public interface PieListener {
        void onRecordClick(int dataId);
    }

    public MyFriendHeartRateDountBigChartView(Context context,PieListener PieListener) {
        super(context);
        this.mPieListener = PieListener;
    }

    public MyFriendHeartRateDountBigChartView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public MyFriendHeartRateDountBigChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    public void initView(List<PieChartEntity> datas) {
        this.mDatas = datas;
        chartDataSet();
        chartRender();
    }


    private void chartDataSet() {
        //设置图表数据源
        //PieData(标签，百分比，在饼图中对应的颜色)
        for (int i = 0; i < mDatas.size(); i++) {
            lPieData.add(new PieData("", "", mDatas.get(i).getPersent(), mDatas.get(i).getColor()));
        }
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


            //显示边框线，并设置其颜色
            chart.getArcBorderPaint().setColor(Color.TRANSPARENT);
            //可用这个修改环所占比例
            chart.setInnerRadius(0.6f);

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
        mPieListener.onRecordClick(record.getDataID());
    }

}
