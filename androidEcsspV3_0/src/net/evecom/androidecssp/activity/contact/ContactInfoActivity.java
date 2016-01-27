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
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import net.evecom.androidecssp.R;
import net.evecom.androidecssp.base.BaseActivity;
import net.evecom.androidecssp.base.BaseModel;
import net.evecom.androidecssp.base.IPickCallback;

/**
 * ��ϵ����Ϣ����
 * 
 * @author Stark Zhou
 * @created 2015-11-12 ����5:00:43
 */
public class ContactInfoActivity extends BaseActivity {
    /** contactInfo */
    private BaseModel contactInfo = null;
    /** group */
    private BaseModel group = null;
    /** sexView */
    private TextView sexView;
    /** deleteResult */
    private String deleteResult = "";
    /** phoneEditText */
    private TextView phoneText;
    /** officetelEditText */
    private TextView officetelText;
    /** sexValueKeyMap */
    HashMap<String, String> sexValueKeyMap = new HashMap<String, String>();
    /** groupId */
    private String groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_info_at);
        Intent intent = getIntent();
        contactInfo = (BaseModel) getData("cantactInfo", intent);
        group = (BaseModel) getData("group", intent);
        // splitCalls(contactInfo.get("callnos").toString());
        init();
    }

    /**
     * ��ϵ���ͷ���
     */
    /*
     * public void splitCalls(String calls) {
     * 
     * String[] callGrp = new String[] {}; String[] callNo = new String[] {};
     * callGrp = calls.split(";");
     * 
     * for (int i = 0; i < callGrp.length; i++) { callNo =
     * callGrp[i].split(","); for (int j = 0; j < 2; j++) { if
     * (callNo[0].equals("1")) { phoneEditText.setText(callNo[1]); } else if
     * (callNo[0].equals("2")) { officetelEditText.setText(callNo[1]); } } } }
     */

    /**
     * 
     * ��ʼ����ϵ����Ϣ
     * 
     * @author Stark Zhou
     * @created 2015-11-13 ����3:22:07
     */
    private void init() {

        TextView nameEditText = (TextView) findViewById(R.id.contact_name);
        nameEditText.setText(ifnull(contactInfo.get("name") + "", ""));
        phoneText = (TextView) findViewById(R.id.contact_phone);
        phoneText.setText(ifnull(contactInfo.get("�ֻ�"), ""));
        officetelText = (TextView) findViewById(R.id.contact_officetel);
        officetelText.setText(ifnull(contactInfo.get("�̻�"), ""));
        TextView faxText = (TextView) findViewById(R.id.contact_fax);
        faxText.setText(ifnull(contactInfo.get("����"), ""));
        TextView clusterText = (TextView) findViewById(R.id.contact_cluster);
        clusterText.setText(ifnull(contactInfo.get("��Ⱥ"), ""));
        TextView addrEditText = (TextView) findViewById(R.id.contact_addr);
        addrEditText.setText(ifnull(contactInfo.get("address") + "", ""));
        sexView = (TextView) findViewById(R.id.contact_sex);
        TextView deptEditText = (TextView) findViewById(R.id.contact_dept);
        deptEditText.setText(ifnull(contactInfo.get("dept") + "", ""));
        TextView groupEditText = (TextView) findViewById(R.id.contact_group);
        groupEditText.setText(ifnull(contactInfo.get("groupname") + "", "δ����"));
        sexView.setText(ifnull(contactInfo.get("sexname") + "", ""));
    }

    /**
     * �����ֻ�
     */
    public void callPhone(View view) {
        if (phoneText.getText() != null && "" != phoneText.getText()) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneText.getText()));
            startActivity(intent);
        }
    }

    /**
     * ����绰
     */
    public void callTel(View view) {
        if (officetelText.getText() != null && "" != officetelText.getText()) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + officetelText.getText()));
            startActivity(intent);
        }
    }

    /**
     * ���Ͷ���
     */
    public void sendMessage(View view) {
        if (phoneText.getText() != null && "" != phoneText.getText()) {
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phoneText.getText()));
            startActivity(intent);
        }
    }

    /**
     * 
     * �h����ϵ��
     * 
     * @author Stark Zhou
     * @created 2015-11-25 ����12:05:15
     * @param view
     */
    public void delete(View view) {

        DialogPickToast("��ʾ", "�Ƿ�ɾ����ϵ�ˣ�", "ȷ��", "ȡ��", new IPickCallback() {

            @Override
            public void yes() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HashMap<String, String> hashMap = new HashMap<String, String>();
                        hashMap.put("id", contactInfo.getStr("id"));
                        Message message = new Message();
                        try {
                            deleteResult = connServerForResultPost("jfs/ecssp/mobile/contactCtr/delContact", hashMap);
                            if (deleteResult.length() > 0) {
                                message.what = MESSAGETYPE_01;
                            }
                        } catch (ClientProtocolException e) {
                            message.what = MESSAGETYPE_02;
                        } catch (IOException e) {
                            message.what = MESSAGETYPE_02;
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

    /**
     * deleteHandler
     */
    private Handler deleteHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGETYPE_01:// ɾ���ɹ�
                    Intent intent = new Intent(getApplicationContext(), ContactListActivity.class);

                    ContactListActivity.pushData("group", group, intent);
                    toast("ɾ���ɹ�", 1);
                    startActivity(intent);
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

}
