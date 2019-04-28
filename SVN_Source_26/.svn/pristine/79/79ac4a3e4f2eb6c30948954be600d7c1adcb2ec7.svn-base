package com.app.pao.activity.workout;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.config.AppEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.data.db.MaidianData;
import com.app.pao.entity.db.DBEntityMaidian;
import com.app.pao.entity.network.GetPlaybackCameraListResult;
import com.app.pao.entity.network.GetWxInviteResult;
import com.app.pao.network.api.MyRequestCallBack;
import com.app.pao.network.utils.RequestParamsBuild;
import com.app.pao.utils.DeviceUtils;
import com.app.pao.utils.Log;
import com.app.pao.utils.NumUtils;
import com.app.pao.utils.T;
import com.app.pao.utils.TimeUtils;
import com.app.pao.utils.wx.WxShareManager;
import com.google.android.exoplayer.audio.AudioCapabilities;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.tencent.qcload.playersdk.ui.PlayerActionInterface;
import com.tencent.qcload.playersdk.ui.TitleMenu;
import com.tencent.qcload.playersdk.ui.VideoControllerView;
import com.tencent.qcload.playersdk.ui.VideoRootFrame;
import com.tencent.qcload.playersdk.util.BuildUtil;
import com.tencent.qcload.playersdk.util.PlayerListener;
import com.tencent.qcload.playersdk.util.VideoInfo;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raul.Fan on 2016/3/16.
 * 跑步历史播放界面
 */
@ContentView(R.layout.activity_video_play)
public class HistoryVideoPlayActivity extends BaseAppCompActivity implements View.OnClickListener {


    /* contains */
    private static final int DISMISS_CONTROL = 0x01;

    /* local view */
    @ViewInject(R.id.player)
    private VideoRootFrame mPlayer;
    @ViewInject(R.id.tv_camera_name)
    private TextView mCameraNameTv;
    @ViewInject(R.id.tv_camera_tip)
    private TextView mCameraTipTv;
    @ViewInject(R.id.ll_control)
    private LinearLayout mControlLl;
    @ViewInject(R.id.ll_share)
    private LinearLayout mShareLl;

    LinearLayout controlPanel;
    ImageButton playBtn;

    /* local data */
    private List<VideoInfo> videos = new ArrayList<VideoInfo>();
    private int mCurrPos;
    private List<GetPlaybackCameraListResult.VideoEntity> mDataList;
    private int mWorkoutid;

    private WxShareManager share;
    private GetWxInviteResult result;

//    private Handler mHandler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            if (msg.what == DISMISS_CONTROL){
//                mControlLl.setVisibility(View.INVISIBLE);
//            }
//        }
//    };

    @Override
    protected void initData() {
        mDataList = (List<GetPlaybackCameraListResult.VideoEntity>) getIntent().getExtras().get("list");
        mCurrPos = getIntent().getExtras().getInt("pos");
        mWorkoutid = getIntent().getExtras().getInt("workoutid");

        share = new WxShareManager(this);
        getCurrPlayVideoList();
    }


    @Override
    protected void initViews() {
//        LayoutInflater inflate = (LayoutInflater)this.mContext.getSystemService("layout_inflater");
//        this.mRoot = inflate.inflate(BuildUtil.getResourceIdByName("layout", "qcloud_player_media_controller"), (ViewGroup)null);

//        controlPanel = (LinearLayout)mPlayer.findViewById(BuildUtil.getResourceIdByName("layout", "qcloud_player_media_controller"));
//        controlPanel.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                T.showShort(mContext,"controlPanel.getVisibility():" + controlPanel.getVisibility());
//                mControlLl.setVisibility(controlPanel.getVisibility());
//            }
//        });
//        playBtn = (ImageButton)mPlayer.findViewById(BuildUtil.getResourceIdByName("id", "pause"));
//        playBtn.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                T.showShort(mContext,"playBtn.getVisibility():" + playBtn.getVisibility());
//                mControlLl.setVisibility(controlPanel.getVisibility());
//            }
//        });

//        mControlLl.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                mPlayer.dispatchTouchEvent(event);
//                return false;
//            }
//        });
        FrameLayout root = (FrameLayout) mPlayer.findViewById(BuildUtil.getResourceIdByName("id", "root"));
        VideoControllerView controllerView = null;
//        T.showShort(mContext, "root.getChildCount();" + root.getChildCount());

        for (int i = 0; i < root.getChildCount(); i++) {
            View v = root.getChildAt(i);
            if (v instanceof VideoControllerView) {
                controllerView = (VideoControllerView) v;
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) v.getLayoutParams();
                lp.height = (int) DeviceUtils.getScreenHeight();
                v.setLayoutParams(lp);
                break;
            }
        }

//        controllerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(){
//
//            @Override
//            public void onGlobalLayout() {
//
//            }
//        });
//        LinearLayout cLl = (LinearLayout) root.findViewById((BuildUtil.getResourceIdByName("layout", "qcloud_player_media_controller")));
//        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) cLl.getLayoutParams();
//        lp.height = (int) DeviceUtils.getScreenHeight();
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
    }

    @Override
    protected void doMyOnCreate() {
        mPlayer.play(videos);
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

    @Override
    @OnClick({R.id.btn_prev, R.id.btn_next, R.id.btn_back,R.id.btn_share,
            R.id.ll_share,R.id.ll_share_wx_friend,R.id.ll_share_wx_friend_circle})
    public void onClick(View v) {
        //点击下一个视频
        if (v.getId() == R.id.btn_next) {
            if (mCurrPos == (mDataList.size() - 1)) {
                T.showShort(mContext, "这已经是最后一个镜头啦");
            } else {
                mCurrPos++;
                mPlayer.release();
                getCurrPlayVideoList();
                mPlayer.release();
                mPlayer.play(videos);
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
            }
            //点击返回
        } else if (v.getId() == R.id.btn_back) {
            mPlayer.release();
            finish();
            //点击分享
        }else if(v.getId() == R.id.btn_share){
            MaidianData.saveMaidian(mContext, new DBEntityMaidian(System.currentTimeMillis() + "",
                    AppEnum.MaidianType.SharePersonalVideo, TimeUtils.NowTime()));
            shareHistoryVideo(mDataList.get(mCurrPos).getId());
        }else if(v.getId() == R.id.ll_share_wx_friend){
            share.startWxShareUrlByCustom(SHARE_MEDIA.WEIXIN, result.getText(), result.getTitle(), result.getLink());
            mShareLl.setVisibility(View.GONE);
            mPlayer.play();
        }else if(v.getId() == R.id.ll_share_wx_friend_circle) {
            share.startWxShareUrlByCustom(SHARE_MEDIA.WEIXIN_CIRCLE, result.getText(),result.getTitle(), result.getLink());
            mShareLl.setVisibility(View.GONE);
            mPlayer.play();
        }else if(v.getId() == R.id.ll_share){
            mShareLl.setVisibility(View.GONE);
            mPlayer.play();
        }
    }

    private void getCurrPlayVideoList() {
        videos.clear();
        VideoInfo videoInfo = new VideoInfo();
        videoInfo.description = "镜头#" + (mCurrPos + 1);
        videoInfo.type = VideoInfo.VideoType.HLS;
        videoInfo.url = mDataList.get(mCurrPos).getUrl();
        videos.add(videoInfo);
        mCameraNameTv.setText(videoInfo.description);
        String length = NumUtils.formatLength(mDataList.get(mCurrPos).getPositionmeters() / 1000f) + "km  ";
        String time = TimeUtils.formatDurationStr(TimeUtils.getTimesetFromStartTime(mDataList.get(mCurrPos).getStarttime(),
                mDataList.get(mCurrPos).getEndtime()));
        mCameraTipTv.setText("(" + length + time + ")");
    }

    /**
     * 分享视频
     */
    private void shareHistoryVideo(int videoid) {
        if (mDialogBuilder.progressDialog != null && mDialogBuilder.progressDialog.isShowing()) {
            return;
        }
        mDialogBuilder.showProgressDialog(mContext, "正在获取分享信息..", false);
        // 发送请求
        HttpUtils https = new HttpUtils();
        String POST_URL = URLConfig.URL_SHARE_HISTORY_VIDEO;
        final RequestParams params = RequestParamsBuild.BuildGetHsitoryVideoShareRequest(mContext, mWorkoutid, videoid);
        https.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                T.showShort(mContext, errorMsg);
            }

            @Override
            protected void onRightResponse(String Response) {
                result = JSON.parseObject(Response, GetWxInviteResult.class);
                if (result.getTitle() != null && !result.getTitle().isEmpty()) {
                    mShareLl.setVisibility(View.VISIBLE);
                    mPlayer.pause();
//                    share.startWxShareUrl(result.getText(), result.getTitle(), result.getLink());
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
}
