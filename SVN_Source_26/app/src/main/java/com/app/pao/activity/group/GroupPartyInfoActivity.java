package com.app.pao.activity.group;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.activity.main.ScanQrCodeActivity;
import com.app.pao.activity.party.GroupPartyEditSummaryActivity;
import com.app.pao.activity.party.GroupPartySummaryActivity;
import com.app.pao.config.AppEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.entity.network.GetGroupPartyPaceLevelResult;
import com.app.pao.entity.network.GetPartyInfoResult;
import com.app.pao.entity.network.GetPartyMemberResult;
import com.app.pao.entity.network.GetQrcodeResult;
import com.app.pao.network.api.MyRequestCallBack;
import com.app.pao.network.utils.RequestParamsBuild;
import com.app.pao.ui.dialog.v1.MyDialogBuilderV1;
import com.app.pao.ui.widget.CircularImage;
import com.app.pao.ui.widget.LoadingView;
import com.app.pao.utils.ImageUtils;
import com.app.pao.utils.QrCodeUtils;
import com.app.pao.utils.T;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.rey.material.widget.Button;
import com.rey.material.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raul on 2015/12/7.
 * 活动详情页面
 */
@ContentView(R.layout.activity_group_party_info)
public class GroupPartyInfoActivity extends BaseAppCompActivity implements View.OnClickListener {

    /* contains */
    private static final String TAG = "GroupPartyInfoActivity";

    private static final int MSG_POST_ERROR = 1;//请求失败
    private static final int MSG_POST_OK = 3;//请求成功

    private static final int REQUEST_CHECK_IN = 1;//签到请求
    /* local data */
    //   private GetGroupPartyListResult mPartyListInfor;//活动的列表信息
    private GetPartyInfoResult mPartyInfor;
    private List<GetPartyMemberResult> mMemberList;//参与活动的人员列表
    private String qrCode;
//    private int mGroupId;

    private BitmapUtils mBitmapUtils;//画图工具

    /* local view */
    @ViewInject(R.id.tv_party_name)
    private TextView mPartyNameTv;//活动名称
    @ViewInject(R.id.tv_party_status)
    private TextView mPartyStatusTv;//活动状态文本
    @ViewInject(R.id.btn_party)
    private Button mPartyBtn;//活动操作按钮
    @ViewInject(R.id.btn_party_cancel)
    private Button mPartyCancelBtn;//活动取消按钮
    @ViewInject(R.id.iv_qrcode)
    private ImageView mQrcodeView;//二维码页面
    @ViewInject(R.id.iv_runner_1)
    private CircularImage mRunner1;
    @ViewInject(R.id.iv_runner_2)
    private CircularImage mRunner2;
    @ViewInject(R.id.iv_runner_3)
    private CircularImage mRunner3;
    @ViewInject(R.id.iv_runner_4)
    private CircularImage mRunner4;
    @ViewInject(R.id.iv_runner_5)
    private CircularImage mRunner5;
    @ViewInject(R.id.tv_member_count)
    private TextView mRunnerCountTv;//跑步人数文本
    @ViewInject(R.id.tv_regist_time)
    private TextView mRegistTv;//报名截止时间
    @ViewInject(R.id.tv_location)
    private TextView mLocationTv;//地点文本
    @ViewInject(R.id.tv_starttime)
    private TextView mStartTimeTv;//活动时间文本
    @ViewInject(R.id.tv_description)
    private TextView mTipTv;//活动简介文本
    @ViewInject(R.id.rl_load)
    private LinearLayout mLoadViewRl;//加载View
    @ViewInject(R.id.loadview)
    private LoadingView mLoadV;
    @ViewInject(R.id.tv_reload)
    private TextView mReladV;
    @ViewInject(R.id.iv_party_photo)
    private ImageView mPartyPhotoIv;

    private int mPartyId;


    /**
     * 请求返回处理
     */
    Handler mPostHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                //发送请求错误
                case MSG_POST_ERROR:
                    T.showShort(mContext, (String) msg.obj);
                    mLoadV.setLoadingText("加载失败");
                    mReladV.setVisibility(View.VISIBLE);
                    if (mDialogBuilder.progressDialog != null) {
                        mDialogBuilder.progressDialog.dismiss();
                    }
                    break;
                //刷新成功
                case MSG_POST_OK:
                    updateViewByPostResult();
                    mLoadV.setVisibility(View.GONE);
                    mLoadViewRl.setVisibility(View.GONE);
                    if (mDialogBuilder.progressDialog != null) {
                        mDialogBuilder.progressDialog.dismiss();
                    }
                    break;
            }
        }
    };

    @Override
    @OnClick({R.id.ll_member, R.id.btn_party, R.id.btn_party_cancel, R.id.tv_reload, R.id.title_bar_left_menu, R.id.ll_my_workout, R.id.iv_party_photo})
    public void onClick(View v) {
        switch (v.getId()) {
            //跳转到活动人员列表界面
            case R.id.ll_member:
                launchPartyMemberListActivity();
                break;
            //活动操作按钮
            case R.id.btn_party:
                doParty();
                break;
            //取消活动
            case R.id.btn_party_cancel:
                doCancelParty();
                break;
            case R.id.tv_reload:
                GetPartyMemberList();
                mReladV.setVisibility(View.GONE);
                break;
            case R.id.title_bar_left_menu:
                finish();
                break;
            case R.id.ll_my_workout:
                launchPartySelectWorkOutActivity();
                break;
            case R.id.iv_party_photo:
                launchPartyPhotoWall();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CHECK_IN) {
                mPartyInfor.setPersonstatus(AppEnum.groupPartyPersonStatus.LOGIN);
                initViews();
            }
        }
    }

    @Override
    protected void initData() {
        mBitmapUtils = new BitmapUtils(mContext);
        mBitmapUtils.configDefaultLoadingImage(R.mipmap.ic_launcher);
        mBitmapUtils.configDefaultLoadFailedImage(R.mipmap.ic_launcher);
        if (getIntent().hasExtra("partyid")) {
            mPartyId = getIntent().getIntExtra("partyid", 0);
        }
    }

    @Override
    protected void initViews() {
        if (mPartyInfor == null) {
            return;
        }
        //活动名称
        mPartyNameTv.setText(mPartyInfor.getName());
        //活动图片
        if (!mPartyInfor.getPicture().equals(""))
            mBitmapUtils.display(mPartyPhotoIv, mPartyInfor.getPicture());
        //截止报名时间
        mRegistTv.setText(mPartyInfor.getSignupduetime());
        //地点
        mLocationTv.setText(mPartyInfor.getLocationprovince() + " " + mPartyInfor.getLocationcity() + " " +
                mPartyInfor.getLocation());
        //活动时间
        mStartTimeTv.setText(mPartyInfor.getStarttime());
        //活动简介
        mTipTv.setText(mPartyInfor.getDescription());

        //活动状态
        if (mPartyInfor.getStatus() == AppEnum.groupPartyStatus.REGIST_START) {
            mQrcodeView.setVisibility(View.GONE);
            mPartyStatusTv.setText("报名开始");
            //若是组织者或负责人
            if (mPartyInfor.getPersonstatus() == AppEnum.groupPartyPersonStatus.PARTY_OWNER || mPartyInfor.getPersonstatus() == AppEnum.groupPartyPersonStatus.PARTY_LEADER) {
                mPartyBtn.setText("活动开始");
                mPartyCancelBtn.setVisibility(View.VISIBLE);

                //若是普通成员
            } else {
                mPartyCancelBtn.setVisibility(View.GONE);
                if (mPartyInfor.getPersonstatus() == AppEnum.groupPartyPersonStatus.NOT_REGIST) {
                    mPartyBtn.setText("立即报名");
                    mPartyBtn.setEnabled(true);
                } else {
                    mPartyBtn.setText("已报名");
                    mPartyBtn.setEnabled(false);
                }
            }
        } else if (mPartyInfor.getStatus() == AppEnum.groupPartyStatus.PARTY_START) {
            mPartyStatusTv.setText("活动进行中");
            //组织者
            if (mPartyInfor.getPersonstatus() == AppEnum.groupPartyPersonStatus.PARTY_OWNER || mPartyInfor.getPersonstatus() == AppEnum.groupPartyPersonStatus.PARTY_LEADER) {
                mPartyBtn.setText("活动结束");
                mQrcodeView.setVisibility(View.VISIBLE);
                mPartyCancelBtn.setVisibility(View.VISIBLE);
                mPartyCancelBtn.setText("修改签到定位");
                //普通成员
            } else {
                mPartyCancelBtn.setVisibility(View.GONE);
                mQrcodeView.setVisibility(View.GONE);
                if (mPartyInfor.getPersonstatus() == AppEnum.groupPartyPersonStatus.NOT_REGIST) {
                    mPartyBtn.setText("立即报名");
                    mPartyBtn.setEnabled(true);
                } else if (mPartyInfor.getPersonstatus() == AppEnum.groupPartyPersonStatus.REGIST) {
                    mPartyBtn.setText("签到");
                    mPartyBtn.setEnabled(true);
                } else {
                    mPartyBtn.setText("已签到");
                    mPartyBtn.setEnabled(false);
                }
            }
        } else if (mPartyInfor.getStatus() == AppEnum.groupPartyStatus.PARTY_STOP) {
            mPartyStatusTv.setText("活动结束");
            mPartyBtn.setText("活动总结");

            if (mPartyInfor.getPersonstatus() == AppEnum.groupPartyPersonStatus.PARTY_OWNER || mPartyInfor.getPersonstatus() == AppEnum.groupPartyPersonStatus.PARTY_LEADER) {
                mQrcodeView.setVisibility(View.VISIBLE);
                mPartyCancelBtn.setVisibility(View.VISIBLE);
                mPartyCancelBtn.setText("修改活动总结");

                //普通成员
            } else {
                mQrcodeView.setVisibility(View.GONE);
                mPartyCancelBtn.setVisibility(View.GONE);
            }
        } else if (mPartyInfor.getStatus() == AppEnum.groupPartyStatus.PARTY_CANCEL) {
            mPartyStatusTv.setText("活动取消");
            mPartyCancelBtn.setVisibility(View.GONE);
            mPartyBtn.setVisibility(View.GONE);
            mQrcodeView.setVisibility(View.GONE);
        }

    }

    @Override
    protected void doMyOnCreate() {

    }

    @Override
    protected void updateData() {

    }

    @Override
    protected void updateViews() {
        postGetPartyInfo();
    }

    @Override
    protected void destroy() {
        cancelHandler();
    }

    /**
     * 活动成员页面
     */
    private void launchPartyMemberListActivity() {
        Bundle b = new Bundle();
        b.putSerializable("party", mPartyInfor);
        startActivity(GroupPartyMemberListActivity.class, b);
    }

    /**
     * 活动负责人
     */
    private void launchPartyManagerActivity() {
        Bundle b = new Bundle();
        b.putInt("partyId", mPartyId);
        b.putInt("groupid", mPartyInfor.getGroupid());
        startActivity(GroupPartyManagerActivity.class, b);
    }

    /**
     * 活动历史选择
     */
    private void launchPartySelectWorkOutActivity() {
        Bundle b = new Bundle();
        b.putInt("partyId", mPartyId);
        startActivity(GroupPartySelectWorkOutActivity.class, b);
    }

    /**
     * 活动照片墙
     */
    private void launchPartyPhotoWall() {
        Bundle b = new Bundle();
        b.putInt("partyId", mPartyId);
        startActivity(GroupPartyPhotoWallActivity.class, b);
    }

    /**
     * 更新页面
     */
    private void updateViewByPostResult() {
        if (mMemberList.size() == 0) {
            return;
        }
        //已报名人
        mRunner2.setVisibility(View.INVISIBLE);
        mRunner3.setVisibility(View.INVISIBLE);
        mRunner4.setVisibility(View.INVISIBLE);
        mRunner5.setVisibility(View.INVISIBLE);
        mRunner1.setVisibility(View.VISIBLE);
        ImageUtils.loadUserImage(mMemberList.get(0).getAvatar(), mRunner1);
        if (mMemberList.size() > 1) {
            mRunner2.setVisibility(View.VISIBLE);
            ImageUtils.loadUserImage(mMemberList.get(1).getAvatar(), mRunner2);
        }
        if (mMemberList.size() > 2) {
            mRunner3.setVisibility(View.VISIBLE);
            ImageUtils.loadUserImage(mMemberList.get(2).getAvatar(), mRunner3);
        }
        if (mMemberList.size() > 3) {
            mRunner4.setVisibility(View.VISIBLE);
            ImageUtils.loadUserImage(mMemberList.get(3).getAvatar(), mRunner4);
        }
        if (mMemberList.size() > 4) {
            mRunner5.setVisibility(View.VISIBLE);
            ImageUtils.loadUserImage(mMemberList.get(4).getAvatar(), mRunner5);
        }
        //报名人
        mRunnerCountTv.setText((mMemberList.size() - 1) + "人报名");
        mQrcodeView.setImageBitmap(QrCodeUtils.create2DCode(qrCode));
    }


    /**
     * 作活动相关操作
     */
    private void doParty() {
        //活动已结束
        if (mPartyInfor.getStatus() == AppEnum.groupPartyStatus.PARTY_STOP) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("party", mPartyInfor);
            startActivity(GroupPartySummaryActivity.class, bundle);
            return;
        }

        if (mPartyInfor.getPersonstatus() == AppEnum.groupPartyPersonStatus.PARTY_OWNER) {
            //开始报名状态
            if (mPartyInfor.getStatus() == AppEnum.groupPartyStatus.REGIST_START) {
                startParty();
                return;
            }
            //开始状态
            if (mPartyInfor.getStatus() == AppEnum.groupPartyStatus.PARTY_START) {
                finishParty();
                return;
            }
        }
        //报名
        if (mPartyInfor.getPersonstatus() == AppEnum.groupPartyPersonStatus.NOT_REGIST) {
            getPartyPaceLevel();
            //  signupParty();
            return;
        }
        //签到
        if (mPartyInfor.getPersonstatus() == AppEnum.groupPartyPersonStatus.REGIST) {
            List<String> listData = new ArrayList<>();
            listData.add("当面签到");
            listData.add("定位签到");
            listData.add("远程签到");
            mDialogBuilder.showListDialog(mContext, listData);
            mDialogBuilder.setListener(new MyDialogBuilderV1.ListDialogListener() {
                @Override
                public void onItemClick(int position) {
                    switch (position) {
                        case 0:
                            startActivityForResult(ScanQrCodeActivity.class, REQUEST_CHECK_IN);
                            break;
                        case 1:
                            launchGpsSignIn();
                            break;
                        case 2:

                            break;
                    }
                }

                @Override
                public void onCancel() {

                }
            });

            return;
        }

    }

    private void doCancelParty() {
        //活动已结束
        if (mPartyInfor.getStatus() == AppEnum.groupPartyStatus.PARTY_STOP) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("party", mPartyInfor);
            startActivity(GroupPartyEditSummaryActivity.class, bundle);
            return;
        }

        if (mPartyInfor.getPersonstatus() == AppEnum.groupPartyPersonStatus.PARTY_OWNER) {
            //开始报名状态
            if (mPartyInfor.getStatus() == AppEnum.groupPartyStatus.REGIST_START) {
                cancelParty();
                return;
            }
            //已开始状态
            if (mPartyInfor.getStatus() == AppEnum.groupPartyStatus.PARTY_START) {
                startActivity(GroupPartyEditGpsActivity.class);
                return;
            }
        }
//        //报名
//        if (mPartyListInfor.getPersonstatus() == AppEnum.groupPartyPersonStatus.NOT_REGIST) {
//
//            return;
//        }
//        //签到
//        if (mPartyListInfor.getPersonstatus() == AppEnum.groupPartyPersonStatus.REGIST) {
//        }
    }

    /**
     * 定位签到
     */
    private void launchGpsSignIn() {
        Bundle b = new Bundle();
        b.putDouble("latitude", mPartyInfor.getLatitude());
        b.putDouble("longitude", mPartyInfor.getLongitude());
        b.putInt("distance", mPartyInfor.getDistance());
        b.putInt("partyId", mPartyId);
        startActivityForResult(GroupPartyGpsSignInActivity.class, b, REQUEST_CHECK_IN);
    }

    /**
     * 开始活动
     */
    private void startParty() {
        mDialogBuilder.showProgressDialog(mContext, "正在开始请求", false);
        HttpUtils https = new HttpUtils();
        String POST_URL = URLConfig.URL_PARTY_START;
        RequestParams params = RequestParamsBuild.BuildStartPartyParams(mContext, mPartyId);
        https.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                T.showShort(mContext, errorMsg);
            }

            @Override
            protected void onRightResponse(String Response) {
                T.showShort(mContext, "活动已开始");
                mPartyInfor.setStatus(AppEnum.groupPartyStatus.PARTY_START);
                initViews();
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

    /**
     * 报名活动
     */
    private void signupParty(String pace, int isleader) {
        mDialogBuilder.showProgressDialog(mContext, "报名中..", false);
        HttpUtils https = new HttpUtils();
        String POST_URL = URLConfig.URL_PARTY_SIGNUP;
        RequestParams params = RequestParamsBuild.BuildSignupPartyParams(mContext, mPartyId, pace, isleader);
        https.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                T.showShort(mContext, errorMsg);
            }

            @Override
            protected void onRightResponse(String Response) {
                T.showShort(mContext, "报名成功");
                mPartyInfor.setPersonstatus(AppEnum.groupPartyPersonStatus.REGIST);
                initViews();
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

    /**
     * 结束活动
     */
    private void finishParty() {
        mDialogBuilder.showProgressDialog(mContext, "正在发送请求", false);
        HttpUtils https = new HttpUtils();
        String POST_URL = URLConfig.URL_PARTY_FINSIH;
        RequestParams params = RequestParamsBuild.BuildFinishPartyParams(mContext, mPartyId);
        https.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                T.showShort(mContext, errorMsg);
            }

            @Override
            protected void onRightResponse(String Response) {
                T.showShort(mContext, "活动已结束");
                mPartyInfor.setStatus(AppEnum.groupPartyStatus.PARTY_STOP);
                initViews();
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

    /**
     * 取消活动
     */
    private void cancelParty() {
        mDialogBuilder.showChoiceTwoBtnDialog(mContext, "注意", "是否取消本次活动", "取消", "确定");
        mDialogBuilder.setListener(new MyDialogBuilderV1.ChoiceTwoBtnDialogListener() {
            @Override
            public void onLeftBtnClick() {

            }

            @Override
            public void onRightBtnClick() {
                postCancelParty();
            }

            @Override
            public void onCancel() {

            }
        });
    }

    /**
     * 发送取消活动请求
     */
    private void postCancelParty() {
        mDialogBuilder.showProgressDialog(mContext, "正在发送请求", false);
        HttpUtils https = new HttpUtils();
        String POST_URL = URLConfig.URL_PARTY_CANCEL;
        RequestParams params = RequestParamsBuild.BuildCancelPartyParams(mContext, mPartyId);
        https.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {

            }

            @Override
            protected void onRightResponse(String Response) {
                T.showShort(mContext, "活动已取消");
                finish();
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
     * 获取活动详情
     */
    private void postGetPartyInfo() {
        mDialogBuilder.showProgressDialog(mContext, "正在获取活动详情", false);
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_GET_PARTY_INFO;
        RequestParams params = RequestParamsBuild.buildGetPartyInfoRequest(mContext, mPartyId);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                if (mPostHandler != null) {
                    Message msg = new Message();
                    msg.what = MSG_POST_ERROR;
                    msg.obj = errorMsg;
                    mPostHandler.sendMessage(msg);
                }
            }

            @Override
            protected void onRightResponse(String Response) {
                mPartyInfor = JSON.parseObject(Response, GetPartyInfoResult.class);
                initViews();
                GetPartyMemberList();
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                if (mPostHandler != null) {
                    Message msg = new Message();
                    msg.what = MSG_POST_ERROR;
                    msg.obj = s;
                    mPostHandler.sendMessage(msg);
                }
            }

            @Override
            protected void onFinish() {

            }
        });
    }

    /**
     * 获取活动人员列表
     */
    private void GetPartyMemberList() {
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_GET_PARTY_MEMBER_LIST;
        RequestParams params = RequestParamsBuild.buildGetPartyMemberListRequest(mContext, mPartyId);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                T.showShort(mContext, errorMsg);
                if (mPostHandler != null) {
                    mPostHandler.sendEmptyMessage(MSG_POST_ERROR);
                }
            }

            @Override
            protected void onRightResponse(String Response) {
                mMemberList = JSON.parseArray(Response, GetPartyMemberResult.class);
                getQRCode();
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                if (mPostHandler != null) {
                    mPostHandler.sendEmptyMessage(MSG_POST_ERROR);
                }
            }

            @Override
            protected void onFinish() {

            }
        });
    }


    /**
     * 获取二维码
     */
    private void getQRCode() {
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_GET_QRCODE;
        RequestParams params = RequestParamsBuild.buildQrcodeRequest(mContext, mPartyId, AppEnum.QrcodeType.PARTY);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                T.showShort(mContext, errorMsg);
                if (mPostHandler != null) {
                    mPostHandler.sendEmptyMessage(MSG_POST_ERROR);
                }
            }

            @Override
            protected void onRightResponse(String Response) {
                GetQrcodeResult result = JSON.parseObject(Response, GetQrcodeResult.class);
                qrCode = result.getQrcode();
                if (mPostHandler != null) {
                    mPostHandler.sendEmptyMessage(MSG_POST_OK);
                }
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                if (mPostHandler != null) {
                    mPostHandler.sendEmptyMessage(MSG_POST_ERROR);
                }
            }

            @Override
            protected void onFinish() {

            }
        });
    }

    /**
     * 获取跑团活动中的配速分档
     */
    private void getPartyPaceLevel() {
        mDialogBuilder.showProgressDialog(mContext, "请稍后...", false);
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_GET_PARTY_PACE_LEVEL;
        RequestParams params = RequestParamsBuild.buildGetPartyPaceLevel(mContext);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {
            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                T.showShort(mContext, errorMsg);
            }

            @Override
            protected void onRightResponse(String Response) {
                GetGroupPartyPaceLevelResult paceLevel = JSON.parseObject(Response, GetGroupPartyPaceLevelResult.class);
                ArrayList<String> data = new ArrayList<String>();
                data.add("无配速");
                data.addAll(paceLevel.getPace());
                mDialogBuilder.showSpinnerDialog(mContext, data);
                mDialogBuilder.setSpinnerListener(new MyDialogBuilderV1.SpinnerDialogListener() {
                    @Override
                    public void onCancelBtnClick() {

                    }

                    @Override
                    public void onConfirmBtnClick(String spStr, boolean isChecked) {
                        if (isChecked) {
                            signupParty(spStr, 1);
                        } else {
                            signupParty(spStr, 0);
                        }

                    }
                });
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

    /**
     * 取消Handler
     */
    private void cancelHandler() {
        if (mPostHandler != null) {
            mPostHandler.removeMessages(MSG_POST_OK);
            mPostHandler.removeMessages(MSG_POST_ERROR);
        }
    }
}
