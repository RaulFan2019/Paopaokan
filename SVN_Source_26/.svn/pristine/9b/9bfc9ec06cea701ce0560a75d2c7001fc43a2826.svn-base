package com.app.pao.activity.group;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.activity.workout.AddHistoryByHandActivity;
import com.app.pao.adapter.GroupPartySelectWorkOutAdapter;
import com.app.pao.config.URLConfig;
import com.app.pao.entity.network.GetWorkOutListForPartyResult;
import com.app.pao.network.api.MyRequestCallBack;
import com.app.pao.network.utils.RequestParamsBuild;
import com.app.pao.ui.widget.FullyLinearLayoutManager;
import com.app.pao.utils.Log;
import com.app.pao.utils.T;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/10.
 * <p/>
 * 我的活动workOut
 */
@ContentView(R.layout.activity_group_party_select_workout)
public class GroupPartySelectWorkOutActivity extends BaseAppCompActivity implements View.OnClickListener {
    private static final int MSG_POST_ERROR = 0;
    private static final int MSG_POST_OK = 1;

    private RecyclerView mWorkoutRv;

    private int mPartyId;
    private int mUserId;
    private List<GetWorkOutListForPartyResult.PartyWorkOut> mWorkOutList;
    private List<GetWorkOutListForPartyResult.PartyWorkOut> mSelectWorkOutList;
    private GroupPartySelectWorkOutAdapter mWorkOutAdapter;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_POST_OK) {
                mWorkOutAdapter = new GroupPartySelectWorkOutAdapter(mWorkOutList);
                mWorkoutRv.setAdapter(mWorkOutAdapter);

            } else if (msg.what == MSG_POST_ERROR) {
                T.showShort(mContext, msg.obj.toString());
            }
        }
    };

    @Override
    @OnClick({R.id.btn_choose_confirm, R.id.title_bar_left_menu, R.id.btn_add_history})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_choose_confirm:
                if(mWorkOutAdapter != null) {
                    mSelectWorkOutList = mWorkOutAdapter.getmDataList();
                    if (mSelectWorkOutList != null) {
                        setCheckInWorkOutList();
                    } else {
                        T.showShort(mContext, "您还未选择任何跑步数据");
                    }
                }
                break;
            //返回键
            case R.id.title_bar_left_menu:
                finish();
                break;
            //手动增加跑步历史
            case R.id.btn_add_history:
                startActivity(AddHistoryByHandActivity.class);
                break;
        }
    }

    @Override
    protected void initData() {
        mPartyId = getIntent().getIntExtra("partyId", 0);
        mUserId = LocalApplication.getInstance().getLoginUser(mContext).getUserId();
        mWorkOutList = new ArrayList<GetWorkOutListForPartyResult.PartyWorkOut>();
        mSelectWorkOutList = new ArrayList<GetWorkOutListForPartyResult.PartyWorkOut>();
    }

    @Override
    protected void initViews() {
        mWorkoutRv = (RecyclerView) findViewById(R.id.rv_workout);
        mWorkoutRv.setLayoutManager(new FullyLinearLayoutManager(this));
    }

    @Override
    protected void doMyOnCreate() {

    }

    @Override
    protected void updateData() {

    }

    @Override
    protected void updateViews() {
        getWorkoutListForParty();
    }

    @Override
    protected void destroy() {
        cancelHandler();
    }

    private void cancelHandler() {
        if (handler != null) {
            handler.removeMessages(MSG_POST_ERROR);
            handler.removeMessages(MSG_POST_OK);
        }
    }

    /**
     * 获取历史数据
     */
    private void getWorkoutListForParty() {
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_GET_WORKPUT_LIST_FOR_PARTY;
        RequestParams params = RequestParamsBuild.buildGetWorkoutListForParty(mContext,mPartyId);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {
            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                if (handler != null) {
                    Message msg = new Message();
                    msg.what = MSG_POST_ERROR;
                    msg.obj = errorMsg;
                    handler.sendMessage(msg);
                }
            }

            @Override
            protected void onRightResponse(String Response) {
                GetWorkOutListForPartyResult result = JSON.parseObject(Response, GetWorkOutListForPartyResult.class);
                mWorkOutList = result.getWorkout();
                if (handler != null) {
                    Message msg = new Message();
                    msg.what = MSG_POST_OK;
                    handler.sendMessage(msg);
                }
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                if (handler != null) {
                    Message msg = new Message();
                    msg.what = MSG_POST_ERROR;
                    msg.obj = s;
                    handler.sendMessage(msg);
                }
            }

            @Override
            protected void onFinish() {

            }
        });
    }


    /**
     * 设置活动workout
     */
    private void setCheckInWorkOutList() {
        mDialogBuilder.showProgressDialog(mContext, "正在保存...", false);
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_SET_CHECK_IN_WORKOUT_LIST;
        StringBuffer mWorkOutList = new StringBuffer();
        for (int i = 0; i < mSelectWorkOutList.size(); i++) {
            if (mSelectWorkOutList.get(i).getSelected() == 1) {
                mWorkOutList.append(mSelectWorkOutList.get(i).getId() + ",");
            }
        }
        if (mWorkOutList.length() > 0) {
            mWorkOutList.deleteCharAt(mWorkOutList.length() - 1);
        } else {
            T.showShort(mContext, "请选择历史记录");
        }

        RequestParams params = RequestParamsBuild.buildSetCheckInWorkOutList(mContext,mPartyId, mWorkOutList.toString());
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {
            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                T.showShort(mContext, errorMsg);
            }

            @Override
            protected void onRightResponse(String Response) {
                T.showShort(mContext, "打卡成功");
                finish();
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                T.showShort(mContext, s);
            }

            @Override
            protected void onFinish() {
                mDialogBuilder.Destroy();
            }
        });
    }
}
