package com.app.pao.ui.widget.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;

import com.app.pao.entity.db.DBEntityHeartrate;
import com.app.pao.entity.model.HistoryHeartrateEntity;
import com.app.pao.ui.widget.DemoView;
import com.app.pao.utils.DeviceUtils;

import org.xclcharts.chart.PointD;
import org.xclcharts.chart.SplineChart;
import org.xclcharts.chart.SplineData;
import org.xclcharts.common.IFormatterTextCallBack;
import org.xclcharts.renderer.XChart;
import org.xclcharts.renderer.XEnum;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Raul on 2015/12/22.
 * 好友历史的曲线
 */
public class FriendHeartRateChartViewReplace extends DemoView {

    /* contains */
    private static final String TAG = "HeartRateChartViewReplace";

    /* local data */
    private SplineChart splineChart = new SplineChart();
    private LinkedList<SplineData> mLIneChartData = new LinkedList<SplineData>();//数据列表
    private long maxTimeOffset;
    private int minBmp;
    private int maxBmp;

    /* Constructor */
    public FriendHeartRateChartViewReplace(Context context) {
        super(context);
    }

    public FriendHeartRateChartViewReplace(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FriendHeartRateChartViewReplace(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 图所占范围大小
        splineChart.setChartRange(w, h);
    }

    @Override
    public void render(Canvas canvas) {
        try {
            splineChart.render(canvas);

        } catch (Exception e) {
        }
    }

    @Override
    public List<XChart> bindChart() {
        List<XChart> lst = new ArrayList<XChart>();
        lst.add(splineChart);
        return lst;
    }


    /**
     * 初始化界面
     *
     * @param maxTimeOffset
     */
    public void initViews(long maxTimeOffset, int minHeartrate, int maxHeartrate) {
        this.maxTimeOffset = maxTimeOffset;
        minBmp = minHeartrate;
        maxBmp = maxHeartrate;
        chartRender();
        splineChart.setDataSource(mLIneChartData);
    }

    /**
     * 增加一条线
     *
     * @param mHeartrateList
     * @param color
     */
    public void addLine(List<HistoryHeartrateEntity> mHeartrateList, int color , boolean draw) {
        List<PointD> linePoint = new ArrayList<PointD>();
        for (int i = 0; i < mHeartrateList.size(); i++) {
            HistoryHeartrateEntity heartrate = mHeartrateList.get(i);
            double x = heartrate.getTimeofset();
            double y = heartrate.getBmp();
            linePoint.add(new PointD(x, y));
        }
        SplineData dataSeries = new SplineData("", linePoint, color);
        // 把线弄细点
        dataSeries.getLinePaint().setStrokeWidth(8);
        dataSeries.setDotStyle(XEnum.DotStyle.HIDE);
        dataSeries.setLabelVisible(false);

        Path path = new Path();
        path.addCircle(0, 0, 3, Path.Direction.CCW);
        PathEffect pathEffect = new DashPathEffect(new float[]{20, 10, 20, 10}, 0);
        mLIneChartData.add(dataSeries);
        if(draw){
            invalidate();
        }
    }

    /**
     * 增加所有
     *
     * @param mHeartrateList
     * @param color
     */
    public void addAllLine(List<List<HistoryHeartrateEntity>> mHeartrateList, int color) {

        for (int i = 0; i < mHeartrateList.size(); i++) {
            List<PointD> linePoint = new ArrayList<PointD>();
            for (int j = 0; j < mHeartrateList.get(i).size(); j++) {
                HistoryHeartrateEntity heartrate = mHeartrateList.get(i).get(j);
                double x = heartrate.getTimeofset();
                double y = heartrate.getBmp();
                linePoint.add(new PointD(x, y));
            }
            SplineData dataSeries = new SplineData("", linePoint, color);
            // 把线弄细点
            dataSeries.getLinePaint().setStrokeWidth(8);
            dataSeries.setDotStyle(XEnum.DotStyle.HIDE);
            dataSeries.setLabelVisible(false);

            Path path = new Path();
            path.addCircle(0, 0, 3, Path.Direction.CCW);
            PathEffect pathEffect = new DashPathEffect(new float[]{20, 10, 20, 10}, 0);
            mLIneChartData.add(dataSeries);
        }
        invalidate();
    }

    private void chartRender() {
        try {
            // 设置绘图区默认缩进px值,留置空间显示Axis,Axistitle....
            int[] ltrb = getBarLnDefaultSpadding();
            splineChart.setPadding(ltrb[0], ltrb[1], ltrb[2], ltrb[3]);

            // 不显示边框
            splineChart.hideBorder();
            // 禁止缩放
            splineChart.disableScale();
            // 禁用移动
            splineChart.disablePanMode();


            // 坐标系
            // 数据轴最大值
            splineChart.getDataAxis().setAxisMax(maxBmp);
            splineChart.getDataAxis().setAxisMin(minBmp);

            // 数据轴刻度间隔
            splineChart.getDataAxis().setAxisSteps((maxBmp - minBmp) / 2);

            // 标签轴最大值
            splineChart.setCategoryAxisMax(maxTimeOffset);
            // 标签轴最小值
            splineChart.setCategoryAxisMin(0);

            // 设置图的背景色
            splineChart.setApplyBackgroundColor(false);

            // 调轴线与网络线风格

            splineChart.getCategoryAxis().hideTickMarks();
            splineChart.getDataAxis().hideAxisLine();
            splineChart.getDataAxis().hideTickMarks();
            splineChart.getPlotGrid().showHorizontalLines();
            // chart.hideTopAxis();
            // chart.hideRightAxis();

            splineChart.getPlotGrid().getHorizontalLinePaint().setColor(Color.parseColor("#888888"));
            splineChart.getPlotGrid().getHorizontalLinePaint().setAlpha(30);
            splineChart.getPlotGrid().getHorizontalLinePaint().setTextSize(DeviceUtils.dpToPixel(12));
            splineChart.getCategoryAxis().getAxisPaint().setColor(splineChart.getPlotGrid().getHorizontalLinePaint()
                    .getColor());
            splineChart.getCategoryAxis().getAxisPaint()
                    .setStrokeWidth(splineChart.getPlotGrid().getHorizontalLinePaint().getStrokeWidth());

            // 激活点击监听
            splineChart.ActiveListenItemClick();
            // 为了让触发更灵敏，可以扩大5px的点击监听范围
            splineChart.extPointClickRange(5);
            splineChart.showClikedFocus();

            // 显示平滑曲线
            splineChart.setCrurveLineStyle(XEnum.CrurveLineStyle.BEZIERCURVE);

            splineChart.getDataAxis().setLabelFormatter(new IFormatterTextCallBack() {
                @Override
                public String textFormatter(String s) {
                    return "";
//                    return Math.floor(Float.parseFloat(s)) + "";
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
