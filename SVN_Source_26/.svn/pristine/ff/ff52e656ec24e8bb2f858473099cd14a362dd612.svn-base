package com.app.pao.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;

import com.app.pao.entity.model.PlayBackPointEntity;
import com.app.pao.entity.network.GetPlaybackCameraListResult;
import com.app.pao.utils.DeviceUtils;
import com.app.pao.utils.Log;

import org.xclcharts.chart.AreaChart;
import org.xclcharts.chart.AreaData;
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
 * 跑步回放的曲线
 */
public class PlayBackChartView extends DemoView {

    /* contains */
    private static final String TAG = "PlayBackChartView";

    /* local data */
    private SplineChart splineChart = new SplineChart();
    private AreaChart areaChart = new AreaChart();

    private LinkedList<SplineData> mLIneChartData = new LinkedList<SplineData>();//数据列表
    private LinkedList<AreaData> mDataset = new LinkedList<AreaData>();
    private LinkedList<String> mLabels = new LinkedList<String>();

    private long maxTimeOffset;
    private long minTimeOffset;
    private double maxAx = 0;

    /* Constructor */
    public PlayBackChartView(Context context) {
        super(context);
    }

    public PlayBackChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PlayBackChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 图所占范围大小
        splineChart.setChartRange(w, h);
        areaChart.setChartRange(w, h);
    }

    @Override
    public void render(Canvas canvas) {
        try {
            splineChart.render(canvas);
            areaChart.render(canvas);
        } catch (Exception e) {
        }
    }

    @Override
    public List<XChart> bindChart() {
        List<XChart> lst = new ArrayList<XChart>();
        lst.add(splineChart);
        lst.add(areaChart);
        return lst;
    }


    /**
     * 初始化界面
     *
     * @param maxTimeOffset
     */
    public void initViews(long minTimeOffset, long maxTimeOffset, double maxPace) {
        this.maxTimeOffset = maxTimeOffset;
        this.minTimeOffset = minTimeOffset;
        this.maxAx = 50000 / maxPace;
        chartRender();
        splineChart.setDataSource(mLIneChartData);

    }

    /**
     * 增加一条线
     *
     * @param mPlayList
     * @param color
     */
    public void addLine(List<PlayBackPointEntity> mPlayList, int color, boolean draw,
                        List<GetPlaybackCameraListResult.VideoEntity> vieoList) {
        List<PointD> linePoint = new ArrayList<PointD>();
        List<Double> heartratePoint = new LinkedList<Double>();
        int drawStep = (mPlayList.size() / 100) + 1;
        for (int i = 0; i < mPlayList.size(); i += drawStep) {
            PlayBackPointEntity playBackPointEntity = mPlayList.get(i);
            double x = playBackPointEntity.getTimeofset();
            linePoint.add(new PointD(x, playBackPointEntity.getBmp()));
            mLabels.add("");
            if (playBackPointEntity.getPace() == 0) {
                heartratePoint.add(0.0);
                continue;
            }
            double y = 50000 / playBackPointEntity.getPace();
            heartratePoint.add(y);
        }
        SplineData dataSeries = new SplineData("", linePoint, color);
        // 把线弄细点
        dataSeries.getLinePaint().setStrokeWidth(4);
        dataSeries.setDotStyle(XEnum.DotStyle.HIDE);
        dataSeries.setLabelVisible(false);
//        Path path = new Path();
//        path.addCircle(0, 0, 3, Path.Direction.CCW);
//        PathEffect pathEffect = new DashPathEffect(new float[]{20, 10, 20, 10}, 0);
        mLIneChartData.add(dataSeries);
        //画视频线
        for (int i = 0; i < vieoList.size(); i++) {
            List<PointD> tmplinePoint = new ArrayList<PointD>();
            GetPlaybackCameraListResult.VideoEntity vEntity = vieoList.get(i);
            Log.v(TAG,"i:" + vEntity.getStartoffset());
            tmplinePoint.add(new PointD(vEntity.getStartoffset(), maxAx * 0.98));
            tmplinePoint.add(new PointD(vEntity.getEndoffset(), maxAx * 0.98));
            SplineData tmpSeries = new SplineData("", tmplinePoint, color);
            tmpSeries.getLinePaint().setStrokeWidth(4);
            tmpSeries.setDotStyle(XEnum.DotStyle.HIDE);
            tmpSeries.setLabelVisible(false);
            mLIneChartData.add(tmpSeries);
        }

        //区域图
        AreaData line = new AreaData("", heartratePoint,
                Color.parseColor("#717ef3"), Color.parseColor("#25717ef3"), Color.parseColor("#717ef3"));
        //不显示点
        line.setDotStyle(XEnum.DotStyle.HIDE);//隐藏图形
        line.setApplayGradient(true);
        line.setAreaBeginColor(Color.parseColor("#25717ef3"));
        line.setAreaEndColor(Color.parseColor("#717ef3"));
        mDataset.add(line);
        areaChart.setDataSource(mDataset);
        areaChart.setCategories(mLabels);
        if (draw) {
            invalidate();
        }
    }

    public void drawVideoView(boolean visible) {
        int color;
        if(visible){
            color = Color.parseColor("#68ffa1");
        }else{
            color = Color.TRANSPARENT;
        }
        for (int i = 1; i < mLIneChartData.size(); i++) {
            SplineData tmpSeries = mLIneChartData.get(i);
            tmpSeries.setLineColor(color);
            mLIneChartData.set(i,tmpSeries);
        }
        invalidate();
    }

    /**
     * 增加所有
     *
     * @param mPlayList
     * @param color
     */
    public void addAllLine(List<List<PlayBackPointEntity>> mPlayList, int color) {

        for (int i = 0; i < mPlayList.size(); i++) {
            List<PointD> linePoint = new ArrayList<PointD>();
            for (int j = 0; j < mPlayList.get(i).size(); j++) {
                PlayBackPointEntity playBackPointEntity = mPlayList.get(i).get(j);
                double x = playBackPointEntity.getTimeofset();
                double y = playBackPointEntity.getPace();
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
            int[] ltrb = getPlayBackSpadding();
            splineChart.setPadding(ltrb[0], ltrb[1], ltrb[2], ltrb[3]);
            areaChart.setPadding(ltrb[0], ltrb[1], ltrb[2], ltrb[3]);

            // 不显示边框
            splineChart.hideBorder();
            areaChart.hideBorder();
            // 禁止缩放
            splineChart.disableScale();
            areaChart.disableScale();
            // 禁用移动
            splineChart.disablePanMode();
            areaChart.disablePanMode();

            // 坐标系
            // 数据轴最大值
            splineChart.getDataAxis().setAxisMax(maxAx);
            areaChart.getDataAxis().setAxisMax(maxAx);
            splineChart.getDataAxis().setAxisMin(0);
            areaChart.getDataAxis().setAxisMin(0);

            // 数据轴刻度间隔
            splineChart.getDataAxis().setAxisSteps((maxAx - 0) / 2);
            areaChart.getDataAxis().setAxisSteps((maxAx - 0) / 2);

            // 标签轴最大值
            splineChart.setCategoryAxisMax(maxTimeOffset);
            // 标签轴最小值
            splineChart.setCategoryAxisMin(minTimeOffset);

            // 设置图的背景色
            splineChart.setApplyBackgroundColor(false);
            //透明度
            areaChart.setAreaAlpha(180);

            // 调轴线与网络线风格

            splineChart.getCategoryAxis().hideTickMarks();
            splineChart.getDataAxis().hideAxisLine();
            splineChart.getDataAxis().hideTickMarks();
            splineChart.getPlotGrid().showHorizontalLines();

            areaChart.getCategoryAxis().hideTickMarks();
            areaChart.getDataAxis().hideAxisLine();
            areaChart.getDataAxis().hideTickMarks();
            areaChart.getPlotGrid().showHorizontalLines();
            // chart.hideTopAxis();
            // chart.hideRightAxis();

            splineChart.getPlotGrid().getHorizontalLinePaint().setColor(Color.parseColor("#888888"));
            splineChart.getPlotGrid().getHorizontalLinePaint().setAlpha(30);
            splineChart.getPlotGrid().getHorizontalLinePaint().setTextSize(DeviceUtils.dpToPixel(12));
            splineChart.getCategoryAxis().getAxisPaint().setColor(splineChart.getPlotGrid().getHorizontalLinePaint()
                    .getColor());
            splineChart.getCategoryAxis().getAxisPaint()
                    .setStrokeWidth(splineChart.getPlotGrid().getHorizontalLinePaint().getStrokeWidth());

            areaChart.getPlotGrid().getHorizontalLinePaint().setColor(Color.parseColor("#888888"));
            areaChart.getPlotGrid().getHorizontalLinePaint().setAlpha(30);
            areaChart.getPlotGrid().getHorizontalLinePaint().setTextSize(DeviceUtils.dpToPixel(12));
            areaChart.getCategoryAxis().getAxisPaint().setColor(splineChart.getPlotGrid().getHorizontalLinePaint()
                    .getColor());
            areaChart.getCategoryAxis().getAxisPaint()
                    .setStrokeWidth(splineChart.getPlotGrid().getHorizontalLinePaint().getStrokeWidth());

            // 激活点击监听
            splineChart.ActiveListenItemClick();
            // 为了让触发更灵敏，可以扩大5px的点击监听范围
            splineChart.extPointClickRange(5);
            splineChart.showClikedFocus();

            // 显示平滑曲线
            splineChart.setCrurveLineStyle(XEnum.CrurveLineStyle.BEZIERCURVE);
            areaChart.setCrurveLineStyle(XEnum.CrurveLineStyle.BEZIERCURVE);

            splineChart.getDataAxis().setLabelFormatter(new IFormatterTextCallBack() {
                @Override
                public String textFormatter(String s) {
                    return "";
//                    return Math.floor(Float.parseFloat(s)) + "";
                }
            });

            areaChart.getDataAxis().setLabelFormatter(new IFormatterTextCallBack() {
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
