package com.app.pao.activity.party;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.config.AppEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.entity.network.GetPartyInfoDetailResult;
import com.app.pao.entity.network.GetWxInviteResult;
import com.app.pao.network.api.MyRequestCallBack;
import com.app.pao.network.utils.RequestParamsBuild;
import com.app.pao.ui.dialog.v1.MyDialogBuilderV1;
import com.app.pao.utils.T;
import com.app.pao.utils.wx.WxShareManager;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.socialize.bean.SHARE_MEDIA;

import hwh.com.pulltorefreshlibrary.PullToRefreshLayout;

/**
 * Created by Raul on 2015/12/8.
 * 活动总结页面
 */
@ContentView(R.layout.activity_group_party_summary)
public class GroupPartySummaryActivity extends BaseAppCompActivity implements View.OnClickListener {
    /* contains */
    private static final String TAG = "GroupPartySummaryActivity";

    /* local data */
    private GetPartyInfoDetailResult mPartyInfor;//活动的列表信息

    /* local view */
    @ViewInject(R.id.content_view)
    private WebView mWebView;
    @ViewInject(R.id.ibtn_edit_summary)
    private ImageView mEditSummaryIv;


    @Override
    @OnClick({R.id.ibtn_edit_summary, R.id.title_bar_left_menu, R.id.ibtn_share_summary})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_bar_left_menu:
                finish();
                break;
            case R.id.ibtn_edit_summary:
                launchEditSummary();
                break;
            case R.id.ibtn_share_summary:
                shareParty();
                break;
        }
    }

    @Override
    protected void initData() {
        mPartyInfor = (GetPartyInfoDetailResult) getIntent().getExtras().get("party");
    }

    @Override
    protected void initViews() {
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
            }
        });
    }

    @Override
    protected void doMyOnCreate() {
        mWebView.loadUrl(mPartyInfor.getUrl());
        if(mPartyInfor.getPersonrole() == AppEnum.PersonRole.OWNER){
            mEditSummaryIv.setVisibility(View.VISIBLE);
        }else {
            mEditSummaryIv.setVisibility(View.GONE);
        }
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
     *编辑活动总结
     */
    private void launchEditSummary(){
        Bundle b = new Bundle();
        b.putInt("partyid",mPartyInfor.getId());
        startActivity(GroupPartyEditSummaryActivity.class,b);
    }

    /**
     * 分享活动
     */
    private void shareParty(){
        if (mDialogBuilder.progressDialog != null && mDialogBuilder.progressDialog.isShowing()) {
            return;
        }
        mDialogBuilder.showProgressDialog(mContext, "正在获取分享信息..", false);
        // 发送请求
        HttpUtils https = new HttpUtils();
        String POST_URL = URLConfig.URL_GET_PARTY_SUM_SHARE_TEXT;
        final RequestParams params = RequestParamsBuild.BuildGetPartySumShareRequest(mContext,mPartyInfor.getId());
        https.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                T.showShort(mContext, errorMsg);
            }

            @Override
            protected void onRightResponse(String Response) {
                final GetWxInviteResult result = JSON.parseObject(Response, GetWxInviteResult.class);
                if (result.getTitle() != null && !result.getTitle().isEmpty()) {
                    final WxShareManager share = new WxShareManager(GroupPartySummaryActivity.this);
                    mDialogBuilder.setShareWxFriendDialog(mContext, "分享活动总结到");
                    mDialogBuilder.setListener(new MyDialogBuilderV1.ShareWxFriendDialogListener() {
                        @Override
                        public void onWxFriendClick() {
                            share.startWxShareUrlByCustom(SHARE_MEDIA.WEIXIN, result.getText(), result.getTitle(), result.getLink(),result.image);
                        }

                        @Override
                        public void onWxFriendCircleClick() {
                            share.startWxShareUrlByCustom(SHARE_MEDIA.WEIXIN_CIRCLE, result.getText(),result.getTitle(), result.getLink(),result.image);
                        }

                        @Override
                        public void onCancle() {

                        }
                    });
//                    share.startWxShareUrl(result.getText(), result.getTitle(), result.getLink());
                }
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
