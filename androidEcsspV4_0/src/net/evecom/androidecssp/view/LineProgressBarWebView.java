/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package net.evecom.androidecssp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * 
 * ���� ֱ�߽�����webview
 * 
 * @author Mars zhang
 * @created 2015-11-6 ����9:10:38
 */
public class LineProgressBarWebView extends WebView {

    /**
     * 
     * ����
     * 
     * @author Mars zhang
     * @created 2015-11-25 ����2:26:04
     * @param context
     * @param attrs
     * @param defStyle
     */
    public LineProgressBarWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

    /**
     * 
     * ����
     * 
     * @author Mars zhang
     * @created 2015-11-25 ����2:26:11
     * @param context
     */
    public LineProgressBarWebView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    /**
     * 
     * ����
     * @author Mars zhang
     * @created 2015-11-25 ����2:26:18
     * @param context
     * @param attrs
     */
    public LineProgressBarWebView(Context context, AttributeSet attrs) {//
        super(context);
        // lineProgressBar =new ProgressBar(context, null,
        // android.R.attr.progressBarStyleHorizontal);
        // lineProgressBar.setLayoutParams(new
        // LayoutParams(LayoutParams.FILL_PARENT, 3,0,0));
        // this.addView(lineProgressBar);//�ӵ�webView��
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        // LayoutParams progressLayoutParams = (LayoutParams)
        // lineProgressBar.getLayoutParams();
        // progressLayoutParams.x=l;//xλ��Ϊ����ƫ�ƴ�С
        // progressLayoutParams.y=t;//yλ��Ϊ����ƫ�ƴ�С
        // lineProgressBar.setLayoutParams(progressLayoutParams);
        super.onScrollChanged(l, t, oldl, oldt);// superִ���ں�
    }

}