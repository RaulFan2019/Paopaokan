package com.app.pao.activity.workout;

import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.amap.api.maps.model.LatLng;
import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.activity.BaseAppCompActivityV2;
import com.app.pao.adapter.HistoryVideoListAdapter;
import com.app.pao.adapter.HistoryVideoSocialAdapter;
import com.app.pao.config.AppEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.data.db.MaidianData;
import com.app.pao.data.db.UploadData;
import com.app.pao.entity.db.DBEntityLocation;
import com.app.pao.entity.db.DBEntityMaidian;
import com.app.pao.entity.db.DBUploadEntity;
import com.app.pao.entity.db.DBUserEntity;
import com.app.pao.entity.event.EventComment;
import com.app.pao.entity.event.EventSendSocial;
import com.app.pao.entity.network.GetPlaybackCameraListResult;
import com.app.pao.entity.network.GetSocialListResult;
import com.app.pao.entity.network.GetWxInviteResult;
import com.app.pao.network.api.MyRequestCallBack;
import com.app.pao.network.utils.RequestParamsBuild;
import com.app.pao.ui.dialog.v1.MyDialogBuilderV1;
import com.app.pao.ui.widget.CircularImage;
import com.app.pao.ui.widget.FullyLinearLayoutManager;
import com.app.pao.utils.DeviceUtils;
import com.app.pao.utils.ImageUtils;
import com.app.pao.utils.Log;
import com.app.pao.utils.NumUtils;
import com.app.pao.utils.T;
import com.app.pao.utils.TimeUtils;
import com.app.pao.utils.wx.WxShareManager;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.tencent.qcload.playersdk.ui.UiChangeInterface;
import com.tencent.qcload.playersdk.ui.VideoRootFrame;
import com.tencent.qcload.playersdk.util.PlayerListener;
import com.tencent.qcload.playersdk.util.VideoInfo;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raul.Fan on 2016/5/10.
 * 跑步历史视频列表
 */
@ContentView(R.layout.activity_history_video_list)
public class HistoryVideoListActivity extends BaseAppCompActivity implements View.OnClickListener {

    private static final String TAG = "HistoryVideoListActivity";

    /* local view*/
    @ViewInject(R.id.player)
    private VideoRootFrame mPlayer;
    @ViewInject(R.id.tv_camera_name)
    private TextView mCameraNameTv;
    @ViewInject(R.id.fl_video)
    private FrameLayout mVideoFl;
    @ViewInject(R.id.ll_video)
    private LinearLayout mVideoLl;

    @ViewInject(R.id.rv_video)
    private RecyclerView mVideoRv;
    @ViewInject(R.id.rv_social)
    private RecyclerView mSocialRv;

    @ViewInject(R.id.ll_input)
    private LinearLayout mInputLl;
    @ViewInject(R.id.et_social)
    private EditText mSocialEt;
    @ViewInject(R.id.iv_mine)
    private CircularImage mAvatarIv;
    @ViewInject(R.id.tv_video_count)
    private TextView mVideoCountTv;
    @ViewInject(R.id.tv_comments_count)
    private TextView mSocialCountTv;
    @ViewInject(R.id.sv)
    private ScrollView mSv;
    @ViewInject(R.id.v_video_control)
    private Button mVideoControlV;
    @ViewInject(R.id.ll_video_control)
    private LinearLayout mTll;

    /* local data */
    private List<VideoInfo> videos = new ArrayList<VideoInfo>();
    private int mCurrPos;
    private List<GetPlaybackCameraListResult.VideoEntity> mDataList;
    private List<GetSocialListResult.SocialsEntity> mSocialsEntityList = new ArrayList<>();

    private int mWorkoutId;
    private int mOwnerId;

    private BitmapUtils mBitmapU;
    private HistoryVideoListAdapter mVideoAdapter;
    private HistoryVideoSocialAdapter mSocialAdapter;

    private EventComment mCurrCommentEvent;//当前选中的评论位置

    private boolean isFullScreen = false;
    private boolean isShowVideoList = false;


    Handler FHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x01) {
                if (mSocialEt != null) {
                    mSocialEt.setVisibility(View.VISIBLE);
                }
//                mSocialEt.setFocusable(true);
//                imm.hideSoftInputFromWindow(mSocialEt.getWindowToken(), 0);
            }
        }
    };

    /**
     * 评论发生变化
     *
     * @param eventComment
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EventComment eventComment) {
        mCurrCommentEvent = eventComment;
        if (mCurrCommentEvent.replayUserId == 0) {
            mSocialEt.setHint("我来说两句");
        } else {
            mSocialEt.setHint("回复" + mCurrCommentEvent.replayUserNickName + ": ");
        }
        mSocialEt.requestFocus();
        imm.showSoftInput(mSocialEt, 0);
    }

    @Override
    @OnClick({R.id.btn_prev, R.id.btn_next, R.id.btn_back, R.id.btn_share,
            R.id.ll_share, R.id.ll_share_wx_friend, R.id.ll_share_wx_friend_circle,
            R.id.fullscreen, R.id.ll_video_control})
    public void onClick(View v) {
        //点击下一个视频
        if (v.getId() == R.id.btn_next) {
            if (mCurrPos == (mDataList.size() - 1)) {
                T.showShort(mContext, "这已经是最后一个镜头啦");
            } else {
                mCurrPos++;
                mPlayer.release();
                getCurrPlayVideoList();
                mPlayer.play(videos);
                mVideoAdapter.highlightItem(mCurrPos);
            }
            //点击上一个视频
        } else if (v.getId() == R.id.btn_prev) {
            if (mCurrPos == 0) {
                T.showShort(mContext, "没有更前面的镜头啦");
            } else {
                mCurrPos--;
                mPlayer.release();
                getCurrPlayVideoList();
                mPlayer.play(videos);
                mVideoAdapter.highlightItem(mCurrPos);
            }
            //点击返回
        } else if (v.getId() == R.id.btn_back) {
            if (isFullScreen) {
                //播放器全屏时，将页面设置为竖屏状态
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mVideoLl.getLayoutParams();
                lp.height = (int) DeviceUtils.dpToPixel(202);
                mSv.setVisibility(View.VISIBLE);
                mInputLl.setVisibility(View.VISIBLE);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                isFullScreen = !isFullScreen;
            } else {
                mPlayer.release();
                finish();
            }
            //点击分享
        } else if (v.getId() == R.id.btn_share) {
            MaidianData.saveMaidian(mContext, new DBEntityMaidian(System.currentTimeMillis() + "",
                    AppEnum.MaidianType.SharePersonalVideo, TimeUtils.NowTime()));
            if (LocalApplication.getInstance().getLoginUser(mContext).userId == mOwnerId) {
                shareHistoryVideo(mDataList.get(mCurrPos).getId());
            } else {
                SpannableString string = new SpannableString("抱歉不能分享他人视频");
                mDialogBuilder.showSimpleMsgDialog(mContext, "提示", string);
            }
        } else if (v.getId() == R.id.fullscreen) {
            if (isFullScreen) {
                //播放器全屏时，将页面设置为竖屏状态
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mVideoLl.getLayoutParams();
                lp.height = (int) DeviceUtils.dpToPixel(202);
                mSv.setVisibility(View.VISIBLE);
                mInputLl.setVisibility(View.VISIBLE);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            } else {
                //播放器非全屏时，将页面设置为横屏状态，此时播放器控件宽度自适应到屏幕宽度实现全屏
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mVideoLl.getLayoutParams();
                lp.height = lp.MATCH_PARENT;
                mSv.setVisibility(View.GONE);
                mInputLl.setVisibility(View.GONE);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
            isFullScreen = !isFullScreen;
            //点击视频控制区域
        } else if (v.getId() == R.id.ll_video_control) {
            if (isShowVideoList) {
                mVideoRv.setVisibility(View.GONE);
                mVideoControlV.setBackgroundResource(R.drawable.icon_down);
            } else {
                mVideoRv.setVisibility(View.VISIBLE);
                mVideoControlV.setBackgroundResource(R.drawable.icon_up);
            }
            isShowVideoList = !isShowVideoList;
        }
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        mDataList = (List<GetPlaybackCameraListResult.VideoEntity>) getIntent().getExtras().get("list");
        DBUserEntity userEntity = LocalApplication.getInstance().getLoginUser(mContext);
        mCurrCommentEvent = new EventComment(0, 0, 0, 0, userEntity.nickname, userEntity.gender, userEntity.avatar, userEntity.name);
        mCurrPos = getIntent().getExtras().getInt("pos");
        mWorkoutId = getIntent().getExtras().getInt("workoutid");
        mOwnerId = getIntent().getExtras().getInt("userid");

        mBitmapU = new BitmapUtils(mContext);
        mVideoAdapter = new HistoryVideoListAdapter(mContext, mDataList);
        mSocialAdapter = new HistoryVideoSocialAdapter(mContext, mSocialsEntityList);
        mVideoAdapter.setOnItemClickListener(new HistoryVideoListAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(int postion) {
                mVideoAdapter.highlightItem(postion);
                mCurrPos = postion;
                mPlayer.release();
                getCurrPlayVideoList();
                mPlayer.release();
                mPlayer.play(videos);
            }
        });
        mVideoAdapter.highlightItem(mCurrPos);
        getCurrPlayVideoList();
    }

    @Override
    protected void initViews() {
        mVideoRv.setFocusable(false);
        mSocialRv.setFocusable(false);
        mVideoControlV.requestFocus();
        mSocialEt.setVisibility(View.GONE);
//        mSocialEt.setFocusable(false);
//        mSocialEt.clearFocus();
        imm.hideSoftInputFromWindow(mSocialEt.getWindowToken(), 0);
        ImageUtils.loadUserImage(LocalApplication.getInstance().getLoginUser(mContext).avatar, mAvatarIv);
        mVideoCountTv.setText("共" + mDataList.size() + "段个人视频");

        mVideoRv.setLayoutManager(new FullyLinearLayoutManager(mContext));
        mVideoRv.setAdapter(mVideoAdapter);

        mSocialRv.setLayoutManager(new FullyLinearLayoutManager(mContext));
        mSocialRv.setAdapter(mSocialAdapter);

        //监听编辑框的Action
        mSocialEt.setOnEditorActionListener(new android.widget.TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(android.widget.TextView v, int actionId, KeyEvent event) {
                if (!mSocialEt.getText().toString().equals("")) {
                    addNewComments(mDataList.get(mCurrPos).id, mCurrCommentEvent.replayUserId, mSocialEt.getText().toString(),
                            mCurrCommentEvent.replayUserNickName, mCurrCommentEvent.replayUserName,
                            mCurrCommentEvent.replayUserGender, mCurrCommentEvent.replayUserAvatar);
                }
                return false;
            }
        });
        mPlayer.setListener(new PlayerListener() {

            @Override
            public void onError(Exception arg0) {
                arg0.printStackTrace();
            }

            /**
             * 播放器状态监听器
             * 返回码
             * 1 STATE_IDLE 播放器空闲，既不在准备也不在播放
             * 2 STATE_PREPARING 播放器正在准备
             * 3 STATE_BUFFERING 播放器已经准备完毕，但无法立即播放。此状态的原因有很
             多，但常见的是播放器需要缓冲更多数据才能开始播放
             * 4 STATE_PAUSE 播放器准备好并可以立即播放当前位置
             * 5 STATE_PLAY 播放器正在播放中
             * 6 STATE_ENDED 播放已完毕
             */
            @Override
            public void onStateChanged(int arg0) {
                if (arg0 == 2) {
//                    T.showShort(mContext, "正在播放:" + mPlayer.getgetContentDescription());
                }
//                Log.d(TAG, "player states:" + arg0);
            }
        });
        mPlayer.setToggleFullScreenHandler(new UiChangeInterface() {
            @Override
            public void OnChange() {
                if (mPlayer.isFullScreen()) {
                    //播放器全屏时，将页面设置为竖屏状态
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                } else {
                    //播放器非全屏时，将页面设置为横屏状态，此时播放器控件宽度自适应到屏幕宽度实现全屏
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
            }
        });
    }

    @Override
    protected void doMyOnCreate() {
        mPlayer.play(videos);
        FHandler.sendEmptyMessageDelayed(0x01, 1000);
    }

    @Override
    protected void updateData() {

    }

    @Override
    protected void updateViews() {

    }

    @Override
    protected void destroy() {
        EventBus.getDefault().unregister(this);
        causeGC();
    }

    private void getCurrPlayVideoList() {
        postGetSocial(mDataList.get(mCurrPos).id);
        videos.clear();
        VideoInfo videoInfo = new VideoInfo();
        videoInfo.description = "视频" + (mCurrPos + 1);
        videoInfo.type = VideoInfo.VideoType.HLS;
        videoInfo.url = mDataList.get(mCurrPos).getUrl();
        videos.add(videoInfo);
        mCameraNameTv.setText(videoInfo.description);
    }

    /**
     * 分享历史
     */
    private void shareHistoryVideo(int videoid) {
        if (mDialogBuilder.progressDialog != null && mDialogBuilder.progressDialog.isShowing()) {
            return;
        }
        mDialogBuilder.showProgressDialog(mContext, "正在获取分享信息..", false);
        // 发送请求
        HttpUtils https = new HttpUtils();
        String POST_URL = URLConfig.URL_SHARE_HISTORY_VIDEO;
        int UserId = LocalApplication.getInstance().getLoginUser(mContext).getUserId();
        final RequestParams params = RequestParamsBuild.BuildGetHsitoryVideoShareRequest(mContext, mWorkoutId, videoid);
        https.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                T.showShort(mContext, errorMsg);
            }

            @Override
            protected void onRightResponse(String Response) {
                final GetWxInviteResult result = JSON.parseObject(Response, GetWxInviteResult.class);
                if (result.getTitle() != null && !result.getTitle().isEmpty()) {
                    final WxShareManager share = new WxShareManager(HistoryVideoListActivity.this);
                    mDialogBuilder.setShareWxFriendDialog(mContext, "分享视频到");
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
     * 获取点赞和评论列表
     */
    private void postGetSocial(int videoId) {
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_GET_SOCIAL_LIST;
        RequestParams params = RequestParamsBuild.buildGetSocialListRequest(mContext, videoId, 3);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {
            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
            }

            @Override
            protected void onRightResponse(String Response) {
                GetSocialListResult result = JSON.parseObject(Response, GetSocialListResult.class);
                if (result.getSocials() != null) {
                    mSocialsEntityList.clear();
                    mSocialsEntityList.addAll(result.socials);
                    mSocialCountTv.setText(mSocialsEntityList.size() + "");
                    mSocialAdapter.notifyDataSetChanged();
                }
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {

            }

            @Override
            protected void onFinish() {

            }
        });
    }

    private void causeGC() {
        videos.clear();
        mDataList.clear();
    }

    /**
     * 发送评论
     */
    public void addNewComments(final int videoId, final int replayUserId, final String comment,
                               final String replayUserNickName, final String replayUserName,
                               final int replayUserGender, final String replayUserAvatar) {
        //发送评论
        String info = videoId + ":" + AppEnum.dynamicType.VIDEO
                + ":" + replayUserId + ":" + comment;

        DBUploadEntity uploadEntity = new DBUploadEntity(DBUploadEntity.TYPE_COMMENTS, URLConfig
                .URL_ADD_COMMENT, info
                , "", LocalApplication.getInstance().getLoginUser(mContext).getUserId());
        UploadData.createNewUploadData(mContext, uploadEntity);

        //改变列表
        DBUserEntity user = LocalApplication.getInstance().getLoginUser(mContext);
        GetSocialListResult.SocialsEntity result = new GetSocialListResult.SocialsEntity();
        result.intsocialtime = System.currentTimeMillis();
        result.socialtime = TimeUtils.NowTime();
        result.socialtype = 2;
        result.userid = user.userId;
        result.id = 0;
        result.longitude = 0;
        result.latitude = 0;
        result.comment = comment;
        result.mediatype = 1;
        result.mediaurl = "";
        result.length = 0;
        result.username = user.name;
        result.nickname = user.nickname;
        result.gender = user.gender;
        result.avatar = user.avatar;
        result.replyuserid = replayUserId;
        result.replyusername = replayUserName;
        result.replynickname = replayUserNickName;
        result.replygender = replayUserGender;
        result.replyavatar = replayUserAvatar;
        result.position = 0;
        result.isSelect = true;
        result.readnow = false;
        mSocialsEntityList.add(0, result);
        mSocialCountTv.setText(mSocialsEntityList.size() + "");
        mSocialAdapter.notifyDataSetChanged();

        T.showShort(mContext, "发送消息成功");
        mSv.smoothScrollTo(0, (int) mSocialRv.getY());
        mSocialEt.setText("");
//        mSocialRv.setFocusable(true);
//        mSocialRv.requestFocus();
//        mSocialRv.scrollToPosition(0);
    }

}
