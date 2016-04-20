/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package net.evecom.androidecssp.service;

import net.evecom.androidecssp.base.BaseService;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;

import com.tianditu.android.maps.GeoPoint;
import com.tianditu.android.maps.MapView;
import com.tianditu.android.maps.MyLocationOverlay;
/**
 * 
 * 描述  定位服务
 * @author Mars zhang
 * @created 2015-12-1 上午11:37:00
 */
public class LocationService extends BaseService {

    
    
/*    public LocationService() {
        super("LocationService");
    }
    
    public LocationService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
//        new MessageThread().start();
    }*/





    /** 定位客户端 */
    MyLocationOverlay mMyLocation = null;
    /** 当前坐标 */
    private Location currentLocation = null;
    /** 定位管理器 */
    LocationManager locationManager = null;
    
    @Override
    public void onCreate() { 
        toastInOtherThread("onCreate", 0);
        requestLocationUpdate(); 
//        mMyLocation = new MyOverlay(this, null);
        
        super.onCreate(); 
        
    }
    
    /**
     * 
     * 描述  开启定位服务
     * @author Mars zhang
     * @created 2015-12-1 上午11:45:22
     */
    private void requestLocationUpdate() {
        // 天地图描点 实现系统定位回调接口并处理了坐标数据
        mMyLocation = new MyOverlay(getApplicationContext(), null);
        // 定位管理器
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            toastInOtherThread("开始定位", 0);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, mMyLocation);
        } else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, mMyLocation);
            toastInOtherThread("开始基站定位", 0);
        } else {
            toastInOtherThread("定位未打开", 0);
        }
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        toastInOtherThread("onStartCommand", 0);
        return super.onStartCommand(intent, flags, startId);
    } 
    
    @Override
    public IBinder onBind(Intent intent) {
        toastInOtherThread("onBind", 0);
        return null;
    }
    
    @Override
    public boolean onUnbind(Intent intent) {
        toastInOtherThread("onUnbind", 0);
        return super.onUnbind(intent);
    }
    
    @Override
    public void onDestroy() {
        toastInOtherThread("onDestroy", 0);
        if (null != mMyLocation) {
            locationManager.removeUpdates(mMyLocation);
        }
        super.onDestroy();
    }
    
    
    
    
    
    
    
/*    private void toastInOtherThread(String string, int i) {
        System.out.println(string);
        
    }*/







    /**
     * 天地图  内部类
     */
    class MyOverlay extends MyLocationOverlay {
        public MyOverlay(Context context, MapView mapView) {
            super(context, mapView);
        }

        /*
         * 处理在"我的位置"上的点击事件
         */
        protected boolean dispatchTap() {
            return true;
        }

        @Override
        public void onLocationChanged(Location location) { 
            super.onLocationChanged(location);
            if (location != null) {
                currentLocation = location; 
            }
            GeoPoint point = mMyLocation.getMyLocation();
        } 
    }









}
