package com.app.pao.activity.party;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.config.URLConfig;
import com.app.pao.network.api.MyRequestCallBack;
import com.app.pao.network.utils.RequestParamsBuild;
import com.app.pao.ui.widget.SwitchView;
import com.app.pao.utils.T;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/2/16.
 * 报名选择配速界面
 */
@ContentView(R.layout.dialog_spinner)
public class signUpPartyActivity extends BaseAppCompActivity implements View.OnClickListener {

    /* local data */
    private ArrayList<String> mPaceList;//配速列表
    private int mPartyId;

    /* local view */
    @ViewInject(R.id.spinner_label)
    private Spinner mSp;
    @ViewInject(R.id.sv_jpush)
    private SwitchView leadSv;


    @Override
    @OnClick({R.id.ll_left, R.id.ll_right,R.id.ll_base})
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.ll_left) {
            finish();
        } else if (viewId == R.id.ll_right) {
            signUp();
        }else if(viewId == R.id.ll_base){
            finish();
        }
    }


    @Override
    protected void initData() {
        mPaceList = getIntent().getExtras().getStringArrayList("pace");
        mPartyId = getIntent().getExtras().getInt("partyid");
    }

    @Override
    protected void initViews() {
        getWindow().setBackgroundDrawableResource(R.color.window_transparent);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,R.layout.item_dialog_spinner_speed,mPaceList);
        mSp.setAdapter(adapter);
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


    private void signUp() {
        mDialogBuilder.showProgressDialog(mContext, "报名中..", false);
        HttpUtils https = new HttpUtils();
        String POST_URL = URLConfig.URL_PARTY_SIGNUP;
        RequestParams params = RequestParamsBuild.BuildSignupPartyParams(mContext,mPartyId, mPaceList.get(mSp.getSelectedItemPosition()), (leadSv.isOpened() ? 1 : 0));
        https.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                T.showShort(mContext, errorMsg);
            }

            @Override
            protected void onRightResponse(String Response) {
                T.showShort(mContext, "报名成功");
                if (mDialogBuilder.progressDialog != null) {
                    mDialogBuilder.progressDialog.dismiss();
                }
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
