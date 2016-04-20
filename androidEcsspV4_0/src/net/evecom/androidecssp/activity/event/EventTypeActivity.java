/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package net.evecom.androidecssp.activity.event;

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
 * 描述 事件列表
 * 
 * @author Mars zhang
 * @created 2015-11-12 上午10:13:27
 */
public class EventTypeActivity extends BaseActivity {
    /** MemberVariables */
    private ListView treeListView = null;
    /** MemberVariables */
    private String selectId = "";
    /** MemberVariables */
    private String selectName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_type_at);
        HashMap<String, String> postmap = new HashMap<String, String>();
        treeListView = (ListView) findViewById(R.id.event_list_listView_1);
        UiUtil.initTree("事件类型", "10000", "jfs/ecssp/mobile/eventCtr/getAsyEventTypeTree", postmap,
                R.layout.tree_data_item,R.id.tree_data_item_image, R.id.tree_data_item_text, R.drawable.ic_launcher,
                R.drawable.ljwg_dw_gzrz_aqjj, 90,new AsyTreeAdapter.AsyTreeItemclick() {
                    @Override
                    public void onItemClick(String itemId, String itemName) {
                        selectId = itemId;
                        selectName = itemName; 
                    }
                },treeListView,instance);
    } 
    
    /**
     * 
     * 描述
     * @author Mars zhang
     * @created 2015-12-17 上午11:30:42
     * @param view
     */
    public void selectedclick(View view){
        Intent intent =new Intent();
        intent.putExtra("eventTypeid", selectId);
        intent.putExtra("eventTypeName", selectName);
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
     * 描述
     * @author Mars zhang
     * @created 2015-12-17 下午6:27:30
     * @param view
     * @see net.evecom.androidecssp.base.BaseActivity#fh(android.view.View)
     */
    public void fh(View view){
        Intent intent = new Intent();
        setResult(4, intent);
        finish();
    }
}
