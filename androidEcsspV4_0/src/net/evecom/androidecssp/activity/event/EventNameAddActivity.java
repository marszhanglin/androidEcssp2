/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package net.evecom.androidecssp.activity.event;

import net.evecom.androidecssp.R;
import net.evecom.androidecssp.base.BaseActivity;
import net.evecom.androidecssp.base.BaseModel;
import net.evecom.androidecssp.util.ShareUtil;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

/**
 * 
 * ���� EventNameAddActivity
 * 
 * @author Mars zhang
 * @created 2015-11-23 ����2:21:54
 */
public class EventNameAddActivity extends BaseActivity {
    /** MemberVariables */
    private EditText nameED; // event_name_add_name_ed
    /** MemberVariables */
    private EditText addrED; // event_name_add_addr_ed
    /** MemberVariables */
    private EditText bgrED; // event_name_add_name_bgr
    /** MemberVariables */
    private EditText phoneED; // event_name_add_phone_ed
    /** MemberVariables */
    private EditText contentED; // event_name_add_content

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_name_add_activity);
        initView();
        initdata();
    }

    /**
     * 
     * ���� ��ʼ������
     * 
     * @author Mars zhang
     * @created 2015-11-23 ����4:41:29
     */
    private void initView() {
        nameED = (EditText) findViewById(R.id.event_name_add_name_ed);
        addrED = (EditText) findViewById(R.id.event_name_add_addr_ed);
        bgrED = (EditText) findViewById(R.id.event_name_add_name_bgr);
        phoneED = (EditText) findViewById(R.id.event_name_add_phone_ed);
        contentED = (EditText) findViewById(R.id.event_name_add_content);
    }

    /**
     * 
     * ���� ��ʼ������
     * 
     * @author Mars zhang
     * @created 2015-11-24 ����5:22:30
     */
    private void initdata() {
        Intent intent = getIntent();
        String bgrname = ifnull(intent.getStringExtra("bgrnameTV"), "");
        String bgrphone = ifnull(intent.getStringExtra("bgrphoneTV"), "");
        nameED.setText(ifnull(intent.getStringExtra("nameTV"), ""));
        addrED.setText(ifnull(intent.getStringExtra("addrTV"), ""));
        if(bgrname.length()<1){
            bgrED.setText(ShareUtil.getString(instance, "PASSNAME", "usernameCN", ""));
        }else{
            bgrED.setText(bgrname);
        }
        if(bgrname.length()<1){
            phoneED.setText(ShareUtil.getString(instance, "PASSNAME", "mobile_In_clound", ""));
        }else{
            phoneED.setText(bgrphone);
        }
        contentED.setText(ifnull(intent.getStringExtra("contentTV"), ""));
    }

    /**
     * 
     * ���� ��д���
     * 
     * @author Mars zhang
     * @created 2015-11-23 ����4:40:27
     * @param view
     */
    public void bc(View view) {
        // У��
        if (nameED.getText().toString().trim().length() == 0) {
            errorAni(nameED);
            toast("�������¼�����", 1);
            return;
        } else if (addrED.getText().toString().trim().length() == 0) {
            errorAni(addrED);
            toast("�������·���ַ", 1);
            return;
        } else if (bgrED.getText().toString().trim().length() == 0) {
            errorAni(bgrED);
            toast("�����뱨����", 1);
            return;
        } else if (phoneED.getText().toString().trim().length() == 0) {
            errorAni(phoneED);
            toast("��������ϵ�绰", 1);
            return;
        } else if (contentED.getText().toString().trim().length() == 0) {
            errorAni(contentED);
            toast("�������¼�����", 1);
            return;
        }

        BaseModel eventinfo = new BaseModel();
        eventinfo.set("eventname", nameED.getText().toString());
        eventinfo.set("happenaddress", addrED.getText().toString());
        eventinfo.set("reporterperson", bgrED.getText().toString());
        eventinfo.set("reportertel", phoneED.getText().toString());
        eventinfo.set("eventcontent", contentED.getText().toString());
        Intent intent = new Intent();
        BaseActivity.pushData("eventinfo", eventinfo, intent);
        setResult(1, intent);
        finish();
    }

    /**
     * 
     * ����
     * 
     * @author Mars zhang
     * @created 2015-11-23 ����5:23:06
     * @param view
     * @see net.evecom.androidecssp.base.BaseActivity#fh(android.view.View)
     */
    public void fh(View view) {
        Intent intent = new Intent();
        setResult(1, intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            // Intent intent = new Intent(this, EventAddActivity.class);
            Intent intent = new Intent();
            setResult(4, intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
