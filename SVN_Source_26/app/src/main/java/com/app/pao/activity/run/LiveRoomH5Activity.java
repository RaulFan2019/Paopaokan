package com.app.pao.activity.run;

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
import com.app.pao.data.db.WorkoutData;
import com.app.pao.entity.db.DBEntityMaidian;
import com.app.pao.entity.network.GetRaceListResult;
import com.app.pao.entity.network.GetWxInviteResult;
import com.app.pao.network.api.MyRequestCallBack;
import com.app.pao.network.utils.RequestParamsBuild;
import com.app.pao.ui.dialog.v1.MyDialogBuilderV1;
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

import hwh.com.pulltorefreshlibrary.PullToRefreshLayout;

/**
 * Created by Raul on 2015/12/5.
 * 赛事专用网页界面
 */
@ContentView(R.layout.activity_webview)
public class LiveRoomH5Activity extends BaseAppCompActivity {

    /* contains */
    private static final String TAG = "LiveRoomH5Activity";

    private static final String TEST_URL = "http://player.youku.com/embed/XNTM5MTUwNDA0";

    public static final int TYPE_RACE = 0x05;


    /* local data */
    private String mLiveUrl;
    private int type = 0;

    /* local view */
    @ViewInject(R.id.toolbar)
    private Toolbar mToolbar;//标题栏
    @ViewInject(R.id.content_view)
    private WebView mWebView;
    @ViewInject(R.id.pb_web_load)
    private ProgressBar mWebLoadPb;//网页加载进度条

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
            shareLiveRoom();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(type != TYPE_RACE){
            getMenuInflater().inflate(R.menu.menu_live_room, menu);
        }
        return true;
    }

    @Override
    protected void initData() {
        mLiveUrl = getIntent().getExtras().getString("url");
        if(getIntent().getExtras().containsKey("type")){
            type = getIntent().getExtras().getInt("type");
        }
    }

    @Override
    protected void initViews() {
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
//        webView.setWebChromeClient(new WebChromeClient());
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
        mWebView.loadUrl(mLiveUrl);
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
        if (WorkoutData.getUnFinishWorkout(mContext, LocalApplication.getInstance().getLoginUser(mContext).userId) != null) {
            finish();
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
     * 个人直播间分享
     */
    private void shareLiveRoom() {
        if (mDialogBuilder.progressDialog != null && mDialogBuilder.progressDialog.isShowing()) {
            return;
        }
        mDialogBuilder.showProgressDialog(mContext, "正在获取分享信息..", false);
        // 发送请求
        HttpUtils https = new HttpUtils();
        String POST_URL = URLConfig.URL_LIVE_ROOM_SHARE;
        final RequestParams params = RequestParamsBuild.BuildGetLiveRoomShareRequest(mContext);
        https.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                T.showShort(mContext, errorMsg);
            }

            @Override
            protected void onRightResponse(String Response) {
                final GetWxInviteResult result = JSON.parseObject(Response, GetWxInviteResult.class);
//                if (result.getTitle() != null && !result.getTitle().isEmpty()) {
                final WxShareManager share = new WxShareManager(LiveRoomH5Activity.this);
                mDialogBuilder.setShareWxFriendDialog(mContext, "分享跑步直播页到");
                mDialogBuilder.setListener(new MyDialogBuilderV1.ShareWxFriendDialogListener() {
                    @Override
                    public void onWxFriendClick() {
                        share.startWxShareUrlByCustom(SHARE_MEDIA.WEIXIN, result.text, result.title, result.link,result.image);
                    }

                    @Override
                    public void onWxFriendCircleClick() {
                        share.startWxShareUrlByCustom(SHARE_MEDIA.WEIXIN_CIRCLE, result.text, result.title, result.link,result.image);
                    }

                    @Override
                    public void onCancle() {

                    }
                });
//                    share.startWxShareUrl(result.getText(), result.getTitle(), mWebView.getUrl());
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
}
