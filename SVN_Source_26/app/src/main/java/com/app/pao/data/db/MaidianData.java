package com.app.pao.data.db;

import android.content.Context;

import com.app.pao.LocalApplication;
import com.app.pao.entity.db.DBEntityMaidian;
import com.app.pao.utils.Log;
import com.lidroid.xutils.exception.DbException;

import java.util.List;

/**
 * 埋点信息处理
 * <p>
 * Created by LY on 2016/4/18.
 */
public class MaidianData {

    private static final String TAG = "MaidianData";
    /**
     * 保存单条埋点信息
     *
     * @param context
     * @param maidian
     * @return
     */
    public static boolean saveMaidian(final Context context, final DBEntityMaidian maidian) {
//        Log.v(TAG,"saveMaidian");
        try {
            LocalApplication.getInstance().getDbUtils(context).save(maidian);
            return true;
        } catch (DbException e) {
            return false;
        }
    }

    /**
     * 保存埋点数组
     *
     * @param context
     * @param maidianList
     * @return
     */
    public static boolean saveMaidianList(final Context context, final List<DBEntityMaidian> maidianList) {
        try {
            LocalApplication.getInstance().getDbUtils(context).saveAll(maidianList);
            return true;
        } catch (DbException e) {
            return false;
        }
    }

    /**
     * 获取所有的埋点信息
     *
     * @param context
     * @return
     */
    public static List<DBEntityMaidian> getAllMaidianList(final Context context) {
        try {
            List<DBEntityMaidian> maidianList = LocalApplication.getInstance().getDbUtils(context)
                    .findAll(DBEntityMaidian.class);
            if (maidianList == null || maidianList.size() == 0) {
                return null;
            } else {
                return maidianList;
            }
        } catch (DbException e) {
            return null;
        }
    }

    /**
     * 删除所有当前埋点信息
     *
     * @param context
     */
    public static void deleteAllMaidianList(final Context context, final List<DBEntityMaidian> maidianList) {
//        Log.v(TAG,"deleteAllMaidianList");
        try {
            if (maidianList != null) {
                LocalApplication.getInstance().getDbUtils(context).deleteAll(maidianList);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
}
