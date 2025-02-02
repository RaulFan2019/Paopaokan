package com.app.pao.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Picture;
import android.graphics.PixelFormat;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.app.pao.R;
import com.app.pao.config.AppConfig;
import com.app.pao.config.MapEnum;
import com.lidroid.xutils.BitmapUtils;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageUtils {


    /**
     * 缓冲用户头像图片
     *
     * @param avatar
     * @param imageView
     */
    public static void loadUserImage(final String avatar, final ImageView imageView) {
        ImageOptions imageOptions = new ImageOptions.Builder()
                .setCrop(true)
                .setLoadingDrawableId(R.drawable.icon_user_photo)
                .setFailureDrawableId(R.drawable.icon_user_photo)
                .setImageScaleType(ImageView.ScaleType.FIT_CENTER)
                .setSize(50, 50)
                .build();
        x.image().bind(imageView, avatar, imageOptions);
    }

    /**
     * 缓冲历史活动大图
     *
     * @param avatar    //地址
     * @param imageView //图片控件
     */
    public static void loadWorkoutBgImage(final String avatar, final ImageView imageView) {
        ImageOptions imageOptions = new ImageOptions.Builder()
                .setCrop(true)
                .setLoadingDrawableId(R.drawable.icon_history_bg_default)
                .setFailureDrawableId(R.drawable.icon_history_bg_default)
                .setImageScaleType(ImageView.ScaleType.FIT_CENTER)
                .setSize(100, 100)
                .build();
        x.image().bind(imageView, avatar, imageOptions);
    }

    /**
     * 缓冲跑团图片
     *
     * @param avatar    //地址
     * @param imageView //图片控件
     */
    public static void loadGroupImage(final String avatar, final ImageView imageView) {
        ImageOptions imageOptions = new ImageOptions.Builder()
                .setCrop(true)
                .setLoadingDrawableId(R.drawable.icon_default_group)
                .setFailureDrawableId(R.drawable.icon_default_group)
                .setImageScaleType(ImageView.ScaleType.FIT_CENTER)
                .setSize(50, 50)
                .build();
        x.image().bind(imageView, avatar, imageOptions);
    }

    /**
     * 缓冲图片活动小图标
     *
     * @param avatar    //地址
     * @param imageView //图片控件
     */
    public static void loadPartySmallImage(final String avatar, final ImageView imageView) {
        ImageOptions imageOptions = new ImageOptions.Builder()
                .setCrop(true)
                .setLoadingDrawableId(R.drawable.icon_default_small_party)
                .setFailureDrawableId(R.drawable.icon_default_small_party)
                .setImageScaleType(ImageView.ScaleType.FIT_CENTER)
                .setSize(100, 100)
                .build();
        x.image().bind(imageView, avatar, imageOptions);
    }

    /**
     * 缓冲跑团大图
     * @param avatar    //地址
     * @param imageView //图片控件
     */
    public static void loadGroupBigImage(final String avatar, final ImageView imageView) {
        ImageOptions imageOptions = new ImageOptions.Builder()
                .setCrop(true)
                .setLoadingDrawableId(R.drawable.icon_default_picture)
                .setFailureDrawableId(R.drawable.icon_default_picture)
                .setImageScaleType(ImageView.ScaleType.FIT_CENTER)
                .setSize(150, 150)
                .build();
        x.image().bind(imageView, avatar, imageOptions);
    }


    /**
     * 缓冲图片活动大图
     *
     * @param avatar    //地址
     * @param imageView //图片控件
     */
    public static void loadPartyBigImage(final String avatar, final ImageView imageView) {
        ImageOptions imageOptions = new ImageOptions.Builder()
                .setCrop(true)
                .setLoadingDrawableId(R.drawable.bg_party_default)
                .setFailureDrawableId(R.drawable.bg_party_default)
                .setImageScaleType(ImageView.ScaleType.FIT_CENTER)
                .setSize(150, 150)
                .build();
        x.image().bind(imageView, avatar, imageOptions);
    }

    /**
     * 从文件制作用户奔跑头像
     *
     * @param context
     * @param file
     * @return
     */
    public static BitmapDescriptor loadUserDesFromFile(final Context context, final File file) {
        float round = DeviceUtils.dp2px(context, 29);
        Bitmap b = ImageUtils.RoundBitmap(context,
                ImageUtils.zoomImage(BitmapFactory.decodeFile(file.getAbsolutePath()), round, round));
        return BitmapDescriptorFactory.fromBitmap(b);
    }

    /**
     * 获取默认的用户头像
     *
     * @param context
     * @return
     */
    public static BitmapDescriptor loadDefaultUserDes(final Context context) {
        float round = DeviceUtils.dp2px(context, 29);
        Bitmap b = RoundBitmap(context, zoomImage(
                BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_user_photo), round, round));
        return BitmapDescriptorFactory.fromBitmap(b);
    }


    /**
     * 压缩图片到100kb
     *
     * @param
     * @return
     */
    public static Bitmap compressImage(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bm = BitmapFactory.decodeFile(path, options);
        int large = (options.outWidth > options.outHeight) ? options.outWidth : options.outHeight;
        if (large > 500) {
            options.inSampleSize = (int) Math.ceil(large / 500);
        }
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(path, options);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int op = 100;
        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            op -= 10;// 每次都减少10
            if (op < 10) {
                break;
            }
            bm.compress(Bitmap.CompressFormat.JPEG, op, baos);// 这里压缩options%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /**
     * 压缩图片到100kb
     *
     * @param
     * @return
     */
    public static Bitmap compressBitmap(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            options -= 20;// 每次都减少10
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中

        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /**
     * 压缩图片到100kb
     *
     * @param
     * @return
     */
    public static Bitmap compressBitmap(Bitmap image, int size) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > size) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            options -= 10;// 每次都减少10
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中

        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }


    public static Bitmap drawable2Bitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof NinePatchDrawable) {
            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                    drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            return bitmap;
        } else {
            return null;
        }
    }

    /***
     * 图片的缩放方法
     *
     * @param bgimage   ：源图片资源
     * @param newWidth  ：缩放后宽度
     * @param newHeight ：缩放后高度
     * @return
     */
    public static Bitmap zoomImage(Bitmap bgimage, double newWidth, double newHeight) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width, (int) height, matrix, true);
        bgimage = null;
        return bitmap;
    }


    /**
     * 缓冲视频播放图片
     *
     * @param BitmapU   //图片工具
     * @param avatar    //地址
     * @param imageView //图片控件
     */
    public static void loadVideoVImage(final BitmapUtils BitmapU, final String avatar, final ImageView imageView) {
        BitmapU.configDefaultLoadFailedImage(R.drawable.bg_video_default_v);
        BitmapU.configDefaultAutoRotation(true);
        if (avatar != null && !avatar.startsWith("http://wx.qlogo.cn/") && !avatar.equals("")) {
            BitmapU.display(imageView, avatar);
        } else {
            BitmapU.display(imageView, avatar);
        }
    }

    /**
     * 缓冲视频播放图片
     *
     * @param BitmapU   //图片工具
     * @param avatar    //地址
     * @param imageView //图片控件
     */
    public static void loadVideoHImage(final BitmapUtils BitmapU, final String avatar, final ImageView imageView) {
        BitmapU.configDefaultLoadFailedImage(R.drawable.bg_video_default_h);
        BitmapU.configDefaultAutoRotation(true);
        if (avatar != null && !avatar.startsWith("http://wx.qlogo.cn/") && !avatar.equals("")) {
            BitmapU.display(imageView, avatar);
        } else {
            BitmapU.display(imageView, avatar);
        }
    }

    /**
     * 图片切圆
     *
     * @param src
     * @return
     */
    public static Bitmap RoundBitmap(Context context, Bitmap src) {
        //背景图片
        BitmapFactory.Options option = new BitmapFactory.Options();
        Bitmap bg = BitmapFactory.decodeResource(context.getResources(), R.drawable.bg_live_runner, option)
                .copy(Bitmap.Config.ARGB_8888, true);
        int srcRound = (int) (bg.getWidth() - DeviceUtils.dpToPixel(4f));

        Matrix matrix = new Matrix();
//        matrix.postScale(0.9f, 0.9f);
        src = Bitmap.createBitmap(src, 0, 0, srcRound, srcRound, matrix, true);

        // 获取圆形头像的Bitmap
        int radius = src.getWidth() / 2; // src为我们要画上去的图，跟上一个示例中的bitmap一样。
        Bitmap dest = Bitmap.createBitmap(srcRound, srcRound, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(dest);
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        Path path = new Path();
        path.addCircle(radius, radius, radius, Path.Direction.CW);
        c.clipPath(path); // 裁剪区域
        c.drawBitmap(src, 0, 0, paint);// 把图画上去

        // option.inJustDecodeBounds = false;
        Bitmap result = Bitmap.createBitmap(bg.getWidth(), bg.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas lastC = new Canvas(result);
        lastC.drawBitmap(bg, 0, 0, paint);
        lastC.drawBitmap(dest, DeviceUtils.dpToPixel(2f), DeviceUtils.dpToPixel(2f), paint);

        bg.recycle();
        dest.recycle();
        src.recycle();
        System.gc();
        return result;
    }


    /**
     * 制作Split图片
     *
     * @param context
     * @param splite
     * @return
     */
    public static Bitmap GetSpliteBitmap(Context context, int splite, final Typeface typeFace) {
        BitmapFactory.Options option = new BitmapFactory.Options();
        Bitmap bgBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_map_split)
                .copy(Bitmap.Config.ARGB_8888, true);
        // 获得图片的宽高
        int width = bgBitmap.getWidth();
        int height = bgBitmap.getHeight();

        Canvas c = new Canvas(bgBitmap);
        //画空心圆
//        Paint CirclePaint = new Paint();
//        CirclePaint.setStyle(Paint.Style.STROKE);
//        // 消除锯齿
//        CirclePaint.setAntiAlias(true);
//        // 设置画笔的颜色
//        CirclePaint.setColor(Color.WHITE);
//        // 设置paint的外框宽度
//        CirclePaint.setStrokeWidth(MapEnum.STROKE_WIDTH_SPLITE);
//        c.drawCircle(width / 2, width / 2, width / 2 - MapEnum.STROKE_WIDTH_SPLITE / 2, CirclePaint);

        //画数字
        Paint paint = new Paint();
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTypeface(typeFace);
        paint.setColor(Color.WHITE);
        paint.setTextSize(MapEnum.FONT_SIZE_SPLITE);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        // 计算文字高度
        float fontHeight = fontMetrics.bottom - fontMetrics.top;
        // 计算文字baseline
        float textBaseY = height - (height - fontHeight) / 2 - fontMetrics.bottom;
        c.drawText(String.valueOf(splite), width / 2, textBaseY, paint);
        c.save();
        return bgBitmap;
    }


    /**
     * 制作Video图片
     *
     * @param context
     * @param splite
     * @return
     */
    public static Bitmap GetVideoBitmap(Context context, int splite, final Typeface typeFace) {
        BitmapFactory.Options option = new BitmapFactory.Options();
        Bitmap bgBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_map_mark_video)
                .copy(Bitmap.Config.ARGB_8888, true);
        // 获得图片的宽高
        int width = bgBitmap.getWidth();
        int height = bgBitmap.getHeight();

        Canvas c = new Canvas(bgBitmap);
        //画空心圆
//        Paint CirclePaint = new Paint();
//        CirclePaint.setStyle(Paint.Style.STROKE);
//        // 消除锯齿
//        CirclePaint.setAntiAlias(true);
//        // 设置画笔的颜色
//        CirclePaint.setColor(Color.WHITE);
//        // 设置paint的外框宽度
//        CirclePaint.setStrokeWidth(MapEnum.STROKE_WIDTH_SPLITE);
//        c.drawCircle(width / 2, width / 2, width / 2 - MapEnum.STROKE_WIDTH_SPLITE / 2, CirclePaint);

        //画数字
        Paint paint = new Paint();
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTypeface(typeFace);
        paint.setColor(Color.WHITE);
        paint.setTextSize(MapEnum.FONT_SIZE_SPLITE);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        // 计算文字高度
        float fontHeight = fontMetrics.bottom - fontMetrics.top;
        // 计算文字baseline
        float textBaseY = height - (height - fontHeight) / 2 - fontMetrics.bottom;
        c.drawText(String.valueOf(splite), width / 2, textBaseY, paint);
        c.save();
        return bgBitmap;
    }

    /**
     * 进入裁剪图片页面
     */
    public static void startPhotoCut(final Activity activity, final Uri uri, final int RequestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
//        intent.putExtra("return-data", true);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, AppConfig.cutFileUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        activity.startActivityForResult(intent, RequestCode);
    }

    /**
     * 截屏
     *
     * @param context
     * @return
     */
    private Bitmap captureScreen(Activity context) {
        View cv = context.getWindow().getDecorView();
        Bitmap bmp = Bitmap.createBitmap(cv.getWidth(), cv.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        cv.draw(canvas);
        return bmp;
    }

    /**
     * 截取webView快照(webView加载的整个内容的大小)
     *
     * @param webView
     * @return
     */
    public static Bitmap captureWebView(WebView webView) {
        Picture snapShot = webView.capturePicture();

        Bitmap bmp = Bitmap.createBitmap(snapShot.getWidth(), snapShot.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        snapShot.draw(canvas);
        return bmp;
    }

    /**
     * 保存图片到系统相册，并发出通知
     */
    public static void saveBitmapToSystem(final Context context, final Bitmap bitmap, final String picName) {
        File f = new File(AppConfig.DEFAULT_SAVE_CUT_BITMAP, picName);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            MediaStore.Images.Media.insertImage(context.getContentResolver(), f.getAbsolutePath(), "title", "description");
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + f.getAbsolutePath())));
            T.show(context, "已保存到图库", Toast.LENGTH_LONG);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


}
