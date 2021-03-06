/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package net.evecom.androidecssp.gps.overitem;

import net.evecom.androidecssp.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.tianditu.android.maps.GeoPoint;
import com.tianditu.android.maps.MapView;
import com.tianditu.android.maps.Overlay;
import com.tianditu.android.maps.OverlayItem;
/**
 * 
 * ����
 * @author Mars zhang
 * @created 2015-12-24 ����3:46:23
 */
public class ManualLocationOverlay extends Overlay {
    private Context mCon = null;
    private OverlayItem mItem = null;
    private Paint mPaint = null;
    private ManualMapOnCilck minterface;

    public interface ManualMapOnCilck {
        public void mapclick(GeoPoint point, MapView mapView);
    }

    public ManualLocationOverlay(Context con, ManualMapOnCilck manualMapOnCilck) {
        mCon = con;
        mPaint = new Paint();
        this.minterface = manualMapOnCilck;
    }

    @Override
    public boolean onTap(GeoPoint point, MapView mapView) {
        if (null != minterface)
            minterface.mapclick(point, mapView);
        mItem = new OverlayItem(point, "Tap", point.toString());
        mapView.postInvalidate();
        return true;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event, MapView mapView) {
        return super.onKeyUp(keyCode, event, mapView);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event, MapView mapView) {
        return super.onKeyDown(keyCode, event, mapView);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event, MapView mapView) {
        return super.onTouchEvent(event, mapView);
    }

    @Override
    public void draw(Canvas canvas, MapView mapView, boolean shadow) {
        // TODO Auto-generated method stub
        super.draw(canvas, mapView, shadow);
        if (mItem == null)
            return;
        mPaint.setColor(Color.RED);
        Drawable d = mCon.getResources().getDrawable(R.drawable.poiresult_sel);

        Point point = mapView.getProjection().toPixels(mItem.getPoint(), null);
        d.setBounds(point.x - d.getIntrinsicWidth() / 2, point.y - d.getIntrinsicHeight(),
                point.x + d.getIntrinsicWidth() / 2, point.y);
        d.draw(canvas);
    }
}
