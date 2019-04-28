package com.app.pao.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.app.pao.R;
import com.lidroid.xutils.BitmapUtils;

import java.io.File;

/**
 * 直播计算工具
 *
 * @author Raul
 */
public class LiveUtils {

    /**
     * 获取用户的位置标记
     */
    public static BitmapDescriptor getUserBitmapDescriptor(final Context context, final BitmapUtils mBitmapU, String avatar) {
        float round = DeviceUtils.dpToPixel(29);
        BitmapDescriptor result;
        Bitmap b;
        // 若头像为空
        if ("".equals(avatar)) {
            b = ImageUtils.RoundBitmap(context, ImageUtils.zoomImage(
                    BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_user_photo), round, round));
        } else {
            if (!avatar.startsWith("http://wx.qlogo.cn/") && !avatar.equals("")) {
                avatar += "?imageView2/1/w/200/h/200";
            }
            File headBitmapFile = mBitmapU.getBitmapFileFromDiskCache(avatar);
            // 若缓冲图片文件存在
            if (headBitmapFile != null && headBitmapFile.exists()) {
                b = ImageUtils.RoundBitmap(context,
                        ImageUtils.zoomImage(BitmapFactory.decodeFile(headBitmapFile.getAbsolutePath()), round, round));
            } else {
                b = ImageUtils.RoundBitmap(context,
                        ImageUtils.zoomImage(
                                BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_user_photo),
                                round,
                                round));
            }
        }
        result = BitmapDescriptorFactory.fromBitmap(b);
        b = null;
        System.gc();
        return result;
    }
}
