/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package net.evecom.androidecssp.activity.event;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import net.evecom.androidecssp.R;
import net.evecom.androidecssp.activity.pub.AsyAreaTreeActivity;
import net.evecom.androidecssp.activity.pub.AsyDeptTreeActivity;
import net.evecom.androidecssp.activity.pub.imagescan.PictureSelectActivity;
import net.evecom.androidecssp.base.BaseActivity;
import net.evecom.androidecssp.base.BaseModel;
import net.evecom.androidecssp.bean.FileManageBean;
import net.evecom.androidecssp.util.ShareUtil;
import net.evecom.androidecssp.view.gallery.GalleryFlow;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView.ScaleType;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

/**
 * 描述
 * 
 * @author Stark Zhou
 * @created 2015-12-30 下午3:59:38
 */
public class ContinueAddActivity extends BaseActivity {
    /** filebeans */
    private List<FileManageBean> filebeans = new ArrayList<FileManageBean>();
    /** mGallery */
    GalleryFlow mGallery = null;
    /** eventName */
    private TextView eventName;
    /** continueName */
    private EditText continueName;
    /** continueMonitor */
    private TextView continueMonitor;
    /** continueTime */
    private TextView happenTime;
    /** continueType */
    private TextView continueType;
    /** continueArea */
    private TextView continueArea;
    /** continueDept */
    private TextView continueDept;
    /** continueReporter */
    private EditText continueReporter;
    /** continueTel */
    private EditText continueTel;
    /** continueContent */
    private EditText continueContent;
    /** continueStep */
    private EditText continueStep;
    /** continueRemark */
    private EditText continueRemark;
    /** continueAddr */
    private EditText continueAddr;
    /** eventInfo */
    private BaseModel eventInfo;
    /** typeValueKeyMap */
    HashMap<String, String> typeValueKeyMap = new HashMap<String, String>();
    /** saveResult */
    private String saveResult = "";
    /** istimePicked */
    private boolean istimePicked = false;
    /** calendar */
    private Calendar calendar;
    /** monitorValue */
    private EditText monitorValue;
    /** monitorSpin */
    private Spinner monitorSpin;
    /** monitorType */
    private String monitorType = "";
    /** spinItems */
    private String[] spinItems;
    /** monitorMap */
    HashMap<String, String> monitorMap = new HashMap<String, String>();
    /** hashMap */
    HashMap<String, String> hashMap = new HashMap<String, String>();
    /** areaMap */
    HashMap<String, String> areaMap = new HashMap<String, String>();
    /** monitors */
    private List<BaseModel> monitors = new ArrayList<BaseModel>();
    /** reporttime */
    private TextView reportTime;
    /** happenDate */
    private RelativeLayout happenDateBlock;
    /** happenArea */
    private RelativeLayout happenAreaBlock;
    /** happenAddr */
    private RelativeLayout happenAddrBlock;
    /** reportdate */
    private TextView reportDate;
    /** happendate */
    private TextView happenDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_continue_add);
        Intent intent = getIntent();
        eventInfo = (BaseModel) getData("eventInfo", intent);
        monitors = (List<BaseModel>) getData("monitorType", intent);
        init();
        initData();
    }

    /**
     * 
     * 文件选择
     * 
     * @author Stark Zhou
     * @created 2016-1-4 上午10:43:56
     * @param view
     */
    public void choose_image(View view) {
        Intent intent = new Intent(instance, PictureSelectActivity.class);
        startActivityForResult(intent, R.layout.picture_select_at);
    }

    /**
     * 
     * 初始化指标
     * 
     * @author Stark Zhou
     * @created 2015-12-30 下午2:44:10
     */
    private void initData() {
        int s = monitors.size();
        spinItems = new String[s];
        for (int i = 0; i < monitors.size(); i++) {
            spinItems[i] = monitors.get(i).get("indiname").toString();
            monitorMap.put(monitors.get(i).get("indiname").toString(), monitors.get(i).get("indivalue").toString());
        }
        mapToStr();
    }

    /**
     * 
     * 初始化
     * 
     * @author Stark Zhou
     * @created 2015-12-30 下午2:44:26
     */
    private void init() {
        calendar = Calendar.getInstance();
        typeValueKeyMap.put("旧事件", "1");
        typeValueKeyMap.put("新事件", "2");
        happenAreaBlock = (RelativeLayout) findViewById(R.id.happen_area_block);
        happenAreaBlock.setVisibility(View.GONE);
        happenDateBlock = (RelativeLayout) findViewById(R.id.happen_date_block);
        happenDateBlock.setVisibility(View.GONE);
        happenAddrBlock = (RelativeLayout) findViewById(R.id.happen_addr_block);
        happenAddrBlock.setVisibility(View.GONE);
        reportTime = (TextView) findViewById(R.id.continue_reporttime_add);
        reportDate = (TextView) findViewById(R.id.continue_reportdate_add);
        eventName = (TextView) findViewById(R.id.continue_eventname_add);
        eventName.setText(eventInfo.getStr("eventname"));
        continueMonitor = (TextView) findViewById(R.id.continue_monitor_add);
        continueName = (EditText) findViewById(R.id.continue_name_add);
        happenTime = (TextView) findViewById(R.id.happen_time_add);
        happenDate = (TextView) findViewById(R.id.happen_date_add);
        continueType = (TextView) findViewById(R.id.continue_type_add);
        continueType.setText("旧事件");
        continueArea = (TextView) findViewById(R.id.continue_area_add);
        continueAddr = (EditText) findViewById(R.id.continue_addr_add);
        continueDept = (TextView) findViewById(R.id.continue_dept_add);
        continueReporter = (EditText) findViewById(R.id.continue_reporter_add);
        String userName = ShareUtil.getString(getApplicationContext(), "PASSNAME", "usernameCN", "0");
        continueReporter.setText(userName);
        continueTel = (EditText) findViewById(R.id.continue_tel_add);
        continueContent = (EditText) findViewById(R.id.continue_content_add);
        continueStep = (EditText) findViewById(R.id.continue_step_add);
        continueRemark = (EditText) findViewById(R.id.continue_remark_add);
        happenDate.setText(calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-"
                + calendar.get(Calendar.DAY_OF_MONTH));
        happenTime.setText(calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));
        reportDate.setText(calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-"
                + calendar.get(Calendar.DAY_OF_MONTH));
        reportTime.setText(calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));

        // 时间选择器
        happenDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                calendar = Calendar.getInstance();
                happenDate.setBackgroundResource(R.drawable.time_btn_press);
                new DatePickerDialog(ContinueAddActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker arg0, int year, int monthOfYear, int dayOfMonth) {
                        happenDate.setText("" + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        happenDate.setBackgroundResource(R.drawable.time_btn);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
                        .show();

            }
        });
        happenTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                calendar = Calendar.getInstance();
                happenTime.setBackgroundResource(R.drawable.time_btn_press);
                new TimePickerDialog(ContinueAddActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        happenTime.setText(hourOfDay + ":" + minute);
                        happenTime.setBackgroundResource(R.drawable.time_btn);
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();

            }
        });

        reportDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                calendar = Calendar.getInstance();
                reportDate.setBackgroundResource(R.drawable.time_btn_press);
                new DatePickerDialog(ContinueAddActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker arg0, int year, int monthOfYear, int dayOfMonth) {
                        reportDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        reportDate.setBackgroundResource(R.drawable.time_btn);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });

        reportTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                reportTime.setBackgroundResource(R.drawable.time_btn_press);
                calendar = Calendar.getInstance();
                new TimePickerDialog(ContinueAddActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        reportTime.setText(hourOfDay + ":" + minute);
                        reportTime.setBackgroundResource(R.drawable.time_btn);
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
        // 部门选择树返回结果
            case R.layout.dept_asytree_at:
                String deptid = data.getStringExtra("deptid");
                String deptName = data.getStringExtra("deptName");
                if (null != deptid) {
                    hashMap.put("infoContinue.reporterdept", deptid);
                    continueDept.setText(deptName);
                }
                break;
            // 区域选择树返回结果
            case R.layout.area_asytree_at:
                String areaid = data.getStringExtra("areaid");
                String areaName = data.getStringExtra("areaName");
                if (null != areaid) {
                    hashMap.put("infoContinue.coutinuearea", areaid);
                    continueArea.setText(areaName);
                }
                break;
            // 文件选择返回结果
            case R.layout.picture_select_at:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        filebeans.removeAll(filebeans);
                        filebeans = getDb().findAll(FileManageBean.class);
                        Message message = new Message();
                        message.what = R.layout.picture_select_at;
                        pubHandler.sendMessage(message);
                    }
                }).start();
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 公用pubHandler
     */
    public Handler pubHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case R.layout.picture_select_at:
                    updateGallery();
                    break;
                case 1:

                    break;
                default:
                    break;
            }
        };
    };

    /**
     * 
     * 刷新画廊
     * 
     * @author Stark Zhou
     * @created 2016-1-4 上午11:21:29
     */
    private void updateGallery() {
        ((GalleryAdapter) mGallery.getAdapter()).notifyDataSetChanged();
    }

    /**
     * 
     * gallery适配器
     * 
     * @author Stark Zhou
     * @created 2016-1-4 上午11:21:54
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
            displayImage(imageView, filebeans.get(position).getFile_URL());
            imageView.setScaleType(ScaleType.FIT_XY);
            return imageView;
        }
    }

    /**
     * 提交表单
     */
    public void submit(View view) {
        if (noChecked()) {
            return;
        }
        hashMap.put("eventid", eventInfo.get("id").toString());
        hashMap.put("infoContinue.eventid", eventInfo.get("id").toString());
        hashMap.put("infoContinue.coutinuetype", typeValueKeyMap.get(continueType.getText().toString()));
        hashMap.put("infoContinue.coutinueName", continueName.getText().toString());

        if (continueType.getText().toString().equals("新事件")) {
            hashMap.put("infoContinue.happenaddress", continueAddr.getText().toString());
            hashMap.put("infoContinue.gisy", ShareUtil.getString(getApplicationContext(), "GPS", "latitude", ""));
            hashMap.put("infoContinue.gisx", ShareUtil.getString(getApplicationContext(), "GPS", "longitude", ""));
            hashMap.put("infoContinue.happendate", happenDate.getText() + " " + happenTime.getText() + ":00");
        }

        hashMap.put("infoContinue.reporterperson", continueReporter.getText().toString());
        hashMap.put("infoContinue.reportertel", continueTel.getText().toString());
        hashMap.put("infoContinue.coutinuecontent", continueContent.getText().toString());
        hashMap.put("infoContinue.takensteps", continueStep.getText().toString());
        hashMap.put("infoContinue.remark", continueRemark.getText().toString());
        String reportDt = reportDate.getText().toString() + " " + reportTime.getText().toString() + ":00";
        hashMap.put("infoContinue.reporterdate", reportDt);
        // 加入指标值
        for (int i = 0; i < monitors.size(); i++) {
            if (monitorMap.get(monitors.get(i).get("indiname").toString()) == null
                    || monitorMap.get(monitors.get(i).get("indiname").toString()).toString().equals("")) {
                hashMap.put("indiname" + i, monitors.get(i).get("indiname").toString());
                hashMap.put("indivalue" + i, "--");
            } else {
                hashMap.put("indiname" + i, monitors.get(i).get("indiname").toString());
                hashMap.put("indivalue" + i, monitorMap.get(monitors.get(i).get("indiname").toString()));
            }
        }

        postdata(hashMap);
    }

    /**
     * 提交表单数据
     * 
     */
    private void postdata(final HashMap<String, String> entity) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                try {
                    saveResult = connServerForResultPost("jfs/ecssp/mobile/eventContinueCtr/eventContinue", entity);
                    if (saveResult.length() > 0) {
                        message.what = MESSAGETYPE_01;
                        String continueId = "";
                        try {
                            BaseModel continueInfo = getObjInfo(saveResult);
                            if (null != continueInfo) {
                                continueId = continueInfo.get("id");
                            }
                        } catch (JSONException e) {
                            Log.e("mars", e.getMessage());
                        }
                        // 保存文件
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("continueId", continueId);
                        postImage(map, filebeans, "jfs/ecssp/mobile/eventContinueCtr/continueFileSave");
                    }
                } catch (ClientProtocolException e) {
                    message.what = MESSAGETYPE_02;
                    Log.e("stark", e.getMessage());
                } catch (IOException e) {
                    message.what = MESSAGETYPE_02;
                    Log.e("stark", e.getMessage());
                }
                saveHandler.sendMessage(message);
            }
        }).start();
    }

    /** saveHandler */
    private Handler saveHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGETYPE_01:// 文本保存成功 跳转至事件列表 并提交图片资源
                    toast("保存成功", 1);
                    Intent intent = new Intent();
                    intent.putExtra("ifSave", 1);
                    setResult(1, intent);
                    finish();
                    break;
                case MESSAGETYPE_02:
                    toast("请重新上报事件！", 1);
                    break;
                default:
                    break;
            }
        };
    };

    /**
     * 
     * 新旧事件下拉组件
     * 
     * @author Stark Zhou
     * @created 2015-12-30 下午2:44:51
     * @param view
     */
    public void continueType(View view) {

        final String[] strs = typeValueKeyMap.keySet().toArray(new String[typeValueKeyMap.size()]);
        Dialog dialog = new AlertDialog.Builder(ContinueAddActivity.this).setIcon(R.drawable.qq_dialog_default_icon)
                .setTitle("请选择新旧事件").setItems(strs, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        continueType.setText(strs[which]);
                        // 旧事件显示事件的发生时间和发生区域
                        if (strs[which].equals("旧事件")) {
                            happenAreaBlock.setVisibility(View.GONE);
                            happenDateBlock.setVisibility(View.GONE);
                            happenAddrBlock.setVisibility(View.GONE);
                        } else {
                            happenAreaBlock.setVisibility(View.VISIBLE);
                            happenDateBlock.setVisibility(View.VISIBLE);
                            happenAddrBlock.setVisibility(View.VISIBLE);
                        }
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }

    /**
     * 
     * 指标下拉组件
     * 
     * @author Stark Zhou
     * @created 2015-12-30 下午2:45:12
     * @param view
     */

    public void initDialog(View view) {
        if (monitors.size() == 0) {
            toast("此事件暂无指标", 0);
        } else {
            final View dialogView = LayoutInflater.from(ContinueAddActivity.this).inflate(R.layout.monitor_add_dialog,
                    null);
            monitorValue = (EditText) dialogView.findViewById(R.id.monitor_value);
            monitorSpin = (Spinner) dialogView.findViewById(R.id.monitor_spinner);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                    spinItems);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            monitorSpin.setAdapter(adapter);
            monitorSpin.setOnItemSelectedListener(new OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                    monitorType = (String) monitorSpin.getSelectedItem();
                    monitorValue.setText(monitors.get(position).get("indivalue").toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    monitorValue.setText(monitors.get(0).get("indivalue").toString());

                }
            });

            Dialog monitorDia = new AlertDialog.Builder(ContinueAddActivity.this)
                    .setIcon(R.drawable.qq_dialog_default_icon).setTitle("事件指标").setView(dialogView)
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            // 把对应的指标值写入map中
                            if (monitorValue.getText() != null && monitorValue.getText().toString().equals("") == false) {
                                monitorMap.put(monitorType.toString(), monitorValue.getText().toString());
                            } else {
                                monitorMap.put(monitorType.toString(), "--");
                            }
                            mapToStr();

                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            arg0.dismiss();
                        }
                    }).create();
            monitorDia.show();
        }
    }

    /**
     * 
     * 重写返回
     * 
     * @author Stark Zhou
     * @created 2016-1-4 下午2:30:00
     * @param view
     */
    public void addBack(View view) {
        Intent intent = new Intent();
        setResult(4, intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent intent = new Intent();
            setResult(4, intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 
     * hashmap转为string
     * 
     * @author Stark Zhou
     * @created 2015-12-30 下午2:45:41
     */
    private void mapToStr() {
        Iterator it = monitorMap.entrySet().iterator();
        StringBuilder myStr = new StringBuilder();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();

            myStr.append(key);
            myStr.append("  :  ");
            myStr.append(value);
            myStr.append("\n");
        }
        continueMonitor.setText(myStr);
    }

    /**
     * 
     * 部门树
     * 
     * @author Stark Zhou
     * @created 2015-12-30 下午2:59:29
     * @param view
     */
    public void continueDept(View view) {
        // Intent intent =new Intent(instance, PersonSearchActivity.class);
        Intent intent = new Intent(instance, AsyDeptTreeActivity.class);
        startActivityForResult(intent, R.layout.dept_asytree_at);
    }

    /**
     * 
     * 区域树
     * 
     * @author Stark Zhou
     * @created 2015-12-30 下午2:59:40
     * @param view
     */
    public void continueArea(View view) {
        Intent intent = new Intent(instance, AsyAreaTreeActivity.class);
        startActivityForResult(intent, R.layout.area_asytree_at);
    }

    /**
     * 校验表单
     */
    private Boolean noChecked() {
        if (continueName.getText().toString().trim().length() == 0) {
            DialogToastNoCall("请输入续报名称！");
            return true;
        }

        if (continueType.getText().toString().equals("新事件")) {
            if (continueArea.getText().toString().trim().length() == 0) {
                DialogToastNoCall("请输入所属区域！");
                return true;
            }
            if (happenDate.getText().toString().trim().length() == 0) {
                DialogToastNoCall("请输入事发时间！");
                return true;
            }

        }
        if (continueType.getText().toString().trim().length() == 0) {
            DialogToastNoCall("请输入续报类型！");
            return true;
        }
        if (reportDate.getText().toString().trim().length() == 0) {
            DialogToastNoCall("请输入续报时间！");
            return true;
        }

        if (continueDept.getText().toString().trim().length() == 0) {
            DialogToastNoCall("请输入续报单位！");
            return true;
        }

        if (continueContent.getText().toString().trim().length() == 0) {
            DialogToastNoCall("请输入续报内容！");
            return true;
        }
        return false;
    }

}
