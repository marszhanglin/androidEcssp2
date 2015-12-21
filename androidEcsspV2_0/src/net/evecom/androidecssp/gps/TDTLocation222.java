/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package net.evecom.androidecssp.gps;

import java.util.List;

import net.evecom.androidecssp.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.tianditu.android.maps.GeoPoint;
import com.tianditu.android.maps.MapView;
import com.tianditu.android.maps.MyLocationOverlay;
import com.tianditu.android.maps.Overlay;

/**
 * 
 * ����
 * 
 * @author Mars zhang
 * @created 2015-11-25 ����11:24:05
 */
public class TDTLocation222 extends Activity {
    /** MemberVariables */
    private MapView mMapView = null;
    /** MemberVariables */
    protected View mViewLayout = null;
    /** MemberVariables */
    protected Context mCon = null;
    /** MemberVariables */
    int mCount = 0;
    /** MemberVariables */
    Handler handl;
    /** MemberVariables */
    private Button locationBtn = null;
    /** MemberVariables */
    private Button backButton = null;
    /** MemberVariables */
    MyLocationOverlay mMyLocation = null;
    /** MemberVariables */
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(this);
        // �������й���һ��
        mViewLayout = inflater.inflate(R.layout.get_tdt_map2, null);
        setContentView(mViewLayout);
        mMapView = (MapView) findViewById(R.id.mapview);
        mMapView.displayZoomControls(true);
        mCon = this;
        List<Overlay> list = mMapView.getOverlays();
        mMyLocation = new MyOverlay(this, mMapView);
        mMyLocation.enableCompass();
        mMyLocation.enableMyLocation();
        list.add(mMyLocation);

        backButton = (Button) findViewById(R.id.map_btn_back);
        backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(3, intent);
                finish();
            }
        });
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, mMyLocation);
        } else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, mMyLocation);
        }

        locationBtn = (Button) findViewById(R.id.contact_group_find);
        locationBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                GeoPoint point = mMyLocation.getMyLocation();
                if (point != null)
                    mMapView.getController().animateTo(point);

            }

        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        mMapView.getController().stopAnimation(false);
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { // back��
            Intent intent = new Intent();
            setResult(3, intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
    
    
    @Override
    protected void onPause() {
        if (null != mMyLocation) {
            locationManager.removeUpdates(mMyLocation);
        }
        super.onPause();
    }

    /**
     * 
     * ����
     * 
     * @author Mars zhang
     * @created 2015-11-25 ����2:15:48
     */
    class MyOverlay extends MyLocationOverlay {
        public MyOverlay(Context context, MapView mapView) {
            super(context, mapView);
            // TODO Auto-generated constructor stub
        }

        /*
         * ������"�ҵ�λ��"�ϵĵ���¼�
         */
        protected boolean dispatchTap() {
            Toast.makeText(mCon, "��������ҵ�λ��", Toast.LENGTH_SHORT).show();
            return true;
        }

        @Override
        public void onLocationChanged(Location location) {
            // TODO Auto-generated method stub
            super.onLocationChanged(location);
            if (location != null) {
                SharedPreferences sp = getApplicationContext().getSharedPreferences("GPS", MODE_PRIVATE);
                // ��������
                Editor editor = sp.edit();
                editor.putString("latitude", "" + location.getLatitude());
                editor.putString("longitude", "" + location.getLongitude());
                editor.commit();
                String strLog = String.format("����ǰ��λ��:\r\n" + "γ��:%f\r\n" + "����:%f", location.getLongitude(),
                        location.getLatitude());
                Toast.makeText(mCon, strLog, Toast.LENGTH_SHORT).show();
                backButton.setVisibility(View.VISIBLE);
            }
            GeoPoint point = mMyLocation.getMyLocation();
            if (point != null)
                mMapView.getController().animateTo(point);
        }
    }

}