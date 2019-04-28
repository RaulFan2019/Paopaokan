package com.app.pao.fragment.title.menu;

import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.adapter.TitleMenuListAdapter;
import com.app.pao.data.db.MessageData;
import com.app.pao.fragment.BaseFragment;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnItemClick;

import java.util.List;

/**
 * Created by Raul on 2015/11/30.
 * 消息列表界面
 */
public class TitleMenuFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    /* contains */
    private static final String TAG = "TitleMenuFragment";

    /* local data */
    private TitleMenuListAdapter mAdapter;

    private List<String> mDatas;

    private int userid;

    private OnTitleItemClickListener itemClickListener;

    /* local view */
    @ViewInject(R.id.list_menu)
    private ListView mListView;
    @ViewInject(R.id.ll_background)
    private LinearLayout mBackgroundLl;


    public static TitleMenuFragment getInstance(List<String> mDatas, OnTitleItemClickListener itemClickListener) {
        TitleMenuFragment instance = new TitleMenuFragment();
        instance.mDatas = mDatas;
        instance.itemClickListener = itemClickListener;
        return instance;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_title_menu;
    }

    @Override
    protected void initParams() {
        //  mDatas = new ArrayList<String>();
        userid = LocalApplication.getInstance().getLoginUser(mContext).getUserId();
        mAdapter = new TitleMenuListAdapter(mContext, mDatas, MessageData.getNewGroupMsgCount(mContext, userid));
        mListView.setAdapter(mAdapter);


    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        mBackgroundLl.setVisibility(View.GONE);
        super.onDestroyView();
    }

//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        super.onHiddenChanged(hidden);
//        if (hidden) {
//            mBackgroundLl.setVisibility(View.GONE);
////            Animation animation = new AlphaAnimation(1,0);
////            animation.setDuration(100);
////            mBackgroundLl.setAnimation(animation);
////            animation.setAnimationListener(new Animation.AnimationListener() {
////                @Override
////                public void onAnimationStart(Animation animation) {
////
////                }
////
////                @Override
////                public void onAnimationEnd(Animation animation) {
////                    mBackgroundLl.setVisibility(View.GONE);
////                    mBackgroundLl.clearAnimation();
////                }
////
////                @Override
////                public void onAnimationRepeat(Animation animation) {
////
////                }
////            });
//
//        } else {
//            mBackgroundLl.setVisibility(View.VISIBLE);
//        }
//    }

    @Override
    @OnItemClick(R.id.list_menu)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (itemClickListener != null)
            itemClickListener.OnTitleItemClick(position);
    }


    public interface OnTitleItemClickListener {
        public void OnTitleItemClick(int position);
    }


}
