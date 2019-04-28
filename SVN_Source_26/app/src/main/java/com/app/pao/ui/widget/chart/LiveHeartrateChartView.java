package com.app.pao.ui.widget.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;

import com.app.pao.entity.db.DBEntityHeartrate;
import com.app.pao.entity.model.LiveHeartrateEntity;
import com.app.pao.ui.widget.DemoView;
import com.app.pao.utils.DeviceUtils;

import org.xclcharts.chart.AreaChart;
import org.xclcharts.chart.AreaData;
import org.xclcharts.chart.PointD;
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
public class LiveHeartrateChartView extends DemoView {

    /* contains */
    private static final String TAG = "HistoryPaceChartView";

    /* local data */
    private AreaChart areaChart = new AreaChart();

    private LinkedList<SplineData> mLIneChartData = new LinkedList<SplineData>();//数据列表
    private LinkedList<AreaData> mDataset = new LinkedList<AreaData>();
    private LinkedList<String> mLabels = new LinkedList<String>();

    private long maxTimeOffset;
    private long minTimeOffset;
    private double maxAx = 0;

    /* Constructor */
    public LiveHeartrateChartView(Context context) {
        super(context);
    }

    public LiveHeartrateChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LiveHeartrateChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 图所占范围大小
        areaChart.setChartRange(w, h);
    }

    @Override
    public void render(Canvas canvas) {
        try {
            areaChart.render(canvas);
        } catch (Exception e) {
        }
    }

    @Override
    public List<XChart> bindChart() {
        List<XChart> lst = new ArrayList<XChart>();
        lst.add(areaChart);
        return lst;
    }


    /**
     * 初始化界面
     *
     * @param maxTimeOffset
     */
    public void initViews(long minTimeOffset, long maxTimeOffset) {
        this.maxTimeOffset = maxTimeOffset;
        this.minTimeOffset = minTimeOffset;
        chartRender();

    }

    /**
     * 增加一条线
     *
     * @param mPlayList
     * @param color
     */
    public void addLine(List<LiveHeartrateEntity> mPlayList, int color, boolean draw) {
        List<PointD> linePoint = new ArrayList<PointD>();
        List<Double> heartratePoint = new LinkedList<Double>();
        int drawStep = (mPlayList.size() / 50) + 1;
        for (int i = 0; i < mPlayList.size(); i += drawStep) {
            LiveHeartrateEntity entityHeartrate = mPlayList.get(i);
            double x = i;
            linePoint.add(new PointD(x, 0));
            mLabels.add("");
            if (entityHeartrate.bpm == 0) {
                heartratePoint.add(0.0);
                continue;
            }
            if (maxAx < entityHeartrate.bpm) {
                maxAx = entityHeartrate.bpm;
            }
            double y = entityHeartrate.bpm;
            heartratePoint.add(y);
        }
        areaChart.getDataAxis().setAxisMax(maxAx * 1.5);
        SplineData dataSeries = new SplineData("", linePoint, color);
        // 把线弄细点
        dataSeries.getLinePaint().setStrokeWidth(1f);
        dataSeries.setDotStyle(XEnum.DotStyle.HIDE);
        dataSeries.setLabelVisible(false);
        mLIneChartData.add(dataSeries);


        //区域图
        AreaData line = new AreaData("", heartratePoint,
                Color.parseColor("#28caa6"), Color.parseColor("#8028caa6"), Color.parseColor("#0028caa6"));
        //不显示点
        line.setDotStyle(XEnum.DotStyle.HIDE);//隐藏图形
        line.setApplayGradient(true);
//        line.setAreaBeginColor(Color.parseColor("#f06522"));
//        line.setAreaEndColor(Color.parseColor("#10f06522"));
        line.getLinePaint().setStrokeWidth(1f);
        mDataset.add(line);
        areaChart.setDataSource(mDataset);
        areaChart.setCategories(mLabels);
        if (draw) {
            invalidate();
        }
    }

    private void chartRender() {
        try {
            // 设置绘图区默认缩进px值,留置空间显示Axis,Axistitle....
            int[] ltrb = getHistorySpadding();
            areaChart.setPadding(ltrb[0], ltrb[1], ltrb[2], ltrb[3]);

            // 不显示边框
            areaChart.hideBorder();
            // 禁止缩放
            areaChart.disableScale();
            // 禁用移动
            areaChart.disablePanMode();

            // 坐标系
            // 数据轴最大值
            areaChart.getDataAxis().setAxisMin(0);

            //透明度
            areaChart.setAreaAlpha(180);

            // 调轴线与网络线风格
            areaChart.getCategoryAxis().hide();
            areaChart.getDataAxis().hide();
            areaChart.getPlotGrid().hideHorizontalLines();
            // chart.hideTopAxis();
            // chart.hideRightAxis();

            areaChart.getPlotGrid().getHorizontalLinePaint().setColor(Color.parseColor("#00888888"));
            areaChart.getPlotGrid().getHorizontalLinePaint().setAlpha(30);
            areaChart.getPlotGrid().getHorizontalLinePaint().setTextSize(DeviceUtils.dpToPixel(12));

            // 显示平滑曲线
            areaChart.setCrurveLineStyle(XEnum.CrurveLineStyle.BEZIERCURVE);


            areaChart.getDataAxis().setLabelFormatter(new IFormatterTextCallBack() {
                @Override
                public String textFormatter(String s) {
                    return "";
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
