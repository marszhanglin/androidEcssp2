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
import net.evecom.androidecssp.activity.taskresponse.TaskAddActivity;
import net.evecom.androidecssp.base.BaseActivity;
import net.evecom.androidecssp.base.BaseModel;
import net.evecom.androidecssp.base.IPickCallback;

/**
 * 联系人信息界面
 *
 * @author Stark Zhou
 * @created 2015-11-12 下午5:00:43
 */
public class ContactInfoActivity extends BaseActivity{
    /**contactInfo */
    private BaseModel contactInfo=null;
    /**group */
    private BaseModel group=null;
    /**sexView*/
    private TextView sexView;
    /**deleteResult*/
    private String deleteResult="";
    /**sexValueKeyMap*/
    HashMap<String, String> sexValueKeyMap=new HashMap<String, String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_info_at);
        Intent intent= getIntent();
        contactInfo= (BaseModel) getData("cantactInfo", intent);
        group= (BaseModel) getData("group", intent);
        init();
        initData();
    }
    /**
     * 
     * 初始化联系人信息
     *
     * @author Stark Zhou
     * @created 2015-11-13 下午3:22:07
     */
    private void init() {
        TextView nameEditText = (TextView) findViewById(R.id.contact_name);
        nameEditText.setText(ifnull(contactInfo.get("name")+"", ""));
        TextView phoneEditText = (TextView) findViewById(R.id.contact_phone);
        phoneEditText.setText(ifnull(contactInfo.get("mobiletel")+"",""));
        TextView officetelEditText = (TextView) findViewById(R.id.contact_officetel);
        officetelEditText.setText(ifnull(contactInfo.get("officetel")+"",""));
        TextView emailEditText = (TextView) findViewById(R.id.contact_email);
        emailEditText.setText(ifnull(contactInfo.get("email")+"",""));
        sexView = (TextView) findViewById(R.id.contact_sex);
        TextView deptEditText = (TextView) findViewById(R.id.contact_dept);
        deptEditText.setText(ifnull(contactInfo.get("deptname")+"",""));
        TextView groupEditText = (TextView) findViewById(R.id.contact_group);
        groupEditText.setText(ifnull(group.get("groupname")+"",""));
        TextView postEditText = (TextView) findViewById(R.id.contact_post);
        postEditText.setText(ifnull(contactInfo.get("post")+"",""));
        TextView addrareaEditText = (TextView) findViewById(R.id.contact_addrarea);
        addrareaEditText.setText(ifnull(contactInfo.get("addrarea")+"",""));
        TextView isdutyEditText = (TextView) findViewById(R.id.contact_isduty);
        isdutyEditText.setText(ifnull(contactInfo.get("isduty")+"",""));
        sexView.setText(contactInfo.get("sexname")+"");
    }
    /**
     * 
     * 初始化性别数据字典
     *
     * @author Stark Zhou
     * @created 2015-11-13 下午3:55:37
     */
    private void initData(){
        getDict("SYSTEM_SEX",sexValueKeyMap);
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
        final String[] strs =sexValueKeyMap.keySet().toArray(new String[sexValueKeyMap.size()]);
        Dialog dialog = new AlertDialog.Builder(ContactInfoActivity.this)
        .setIcon(R.drawable.qq_dialog_default_icon).setTitle("请选择性别")
        .setItems(strs, new DialogInterface.OnClickListener() {
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
     * 刪除联系人
     *
     * @author Stark Zhou
     * @created 2015-11-25 下午12:05:15
     * @param view
     */   
    public void delete(View view){
        
        DialogPickToast("提示","是否删除联系人？","确定","取消",new IPickCallback() {
            
            @Override
            public void yes() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HashMap<String,String> hashMap=new HashMap<String,String>();
                        hashMap.put("id",contactInfo.getStr("id"));
                        Message message = new Message();                
                        try {
                            deleteResult = connServerForResultPost(
                                    "jfs/ecssp/mobile/contactCtr/delContact", hashMap);
                            if ( deleteResult .length() > 0) {
                                message.what = MESSAGETYPE_01;
                                }
                        } catch (ClientProtocolException e) {
                            message.what = MESSAGETYPE_02;
                            Log.e("stark", e.getMessage());
                        } catch (IOException e) {
                            message.what = MESSAGETYPE_02;
                            Log.e("stark", e.getMessage());
                        }
                        
                        deleteHandler.sendMessage(message);
                    }
                }).start();
            }
            @Override
            public void no() {
            }
     
        });
    }
    /** deleteHandler */
    private Handler deleteHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MESSAGETYPE_01://删除成功
                Intent intent = new Intent(getApplicationContext(),
                        ContactListActivity.class); 
                ContactListActivity.pushData("group",group,intent);
                toast("删除成功",1);
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

}
