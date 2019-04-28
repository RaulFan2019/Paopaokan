package com.app.pao.activity.friend;

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
import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.adapter.SearchFriendsRvAdapter;
import com.app.pao.config.AppEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.data.db.MaidianData;
import com.app.pao.entity.db.DBEntityMaidian;
import com.app.pao.entity.network.GetSearchUserResult;
import com.app.pao.network.api.MyRequestCallBack;
import com.app.pao.network.utils.RequestParamsBuild;
import com.app.pao.utils.Log;
import com.app.pao.utils.T;
import com.app.pao.utils.TimeUtils;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnItemClick;
import com.rey.material.widget.EditText;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raul on 2015/11/25.
 * 搜索用户列表
 */
@ContentView(R.layout.activity_search_friend)
public class SearchFriendActivity extends BaseAppCompActivity implements TextView.OnEditorActionListener, View
        .OnClickListener{

    /* contains */
    private static final String TAG = "SearchFriendActivity";

    private static final int MSG_UPDATE_VIEW = 0;// 更新listView
    private static final int MSG_SERCH = 1;// 搜索


    /* local view */
    @ViewInject(R.id.toolbar)
    private Toolbar mToolbar;//标题栏
    @ViewInject(R.id.lv_search_friend_list)
    private RecyclerView mListView;//列表
    @ViewInject(R.id.et_search_friends_input)
    private EditText mSearchEt;
    @ViewInject(R.id.iv_search_friends_fork)
    private ImageView mSearchForkIv;

    private SearchFriendsRvAdapter mAdapter; // 搜索好友适配器
    private BitmapUtils mBitmaoUtils;

    /* local data */
    private boolean mPostAble;//是否可以发送请求
    private List<GetSearchUserResult.UsersEntity> userList = new ArrayList<GetSearchUserResult.UsersEntity>();
    private boolean mIsRefreshing = false;

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
     * 更新列表响应Handler
     **/
    private Handler FriendsHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // 若是更新列表请求
            if (msg.what == MSG_UPDATE_VIEW) {
                mAdapter.notifyDataSetChanged();
                mIsRefreshing = false;
                // 开始搜索好友列表
            } else if (msg.what == MSG_SERCH) {
                toDoSearch(false);
            }
        }
    };

    @Override
    @OnClick(R.id.iv_search_friends_fork)
    public void onClick(View v) {
        if(v.getId() == R.id.iv_search_friends_fork){
            mSearchEt.setText("");
        }
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


    @Override
    protected void initData() {
        MaidianData.saveMaidian(mContext, new DBEntityMaidian(System.currentTimeMillis() + "",
                AppEnum.MaidianType.SearchFriend, TimeUtils.NowTime()));
        mPostAble = true;
        mBitmaoUtils = new BitmapUtils(mContext);
        userList = new ArrayList<>();
        mAdapter = new SearchFriendsRvAdapter(userList,mContext,mBitmaoUtils);
        mAdapter.setOnAddFriendButClickListener(new SearchFriendsRvAdapter.OnAddFriendButClickListener() {
            @Override
            public void addFriend(int pos) {
                AddFriend(pos);
            }
        });

    }

    @Override
    protected void initViews() {
        mPostAble = true;
        setSupportActionBar(mToolbar);
        mListView.setAdapter(mAdapter);
        mListView.setLayoutManager(new LinearLayoutManager(this));

        mListView.setOnTouchListener(new View.OnTouchListener() {
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

    /**
     * 检查输入
     */
    private void toDoSearch(boolean hasTip) {
        String searchStr = mSearchEt.getText().toString();

        if (null == searchStr || searchStr.equals("")) {
            mSearchEt.setError("请输入搜索项");
            return;
        }

        SearchFriends(searchStr, hasTip);
    }

    /**
     * 根据用户输入搜索好友
     *
     * @param searchStr
     */
    private void SearchFriends(final String searchStr, final boolean hasTip) {
        mPostAble = false;
        HttpUtils https = new HttpUtils();
        String POST_URL = URLConfig.URL_SEARCH_USERS_BY_NAME;
        RequestParams params = RequestParamsBuild.BuildSearchUserByNameParams(mContext,searchStr);
        if (hasTip) {
            mDialogBuilder.showProgressDialog(mContext, "正在搜索用户...", false);
        }
        https.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {

            }

            @Override
            protected void onRightResponse(String Response) {
                if (userList != null && userList.size() > 0) {
                    mIsRefreshing = true;
                    userList.clear();
                }
                GetSearchUserResult result = JSON.parseObject(Response, GetSearchUserResult.class);
                if(result==null) {
                    T.showShort(mContext, "未搜索到用户..");
                    return;
                }
                if(result.getUsers()!=null) {
                    userList.addAll(result.getUsers());
                }
                if (result.getCount() == 0 && hasTip) {
                    T.showShort(mContext, "未搜索到用户..");
                }
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
     * 向对方发送好友请求
     *
     * @param pos
     */
    private void AddFriend(final int pos) {
        if (mDialogBuilder.progressDialog != null && mDialogBuilder.progressDialog.isShowing()) {
            return;
        }
        final GetSearchUserResult.UsersEntity user = userList.get(pos);

        mDialogBuilder.showProgressDialog(mContext, "正在向对方发送请求..", false);
        // 发送好友请求
        HttpUtils https = new HttpUtils();
        String POST_URL = URLConfig.URL_APPLY_ADD_FRIEND;
        RequestParams params = RequestParamsBuild.BuildAddFriendParams(mContext,user.getId());
        https.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {


            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                T.showShort(mContext, errorMsg);
            }

            @Override
            protected void onRightResponse(String Response) {
                try {
                    T.showShort(mContext, "请求已发送");
                    user.setHasSendApply(AppEnum.HasApply.APPLY);
                    mIsRefreshing = false;
                    userList.set(pos, user);
                    mAdapter.notifyDataSetChanged();
                    mIsRefreshing = true;
                } catch (IndexOutOfBoundsException exception) {

                }
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {

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
     * 显示没有结果搜索结果
     */
    private void clearSerchList() {
        if(userList != null){
            userList.clear();
        }
        FriendsHandler.removeMessages(MSG_SERCH);
        FriendsHandler.sendEmptyMessage(MSG_UPDATE_VIEW);
    }

}
