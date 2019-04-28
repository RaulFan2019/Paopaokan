package com.app.pao.activity.settings;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.activity.main.ImageCutActivity;
import com.app.pao.config.AppConfig;
import com.app.pao.config.AppEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.data.db.UserData;
import com.app.pao.entity.db.DBUserEntity;
import com.app.pao.entity.model.ProvinceBean;
import com.app.pao.entity.network.GetUploadAvatarResult;
import com.app.pao.network.api.MyRequestCallBack;
import com.app.pao.network.utils.RequestParamsBuild;
import com.app.pao.ui.dialog.v1.MyDialogBuilderV1;
import com.app.pao.ui.utils.XmlParserHandler;
import com.app.pao.ui.widget.CircularImage;
import com.app.pao.utils.FileUtils;
import com.app.pao.utils.ImageUtils;
import com.app.pao.utils.Log;
import com.app.pao.utils.QrCodeUtils;
import com.app.pao.utils.T;
import com.app.pao.utils.TimeUtils;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.rey.material.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import hwh.com.pickviewlibrary.OptionsPickerView;
import hwh.com.pickviewlibrary.TimePickerView;

/**
 * Created by Raul on 2015/11/16.
 * 完善用户基本信息页面
 */
@ContentView(R.layout.activity_perfect_userinfo)
public class UserBasicInfoActivity extends BaseAppCompActivity implements View.OnClickListener {

    /* contains */
    private static final String TAG = "UserBasicInfoActivity";
    private static final int REQUEST_CHANGE_IMAGE = 0x01;
    private static final int REQUEST_CUT_IMAGE = 0x02;
    private static final int REQUEST_EDIT_NICKNAME = 0x03;
    private static final int REQUEST_EDIT_GENDER = 0x04;
    private static final int REQUEST_CUT_IMAGE_FOR_M = 0x05;

    private static final int MSG_POST_ERROR = 1;//上传出错
    private static final int MSG_POST_PHOTO_OK = 2;//上传头像成功
    private static final int MSG_POST_LOCATION_OK = 3;//上传位置成功
    private static final int MSG_POST_BIRTHDAY_OK = 4;//上传生日成功
    private static final int MSG_POST_HEIGHT_OK = 5;//上传身高成功
    private static final int MSG_POST_WEIGHT_OK = 6;//上传体重成功

    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 0x00;

    /* local data */
    private DBUserEntity mUserEntity;//用户信息

    private boolean isChanged;//是否有改变
    private boolean mPostAble;//是否可以上传信息

    private BitmapUtils mBitmapUtils;//图片工具
    private ArrayList<String> mSelectImagePathList;//图像列表
    private File mAvatarF;//头像文件

    /* local view */
    @ViewInject(R.id.iv_perfectuser_user_avatar)
    private CircularImage mAvatarIv;//用户头像
    @ViewInject(R.id.tv_nickname)
    private TextView mNickanmeTv;//用户昵称
    @ViewInject(R.id.tv_user_location)
    private TextView mLocationTv;//位置
    @ViewInject(R.id.tv_gander)
    private TextView mSexTv;//性别
    @ViewInject(R.id.tv_birthday)
    private TextView mBirthdayTv;//生日
    @ViewInject(R.id.tv_height)
    private TextView mHeightTv;//身高
    @ViewInject(R.id.tv_weight)
    private TextView mWeightTv;//体重

    private TimePickerView mBirthdayPick;//生日设置框
    private OptionsPickerView mLocationPick;//地址设置框
    private OptionsPickerView mSetPick; //身高体重设置框


    /**
     * 请求Handler
     */
    Handler mPostHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            mPostAble = true;
            mDialogBuilder.progressDialog.dismiss();

            super.handleMessage(msg);
            switch (msg.what) {
                //上传出现错误
                case MSG_POST_ERROR:
                    T.showLong(mContext, (String) msg.obj);
                    break;
                //上传照片成功
                case MSG_POST_PHOTO_OK:
                    ImageUtils.loadUserImage(mUserEntity.getAvatar(), mAvatarIv);
                    break;
                //上传位置
                case MSG_POST_LOCATION_OK:
                    mLocationTv.setText(mUserEntity.getProvince() + " " + mUserEntity.getCity());
                    break;
                //上传生日
                case MSG_POST_BIRTHDAY_OK:
                    mBirthdayTv.setText(mUserEntity.getBirthday());
                    break;
                //上传身高
                case MSG_POST_HEIGHT_OK:
                    mHeightTv.setText(mUserEntity.getHeight() + "厘米");
                    break;
                //上传体重
                case MSG_POST_WEIGHT_OK:
                    mWeightTv.setText(mUserEntity.getWeight() + "公斤");
                    break;
            }
        }
    };

    /**
     * 点击事件
     *
     * @param v
     */
    @Override
    @OnClick({R.id.title_bar_left_menu, R.id.ll_edit_avatar, R.id.ll_edit_nickname,
            R.id.ll_edit_location, R.id.ll_edit_gander, R.id.ll_edit_birthday,
            R.id.ll_edit_height, R.id.ll_edit_weight,R.id.ll_qrcode})
    public void onClick(View v) {
        switch (v.getId()) {
            //点击左上角返回
            case R.id.title_bar_left_menu:
                onBackPressed();
                break;
            //编辑头像
            case R.id.ll_edit_avatar:
                changePhoto();
                break;
            //编辑昵称
            case R.id.ll_edit_nickname:
                changeNickname();
                break;
            //编辑地址
            case R.id.ll_edit_location:
                changeLocation();
                break;
            //编辑性别
            case R.id.ll_edit_gander:
                changeSex();
                break;
            //编辑生日
            case R.id.ll_edit_birthday:
                changeBirthday();
                break;
            //编辑身高
            case R.id.ll_edit_height:
                changeHeight();
                break;
            //编辑体重
            case R.id.ll_edit_weight:
                changeWeight();
                break;
            //跳转到我的二维码界面
            case R.id.ll_qrcode:
                startMyQRCode();
                break;
//            //跳转到帐号绑定页面
//            case R.id.ll_edit_safe:
//                launchSafeSettings();
//                break;
        }
    }

    /**
     * 返回键行为
     */
    @Override
    public void onBackPressed() {
        //发生改变
        if (isChanged) {
            //TODO
        } else {
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                T.showShort(mContext, "已授权");
            } else {
                // Permission Denied
            }
        }
    }

    /**
     * on Activity result
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHANGE_IMAGE) {
            if (resultCode == RESULT_OK) {
                mSelectImagePathList = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                ImageUtils.startPhotoCut(this, Uri.fromFile(new File(mSelectImagePathList.get(0))), REQUEST_CUT_IMAGE);
            }
        } else if (requestCode == REQUEST_CUT_IMAGE) {
            if (resultCode == RESULT_OK) {
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(AppConfig.cutFileUri));
                    Bitmap resultBmap = ImageUtils.compressBitmap(bitmap);
                    mAvatarF = FileUtils.saveBigBitmap(resultBmap);
                    postAvatar();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }  else if (requestCode == REQUEST_EDIT_NICKNAME) {
            if (resultCode == RESULT_OK) {
                //修改姓名成功
                mUserEntity = LocalApplication.getInstance().getLoginUser(mContext);
                mNickanmeTv.setText(mUserEntity.getNickname());
                T.showShort(mContext, "修改昵称成功!");
            }
        } else if (requestCode == REQUEST_EDIT_GENDER) {
            if (resultCode == RESULT_OK) {
                //修改姓名成功
                mUserEntity = LocalApplication.getInstance().getLoginUser(mContext);
                if (mUserEntity.getGender() == AppEnum.UserGander.WOMEN) {
                    mSexTv.setText(mContext.getResources().getString(R.string.Gander_Women));
                } else {
                    mSexTv.setText(mContext.getResources().getString(R.string.Gander_Man));
                }
            }
        }
    }

    @Override
    protected void initData() {
        isChanged = false;
        mPostAble = true;
        mUserEntity = LocalApplication.getInstance().getLoginUser(mContext);
        mBitmapUtils = new BitmapUtils(mContext);

        //获取当前年份
        Calendar calendar = Calendar.getInstance();
        int curYear = calendar.get(Calendar.YEAR);

        //初始化生日选择器
        mBirthdayPick = new TimePickerView(mContext, TimePickerView.Type.YEAR_MONTH_DAY, 1950, curYear);
        //初始化地址选择器
        mLocationPick = new OptionsPickerView(mContext);
        //初始化体重身高设置选择器
        mSetPick = new OptionsPickerView(mContext);
    }

    @Override
    protected void initViews() {
        ImageUtils.loadUserImage(mUserEntity.getAvatar(), mAvatarIv);
        mNickanmeTv.setText(mUserEntity.getNickname());
        mLocationTv.setText(mUserEntity.getProvince() + " " + mUserEntity.getCity());
        if (mUserEntity.getGender() == AppEnum.UserGander.WOMEN) {
            mSexTv.setText(mContext.getResources().getString(R.string.Gander_Women));
        } else {
            mSexTv.setText(mContext.getResources().getString(R.string.Gander_Man));
        }
        mBirthdayTv.setText(mUserEntity.getBirthday());
        mHeightTv.setText(mUserEntity.getHeight() + "厘米");
        mWeightTv.setText(mUserEntity.getWeight() + "公斤");

//        //绘制二维码
//        if (mUserEntity.getQrcode() != null && !mUserEntity.getQrcode().isEmpty()) {
//            mQrCodeIv.setImageBitmap(QrCodeUtils.create2DCode(mUserEntity.getQrcode()));
//        }
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
     * 更换照片
     */
    private void changePhoto() {
        Intent intent = new Intent(mContext, MultiImageSelectorActivity.class);
        // 是否显示拍摄图片
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
        // 最大可选择图片数量
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 1);
        // 选择模式
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_SINGLE);
        startActivityForResult(intent, REQUEST_CHANGE_IMAGE);
    }

    /**
     * 编辑昵称
     */
    private void changeNickname() {

        startActivityForResult(EditUserNicknameActivity.class, REQUEST_EDIT_NICKNAME);
    }

    /**
     * 跳转到帐号绑定页面
     */
    private void launchSafeSettings() {

    }

    /**
     * 弹出地址设置框
     */
    private void changeLocation() {
        //联动选择器1 省
        final ArrayList<ProvinceBean> options1Items = new ArrayList<ProvinceBean>();
        //联动选择器2 市
        final ArrayList<ArrayList<String>> options2Items = new ArrayList<ArrayList<String>>();
        //选择器默认位置值
        int options1Pos = 0;
        int options2Pos = 0;
        //获取默认数据
        String province = mUserEntity.getProvince();
        String city = mUserEntity.getCity();
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

        //初始化选择器数据
        mLocationPick.setPicker(options1Items, options2Items, true);
        //设置选择器是否无限滚动
        mLocationPick.setCyclic(false);
        //设置选择器标题
        mLocationPick.setTitle("选择地址");
        //设置选择器默认值
        mLocationPick.setSelectOptions(options1Pos, options2Pos);
        //选择器回调
        mLocationPick.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                postUpdateLocation(options1Items.get(options1).getName(), options2Items.get(options1).get(option2));
            }
        });

        mLocationPick.show();
    }

    /**
     * 修改性别
     */
    private void changeSex() {
        startActivityForResult(EditUserGenderActivity.class, REQUEST_EDIT_GENDER);
    }

    /**
     * 设置生日
     */
    private void changeBirthday() {

        //设置时间选择器的默认参数
        mBirthdayPick.setTime(TimeUtils.birthdayStrToDate(mUserEntity.getBirthday()));

        //设置选择器是否可以循环滚动
        mBirthdayPick.setCyclic(false);
        //设置选择时间回调
        mBirthdayPick.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                //开始向服务器发送更新请求
                postUpdateBirthday(TimeUtils.dateToString(date));
            }
        });
        mBirthdayPick.setCancelable(true);
        mBirthdayPick.show();
    }

    /**
     * 设置身高
     */
    private void changeHeight() {
        int defaultHeight = mUserEntity.getHeight();
        final ArrayList<Integer> options1Items = new ArrayList<Integer>();
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
        showSetPick("身高设置", "厘米", options1Pos, options1Items, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                postUpdateHeight(options1Items.get(options1));
            }
        });

    }

    /**
     * 设置体重
     */
    private void changeWeight() {
        int defaultWeight = mUserEntity.getWeight();
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

        showSetPick("体重设置", "公斤", options1Pos, options1Items, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                postUpdateWeight(options1Items.get(options1));
            }
        });
    }

    private void startMyQRCode(){
        Bundle bundle = new Bundle();
        bundle.putString("avatar",mUserEntity.getAvatar());
        bundle.putString("nickName",mUserEntity.getNickname());
        bundle.putString("qrcode",mUserEntity.getQrcode());
        bundle.putInt("hasSys",0);
        startActivity(MyQRCodeActivity.class, bundle);
    }

    /**
     * 显示Height 或 Weight 设置框
     *
     * @param title          标题
     * @param selectListener 监听回调事件
     */
    private void showSetPick(final String title, final String labels, final int position, final ArrayList<Integer> options1Items, OptionsPickerView.OnOptionsSelectListener selectListener) {

        //初始化选择器数据
        mSetPick.setPicker(options1Items);
        //设置选择器是否无限滚动
        mSetPick.setCyclic(false);
        //设置选择器标题
        mSetPick.setTitle(title);

        mSetPick.setLabels(labels);
        //设置选择器默认值
        mSetPick.setSelectOptions(position);
        //选择器回调
        mSetPick.setOnoptionsSelectListener(selectListener);
        mSetPick.show();
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
     * 上传头像到服务器
     */
    private void postAvatar() {
        if (!mPostAble) {
            return;
        }
        showProgressDialog("正在上传头像..");
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_UPLOAD_AVATAR;
        RequestParams params = RequestParamsBuild.buildUploadAvatarRequest(mContext,mAvatarF);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                if (mPostHandler != null) {
                    Message mPostMsg = new Message();
                    mPostMsg.what = MSG_POST_ERROR;
                    mPostMsg.obj = errorMsg;
                    mPostHandler.sendMessage(mPostMsg);
                }
            }

            @Override
            protected void onRightResponse(String Response) {
                GetUploadAvatarResult resultEntity = JSON.parseObject(Response, GetUploadAvatarResult.class);
                String avatar = resultEntity.getUrl();
                postUpdateUserAvatar(avatar);
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                if (mPostHandler != null) {
                    Message mPostMsg = new Message();
                    mPostMsg.what = MSG_POST_ERROR;
                    mPostMsg.obj = s;
                    mPostHandler.sendMessage(mPostMsg);
                }
            }

            @Override
            protected void onFinish() {

            }
        });
    }

    /**
     * 修改用户头像信息
     */
    private void postUpdateUserAvatar(final String avatar) {

        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_UPDATE_USER_INFO;
        RequestParams params = RequestParamsBuild.buildResetUserAvatarRequest(mContext,avatar);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                if (mPostHandler != null) {
                    Message mPostMsg = new Message();
                    mPostMsg.what = MSG_POST_ERROR;
                    mPostMsg.obj = errorMsg;
                    mPostHandler.sendMessage(mPostMsg);
                }
            }

            @Override
            protected void onRightResponse(String Response) {
                mUserEntity.setAvatar(avatar);
                UserData.updateUser(mContext,mUserEntity);
                LocalApplication.getInstance().setLoginUser(mUserEntity);
                if (mPostHandler != null) {
                    Message mPostMsg = new Message();
                    mPostMsg.what = MSG_POST_PHOTO_OK;
                    mPostHandler.sendMessage(mPostMsg);
                }
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                if (mPostHandler != null) {
                    Message mPostMsg = new Message();
                    mPostMsg.what = MSG_POST_ERROR;
                    mPostMsg.obj = s;
                    mPostHandler.sendMessage(mPostMsg);
                }
            }

            @Override
            protected void onFinish() {

            }
        });
    }

    /**
     * 修改所在地位置信息
     */
    private void postUpdateLocation(final String province, final String city) {
        if (!mPostAble) {
            return;
        }
        showProgressDialog("正在更新···");
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_UPDATE_USER_INFO;
        RequestParams params = RequestParamsBuild.buildResetUserLocationRequest(mContext,province, city);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                if (mPostHandler != null) {
                    Message mPostMsg = new Message();
                    mPostMsg.what = MSG_POST_ERROR;
                    mPostMsg.obj = errorMsg;
                    mPostHandler.sendMessage(mPostMsg);
                }
            }

            @Override
            protected void onRightResponse(String Response) {
                mUserEntity.setProvince(province);
                mUserEntity.setCity(city);
                UserData.updateUser(mContext,mUserEntity);
                LocalApplication.getInstance().setLoginUser(mUserEntity);
                if (mPostHandler != null) {
                    Message mPostMsg = new Message();
                    mPostMsg.what = MSG_POST_LOCATION_OK;
                    mPostHandler.sendMessage(mPostMsg);
                }
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                if (mPostHandler != null) {
                    Message mPostMsg = new Message();
                    mPostMsg.what = MSG_POST_ERROR;
                    mPostMsg.obj = s;
                    mPostHandler.sendMessage(mPostMsg);
                }
            }

            @Override
            protected void onFinish() {

            }
        });
    }

    /**
     * 修改用户生日
     *
     * @param birthday
     */
    private void postUpdateBirthday(final String birthday) {
        if (!mPostAble) {
            return;
        }
        showProgressDialog("正在更新···");
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_UPDATE_USER_INFO;
        RequestParams params = RequestParamsBuild.buildResetUserBirthdayRequest(mContext,birthday);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                if (mPostHandler != null) {
                    Message mPostMsg = new Message();
                    mPostMsg.what = MSG_POST_ERROR;
                    mPostMsg.obj = errorMsg;
                    mPostHandler.sendMessage(mPostMsg);
                }
            }

            @Override
            protected void onRightResponse(String Response) {
                mUserEntity.setBirthday(birthday);
                UserData.updateUser(mContext,mUserEntity);
                LocalApplication.getInstance().setLoginUser(mUserEntity);
                if (mPostHandler != null) {
                    Message mPostMsg = new Message();
                    mPostMsg.what = MSG_POST_BIRTHDAY_OK;
                    mPostHandler.sendMessage(mPostMsg);
                }
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                if (mPostHandler != null) {
                    Message mPostMsg = new Message();
                    mPostMsg.what = MSG_POST_ERROR;
                    mPostMsg.obj = s;
                    mPostHandler.sendMessage(mPostMsg);
                }
            }

            @Override
            protected void onFinish() {

            }
        });
    }

    /**
     * 修改用户身高
     *
     * @param height
     */
    private void postUpdateHeight(final int height) {
        if (!mPostAble) {
            return;
        }
        showProgressDialog("正在更新···");
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_UPDATE_USER_INFO;
        RequestParams params = RequestParamsBuild.buildResetUserHeightRequest(mContext,height);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                if (mPostHandler != null) {
                    Message mPostMsg = new Message();
                    mPostMsg.what = MSG_POST_ERROR;
                    mPostMsg.obj = errorMsg;
                    mPostHandler.sendMessage(mPostMsg);
                }
            }

            @Override
            protected void onRightResponse(String Response) {
                mUserEntity.setHeight(height);
                UserData.updateUser(mContext,mUserEntity);
                LocalApplication.getInstance().setLoginUser(mUserEntity);
                if (mPostHandler != null) {
                    Message mPostMsg = new Message();
                    mPostMsg.what = MSG_POST_HEIGHT_OK;
                    mPostHandler.sendMessage(mPostMsg);
                }
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                if (mPostHandler != null) {
                    Message mPostMsg = new Message();
                    mPostMsg.what = MSG_POST_ERROR;
                    mPostMsg.obj = s;
                    mPostHandler.sendMessage(mPostMsg);
                }
            }

            @Override
            protected void onFinish() {

            }
        });
    }

    /**
     * 修改用户体重
     *
     * @param weight
     */
    private void postUpdateWeight(final int weight) {
        if (!mPostAble) {
            return;
        }
        showProgressDialog("正在更新···");
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_UPDATE_USER_INFO;
        RequestParams params = RequestParamsBuild.buildResetUserWeightRequest(mContext,weight);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                if (mPostHandler != null) {
                    Message mPostMsg = new Message();
                    mPostMsg.what = MSG_POST_ERROR;
                    mPostMsg.obj = errorMsg;
                    mPostHandler.sendMessage(mPostMsg);
                }
            }

            @Override
            protected void onRightResponse(String Response) {
                mUserEntity.setWeight(weight);
                UserData.updateUser(mContext,mUserEntity);
                LocalApplication.getInstance().setLoginUser(mUserEntity);
                if (mPostHandler != null) {
                    Message mPostMsg = new Message();
                    mPostMsg.what = MSG_POST_WEIGHT_OK;
                    mPostHandler.sendMessage(mPostMsg);
                }
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                if (mPostHandler != null) {
                    Message mPostMsg = new Message();
                    mPostMsg.what = MSG_POST_ERROR;
                    mPostMsg.obj = s;
                    mPostHandler.sendMessage(mPostMsg);
                }
            }

            @Override
            protected void onFinish() {

            }
        });
    }

    //显示等待信息
    private void showProgressDialog(String msg) {
        mDialogBuilder.showProgressDialog(this, msg, true);
        mDialogBuilder.setListener(new MyDialogBuilderV1.ProgressDilaogListener() {
            @Override
            public void onCancelBtnClick() {
                mPostAble = true;
                cancelPostHandler();
            }

            @Override
            public void onCancel() {
                mPostAble = true;
                cancelPostHandler();
            }
        });
    }

    /**
     * 取消请求
     */
    private void cancelPostHandler() {
        if (mPostHandler != null) {
            mPostHandler.removeMessages(MSG_POST_PHOTO_OK);
            mPostHandler.removeMessages(MSG_POST_ERROR);
        }
    }

}
