/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package net.evecom.androidecssp.gps.overitem;

import java.util.ArrayList;
import java.util.List;

import net.evecom.androidecssp.R;
import net.evecom.androidecssp.util.GpsUtil;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tianditu.android.maps.GeoPoint;
import com.tianditu.android.maps.ItemizedOverlay;
import com.tianditu.android.maps.MapView;
import com.tianditu.android.maps.MapView.LayoutParams;
import com.tianditu.android.maps.Overlay;
import com.tianditu.android.maps.OverlayItem;

/**
 * 
 * 描述 资源气泡
 * 
 * @author Mars zhang
 * @created 2015-12-10 上午10:18:50
 */
public class ResourvceOverItem extends ItemizedOverlay<OverlayItem> implements Overlay.Snappable {
    /** MemberVariables */
    private List<OverlayItem> GeoList = new ArrayList<OverlayItem>();
    /** MemberVariables */
    private Context mContext;
    /** MemberVariables */
    private static Drawable mMaker = null;
    /** MemberVariables */
    private LayoutInflater inflater;
    /** MemberVariables */
    private MapView mp;
    /** centerpoint */
    private GeoPoint mcenterPoint; 
    /** 回调 */
    private ResourceItemInterface minterface;
    /**
     * 
     * 描述 
     * @author Mars zhang
     * @created 2015-12-11 上午11:55:54
     */
    public interface ResourceItemInterface{
        /**点击*/
        void resourceItemOnclick(GeoPoint geoPoint);
    }
    /**
     * 
     * 描述
     * 
     * @author Mars zhang
     * @created 2015-11-25 下午5:15:55
     * @param marker
     * @param context
     */
    public ResourvceOverItem(Drawable marker, Context context, MapView mapView, GeoPoint cPoint,ResourceItemInterface resourceItemInterface) {
        super((mMaker = boundCenterBottom(marker)));
        inflater = LayoutInflater.from(context);
        mContext = context;
        mp = mapView;
        mcenterPoint = cPoint;
        this.minterface = resourceItemInterface;
    }

    @Override
    protected OverlayItem createItem(int i) {
        View mPopView = inflater.inflate(R.layout.popview, null);
        TextView mText = (TextView) mPopView.findViewById(R.id.text);
        mp.addView(mPopView, new MapView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, GeoList
                .get(i).getPoint(), MapView.LayoutParams.BOTTOM_CENTER));
        mText.setText(GeoList.get(i).getTitle());
        return GeoList.get(i);
    }

    @Override
    public int size() {
        return GeoList.size();
    }

    public void addItem(OverlayItem item) {
        item.setMarker(mMaker);
        GeoList.add(item);
        // createItem(int)方法构造item。一旦有了数据，在调用其它方法前，首先调用这个方法
        populate();
    }

    @Override
    // 处理当点击事件
    protected boolean onTap(int i) { 
        double distance = GpsUtil.getDistanceFromBaidu(mcenterPoint.getLatitudeE6() / 1E6, mcenterPoint.getLongitudeE6() / 1E6,
                GeoList.get(i).getPoint().getLatitudeE6() / 1E6, GeoList.get(i).getPoint().getLongitudeE6() / 1E6);
        String distanceStr = (distance+"").substring(0, (distance+"").indexOf(".")+3);
        Toast.makeText(mContext,GeoList.get(i).getTitle()+"距事发地"+distanceStr+ "公里", 1).show();
        this.minterface.resourceItemOnclick(GeoList.get(i).getPoint());
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