package com.app.pao.activity.party;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.activity.settings.MultiImageSelectorActivity;
import com.app.pao.config.AppEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.entity.model.ProvinceBean;
import com.app.pao.entity.network.GetGroupDetailInfoRequest;
import com.app.pao.entity.network.GetGroupInfoResult;
import com.app.pao.entity.network.GetUploadAvatarResult;
import com.app.pao.network.api.MyRequestCallBack;
import com.app.pao.network.utils.RequestParamsBuild;
import com.app.pao.ui.utils.XmlParserHandler;
import com.app.pao.ui.widget.tagview.OnTagClickListener;
import com.app.pao.ui.widget.tagview.Tag;
import com.app.pao.ui.widget.tagview.TagView;
import com.app.pao.utils.FileUtils;
import com.app.pao.utils.ImageUtils;
import com.app.pao.utils.StringUtils;
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
import com.lidroid.xutils.view.annotation.event.OnTouch;
import com.rey.material.widget.CompoundButton;
import com.rey.material.widget.RadioButton;
import com.rey.material.widget.TextView;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import hwh.com.pickviewlibrary.OptionsPickerView;
import hwh.com.pickviewlibrary.TimePickerView;

/**
 * Created by Raul on 2015/12/7.
 * 发布活动页面
 */
@ContentView(R.layout.activity_group_create_party)
public class CreateGroupPartyActivity extends BaseAppCompActivity implements View.OnClickListener, View.OnTouchListener {

    /* contains */
    private static final String TAG = "CreateGroupPartyActivity";

    private static final int REQUEST_GET_LABEL_LIST = 1;
    private static final int REQUEST_GET_PARTY_PHOTO = 2;

    /* local data */
    private String mPartyName;//活动名称

    private String mStartTime = null;//活动开始时间字符串
    private String mRegistTIme = null;//报名截止时间字符串
    private String mEndTime = null;//活动结束时间字符串
    private Date mStartTimeDate = null;//开始时间
    private Date mRegistTimeDate = null;//报名截止时间
    private Date mEndTimeDate = null;//活动结束时间

    private String province;//省
    private String city;//市
    private String mLocation = null;//具体位置信息
    private String mDescription = null;//具体描述

    private GetGroupDetailInfoRequest mGroupInfo;//跑团详情
    private List<Tag> mTagList;
    private List<Tag> mSelectTagList;

    /* local view */
    @ViewInject(R.id.toolbar)
    private Toolbar mToolbar;//标题栏
    @ViewInject(R.id.et_name)
    private EditText mNameEt;//跑步名称输入框
    @ViewInject(R.id.tv_starttime)
    private TextView mStartTimeTv;//开始时间文本
    @ViewInject(R.id.tv_registtime)
    private TextView mRegistTimeTv;//报名截止时间文本
    @ViewInject(R.id.tv_end_time)
    private TextView mEndTimeTv;//活动结束时间文本
    @ViewInject(R.id.tv_city)
    private TextView mCityTv;//城市文本
    @ViewInject(R.id.et_location)
    private EditText mLocationEt;//具体位置输入框
    @ViewInject(R.id.et_description)
    private EditText mDescriptionEt;//描述输入框

    @ViewInject(R.id.iv_photo)
    private ImageView mPartyPicture;
    @ViewInject(R.id.tagv_select_tag)
    private TagView mSelectTagView;

    private TimePickerView mStartTimePick;//开始时间设置框
    private TimePickerView mRegistTimePick;//报名截止时间设置框
    private TimePickerView mEndTimePick;//报名截止时间设置框
    private OptionsPickerView mLocationPick;//地址设置框

    /* local data */
    private BitmapUtils bitmapUtils;
    private String mLabelIdList = "";
    private String mPhotoUrl = "";

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
     * on Touch
     *
     * @param v
     * @param event
     * @return
     */
    @Override
    @OnTouch({R.id.sv_base})
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.sv_base && event.getAction() == MotionEvent.ACTION_DOWN) {

            //输入法控制器
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//            T.showShort(mContext,"imm.isActive():" + imm.isActive());
//            if (imm.isActive()) {
//                //隐藏软键盘
//                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
//            }
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_GET_PARTY_PHOTO) {
                mDialogBuilder.showProgressDialog(mContext, "正在上传照片..", false);
                ArrayList<String> mSelectImagePathList = data.getStringArrayListExtra(MultiImageSelectorActivity
                        .EXTRA_RESULT);
                Bitmap resultBmap = ImageUtils.compressImage(mSelectImagePathList.get(0));
                postAvatar(FileUtils.saveBigBitmap(resultBmap));
            }
        }
    }


    @Override
    @OnClick({R.id.btn_commit, R.id.ll_starttime, R.id.ll_regist, R.id.ll_city, R.id.iv_photo, R.id.ll_end_time})
    public void onClick(View v) {
        switch (v.getId()) {
            //提交发布
            case R.id.btn_commit:
                createParty();
                break;
            //跑步开始时间
            case R.id.ll_starttime:
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                changeStartTime();
                break;
            //报名截止时间
            case R.id.ll_regist:
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                changeRegistTime();
                break;
            //选择城市
            case R.id.ll_city:
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                changeLocation();
                break;
            //活动照片
            case R.id.iv_photo:
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                addPhoto(REQUEST_GET_PARTY_PHOTO);
                break;
            //选择结束时间
            case R.id.ll_end_time:
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                changeEndTime();
                break;
        }
    }


    @Override
    protected void initData() {
        mGroupInfo = (GetGroupDetailInfoRequest) getIntent().getExtras().getSerializable("group");
        province = mGroupInfo.getRungroup().getLocationprovince();
        city = mGroupInfo.getRungroup().getLocationcity();

        //初始化地址选择器
        mLocationPick = new OptionsPickerView(mContext);

        Calendar calendar = Calendar.getInstance();
        int curYear = calendar.get(Calendar.YEAR);

        //初始化开始时间选择器
        mStartTimePick = new TimePickerView(mContext, TimePickerView.Type.ALL, curYear, curYear + 1);
        //初始化报名时间选择器
        mRegistTimePick = new TimePickerView(mContext, TimePickerView.Type.ALL, curYear, curYear + 1);
        //活动结束时间
        mEndTimePick = new TimePickerView(mContext, TimePickerView.Type.ALL, curYear, curYear + 1);
        //初始化标签页面
        mTagList = new ArrayList<Tag>();
        mSelectTagList = new ArrayList<Tag>();

        for (int i = 0; i < mGroupInfo.getRungroup().getLabel().size(); i++) {
            Tag tag = new Tag(mGroupInfo.getRungroup().getLabel().get(i).getName());
            tag.id = mGroupInfo.getRungroup().getLabel().get(i).getId();
            tag.layoutColor = Color.TRANSPARENT;
            tag.tagTextColor = Color.parseColor("#888888");
            tag.deleteIndicatorColor = Color.parseColor("#888888");
            tag.tagTextSize = 12;
            tag.radius = 2;
            tag.isShowImg = true;
            tag.imgDrawableRes = R.drawable.icon_tag_not_select;
            tag.layoutBorderColor = Color.parseColor("#888888");
            tag.layoutBorderSize = 1;
            tag.mTempValue = 0;
            mTagList.add(tag);
        }
        mSelectTagView.setLineMargin(5);
        mSelectTagView.addTags(mTagList);
        mSelectTagView.setOnTagClickListener(new OnTagClickListener() {
            @Override
            public void onTagClick(Tag tag, int position) {
                //若是选中状态
                if (tag.mTempValue == 1) {
                    tag.imgDrawableRes = R.drawable.icon_tag_not_select;
                    tag.tagTextColor = Color.parseColor("#888888");
                    tag.deleteIndicatorColor = Color.parseColor("#888888");
                    tag.layoutBorderColor = Color.parseColor("#888888");
                    tag.mTempValue = 0;
                    mSelectTagView.refreshOneTag(position, tag);
                    mSelectTagList.remove(tag);
                    //非选中
                } else {
                    tag.imgDrawableRes = R.drawable.icon_tag_select;
                    tag.tagTextColor = Color.parseColor("#F06522");
                    tag.deleteIndicatorColor = Color.parseColor("#F06522");
                    tag.layoutBorderColor = Color.parseColor("#F06522");
                    tag.mTempValue = 1;
                    mSelectTagView.refreshOneTag(position, tag);
                    mSelectTagList.add(tag);
                }
            }
        });
        bitmapUtils = new BitmapUtils(this);
    }

    @Override
    protected void initViews() {
        setSupportActionBar(mToolbar);
        mCityTv.setText(province + " " + city);
        mNameEt.requestFocus();
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
     * 创建活动
     */
    private void createParty() {
        //检查活动名称
        mPartyName = mNameEt.getText().toString();
        String error = StringUtils.checkPartyNameError(mContext, mPartyName);
        if (!error.equals(AppEnum.DEFAULT_CHECK_ERROR)) {
            mNameEt.setError(error);
            T.showShort(mContext, error);
            return;
        }
        mNameEt.setError(null);
        //检查是否设置了开始时间和报名截止时间
        if (mStartTimeDate == null) {
            mStartTimeTv.setError("尚未设置开始时间");
            T.showShort(mContext, "尚未设置开始时间");
            return;
        }
        mStartTimeTv.setError(null);
        //检查是否设置了报名截止时间
        if (mStartTimeDate == null) {
            mStartTimeTv.setError("尚未设置报名截止时间");
            T.showShort(mContext, "尚未设置报名截止时间");
            return;
        }
        mStartTimeTv.setError(null);
        //检查是否设置了活动结束时间
        if (mEndTimeDate == null) {
            mEndTimeTv.setError("尚未设置报名截止时间");
            T.showShort(mContext, "尚未设置报名截止时间");
            return;
        }
        mEndTimeTv.setError(null);
        //检查具体位置信息
        mLocation = mLocationEt.getText().toString();
        error = StringUtils.checkPartyLocationError(mContext, mLocation);
        if (!error.equals(AppEnum.DEFAULT_CHECK_ERROR)) {
            mLocationEt.setError(error);
            T.showShort(mContext, error);
            return;
        }
        mLocationEt.setError(null);
        //检查描述
        mDescription = mDescriptionEt.getText().toString();
        error = StringUtils.checkPartyDescriptionError(mContext, mDescription);
        if (!error.equals(AppEnum.DEFAULT_CHECK_ERROR)) {
            mDescriptionEt.setError(error);
            T.showShort(mContext, error);
            return;
        }
        mDescriptionEt.setError(null);

        //标签列表
        if (mSelectTagList.size() > 0) {
            for (int i = 0; i < mSelectTagList.size(); i++) {
                mLabelIdList += mSelectTagList.get(i).id + ",";
            }
            mLabelIdList = mLabelIdList.substring(0, mLabelIdList.length() - 1);
        }

        postCreatePartyRequest();
    }

    /**
     * 发送创建活动的请求
     */
    private void postCreatePartyRequest() {
        mDialogBuilder.showProgressDialog(mContext, "正在请求创建活动.", false);
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_CREATE_PARTY;
        int groupid = mGroupInfo.getRungroup().getId();
        RequestParams params = RequestParamsBuild.buildCreatePartyRequest(mContext,groupid, mPartyName, mDescription,
                mRegistTIme, mStartTime, mEndTime, mLocation, province, city, mLabelIdList, mPhotoUrl);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                T.showShort(mContext, errorMsg);

            }

            @Override
            protected void onRightResponse(String Response) {
                T.showShort(mContext, "发布活动成功!");
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

    /**
     * 设置活动开始时间
     */
    private void changeStartTime() {
        //获取当前年份
        Calendar calendar = Calendar.getInstance();
        int curYear = calendar.get(Calendar.YEAR);

        //设置时间选择器的默认参数
        mStartTimePick.setTime(null);
        //设置选择器是否可以循环滚动
        mStartTimePick.setCyclic(false);
        mStartTimePick.setCancelable(true);
        //设置选择时间回调
        mStartTimePick.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                if (date.before(new Date(System.currentTimeMillis()))) {
                    T.showShort(mContext, "设置失败: 开始时间不能早于现在");
                    return;
                }
                //若活动开始时间在活动结束时间之后
                if (mEndTimeDate != null && mEndTimeDate.before(date)) {
                    T.showShort(mContext, "设置失败: 开始时间不能晚于结束时间");
                    return;
                }
                mStartTimeDate = date;
                mStartTime = TimeUtils.dateToPartyTimeStr(mStartTimeDate);
                mStartTimeTv.setText(mStartTime);

                if (mEndTimeDate == null) {
                    mEndTime = TimeUtils.getPartyAfrerTime(mStartTime, 2 * 3600);
                    mEndTimeDate = TimeUtils.partyStringToDate(mEndTime);
                    mEndTimeTv.setText(mEndTime);
                }
                if (mRegistTimeDate == null) {
                    mRegistTIme = TimeUtils.getPartyAfrerTime(mStartTime, 1800);
                    mRegistTimeDate = TimeUtils.partyStringToDate(mRegistTIme);
                    mRegistTimeTv.setText(mRegistTIme);
                }
            }
        });
        mStartTimePick.show();
    }

    /**
     * 设置报名截止时间
     */
    private void changeRegistTime() {
        //获取当前年份
        Calendar calendar = Calendar.getInstance();
        int curYear = calendar.get(Calendar.YEAR);

        //设置时间选择器的默认参数
        mRegistTimePick.setTime(null);
        //设置选择器是否可以循环滚动
        mRegistTimePick.setCyclic(false);
        mRegistTimePick.setCancelable(true);
        //设置选择时间回调
        mRegistTimePick.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                if (date.before(new Date(System.currentTimeMillis()))) {
                    T.showShort(mContext, "设置失败: 报名截止时间不能早于现在");
                    return;
                }
                //若报名截止时间晚于结束时间
                if (mEndTimeDate != null && mEndTimeDate.before(date)) {
                    T.showShort(mContext, "设置失败: 报名截止时间不能晚于结束时间");
                    return;
                }
                mRegistTimeDate = date;
                mRegistTIme = TimeUtils.dateToPartyTimeStr(mRegistTimeDate);
                mRegistTimeTv.setText(mRegistTIme);
            }
        });
        mRegistTimePick.show();
    }

    /**
     * 设置结束时间
     */
    private void changeEndTime() {
        //获取当前年份
        Calendar calendar = Calendar.getInstance();
        int curYear = calendar.get(Calendar.YEAR);

        //设置时间选择器的默认参数
        mEndTimePick.setTime(null);
        //设置选择器是否可以循环滚动
        mEndTimePick.setCyclic(false);
        mEndTimePick.setCancelable(true);
        //设置选择时间回调
        mEndTimePick.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                if (date.before(new Date(System.currentTimeMillis()))) {
                    T.showShort(mContext, "设置失败: 结束时间不能早于现在");
                    return;
                }
                //若活动开始时间在活动结束时间之后
                if (mStartTimeDate != null && mStartTimeDate.after(date)) {
                    T.showShort(mContext, "设置失败: 开始时间不能晚于结束时间");
                    return;
                }
                //若报名截止时间晚于结束时间
                if (mRegistTimeDate != null && mRegistTimeDate.after(date)) {
                    T.showShort(mContext, "设置失败: 报名截止时间不能晚于结束时间");
                    return;
                }
                mEndTimeDate = date;
                mEndTime = TimeUtils.dateToPartyTimeStr(mEndTimeDate);
                mEndTimeTv.setText(mEndTime);
            }
        });
        mEndTimePick.show();
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
        mLocationPick.setCancelable(true);
        //设置选择器标题
        mLocationPick.setTitle("选择地址");
        //设置选择器默认值
        mLocationPick.setSelectOptions(options1Pos, options2Pos);
        //选择器回调
        mLocationPick.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                province = options1Items.get(options1).getName();
                city = options2Items.get(options1).get(option2);
                mCityTv.setText(province + " " + city);
            }
        });
        mLocationPick.show();
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
     * 更换照片
     */
    private void addPhoto(int Request) {
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
     * 上传头像到服务器
     */
    private void postAvatar(File mAvatarF) {
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_UPLOAD_AVATAR;
        RequestParams params = RequestParamsBuild.buildUploadAvatarRequest(mContext,mAvatarF);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                T.showShort(mContext, errorMsg);
            }

            @Override
            protected void onRightResponse(String Response) {
                GetUploadAvatarResult resultEntity = JSON.parseObject(Response, GetUploadAvatarResult.class);
                mPhotoUrl = resultEntity.getUrl();
                bitmapUtils.display(mPartyPicture, mPhotoUrl);
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
