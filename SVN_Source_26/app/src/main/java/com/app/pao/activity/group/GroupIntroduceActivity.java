package com.app.pao.activity.group;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.activity.main.ImageCutActivity;
import com.app.pao.activity.settings.MultiImageSelectorActivity;
import com.app.pao.config.AppConfig;
import com.app.pao.config.AppEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.entity.network.GetGroupDetailInfoRequest;
import com.app.pao.entity.network.GetGroupInfoResult;
import com.app.pao.entity.network.GetUploadAvatarResult;
import com.app.pao.network.api.MyRequestCallBack;
import com.app.pao.network.utils.RequestParamsBuild;
import com.app.pao.ui.dialog.v1.MyDialogBuilderV1;
import com.app.pao.ui.widget.LoadingView;
import com.app.pao.ui.widget.tagview.OnTagClickListener;
import com.app.pao.ui.widget.tagview.OnTagDeleteListener;
import com.app.pao.ui.widget.tagview.Tag;
import com.app.pao.ui.widget.tagview.TagView;
import com.app.pao.utils.DeviceUtils;
import com.app.pao.utils.FileUtils;
import com.app.pao.utils.ImageUtils;
import com.app.pao.utils.Log;
import com.app.pao.utils.T;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/25.
 * 跑团介绍 new
 * Changed by LeiYang on 2016/3/29
 */
@ContentView(R.layout.activity_group_introduce)
public class GroupIntroduceActivity extends BaseAppCompActivity implements View.OnClickListener {
    private static final int MSG_POST_ERROR = 0;
    private static final int MSG_POST_OK = 1;

    private static final int REQUEST_PHOTO = 1;
    private static final int REQUEST_CUT_PHOTO = 2;
    private static final int REQUEST_CUT_PHOTO_FOR_M = 3;

    @ViewInject(R.id.iv_group_intro_photo)
    private ImageView mGroupIntroPhotoIv;
    @ViewInject(R.id.tv_group_intro_name)
    private TextView mGroupIntroNameTv;
    @ViewInject(R.id.tv_group_intro_location)
    private TextView mGroupIntroLocationTv;
    @ViewInject(R.id.tv_group_intro_content)
    private TextView mGroupIntroContentTv;
    @ViewInject(R.id.tv_group_tag_title)
    private TextView mGroupTagTitleTv;
    @ViewInject(R.id.tagv_group_tag)
    private TagView mGroupTagTv;
    @ViewInject(R.id.tv_member_tag_title)
    private TextView mMemberTagTitleTv;
    @ViewInject(R.id.tagv_member_tag)
    private TagView mMemberTagTv;
    @ViewInject(R.id.iv_is_edit_group_photo)
    private ImageView mIsEditGroupPhotoIv;

    @ViewInject(R.id.rl_load)
    private LinearLayout mLoadViewRl;//加载View
    @ViewInject(R.id.loadview)
    private LoadingView mLoadV;
    @ViewInject(R.id.tv_reload)
    private com.rey.material.widget.TextView mReladV;

    private GetGroupInfoResult mGroupInfo;
    private int mGroupId;
    private boolean isFirstIn;
    private BitmapUtils bitmapUtils;
    private File mGroupAvatarF;


    private Handler mPostHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_POST_ERROR) {
                T.showShort(mContext, msg.obj.toString());
                if(mLoadViewRl.getVisibility() == View.VISIBLE) {
                    mLoadV.setLoadingText("加载失败");
                    mReladV.setVisibility(View.VISIBLE);
                }
                mPostHandler.removeCallbacks(mPostRunnable);
            } else if (msg.what == MSG_POST_OK) {
                showAllViews();
                mPostHandler.removeCallbacks(mPostRunnable);
                mLoadV.setVisibility(View.GONE);
                mLoadViewRl.setVisibility(View.GONE);
            }

            if(mDialogBuilder.progressDialog != null && mDialogBuilder.progressDialog.isShowing()){
                mDialogBuilder.progressDialog.dismiss();
            }
        }
    };

    Runnable mPostRunnable = new Runnable() {
        @Override
        public void run() {
            mDialogBuilder.showProgressDialog(mContext, "正在加载", true);
        }
    };

    @Override
    @OnClick({R.id.title_bar_left_menu,R.id.rl_group_photo,R.id.tv_group_intro_content,R.id.tv_reload})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_bar_left_menu:
                finish();
                break;
            case R.id.rl_group_photo:
                if(mGroupInfo.getRungroup().getRole() ==  AppEnum.groupRole.MANAGER ||mGroupInfo.getRungroup().getRole() ==  AppEnum.groupRole.OWNER){
                    changePhoto(REQUEST_PHOTO);
                }
                break;
            case R.id.tv_group_intro_content:
                if(mGroupInfo.getRungroup().getRole() ==  AppEnum.groupRole.MANAGER ||mGroupInfo.getRungroup().getRole() ==  AppEnum.groupRole.OWNER){
                    Bundle b = new Bundle();
                    b.putInt("groupId",mGroupId);
                    b.putString("groupDescription",mGroupInfo.getRungroup().getDescription());
                    startActivity(EditGroupIntroduceActivity.class, b);
                }
                break;
            case R.id.tv_reload:
                getRunGroupInfo();
                mLoadV.setLoadingText("加载中...");
                mReladV.setVisibility(View.GONE);
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

    @Override
    protected void initData() {
        mGroupId = getIntent().getIntExtra("groupId", 0);
        bitmapUtils = new BitmapUtils(mContext);
        isFirstIn = true;
    }

    @Override
    protected void initViews() {
        mGroupTagTv.setLineMargin(5);
        mGroupTagTv.setOnTagClickListener(new OnTagClickListener() {
            @Override
            public void onTagClick(Tag tag, int position) {
                List<GetGroupInfoResult.RungroupEntity.Label> labelList = mGroupInfo.getRungroup().getLabel();
                if (position == labelList.size()) {
                    launchGreateTag();
                } else {
                    launchSingleTag(labelList.get(position).getId());
                }
            }
        });

        mGroupTagTv.setOnTagDeleteListener(new OnTagDeleteListener() {
            @Override
            public void onTagDeleted(Tag tag, int position) {
                showDeleteTagDialog(position);
            }
        });
        mMemberTagTv.setLineMargin(5);
        mMemberTagTv.setOnTagClickListener(new OnTagClickListener() {
            @Override
            public void onTagClick(Tag tag, int position) {
                //若是选中状态
                if (tag.mTempValue == 1) {
                    removeMemberTag(position, tag);
                    //非选中
                } else {
                    addMemberOneTag(position, tag);
                }


            }
        });
    }

    @Override
    protected void doMyOnCreate() {

    }

    @Override
    protected void updateData() {

    }

    @Override
    protected void updateViews() {
        getRunGroupInfo();
        if(isFirstIn){
            isFirstIn = false;
        }else{
            mPostHandler.postDelayed(mPostRunnable,500);
        }
    }

    @Override
    protected void destroy() {
        cancelHandler();
    }

    private void cancelHandler() {
        if (mPostHandler != null) {
            mPostHandler.removeMessages(MSG_POST_ERROR);
            mPostHandler.removeMessages(MSG_POST_OK);
            mPostHandler.removeCallbacks(mPostRunnable);
        }
    }

    /**
     * 初始化所有的显示
     */
    private void showAllViews() {
        GetGroupInfoResult.RungroupEntity group = mGroupInfo.getRungroup();

        if(group.getRole() ==  AppEnum.groupRole.MANAGER ||group.getRole() ==  AppEnum.groupRole.OWNER){
            mIsEditGroupPhotoIv.setVisibility(View.VISIBLE);
            mGroupIntroPhotoIv.setEnabled(true);
            mGroupIntroContentTv.setEnabled(true);
            mGroupTagTv.setEnabled(true);
            Drawable img = getResources().getDrawable(R.drawable.icon_edit);
            img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
            mGroupIntroContentTv.setCompoundDrawables(null, null, img, null);
            mGroupIntroContentTv.setCompoundDrawablePadding((int) DeviceUtils.dpToPixel(5));
            mGroupIntroContentTv.setText(group.getDescription());
        }else {
            mIsEditGroupPhotoIv.setVisibility(View.GONE);
            mGroupIntroPhotoIv.setEnabled(false);
            mGroupIntroContentTv.setEnabled(false);
            mGroupTagTv.setEnabled(false);
            mGroupIntroContentTv.setText(group.getDescription());
            mGroupIntroContentTv.setCompoundDrawables(null, null, null, null);
        }

        mGroupIntroNameTv.setText(group.getName());
        mGroupIntroLocationTv.setText(group.getLocationprovince() + " " + group.getLocationcity());

        ImageUtils.loadGroupImage(group.getAvatar(), mGroupIntroPhotoIv);

        showGroupTag();
        if(mGroupInfo.getRungroup().getRole() < AppEnum.groupRole.APPLY && mGroupInfo.getRungroup().getRole() !=
                AppEnum.groupRole.NOT_MEMBER){
            showMemberTag();
        }else {
            mMemberTagTitleTv.setVisibility(View.GONE);
            mMemberTagTv.setVisibility(View.GONE);
        }

    }

    /**
     * 显示跑团所有TAG
     */
    private void showGroupTag() {
        List<GetGroupInfoResult.RungroupEntity.Label> labelList = mGroupInfo.getRungroup().getLabel();
        List<Tag> tagList = new ArrayList<>();
        if(labelList == null ||labelList.size() == 0) {
            if (mGroupInfo.getRungroup().getRole() == AppEnum.groupRole.OWNER || mGroupInfo.getRungroup().getRole() == AppEnum.groupRole.MANAGER){
                mGroupTagTitleTv.setVisibility(View.VISIBLE);
                mGroupTagTv.setVisibility(View.VISIBLE);
                Tag endTag = new Tag(" + ");
                endTag.layoutColor = Color.TRANSPARENT;
                endTag.tagTextColor = Color.parseColor("#888888");
                endTag.tagTextSize = 12;
                endTag.radius = 2;
                endTag.layoutBorderColor = Color.parseColor("#888888");
                endTag.layoutBorderSize = 1;
                tagList.add(endTag);
                mGroupTagTv.addTags(tagList);
            }else {
                mGroupTagTitleTv.setVisibility(View.GONE);
                mGroupTagTv.setVisibility(View.GONE);
            }
            return;
        }

        if(mGroupInfo.getRungroup().getRole() == AppEnum.groupRole.OWNER || mGroupInfo.getRungroup().getRole() == AppEnum.groupRole.MANAGER){
            for (int i = 0; i < labelList.size(); i++) {
                //用html来控制textView显示不同颜色
                Tag tag = new Tag( labelList.get(i).getName(),labelList.get(i).getMembercount()+"人");
                tag.layoutColor = Color.TRANSPARENT;
                tag.tagTextColor = Color.parseColor("#F06522");
                tag.tagPlusTextColor = Color.parseColor("#888888");
                tag.deleteIndicatorColor = Color.parseColor("#F06522");
                tag.layoutColorPress=Color.parseColor("#22888888");
                tag.isDeletable = true;
                tag.tagTextSize = 12;
                tag.tagPlusTextSize=10;
                tag.radius = 2;
                tag.layoutBorderColor = Color.parseColor("#F06522");
                tag.layoutBorderSize = 1;
                tagList.add(tag);
            }
            Tag endTag = new Tag(" + ");
            endTag.layoutColor = Color.TRANSPARENT;
            endTag.tagTextColor = Color.parseColor("#888888");
            endTag.tagTextSize = 12;
            endTag.radius = 2;
            endTag.layoutBorderColor = Color.parseColor("#888888");
            endTag.layoutBorderSize = 1;
            tagList.add(endTag);
        }else {
            for (int i = 0; i < labelList.size(); i++) {
                Tag tag = new Tag(labelList.get(i).getName());
                tag.layoutColor = Color.TRANSPARENT;
                tag.layoutColorPress=Color.TRANSPARENT;
                tag.tagTextColor = Color.parseColor("#F06522");
                tag.deleteIndicatorColor = Color.parseColor("#F06522");
                tag.tagTextSize = 12;
                tag.radius = 2;
                tag.layoutBorderColor = Color.parseColor("#F06522");
                tag.layoutBorderSize = 1;
                tagList.add(tag);
            }
        }

        mGroupTagTv.addTags(tagList);
    }

    /**
     * 显示成员的TAG
     */
    private void showMemberTag(){
        List<GetGroupInfoResult.RungroupEntity.Label> labelList = mGroupInfo.getRungroup().getLabel();
        if(labelList == null ||labelList.size() == 0){
            mMemberTagTitleTv.setVisibility(View.GONE);
            mMemberTagTv.setVisibility(View.GONE);
            return;
        }else {
            mMemberTagTitleTv.setVisibility(View.VISIBLE);
            mMemberTagTv.setVisibility(View.VISIBLE);
        }
        List<Tag> tagList = new ArrayList<>();
        for (int i = 0; i < labelList.size(); i++) {
            if(labelList.get(i).getHasLabel() ==  1){
                Tag tag = new Tag( labelList.get(i).getName());
                tag.layoutColor = Color.TRANSPARENT;
                tag.tagTextColor = Color.parseColor("#F06522");
                tag.deleteIndicatorColor = Color.parseColor("#F06522");
                tag.tagTextSize = 12;
                tag.radius = 2;
                tag.isShowImg = true;
                tag.imgDrawableRes = R.drawable.icon_tag_select;
                tag.layoutBorderColor = Color.parseColor("#F06522");
                tag.layoutBorderSize = 1;
                tag.mTempValue = 1;
                tagList.add(tag);
            }else {
                Tag tag = new Tag( labelList.get(i).getName());
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
                tagList.add(tag);
            }
        }
            mMemberTagTv.addTags(tagList);
    }

    /**
     * 创建标签
     */
    private void launchGreateTag(){
        Bundle b = new Bundle();
        b.putInt("groupId",mGroupId);
        startActivity(GroupCreateTagActivity.class, b);
    }

    /**
     * 进入单个标签页面
     *
     * @param labelId
     */
    private void launchSingleTag(int labelId){
        Bundle b = new Bundle();
        b.putInt("labelId",labelId);
        startActivity(GroupSingleTagActivity.class,b);
    }

    /**
     * 提示是否删除标签
     *
     * @param position
     */
    private void showDeleteTagDialog(final int position){
        mDialogBuilder.showChoiceTwoBtnDialog(mContext, "提示", "确认删除标签 <" + mGroupInfo.getRungroup().getLabel().get(position).getName() + "> ?", "取消", "确定");
        mDialogBuilder.setListener(new MyDialogBuilderV1.ChoiceTwoBtnDialogListener() {
            @Override
            public void onLeftBtnClick() {

            }

            @Override
            public void onRightBtnClick() {
                removeLabel(position);
            }

            @Override
            public void onCancel() {
            }
        });
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
     * 获取跑团详情
     */
    private void getRunGroupInfo() {
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_GET_GROUP_INFO;
        RequestParams params = RequestParamsBuild.buildGetGroupInfoRequest(mContext,mGroupId);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                T.showShort(mContext, errorMsg);
                if (mPostHandler != null) {
                    Message msg = new Message();
                    msg.what = MSG_POST_ERROR;
                    msg.obj = errorMsg;
                    mPostHandler.sendMessage(msg);
                }
            }

            @Override
            protected void onRightResponse(String Response) {
                mGroupInfo = JSON.parseObject(Response, GetGroupInfoResult.class);
                //若是好友
                if (mPostHandler != null) {
                    mPostHandler.sendEmptyMessage(MSG_POST_OK);
                }
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                T.showShort(mContext, s);
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
     * 删除标签
     */
    private void removeLabel(final int position) {
        mDialogBuilder.showProgressDialog(mContext, "正在删除..", false);

        HttpUtils http = new HttpUtils();
        final String POST_URL = URLConfig.URL_REMOVE_LABEL;
        RequestParams params = RequestParamsBuild.buildRemoveGroupLabel(mContext,mGroupInfo.getRungroup().getLabel().get(position).getId());
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {
            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                T.showShort(mContext,errorMsg);
            }

            @Override
            protected void onRightResponse(String Response) {
                //刷新团标签
                mGroupTagTv.remove(position);
                //刷新个人标签
                mMemberTagTv.remove(position);
                //刷新数据
                mGroupInfo.getRungroup().getLabel().remove(position);
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                T.showShort(mContext,s);
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
     * 添加团员的一个标签
     *
     * @param position
     */
    private void addMemberOneTag(final int position,final Tag tag){
        mDialogBuilder.showProgressDialog(mContext,"正在保存...",false);

        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_ADD_PERSON_LABEL_LIST;
        int mUserId = LocalApplication.getInstance().getLoginUser(mContext).getUserId();
        Log.i("TAG ","LABEL ID:"+mGroupInfo.getRungroup().getLabel().get(position).getId());
        RequestParams params = RequestParamsBuild.buildAddMemberOneLabel(mContext,mUserId, mGroupInfo.getRungroup().getLabel().get(position).getId());
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {
            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                T.showShort(mContext,errorMsg);
            }

            @Override
            protected void onRightResponse(String Response) {
                tag.imgDrawableRes = R.drawable.icon_tag_select;
                tag.tagTextColor = Color.parseColor("#F06522");
                tag.deleteIndicatorColor = Color.parseColor("#F06522");
                tag.layoutBorderColor = Color.parseColor("#F06522");
                tag.mTempValue = 1;
                mMemberTagTv.refreshOneTag(position, tag);
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                T.showShort(mContext,s);
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
     * 移除团员一个标签
     *
     * @param position
     */
    private void removeMemberTag(final int position,final Tag tag){
        mDialogBuilder.showProgressDialog(mContext,"正在保存...",false);

        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_REMOVE_PERSON_LABEL_LIST;
        int mUserId = LocalApplication.getInstance().getLoginUser(mContext).getUserId();
        RequestParams params = RequestParamsBuild.buildRemoveMemberOneLabel(mContext,mUserId, mGroupInfo.getRungroup().getLabel().get(position).getId());
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {
            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                T.showShort(mContext,errorMsg);
            }

            @Override
            protected void onRightResponse(String Response) {
                tag.imgDrawableRes = R.drawable.icon_tag_not_select;
                tag.tagTextColor = Color.parseColor("#888888");
                tag.deleteIndicatorColor = Color.parseColor("#888888");
                tag.layoutBorderColor = Color.parseColor("#888888");
                tag.mTempValue = 0;
                mMemberTagTv.refreshOneTag(position,tag);
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                T.showShort(mContext,s);
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
     * 上传头像到服务器
     */
    private void postAvatar(final File mAvatarF) {
        mDialogBuilder.showProgressDialog(mContext, "正在上传照片..", false);
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
                String avatar = resultEntity.getUrl();
                postGroupUserAvatar(avatar, mAvatarF);

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
     * 修改跑团头像信息
     */
    private void postGroupUserAvatar(final String avatar,final File mAvatarF) {
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_UPDATE_GROUP_INFO;
        RequestParams params = RequestParamsBuild.buildResetGroupAvatarRequest(mContext,mGroupInfo.getRungroup().getId()
                , avatar);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                T.showShort(mContext, errorMsg);
            }

            @Override
            protected void onRightResponse(String Response) {
//                ImageUtils.loadGroupImage(mBitmapUtils, avatar, mAvatarIv);
                bitmapUtils.display(mGroupIntroPhotoIv, String.valueOf(Uri.fromFile(mAvatarF)));
                T.showShort(mContext, "上传成功");
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
