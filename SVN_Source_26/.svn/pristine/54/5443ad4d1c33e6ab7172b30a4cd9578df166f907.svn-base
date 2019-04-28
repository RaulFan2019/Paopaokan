package com.app.pao.activity.group;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.config.AppEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.network.api.MyRequestCallBack;
import com.app.pao.network.utils.RequestParamsBuild;
import com.app.pao.utils.T;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.rey.material.widget.Button;
import com.rey.material.widget.ProgressView;
import com.rey.material.widget.TextView;

/**
 * Created by Administrator on 2016/1/14.
 * <p/>
 * 编辑定位签到范围
 */
@ContentView(R.layout.activity_group_party_edit_gps)
public class GroupPartyEditGpsActivity extends BaseAppCompActivity implements View.OnClickListener, AMapLocationListener {

    /* local data*/
    private AMap mAMap;//地图

    @ViewInject(R.id.ll_gps_state)
    private LinearLayout mGpsStateLl;
    @ViewInject(R.id.tv_gps_search)
    private TextView mGpsStateTv;
    @ViewInject(R.id.ll_gps_range)
    private LinearLayout mGpsRangeLl;
    @ViewInject(R.id.et_gps_range)
    private EditText mGpsRangeEt;
    @ViewInject(R.id.progress_gps_search)
    private ProgressView mGpsStatePv;
    @ViewInject(R.id.mv_sign_range)
    private MapView mapView;
    @ViewInject(R.id.btn_confirm_sign)
    private Button mSignConfirmBtn;

    private AMapLocation mAMapLocation;//定位成功的位置
    private double mLatitude;//纬度
    private double mLongitude;//经度
    private Marker mLocationMk;//位置坐标\
    private Circle mSignRangeCl;//地图签到范围标注

    private int mRange = 100;//地图圆圈半径
    private int mPartyId;//活动Id

    @Override
    @OnClick({R.id.title_bar_left_menu, R.id.btn_confirm_sign})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_bar_left_menu:
                finish();
                break;
            case R.id.btn_confirm_sign:
                setCheckinPosition();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapView.onCreate(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {
        if (mAMap == null) {
            mAMap = mapView.getMap();
        }
        mGpsStatePv.start();

        mGpsRangeEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String mGpsRange = mGpsRangeEt.getText().toString();
                if (!mGpsRange.isEmpty()) {
                    mRange = Integer.parseInt(mGpsRange);
                    drawMapByNewLocation();
                }
            }
        });
    }

    @Override
    protected void doMyOnCreate() {
        //TODO
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

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {

        float accuracy = aMapLocation.getAccuracy();// 精度
        if (accuracy < AppEnum.Gps.ACCURACY_POWER) {
            mGpsStateTv.setText(R.string.Tv_GroupPartyEditGps_Gps_Ok);
            mGpsStatePv.stop();
            mGpsStateLl.setVisibility(View.GONE);
            mGpsRangeLl.setVisibility(View.VISIBLE);
            mSignConfirmBtn.setVisibility(View.VISIBLE);
            mAMapLocation = aMapLocation;
            mLatitude = aMapLocation.getLatitude();
            mLongitude = aMapLocation.getLongitude();
            drawMapByNewLocation();
        }
    }

    /**
     * 通过点 半径 画出圆
     */
    public void drawMapByNewLocation() {
        LatLng mCurrentLatLng = new LatLng(mLatitude, mLongitude);

        //根据设置范围大小决定地图大小
        int mapLevel = 10;
        if (mRange <= 100) {
            mapLevel = 17;
        } else if (mRange <= 300) {
            mapLevel = 16;
        } else if (mRange <= 500) {
            mapLevel = 15;
        } else if (mRange <= 1000) {
            mapLevel = 14;
        } else if (mRange <= 1500) {
            mapLevel = 13;
        } else if (mRange > 1500) {
            mapLevel = 12;
        }

        mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mCurrentLatLng, mapLevel));

        if (mLocationMk != null) {
            mLocationMk.remove();
            mLocationMk.destroy();
        }
        if (mSignRangeCl != null) {
            mSignRangeCl.remove();
        }

        mLocationMk = mAMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_fm_new_flag)).title("当前位置").position(mCurrentLatLng).anchor((float) 0, (float) 1));
        mSignRangeCl = mAMap.addCircle(new CircleOptions().center(mCurrentLatLng).radius(mRange).fillColor(Color.argb(100, 240, 101, 34)).strokeColor(Color.argb(0, 0, 0, 0)).strokeWidth(0));
    }

    /**
     * 保存签到位置
     */
    private void setCheckinPosition() {
        mDialogBuilder.showProgressDialog(mContext, "正在保存...", false);

        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_SET_CHECK_IN_POSITION;
        RequestParams params = RequestParamsBuild.buildSetCheckInPosition(mContext,mPartyId, mLatitude, mLongitude);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {
            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                T.showShort(mContext, errorMsg);
            }

            @Override
            protected void onRightResponse(String Response) {
                T.showShort(mContext, "标签位置保存成功。");
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
}
