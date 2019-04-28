package com.app.pao.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.PathEffect;
import android.util.AttributeSet;

import com.app.pao.LocalApplication;
import com.app.pao.entity.model.HistoryHeartrateEntity;

import org.xclcharts.chart.AreaChart;
import org.xclcharts.chart.AreaData;
import org.xclcharts.chart.CustomLineData;
import org.xclcharts.chart.PointD;
import org.xclcharts.chart.SplineChart;
import org.xclcharts.chart.SplineData;
import org.xclcharts.common.IFormatterTextCallBack;
import org.xclcharts.renderer.XChart;
import org.xclcharts.renderer.XEnum;
import org.xclcharts.renderer.XEnum.DotStyle;
import org.xclcharts.renderer.info.AnchorDataPoint;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MyFriendHeartRateChartView extends DemoView {

    private String TAG = "MyHeartRateLineChartView";
    private SplineChart splineChart = new SplineChart();
    private AreaChart areaChart = new AreaChart();

    // 分类轴标签集合
    private LinkedList<String> labels = new LinkedList<String>();
    private LinkedList<SplineData> mLIneChartData = new LinkedList<SplineData>();

    // 批注
    List<AnchorDataPoint> mAnchorSet = new ArrayList<AnchorDataPoint>();
    AnchorDataPoint maxAnPoint = null;

    // splinechart支持横向和竖向定制线
    // private List<CustomLineData> mXLIneCustomLineDataset = new
    // ArrayList<CustomLineData>();
    // private List<CustomLineData> mYLIneCustomLineDataset = new
    // ArrayList<CustomLineData>();

    // area chart 数据集合
    private LinkedList<AreaData> mAreaDataset = new LinkedList<AreaData>();
    private List<CustomLineData> mAreaCustomLineDataset = new LinkedList<CustomLineData>();

    private List<HistoryHeartrateEntity> mHeartrateList;
    private int maxHeartrate;// 最大心率
    private int avgHeartrate;// 平均心率
    private int mHeartrateStep;

    public MyFriendHeartRateChartView(Context context) {
        super(context);
    }

    public MyFriendHeartRateChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyFriendHeartRateChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void initView(List<HistoryHeartrateEntity> mheartrateList, int avgHeartrate, int age) {
        this.mHeartrateList = mheartrateList;
        this.avgHeartrate = avgHeartrate;
        this.maxHeartrate = (220 - age);

        mHeartrateStep = 1;

        if (mHeartrateList.size() > 250) {
            mHeartrateStep = mHeartrateList.size() / 250;
        }
        chartLabels();
        chartLineDataSet();
        chartCustomeLines();
        chartLineRender();
        chartAreaDataSet();
        chartAreaRender();
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
     * 期望线/分界线
     */
    private void chartCustomeLines() {
        // CustomLineData cdx1 = new CustomLineData("", (double) maxHeartrate,
        // Color.BLACK, 6);
        // cdx1.setLineStyle(LineStyle.DOT);
        // mXLIneCustomLineDataset.add(cdx1);
    }

    private void chartLineRender() {
        try {
            // 设置绘图区默认缩进px值,留置空间显示Axis,Axistitle....
            int[] ltrb = getBarLnDefaultSpadding();
            splineChart.setPadding(ltrb[0], ltrb[1], ltrb[2], ltrb[3]);
            // 数据源
            splineChart.setCategories(labels);
            splineChart.setDataSource(mLIneChartData);

            // 坐标系
            // 数据轴最大值
            splineChart.getDataAxis().setAxisMax(220);
            splineChart.getDataAxis().setAxisMin(0);

            // 数据轴刻度间隔
            splineChart.getDataAxis().setAxisSteps(100);
            // splineChart.setCustomLines(mXLIneCustomLineDataset); // y轴
//             splineChart.setCategoryAxisCustomLines(mXLIneCustomLineDataset);
            // 禁止缩放
            splineChart.disableScale();
            // 禁用移动
            splineChart.disablePanMode();

            // 标签轴最大值
            if (mHeartrateList.size() > 0) {
                splineChart.setCategoryAxisMax(mHeartrateList.get(mHeartrateList.size() - 1).getTimeofset());
            } else {
                splineChart.setCategoryAxisMax(1);
            }

            // 标签轴最小值
            splineChart.setCategoryAxisMin(mHeartrateList.get(0).getTimeofset());

            // 设置图的背景色
            splineChart.setBackgroundColor(Color.TRANSPARENT);

            // 调轴线与网络线风格
            splineChart.getCategoryAxis().hideTickMarks();
            splineChart.getDataAxis().hideAxisLine();
            splineChart.getDataAxis().hideTickMarks();
            splineChart.getCategoryAxis().getAxisPaint()
                    .setColor(splineChart.getPlotGrid().getHorizontalLinePaint().getColor());
            splineChart.getCategoryAxis().getAxisPaint()
                    .setStrokeWidth(splineChart.getPlotGrid().getHorizontalLinePaint().getStrokeWidth());

            // 激活点击监听
            splineChart.ActiveListenItemClick();
            // 为了让触发更灵敏，可以扩大5px的点击监听范围
            splineChart.extPointClickRange(5);
            splineChart.showClikedFocus();

            // 激活参考线
            splineChart.showDyLine();
            splineChart.getDyLine().setDyLineStyle(XEnum.DyLineStyle.Vertical);

            // splineChart.disablePanMode();
            // 显示平滑曲线
            splineChart.setCrurveLineStyle(XEnum.CrurveLineStyle.BEZIERCURVE);

        } catch (Exception e) {
            e.printStackTrace();
            // Log.e(TAG, e.toString());
        }
    }

    private void chartLineDataSet() {
        // 线1的数据集
        List<PointD> linePoint1 = new ArrayList<PointD>();
        for (int i = 0; i < mHeartrateList.size(); i += mHeartrateStep) {
            HistoryHeartrateEntity heartrate = mHeartrateList.get(i);
            double x = heartrate.getTimeofset();
            double y = heartrate.getBmp();
            linePoint1.add(new PointD(x, y));
        }
        // Log.v(TAG, "mHeartrateList.size() :" + mHeartrateList.size());
        if (mHeartrateList.size() != 0) {
            HistoryHeartrateEntity firstHeartrate = mHeartrateList.get(0);
            HistoryHeartrateEntity lastHeartrate = mHeartrateList.get(mHeartrateList.size() - 1);

            SplineData dataSeries1 = new SplineData("", linePoint1, Color.parseColor("#e56522"));
            // 把线弄细点
            dataSeries1.getLinePaint().setStrokeWidth(8);
            dataSeries1.setDotStyle(DotStyle.HIDE);
            dataSeries1.setLabelVisible(false);

            Path path = new Path();
            path.addCircle(0, 0, 3, Direction.CCW);
            PathEffect pathEffect = new DashPathEffect(new float[] { 20, 10, 20, 10 }, 0);

            // 设定数据源
            mLIneChartData.add(dataSeries1);
        }
    }

    private void chartAreaRender() {
        try {
            // 设置绘图区默认缩进px值,留置空间显示Axis,Axistitle....
            int[] ltrb = getBarLnDefaultSpadding();
            areaChart.setPadding(ltrb[0], ltrb[1], ltrb[2], ltrb[3]);

            // 轴数据源
            // 标签轴
            areaChart.setCategories(labels);

            // 数据轴
//            areaChart.setDataSource(mAreaDataset);
            // 仅横向平移
            // chart.setPlotPanMode(XEnum.PanMode.HORIZONTAL);

            // 数据轴范围
            areaChart.getDataAxis().setAxisMax(220);
            areaChart.getDataAxis().setAxisMin(40);
            // 数据轴刻度间隔
            areaChart.getDataAxis().setAxisSteps(80);
            // 禁止缩放
            areaChart.disableScale();
            // 禁止移动
            areaChart.disablePanMode();
            // 网格
            areaChart.getPlotGrid().getHorizontalLinePaint().setColor(Color.GRAY);
            areaChart.getPlotGrid().getHorizontalLinePaint().setAlpha(30);
            areaChart.getPlotGrid().showHorizontalLines();

            // 把轴线和刻度线给隐藏起来
            areaChart.getDataAxis().hideAxisLine();
            areaChart.getDataAxis().hideTickMarks();
            areaChart.getCategoryAxis().hideAxisLine();
            areaChart.getCategoryAxis().hideTickMarks();

            // 透明度
            areaChart.setAreaAlpha(180);
            // 扩大显示宽度
            // chart.getPlotArea().extWidth(100f);

            // areaChart.disablePanMode(); // test

            // 定义数据轴标签显示格式
            areaChart.getDataAxis().setLabelFormatter(new IFormatterTextCallBack() {

                @Override
                public String textFormatter(String value) {
                    return value;
                }

            });
            areaChart.getCategoryAxis().setLabelFormatter(new IFormatterTextCallBack() {

                @Override
                public String textFormatter(String arg0) {
                    return arg0;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void chartAreaDataSet() {
        // 将标签与对应的数据集分别绑定
        // 标签对应的数据集

//        List<Double> dataSeries1 = new LinkedList<Double>();
//        dataSeries1.add((double) avgHeartrate);
//        dataSeries1.add((double) avgHeartrate);
        // 设置每条线各自的显示属性
        // key,数据集,线颜色,区域颜色
//        AreaData line1 = new AreaData("", dataSeries1, Color.parseColor("#e0ededed"), Color.parseColor("#e0ededed"),
//                Color.parseColor("#e0ededed"));
//        line1.setDotStyle(XEnum.DotStyle.HIDE);// 隐藏图形
//        line1.setLabelVisible(false);
//        mAreaDataset.add(line1);
    }

    private void chartLabels() {
        labels.add("");
        labels.add("");
    }

}
