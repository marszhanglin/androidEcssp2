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
import net.tsz.afinal.http.AjaxCallBack;

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
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 
 * 描述 添加任务反馈
 * 
 * @author Mars zhang
 * @created 2015-11-12 上午10:15:43
 */
public class TaskResponseAddActivity extends BaseActivity {
    /** MemberVariables */
    private EditText titleeditText;
    /** MemberVariables */
    private EditText contenteditText;
    /** MemberVariables */
    // private EditText keywordeditText;
    /** MemberVariables */
    // private EditText peopleeditText;
    /** MemberVariables */
    private EditText remarkeditText;
    /** MemberVariables */
    private TextView leveView;
    /** MemberVariables */
    private TextView stateView;
    // /** MemberVariables */
    // private BaseModel eventInfo;
    // /** MemberVariables */
    // private BaseModel projectInfo;
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
    /** MemberVariables */
    private HashMap<String, String> taskstateKeyMap = new HashMap<String, String>();
    /** lineProgressBar */
    public ProgressBar lineProgressBar;
    /** 是否正在提交数据 */
    private Boolean issubmiting=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_response_add_activity);
        Intent intent = getIntent();
        // eventInfo = (BaseModel) getData("eventInfo", intent);
        // projectInfo = (BaseModel) getData("projectInfo", intent);
        taskInfo = (BaseModel) getData("taskInfo", intent);
        init();
        initdata();
    }

    /**
     * 
     * 描述
     * 
     * @author Mars zhang
     * @created 2015-11-25 下午2:07:42
     */
    private void init() {
        titleeditText = (EditText) findViewById(R.id.task_response_title_et);
        contenteditText = (EditText) findViewById(R.id.task_response_content_et);
        // keywordeditText = (EditText)
        // findViewById(R.id.task_response_keyword_et);
        // peopleeditText = (EditText)
        // findViewById(R.id.task_response_people_et);
        remarkeditText = (EditText) findViewById(R.id.task_response_remark_et);

        leveView = (TextView) findViewById(R.id.task_response_leve_tv);
        stateView = (TextView) findViewById(R.id.task_response_state_tv);
        lineProgressBar = (ProgressBar) findViewById(R.id.http_progress_id);
        initGallery();

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

    /**
     * 
     * 描述
     * 
     * @author Mars zhang
     * @created 2015-11-25 下午2:07:52
     */
    private void initdata() {
        clearFilesRecord();
        getDict("PLAN_EVENTTASK_RESPONSE_LEVEL", levevalueKeyMap);
        getDict("PLAN_EVENT_TASK_STATE", taskstateKeyMap);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case R.layout.afnail_picture_activity: // 图片预览 在预览处不要有删除 耗性能

                break;
            case R.layout.picture_select_at: // 图片选择跳转
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
     * 保存提交
     */
    private void submit(View view) {
        if (titleeditText.getText().toString().trim().length() == 0) {
            errorAni(titleeditText);
            errorAni(view);
            dialogToastNoCall("请输入反馈标题!");
            return;
        }
        if (contenteditText.getText().toString().trim().length() == 0) {
            errorAni(contenteditText);
            errorAni(view);
            dialogToastNoCall("请输入反馈内容!");
            return;
        }

        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("planEventTaskResponse.eventid", taskInfo.get("eventid").toString());
        hashMap.put("planEventTaskResponse.responselevel", levevalueKeyMap.get(leveView.getText().toString()));
        hashMap.put("taskstate", taskstateKeyMap.get(stateView.getText().toString()));
        hashMap.put("planEventTaskResponse.responsetitle", titleeditText.getText().toString());
        hashMap.put("planEventTaskResponse.remark", remarkeditText.getText().toString());
        hashMap.put("planEventTaskResponse.responsedeptid", ShareUtil.getString(instance, "PASSNAME", "orgid", ""));
        hashMap.put("planEventTaskResponse.responsename", ShareUtil.getString(instance, "PASSNAME", "usernameCN", ""));
        hashMap.put("planEventTaskResponse.planid", taskInfo.get("planid").toString());
        hashMap.put("planEventTaskResponse.taskid", taskInfo.get("id").toString());
        hashMap.put("planEventTaskResponse.responsecon", contenteditText.getText().toString());
        hashMap.put("planEventTaskResponse.responseid", ShareUtil.getString(instance, "PASSNAME", "userid", ""));
        postdata(hashMap);
    }

    /**
     * 
     * 描述
     * 
     * @author Mars zhang
     * @created 2015-11-25 下午2:08:08
     * @param entity
     */
    private void postdata(final HashMap<String, String> entity) {
        if(issubmiting){
            toast("正在上传数据......", 1);
           return; 
        }
        issubmiting = true;
        HashMap<String, String> hashMap = new HashMap<String, String>();
        if (null != filebeans && filebeans.size() > 0) {
            postImage(hashMap, filebeans, "jfs/ecssp/mobile/taskresponseCtr/taskResponseFileSave",
                    new AjaxCallBack<String>() {
                        @Override
                        public void onLoading(long count, long current) {
                            int newProgress = Math.abs((int) (current / count)); 
                            if(newProgress>100) return ;
                            if (newProgress == 100) {
                                lineProgressBar.setVisibility(View.GONE);
                            } else {
                                if (lineProgressBar.getVisibility() == View.GONE)
                                    lineProgressBar.setVisibility(View.VISIBLE);
                                lineProgressBar.setProgress(newProgress);
                            }
                        }

                        @Override
                        public void onFailure(Throwable t, int errorNo, String strMsg) {
                            toast("文件上传失败，请重新上传", 1);
                            issubmiting = false;
                            super.onFailure(t, errorNo, strMsg);
                        }

                        @Override
                        public void onSuccess(String t) {
                            super.onSuccess(t); 
                            entity.put("planEventTaskResponse.detailattach", t);
                            postStrForm(entity);
                        }
                    });
        } else {
            postStrForm(entity);
        }
    }

    /**
     * 
     * 描述
     * 
     * @author Mars zhang
     * @created 2016-1-27 上午9:21:54
     */
    private void postStrForm(final HashMap<String, String> entity) {
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
                    /*String responseid = "";
                    try {
                        TaskResponseInfo taskResponseInfo = getTaskResponseInfo(saveResult);
                        if (null != taskResponseInfo) {
                            responseid = taskResponseInfo.getId();
                        }
                    } catch (JSONException e) {
                    }*/
                } else {
                    message.what = MESSAGETYPE_02;
                }
                saveHandler.sendMessage(message);
            }
        }).start();
    }

    /**
     * 
     * 描述
     * 
     * @author Mars zhang
     * @created 2015-11-25 下午2:08:08
     * @param entity
     */
    /*
     * private void postdata(final HashMap<String, String> entity) { new
     * Thread(new Runnable() {
     * 
     * @Override public void run() { Message message = new Message(); try {
     * saveResult =
     * connServerForResultPost("jfs/ecssp/mobile/taskresponseCtr/taskResponseAdd"
     * , entity); } catch (ClientProtocolException e) { message.what =
     * MESSAGETYPE_02; Log.e("mars", e.getMessage()); } catch (IOException e) {
     * message.what = MESSAGETYPE_02; Log.e("mars", e.getMessage()); } if
     * (saveResult.length() > 0) { message.what = MESSAGETYPE_01; String
     * responseid = ""; try { TaskResponseInfo taskResponseInfo =
     * getTaskResponseInfo(saveResult); if (null != taskResponseInfo) {
     * responseid = taskResponseInfo.getId(); } } catch (JSONException e) { }
     * HashMap<String, String> hashMap = new HashMap<String, String>();
     * hashMap.put("taskresponseId", responseid); postImage(hashMap, filebeans,
     * "jfs/ecssp/mobile/taskresponseCtr/taskResponseFileSave"); } else {
     * message.what = MESSAGETYPE_02; } saveHandler.sendMessage(message); }
     * }).start(); }
     */

    /**
     * 解析反馈json数据
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
     * 消息处理机制
     */
    private Handler saveHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGETYPE_01:// 文本保存成功 跳转至反馈列表 并提交图片资源
                    /*
                     * Intent intent = new Intent(getApplicationContext(),
                     * ResponseListActivity.class);
                     * ResponseListActivity.pushData("eventInfo", eventInfo,
                     * intent); ResponseListActivity.pushData("projectInfo",
                     * projectInfo, intent);
                     * ResponseListActivity.pushData("taskInfo", taskInfo,
                     * intent); startActivity(intent);
                     */
                    toast("反馈成功", 1);
                    finish();
                    break;
                case MESSAGETYPE_02:
                    toast("反馈失败,请重新保存反馈", 1);
                    issubmiting = false;
                    break;
                default:
                    break;
            }
        };
    };

    /**
     * 
     * 描述
     * 
     * @author Mars zhang
     * @created 2015-11-25 下午2:08:23
     * @param view
     */
    public void zyjb(View view) {
        final String[] strs = levevalueKeyMap.keySet().toArray(new String[levevalueKeyMap.size()]);
        Dialog dialog = new AlertDialog.Builder(TaskResponseAddActivity.this)
                .setIcon(R.drawable.qq_dialog_default_icon).setTitle("请选择级别")
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
     * 描述
     * 
     * @author Mars zhang
     * @created 2015-11-25 下午2:08:23
     * @param view
     */
    public void rwzt(View view) {
        final String[] strs = taskstateKeyMap.keySet().toArray(new String[taskstateKeyMap.size()]);
        Dialog dialog = new AlertDialog.Builder(TaskResponseAddActivity.this)
                .setIcon(R.drawable.qq_dialog_default_icon).setTitle("请选择任务状态")
                .setItems(strs, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        stateView.setText(strs[which]);
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }
    

    /**
     * 
     * 描述
     * 
     * @author Mars zhang
     * @created 2015-11-25 下午2:08:32
     * @param view
     */
    public void bc(View view) {
        submit(view);
    }

    /**
     * 
     * 描述 选择文件点击
     * 
     * @author Mars zhang
     * @created 2015-11-23 下午2:14:11
     * @param view
     */
    public void chooseimage(View view) {
        Intent intent = new Intent(instance, PictureSelectActivity.class);
        startActivityForResult(intent, R.layout.picture_select_at);
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
            displayImage(imageView, filebeans.get(position).getFileURL());
            imageView.setScaleType(ScaleType.FIT_XY);
            return imageView;
        }
    }
}
