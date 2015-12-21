/*
 * Copyright (c) 2005, 2015, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package net.evecom.androidecssp.activity.contact;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import net.evecom.androidecssp.R;
import net.evecom.androidecssp.base.BaseActivity;
import net.evecom.androidecssp.base.BaseModel;
import net.evecom.androidecssp.base.IPickCallback;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
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
    /** contactListItem */
    private TextView contactListItem = null;
    /** contactInfos */
    private List<BaseModel> contactInfos = null;
    /** group */
    private BaseModel group = null;
    /** resutArray */
    private String resutArray = "";
    /** deleteResult */
    private String deleteResult = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_list_at);
        Intent intent = getIntent();
        group = (BaseModel) getData("group", intent);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        contactListView = (ListView) findViewById(R.id.contact_list_listView_1);
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
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("groupid", group.getStr("id"));
                try {
                    resutArray = connServerForResultPost("jfs/ecssp/mobile/contactCtr/getContactList", hashMap);
                } catch (ClientProtocolException e) {
                    message.what = MESSAGETYPE_02;
                    Log.e("mars", e.getMessage());
                } catch (IOException e) {
                    message.what = MESSAGETYPE_02;
                    Log.e("mars", e.getMessage());
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
                        Log.e("mars", e.getMessage());
                    }
                } else {
                    message.what = MESSAGETYPE_02;
                }
                Log.v("mars", resutArray);
                contactListHandler.sendMessage(message);

            }
        }).start();

    }

    /**
     * 
     * 新增按钮
     * 
     * @author Stark Zhou
     * @created 2015-11-16 上午9:22:09
     * @param view
     */
    public void addcontact(View view) {
        Intent intent = new Intent(instance, ContactAddActivity.class);
        ContactAddActivity.pushData("group", group, intent);
        startActivity(intent);
    }

    /**
     * contactListHandler
     */
    private Handler contactListHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGETYPE_01:// 数据请求成功画列表
                    ContactAdapter contactAdapter = new ContactAdapter(getApplicationContext(), contactInfos);
                    contactListView.setAdapter(contactAdapter);
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
        /** MemberVariables */
        private List<BaseModel> list;

        /** ContactAdapter */
        public ContactAdapter(Context context, List<BaseModel> list) {
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
                view = inflater.inflate(R.layout.contact_list_item, null);
            }
            TextView textViewContactName = (TextView) view.findViewById(R.id.contact_list_item_1);
            TextView textViewContactPhone = (TextView) view.findViewById(R.id.contact_list_item_2);
            TextView textViewContactEmail = (TextView) view.findViewById(R.id.contact_list_item_3);
            ImageView callButton = (ImageView) view.findViewById(R.id.callbutton);
            textViewContactName.setText("" + list.get(i).get("name"));
            textViewContactPhone.setText("" + list.get(i).get("mobiletel"));
            textViewContactEmail.setText("邮箱：" + ifnull(list.get(i).get("email") + "", ""));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(instance, ContactInfoActivity.class);
                    ContactInfoActivity.pushData("cantactInfo", list.get(i), intent);
                    ContactInfoActivity.pushData("group", group, intent);
                    startActivity(intent);
                    Log.v("mars", "点击了列表" + list.get(i).get("name"));
                }
            });
            callButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + list.get(i).get("mobiletel")));
                    startActivity(intent);
                    // Log.v("stark", "拨打电话"+list.get(i).get("mobiletel"));
                }
            });

            return view;
        }
    }
}
