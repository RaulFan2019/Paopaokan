package com.app.pao.activity.workout;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.adapter.HistoryInfoPagerAdapter;
import com.app.pao.config.AppEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.data.PreferencesData;
import com.app.pao.data.db.MaidianData;
import com.app.pao.data.db.UploadData;
import com.app.pao.data.db.WorkoutData;
import com.app.pao.entity.db.DBEntityMaidian;
import com.app.pao.entity.db.DBEntityWorkout;
import com.app.pao.entity.db.DBUserEntity;
import com.app.pao.entity.event.EventComment;
import com.app.pao.entity.event.EventRecord;
import com.app.pao.entity.event.EventSwitchFragment;
import com.app.pao.entity.network.GetPlaybackCameraListResult;
import com.app.pao.entity.network.GetSocialListResult;
import com.app.pao.entity.network.GetWorkoutFullEntity;
import com.app.pao.entity.network.GetWxInviteResult;
import com.app.pao.fragment.history.HistoryBasicInfoFragmentV2;
import com.app.pao.fragment.history.HistoryHeartrateInfoFragmentV2;
import com.app.pao.fragment.history.HistorySpliteListFragmentV2;
import com.app.pao.network.api.MyRequestCallBack;
import com.app.pao.network.utils.RequestParamsBuild;
import com.app.pao.ui.dialog.v1.MyDialogBuilderV1;
import com.app.pao.ui.widget.CustomViewPager;
import com.app.pao.ui.widget.LoadingView;
import com.app.pao.utils.Log;
import com.app.pao.utils.T;
import com.app.pao.utils.TimeUtils;
import com.app.pao.utils.wx.WxShareManager;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.rey.material.widget.ImageButton;
import com.rey.material.widget.TabPageIndicator;
import com.rey.material.widget.TextView;
import com.app.pao.adapter.HistoryInfoPagerAdapter.Tab;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raul.Fan on 2016/5/3.
 */
@ContentView(R.layout.activity_history_info_v2)
public class HistoryInfoActivityV2 extends BaseAppCompActivity implements View.OnClickListener {

    private static final String TAG = "HistoryInfoActivityV2";
    /* contains */
    private static final int MSG_POST_ERROR = 1;
    private static final int MSG_POST_OK = 2;
    private static final int MSG_GET_COMMENTS_AND_THUMB = 4;
    private static final int MSG_UPDATE_VIEW = 5;
    private static final int MSG_POST_VIDEO_OVER = 6;

    /* local data */
    private String mWorkoutName;
    private DBEntityWorkout mDBWorkout;//跑步历史
    private int workoutId;//跑步历史id
    private String mResponse;//跑步历史Json数据

    private Tab[] mItems;
    private HistoryInfoPagerAdapter mPagerAdapter;//Tab适配器
    private FragmentManager mFragMgr;
    private int mLastFragment = 1;//跳转前页面显示位置

    private doLoadHistoryTask mLoadHistoryTask;//分析跑步历史数据

    private List<GetSocialListResult.SocialsEntity> socialList = new ArrayList<>();
    private List<GetPlaybackCameraListResult.VideoEntity> mVieoList = new ArrayList<>();//视频列表


    private Fragment mContentFragment; // 当前显示Fragment
    private HistoryBasicInfoFragmentV2 mBasicFragment;
    private HistorySpliteListFragmentV2 mSplitFragment;
    private HistoryHeartrateInfoFragmentV2 mHeartrateFragment;

    private RotateAnimation animation;//正在同步的动画

    /* local view */
    @ViewInject(R.id.ll_activity_base)
    private LinearLayout mBaseLl;
    @ViewInject(R.id.btn_sync)
    private ImageButton mSyncBtn;//同步按钮
    @ViewInject(R.id.share)
    private ImageButton mShareBtn;//分享按钮
    @ViewInject(R.id.tbi_history)
    private TabPageIndicator mTpi;//Tab控件
    @ViewInject(R.id.vp_history)
    private CustomViewPager mViewPager;//ViewPager


    @ViewInject(R.id.rl_load)
    private LinearLayout mLoadViewRl;//加载布局
    @ViewInject(R.id.loadview)
    private LoadingView mLoadV;//加载动画控件
    @ViewInject(R.id.tv_reload)
    private TextView mReloadTv;//加载文字
    @ViewInject(R.id.ll_title_right)
    private LinearLayout mTitleRll;
    @ViewInject(R.id.tv_title_loading)
    private TextView mLoadTitleTv;//标题加载文字

    /**
     * 请求返回处理
     */
    Handler mPostHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                //网络请求失败
                case MSG_POST_ERROR:
                    showPostError((String) msg.obj);
                    break;
                //请求成功
                case MSG_POST_OK:
                    postGetCameraList();
                    break;
                //获取视频列表结束
                case MSG_POST_VIDEO_OVER:
                    postGetThumbsAndComments();
                    break;
                case MSG_GET_COMMENTS_AND_THUMB:
//                    Log.v(TAG, "socialList.size(): " + socialList.size());
                    mLoadV.setLoadingText("正在解析数据...");
                    mLoadHistoryTask = new doLoadHistoryTask();
                    mLoadHistoryTask.execute(mResponse);
                    break;

                case MSG_UPDATE_VIEW:
                    updateViewsAfterParse();
                    break;
            }
        }
    };

    /**
     * Fragment 切换事件
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EventSwitchFragment event) {
        if (event.type == EventSwitchFragment.TYPE_HISTORY) {
            if (event.event == EventSwitchFragment.EVENT_HISTORY_HEARTRATE) {
                mViewPager.setCurrentItem(2);
            } else if (event.event == EventSwitchFragment.EVENT_HISTORY_SPLIT) {
                mViewPager.setCurrentItem(1);
            }
        }
    }

    /**
     * 有新的Record产生
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EventRecord event) {
        mShareBtn.setVisibility(View.VISIBLE);
        mSyncBtn.clearAnimation();
        mSyncBtn.setVisibility(View.GONE);
    }


    @Override
    @OnClick({R.id.view_guid, R.id.title_bar_left_menu, R.id.share, R.id.btn_sync,
            R.id.delete, R.id.tv_reload})
    public void onClick(View v) {
        //点击返回
        if (v.getId() == R.id.title_bar_left_menu) {
            rebackChanged();
            //点击分享
        } else if (v.getId() == R.id.share) {
            MaidianData.saveMaidian(mContext, new DBEntityMaidian(System.currentTimeMillis() + "",
                    AppEnum.MaidianType.ShareRunningRecord, TimeUtils.NowTime()));
            shareHistory();
            //点击删除
        } else if (v.getId() == R.id.delete) {
            showDeleteDialog();
            //点击重新加载
        } else if (v.getId() == R.id.tv_reload) {
            mLoadV.setLoadingText("加载中..");
            mReloadTv.setVisibility(View.INVISIBLE);
            doMyOnCreate();
            //点击同步按钮
        } else if (v.getId() == R.id.btn_sync) {
            int count = UploadData.getSyncCount(mContext, mWorkoutName);
            if (count != 0) {
                showUploadingDialog();
            } else {
                T.showShort(mContext, "同步完成，可以分享跑步记录啦");
                mShareBtn.setVisibility(View.VISIBLE);
                mSyncBtn.clearAnimation();
                mSyncBtn.setVisibility(View.GONE);
            }
        }
    }


    @Override
    protected void initData() {
        //获取跑步历史数据
        mWorkoutName = getIntent().getExtras().getString("workoutname");
        mDBWorkout = WorkoutData.getWorkoutByName(mContext, mWorkoutName);
        if (mDBWorkout != null && mDBWorkout.workoutId > 0) {
            workoutId = mDBWorkout.getWorkoutId();
        } else {
            workoutId = getIntent().getExtras().getInt("workoutid");
        }
        mBasicFragment = HistoryBasicInfoFragmentV2.newInstance();
        mSplitFragment = HistorySpliteListFragmentV2.newInstance();
        mHeartrateFragment = HistoryHeartrateInfoFragmentV2.newInstance();
        mFragMgr = getSupportFragmentManager();
    }

    @Override
    protected void initViews() {
        getWindow().setFormat(PixelFormat.TRANSLUCENT);//设置此项可以防止切换Fragment时屏幕闪屏的问题
        EventBus.getDefault().register(this);
        /* 标题 */
        mLoadTitleTv.setVisibility(View.VISIBLE);
        mLoadTitleTv.setText("正在加载中....");
        mTitleRll.setVisibility(View.INVISIBLE);


        if (LocalApplication.getInstance().getLoginUser(mContext).userId == AppEnum.DEFAULT_USER_ID) {
            mShareBtn.setVisibility(View.GONE);
            mSyncBtn.setVisibility(View.GONE);
        }
        /* TAB */
        mItems = new Tab[]{Tab.BASIC, Tab.SPLIT, Tab.HEARTRATE};
        mPagerAdapter = new HistoryInfoPagerAdapter(getSupportFragmentManager(), mItems, new Fragment[]{mBasicFragment, mSplitFragment, mHeartrateFragment});
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);

        mTpi.setViewPager(mViewPager);
        mTpi.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
//                if (position == 0) {
//                    mViewPager.setNoScroll(true);
//                } else {
//                    mViewPager.setNoScroll(false);
//                }
                imm.hideSoftInputFromWindow(mSyncBtn.getWindowToken(), 0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }

        });
//        mViewPager.setNoScroll(true);
        mViewPager.setCurrentItem(0);

        mBaseLl.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {

                //比较Activity根布局与当前布局的大小
                int heightDiff = mBaseLl.getRootView().getHeight() - mBaseLl.getHeight();
                if (heightDiff > 100) {
                    //大小超过100时，一般为显示虚拟键盘事件
//                    Log.v(TAG,"show keyboard");
                } else {
//                    Log.v(TAG,"hide keyboard");
                    //大小小于100时，为不显示虚拟键盘或虚拟键盘隐藏
                    DBUserEntity userEntity = LocalApplication.getInstance().getLoginUser(mContext);
                    EventBus.getDefault().post(new EventComment(0, 0, 0, 0, userEntity.nickname, userEntity.gender, userEntity.avatar, userEntity.name));
                }
            }
        });

        /** 设置旋转动画 */
        animation = new RotateAnimation(360f, 0f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(100);//重复次数
        animation.setDuration(1000);
        mSyncBtn.setAnimation(animation);
    }

    @Override
    protected void doMyOnCreate() {
        // 若数据库存在直接从本地刷新界面
        if (mDBWorkout != null) {
            postGetCameraList();
        } else {
            postGetWorkoutFullDataRequest();
        }
    }

    @Override
    protected void updateData() {

    }

    @Override
    protected void updateViews() {
        if (UploadData.getSyncCount(mContext, mWorkoutName) != 0) {
            mShareBtn.setVisibility(View.GONE);
            mSyncBtn.setVisibility(View.VISIBLE);
            mSyncBtn.startAnimation(animation);
            animation.start();
        } else {
            mShareBtn.setVisibility(View.VISIBLE);
            animation.cancel();
            mSyncBtn.clearAnimation();
            mSyncBtn.setVisibility(View.GONE);
        }
    }

    @Override
    protected void destroy() {
        EventBus.getDefault().unregister(this);
        cancelHandler();
        cancelTask();
        causeGC();
    }

    @Override
    public void onBackPressed() {
        rebackChanged();
    }

    private void cancelTask() {
        if (mLoadHistoryTask != null) {
            mLoadHistoryTask.cancel(true);
            LocalApplication.getInstance().getDbUtils(mContext).close();
            LocalApplication.getInstance().setDbUtils(null);
        }
    }

    /**
     * 结束Handler事件
     */
    private void cancelHandler() {
        if (mPostHandler != null) {
            mPostHandler.removeMessages(MSG_GET_COMMENTS_AND_THUMB);
            mPostHandler.removeMessages(MSG_POST_ERROR);
            mPostHandler.removeMessages(MSG_POST_OK);
            mPostHandler.removeMessages(MSG_POST_VIDEO_OVER);
            mPostHandler.removeMessages(MSG_UPDATE_VIEW);
            mPostHandler = null;
        }
    }

    /**
     * 通知GC
     */
    private void causeGC() {
        mDBWorkout = null;
        socialList.clear();
        mVieoList.clear();
        mLoadV.setVisibility(View.GONE);
    }


    /**
     * 获取所有workout数据
     */
    private void postGetWorkoutFullDataRequest() {
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_GET_WORKOUT_INFO;
        RequestParams params = RequestParamsBuild.buildGetWorkoutInforRequest(mContext, workoutId);
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
                if (mPostHandler != null) {
                    Message msg = new Message();
                    msg.what = MSG_POST_OK;
                    mResponse = Response;
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

    /**
     * 获取视频数据
     */
    private void postGetCameraList() {
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_GET_PLAYBACK_CAMERA_LIST;
        RequestParams params = RequestParamsBuild.buildGetPlaybackCameraListRequest(mContext, workoutId);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {
            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {

            }

            @Override
            protected void onRightResponse(String Response) {
                GetPlaybackCameraListResult result = JSON.parseObject(Response, GetPlaybackCameraListResult.class);
                if (result.getVideo() != null) {
                    mVieoList.clear();
                    mVieoList.addAll(result.getVideo());
                }
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {

            }

            @Override
            protected void onFinish() {
                if (mPostHandler != null) {
                    mPostHandler.sendEmptyMessage(MSG_POST_VIDEO_OVER);
                }
            }
        });
    }

    /**
     * 获取点赞和评论列表
     */
    private void postGetThumbsAndComments() {
//        Log.v(TAG,"postGetThumbsAndComments");
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_GET_SOCIAL_LIST;
        RequestParams params = RequestParamsBuild.buildGetSocialListRequest(mContext, workoutId, 1);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {
            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                Log.v(TAG, "onErrorResponse errorMsg:" + errorMsg);
            }

            @Override
            protected void onRightResponse(String Response) {
                GetSocialListResult result = JSON.parseObject(Response, GetSocialListResult.class);
                Log.v(TAG, "result.socials != null: " + (result.socials != null));
                if (result.getSocials() != null) {
                    socialList.clear();
                    socialList.addAll(result.socials);
                    Log.v(TAG, "postGetThumbsAndComments socialList.size(): " + socialList.size());
                }
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {

            }

            @Override
            protected void onFinish() {
                if (mPostHandler != null) {
                    mPostHandler.sendEmptyMessage(MSG_GET_COMMENTS_AND_THUMB);
                }
            }
        });
    }

    /**
     * 分享历史
     */
    private void shareHistory() {
        if (mDialogBuilder.progressDialog != null && mDialogBuilder.progressDialog.isShowing()) {
            return;
        }
        mDialogBuilder.showProgressDialog(mContext, "正在获取分享信息..", false);
        // 发送请求
        HttpUtils https = new HttpUtils();
        String POST_URL = URLConfig.URL_SHARE_WORKOUT;
        int UserId = LocalApplication.getInstance().getLoginUser(mContext).getUserId();
        final RequestParams params = RequestParamsBuild.BuildGetWorkoutShareByWorkoutNameRequest(mContext, UserId, mDBWorkout.starttime);
        https.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                T.showShort(mContext, errorMsg);
            }

            @Override
            protected void onRightResponse(String Response) {
                final GetWxInviteResult result = JSON.parseObject(Response, GetWxInviteResult.class);
                if (result.getTitle() != null && !result.getTitle().isEmpty()) {
                    final WxShareManager share = new WxShareManager(HistoryInfoActivityV2.this);
                    mDialogBuilder.setShareWxFriendDialog(mContext, "分享跑步记录到");
                    mDialogBuilder.setListener(new MyDialogBuilderV1.ShareWxFriendDialogListener() {
                        @Override
                        public void onWxFriendClick() {
                            share.startWxShareUrlByCustom(SHARE_MEDIA.WEIXIN, result.text, result.title, result.link, result.image);
                        }

                        @Override
                        public void onWxFriendCircleClick() {
                            share.startWxShareUrlByCustom(SHARE_MEDIA.WEIXIN_CIRCLE, result.text, result.title, result.link, result.image);
                        }

                        @Override
                        public void onCancle() {

                        }
                    });
                }
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                T.showShort(mContext, s);
            }

            @Override
            protected void onFinish() {
                if (mDialogBuilder.progressDialog != null) {
                    mDialogBuilder.progressDialog.dismiss();
                }
            }
        });
    }


    /**
     * 显示删除历史的对话框
     */
    private void showDeleteDialog() {
        mDialogBuilder.showChoiceTwoBtnDialog(mContext, "警告", "删除跑步记录后将不能恢复", "取消", "删除");
        mDialogBuilder.setListener(new MyDialogBuilderV1.ChoiceTwoBtnDialogListener() {
            @Override
            public void onLeftBtnClick() {

            }

            @Override
            public void onRightBtnClick() {
                deleteHistory();
            }

            @Override
            public void onCancel() {

            }
        });
    }


    /**
     * 删除历史
     */
    private void deleteHistory() {
        if (mDialogBuilder.progressDialog != null && mDialogBuilder.progressDialog.isShowing()) {
            return;
        }
        if (mDBWorkout == null) {
            return;
        }
        mDialogBuilder.showProgressDialog(mContext, "正在删除跑步记录..", false);
        // 发送请求
        HttpUtils https = new HttpUtils();
        String POST_URL = URLConfig.URL_REMOVE_WORKOUT_INFO;
        final RequestParams params = RequestParamsBuild.removeWorkout(mContext, mDBWorkout.getName());
        https.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                T.showShort(mContext, errorMsg);
            }

            @Override
            protected void onRightResponse(String Response) {
                WorkoutData.deleteWorkout(mContext, mDBWorkout.getName());
                Intent i = new Intent();
                i.putExtra("isDelete", true);
                i.putExtra("starttime", mDBWorkout.starttime);
                setResult(RESULT_OK, i);
                T.showShort(mContext, "删除成功");
                finish();
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                T.showShort(mContext, s);
            }

            @Override
            protected void onFinish() {
                if (mDialogBuilder.progressDialog != null) {
                    mDialogBuilder.progressDialog.dismiss();
                }
            }
        });
    }

    /**
     * 显示同步对话框
     */
    private void showUploadingDialog() {
        ImageSpan span = new ImageSpan(this, R.drawable.icon_dialog_share);
        SpannableString spanStr = new SpannableString("请等待跑步记录上传完毕，\n 就可以分享 1 了");
        spanStr.setSpan(span, spanStr.length() - 3, spanStr.length() - 2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        mDialogBuilder.showSimpleMsgDialog(mContext, "跑步记录上传中", spanStr);
    }

    /**
     * 显示加载错误
     */
    private void showPostError(final String error) {
        T.showShort(mContext, error);
        mLoadV.setLoadingText("加载失败");
        mReloadTv.setVisibility(View.VISIBLE);
    }


    /**
     * 解析后显示页面
     */
    private void updateViewsAfterParse() {
        //去除缓冲提示
        mLoadV.setVisibility(View.GONE);
        mLoadViewRl.setVisibility(View.GONE);
        mTitleRll.setVisibility(View.VISIBLE);
        mLoadTitleTv.setVisibility(View.GONE);
        mTpi.setVisibility(View.VISIBLE);
        //更新Fragment数据
        mSplitFragment.updateView();
        mBasicFragment.updateWorkoutView();
        mHeartrateFragment.updateViewFromActivity();
        mViewPager.setView(mBasicFragment.mBaseLl);
    }


    /**
     * 启用线程处理历史数据
     */
    class doLoadHistoryTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            if (params[0] != null) {
                GetWorkoutFullEntity result = JSON.parseObject(params[0], GetWorkoutFullEntity.class);
                //保存数据至数据库
                WorkoutData.saveFullWorkout(mContext, result);
                result = new GetWorkoutFullEntity();
                mDBWorkout = WorkoutData.getWorkoutByName(mContext, mWorkoutName);
            }
            return params[0];
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (mPostHandler != null) {
                mPostHandler.sendEmptyMessage(MSG_UPDATE_VIEW);
            }
        }
    }


    /**
     * 返回评论点赞变化数据
     */
    private void rebackChanged() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * get and set
     **/
    public DBEntityWorkout getDBWorkout() {
        return mDBWorkout;
    }

    public String getWorkoutName() {
        return mWorkoutName;
    }

    public List<GetSocialListResult.SocialsEntity> getSocialList() {
        return socialList;
    }

    public List<GetPlaybackCameraListResult.VideoEntity> getVieoList() {
        return mVieoList;
    }
}
