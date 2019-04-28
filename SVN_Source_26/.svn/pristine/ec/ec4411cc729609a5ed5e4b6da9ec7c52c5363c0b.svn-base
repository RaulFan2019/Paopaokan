package com.app.pao.data.db;

import android.content.Context;

import com.app.pao.LocalApplication;
import com.app.pao.config.AppEnum;
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
public class SpliteData {

    /**
     * 获取未完成的Splite
     *
     * @return
     */
    public static DBEntitySplite getUnFinishSplite(Context context, final String workoutName) {
        DBEntitySplite splite;
        try {
            splite = LocalApplication.getInstance().getDbUtils(context)
                    .findFirst(Selector.from(DBEntitySplite.class).where("status", "=", AppEnum.RunningStatus
                            .RUNNING).and("workoutName", "=", workoutName));
            if (splite != null) {
                return splite;
            } else {
                return null;
            }
        } catch (DbException e) {
            return null;
        }
    }

    /**
     * 获取Splite序号
     *
     * @return
     */
    public static long getSpliteIndexFromWorkout(final Context context, final String workoutName) {
        try {
            long count = LocalApplication.getInstance().getDbUtils(context)
                    .count(Selector.from(DBEntitySplite.class)
                            .where("workoutName", "=", "workoutName"));
            return count;
        } catch (DbException e) {
            return 0;
        }
    }

    /**
     * 获取所有分段列表
     *
     * @return
     */
    public static List<DBEntitySplite> getSpliteFromWorkout(final Context context, final String workoutName) {
        List<DBEntitySplite> result = new ArrayList<DBEntitySplite>();
        try {
            List<DBEntitySplite> list = LocalApplication.getInstance().getDbUtils(context)
                    .findAll(Selector.from(DBEntitySplite.class)
                            .where("workoutName", "=", workoutName));
            if (list != null && list.size() > 0) {
                result = list;
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 保存分段信息
     */
    public static boolean saveSplite(final Context context, final DBEntitySplite split) {
        try {
            LocalApplication.getInstance().getDbUtils(context).save(split);
            return true;
        } catch (DbException e) {
            return false;
        }
    }

    /**
     * 保存分段信息
     */
    public static boolean saveSpliteList(final Context context, final List<DBEntitySplite> splitList) {
        try {
            LocalApplication.getInstance().getDbUtils(context).saveAll(splitList);
            return true;
        } catch (DbException e) {
            return false;
        }
    }

    /**
     * 更新splite信息
     *
     * @return
     */
    public static boolean updateSplite(final Context context, final DBEntitySplite splite) {
        try {
            LocalApplication.getInstance().getDbUtils(context).replace(splite);
            return true;
        } catch (DbException e) {
            return false;
        }
    }
}
