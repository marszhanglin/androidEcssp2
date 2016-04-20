/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package net.evecom.androidecssp.activity.taskresponse;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import net.evecom.androidecssp.R;
import net.evecom.androidecssp.base.BaseActivity;
import net.evecom.androidecssp.base.BaseModel;
import net.mutil.util.HttpUtil;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 
 * ÃèÊö TaskAndResponsesInfoActivity
 * 
 * @author Mars zhang
 * @created 2015-11-23 ÏÂÎç2:21:54
 */
public class TaskAndResponsesInfoActivity extends BaseActivity {
    /** MemberVariables */
    private TextView taskdeptTV; // task_response_info_dept
    /** MemberVariables */
    private TextView taskenddateTV; // task_response_info_enddate
    /** MemberVariables */
    private TextView tasknameTV; // task_response_info_taskname
    /** MemberVariables */
    private ImageView stateImageView; // task_response_info_status_img
    /** MemberVariables */
    private TextView taskcontentTV; // task_response_info_content
    /** MemberVariables */
    private TextView taskpersonidTV; // task_response_info_person
    /** MemberVariables */
    private TextView createtimeTV; // task_response_info_createtime
    /** MemberVariables */
    private LinearLayout responseLayout; // task_response_info_response_ly
    /** MemberVariables */
    private BaseModel eventInfo;
    /** MemberVariables */
    private BaseModel projectInfo;
    /** MemberVariables */
    private BaseModel taskInfo;
    /** MemberVariables */
    HashMap<String, String> statevalueKeyMap = new HashMap<String, String>();
    /** MemberVariables */
    HashMap<String, String> valueKeyMap = new HashMap<String, String>();
    /** MemberVariables */
    private String saveResult = "";
    /** MemberVariables */
    private Calendar calendar;
    /** MemberVariables */
    private boolean istimePicked = false;
    /** MemberVariables */
    private String resPonsesresutArray = "";
    /** MemberVariables */
    private List<BaseModel> taskResponseInfos = null;
    /** MemberVariables */
    private static boolean ifloadresponselist = true;
    /** MemberVariables */
    private Handler responseListHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGETYPE_01:
                    loadResponses();
                    break;
                case MESSAGETYPE_02:

                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_response_info_activity);
        Intent intent = getIntent();

        eventInfo = (BaseModel) getData("eventInfo", intent);
        projectInfo = (BaseModel) getData("projectInfo", intent);
        taskInfo = (BaseModel) getData("taskInfo", intent);
        init();
        initdata();
    }

    /**
     * 
     * ÃèÊö
     * 
     * @author Mars zhang
     * @created 2015-11-25 ÏÂÎç2:06:15
     */
    private void init() {

        taskdeptTV = (TextView) findViewById(R.id.task_response_info_dept);
        taskenddateTV = (TextView) findViewById(R.id.task_response_info_enddate);
        tasknameTV = (TextView) findViewById(R.id.task_response_info_taskname);
        stateImageView = (ImageView) findViewById(R.id.task_response_info_status_img);// state
        taskcontentTV = (TextView) findViewById(R.id.task_response_info_content);
        taskpersonidTV = (TextView) findViewById(R.id.task_response_info_person);
        createtimeTV = (TextView) findViewById(R.id.task_response_info_createtime);
        responseLayout = (LinearLayout) findViewById(R.id.task_response_info_response_ly);

    }

    /**
     * 
     * ÃèÊö
     * 
     * @author Mars zhang
     * @created 2015-11-25 ÏÂÎç2:06:20
     */
    private void initdata() {
        taskdeptTV.setText(ifnull(taskInfo.get("taskdept") + "", ""));
        taskenddateTV.setText(ifnull(taskInfo.get("taskeffi") + "", ""));
        tasknameTV.setText(ifnull(taskInfo.get("taskname") + "", ""));
        taskcontentTV.setText(ifnull(taskInfo.get("taskcontern") + "", ""));
        taskpersonidTV.setText(ifnull(taskInfo.get("taskperson") + "", ""));
        createtimeTV.setText(ifnull(taskInfo.get("createtime") + "", ""));
        String statevalue = ifnull(taskInfo.get("state"), "");
        if (statevalue.length() > 0) {
            stateImageView.setVisibility(View.VISIBLE);
            if (statevalue.equals("1")) {
            } else if (statevalue.equals("2")) {
                stateImageView.setBackgroundResource(R.drawable.task_status_ing);
            } else if (statevalue.equals("3")) {
                stateImageView.setBackgroundResource(R.drawable.task_status_nostart);
            } else {
                stateImageView.setVisibility(View.INVISIBLE);
            }
        }

        // setDictNameByValueToView("PLAN_EVENT_TASK_STATE",
        // taskInfo.get("state")+"", stateView);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ifloadresponselist) {
            ifloadresponselist = false;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Message message = new Message();
                    HashMap<String, String> hashMap = new HashMap<String, String>();
                    hashMap.put("taskId", taskInfo.get("id").toString());
                    try {
                        resPonsesresutArray = connServerForResultPost(
                                "jfs/ecssp/mobile/taskresponseCtr/getTaskResponseByTaskId", hashMap);
                    } catch (ClientProtocolException e) {
                        message.what = MESSAGETYPE_02;
                        Log.e("mars", e.getMessage());
                    } catch (IOException e) {
                        message.what = MESSAGETYPE_02;
                        Log.e("mars", e.getMessage());
                    }
                    if (resPonsesresutArray.length() > 0) {
                        try {
                            taskResponseInfos = getObjsInfo(resPonsesresutArray);
                            if (null == taskResponseInfos) {
                                message.what = MESSAGETYPE_02;
                            } else {
                                message.what = MESSAGETYPE_01;
                            }
                        } catch (JSONException e) {
                            message.what = MESSAGETYPE_02;
                            Log.e("mars", e.getMessage());
                        }
                    } else {
                        message.what = MESSAGETYPE_02;
                    }
                    Log.v("mars", resPonsesresutArray);
                    responseListHandler.sendMessage(message);

                }
            }).start();
        }
    }

    @Override
    public void finish() {
        ifloadresponselist = true;
        super.finish();

    }

    /**
     * 
     * ÃèÊö
     * 
     * @author Mars zhang
     * @created 2015-12-29 ÏÂÎç7:52:06
     */
    private void loadResponses() {
        responseLayout.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(instance);
        for (int i = 0; i < taskResponseInfos.size(); i++) {
            final BaseModel baseModel = taskResponseInfos.get(i);
            View view = inflater.inflate(R.layout.task_response_response_item, null, true);
            responseLayout.addView(view);
            // LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(
            // LinearLayout.LayoutParams.WRAP_CONTENT,
            // LinearLayout.LayoutParams.WRAP_CONTENT);
            // lp.setMargins(0, 0, 0, 10);
            // view.setLayoutParams(lp);
            TextView dept = (TextView) view.findViewById(R.id.task_response_info_response_dept);
            TextView createtime = (TextView) view.findViewById(R.id.task_response_info_response_createtime);
            TextView name = (TextView) view.findViewById(R.id.task_response_info_response_name);
            TextView context = (TextView) view.findViewById(R.id.task_response_info_response_content);
            LinearLayout layout = (LinearLayout) view.findViewById(R.id.task_response_info_response_imagely);

            dept.setText(ifnull(baseModel.get("name"), ""));
            createtime.setText(ifnull(baseModel.get("createtime"), ""));
            name.setText(ifnull(baseModel.get("responsetitle"), ""));
            context.setText(ifnull(baseModel.get("responsecon"), ""));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), TaskResponseInfoActivity.class);
                    pushData("responseinfo", baseModel, intent);
                    startActivity(intent);
                }
            });

            String imageids = ifnull(baseModel.get("detailattach"), "");
            if (imageids.length() > 0) {
                String[] imagesplits = imageids.split(",");
                for (int j = 0; j < imagesplits.length && j < 6; j++) {
                    View imgview = inflater.inflate(R.layout.task_response_response_item_image, null, true);
                    layout.addView(imgview);
                    // LinearLayout.LayoutParams imagelp=new
                    // LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    // LinearLayout.LayoutParams.WRAP_CONTENT);
                    // lp.setMargins(2, 2, 2, 2);
                    // imgview.setLayoutParams(imagelp);
                    ImageView imageView = (ImageView) imgview.findViewById(R.id.task_response_rsponse_item_iamge);
                    HashMap<String, String> mparas = new HashMap<String, String>();
                    mparas.put("fileid", imagesplits[j]);
                    displayImageWithWidthHeight(imageView, HttpUtil.getPCURL()
                            + "jfs/ecssp/mobile/pubCtr/getImageFlowById", mparas, 60, 60);
                }
            }

        }

    };

    /**
     * 
     * ÃèÊö ±£´æ
     * 
     * @author Mars zhang
     * @created 2015-11-13 ÏÂÎç2:25:34
     * @param view
     */
    public void fk(View view) {
        ifloadresponselist = true;
        Intent intent = new Intent(getApplicationContext(), TaskResponseAddActivity.class);
        TaskResponseAddActivity.pushData("eventInfo", eventInfo, intent);
        TaskResponseAddActivity.pushData("projectInfo", projectInfo, intent);
        TaskResponseAddActivity.pushData("taskInfo", taskInfo, intent);
        startActivity(intent);
    }

    /**
     * 
     * ÃèÊö ·´À¡ÀúÊ·
     * 
     * @author Mars zhang
     * @created 2015-11-27 ÏÂÎç2:26:21
     * @param view
     */
    public void fkhistroy(View view) {
        Intent intent = new Intent(instance, ResponseListActivity.class);
        ResponseListActivity.pushData("eventInfo", eventInfo, intent);
        ResponseListActivity.pushData("projectInfo", projectInfo, intent);
        ResponseListActivity.pushData("taskInfo", taskInfo, intent);
        startActivity(intent);
    }
}
