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
import net.evecom.androidecssp.activity.pub.TreeListActivity;
import net.evecom.androidecssp.activity.pub.AsyAreaTreeActivity;
import net.evecom.androidecssp.activity.pub.AsyDeptTreeActivity;
import net.evecom.androidecssp.activity.pub.DeptListActivity;
import net.evecom.androidecssp.activity.pub.imagescan.PictureSelectActivity;
import net.evecom.androidecssp.base.AfnailPictureActivity;
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
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
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
 * ����
 * 
 * @author Stark Zhou
 * @created 2015-12-30 ����3:59:38
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
     * �ļ�ѡ��
     * 
     * @author Stark Zhou
     * @created 2016-1-4 ����10:43:56
     * @param view
     */
    public void chooseImage(View view) {
        Intent intent = new Intent(instance, PictureSelectActivity.class);
        startActivityForResult(intent, R.layout.picture_select_at);
    }

    /**
     * 
     * ��ʼ��ָ��
     * 
     * @author Stark Zhou
     * @created 2015-12-30 ����2:44:10
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
     * ��ʼ��
     * 
     * @author Stark Zhou
     * @created 2015-12-30 ����2:44:26
     */
    private void init() {
        initGallery();
        clearLocation();
        clearFilesRecord();
        calendar = Calendar.getInstance();
        typeValueKeyMap.put("���¼�", "1");
        typeValueKeyMap.put("���¼�", "2");
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
        continueType.setText("���¼�");
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

        // ʱ��ѡ����
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
    /**
     * 
     * ����ļ�
     *
     * @author Stark Zhou
     * @created 2016-4-5 ����11:06:31
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
     * ����
     *
     * @author Stark Zhou
     * @created 2016-4-5 ����11:03:31
     */
    private void clearLocation() {
        SharedPreferences sp = getApplicationContext().getSharedPreferences("GPS", MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("eventlatitude", "" );
        editor.putString("eventlongitude", "");
        editor.commit();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
        // ����ѡ�������ؽ��
            case R.layout.tree_list_at:
                
                String id = data.getStringExtra("nodeid");
                String name = data.getStringExtra("nodeName");
                String title = data.getStringExtra("title");
                if (null != id) {
                    if(title.equals("��֯����")){
                    hashMap.put("infoContinue.reporterdept", id);
                    hashMap.put("infoContinue.reporterdeptname", name);
                    continueDept.setText(name);
                    }else{
                        hashMap.put("infoContinue.coutinuearea", id);
                        continueArea.setText(name);
                    }
                }
                break;
            // �ļ�ѡ�񷵻ؽ��
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
     * ����pubHandler
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
     * ˢ�»���
     * 
     * @author Stark Zhou
     * @created 2016-1-4 ����11:21:29
     */
    private void updateGallery() {
        ((GalleryAdapter) mGallery.getAdapter()).notifyDataSetChanged();
    }
    /**
     * 
     * ��ʼ������
     *
     * @author Stark Zhou
     * @created 2016-4-5 ����10:27:57
     */
    private void initGallery() {
        mGallery = (GalleryFlow) findViewById(R.id.continue_add_gallery_flow);
        mGallery.setBackgroundColor(Color.parseColor("#ffffff")); // ����ɫ
        mGallery.setSpacing(90);// ���
        mGallery.setMaxRotationAngle(20);// ������б�Ƕ�
        mGallery.setFadingEdgeLength(10); // �߿򽥱����
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
     * gallery������
     * 
     * @author Stark Zhou
     * @created 2016-1-4 ����11:21:54
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
            displayImage(imageView, filebeans.get(position).getFileURL());
            imageView.setScaleType(ScaleType.FIT_XY);
            return imageView;
        }
    }

    /**
     * �ύ����
     */
    public void submit(View view) {
        if (noChecked()) {
            return;
        }
        hashMap.put("eventid", eventInfo.get("id").toString());
        hashMap.put("infoContinue.eventid", eventInfo.get("id").toString());
        hashMap.put("infoContinue.coutinuetype", typeValueKeyMap.get(continueType.getText().toString()));
        hashMap.put("infoContinue.coutinueName", continueName.getText().toString());

        if (continueType.getText().toString().equals("���¼�")) {
            hashMap.put("infoContinue.happenaddress", continueAddr.getText().toString());
            hashMap.put("infoContinue.gisy", ShareUtil.getString(getApplicationContext(), "GPS", "eventlatitude", ""));
            hashMap.put("infoContinue.gisx", ShareUtil.getString(getApplicationContext(), "GPS", "eventlongitude", ""));
            hashMap.put("infoContinue.happendate", happenDate.getText() + " " + happenTime.getText() + ":00");
        }

        hashMap.put("infoContinue.reporterperson", continueReporter.getText().toString());
        hashMap.put("infoContinue.reportertel", continueTel.getText().toString());
        hashMap.put("infoContinue.coutinuecontent", continueContent.getText().toString());
        hashMap.put("infoContinue.takensteps", continueStep.getText().toString());
        hashMap.put("infoContinue.remark", continueRemark.getText().toString());
        String reportDt = reportDate.getText().toString() + " " + reportTime.getText().toString() + ":00";
        hashMap.put("infoContinue.reporterdate", reportDt);
        // ����ָ��ֵ
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
     * �ύ��������
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
                        // �����ļ�
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
                case MESSAGETYPE_01:// �ı�����ɹ� ��ת���¼��б� ���ύͼƬ��Դ
                    toast("����ɹ�", 1);
                    Intent intent = new Intent();
                    intent.putExtra("isSave", true);
                    setResult(1, intent);
                    finish();
                    break;
                case MESSAGETYPE_02:
                    toast("�������ϱ��¼���", 1);
                    break;
                default:
                    break;
            }
        };
    };

    /**
     * 
     * �¾��¼��������
     * 
     * @author Stark Zhou
     * @created 2015-12-30 ����2:44:51
     * @param view
     */
    public void continueType(View view) {

        final String[] strs = typeValueKeyMap.keySet().toArray(new String[typeValueKeyMap.size()]);
        Dialog dialog = new AlertDialog.Builder(ContinueAddActivity.this).setIcon(R.drawable.qq_dialog_default_icon)
                .setTitle("��ѡ���¾��¼�").setItems(strs, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        continueType.setText(strs[which]);
                        // ���¼���ʾ�¼��ķ���ʱ��ͷ�������
                        if (strs[which].equals("���¼�")) {
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
     * ָ���������
     * 
     * @author Stark Zhou
     * @created 2015-12-30 ����2:45:12
     * @param view
     */

    public void initDialog(View view) {
        if (monitors.size() == 0) {
            toast("���¼�����ָ��", 0);
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
                    .setIcon(R.drawable.qq_dialog_default_icon).setTitle("�¼�ָ��").setView(dialogView)
                    .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            // �Ѷ�Ӧ��ָ��ֵд��map��
                            if (monitorValue.getText() != null &&
                                    monitorValue.getText().toString().equals("") == false) {
                                monitorMap.put(monitorType.toString(), monitorValue.getText().toString());
                            } else {
                                monitorMap.put(monitorType.toString(), "--");
                            }
                            mapToStr();

                        }
                    }).setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
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
     * ��д����
     * 
     * @author Stark Zhou
     * @created 2016-1-4 ����2:30:00
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
     * hashmapתΪstring
     * 
     * @author Stark Zhou
     * @created 2015-12-30 ����2:45:41
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
     * ������
     * 
     * @author Stark Zhou
     * @created 2015-12-30 ����2:59:29
     * @param view
     */
    public void continueDept(View view) {
        // Intent intent =new Intent(instance, PersonSearchActivity.class);
        Intent intent = new Intent(instance, TreeListActivity.class);
        BaseActivity.pushObjData("title", "��֯����", intent);
        BaseActivity.pushObjData("url", "jfs/ecssp/mobile/pubCtr/getAsyDeptTree", intent);
        BaseActivity.pushObjData("rootid", "root", intent);
        BaseActivity.pushObjData("rootname", "����ʡ����", intent);
        startActivityForResult(intent, R.layout.tree_list_at);
    }

    /**
     * 
     * ������
     * 
     * @author Stark Zhou
     * @created 2015-12-30 ����2:59:40
     * @param view
     */
    public void continueArea(View view) {
        Intent intent = new Intent(instance, TreeListActivity.class);
        BaseActivity.pushObjData("title", "��������", intent);
        BaseActivity.pushObjData("url", "jfs/ecssp/mobile/pubCtr/getAsyAreaTree", intent);
        BaseActivity.pushObjData("rootid", "350000", intent);
        BaseActivity.pushObjData("rootname", "����ʡ", intent);
        startActivityForResult(intent, R.layout.tree_list_at);
    }

    /**
     * У�����
     */
    private Boolean noChecked() {
        if (continueName.getText().toString().trim().length() == 0) {
            dialogToastNoCall("�������������ƣ�");
            return true;
        }

        if (continueType.getText().toString().equals("���¼�")) {
            if (continueArea.getText().toString().trim().length() == 0) {
                dialogToastNoCall("��������������");
                return true;
            }
            if (happenDate.getText().toString().trim().length() == 0) {
                dialogToastNoCall("�������·�ʱ�䣡");
                return true;
            }

        }
        if (continueType.getText().toString().trim().length() == 0) {
            dialogToastNoCall("�������������ͣ�");
            return true;
        }
        if (reportDate.getText().toString().trim().length() == 0) {
            dialogToastNoCall("����������ʱ�䣡");
            return true;
        }

        if (continueDept.getText().toString().trim().length() == 0) {
            dialogToastNoCall("������������λ��");
            return true;
        }

        if (continueContent.getText().toString().trim().length() == 0) {
            dialogToastNoCall("�������������ݣ�");
            return true;
        }
        return false;
    }

}