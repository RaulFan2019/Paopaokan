package com.app.pao.activity.workout;

import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.config.URLConfig;
import com.app.pao.network.api.MyRequestCallBack;
import com.app.pao.network.utils.RequestParamsBuild;
import com.app.pao.utils.Log;
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
import java.util.List;

import hwh.com.pickviewlibrary.OptionsPickerView;
import hwh.com.pickviewlibrary.TimePickerView;

/**
 * Created by Raul on 2016/1/31.
 * 手动增加跑步历史
 */
@ContentView(R.layout.activity_add_history_by_hand)
public class AddHistoryByHandActivity extends BaseAppCompActivity implements View.OnClickListener {

    /* contains */
    private static final String TAG = "AddHistoryByHandActivity";

    /* local view */
    @ViewInject(R.id.toolbar)
    private Toolbar mToolbar;//标题栏
    @ViewInject(R.id.et_length)
    private EditText mLengthEt;//距离输入框
    @ViewInject(R.id.tv_duration)
    private TextView mDurationTv;
    @ViewInject(R.id.tv_pace)
    private TextView mPaceTv;
    @ViewInject(R.id.tv_starttime)
    private TextView mStartTimeTv;

    private TimePickerView mStartTimePick;//开始时间设置框
    private OptionsPickerView mSpaceTimePick;
    private OptionsPickerView mRunDurationPick;

    /* local data */
    private float mLength;//距离
    private String mStartTime;
    private int mPace = 0;
    private int mDuration = 0;



    /**
     * Toolbar 点击事件
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * on Click
     *
     * @param v
     */
    @Override
    @OnClick({R.id.ll_duration, R.id.ll_pace, R.id.ll_starttime, R.id.btn_confirm})
    public void onClick(View v) {
        //点击耗时
        if (v.getId() == R.id.ll_duration) {
            showHistoryDurationTime();
            //点击配速
        } else if (v.getId() == R.id.ll_pace) {
//            showSpaceTime();

            //点击开始时间
        } else if (v.getId() == R.id.ll_starttime) {
            showWorkOutStartTime();
            //点击确定
        } else if (v.getId() == R.id.btn_confirm) {
            checkWorkoutInfo();
        }
    }


    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {
        setSupportActionBar(mToolbar);

        Calendar calendar = Calendar.getInstance();
        int curYear = calendar.get(Calendar.YEAR);
        mStartTimePick = new TimePickerView(mContext, TimePickerView.Type.ALL,curYear,curYear);

        mLengthEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                final String mLengthStr = mLengthEt.getText().toString();
                if(!mLengthStr.isEmpty()){
                    mPace = (int) (mDuration/Float.valueOf(mLengthStr));
                    mPaceTv.setText(TimeUtils.formatSecondsToSpeedTime(mPace,"'")+"''");
                }
            }
        });
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
     * 设置历史开始时间
     */
    private void showWorkOutStartTime(){
        mStartTimePick.setTitle("开始时间");
        mStartTimePick.setTime(null);
        mStartTimePick.setCancelable(true);
        if(mStartTime != null && !mStartTime.isEmpty()) {
            mStartTimePick.setTime(TimeUtils.stringToDate(mStartTime));
        }
        //设置选择器是否可以循环滚动
        mStartTimePick.setCyclic(false);
        //设置选择时间回调
        mStartTimePick.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                if (date.before(new Date(System.currentTimeMillis()))) {
//                    if (TimeUtils.checkIsToday(date)) {
                        mStartTime = TimeUtils.dateToPartyTimeStr(date);
                        mStartTimeTv.setText(mStartTime);
//                    } else {
//                        T.showShort(mContext, "设置失败: 开始时间不能超过今天");
//                    }
                } else {
                    T.showShort(mContext, "设置失败: 开始时间不能晚于现在");
                }
            }
        });
        mStartTimePick.show();
    }

    /**
     * 显示配速
     */
    private void showSpaceTime(){
        mSpaceTimePick = new OptionsPickerView(mContext);
        ArrayList<Integer> spaceList = new ArrayList<Integer>();
        for (int i = 0;i<60;i++){
            spaceList.add(i);
        }
        ArrayList<ArrayList<Integer>> sceList = new ArrayList<ArrayList<Integer>>();
        sceList.add(spaceList);

        mSpaceTimePick.setTitle("配速");
        mSpaceTimePick.setPicker(spaceList, sceList, false);
        mSpaceTimePick.setLabels("分", "秒");
        mSpaceTimePick.setSelectOptions((int)(mPace%3600/60),(int)(mPace%60));
        mSpaceTimePick.setCancelable(true);
        mSpaceTimePick.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                mPace = options1 * 60 + option2;
                mPaceTv.setText(options1+"'"+option2+"''");
            }
        });
        mSpaceTimePick.show();
    }

    /**
     * 显示持续时间
     */
    private void showHistoryDurationTime(){
        mRunDurationPick = new OptionsPickerView(mContext);
        ArrayList<Integer> optionList1 = new ArrayList<Integer>();
        for (int i = 0;i<60;i++){
            optionList1.add(i);
        }
        ArrayList<ArrayList<Integer>> optionList2 = new ArrayList<ArrayList<Integer>>();
        optionList2.add(optionList1);

        ArrayList<ArrayList<ArrayList<Integer>>> optionList3 = new ArrayList<ArrayList<ArrayList<Integer>>>();
        optionList3.add(optionList2);

        mRunDurationPick.setTitle("用时");
        mRunDurationPick.setPicker(optionList1, optionList2, optionList3, false);
        mRunDurationPick.setLabels("时", "分", "秒");
        mRunDurationPick.setSelectOptions((mDuration / 3600), (mDuration % 3600 / 60), (mDuration % 60));
        mRunDurationPick.setCancelable(true);
        mRunDurationPick.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                mDuration = options1*3600+option2*60+options3;
                mDurationTv.setText(options1+" 时 "+option2+" 分 "+options3+" 秒 ");

               final String mLengthStr = mLengthEt.getText().toString();
                if(!mLengthStr.isEmpty()){
                    mPace = (int) (mDuration/Float.valueOf(mLengthStr));
                    mPaceTv.setText(TimeUtils.formatSecondsToSpeedTime(mPace,"'")+"''");
                }

            }
        });
        mRunDurationPick.show();
    }


    /**
     * 检查跑步历史信息
     */
    private void checkWorkoutInfo() {
        //检查距离
        if (mLengthEt.getText().toString().equals("")) {
            T.showShort(mContext, "请填写跑步距离");
            mLengthEt.setError("请填写跑步距离");
            return;
        }
        mLength = Float.parseFloat(mLengthEt.getText().toString());
        //检查
        if(mDuration ==0 ){
            T.showShort(mContext, "请选择用时");
            return;
        }

        if(mStartTime == null || mStartTime.isEmpty()){
            T.showShort(mContext, "请选择开始时间");
            return;
        }

        if(mPace == 0){
            T.showShort(mContext, "请选择配速");
            return;
        }

        saveHandHistory();
    }


    /**
     * 保存历史
     */
    private void saveHandHistory() {
        mDialogBuilder.showProgressDialog(mContext,"保存...",false);

        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_CREATE_MANUAL_WORKOUT;
        RequestParams params = RequestParamsBuild.buildCreateManualWorkOut(mContext,mLength*1000,mDuration,mStartTime,mPace);

        http.send(HttpRequest.HttpMethod.POST,POST_URL,params,new MyRequestCallBack(mContext){
            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                T.showShort(mContext,"保存失败");
            }

            @Override
            protected void onRightResponse(String Response) {
                T.showShort(mContext,"保存成功");
                finish();
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                T.showShort(mContext,"保存失败");
            }

            @Override
            protected void onFinish() {
                if(mDialogBuilder != null){
                    mDialogBuilder.progressDialog.dismiss();
                }
            }
        });
    }


}
