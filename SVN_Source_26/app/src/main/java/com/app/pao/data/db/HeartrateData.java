package com.app.pao.data.db;

import android.content.Context;

import com.app.pao.LocalApplication;
import com.app.pao.config.AppEnum;
import com.app.pao.entity.db.DBEntityHeartrate;
import com.app.pao.entity.db.DBEntityLocation;
import com.app.pao.utils.Log;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import java.util.ArrayList;
import java.util.List;

/**
 * 跑步历史信息处理
 *
 * @author Raul
 */
public class HeartrateData {

    private static final String TAG = "LocationData";

    /**
     * 保存单个Heartrate 信息
     *
     * @param entity
     * @return
     */
    public static boolean saveHeartrate(final Context context, final DBEntityHeartrate entity) {
        try {
            LocalApplication.getInstance().getDbUtils(context).save(entity);
            return true;
        } catch (DbException e) {
            return false;
        }
    }

    /**
     * 更新列表信息
     */
    public static void updateList(final Context context, final List<DBEntityHeartrate> list) {
        try {
            LocalApplication.getInstance().getDbUtils(context).replaceAll(list);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 计算 split 平均心率
     *
     * @return
     */
    public static int getAvgheartrateInSplite(final Context context,
                                              final String workoutname, final long spliteid) {
        int result = 0;
        int total = 0;
        try {
            List<DBEntityHeartrate> heartlist = LocalApplication.getInstance().getDbUtils(context)
                    .findAll(Selector.from(DBEntityHeartrate.class).where("spliteId", "=", spliteid).and("workoutName",
                            "=", workoutname));
            if (heartlist != null) {
                for (DBEntityHeartrate hreatrate : heartlist) {
                    total += hreatrate.getBmp();
                }
                if (heartlist.size() != 0) {
                    result = total / heartlist.size();
                }
            }
        } catch (DbException e) {
            e.printStackTrace();
            return result;
        }
        return result;
    }

    /**
     * 获取平均心率
     *
     * @return
     */
    public static int getAvgHeartrateInWork(final Context context, final String name) {
        int result = 0;
        int total = 0;
        try {
            List<DBEntityHeartrate> heartlist = LocalApplication.getInstance().getDbUtils(context)
                    .findAll(Selector.from(DBEntityHeartrate.class).where("workoutName", "=", name));
            if (heartlist != null) {
                for (DBEntityHeartrate hreatrate : heartlist) {
                    total += hreatrate.getBmp();
                }
                result = total / heartlist.size();
            }
        } catch (DbException e) {
            e.printStackTrace();
            return result;
        }
        return result;
    }

    /**
     * 保存Heartrate信息
     *
     * @return
     */
    public static boolean saveHeartrateList(final Context context,
                                            final List<DBEntityHeartrate> HeartrateList, final String workoutName) {

        if (HeartrateList.size() < 1) {
            return true;
        }
        //key
        String[] fields = new String[]{"workoutName", "lapId", "spliteId", "timeofset",
                "bmp", "uploadStatus"};
        String fieldStr = "(";
        for (int sqlStr = 0; sqlStr < fields.length; ++sqlStr) {
            fieldStr = fieldStr + "\'" + fields[sqlStr] + "\',";
        }
        fieldStr = fieldStr.substring(0, fieldStr.length() - 1);
        fieldStr = fieldStr + ")";
        int index = 0;
        int step = HeartrateList.size() / 500 + 1;
        try {
            LocalApplication.getInstance().getDbUtils(context).createTableIfNotExist(DBEntityHeartrate.class);
            StringBuffer insertBuffer = new StringBuffer();
            for (int i = 1; i < step; i++) {
                insertBuffer.append("INSERT INTO " + "heartrate" + " " + fieldStr + " VALUES ");
                List<DBEntityHeartrate> tempList = HeartrateList.subList(index, index + 500);
                index = index + 500;
                for (int j = 0; j < tempList.size(); j++) {
                    DBEntityHeartrate e = tempList.get(j);
                    String valueStr = "('" + workoutName + "','" + e.lapId + "'," +
                            e.spliteId + "," + e.timeofset + "," + e.bmp + "," + e.uploadStatus + "),";
                    insertBuffer.append(valueStr);
                }
                insertBuffer.deleteCharAt(insertBuffer.length() - 1);
                LocalApplication.getInstance().getDbUtils(context).execNonQuery(insertBuffer.toString());
                insertBuffer.setLength(0);
            }
            List<DBEntityHeartrate> tempList = HeartrateList.subList(index, HeartrateList.size() - 1);
            if (tempList.size() == 0) {
                return true;
            }
            insertBuffer.append("INSERT INTO " + "heartrate" + " " + fieldStr + " VALUES ");
            for (int j = 0; j < tempList.size(); j++) {
                DBEntityHeartrate e = tempList.get(j);
                String valueStr = "('" + workoutName + "','" + e.lapId + "'," +
                        e.spliteId + "," + e.timeofset + "," + e.bmp + "," + e.uploadStatus + "),";
                insertBuffer.append(valueStr);
            }
            insertBuffer.deleteCharAt(insertBuffer.length() - 1);
            LocalApplication.getInstance().getDbUtils(context).execNonQuery(insertBuffer.toString());

        } catch (DbException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 获取所有未上传的心律
     *
     * @param name
     * @return
     */
    public static List<DBEntityHeartrate> getUnUploadHeartrate(final Context context,
                                                               final String name, final long lapId) {
        List<DBEntityHeartrate> result = new ArrayList<DBEntityHeartrate>();
        try {
            result = LocalApplication.getInstance().getDbUtils(context)
                    .findAll(Selector.from(DBEntityHeartrate.class).where("workoutName", "=", name)
                            .and("uploadStatus", "=", AppEnum.UploadStatus.START).and("lapId", "=", lapId));
        } catch (DbException e) {
        }
        return result;
    }

    /**
     * 获取当前指定workout的所有心率
     *
     * @return
     */
    public static List<DBEntityHeartrate> getAllHeartrateFromWork(final Context context, final String name) {
        List<DBEntityHeartrate> result = new ArrayList<DBEntityHeartrate>();
        try {
            List<DBEntityHeartrate> list = LocalApplication.getInstance().getDbUtils(context)
                    .findAll(Selector.from(DBEntityHeartrate.class).where("workoutName", "=", name));
            if (list != null) {
                result = list;
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return result;
    }
}
