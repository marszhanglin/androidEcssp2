/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package net.evecom.androidecssp.activity.pub;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import net.evecom.androidecssp.R;
import net.evecom.androidecssp.R.layout;
import net.evecom.androidecssp.activity.contact.ContactListActivity;
import net.evecom.androidecssp.activity.contact.AllGroupActivity.GroupAdapter;
import net.evecom.androidecssp.base.BaseActivity;
import net.evecom.androidecssp.base.BaseModel;
import net.evecom.androidecssp.util.ShareUtil;
import net.mutil.util.AnimationUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 
 * 区域列表
 * 
 * @author Stark Zhou
 * @created 2016-1-27 下午4:53:34
 */
public class DeptListActivity extends BaseActivity {
    /** listview */
    private ListView deptList;
    /** resultArray */
    private String resultArray;
    /** depts */
    private List<BaseModel> depts;
    /** deptAdapter */
    private DeptAdapter deptAdapter;
    /** dept */
    private BaseModel dept;
    /** 上一级记录 */
    private List<BaseModel> lastRank;
    /** rl */
    private RelativeLayout rl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dept_list_at);
        dept = null;
        lastRank = new ArrayList<BaseModel>();
        deptList = (ListView) findViewById(R.id.dept_listview);
        rl = (RelativeLayout) findViewById(R.id.dept_bottom_block);
        ImageView lastdept = (ImageView) findViewById(R.id.last_dept_btn);
        rl.setVisibility(View.GONE);
        lastdept.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (lastRank.size() > 0) {
                    // 返回上一级，设置父节点为前两级的数据，同时删除数组末尾的记录
                    dept = lastRank.get(lastRank.size() - 1);
                    lastRank.remove(lastRank.size() - 1);
                } else {
                    // 否则返回最上级，父节点为null
                    dept = null;
                }
                initList();
                deptAdapter.notifyDataSetChanged();
            }
        });
        initList();
    }

    /**
     * 
     * 初始化列表
     * 
     * @author Stark Zhou
     * @created 2016-1-27 下午4:58:46
     */
    private void initList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                HashMap<String, String> map = new HashMap<String, String>();
                String deptId;
                try {
                    if (dept != null) {
                        deptId = dept.getStr("id");
                        map.put("currentid", deptId);
                    }
                    resultArray = connServerForResultPost("jfs/ecssp/mobile/pubCtr/getAsyDeptTree", map);
                } catch (ClientProtocolException e) {
                    message.what = MESSAGETYPE_02;
                } catch (IOException e) {
                    message.what = MESSAGETYPE_02;
                }
                if (resultArray.length() > 0) {
                    try {
                        depts = getObjsInfo(resultArray);
                        if (depts.size() == 0) {
                            BaseModel root = new BaseModel();
                            root.set("id", "root");
                            root.set("name", "福建省政府");
                            depts.add(0, root);
                        }
                        if (null == depts) {
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
                Log.v("stark", resultArray);
                deptListHandler.sendMessage(message);
            }
        }).start();
    }

    /**
     * 消息处理器
     */
    private Handler deptListHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGETYPE_01:// 数据请求成功画列表
                    if (depts.get(0).get("haschildren") != null) {
                        rl.setVisibility(View.VISIBLE);
                    } else {
                        rl.setVisibility(View.GONE);
                    }
                    deptAdapter = new DeptAdapter(getApplicationContext(), depts);
                    deptList.setAdapter(deptAdapter);
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
     * 
     * 列表适配器
     * 
     * @author Stark Zhou
     * @created 2016-1-29 上午10:37:48
     */
    public class DeptAdapter extends BaseAdapter implements ListAdapter {
        /** MemberVariables */
        private Context context;
        /** MemberVariables */
        private LayoutInflater inflater;
        /** MemberVariables */
        private List<BaseModel> list;

        public DeptAdapter(Context context, List<BaseModel> list) {
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
                view = inflater.inflate(R.layout.dept_list_item, null);
            }
            TextView deptName = (TextView) view.findViewById(R.id.dept_list_name);
            deptName.setText("" + list.get(i).get("name"));
            ImageView deptImg = (ImageView) view.findViewById(R.id.dept_list_img);
            if (list.get(i).get("haschildren") != null && list.get(i).get("haschildren").toString().equals("false")) {
                deptImg.setBackgroundResource(R.drawable.group_2);
            } else {
                deptImg.setBackgroundResource(R.drawable.group_1);
            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AnimationUtil.AniZoomIn(v);
                    Intent intent = new Intent();
                    intent.putExtra("deptid", list.get(i).get("id").toString());
                    intent.putExtra("deptName", list.get(i).get("name").toString());
                    setResult(1, intent);
                    finish();
                }
            });

            deptImg.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    if (list.get(i).get("haschildren") == null
                            || list.get(i).get("haschildren").toString().equals("true")) {
                        if (dept != null) {
                            lastRank.add(dept);
                        }
                        AnimationUtil.AniZoomIn(v);
                        dept = list.get(i);
                        initList();
                        deptAdapter.notifyDataSetChanged();
                    }
                }
            });

            return view;
        }
    }

    /**
     * 
     * 重写返回方法
     * 
     * @author Stark Zhou
     * @created 2015-12-30 下午2:23:38
     * @param view
     */
    public void back(View view) {
        Intent intent = new Intent();
        setResult(4, intent);
        finish();
    }

    /**
     * 
     * 重写返回方法
     * 
     * @author Stark Zhou
     * @created 2015-12-30 下午2:23:38
     * @param view
     */
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
