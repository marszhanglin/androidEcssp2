/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package net.evecom.androidecssp.view.gallery;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.Gallery;

/**
 * 
 * ���� GalleryFlow
 * 
 * @author Mars zhang
 * @created 2015-11-25 ����11:23:22
 */
public class GalleryFlow extends Gallery {
    /**
     * The camera class is used to 3D transformation matrix.
     */
    private Camera mCamera = new Camera();

    /**
     * The max rotation angle.
     */
    private int mMaxRotationAngle = 60;

    /**
     * The max zoom value (Z axis).
     */
    private int mMaxZoom = -120;

    /**
     * ���ȵ����ġ�
     */
    private int mCoveflowCenter = 0;

    /**
     * 
     * ����
     * 
     * @author Mars zhang
     * @created 2015-11-25 ����2:25:15
     * @param context
     */
    public GalleryFlow(Context context) {
        this(context, null);
    }

    /**
     * 
     * ����
     * 
     * @author Mars zhang
     * @created 2015-11-25 ����2:25:18
     * @param context
     * @param attrs
     */
    public GalleryFlow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 
     * ����
     * 
     * @author Mars zhang
     * @created 2015-11-25 ����2:25:21
     * @param context
     * @param attrs
     * @param defStyle
     */
    public GalleryFlow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        // Enable set transformation.
        // �����»�����child��ʱ�򶼻�ٷ�getChildStaticTransformation�������
        this.setStaticTransformationsEnabled(true);
        // Enable set the children drawing order. ������������ getChildDrawingOrder
        this.setChildrenDrawingOrderEnabled(true);
    }

    /**
     * 
     * ����
     * 
     * @author Mars zhang
     * @created 2015-11-25 ����2:25:25
     * @return
     */
    public int getMaxRotationAngle() {
        return mMaxRotationAngle;
    }

    /**
     * 
     * ����
     * 
     * @author Mars zhang
     * @created 2015-11-25 ����2:25:28
     * @param maxRotationAngle
     */
    public void setMaxRotationAngle(int maxRotationAngle) {
        mMaxRotationAngle = maxRotationAngle;
    }

    /**
     * 
     * ����
     * 
     * @author Mars zhang
     * @created 2015-11-25 ����2:25:30
     * @return
     */
    public int getMaxZoom() {
        return mMaxZoom;
    }

    /**
     * 
     * ����
     * 
     * @author Mars zhang
     * @created 2015-11-25 ����2:25:34
     * @param maxZoom
     */
    public void setMaxZoom(int maxZoom) {
        mMaxZoom = maxZoom;
    }

    /** ����������� */
    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        // Current selected index.
        int selectedIndex = getSelectedItemPosition() - getFirstVisiblePosition();
        if (selectedIndex < 0) {
            return i;
        }

        if (i < selectedIndex) {
            return i;
        } else if (i >= selectedIndex) {
            return childCount - 1 - i + selectedIndex;
        } else {
            return i;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mCoveflowCenter = getCenterOfCoverflow();
        super.onSizeChanged(w, h, oldw, oldh);
    }

    /**
     * 
     * ����
     * 
     * @author Mars zhang
     * @created 2015-11-25 ����2:24:55
     * @param view
     * @return
     */
    private int getCenterOfView(View view) {
        return view.getLeft() + view.getWidth() / 2;
    }

    /** ÿ���ػ�ʱ���� �ı�Ƕ� */
    @Override
    protected boolean getChildStaticTransformation(View child, Transformation t) {
        super.getChildStaticTransformation(child, t);

        // //��ȡ��ͼ������
        // final int childCenter = getCenterOfView(child);
        // final int childWidth = child.getWidth();
        //
        // int rotationAngle = 0;
        // t.clear();
        // t.setTransformationType(Transformation.TYPE_MATRIX);
        //
        //
        // // System.out.println(childCenter+"=="+mCoveflowCenter);
        // // ��������������ģ����ǲ���ת����
        // if (childCenter == mCoveflowCenter)
        // {
        // transformImageBitmap(child, t, 0); //��ת�Ƕ�0
        // }
        // else
        // {
        // // ������ת�Ƕȡ�
        // rotationAngle = (int)(((float)(mCoveflowCenter - childCenter) /
        // childWidth) * mMaxRotationAngle);
        // transformImageBitmap(child, t, rotationAngle);
        // }

        return true;
    }

    /**
     * 
     * ����
     * 
     * @author Mars zhang
     * @created 2015-11-25 ����2:25:00
     * @return
     */
    private int getCenterOfCoverflow() {
        return (getWidth() - getPaddingLeft() - getPaddingRight()) / 2 + getPaddingLeft();
    }

    /**
     * 
     * ����
     * 
     * @author Mars zhang
     * @created 2015-11-25 ����2:25:04
     * @param child
     * @param t
     * @param rotationAngle
     */
    private void transformImageBitmap(View child, Transformation t, int rotationAngle) {
        mCamera.save();

        final Matrix imageMatrix = t.getMatrix();
        final int imageHeight = child.getHeight();
        final int imageWidth = child.getWidth();
        final int rotation = Math.abs(rotationAngle);

        // Zoom on Z axis.
        mCamera.translate(0, 0, mMaxZoom);

        if (rotation < mMaxRotationAngle) {
            float zoomAmount = (float) (mMaxZoom + rotation * 1.5f);
            mCamera.translate(0, 0, zoomAmount);
        }

        // Rotate the camera on Y axis.
        mCamera.rotateY(rotationAngle);
        // Get the matrix from the camera, in fact, the matrix is S (scale)
        // transformation.
        mCamera.getMatrix(imageMatrix);

        // The matrix final is T2 * S * T1, first translate the center point to
        // (0, 0),
        // then scale, and then translate the center point to its original
        // point.
        // T * S * T

        // S * T1
        imageMatrix.postTranslate((imageWidth / 2), (imageHeight / 2));
        // (T2 * S) * T1
        imageMatrix.preTranslate(-(imageWidth / 2), -(imageHeight / 2));

        mCamera.restore();
    }
}