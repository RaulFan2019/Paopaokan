package com.app.pao.fragment.run;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.app.pao.R;
import com.app.pao.activity.workout.HistoryVideoListActivity;
import com.app.pao.adapter.HistoryVideoAdapter;
import com.app.pao.entity.event.EventUpdateVideoPos;
import com.app.pao.entity.network.GetPlaybackCameraListResult;
import com.app.pao.fragment.BaseFragment;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Raul.Fan on 2016/5/12.
 */
public class LiveVideoFragmentV2 extends BaseFragment implements View.OnClickListener {

    /* contains */
    private static final String TAG = "LiveVideoFragmentV2";
    /* local data */
    private List<GetPlaybackCameraListResult.VideoEntity> mVideoList = new ArrayList<>();//视频列表

    /* local view */
    @ViewInject(R.id.rv_video)
    private RecyclerView mVideoRV;
    @ViewInject(R.id.ll_video_none)
    private LinearLayout mVideoNoneLl;
    @ViewInject(R.id.v_select)
    private View mSelectV;

    private HistoryVideoAdapter mAdapter;
    public int mWorkoutId;
    private int mCurrPos = 0;

    public static LiveVideoFragmentV2 newInstance() {
        LiveVideoFragmentV2 fragment = new LiveVideoFragmentV2();
        return fragment;
    }

    @Override
    @OnClick(R.id.v_select)
    public void onClick(View v) {
        if (v.getId() == R.id.v_select) {
            Intent i = new Intent(mContext, HistoryVideoListActivity.class);
            Bundle b = new Bundle();
            b.putInt("pos", mCurrPos);
            b.putSerializable("list", (Serializable) mVideoList);
            b.putInt("workoutid", mWorkoutId);
            b.putInt("userid", -1);
            i.putExtras(b);
            mContext.startActivity(i);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_history_video;
    }

    @Override
    protected void initParams() {
        mWorkoutId = getArguments().getInt("workoutid");
        mVideoList.clear();
        mVideoList.addAll((Collection<? extends GetPlaybackCameraListResult.VideoEntity>) getArguments().getSerializable("videoList"));

        LinearLayoutManager manager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        mVideoRV.setLayoutManager(manager);
        mAdapter = new HistoryVideoAdapter(mContext, mVideoList);
        mVideoRV.setAdapter(mAdapter);
        mVideoRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    mAdapter.highlightItem(getMiddlePosition());
                    mCurrPos = getMiddlePosition() - 2;
                    EventBus.getDefault().post(new EventUpdateVideoPos(mCurrPos));
                    //将位置移动到中间位置
                    ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPositionWithOffset(getScrollPosition(), 0);
                    mSelectV.setBackgroundResource(R.drawable.bg_history_video_selection);
                } else {
                    mSelectV.setBackgroundResource(R.drawable.bg_history_video_selection_scroll);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

            }
        });
        mAdapter.setOnItemClickListener(new HistoryVideoAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(int postion) {
                mAdapter.highlightItem(getMiddlePosition());
                updateHighlightPos(postion);
                mCurrPos = postion - 2;
                EventBus.getDefault().post(new EventUpdateVideoPos(postion - 2));
            }
        });
//        updateHighlightPos((mVideoList.size() / 2 + 2));
        if (mVideoList.size() > 0) {
            mVideoNoneLl.setVisibility(View.GONE);
        }
    }

    public void updateVideoList(List<GetPlaybackCameraListResult.VideoEntity> VideoList) {
        //get data from intent
        mVideoList.clear();
        mVideoList.addAll(VideoList);
        //初始化列表
        mAdapter.notifyDataSetChanged();
        updateHighlightPos((mVideoList.size() / 2 + 2));
        if (mVideoList.size() > 0) {
            mVideoNoneLl.setVisibility(View.GONE);
        }
    }

    public void updateHighlightPos(int pos) {
        if (mAdapter.mHighlight < pos) {
            mVideoRV.smoothScrollToPosition(pos + 2);
        } else {
            mVideoRV.smoothScrollToPosition(pos - 2);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        causeGC();
    }

    private void causeGC() {
        mVideoList.clear();
    }

    /**
     * 获取中间位置的position
     *
     * @return
     */
    private int getMiddlePosition() {
        return getScrollPosition() + (mAdapter.ITEM_NUM / 2);
    }

    /**
     * 获取滑动值, 滑动偏移 / 每个格子宽度
     *
     * @return 当前值
     */
    private int getScrollPosition() {
        return (int) (((double) mVideoRV.computeHorizontalScrollOffset()
                / (double) mAdapter.getItemWidth()) + 0.5f);
    }

}
