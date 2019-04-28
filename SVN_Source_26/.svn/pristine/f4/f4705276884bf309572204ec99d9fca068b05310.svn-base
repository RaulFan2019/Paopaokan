package com.app.pao.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;

import com.app.pao.LocalApplication;
import com.app.pao.activity.workout.NewRecordActivityV2;
import com.app.pao.config.URLConfig;
import com.app.pao.data.db.RecordData;
import com.app.pao.data.db.UploadData;
import com.app.pao.data.db.WorkoutData;
import com.app.pao.entity.db.DBEntityWorkout;
import com.app.pao.entity.db.DBUploadEntity;
import com.app.pao.entity.db.DBUserEntity;
import com.app.pao.entity.event.EventNewUploadData;
import com.app.pao.entity.event.EventNewWorkout;
import com.app.pao.entity.event.EventRecord;
import com.app.pao.entity.event.EventRunningComment;
import com.app.pao.entity.event.EventRunningThumb;
import com.app.pao.entity.network.GetRunningUploadResult;
import com.app.pao.network.api.MyRequestCallBack;
import com.app.pao.network.utils.RequestParamsBuild;
import com.app.pao.utils.NetworkUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raul on 2015/10/29.
 * 监听需要上传的数据的后台服务
 */
public class UploadWatcherService extends Service {

    /* contains */
    private static final String TAG = "UploadWatcherService";

    public static final String UPLOAD_NEW_WORKOUT_ACTION = "com.app.pao.upload.workout.new";

    public static final int MSG_UPLOAD = 0x01;

    /* local data */
    private long delayTime = 5000;
    private static final long DelayTimeMax = 60 * 1000 * 5;

    private ConnectionChangeReceiver myReceiver;

    private int mRepeatTime = 0;//重试次数
    private boolean uploading = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        Log.v(TAG, "onCreate");
        startUpload();
        registerReceiver();
        EventBus.getDefault().register(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        stopUpload();
        unregisterReceiver();
        super.onDestroy();
    }

    Handler uploadHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_UPLOAD) {
                //若正在上传
                if (uploading) {
                    return;
                }
                uploading = true;
                // 若网络不好, 过段时间重试
                if (!NetworkUtils.isNetworkConnected(UploadWatcherService.this)) {
                    return;
                }
                if (LocalApplication.getInstance().getLoginUser(UploadWatcherService.this) == null) {
                    return;
                }
                // 若需要上传的数据是空, 重新获取数据
                DBUploadEntity uploadEntity = UploadData.getUploadEntity(UploadWatcherService.this,
                        LocalApplication.getInstance().getLoginUser(UploadWatcherService.this).userId);
                if (uploadEntity != null) {
                    postData(uploadEntity);
                } else {
                    uploading = false;
                    return;
                }
            }
        }
    };

    /**
     * 收到新上传的数据
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EventNewUploadData event) {
        startUpload();
    }


    /**
     * 开始上传线程
     */
    private void startUpload() {
        // 启动计时线程，开始上传
        uploadHandler.sendEmptyMessage(MSG_UPLOAD);
    }

    private void stopUpload() {
        if (uploadHandler != null) {
            uploadHandler.removeMessages(MSG_UPLOAD);
        }
    }

    private void registerReceiver() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        myReceiver = new ConnectionChangeReceiver();
        this.registerReceiver(myReceiver, filter);
    }

    private void unregisterReceiver() {
        this.unregisterReceiver(myReceiver);
    }

    /**
     * 发送数据
     */
    private void postData(final DBUploadEntity uploadEntity) {
//        Log.v(TAG,"postData uploadEntity.getUploadType():" + uploadEntity.getUploadType());
        //若上传的数据是跑步相关
        if (uploadEntity.getUploadType() == DBUploadEntity.TYPE_UPLOAD_WORKOUT) {
            postWorkout(uploadEntity);
            //删除跑步相关
        } else if (uploadEntity.getUploadType() == DBUploadEntity.TYPE_DELETE_WORTOUT) {
            postDeleteWorkout(uploadEntity);
            //点赞
        } else if (uploadEntity.getUploadType() == DBUploadEntity.TYPE_THUMB_UP) {
            postThumbUp(uploadEntity);
            //消息处理
        } else if (uploadEntity.getUploadType() == DBUploadEntity.TYPE_READ_MESSAGE) {
            postReadMessage(uploadEntity);
            //动态点赞
        } else if (uploadEntity.getUploadType() == DBUploadEntity.TYPE_THUMB_UP_YES) {
            postReadThumbYes(uploadEntity);
            //动态取消点赞
        } else if (uploadEntity.getUploadType() == DBUploadEntity.TYPE_THUMB_UP_NO) {
            postReadThumbNo(uploadEntity);
            //上传评论
        } else if (uploadEntity.getUploadType() == DBUploadEntity.TYPE_COMMENTS) {
            postSendComments(uploadEntity);
            //上传原始数据
        } else if (uploadEntity.getUploadType() == DBUploadEntity.TYPE_UPLOAD_ORIGINAL_POSITION) {
            postOgiginalPosition(uploadEntity);
        }
    }


    /**
     * 上传跑步信息
     */
    private void postWorkout(final DBUploadEntity uploadEntity) {
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_SAVE_DATA_SEGMENT;
        RequestParams params = RequestParamsBuild.saveWorkoutSegment(UploadWatcherService.this, uploadEntity.info);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(this) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                mRepeatTime++;
                delayTime += 1000;
                if (delayTime > DelayTimeMax) {
                    delayTime = DelayTimeMax;
                }
                if (mRepeatTime > 5) {
                    UploadData.deleteUploadInfo(UploadWatcherService.this, uploadEntity);
                }
            }

            @Override
            protected void onRightResponse(String Response) {
                mRepeatTime = 0;
                delayTime = 100;
                UploadData.deleteUploadInfo(UploadWatcherService.this, uploadEntity);
                GetRunningUploadResult resultEntity = com.alibaba.fastjson.JSONObject.parseObject(Response,
                        GetRunningUploadResult.class);
                //是否是新的Workout
                if (resultEntity.getNewWorkout() == 1) {
                    DBEntityWorkout mDBWorkout = WorkoutData.getWorkoutByName(UploadWatcherService.this,
                            uploadEntity.getWorkoutName());
                    if (mDBWorkout != null) {
                        mDBWorkout.setWorkoutId(resultEntity.getId());
                        WorkoutData.updateWorkout(UploadWatcherService.this, mDBWorkout);
//                        Intent i = new Intent(UPLOAD_NEW_WORKOUT_ACTION);
//                        Bundle b = new Bundle();
//                        b.putSerializable("event", new EventNewWorkout(resultEntity.id, uploadEntity.workoutName));
//                        i.putExtras(b);
//                        sendBroadcast(i);
                        EventBus.getDefault().post(new EventNewWorkout(resultEntity.id, uploadEntity.workoutName));
                    }
                }
                //是否有点赞信息信息
                if (resultEntity.getThumbupcount() > 0) {
                    EventBus.getDefault().post(new EventRunningThumb());
                }
                //是否有语音信息
                if (resultEntity.getComments() != null && resultEntity.getComments().size() > 0) {
                    EventBus.getDefault().post(new EventRunningComment(resultEntity.getComments()));
                }
                //如果是跑步结束
                if (resultEntity.getIsworkoutend() == 1) {
                    EventBus.getDefault().post(new EventRecord());
                    DBUserEntity userEntity = LocalApplication.getInstance().getLoginUser(UploadWatcherService.this);
                    try {
                        //获取本次跑步的长度
//                        org.json.JSONObject object = new org.json.JSONObject(uploadEntity.info);
//                        float workoutLength = Float.parseFloat(object.getString("length"));
                        List<GetRunningUploadResult.NewrecordEntity> recordList = new ArrayList<GetRunningUploadResult.NewrecordEntity>();
//                        GetRunningUploadResult.NewrecordEntity length = new GetRunningUploadResult.NewrecordEntity(
//                                AppEnum.NewRecordType.TotalLengthChange, (int) resultEntity.getTotallength(),
//                                (int) (resultEntity.getTotallength() - workoutLength), 0, 0);
//                        recordList.add(length);
                        //是否有Record 信息
                        if (resultEntity.getNewrecord() != null && resultEntity.getNewrecord().size() > 0) {
                            recordList.addAll(resultEntity.getNewrecord());
                        }
                        //若记录有更新
                        if (recordList.size() > 0
//                                || NumUtils.getTotalLengthLevel(length.getRecord()) > NumUtils.getTotalLengthLevel(length.getPrerecord())
                                ) {
                            RecordData.saveRecord(UploadWatcherService.this, userEntity.userId, recordList);
                            //app在前台，并且没在跑步跳转到新记录页面
                            if (LocalApplication.getInstance().isActive()
                                    && WorkoutData.getUnFinishWorkout(UploadWatcherService.this, userEntity.userId) == null) {
                                Intent intent = new Intent(UploadWatcherService.this, NewRecordActivityV2.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
//                        } else {
//                            EventBus.getDefault().post(new EventRecord(false));
                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
                    } catch (NumberFormatException ex) {
                        ex.printStackTrace();
                    }
                }
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                delayTime += 1000;
                if (delayTime > DelayTimeMax) {
                    delayTime = DelayTimeMax;
                }
            }

            @Override
            protected void onFinish() {
                uploading = false;
                uploadHandler.sendEmptyMessageDelayed(MSG_UPLOAD, delayTime);
            }
        });
    }


    /**
     * 上传点攒
     */
    private void postThumbUp(final DBUploadEntity uploadEntity) {
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_THUMB_UP_TO_WORKOUT;
        RequestParams params = RequestParamsBuild.thumbUpToWorkoutRequest(UploadWatcherService.this, uploadEntity.info);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(this) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                delayTime += 1000;
                if (delayTime > DelayTimeMax) {
                    delayTime = DelayTimeMax;
                }
            }

            @Override
            protected void onRightResponse(String Response) {
                delayTime = 100;
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                delayTime += 1000;
                if (delayTime > DelayTimeMax) {
                    delayTime = DelayTimeMax;
                }
            }

            @Override
            protected void onFinish() {
                uploading = false;
                UploadData.deleteUploadInfo(UploadWatcherService.this, uploadEntity);
                uploadHandler.sendEmptyMessageDelayed(MSG_UPLOAD, delayTime);
            }
        });
    }

    /**
     * 上传删除跑步信息
     */
    private void postDeleteWorkout(final DBUploadEntity uploadEntity) {
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_REMOVE_WORKOUT_INFO;
        RequestParams params = RequestParamsBuild.removeWorkout(UploadWatcherService.this, uploadEntity.workoutName);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(this) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                delayTime += 1000;
                if (delayTime > DelayTimeMax) {
                    delayTime = DelayTimeMax;
                }
            }

            @Override
            protected void onRightResponse(String Response) {
                delayTime = 100;
                UploadData.deleteUploadInfo(UploadWatcherService.this, uploadEntity);
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                delayTime += 1000;
                if (delayTime > DelayTimeMax) {
                    delayTime = DelayTimeMax;
                }
            }

            @Override
            protected void onFinish() {
                uploading = false;
                uploadHandler.sendEmptyMessageDelayed(MSG_UPLOAD, delayTime);
                ;
            }
        });
    }

    /**
     * 发送点赞
     */
    private void postReadThumbYes(final DBUploadEntity uploadEntity) {
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_THUMB_UP_YES;
        String[] info = uploadEntity.getInfo().split(":");
        RequestParams params = RequestParamsBuild.thumbUpYesRequest(UploadWatcherService.this, info[0], info[1]);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(this) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                UploadData.deleteUploadInfo(UploadWatcherService.this, uploadEntity);
                delayTime += 1000;
                if (delayTime > DelayTimeMax) {
                    delayTime = DelayTimeMax;
                }
            }

            @Override
            protected void onRightResponse(String Response) {
                UploadData.deleteUploadInfo(UploadWatcherService.this, uploadEntity);
                delayTime = 100;
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                delayTime += 1000;
                if (delayTime > DelayTimeMax) {
                    delayTime = DelayTimeMax;
                }
            }

            @Override
            protected void onFinish() {
                uploading = false;
                uploadHandler.sendEmptyMessageDelayed(MSG_UPLOAD, delayTime);
                ;
            }
        });
    }


    /**
     * 上传取消点赞
     */
    private void postReadThumbNo(final DBUploadEntity uploadEntity) {
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_THUMB_UP_NO;
        String[] info = uploadEntity.getInfo().split(":");
        RequestParams params = RequestParamsBuild.thumbUpNoRequest(UploadWatcherService.this, info[0], info[1]);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(this) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                UploadData.deleteUploadInfo(UploadWatcherService.this, uploadEntity);
                delayTime += 1000;
                if (delayTime > DelayTimeMax) {
                    delayTime = DelayTimeMax;
                }
            }

            @Override
            protected void onRightResponse(String Response) {
                UploadData.deleteUploadInfo(UploadWatcherService.this, uploadEntity);
                delayTime = 100;
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                delayTime += 1000;
                if (delayTime > DelayTimeMax) {
                    delayTime = DelayTimeMax;
                }
            }

            @Override
            protected void onFinish() {
                uploading = false;
                uploadHandler.sendEmptyMessageDelayed(MSG_UPLOAD, delayTime);
                ;
            }
        });
    }


    /**
     * 上传评论
     */
    private void postSendComments(final DBUploadEntity uploadEntity) {
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_ADD_COMMENT;
        String[] info = uploadEntity.getInfo().split(":");
        RequestParams params = RequestParamsBuild.addCommentsRequest(UploadWatcherService.this, info[0], info[1], info[2], info[3]);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(this) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                UploadData.deleteUploadInfo(UploadWatcherService.this, uploadEntity);
                delayTime += 1000;
                if (delayTime > DelayTimeMax) {
                    delayTime = DelayTimeMax;
                }
            }

            @Override
            protected void onRightResponse(String Response) {
                UploadData.deleteUploadInfo(UploadWatcherService.this, uploadEntity);
                delayTime = 100;
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                delayTime += 1000;
                if (delayTime > DelayTimeMax) {
                    delayTime = DelayTimeMax;
                }
            }

            @Override
            protected void onFinish() {
                uploading = false;
                uploadHandler.sendEmptyMessageDelayed(MSG_UPLOAD, delayTime);
            }
        });
    }


    /**
     * 上传原始数据点
     */
    private void postOgiginalPosition(final DBUploadEntity uploadEntity) {
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_UPLOAD_ORIGINAL_POSITION;
        RequestParams params = RequestParamsBuild.uploadOgiginalPosition(UploadWatcherService.this, uploadEntity.info);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(this) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                UploadData.deleteUploadInfo(UploadWatcherService.this, uploadEntity);
                delayTime += 1000;
                if (delayTime > DelayTimeMax) {
                    delayTime = DelayTimeMax;
                }
            }

            @Override
            protected void onRightResponse(String Response) {
                UploadData.deleteUploadInfo(UploadWatcherService.this, uploadEntity);
                delayTime = 100;
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                delayTime += 1000;
                if (delayTime > DelayTimeMax) {
                    delayTime = DelayTimeMax;
                }
            }

            @Override
            protected void onFinish() {
                uploading = false;
                uploadHandler.sendEmptyMessageDelayed(MSG_UPLOAD, delayTime);
                ;
            }
        });
    }


    /**
     * 上传已读消息
     */
    private void postReadMessage(final DBUploadEntity uploadEntity) {
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_READ_MESSAGE;
        RequestParams params = RequestParamsBuild.readMessageRequest(UploadWatcherService.this, Integer.parseInt(uploadEntity.info));
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(this) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                UploadData.deleteUploadInfo(UploadWatcherService.this, uploadEntity);
                delayTime += 1000;
                if (delayTime > DelayTimeMax) {
                    delayTime = DelayTimeMax;
                }
            }

            @Override
            protected void onRightResponse(String Response) {
                UploadData.deleteUploadInfo(UploadWatcherService.this, uploadEntity);
                delayTime = 100;
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                delayTime += 1000;
                if (delayTime > DelayTimeMax) {
                    delayTime = DelayTimeMax;
                }
            }

            @Override
            protected void onFinish() {
                uploading = false;
                uploadHandler.sendEmptyMessageDelayed(MSG_UPLOAD, delayTime);
                ;
            }
        });
    }

    /**
     * @author Javen
     */
    public class ConnectionChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo wifiNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
                //改变背景或者 处理网络的全局变量
            } else {
                uploading = false;
                uploadHandler.sendEmptyMessage(MSG_UPLOAD);
                //改变背景或者 处理网络的全局变量
            }
        }
    }
}
