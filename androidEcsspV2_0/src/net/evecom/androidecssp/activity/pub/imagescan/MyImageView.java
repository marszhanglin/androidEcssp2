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
 * 描述
 * 
 * @author Mars zhang
 * @created 2015-11-25 上午11:26:58
 */
public class MyImageView extends ImageView {
    /** MemberVariables */
    private OnMeasureListener onMeasureListener;

    /**
     * 
     * 描述
     * 
     * @author Mars zhang
     * @created 2015-11-25 下午12:05:32
     * @param onMeasureListener
     */
    public void setOnMeasureListener(OnMeasureListener onMeasureListener) {
        this.onMeasureListener = onMeasureListener;
    }

    /**
     * 
     * 描述
     * 
     * @author Mars zhang
     * @created 2015-11-25 下午12:05:36
     * @param context
     * @param attrs
     */
    public MyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 
     * 描述
     * 
     * @author Mars zhang
     * @created 2015-11-25 下午12:05:40
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

        // 将图片测量的大小回调到onMeasureSize()方法中
        if (onMeasureListener != null) {
            onMeasureListener.onMeasureSize(getMeasuredWidth(), getMeasuredHeight());
        }
    }

    /**
     * 
     * 描述
     * 
     * @author Mars zhang
     * @created 2015-11-25 下午12:05:45
     */
    public interface OnMeasureListener {
        public void onMeasureSize(int width, int height);
    }

}
