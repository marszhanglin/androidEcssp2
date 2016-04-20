/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package net.evecom.androidecssp.activity.pub.imagescan;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 
 * ����
 * 
 * @author Mars zhang
 * @created 2015-11-25 ����11:26:58
 */
public class MyImageView extends ImageView {
    /** MemberVariables */
    private OnMeasureListener onMeasureListener;

    /**
     * 
     * ����
     * 
     * @author Mars zhang
     * @created 2015-11-25 ����12:05:32
     * @param onMeasureListener
     */
    public void setOnMeasureListener(OnMeasureListener onMeasureListener) {
        this.onMeasureListener = onMeasureListener;
    }

    /**
     * 
     * ����
     * 
     * @author Mars zhang
     * @created 2015-11-25 ����12:05:36
     * @param context
     * @param attrs
     */
    public MyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 
     * ����
     * 
     * @author Mars zhang
     * @created 2015-11-25 ����12:05:40
     * @param context
     * @param attrs
     * @param defStyle
     */
    public MyImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // ��ͼƬ�����Ĵ�С�ص���onMeasureSize()������
        if (onMeasureListener != null) {
            onMeasureListener.onMeasureSize(getMeasuredWidth(), getMeasuredHeight());
        }
    }

    /**
     * 
     * ����
     * 
     * @author Mars zhang
     * @created 2015-11-25 ����12:05:45
     */
    public interface OnMeasureListener {
        public void onMeasureSize(int width, int height);
    }

}
