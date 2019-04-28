package com.app.pao.data.db;

import android.content.Context;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.app.pao.LocalApplication;
import com.app.pao.config.AppEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.entity.db.DBEntityHeartrate;
import com.app.pao.entity.db.DBEntityLap;
import com.app.pao.entity.db.DBEntityLocation;
import com.app.pao.entity.db.DBEntityOriginalPosition;
import com.app.pao.entity.db.DBEntitySplite;
import com.app.pao.entity.db.DBEntityWorkout;
import com.app.pao.entity.db.DBUploadEntity;
import com.app.pao.entity.event.EventNewUploadData;
import com.app.pao.utils.Log;
import com.app.pao.utils.TimeUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.table.DbModel;
import com.lidroid.xutils.exception.DbException;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Created by Raul on 2015/10/30.
 * upload 数据库管理
 */
public class UploadData extends Observable {

    private static final String TAG = "UploadData";

    /**
     * 创建一个新的上传数据
     *
     * @param dbUploadEntity
     * @return
     */
    public static final boolean createNewUploadData(final Context context, final DBUploadEntity dbUploadEntity) {
        try {
            LocalApplication.getInstance().getDbUtils(context).save(dbUploadEntity);
            EventBus.getDefault().post(new EventNewUploadData());
//            LocalApplication.getInstance().getUploadData().setChanged();
//            LocalApplication.getInstance().getUploadData().notifyObservers();
            return true;
        } catch (DbException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 获取需要同步的历史数据条数
     *
     * @return
     */
    public static final int getSyncCount(final Context context, String startTime) {
        try {
            List<DBUploadEntity> entities = LocalApplication.getInstance().getDbUtils(context)
                    .findAll(Selector.from(DBUploadEntity.class)
                            .where("workoutName", "=", startTime)
                            .and("uploadType", "=", DBUploadEntity.TYPE_UPLOAD_WORKOUT));
            if (entities != null) {
                return entities.size();
            } else {
                return 0;
            }
        } catch (DbException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 删除一个需要上传的数据
     *
     * @param dbUploadEntity
     * @return
     */
    public static boolean deleteUploadData(final Context context, final DBUploadEntity dbUploadEntity) {
        try {
            LocalApplication.getInstance().getDbUtils(context).delete(dbUploadEntity);
            return true;
        } catch (DbException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除跑步历史相关的上传数据
     *
     * @param workout
     * @return
     */
    public static boolean DropWorkout(final Context context, final DBEntityWorkout workout) {
        try {
            List<DBUploadEntity> deleteList = LocalApplication.getInstance().getDbUtils(context)
                    .findAll(Selector.from(DBUploadEntity.class)
                            .where("workoutName", "=", workout.getName())
                            .and("userId", "=", workout.getUserId())
                            .and("uploadType", "=", DBUploadEntity.TYPE_UPLOAD_WORKOUT));
            LocalApplication.getInstance().getDbUtils(context).deleteAll(deleteList);
        } catch (DbException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 获取没有同步完成的跑步历史
     *
     * @return
     */
    public static List<String> getUnfinishWorkoutNameList(final Context context, final int userid) {
        List<String> result = new ArrayList<String>();
        try {
            List<DbModel> dbModel = LocalApplication.getInstance().getDbUtils(context).findDbModelAll(Selector.from
                    (DBUploadEntity.class).where("userId", "=", userid).and("uploadType", "=",
                    DBUploadEntity.TYPE_UPLOAD_WORKOUT).groupBy("workoutName"));

            if (dbModel != null) {
                for (DbModel model : dbModel) {
                    result.add(model.getString("workoutName"));
                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 保存需要上传的历史信息
     *
     * @param workout
     * @param lap
     */
    public static int saveLocationInfo(Context context, DBEntityWorkout workout, DBEntityLap lap, int userid) {
        List<DBEntityLocation> locList = LocationData.getUnUploadLocation(context, workout.getName(), lap.getStarttime());
        if (locList != null) {
            if (locList.size() != 0) {
                JSONObject uploadObj = new JSONObject();
                uploadObj.put("contenttype", "sessiondata");
                uploadObj.put("users_id", workout.userId);
                uploadObj.put("starttime", workout.starttime);
                uploadObj.put("length", workout.length);
                uploadObj.put("duration", workout.duration);
                uploadObj.put("calorie", workout.calorie);
                uploadObj.put("minpace", 0);
                uploadObj.put("maxpace", 0);
                uploadObj.put("maxheight", workout.maxHeight);
                uploadObj.put("minheight", workout.minHeight);
                JSONArray lapArray = new JSONArray();
                JSONObject lapobj = new JSONObject();
                JSONArray locArray = new JSONArray();
                // JSONArray heartArray = new JSONArray();
                for (DBEntityLocation loc : locList) {
                    JSONObject locObj = new JSONObject();
                    locObj.put("timeoffset", loc.timeoffset);
                    locObj.put("latitude", loc.latitude);
                    locObj.put("longitude", loc.longitude);
                    locObj.put("originallatitude", loc.originallatitude);
                    locObj.put("originallongitude", loc.originallongitude);
                    locObj.put("altitude", loc.altitude);
                    locObj.put("haccuracy", loc.haccuracy);
                    locObj.put("vaccuracy", loc.vaccuracy);
                    locObj.put("speed", loc.speed);
                    locArray.add(locObj);
                    loc.setUploadStatus(AppEnum.UploadStatus.OVER);
                }

                lapobj.put("lap", lap.getLap());
                lapobj.put("starttime", lap.getStarttime());
                lapobj.put("locationdata", locArray);
                lapArray.add(lapobj);
                uploadObj.put("lap", lapArray);
                DBUploadEntity uploadEntity = new DBUploadEntity(DBUploadEntity.TYPE_UPLOAD_WORKOUT, URLConfig
                        .URL_SAVE_DATA_SEGMENT, uploadObj.toJSONString(), workout.getName(), userid);
                UploadData.createNewUploadData(context, uploadEntity);
                LocationData.updateList(context, locList);
            }
            return locList.size();
        } else {
            return 0;
        }
    }


    /**
     * 保存原始位置信息
     *
     * @param workout
     * @param userid
     */
    public static void saveOriginalLocationInfo(Context context, DBEntityWorkout workout, int userid) {
        List<DBEntityOriginalPosition> locList = OriginalPositionData.getUnUploadLocation(context, workout.getName());
        if (locList != null) {
            if (locList.size() != 0) {
                JSONObject uploadObj = new JSONObject();
                uploadObj.put("starttime", workout.getStarttime());
                JSONArray locArray = new JSONArray();
                for (DBEntityOriginalPosition originalPosition : locList) {
                    JSONObject locObj = new JSONObject();
                    locObj.put("latitude", originalPosition.getLatitude());
                    locObj.put("longitude", originalPosition.getLongitude());
                    locObj.put("timeoffset", originalPosition.getTimeoffset());
                    locObj.put("haccuracy", originalPosition.getHaccuracy());
                    locObj.put("latitude", originalPosition.getLatitude());
                    locObj.put("errorcode", originalPosition.getErrorcode());
                    locArray.add(locObj);
                    originalPosition.setUploadStatus(AppEnum.UploadStatus.OVER);
                }
                uploadObj.put("location", locArray);
                DBUploadEntity uploadEntity = new DBUploadEntity(DBUploadEntity.TYPE_UPLOAD_ORIGINAL_POSITION, URLConfig
                        .URL_UPLOAD_ORIGINAL_POSITION, uploadObj.toJSONString(), workout.getName(), userid);
                UploadData.createNewUploadData(context, uploadEntity);
                OriginalPositionData.updateList(context, locList);
            }
        }
    }

    /**
     * 上传历史结束信息
     *
     * @param workout
     */
    public static void finishWorkout(Context context, DBEntityWorkout workout, int userid) {
        Log.v("finishworkout", "finishWorkout");
        JSONObject uploadObj = new JSONObject();
        uploadObj.put("contenttype", "workoutend");
        uploadObj.put("users_id", workout.userId);
        uploadObj.put("starttime", workout.starttime);
        uploadObj.put("length", workout.length);
        uploadObj.put("maxheight", workout.maxHeight);
        uploadObj.put("minheight", workout.minHeight);
        uploadObj.put("maxpace", workout.maxPace);
        uploadObj.put("minpace", workout.minPace);
        uploadObj.put("avgpace", workout.avgPace);
        uploadObj.put("calorie", workout.calorie);
        uploadObj.put("minheartrate", 0);
        uploadObj.put("maxheartrate", 0);
        uploadObj.put("avgheartrate", 0);
        uploadObj.put("maxspeed", workout.maxSpeed);
        uploadObj.put("minspeed", workout.minSpeed);
        uploadObj.put("avgspeed", workout.avgSpeed);
        uploadObj.put("duration", workout.duration);
        uploadObj.put("stepcount",(workout.endStep - workout.startStep));
        DBUploadEntity uploadEntity = new DBUploadEntity(DBUploadEntity.TYPE_UPLOAD_WORKOUT, URLConfig
                .URL_SAVE_DATA_SEGMENT, uploadObj.toJSONString(), workout.getName(), userid);
        UploadData.createNewUploadData(context, uploadEntity);
    }

    /**
     * 修改跑步历史卡路里信息
     *
     * @param workout
     */
    public static void updateWorkoutCalorie(Context context, DBEntityWorkout workout, int userid) {
        JSONObject uploadObj = new JSONObject();
        uploadObj.put("contenttype", "sessiondata");
        uploadObj.put("users_id", workout.getUserId());
        uploadObj.put("starttime", workout.getStarttime());
        uploadObj.put("length", workout.getLength());
        uploadObj.put("maxheight", workout.getMaxHeight());
        uploadObj.put("minheight", workout.getMinHeight());
        uploadObj.put("maxpace", workout.getMaxPace());
        uploadObj.put("minpace", workout.getMinPace());
        uploadObj.put("avgpace", workout.getAvgPace());
        uploadObj.put("calorie", workout.getCalorie());
        uploadObj.put("minheartrate", 0);
        uploadObj.put("maxheartrate", 0);
        uploadObj.put("avgheartrate", 0);
        uploadObj.put("maxspeed", workout.getMaxSpeed());
        uploadObj.put("minspeed", workout.getMinSpeed());
        uploadObj.put("avgspeed", workout.getAvgSpeed());
        DBUploadEntity uploadEntity = new DBUploadEntity(DBUploadEntity.TYPE_UPLOAD_WORKOUT, URLConfig
                .URL_SAVE_DATA_SEGMENT, uploadObj.toJSONString(), workout.getName(), userid);
        UploadData.createNewUploadData(context, uploadEntity);
    }


    /**
     * 上传结束跑步分段数据
     *
     * @param splite
     */
    public static void finishSplite(Context context, DBEntitySplite splite, int userId) {
        JSONObject uploadObj = new JSONObject();
        uploadObj.put("contenttype", "sessiondata");
        uploadObj.put("users_id", userId);
        uploadObj.put("starttime", splite.getWorkoutName());
        JSONArray spliteArray = new JSONArray();
        JSONObject spliteObj = new JSONObject();
        spliteObj.put("id", splite.getId());
        spliteObj.put("length", splite.getLength());
        spliteObj.put("duration", splite.getDuration());
        spliteObj.put("avgheartrate", splite.getAvgheartrate());
        spliteObj.put("minaltitude", splite.getMinaltitude());
        spliteObj.put("maxaltitude", splite.getMaxaltitude());
        spliteObj.put("latitude", splite.getLatitude());
        spliteObj.put("longitude", splite.getLongitude());
        spliteArray.add(spliteObj);
        uploadObj.put("splits", spliteArray);
        DBUploadEntity uploadEntity = new DBUploadEntity(DBUploadEntity.TYPE_UPLOAD_WORKOUT, URLConfig
                .URL_SAVE_DATA_SEGMENT, uploadObj.toJSONString(), splite.getWorkoutName(), userId);
        UploadData.createNewUploadData(context, uploadEntity);
    }

    /**
     * 保存心率上传信息
     */
    public static void saveHeartrateInfo(Context context, final DBEntityWorkout workout, final DBEntityLap lap, int userId) {
        // List<DBEntityLocation> locList =
        // LocationData.getUnUploadLocation(workout.getName(),
        // lap.getStarttime());
        List<DBEntityHeartrate> heartrateList =
                HeartrateData.getUnUploadHeartrate(context, workout.getName(), lap.getLapId());
        if (heartrateList != null) {
            if (heartrateList.size() != 0) {
                JSONObject uploadObj = new JSONObject();
                uploadObj.put("contenttype", "sessiondata");
                uploadObj.put("users_id", workout.getUserId());
                uploadObj.put("starttime", workout.getStarttime());
                uploadObj.put("length", workout.getLength());
                uploadObj.put("duration", workout.getDuration());
                uploadObj.put("calorie", workout.getCalorie());
                uploadObj.put("minpace", 0);
                uploadObj.put("maxpace", 0);
                uploadObj.put("maxheight", workout.getMaxHeight());
                uploadObj.put("minheight", workout.getMinHeight());
                JSONArray lapArray = new JSONArray();
                JSONObject lapobj = new JSONObject();
                // JSONArray locArray = new JSONArray();
                JSONArray heartArray = new JSONArray();
                // 增加心率信息
                if (heartrateList != null && heartrateList.size() > 0) {
                    for (DBEntityHeartrate entity : heartrateList) {
                        JSONObject heartObj = new JSONObject();
                        heartObj.put("timeoffset", entity.getTimeofset()
                                - TimeUtils.getTimesetFromStartTime(workout.getStarttime(), lap.getStarttime()));
                        heartObj.put("bpm", entity.bmp);
                        heartObj.put("stridefrequency",entity.cadence);
                        heartArray.add(heartObj);
                        entity.setUploadStatus(AppEnum.UploadStatus.OVER);
                    }
                }

                lapobj.put("lap", lap.getLap());
                lapobj.put("starttime", lap.getStarttime());
                lapobj.put("heartratedata", heartArray);
                lapArray.add(lapobj);
                uploadObj.put("lap", lapArray);
                DBUploadEntity uploadEntity = new DBUploadEntity(DBUploadEntity.TYPE_UPLOAD_WORKOUT, URLConfig
                        .URL_SAVE_DATA_SEGMENT, uploadObj.toJSONString(), workout.getName(), userId);
                UploadData.createNewUploadData(context, uploadEntity);
                HeartrateData.updateList(context, heartrateList);
            }
        }
    }

    /**
     * 获取需要上传的数据
     *
     * @return
     */
    public synchronized static DBUploadEntity getUploadEntity(Context context, int userid) {
//        Log.v(TAG, "getUploadEntity");
        DBUploadEntity result = null;
        try {
            result = LocalApplication.getInstance().getDbUtils(context)
                    .findFirst(Selector.from(DBUploadEntity.class)
                            .where("userId", "=", userid));
        } catch (DbException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取需要上传的数据
     *
     * @return
     */
    public synchronized static DBUploadEntity getUploadWorkoutEntity(Context context, int userid) {
        DBUploadEntity result = null;
        try {
            result = LocalApplication.getInstance().getDbUtils(context)
                    .findFirst(Selector.from(DBUploadEntity.class)
                            .where("userId", "=", userid)
                            .and("uploadType", "=", DBUploadEntity.TYPE_UPLOAD_WORKOUT));
        } catch (DbException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 删除上传信息
     *
     * @param entity
     */
    public synchronized static void deleteUploadInfo(final Context context, final DBUploadEntity entity) {
        try {
            if (entity != null) {
                LocalApplication.getInstance().getDbUtils(context).delete(entity);
//                LocalApplication.getInstance().getUploadData().setChanged();
//                LocalApplication.getInstance().getUploadData().notifyObservers();
            }
        } catch (DbException e) {
            e.printStackTrace();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 上传信息是否存在
     */
    public static boolean isExist(final Context context, final DBUploadEntity entity) {
        try {
            DBUploadEntity tmpEntity = LocalApplication.getInstance().getDbUtils(context).findById(DBUploadEntity.class, entity.getId());
            if (tmpEntity != null) {
                return true;
            } else {
                return false;
            }
        } catch (DbException e) {
            e.printStackTrace();
            return false;
        }
    }
}
