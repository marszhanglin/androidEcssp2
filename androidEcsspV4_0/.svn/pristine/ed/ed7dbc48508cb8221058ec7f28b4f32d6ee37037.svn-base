/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package net.evecom.androidecssp.activity.resource;

import java.util.HashMap;

import net.evecom.androidecssp.R;
import net.evecom.androidecssp.base.BaseActivity;
import net.evecom.androidecssp.base.BaseModel;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * 
 * ÃèÊö TaskInfoActivity
 * 
 * @author Mars zhang
 * @created 2015-11-23 ÏÂÎç2:21:54
 */
public class ResourceInfoActivity extends BaseActivity {
    /** MemberVariables */
    private TextView resourcename; // resource_name_tv
    /** MemberVariables */
    private TextView resourcedept; // resource_dept_tv
    /** MemberVariables */
    private TextView resourcenum; // resource_num_tv
    /** MemberVariables */
    private TextView resourcetype; // resource_type_tv
    /** MemberVariables */
    private TextView resourcephone; // resource_phone_tv
    /** MemberVariables */
    private TextView resourcearea; // resource_area_tv
    /** MemberVariables */
    private TextView resourceaddr; // resource_addr_tv
    /** MemberVariables */
    private BaseModel resourceinfo;
    /** MemberVariables */
    HashMap<String, String> statevalueKeyMap = new HashMap<String, String>();
    /** MemberVariables */
    HashMap<String, String> valueKeyMap = new HashMap<String, String>();
    /** MemberVariables */
    public boolean istimePicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resource_info_activity);
        Intent intent = getIntent();
        resourceinfo = (BaseModel) getData("resourceinfo", intent); ;
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
        resourcename = (TextView) findViewById(R.id.resource_name_tv);
        resourcedept = (TextView) findViewById(R.id.resource_dept_tv);
        resourcenum = (TextView) findViewById(R.id.resource_num_tv);
        
        resourcetype = (TextView) findViewById(R.id.resource_type_tv);
        resourcephone = (TextView) findViewById(R.id.resource_phone_tv);
        resourcearea = (TextView) findViewById(R.id.resource_area_tv);
        resourceaddr = (TextView) findViewById(R.id.resource_addr_tv);
    }
    
    /**
     * 
     * ÃèÊö
     * 
     * @author Mars zhang
     * @created 2015-11-25 ÏÂÎç2:06:20
     */
    private void initdata() {
        resourcename.setText(ifnull(resourceinfo.get("name")+"", ""));
        resourcedept.setText(ifnull(resourceinfo.get("deptname")+"", ""));
        resourcenum.setText(ifnull(resourceinfo.get("num")+"", ""));
        resourcetype.setText(ifnull(resourceinfo.get("typename")+"", ""));
        resourcephone.setText(ifnull(resourceinfo.get("phone")+"", ""));
        resourcearea.setText(ifnull(resourceinfo.get("areaname")+"", "")); 
        resourceaddr.setText(ifnull(resourceinfo.get("address")+"", "")); 
//        setDictNameByValueToView("PLAN_EVENT_TASK_STATE", taskInfo.get("state")+"", stateView);
    }
 
}
