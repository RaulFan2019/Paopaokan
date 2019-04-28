package com.app.pao.activity.group;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.app.pao.ActivityStackManager;
import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.config.AppEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.entity.network.GetGroupInfoResult;
import com.app.pao.network.api.MyRequestCallBack;
import com.app.pao.network.utils.RequestParamsBuild;
import com.app.pao.utils.DeviceUtils;
import com.app.pao.utils.StringUtils;
import com.app.pao.utils.T;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * Created by Raul on 2015/12/8.
 * 解散跑团页面
 */
@ContentView(R.layout.activity_group_dissmis)
public class DismissGroupActivity extends BaseAppCompActivity implements View.OnClickListener {

    private static final String TAG = "DismissGroupActivity";

    /* local view */
    @ViewInject(R.id.toolbar)
    private Toolbar mToolbar;//标题栏
    @ViewInject(R.id.et_name)
    private EditText mNameEt;//姓名输入
    @ViewInject(R.id.et_phone)
    private EditText mPhoneEt;//手机号码输入
    @ViewInject(R.id.et_description)
    private EditText mDescriptionEt;//描述输入框

    /* local data */
    private int mGroupId;
    private String mName;//用户姓名
    private String mPhone;//用户手机号
    private String mGroupDescription;//解散描述
    private String mCountryIso;//国际号
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
     * on Click
     *
     * @param v
     */
    @Override
    @OnClick({R.id.btn_commit})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_commit:
                commit();
                break;
        }
    }


    @Override
    protected void initData() {
        mGroupId = getIntent().getIntExtra("groupid", 0);
        mCountryIso = DeviceUtils.GetCountryIso(mContext);
    }

    @Override
    protected void initViews() {
        ActivityStackManager.getAppManager().addTempActivity(this);
        setSupportActionBar(mToolbar);
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
        ActivityStackManager.getAppManager().finishTempActivity(this);
    }

    /**
     * 提交解散
     */
    private void commit() {
        //检查姓名
        mName = mNameEt.getText().toString();
        String error = StringUtils.checkNickNameError(mContext, mName);
        if (!error.equals(AppEnum.DEFAULT_CHECK_ERROR)) {
            mNameEt.setError(error);
            return;
        }

        //检查手机
        mPhone = mPhoneEt.getText().toString();
        error = StringUtils.checkPhoneNumInputError(mContext, mPhone,mCountryIso);
        if (!error.equals(AppEnum.DEFAULT_CHECK_ERROR)) {
            mPhoneEt.setError(error);
            return;
        }

        //炮团描述检查
        mGroupDescription = mDescriptionEt.getText().toString();
        error = StringUtils.checkGroupDescription(mContext, mGroupDescription);
        if (!error.equals(AppEnum.DEFAULT_CHECK_ERROR)) {
            mDescriptionEt.setError(error);
            return;
        }

        postDismissGroup();
    }

    /**
     * 发送跑团解散请求
     */
    private void postDismissGroup() {
        mDialogBuilder.showProgressDialog(mContext, "正在请求解散跑团..", false);
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_GROUP_DISSMIS;
        RequestParams params = RequestParamsBuild.buildDismisGroupRequest(mContext,mGroupId,
                mName, mPhone, mGroupDescription);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                T.showShort(mContext, errorMsg);

            }

            @Override
            protected void onRightResponse(String Response) {
                T.showShort(mContext, "您已提交解散申请");
                ActivityStackManager.getAppManager().finishAllTempActivity();
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
