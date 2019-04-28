package com.app.pao.data.db;

import android.content.Context;

import com.app.pao.LocalApplication;
import com.app.pao.config.AppEnum;
import com.app.pao.entity.db.DBEntityLocation;
import com.app.pao.entity.db.DBEntityOriginalPosition;
import com.app.pao.utils.Log;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raul.Fan on 2016/3/15.
 * 原始数据处理
 */
public class OriginalPositionData {

    private static final String TAG = "OriginalPositionData";

    /**
     * 保存单个原始location 信息
     *
     * @param entity
     * @return
     */
    public static boolean saveLocation(final Context context,final DBEntityOriginalPosition entity) {
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
    public static void updateList(final Context context, final List<DBEntityOriginalPosition> list) {
        try {
            LocalApplication.getInstance().getDbUtils(context).replaceAll(list);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取所有未上传的location 信息
     *
     * @param workoutName
     * @return
     */
    public static List<DBEntityOriginalPosition> getUnUploadLocation(final Context context,final String workoutName) {
        List<DBEntityOriginalPosition> result = new ArrayList<DBEntityOriginalPosition>();
        try {
            result = LocalApplication.getInstance().getDbUtils(context)
                    .findAll(Selector.from(DBEntityOriginalPosition.class)
                    .where("starttime", "=", workoutName)
                            .and("uploadStatus", "=", AppEnum.UploadStatus.START));
        } catch (DbException e) {
        }
        return result;
    }
}
