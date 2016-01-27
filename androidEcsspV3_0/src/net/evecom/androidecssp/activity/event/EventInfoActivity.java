/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package net.evecom.androidecssp.activity.event;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.evecom.androidecssp.R;
import net.evecom.androidecssp.activity.taskresponse.ProjectListActivity;
import net.evecom.androidecssp.base.AfnailPictureActivity;
import net.evecom.androidecssp.base.BaseActivity;
import net.evecom.androidecssp.base.BaseModel;
import net.evecom.androidecssp.bean.FileManageBean;
import net.evecom.androidecssp.gps.EventItemizedOverlayActivity;
import net.evecom.androidecssp.gps.bean.GpsPoint;
import net.evecom.androidecssp.util.ShareUtil;
import net.evecom.androidecssp.view.gallery.GalleryFlow;
import net.mutil.util.HttpUtil;

import org.apache.http.client.ClientProtocolException;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.tianditu.android.maps.GeoPoint;
import com.tianditu.android.maps.MapView;
import com.tianditu.android.maps.MyLocationOverlay;

/**
 * 
 * ���� �¼���ϸ��Ϣ
 * 
 * @author Mars zhang
 * @created 2015-11-12 ����10:13:17
 */
public class EventInfoActivity extends BaseActivity {
    /** MemberVariables */
    private List<FileManageBean> filebeans = new ArrayList<FileManageBean>();
    /** MemberVariables */
    GalleryFlow mGallery = null;
    /** MemberVariables */
    private TextView nameTV;// event_add_name
    /** MemberVariables */
    private TextView addrTV;// event_add_addr
    /** MemberVariables */
    private TextView sjjbTV;// event_add_sjjb
    /** MemberVariables */
    private TextView clztTV;// event_add_clzt
    /** MemberVariables */
    private TextView bgrTV;// event_add_bgr
    /** MemberVariables */
    private TextView contentTV;// event_add_content
    /** MemberVariables */
    private BaseModel eventInfo = null;
    /** ��λ�ͻ��� */
    MyLocationOverlay mMyLocation = null;
    /** ��ǰ���� */
    private Location currentLocation = null;
    /** ��λ������ */
    LocationManager locationManager = null;
    /** �Ƿ�����¼��� */
    private boolean ifqueryallevents = true;

    /*
     * private Handler pubHandler = new Handler() { public void
     * handleMessage(Message msg) { switch (msg.what) { case MESSAGETYPE_01:
     * updateGallery(); break; case MESSAGETYPE_02: toast("�ļ���ʾʧ��", 0); break;
     * default: break; } }; };
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_info_activity);
        initdata();
        initView();
        initViewData();
        requestLocationUpdate();
    }

    /**
     * 
     * ���� initViewData
     * 
     * @author Mars zhang
     * @created 2015-11-25 ����10:22:52
     */
    private void initViewData() {
        contentTV = (TextView) findViewById(R.id.event_add_content);
        nameTV.setText(ifnull(eventInfo.getStr("eventname"), ""));
        addrTV.setText(ifnull(eventInfo.getStr("happenaddress"), ""));
        bgrTV.setText(ifnull(eventInfo.getStr("reporterperson"), "") + "-"
                + ifnull(eventInfo.getStr("reportertel"), ""));
        contentTV.setText(ifnull(eventInfo.getStr("eventname"), ""));
        setDictNameByValueToView("SUDDENEVENT_LEVEL", eventInfo.getStr("eventlever"), sjjbTV);
        clztTV.setText(ifnull(eventInfo.getStr("typename"), ""));
        // setDictNameByValueToView("EVENT_DEAL_STATUS",
        // eventInfo.getStr("eventstatus"), clztTV);

    }

    /**
     * 
     * ���� initView
     * 
     * @author Mars zhang
     * @created 2015-11-25 ����10:23:26
     */
    private void initView() {
        initGallery();
        nameTV = (TextView) findViewById(R.id.event_add_name);
        addrTV = (TextView) findViewById(R.id.event_add_addr);
        sjjbTV = (TextView) findViewById(R.id.event_add_sjjb);
        clztTV = (TextView) findViewById(R.id.event_add_clzt);
        bgrTV = (TextView) findViewById(R.id.event_add_bgr);

        if (ifqueryallevents) {// ��Ҫ����
            findViewById(R.id.event_info_at_qd_ly).setVisibility(View.GONE);
            findViewById(R.id.event_info_at_cz_image).setVisibility(View.GONE);
            findViewById(R.id.event_info_at_xb_image).setVisibility(View.GONE);
        }
    }

    /**
     * 
     * ���� ��ʼ������
     * 
     * @author Mars zhang
     * @created 2015-11-23 ����5:32:52
     */
    private void initdata() {
        Intent intent = getIntent();
        eventInfo = (BaseModel) getData("eventInfo", intent);
        ifqueryallevents = intent.getBooleanExtra("ifqueryallevents", true);
        clearFilesRecord();
        String mfileids = eventInfo.getStr("eventannex");
        String[] mids = mfileids.split(",");
        for (int i = 0; i < mids.length; i++) {
            FileManageBean fileManageBean = new FileManageBean();
            fileManageBean.setFile_by1(mids[i]);
            filebeans.add(fileManageBean);
        }
    }

    /**
     * 
     * ���� ��ʼ������ʱ������ݿ��ļ���¼
     * 
     * @author Mars zhang
     * @created 2015-11-23 ����9:50:22
     */
    private void clearFilesRecord() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getDb().deleteAll(FileManageBean.class);
            }
        }).start();
    }

    /**
     * 
     * ���� ��ʼ��gallery
     * 
     * @author Mars zhang
     * @created 2015-11-19 ����10:55:25
     */
    private void initGallery() {
        mGallery = (GalleryFlow) findViewById(R.id.event_add_gallery_flow);
        mGallery.setBackgroundColor(Color.parseColor("#ffffff")); // ����ɫ
        mGallery.setSpacing(90);// ���
        mGallery.setMaxRotationAngle(20);// ������б�Ƕ�
        mGallery.setFadingEdgeLength(10); // �߿򽥱���
        mGallery.setGravity(Gravity.CENTER_VERTICAL); // ���ö��뷽ʽ
        mGallery.setAdapter(new GalleryAdapter());
        mGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), AfnailPictureActivity.class);
                BaseActivity.pushObjData("filebean", filebeans.get(position), intent);
                startActivityForResult(intent, R.layout.afnail_picture_activity);
            }
        });
    }

    /**
     * 
     * ���� ˢ��gallery����
     * 
     * @author Mars zhang
     * @created 2015-11-19 ����11:13:14
     */
    private void updateGallery() {
        ((GalleryAdapter) mGallery.getAdapter()).notifyDataSetChanged();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1: // ͼƬ����
                break;
            case R.layout.afnail_picture_activity: // ͼƬԤ�� ��Ԥ������Ҫ��ɾ�� ������

                break;
            case 3: // ��λ����

                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 
     * ���� �¼�λ��
     * 
     * @author Mars zhang
     * @created 2015-11-25 ����9:25:12
     * @param view
     */
    public void nowdw(View view) {
        Intent intent = new Intent(instance, EventItemizedOverlayActivity.class);
        GpsPoint mGpsPoint = new GpsPoint();
        mGpsPoint.setDescription(eventInfo.get("eventname") + "");
        mGpsPoint.setName(eventInfo.get("eventname") + "");
        String mgisx = ifnull(eventInfo.get("gisx") + "", "0");
        String mgisy = ifnull(eventInfo.get("gisy") + "", "0");
        mGpsPoint.setGisx(Double.parseDouble(mgisx));
        mGpsPoint.setGisy(Double.parseDouble(mgisy));
        // List<GpsPoint> gpsPoints = new ArrayList<GpsPoint>();
        // gpsPoints.add(mGpsPoint);
        EventInfoActivity.pushObjData("evenTgpsPoints", mGpsPoint, intent);
        startActivity(intent);
    }

    /**
     * 
     * ���� ���õ��
     * 
     * @author Mars zhang
     * @created 2015-11-25 ����9:25:12
     * @param view
     */
    public void cz(View view) {
        Intent intent = new Intent(instance, ProjectListActivity.class);
        ProjectListActivity.pushData("eventInfo", eventInfo, intent);
        startActivity(intent);
    }

    /**
     * 
     * ���� ����
     * 
     * @author Mars zhang
     * @created 2015-12-30 ����4:16:46
     * @param view
     */
    public void xb(View view) {
        Intent intent = new Intent(instance, EventContinueActivity.class);
        EventInfoActivity.pushObjData("eventInfo", eventInfo, intent);
        startActivity(intent);
    }

    /**
     * 
     * ���� �¼�ǩ��
     * 
     * @author Mars zhang
     * @created 2015-12-1 ����10:36:29
     * @param view
     */
    public void signevent(View view) {
        Dialog delDia = new AlertDialog.Builder(EventInfoActivity.this).setIcon(R.drawable.qq_dialog_default_icon)
                .setTitle("��ʾ").setMessage("�ѵִ��ֳ����¶�λ���ݣ�")
                .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dia, int which) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // Intent intent =new
                                // Intent(EventInfoActivity.this,
                                // LocationService.class);
                                // startService(intent);

                                HashMap<String, String> entityMap = new HashMap<String, String>();
                                entityMap.put("eventid", eventInfo.getStr("id"));
                                entityMap.put("gisx", ShareUtil.getString(instance, "GPS", "latitude", "0.0"));
                                entityMap.put("gisy", ShareUtil.getString(instance, "GPS", "longitude", "0.0"));
                                entityMap.put("orgid", ShareUtil.getString(instance, "PASSNAME", "orgid", ""));
                                try {
                                    String resut = connServerForResultPost("jfs/ecssp/mobile/eventCtr/signEvent",
                                            entityMap);
                                    toastInOtherThread(resut, 1);
                                } catch (ClientProtocolException e) {
                                } catch (IOException e) {
                                }
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
        delDia.show();
    }

    /**
     * 
     * ���� gallery��������
     * 
     * @author Mars zhang
     * @created 2015-11-19 ����11:05:15
     */
    private class GalleryAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return filebeans.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (null == convertView) {
                convertView = new ImageView(instance);
                convertView.setLayoutParams(new Gallery.LayoutParams(160, 240));// �ؼ��Ĵ�С
            }
            ImageView imageView = (ImageView) convertView;

            // displayImage(imageView, filebeans.get(position).getFile_URL());
            HashMap<String, String> mparas = new HashMap<String, String>();
            mparas.put("fileid", filebeans.get(position).getFile_by1());
            displayImage(imageView, HttpUtil.getPCURL() + "jfs/ecssp/mobile/pubCtr/getImageFlowById", mparas);

            imageView.setScaleType(ScaleType.FIT_XY);
            return imageView;
        }
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
     * ���� ������λ����
     * 
     * @author Mars zhang
     * @created 2015-12-1 ����11:45:22
     */
    private void requestLocationUpdate() {
        // ���ͼ��� ʵ��ϵͳ��λ�ص��ӿڲ���������������
        mMyLocation = new MyOverlay(EventInfoActivity.this, null);
        // ��λ������
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            toast("��ʼ��λ", 0);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, mMyLocation);
        } else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, mMyLocation);
//            toast("��ʼ��վ��λ", 0);
        } else {
//            toast("��λδ��", 0);
        }
    }

    /**
     * ���ͼ �ڲ���
     */
    class MyOverlay extends MyLocationOverlay {
        public MyOverlay(Context context, MapView mapView) {
            super(context, mapView);
        }

        /*
         * ������"�ҵ�λ��"�ϵĵ���¼�
         */
        protected boolean dispatchTap() {
            return true;
        }

        @Override
        public void onLocationChanged(Location location) {
            super.onLocationChanged(location);
            if (location != null) {
                currentLocation = location;
                SharedPreferences sp = getApplicationContext().getSharedPreferences("GPS", MODE_PRIVATE);
                // ��������
                Editor editor = sp.edit();
                editor.putString("latitude", "" + location.getLatitude());
                editor.putString("longitude", "" + location.getLongitude());
                editor.commit();
                // toast(location.getLatitude()+" "+location.getLongitude(), 0);
            }
            GeoPoint point = mMyLocation.getMyLocation();
        }
    }

}
