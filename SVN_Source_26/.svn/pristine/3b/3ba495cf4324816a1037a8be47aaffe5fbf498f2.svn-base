package com.app.pao.activity.main;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.config.AppEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.data.PreferencesData;
import com.app.pao.data.db.MaidianData;
import com.app.pao.entity.db.DBEntityMaidian;
import com.app.pao.entity.network.GetRaceListResult;
import com.app.pao.entity.network.GetWxInviteResult;
import com.app.pao.fragment.dynamic.DynamicFragment;
import com.app.pao.network.api.MyRequestCallBack;
import com.app.pao.network.utils.RequestParamsBuild;
import com.app.pao.ui.dialog.v1.MyDialogBuilderV1;
import com.app.pao.utils.ImageUtils;
import com.app.pao.utils.Log;
import com.app.pao.utils.T;
import com.app.pao.utils.TimeUtils;
import com.app.pao.utils.wx.WxShareManager;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import hwh.com.pulltorefreshlibrary.PullToRefreshLayout;

/**
 * Created by Raul on 2015/12/5.
 * 赛事专用网页界面
 */
@ContentView(R.layout.activity_webview)
public class RaceWebViewActivity extends BaseAppCompActivity {

    /* contains */
    private static final String TAG = "RaceWebViewActivity";

    /* local data */
    private static String mUrl;

    /* local view */
    @ViewInject(R.id.toolbar)
    private Toolbar mToolbar;//标题栏
    @ViewInject(R.id.content_view)
    private WebView mWebView;
    @ViewInject(R.id.pb_web_load)
    private ProgressBar mWebLoadPb;//网页加载进度条


    private int webViewType;

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
        } else if (item.getItemId() == R.id.action_share) {
            shareHistory();
            //TEST
//            savePic();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (webViewType != AppEnum.WebViewType.AdViewInto) {
            getMenuInflater().inflate(R.menu.menu_live_room, menu);
        }

        return true;
    }

    @Override
    protected void initData() {
        mUrl = getIntent().getStringExtra("url");
        int userid = 14;
        String sessionid = "";
        if(LocalApplication.getInstance().getLoginUser(mContext) != null){
            userid = LocalApplication.getInstance().getLoginUser(mContext).userId;
            sessionid = LocalApplication.getInstance().getLoginUser(mContext).sessionid;
        }

        if (mUrl.contains("?")) {
            mUrl += "&userid=" + userid
                    + "&sessionid=" + sessionid;
        } else {
            mUrl += "?userid=" + userid
                    + "&sessionid=" + sessionid;
        }
        webViewType = getIntent().getIntExtra("type", 0);
    }

    @Override
    protected void initViews() {
        int userId;
        if (LocalApplication.getInstance().getLoginUser(mContext) == null) {
            userId = AppEnum.DEFAULT_USER_ID;
        } else {
            userId = LocalApplication.getInstance().getLoginUser(mContext).userId;
        }

        if (!PreferencesData.getShowDaliyNewsGuid(mContext)
                || userId == AppEnum.DEFAULT_USER_ID
                || webViewType == AppEnum.WebViewType.AdViewInto
                || webViewType == AppEnum.WebViewType.PersonalLivingRoom) {
//            Log.v("TESTTEST","GONE");
        } else {
            PreferencesData.setShowDaliyNewsGuid(mContext, false);
        }
        setSupportActionBar(mToolbar);
        //启用支持javascript
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setPluginState(WebSettings.PluginState.ON);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setAllowFileAccess(true);
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        //不使用webview缓存
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                mWebLoadPb.setVisibility(View.VISIBLE);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                mToolbar.setTitle(mWebView.getTitle());
            }
        });

        mWebView.setWebChromeClient(new WebViewChromeClient());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView.canGoBack()) {
                mWebView.goBack();//返回上一页面
                return true;
            } else {
                finish();//退出程序
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void doMyOnCreate() {
        mWebView.loadUrl(mUrl);
    }

    @Override
    protected void updateData() {

    }

    @Override
    protected void updateViews() {
        try {
            mWebView.getClass().getMethod("onResume").invoke(mWebView, (Object[]) null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            mWebView.getClass().getMethod("onPause").invoke(mWebView, (Object[]) null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void destroy() {
        mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
    }

    /**
     * 处理进度条，进度
     *
     * @author Administrator
     *         条
     */
    private class WebViewChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            mWebLoadPb.setProgress(newProgress);
            if (newProgress == 100) {
                mWebLoadPb.setVisibility(View.GONE);

                //调用url时调用网页javaScript方法
//                mActivityWv.loadUrl("javascript:login('"+loginToken+"','"+userId+"','"+type+"','"+activityId+"')");
            }
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            mToolbar.setTitle(title);
        }
    }

    /**
     * 分享
     */
    private void shareHistory() {
        if (mDialogBuilder.progressDialog != null && mDialogBuilder.progressDialog.isShowing()) {
            return;
        }
        mDialogBuilder.showProgressDialog(mContext, "正在获取分享信息..", false);
        // 发送请求
        HttpUtils https = new HttpUtils();
        String POST_URL = URLConfig.URL_GET_WX_SHARE_DAILY_TEXT;
        final RequestParams params = RequestParamsBuild.BuildGetWxShareDailyText(mContext);
        https.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                T.showShort(mContext, errorMsg);
            }

            @Override
            protected void onRightResponse(String Response) {
                final GetWxInviteResult result = JSON.parseObject(Response, GetWxInviteResult.class);
                final WxShareManager share = new WxShareManager(RaceWebViewActivity.this);
                String urlNow = mWebView.getUrl();
                String title = "分享到";
                MaidianData.saveMaidian(mContext, new DBEntityMaidian(System.currentTimeMillis() + "",
                        AppEnum.MaidianType.ShareMatchLivingRomm, TimeUtils.NowTime()));
//                if (urlNow.contains(DynamicFragment.BANNERINTO)) {
//                    MaidianData.saveMaidian(mContext, new DBEntityMaidian(System.currentTimeMillis() + "",
//                            AppEnum.MaidianType.ShareBanner, TimeUtils.NowTime()));
//                    title = "分享日日报到";
//                } else {
//                    MaidianData.saveMaidian(mContext, new DBEntityMaidian(System.currentTimeMillis() + "",
//                            AppEnum.MaidianType.ShareMatchLivingRomm, TimeUtils.NowTime()));
//                    title = "分享赛事直播间到";
//                }
                mDialogBuilder.setShareWxFriendDialog(mContext, title);
                mDialogBuilder.setListener(new MyDialogBuilderV1.ShareWxFriendDialogListener() {
                    @Override
                    public void onWxFriendClick() {
                        share.startWxShareUrlByCustom(SHARE_MEDIA.WEIXIN, result.text, mWebView.getTitle(), mWebView.getUrl(), result.image);
                    }

                    @Override
                    public void onWxFriendCircleClick() {
                        share.startWxShareUrlByCustom(SHARE_MEDIA.WEIXIN_CIRCLE, result.text, mWebView.getTitle(), mWebView.getUrl(), result.image);
                    }

                    @Override
                    public void onCancle() {

                    }
                });
//                if (result.getTitle() != null && !result.getTitle().isEmpty()) {
//                    WxShareManager share = new WxShareManager(RaceWebViewActivity.this);
//                    share.startWxShareUrl(result.getText(), mWebView.getTitle(), mWebView.getUrl());
//                }
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


    private void savePic() {
        mDialogBuilder.showProgressDialog(mContext, "正在保存图片", false);
        Bitmap bitmap = ImageUtils.captureWebView(mWebView);
        ImageUtils.saveBitmapToSystem(mContext, bitmap, System.currentTimeMillis() + "");
        mDialogBuilder.progressDialog.dismiss();
    }

}
