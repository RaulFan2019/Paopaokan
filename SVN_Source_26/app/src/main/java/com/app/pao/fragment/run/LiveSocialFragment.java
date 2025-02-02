package com.app.pao.fragment.run;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.activity.run.LiveActivityV3;
import com.app.pao.adapter.LiveSocialRvAdapterV3;
import com.app.pao.config.AppEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.entity.db.DBUserEntity;
import com.app.pao.entity.event.EventComment;
import com.app.pao.entity.event.EventPlayVoice;
import com.app.pao.entity.event.EventSendSocial;
import com.app.pao.entity.event.EventUpdateSocialPos;
import com.app.pao.entity.network.GetUploadAvatarResult;
import com.app.pao.entity.network.LiveSocial;
import com.app.pao.fragment.BaseFragment;
import com.app.pao.network.api.MyRequestCallBack;
import com.app.pao.network.utils.RequestParamsBuild;
import com.app.pao.ui.widget.CircularImage;
import com.app.pao.ui.widget.LinearLayoutNoScroll;
import com.app.pao.utils.DeviceUtils;
import com.app.pao.utils.ImageUtils;
import com.app.pao.utils.T;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import net.micode.soundrecorder.Recorder;
import net.micode.soundrecorder.RecorderService;
import net.micode.soundrecorder.RemainingTimeCalculator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Raul.Fan on 2016/5/12.
 */
public class LiveSocialFragment extends BaseFragment implements View.OnClickListener, Recorder.OnStateChangedListener {


    private static final String TAG = "LiveSocialFragment";

    private static final int INPUT_KEYBOARD = 1;
    private static final int INPUT_VOICE = 2;
    public static final int BITRATE_3GPP = 20 * 1024 * 8; // bits/sec
    public static final int BITRATE_AMR = 2 * 1024 * 8;
    private long mMaxFileSize = -1; // can be specified in the intent

    /* local data */
    private List<LiveSocial> mSocialList = new ArrayList<>();

    private BitmapUtils mBitmapU;
    private int mHasGiveThumbup = AppEnum.hasGiveThumbup.NO;
    private int mThumbCount = 0;
    private int mCommentsCount = 0;
    private int mLastCurrSocialIndex = 0;//上次焦点item
    private int mLastVoicePlayPosition = -1;//上次播放语音的位置

    private double lastLatitude;
    private double lastLongitude;
    private float length;
    public int mWorkoutId;

    /* local data about input*/
    private MediaPlayer mVoicePlayer;//播放器
    private int mInputType = INPUT_KEYBOARD;

    private float mVoiceDownY;
    private float mVoiceCancelY;
    private Recorder mRecorder;
    private RecorderReceiver mReceiver;
    private RemainingTimeCalculator mRemainingTimeCalculator;

    private Typeface typeFace;//字体

    private LiveSocialRvAdapterV3 mAdapter;
    private EventComment mCurrCommentEvent;//当前选中的评论位置

    /* local view */
    @ViewInject(R.id.iv_mine)
    private CircularImage mAvatarIv;
    @ViewInject(R.id.tv_none)
    private TextView mNoneTv;
    @ViewInject(R.id.rv_social)
    private RecyclerView mRv;
    @ViewInject(R.id.et_social)
    private EditText mSocialEt;
    @ViewInject(R.id.tv_comments_count)
    private TextView mCommentCountTv;
    @ViewInject(R.id.tv_thumb_count)
    private TextView mThumbCountTv;
    @ViewInject(R.id.btn_social_thumb)
    private ImageButton mThumbBtn;
    @ViewInject(R.id.tv_input)
    private TextView mInputTv;
    @ViewInject(R.id.btn_input_control)
    private ImageButton mInputControlBtn;
    @ViewInject(R.id.ll_input)
    private LinearLayoutNoScroll mInputLl;


    public static LiveSocialFragment newInstance() {
        LiveSocialFragment fragment = new LiveSocialFragment();
        return fragment;
    }

    /**
     * 评论发生变化
     *
     * @param eventComment
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EventComment eventComment) {
        mCurrCommentEvent = eventComment;
        if (eventComment.replayUserId == 0) {
            mSocialEt.setHint("我来说两句");
        } else {
            mSocialEt.setHint("回复" + mCurrCommentEvent.replayUserNickName + ": ");
            mSocialEt.requestFocus();
            imm.showSoftInput(mSocialEt, 0);
        }

    }

    /**
     * 评论焦点发生变化
     *
     * @param eventComment
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EventUpdateSocialPos eventComment) {
        LiveSocial lastEntity = mSocialList.get(mLastCurrSocialIndex);
        lastEntity.setIsSelect(false);
        LiveSocial nowEntity = mSocialList.get(eventComment.pos);
        nowEntity.setIsSelect(true);
        mAdapter.notifyItemChanged(mLastCurrSocialIndex);
        mAdapter.notifyItemChanged(eventComment.pos);
        mLastCurrSocialIndex = eventComment.pos;
    }


    /**
     * 接收到语音播放信息
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EventPlayVoice event) {
        int pos = event.getPosstion();
        if (mLastVoicePlayPosition == pos) {
            mVoicePlayer.stop();
            voiceStoped();
            return;
        }
        if (mLastVoicePlayPosition != -1) {
            mVoicePlayer.stop();
            LiveSocial lastSocial = mSocialList.get(mLastVoicePlayPosition);
            lastSocial.readnow = false;
            mSocialList.set(mLastVoicePlayPosition, lastSocial);
        }
        LiveSocial social = mSocialList.get(pos);
        social.readnow = true;

        mSocialList.set(event.getPosstion(), social);
        mAdapter.notifyItemChanged(mLastVoicePlayPosition);
        mAdapter.notifyItemChanged(pos);
        mLastVoicePlayPosition = event.getPosstion();

        String mediaUrl = social.mediaurl;
        mVoicePlayer = new MediaPlayer();
        mVoicePlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mVoicePlayer = null;
                voiceStoped();
            }
        });
        mVoicePlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                mVoicePlayer = null;
                voiceStoped();
                return false;
            }
        });
        try {
            mVoicePlayer.setDataSource(mediaUrl);
            mVoicePlayer.prepare();
            mVoicePlayer.start();
        } catch (IllegalArgumentException e) {
            mVoicePlayer = null;
            voiceStoped();
            return;
        } catch (IOException e) {
            mVoicePlayer = null;
            voiceStoped();
            return;
        }
    }

    @Override
    @OnClick({R.id.btn_social_thumb, R.id.btn_input_control})
    public void onClick(View v) {
        //点击点赞按钮
        if (v.getId() == R.id.btn_social_thumb) {
            onClickThumbBtn();
            //点击改变输入方式按钮
        } else if (v.getId() == R.id.btn_input_control) {
            changeInputType();
        }
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_live_social;
    }

    @Override
    protected void initParams() {
        EventBus.getDefault().register(this);
        initData();
        initViews();
        initRecordData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initData() {
        mVoiceCancelY = DeviceUtils.dp2px(mContext, 150);
        mSocialList.clear();
        mSocialList.addAll((Collection<? extends LiveSocial>) getArguments().getSerializable("socialList"));
        //解析评论
        if (mSocialList.size() > 0) {
            LiveSocial entity = mSocialList.get(0);
            entity.setIsSelect(true);
            mSocialList.set(0, entity);
        }
        lastLatitude = getArguments().getDouble("latitude");
        lastLongitude = getArguments().getDouble("longitude");
        length = (float) getArguments().getDouble("length");
        mWorkoutId = getArguments().getInt("workoutid");
        DBUserEntity userEntity = LocalApplication.getInstance().getLoginUser(mContext);
        mCurrCommentEvent = new EventComment(0, lastLatitude, lastLongitude, 0, userEntity.nickname, userEntity.gender, userEntity.avatar, userEntity.name);
        int mineId = userEntity.userId;
        //统计评论数量
        for (int i = 0, socialsize = mSocialList.size(); i < socialsize; i++) {
            LiveSocial entity = mSocialList.get(i);
            if (entity.type == 1) {
                mThumbCount++;
                if (entity.userid == mineId) {
                    mHasGiveThumbup = AppEnum.hasGiveThumbup.YES;
                }
            } else {
                mCommentsCount++;
            }
        }
        mBitmapU = new BitmapUtils(mContext);
        mAdapter = new LiveSocialRvAdapterV3(mContext, mSocialList);
    }

    /**
     * 初始化页面
     */
    private void initViews() {
        typeFace = Typeface.createFromAsset(getActivity().getAssets(), "fonts/TradeGothicLTStd-BdCn20.otf");
        mCommentCountTv.setTypeface(typeFace);
        mThumbCountTv.setTypeface(typeFace);

        ImageUtils.loadUserImage(LocalApplication.getInstance().getLoginUser(mContext).avatar, mAvatarIv);
        //初始化列表
        if (mSocialList.size() > 0) {
            mNoneTv.setVisibility(View.GONE);
            mCommentCountTv.setText(mCommentsCount + "");
            mThumbCountTv.setText(mThumbCount + "");
            if (mHasGiveThumbup == AppEnum.hasGiveThumbup.YES) {
                mThumbBtn.setBackgroundResource(R.drawable.btn_social_thumb_yes);
            } else {
                mThumbBtn.setBackgroundResource(R.drawable.btn_social_thumb_no);
            }
            mRv.setLayoutManager(new LinearLayoutManager(mContext));
            mRv.setAdapter(mAdapter);
            mRv.setVisibility(View.VISIBLE);
        } else {
            mNoneTv.setVisibility(View.VISIBLE);
            mRv.setVisibility(View.GONE);
        }

        //监听编辑框的Action
        mSocialEt.setOnEditorActionListener(new android.widget.TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(android.widget.TextView v, int actionId, KeyEvent event) {
                if (!mSocialEt.getText().toString().equals("")) {
                    EventBus.getDefault().post(new EventSendSocial(EventSendSocial.COMMENTS, mSocialEt.getText().toString(),
                            mCurrCommentEvent.replayUserId, mCurrCommentEvent.replayUserNickName, mCurrCommentEvent.replayUserName,
                            mCurrCommentEvent.replayUserGender, mCurrCommentEvent.replayUserAvatar, mHasGiveThumbup));
                }
                return false;
            }
        });
        //监听长按录音
        mInputLl.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mInputType == INPUT_VOICE) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        LiveActivityV3 activityV3 = (LiveActivityV3) getActivity();
                        activityV3.mViewPager.setView(mInputLl);
                        mVoiceDownY = DeviceUtils.getScreenHeight();
                        mInputLl.setBackgroundResource(R.drawable.bg_et_social_pressed);
//                        mInputTv.setTextColor(Color.parseColor("#ffffff"));
                        startRecording();
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        LiveActivityV3 activityV3 = (LiveActivityV3) getActivity();
                        activityV3.mViewPager.setView(null);
                        mInputLl.setBackgroundResource(R.drawable.bg_et_social_et);
//                        mInputTv.setTextColor(Color.parseColor("#888888"));
                        mInputTv.setText("按住 发送实时语音");
                        if ((mVoiceDownY - event.getY()) < mVoiceCancelY) {
                            finishRecording();
                        }
                    } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
//                        Log.v(TAG, "move (mVoiceDownY - event.getY():" + (mVoiceDownY - event.getY()));
                        if ((mVoiceDownY - event.getY()) > mVoiceCancelY) {
                            mInputTv.setText("松开   取消");
                        } else {
                            mInputTv.setText("松开   发送");
                        }
                    }
                    return false;
                } else {
                    return false;
                }
            }
        });
    }

    /**
     * 上次的语音播放结束
     */
    private void voiceStoped() {
        if (mLastVoicePlayPosition != -1) {
            LiveSocial lastSocial = mSocialList.get(mLastVoicePlayPosition);
            lastSocial.setReadnow(false);
            mSocialList.set(mLastVoicePlayPosition, lastSocial);
            mAdapter.notifyItemChanged(mLastVoicePlayPosition);
            mLastVoicePlayPosition = -1;
        }
    }

    /**
     * 初始化录音相关数据
     */
    private void initRecordData() {
        mVoicePlayer = new MediaPlayer();
        mRecorder = new Recorder(mContext);
        mRecorder.setOnStateChangedListener(this);
        mRemainingTimeCalculator = new RemainingTimeCalculator();
        mReceiver = new RecorderReceiver();
        getActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    /**
     * 开始录音
     */
    private void startRecording() {
        mRecorder.clear();
        mRecorder.reset();
        mInputTv.setText("松开   发送");
        String filename = LocalApplication.getInstance().getLoginUser(mContext).userId + "_" + System.currentTimeMillis();
        mRemainingTimeCalculator.reset();
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            T.showShort(mContext, "SD卡未准备好，不能录音");
        } else if (!mRemainingTimeCalculator.diskSpaceAvailable()) {
            T.showShort(mContext, "SD卡未准备好，不能录音");
        } else {
            boolean isHighQuality = true;
            // HACKME: for HD2, there is an issue with high quality 3gpp
            // use low quality instead
            if (Build.MODEL.equals("HTC HD2")) {
                isHighQuality = false;
            }
            mRemainingTimeCalculator.setBitRate(BITRATE_3GPP);
            mRecorder.startRecording(MediaRecorder.OutputFormat.THREE_GPP, filename,
                    ".3gpp", isHighQuality, mMaxFileSize);

            if (mMaxFileSize != -1) {
                mRemainingTimeCalculator.setFileSizeLimit(mRecorder.sampleFile(), mMaxFileSize);
            }
        }
    }


    /**
     * 结束录音
     */
    private void finishRecording() {
        mInputTv.setText("按住  发送实时语音");
        mRecorder.stop();
        //获取录音结果
        int recordLength = mRecorder.sampleLength();
        //若录音过短
        if (recordLength < 1) {
            T.showShort(mContext, "录音过短");
            return;
        }
        if (recordLength > 15) {
            T.showShort(mContext, "录音过长");
            return;
        }

        mDialogBuilder.showProgressDialog(mContext, "正在发送语音", true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                postVoice();
            }
        }, 1000);
    }

    /**
     * 点击点赞按钮
     */
    private void onClickThumbBtn() {
        EventBus.getDefault().post(new EventSendSocial(EventSendSocial.THUMB, "",
                mCurrCommentEvent.replayUserId, mCurrCommentEvent.replayUserNickName, mCurrCommentEvent.replayUserName,
                mCurrCommentEvent.replayUserGender, mCurrCommentEvent.replayUserAvatar, mHasGiveThumbup));
    }

    /**
     * 切换输入方式
     */
    private void changeInputType() {
        if (mInputType == INPUT_KEYBOARD) {
            mInputTv.setVisibility(View.VISIBLE);
            mSocialEt.setVisibility(View.GONE);
            mInputTv.setText("按住  发送实时语音");
            mInputControlBtn.setImageResource(R.drawable.icon_input_keyboard_grey);
            mInputLl.setBackgroundResource(R.drawable.bg_et_social_et);
            mInputType = INPUT_VOICE;
            imm.hideSoftInputFromWindow(mInputTv.getWindowToken(), 0);
        } else {
            mInputTv.setVisibility(View.GONE);
            mSocialEt.setVisibility(View.VISIBLE);
            mInputControlBtn.setImageResource(R.drawable.icon_input_voice_grey);
            mInputLl.setBackgroundResource(R.drawable.bg_et_social);
            mInputType = INPUT_KEYBOARD;
        }
    }

    /**
     * 取消点赞
     *
     * @param pos
     */
    public void cancelThumb(int pos) {
        mSocialList.remove(pos);
        mAdapter.notifyDataSetChanged();
        mThumbCount--;
        mThumbCountTv.setText(mThumbCount + "");
        mThumbBtn.setBackgroundResource(R.drawable.btn_social_thumb_no);
        mHasGiveThumbup = AppEnum.hasGiveThumbup.NO;
        if (mSocialList.size() == 0) {
            initViews();
        }
    }

    /**
     * 增加点赞
     *
     * @param result
     */
    public void andThumb(final LiveSocial result) {
        if (mSocialList.size() > 0) {
            LiveSocial lastEntity = mSocialList.get(mLastCurrSocialIndex);
            lastEntity.setIsSelect(false);
        }
        mSocialList.add(0, result);
        mLastCurrSocialIndex = 0;
        mAdapter.notifyDataSetChanged();
        mThumbCount++;
        mThumbCountTv.setText(mThumbCount + "");
        mHasGiveThumbup = AppEnum.hasGiveThumbup.YES;
        if (mSocialList.size() == 1) {
            initViews();
        }
        imm.hideSoftInputFromWindow(mSocialEt.getWindowToken(), 0);
    }


    /**
     * 更新列表
     *
     * @param result
     */
    public void updateSocialList(final LiveSocial result) {
        if (mSocialList.size() > 0) {
            LiveSocial lastEntity = mSocialList.get(mLastCurrSocialIndex);
            lastEntity.setIsSelect(false);
        }
        mSocialList.add(0, result);
        mLastCurrSocialIndex = 0;
        mAdapter.notifyDataSetChanged();
        mCommentsCount++;
        mCommentCountTv.setText(mCommentsCount + "");
        if (mSocialList.size() == 1) {
            initViews();
        }
        T.showShort(mContext, "发送评论成功!");
        mSocialEt.setText("");
        imm.hideSoftInputFromWindow(mSocialEt.getWindowToken(), 0);
    }

    /**
     * 更新所有评论数据
     *
     * @param socials
     */
    public void updateSocialList(List<LiveSocial> socials, final int addThumbCount, final int addCommentCount) {
//        if (mSocialList.size() > 0) {
//            LiveSocial lastEntity = mSocialList.get(mLastCurrSocialIndex);
//            lastEntity.setIsSelect(false);
//        }
        mSocialList.clear();
        mSocialList.addAll(socials);
        mAdapter.notifyDataSetChanged();
        mLastCurrSocialIndex += (addThumbCount + addCommentCount);
        if (mLastCurrSocialIndex == mSocialList.size()) {
            mLastCurrSocialIndex--;
        }
        mCommentsCount += addCommentCount;
        mThumbCount += addThumbCount;
        mThumbCountTv.setText(mThumbCount + "");
        mCommentCountTv.setText(mCommentsCount + "");
        if (mNoneTv.getVisibility() == View.VISIBLE) {
            initViews();
        }
    }

    /**
     * 上传头像到服务器
     */
    private void postVoice() {
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_UPLOAD_AVATAR;
        RequestParams params = RequestParamsBuild.buildUploadAvatarRequest(mContext, mRecorder.sampleFile());
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                if (mDialogBuilder.progressDialog != null) {
                    mDialogBuilder.progressDialog.dismiss();
                }
                T.showShort(mContext, "发送语音失败");
            }

            @Override
            protected void onRightResponse(String Response) {
                GetUploadAvatarResult resultEntity = JSON.parseObject(Response, GetUploadAvatarResult.class);
                String url = resultEntity.url;
                postUpdateUserVoice(url);
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                if (mDialogBuilder.progressDialog != null) {
                    mDialogBuilder.progressDialog.dismiss();
                }
                T.showShort(mContext, s);
            }

            @Override
            protected void onFinish() {

            }
        });
    }

    /**
     * 上传用户实时语音
     */
    private void postUpdateUserVoice(final String url) {
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_ADD_COMMENT;
        RequestParams params = RequestParamsBuild.buildUploadLiveVoiceRequest(mContext, mWorkoutId,
                AppEnum.dynamicType.WORKOUT, 0, 2, mRecorder.sampleLength(), url);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                if (mDialogBuilder.progressDialog != null) {
                    mDialogBuilder.progressDialog.dismiss();
                }
                T.showShort(mContext, errorMsg);
            }

            @Override
            protected void onRightResponse(String Response) {
                if (mDialogBuilder.progressDialog != null) {
                    mDialogBuilder.progressDialog.dismiss();
                }
                EventBus.getDefault().post(new EventSendSocial(EventSendSocial.VOICE, mRecorder.sampleLength() + "&:&" + url,
                        mCurrCommentEvent.replayUserId, mCurrCommentEvent.replayUserNickName, mCurrCommentEvent.replayUserName,
                        mCurrCommentEvent.replayUserGender, mCurrCommentEvent.replayUserAvatar, mHasGiveThumbup));
//                DBUserEntity user = LocalApplication.getInstance().getLoginUser(mContext);
//                LiveSocial social = new LiveSocial(2, user.getUserId(), user.getAvatar(), user.getNickname(), "我的语音",
//                        TimeUtils.NowTime(), 2, url, mRecorder.sampleLength());
//                mSocialList.add(0, social);
//                mAdapter.notifyDataSetChanged();
                T.showShort(mContext, "发送语音成功");
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                if (mDialogBuilder.progressDialog != null) {
                    mDialogBuilder.progressDialog.dismiss();
                }
                T.showShort(mContext, s);
            }

            @Override
            protected void onFinish() {

            }
        });
    }

    /**
     * 语音播放器的回调
     *
     * @param state
     */
    /**
     * 录音接收器
     */
    private class RecorderReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra(RecorderService.RECORDER_SERVICE_BROADCAST_STATE)) {
                boolean isRecording = intent.getBooleanExtra(
                        RecorderService.RECORDER_SERVICE_BROADCAST_STATE, false);
                mRecorder.setState(isRecording ? Recorder.RECORDING_STATE : Recorder.IDLE_STATE);
            } else if (intent.hasExtra(RecorderService.RECORDER_SERVICE_BROADCAST_ERROR)) {
                int error = intent.getIntExtra(RecorderService.RECORDER_SERVICE_BROADCAST_ERROR, 0);
                mRecorder.setError(error);
            }
        }
    }

    @Override
    public void onStateChanged(int state) {

    }

    @Override
    public void onError(int error) {
        Resources res = getResources();

        String message = null;
        switch (error) {
            case Recorder.STORAGE_ACCESS_ERROR:
                message = "缺少录音相关权限";
                break;
            case Recorder.IN_CALL_RECORD_ERROR:
                // TODO: update error message to reflect that the recording
                // could not be
                // performed during a call.
            case Recorder.INTERNAL_ERROR:
//                message = "录音失败";
                break;
        }
        if (message != null) {
            T.showShort(mContext, message);
//            new AlertDialog.Builder(this).setTitle(R.string.app_name).setMessage(message)
//                    .setPositiveButton("OK", null).setCancelable(false).show();
        }
    }
}
