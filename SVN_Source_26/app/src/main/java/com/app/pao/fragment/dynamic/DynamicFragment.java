package com.app.pao.fragment.dynamic;

import android.content.Intent;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.activity.friend.UserInfoActivity;
import com.app.pao.activity.main.MessageListActivityReplace;
import com.app.pao.activity.main.RaceWebViewActivity;
import com.app.pao.activity.main.ScanQRCodeActivityReplace;
import com.app.pao.activity.party.PartyInfoActivity;
import com.app.pao.activity.workout.FriendHistoryInfoActivityV2;
import com.app.pao.activity.workout.HistoryInfoActivityV2;
import com.app.pao.adapter.DynamicListAdapterReplace;
import com.app.pao.adapter.DynamicRunnnerRvAdapter;
import com.app.pao.config.AppEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.data.db.MaidianData;
import com.app.pao.data.db.MessageData;
import com.app.pao.data.db.UploadData;
import com.app.pao.data.db.WorkoutData;
import com.app.pao.entity.adapter.DynamicLineItem;
import com.app.pao.entity.db.DBEntityMaidian;
import com.app.pao.entity.db.DBUploadEntity;
import com.app.pao.entity.event.EventCommentUserClick;
import com.app.pao.entity.event.EventDynamicClick;
import com.app.pao.entity.event.EventJpush;
import com.app.pao.entity.event.EventThumb;
import com.app.pao.entity.network.GetDynamicListResult;
import com.app.pao.entity.network.GetRaceListResult;
import com.app.pao.entity.network.GetRunningUserResult;
import com.app.pao.fragment.BaseFragment;
import com.app.pao.network.api.MyRequestCallBack;
import com.app.pao.network.utils.RequestParamsBuild;
import com.app.pao.ui.widget.BadgeView;
import com.app.pao.ui.widget.FullyLinearLayoutManager;
import com.app.pao.ui.widget.LoadingView;
import com.app.pao.utils.DeviceUtils;
import com.app.pao.utils.T;
import com.app.pao.utils.TimeUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import hwh.com.pulltorefreshlibrary.PullToRefreshLayout;
import hwh.com.pulltorefreshlibrary.PullableScrollViewForRecyclerView;

/**
 * Created by Raul on 2015/11/11.
 * 动态页面
 */
public class DynamicFragment extends BaseFragment implements View.OnClickListener {


    /* contains */
    private static final String TAG = "DynamicFragment";

    SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static String BANNERINTO = "";

    private static final int MSG_UPDATE_VIEW = 0;//更新页面
    private static final int MSG_UPDATE_ERROR = 1;//更新失败
    private static final int RESULT_BACK_DELECT = 2;//删除历史返回调用
    private static final int MSG_UPDATE_RUNNER = 4;//仅更新跑步人员

    /* local view */
    @ViewInject(R.id.refresh_layout_dynamic_list)
    private PullToRefreshLayout mFreshLayout;//刷新VIew
    @ViewInject(R.id.lv_dynamic_list)
    private RecyclerView mRvView;//动态列表

    @ViewInject(R.id.race_layout)
    private FrameLayout mRaceLayout;
    @ViewInject(R.id.sv_dynamic)
    private PullableScrollViewForRecyclerView mPullSv;
    @ViewInject(R.id.btn_message)
    private ImageButton messageBtn;

    private BadgeView badgeView;
    @ViewInject(R.id.rl_load)
    private LinearLayout mLoadViewRl;//加载View
    @ViewInject(R.id.loadview)
    private LoadingView mLoadV;
    @ViewInject(R.id.tv_reload)
    private TextView mReladV;

    @ViewInject(R.id.ll_simple_runner_base)
    private LinearLayout mSimpleRunnerLlBase;
    @ViewInject(R.id.lv_runner_list)
    private RecyclerView mRunnerRv;

    //web view
    @ViewInject(R.id.webView)
    private WebView mWebView;
    @ViewInject(R.id.ll_click_webview)
    private LinearLayout mWebViewLl;

    /* local data */
    private List<GetRunningUserResult.FriendsEntity> mRunnerList = new ArrayList<GetRunningUserResult.FriendsEntity>();
    private DynamicRunnnerRvAdapter mRunnnerAdapter;

    private boolean isFirstIn = true;
    private boolean postAble = true;

    private String mLastDate;//最后一个跑步历史日期

    private DynamicListAdapterReplace mAdapter;//适配器

    //时间适配数据
    private Date mLastFechDay;// 最后更新日
    private int mLastFechIndexDay = -1;

    // 当前使用列表
    private ArrayList<DynamicLineItem> mLineDatas = new ArrayList<DynamicLineItem>();
    private int sectionManager = -1;
    private int sectionFirstPosition = 0;
    private int headerCount = 0;

    //webView 相关
    private static String mBarUrl;//bar上显示的H5
    private static String mAdUrl;//跳转后显示的H5


    private static int mNewMessageCount;
    private static int mClickPostion;

    public static DynamicFragment newInstance() {
        DynamicFragment fragment = new DynamicFragment();
        return fragment;
    }

    /**
     * 更新页面Handler
     */
    Handler updateViewHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_UPDATE_VIEW) {
                postAble = true;
                if ((boolean) msg.obj) {
                    mFreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                } else {
                    mFreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                }
                refreshList((boolean) msg.obj);
                mLoadV.setVisibility(View.GONE);
                mLoadViewRl.setVisibility(View.GONE);
            } else if (msg.what == MSG_UPDATE_ERROR) {
                postAble = true;
                if ((boolean) msg.obj) {
                    mFreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                } else {
                    mFreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
                }
                mLoadV.setLoadingText("加载失败");
                mReladV.setVisibility(View.VISIBLE);
            } else if (msg.what == MSG_UPDATE_RUNNER) {
                PostGetFriendRequst(true);
            }
        }
    };


    /**
     * onClick
     *
     * @param v
     */
    @Override
    @OnClick({R.id.btn_message, R.id.tv_reload,
            R.id.btn_qrcode, R.id.ll_click_webview})
    public void onClick(View v) {
        //消息列表
        if (v.getId() == R.id.btn_message) {
            startActivity(MessageListActivityReplace.class);
            //跑步人员列表
        } else if (v.getId() == R.id.tv_reload) {
            mLoadV.setLoadingText("加载中..");
            mReladV.setVisibility(View.INVISIBLE);
            postGetWorkoutList(true);
            //二维码扫描
        } else if (v.getId() == R.id.btn_qrcode) {
            launchQrCode();
            //进入日日报
        } else if (v.getId() == R.id.ll_click_webview) {
            MaidianData.saveMaidian(mContext, new DBEntityMaidian(System.currentTimeMillis() + "", AppEnum.MaidianType.BannerInto, TimeUtils.NowTime()));
            launchWebView();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == RESULT_BACK_DELECT) {
                if (data.getBooleanExtra("isDelete", true)) {
                    if (postAble) {
                        AsyncTask postTask = new AsyncTask() {
                            @Override
                            protected Object doInBackground(Object[] params) {
                                initData();
                                postGetWorkoutList(true);
                                return null;
                            }
                        };
                        postTask.execute();
                    }
                } else {
                    DynamicLineItem item = mLineDatas.get(mClickPostion);
                    item.result.setCommentscount(data.getIntExtra("commentSize", item.result.getCommentscount()));
                    item.result.setThumbupcount(data.getIntExtra("thumbSize", item.result.getThumbupcount()));
                    item.result.setHasGiveThumbup(data.getIntExtra("hasGiveThumb", item.result.getHasGiveThumbup()));
                    mLineDatas.set(mClickPostion, item);
                    mAdapter.notifyItemChanged(mClickPostion);
                }
            }
        }
    }

    /**
     * 收到消息的变化
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EventJpush event) {
        updateMessageView();
    }

    /**
     * 点赞发生变化
     *
     * @param eventThumb
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EventThumb eventThumb) {
        int position = eventThumb.getPostion();
        DynamicLineItem item = mLineDatas.get(position);
        DBUploadEntity uploadEntity;
        String info = item.result.getId() + ":" + item.result.getDynamictype();
        //若已经点赞过
        if (item.result.getHasGiveThumbup() == AppEnum.hasGiveThumbup.YES) {
            T.showShort(mContext, "您已经赞过啦");
//            uploadEntity = new DBUploadEntity(DBUploadEntity.TYPE_THUMB_UP_NO, URLConfig
//                    .URL_THUMB_UP_NO, info
//                    , "", LocalApplication.getInstance().getLoginUser(mContext).getUserId());
//            item.result.setHasGiveThumbup(AppEnum.hasGiveThumbup.NO);
//            item.result.setThumbupcount(item.result.getThumbupcount() - 1);
            //DO NOTHING
        } else {
            uploadEntity = new DBUploadEntity(DBUploadEntity.TYPE_THUMB_UP_YES, URLConfig
                    .URL_THUMB_UP_YES, info, "",
                    LocalApplication.getInstance().getLoginUser(mContext).getUserId());
            item.result.setHasGiveThumbup(AppEnum.hasGiveThumbup.YES);
            item.result.setThumbupcount(item.result.getThumbupcount() + 1);
            //改变列表
            mLineDatas.set(position, item);
            mAdapter.notifyItemChanged(position);
            //发送点赞请求
            UploadData.createNewUploadData(mContext, uploadEntity);
        }
    }

    /**
     * 用户点击事件
     *
     * @param userClick
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EventCommentUserClick userClick) {
        if (userClick.userId != LocalApplication.getInstance().getLoginUser(mContext).userId) {
            Bundle bundle = new Bundle();
            bundle.putInt("userid", userClick.userId);
            startActivity(UserInfoActivity.class, bundle);
        }
    }

    /**
     * 内容点击事件
     *
     * @param dynamicClick
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EventDynamicClick dynamicClick) {
        mClickPostion = dynamicClick.position;
        if (dynamicClick.getItem().getDynamictype() == AppEnum.dynamicType.WORKOUT) {
            LaunchHistoryInfo(dynamicClick.item);
        } else {
            LaunchPartyInfo(dynamicClick.item);
        }

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_dynamic;
    }

    @Override
    protected void initParams() {
        initData();
        initViews();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        int userId = LocalApplication.getInstance().getLoginUser(mContext).userId;
        if (WorkoutData.getUnFinishWorkout(mContext, userId) != null) {
            return;
        }
        // 第一次进入自动刷新
        if (isFirstIn) {
            if (postAble) {
                AsyncTask postTask = new AsyncTask() {
                    @Override
                    protected Object doInBackground(Object[] params) {
                        postGetWorkoutList(true);
                        isFirstIn = false;
                        return null;
                    }
                };
                postTask.execute();
            }
        } else {
            updateViewHandler.sendEmptyMessage(MSG_UPDATE_RUNNER);
        }
        updateMessageView();
    }

    @Override
    public void onPause() {
        super.onPause();
        updateViewHandler.removeMessages(MSG_UPDATE_RUNNER);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        cancelHandler();
        causeGC();
    }

    private void initData() {
        postAble = true;
        mLastFechIndexDay = -1;
        mLastDate = TimeUtils.NowTime();
        mLastFechDay = TimeUtils.getToday();
    }

    private void initViews() {
        badgeView = new BadgeView(mContext, messageBtn);
        badgeView.setTextSize(9);
//        badgeView.setBackgroundColor(Color.WHITE);
        badgeView.setBackgroundResource(R.drawable.icon_red_circle);
        badgeView.setTextColor(Color.parseColor("#ffffff"));
        badgeView.setBadgeMargin((int) DeviceUtils.dpToPixel(3));
        initRefreshListener();
        mRvView.setFocusable(false);
        mRvView.setLayoutManager(new FullyLinearLayoutManager(mContext));
        mRunnerRv.setLayoutManager(new FullyLinearLayoutManager(mContext));

        //web view
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mRaceLayout.getLayoutParams();
        layoutParams.height = (int) (DeviceUtils.getScreenWidth() * 216 / 640);
        mRaceLayout.setLayoutParams(layoutParams);
        mWebView.getSettings().setSupportZoom(false);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                //handler.cancel(); 默认的处理方式，WebView变成空白页
//                        //接受证书
                handler.proceed();
                //handleMessage(Message msg); 其他处理
            }
        });
    }

    /**
     * 刷新事件监听
     */
    private void initRefreshListener() {
        mFreshLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                if (postAble) {
                    AsyncTask postTask = new AsyncTask() {
                        @Override
                        protected Object doInBackground(Object[] params) {
                            initData();
                            if (updateViewHandler != null) {
                                updateViewHandler.removeMessages(MSG_UPDATE_RUNNER);
                            }
                            postGetWorkoutList(true);
                            return null;
                        }
                    };
                    postTask.execute();
                }
            }

            @Override
            public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
                if (postAble) {
                    AsyncTask postTask = new AsyncTask() {
                        @Override
                        protected Object doInBackground(Object[] params) {
                            postGetWorkoutList(false);
                            return null;
                        }
                    };
                    postTask.execute();
                }
            }
        });
    }


    /**
     * 获取跑步历史列表
     */
    private void postGetWorkoutList(final boolean isFresh) {
        if (!postAble) {
            return;
        }
        postAble = false;
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_GET_DYNAMIC;
        String limit = 10 + "";
        RequestParams params = RequestParamsBuild.buildGetDynamicRequest(mContext, mLastDate, limit);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                T.showShort(mContext, errorMsg);
                Message msg = new Message();
                msg.what = MSG_UPDATE_ERROR;
                msg.obj = isFresh;
                if (updateViewHandler != null) {
                    updateViewHandler.sendMessage(msg);
                }
            }

            @Override
            protected void onRightResponse(String Response) {
                if (isFresh) {
                    // 初始化列表
                    mLineDatas = new ArrayList<DynamicLineItem>();
                    sectionManager = -1;
                    sectionFirstPosition = 0;
                    headerCount = 0;
                }
                Message msg = new Message();
                msg.what = MSG_UPDATE_VIEW;
                msg.obj = isFresh;
                //若只有一条数据时不需要刷新
                if (mLineDatas != null && mLineDatas.size() == 2) {
                    if (updateViewHandler != null) {
                        updateViewHandler.sendMessage(msg);
                    }
                    return;
                }

                List<GetDynamicListResult> result = JSON.parseArray(Response, GetDynamicListResult.class);

                if (result != null) {
                    praseNewWorkout(result);
                }

                if (isFresh) {
                    PostGetFriendRequst(false);
                } else {
                    if (updateViewHandler != null) {
                        updateViewHandler.sendMessage(msg);
                    }
                }
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                T.showShort(mContext, s);
                Message msg = new Message();
                msg.what = MSG_UPDATE_ERROR;
                msg.obj = isFresh;
                if (updateViewHandler != null) {
                    updateViewHandler.sendMessage(msg);
                }
            }

            @Override
            protected void onFinish() {

            }
        });
    }


    /**
     * 解析新的跑步历史
     *
     * @param resultList
     */
    private void praseNewWorkout(List<GetDynamicListResult> resultList) {

        Calendar calendar = Calendar.getInstance();
        try {
            //解析数据
            for (int i = 0; i < resultList.size(); i++) {
                GetDynamicListResult dynamicEntity = resultList.get(i);
                Date itemDate = myFormatter.parse(dynamicEntity.getStarttime());
                boolean isTitle = false;
                // 判断时间是否满足目前的日期
                if (itemDate.after(mLastFechDay)) {
                    if (mLastFechIndexDay == -1) {
                        calendar.setTime(mLastFechDay);
                        mLastFechIndexDay++;
                        sectionManager = (sectionManager + 1) % 2;
                        sectionFirstPosition = i + headerCount;
                        headerCount += 1;
                        isTitle = true;
                    }
                    //找到满足日期
                } else {
                    for (; itemDate.before(mLastFechDay); ) {
                        calendar.setTime(mLastFechDay);
                        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 1);
                        mLastFechDay = calendar.getTime();
                    }
                    mLastFechIndexDay++;

                    sectionManager = (sectionManager + 1) % 2;
                    sectionFirstPosition = i + headerCount;
                    headerCount += 1;
                    isTitle = true;


                }
                if (isTitle) {
                    mLineDatas.add(new DynamicLineItem(TimeUtils
                            .getTimestampDayString(mLastFechDay), dynamicEntity, true));
                }
                mLineDatas.add(new DynamicLineItem(TimeUtils
                        .getTimestampDayString(mLastFechDay), dynamicEntity, false));
            }
            if (mLineDatas.size() > 0) {
                mLastDate = mLineDatas.get(mLineDatas.size() - 1).result.getStarttime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * 刷新列表
     */
    private void refreshList(boolean isFresh) {
        //刷新列表
        mAdapter = new DynamicListAdapterReplace(mContext, mLineDatas);
        mRvView.setAdapter(mAdapter);

        mRunnnerAdapter = new DynamicRunnnerRvAdapter(mContext, mRunnerList);
        mRunnerRv.setAdapter(mRunnnerAdapter);

        if (!isFresh) {
            return;
        }
        updateRunnerView();
        //比赛列表
        if (mBarUrl == null) {
            mRaceLayout.setVisibility(View.GONE);
        } else {
            mWebView.loadUrl(mBarUrl);
            mRaceLayout.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 更新跑步人员页面
     */
    private void updateRunnerView() {
        if (mRunnerList.size() == 0) {
            mSimpleRunnerLlBase.setVisibility(View.GONE);
            return;
        }
        mSimpleRunnerLlBase.setVisibility(View.VISIBLE);
        mRunnerRv.setVisibility(View.VISIBLE);
        if (mRunnnerAdapter != null) {
            mRunnnerAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 跳转到历史详情
     *
     * @param item
     */
    private void LaunchHistoryInfo(GetDynamicListResult item) {
        int TargetId = item.getUserid();
        //跳转历史
        if (TargetId == LocalApplication.getInstance().getLoginUser(mContext).getUserId()) {
            Bundle bundle = new Bundle();
            bundle.putString("workoutname", item.getStarttime());
            bundle.putInt("workoutid", item.getId());
            Intent i = new Intent(mContext, HistoryInfoActivityV2.class);
            i.putExtras(bundle);
            startActivityForResult(i, RESULT_BACK_DELECT);
        } else {
            Bundle bundle = new Bundle();
            bundle.putString("workoutname", item.getStarttime());
            bundle.putInt("workoutid", item.getId());
            bundle.putString("nickname", item.getNickname());
            bundle.putInt("userid", item.getUserid());
            bundle.putInt("Age", TimeUtils.getAgeFromBirthDay(item.getBirthdate()));
            bundle.putString("avatar", item.getAvatar());
            startActivityForResult(FriendHistoryInfoActivityV2.class, bundle, RESULT_BACK_DELECT);
        }
    }

    /**
     * 跳转到活动详情
     *
     * @param item
     */
    private void LaunchPartyInfo(GetDynamicListResult item) {
        Bundle bundle = new Bundle();
        bundle.putInt("partyid", item.getId());
        startActivity(PartyInfoActivity.class, bundle);
    }

    /**
     * 获取好友列表请求
     */
    private void PostGetFriendRequst(final boolean onlyGetRunner) {
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_GET_RUNNING_UERS;
        int userId = LocalApplication.getInstance().getLoginUser(mContext).getUserId();
        RequestParams params = RequestParamsBuild.buildGetRunningUsersListRequest(mContext);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
//                T.showShort(mContext, errorMsg);
            }

            @Override
            protected void onRightResponse(String Response) {
                GetRunningUserResult result = JSON.parseObject(Response, GetRunningUserResult.class);
                List<GetRunningUserResult.FriendsEntity> FriendList = result.getFriends();
                mRunnerList.clear();
                if (FriendList != null) {
                    mRunnerList.addAll(FriendList);
                }
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {

            }

            @Override
            protected void onFinish() {
                if (!onlyGetRunner) {
                    postGetRaceList();
                } else {
                    updateRunnerView();
                }
            }
        });
    }


    /**
     * 获取比赛列表
     */
    private void postGetRaceList() {
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_GET_RACE_LIST;
        RequestParams params = RequestParamsBuild.buildGetRaceListRequest(mContext);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                T.showShort(mContext, errorMsg);
            }

            @Override
            protected void onRightResponse(String Response) {
                GetRaceListResult result = JSON.parseObject(Response, GetRaceListResult.class);
                if (result.getRace() != null && result.getRace().size() > 0) {
                    mBarUrl = result.getRace().get(0).barh5url;
                    mAdUrl = result.getRace().get(0).h5url;
                    BANNERINTO = mAdUrl;
                } else {
                    mBarUrl = null;
                    mAdUrl = null;
                }
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {

            }

            @Override
            protected void onFinish() {
                if (updateViewHandler != null) {
                    Message msg = new Message();
                    msg.what = MSG_UPDATE_VIEW;
                    msg.obj = true;
                    updateViewHandler.sendMessage(msg);
                }
            }
        });
    }


    /**
     * 取消事件
     */
    private void cancelHandler() {
        if (updateViewHandler != null) {
            updateViewHandler.removeMessages(MSG_UPDATE_ERROR);
            updateViewHandler.removeMessages(MSG_UPDATE_VIEW);
        }
    }

    /**
     * 通知GC
     */
    private void causeGC() {
        mAdapter = null;
        mRunnnerAdapter = null;
        mRunnerList.clear();
        mLineDatas.clear();
        mLoadV.setVisibility(View.GONE);
    }

    /**
     * 更新消息显示
     */
    private void updateMessageView() {
        int userId = LocalApplication.getInstance().getLoginUser(mContext).getUserId();
        mNewMessageCount = MessageData.getAllNewMessageByUserCount(mContext, userId);
        //TEST
//        mNewMessageCount = 7;
        if (mNewMessageCount == 0) {
            badgeView.hide();
        } else {
            badgeView.show();
            if (mNewMessageCount <= 99) {
                badgeView.setText(mNewMessageCount + "");
            } else {
                badgeView.setText("…");
            }
        }
    }

    /**
     * 启动WebView界面
     */
    private void launchWebView() {
        Intent intent = new Intent();
        intent.putExtra("url", mAdUrl);
        intent.setClass(mContext, RaceWebViewActivity.class);
        mContext.startActivity(intent);
    }


    /**
     * 启动扫描页面
     */
    private void launchQrCode() {
        Bundle bundle = new Bundle();
        bundle.putInt("hasScanSys", 1);
        startActivity(ScanQRCodeActivityReplace.class, bundle);
    }

}
