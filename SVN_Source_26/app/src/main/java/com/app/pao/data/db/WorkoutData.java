package com.app.pao.data.db;

import android.content.Context;
import android.util.Log;

import com.app.pao.LocalApplication;
import com.app.pao.config.AppEnum;
import com.app.pao.entity.db.DBEntityHeartrate;
import com.app.pao.entity.db.DBEntityLap;
import com.app.pao.entity.db.DBEntityLocation;
import com.app.pao.entity.db.DBEntitySplite;
import com.app.pao.entity.db.DBEntityWorkout;
import com.app.pao.entity.network.GetWorkoutFullEntity;
import com.app.pao.entity.network.GetWorkoutHeartrateEntity;
import com.app.pao.entity.network.GetWorkoutLapEntity;
import com.app.pao.entity.network.GetWorkoutLocationEntity;
import com.app.pao.entity.network.GetWorkoutSplitsEntity;
import com.app.pao.utils.T;
import com.app.pao.utils.TimeUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raul on 2015/11/18.
 * 跑步历史数据库处理
 */
public class WorkoutData {

    /* contains */
    private static final String TAG = "WorkoutData";


    /**
     * 获取未完成的跑步信息
     *
     * @param userId 用户ID
     * @return
     */
    public static DBEntityWorkout getUnFinishWorkout(final Context context, final int userId) {
        DBEntityWorkout workout = null;
        try {
            workout = LocalApplication.getInstance().getDbUtils(context)
                    .findFirst(Selector.from(DBEntityWorkout.class)
                            .where("status", "=", "1")
                            .and("userid", "=", userId));
            if (workout != null) {
                return workout;
            } else {
                return null;
            }
        } catch (DbException e) {
//            T.showShort(context,e.getMessage());
            return null;
        }
    }

    /**
     * 根据跑步历史获取跑步详情
     *
     * @param name
     * @return
     */
    public static DBEntityWorkout getWorkoutByName(final Context context, final String name) {
        DBEntityWorkout workout = null;
        try {
            workout = LocalApplication.getInstance().getDbUtils(context)
                    .findFirst(Selector.from(DBEntityWorkout.class)
                            .where("name", "=", name));
        } catch (DbException e) {
            e.printStackTrace();
            return null;
        }
        return workout;
    }

    /**
     * 根据用户Id获取用户的所有历史数据list
     *
     * @param userId
     * @return
     */
    public static List<DBEntityWorkout> getWorkOutList(final Context context, final int userId) {
        List<DBEntityWorkout> workoutList = new ArrayList<>();
        try {
            workoutList = LocalApplication.getInstance().getDbUtils(context)
                    .findAll(Selector.from(DBEntityWorkout.class)
                            .where("userid", "=", userId).orderBy("starttime",true));
        } catch (DbException e) {
            e.printStackTrace();
        }
        return workoutList;
    }

    /**
     * 根据用户Id获取用户的所有历史数据list
     *
     * @param userId
     * @return
     */
    public static String getWorkOutCountAndLength(final Context context, final int userId) {
        List<DBEntityWorkout> workoutList = new ArrayList<>();
        int count = 0;
        float length = 0;
        try {
            workoutList = LocalApplication.getInstance().getDbUtils(context)
                    .findAll(Selector.from(DBEntityWorkout.class)
                            .where("userid", "=", userId));
            if (workoutList != null && workoutList.size() > 0) {
                count = workoutList.size();
                for (DBEntityWorkout dbEntityWorkout : workoutList) {
                    length += dbEntityWorkout.length;
                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return count + ":" + length;
    }

    /**
     * 保存跑步历史信息
     */
    public static boolean saveWorkout(final Context context, final DBEntityWorkout workout) {
        try {
            LocalApplication.getInstance().getDbUtils(context).save(workout);
            return true;
        } catch (DbException e) {
            return false;
        }
    }

    /**
     * 更新跑步历史信息
     *
     * @return
     */
    public static boolean updateWorkout(final Context context, final DBEntityWorkout workout) {
        try {
            LocalApplication.getInstance().getDbUtils(context).replace(workout);
            return true;
        } catch (DbException e) {
            return false;
        }
    }

    /**
     * 删除指定的跑步历史
     */
    public static void deleteWorkout(final Context context, final String starttime) {
        try {
            LocalApplication.getInstance().getDbUtils(context).delete(DBEntityWorkout.class,
                    WhereBuilder.b("starttime", "=", starttime));
            LocalApplication.getInstance().getDbUtils(context).delete(DBEntityLap.class,
                    WhereBuilder.b("workoutName", "=", starttime));
            LocalApplication.getInstance().getDbUtils(context).delete(DBEntityHeartrate.class,
                    WhereBuilder.b("workoutName", "=", starttime));
            LocalApplication.getInstance().getDbUtils(context).delete(DBEntityLocation.class,
                    WhereBuilder.b("workoutName", "=", starttime));
            LocalApplication.getInstance().getDbUtils(context).delete(DBEntitySplite.class,
                    WhereBuilder.b("workoutName", "=", starttime));
            LocalApplication.getInstance().getDbUtils(context).delete(DBEntityLocation.class,
                    WhereBuilder.b("workoutName", "=", starttime));
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存跑步历史
     */
    public static boolean saveFullWorkout(final Context context, final GetWorkoutFullEntity fullInfo) {
        String name = fullInfo.name;
        String starttime = fullInfo.starttime;
        int workoutId = fullInfo.id;
        int status = fullInfo.status;
        long duration = fullInfo.duration;
        float length = fullInfo.length;
        double maxHeight = fullInfo.maxheight;
        double minHeight = fullInfo.minheight;
        int maxPace = fullInfo.maxpace;
        int minPace = fullInfo.minpace;
        int avgPace = 0;
        int maxSpeed = fullInfo.maxspeed;
        int avgSpeed = 0;
        if (fullInfo.getLength() != 0) {
            avgSpeed = (int) (fullInfo.duration * 1000 / fullInfo.length);
        }
        avgPace = avgSpeed;
        float calorie = fullInfo.getCalorie();
        int avgHeartrate = fullInfo.avgheartrate;
        int maxHeartrate = fullInfo.maxheartrate;
        int minHeartrate = fullInfo.minheartrate;
        int minSpeed = fullInfo.getMinspeed();
        int userId = fullInfo.getUsers_id();
        deleteWorkout(context, name);
        // 获取workout 信息
        DBEntityWorkout saveEntity = new DBEntityWorkout(System.currentTimeMillis(), name, starttime, workoutId, status,
                duration, length, calorie, maxHeight, minHeight, maxPace, minPace, avgPace, maxSpeed, avgSpeed,
                avgHeartrate, maxHeartrate, minHeartrate, minSpeed, userId,-1,-1);
        List<DBEntityLap> lapDbList = new ArrayList<DBEntityLap>();
        List<DBEntityLocation> locationDbList = new ArrayList<DBEntityLocation>();
        List<DBEntitySplite> spliteListDb = new ArrayList<DBEntitySplite>();
        List<DBEntityHeartrate> heartrateDBList = new ArrayList<DBEntityHeartrate>();

        List<GetWorkoutLapEntity> lapList = fullInfo.getLap();
        List<GetWorkoutSplitsEntity> spliteList = fullInfo.getSplits();

        boolean bSaveSuccess = true;
        if (spliteList != null) {
            // 保存分段信息
            for (int i = 0; i < spliteList.size(); i++) {
                GetWorkoutSplitsEntity splite = spliteList.get(i);
                DBEntitySplite dbSplite = new DBEntitySplite(System.currentTimeMillis() + i, i, name,
                        splite.getLength(), splite.getDuration(), splite.getAvgheartrate(), splite.getMinaltitude(),
                        splite.getMaxaltitude(), status, splite.getLatitude(), splite.getLongitude());
                spliteListDb.add(dbSplite);
            }
            bSaveSuccess = SpliteData.saveSpliteList(context, spliteListDb);
        }
        if (!bSaveSuccess) {
            return false;
        }
        // 遍历LAP
        long time = System.currentTimeMillis();
        long loc_k = 0;
        long heart_d = 0;
        long lap_k = 0;
        if (lapList != null) {
            for (int i = 0; i < lapList.size(); i++) {
                GetWorkoutLapEntity lap = lapList.get(i);
                long lapTimeOfSet = TimeUtils.getTimesetFromStartTime(fullInfo.getStarttime(), lap.getStarttime());
                // 获取location 信息
                for (GetWorkoutLocationEntity locEntity : lap.getLocationdata()) {
                    long locId = time + loc_k;
                    loc_k++;
                    DBEntityLocation dbLoc = new DBEntityLocation(locId, name, lap.getStarttime(),
                            locEntity.getLatitude(), locEntity.getLongitude(), locEntity.getAltitude(),
                            locEntity.getHaccuracy(), locEntity.getVaccuracy(),
                            (lapTimeOfSet + locEntity.getTimeoffset()), locEntity.getSpeed(),
                            AppEnum.UploadStatus.OVER, locEntity.getLongitude(), locEntity.getAltitude());
                    locationDbList.add(dbLoc);
                }
                // 获取心率信息
                for (GetWorkoutHeartrateEntity heartRateEntity : lap.getHeartratedata()) {
                    long heartId = time + heart_d;
                    DBEntityHeartrate dbheartrate = new DBEntityHeartrate(heartId, name, -1, -1,
                            (lapTimeOfSet + heartRateEntity.getTimeoffset()), heartRateEntity.getBpm(),
                            AppEnum.UploadStatus.OVER,0);
                    heart_d++;
                    heartrateDBList.add(dbheartrate);
                }
                long lapId = time + lap_k;
                DBEntityLap dblap = new DBEntityLap(lapId, lap.getStarttime(), name, lap.getDuration(), lap.getLap(),
                        lap.getLength(), lap.getStatus());
                lap_k++;
                lapDbList.add(dblap);
            }
        }

        // 保存位置信息
        bSaveSuccess = LocationData.saveLocationList(context, locationDbList, name);
        if (!bSaveSuccess) {
            return false;
        }
        // 保存心率信息
        bSaveSuccess = HeartrateData.saveHeartrateList(context, heartrateDBList,name);
        if (!bSaveSuccess) {
            return false;
        }
        // 保存lap信息
        bSaveSuccess = LapData.saveLapList(context, lapDbList);
        if (!bSaveSuccess) {
            return false;
        }
        try {
            LocalApplication.getInstance().getDbUtils(context).save(saveEntity);
        } catch (DbException e) {
            bSaveSuccess = false;
        }
        return bSaveSuccess;
    }
}
