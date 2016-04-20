/*
 * Copyright (c) 2005, 2016, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package net.evecom.androidecssp.view.gallery;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Gallery;

/**
 * ����
 *
 * @author Stark Zhou
 * @created 2016-2-2 ����2:01:33
 */
public class GroupGallery extends Gallery {
    /**mLastMotionX*/
    float mLastMotionX = 0;  

    /**
     * ����
     *
     * @author Stark Zhou
     * @created 2016-2-2 ����2:01:57
     * @param context
     */
    public GroupGallery(Context context) {
        super(context);
    }
    
     
    /**
     * ����
     *
     * @author Stark Zhou
     * @created 2016-2-2 ����2:31:54
     * @param context
     * @param attrs
     * @param defStyle
     */
    public GroupGallery(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }


    /**
     * ����
     *
     * @author Stark Zhou
     * @created 2016-2-2 ����2:31:54
     * @param context
     * @param attrs
     */
    public GroupGallery(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }


    /**
     * ����
     *
     * @author Stark Zhou
     * @created 2016-2-2 ����2:02:42
     * @param ev
     * @return
     * @see android.view.ViewGroup#onInterceptTouchEvent(android.view.MotionEvent)
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action=ev.getAction();
        final float x=ev.getX();
        switch(action){
            case MotionEvent.ACTION_MOVE:
                final int xDiff=(int)Math.abs(x-mLastMotionX);
                if(xDiff>50){
                    return true;
                }
                break;
            case MotionEvent.ACTION_DOWN:
                mLastMotionX = x;                 
                //��ֹ����"��2��"ʱ,����ͻ��  
                onTouchEvent(ev);  
                break;
            default :
                break;
        }
        return false;
    }

}
