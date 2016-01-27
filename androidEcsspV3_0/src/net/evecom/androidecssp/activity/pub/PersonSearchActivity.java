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
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
 * 描述 人员选择界面
 * 
 * @author Mars zhang
 * @created 2015-11-13 下午2:50:15
 */
public class PersonSearchActivity extends BaseActivity {
    /** MemberVariables */
    private EditText nameEdit; // searchperson_name_edit
    /** MemberVariables */
    private EditText orgnameEdit; // search_person_orgname_edit
    /** MemberVariables */
    private EditText sexEdit; // search_person_sex_edit
    /** MemberVariables */
    private ListView listView; // search_for_qy_listView
    /** MemberVariables */
    HashMap<String, String> sexvalueKeyMap = new HashMap<String, String>();
    /** MemberVariables */
    private List<BaseModel> baseModels;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.people_search_at);
        init();
        updatelist();

    }

    /**
     * 
     * 描述
     * 
     * @author Mars zhang
     * @created 2015-11-25 下午2:03:52
     */
    private void init() {

        nameEdit = (EditText) findViewById(R.id.searchperson_name_edit);
        orgnameEdit = (EditText) findViewById(R.id.search_person_orgname_edit);
        sexEdit = (EditText) findViewById(R.id.search_person_sex_edit);

        listView = (ListView) findViewById(R.id.search_for_qy_listView);
        System.out.println("类：" + getIntent().getClass().getName());
        getDict("SYSTEM_SEX", sexvalueKeyMap);
    }

    /**
     * 
     * 描述 性别输入框点击
     * 
     * @author Mars zhang
     * @created 2015-11-17 上午9:08:14
     * @param view
     */
    public void sexonclick(View view) {
        final String[] strs = sexvalueKeyMap.keySet().toArray(new String[sexvalueKeyMap.size()]);
        Dialog dialog = new AlertDialog.Builder(instance).setIcon(R.drawable.qq_dialog_default_icon).setTitle("请选择级别")
                .setItems(strs, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sexEdit.setText(strs[which]);
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
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
     * 描述
     * 
     * @author Mars zhang
     * @created 2015-11-25 下午2:03:58
     */
    private void updatelist() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                try {
                    HashMap<String, String> entityMap = new HashMap<String, String>();
                    entityMap.put("name", nameEdit.getText().toString().trim());
                    entityMap.put("orgname", orgnameEdit.getText().toString().trim());
                    entityMap.put("sexDict", ifnull(sexvalueKeyMap.get(sexEdit.getText().toString()), ""));
                    entityMap.put("pagesize", HttpUtil.getPageSize(instance));
                    String result = connServerForResultPost("jfs/ecssp/mobile/pubCtr/searchUserlist", entityMap);
                    System.out.println(result);
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

    /** MemberVariables */
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
                view = inflater.inflate(R.layout.people_search_list_item, null);
            }
            TextView textViewName = (TextView) view.findViewById(R.id.people_search_list_item_name);
            TextView textViewsex = (TextView) view.findViewById(R.id.people_search_list_item_sex);
            TextView textViewaddr = (TextView) view.findViewById(R.id.people_search_list_item_addr);
            TextView textViewphone = (TextView) view.findViewById(R.id.people_search_list_item_phone);
            TextView textVieworgname = (TextView) view.findViewById(R.id.people_search_list_item_orgname);

            textViewName.setText("姓    名：" + ifnull(list.get(i).get("name") + "", "无"));
            textViewsex.setText("性    别：" + ifnull(list.get(i).get("sexname") + "", "无"));
            textViewaddr.setText("地    址：" + ifnull(list.get(i).get("address") + "", "无"));
            textViewphone.setText("手    机：" + ifnull(list.get(i).get("mobile") + "", "无"));
            textVieworgname.setText("所属组织：" + ifnull(list.get(i).get("orgname") + "", "无"));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    BaseActivity.pushData("personinfo", list.get(i), intent);
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
