/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package net.evecom.androidecssp.activity.taskresponse;

import java.util.Calendar;
import java.util.HashMap;

import net.evecom.androidecssp.R;
import net.evecom.androidecssp.base.BaseActivity;
import net.evecom.androidecssp.base.BaseModel;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 
 * ÃèÊö TaskInfoActivity
 * 
 * @author Mars zhang
 * @created 2015-11-23 ÏÂÎç2:21:54
 */
public class TaskInfoActivity extends BaseActivity {
    /** MemberVariables */
    private EditText tasknameeditText; // taskadd_taskname_et
    /** MemberVariables */
    private TextView contenteditText; // taskadd_taskcontern_et
    /** MemberVariables */
    private TextView remarkeditText; // taskadd_remark_et
    /** MemberVariables */
    private TextView keywordeditText; // taskadd_keyword_et
    /** MemberVariables */
    private TextView stateView; // taskadd_state_tv
    /** MemberVariables */
    private TextView taskeffiView; // taskadd_taskeffi_tv
    /** MemberVariables */
    private TextView taskpersonidView; // taskadd_taskpersonid_tv
    /** MemberVariables */
    private RelativeLayout taskeffilinearLayout; // taskadd_taskeffi_ll
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
    public boolean istimePicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_info_activity);
        Intent intent = getIntent();
        
        eventInfo = (BaseModel) getData("eventInfo", intent);
        projectInfo = (BaseModel) getData("projectInfo", intent);
        taskInfo = (BaseModel) getData("taskInfo", intent); ;
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
        tasknameeditText = (EditText) findViewById(R.id.taskadd_taskname_et);
        contenteditText = (TextView) findViewById(R.id.taskadd_taskcontern_et);
        remarkeditText = (TextView) findViewById(R.id.taskadd_remark_et);
        keywordeditText = (TextView) findViewById(R.id.taskadd_keyword_et);
        
        stateView = (TextView) findViewById(R.id.taskadd_state_tv);
        taskeffiView = (TextView) findViewById(R.id.taskadd_taskeffi_tv);
        taskpersonidView = (TextView) findViewById(R.id.taskadd_taskpersonid_tv);
    }
    
    /**
     * 
     * ÃèÊö
     * 
     * @author Mars zhang
     * @created 2015-11-25 ÏÂÎç2:06:20
     */
    private void initdata() {
        tasknameeditText.setText(ifnull(taskInfo.get("taskname")+"", ""));
        contenteditText.setText(ifnull(taskInfo.get("taskcontern")+"", ""));
        remarkeditText.setText(ifnull(taskInfo.get("remark")+"", ""));
        keywordeditText.setText(ifnull(taskInfo.get("keyword")+"", ""));
        taskeffiView.setText(ifnull(taskInfo.get("taskeffi")+"", ""));
        taskpersonidView.setText(ifnull(taskInfo.get("taskperson")+"", "")); 
        setDictNameByValueToView("PLAN_EVENT_TASK_STATE", taskInfo.get("state")+"", stateView);
    }
    
    /**
     * 
     * ÃèÊö ±£´æ
     * 
     * @author Mars zhang
     * @created 2015-11-13 ÏÂÎç2:25:34
     * @param view
     */
    public void fk(View view) {
        Intent intent = new Intent(getApplicationContext(), TaskResponseAddActivity.class);
        TaskResponseAddActivity.pushData("eventInfo", eventInfo, intent);
        TaskResponseAddActivity.pushData("projectInfo", projectInfo, intent);
        TaskResponseAddActivity.pushData("taskInfo", taskInfo, intent);
        startActivity(intent);
    }
    
    /**
     * 
     * ÃèÊö ·´À¡ÀúÊ·
     * @author Mars zhang
     * @created 2015-11-27 ÏÂÎç2:26:21
     * @param view
     */
    public  void fkhistroy(View view){
        Intent intent = new Intent(instance, ResponseListActivity.class);
        ResponseListActivity.pushData("eventInfo", eventInfo, intent);
        ResponseListActivity.pushData("projectInfo", projectInfo, intent);
        ResponseListActivity.pushData("taskInfo", taskInfo, intent);
        startActivity(intent);
    }
}
