/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package net.evecom.androidecssp.view;

import net.evecom.androidecssp.R;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 
 * ����MyImageButton
 * 
 * @author Mars zhang
 * @created 2014-11-5 ����10:58:25
 */
public class MyImageButton extends TextView {
    /**
     * 
     * ����
     * 
     * @author Mars zhang
     * @created 2015-11-25 ����2:26:31
     * @param context
     * @param attrs
     */
    public MyImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setClickable(true);
        setFocusable(true);
        // TextView t = new TextView(context);
        setBackgroundResource(R.drawable.wgh_main_jd2);
    }
}