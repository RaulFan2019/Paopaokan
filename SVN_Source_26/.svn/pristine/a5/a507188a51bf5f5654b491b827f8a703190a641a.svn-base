package com.app.pao.utils;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by Raul.Fan on 2016/3/6.
 */
public class SmoothUtils {

    static double[] Num = new double[]{
            0.000177626186556307,
            -0.000292321692208296,
            0.000184219758154182,
            0.000184219758154182,
            -0.000292321692208296,
            0.000177626186556307
    };
    static double[] Den = new double[]{
            1,
            -4.42555540567879,
            7.86384955099945,
            -7.01074593810779,
            3.13497400641412,
            -0.562383165121981};


    /**
     * 处理平滑的运算
     *
     * @param originalList 原始列表
     * @param smoothList   处理过的列表
     * @param newLocation  新增的点
     */
    public static void Smooth(ArrayList<LatLng> originalList,
                              ArrayList<LatLng> smoothList, LatLng newLocation) {
        //若点的列表小于6
        if (originalList.size() < 7) {
            originalList.add(newLocation);
            smoothList.add(newLocation);
            return;
        }
        //列表向右平移
        originalList.remove(0);
        originalList.add(newLocation);
        smoothList.remove(0);
        smoothList.add(newLocation);
        //获取等待计算的列表
        ArrayList<Double> originaLatitudeList = new ArrayList<Double>();
        ArrayList<Double> originaLongitudeList = new ArrayList<Double>();
        ArrayList<Double> smoothLatitudeList = new ArrayList<Double>();
        ArrayList<Double> smoothLongitudeList = new ArrayList<Double>();
        for (int i = 5; i > -1; i--) {
            originaLatitudeList.add(originalList.get(i).latitude);
            originaLongitudeList.add(originalList.get(i).longitude);
            smoothLatitudeList.add(smoothList.get(i).latitude);
            smoothLongitudeList.add(smoothList.get(i).longitude);
        }
        //获取新纬度
        double Latitude = (MultiplicationSumCalculation(originaLatitudeList, Num, 0)
                - MultiplicationSumCalculation(smoothLatitudeList, Den, 1)) / Den[0];

        double Longitude = (MultiplicationSumCalculation(originaLongitudeList, Num, 0)
                - MultiplicationSumCalculation(smoothLongitudeList, Den, 1)) / Den[0];

        smoothList.set(5, new LatLng(Latitude, Longitude));
    }

    /**
     * 点乘运算
     */
    public static double MultiplicationSumCalculation(ArrayList<Double> Num1, double[] Num2, int start) {
        double sum = 0;
        for (int i = start; i < Num2.length; i++) {
            sum += Num1.get(i) * Num2[i];
        }
        return sum;
    }
}
