package com.app.pao.activity.group;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.adapter.GroupManagerListAdapter;
import com.app.pao.config.AppEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.entity.network.GetGroupInfoResult;
import com.app.pao.entity.network.GetGroupMemberListResult;
import com.app.pao.network.api.MyRequestCallBack;
import com.app.pao.network.utils.RequestParamsBuild;
import com.app.pao.utils.T;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.rey.material.widget.EditText;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Raul on 2015/12/8.
 * 跑团增加管理员界面
 */
@ContentView(R.layout.activity_group_add_manage_list)
public class GroupAddMangerActivity extends BaseAppCompActivity implements View.OnClickListener{

    /* contains */
    private static final String TAG = "GroupAddMangerActivity";

    private static final int MSG_POST_ERROR = 1;
    private static final int MSG_POST_OK = 3;

    /* local view */
    @ViewInject(R.id.toolbar)
    private Toolbar mToolbar;//标题栏
    @ViewInject(R.id.listview_manager)
    private ListView mListView;//列表
    @ViewInject(R.id.et_search_member_input)
    private EditText mSearchEt;//搜索输入框
    @ViewInject(R.id.iv_search_member_fork)
    private ImageView mSearchForkIv;
    @ViewInject(R.id.ll_search)
    private LinearLayout mSearchLl;//搜索框

    private GroupManagerListAdapter mAdapter;//管理员列表适配器

    /* local data */
    private boolean mPostAble;//是否可以发送请求
    private List<GetGroupMemberListResult.MemberEntity> mDatas;//成员列表
    List<GetGroupMemberListResult.MemberEntity> mSearchDatas = new ArrayList<GetGroupMemberListResult
            .MemberEntity>();
    private int userId;//用户id
    private int mGroupId;

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
     * 请求返回处理
     */
    Handler mPostHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                //发送请求错误
                case MSG_POST_ERROR:
                    T.showShort(mContext, (String) msg.obj);
                    mPostAble = true;
                    break;
                //刷新成功
                case MSG_POST_OK:
                    mPostAble = true;
                    updateViewByPostResult();
                    break;
            }
        }
    };

    @Override
    @OnClick(R.id.iv_search_member_fork)
    public void onClick(View v) {
        if(v.getId() == R.id.iv_search_member_fork){
            mSearchEt.setText("");
        }
    }

    @Override
    protected void initData() {
        mPostAble = true;
        userId = LocalApplication.getInstance().getLoginUser(mContext).getUserId();
        mGroupId = getIntent().getIntExtra("groupid",0);
    }

    @Override
    protected void initViews() {
        setSupportActionBar(mToolbar);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GetGroupMemberListResult.MemberEntity memberEntity = mSearchDatas.get(position);
                if (memberEntity.getRole() == AppEnum.groupRole.MANAGER) {
                    removeManager(memberEntity, position);
                } else {
                    addManager(memberEntity, position);
                }
            }
        });

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
                    mSearchDatas.clear();
                    for (GetGroupMemberListResult.MemberEntity entity : mDatas) {
                        if (entity.getRole() != AppEnum.groupRole.OWNER) {
                            mSearchDatas.add(entity);
                        }
                    }
                    mAdapter = new GroupManagerListAdapter(mContext, mSearchDatas);
                    mListView.setAdapter(mAdapter);
                    // 若输入的内容不为空
                } else {
                    if (mSearchForkIv.getVisibility() != View.VISIBLE) {
                        mSearchForkIv.setVisibility(View.VISIBLE);
                    }
                    mSearchDatas = new ArrayList<GetGroupMemberListResult.MemberEntity>();
                    for (GetGroupMemberListResult.MemberEntity entity : mDatas) {
                        if (entity.getNickname().contains(s) && entity.getRole() != AppEnum.groupRole.OWNER) {
                            mSearchDatas.add(entity);
                        }
                    }
                    mAdapter = new GroupManagerListAdapter(mContext, mSearchDatas);
                    mListView.setAdapter(mAdapter);
                }

            }
        });
    }

    @Override
    protected void doMyOnCreate() {
        PostGetMemberRequst();
    }

    @Override
    protected void updateData() {

    }

    @Override
    protected void updateViews() {

    }

    @Override
    protected void destroy() {
        cancelHandler();
    }

    /**
     * 获取成员列表
     */
    private void PostGetMemberRequst() {
        mDialogBuilder.showProgressDialog(mContext, "正在获取人员列表..", false);
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_GET_GROUP_MEMBER_LIST;
        RequestParams params = RequestParamsBuild.buildGetGroupMemberListRequest(mContext,mGroupId);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {


            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                T.showShort(mContext, errorMsg);
                if (mPostHandler != null) {
                    mPostHandler.sendEmptyMessage(MSG_POST_ERROR);
                }
            }

            @Override
            protected void onRightResponse(String Response) {
                GetGroupMemberListResult result = JSON.parseObject(Response, GetGroupMemberListResult.class);
                mDatas = result.getMember();
                mSearchDatas.clear();
                for (GetGroupMemberListResult.MemberEntity entity : mDatas) {
                    if (entity.getRole() != AppEnum.groupRole.OWNER) {
                        mSearchDatas.add(entity);
                    }
                }
                if (mPostHandler != null) {
                    mPostHandler.sendEmptyMessage(MSG_POST_OK);
                }
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                if (mPostHandler != null) {
                    mPostHandler.sendEmptyMessage(MSG_POST_ERROR);
                }
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
     * 解除管理员
     *
     * @param memberEntity
     * @param pos
     */
    private void removeManager(final GetGroupMemberListResult.MemberEntity memberEntity, final int pos) {
        mDialogBuilder.showProgressDialog(mContext, "解除管理员..", false);
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_GROUP_REMOVE_MANAGER;
        RequestParams params = RequestParamsBuild.buildRemoveManagerRequest(mContext,mGroupId,
                memberEntity.getId());
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {


            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                T.showShort(mContext, errorMsg);

            }

            @Override
            protected void onRightResponse(String Response) {
                memberEntity.setRole(AppEnum.groupRole.MEMBER);
                mSearchDatas.set(pos, memberEntity);
                for (int i = 0; i < mDatas.size(); i++) {
                    if (mDatas.get(i).getId() == memberEntity.getId()) {
                        mDatas.set(i, memberEntity);
                    }
                }
                mAdapter = new GroupManagerListAdapter(mContext, mSearchDatas);
                mListView.setAdapter(mAdapter);
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
     * 增加管理员
     *
     * @param memberEntity
     * @param pos
     */
    private void addManager(final GetGroupMemberListResult.MemberEntity memberEntity, final int pos) {
        mDialogBuilder.showProgressDialog(mContext, "增加管理员..", false);
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_GROUP_ADD_MANAGER;
        RequestParams params = RequestParamsBuild.buildAndManagerRequest(mContext,mGroupId,
                memberEntity.getId());
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {


            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                T.showShort(mContext, errorMsg);

            }

            @Override
            protected void onRightResponse(String Response) {
                memberEntity.setRole(AppEnum.groupRole.MANAGER);
                mSearchDatas.set(pos, memberEntity);
                for (int i = 0; i < mDatas.size(); i++) {
                    if (mDatas.get(i).getId() == memberEntity.getId()) {
                        mDatas.set(i, memberEntity);
                    }
                }
                mAdapter = new GroupManagerListAdapter(mContext, mSearchDatas);
                mListView.setAdapter(mAdapter);
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
     * 根据获取的数据更新页面
     */
    private void updateViewByPostResult() {
        mAdapter = new GroupManagerListAdapter(mContext, mSearchDatas);
        mListView.setAdapter(mAdapter);
    }

    /**
     * 销毁Handler事件
     */
    private void cancelHandler() {
        if (mPostHandler != null) {
            mPostHandler.removeMessages(MSG_POST_OK);
            mPostHandler.removeMessages(MSG_POST_ERROR);
        }
    }


}
