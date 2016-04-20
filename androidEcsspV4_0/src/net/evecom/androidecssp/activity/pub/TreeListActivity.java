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
import android.graphics.Color;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
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
public class TreeListActivity extends BaseActivity {
    /** listview */
    private ListView nodeList;
    /** resultArray */
    private String resultArray;
    /** nodes */
    private List<BaseModel> nodes;
    /** nodeAdapter */
    private NodeAdapter nodeAdapter;
    /** node */
    private BaseModel node;
    /** lastRank */
    private List<BaseModel> lastRank;
    /** rl */
    private RelativeLayout rl;
    /** url */
    private String url;
    /** rootid */
    private String rootid;
    /** rootname */
    private String rootname;
    /**selectedNode*/
    private BaseModel selectedNode;
    /**title*/
    private String title;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tree_list_at);
        Intent intent = getIntent();
        title = (String) getData("title", intent);
        url = (String) getData("url", intent);
        rootid = (String) getData("rootid", intent);
        rootname = (String) getData("rootname", intent);
        node = null;
        lastRank = new ArrayList<BaseModel>();
        TextView treeTitle = (TextView) findViewById(R.id.tree_title);
        treeTitle.setText(title);
        nodeList = (ListView) findViewById(R.id.area_listview);
        nodeList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        rl = (RelativeLayout) findViewById(R.id.area_bottom_block);
        ImageView lastnode = (ImageView) findViewById(R.id.last_area_btn);
        rl.setVisibility(View.GONE);
        lastnode.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (lastRank.size() > 0) {
                    // 返回上一级，设置父节点为前两级的数据，同时删除数组末尾的记录
                    node = lastRank.get(lastRank.size() - 1);
                    lastRank.remove(lastRank.size() - 1);
                } else {
                    // 否则返回最上级，父节点为null
                    node = null;
                }
                initList();
                nodeAdapter.notifyDataSetChanged();
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
                String nodeId;
                try {
                    // 如果不是第一级,则加入父节点id
                    if (node != null) {
                        nodeId = node.getStr("id");
                        map.put("currentid", nodeId);
                    }
                    resultArray = connServerForResultPost(url, map);
                } catch (ClientProtocolException e) {
                    message.what = MESSAGETYPE_02;
                } catch (IOException e) {
                    message.what = MESSAGETYPE_02;
                }
                if (resultArray.length() > 0) {
                    try {
                        nodes = getObjsInfo(resultArray);
                        // 设置默认第一级
                        if (nodes.size() == 0) {
                            BaseModel root = new BaseModel();
                            root.set("id", rootid);
                            root.set("name", rootname);
                            nodes.add(0, root);
                        }
                        if (null == nodes) {
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
                nodeListHandler.sendMessage(message);
            }
        }).start();
    }

    /**
     * 消息处理器
     */
    private Handler nodeListHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGETYPE_01:// 数据请求成功画列表
                    if (nodes.get(0).get("haschildren") != null) {
                        rl.setVisibility(View.VISIBLE);
                    } else {
                        rl.setVisibility(View.GONE);
                    }
                    nodeAdapter = new NodeAdapter(getApplicationContext(), nodes);
                    nodeList.setAdapter(nodeAdapter);
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
    public class NodeAdapter extends BaseAdapter implements ListAdapter {
        /** MemberVariables */
        private Context context;
        /** MemberVariables */
        private LayoutInflater inflater;
        /** MemberVariables */
        private List<BaseModel> list;
        /** lastView */
        View lastView;
        /**lastCheck*/
        CheckBox lastCheck;

        public NodeAdapter(Context context, List<BaseModel> list) {
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
                view = inflater.inflate(R.layout.tree_list_item, null);
            }

            final CheckBox treeNode=(CheckBox) view.findViewById(R.id.node_check);
            TextView nodeName = (TextView) view.findViewById(R.id.area_list_name);
            nodeName.setText("" + list.get(i).get("name"));
            ImageView nodeImg = (ImageView) view.findViewById(R.id.area_list_img);
            // 是否可以展开设置不同图标
            if (list.get(i).get("haschildren") != null && list.get(i).get("haschildren").toString().equals("false")) {
                nodeImg.setBackgroundResource(R.drawable.group_2);
            } else {
                nodeImg.setBackgroundResource(R.drawable.group_1);
            }

           
            treeNode.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                
                @Override
                public void onCheckedChanged(CompoundButton c, boolean isChecked) {
                    //如果已经有选中的节点并且选中的是不同的节点，则取消上一个节点的选择状态，模拟单选效果
                    if(lastCheck!=null&&lastCheck!=c){
                        lastCheck.setChecked(false);
                    }
                    lastCheck=(CheckBox) c;
                    //如果节点被选中，则记录当前节点的数据为返回数据
                    if(isChecked){
                        selectedNode=list.get(i);
                        ((View) treeNode.getParent().getParent().getParent()).setBackgroundColor(
                                Color.parseColor("#FFF7B2"));
                    }else{
                        ((View) treeNode.getParent().getParent().getParent()).setBackgroundColor(
                                Color.parseColor("#ffffff"));
                    }
                }
            });
            
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 可展开时触发事件
                    if (lastView != null) {
                        lastView.setBackgroundColor(Color.parseColor("#ffffff"));
                    }
                    v.setBackgroundColor(Color.parseColor("#FFF7B2"));
                    lastView = v;
                    if (list.get(i).get("haschildren") == null
                            || list.get(i).get("haschildren").toString().equals("true")) {
                        // 记录上一级的父节点数据
                        if (node != null) {
                            lastRank.add(node);
                        }
                        node = list.get(i);
                        initList();
                        nodeAdapter.notifyDataSetChanged();
                    }else{
                        //最细级节点点击时选中
                        treeNode.setChecked(!treeNode.isChecked());
                    }
                }
            });

            nodeImg.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    AnimationUtil.AniZoomIn(v);
                    Intent intent = new Intent();
                    intent.putExtra("nodeid", list.get(i).get("id").toString());
                    intent.putExtra("nodeName", list.get(i).get("name").toString());
                    intent.putExtra("title", title);
                    setResult(1, intent);
                    finish();
                }
            });

            return view;
        }
    }
    /**
     * 
     * 节点选择
     *
     * @author Stark Zhou
     * @created 2016-2-18 上午10:55:55
     * @param v
     */
    public void nodeSelect(View v){
        if(selectedNode!=null){
        Intent intent = new Intent();
        intent.putExtra("nodeid", selectedNode.get("id").toString());
        intent.putExtra("nodeName", selectedNode.get("name").toString());
        intent.putExtra("title", title);
        setResult(1, intent);
        finish();
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
