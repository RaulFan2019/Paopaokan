package com.app.pao.data.db;

import android.content.Context;

import com.app.pao.LocalApplication;
import com.app.pao.config.AppEnum;
import com.app.pao.entity.db.DBEntityLap;
import com.app.pao.entity.db.DBEntitySplite;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import java.util.ArrayList;
import java.util.List;

/**
 * 跑步历史信息处理
 *
 * @author Raul
 */
public class LapData {

    /**
     * 获取未完成的lap
     *
     * @return
     */
    public static DBEntityLap getUnFinishLap(final Context context,final String workoutName) {
        DBEntityLap lap;
        try {
            lap = LocalApplication.getInstance().getDbUtils(context)
                    .findFirst(Selector.from(DBEntityLap.class)
                            .where("status", "=", AppEnum.RunningStatus.RUNNING)
                            .and("workoutName", "=", workoutName));
            if (lap != null) {
                return lap;
            } else {
                return null;
            }
        } catch (DbException e) {
            return null;
        }
    }

    /**
     * 获取lap序号
     *
     * @return
     */
    public static long getLapIndexFromWorkout(final Context context,final String workoutName) {
        try {

            List<DBEntityLap> lapList = LocalApplication.getInstance().getDbUtils(context)
                    .findAll(Selector.from(DBEntityLap.class)
                            .where("workoutName", "=", workoutName));
            if (lapList == null || lapList.size() == 0) {
                return 0;
            } else {
                return lapList.size();
            }
        } catch (DbException e) {
            return 0;
        }
    }

    /**
     * 保存lap信息
     */
    public static boolean saveLap(final Context context, final DBEntityLap lap) {
        try {
            LocalApplication.getInstance().getDbUtils(context).save(lap);
            return true;
        } catch (DbException e) {
            return false;
        }
    }

    /**
     * 保存lap信息
     */
    public static boolean saveLapList(final Context context , final List<DBEntityLap> lapList) {
        try {
            LocalApplication.getInstance().getDbUtils(context).saveAll(lapList);
            return true;
        } catch (DbException e) {
            return false;
        }
    }

    /**
     * 更新lap信息
     *
     * @return
     */
    public static boolean updateLap(Context context,DBEntityLap lap) {
        try {
            LocalApplication.getInstance().getDbUtils(context).replace(lap);
            return true;
        } catch (DbException e) {
            return false;
        }
    }

    /**
     * 获取所有分段列表
     *
     * @return
     */
    public static List<DBEntityLap> getLapFromWorkout(Context context,String workoutName) {
        List<DBEntityLap> result = new ArrayList<DBEntityLap>();
        try {
            result = LocalApplication.getInstance().getDbUtils(context)
                    .findAll(Selector.from(DBEntityLap.class).where("workoutName", "=", workoutName));
        } catch (DbException e) {
            e.printStackTrace();
        }
        return result;
    }
}
