/*
 * Copyright (c) 2005, 2015, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package net.evecom.androidecssp.activity.contact;

import java.io.IOException;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import net.evecom.androidecssp.R;
import net.evecom.androidecssp.activity.pub.DeptSearchActivity;
import net.evecom.androidecssp.base.BaseActivity;
import net.evecom.androidecssp.base.BaseModel;

/**
 * 新增联系人
 * 
 * @author Stark Zhou
 * @created 2015-11-16 上午8:47:09
 */
public class ContactAddActivity extends BaseActivity {
    /** sexView */
    private TextView sexView;
    /** isdutyView */
    private TextView isdutyView;
    /** nameEditText */
    private EditText nameEditText;
    /** phoneEditText */
    private EditText phoneEditText;
    /** officetelEditText */
    private EditText officetelEditText;
    /** emailEditText */
    private EditText emailEditText;
    /** deptView */
    private TextView deptView;
    /** groupEditText */
    private TextView groupEditText;
    /** postEditText */
    private EditText postEditText;
    /** addrareaEditText */
    private EditText addrareaEditText;
    /** saveResult */
    private String saveResult = "";
    /** group */
    private BaseModel group = null;
    /** sexValueKeyMap */
    HashMap<String, String> sexValueKeyMap = new HashMap<String, String>();
    /** deptValueKeyMap */
    HashMap<String, String> deptValueKeyMap = new HashMap<String, String>();
    /** isDutyValueKeyMap */
    HashMap<String, String> isDutyValueKeyMap = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_add_at);
        Intent intent = getIntent();
        group = (BaseModel) getData("group", intent);
        init();
        initData();
    }

    /**
     * 初始化
     */
    private void init() {
        nameEditText = (EditText) findViewById(R.id.contact_name_add);
        phoneEditText = (EditText) findViewById(R.id.contact_phone_add);
        officetelEditText = (EditText) findViewById(R.id.contact_officetel_add);
        emailEditText = (EditText) findViewById(R.id.contact_email_add);
        sexView = (TextView) findViewById(R.id.contact_sex_add);
        deptView = (TextView) findViewById(R.id.contact_dept_add);
        isdutyView = (TextView) findViewById(R.id.contact_isduty_add);
        groupEditText = (TextView) findViewById(R.id.contact_group_add);
        postEditText = (EditText) findViewById(R.id.contact_post_add);
        addrareaEditText = (EditText) findViewById(R.id.contact_addrarea_add);
        groupEditText.setText(group.getStr("groupname"));
    }

    /**
     * 提交联系人
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case R.layout.dept_search_at:
                BaseModel deptInfo = (BaseModel) getData("deptinfo", data);
                deptValueKeyMap.put(deptInfo.getStr("name"), deptInfo.getStr("id"));
                deptValueKeyMap.put("deptid", deptInfo.getStr("id"));
                deptValueKeyMap.put("deptname", deptInfo.getStr("name"));
                deptView.setText(deptInfo.getStr("name"));
                break;
            case 2:
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 提交表单
     */
    public void submit(View view) {
        if (noChecked()) {
            return;
        }
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("contactModel.name", nameEditText.getText().toString());
        hashMap.put("contactModel.mobiletel", phoneEditText.getText().toString());
        hashMap.put("contactModel.officetel", officetelEditText.getText().toString());
        hashMap.put("contactModel.email", emailEditText.getText().toString());
        hashMap.put("contactModel.sex", sexValueKeyMap.get(sexView.getText().toString()));
        hashMap.put("contactModel.isduty", isDutyValueKeyMap.get(isdutyView.getText().toString()));
        hashMap.put("contactModel.deptid", deptValueKeyMap.get("deptid"));
        hashMap.put("contactModel.deptname", deptValueKeyMap.get("deptname"));
        hashMap.put("contactModel.groupid", group.getStr("id"));
        hashMap.put("contactModel.post", postEditText.getText().toString());
        hashMap.put("contactModel.addrarea", addrareaEditText.getText().toString());

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
                    saveResult = connServerForResultPost("jfs/ecssp/mobile/contactCtr/addContact", entity);
                    if (saveResult.length() > 0) {
                        message.what = MESSAGETYPE_01;
                    }
                } catch (ClientProtocolException e) {
                    message.what = MESSAGETYPE_02;
                    Log.e("stark", e.getMessage());
                } catch (IOException e) {
                    message.what = MESSAGETYPE_02;
                    Log.e("stark", e.getMessage());
                }
                /*
                 * if (saveResult.length() > 0) { message.what = MESSAGETYPE_01;
                 * String eventId = ""; try { BaseModel eventInfo =
                 * getObjInfo(saveResult); if (null != eventInfo) { eventId =
                 * eventInfo.get("id"); } } catch (JSONException e) {
                 * Log.e("mars", e.getMessage()); } HashMap<String, String>
                 * map=new HashMap<String, String>(); map.put("eventId",
                 * eventId);
                 * postImage(map,fileList,"jfs/ecssp/mobile/eventCtr/eventFileSave"
                 * ); } else { message.what = MESSAGETYPE_02; }
                 */
                saveHandler.sendMessage(message);
            }
        }).start();
    }

    /** saveHandler */
    private Handler saveHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGETYPE_01:// 文本保存成功 跳转至事件列表 并提交图片资源
                    Intent intent = new Intent(getApplicationContext(), ContactListActivity.class);
                    ContactListActivity.pushData("group", group, intent);
                    toast("保存成功", 1);
                    startActivity(intent);
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
     * 初始化性别和是否的数据字典
     */
    private void initData() {
        getDict("SYSTEM_SEX", sexValueKeyMap);
        getDict("SYSTEM_YES_NO", isDutyValueKeyMap);
    }

    /**
     * 
     * 性别下拉框
     * 
     * @author Stark Zhou
     * @created 2015-11-13 下午4:58:19
     * @param view
     */
    public void sex(View view) {
        final String[] strs = sexValueKeyMap.keySet().toArray(new String[sexValueKeyMap.size()]);
        Dialog dialog = new AlertDialog.Builder(ContactAddActivity.this).setIcon(R.drawable.qq_dialog_default_icon)
                .setTitle("请选择性别").setItems(strs, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sexView.setText(strs[which]);
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }

    /**
     * 
     * 是否值班人员下拉框
     * 
     * @author Stark Zhou
     * @created 2015-11-18 下午2:31:42
     * @param view
     */
    public void isduty(View view) {
        final String[] strs = isDutyValueKeyMap.keySet().toArray(new String[isDutyValueKeyMap.size()]);
        Dialog dialog = new AlertDialog.Builder(ContactAddActivity.this).setIcon(R.drawable.qq_dialog_default_icon)
                .setTitle("请选择是否值班人员").setItems(strs, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isdutyView.setText(strs[which]);
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }

    /**
     * 
     * 部门检索
     * 
     * @author Stark Zhou
     * @created 2015-11-17 下午4:21:35
     * @param view
     */
    public void dept(View view) {
        // Intent intent =new Intent(instance, PersonSearchActivity.class);
        Intent intent = new Intent(instance, DeptSearchActivity.class);
        startActivityForResult(intent, R.layout.dept_search_at);
    }

    /**
     * 校验表单
     */
    private Boolean noChecked() {
        if (nameEditText.getText().toString().trim().length() == 0) {
            dialogToastNoCall("请输入联系人姓名！");
            return true;
        }
        if (phoneEditText.getText().toString().trim().length() == 0) {
            dialogToastNoCall("请输入联系人电话！");
            return true;
        }

        return false;
    }

}
