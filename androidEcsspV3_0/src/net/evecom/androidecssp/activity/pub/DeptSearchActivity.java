/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package net.evecom.androidecssp.activity.pub;

import java.util.HashMap;
import java.util.List;

import net.evecom.androidecssp.R;
import net.evecom.androidecssp.base.BaseActivity;
import net.evecom.androidecssp.base.BaseModel;
import net.mutil.util.HttpUtil;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 
 * 描述 机构选择界面
 * 
 * @author Mars zhang
 * @created 2015-11-13 下午2:50:15
 */
public class DeptSearchActivity extends BaseActivity {
    /** nameEdit */
    private EditText nameEdit; // dept_name_edit
    /** listView */
    private ListView listView; // search_for_dept_listView
    /** baseModels */
    private List<BaseModel> baseModels;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dept_search_at);
        init();
        updatelist();

    }

    /**
     * 
     * 描述 init
     * 
     * @author Mars zhang
     * @created 2015-11-17 下午4:45:34
     */
    private void init() {

        nameEdit = (EditText) findViewById(R.id.dept_name_edit);

        listView = (ListView) findViewById(R.id.search_for_dept_listView);

    }

    /**
     * 
     * 描述 查询按钮点击
     * 
     * @author Mars zhang
     * @created 2015-11-13 下午3:59:59
     * @param view
     */
    public void searchonclick(View view) {

        updatelist();
    }

    /**
     * 
     * 描述 updatelist
     * 
     * @author Mars zhang
     * @created 2015-11-17 下午4:45:50
     */
    private void updatelist() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                try {
                    HashMap<String, String> entityMap = new HashMap<String, String>();
                    entityMap.put("name", nameEdit.getText().toString().trim());
                    entityMap.put("pagesize", HttpUtil.getPageSize(instance));
                    String result = connServerForResultPost("jfs/ecssp/mobile/pubCtr/searchOrglist", entityMap);
                    if (null != result && result.length() > 0) {
                        msg.what = MESSAGETYPE_01;
                        baseModels = getObjsInfo(result);
                    } else {
                        msg.what = MESSAGETYPE_02;
                    }
                } catch (Exception e) {
                    msg.what = MESSAGETYPE_02;
                }

                listaResuesthandler.sendMessage(msg);
            }
        }).start();
    }

    /**
     * listaResuesthandler
     */
    Handler listaResuesthandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGETYPE_01:
                    listView.setAdapter(new BaseListAdapter(instance, baseModels));
                    break;
                case MESSAGETYPE_02:
                    toast("请求失败，请检查网络是否可用！", 1);
                    break;
                default:
                    break;
            }
        };
    };

    /**
     * 匿名适ListView配器类
     * 
     * @author Mars zhang
     * 
     */
    public class BaseListAdapter extends BaseAdapter implements ListAdapter {
        /** MemberVariables */
        private Context context;
        /** MemberVariables */
        private LayoutInflater inflater;
        /** MemberVariables */
        private List<BaseModel> list;

        public BaseListAdapter(Context context, List<BaseModel> list) {
            this.context = context;
            inflater = LayoutInflater.from(context);
            this.list = list;
        }

        @Override
        public int getCount() {
            return list == null ? 0 : list.size();

        }

        @Override
        public Object getItem(int item) {
            return this.list.get(item);
        }

        @Override
        public long getItemId(int itemId) {
            return itemId;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            if (null == view) {
                view = inflater.inflate(R.layout.dept_search_list_item, null);
            }
            TextView textViewName = (TextView) view.findViewById(R.id.dept_search_list_item_name);
            TextView textViewaddr = (TextView) view.findViewById(R.id.dept_search_list_item_addr);
            TextView textViewdescript = (TextView) view.findViewById(R.id.dept_search_list_item_descript);

            textViewName.setText("机构名称：" + ifnull(list.get(i).get("name") + "", "无"));
            textViewaddr.setText("地    址：" + ifnull(list.get(i).get("addr") + "", "无"));
            textViewdescript.setText("描    述：" + ifnull(list.get(i).get("description") + "", "无"));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    BaseActivity.pushData("deptinfo", list.get(i), intent);
                    setResult(1, intent);
                    finish();
                }
            });

            return view;
        }
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
