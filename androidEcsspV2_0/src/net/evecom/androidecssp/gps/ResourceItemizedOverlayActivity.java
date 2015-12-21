/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package net.evecom.androidecssp.gps;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.evecom.androidecssp.R;
import net.evecom.androidecssp.base.BaseActivity;
import net.evecom.androidecssp.base.BaseModel;
import net.evecom.androidecssp.gps.overitem.MyGpsLocationOverlay;
import net.evecom.androidecssp.gps.overitem.ResourvceOverItem;
import net.evecom.androidecssp.util.GpsUtil;
import net.evecom.androidecssp.view.wheel.OnWheelChangedListener;
import net.evecom.androidecssp.view.wheel.OnWheelScrollListener;
import net.evecom.androidecssp.view.wheel.WheelView;
import net.evecom.androidecssp.view.wheel.adapter.AbstractWheelTextAdapter;
import net.mutil.util.HttpUtil;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tianditu.android.maps.GeoPoint;
import com.tianditu.android.maps.MapController;
import com.tianditu.android.maps.MapView;
import com.tianditu.android.maps.MyLocationOverlay;
import com.tianditu.android.maps.Overlay;
import com.tianditu.android.maps.OverlayItem;

/**
 * 
 * ����
 * 
 * @author Mars zhang
 * @created 2015-12-11 ����8:44:35
 */
public class ResourceItemizedOverlayActivity extends BaseActivity {
    /** MemberVariables */
    public static View mPopView = null;
    /** MemberVariables */
    public static TextView mText = null;
    /** MemberVariables */
    public static MapView mMapView = null;
    /** MemberVariables */
    protected static Context mCon = null;
    /** MemberVariables */
    protected MapController mController = null;
    /** MemberVariables */
    private GeoPoint centerGpsPoint = null; 
    /** �ܱ���Դ��Χ��ѡ��ֵ */
    private String[] aroundNum = { "1", "5", "10", "15", "20", "25", "30", "35", "40", "50", "100", "200", "400" };
    /** �ܱ���Դ��Χѡ��ֵ */
    private String aroundChouseValue = "5";// Ĭ�ϲ�ѯ�ܱ�5�������Դ
    /** tempindex */
    private int tempindex = 0;

    /** resourceTypehashmap */
    private HashMap<String, String> resourceTypehashmap = new HashMap<String, String>();
    /** �ܱ���Դ��Χ��ѡ��ֵ */
    private String[] resourceType = {};
    /** �ܱ���Դ��Χѡ��ֵ */
    private String resourceTypeChouseValue = "";
    /** resourceTypetempindex */
    private int resourceTypetempindex = 0;
    /** ��Դ��Χѡ��Ի��� */
    private Dialog delDia;
    /** ������Դ����ED */
    private EditText dialogNameEd;
    /** ��Դ�б� */
    private ListView resourceListView;
    /** ��Դ���ݶ��� */
    private List<BaseModel> resourceModels = new ArrayList<BaseModel>();
    /** ��Դ�б������� */
    private ModelAdapter mAdapter;
    /** ��Դ���ְ� */
    private RelativeLayout resourceRelativeLayout;
    /** MyLocationOverlay */
    private MyLocationOverlay myLocation;
    /** SharedPreferences */
    SharedPreferences sp = null;
    /** MemberVariables */
    private LocationManager locationManager;
    /** ����UI���� */
    private Handler pubhandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGETYPE_01: // ��Դ�����б�
                    mAdapter.notifyDataSetChanged();
                    break;
                case MESSAGETYPE_02:
                    toast("����ʧ��", 0);
                    break;
                default:
                    break;
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        initData();
        /** 2.2 ***/
        setContentView(R.layout.itemized_resource);
        /** 2.2 ***/
        mMapView = (MapView) findViewById(R.id.itemized_mapview);
        mMapView.setBuiltInZoomControls(true);
        // ������ʱ��ʾOverlay�����Ի�Ӱ�������ʾЧ��
        // mMapView.setDrawOverlayWhenZooming(true);
        mController = mMapView.getController();
        mCon = this;
        // ��λ
        List<Overlay> list = mMapView.getOverlays();
        centerGpsPoint = new GeoPoint((int) (26.031846613632812 * 1E6), (int) (119.4909095748828 * 1E6));
        mController.setCenter(centerGpsPoint);
        sp = instance.getSharedPreferences("GPS", instance.MODE_PRIVATE);
        myLocation = new MyGpsLocationOverlay(this, mMapView, new MyGpsLocationOverlay.MyGpsLocationOverlayinterface() {
            @Override
            public void onLocationChanged(Location location, GeoPoint geoPoint) {
                Editor editor = sp.edit();
                editor.putString("latitude", "" + location.getLatitude());
                editor.putString("longitude", "" + location.getLongitude());
                editor.commit();
                String strLog = String.format("����ǰ��λ��:\r\n" + "γ��:%f\r\n" + "����:%f", location.getLongitude(),
                        location.getLatitude());
                Toast.makeText(instance, strLog, Toast.LENGTH_SHORT).show();
                System.out.println(strLog);
                centerGpsPoint = geoPoint;
            }
        });
        myLocation.enableCompass();
        myLocation.enableMyLocation();
        list.add(myLocation);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, myLocation);
        } else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, myLocation);
        }

        // initAroundDialog();
        initOtherView();

    }

    /**
     * 
     * ����
     * 
     * @author Mars zhang
     * @created 2015-12-14 ����11:46:39
     */
    private void initData() {
        getLikeDict("jfs/ecssp/mobile/eventCtr/getResourceType", null, resourceTypehashmap);
    }

    /**
     * 
     * 
     * ����
     * 
     * @author Mars zhang
     * @created 2015-12-8 ����8:32:39
     */
    private void initOtherView() {
        resourceRelativeLayout = (RelativeLayout) findViewById(R.id.itemized_resource_rl);
        resourceListView = (ListView) findViewById(R.id.itemized_lv);
        mAdapter = new ModelAdapter(instance);
        resourceListView.setAdapter(mAdapter);
    }

    /**
     * 
     * ���� ��Դѡ��Ի���
     * 
     * @author Mars zhang
     * @created 2015-12-8 ����2:48:19
     */
    private void initAroundDialog() {
        View dialogView = LayoutInflater.from(ResourceItemizedOverlayActivity.this).inflate(R.layout.base_wheel_view,
                null);
        dialogNameEd = (EditText) dialogView.findViewById(R.id.base_wheel_view_name_ed);

        WheelView wheelView = (WheelView) dialogView.findViewById(R.id.country);
        wheelView.setVisibleItems(1);
        wheelView.setViewAdapter(new CountryAdapter(ResourceItemizedOverlayActivity.this, aroundNum));
        wheelView.addScrollingListener(new OnWheelScrollListener() {
            public void onScrollingStarted(WheelView wheel) {

            }

            public void onScrollingFinished(WheelView wheel) {
                aroundChouseValue = aroundNum[tempindex];
            }
        });
        wheelView.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                tempindex = newValue;
            }
        });
        wheelView.setCurrentItem(0);
        aroundChouseValue = aroundNum[1];
        tempindex = 0;

        // ��Դ����
        resourceType = resourceTypehashmap.keySet().toArray(new String[resourceTypehashmap.size()]);
        WheelView resourceWheelView = (WheelView) dialogView.findViewById(R.id.country_resource_type);
        resourceWheelView.setVisibleItems(1);
        resourceWheelView.setViewAdapter(new CountryAdapter(ResourceItemizedOverlayActivity.this, resourceType));
        resourceWheelView.addScrollingListener(new OnWheelScrollListener() {
            public void onScrollingStarted(WheelView wheel) {

            }

            public void onScrollingFinished(WheelView wheel) {
                resourceTypeChouseValue = resourceType[resourceTypetempindex];
            }
        });
        resourceWheelView.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                resourceTypetempindex = newValue;
            }
        });
        if (resourceType.length > 1) {
            resourceWheelView.setCurrentItem(0);
            resourceTypeChouseValue = resourceType[1];
            resourceTypetempindex = 0;
        }

        delDia = new AlertDialog.Builder(ResourceItemizedOverlayActivity.this)
                .setIcon(R.drawable.qq_dialog_default_icon).setTitle("��Դ����").setView(dialogView)
                .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dia, int which) {
                        toast("�����ܱ�" + aroundChouseValue + "�������Դ", 1);
                        if (null != resourceRelativeLayout) {
                            resourceRelativeLayout.setVisibility(View.VISIBLE);
                        }
                        final Message message = new Message();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if (null == centerGpsPoint) {
                                    return;
                                }
                                HashMap<String, String> mEntityMap = new HashMap<String, String>();
                                int maround = Integer.parseInt(aroundChouseValue);
                                double longdiffer = GpsUtil.getLongDifferFromMeters(mMapView, maround,
                                        ResourceItemizedOverlayActivity.this, centerGpsPoint);
                                double latdiffer = GpsUtil.getLatDifferFromMeters(mMapView, maround,
                                        ResourceItemizedOverlayActivity.this, centerGpsPoint);

                                mEntityMap.put("longdiffer", longdiffer + "");
                                mEntityMap.put("latdiffer", latdiffer + "");
                                mEntityMap.put("centergisy", centerGpsPoint.getLatitudeE6()/1E6 + "");
                                mEntityMap.put("centergisx", centerGpsPoint.getLongitudeE6()/1E6 + "");
                                mEntityMap.put("pagesize", HttpUtil.getPageSize(instance));
                                mEntityMap.put("resourcename", ifnull(dialogNameEd.getText().toString(), ""));
                                mEntityMap.put("resourcetype",
                                        ifnull(resourceTypehashmap.get(resourceTypeChouseValue), ""));
                                try {
                                    String mResult = connServerForResultPost(
                                            "jfs/ecssp/mobile/eventCtr/searchResourceAround", mEntityMap);
                                    resourceModels = getObjsInfo(mResult);
                                    message.what = MESSAGETYPE_01;
                                } catch (ClientProtocolException e) {
                                    message.what = MESSAGETYPE_02;
                                } catch (IOException e) {
                                    message.what = MESSAGETYPE_02;
                                } catch (JSONException e) {
                                    message.what = MESSAGETYPE_02;
                                }
                                pubhandler.sendMessage(message);

                            }
                        }).start();
                        dia.dismiss();
                    }
                }).setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dia, int which) {
                        dia.dismiss();
                    }
                }).create();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
            finish();
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        if (null != myLocation) {
            locationManager.removeUpdates(myLocation);
        }
        super.onPause();
    }

    /**
     * 
     * ���� �ܱ���Դ
     * 
     * @author Mars zhang
     * @created 2015-12-8 ����10:29:18
     * @param view
     */
    public void resourcearound(View view) {
        if (null == delDia) {
            initAroundDialog();
        }
        if (delDia.isShowing()) {
            return;
        }
        if (null != resourceRelativeLayout && resourceRelativeLayout.getVisibility() == View.VISIBLE) {
            resourceRelativeLayout.setVisibility(View.GONE);
        } else {
            delDia.show();
        }

    }

    /**
     * wheelView������ 2014-7-22����5:56:27 ��CountryAdapter
     * 
     * @author Mars zhang
     * 
     */
    private class CountryAdapter extends AbstractWheelTextAdapter {
        /** MemberVariables */
        String[] list;

        protected CountryAdapter(Context context, String[] list) {
            super(context, R.layout.tempitem, NO_RESOURCE);
            this.list = list;
            setItemTextResource(R.id.tempValue);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
            return view;
        }

        @Override
        public int getItemsCount() {
            return list.length;
        }

        @Override
        protected CharSequence getItemText(int index) {
            return (null != list[index] ? list[index] : "");
        }

    }

    /**
     * ����ͨ��ListView��������
     * 
     * @author Mars zhang
     */
    public class ModelAdapter extends BaseAdapter implements ListAdapter {
        /** MemberVariables */
        private Context context;
        /** MemberVariables */
        private LayoutInflater inflater;

        public ModelAdapter(Context context) {
            this.context = context;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return resourceModels == null ? 0 : resourceModels.size();

        }

        @Override
        public Object getItem(int item) {
            return resourceModels.get(item);
        }

        @Override
        public long getItemId(int itemId) {
            return itemId;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            if (null == view) {
                view = inflater.inflate(R.layout.resource_list_item13, null);
            }
            TextView deptTv = (TextView) view.findViewById(R.id.resource_list_item13_tv_4);
            TextView nameTv = (TextView) view.findViewById(R.id.resource_list_item13_tv_1);
            TextView typeTv = (TextView) view.findViewById(R.id.resource_list_item13_tv_2);
            TextView areaTv = (TextView) view.findViewById(R.id.resource_list_item13_tv_3);
            final BaseModel mResourceModel = resourceModels.get(i);
            deptTv.setText("����������" + ifnull(mResourceModel.get("deptname"), ""));
            nameTv.setText("��Դ����:" + ifnull(mResourceModel.get("name"), ""));
            typeTv.setText(ifnull(mResourceModel.get("tablenamecn"), ""));
            areaTv.setText(ifnull(mResourceModel.get("areaname"), ""));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    double mResourceGisx = Double.parseDouble(ifnull(mResourceModel.get("gisx"), "0"));
                    double mResourceGisy = Double.parseDouble(ifnull(mResourceModel.get("gisy"), "0"));

                    Resources res = getResources();
                    Drawable marker = res.getDrawable(R.drawable.overitem_resource);// ��
                    ResourvceOverItem mResourcceOverlay = new ResourvceOverItem(marker,
                            ResourceItemizedOverlayActivity.this, mMapView, centerGpsPoint,
                            new ResourvceOverItem.ResourceItemInterface() {
                                @Override
                                public void resourceItemOnclick(GeoPoint geoPoint) {

                                }
                            });
                    GeoPoint mResourceP = new GeoPoint((int) (mResourceGisy * 1E6), (int) (mResourceGisx * 1E6));
                    mResourcceOverlay.addItem(new OverlayItem(mResourceP, ifnull(mResourceModel.get("name"), ""),
                            ifnull(mResourceModel.get("name"), "")));
                    mMapView.getOverlays().add(mResourcceOverlay);
                    mMapView.postInvalidate();
                    if (null != resourceRelativeLayout && resourceRelativeLayout.getVisibility() == View.VISIBLE) {
                        resourceRelativeLayout.setVisibility(View.GONE);
                    }
                }
            });
            return view;
        }
    }
}
