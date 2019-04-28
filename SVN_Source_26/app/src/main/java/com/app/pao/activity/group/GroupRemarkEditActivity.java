package com.app.pao.activity.group;

import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.config.URLConfig;
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

/**
 * Created by Administrator on 2016/1/10.
 * <p/>
 * 编辑团备注
 */
@ContentView(R.layout.activity_group_remark_edit)
public class GroupRemarkEditActivity extends BaseAppCompActivity implements View.OnClickListener {

    /* local view */
    @ViewInject(R.id.et_member_remark)
    private EditText mRemarkEt;
    @ViewInject(R.id.ll_base)
    private LinearLayout mBaseLl;//

    /* local data */
    private String mRemarkStr;
    private int mGroupId;
    private int mMemberId;

    @Override
    @OnClick({R.id.title_bar_left_menu, R.id.btn_confirm_remark})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_bar_left_menu:
                finish();
                break;
            case R.id.btn_confirm_remark:
                confirmRemark();
                break;
        }
    }

    @Override
    protected void initData() {
        mGroupId  = getIntent().getIntExtra("groupId",0);
        mMemberId = getIntent().getIntExtra("userId",0);
        mRemarkStr = getIntent().getStringExtra("userAlias");
    }

    @Override
    protected void initViews() {
        mRemarkEt.setText(mRemarkStr);
        mRemarkEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                imm.hideSoftInputFromWindow(mRemarkEt.getWindowToken(), 0);
                confirmRemark();
                return false;
            }
        });
        mBaseLl.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    imm.hideSoftInputFromWindow(mRemarkEt.getWindowToken(), 0);
                }
                return false;
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

    }

    /**
     * 设置备注
     */
    private void confirmRemark() {
        mRemarkStr = mRemarkEt.getText().toString();
        if (mRemarkStr == null || mRemarkStr.isEmpty()) {
            mRemarkEt.setError("请输入备注");
            return;
        }

        mDialogBuilder.showProgressDialog(mContext, "修改中...", false);

        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_SET_MEMBER_ALIAS;
        RequestParams params = RequestParamsBuild.buildSetMemberAlias(mContext,mMemberId, mGroupId, mRemarkStr);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {
            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                T.showShort(mContext, errorMsg);
            }

            @Override
            protected void onRightResponse(String Response) {
                T.showShort(mContext, "修改成功。");
                finish();
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                T.showShort(mContext, s);
            }

            @Override
            protected void onFinish() {
                if (mDialogBuilder.progressDialog != null && mDialogBuilder.progressDialog.isShowing()) {
                    mDialogBuilder.progressDialog.dismiss();
                }
            }
        });
    }


}
