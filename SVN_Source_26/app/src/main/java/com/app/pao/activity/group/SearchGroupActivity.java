package com.app.pao.activity.group;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.adapter.GroupListAdapter;
import com.app.pao.adapter.GroupListAdapterReplace;
import com.app.pao.config.URLConfig;
import com.app.pao.entity.network.GetGroupListResult;
import com.app.pao.entity.network.GetSearchGroupListResult;
import com.app.pao.network.api.MyRequestCallBack;
import com.app.pao.network.utils.RequestParamsBuild;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnItemClick;
import com.rey.material.widget.EditText;
import com.rey.material.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raul on 2015/11/25.
 * 搜索跑团页面
 */
@ContentView(R.layout.activity_search_group)
public class SearchGroupActivity extends BaseAppCompActivity implements TextView.OnEditorActionListener, View
        .OnClickListener, AdapterView.OnItemClickListener {

    /* contains */
    private static final String TAG = "SearchGroupActivity";

    private static final int MSG_UPDATE_VIEW = 0;// 更新listView
    private static final int MSG_SERCH = 1;// 搜索

    /* local view */
    @ViewInject(R.id.toolbar)
    private Toolbar mToolbar;//标题栏
    @ViewInject(R.id.lv_search_friend_list)
    private RecyclerView mGroupList;//列表
    @ViewInject(R.id.et_search_friends_input)
    private EditText mSearchEt;
    @ViewInject(R.id.iv_search_friends_fork)
    private ImageView mSearchForkIv;

    //    private GroupListAdapter mAdapter;//适配器
    private GroupListAdapterReplace mGroupAdapter;

    /* local data*/
    private List<GetGroupListResult> mDatas = new ArrayList<GetGroupListResult>();
    private List<Integer> nullList = new ArrayList<>();
    private boolean mPostAble;//是否可以发送请求

    private static boolean mIsRefreshing = false;

    /**
     * 更新列表响应Handler
     **/
    private Handler FriendsHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // 若是更新列表请求
            if (msg.what == MSG_UPDATE_VIEW) {
                mGroupAdapter.notifyDataSetChanged();
                mIsRefreshing = false;
                // 开始搜索
            } else if (msg.what == MSG_SERCH) {
                toDoSearch(false);
            }
        }
    };

    /**
     * Toolbar 点击事件
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * list item click
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    @OnItemClick(R.id.lv_search_friend_list)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        GetGroupListResult group = mDatas.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("groupid", group.getId());
        startActivity(GroupInfoActivity.class, bundle);
    }


    /**
     * 检查输入
     */
    private void toDoSearch(boolean hasTip) {
        String searchStr = mSearchEt.getText().toString();

        if (null == searchStr || searchStr.equals("")) {
            mSearchEt.setError("请输入搜索项");
            return;
        }

        SearchGroups(searchStr, hasTip);
    }


    @Override
    protected void initData() {
        mPostAble = true;
    }

    @Override
    @OnClick({R.id.iv_search_friends_fork})
    public void onClick(View v) {
        if (v.getId() == R.id.iv_search_friends_fork) {
            mSearchEt.setText("");
        }
    }

    @Override
    protected void initViews() {
        setSupportActionBar(mToolbar);
        mGroupAdapter = new GroupListAdapterReplace(mContext, mDatas,nullList);
        mGroupList.setLayoutManager(new LinearLayoutManager(mContext));
        mGroupList.setAdapter(mGroupAdapter);
        mGroupList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mIsRefreshing) {
                    return true;
                } else {
                    return false;
                }
            }
        });
        //编辑框监听
        mSearchEt.setOnEditorActionListener(this);
        mSearchEt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String newText = mSearchEt.getText().toString();
                // 若输入的内容为空
                if (newText == null || newText.equals("")) {
                    if (mSearchForkIv.getVisibility() != View.GONE) {
                        mSearchForkIv.setVisibility(View.GONE);
                    }
                    clearSerchList();
                    // 若输入的内容不为空
                } else {
                    if (mSearchForkIv.getVisibility() != View.VISIBLE) {
                        mSearchForkIv.setVisibility(View.VISIBLE);
                    }
                    if (!mPostAble) {
                        return;
                    }
                    FriendsHandler.removeMessages(MSG_SERCH);
                    FriendsHandler.sendEmptyMessageDelayed(MSG_SERCH, 500);
                }
            }
        });
    }

    @Override
    protected void doMyOnCreate() {

    }

    @Override
    protected void updateData() {

    }

    @Override
    protected void updateViews() {

    }

    @Override
    protected void destroy() {
        clearSerchList();
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
            FriendsHandler.removeMessages(MSG_SERCH);
            // 这里调用搜索方法
            if (mPostAble) {
                toDoSearch(true);
            }
            return true;
        }
        return false;
    }


    /**
     * 根据用户输入搜索跑团
     *
     * @param searchStr
     */
    private void SearchGroups(final String searchStr, final boolean hasTip) {
        mPostAble = false;
        HttpUtils https = new HttpUtils();
        String POST_URL = URLConfig.URL_SEARCH_GROUP;
        RequestParams params = RequestParamsBuild.BuildSearchGroupByNameParams(mContext, searchStr);
        if (hasTip) {
            mDialogBuilder.showProgressDialog(mContext, "正在搜索跑团...", false);
        }
        https.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {

            }

            @Override
            protected void onRightResponse(String Response) {
                if (mDatas != null && mDatas.size() > 0) {
                    mIsRefreshing = true;
                    mDatas.clear();
                }
                GetSearchGroupListResult result = JSON.parseObject(Response, GetSearchGroupListResult.class);
                mDatas.addAll(result.getRungroup());

                // 更新Listview
                if (FriendsHandler != null) {
                    FriendsHandler.sendEmptyMessage(MSG_UPDATE_VIEW);
                }
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {

            }

            @Override
            protected void onFinish() {
                if (mDialogBuilder.progressDialog != null && hasTip) {
                    mDialogBuilder.progressDialog.dismiss();
                }
                mPostAble = true;
            }
        });
    }

    /**
     * 显示没有结果搜索结果
     */
    private void clearSerchList() {
        if (mDatas != null) {
            mDatas.clear();
        }
        FriendsHandler.removeMessages(MSG_SERCH);
        FriendsHandler.sendEmptyMessage(MSG_UPDATE_VIEW);
    }
}
