package com.app.pao.activity.settings;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.activity.workout.HistoryInfoActivityV2;
import com.app.pao.config.AppEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.data.db.UploadData;
import com.app.pao.data.db.UserData;
import com.app.pao.data.db.WorkoutData;
import com.app.pao.entity.db.DBEntityWorkout;
import com.app.pao.entity.db.DBUserEntity;
import com.app.pao.network.api.MyRequestCallBack;
import com.app.pao.network.utils.RequestParamsBuild;
import com.app.pao.ui.dialog.v1.MyDialogBuilderV1;
import com.app.pao.utils.CalorieUtils;
import com.app.pao.utils.T;
import com.app.pao.utils.TimeUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.rey.material.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import hwh.com.pickviewlibrary.OptionsPickerView;
import hwh.com.pickviewlibrary.TimePickerView;

/**
 * Created by Raul on 2015/12/13.
 * 跑步后显示完善资料
 */
@ContentView(R.layout.activity_perfect_user_after_run)
public class PerfectUserInfoAfterRunningActivtiy extends BaseAppCompActivity implements View.OnClickListener {

    /* contains */
    private static final String TAG = "PerfectUserInfoAfterRunningActivtiy";


    private static final int REQUEST_EDIT_GENDER = 0x04;


    private static final int MSG_POST_ERROR = 1;//上传出错
    private static final int MSG_POST_PHOTO_OK = 2;//上传头像成功
    private static final int MSG_POST_BIRTHDAY_OK = 4;//上传生日成功
    private static final int MSG_POST_HEIGHT_OK = 5;//上传身高成功
    private static final int MSG_POST_WEIGHT_OK = 6;//上传体重成功


    /* local view */
    @ViewInject(R.id.toolbar)
    private Toolbar mToolbar;//标题栏
    @ViewInject(R.id.tv_gander)
    private TextView mSexTv;//性别
    @ViewInject(R.id.tv_birthday)
    private TextView mBirthdayTv;//生日
    @ViewInject(R.id.tv_height)
    private TextView mHeightTv;//身高
    @ViewInject(R.id.tv_weight)
    private TextView mWeightTv;//体重

    private TimePickerView mBirthdayPick;//生日设置框
    private OptionsPickerView mSetPick; //身高体重设置框

    /* local data */
    private DBUserEntity mUserEntity;//用户信息
    private boolean mPostAble;//是否可以上传信息

    private String mWorkoutName;

    private int gander;//性别
    private String birthday;//生日
    private int weight;//体重
    private int height;//身高

    /**
     * 点击事件
     *
     * @param v
     */
    @Override
    @OnClick({R.id.ll_edit_gander, R.id.ll_edit_birthday, R.id.ll_edit_height, R.id.ll_edit_weight, R.id.btn_finish})
    public void onClick(View v) {
        switch (v.getId()) {
            //编辑性别
            case R.id.ll_edit_gander:
                changeSex();
                break;
            //编辑生日
            case R.id.ll_edit_birthday:
                changeBirthday();
                break;
            //编辑身高
            case R.id.ll_edit_height:
                changeHeight();
                break;
            //编辑体重
            case R.id.ll_edit_weight:
                changeWeight();
                break;
            //完成
            case R.id.btn_finish:
                checkUserInfo();
                break;
        }
    }


    /**
     * 请求Handler
     */
    Handler mPostHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            mPostAble = true;
            mDialogBuilder.progressDialog.dismiss();

            super.handleMessage(msg);
            switch (msg.what) {
                //上传出现错误
                case MSG_POST_ERROR:
                    T.showLong(mContext, (String) msg.obj);
                    break;
                //上传生日
                case MSG_POST_BIRTHDAY_OK:
                    mBirthdayTv.setText(mUserEntity.getBirthday());
                    break;
                //上传身高
                case MSG_POST_HEIGHT_OK:
                    mHeightTv.setText(mUserEntity.getHeight() + "厘米");
                    break;
                //上传体重
                case MSG_POST_WEIGHT_OK:
                    mWeightTv.setText(mUserEntity.getWeight() + "公斤");
                    break;
            }
        }
    };

    /**
     * on Activity result
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_EDIT_GENDER) {
            if (resultCode == RESULT_OK) {
                //修改姓名成功
                mUserEntity = LocalApplication.getInstance().getLoginUser(mContext);
                if (mUserEntity.getGender() == AppEnum.UserGander.WOMEN) {
                    mSexTv.setText(mContext.getResources().getString(R.string.Gander_Women));
                } else {
                    mSexTv.setText(mContext.getResources().getString(R.string.Gander_Man));
                }
            }
        }
    }

    @Override
    protected void initData() {
        mPostAble = true;
        mUserEntity = LocalApplication.getInstance().getLoginUser(mContext);
        mWorkoutName = getIntent().getExtras().getString("workoutname");

        //获取当前年份
        Calendar calendar = Calendar.getInstance();
        int curYear = calendar.get(Calendar.YEAR);

        //初始化生日选择器
        mBirthdayPick = new TimePickerView(mContext, TimePickerView.Type.YEAR_MONTH_DAY, 1950, curYear);
        //初始化体重身高设置选择器
        mSetPick = new OptionsPickerView(mContext);
    }

    @Override
    protected void initViews() {
        setSupportActionBar(mToolbar);

        if (mUserEntity.getGender() == AppEnum.UserGander.WOMEN) {
            mSexTv.setText(mContext.getResources().getString(R.string.Gander_Women));
        } else {
            mSexTv.setText(mContext.getResources().getString(R.string.Gander_Man));
        }
        mBirthdayTv.setText(mUserEntity.getBirthday());
        mHeightTv.setText(mUserEntity.getHeight() + "厘米");
        mWeightTv.setText(mUserEntity.getWeight() + "公斤");
    }

    @Override
    protected void doMyOnCreate() {

    }

    @Override
    protected void updateData() {

    }

    @Override
    protected void updateViews() {

    }

    @Override
    protected void destroy() {

    }

    /**
     * 修改性别
     */
    private void changeSex() {
        startActivityForResult(EditUserGenderActivity.class, REQUEST_EDIT_GENDER);
    }

    /**
     * 设置生日
     */
    private void changeBirthday() {

        //设置时间选择器的默认参数
        mBirthdayPick.setTime(TimeUtils.stringToDate(mUserEntity.getBirthday()));

        //设置选择器是否可以循环滚动
        mBirthdayPick.setCyclic(false);
        //设置选择时间回调
        mBirthdayPick.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                //开始向服务器发送更新请求
                postUpdateBirthday(TimeUtils.dateToString(date));
            }
        });
        mBirthdayPick.show();
    }

    /**
     * 设置身高
     */
    private void changeHeight() {
        int defaultHeight = mUserEntity.getHeight();
        final ArrayList<Integer> options1Items = new ArrayList<Integer>();
        //选择器默认位置值
        int options1Pos = 0;
        //插值
        for (int i = 100; i <= 300; i++) {
            options1Items.add(i);
            //若值相同则为默认值
            if (i == defaultHeight) {
                options1Pos = i - 100;
            }
        }
        showSetPick("身高设置", "厘米", options1Pos, options1Items, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                postUpdateHeight(options1Items.get(options1));
            }
        });

    }

    /**
     * 设置体重
     */
    private void changeWeight() {
        int defaultWeight = mUserEntity.getWeight();
        final ArrayList<Integer> options1Items = new ArrayList<Integer>();
        //选择器默认位置值
        int options1Pos = 0;
        //插值
        for (int i = 35; i <= 300; i++) {
            options1Items.add(i);
            //若值相同则为默认值
            if (i == defaultWeight) {
                options1Pos = i - 35;
            }
        }

        showSetPick("体重设置", "公斤", options1Pos, options1Items, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                postUpdateWeight(options1Items.get(options1));
            }
        });
    }

    /**
     * 检查用户信息
     */
    private void checkUserInfo() {
        //检查身高
        if (mUserEntity.getHeight() == 0) {
            T.showShort(mContext, "身高不能为0");
            return;
        }
        //检查体重
        if (mUserEntity.getWeight() == 0) {
            T.showShort(mContext, "体重不能为0");
            return;
        }
        //修改跑步历史
        DBEntityWorkout mDBWorkout = WorkoutData.getWorkoutByName(mContext,mWorkoutName);
        if (mDBWorkout != null) {
            mDBWorkout.setCalorie(CalorieUtils.getCalorie(mContext,0, mDBWorkout.getDuration(), mDBWorkout.getLength() * 1.0f));
            WorkoutData.updateWorkout(mContext, mDBWorkout);
            UploadData.updateWorkoutCalorie(mContext, mDBWorkout, LocalApplication.getInstance().getLoginUser(mContext).getUserId());
            Bundle bundle = new Bundle();
            bundle.putString("workoutname", mWorkoutName);
            startActivity(HistoryInfoActivityV2.class, bundle);
        }
        finish();
    }

    /**
     * 显示Height 或 Weight 设置框
     *
     * @param title          标题
     * @param selectListener 监听回调事件
     */
    private void showSetPick(final String title, final String labels, final int position, final ArrayList<Integer>
            options1Items, OptionsPickerView.OnOptionsSelectListener selectListener) {

        //初始化选择器数据
        mSetPick.setPicker(options1Items);
        //设置选择器是否无限滚动
        mSetPick.setCyclic(false);
        //设置选择器标题
        mSetPick.setTitle(title);

        mSetPick.setLabels(labels);
        //设置选择器默认值
        mSetPick.setSelectOptions(position);
        //选择器回调
        mSetPick.setOnoptionsSelectListener(selectListener);
        mSetPick.show();
    }

    /**
     * 修改用户生日
     *
     * @param birthday
     */
    private void postUpdateBirthday(final String birthday) {
        if (!mPostAble) {
            return;
        }
        showProgressDialog("正在更新···");
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_UPDATE_USER_INFO;
        RequestParams params = RequestParamsBuild.buildResetUserBirthdayRequest(mContext,birthday);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                if (mPostHandler != null) {
                    Message mPostMsg = new Message();
                    mPostMsg.what = MSG_POST_ERROR;
                    mPostMsg.obj = errorMsg;
                    mPostHandler.sendMessage(mPostMsg);
                }
            }

            @Override
            protected void onRightResponse(String Response) {
                mUserEntity.setBirthday(birthday);
                UserData.updateUser(mContext, mUserEntity);
                LocalApplication.getInstance().setLoginUser(mUserEntity);
                if (mPostHandler != null) {
                    Message mPostMsg = new Message();
                    mPostMsg.what = MSG_POST_BIRTHDAY_OK;
                    mPostHandler.sendMessage(mPostMsg);
                }
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                if (mPostHandler != null) {
                    Message mPostMsg = new Message();
                    mPostMsg.what = MSG_POST_ERROR;
                    mPostMsg.obj = s;
                    mPostHandler.sendMessage(mPostMsg);
                }
            }

            @Override
            protected void onFinish() {

            }
        });
    }

    /**
     * 修改用户身高
     *
     * @param height
     */
    private void postUpdateHeight(final int height) {
        if (!mPostAble) {
            return;
        }
        showProgressDialog("正在更新···");
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_UPDATE_USER_INFO;
        RequestParams params = RequestParamsBuild.buildResetUserHeightRequest(mContext,height);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                if (mPostHandler != null) {
                    Message mPostMsg = new Message();
                    mPostMsg.what = MSG_POST_ERROR;
                    mPostMsg.obj = errorMsg;
                    mPostHandler.sendMessage(mPostMsg);
                }
            }

            @Override
            protected void onRightResponse(String Response) {
                mUserEntity.setHeight(height);
                UserData.updateUser(mContext, mUserEntity);
                LocalApplication.getInstance().setLoginUser(mUserEntity);
                if (mPostHandler != null) {
                    Message mPostMsg = new Message();
                    mPostMsg.what = MSG_POST_HEIGHT_OK;
                    mPostHandler.sendMessage(mPostMsg);
                }
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                if (mPostHandler != null) {
                    Message mPostMsg = new Message();
                    mPostMsg.what = MSG_POST_ERROR;
                    mPostMsg.obj = s;
                    mPostHandler.sendMessage(mPostMsg);
                }
            }

            @Override
            protected void onFinish() {

            }
        });
    }

    /**
     * 修改用户体重
     *
     * @param weight
     */
    private void postUpdateWeight(final int weight) {
        if (!mPostAble) {
            return;
        }
        showProgressDialog("正在更新···");
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_UPDATE_USER_INFO;
        RequestParams params = RequestParamsBuild.buildResetUserWeightRequest(mContext,weight);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                if (mPostHandler != null) {
                    Message mPostMsg = new Message();
                    mPostMsg.what = MSG_POST_ERROR;
                    mPostMsg.obj = errorMsg;
                    mPostHandler.sendMessage(mPostMsg);
                }
            }

            @Override
            protected void onRightResponse(String Response) {
                mUserEntity.setWeight(weight);
                UserData.updateUser(mContext, mUserEntity);
                LocalApplication.getInstance().setLoginUser(mUserEntity);
                if (mPostHandler != null) {
                    Message mPostMsg = new Message();
                    mPostMsg.what = MSG_POST_WEIGHT_OK;
                    mPostHandler.sendMessage(mPostMsg);
                }
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                if (mPostHandler != null) {
                    Message mPostMsg = new Message();
                    mPostMsg.what = MSG_POST_ERROR;
                    mPostMsg.obj = s;
                    mPostHandler.sendMessage(mPostMsg);
                }
            }

            @Override
            protected void onFinish() {

            }
        });
    }


    //显示等待信息
    private void showProgressDialog(String msg) {
        mDialogBuilder.showProgressDialog(this, msg, true);
        mDialogBuilder.setListener(new MyDialogBuilderV1.ProgressDilaogListener() {
            @Override
            public void onCancelBtnClick() {
                mPostAble = true;
                cancelPostHandler();
            }

            @Override
            public void onCancel() {
                mPostAble = true;
                cancelPostHandler();
            }
        });
    }


    /**
     * 取消请求
     */
    private void cancelPostHandler() {
        if (mPostHandler != null) {
            mPostHandler.removeMessages(MSG_POST_PHOTO_OK);
            mPostHandler.removeMessages(MSG_POST_ERROR);
        }
    }
}
