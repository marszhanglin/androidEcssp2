/*
 * Copyright (c) 2005, 2015, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package net.evecom.androidecssp.activity.contact;

import java.io.IOException;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import net.evecom.androidecssp.R;
import net.evecom.androidecssp.activity.pub.AsyDeptTreeActivity;
import net.evecom.androidecssp.activity.pub.DeptListActivity;
import net.evecom.androidecssp.activity.pub.DeptSearchActivity;
import net.evecom.androidecssp.activity.pub.TreeListActivity;
import net.evecom.androidecssp.activity.pub.WelcomeActivity;
import net.evecom.androidecssp.base.BaseActivity;
import net.evecom.androidecssp.base.BaseModel;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 
 * 移动用户注册
 * 
 * @author Stark Zhou
 * @created 2016-1-6 下午7:29:16
 */
public class UserRegistActivity extends BaseActivity {
    /** userName */
    private EditText userName;
    /** userLoginname */
    private EditText userLoginname;
    /** userSex */
    private TextView userSex;
    /** userPhone */
    private EditText userPhone;
    /** userIDcard */
    private EditText userIDcard;
    /** userEmail */
    private EditText userEmail;
    /** userDept */
    private TextView userDept;
    /** saveResult */
    private String saveResult = "";
    /** sexValueKeyMap */
    HashMap<String, String> sexValueKeyMap = new HashMap<String, String>();
    /** deptValueKeyMap */
    HashMap<String, String> deptValueKeyMap = new HashMap<String, String>();
    /** hashMap */
    HashMap<String, String> hashMap = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_regist);
        init();
        initData();
    }

    /**
     * 
     * 初始化界面
     * 
     * @author Stark Zhou
     * @created 2016-2-19 上午11:07:50
     */
    public void init() {
        userName = (EditText) findViewById(R.id.user_name_reg);
        userLoginname = (EditText) findViewById(R.id.user_loginname_reg);
        userSex = (TextView) findViewById(R.id.user_sex_reg);
        userPhone = (EditText) findViewById(R.id.user_phone_reg);
        userIDcard = (EditText) findViewById(R.id.user_idcard_reg);
        userEmail = (EditText) findViewById(R.id.user_email_reg);
        userDept = (TextView) findViewById(R.id.user_dept_reg);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case R.layout.tree_list_at:
                String deptid = data.getStringExtra("nodeid");
                String deptName = data.getStringExtra("nodeName");
                if (null != deptid) {
                    hashMap.put("userMobile.orgid", deptid);
                    userDept.setText(deptName);
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
     * 
     * 提交注册表单
     * 
     * @author Stark Zhou
     * @created 2016-2-19 上午11:08:33
     * @param v
     */
    public void registUser(View v) {
        if (noChecked()) {
            return;
        }

        hashMap.put("userMobile.name", userName.getText().toString());
        hashMap.put("userMobile.loginname", userLoginname.getText().toString());
        hashMap.put("userMobile.sex", sexValueKeyMap.get(userSex.getText().toString()));
        hashMap.put("userMobile.mobile", userPhone.getText().toString());
        hashMap.put("userMobile.idcard", userIDcard.getText().toString());
        hashMap.put("userMobile.email", userEmail.getText().toString());
        postdata(hashMap);
    }

    /**
     * 
     * 提交表单数据
     * 
     * @author Stark Zhou
     * @created 2016-2-19 上午11:08:49
     * @param entity
     */
    private void postdata(final HashMap<String, String> entity) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                try {
                    saveResult = connServerForResultPost("jfs/ecssp/mobile/userMobileCtr/userMobileAdd", entity);
                    String isPass = getObjInfo(saveResult).get("result");
                    if (isPass.length() > 0) {
                        message.what = MESSAGETYPE_01;
                    }
                } catch (ClientProtocolException e) {
                    message.what = MESSAGETYPE_02;
                } catch (IOException e) {
                    message.what = MESSAGETYPE_02;
                } catch (JSONException e) {
                    message.what = MESSAGETYPE_02;
                }
                saveHandler.sendMessage(message);
            }
        }).start();
    }

    /**
     * saveHandler
     */
    private Handler saveHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGETYPE_01:
                    Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                    toast("注册成功！", 1);
                    startActivity(intent);
                    finish();
                    break;
                case MESSAGETYPE_02:
                    toast("账号名已存在！请重新注册", 1);
                    break;
                default:
                    break;
            }
        };
    };

    /**
     * 
     * 初始化性别map
     * 
     * @author Stark Zhou
     * @created 2016-2-19 上午11:09:06
     */
    public void initData() {
        getDict("SYSTEM_SEX", sexValueKeyMap);
    }

    /**
     * 
     * 部门
     * 
     * @author Stark Zhou
     * @created 2016-2-19 上午11:09:25
     * @param view
     */
    public void dept(View view) {
        Intent intent = new Intent(instance, TreeListActivity.class);
        BaseActivity.pushObjData("title", "组织机构", intent);
        BaseActivity.pushObjData("url", "jfs/ecssp/mobile/pubCtr/getAsyDeptTree", intent);
        BaseActivity.pushObjData("rootid", "root", intent);
        BaseActivity.pushObjData("rootname", "福建省政府", intent);
        startActivityForResult(intent, R.layout.tree_list_at);
    }

    /**
     * 
     * 性别下拉框
     * 
     * @author Stark Zhou
     * @created 2016-2-19 上午11:07:29
     * @param view
     */
    public void sex(View view) {
        final String[] strs = sexValueKeyMap.keySet().toArray(new String[sexValueKeyMap.size()]);
        Dialog dialog = new AlertDialog.Builder(UserRegistActivity.this).setIcon(R.drawable.qq_dialog_default_icon)
                .setTitle("请选择性别").setItems(strs, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        userSex.setText(strs[which]);
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }

    /**
     * 
     * 表单验证
     * 
     * @author Stark Zhou
     * @created 2016-2-19 上午11:07:09
     * @return
     */
    private Boolean noChecked() {
        if (userName.getText().toString().trim().length() == 0) {
            dialogToastNoCall("请输入姓名！");
            return true;
        }
        if (userPhone.getText().toString().trim().length() == 0) {
            dialogToastNoCall("请输入手机号码！");
            return true;
        }
        if (userLoginname.getText().toString().trim().length() == 0) {
            dialogToastNoCall("请输入登录账号！");
            return true;
        }
        if (userDept.getText().toString().trim().length() == 0) {
            dialogToastNoCall("请输入所属机构！");
            return true;
        }
        return false;
    }

}
