package com.app.pao.activity.group;

import android.view.View;
import android.widget.EditText;

import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.config.AppEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.network.api.MyRequestCallBack;
import com.app.pao.network.utils.RequestParamsBuild;
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
 * Created by Administrator on 2016/1/28.
 */
@ContentView(R.layout.activity_edit_group_introduce)
public class EditGroupIntroduceActivity extends BaseAppCompActivity implements View.OnClickListener{

    @ViewInject(R.id.et_introduce)
    private EditText mEditGroupIntroduceEt;

    private int mGroupId;
    private String mGroupDescription;

    @Override
    @OnClick({R.id.title_bar_left_menu,R.id.btn_save_edit})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_bar_left_menu:
                    finish();
                break;
            case R.id.btn_save_edit:
                    saveIntroduction();
                break;
        }
    }

    @Override
    protected void initData() {
        mGroupId = getIntent().getIntExtra("groupId",0);
        mGroupDescription = getIntent().getStringExtra("groupDescription");
    }

    @Override
    protected void initViews() {
        mEditGroupIntroduceEt.setText(mGroupDescription);
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

    }

    /**
     * 保存跑团简介绍
     */
    private void saveIntroduction() {
        final String descriptionStr = mEditGroupIntroduceEt.getText().toString();
        String error = StringUtils.checkGroupDescription(mContext, descriptionStr);
        if (!error.equals(AppEnum.DEFAULT_CHECK_ERROR)) {
            T.showShort(mContext, error);
            return;
        }
        mDialogBuilder.showProgressDialog(mContext, "正在保存..", false);
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_UPDATE_GROUP_INFO;
        RequestParams params = RequestParamsBuild.buildupdateGroupDescriptionRequest(mContext,mGroupId, descriptionStr);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                T.showShort(mContext, errorMsg);
            }

            @Override
            protected void onRightResponse(String Response) {
                T.showShort(mContext, "保存成功");
                finish();
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
