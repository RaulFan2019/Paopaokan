package com.app.pao.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;

import com.app.pao.ActivityStackManager;
import com.app.pao.LocalApplication;
import com.app.pao.activity.main.ClipGroupDialogActivity;
import com.app.pao.activity.main.ClipUserDialogActivity;
import com.app.pao.activity.workout.NewRecordActivityV2;
import com.app.pao.config.AppEnum;
import com.app.pao.data.db.RecordData;
import com.app.pao.data.db.WorkoutData;
import com.app.pao.entity.network.GetParseInviteTextResult;
import com.app.pao.ui.dialog.v1.MyDialogBuilderV1;
import com.app.pao.utils.AppUtils;
import com.app.pao.utils.Log;
import com.app.pao.utils.T;
import com.lidroid.xutils.ViewUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

/**
 * Created by Raul on 2015/11/10.
 * AppCompatActivity 基础类
 */
public abstract class BaseAppCompActivity extends AppCompatActivity {

    /*contains */
    protected Context mContext;//上下文
    private static final String TAG = "BaseAppCompActivity";
    protected boolean CheckNewData = true;

    /* local data */
    protected MyDialogBuilderV1 mDialogBuilder;//V1版本的对话框生成工具
    protected InputMethodManager imm;


    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        try {
            res.updateConfiguration(config, res.getDisplayMetrics());
        }catch (ClassCastException e){

        }
        return res;
    }

    /**
     * on Create
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setBackgroundDrawable(null);
        mContext = this;
        ActivityStackManager.getAppManager().addActivity(this);
        ViewUtils.inject(this);
        mDialogBuilder = new MyDialogBuilderV1();
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        initData();
        initViews();
        doMyOnCreate();
    }

    /**
     * on Resume
     */
    @Override
    protected void onResume() {
        super.onResume();
        // 友盟集成
        MobclickAgent.onResume(this);
        updateData();
        updateViews();
        if(!CheckNewData){
            return;
        }
        if (!LocalApplication.getInstance().isActive()) {
            //app 从后台唤醒，进入前台
            LocalApplication.getInstance().setIsActive(true);
            if(LocalApplication.getInstance().getLoginUser(mContext) == null){
                return;
            }
            if (LocalApplication.getInstance().getClipresult() != null) {
                GetParseInviteTextResult result = LocalApplication.getInstance().getClipresult();
                LocalApplication.getInstance().setClipresult(null);
                Intent intent = new Intent();
                Bundle b = new Bundle();
                b.putSerializable("data", result);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtras(b);
                //若类型是好友
                if (result.getType() == AppEnum.ClipType.USER) {
                    intent.setClass(mContext, ClipUserDialogActivity.class);
                    mContext.startActivity(intent);
                    //若类型是团
                } else if (result.getType() == AppEnum.ClipType.GROUP) {
                    intent.setClass(mContext, ClipGroupDialogActivity.class);
                    mContext.startActivity(intent);
                }
                return;
            }


            int userId = LocalApplication.getInstance().getLoginUser(mContext).getUserId();
            //若有新记录，且不在跑步
            if (RecordData.getRecords(mContext, userId).size() > 0
                    && WorkoutData.getUnFinishWorkout(mContext, userId) == null) {
                T.showLong(mContext,"有新纪录产生");
                startActivity(NewRecordActivityV2.class);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!AppUtils.isAppOnForeground(getApplicationContext())) {
            //app 进入后台
            LocalApplication.getInstance().setIsActive(false);
        }
    }

    /**
     * on Pause
     */
    @Override
    protected void onPause() {
        super.onPause();
        // 友盟集成
        MobclickAgent.onPause(this);
    }

    /**
     * on Destroy
     */
    @Override
    protected void onDestroy() {
        destroy();
        ActivityStackManager.getAppManager().finishActivity(this);
        mDialogBuilder.Destroy();
        super.onDestroy();
    }

    protected void startActivity(Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(mContext, cls);
        startActivity(intent);
    }

    protected void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(mContext, cls);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    protected void startActivityForResult(Class<?> cls, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(mContext, cls);
        startActivityForResult(intent, requestCode);
    }

    protected void startActivityForResult(Class<?> cls, Bundle bundle, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(mContext, cls);
        intent.putExtras(bundle);
        startActivityForResult(intent, requestCode);
    }


    //准备数据
    protected abstract void initData();

    //准备数据
    protected abstract void initViews();

    //执行Create结束后的事情
    protected abstract void doMyOnCreate();

    //更新数据
    protected abstract void updateData();

    //更新界面
    protected abstract void updateViews();

    //销毁界面的时候需要做的事
    protected abstract void destroy();

}
