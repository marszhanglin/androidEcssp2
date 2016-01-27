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
 * 描述 事件详细信息
 * 
 * @author Mars zhang
 * @created 2015-11-12 上午10:13:17
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
    /** 定位客户端 */
    MyLocationOverlay mMyLocation = null;
    /** 当前坐标 */
    private Location currentLocation = null;
    /** 定位管理器 */
    LocationManager locationManager = null;
    /** 是否紧急事件下 */
    private boolean ifqueryallevents = true;

    /*
     * private Handler pubHandler = new Handler() { public void
     * handleMessage(Message msg) { switch (msg.what) { case MESSAGETYPE_01:
     * updateGallery(); break; case MESSAGETYPE_02: toast("文件显示失败", 0); break;
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
     * 描述 initViewData
     * 
     * @author Mars zhang
     * @created 2015-11-25 上午10:22:52
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
     * 描述 initView
     * 
     * @author Mars zhang
     * @created 2015-11-25 上午10:23:26
     */
    private void initView() {
        initGallery();
        nameTV = (TextView) findViewById(R.id.event_add_name);
        addrTV = (TextView) findViewById(R.id.event_add_addr);
        sjjbTV = (TextView) findViewById(R.id.event_add_sjjb);
        clztTV = (TextView) findViewById(R.id.event_add_clzt);
        bgrTV = (TextView) findViewById(R.id.event_add_bgr);

        if (ifqueryallevents) {// 不要处置
            findViewById(R.id.event_info_at_qd_ly).setVisibility(View.GONE);
            findViewById(R.id.event_info_at_cz_image).setVisibility(View.GONE);
            findViewById(R.id.event_info_at_xb_image).setVisibility(View.GONE);
        }
    }

    /**
     * 
     * 描述 初始化数据
     * 
     * @author Mars zhang
     * @created 2015-11-23 下午5:32:52
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
     * 描述 初始化界面时清空数据库文件记录
     * 
     * @author Mars zhang
     * @created 2015-11-23 上午9:50:22
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
     * 描述 初始化gallery
     * 
     * @author Mars zhang
     * @created 2015-11-19 上午10:55:25
     */
    private void initGallery() {
        mGallery = (GalleryFlow) findViewById(R.id.event_add_gallery_flow);
        mGallery.setBackgroundColor(Color.parseColor("#ffffff")); // 背景色
        mGallery.setSpacing(90);// 间距
        mGallery.setMaxRotationAngle(20);// 设置倾斜角度
        mGallery.setFadingEdgeLength(10); // 边框渐变宽度
        mGallery.setGravity(Gravity.CENTER_VERTICAL); // 设置对齐方式
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
     * 描述 刷新gallery画廊
     * 
     * @author Mars zhang
     * @created 2015-11-19 上午11:13:14
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
            case 1: // 图片界面
                break;
            case R.layout.afnail_picture_activity: // 图片预览 在预览处不要有删除 耗性能

                break;
            case 3: // 定位界面

                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 
     * 描述 事件位置
     * 
     * @author Mars zhang
     * @created 2015-11-25 上午9:25:12
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
     * 描述 处置点击
     * 
     * @author Mars zhang
     * @created 2015-11-25 上午9:25:12
     * @param view
     */
    public void cz(View view) {
        Intent intent = new Intent(instance, ProjectListActivity.class);
        ProjectListActivity.pushData("eventInfo", eventInfo, intent);
        startActivity(intent);
    }

    /**
     * 
     * 描述 续报
     * 
     * @author Mars zhang
     * @created 2015-12-30 下午4:16:46
     * @param view
     */
    public void xb(View view) {
        Intent intent = new Intent(instance, EventContinueActivity.class);
        EventInfoActivity.pushObjData("eventInfo", eventInfo, intent);
        startActivity(intent);
    }

    /**
     * 
     * 描述 事件签到
     * 
     * @author Mars zhang
     * @created 2015-12-1 上午10:36:29
     * @param view
     */
    public void signevent(View view) {
        Dialog delDia = new AlertDialog.Builder(EventInfoActivity.this).setIcon(R.drawable.qq_dialog_default_icon)
                .setTitle("提示").setMessage("已抵达现场更新定位数据？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
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
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dia, int which) {
                        dia.dismiss();
                    }
                }).create();
        delDia.show();
    }

    /**
     * 
     * 描述 gallery的适配器
     * 
     * @author Mars zhang
     * @created 2015-11-19 上午11:05:15
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
                convertView.setLayoutParams(new Gallery.LayoutParams(160, 240));// 控件的大小
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
     * 描述 开启定位服务
     * 
     * @author Mars zhang
     * @created 2015-12-1 上午11:45:22
     */
    private void requestLocationUpdate() {
        // 天地图描点 实现系统定位回调接口并处理了坐标数据
        mMyLocation = new MyOverlay(EventInfoActivity.this, null);
        // 定位管理器
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            toast("开始定位", 0);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, mMyLocation);
        } else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, mMyLocation);
//            toast("开始基站定位", 0);
        } else {
//            toast("定位未打开", 0);
        }
    }

    /**
     * 天地图 内部类
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
                SharedPreferences sp = getApplicationContext().getSharedPreferences("GPS", MODE_PRIVATE);
                // 存入数据
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
