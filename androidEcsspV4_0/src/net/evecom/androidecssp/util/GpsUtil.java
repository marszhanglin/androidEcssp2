/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package net.evecom.androidecssp.util;

import net.evecom.androidecssp.base.BaseActivity;

import android.graphics.Point;

import com.tianditu.android.maps.GeoPoint;
import com.tianditu.android.maps.MapView;
import com.tianditu.android.maps.Projection;

/**
 * 
 * 描述 定位工具
 * 
 * @author Mars zhang
 * @created 2015-12-8 下午2:52:00
 */
public class GpsUtil {
    /** 地球的半径KM(公里) */
    private static final double EARTHRADIUS = 6378.137;

    /**
     * 
     * 描述 每公里相差多少经度
     * 
     * @author Mars zhang
     * @created 2015-12-8 下午3:38:50
     * @param mMapView
     * @param maround
     * @param context
     * @param centerGeoPoint
     */
    public static double getLongDifferFromMeters(MapView mMapView, int maround, BaseActivity context,
            GeoPoint centerGeoPoint) {
        int swidth = context.getWindowManager().getDefaultDisplay().getWidth();
        int sheight = context.getWindowManager().getDefaultDisplay().getHeight();
        Projection mProjection = mMapView.getProjection();
        float aroundpx = mProjection.metersToEquatorPixels(maround * 1000);
        if (null != centerGeoPoint) {
            GeoPoint oGeopoint = mProjection.fromPixels(swidth / 2, sheight / 2);
            GeoPoint xGeopoint = mProjection.fromPixels((int) (aroundpx + 0.5) + swidth / 2, 0);
            return (xGeopoint.getLongitudeE6() - oGeopoint.getLongitudeE6()) / 1E6;
        } else {
            Point opoint = mProjection.toPixels(centerGeoPoint, null);
            GeoPoint xGeopoint = mProjection.fromPixels((int) (aroundpx + 0.5) + opoint.x, 0);
            return (xGeopoint.getLongitudeE6() - opoint.x) / 1E6;
        }
    }

    /**
     * 
     * 描述 每公里相差多少纬度
     * 
     * @author Mars zhang
     * @created 2015-12-8 下午3:38:50
     * @param mMapView
     * @param maround
     * @param context
     * @param centerGeoPoint
     */
    public static double getLatDifferFromMeters(MapView mMapView, int maround, BaseActivity context,
            GeoPoint centerGeoPoint) {
        int swidth = context.getWindowManager().getDefaultDisplay().getWidth();
        int sheight = context.getWindowManager().getDefaultDisplay().getHeight();
        Projection mProjection = mMapView.getProjection();
        float aroundpx = mProjection.metersToEquatorPixels(maround * 1000);
        if (null != centerGeoPoint) {
            GeoPoint oGeopoint = mProjection.fromPixels(swidth / 2, sheight / 2);
            GeoPoint yGeopoint = mProjection.fromPixels(0, (int) (aroundpx + 0.5) + sheight / 2);
            return (yGeopoint.getLatitudeE6() - oGeopoint.getLatitudeE6()) / 1E6;
        } else {
            Point opoint = mProjection.toPixels(centerGeoPoint, null);
            GeoPoint yGeopoint = mProjection.fromPixels(0, (int) (aroundpx + 0.5) + opoint.y);
            return (yGeopoint.getLongitudeE6() - opoint.y) / 1E6;
        }
    }

    /**
     * 
     * 描述 计算两个维度间的距离(公里)
     * 
     * @author Mars zhang
     * @created 2015-12-10 下午2:38:28
     * @param lat1Str
     * @param lng1Str
     * @param lat2Str
     * @param lng2Str
     * @return
     */
    public static double getDistance(double lat1, double lng1, double lat2, double lng2) {

        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double difference = radLat1 - radLat2;
        double mdifference = rad(lng1) - rad(lng2);
        double distance = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(difference / 2), 2) + Math.cos(radLat1)
                * Math.cos(radLat2) * Math.pow(Math.sin(mdifference / 2), 2)));

        distance = distance * EARTHRADIUS;

        return distance;
    }

    /**
     * 
     * 描述 计算两个维度间的距离(公里) http://wenku.baidu.com/link?url=
     * aexRXQ822IapdnQ2j3zaqfQj611d_XXihumaYx0qCOiEsGUkl0t7p5zrXSHuuQInyFJGhHOWTnfPKqMAsIe8fuceuqDL8vBRLWZetkTCvme
     * 
     * @author Mars zhang
     * @created 2015-12-10 下午2:38:28
     * @param lat1Str
     * @param lng1Str
     * @param lat2Str
     * @param lng2Str
     * @return
     */
    public static double getDistanceFromBaidu(double lat1, double lng1, double lat2, double lng2) {

        double a = rad(lat1);
        double c = rad(lat2);
        double radLng1 = rad(lng1);
        double radLng2 = rad(lng2);
        double b = radLng1 - radLng2;
        double k = Math.sqrt(Math.pow((Math.sin(a) - Math.sin(c)), 2)
                + Math.pow(Math.cos(a) - Math.cos(c) * Math.cos(b), 2) + Math.pow(Math.cos(c) * Math.sin(b), 2));
        
        double distance = 2 * EARTHRADIUS * Math.asin(k / 2);

        return distance;
    }

    /**
     * 
     * 描述 2π/360 * 度数
     * 
     * @author Mars zhang
     * @created 2015-12-10 下午2:36:49
     * @param d
     * @return
     */
    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }
}
