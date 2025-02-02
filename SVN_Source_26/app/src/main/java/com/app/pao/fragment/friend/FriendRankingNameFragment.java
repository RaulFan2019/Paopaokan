package com.app.pao.fragment.friend;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.activity.friend.SearchFriendActivity;
import com.app.pao.activity.friend.SearchFriendFromPhoneActivity;
import com.app.pao.activity.main.MainActivityV2;
import com.app.pao.activity.main.ScanQRCodeActivityReplace;
import com.app.pao.adapter.FriendRankingLengthAdapter;
import com.app.pao.adapter.FriendRankingNameAdapter;
import com.app.pao.config.AppEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.data.db.MaidianData;
import com.app.pao.entity.db.DBEntityMaidian;
import com.app.pao.entity.network.FriendRankNameWithPyFirst;
import com.app.pao.entity.network.GetFriendRankResult;
import com.app.pao.fragment.BaseFragment;
import com.app.pao.network.api.MyRequestCallBack;
import com.app.pao.network.utils.RequestParamsBuild;
import com.app.pao.ui.widget.LoadingView;
import com.app.pao.ui.widget.SideBar;
import com.app.pao.utils.Log;
import com.app.pao.utils.T;
import com.app.pao.utils.TimeUtils;
import com.app.pao.utils.comparator.FriendRankingNameComparator;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.rey.material.widget.EditText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hwh.com.pulltorefreshlibrary.PullToRefreshLayout;

/**
 * Created by LY on 2016/3/16.
 */
public class FriendRankingNameFragment extends BaseFragment implements View.OnClickListener {

    /* contains*/
    private static final int MSG_POST_ERROR = 0;
    private static final int MSG_POST_OK = 1;
    private static final int SEARCH_FRIEND_OK = 2;

    /* local view */
    @ViewInject(R.id.et_search_friend_input)
    private EditText mSearchEt;
    @ViewInject(R.id.rv_friend_ranking_name_list)
    private RecyclerView mFriendRankNameRv;
    @ViewInject(R.id.sidebar)
    private SideBar mSideBar;
    @ViewInject(R.id.iv_search_member_fork)
    private ImageView mSearchForkIv;
    @ViewInject(R.id.refresh_layout_friend_list)
    private PullToRefreshLayout mRefeshLayout;//刷新VIew
    @ViewInject(R.id.ll_base_friend_list)
    private LinearLayout mBaseFriendListLl;
    @ViewInject(R.id.rl_load)
    private LinearLayout mLoadLl;
    @ViewInject(R.id.lv_load)
    private LoadingView mLoadLv;
    @ViewInject(R.id.tv_reload)
    private android.widget.TextView mReLoadTv;
    @ViewInject(R.id.ll_none)
    public LinearLayout mNoneLl;

    private FriendRankingNameAdapter mAdapter;
    private FriendRankingLengthAdapter mAdapterByLength;
    private int oldListSize;//用于记录上一次朋友列表的长度
    private int oldPosition;//用于记录上一次sidebar监听的位置
    private boolean isFirstIn;
    private boolean mPostAble;//是否可以发送请求
    private FriendListFragment mParentFragment;

    private List<GetFriendRankResult> mFriendNameList = new ArrayList<>();
    private List<GetFriendRankResult> mSearchDatas;
    private List<FriendRankNameWithPyFirst> mFriendNameListWithPy;
    private int userId;

    private Handler mPsotHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_POST_ERROR:
                    T.showShort(mContext, msg.obj.toString());
                    mPsotHandler.removeCallbacks(mPsotRunnable);
                    mPostAble = true;
                    if (mLoadLl.getVisibility() == View.VISIBLE) {
                        mLoadLv.setLoadingText("加载失败");
                        mReLoadTv.setVisibility(View.VISIBLE);
                    }
                    break;
                case MSG_POST_OK:
                    mPostAble = true;
                    mRefeshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                    updateViewByPostResult();
                    mPsotHandler.removeCallbacks(mPsotRunnable);
                    mLoadLv.setVisibility(View.GONE);
                    mLoadLl.setVisibility(View.GONE);
                    break;
//                case SEARCH_FRIEND_OK:
//                    int positon = (int) msg.obj;
//                    mFriendRankNameRv.scrollToPosition(positon);
//                    break;
            }

            if (mDialogBuilder.progressDialog != null && mDialogBuilder.progressDialog.isShowing()) {
                mDialogBuilder.progressDialog.dismiss();
            }
        }
    };

    Runnable mPsotRunnable = new Runnable() {
        @Override
        public void run() {
            mDialogBuilder.showProgressDialog(mContext, "正在加载", true);
        }
    };

    @Override
    @OnClick({R.id.iv_search_member_fork, R.id.tv_reload,R.id.ll_search_friend_num,
            R.id.ll_search_friend_numlist,R.id.ll_search_friend_wx,R.id.ll_search_friend_sys})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_search_member_fork:
                mSearchEt.setText("");
                break;
            case R.id.tv_reload:
                getFriendSortNameRequest();
                mLoadLv.setLoadingText("加载中...");
                mReLoadTv.setVisibility(View.GONE);
                break;
            //搜索用户(当好友为0时)
            case R.id.ll_search_friend_num:
                startActivity(SearchFriendActivity.class);
                break;
            //通讯录添加(当好友为0时)
            case R.id.ll_search_friend_numlist:
                startActivity(SearchFriendFromPhoneActivity.class);
                break;
            //微信邀请(当好友为0时)
            case R.id.ll_search_friend_wx:
                ((MainActivityV2)getActivity()).getSmsInviteText();
                break;
            //扫一扫(当好友为0时)
            case  R.id.ll_search_friend_sys:
                MaidianData.saveMaidian(mContext, new DBEntityMaidian(System.currentTimeMillis() + "",
                        AppEnum.MaidianType.AddFriendBySys, TimeUtils.NowTime()));
                Bundle bundle = new Bundle();
                bundle.putInt("hasScanSys",1);
                startActivity(ScanQRCodeActivityReplace.class,bundle);
                break;

        }
    }

    public static FriendRankingNameFragment newInstance() {
        FriendRankingNameFragment nameFragment = new FriendRankingNameFragment();
        return nameFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_friend_ranking_name;
    }

    @Override
    protected void initParams() {
        initDate();
        initViews();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPostAble = true;
        if (isFirstIn) {
            isFirstIn = false;

            getFriendSortNameRequest();
//            getFriendSortNameRequest();
        } else {
//            if(mParentFragment.getmCurrentFragment() instanceof FriendRankingNameFragment)
            getFriendSortNameRequest();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelHandle();
    }

    private void initDate() {
        userId = LocalApplication.getInstance().getLoginUser(mContext).getUserId();
        mFriendNameListWithPy = new ArrayList<>();
        isFirstIn = true;
        mPostAble = true;
        oldPosition = -1;
    }

    private void initViews() {
        mParentFragment = (FriendListFragment) getParentFragment();
        mAdapter = new FriendRankingNameAdapter(mFriendNameListWithPy, mContext);
        mFriendRankNameRv.setLayoutManager(new LinearLayoutManager(mContext));
        mFriendRankNameRv.setAdapter(mAdapter);

//        Log.v(TAG, "mParentFragment==null:" + (mParentFragment == null));
//        Log.v(TAG, "mParentFragment.mNoneLl==null:" + (mParentFragment.mNoneLl == null));
        //注册下方的上拉刷新条
        RelativeLayout loadView = (RelativeLayout) mRefeshLayout.findViewById(R.id.loadmore_view);
        //隐藏这个刷新条
        loadView.setVisibility(View.GONE);
        initRefreshListener();
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
                    mAdapter = new FriendRankingNameAdapter(mFriendNameListWithPy, mContext);
                    mFriendRankNameRv.setAdapter(mAdapter);
                } else {
                    if (mSearchForkIv.getVisibility() != View.VISIBLE) {
                        mSearchForkIv.setVisibility(View.VISIBLE);
                    }
                    mSearchDatas = new ArrayList<>();
                    for (GetFriendRankResult entity : mFriendNameList) {
                        if (entity.getNickname().contains(s)) {
                            mSearchDatas.add(entity);
                        }
                    }
                    mAdapterByLength = new FriendRankingLengthAdapter(mSearchDatas, mContext);
                    mFriendRankNameRv.setAdapter(mAdapterByLength);
                }

            }
        });
    }

    /**
     * 刷新事件监听
     */
    private void initRefreshListener() {
        mRefeshLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
                if (mPostAble) {
                    mPostAble = false;
                    getFriendSortNameRequest();
                }
            }

            @Override
            public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
                mRefeshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            }
        });
    }

    private void getFriendSortNameRequest() {
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_GET_RANKING_FRIEND_LIST;

        int type = AppEnum.friendRankType.NAME;
        RequestParams params = RequestParamsBuild.buildGetFriendRankRequest(mContext, userId, type);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {
            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                if (mPsotHandler != null) {
                    Message msg = new Message();
                    msg.what = MSG_POST_ERROR;
                    msg.obj = errorMsg;
                    mPsotHandler.sendMessage(msg);
                }
            }

            @Override
            protected void onRightResponse(String Response) {
                if (mFriendNameList != null) {
                    mFriendNameList.clear();
                }
                for(int i  = 0 ; i < mFriendNameList.size() ; i ++){
                    if(userId == mFriendNameList.get(i).getId()){
                        mFriendNameList.remove(i);
                    }
                }
                mFriendNameList.addAll(JSON.parseArray(Response, GetFriendRankResult.class));
                if (mPsotHandler != null) {
                    Message msg = new Message();
                    msg.what = MSG_POST_OK;
                    mPsotHandler.sendMessage(msg);
                }
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                if (mPsotHandler != null) {
                    Message msg = new Message();
                    msg.what = MSG_POST_ERROR;
                    msg.obj = s;
                    mPsotHandler.sendMessage(msg);
                }
            }

            @Override
            protected void onFinish() {

            }
        });
    }

    /**
     * POST返回完成之后进行的操作
     */
    public  void updateViewByPostResult() {
        if (mFriendNameList.size() > 0) {
            mNoneLl.setVisibility(View.INVISIBLE);

            Collections.sort(mFriendNameList, new FriendRankingNameComparator());
            List<String> strList = new ArrayList<>();
            mFriendNameListWithPy.clear();
            for (int i = 0; i < mFriendNameList.size(); i++) {
                if (i == 0) {
                    strList.add(mFriendNameList.get(i).getPinYinFirst() + "");
                    mFriendNameListWithPy.add(new FriendRankNameWithPyFirst(0, null, mFriendNameList.get(i).getPinYinFirst(), i + 1));
                    mFriendNameListWithPy.add(new FriendRankNameWithPyFirst(1, mFriendNameList.get(i), ' ', i + 1));
                } else if (mFriendNameList.get(i - 1).getPinYinFirst() != mFriendNameList.get(i).getPinYinFirst()) {
                    strList.add(mFriendNameList.get(i).getPinYinFirst() + "");
                    mFriendNameListWithPy.add(new FriendRankNameWithPyFirst(0, null, mFriendNameList.get(i).getPinYinFirst(), i + 1));
                    mFriendNameListWithPy.add(new FriendRankNameWithPyFirst(1, mFriendNameList.get(i), ' ', i + 1));
                } else {
                    mFriendNameListWithPy.add(new FriendRankNameWithPyFirst(1, mFriendNameList.get(i), ' ', i + 1));
                }
            }
            mFriendNameListWithPy.add(new FriendRankNameWithPyFirst(2, null, ' ', mFriendNameList.size()));
            mAdapter.notifyDataSetChanged();

            if (strList.size() == 1) {
                mSideBar.setVisibility(View.GONE);
            } else {
                String[] strArr = new String[strList.size()];
                for (int i = 0; i < strList.size(); i++) {
                    strArr[i] = strList.get(i);
                }
                mSideBar.setVisibility(View.VISIBLE);
                mSideBar.setStrArr(strArr);

                mSideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
                    /**
                     * 此方法用于跳转到sidebar监听的位置
                     *
                     * @param s
                     */
                    @Override
                    public void onTouchingLetterChanged(String s) {

                        int position = mAdapter.getPositionForSelection(s.charAt(0));
                        LinearLayoutManager lm = (LinearLayoutManager) mFriendRankNameRv.getLayoutManager();
                        lm.scrollToPositionWithOffset(position, 0);

                    }
                });
            }
        } else {
            //如果好友列表长度为0，则显示父FragMnet的mNoneLl
            mNoneLl.setVisibility(View.VISIBLE);
        }
    }

    private void cancelHandle() {
        if (mPsotHandler != null) {
            mPsotHandler.removeMessages(MSG_POST_ERROR);
            mPsotHandler.removeMessages(MSG_POST_OK);
            mPsotHandler.removeMessages(SEARCH_FRIEND_OK);
        }
    }
}
