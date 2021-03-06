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

import net.evecom.androidecssp.R;
import net.evecom.androidecssp.activity.pub.AsyDeptTreeActivity;
import net.evecom.androidecssp.base.BaseActivity;
import net.evecom.androidecssp.base.BaseModel;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 
 * 联系人列表Activity
 * 
 * @author Stark Zhou
 * @created 2015-11-25 下午12:08:19
 */
public class ContactListActivity extends BaseActivity {
    /** contactListView */
    private ListView contactListView = null;
    /** contactInfos */
    private List<BaseModel> contactInfos = new ArrayList<BaseModel>();
    /** group */
    private BaseModel group = null;
    /** resutArray */
    private String resutArray = "";
    /** deleteResult */
    private EditText searchEdit;
    /** contactAdapter */
    private ContactAdapter contactAdapter;
    /** called */
    private String called = "空";
    /** groupId */
    private String groupId;
    /** textViewContactName */
    private TextView textViewContactName;
    /** groupNameTab */
    private TextView groupNameTab;
    /** groupTv */
    private TextView groupTv;
    /** isClean */
    private boolean isClean;
    /** firstGroup */
    private BaseModel firstGroup = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_list_at);
        Intent intent = getIntent();
        // 记录第一级分组条件
        firstGroup = (BaseModel) getData("group", intent);
        if (firstGroup != null) {
            group = firstGroup;
            groupId = group.get("id");
        } else {
            groupId = (String) getData("groupId", intent);
        }
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        contactListView = (ListView) findViewById(R.id.contact_list_listView_1);
        searchEdit = (EditText) findViewById(R.id.contact_search_edit);
        groupTv = (TextView) findViewById(R.id.group_tv);
        groupNameTab = (TextView) findViewById(R.id.group_name_tab);
        groupNameTab.setText("" + group.get("name"));
        groupNameTab.requestFocus();
        if (group.get("id") != null) {
            groupId = group.get("id").toString();
        }
        String rank = group.get("rank").toString();
        String isLeaf = group.get("isleaf").toString();
        // 第一级隐藏分组
        if (isLeaf.equals("1") && rank.equals("1")) {
            groupTv.setVisibility(View.GONE);
        }
        groupTv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // 若清空了分组条件点击分组后结束当前列表，回到最外层
                if (isClean) {
                    finish();
                } else {// 否则继续按第一层条件选择
                    Intent intent = new Intent(instance, AllGroupActivity.class);
                    BaseActivity.pushData("group", firstGroup, intent);
                    startActivityForResult(intent, R.layout.all_group_at);
                }
            }
        });

        groupNameTab.setOnClickListener(new View.OnClickListener() {
            // 分组显示
            @Override
            public void onClick(View arg0) {
                // 点击时移除组件及分组信息
                groupNameTab.setVisibility(View.GONE);
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) searchEdit.getLayoutParams();
                lp.setMargins(10, 10, 10, 10);
                searchEdit.setLayoutParams(lp);
                firstGroup = null;
                group = firstGroup;
                Mythread postThread = new Mythread();
                postThread.start();
                contactAdapter.notifyDataSetChanged();
                groupTv.setVisibility(View.GONE);
                isClean = true;
            }
        });
        initlist();
    }

    /**
     * 初始化列表
     */
    private void initlist() {
        Mythread postThread = new Mythread();
        postThread.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case R.layout.all_group_at:
                group = (BaseModel) getData("group", data);
                if (group == null) {
                    groupNameTab.setText("" + firstGroup.get("name"));
                } else {
                    groupNameTab.setText("" + group.get("name"));
                }
                initlist();
                contactAdapter.notifyDataSetChanged();
                break;

            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 
     * 描述
     * 
     * @author Stark Zhou
     * @created 2015-12-28 下午6:10:19
     */
    public class Mythread extends Thread {
        public Mythread() {
            super(new Runnable() {
                @Override
                public void run() {

                    Message message = new Message();
                    try {
                        HashMap<String, String> map = new HashMap<String, String>();
                        String searchStr = "" + searchEdit.getText();
                        if (group != null) {
                            // 如果为未分组

                            if (group.get("id") == null) {
                                map.put("searchStr", searchStr);
                                resutArray = connServerForResultPost("jfs/ecssp/mobile/contactCtr/getEmptyGroup", map);
                            }
                            // 否则搜索带上分组及搜索字段
                            else {
                                map.put("groupid", group.get("id").toString());
                                map.put("searchStr", searchStr);
                                resutArray = connServerForResultPost("jfs/ecssp/mobile/contactCtr/getContactByGrp", map);
                            }
                        } else {
                            // 没有分组条件时
                            map.put("searchStr", searchStr);
                            resutArray = connServerForResultPost("jfs/ecssp/mobile/contactCtr/getContactList", map);
                        }
                    } catch (ClientProtocolException e) {
                        message.what = MESSAGETYPE_02;
                    } catch (IOException e) {
                        message.what = MESSAGETYPE_02;
                    }
                    if (resutArray.length() > 0) {
                        try {
                            contactInfos = getObjsInfo(resutArray);
                            if (null == contactInfos) {
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
                    contactListHandler.sendMessage(message);

                }
            });
        }

    }

    /**
     * 搜索按钮点击事件
     */
    public void search(View view) {
        contactInfos.removeAll(contactInfos);
        Mythread postThread = new Mythread();
        postThread.start();
        contactAdapter.notifyDataSetChanged();
    }

    /**
     * contactListHandler
     */
    private Handler contactListHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGETYPE_01:// 数据请求成功画列表
                    if (null == contactAdapter) {
                        contactAdapter = new ContactAdapter(getApplicationContext());
                        contactListView.setAdapter(contactAdapter);
                    }
                    contactAdapter.notifyDataSetChanged();
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
     * @author Stark zhou
     */
    public class ContactAdapter extends BaseAdapter implements ListAdapter {
        /** MemberVariables */
        private Context context;
        /** MemberVariables */
        private LayoutInflater inflater;

        /** ContactAdapter */
        public ContactAdapter(Context context) {
            this.context = context;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return contactInfos == null ? 0 : contactInfos.size();

        }

        @Override
        public Object getItem(int item) {
            return contactInfos.get(item);
        }

        @Override
        public long getItemId(int itemId) {
            return itemId;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            if (null == view) {
                view = inflater.inflate(R.layout.contact_list_item, null);
            }
            // 设置显示的号码，优先显示手机，其次电话
            if (contactInfos.get(i).get("手机") != null) {
                called = contactInfos.get(i).get("手机");
            } else if (contactInfos.get(i).get("固话") != null) {
                called = contactInfos.get(i).get("固话");
            } else {
                called = "空";
            }
            textViewContactName = (TextView) view.findViewById(R.id.contact_list_item_1);
            TextView textViewContactPhone = (TextView) view.findViewById(R.id.contact_list_item_2);
            TextView textViewContactGroup = (TextView) view.findViewById(R.id.contact_list_item_3);
            ImageView callButton = (ImageView) view.findViewById(R.id.callbutton);
            // 优先显示用户别名
            if (contactInfos.get(i).get("alias") == null || contactInfos.get(i).get("alias").equals(null)) {
                textViewContactName.setText("" + contactInfos.get(i).get("name"));
            } else {
                textViewContactName.setText("" + contactInfos.get(i).get("alias"));
            }
            textViewContactPhone.setText("" + called);
            textViewContactGroup.setText(ifnull("" + contactInfos.get(i).get("groupname"), "未分组"));

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(instance, ContactInfoActivity.class);
                    ContactInfoActivity.pushData("cantactInfo", contactInfos.get(i), intent);
                    ContactGroupActivity.pushData("group", group, intent);
                    startActivity(intent);
                }
            });
            callButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + called));
                    startActivity(intent);
                }
            });

            return view;
        }
    }

}
