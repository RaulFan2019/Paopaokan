package com.app.pao.utils;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.app.pao.LocalApplication;
import com.app.pao.entity.db.DBUserEntity;

/**
 * 卡路里计算工具
 *
 * @author Raul
 */
public class CalorieUtils {

    private static final String TAG = "CalorieUtils";

    /**
     * 根据海拔变化 平均速度,经过的时间,距离算出来的卡路里
     *
     * @param altitudeChangefloat
     * @param time
     * @param tempLength
     * @return(千卡)
     */
    public static float getCalorie(Context context,double altitudeChangefloat, long time, float tempLength) {
        float Slope = 0;
//		if (tempLength > 0) {
//			Slope = (float) (altitudeChangefloat / tempLength);
//		}
//		if (Slope < 0) {
//			Slope = 0;
//		}
        float speed = tempLength * 60 / time;
        float MET = (3.5f + 0.2f * speed + 0.9f * speed * Slope) / 3.5f;
        float BMR = getBMRFromPerson(context);
        float result = (1000 * BMR / 24) * MET * ((float) time / 3600f);

        return result / 1000;
//                / 4.184f;
    }

    /**
     * 计算卡路里
     *
     * @param beginLoc
     * @param endLoc
     * @param time
     * @return
     */
    public static float getCalorieFromTwoLocation(Context context,AMapLocation beginLoc, AMapLocation endLoc, long time,
                                                  float tempLength) {

        float speed = endLoc.getSpeed() * 60;
        float Slope = 0;
        if (tempLength > 0) {
            Slope = (float) ((endLoc.getAltitude() - beginLoc.getAltitude()) / tempLength);
        }
        if (Slope < 0) {
            Slope = 0;
        }
        float MET = (3.5f + 0.2f * speed + 0.9f * speed * Slope) / 3.5f;
        float BMR = getBMRFromPerson(context);
        float result = (1000 * BMR / 24) * MET * ((float) time / 3600f);
        return result;
    }

    /**
     * 计算BMR
     *
     * @return
     */
    public static float getBMRFromPerson(Context context) {
        DBUserEntity user = LocalApplication.getInstance().getLoginUser(context);
        float result = 0;
        if (user.getGender() == 1) {
            // 1063 + 845 + 66 - 272 = 1702
            result = (float) (66 + (6.23 * 2.2 * user.getWeight()) + (12.7 * user.getHeight() / 2.54)
                    - (6.8 * user.getAge()));
        } else {
            result = (float) (655 + (4.35 * 2.2 * user.getWeight()) + (4.7 * user.getHeight() / 2.54)
                    - (4.7 * user.getAge()));
        }
        return result;
    }
}
