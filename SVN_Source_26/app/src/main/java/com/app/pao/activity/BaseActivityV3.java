package com.app.pao.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.app.pao.ActivityStackManager;
import com.app.pao.LocalApplication;
import com.app.pao.activity.main.ClipGroupDialogActivity;
import com.app.pao.activity.main.ClipUserDialogActivity;
import com.app.pao.activity.workout.NewRecordActivityV2;
import com.app.pao.config.AppEnum;
import com.app.pao.data.db.RecordData;
import com.app.pao.data.db.WorkoutData;
import com.app.pao.entity.network.GetParseInviteTextResult;
import com.app.pao.utils.AppUtils;
import com.app.pao.utils.Log;
import com.app.pao.utils.T;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/7/18.
 */
public abstract class BaseActivityV3 extends AppCompatActivity {

    protected boolean mCheckNewData = true;
    protected Bundle savedInstanceState;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityStackManager.getAppManager().addActivity(this);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        this.savedInstanceState = savedInstanceState;
        initData();
        initViews();
        doMyCreate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        //若本页面需要检查新数据，并从后台切换到前台
        if (!LocalApplication.getInstance().isActive){
            LocalApplication.getInstance().isActive = true;
            if (mCheckNewData){
                //检查复制 ，黏贴信息
                if (LocalApplication.getInstance().getClipresult() != null) {
                    showClip();
                    return;
                }
                checkNewRecord();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!AppUtils.isAppOnForeground(getApplicationContext())) {
            //app 进入后台
            LocalApplication.getInstance().setIsActive(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        causeGC();
        ActivityStackManager.getAppManager().finishActivity(this);
    }


    protected abstract int getLayoutId();

    protected void startActivity(Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), cls);
        startActivity(intent);
    }

    protected void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), cls);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    protected void startActivityForResult(Class<?> cls, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), cls);
        startActivityForResult(intent, requestCode);
    }

    protected void startActivityForResult(Class<?> cls, Bundle bundle, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), cls);
        intent.putExtras(bundle);
        startActivityForResult(intent, requestCode);
    }

    //初始化数据
    protected abstract void initData();

    //初始化页面
    protected abstract void initViews();

    //执行初始化后的事情
    protected abstract void doMyCreate();

    //释放内存
    protected abstract void causeGC();


    /**
     * 显示黏贴信息
     */
    private void showClip() {
        GetParseInviteTextResult result = LocalApplication.getInstance().getClipresult();
        LocalApplication.getInstance().setClipresult(null);
        Intent intent = new Intent();
        Bundle b = new Bundle();
        b.putSerializable("data", result);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtras(b);
        //若类型是好友
        if (result.getType() == AppEnum.ClipType.USER) {
            intent.setClass(getApplicationContext(), ClipUserDialogActivity.class);
            startActivity(intent);
            //若类型是团
        } else if (result.getType() == AppEnum.ClipType.GROUP) {
            intent.setClass(getApplicationContext(), ClipGroupDialogActivity.class);
            startActivity(intent);
        }
    }

    /**
     * 检查是否有新记录
     */
    private void checkNewRecord() {
        //检查是否有新记录信息
        int userId = LocalApplication.getInstance().getLoginUser(getApplicationContext()).userId;
        //若有新记录，且不在跑步
        if (RecordData.getRecords(getApplicationContext(), userId).size() > 0
                && WorkoutData.getUnFinishWorkout(getApplicationContext(), userId) == null) {
            T.showLong(getApplicationContext(), "有新纪录产生");
            startActivity(NewRecordActivityV2.class);
        }
    }

}
