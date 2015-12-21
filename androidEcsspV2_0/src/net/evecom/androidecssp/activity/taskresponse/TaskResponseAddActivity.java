/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package net.evecom.androidecssp.activity.taskresponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.evecom.androidecssp.R;
import net.evecom.androidecssp.activity.pub.imagescan.PictureSelectActivity;
import net.evecom.androidecssp.base.AfnailPictureActivity;
import net.evecom.androidecssp.base.BaseActivity;
import net.evecom.androidecssp.base.BaseModel;
import net.evecom.androidecssp.bean.FileManageBean;
import net.evecom.androidecssp.util.ShareUtil;
import net.evecom.androidecssp.view.gallery.GalleryFlow;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

/**
 * 
 * ���� ���������
 * 
 * @author Mars zhang
 * @created 2015-11-12 ����10:15:43
 */
public class TaskResponseAddActivity extends BaseActivity {
    /** MemberVariables */
    private EditText titleeditText;
    /** MemberVariables */
    private EditText contenteditText;
    /** MemberVariables */
    private EditText keywordeditText;
    /** MemberVariables */
//    private EditText peopleeditText;
    /** MemberVariables */
    private EditText remarkeditText;
    /** MemberVariables */
    private TextView leveView;
    /** MemberVariables */
    private BaseModel eventInfo;
    /** MemberVariables */
    private BaseModel projectInfo;
    /** MemberVariables */
    private BaseModel taskInfo;
    /** MemberVariables */
    private String[] levestr;
    /** MemberVariables */
    private String saveResult = "";
    /** MemberVariables */
    GalleryFlow mGallery = null;
    /** MemberVariables */
    private List<FileManageBean> filebeans = new ArrayList<FileManageBean>();
    /** MemberVariables */
    private HashMap<String, String> levevalueKeyMap = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_response_add_activity);
        Intent intent = getIntent();
        eventInfo = (BaseModel) getData("eventInfo", intent);
        projectInfo = (BaseModel) getData("projectInfo", intent);
        taskInfo = (BaseModel) getData("taskInfo", intent);
        init();
        initdata();
    }

    /**
     * 
     * ����
     * 
     * @author Mars zhang
     * @created 2015-11-25 ����2:07:42
     */
    private void init() {
        titleeditText = (EditText) findViewById(R.id.task_response_title_et);
        contenteditText = (EditText) findViewById(R.id.task_response_content_et);
        keywordeditText = (EditText) findViewById(R.id.task_response_keyword_et);
//        peopleeditText = (EditText) findViewById(R.id.task_response_people_et);
        remarkeditText = (EditText) findViewById(R.id.task_response_remark_et);

        leveView = (TextView) findViewById(R.id.task_response_leve_tv);
        initGallery();

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

    /**
     * 
     * ����
     * 
     * @author Mars zhang
     * @created 2015-11-25 ����2:07:52
     */
    private void initdata() {
        clearFilesRecord();
        getDict("PLAN_EVENTTASK_RESPONSE_LEVEL", levevalueKeyMap);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case R.layout.afnail_picture_activity: // ͼƬԤ�� ��Ԥ������Ҫ��ɾ�� ������

                break;
            case R.layout.picture_select_at: // ͼƬѡ����ת
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
     * �����ύ
     */
    private void submit(View view) {
        if (titleeditText.getText().toString().trim().length() == 0) {
            errorAni(titleeditText);
            errorAni(view);
            DialogToastNoCall("�����뷴������!");
            return;
        }
        if (contenteditText.getText().toString().trim().length() == 0) {
            errorAni(contenteditText);
            errorAni(view);
            DialogToastNoCall("�����뷴������!");
            return;
        } 

        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("planEventTaskResponse.eventid", eventInfo.get("id").toString());
        hashMap.put("planEventTaskResponse.responselevel",levevalueKeyMap.get(leveView.getText().toString()));
        hashMap.put("planEventTaskResponse.responsetitle", titleeditText.getText().toString());
        hashMap.put("planEventTaskResponse.remark", remarkeditText.getText().toString());
        hashMap.put("planEventTaskResponse.responsedeptid",
                ShareUtil.getString(instance, "PASSNAME", "orgid", ""));
        hashMap.put("planEventTaskResponse.responsename",
                ShareUtil.getString(instance, "PASSNAME", "usernameCN", ""));
        hashMap.put("planEventTaskResponse.planid", projectInfo.get("planid").toString());
        hashMap.put("planEventTaskResponse.taskid", taskInfo.get("id").toString());
        hashMap.put("planEventTaskResponse.responsecon", contenteditText.getText().toString());
        hashMap.put("planEventTaskResponse.responseid",
                ShareUtil.getString(instance, "PASSNAME", "userid", ""));
        postdata(hashMap);
    }

    /**
     * 
     * ����
     * 
     * @author Mars zhang
     * @created 2015-11-25 ����2:08:08
     * @param entity
     */
    private void postdata(final HashMap<String, String> entity) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                try {
                    saveResult = connServerForResultPost("jfs/ecssp/mobile/taskresponseCtr/taskResponseAdd", entity);
                } catch (ClientProtocolException e) {
                    message.what = MESSAGETYPE_02;
                    Log.e("mars", e.getMessage());
                } catch (IOException e) {
                    message.what = MESSAGETYPE_02;
                    Log.e("mars", e.getMessage());
                }
                if (saveResult.length() > 0) {
                    message.what = MESSAGETYPE_01;
                    String responseid = "";
                    try {
                        TaskResponseInfo taskResponseInfo = getTaskResponseInfo(saveResult);
                        if (null != taskResponseInfo) {
                            responseid = taskResponseInfo.getId();
                        }
                    } catch (JSONException e) {
                    }
                    HashMap<String, String> hashMap = new HashMap<String, String>();
                    hashMap.put("taskresponseId", responseid);
                     postImage(hashMap, filebeans,
                     "jfs/ecssp/mobile/taskresponseCtr/taskResponseFileSave");
                } else {
                    message.what = MESSAGETYPE_02;
                }
                saveHandler.sendMessage(message);
            }
        }).start(); 
    }

    /**
     * ��������json����
     * 
     */
    public static TaskResponseInfo getTaskResponseInfo(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);
        TaskResponseInfo taskResponseInfo = new TaskResponseInfo();
        taskResponseInfo.setId(jsonObject.getString("id"));
        taskResponseInfo.setResponsetitle("responsetitle");
        return taskResponseInfo;
    }

    /**
     * ��Ϣ�������
     */
    private Handler saveHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGETYPE_01:// �ı�����ɹ� ��ת�������б� ���ύͼƬ��Դ
                    Intent intent = new Intent(getApplicationContext(), ResponseListActivity.class);
                    ResponseListActivity.pushData("eventInfo", eventInfo, intent);
                    ResponseListActivity.pushData("projectInfo", projectInfo, intent);
                    ResponseListActivity.pushData("taskInfo", taskInfo, intent);
                    startActivity(intent);
                    finish();
                    break;
                case MESSAGETYPE_02:
                    toast("�����±��淴��", 1);
                    break;
                default:
                    break;
            }
        };
    };

    /**
     * 
     * ����
     * 
     * @author Mars zhang
     * @created 2015-11-25 ����2:08:23
     * @param view
     */
    public void zyjb(View view) {
        final String[] strs = levevalueKeyMap.keySet().toArray(new String[levevalueKeyMap.size()]);
        Dialog dialog = new AlertDialog.Builder(TaskResponseAddActivity.this)
                .setIcon(R.drawable.qq_dialog_default_icon).setTitle("��ѡ�񼶱�")
                .setItems(strs, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        leveView.setText(strs[which]);
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }

    /**
     * 
     * ����
     * 
     * @author Mars zhang
     * @created 2015-11-25 ����2:08:32
     * @param view
     */
    public void bc(View view) {
        submit(view);
    }

    /**
     * 
     * ���� ѡ���ļ����
     * 
     * @author Mars zhang
     * @created 2015-11-23 ����2:14:11
     * @param view
     */
    public void choose_image(View view) {
        Intent intent = new Intent(instance, PictureSelectActivity.class);
        startActivityForResult(intent, R.layout.picture_select_at);
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
            displayImage(imageView, filebeans.get(position).getFile_URL());
            imageView.setScaleType(ScaleType.FIT_XY);
            return imageView;
        }
    }
}
