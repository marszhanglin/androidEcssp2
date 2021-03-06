/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package net.evecom.androidecssp.activity.pub;

import net.evecom.androidecssp.R;
import net.evecom.androidecssp.activity.contact.ContactGroupActivity;
import net.evecom.androidecssp.activity.event.EventAddActivity;
import net.evecom.androidecssp.activity.event.EventListActivity;
import net.evecom.androidecssp.base.BaseActivity;
import net.evecom.androidecssp.gps.ResourceItemizedOverlayActivity;
import net.mutil.util.AnimationUtil;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

/**
 * 
 * ����
 * @author Mars zhang
 * @created 2015-12-9 ����3:48:38
 */
public class MainMenuActivity extends BaseActivity {
    /** MainMenuActivityʵ�� */
    public static MainMenuActivity mainMenuActivityInstance = null;
    private Intent intent=null;
    private TextView areaTV =null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_activity);
        mainMenuActivityInstance = this; 
        areaTV = (TextView) findViewById(R.id.main_menu_area_tv_id);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { // back��

        }
        return false;
    }

    /**
     * 
     * ����
     * 
     * @author Mars zhang
     * @created 2015-12-9 ����10:34:12
     * @param view
     */
    public void main1(View view) {
        AnimationUtil.AniZoomIn(view);
        intent = new Intent(getApplicationContext(), EmergencyNotification.class);
        startActivity(intent);
    }

    /**
     * 
     * ����  �¼��ϱ�
     * 
     * @author Mars zhang
     * @created 2015-12-9 ����10:34:16
     * @param view
     */
    public void main2(View view) {
        AnimationUtil.AniZoomIn(view);
        intent = new Intent(MainMenuActivity.this, EventAddActivity.class);
        startActivity(intent);
    }

    /**
     * 
     * ���� ������
     * 
     * @author Mars zhang
     * @created 2015-12-9 ����10:34:19
     * @param view
     */
    public void main3(View view) {
        AnimationUtil.AniZoomIn(view);
        intent = new Intent(getApplicationContext(), EventListActivity.class);
        startActivity(intent);
    }

    /**
     * 
     * ����  ͨѶ¼
     * 
     * @author Mars zhang
     * @created 2015-12-9 ����10:34:22
     * @param view
     */
    public void main4(View view) {
        AnimationUtil.AniZoomIn(view);
        intent = new Intent(MainMenuActivity.this, ContactGroupActivity.class);
        startActivity(intent);
    }

    /**
     * 
     * ���� ϵͳ����
     *  
     * @author Mars zhang
     * @created 2015-12-9 ����10:34:25
     * @param view
     */
    public void main5(View view) {
        AnimationUtil.AniZoomIn(view);
        intent = new Intent(MainMenuActivity.this, SystemSetingActivity.class);
        startActivity(intent);
    }

    /**
     * 
     * ����  ���¹���
     * 
     * @author Mars zhang
     * @created 2015-12-9 ����10:34:28
     * @param view
     */
    public void main6(View view) {
        AnimationUtil.AniZoomIn(view);
    }

    /**
     * 
     * ����  ��Դ����
     * 
     * @author Mars zhang
     * @created 2015-12-9 ����10:34:31
     * @param view
     */
    public void main7(View view) {
        AnimationUtil.AniZoomIn(view);
//        intent = new Intent(MainMenuActivity.this, BaseWebActivity.class);
        intent = new Intent(MainMenuActivity.this, ResourceItemizedOverlayActivity.class);
        startActivity(intent);
    }

    /**
     * 
     * ����  �yӋ����
     * 
     * @author Mars zhang
     * @created 2015-12-9 ����10:34:34
     * @param view
     */
    public void main8(View view) {
        AnimationUtil.AniZoomIn(view);
    }

    /**
     * 
     * ����
     * 
     * @author Mars zhang
     * @created 2015-12-9 ����10:34:37
     * @param view
     */
    public void main9(View view) {
        AnimationUtil.AniZoomIn(view);
        finish();
    }
    
    
    /**
     * 
     * ����
     * @author Mars zhang
     * @created 2015-12-15 ����8:42:48
     */
    public void orgclick(View view){ 
        Intent intent =new Intent(instance, AsyAreaTreeActivity.class);
        startActivityForResult(intent, R.layout.dept_asytree_at); 
    }
    
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case R.layout.dept_asytree_at:
                String areaName=data.getStringExtra("areaName");
                String areaid=data.getStringExtra("areaid");
                if(null!=areaName&&areaName.length()>0){
                    areaTV.setText(areaName);
                }
                break;

            default:
                break;
        }
        
    }
}