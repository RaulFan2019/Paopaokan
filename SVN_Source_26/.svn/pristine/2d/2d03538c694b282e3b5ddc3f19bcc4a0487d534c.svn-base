package com.app.pao.data.db;

import android.content.Context;

import com.app.pao.LocalApplication;
import com.app.pao.entity.db.DBEntityCache;
import com.app.pao.entity.db.DBEntityMaidian;
import com.lidroid.xutils.exception.DbException;

import java.util.List;

/**
 * Created by Raul.Fan on 2016/4/20.
 */
public class CacheData {


    /**
     * 保存单挑缓存信息
     *
     * @param context
     * @param cache
     * @return
     */
    public static boolean saveCache(final Context context, final DBEntityCache cache) {
        try {
            LocalApplication.getInstance().getDbUtils(context).save(cache);
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
    public static List<DBEntityCache> getAllCacheList(final Context context,final int userId) {
        try {
            List<DBEntityCache> cacheList = LocalApplication.getInstance().getDbUtils(context)
                    .findAll(DBEntityCache.class);
            if (cacheList == null || cacheList.size() == 0) {
                return null;
            } else {
                return cacheList;
            }
        } catch (DbException e) {
            return null;
        }
    }
}
