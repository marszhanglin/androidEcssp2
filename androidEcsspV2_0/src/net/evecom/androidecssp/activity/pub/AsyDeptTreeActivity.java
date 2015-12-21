/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package net.evecom.androidecssp.activity.pub;

import java.util.HashMap;

import net.evecom.androidecssp.R;
import net.evecom.androidecssp.base.BaseActivity;
import net.evecom.androidecssp.util.UiUtil;
import net.mutil.view.aystree.AsyTreeAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;

/**
 * 
 * ���� ����ѡ�����
 * 
 * @author Mars zhang
 * @created 2015-11-13 ����2:50:15
 */
public class AsyDeptTreeActivity extends BaseActivity { 
    /** listView */
    private ListView listView; // search_for_dept_listView 
    /** MemberVariables */
    private String selectId = "";
    /** MemberVariables */
    private String selectName = "";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dept_asytree_at); 
        HashMap<String, String> postmap = new HashMap<String, String>();
        listView = (ListView) findViewById(R.id.dept_asytree_ls);
        UiUtil.initTree("����ʡ����", "root", "jfs/ecssp/mobile/pubCtr/getAsyDeptTree", postmap, R.layout.tree_data_item,
                R.id.tree_data_item_image, R.id.tree_data_item_text, R.drawable.ic_launcher,R.drawable.ljwg_dw_gzrz_aqjj, 90,new AsyTreeAdapter.AsyTreeItemclick() {
                    @Override
                    public void onItemClick(String itemId, String itemName) {
                        selectId = itemId;
                        selectName = itemName; 
                    }
                },listView,instance);
    } 
    
    /**
     * 
     * ����
     * @author Mars zhang
     * @created 2015-12-17 ����11:30:42
     * @param view
     */
    public void selectedclick(View view){
        Intent intent =new Intent();
        intent.putExtra("deptid", selectId);
        intent.putExtra("deptName", selectName);
        setResult(1, intent);
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
    
    /**
     * 
     * ����
     * @author Mars zhang
     * @created 2015-12-17 ����6:27:30
     * @param view
     * @see net.evecom.androidecssp.base.BaseActivity#fh(android.view.View)
     */
    public void fh(View view){
        Intent intent = new Intent();
        setResult(4, intent);
        finish();
    }

}
