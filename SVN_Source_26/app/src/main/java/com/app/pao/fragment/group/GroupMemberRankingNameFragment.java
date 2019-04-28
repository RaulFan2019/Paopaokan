package com.app.pao.fragment.group;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.app.pao.R;
import com.app.pao.activity.group.GroupMemberRankingActivity;
import com.app.pao.adapter.GroupMemberAdapter;
import com.app.pao.adapter.GroupMemberRankingNameAdapter;
import com.app.pao.config.AppEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.entity.network.GetGroupDetailInfoRequest;
import com.app.pao.entity.network.GetGroupMemberListResult;
import com.app.pao.entity.network.GetGroupMemberSortResult;
import com.app.pao.entity.network.GroupRankNameWithFirstPy;
import com.app.pao.fragment.BaseFragment;
import com.app.pao.network.api.MyRequestCallBack;
import com.app.pao.network.utils.RequestParamsBuild;
import com.app.pao.ui.widget.FullyLinearLayoutManager;
import com.app.pao.ui.widget.LoadingView;
import com.app.pao.ui.widget.SideBar;
import com.app.pao.utils.Log;
import com.app.pao.utils.T;
import com.app.pao.utils.comparator.FriendRankingNameComparator;
import com.app.pao.utils.comparator.GroupMemberRankingNameComparator;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnTouch;
import com.rey.material.widget.EditText;
import com.rey.material.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hwh.com.pulltorefreshlibrary.PullToRefreshLayout;

/**
 * Created by Administrator on 2016/1/28.
 * <p/>
 * 跑团成员 按照姓名排序
 */
public class GroupMemberRankingNameFragment extends BaseFragment implements View.OnClickListener {

    private static final String VALUE_KEY = "groupId";
    private static final int MSG_POST_ERROR = 0;
    private static final int MSG_POST_OK = 1;
    @ViewInject(R.id.rv_member_ranking_name_list)
    private RecyclerView mNameRankingRv;
//    @ViewInject(R.id.ll_name_sort)
//    private LinearLayout mRankingTextLl;
//    @ViewInject(R.id.tv_name_sort)
//    private TextView mRankingTextTv;
    @ViewInject(R.id.sb_name_py)
    private SideBar mNamePySb;
    @ViewInject(R.id.et_search_member_input)
    private EditText mSearchEt;
    @ViewInject(R.id.iv_search_member_fork)
    private ImageView mSearchForkIv;

    @ViewInject(R.id.rl_load)
    private LinearLayout mLoadViewRl;//加载View
    @ViewInject(R.id.loadview)
    private LoadingView mLoadV;
    @ViewInject(R.id.tv_reload)
    private com.rey.material.widget.TextView mReladV;

    private GroupMemberRankingNameAdapter mNameAdapter;
    private List<GetGroupMemberSortResult> mNameList;
    //    private List<GetGroupMemberSortResult> mSearchDatas;
    private List<GroupRankNameWithFirstPy> mSearchDatas;
    private List<GroupRankNameWithFirstPy> mPyNameList;
    private GetGroupDetailInfoRequest mGroupInfo;
    private int mGroupId;
    private boolean isFirstIn;

    private Handler mPostHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_POST_ERROR) {
                T.showShort(mContext, msg.obj.toString());
                mPostHandler.removeCallbacks(mPostRunnable);
                if (mLoadViewRl.getVisibility() == View.VISIBLE) {
                    mLoadV.setLoadingText("加载失败");
                    mReladV.setVisibility(View.VISIBLE);
                }

            } else if (msg.what == MSG_POST_OK) {
                refreshView();
                mPostHandler.removeCallbacks(mPostRunnable);
                mLoadV.setVisibility(View.GONE);
                mLoadViewRl.setVisibility(View.GONE);
            }

            if (mDialogBuilder.progressDialog != null && mDialogBuilder.progressDialog.isShowing()) {
                mDialogBuilder.progressDialog.dismiss();
            }
        }
    };

    Runnable mPostRunnable = new Runnable() {
        @Override
        public void run() {
            mDialogBuilder.showProgressDialog(mContext, "正在加载", true);
        }
    };

    @Override
    @OnClick({R.id.iv_search_member_fork, R.id.tv_reload})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_search_member_fork:
                mSearchEt.setText("");
                break;
            case R.id.tv_reload:
                postGetSortNameRequest();
                mLoadV.setLoadingText("加载中...");
                mReladV.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 实例化
     *
     * @return
     */
    public static GroupMemberRankingNameFragment newInstance() {
        GroupMemberRankingNameFragment nameFragment = new GroupMemberRankingNameFragment();
        return nameFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_member_ranking_name;
    }

    @Override
    protected void initParams() {
        mNameList = new ArrayList<>();
        mPyNameList = new ArrayList<>();
        GroupMemberRankingActivity mActivity = (GroupMemberRankingActivity) getActivity();
        mGroupInfo = mActivity.getmGroupInfo();
        mGroupId = mActivity.getmGroupId();
        isFirstIn = true;
        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        postGetSortNameRequest();
        if (isFirstIn) {
            isFirstIn = false;
        } else {
            mPostHandler.postDelayed(mPostRunnable, 500);
        }
    }

    @Override
    public void onDestroy() {
        cancelHandler();
        super.onDestroy();
    }

    private void cancelHandler() {
        mPostHandler.removeCallbacks(mPostRunnable);
        mPostHandler.removeMessages(MSG_POST_OK);
        mPostHandler.removeMessages(MSG_POST_ERROR);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mNameRankingRv.setLayoutManager(new LinearLayoutManager(mContext));
        mNameRankingRv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                return false;
            }
        });
        mNamePySb.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                int pos = mNameAdapter.getPositionForSelection(s.charAt(0));
                if (pos != -1) {
                    LinearLayoutManager lm = (LinearLayoutManager) mNameRankingRv.getLayoutManager();
                    lm.scrollToPositionWithOffset(pos,0);
                }
            }
        });

        mSearchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String newText = mSearchEt.getText().toString();
                if (newText.isEmpty()) {
                    if (mSearchForkIv.getVisibility() != View.GONE) {
                        mSearchForkIv.setVisibility(View.GONE);
                    }
                    mSearchDatas = mPyNameList;
                    mNameAdapter = new GroupMemberRankingNameAdapter(mContext, mGroupInfo, mSearchDatas);
                    mNameRankingRv.setAdapter(mNameAdapter);
                } else {
                    if (mSearchForkIv.getVisibility() != View.VISIBLE) {
                        mSearchForkIv.setVisibility(View.VISIBLE);
                    }
                    mSearchDatas = new ArrayList<GroupRankNameWithFirstPy>();
                    for (GroupRankNameWithFirstPy entity : mPyNameList) {
                        if (entity.getGroupMember() != null && entity.getGroupMember().getDisplayname().contains(s)) {
                            entity.setType(1);
                            mSearchDatas.add(entity);
                        }
                    }
                    mNameAdapter = new GroupMemberRankingNameAdapter(mContext, mGroupInfo, mSearchDatas);
                    mNameRankingRv.setAdapter(mNameAdapter);
                }

            }
        });
        mNameRankingRv.requestFocus();
    }

    /**
     * 刷新RecycleView
     */
    private void refreshView() {
        //     if (mNameAdapter == null) {
        mNameAdapter = new GroupMemberRankingNameAdapter(mContext, mGroupInfo, mPyNameList);
        mNameRankingRv.setAdapter(mNameAdapter);
        //    } else {
        //         mNameAdapter.notifyDataSetChanged();
        //     }
    }

    /**
     * 获取姓名排行榜
     */
    private void postGetSortNameRequest() {
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_GET_RUNGROUP_RANK_LIST;
        int type = AppEnum.groupRankType.NAME;
        RequestParams params = RequestParamsBuild.buildGetGroupRankRequest(mContext,mGroupId, type);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                if (mPostHandler != null) {
                    Message msg = new Message();
                    msg.what = MSG_POST_ERROR;
                    msg.obj = errorMsg;
                    mPostHandler.sendMessage(msg);
                }
            }

            @Override
            protected void onRightResponse(String Response) {
                if (mNameList != null && mPyNameList != null) {
                    mNameList.clear();
                    mPyNameList.clear();
                }
                mNameList.addAll(JSON.parseArray(Response, GetGroupMemberSortResult.class));
                if (mNameList.size() > 0) {
                    List<String> sideList = new ArrayList<String>();
                    Collections.sort(mNameList, new GroupMemberRankingNameComparator());
                    //第一开始时确定是拼音
                    mPyNameList.add(new GroupRankNameWithFirstPy(0, mNameList.get(0).getFirstPinYinChar(), null, 0));
                    mPyNameList.add(new GroupRankNameWithFirstPy(1, mNameList.get(0).getFirstPinYinChar(), mNameList.get(0), 0));
                    sideList.add(mNameList.get(0).getFirstPinYinChar() + "");
                    for (int i = 1; i < mNameList.size(); i++) {
                        //若上一次与这一次拼音相同，则继续插入数据，属于同一拼音组
                        if (mNameList.get(i - 1).getFirstPinYinChar() == mNameList.get(i).getFirstPinYinChar()) {
                            mPyNameList.add(new GroupRankNameWithFirstPy(1, mNameList.get(i - 1).getFirstPinYinChar(), mNameList.get(i), 0));
                            //若不相同，则表示是新的一组，此时重新插入新的数据组
                        } else {
                            sideList.add(mNameList.get(i).getFirstPinYinChar() + "");
                            mPyNameList.add(new GroupRankNameWithFirstPy(0, mNameList.get(i).getFirstPinYinChar(), null, 0));
                            mPyNameList.add(new GroupRankNameWithFirstPy(1, mNameList.get(i).getFirstPinYinChar(), mNameList.get(i), 0));
                        }
                    }
                    mPyNameList.add(new GroupRankNameWithFirstPy(2,' ',null,mNameList.size()));
                    mNamePySb.setStrArr(sideList.toArray(new String[sideList.size()]));
                } else {
                    // TODO: 2016/3/28
                }

                if (mPostHandler != null) {
                    Message msg = new Message();
                    msg.what = MSG_POST_OK;
                    mPostHandler.sendMessage(msg);
                }
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                if (mPostHandler != null) {
                    Message msg = new Message();
                    msg.what = MSG_POST_ERROR;
                    msg.obj = s;
                    mPostHandler.sendMessage(msg);
                }
            }

            @Override
            protected void onFinish() {

            }
        });
    }


}
