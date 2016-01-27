/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package net.evecom.androidecssp.gps.overitem;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.tianditu.android.maps.GeoPoint;
import com.tianditu.android.maps.ItemizedOverlay;
import com.tianditu.android.maps.MapView;
import com.tianditu.android.maps.Overlay;
import com.tianditu.android.maps.OverlayItem;
/**
 * 
 * 描述  事件气泡
 * @author Mars zhang
 * @created 2015-12-10 上午10:18:50
 */
public class EventOverItem extends ItemizedOverlay<OverlayItem> implements Overlay.Snappable {
        /** MemberVariables */
        private List<OverlayItem> GeoList = new ArrayList<OverlayItem>();
        /** MemberVariables */
        private Context mContext;
        /** MemberVariables */
        private static Drawable mMaker = null;

        /**
         * 
         * 描述
         * 
         * @author Mars zhang
         * @created 2015-11-25 下午5:15:55
         * @param marker
         * @param context
         */
        public EventOverItem(Drawable marker, Context context) {
            super((mMaker = boundCenterBottom(marker)));
            mContext = context;  
        }

        @Override
        protected OverlayItem createItem(int i) {
            return GeoList.get(i);
        }

        @Override
        public int size() {
            return GeoList.size();
        }
        /**
         * 
         * 描述
         * @author Mars zhang
         * @created 2015-12-14 下午2:17:17
         * @param item
         */
        public void addItem(OverlayItem item) {
            item.setMarker(mMaker);
            GeoList.add(item);
         // createItem(int)方法构造item。一旦有了数据，在调用其它方法前，首先调用这个方法
            populate();
        } 
        @Override
        // 处理当点击事件
        protected boolean onTap(int i) {  
            
            
            return true;
        }

        @Override
        public boolean onTouchEvent(MotionEvent event, MapView mapView) {
            // TODO Auto-generated method stub
            return super.onTouchEvent(event, mapView);
        }

        @Override
        public boolean onKeyUp(int keyCode, KeyEvent event, MapView mapView) {
            // TODO Auto-generated method stub
            return super.onKeyUp(keyCode, event, mapView);
        }

        @Override
        public boolean onTrackballEvent(MotionEvent event, MapView mapView) {
            // TODO Auto-generated method stub
            return super.onTrackballEvent(event, mapView);
        }

        @Override
        public void draw(Canvas canvas, MapView mapView, boolean shadow) {
            // TODO Auto-generated method stub
            super.draw(canvas, mapView, shadow);
        }

        @Override
        public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when) {
            // TODO Auto-generated method stub
            return super.draw(canvas, mapView, shadow, when);
        }

        @Override
        public boolean onTap(GeoPoint p, MapView mapView) {
            return super.onTap(p, mapView);
        }

    }