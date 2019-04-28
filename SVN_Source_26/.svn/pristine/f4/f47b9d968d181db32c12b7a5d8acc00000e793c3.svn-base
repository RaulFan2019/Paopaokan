package com.app.pao.activity.friend;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.adapter.SearchFriendsFromPhoneAdapter;
import com.app.pao.adapter.SimpleBaseAdapter;
import com.app.pao.config.AppEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.data.db.MaidianData;
import com.app.pao.entity.db.DBEntityMaidian;
import com.app.pao.entity.model.PhoneNumEntity;
import com.app.pao.entity.network.GetSearFriendbyNameList;
import com.app.pao.entity.network.GetSmsInviteResult;
import com.app.pao.network.api.MyRequestCallBack;
import com.app.pao.network.utils.RequestParamsBuild;
import com.app.pao.ui.widget.LetterListView;
import com.app.pao.utils.Log;
import com.app.pao.utils.T;
import com.app.pao.utils.TimeUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.rey.material.widget.EditText;
import com.rey.material.widget.ProgressView;
import com.rey.material.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raul on 2015/11/25.
 * 从通讯录添加好友
 */
@ContentView(R.layout.activity_search_friend_from_phone)
public class SearchFriendFromPhoneActivity extends BaseAppCompActivity implements View.OnClickListener {


    /* contains */
    private static final String TAG = "SearchFriendFromPhoneActivity";

    private static final int MSG_UPDATE_VIEW = 0;//刷新页面


    // 获取库Phone表字段
    private static final String[] PHONES_PROJECTION = new String[]{Phone
            .DISPLAY_NAME, Phone.NUMBER, Photo.PHOTO_ID,
            Phone.CONTACT_ID, Phone.SORT_KEY_PRIMARY};

    //Phone Index
    private static final int PHONES_DISPLAY_NAME_INDEX = 0; //联系人显示名称
    private static final int PHONES_NUMBER_INDEX = 1;//电话号码
    private static final int PHONES_PHOTO_ID_INDEX = 2;//头像ID
    private static final int PHONES_CONTACT_ID_INDEX = 3;// 联系人的ID
    private static final int PHONES_CONTACT_SORT_KEY_ID = 4;//获取联系人SORT_KEY

    /* local view */
    @ViewInject(R.id.toolbar)
    private Toolbar mToolbar;//标题栏
    @ViewInject(R.id.progressbar_load)
    private ProgressView mLoadPv;//加载图标
    @ViewInject(R.id.et_search_friends_input)
    private EditText mSearchEt;
    @ViewInject(R.id.iv_search_friends_fork)
    private ImageView mForkIv;
    @ViewInject(R.id.tv_load)
    private TextView mLoadTv;//等待文字
    @ViewInject(R.id.lv_search_phone_list)
    private ListView mListView;//列表
    @ViewInject(R.id.llv_phone_num_sort)
    private LetterListView mSortLlv; // 字母索引
    @ViewInject(R.id.tv_big_letter)
    private TextView mBigLetterTv;// 快速引导字母文本

    /* local data */
    private List<PhoneNumEntity> mPhoneNumList = new ArrayList<PhoneNumEntity>();//通讯录列表
    private List<GetSearFriendbyNameList.UsersEntity> mSearchList = new ArrayList<GetSearFriendbyNameList
            .UsersEntity>();//查询获取的用户列表
    // 临时查询数据
    private List<PhoneNumEntity> mQueryList = new ArrayList<PhoneNumEntity>();
    private SearchFriendsFromPhoneAdapter mAdapter;//适配器

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

    @Override
    @OnClick({R.id.iv_search_friends_fork})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_search_friends_fork:
                mSearchEt.setText("");
                break;
        }
    }

    /**
     * 刷新页面Handler
     */
    Handler mUpdateViewHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            List<PhoneNumEntity> mList = new ArrayList<>();
            mList.addAll(mPhoneNumList);
            mPhoneNumList.clear();
            for(int i = 0;i<mList.size();i++){
                if(mList.get(i).getUsersEntity() != null){
                    mPhoneNumList.add(mList.get(i));
                }
            }
            for(int i = 0;i<mList.size();i++){
                if(mList.get(i).getUsersEntity() == null){
                    mPhoneNumList.add(mList.get(i));
                }
            }
            mAdapter = new SearchFriendsFromPhoneAdapter(mContext, mPhoneNumList);
            mAdapter.setOnItemBtnClickListener(new SimpleBaseAdapter.OnItemBtnClickListener() {

                @Override
                public void onItemBtnClick(int pos) {
                    PhoneNumEntity phoneNumEntity = mPhoneNumList.get(pos);
                    addFriend(pos, phoneNumEntity);
                }
            });
            mLoadTv.setVisibility(View.GONE);
            mLoadPv.stop();
            mListView.setAdapter(mAdapter);
        }

    };

    @Override
    protected void initData() {
        // 电话list
        mPhoneNumList = new ArrayList<PhoneNumEntity>();
        // 获取手机中的电话号码信息
        getPhoneContacts();
        // 获取SIM卡中的电话号码信息（若手机中没有人，尝试读取Sim卡）
        if (mPhoneNumList.size() == 0) {
            getSIMContacts();
        }
        MaidianData.saveMaidian(mContext, new DBEntityMaidian(System.currentTimeMillis() + "",
                AppEnum.MaidianType.AddFriendByPhoneList, TimeUtils.NowTime()));
    }

    @Override
    protected void initViews() {
        setSupportActionBar(mToolbar);
        if (mPhoneNumList.size() == 0) {
            mLoadPv.setVisibility(View.GONE);
            mLoadTv.setVisibility(View.VISIBLE);
            mLoadTv.setText("未读取到电话簿信息");
        }
    }

    @Override
    protected void doMyOnCreate() {

        mSearchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String newText = mSearchEt.getText().toString();
                if (newText == null || newText.isEmpty()) {
                    if (mForkIv.getVisibility() != View.GONE) {
                        mForkIv.setVisibility(View.GONE);
                    }
                } else {
                    if (mForkIv.getVisibility() != View.VISIBLE) {
                        mForkIv.setVisibility(View.VISIBLE);
                    }
                }
                mQueryList.clear();
                if (mPhoneNumList != null && mPhoneNumList.size() > 0) {

                    for (int i = 0; i < mPhoneNumList.size(); i++) {
                        PhoneNumEntity phone = mPhoneNumList.get(i);
                        if (phone.getSortKey().contains(newText.toUpperCase())
                                || phone.getContactName().contains(newText)
                                || phone.getPhoneNumber().contains(newText)) {
                            mQueryList.add(mPhoneNumList.get(i));
                        }
                    }
                }
                mAdapter = new SearchFriendsFromPhoneAdapter(SearchFriendFromPhoneActivity.this, mQueryList);
                mAdapter.setOnItemBtnClickListener(new SimpleBaseAdapter.OnItemBtnClickListener() {

                    @Override
                    public void onItemBtnClick(int pos) {
                        PhoneNumEntity phoneNumEntity = mQueryList.get(pos);
                        addFriend(pos, phoneNumEntity);
                    }
                });
                mListView.setAdapter(mAdapter);
            }
        });
    }

    @Override
    protected void updateData() {

    }

    @Override
    protected void updateViews() {
        mLoadPv.start();
        mLoadTv.setVisibility(View.VISIBLE);
        StringBuilder sb = new StringBuilder();
        if (mPhoneNumList != null && mPhoneNumList.size() > 0) {
            sb.append(mPhoneNumList.get(0).getPhoneNumber());
            for (int i = 1; i < mPhoneNumList.size(); i++) {
                sb.append("," + mPhoneNumList.get(i).getPhoneNumber());
            }
            // 检查电话是否存在于服务器
            checkUser(sb.toString());
        }
    }

    @Override
    protected void destroy() {
        if (mUpdateViewHandler != null) {
            mUpdateViewHandler.removeMessages(MSG_UPDATE_VIEW);
        }

    }

    /**
     * 得到手机通讯录联系人信息
     **/
    private void getPhoneContacts() {
        ContentResolver resolver = mContext.getContentResolver();

        // 获取手机联系人 "sort_key_alt" 排序
        Cursor phoneCursor = resolver.query(Phone.CONTENT_URI, PHONES_PROJECTION, null, null, "sort_key_alt");

        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {

                // 得到手机号码
                String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX).replace(" ", "");
                // 当手机号码为空的或者为空字段 跳过当前循环
                if (TextUtils.isEmpty(phoneNumber))
                    continue;
                if (phoneNumber.startsWith("+86")) {
                    phoneNumber = phoneNumber.substring(3); // 去掉+86
                }
                // 得到联系人名称
                String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);
                // 得到联系人ID
                Long contactid = phoneCursor.getLong(PHONES_CONTACT_ID_INDEX);

                // 得到联系人头像ID
                Long photoid = phoneCursor.getLong(PHONES_PHOTO_ID_INDEX);
                String sortKey = phoneCursor.getString(PHONES_CONTACT_SORT_KEY_ID);

                PhoneNumEntity mPeople = new PhoneNumEntity();
                mPeople.setPhoneNumber(phoneNumber);
                mPeople.setContactid(contactid);
                mPeople.setContactName(contactName);
                mPeople.setPhotoid(photoid);
                mPeople.setSortKey(sortKey);
                mPhoneNumList.add(mPeople);
            }
            phoneCursor.close();
        }
    }

    /*
    *  得到手机SIM卡联系人人信息 *
    * */
    private void getSIMContacts() {
        ContentResolver resolver = mContext.getContentResolver();
        // 获取Sims卡联系人
        Uri uri = Uri.parse("content://icc/adn");
        Cursor phoneCursor = resolver.query(uri, PHONES_PROJECTION, null, null, "sort_key_alt");
        try {
            if (phoneCursor != null) {
                phoneCursor.moveToFirst();
                while (phoneCursor.moveToNext()) {

                    // 得到手机号码
                    String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX).replace(" ", "");
                    // 当手机号码为空的或者为空字段 跳过当前循环
                    if (TextUtils.isEmpty(phoneNumber))
                        continue;
                    if (phoneNumber.startsWith("+86")) {
                        phoneNumber = phoneNumber.substring(3); // 去掉+86
                    }
                    // 得到联系人名称
                    String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);

                    String sortKey = phoneCursor.getString(PHONES_CONTACT_SORT_KEY_ID);

                    // Sim卡中没有联系人头像
                    PhoneNumEntity mPeople = new PhoneNumEntity();
                    mPeople.setPhoneNumber(phoneNumber);
                    mPeople.setContactName(contactName);
                    mPeople.setSortKey(sortKey);
                    mPhoneNumList.add(mPeople);
                }
                phoneCursor.close();
            }
        } catch (IllegalStateException ex) {
            T.showShort(mContext, "读取联系人失败");
        }

    }

    /**
     * 匹配搜索
     *
     * @param check
     */
    private void checkUser(String check) {
        if (mPhoneNumList == null || mPhoneNumList.size() == 0) {
            mLoadPv.stop();
            mLoadTv.setVisibility(View.GONE);
            return;
        }
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_SEARCH_USERS_BY_NAME_LIST;

        RequestParams params = RequestParamsBuild.buildSearchUsersByNameRequest(mContext,check);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {


            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {

            }

            @Override
            protected void onRightResponse(String Response) {
                GetSearFriendbyNameList result = JSON.parseObject(Response, GetSearFriendbyNameList.class);
                mSearchList = result.getUsers();
                refreshUserList();
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {

            }

            @Override
            protected void onFinish() {

            }
        });
    }


    /**
     * 刷新用户状态列表
     */
    private void refreshUserList() {
        for (int i = 0; i < mPhoneNumList.size(); i++) {
            PhoneNumEntity phoneNumEntity = mPhoneNumList.get(i);
            for (int j = 0; j < mSearchList.size(); j++) {
                if (phoneNumEntity.getPhoneNumber().equals(mSearchList.get(j).getName())) {
                    if (mSearchList.get(j).getId() != 0) {
                        phoneNumEntity.setUsersEntity(mSearchList.get(j));
                        mPhoneNumList.set(i, phoneNumEntity);
                    }
                    break;
                }
            }
        }
        mUpdateViewHandler.sendEmptyMessage(MSG_UPDATE_VIEW);
    }

    /**
     * 加好友
     */
    private void addFriend(int pos, PhoneNumEntity entity) {
        //已是注册用户
        if (entity.getUsersEntity() != null) {
            AddFriend(pos, entity.getUsersEntity());
            //发送短信
        } else {
            getSmsInvite(pos, entity);
        }
    }

    /**
     * 向对方发送好友请求
     *
     * @param pos
     */
    private void AddFriend(final int pos, final GetSearFriendbyNameList.UsersEntity entity) {
        if (mDialogBuilder.progressDialog != null && mDialogBuilder.progressDialog.isShowing()) {
            return;
        }

        mDialogBuilder.showProgressDialog(mContext, "正在向对方发送请求..", false);
        // 发送好友请求
        HttpUtils https = new HttpUtils();
        String POST_URL = URLConfig.URL_APPLY_ADD_FRIEND;
        final RequestParams params = RequestParamsBuild.BuildAddFriendParams(mContext,entity.getId());
        https.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {


            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                T.showShort(mContext, errorMsg);
            }

            @Override
            protected void onRightResponse(String Response) {
                T.showShort(mContext, "请求已发送");
                entity.setHasSendApply(AppEnum.HasApply.APPLY);
                PhoneNumEntity phoneNumEntity = mPhoneNumList.get(pos);
                phoneNumEntity.setUsersEntity(entity);
                mPhoneNumList.set(pos, phoneNumEntity);
                mAdapter.notifyDataSetChanged();
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
     * 获取好友短信邀请文本
     *
     * @param pos
     */
    private void getSmsInvite(final int pos, final PhoneNumEntity entity) {
        if (mDialogBuilder.progressDialog != null && mDialogBuilder.progressDialog.isShowing()) {
            return;
        }

        mDialogBuilder.showProgressDialog(mContext, "正在向对方发送请求..", false);
        // 发送好友请求
        HttpUtils https = new HttpUtils();
        String POST_URL = URLConfig.URL_GET_FRIEND_SMS_INVITE;
        final RequestParams params = RequestParamsBuild.BuildGetSMSInviteRequest(mContext);
        https.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {


            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                T.showShort(mContext, errorMsg);
            }

            @Override
            protected void onRightResponse(String Response) {
                GetSmsInviteResult result = JSON.parseObject(Response, GetSmsInviteResult.class);
                Uri uri = Uri.parse("smsto://" + entity.getPhoneNumber());
                Intent ii = new Intent(Intent.ACTION_SENDTO, uri);
                ii.putExtra("sms_body", result.getText());
                startActivity(ii);
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

}
