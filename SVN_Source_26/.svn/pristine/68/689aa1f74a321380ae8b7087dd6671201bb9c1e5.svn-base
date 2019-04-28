package com.app.pao.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by Raul on 2016/1/20.
 */

public class RotateLinearLayout extends LinearLayout{

    public RotateLinearLayout(Context context) {
        super(context);
    }

    public RotateLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RotateLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public RotateLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Matrix matrix = getMatrix();
        float matrix_values[] = {-1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f};
          matrix.setValues(matrix_values);
//          // 做下面的平移变换，纯粹是为了让变换后的图像和原图像不重叠
//          matrix.postTranslate(canvas.getWidth() * 2f, 0f);
//          view.setImageMatrix(matrix);
        canvas.setMatrix(matrix);
        super.onDraw(canvas);
    }

}
