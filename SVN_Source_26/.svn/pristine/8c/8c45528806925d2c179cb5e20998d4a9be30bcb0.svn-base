package com.app.pao.data.db;

import android.content.Context;

import com.amap.api.maps.model.LatLng;
import com.app.pao.LocalApplication;
import com.app.pao.config.AppEnum;
import com.app.pao.entity.db.DBEntityLap;
import com.app.pao.entity.db.DBEntityLocation;
import com.app.pao.entity.db.DBEntityWorkout;
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
public class LocationData {

    private static final String TAG = "LocationData";

    /**
     * 保存单个location 信息
     *
     * @param entity
     * @return
     */
    public static boolean saveLocation(final Context context, final DBEntityLocation entity) {
        try {
            LocalApplication.getInstance().getDbUtils(context).save(entity);
            return true;
        } catch (DbException e) {
            // Log.v(TAG, "saveLocation fail");
            return false;
        }
    }

    /**
     * 插入位置列表
     *
     * @param locList
     * @param workoutName
     * @return
     */
    public static boolean saveLocationList(final Context context,
                                           final List<DBEntityLocation> locList, final String workoutName) {
        if (locList.size() < 1) {
            return true;
        }
        //key
        String[] fields = new String[]{"workoutName", "lapStartTime", "latitude", "longitude",
                "haccuracy", "altitude", "vaccuracy", "timeoffset", "speed", "uploadStatus"};
        String fieldStr = "(";
        for (int sqlStr = 0; sqlStr < fields.length; ++sqlStr) {
            fieldStr = fieldStr + "\'" + fields[sqlStr] + "\',";
        }
        fieldStr = fieldStr.substring(0, fieldStr.length() - 1);
        fieldStr = fieldStr + ")";
        int index = 0;
        int step = locList.size() / 500 + 1;
        try {
            LocalApplication.getInstance().getDbUtils(context).createTableIfNotExist(DBEntityLocation.class);
            StringBuffer insertBuffer = new StringBuffer();
            for (int i = 1; i < step; i++) {
                insertBuffer.append("INSERT INTO " + "location" + " " + fieldStr + " VALUES ");
                List<DBEntityLocation> tempList = locList.subList(index, index + 500);
                index = index + 500;
                for (int j = 0; j < tempList.size(); j++) {
                    DBEntityLocation e = tempList.get(j);
                    String valueStr = "('" + workoutName + "','" + e.getLapStartTime() + "'," +
                            e.getLatitude() + "," + e.getLongitude() + "," + e.getHaccuracy() + "," + e.getAltitude()
                            + "," + e.getVaccuracy() + "," + e.getTimeoffset() + "," + e.getSpeed() + "," + 1 + "),";
                    insertBuffer.append(valueStr);
                }
                insertBuffer.deleteCharAt(insertBuffer.length() - 1);
                LocalApplication.getInstance().getDbUtils(context).execNonQuery(insertBuffer.toString());
                insertBuffer.setLength(0);
            }
            List<DBEntityLocation> tempList = locList.subList(index, locList.size() - 1);
            if (tempList.size() == 0) {
                return true;
            }
            insertBuffer.append("INSERT INTO " + "location" + " " + fieldStr + " VALUES ");
            for (int j = 0; j < tempList.size(); j++) {
                DBEntityLocation e = tempList.get(j);
                String valueStr = "('" + workoutName + "','" + e.getLapStartTime() + "'," +
                        e.getLatitude() + "," + e.getLongitude() + "," + e.getHaccuracy() + "," + e.getAltitude()
                        + "," + e.getVaccuracy() + "," + e.getTimeoffset() + "," + e.getSpeed() + "," + 1 + "),";
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
     * 获取一个跑步历史的第一个点
     *
     * @param workout
     * @return
     */
    public static DBEntityLocation getFirstLocation(final Context context, final DBEntityWorkout workout) {
        DBEntityLocation location = null;
        try {
            location = LocalApplication.getInstance().getDbUtils(context)
                    .findFirst(Selector.from(DBEntityLocation.class)
                            .where("workoutName", "=", workout.getName()));
        } catch (DbException e) {
            e.printStackTrace();
        }
        return location;
    }

    /**
     * 获取最快配速
     *
     * @return
     */
    public static int getMinPace(final Context context, final String workoutName) {
        try {
            DBEntityLocation location = LocalApplication.getInstance().getDbUtils(context)
                    .findFirst(Selector.from(DBEntityLocation.class)
                            .where("workoutName", "=", workoutName).and("speed", "<>", "0").orderBy("speed"));
            if (location != null) {
                return location.speed;
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取最后一个或第一个
     *
     * @param lap
     * @param first
     * @return
     */
    public static DBEntityLocation getLocationByLap(final Context context, DBEntityLap lap, boolean first) {
        DBEntityLocation location = null;
        try {
            location = LocalApplication.getInstance().getDbUtils(context)
                    .findFirst(Selector.from(DBEntityLocation.class)
                            .where("workoutName", "=", lap.getWorkoutName())
                            .and("lapStartTime", "=", lap.getStarttime())
                            .orderBy("location_id", !first));
        } catch (DbException e) {
            e.printStackTrace();
            return null;
        }
        return location;
    }


    /**
     * 获取最后18个点
     *
     * @param lap
     * @return
     */
    public static List<DBEntityLocation> getLocationListByLap(final Context context, final DBEntityLap lap) {
        List<DBEntityLocation> result = new ArrayList<>();
        try {
            List<DBEntityLocation> tempList = LocalApplication.getInstance().getDbUtils(context)
                    .findAll(Selector.from(DBEntityLocation.class)
                            .where("workoutName", "=", lap.getWorkoutName())
                            .and("lapStartTime", "=", lap.getStarttime()).orderBy("location_id"));
            if (tempList != null) {
                result.addAll(tempList);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 获取所有未上传的location 信息
     *
     * @param workoutName
     * @return
     */
    public static List<DBEntityLocation> getUnUploadLocation(final Context context, final String workoutName, final String lapStartTime) {
        List<DBEntityLocation> result = new ArrayList<DBEntityLocation>();
        try {
            result = LocalApplication.getInstance().getDbUtils(context).findAll(Selector.from(DBEntityLocation.class)
                    .where("workoutName", "=", workoutName).and("uploadStatus", "=", AppEnum.UploadStatus.START)
                    .and("lapStartTime", "=", lapStartTime));
        } catch (DbException e) {
        }
        return result;
    }

    /**
     * 更新列表信息
     */
    public static void updateList(final Context context, final List<DBEntityLocation> list) {
        try {
            LocalApplication.getInstance().getDbUtils(context).replaceAll(list);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取当前指定workout的所有位置信息
     *
     * @return
     */
    public static List<LatLng> getAllLocFromWorkout(final Context context, final String name) {
        List<LatLng> result = new ArrayList<LatLng>();
        try {
            List<DBEntityLocation> list = LocalApplication.getInstance().getDbUtils(context)
                    .findAll(Selector.from(DBEntityLocation.class).where("workoutName", "=", name));
            if (list != null) {
                for (DBEntityLocation entity : list) {
                    result.add(new LatLng(entity.getLatitude(), entity.getLongitude()));
                }
            } else {
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static List<DBEntityLocation> getAllDbLocFromWorkout(final Context context, final String name) {
        List<DBEntityLocation> result = new ArrayList<DBEntityLocation>();
        try {
            result = LocalApplication.getInstance().getDbUtils(context)
                    .findAll(Selector.from(DBEntityLocation.class).where("workoutName", "=", name));

        } catch (DbException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取从指定位置开始的所有位置信息
     *
     * @param name
     * @param lastId
     * @return
     */
    public static List<DBEntityLocation> getAfterLocFromWorkout(final Context context, final String name, final long lastId) {
        List<DBEntityLocation> result = new ArrayList<DBEntityLocation>();
        try {
            result = LocalApplication.getInstance().getDbUtils(context).findAll(Selector.from(DBEntityLocation.class)
                    .where("workoutName", "=", name).and("location_id", ">=", lastId).orderBy("location_id"));

        } catch (DbException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 最后6个点
     *
     * @param originalList
     * @param smoothList
     * @param mDBLap
     */
//    public static void freshSmoothData(ArrayList<LatLng> originalList,
//                                       ArrayList<LatLng> smoothList, DBEntityLap mDBLap) {
//        try {
//            List<DBEntityLocation> locations = LocalApplication.getInstance().getDbUtils().findAll(Selector.from(DBEntityLocation.class)
//                    .where("workoutName", "=", mDBLap.getWorkoutName())
//                    .and("lapStartTime", "=", mDBLap.getStarttime()).limit(6)
//                    .orderBy("location_id", true));
//            if (locations!= null && !locations.isEmpty()) {
//                for (DBEntityLocation location : locations) {
//                    originalList.add(new LatLng(location.getOriginallatitude(), location.getOriginallongitude()));
//                    smoothList.add(new LatLng(location.getLatitude(), location.getLongitude()));
//                }
//            }
//        } catch (DbException e) {
//            e.printStackTrace();
//        }
//    }
}
