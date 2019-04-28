package com.app.pao.activity.regist;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.app.pao.ActivityStackManager;
import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.activity.BaseActivityV3;
import com.app.pao.activity.main.MainActivityV2;
import com.app.pao.activity.settings.MultiImageSelectorActivity;
import com.app.pao.config.AppConfig;
import com.app.pao.config.AppEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.data.PreferencesData;
import com.app.pao.data.db.UserData;
import com.app.pao.entity.db.DBUserEntity;
import com.app.pao.entity.model.ProvinceBean;
import com.app.pao.entity.network.GetCommonLoginResult;
import com.app.pao.entity.network.GetRegistResult;
import com.app.pao.entity.network.GetUploadAvatarResult;
import com.app.pao.entity.network.REBase;
import com.app.pao.network.api.BaseResponseParser;
import com.app.pao.network.utils.HttpExceptionHelper;
import com.app.pao.network.utils.RequestParamsBuilder;
import com.app.pao.ui.dialog.v1.MyDialogBuilderV1;
import com.app.pao.ui.utils.XmlParserHandler;
import com.app.pao.ui.widget.CircularImage;
import com.app.pao.utils.FileUtils;
import com.app.pao.utils.ImageUtils;
import com.app.pao.utils.Log;
import com.app.pao.utils.StringUtils;
import com.app.pao.utils.T;
import com.app.pao.utils.TimeUtils;
import com.app.pao.utils.business.LoginUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import hwh.com.pickviewlibrary.OptionsPickerView;
import hwh.com.pickviewlibrary.TimePickerView;

/**
 * Created by Raul.Fan on 2016/9/11.
 */
public class RegisterEndActivity extends BaseActivityV3 {

    /* contains */
    private static final int REQUEST_CHANGE_IMAGE = 0x01;
    private static final int REQUEST_CUT_IMAGE = 0x02;

    private static final int MSG_ERROR = 0x01;
    private static final int MSG_REGISTER_OK = 0x02;//注册成功

    /* local view */
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.iv_user_avatar)
    CircularImage mAvatarIv;
    @BindView(R.id.et_nickname)
    EditText mNickNameEt;
    @BindView(R.id.iv_man_select)
    ImageView mManIv;
    @BindView(R.id.tv_man_select)
    TextView mManTv;
    @BindView(R.id.iv_woman_select)
    ImageView mWomanIv;
    @BindView(R.id.tv_woman_select)
    TextView mWomanTv;
    @BindView(R.id.tv_birthday)
    TextView mBirthdayTv;
    @BindView(R.id.tv_height)
    TextView mHeightTv;
    @BindView(R.id.tv_weight)
    TextView mWeightTv;
    @BindView(R.id.tv_user_location)
    TextView mLocationTv;

    private OptionsPickerView mLocationPickV;//位置选择器
    private OptionsPickerView mHeightPickV;//位置选择器
    private OptionsPickerView mWeightPickV;//位置选择器
    private TimePickerView mBirthdayPick;//生日设置框

    private MyDialogBuilderV1 mDialogBuilder;

    /* local data */
    private DBUserEntity mUserEntity;//用户信息

    private String mBirthday = "1991-01-01";
    private int mHeight = 170;
    private int mWeight = 50;
    private String mProvince = "上海";
    private String mCity = "浦东新区";
    private String mAvatar = "";
    private String mNickName = "";
    private int mGender = AppEnum.UserGander.MAN;
    private File mAvatarF;//头像文件
    private boolean mHasChangeAvatar = false;

    private String mPhoneNum;//传过来的手机号
    private String mPassword;//传过来的密码
    private int mRegisterType;//微信注册还是手机注册

    private Callback.Cancelable mCancelable;//取消网络事件

    @Override
    protected int getLayoutId() {
        return R.layout.activity_regist_add_info;
    }

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

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                //请求发生错误
                case MSG_ERROR:
                    T.showShort(RegisterEndActivity.this, (String) msg.obj);
                    mDialogBuilder.progressDialog.dismiss();
                    break;
                //注册成功
                case MSG_REGISTER_OK:
                    mDialogBuilder.progressDialog.dismiss();
                    startActivity(MainActivityV2.class);
                    ActivityStackManager.getAppManager().finishAllTempActivity();
                    break;
            }
        }
    };

    /**
     * on Activity result
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CHANGE_IMAGE) {
            if (resultCode == RESULT_OK) {
                ArrayList<String> mSelectList = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                ImageUtils.startPhotoCut(this, Uri.fromFile(new File(mSelectList.get(0))), REQUEST_CUT_IMAGE);
            }
        } else if (requestCode == REQUEST_CUT_IMAGE) {
            if (resultCode == RESULT_OK) {
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(AppConfig.cutFileUri));
                    Bitmap resultBmp = ImageUtils.compressBitmap(bitmap, 30);
                    mAvatarF = FileUtils.saveBigBitmap(resultBmp);
                    mAvatarIv.setImageBitmap(resultBmp);
                    mHasChangeAvatar = true;
                    bitmap.recycle();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @OnClick({R.id.ll_man_select, R.id.ll_woman_select, R.id.ll_edit_birthday,
            R.id.ll_edit_height, R.id.ll_edit_weight, R.id.ll_edit_location, R.id.btn_regist_commit
            , R.id.iv_user_avatar})
    public void onClick(View view) {
        switch (view.getId()) {
            //切换头像
            case R.id.iv_user_avatar:
                changePhoto();
                break;
            //点击男性
            case R.id.ll_man_select:
                onGenderCheck(true);
                break;
            //点击女性
            case R.id.ll_woman_select:
                onGenderCheck(false);
                break;
            //点击生日
            case R.id.ll_edit_birthday:
                changeBirthday();
                break;
            //修改身高
            case R.id.ll_edit_height:
                changeHeight();
                break;
            //修改体重
            case R.id.ll_edit_weight:
                changeWeight();
                break;
            //修改位置
            case R.id.ll_edit_location:
                showLocationPick();
                break;
            //点击提交按钮
            case R.id.btn_regist_commit:
                onCommitBtnClick();
                break;
        }
    }


    @Override
    protected void initData() {
        mCheckNewData = false;
        mPhoneNum = getIntent().getStringExtra("phonenum");
        mPassword = getIntent().getStringExtra("password");
        mRegisterType = getIntent().getIntExtra("type", AppEnum.RegistType.PhoneNumRegist);
        if (mRegisterType == AppEnum.RegistType.WeixinRegist) {
            mUserEntity = LocalApplication.getInstance().getLoginUser(RegisterEndActivity.this);
            GetCommonLoginResult entity = (GetCommonLoginResult) getIntent().getSerializableExtra("entity");
            mBirthday = entity.birthdate;
            mNickName = entity.weixinnickname;
            if (entity.height != 0) {
                mHeight = entity.height;
            }
            if (entity.weight != 0) {
                mWeight = entity.weight;
            }
            mProvince = entity.locationprovince;
            mCity = entity.locationcity;
            mAvatar = entity.avatar;
            mGender = entity.gender;
        }
    }

    @Override
    protected void initViews() {
        setSupportActionBar(mToolbar);
        mDialogBuilder = new MyDialogBuilderV1();
        //用户名称
        mNickNameEt.setText(mNickName);
        //头像设置
        ImageUtils.loadUserImage(mAvatar, mAvatarIv);
        //位置设置
        mLocationPickV = new OptionsPickerView(RegisterEndActivity.this);
        mLocationTv.setText(mProvince + "  " + mCity);
        //生日设置
        Calendar calendar = Calendar.getInstance();
        int curYear = calendar.get(Calendar.YEAR);
        mBirthdayPick = new TimePickerView(RegisterEndActivity.this, TimePickerView.Type.YEAR_MONTH_DAY, 1950, curYear);
        mBirthdayPick.setTitle("选择生日");
        mBirthdayTv.setText(mBirthday);
        //身高设置
        mHeightPickV = new OptionsPickerView(RegisterEndActivity.this);
        mHeightTv.setText(mHeight + "厘米");
        //体重设置
        mWeightPickV = new OptionsPickerView(RegisterEndActivity.this);
        mWeightTv.setText(mWeight + "公斤");
        //性别设置
        onGenderCheck(mGender == AppEnum.UserGander.MAN);

    }

    @Override
    protected void doMyCreate() {
        ActivityStackManager.getAppManager().addTempActivity(this);
    }

    @Override
    protected void causeGC() {
        ActivityStackManager.getAppManager().finishTempActivity(this);
    }

    /**
     * 选择性别
     *
     * @param isMan
     */
    private void onGenderCheck(boolean isMan) {
        if (isMan) {
            mManIv.setBackgroundResource(R.drawable.icon_man_selected);
            mManTv.setTextColor(Color.parseColor("#67abff"));
            mWomanIv.setBackgroundResource(R.drawable.icon_woman_gray);
            mWomanTv.setTextColor(Color.parseColor("#888888"));
            mGender = AppEnum.UserGander.MAN;
        } else {
            mManIv.setBackgroundResource(R.drawable.icon_man_gray);
            mManTv.setTextColor(Color.parseColor("#888888"));
            mWomanIv.setBackgroundResource(R.drawable.icon_woman_selected);
            mWomanTv.setTextColor(Color.parseColor("#fe6b8d"));
            mGender = AppEnum.UserGander.WOMEN;
        }
    }

    /**
     * 设置出生年月
     */
    private void changeBirthday() {
        //设置时间选择器的默认参数
        mBirthdayPick.setTime(TimeUtils.formatStrToDate(mBirthday, TimeUtils.FORMAT_TYPE_3));
        //设置选择器是否可以循环滚动
        mBirthdayPick.setCyclic(false);
        //设置选择时间回调
        mBirthdayPick.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                //开始向服务器发送更新请求
                mBirthday = TimeUtils.formatDateToStr(date, TimeUtils.FORMAT_TYPE_3);
                mBirthdayTv.setText(mBirthday);
            }
        });
        mBirthdayPick.setCancelable(true);
        mBirthdayPick.show();
    }


    /**
     * 设置身高
     */
    private void changeHeight() {
        int defaultHeight = mHeight;
        final ArrayList<Integer> options1Items = new ArrayList<>();
        //选择器默认位置值
        int options1Pos = 0;
        //插值
        for (int i = 100; i <= 300; i++) {
            options1Items.add(i);
            //若值相同则为默认值
            if (i == defaultHeight) {
                options1Pos = i - 100;
            }
        }

        mHeightPickV.setCancelable(true);
        //初始化选择器数据
        mHeightPickV.setPicker(options1Items);
        //设置选择器是否无限滚动
        mHeightPickV.setCyclic(false);
        //设置选择器标题
        mHeightPickV.setTitle("身高设置");

        mHeightPickV.setLabels("厘米");
        //设置选择器默认值
        mHeightPickV.setSelectOptions(options1Pos);
        //选择器回调
        mHeightPickV.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                mHeight = options1Items.get(options1);
                mHeightTv.setText(mHeight + "厘米");
            }
        });
        mHeightPickV.show();
    }

    /**
     * 设置体重
     */
    private void changeWeight() {
        int defaultWeight = mWeight;
        final ArrayList<Integer> options1Items = new ArrayList<Integer>();
        //选择器默认位置值
        int options1Pos = 0;
        //插值
        for (int i = 35; i <= 300; i++) {
            options1Items.add(i);
            //若值相同则为默认值
            if (i == defaultWeight) {
                options1Pos = i - 35;
            }
        }

        mWeightPickV.setCancelable(true);
        //初始化选择器数据
        mWeightPickV.setPicker(options1Items);
        //设置选择器是否无限滚动
        mWeightPickV.setCyclic(false);
        //设置选择器标题
        mWeightPickV.setTitle("体重设置");

        mWeightPickV.setLabels("公斤");
        //设置选择器默认值
        mWeightPickV.setSelectOptions(options1Pos);
        //选择器回调
        mWeightPickV.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                mWeight = options1Items.get(options1);
                mWeightTv.setText(mWeight + "公斤");
            }
        });
        mWeightPickV.show();
    }

    /**
     * 显示地区选择器
     */
    private void showLocationPick() {
        //联动选择器1 省
        final ArrayList<ProvinceBean> options1Items = new ArrayList<ProvinceBean>();
        //联动选择器2 市
        final ArrayList<ArrayList<String>> options2Items = new ArrayList<ArrayList<String>>();
        //选择器默认位置值
        int options1Pos = 0;
        int options2Pos = 0;
        //获取默认数据
        final String province = mProvince;
        final String city = mCity;
        //初始化省数据
        if (initLocData() != null) {
            options1Items.addAll(initLocData().getProvinceDataList());
        }
        //初始化市数据
        for (int i = 0; i < options1Items.size(); i++) {
            ProvinceBean pro = options1Items.get(i);
            //插入第二项值
            options2Items.add(pro.getCityList());
            //获取省当前地址位置
            if (pro.getName().equals(province)) {
                options1Pos = i;
                //获取市
                for (int n = 0; n < pro.getCityList().size(); n++) {
                    if (pro.getCityList().get(n).equals(city)) {
                        options2Pos = n;
                        break;
                    }
                }
            }
        }

        mLocationPickV.setCancelable(true);
        //初始化选择器数据
        mLocationPickV.setPicker(options1Items, options2Items, true);
        //设置选择器是否无限滚动
        mLocationPickV.setCyclic(false);
        //设置选择器标题
        mLocationPickV.setTitle("选择地址");
        //设置选择器默认值
        mLocationPickV.setSelectOptions(options1Pos, options2Pos);
        //选择器回调
        mLocationPickV.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                mProvince = options1Items.get(options1).getName();
                mCity = options2Items.get(options1).get(option2);
                mLocationTv.setText(mProvince + " " + mCity);
            }
        });

        mLocationPickV.show();
    }


    /**
     * 初始化XML位置数据
     */
    private final XmlParserHandler initLocData() {
        XmlParserHandler handler = new XmlParserHandler();
        AssetManager asset = getResources().getAssets();
        try {
            InputStream input = asset.open("province_data.xml");
            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();
            parser.parse(input, handler);
            input.close();

        } catch (Throwable e) {
            e.printStackTrace();
        } finally {

        }
        return handler;
    }

    /**
     * 改变头像
     */
    private void changePhoto() {
        Intent intent = new Intent(getApplicationContext(), MultiImageSelectorActivity.class);
        // 是否显示拍摄图片
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
        // 最大可选择图片数量
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 1);
        // 选择模式
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_SINGLE);
        startActivityForResult(intent, REQUEST_CHANGE_IMAGE);
    }


    /**
     * 提交注册
     */
    private void onCommitBtnClick() {
        //检查昵称是否有错误
        mNickName = mNickNameEt.getText().toString();
        String error = StringUtils.checkNickNameError(RegisterEndActivity.this, mNickName);
        if (!error.equals(AppEnum.DEFAULT_CHECK_ERROR)) {
            T.showShort(RegisterEndActivity.this, error);
            mNickNameEt.setHintTextColor(Color.parseColor("#f06522"));
            return;
        }
        //检查头像,是否设置
        if (mRegisterType != AppEnum.RegistType.WeixinRegist
                && (mAvatarF == null || !mAvatarF.exists())) {
            T.showShort(RegisterEndActivity.this, getResources().getString(R.string.Error_Check_Avatar_Empty));
            return;
        }
        //-- 正式开始提交流程 --//
        //发送注册提示
        mDialogBuilder.showProgressDialog(this, "注册中..", true);
        mDialogBuilder.setListener(new MyDialogBuilderV1.ProgressDilaogListener() {
            @Override
            public void onCancelBtnClick() {
                if (mCancelable != null) {
                    mCancelable.cancel();
                }
            }

            @Override
            public void onCancel() {
                if (mCancelable != null) {
                    mCancelable.cancel();
                }
            }
        });
        //判断是否需要上传头像
        if (mRegisterType != AppEnum.RegistType.WeixinRegist
                || mHasChangeAvatar) {
            postUserAvatar();
        }else {
            updateWeiXinUser();
        }
    }

    /**
     * 上传用户头像
     */
    private void postUserAvatar() {
        x.task().post(new Runnable() {
            @Override
            public void run() {
                RequestParams params = RequestParamsBuilder.buildUploadAvatarFileRequest(RegisterEndActivity.this,
                        URLConfig.URL_UPLOAD_AVATAR, mAvatarF);
                mCancelable = x.http().post(params, new Callback.CommonCallback<REBase>() {
                    @Override
                    public void onSuccess(REBase reBase) {
                        if (mHandler != null) {
                            if (reBase.errorcode == BaseResponseParser.ERROR_CODE_NONE) {
                                GetUploadAvatarResult entity = JSON.parseObject(reBase.result, GetUploadAvatarResult.class);
                                mAvatar = entity.url;
                                //若是微信登录，修改个人信息即可
                                if (mRegisterType == AppEnum.RegistType.WeixinRegist) {
                                    updateWeiXinUser();
                                    //注册用户
                                } else {
                                    registerUser();
                                }
                            } else {
                                Message mPostMsg = new Message();
                                mPostMsg.what = MSG_ERROR;
                                mPostMsg.obj = reBase.errormsg;
                                mHandler.sendMessage(mPostMsg);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable throwable, boolean b) {
                        if (mHandler != null) {
                            Message mPostMsg = new Message();
                            mPostMsg.what = MSG_ERROR;
                            mPostMsg.obj = HttpExceptionHelper.getErrorMsg(throwable);
                            mHandler.sendMessage(mPostMsg);
                        }
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
     * 更新微信用户信息
     */
    private void updateWeiXinUser() {
        RequestParams params = RequestParamsBuilder.buildUpdateUserInfoRP(RegisterEndActivity.this, URLConfig.URL_UPDATE_USER_INFO,
                mNickName, mBirthday, mWeight, mHeight, mAvatar, mGender, mProvince, mCity,
                JPushInterface.getRegistrationID(RegisterEndActivity.this));
        x.http().post(params, new Callback.CommonCallback<REBase>() {
            @Override
            public void onSuccess(REBase reBase) {
                if (mHandler != null) {
                    if (reBase.errorcode == BaseResponseParser.ERROR_CODE_NONE) {
                        mUserEntity.nickname = mNickName;
                        mUserEntity.birthday = mBirthday;
                        mUserEntity.weight = mWeight;
                        mUserEntity.height = mHeight;
                        mUserEntity.avatar = mAvatar;
                        mUserEntity.gender = mGender;
                        mUserEntity.province = mProvince;
                        mUserEntity.city = mCity;
                        UserData.updateUser(RegisterEndActivity.this, mUserEntity);
                        LocalApplication.getInstance().setLoginUser(mUserEntity);
                        mHandler.sendEmptyMessage(MSG_REGISTER_OK);
                    } else {
                        Message mPostMsg = new Message();
                        mPostMsg.what = MSG_ERROR;
                        mPostMsg.obj = reBase.errormsg;
                        mHandler.sendMessage(mPostMsg);
                    }
                }
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                if (mHandler != null) {
                    Message mPostMsg = new Message();
                    mPostMsg.what = MSG_ERROR;
                    mPostMsg.obj = HttpExceptionHelper.getErrorMsg(throwable);
                    mHandler.sendMessage(mPostMsg);
                }
            }

            @Override
            public void onCancelled(CancelledException e) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 注册用户
     */
    private void registerUser() {
        RequestParams params = RequestParamsBuilder.buildRegisterRP(RegisterEndActivity.this,
                URLConfig.URL_REGIST, mPhoneNum, mNickName, mPassword, mAvatar, JPushInterface.getRegistrationID(RegisterEndActivity.this),
                mGender, mWeight, mHeight, mBirthday, mProvince, mCity);
        mCancelable = x.http().post(params, new Callback.CommonCallback<REBase>() {
            @Override
            public void onSuccess(REBase reBase) {
                if (mHandler != null) {
                    if (reBase.errorcode == BaseResponseParser.ERROR_CODE_NONE) {
                        GetRegistResult resultEntity = JSON.parseObject(reBase.result, GetRegistResult.class);
                        int userId = resultEntity.getId();
                        PreferencesData.setPassword(RegisterEndActivity.this, mPassword);
                        PreferencesData.setUserId(RegisterEndActivity.this, userId);
                        PreferencesData.setUserName(RegisterEndActivity.this, mPhoneNum);
                        PreferencesData.setHasLogin(RegisterEndActivity.this, true);
                        mUserEntity = UserData.ResigtUser(RegisterEndActivity.this, resultEntity);
                        LocalApplication.getInstance().setLoginUser(mUserEntity);
                        mHandler.sendEmptyMessage(MSG_REGISTER_OK);
                    } else {
                        Message mPostMsg = new Message();
                        mPostMsg.what = MSG_ERROR;
                        mPostMsg.obj = reBase.errormsg;
                        mHandler.sendMessage(mPostMsg);
                    }
                }
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                if (mHandler != null) {
                    Message mPostMsg = new Message();
                    mPostMsg.what = MSG_ERROR;
                    mPostMsg.obj = HttpExceptionHelper.getErrorMsg(throwable);
                    mHandler.sendMessage(mPostMsg);
                }
            }

            @Override
            public void onCancelled(CancelledException e) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
}
