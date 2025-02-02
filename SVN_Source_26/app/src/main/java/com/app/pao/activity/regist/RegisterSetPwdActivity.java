package com.app.pao.activity.regist;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.app.pao.ActivityStackManager;
import com.app.pao.R;
import com.app.pao.activity.BaseActivityV3;
import com.app.pao.config.AppEnum;
import com.app.pao.utils.StringUtils;
import com.app.pao.utils.T;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Raul.Fan on 2016/9/6.
 */
public class RegisterSetPwdActivity extends BaseActivityV3 {


    /* local view */
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_login_resetpwd_title)
    TextView mTitleTv;
    @BindView(R.id.et_login_reset_psw)
    EditText mPwdEt;
    @BindView(R.id.btn_reset_password_show)
    ImageButton mShowPwdBtn;

    /* local data */
    private boolean mIsShowPsw = false;//是否显示密码
    private String mPhoneNum;//手机号码
    private String mPassword;//密码

    @Override
    protected int getLayoutId() {
        return R.layout.activity_regist_set_pwd;
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

    @OnClick({R.id.btn_reset_password_show, R.id.btn_login_resetpwd_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            //点击切换密码可见状态按钮
            case R.id.btn_reset_password_show:
                onShowPWDBtnClick();
                break;
            case R.id.btn_login_resetpwd_commit:
                onCommitPwdBtnClick();
                break;
        }
    }

    @Override
    protected void initData() {
        mCheckNewData = false;
        mPhoneNum = getIntent().getExtras().getString("phonenum");

    }

    @Override
    protected void initViews() {
        setSupportActionBar(mToolbar);
        mTitleTv.setText("请设置密码，之后你可以使用\n手机号" + mPhoneNum + " + 密码登录");
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
     * 点击显示密码的按钮
     */
    private void onShowPWDBtnClick(){
        if (mIsShowPsw) {
            // 隐藏密码
            mPwdEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            mShowPwdBtn.setImageResource(R.drawable.icon_showpwd);
        } else {
            //显示密码
            mPwdEt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            mShowPwdBtn.setImageResource(R.drawable.icon_hidepwd);
        }
        mIsShowPsw = !mIsShowPsw;
        //将光标置于最后位置
        Editable ea = mPwdEt.getText();
        mPwdEt.setSelection(ea.length());
    }


    /**
     * 点击提交密码
     */
    private void onCommitPwdBtnClick(){
        //判断密码
        mPassword = mPwdEt.getText().toString();
        String error = StringUtils.checkPasswordInputError(RegisterSetPwdActivity.this, mPassword);
        if (!error.equals(AppEnum.DEFAULT_CHECK_ERROR)) {
            T.showShort(this, error);
            mPwdEt.setError(error);
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString("phonenum", mPhoneNum);
        bundle.putString("password", mPassword);
        bundle.putInt("type", AppEnum.RegistType.PhoneNumRegist);
        startActivity(RegisterEndActivity.class, bundle);
    }

}
