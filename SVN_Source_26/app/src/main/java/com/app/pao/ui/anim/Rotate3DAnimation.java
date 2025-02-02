package com.app.pao.ui.anim;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;
/**
 * 
 * Copyright (c) 2012 All rights reserved
 * 名称：AbRotate3dAnimation2.java 
 * 描述：TODO
 * @author 原作者  QQ：250333410
 * @date：2013-12-11 上午11:36:37
 * @version v1.0
 */
public class Rotate3DAnimation extends Animation {
	private final float mDepthZ;
	private final boolean mReverse;
	private float mFromDegree;
	private float mToDegree;
	private float mCenterX;
	private float mCenterY;
	private float mLeft;
	private float mTop;
	private Camera mCamera;
	private static final String TAG = "Rotate3d";

//	public Rotate3DAnimation(float fromDegree, float toDegree, float left, float top,
//			float centerX, float centerY) {
//		this.mFromDegree = fromDegree;
//		this.mToDegree = toDegree;
//		this.mLeft = left;
//		this.mTop = top;
//		this.mCenterX = centerX;
//		this.mCenterY = centerY;
//
//	}

	@Override
	public void initialize(int width, int height, int parentWidth,
			int parentHeight) {
		super.initialize(width, height, parentWidth, parentHeight);
		mCamera = new Camera();
	}

//	@Override
//	protected void applyTransformation(float interpolatedTime, Transformation t) {
//		final float FromDegree = mFromDegree;
//		float degrees = FromDegree + (mToDegree - mFromDegree)
//				* interpolatedTime;
//		final float centerX = mCenterX;
//		final float centerY = mCenterY;
//		final Matrix matrix = t.getMatrix();
//
//		if (degrees <= -76.0f) {
//			degrees = -90.0f;
//			mCamera.save();
//			mCamera.rotateY(degrees);
//			mCamera.getMatrix(matrix);
//			mCamera.restore();
//		} else if (degrees >= 76.0f) {
//			degrees = 90.0f;
//			mCamera.save();
//			mCamera.rotateY(degrees);
//			mCamera.getMatrix(matrix);
//			mCamera.restore();
//		} else {
//			mCamera.save();
//			mCamera.translate(0, 0, centerX);
//			mCamera.rotateY(degrees);
//			mCamera.translate(0, 0, -centerX);
//			mCamera.getMatrix(matrix);
//			mCamera.restore();
//		}
//
//		matrix.preTranslate(-centerX, -centerY);
//		matrix.postTranslate(centerX, centerY);
//	}

	/**
	 * Creates a new 3D rotation on the Y axis. The rotation is defined by its
	 * start angle and its end angle. Both angles are in degrees. The rotation
	 * is performed around a center point on the 2D space, definied by a pair
	 * of X and Y coordinates, called centerX and centerY. When the animation
	 * starts, a translation on the Z axis (depth) is performed. The length
	 * of the translation can be specified, as well as whether the translation
	 * should be reversed in time.
	 *
	 * @param fromDegrees the start angle of the 3D rotation
	 * @param toDegrees the end angle of the 3D rotation
	 * @param centerX the X center of the 3D rotation
	 * @param centerY the Y center of the 3D rotation
	 * @param reverse true if the translation should be reversed, false otherwise
	 */
	public Rotate3DAnimation(float fromDegrees, float toDegrees,
							 float centerX, float centerY, float depthZ, boolean reverse) {
		this.mFromDegree = fromDegrees;
		this.mToDegree = toDegrees;
		this.mCenterX = centerX;
		this.mCenterY = centerY;
		this.mDepthZ = depthZ;
		this.mReverse = reverse;
	}

	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		final float fromDegrees = mFromDegree;
		float degrees = fromDegrees + ((mToDegree - fromDegrees) * interpolatedTime);

		final float centerX = mCenterX;
		final float centerY = mCenterY;
		final Camera camera = mCamera;

		final Matrix matrix = t.getMatrix();
		// 将当前的摄像头位置保存下来，以便变换进行完成后恢复成原位，
		camera.save();
		// camera.translate，这个方法接受3个参数，分别是x,y,z三个轴的偏移量，我们这里只将z轴进行了偏移，
		if (mReverse) {
			// z的偏移会越来越大。这就会形成这样一个效果，view从近到远
			camera.translate(0.0f, 0.0f, mDepthZ * interpolatedTime);
		} else {
			// z的偏移会越来越小。这就会形成这样一个效果，我们的View从一个很远的地方向我们移过来，越来越近，最终移到了我们的窗口上面～
			camera.translate(0.0f, 0.0f, mDepthZ * (1.0f - interpolatedTime));
		}
		// 是给我们的View加上旋转效果，在移动的过程中，视图还会移Y轴为中心进行旋转
		camera.rotateY(degrees);
		// 是给我们的View加上旋转效果，在移动的过程中，视图还会移X轴为中心进行旋转。
		// camera.rotateX(degrees);
		// 这个是将我们刚才定义的一系列变换应用到变换矩阵上面，调用完这句之后，我们就可以将camera的位置恢复了，以便下一次再使用。
		camera.getMatrix(matrix);
		// camera位置恢复
		camera.restore();

		// 以View的中心点为旋转中心,如果不加这两句，就是以（0,0）点为旋转中心
		matrix.preTranslate(-centerX, -centerY);
		matrix.postTranslate(centerX, centerY);
	}
}
