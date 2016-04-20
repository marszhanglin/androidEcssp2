/*
 * Copyright (c) 2005, 2015, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package net.evecom.androidecssp.activity.inform;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import net.evecom.androidecssp.R;
import net.evecom.androidecssp.activity.contact.ContactListActivity;
import net.evecom.androidecssp.base.BaseActivity;
import net.evecom.androidecssp.base.BaseModel;
import net.evecom.androidecssp.util.ShareUtil;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 通知信息
 * 
 * @author Stark Zhou
 * @created 2015-11-12 下午5:00:43
 */
public class InformListActivity extends BaseActivity {
    /** informListView */
    private ListView informListView = null;
    /** searchEdit */
    private EditText searchEdit;
    /** userId */
    private String userId = "";
    /** resultArray */
    private String resultArray = "";
    /** informs */
    private List<BaseModel> informs = new ArrayList<BaseModel>();
    /** searchStr */
    private String searchStr = "";
    /** informAdapter */
    private InformAdapter informAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inform_list_at);
        init();
    }

    /**
     * 初始化组件
     */
    private void init() {
        informListView = (ListView) findViewById(R.id.inform_list_listView_1);
        searchEdit = (EditText) findViewById(R.id.inform_search_edit);
        searchStr = searchEdit.getText().toString().trim();
        initList();
    }

    /**
     * 初始化列表
     */
    private void initList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                HashMap<String, String> map = new HashMap<String, String>();
                userId = ShareUtil.getString(getApplicationContext(), "PASSNAME", "userid", "0");
                try {
                    map.put("searchStr", searchStr);
                    map.put("userid", userId);
                    resultArray = connServerForResultPost("jfs/ecssp/mobile/informCtr/getInfromList", map);
                } catch (ClientProtocolException e) {
                    message.what = MESSAGETYPE_02;
                } catch (IOException e) {
                    message.what = MESSAGETYPE_02;
                }
                if (resultArray.length() > 0) {
                    try {
                        informs = getObjsInfo(resultArray);
                        if (informs.size() == 0) {
                            message.what = MESSAGETYPE_02;
                        } else {
                            message.what = MESSAGETYPE_01;
                        }
                    } catch (JSONException e) {
                        message.what = MESSAGETYPE_02;
                    }
                } else {
                    message.what = MESSAGETYPE_02;
                }
                informHandler.sendMessage(message);

            }
        }).start();
    }

    /**
     * 搜索点击触发事件
     */
    public void searchInform(View view) {
        informs.removeAll(informs);
        searchStr = searchEdit.getText().toString().trim();
        initList();
        informAdapter.notifyDataSetChanged();
    }

    /**
     * 消息处理器
     */
    private Handler informHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGETYPE_01:// 数据请求成功画列表
                    informAdapter = new InformAdapter(getApplicationContext(), informs);
                    informListView.setAdapter(informAdapter);
                    break;
                case MESSAGETYPE_02:
                    toast("暂无紧急通知", 1);
                    break;
                default:
                    break;
            }

        };
    };

    /**
     * 匿名ListView适配器类
     * 
     * @author Stark zhou
     */
    public class InformAdapter extends BaseAdapter implements ListAdapter {
        /** MemberVariables */
        private Context context;
        /** MemberVariables */
        private LayoutInflater inflater;
        /** MemberVariables */
        private List<BaseModel> list;

        /** GroupAdapter */
        public InformAdapter(Context context, List<BaseModel> list) {
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
                view = inflater.inflate(R.layout.inform_list_item, null);
            }
            TextView informTitle = (TextView) view.findViewById(R.id.inform_list_item_1);
            informTitle.setText("" + list.get(i).get("title"));
            TextView informStartTime = (TextView) view.findViewById(R.id.inform_list_item_2);
            informStartTime.setText("摘要：" + ifnull(list.get(i).get("summary"), ""));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(instance, InformInfoActivity.class);
                    ContactListActivity.pushData("inform", list.get(i), intent);
                    startActivity(intent);
                }
            });

            return view;
        }
    }

}
