package com.app.pao.utils.wx;

import android.app.Activity;

import com.app.pao.R;
import com.app.pao.config.WXConfig;
import com.app.pao.utils.T;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

public class WxShareManager {

    /**
     * 使用友盟分享面板
     **/
    public static final int TYPE_UM_LAYOUT = 0;
    /**
     * 使用自定义分享面板
     **/
    public static final int TYPE_CUSTOM_LAYOUT = 1;

    private Activity activity;

    private final UMSocialService mController; // Um控制

    private ShareResultListener shareResult;

    public static final int TYPE_SCREEN_HISTORY_INFO = 1;

    /**
     * * 友盟分享结果回调
     *
     * @author Administrator
     */
    public interface ShareResultListener {
        // 开始分享
        void onStart();

        // 分享结果
        void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity);
    }

    /**
     * init Share
     *
     * @param activity
     */
    public WxShareManager(Activity activity) {
        this.activity = activity;
        mController = UMServiceFactory.getUMSocialService("com.umeng.share");
        // 初始化微信、朋友圈分享
        addWXPlatform();
    }

    /**
     * @return
     * @功能描述 : 添加微信平台分享
     */
    private void addWXPlatform() {
        String appId = WXConfig.APP_ID;
        String appSecret = WXConfig.APP_SECRET;
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(activity, appId, appSecret);
        wxHandler.addToSocialSDK();

        // 支持微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(activity, appId, appSecret);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();

        // 禁止友盟显示toast
        wxCircleHandler.showCompressToast(false);
        mController.getConfig().closeToast();

        //	mController.registerListener(SnsPost);
    }

    /**
     * 设置分享结果回调
     *
     * @param shareResult
     */
    public void setShareResultListener(ShareResultListener shareResult) {
        this.shareResult = shareResult;
    }

    /**
     * @功能:UM分享面板 开始微信分享网页
     */
    public void startWxShareUrl(String shareContent, String title, String url) {
        // 初始微信分享内容
        WxShareUrlToFriends(shareContent, title, url);
        // 初始化朋友圈分享内容
        WxShareUrlToCirCle(shareContent, title, url);
        // 增加友盟分享回调监听
        openShareByUm();
    }

    /**
     * @param shareType
     * @功能:通过自定义面板 自定义分享方式开始分享
     */
    public void startWxShareUrlByCustom(SHARE_MEDIA shareType, String shareContent, String title, String url) {

        if (shareType == SHARE_MEDIA.WEIXIN) {
            // 初始微信分享内容
            WxShareUrlToFriends(shareContent, title, url);
        } else if (shareType == SHARE_MEDIA.WEIXIN_CIRCLE) {
            // 初始化朋友圈分享内容
            WxShareUrlToCirCle(shareContent, title, url);
        }
        openShareByCustom(shareType);
    }

    /**
     * @param shareType
     * @功能:通过自定义面板 自定义分享方式开始分享
     */
    public void startWxShareUrlByCustom(SHARE_MEDIA shareType, String shareContent, String title, String url, int res) {

        if (shareType == SHARE_MEDIA.WEIXIN) {
            // 初始微信分享内容
            WxShareUrlToFriends(shareContent, title, url, res);
        } else if (shareType == SHARE_MEDIA.WEIXIN_CIRCLE) {
            // 初始化朋友圈分享内容
            WxShareUrlToCirCle(shareContent, title, url, res);
        }
        openShareByCustom(shareType);
    }


    /**
     * @param shareType
     * @功能:通过自定义面板 自定义分享方式开始分享
     */
    public void startWxShareUrlByCustom(SHARE_MEDIA shareType, String shareContent, String title, String url, String imgUrl) {

        if (shareType == SHARE_MEDIA.WEIXIN) {
            // 初始微信分享内容
            WxShareUrlToFriends(shareContent, title, url, imgUrl);
        } else if (shareType == SHARE_MEDIA.WEIXIN_CIRCLE) {
            // 初始化朋友圈分享内容
            WxShareUrlToCirCle(shareContent, title, url, imgUrl);
        }
        openShareByCustom(shareType);
    }

    /**
     * @功能:调用友盟分享面板开始分享
     */
    private void openShareByUm() {

        mController.registerListener(SnsPost);

        // 调用U盟面板开始分享
        mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE);
        mController.openShare(activity, false);
    }

    /**
     * @功能 : 通过自定义面板开始分享
     */
    private void openShareByCustom(SHARE_MEDIA shareType) {
        mController.directShare(activity, shareType, SnsPost);
    }

    /**
     * @功能 : 微信分享网页给好友
     */
    private void WxShareUrlToFriends(String shareContent, String title, String url) {
        UMImage localImage = new UMImage(activity, R.mipmap.ic_launcher);
        WeiXinShareContent weixinContent = new WeiXinShareContent();
        // 添加该内容时将为网页分享，不添加该内容为图片、或文本分享
        weixinContent.setShareContent(shareContent);
        weixinContent.setTitle(title);
        weixinContent.setTargetUrl(url);
        weixinContent.setShareMedia(localImage);
        mController.setShareMedia(weixinContent);
    }

    /**
     * @功能 : 微信分享网页到朋友圈
     */
    private void WxShareUrlToCirCle(String shareContent, String title, String url) {
        UMImage localImage = new UMImage(activity, R.mipmap.ic_launcher);
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setShareContent(shareContent);
        circleMedia.setTitle(title);
        circleMedia.setShareMedia(localImage);
        circleMedia.setTargetUrl(url);
        mController.setShareMedia(circleMedia);
    }

    /**
     * @功能 : 微信分享网页给好友
     */
    private void WxShareUrlToFriends(String shareContent, String title, String url, int res) {
        UMImage localImage = new UMImage(activity, res);
        WeiXinShareContent weixinContent = new WeiXinShareContent();
        // 添加该内容时将为网页分享，不添加该内容为图片、或文本分享
        weixinContent.setShareContent(shareContent);
        weixinContent.setTitle(title);
        weixinContent.setTargetUrl(url);
        weixinContent.setShareMedia(localImage);
        mController.setShareMedia(weixinContent);
    }

    /**
     * @功能 : 微信分享网页到朋友圈
     */
    private void WxShareUrlToCirCle(String shareContent, String title, String url, int res) {
        UMImage localImage = new UMImage(activity, res);
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setShareContent(shareContent);
        circleMedia.setTitle(title);
        circleMedia.setShareMedia(localImage);
        circleMedia.setTargetUrl(url);
        mController.setShareMedia(circleMedia);
    }

    /**
     * @功能 : 微信分享网页给好友
     */
    private void WxShareUrlToFriends(String shareContent, String title, String url, String imgUrl) {
        UMImage localImage = new UMImage(activity, imgUrl);
        WeiXinShareContent weixinContent = new WeiXinShareContent();
        // 添加该内容时将为网页分享，不添加该内容为图片、或文本分享
        weixinContent.setShareContent(shareContent);
        weixinContent.setTitle(title);
        weixinContent.setTargetUrl(url);
        weixinContent.setShareMedia(localImage);
        mController.setShareMedia(weixinContent);
    }

    /**
     * @功能 : 微信分享网页到朋友圈
     */
    private void WxShareUrlToCirCle(String shareContent, String title, String url, String imgUrl) {
        UMImage localImage = new UMImage(activity, imgUrl);
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setShareContent(shareContent);
        circleMedia.setTitle(title);
        circleMedia.setShareMedia(localImage);
        circleMedia.setTargetUrl(url);
        mController.setShareMedia(circleMedia);
    }


    /**
     * 微信分享文本到好友
     *
     * @param shareContent
     */
    public void WxShareTextToFriends(String shareContent, String title, String url) {
        //设置微信好友分享内容
        WeiXinShareContent weixinContent = new WeiXinShareContent();
        weixinContent.setShareContent(shareContent);
        //设置title
        weixinContent.setTitle(title);
//设置分享内容跳转URL
        weixinContent.setTargetUrl(url);
        mController.setShareMedia(weixinContent);
    }

    /**
     * 判断微信是否安装
     *
     * @return
     */
    public boolean WxIsInstall() {
        String appId = WXConfig.APP_ID;
        String appSecret = WXConfig.APP_SECRET;
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(activity, appId, appSecret);
        return wxHandler.isClientInstalled();
    }

    /**
     * 如果用匿名内部类时会导致重复回调
     */
    SnsPostListener SnsPost = new SnsPostListener() {
        @Override
        public void onStart() {
            if (shareResult != null) {
                shareResult.onStart();
            }
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {

            String showText = "分享成功";
            if (eCode != StatusCode.ST_CODE_SUCCESSED) {
                showText = "分享失败 [" + eCode + "]";
            }
            if (shareResult != null) {
                shareResult.onComplete(platform, eCode, entity);
            }
            T.showShort(activity, showText);
        }
    };

}
