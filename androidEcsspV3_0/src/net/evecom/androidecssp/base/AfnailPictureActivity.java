/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package net.evecom.androidecssp.base;

import java.util.HashMap;

import net.evecom.androidecssp.R;
import net.evecom.androidecssp.bean.FileManageBean;
import net.mutil.util.HttpUtil;
import net.tsz.afinal.FinalDb;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * 
 * ���� AfnailPictureActivity
 * 
 * @author Mars zhang
 * @created 2015-11-23 ����11:11:31
 */
public class AfnailPictureActivity extends BaseActivity {
    /** MemberVariables */
    private ImageView imageView;
    /** ���ݿ� */
    private FinalDb db;
    /** ��ʾ�ļ����� */
    private FileManageBean filebean;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.afnail_picture_activity);
        // ����activityʱ���Զ����������
        imageView = (ImageView) findViewById(R.id.test1_picture);
        Intent intent = getIntent();
        filebean = (FileManageBean) getData("filebean", intent);
        if(null!=filebean.getFile_URL()){
            displayImage(imageView, filebean.getFile_URL());
        }
        if(null!=filebean.getFile_by1()){
            HashMap<String, String> mparas = new HashMap<String, String>();
            mparas.put("fileid",filebean.getFile_by1());
            displayImage(imageView, HttpUtil.getPCURL() + "jfs/ecssp/mobile/pubCtr/getImageFlowById", mparas);
        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(4, intent);
                finish();
            }
        });
    }

    /**
     * ���ذ�ť����¼�
     * 
     * @param v
     */
    public void fh(View v) { // ������ ���ذ�ť
        Intent intent = new Intent();
        setResult(4, intent);
        finish();
    }

    /** ɾ�� */
    public void delete(View v) {
        getDb().deleteById(FileManageBean.class, filebean.getFile_ID());
        Intent intent = new Intent();
        setResult(2, intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent intent = new Intent();
            setResult(4, intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
