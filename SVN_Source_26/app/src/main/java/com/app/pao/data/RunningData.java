package com.app.pao.data;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.app.pao.config.AppEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.data.db.LapData;
import com.app.pao.data.db.LocationData;
import com.app.pao.data.db.SpliteData;
import com.app.pao.data.db.UploadData;
import com.app.pao.data.db.WorkoutData;
import com.app.pao.entity.db.DBEntityLap;
import com.app.pao.entity.db.DBEntityLocation;
import com.app.pao.entity.db.DBEntitySplite;
import com.app.pao.entity.db.DBEntityWorkout;
import com.app.pao.entity.db.DBUploadEntity;
import com.app.pao.entity.db.DBUserEntity;
import com.app.pao.entity.network.GetDynamicListResult;
import com.app.pao.utils.T;
import com.app.pao.utils.TimeUtils;

import java.util.List;

/**
 * Created by Raul on 2015/11/23.
 * 跑步相关的逻辑数据处理
 */
public class RunningData {

    /* contains */
    private static final String TAG = "RunningData";


    /**
     * 创建一个新的跑步历史
     */
    public static DBEntityWorkout createNewWorkout(Context context, final int userId) {
        Log.v(TAG, "createNewWorkout");
        if (WorkoutData.getUnFinishWorkout(context, userId) != null) {
            return WorkoutData.getUnFinishWorkout(context, userId);
        }
        // careate new workout
        String startTime = TimeUtils.NowTime();
        DBEntityWorkout workout = new DBEntityWorkout(System.currentTimeMillis(), startTime, startTime, -1, AppEnum
                .RunningStatus.RUNNING, 0, 0, 0, 0d, 0d, 0, 0, 0, 0, 0, 0, 0, 0, 0, userId, -1, -1);
        WorkoutData.saveWorkout(context, workout);
        // 如果用户不是游客创建上传信息
        if (userId != AppEnum.DEFAULT_USER_ID) {
            JSONObject uploadObj = new JSONObject();
            uploadObj.put("contenttype", "workouthead");
            uploadObj.put("users_id", userId);
            uploadObj.put("starttime", workout.getStarttime());
            uploadObj.put("type", 1);
            DBUploadEntity uploadEntity = new DBUploadEntity(DBUploadEntity.TYPE_UPLOAD_WORKOUT, URLConfig
                    .URL_SAVE_DATA_SEGMENT, uploadObj.toJSONString(), workout.getName(), userId);
            UploadData.createNewUploadData(context, uploadEntity);
        }
        return workout;
    }

    /**
     * 建立新的lap信息
     */
    public static DBEntityLap createNewLap(Context context, final String workoutStartTime, final int userId) {
        // Log.v(TAG, "建立新的lap信息");
        String startTime = TimeUtils.NowTime();
        long lap = LapData.getLapIndexFromWorkout(context, workoutStartTime);
//        Log.v(TAG,"lap:" + lap);
        DBEntityLap mDBLap = new DBEntityLap(System.currentTimeMillis(), startTime, workoutStartTime, 0, lap, 0,
                AppEnum.RunningStatus.RUNNING);
        LapData.saveLap(context, mDBLap);
        // 若跑步的人不是游客,创建上传信息
        if (userId != AppEnum.DEFAULT_USER_ID) {
            JSONObject uploadObj = new JSONObject();
            JSONArray lapArray = new JSONArray();
            JSONObject lapObj = new JSONObject();
            lapObj.put("starttime", mDBLap.getStarttime());
            lapObj.put("lap", mDBLap.getLap());
            lapArray.add(lapObj);
            uploadObj.put("contenttype", "sessionhead");
            uploadObj.put("users_id", userId);
            uploadObj.put("starttime", workoutStartTime);
            uploadObj.put("lap", lapArray);
            DBUploadEntity uploadEntity = new DBUploadEntity(DBUploadEntity.TYPE_UPLOAD_WORKOUT, URLConfig
                    .URL_SAVE_DATA_SEGMENT, uploadObj.toJSONString(), workoutStartTime, userId);
            UploadData.createNewUploadData(context, uploadEntity);
        }
        return mDBLap;
    }


    /**
     * 创建新的splite信息
     */
    public static DBEntitySplite createNewSplit(final Context context, final float mRunningLength, final String workoutStartTime) {
        // Log.v(TAG, "创建新的splite信息");
        DBEntitySplite mDBSplit = new DBEntitySplite(System.currentTimeMillis(), (int) (mRunningLength / 1000),
                workoutStartTime, 0, 0, 0, 0.00d, 0.00d, AppEnum.RunningStatus.RUNNING, 0, 0);
        SpliteData.saveSplite(context, mDBSplit);
        return mDBSplit;
    }

    /**
     * 保存结束LAP的数据
     */
    public static void FinishLap(final Context mContext, final int userId,
                                 final String startTime, final DBEntityLap mDBLap) {
        List<DBEntityLocation> locList = LocationData.getUnUploadLocation(mContext, startTime, mDBLap.getStarttime());
        JSONObject uploadObj = new JSONObject();
        uploadObj.put("contenttype", "sessionend");
        uploadObj.put("users_id", userId);
        uploadObj.put("starttime", startTime);
        JSONArray lapArray = new JSONArray();
        JSONObject lapObj = new JSONObject();
        lapObj.put("starttime", mDBLap.getStarttime());
        lapObj.put("lap", mDBLap.getLap());
        lapObj.put("duration", mDBLap.getDuration());
        lapObj.put("length", mDBLap.getLength());
        JSONArray locArray = new JSONArray();
        // JSONArray heartArray = new JSONArray();
        if (locList != null) {
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
        }
        lapObj.put("locationdata", locArray);
        lapArray.add(lapObj);
        uploadObj.put("lap", lapArray);
        LocationData.updateList(mContext, locList);
        DBUploadEntity uploadEntity = new DBUploadEntity(DBUploadEntity.TYPE_UPLOAD_WORKOUT, URLConfig
                .URL_SAVE_DATA_SEGMENT, uploadObj.toJSONString(), startTime, userId);
        UploadData.createNewUploadData(mContext, uploadEntity);
    }


}
