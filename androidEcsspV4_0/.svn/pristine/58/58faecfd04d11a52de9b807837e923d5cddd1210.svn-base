/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package net.evecom.androidecssp.activity.taskresponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.evecom.androidecssp.R;
import net.evecom.androidecssp.base.BaseActivity;
import net.evecom.androidecssp.base.BaseModel;
import net.evecom.androidecssp.util.ShareUtil;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
 * 描述 TaskListActivity
 * 
 * @author Mars zhang
 * @created 2015-11-12 上午10:12:38
 */
public class TaskListActivity extends BaseActivity {
    /** MemberVariables */
    private ListView taskListView = null;
    /** MemberVariables */
    private List<BaseModel> taskInfos = null;
    /** MemberVariables */
    private String resutArray = "";
    /** MemberVariables */
    private BaseModel eventInfo = null;
    /** MemberVariables */
    private BaseModel projectInfo = null;
    /** ifmytask */
    private boolean ifmytask = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_list_at);
        Intent intent = getIntent();
        ifmytask = intent.getBooleanExtra("ifmytask", false);
        if (!ifmytask) {
            eventInfo = (BaseModel) getData("eventInfo", intent);
            projectInfo = (BaseModel) getData("projectInfo", intent);
            findViewById(R.id.task_list_add_at).setVisibility(View.VISIBLE);
        }

        init();
    }

    /**
     * 
     * 描述
     * 
     * @author Mars zhang
     * @created 2015-11-25 下午2:07:02
     */
    private void init() {
        taskListView = (ListView) findViewById(R.id.task_list_listView_1);
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
                    HashMap<String, String> hashMap = new HashMap<String, String>();
                    String url = "";
                    if (!ifmytask) {
                        hashMap.put("eventId", eventInfo.get("id").toString());
                        hashMap.put("projectId", projectInfo.get("id").toString());
                        url = "jfs/ecssp/mobile/taskresponseCtr/getTaskByEventIdAndProjectId";
                    } else {
                        hashMap.put("deptid", ShareUtil.getString(instance, "PASSNAME", "orgid", ""));
                        hashMap.put("userid", ShareUtil.getString(instance, "PASSNAME", "userid", ""));
                        url = "jfs/ecssp/mobile/taskresponseCtr/getTaskByDeptIdAndUserId";
                    }
                    System.out.println(hashMap.values().toArray().toString());
                    resutArray = connServerForResultPost(url, hashMap);
                } catch (ClientProtocolException e) {
                    message.what = MESSAGETYPE_02;
                    Log.e("mars", e.getMessage());
                } catch (IOException e) {
                    message.what = MESSAGETYPE_02;
                    Log.e("mars", e.getMessage());
                }
                if (resutArray.length() > 0) {
                    try {
                        taskInfos = getObjsInfo(resutArray);
                        if (null == taskInfos) {
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
                    ListAdapter eventAdapter = new MyListAdapter(getApplicationContext(), taskInfos);
                    taskListView.setAdapter(eventAdapter);
                    break;
                case MESSAGETYPE_02:
                    toast("该处置暂无任务", 1);
                    break;
                default:
                    break;
            }

        };
    };

    /**
     * 解析事件json数据
     * 
     */
    public static List<TaskInfo> getEvents(String jsonString) throws JSONException {
        List<TaskInfo> list = new ArrayList<TaskInfo>();
        JSONArray jsonArray = null;
        jsonArray = new JSONArray(jsonString);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            TaskInfo taskInfo = new TaskInfo();
            taskInfo.setId(jsonObject.getString("id"));
            taskInfo.setTaskname(jsonObject.getString("taskname"));
            taskInfo.setTaskcontern(jsonObject.getString("taskcontern"));
            taskInfo.setCreatetime(jsonObject.getString("createtime"));
            taskInfo.setKeyword(jsonObject.getString("keyword"));
            taskInfo.setTaskdept(jsonObject.getString("taskdept"));
            taskInfo.setTaskdeptid(jsonObject.getString("taskdeptid"));
            list.add(taskInfo);
        }
        return list;
    }

    /**
     * 匿名适ListView配器类
     * 
     * @author Mars zhang
     */
    public class MyListAdapter extends BaseAdapter implements ListAdapter {
        /** MemberVariables */
        private Context context;
        /** MemberVariables */
        private LayoutInflater inflater;
        /** MemberVariables */
        private List<BaseModel> list;

        public MyListAdapter(Context context, List<BaseModel> list) {
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
                view = inflater.inflate(R.layout.task_list_item13, null);
            }
            TextView textViewTaskName = (TextView) view.findViewById(R.id.list_item13_tv_1);
            TextView textViewDept = (TextView) view.findViewById(R.id.list_item13_tv_2);
            TextView textViewpersion = (TextView) view.findViewById(R.id.list_item13_tv_3);
            TextView textViewTaskEndTime = (TextView) view.findViewById(R.id.list_item13_tv_4);
            textViewTaskName.setText(ifnull(list.get(i).get("taskname") + "", ""));
            textViewDept.setText(ifnull(list.get(i).get("taskdept") + "", ""));
            textViewpersion.setText(ifnull(list.get(i).get("taskperson") + "", ""));
            textViewTaskEndTime.setText("完成期限：" + ifnull(list.get(i).get("taskeffi") + "", ""));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 跳转反馈信息界面
                    // Intent intent = new Intent(instance,
                    // TaskInfoActivity.class);
                    Intent intent = new Intent(instance, TaskAndResponsesInfoActivity.class);
                    // TaskInfoActivity.pushData("eventInfo", eventInfo,
                    // intent);
                    // TaskInfoActivity.pushData("projectInfo", projectInfo,
                    // intent);
                    TaskInfoActivity.pushData("taskInfo", list.get(i), intent);
                    startActivity(intent);
                }
            });
            return view;
        }
    }

    /**
     * 
     * 描述 新增任务
     * 
     * @author Mars zhang
     * @created 2015-11-12 上午10:33:18
     * @param view
     */
    public void add(View view) {
        Intent intent = new Intent(instance, TaskAddActivity.class);
        TaskAddActivity.pushData("eventInfo", eventInfo, intent);
        TaskAddActivity.pushData("projectInfo", projectInfo, intent);
        startActivity(intent);
        finish();
    }
}
