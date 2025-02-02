package com.app.pao.fragment.history;

import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.adapter.HistorySocialRvAdapter;
import com.app.pao.config.AppEnum;
import com.app.pao.entity.db.DBUserEntity;
import com.app.pao.entity.event.EventComment;
import com.app.pao.entity.event.EventPlayVoice;
import com.app.pao.entity.event.EventSendSocial;
import com.app.pao.entity.event.EventUpdateSocialPos;
import com.app.pao.entity.network.GetSocialListResult;
import com.app.pao.fragment.BaseFragment;
import com.app.pao.ui.widget.CircularImage;
import com.app.pao.utils.ImageUtils;
import com.app.pao.utils.T;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Raul.Fan on 2016/5/8.
 * 评论点赞页面
 */
public class HistorySocialsFragment extends BaseFragment implements View.OnClickListener {

    /* local data */
    private List<GetSocialListResult.SocialsEntity> mSocialList = new ArrayList<>();

    private BitmapUtils mBitmapU;
    private int mHasGiveThumbup = AppEnum.hasGiveThumbup.NO;
    private int mThumbCount = 0;
    private int mCommentsCount = 0;
    private int mLastCurrSocialIndex = 0;//上次焦点item
    private int mLastVoicePlayPosition = -1;//上次播放语音的位置

    private MediaPlayer mVoicePlayer;//播放器

    private double lastLatitude;
    private double lastLongitude;
    private float length;

    private HistorySocialRvAdapter mAdapter;
    private EventComment mCurrCommentEvent;//当前选中的评论位置

    private Typeface typeFace;//字体

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

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_history_social;
    }

    @Override
    protected void initParams() {
        EventBus.getDefault().register(this);
        initData();
        initViews();
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
            mCurrCommentEvent.latitude = lastLatitude;
            mCurrCommentEvent.longitude = lastLongitude;
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
        GetSocialListResult.SocialsEntity lastEntity = mSocialList.get(mLastCurrSocialIndex);
        lastEntity.setIsSelect(false);
        GetSocialListResult.SocialsEntity nowEntity = mSocialList.get(eventComment.pos);
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
            GetSocialListResult.SocialsEntity lastSocial = mSocialList.get(mLastVoicePlayPosition);
            lastSocial.readnow = false;
            mSocialList.set(mLastVoicePlayPosition, lastSocial);
        }
        GetSocialListResult.SocialsEntity social = mSocialList.get(pos);
        social.readnow = true;

        mSocialList.set(event.getPosstion(), social);
        mAdapter.notifyItemChanged(mLastVoicePlayPosition);
        mAdapter.notifyItemChanged(pos);
        mLastVoicePlayPosition = event.getPosstion();

        String mediaUrl = social.getMediaurl();
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
    @OnClick({R.id.btn_social_thumb})
    public void onClick(View v) {
        //点击点赞按钮
        if (v.getId() == R.id.btn_social_thumb) {
            onClickThumbBtn();
        }
    }


    public static HistorySocialsFragment newInstance() {
        HistorySocialsFragment fragment = new HistorySocialsFragment();
        return fragment;
    }

    /**
     * 更新列表
     *
     * @param result
     */
    public void updateSocialList(final GetSocialListResult.SocialsEntity result) {
        if (mSocialList.size() > 0) {
            GetSocialListResult.SocialsEntity lastEntity = mSocialList.get(mLastCurrSocialIndex);
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
    public void andThumb(final GetSocialListResult.SocialsEntity result) {
        if (mSocialList.size() > 0) {
            GetSocialListResult.SocialsEntity lastEntity = mSocialList.get(mLastCurrSocialIndex);
            lastEntity.setIsSelect(false);
        }
        mSocialList.add(0, result);
        mLastCurrSocialIndex = 0;
        mAdapter.notifyDataSetChanged();
        mThumbCount++;
        mThumbCountTv.setText(mThumbCount + "");
        mHasGiveThumbup = AppEnum.hasGiveThumbup.YES;
        mThumbBtn.setBackgroundResource(R.drawable.btn_social_thumb_yes);
        if (mSocialList.size() == 1) {
            initViews();
        }
        imm.hideSoftInputFromWindow(mSocialEt.getWindowToken(), 0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        causeGC();
    }

    /**
     * 通知内存释放
     */
    private void causeGC() {
        mSocialList.clear();
    }

    private void initData() {
        mSocialList.clear();
        mSocialList.addAll((Collection<? extends GetSocialListResult.SocialsEntity>) getArguments().getSerializable("socialList"));
        //解析评论
        if (mSocialList.size() > 0) {
            GetSocialListResult.SocialsEntity entity = mSocialList.get(0);
            entity.setIsSelect(true);
            mSocialList.set(0, entity);
        }
        lastLatitude = getArguments().getDouble("latitude");
        lastLongitude = getArguments().getDouble("longitude");
        length = getArguments().getFloat("length");
        DBUserEntity userEntity = LocalApplication.getInstance().getLoginUser(mContext);
        mCurrCommentEvent = new EventComment(0, lastLatitude, lastLongitude, 0, userEntity.nickname, userEntity.gender, userEntity.avatar, userEntity.name);
        int mineId = userEntity.userId;
        for (int i = 0, socialsize = mSocialList.size(); i < socialsize; i++) {
            GetSocialListResult.SocialsEntity entity = mSocialList.get(i);
            if (entity.socialtype == 1) {
                mThumbCount++;
                if (entity.userid == mineId) {
                    mHasGiveThumbup = AppEnum.hasGiveThumbup.YES;
                }
            } else {
                mCommentsCount++;
            }
        }
        mBitmapU = new BitmapUtils(mContext);
        mAdapter = new HistorySocialRvAdapter(mContext, mSocialList);
    }

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
    }

    /**
     * 上次的语音播放结束
     */
    private void voiceStoped() {
        if (mLastVoicePlayPosition != -1) {
            GetSocialListResult.SocialsEntity lastSocial = mSocialList.get(mLastVoicePlayPosition);
            lastSocial.setReadnow(false);
            mSocialList.set(mLastVoicePlayPosition, lastSocial);
            mAdapter.notifyItemChanged(mLastVoicePlayPosition);
            mLastVoicePlayPosition = -1;
        }
    }

    /**
     * 点击点赞按钮
     */
    private void onClickThumbBtn() {
//        Log.v(TAG, "onClickThumbBtn");
        EventBus.getDefault().post(new EventSendSocial(EventSendSocial.THUMB, "",
                mCurrCommentEvent.replayUserId, mCurrCommentEvent.replayUserNickName, mCurrCommentEvent.replayUserName,
                mCurrCommentEvent.replayUserGender, mCurrCommentEvent.replayUserAvatar, mHasGiveThumbup));
    }
}
