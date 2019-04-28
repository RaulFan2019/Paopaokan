package com.app.pao.activity.group;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.app.pao.ActivityStackManager;
import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.activity.main.ImageCutActivity;
import com.app.pao.activity.settings.MultiImageSelectorActivity;
import com.app.pao.config.AppConfig;
import com.app.pao.config.AppEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.entity.model.ProvinceBean;
import com.app.pao.entity.network.GetUploadAvatarResult;
import com.app.pao.network.api.MyRequestCallBack;
import com.app.pao.network.utils.RequestParamsBuild;
import com.app.pao.ui.utils.XmlParserHandler;
import com.app.pao.utils.FileUtils;
import com.app.pao.utils.ImageUtils;
import com.app.pao.utils.StringUtils;
import com.app.pao.utils.T;
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
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import hwh.com.pickviewlibrary.OptionsPickerView;

/**
 * Created by Raul on 2015/11/25.
 * 创建跑团(创建跑团)
 */
@ContentView(R.layout.activity_create_group_commit)
public class  CreateGroupCommitActivity extends BaseAppCompActivity implements View.OnClickListener {

    /* contains */
    private static final String TAG = "CreateGroupCommitActivity";


    private static final int REQUEST_PHOTO = 1;
    private static final int REQUEST_CUT_PHOTO = 2;

    /* local view */
    @ViewInject(R.id.toolbar)
    private Toolbar mToolbar;//标题栏
    @ViewInject(R.id.iv_group_avatar)
    private ImageView mGroupIv;//跑团头像
    @ViewInject(R.id.et_name)
    private EditText mGroupNameEt;//跑团名称输入框
    @ViewInject(R.id.tv_location)
    private TextView mLocationTv;//位置文本
    @ViewInject(R.id.et_description)
    private EditText mDescriptionEt;//描述输入框

    private OptionsPickerView mLocationPick;//地址设置框

    /* local data */
    private String mName;//用户姓名
    private String mPhone;//用户手机号
    private String mEmail;//用户邮箱
    private String mUserAvatar;//用户照片地址
    private String mPartyAvatar;//活动照片地址

    private String mGroupAvatar;//跑团头像
    private File mGroupAvatarF;//跑团头像文件
    private String mGroupName;//跑团名称
    private String mGroupProvince;//跑团省
    private String mGroupCity;//跑团城市
    private String mGroupDescription;//跑团描述


    private BitmapUtils mBitmapUtils;

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

    /**
     * on Click
     *
     * @param v
     */
    @Override
    @OnClick({R.id.iv_group_avatar, R.id.btn_commit, R.id.tv_location})
    public void onClick(View v) {
        switch (v.getId()) {
            //点击跑团头像
            case R.id.iv_group_avatar:
                changePhoto(REQUEST_PHOTO);
                break;
            //提交审核
            case R.id.btn_commit:
                checkCommitInfo();
                break;
            //选择地址
            case R.id.tv_location:
                changeLocation();
                break;
        }
    }

    /**
     * on Activity result
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_PHOTO) {
                ArrayList<String> mSelectImagePathList = data.getStringArrayListExtra(MultiImageSelectorActivity
                        .EXTRA_RESULT);
                ImageUtils.startPhotoCut(this, Uri.fromFile(new File(mSelectImagePathList.get(0))), REQUEST_CUT_PHOTO);
            } else if (requestCode == REQUEST_CUT_PHOTO) {
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(AppConfig.cutFileUri));
                    Bitmap resultBmap = ImageUtils.compressBitmap(bitmap);
                    mGroupAvatarF = FileUtils.saveBigBitmap(resultBmap);
                    postAvatar(mGroupAvatarF);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }
    }


    /**
     * 上传头像到服务器
     */
    private void postAvatar(File mAvatarF) {
        mDialogBuilder.showProgressDialog(mContext, "正在上传照片..", false);
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_UPLOAD_AVATAR;
        RequestParams params = RequestParamsBuild.buildUploadAvatarRequest(mContext, mAvatarF);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                T.showShort(mContext, errorMsg);
            }

            @Override
            protected void onRightResponse(String Response) {
                GetUploadAvatarResult resultEntity = JSON.parseObject(Response, GetUploadAvatarResult.class);
                String avatar = resultEntity.getUrl();
                mGroupAvatar = avatar;
                mBitmapUtils.display(mGroupIv, String.valueOf(Uri.fromFile(mGroupAvatarF)));
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


    @Override
    protected void initData() {
        ActivityStackManager.getAppManager().addTempActivity(this);

        mName = getIntent().getExtras().getString("name");
        mPhone = getIntent().getExtras().getString("mEmail");
        mEmail = getIntent().getExtras().getString("mPhone");
        mUserAvatar = getIntent().getExtras().getString("mUserAvatar");
        mPartyAvatar = getIntent().getExtras().getString("mPartyAvatar");

        mBitmapUtils = new BitmapUtils(mContext);
        //初始化地址选择器
        mLocationPick = new OptionsPickerView(mContext);


    }

    @Override
    protected void initViews() {
        setSupportActionBar(mToolbar);
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
        ActivityStackManager.getAppManager().finishTempActivity(this);
    }


    /**
     * 更换照片
     */
    private void changePhoto(int Request) {
        Intent intent = new Intent(mContext, MultiImageSelectorActivity.class);
        // 是否显示拍摄图片
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
        // 最大可选择图片数量
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 1);
        // 选择模式
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_SINGLE);
        startActivityForResult(intent, Request);
    }

    /**
     * 提交审核
     */
    private void checkCommitInfo() {
        //检查团头像
        if (mGroupAvatar == null) {
            T.showShort(mContext, "团头像不能为空");
            return;
        }
        //检查团名称
        mGroupName = mGroupNameEt.getText().toString();
        String error = StringUtils.checkGroupNameError(mContext, mGroupName);
        if (!error.equals(AppEnum.DEFAULT_CHECK_ERROR)) {
            mGroupNameEt.setError(error);
            return;
        }
        //检查团地理位置
        if (mGroupCity == null || mGroupProvince == null || mGroupCity.equals("") || mGroupProvince.equals("")) {
            T.showShort(mContext, "地理位置不能为空");
            return;
        }
        //炮团描述检查
        mGroupDescription = mDescriptionEt.getText().toString();
        error = StringUtils.checkGroupDescription(mContext, mGroupDescription);
        if (!error.equals(AppEnum.DEFAULT_CHECK_ERROR)) {
            T.showShort(mContext, error);
            return;
        }
        postCreateGroup();
    }

    /**
     * 发送跑团提交申请
     */
    private void postCreateGroup() {
        mDialogBuilder.showProgressDialog(mContext, "正在提交审核", false);
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_CREATE_GROUP;
        RequestParams params = RequestParamsBuild.buildCreateGroupRequest(mContext, mGroupName, mGroupDescription,
                mGroupAvatar, mGroupProvince, mGroupCity, mName, mEmail, mPhone, "", mUserAvatar, mPartyAvatar);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                T.showShort(mContext, errorMsg);
            }

            @Override
            protected void onRightResponse(String Response) {
                T.showShort(mContext, "提交成功,请等待审核!");
                ActivityStackManager.getAppManager().finishAllTempActivity();
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
        String province = mGroupProvince;
        String city = mGroupCity;
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
     * 修改所在地位置信息
     */
    private void postUpdateLocation(final String province, final String city) {
        mGroupProvince = province;
        mGroupCity = city;
        mLocationTv.setText(mGroupProvince + " " + mGroupCity);
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
}
