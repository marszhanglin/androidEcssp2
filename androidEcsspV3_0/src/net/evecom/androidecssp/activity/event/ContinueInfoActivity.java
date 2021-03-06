/*
 * Copyright (c) 2005, 2015, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package net.evecom.androidecssp.activity.event;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import net.evecom.androidecssp.R;

import net.evecom.androidecssp.base.AfnailPictureActivity;
import net.evecom.androidecssp.base.BaseActivity;
import net.evecom.androidecssp.base.BaseModel;
import net.evecom.androidecssp.bean.FileManageBean;
import net.evecom.androidecssp.gps.EventItemizedOverlayActivity;
import net.evecom.androidecssp.gps.bean.GpsPoint;
import net.evecom.androidecssp.view.gallery.GalleryFlow;
import net.mutil.util.HttpUtil;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

/**
 * 描述
 * 
 * @author Stark Zhou
 * @created 2015-12-21 下午4:48:16
 */
public class ContinueInfoActivity extends BaseActivity {
    /** filebeans */
    private List<FileManageBean> filebeans = new ArrayList<FileManageBean>();
    /** continueInfo */
    private BaseModel continueInfo;
    /** eventInfo */
    private BaseModel eventInfo;
    /** typeValueKeyMap */
    HashMap<String, String> typeValueKeyMap = new HashMap<String, String>();
    /** resultArray */
    private String resultArray = "";
    /** monitor */
    private List<BaseModel> monitor = new ArrayList<BaseModel>();
    /** monitorView */
    private TextView monitorView;
    /** mGallery */
    private GalleryFlow mGallery = null;
    /** myStr */
    StringBuilder myStr = new StringBuilder();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_continue_info);
        Intent intent = getIntent();
        continueInfo = (BaseModel) getData("continueInfo", intent);
        eventInfo = (BaseModel) getData("eventInfo", intent);
        getMonitor();
        initdata();
        init();
    }

    /**
     * 
     * 初始化
     * 
     * @author Stark Zhou
     * @created 2015-12-30 下午3:03:01
     */
    private void init() {

        typeValueKeyMap.put("1", "旧事件");
        typeValueKeyMap.put("2", "新事件");
        TextView continueName = (TextView) findViewById(R.id.continue_name);
        continueName.setText(ifnull(continueInfo.get("coutinuename") + "", ""));
        TextView continueType = (TextView) findViewById(R.id.continue_type);
        if (continueInfo.get("coutinuetype") != null
                && continueInfo.get("coutinuetype").toString().equals("null") == false) {
            continueType.setText(ifnull(typeValueKeyMap.get(continueInfo.get("coutinuetype")).toString(), ""));
        }
        TextView reportTime = (TextView) findViewById(R.id.continue_reporttime);
        reportTime.setText(ifnull(continueInfo.get("reporterdate") + "", ""));
        TextView continueArea = (TextView) findViewById(R.id.continue_area);
        continueArea.setText(ifnull(continueInfo.get("areaname") + "", ""));
        TextView continueAddr = (TextView) findViewById(R.id.continue_addr);
        continueAddr.setText(ifnull(continueInfo.get("happenaddress") + "", ""));
        TextView continueTime = (TextView) findViewById(R.id.continue_time);
        continueTime.setText(ifnull(continueInfo.get("happendate") + "", ""));
        TextView continueDept = (TextView) findViewById(R.id.continue_dept);
        continueDept.setText(ifnull(continueInfo.get("deptname") + "", ""));
        TextView continueReporter = (TextView) findViewById(R.id.continue_reporter);
        continueReporter.setText(ifnull(continueInfo.get("reporterperson") + "", ""));
        TextView continueReporterTel = (TextView) findViewById(R.id.continue_reportertel);
        continueReporterTel.setText(ifnull(continueInfo.get("reportertel") + "", ""));
        TextView continueStep = (TextView) findViewById(R.id.continue_step);
        continueStep.setText(ifnull(continueInfo.get("takensteps") + "", ""));
        TextView continueContent = (TextView) findViewById(R.id.continue_content);
        continueContent.setText(ifnull(continueInfo.get("coutinuecontent") + "", ""));
        monitorView = (TextView) findViewById(R.id.continue_monitor);
        initGallery();
    }

    /**
     * 
     * 初始化数据
     * 
     * @author Stark Zhou
     * @created 2015-12-30 下午3:03:15
     */
    private void initdata() {
        String mfileids = continueInfo.getStr("continueannex");
        String[] mids = mfileids.split(",");
        for (int i = 0; i < mids.length; i++) {
            FileManageBean fileManageBean = new FileManageBean();
            fileManageBean.setFile_by1(mids[i]);
            filebeans.add(fileManageBean);
        }
    }

    /**
     * 
     * 获取续报事件位置
     * 
     * @author Stark Zhou
     * @created 2016-1-4 下午5:25:07
     * @param view
     */
    public void nowdw(View view) {
        Intent intent = new Intent(instance, EventItemizedOverlayActivity.class);
        GpsPoint mGpsPoint = new GpsPoint();
        mGpsPoint.setDescription(eventInfo.get("eventname") + "");
        mGpsPoint.setName(continueInfo.get("coutinuename") + "");
        String mgisx = ifnull(continueInfo.get("gisx") + "", "0");
        String mgisy = ifnull(continueInfo.get("gisy") + "", "0");
        mGpsPoint.setGisx(Double.parseDouble(mgisx));
        mGpsPoint.setGisy(Double.parseDouble(mgisy));
        // List<GpsPoint> gpsPoints = new ArrayList<GpsPoint>();
        // gpsPoints.add(mGpsPoint);
        EventInfoActivity.pushObjData("evenTgpsPoints", mGpsPoint, intent);
        startActivity(intent);
    }

    /**
     * 
     * 初始化画廊
     * 
     * @author Stark Zhou
     * @created 2015-12-30 下午3:03:36
     */
    private void initGallery() {
        mGallery = (GalleryFlow) findViewById(R.id.continue_gallery_flow);
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
     * 画廊适配器
     * 
     * @author Stark Zhou
     * @created 2015-12-30 下午3:03:56
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

    /**
     * 消息处理器
     */
    private Handler monitorHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGETYPE_01:// 数据请求成功画列表
                    // 设置指标显示的格式
                    for (int i = 0; i < monitor.size(); i++) {
                        myStr.append(monitor.get(i).get("indiname"));
                        myStr.append("  :  ");
                        myStr.append(monitor.get(i).get("indivalue"));
                        myStr.append("\n");
                    }
                    monitorView.setText(myStr);
                    break;
                case MESSAGETYPE_02:
                    toast("暂无事件发生", 1);
                    break;
                default:
                    break;
            }

        };
    };

    /**
     * 
     * 获取续报指标
     * 
     * @author Stark Zhou
     * @created 2015-12-30 下午3:04:25
     */
    private void getMonitor() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                try {
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("continueid", continueInfo.getStr("id"));
                    map.put("eventid", eventInfo.getStr("id"));
                    resultArray = connServerForResultPost("jfs/ecssp/mobile/eventContinueCtr/getContinueMonitor", map);
                } catch (ClientProtocolException e) {
                    message.what = MESSAGETYPE_02;
                } catch (IOException e) {
                    message.what = MESSAGETYPE_02;
                }
                if (resultArray.length() > 0) {
                    try {
                        monitor = getObjsInfo(resultArray);
                        if (null == monitor) {
                            message.what = MESSAGETYPE_02;
                        } else {
                            message.what = MESSAGETYPE_01;
                        }
                    } catch (JSONException e) {
                        message.what = MESSAGETYPE_02;
                    }
                } else {
                    message.what = MESSAGETYPE_02;
                }
                monitorHandler.sendMessage(message);
            }
        }).start();
    }

}
