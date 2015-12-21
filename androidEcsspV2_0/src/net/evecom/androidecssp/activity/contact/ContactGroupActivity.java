/*
 * Copyright (c) 2005, 2015, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package net.evecom.androidecssp.activity.contact;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import net.evecom.androidecssp.R;
import net.evecom.androidecssp.activity.contact.ContactListActivity.ContactAdapter;
import net.evecom.androidecssp.base.BaseActivity;
import net.evecom.androidecssp.base.BaseModel;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 联系人分组Activity
 *
 * @author Stark Zhou
 * @created 2015-11-16 下午2:06:54
 */
public class ContactGroupActivity extends BaseActivity{
    /**contactGroupView*/
    private ListView contactGroupView=null;
    /**groups*/
    private List<BaseModel> groups=null;
    /**resutArray*/
    private String resutArray="";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_group_at);
        init();
        
    }
    /**
     * 初始化listview
     */   
    private void init() {
        contactGroupView=(ListView) findViewById(R.id.contact_group_listView_1);
        initlist();
    }
    
    /**
     * 初始化列表
     */
    private void initlist(){ 
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message= new Message();
                
                try {
                    resutArray=connServerForResultPost("jfs/ecssp/mobile/contactCtr/getGroupList", null);
                } catch (ClientProtocolException e) {
                    message.what=MESSAGETYPE_02;
                    Log.e("mars", e.getMessage());
                } catch (IOException e) {
                    message.what=MESSAGETYPE_02;
                    Log.e("mars", e.getMessage());
                }
                if(resutArray.length()>0){
                    try {
                        groups=getObjsInfo(resutArray);
                        if(null==groups){
                            message.what=MESSAGETYPE_02;
                        }else{
                            message.what=MESSAGETYPE_01;
                        }
                    } catch (JSONException e) {
                        message.what=MESSAGETYPE_02;
                        Log.e("stark", e.getMessage());
                    }
                }else{
                    message.what=MESSAGETYPE_02;
                }
                Log.v("mars", resutArray);
                contactGroupHandler.sendMessage(message);
                
            }
        }).start();        
    }
    /**
     * 消息处理器
     */
    private Handler contactGroupHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MESSAGETYPE_01://数据请求成功画列表
                GroupAdapter groupAdapter=new GroupAdapter(getApplicationContext(), groups);
                contactGroupView.setAdapter(groupAdapter);
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
    public class GroupAdapter extends BaseAdapter implements ListAdapter {
        /** MemberVariables */
        private Context context;
        /** MemberVariables */
        private LayoutInflater inflater;
        /** MemberVariables */
        private List<BaseModel> list;
        /** GroupAdapter */
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
            textViewGroupName.setText(""+list.get(i).get("groupname"));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(instance, ContactListActivity.class);
                    ContactListActivity.pushData("group",list.get(i),intent);
                    startActivity(intent);
                    Log.v("stark", "点击了列表"+list.get(i).get("name"));
                }
            });
            
            return view;
        }
    }
    
}
