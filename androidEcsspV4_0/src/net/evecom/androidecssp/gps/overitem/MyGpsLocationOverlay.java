/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package net.evecom.androidecssp.gps.overitem;

import android.content.Context;
import android.location.Location;

import com.tianditu.android.maps.GeoPoint;
import com.tianditu.android.maps.MapView;
import com.tianditu.android.maps.MyLocationOverlay;

/**
 * 
 * ����
 * 
 * @author Mars zhang
 * @created 2015-12-14 ����9:39:06
 */
public class MyGpsLocationOverlay extends MyLocationOverlay {
    /** mContext */
    private Context mContext;
    /** mMapView */
    private MapView mMapView;
    /** MyGpsLocationOverlayinterface */
    private MyGpsLocationOverlayinterface mMyGpsLocationOverlayinterface = null;

    /**
     * 
     * ����
     * 
     * @author Mars zhang
     * @created 2015-12-14 ����9:47:44
     */
    public interface MyGpsLocationOverlayinterface {
        /** onLocationChanged */
        public void onLocationChanged(Location location, GeoPoint geoPoint);
    }

    /**
     * 
     * ����
     * 
     * @author Mars zhang
     * @created 2015-12-14 ����9:53:58
     * @param mMyGpsLocationOverlayinterface
     */
    public void setmMyGpsLocationOverlayinterface(MyGpsLocationOverlayinterface mMyGpsLocationOverlayinterface) {
        this.mMyGpsLocationOverlayinterface = mMyGpsLocationOverlayinterface;
    }

    /**
     * 
     * ����
     * 
     * @author Mars zhang
     * @created 2015-12-14 ����9:38:48
     * @param arg0
     * @param arg1
     */
    public MyGpsLocationOverlay(Context context, MapView mapView,
            MyGpsLocationOverlayinterface myGpsLocationOverlayinterface) {
        super(context, mapView);
        mContext = context;
        mMapView = mapView;
        mMyGpsLocationOverlayinterface = myGpsLocationOverlayinterface;
    }

    /**
     * 
     * ����
     * 
     * @author Mars zhang
     * @created 2015-12-14 ����9:38:48
     * @param arg0
     * @param arg1
     */
    public MyGpsLocationOverlay(Context context, MapView mapView) {
        super(context, mapView);
        mContext = context;
        mMapView = mapView;
    }

    /*
     * ������"�ҵ�λ��"�ϵĵ���¼�
     */
    protected boolean dispatchTap() {
        // Toast.makeText(mContext, "��������ҵ�λ��", Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub
        super.onLocationChanged(location);
        if (location != null) {
            GeoPoint point = new GeoPoint((int) (location.getLatitude() * 1E6), (int) (location.getLongitude() * 1E6));
            if (null != point && null != mMapView)
                mMapView.getController().animateTo(point);

            if (null != mMyGpsLocationOverlayinterface) {
                mMyGpsLocationOverlayinterface.onLocationChanged(location, point);
            }
        }
    }
}