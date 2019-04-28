package com.app.pao.activity.group;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.View;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
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
 * Created by Administrator on 2016/1/18.
 */
@ContentView(R.layout.activity_group_party_gps_sign_in)
public class GroupPartyGpsSignInActivity extends BaseAppCompActivity implements View.OnClickListener,AMapLocationListener {

    private AMap mAMap;//地图
    @ViewInject(R.id.tv_gps_search)
    private TextView mGpsStateTv;
    @ViewInject(R.id.tv_sign_in_tip)
    private TextView mSignInTip;
    @ViewInject(R.id.progress_gps_search)
    private ProgressView mGpsStatePv;
    @ViewInject(R.id.mv_sign_range)
    private MapView mapView;
    @ViewInject(R.id.btn_confirm_sign)
    private Button mSignInBtn;


    private double mLatitude;//纬度
    private double mLongitude;//经度
    private Marker mLocationMk;//位置坐标\
    private Circle mSignRangeCl;//地图签到范围标注
    private Marker mUserLocationMK;//用户位置

    private int mRange = 100;//地图圆圈半径
    private int mPartyId;

    @Override
    @OnClick({R.id.title_bar_left_menu,R.id.btn_confirm_sign})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_bar_left_menu:
                finish();
                break;
            case R.id.btn_confirm_sign:
                postCheckInParty();
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
        mLatitude = getIntent().getDoubleExtra("latitude",0);
        mLongitude = getIntent().getDoubleExtra("longitude",0);
        mRange = getIntent().getIntExtra("distance", 0);
        mPartyId = getIntent().getIntExtra("partyId",0);
    }

    @Override
    protected void initViews() {
        if (mAMap == null) {
            mAMap = mapView.getMap();
        }
        mGpsStatePv.start();
        drawMapByNewLocation();
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
            mGpsStatePv.stop();
            mGpsStatePv.setVisibility(View.GONE);
            mGpsStateTv.setText("定位成功！");

            if (mUserLocationMK != null) {
                mUserLocationMK.remove();
                mUserLocationMK.destroy();
            }
            //画出当前所处坐标位置
            LatLng mCurrentLatLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
            mUserLocationMK = mAMap.addMarker((new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_run_end)).position(mCurrentLatLng).anchor((float)0.5,(float)0.5)));
            mAMap.moveCamera(CameraUpdateFactory.changeLatLng(mCurrentLatLng));

            //判断是否在签到范围内
            LatLng mPartyLatLng = new LatLng(mLatitude, mLongitude);
            float distance = AMapUtils.calculateLineDistance(mPartyLatLng,mCurrentLatLng);
            if(distance <= mRange){
                mSignInTip.setText("签到范围内可以签到。");
                mSignInBtn.setVisibility(View.VISIBLE);
            }else{
                mSignInTip.setText("不在签到范围内，请进入范围后才可以签到。");
                mSignInBtn.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 通过点 半径 画出圆
     */
    public void drawMapByNewLocation() {
        LatLng mPartyLatLng = new LatLng(mLatitude, mLongitude);

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

        mAMap.moveCamera(CameraUpdateFactory.zoomTo(mapLevel));

        if (mLocationMk != null) {
            mLocationMk.remove();
            mLocationMk.destroy();
        }
        if (mSignRangeCl != null) {
            mSignRangeCl.remove();
        }

        mLocationMk = mAMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_fm_flag)).title("活动定位").position(mPartyLatLng).anchor((float) 0, (float) 1));
        mSignRangeCl = mAMap.addCircle(new CircleOptions().center(mPartyLatLng).radius(mRange).fillColor(Color.argb(100, 240, 101, 34)).strokeColor(Color.argb(0, 0, 0, 0)).strokeWidth(0));
    }

    /**
     * 正在发送签到
     */
    private void postCheckInParty() {
        mDialogBuilder.showProgressDialog(mContext, "正在签到活动", false);
        HttpUtils https = new HttpUtils();
        String POST_URL = URLConfig.URL_PARTY_CHECK_IN;
        RequestParams params = RequestParamsBuild.BuildCheckInParams(mContext,mPartyId);
        https.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                T.showShort(mContext, errorMsg);
            }

            @Override
            protected void onRightResponse(String Response) {
                T.showShort(mContext, "活动已签到");
                setResult(RESULT_OK);
                finish();
            }


            @Override
            protected void onFailureResponse(HttpException e, String s) {
                T.showShort(mContext,s);
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
