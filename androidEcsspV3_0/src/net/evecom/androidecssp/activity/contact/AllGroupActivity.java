/*
 * Copyright (c) 2005, 2015, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package net.evecom.androidecssp.activity.contact;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import net.evecom.androidecssp.R;
import net.evecom.androidecssp.base.BaseActivity;
import net.evecom.androidecssp.base.BaseModel;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * ����
 * 
 * @author Stark Zhou
 * @created 2015-11-16 ����2:06:54
 */
public class AllGroupActivity extends BaseActivity {
    /** contactGroupView */
    private ListView contactGroupView = null;
    /** groups */
    private List<BaseModel> groups = null;
    /** ranks */
    private List<BaseModel> ranks = new ArrayList<BaseModel>();
    /** resutArray */
    private String resutArray = "";
    /** group */
    private BaseModel group;
    /** groupId */
    private String groupId;
    /** groupAdapter */
    private GroupAdapter groupAdapter;
    /** groupBottom */
    private ImageView groupBottom;
    /** lastGroup */
    private BaseModel lastGroup;
    /** rank */
    private int rank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_group_at);
        Intent intent = getIntent();
        group = (BaseModel) getData("group", intent);
        groupBottom = (ImageView) findViewById(R.id.group_bottom);
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.group_bottom_block);
        // �ж���Ϊ��һ���������ʾ��һ����ť
        if (group != null) {
            groupId = group.getStr("id");
        } else {
            rl.setVisibility(View.GONE);
        }
        init();
    }

    /**
     * 
     * ��ʼ��
     * 
     * @author Stark Zhou
     * @created 2015-12-30 ����2:22:16
     */
    private void init() {
        contactGroupView = (ListView) findViewById(R.id.all_group_listView_1);
        initlist();
        groupBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // �������2�ķ��鷵����һ�������򷵻���ϵ���б�
                if (rank > 2) {
                    group = lastGroup;
                    initlist();
                    groupAdapter.notifyDataSetChanged();
                } else {
                    Intent intent = new Intent();
                    setResult(4, intent);
                    finish();
                }
            }
        });

    }

    /**
     * 
     * ��д���ط���
     * 
     * @author Stark Zhou
     * @created 2015-12-30 ����2:23:38
     * @param view
     */
    public void back(View view) {
        Intent intent = new Intent();
        setResult(4, intent);
        finish();
    }

    /**
     * 
     * ��д���ط���
     * 
     * @author Stark Zhou
     * @created 2015-12-30 ����2:23:38
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

    /**
     * ��ʼ���б�
     */
    private void initlist() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                HashMap<String, String> map = new HashMap<String, String>();
                try {
                    // �ж��Ƿ��з�������
                    if (group != null) {
                        groupId = group.get("id");
                        map.put("groupid", groupId);
                        resutArray = connServerForResultPost("jfs/ecssp/mobile/contactCtr/getGroupByParentId", map);
                    } else {
                        resutArray = connServerForResultPost("jfs/ecssp/mobile/contactCtr/getGroupList", null);
                    }
                } catch (ClientProtocolException e) {
                    message.what = MESSAGETYPE_02;
                    Log.e("mars", e.getMessage());
                } catch (IOException e) {
                    message.what = MESSAGETYPE_02;
                    Log.e("mars", e.getMessage());
                }
                if (resutArray.length() > 0) {
                    try {
                        groups = getObjsInfo(resutArray);
                        rank = groups.get(0).getInt("rank");
                        // ����δ���飬���ӵ�list�ĵ�һ��
                        if (rank == 1) {
                            BaseModel emptyGroup = new BaseModel();
                            emptyGroup.set("name", "δ����");
                            emptyGroup.set("rank", 1);
                            emptyGroup.set("isleaf", "1");
                            emptyGroup.set("id", null);
                            groups.add(0, emptyGroup);
                        }
                        if (null == groups) {
                            message.what = MESSAGETYPE_02;
                        } else {
                            message.what = MESSAGETYPE_01;
                        }
                    } catch (JSONException e) {
                        message.what = MESSAGETYPE_02;
                        Log.e("stark", e.getMessage());
                    }
                } else {
                    message.what = MESSAGETYPE_02;
                }
                Log.v("mars", resutArray);
                contactGroupHandler.sendMessage(message);

            }
        }).start();
    }

    /**
     * ��Ϣ������
     */
    private Handler contactGroupHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGETYPE_01:// ��������ɹ����б�
                    groupAdapter = new GroupAdapter(getApplicationContext(), groups);
                    contactGroupView.setAdapter(groupAdapter);
                    ranks.add(group);
                    break;
                case MESSAGETYPE_02:
                    toast("�����¼�����", 1);
                    break;
                default:
                    break;
            }

        };
    };

    /**
     * 
     * �����б�������
     * 
     * @author Stark Zhou
     * @created 2015-12-30 ����2:25:12
     */
    public class GroupAdapter extends BaseAdapter implements ListAdapter {
        /** MemberVariables */
        private Context context;
        /** MemberVariables */
        private LayoutInflater inflater;
        /** MemberVariables */
        private List<BaseModel> list;

        public GroupAdapter(Context context, List<BaseModel> list) {
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
                view = inflater.inflate(R.layout.contact_group_item, null);
            }
            TextView textViewGroupName = (TextView) view.findViewById(R.id.contact_group_item);
            textViewGroupName.setText("" + list.get(i).get("name"));

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // ���Ϊ��һ����create��ϵ���б�
                    if (list.get(i).get("rank").toString().equals("1")) {
                        Intent intent = new Intent(instance, ContactListActivity.class);
                        BaseActivity.pushData("group", list.get(i), intent);
                        startActivity(intent);
                    }
                    // ���������ϸ�������չ����һ��
                    else if (list.get(i).get("isleaf").toString().equals("0")) {
                        lastGroup = group;
                        group = list.get(i);
                        initlist();
                        groupAdapter.notifyDataSetChanged();
                    } else {// ���򷵻����ݵ���ϵ���б�
                        Intent intent = new Intent();
                        BaseActivity.pushData("group", list.get(i), intent);
                        setResult(4, intent);
                        finish();
                    }
                }
            });
            RelativeLayout grpTocon = (RelativeLayout) view.findViewById(R.id.group_to_contact);
            // �鵽��ϵ�˰�ť������
            grpTocon.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // ���Ϊ��һ���������ϵ���б�
                    if (list.get(i).get("rank").toString().equals("1")) {
                        Intent intent = new Intent(instance, ContactListActivity.class);
                        BaseActivity.pushData("group", list.get(i), intent);
                        startActivity(intent);
                    }
                    // ������뷵�����ݵ���ϵ���б�
                    else {
                        Intent intent = new Intent();
                        BaseActivity.pushData("group", list.get(i), intent);
                        setResult(1, intent);
                        finish();
                    }
                }
            });
            return view;
        }
    }

}