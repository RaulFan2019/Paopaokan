package com.app.pao.activity.main;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.activity.BaseActivityV3;
import com.app.pao.activity.friend.SearchFriendActivity;
import com.app.pao.activity.friend.SearchFriendFromPhoneActivity;
import com.app.pao.activity.group.CreateGroupTipActivity;
import com.app.pao.activity.group.SearchGroupActivity;
import com.app.pao.activity.run.RunningActivityV2;
import com.app.pao.config.AppEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.data.PreferencesData;
import com.app.pao.data.db.MaidianData;
import com.app.pao.data.db.MessageData;
import com.app.pao.data.db.WorkoutData;
import com.app.pao.entity.db.DBEntityMaidian;
import com.app.pao.entity.network.GetSmsInviteResult;
import com.app.pao.entity.network.REBase;
import com.app.pao.entity.network.UpdateEntity;
import com.app.pao.fragment.dynamic.DynamicFragment;
import com.app.pao.fragment.friend.FriendListFragment;
import com.app.pao.fragment.group.GroupFragment;
import com.app.pao.fragment.run.ReadyRunFragment;
import com.app.pao.fragment.settings.LocalUserFragmentV2;
import com.app.pao.fragment.title.menu.TitleMenuFragment;
import com.app.pao.network.api.BaseResponseParser;
import com.app.pao.network.api.MyRequestParamsV2;
import com.app.pao.network.utils.HttpExceptionHelper;
import com.app.pao.service.ClipboardService;
import com.app.pao.service.ListenerService;
import com.app.pao.service.UploadWatcherService;
import com.app.pao.ui.dialog.v1.MyDialogBuilderV1;
import com.app.pao.utils.TimeUtils;
import com.app.pao.utils.UIHelper;
import com.app.pao.utils.wx.WxShareManager;

import org.xutils.common.Callback;
import org.xutils.x;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTouch;

/**
 * Created by Raul.Fan on 2016/9/13.
 */
public class MainActivityV2 extends BaseActivityV3 {


    /* contains */
    public static final int TAB_DYNAMIC = 0;
    public static final int TAB_FRIEND = 1;
    public static final int TAB_GROUP = 2;
    public static final int TAB_USER = 3;
    public static final int TAB_RUN = 4;

    private static final int MSG_ERROR = 0x01;//错误信息
    private static final int MSG_SHARE_FRIEND = 0x02;//邀请好友


    /* local view*/
    @BindView(R.id.iv_dynamic)
    ImageView mDynamicIv;//动态图标
    @BindView(R.id.tv_dynamic)
    TextView mDynamicTv;//动态文本
    @BindView(R.id.iv_group_is_read)
    ImageView mGroupIsReadIv;//团消息是否已读
    @BindView(R.id.iv_group)
    ImageView mGroupIv;//团图标
    @BindView(R.id.tv_group)
    TextView mGroupTv;//团文本
    @BindView(R.id.iv_run)
    ImageView mRunIv;//跑步图标
    @BindView(R.id.tv_run)
    TextView mRunTv;//跑步文本
    @BindView(R.id.iv_friend_is_read)
    ImageView mFriendIsReadIv;//好友消息是否已读
    @BindView(R.id.iv_friend)
    ImageView mFriendIv;//好友图标
    @BindView(R.id.tv_friend)
    TextView mFriendTv;//好友文本
    @BindView(R.id.iv_me)
    ImageView mMeIv;//我的图标
    @BindView(R.id.tv_me)
    TextView mMeTv;//我的文本

    /* local data */
    private DynamicFragment mDynamicFragment;//游客动态页面
    private GroupFragment mGroupFragment;//游客跑团页面
    private FriendListFragment mFriendFragment;//游客好友页面
    private LocalUserFragmentV2 mUserFragment;//游客用户页面
    private ReadyRunFragment mReadyRunFragment;//跑步页面

    private TitleMenuFragment mGroupTitleMenuFragment;//跑团菜单栏
    private TitleMenuFragment mFriendTitleMenuFragment;//好友菜单栏


    private int mFriendMsgCount = 0;//未读好友消息数量
    private int mGroupMsgCount = 0;//未读跑团消息数量
    private boolean isShowMessage = false;//是否显示菜单Fragment

    private int startAd;
    private String adH5Url;

    private long exitTime = 0;

    private int mUserId;

    private MyDialogBuilderV1 mDialogBuilder;
    public WxShareManager share;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main_v2;
    }


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_ERROR:
                    if (mDialogBuilder.progressDialog != null) {
                        mDialogBuilder.progressDialog.dismiss();
                    }
                    break;
                case MSG_SHARE_FRIEND:
                    if (mDialogBuilder.progressDialog != null) {
                        mDialogBuilder.progressDialog.dismiss();
                    }
                    InvitationFriend((GetSmsInviteResult) msg.obj);
                    break;
            }
        }
    };

    @OnClick({R.id.ll_dynamic, R.id.ll_group, R.id.ll_run, R.id.ll_friend, R.id.ll_me})
    public void onClick(View view) {
        if (isShowMessage) {
            return;
        }
        switch (view.getId()) {
            //点击动态
            case R.id.ll_dynamic:
                setTab(TAB_DYNAMIC);
                break;
            //点击团
            case R.id.ll_group:
                setTab(TAB_GROUP);
                break;
            //点击开始跑步
            case R.id.ll_run:
                setTab(TAB_RUN);
                break;
            //点击好友
            case R.id.ll_friend:
                setTab(TAB_FRIEND);
                break;
            //点击我
            case R.id.ll_me:
                setTab(TAB_USER);
                break;
        }
    }


    @OnTouch({R.id.ll_fragment_message})
    public boolean onTouch(View view, MotionEvent event) {
        if (view.getId() == R.id.ll_fragment_message) {
            //判断团菜单是否展开
            if (mGroupTitleMenuFragment != null && mGroupTitleMenuFragment.isVisible()) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.title_menu_in, R.anim.title_menu_out);
                transaction.remove(mGroupTitleMenuFragment).commit();
                isShowMessage = !isShowMessage;
                return true;
            }

            //判断好友菜单是否展开
            if (mFriendTitleMenuFragment != null && mFriendTitleMenuFragment.isVisible()) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.title_menu_in, R.anim.title_menu_out);
                transaction.remove(mFriendTitleMenuFragment).commit();
                isShowMessage = !isShowMessage;
                return true;
            }
        }
        return isShowMessage;
    }

    @Override
    protected void initData() {
        share = new WxShareManager(MainActivityV2.this);
        startAd = getIntent().getIntExtra("startAd", 0);
        adH5Url = PreferencesData.getAdH5Url(MainActivityV2.this);
        mDialogBuilder = new MyDialogBuilderV1();
    }

    @Override
    protected void initViews() {

    }

    @Override
    public void onBackPressed() {
        exit();
    }

    @Override
    protected void doMyCreate() {
        mUserId = LocalApplication.getInstance().getLoginUser(MainActivityV2.this).userId;
        //启动监听服务ListenerService
        Intent listenerIntent = new Intent(MainActivityV2.this, ListenerService.class);
        startService(listenerIntent);
        //启动不断上传Service
        Intent uploadService = new Intent(MainActivityV2.this, UploadWatcherService.class);
        startService(uploadService);
        //启动剪贴板监听服务
        LocalApplication.getInstance().setIsActive(true);
        Intent clipboardService = new Intent(MainActivityV2.this, ClipboardService.class);
        startService(clipboardService);

        if (getIntent().hasExtra("Jump")) {
            setTab(getIntent().getIntExtra("Jump", TAB_RUN));
        }else {
            setTab(TAB_RUN);
        }

        if (WorkoutData.getUnFinishWorkout(MainActivityV2.this, mUserId) != null) {
            startActivity(RunningActivityV2.class);
            return;
        }

        if (startAd == 1) {
            if (adH5Url != null && !adH5Url.equals("")) {
                Bundle bundle = new Bundle();
                bundle.putString("url", adH5Url);
                bundle.putInt("type", AppEnum.WebViewType.AdViewInto);
                startActivity(RaceWebViewActivity.class, bundle);
            }
        }

        //需要更新
        String updateInfo = PreferencesData.getUpdateInfo(MainActivityV2.this);
        if (!updateInfo.equals("")) {
            final UpdateEntity updateEntity = JSON.parseObject(updateInfo, UpdateEntity.class);
            mDialogBuilder.showChoiceTwoBtnDialog(MainActivityV2.this, ("新版本:Ver." + updateEntity.getVersionName()), updateEntity
                    .getInformation(), "取消", "更新");
            mDialogBuilder.setListener(new MyDialogBuilderV1.ChoiceTwoBtnDialogListener() {
                @Override
                public void onLeftBtnClick() {

                }

                @Override
                public void onRightBtnClick() {
                    String url = "http://" + updateEntity.getUrl();
                    UIHelper.openDownLoadService(MainActivityV2.this, url, updateEntity.getAppName());
                }

                @Override
                public void onCancel() {

                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mUserId != AppEnum.DEFAULT_USER_ID) {
            mFriendMsgCount = MessageData.getNewFriendMsgCount(MainActivityV2.this, mUserId);
            mGroupMsgCount = MessageData.getNewGroupMsgCount(MainActivityV2.this, mUserId);
        } else {
            mFriendMsgCount = 0;
            mGroupMsgCount = 0;
        }
        if (mFriendMsgCount == 0) {
            mFriendIsReadIv.setVisibility(View.INVISIBLE);
        } else {
            mFriendIsReadIv.setVisibility(View.VISIBLE);
        }

        if (mGroupMsgCount == 0) {
            mGroupIsReadIv.setVisibility(View.INVISIBLE);
        } else {
            mGroupIsReadIv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void causeGC() {
        mHandler.removeMessages(MSG_ERROR);
        mHandler.removeMessages(MSG_SHARE_FRIEND);
    }

    /**
     * 显示好友菜单
     */
    public void showFriendTitleMenuList() {
        // 开启一个Fragment事务
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.title_menu_in, R.anim.title_menu_out);
        if (isShowMessage) {
            transaction.remove(mFriendTitleMenuFragment);
        } else {
            //  if (mFriendTitleMenuFragment == null) {
            ArrayList<String> mMenuTextList = new ArrayList<String>();
            mMenuTextList.add("搜索用户");
//            mMenuTextList.add("好友申请");
            mMenuTextList.add("添加通讯录好友");
            mMenuTextList.add("邀请微信好友");
            mMenuTextList.add("扫一扫");
            mMenuTextList.add("邀请码加好友");

            mFriendTitleMenuFragment = TitleMenuFragment.getInstance(mMenuTextList, new TitleMenuFragment
                    .OnTitleItemClickListener() {
                @Override
                public void OnTitleItemClick(int position) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(R.anim.title_menu_in, R.anim.title_menu_out);
                    transaction.remove(mFriendTitleMenuFragment).commit();
                    isShowMessage = !isShowMessage;
                    switch (position) {
                        //用户搜索
                        case 0:
                            startActivity(SearchFriendActivity.class);
                            break;
                        //添加通讯录
                        case 1:
                            startActivity(SearchFriendFromPhoneActivity.class);
                            break;
                        //邀请微信好友
                        case 2:
//                            getWxInvite();
                            getSmsInviteText();
                            break;
                        //扫一扫
                        case 3:
                            MaidianData.saveMaidian(MainActivityV2.this, new DBEntityMaidian(System.currentTimeMillis() + "",
                                    AppEnum.MaidianType.AddFriendBySys, TimeUtils.NowTime()));
                            Bundle bundle = new Bundle();
                            bundle.putInt("hasScanSys", 1);
                            startActivity(ScanQRCodeActivityReplace.class, bundle);
                            break;
                        //邀请码加好友
                        case 4:
                            MaidianData.saveMaidian(MainActivityV2.this, new DBEntityMaidian(System.currentTimeMillis() + "",
                                    AppEnum.MaidianType.AddFriendByInviteNum, TimeUtils.NowTime()));
                            startActivity(InputInviteActivity.class);
                            break;
                    }

                }
            });

            transaction.add(R.id.ll_fragment_message, mFriendTitleMenuFragment);
        }
        isShowMessage = !isShowMessage;
        transaction.commitAllowingStateLoss();
    }

    /**
     * 显示团菜单
     */
    public void showGroupTitleMenuList(final boolean hasGroup, final String groupName) {
        // 开启一个Fragment事务
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.title_menu_in, R.anim.title_menu_out);
        if (isShowMessage) {
            transaction.remove(mGroupTitleMenuFragment);
        } else {
            //   if (mGroupTitleMenuFragment == null) {
            ArrayList<String> mMenuTextList = new ArrayList<String>();
            mMenuTextList.add("扫一扫加团");
            mMenuTextList.add("输邀请码加跑团");
            mMenuTextList.add("搜索跑团");
            mMenuTextList.add("申请建团");

            mGroupTitleMenuFragment = TitleMenuFragment.getInstance(mMenuTextList, new TitleMenuFragment
                    .OnTitleItemClickListener() {
                @Override
                public void OnTitleItemClick(int position) {

                    switch (position) {
                        //当面加跑团
                        case 0:
                            Bundle bundl = new Bundle();
                            bundl.putInt("hasScanSys", 0);
                            startActivity(ScanQRCodeActivityReplace.class, bundl);
                            break;
                        //输邀请码加跑团
                        case 1:
                            startActivity(InputInviteActivity.class);
                            break;
                        //搜索跑团
                        case 2:
                            startActivity(SearchGroupActivity.class);
                            break;
                        //申请建团
                        case 3:
                            Bundle bundle = new Bundle();
                            bundle.putBoolean("hasgroup", hasGroup);
                            bundle.putString("groupname", groupName);
                            startActivity(CreateGroupTipActivity.class, bundle);
                            break;

                    }
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(R.anim.title_menu_in, R.anim.title_menu_out);
                    transaction.remove(mGroupTitleMenuFragment).commit();
                    isShowMessage = !isShowMessage;
                }
            });

            transaction.add(R.id.ll_fragment_message, mGroupTitleMenuFragment);
//            } else {
//                transaction.show(mGroupTitleMenuFragment);
//            }
        }
        isShowMessage = !isShowMessage;
        transaction.commitAllowingStateLoss();
    }


    /**
     * 获取短信分享文本
     */
    public void getSmsInviteText() {
        MaidianData.saveMaidian(MainActivityV2.this, new DBEntityMaidian(System.currentTimeMillis() + "",
                AppEnum.MaidianType.InviteFriendByWx, TimeUtils.NowTime()));
        if (mDialogBuilder.progressDialog != null && mDialogBuilder.progressDialog.isShowing()) {
            return;
        }
        mDialogBuilder.showProgressDialog(MainActivityV2.this, "请稍等..", false);
        x.task().post(new Runnable() {
            @Override
            public void run() {
                MyRequestParamsV2 requestParams = new MyRequestParamsV2(MainActivityV2.this, URLConfig.URL_GET_FRIEND_SMS_INVITE);
                x.http().post(requestParams, new Callback.CommonCallback<REBase>() {
                    @Override
                    public void onSuccess(REBase reBase) {
                        //获取成功
                        if (reBase.errorcode == BaseResponseParser.ERROR_CODE_NONE) {
                            GetSmsInviteResult result = JSON.parseObject(reBase.result, GetSmsInviteResult.class);
                            //若已安装微信
                            if (share.WxIsInstall()) {
                                Message msg = new Message();
                                msg.what = MSG_SHARE_FRIEND;
                                msg.obj = result;
                                mHandler.sendMessage(msg);
                            } else {
                                Message msg = new Message();
                                msg.what = MSG_ERROR;
                                msg.obj = "未安装微信";
                                mHandler.sendMessage(msg);
                            }
                        } else {
                            Message msg = new Message();
                            msg.what = MSG_ERROR;
                            msg.obj = reBase.errormsg;
                            mHandler.sendMessage(msg);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable, boolean b) {
                        Message msg = new Message();
                        msg.what = MSG_ERROR;
                        msg.obj = HttpExceptionHelper.getErrorMsg(throwable);
                        mHandler.sendMessage(msg);
                    }

                    @Override
                    public void onCancelled(CancelledException e) {

                    }

                    @Override
                    public void onFinished() {

                    }
                });
            }
        });
    }

    /**
     * 选择TAB
     *
     * @param index
     */
    public void setTab(final int index) {
        resetBtn();
        // 开启一个Fragment事务
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideFragments(transaction);
        switch (index) {
            //点击动态
            case TAB_DYNAMIC:
                mDynamicTv.setTextColor(getResources().getColor(R.color.Tab_TextView_Selected));
                mDynamicIv.setImageResource(R.drawable.icon_dynamic_selected);
                if (mDynamicFragment == null) {
                    mDynamicFragment = DynamicFragment.newInstance();
                    transaction.add(R.id.ll_fragment_root, mDynamicFragment);
                } else {
                    transaction.show(mDynamicFragment);
                }
                break;
            //点击好友
            case TAB_FRIEND:
                mFriendTv.setTextColor(getResources().getColor(R.color.Tab_TextView_Selected));
                mFriendIv.setImageResource(R.drawable.icon_friend_selected);
                if (mFriendFragment == null) {
                    mFriendFragment = FriendListFragment.newInstance();
                    transaction.add(R.id.ll_fragment_root, mFriendFragment);
                } else {
                    transaction.show(mFriendFragment);
                }
                break;
            //点击跑团
            case TAB_GROUP:
                mGroupTv.setTextColor(getResources().getColor(R.color.Tab_TextView_Selected));
                mGroupIv.setImageResource(R.drawable.icon_group_selected);
                if (mGroupFragment == null) {
                    mGroupFragment = GroupFragment.newInstance();
                    transaction.add(R.id.ll_fragment_root, mGroupFragment);
                } else {
                    transaction.show(mGroupFragment);
                }
                break;
            //点击跑步
            case TAB_RUN:
                mRunTv.setTextColor(getResources().getColor(R.color.Tab_TextView_Selected));
                mRunIv.setImageResource(R.drawable.icon_run_selected);
                if (mReadyRunFragment == null) {
                    mReadyRunFragment = ReadyRunFragment.newInstance();
                    transaction.add(R.id.ll_fragment_root, mReadyRunFragment);
                } else {
                    transaction.show(mReadyRunFragment);
                }
                break;
            //点击我
            case TAB_USER:
                mMeIv.setImageResource(R.drawable.icon_me_selected);
                mMeTv.setTextColor(getResources().getColor(R.color.Tab_TextView_Selected));
                if (mUserFragment == null) {
                    mUserFragment = LocalUserFragmentV2.newInstance();
                    transaction.add(R.id.ll_fragment_root, mUserFragment);
                } else {
                    transaction.show(mUserFragment);
                }
                break;
        }
        transaction.commitAllowingStateLoss();
    }


    /**
     * 邀请好友
     *
     * @param result
     */
    private void InvitationFriend(final GetSmsInviteResult result) {
        mDialogBuilder.showShareDialog(MainActivityV2.this, "邀请函已复制", result.getText());
        mDialogBuilder.setShareListener(new MyDialogBuilderV1.ShareDialogListener() {
            @Override
            public void onCancelShare() {

            }

            @Override
            public void onGoShare() {
                ClipboardManager cbManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData textCd = ClipData.newPlainText("123Go", result.getText());
                cbManager.setPrimaryClip(textCd);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setComponent(new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI"));
                intent.setPackage("com.tencent.mm");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    /**
     * 清除掉所有的选中状态。
     */
    private void resetBtn() {
        mDynamicTv.setTextColor(getResources().getColor(R.color.Tab_TextView_Noraml));
        mGroupTv.setTextColor(getResources().getColor(R.color.Tab_TextView_Noraml));
        mFriendTv.setTextColor(getResources().getColor(R.color.Tab_TextView_Noraml));
        mMeTv.setTextColor(getResources().getColor(R.color.Tab_TextView_Noraml));
        mRunTv.setTextColor(getResources().getColor(R.color.Tab_TextView_Noraml));

        mDynamicIv.setImageResource(R.drawable.icon_dynamic_normal);
        mGroupIv.setImageResource(R.drawable.icon_group_normal);
        mFriendIv.setImageResource(R.drawable.icon_friend_normal);
        mMeIv.setImageResource(R.drawable.icon_me_normal);
        mRunIv.setImageResource(R.drawable.icon_run_normal);
    }

    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction 用于对Fragment执行操作的事务
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (mDynamicFragment != null) {
            transaction.hide(mDynamicFragment);
        }
        if (mFriendFragment != null) {
            transaction.hide(mFriendFragment);
        }
        if (mGroupFragment != null) {
            transaction.hide(mGroupFragment);
        }
        if (mUserFragment != null) {
            transaction.hide(mUserFragment);
        }
        if (mReadyRunFragment != null) {
            transaction.hide(mReadyRunFragment);
        }
    }

    /**
     * 按2次退出本页面
     */
    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 3000) {
            Toast.makeText(getApplicationContext(), "再按一次退出跑跑看", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            moveTaskToBack(true);
//            Intent i = new Intent(Intent.ACTION_MAIN);
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            i.addCategory(Intent.CATEGORY_HOME);
//            startActivity(i);
        }
    }
}
