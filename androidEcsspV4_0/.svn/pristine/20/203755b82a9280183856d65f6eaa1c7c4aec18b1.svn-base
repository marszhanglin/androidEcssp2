/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package net.evecom.androidecssp.activity.event;

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
public class ETypeListActivity extends BaseActivity {
    /** listview */
    private ListView etypeList;
    /** resultArray */
    private String resultArray;
    /** etypes */
    private List<BaseModel> etypes;
    /** etypeAdapter */
    private ETypeAdapter etypeAdapter;
    /** etype */
    private BaseModel etype;
    /** lastRank */
    private List<BaseModel> lastRank;
    /** rl */
    private RelativeLayout rl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.etype_list_at);
        etype = null;
        lastRank = new ArrayList<BaseModel>();
        etypeList = (ListView) findViewById(R.id.etype_listview);
        rl = (RelativeLayout) findViewById(R.id.etype_bottom_block);
        ImageView lastetype = (ImageView) findViewById(R.id.last_etype_btn);
        rl.setVisibility(View.GONE);
        lastetype.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (lastRank.size() > 0) {
                    // 返回上一级，设置父节点为前两级的数据，同时删除数组末尾的记录
                    etype = lastRank.get(lastRank.size() - 1);
                    lastRank.remove(lastRank.size() - 1);
                } else {
                    // 否则返回最上级，父节点为null
                    etype = null;
                }
                initList();
                etypeAdapter.notifyDataSetChanged();
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
                String etypeId;
                try {
                    // 如果不是第一级,则加入父节点id
                    if (etype != null) {
                        etypeId = etype.getStr("id");
                        map.put("currentid", etypeId);
                    }
                    resultArray = connServerForResultPost("jfs/ecssp/mobile/eventCtr/getAsyEventTypeTree", map);
                } catch (ClientProtocolException e) {
                    message.what = MESSAGETYPE_02;
                } catch (IOException e) {
                    message.what = MESSAGETYPE_02;
                }
                if (resultArray.length() > 0) {
                    try {
                        etypes = getObjsInfo(resultArray);
                        // 设置默认第一级
                        if (etypes.size() == 0) {
                            BaseModel root = new BaseModel();
                            root.set("id", "10000");
                            root.set("name", "事件类型");
                            etypes.add(0, root);
                        }
                        if (null == etypes) {
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
                etypeListHandler.sendMessage(message);
            }
        }).start();
    }

    /**
     * 消息处理器
     */
    private Handler etypeListHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGETYPE_01:// 数据请求成功画列表
                    if (etypes.get(0).get("haschildren") != null) {
                        rl.setVisibility(View.VISIBLE);
                    } else {
                        rl.setVisibility(View.GONE);
                    }
                    etypeAdapter = new ETypeAdapter(getApplicationContext(), etypes);
                    etypeList.setAdapter(etypeAdapter);
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
     * @created 2016-1-29 上午10:37:14
     */
    public class ETypeAdapter extends BaseAdapter implements ListAdapter {
        /** MemberVariables */
        private Context context;
        /** MemberVariables */
        private LayoutInflater inflater;
        /** MemberVariables */
        private List<BaseModel> list;

        public ETypeAdapter(Context context, List<BaseModel> list) {
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
                view = inflater.inflate(R.layout.etype_list_item, null);
            }
            TextView etypeName = (TextView) view.findViewById(R.id.etype_list_name);
            etypeName.requestFocus();
            etypeName.setText("" + list.get(i).get("name"));
            ImageView etypeImg = (ImageView) view.findViewById(R.id.etype_list_img);
            // 是否可以展开设置不同图标
            if (list.get(i).get("haschildren") != null && list.get(i).get("haschildren").toString().equals("false")) {
                etypeImg.setBackgroundResource(R.drawable.group_2);
            } else {
                etypeImg.setBackgroundResource(R.drawable.group_1);
            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AnimationUtil.AniZoomIn(v);
                    Intent intent = new Intent();
                    intent.putExtra("eventTypeid", list.get(i).get("id").toString());
                    intent.putExtra("eventTypeName", list.get(i).get("name").toString());
                    setResult(1, intent);
                    finish();
                }
            });

            etypeImg.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // 可展开时触发事件
                    if (list.get(i).get("haschildren") == null
                            || list.get(i).get("haschildren").toString().equals("true")) {
                        // 记录上一级的父节点数据
                        if (etype != null) {
                            lastRank.add(etype);
                        }
                        AnimationUtil.AniZoomIn(v);
                        etype = list.get(i);
                        initList();
                        etypeAdapter.notifyDataSetChanged();
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
