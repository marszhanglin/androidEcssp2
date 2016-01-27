/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package net.evecom.androidecssp.activity.event;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import net.evecom.androidecssp.R;
import net.evecom.androidecssp.activity.taskresponse.ProjectListActivity;
import net.evecom.androidecssp.base.BaseActivity;
import net.evecom.androidecssp.base.BaseModel;
import net.evecom.androidecssp.util.ShareUtil;
import net.mutil.util.HttpUtil;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 
 * 描述 事件列表
 * 
 * @author Mars zhang
 * @created 2015-11-12 上午10:13:27
 */
public class EventListActivity extends BaseActivity {
    /** MemberVariables */
    private ListView eventListView = null;
    /** MemberVariables */
    private List<BaseModel> eventInfos = null;
    /** MemberVariables */
    private String resutArray = "";
    /** 是否查询所有事件 */
    private boolean ifqueryallevents = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_list_at);
        ifqueryallevents = getIntent().getBooleanExtra("ifqueryallevents", true);
        init();
    }

    /**
     * 
     * 描述
     * 
     * @author Mars zhang
     * @created 2015-11-25 下午12:02:40
     */
    private void init() {
        eventListView = (ListView) findViewById(R.id.event_list_listView_1);
        initlist();
    }

    /**
     * 初始化列表
     */
    private void initlist() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();

                try {
                    HashMap<String, String> entityMap = new HashMap<String, String>();
                    entityMap.put("pagesize", HttpUtil.getPageSize(instance));
                    if (!ifqueryallevents) {
                       String deptid = ShareUtil.getString(instance, "PASSNAME", "orgid", "");
                       entityMap.put("deptid", deptid); 
                    }
                    resutArray = connServerForResultPost("jfs/ecssp/mobile/eventCtr/getEnentList", entityMap);
                } catch (ClientProtocolException e) {
                    message.what = MESSAGETYPE_02;
                    Log.e("mars", e.getMessage());
                } catch (IOException e) {
                    message.what = MESSAGETYPE_02;
                    Log.e("mars", e.getMessage());
                }
                if (resutArray.length() > 0) {
                    try {
                        eventInfos = getObjsInfo(resutArray);
                        if (null == eventInfos) {
                            message.what = MESSAGETYPE_02;
                        } else {
                            message.what = MESSAGETYPE_01;
                        }
                    } catch (JSONException e) {
                        message.what = MESSAGETYPE_02;
                        Log.e("mars", e.getMessage());
                    }
                } else {
                    message.what = MESSAGETYPE_02;
                }
                Log.v("mars", resutArray);
                eventListHandler.sendMessage(message);

            }
        }).start();

    }

    /**
     * eventListHandler
     */
    private Handler eventListHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGETYPE_01:// 数据请求成功画列表
                    EventAdapter eventAdapter = new EventAdapter(getApplicationContext(), eventInfos);
                    eventListView.setAdapter(eventAdapter);
                    break;
                case MESSAGETYPE_02:
                    toast("暂无事件发生", 1);
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
     */
    public class EventAdapter extends BaseAdapter implements ListAdapter {
        /** MemberVariables */
        private Context context;
        /** MemberVariables */
        private LayoutInflater inflater;
        /** MemberVariables */
        private List<BaseModel> list;

        public EventAdapter(Context context, List<BaseModel> list) {
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
                view = inflater.inflate(R.layout.event_list_item13, null);
            }
            TextView textViewEventName = (TextView) view.findViewById(R.id.list_item13_tv_1);
            TextView textViewEventType = (TextView) view.findViewById(R.id.list_item13_tv_2);
            TextView textViewEventArea = (TextView) view.findViewById(R.id.list_item13_tv_3);
            TextView textViewEventTime = (TextView) view.findViewById(R.id.list_item13_tv_4);
            textViewEventName.setText("" + list.get(i).get("eventname"));
            textViewEventType.setText("" + list.get(i).get("typename"));
            textViewEventArea.setText("" + list.get(i).get("areaname"));
            textViewEventTime.setText("事发时间：" + list.get(i).get("happendate"));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(instance, EventInfoActivity.class);
                    ProjectListActivity.pushData("eventInfo", list.get(i), intent);
                    intent.putExtra("ifqueryallevents", ifqueryallevents);
                    startActivity(intent);
                }
            });
            return view;
        }
    }
}
