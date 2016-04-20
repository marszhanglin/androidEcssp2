/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package net.evecom.androidecssp.activity.taskresponse;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;

import net.evecom.androidecssp.R;
import net.evecom.androidecssp.activity.pub.AsyDeptTreeActivity;
import net.evecom.androidecssp.activity.pub.PersonSearchActivity;
import net.evecom.androidecssp.activity.pub.TreeListActivity;
import net.evecom.androidecssp.base.BaseActivity;
import net.evecom.androidecssp.base.BaseModel;

import org.apache.http.client.ClientProtocolException;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

/**
 * 
 * 描述 添加任务
 * 
 * @author Mars zhang
 * @created 2015-11-12 上午10:15:43
 */
public class TaskAddActivity extends BaseActivity {
    /** MemberVariables */
    private EditText tasknameeditText; // taskadd_taskname_et
    /** MemberVariables */
    private EditText contenteditText; // taskadd_taskcontern_et
    /** MemberVariables */
    private EditText remarkeditText; // taskadd_remark_et
    /** MemberVariables */
    private EditText keywordeditText; // taskadd_keyword_et
    /** MemberVariables */
//    private TextView stateView; // taskadd_state_tv
    /** MemberVariables */
    private TextView taskeffiView; // taskadd_taskeffi_tv
    /** MemberVariables */
    private EditText taskpersonView; // taskadd_taskpersonid_tv
    /** MemberVariables */
    private EditText taskdeptView; // taskadd_taskpersonid_tv
    /** MemberVariables */
    private RelativeLayout taskeffilinearLayout; // taskadd_taskeffi_ll
    /** MemberVariables */
    private BaseModel eventInfo;
    /** MemberVariables */
    private BaseModel projectInfo;
    /** MemberVariables */
//    HashMap<String, String> statevalueKeyMap = new HashMap<String, String>();
    /** MemberVariables */
    HashMap<String, String> valueKeyMap = new HashMap<String, String>();
    /** MemberVariables */
    private String saveResult = "";
    /** MemberVariables */
    private Calendar calendar;
    /** MemberVariables */
    public boolean istimePicked = false;
    
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_add_activity);
        Intent intent = getIntent();
        eventInfo = (BaseModel) getData("eventInfo", intent);
        projectInfo = (BaseModel) getData("projectInfo", intent);
        init();
        initWatch();
    }



    /**
     * 
     * 描述
     * 
     * @author Mars zhang
     * @created 2015-11-25 下午2:06:15
     */
    private void init() {

        tasknameeditText = (EditText) findViewById(R.id.taskadd_taskname_et);
        contenteditText = (EditText) findViewById(R.id.taskadd_taskcontern_et);
        remarkeditText = (EditText) findViewById(R.id.taskadd_remark_et);
        keywordeditText = (EditText) findViewById(R.id.taskadd_keyword_et);

        taskeffiView = (TextView) findViewById(R.id.taskadd_taskeffi_tv);
        taskpersonView = (EditText) findViewById(R.id.taskadd_taskpersonid_ed);
        taskdeptView = (EditText) findViewById(R.id.taskadd_taskdept_et);
        taskeffilinearLayout = (RelativeLayout) findViewById(R.id.taskadd_taskeffi_ll);

        calendar = Calendar.getInstance();
        taskeffiView.setText(calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.MONTH) + "-"
                + calendar.get(Calendar.DAY_OF_MONTH) + " " + calendar.get(Calendar.HOUR_OF_DAY) + ":"
                + calendar.get(Calendar.MINUTE));

        taskeffilinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                new DatePickerDialog(instance, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker arg0, int year, int monthOfYear, int dayOfMonth) {
                        taskeffiView.setText("" + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth + " 00:00");
                        if (!istimePicked) {
                            istimePicked = true;
                            new TimePickerDialog(instance, new TimePickerDialog.OnTimeSetListener() {
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    String tasteffivalue = taskeffiView.getText().toString();
                                    String[] strs = tasteffivalue.split(" ");
                                    taskeffiView.setText("");
                                    taskeffiView.setText(strs[0] + " " + hourOfDay + ":" + minute);
                                    istimePicked = false;
                                }
                            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();

                        }
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });
    }


    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case R.layout.people_search_at: //人员选择跳转
                BaseModel personinfo = (BaseModel) getData("personinfo", data);
                if (null != personinfo) {
                    valueKeyMap.put("taskpersonid", personinfo.getStr("id")); 
                    taskpersonView.setText(personinfo.getStr("name"));
                }
                break;
            case  R.layout.tree_list_at: //机构选择选择跳转
                String id = data.getStringExtra("nodeid");
                String name = data.getStringExtra("nodeName");
               if(null!=id){
                 valueKeyMap.put("taskdeptid", id); 
                 taskdeptView.setText(name);
               } 
                break;
            case 2:  
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 保存提交
     */
    private void submit(View view) {
        // 校验
        if (tasknameeditText.getText().toString().trim().length() == 0) {
            errorAni(tasknameeditText);
            errorAni(view);
            dialogToast("请输入任务名称", null);
            return;
        }
        if (taskdeptView.getText().toString().trim().length() == 0) {
            errorAni((View) taskdeptView.getParent());
            errorAni(view);
            dialogToast("请选择或输入责任单位", null);
            return;
        }
        if (taskpersonView.getText().toString().trim().length() == 0) {
            errorAni((View) taskpersonView.getParent());
            errorAni(view);
            dialogToast("请选择或输入责任人", null);
            return;
        }
        if (taskeffiView.getText().toString().trim().length() == 0) {
            errorAni(taskeffiView);
            errorAni(view);
            dialogToast("请选择完成期限", null);
            return;
        }
        

        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("planEventTask.planId", projectInfo.get("planid") + "");
        hashMap.put("planEventTask.projectId", projectInfo.get("id") + "");
        hashMap.put("planEventTask.eventId", projectInfo.get("eventid") + "");
        hashMap.put("planEventTask.taskName", tasknameeditText.getText().toString());
        hashMap.put("planEventTask.taskEffi", taskeffiView.getText().toString());
        hashMap.put("planEventTask.taskPersonId", valueKeyMap.get("taskpersonid"));
        hashMap.put("planEventTask.taskPerson", taskpersonView.getText().toString());
        hashMap.put("planEventTask.taskContern", contenteditText.getText().toString());
        hashMap.put("planEventTask.remark", remarkeditText.getText().toString());
        hashMap.put("planEventTask.keyword", keywordeditText.getText().toString());
        hashMap.put("planEventTask.projecttopId", projectInfo.get("id") + "");
        
        hashMap.put("planEventTask.taskdeptid", valueKeyMap.get("taskdeptid"));
        hashMap.put("planEventTask.taskdept", taskdeptView.getText().toString());

        postdata(hashMap);
    }

    /**
     * 
     * 描述
     * 
     * @author Mars zhang
     * @created 2015-11-25 下午2:06:28
     * @param entity
     */
    private void postdata(final HashMap<String, String> entity) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                try {
                    saveResult = connServerForResultPost("jfs/ecssp/mobile/taskresponseCtr/taskadd", entity);
                } catch (ClientProtocolException e) {
                    message.what = MESSAGETYPE_02;
                    Log.e("mars", e.getMessage());
                } catch (IOException e) {
                    message.what = MESSAGETYPE_02;
                    Log.e("mars", e.getMessage());
                }
                if (saveResult.length() > 0) {
                    message.what = MESSAGETYPE_01;
                } else {
                    message.what = MESSAGETYPE_02;
                }
                Log.v("mars", saveResult);
                saveHandler.sendMessage(message);
            }
        }).start();

    }

    /**
     * 消息处理机制
     */
    private Handler saveHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGETYPE_01:// 文本保存成功 跳转至反馈列表 并提交图片资源 
                    Intent intent = new Intent(getApplicationContext(), TaskListActivity.class);
                    TaskListActivity.pushData("eventInfo", eventInfo, intent);
                    TaskListActivity.pushData("projectInfo", projectInfo, intent);
                    startActivity(intent);
                    finish();
                    break;
                case MESSAGETYPE_02:
                    toast("请重新保存任务", 1);
                    break;
                default:

                    break;
            }
        };
    };


    /**
     * 
     * 描述 复制人
     * 
     * @author Mars zhang
     * @created 2015-11-13 下午2:25:46
     * @param view
     */
    public void taskpersonid(View view) {
        Intent intent = new Intent(instance, PersonSearchActivity.class);
        // Intent intent =new Intent(instance, DeptSearchActivity.class);
        startActivityForResult(intent, R.layout.people_search_at);
    }

    /**
     * 
     * 描述 负责单位
     * 
     * @author Mars zhang
     * @created 2015-11-13 下午2:25:46
     * @param view
     */
    public void taskdeptid(View view) {
        Intent intent = new Intent(instance, TreeListActivity.class);
        BaseActivity.pushObjData("title", "组织机构", intent);
        BaseActivity.pushObjData("url", "jfs/ecssp/mobile/pubCtr/getAsyDeptTree", intent);
        BaseActivity.pushObjData("rootid", "root", intent);
        BaseActivity.pushObjData("rootname", "福建省政府", intent);
        startActivityForResult(intent, R.layout.tree_list_at);
        /*Intent intent = new Intent(instance, AsyDeptTreeActivity.class);
        startActivityForResult(intent, R.layout.dept_asytree_at);*/
    }
    
    /**
     * 
     * 描述 保存
     * 
     * @author Mars zhang
     * @created 2015-11-13 下午2:25:34
     * @param view
     */
    public void bc(View view) {
        submit(view);
    }
    
    
    /**
     * 
     * 描述  监听
     * @author Mars zhang
     * @created 2015-12-17 下午4:49:58
     */
    private void initWatch() {
        /*taskpersonView.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                
            } 
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                valueKeyMap.put("taskpersonid", "");
            } 
            @Override
            public void afterTextChanged(Editable arg0) {
                
            }
        });
        taskdeptView.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                
            } 
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                valueKeyMap.put("taskdeptid", "");
            } 
            @Override
            public void afterTextChanged(Editable arg0) {
                
            }
        });*/
    } 
}
