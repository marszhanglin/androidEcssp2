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
 * �����б�
 * 
 * @author Stark Zhou
 * @created 2016-1-27 ����4:53:34
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
                    // ������һ�������ø��ڵ�Ϊǰ���������ݣ�ͬʱɾ������ĩβ�ļ�¼
                    etype = lastRank.get(lastRank.size() - 1);
                    lastRank.remove(lastRank.size() - 1);
                } else {
                    // ���򷵻����ϼ������ڵ�Ϊnull
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
     * ��ʼ���б�
     * 
     * @author Stark Zhou
     * @created 2016-1-27 ����4:58:46
     */
    private void initList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                HashMap<String, String> map = new HashMap<String, String>();
                String etypeId;
                try {
                    // ������ǵ�һ��,����븸�ڵ�id
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
                        // ����Ĭ�ϵ�һ��
                        if (etypes.size() == 0) {
                            BaseModel root = new BaseModel();
                            root.set("id", "10000");
                            root.set("name", "�¼�����");
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
     * ��Ϣ������
     */
    private Handler etypeListHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGETYPE_01:// ��������ɹ����б�
                    if (etypes.get(0).get("haschildren") != null) {
                        rl.setVisibility(View.VISIBLE);
                    } else {
                        rl.setVisibility(View.GONE);
                    }
                    etypeAdapter = new ETypeAdapter(getApplicationContext(), etypes);
                    etypeList.setAdapter(etypeAdapter);
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
     * �б�������
     * 
     * @author Stark Zhou
     * @created 2016-1-29 ����10:37:14
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
            // �Ƿ����չ�����ò�ͬͼ��
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
                    // ��չ��ʱ�����¼�
                    if (list.get(i).get("haschildren") == null
                            || list.get(i).get("haschildren").toString().equals("true")) {
                        // ��¼��һ���ĸ��ڵ�����
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

}
