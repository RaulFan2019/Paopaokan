package com.app.pao.activity.main;

import android.os.Bundle;
import android.view.View;

import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.activity.friend.UserInfoActivity;
import com.app.pao.entity.network.GetParseInviteTextResult;
import com.app.pao.ui.dialog.v1.MyDialogBuilderV1;
import com.lidroid.xutils.view.annotation.ContentView;

/**
 * Created by Raul.Fan on 2016/3/8.
 * 复制剪贴板用户相关弹框
 */
@ContentView(R.layout.activity_clip_dialog)
public class ClipUserDialogActivity extends BaseAppCompActivity {

    /* local data */
    private GetParseInviteTextResult result;

    @Override
    protected void initData() {
        result = (GetParseInviteTextResult) getIntent().getExtras().getSerializable("data");
    }

    @Override
    protected void initViews() {
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        result = (GetParseInviteTextResult) getIntent().getExtras().getSerializable("data");
    }

    @Override
    protected void doMyOnCreate() {
        mDialogBuilder.showClipDialog(mContext, "发现好友邀请涵", result.getName(),
                result.getAvatar(), result.getComment(), "忽略", "查看");
        mDialogBuilder.setListener(new MyDialogBuilderV1.ClipDialogListener() {
            @Override
            public void onLeftBtnClick() {
                finish();
            }

            @Override
            public void onRightBtnClick() {
                Bundle bundle = new Bundle();
                bundle.putInt("userid", result.getData());
                startActivity(UserInfoActivity.class,bundle);
                finish();
            }

            @Override
            public void onCancel() {
                finish();
            }
        });
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

}
