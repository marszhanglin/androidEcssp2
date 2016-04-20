/*
 * Copyright (c) 2005, 2015, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package net.evecom.androidecssp.view.autoscoll;

import java.util.HashMap;
import java.util.List;

import net.evecom.androidecssp.R;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 
 * 描述
 * 
 * @author Mars zhang
 * @created 2015-12-28 上午9:43:21
 */
public class AutoScollView extends ViewGroup {
    /** mtexts */
    private List<String> mtexts;
    /** 距离 */
    private int distanceTop = -50;
    /** 每个item的高度 */
    private int perItemHeight = 50;
    /** 间隔几行 */
    private HashMap<Integer, Integer> lineCount = new HashMap<Integer, Integer>();
    /** 最大行数 */
    private int maxLine = 0;

    public AutoScollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoScollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public AutoScollView(Context context) {
        super(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int ccounts = getChildCount();
        for (int i = 0; i < ccounts; i++) {
            View childview = getChildAt(i);
            childview.layout(l, t - distanceTop + perItemHeight * lineCount.get(i), r, b);
        }
    }

    /**
     * 
     * 描述
     * 
     * @author Mars zhang
     * @created 2015-12-28 上午9:44:55
     * @param texts
     */
    public void settexts(List<String> texts) {
        this.mtexts = texts;
        additems();
        mthread.start();
    }

    /**
     * 
     * 描述
     * 
     * @author Mars zhang
     * @created 2015-12-28 下午3:41:27
     */
    private void additems() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        if (null != mtexts && mtexts.size() > 0) {
            for (int i = 0; i < mtexts.size(); i++) {

                String itemstr = mtexts.get(i);
                View childView = layoutInflater.inflate(R.layout.mainmenu_textview_item, null);
                TextView textView = (TextView) childView.findViewById(R.id.mainenu_item_tv_id);
                textView.setText(itemstr);
                if (i != 0) {
                    lineCount.put(i, (int) (itemstr.length() / 7.0 + 0.99999) + lineCount.get(i - 1));
                } else {
                    lineCount.put(i, (int) (itemstr.length() / 7.0 + 0.99999));
                }
                maxLine = Math.max(lineCount.get(i), maxLine);
                addView(childView);
            }
        }
    }

    /**
     * 定时器
     */
    private Thread mthread = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                distanceTop += 9;
                if (distanceTop > ((maxLine * perItemHeight) + 80))
                    distanceTop = -50;
                Message message = new Message();
                message.what = 1;
                mHandler.sendMessage(message);
            }

        }
    });
    /**
     * mHandler
     */
    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    requestLayout();
                    break;

                default:
                    break;
            }
        };
    };

}
