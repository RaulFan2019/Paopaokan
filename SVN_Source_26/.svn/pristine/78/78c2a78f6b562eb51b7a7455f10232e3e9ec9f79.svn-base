package com.app.pao.data.db;

import android.content.Context;

import com.app.pao.LocalApplication;
import com.app.pao.entity.db.DBEntityRecord;
import com.app.pao.entity.network.GetRunningUploadResult;
import com.app.pao.utils.T;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raul.Fan on 2016/3/30.
 * 跑步新记录数据操作
 */
public class RecordData {


    /**
     * 存储新的跑步记录
     *
     * @param recordlist
     */
    public static boolean saveRecord(final Context context, final int userId,
                                     final List<GetRunningUploadResult.NewrecordEntity> recordlist) {
        try {
            //删除过去的记录
            deleteOldRecord(context, userId);
            ArrayList<DBEntityRecord> saveList = new ArrayList<>();
            String dbid = System.currentTimeMillis() + "";
            for (int i = 0; i < recordlist.size(); i++) {
                GetRunningUploadResult.NewrecordEntity entity = recordlist.get(i);
                DBEntityRecord record = new DBEntityRecord(dbid + i, userId, entity.getType(), entity.getRecord(),
                        entity.getPrerecord(), entity.getRanking(), entity.getPreranking());
                saveList.add(record);
            }
            LocalApplication.getInstance().getDbUtils(context).saveAll(saveList);
            return true;
        } catch (DbException e) {
            return false;
        }
    }

    public static void deleteOldRecord(final Context context, final int userId) {
        try {
            List<DBEntityRecord> records = LocalApplication.getInstance().getDbUtils(context)
                    .findAll(Selector.from(DBEntityRecord.class).where("userid", "=", userId));
            if(records != null){
                LocalApplication.getInstance().getDbUtils(context).deleteAll(records);
            }

        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取跑步记录
     *
     * @param context
     * @param userId
     * @return
     */
    public static List<DBEntityRecord> getRecords(final Context context, final int userId) {
        List<DBEntityRecord> records = new ArrayList<>();
        try {
            List<DBEntityRecord> tempList = LocalApplication.getInstance().getDbUtils(context)
                    .findAll(Selector.from(DBEntityRecord.class).where("userid", "=", userId));
            if (tempList != null) {
                records.addAll(tempList);
            }
        } catch (DbException e) {
            return records;
        }
        return records;
    }
}
