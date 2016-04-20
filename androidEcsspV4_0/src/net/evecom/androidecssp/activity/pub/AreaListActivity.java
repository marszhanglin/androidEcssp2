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
 * �����б�
 * 
 * @author Stark Zhou
 * @created 2016-1-27 ����4:53:34
 */
public class AreaListActivity extends BaseActivity {
    /** listview */
    private ListView areaList;
    /** resultArray */
    private String resultArray;
    /** areas */
    private List<BaseModel> areas;
    /** areaAdapter */
    private AreaAdapter areaAdapter;
    /** area */
    private BaseModel area;
    /** lastRank */
    private List<BaseModel> lastRank;
    /** rl */
    private RelativeLayout rl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tree_list_at);
        area = null;
        lastRank = new ArrayList<BaseModel>();
        areaList = (ListView) findViewById(R.id.area_listview);
        rl = (RelativeLayout) findViewById(R.id.area_bottom_block);
        ImageView lastArea = (ImageView) findViewById(R.id.last_area_btn);
        rl.setVisibility(View.GONE);
        lastArea.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (lastRank.size() > 0) {
                    // ������һ�������ø��ڵ�Ϊǰ���������ݣ�ͬʱɾ������ĩβ�ļ�¼
                    area = lastRank.get(lastRank.size() - 1);
                    lastRank.remove(lastRank.size() - 1);
                } else {
                    // ���򷵻����ϼ������ڵ�Ϊnull
                    area = null;
                }
                initList();
                areaAdapter.notifyDataSetChanged();
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
                String areaId;
                try {
                    // ������ǵ�һ��,����븸�ڵ�id
                    if (area != null) {
                        areaId = area.getStr("id");
                        map.put("currentid", areaId);
                    }
                    resultArray = connServerForResultPost("jfs/ecssp/mobile/pubCtr/getAsyAreaTree", map);
                } catch (ClientProtocolException e) {
                    message.what = MESSAGETYPE_02;
                } catch (IOException e) {
                    message.what = MESSAGETYPE_02;
                }
                if (resultArray.length() > 0) {
                    try {
                        areas = getObjsInfo(resultArray);
                        // ����Ĭ�ϵ�һ��
                        if (areas.size() == 0) {
                            BaseModel root = new BaseModel();
                            root.set("id", "350000");
                            root.set("name", "����ʡ");
                            areas.add(0, root);
                        }
                        if (null == areas) {
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
                areaListHandler.sendMessage(message);
            }
        }).start();
    }

    /**
     * ��Ϣ������
     */
    private Handler areaListHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGETYPE_01:// ��������ɹ����б�
                    if (areas.get(0).get("haschildren") != null) {
                        rl.setVisibility(View.VISIBLE);
                    } else {
                        rl.setVisibility(View.GONE);
                    }
                    areaAdapter = new AreaAdapter(getApplicationContext(), areas);
                    areaList.setAdapter(areaAdapter);
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
    public class AreaAdapter extends BaseAdapter implements ListAdapter {
        /** MemberVariables */
        private Context context;
        /** MemberVariables */
        private LayoutInflater inflater;
        /** MemberVariables */
        private List<BaseModel> list;

        public AreaAdapter(Context context, List<BaseModel> list) {
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
            TextView areaName = (TextView) view.findViewById(R.id.area_list_name);
            areaName.setText("" + list.get(i).get("name"));
            ImageView areaImg = (ImageView) view.findViewById(R.id.area_list_img);
            // �Ƿ����չ�����ò�ͬͼ��
            if (list.get(i).get("haschildren") != null && list.get(i).get("haschildren").toString().equals("false")) {
                areaImg.setBackgroundResource(R.drawable.group_2);
            } else {
                areaImg.setBackgroundResource(R.drawable.group_1);
            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AnimationUtil.AniZoomIn(v);
                    Intent intent = new Intent();
                    intent.putExtra("areaid", list.get(i).get("id").toString());
                    intent.putExtra("areaName", list.get(i).get("name").toString());
                    setResult(1, intent);
                    finish();
                }
            });

            areaImg.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // ��չ��ʱ�����¼�
                    if (list.get(i).get("haschildren") == null
                            || list.get(i).get("haschildren").toString().equals("true")) {
                        // ��¼��һ���ĸ��ڵ�����
                        if (area != null) {
                            lastRank.add(area);
                        }
                        AnimationUtil.AniZoomIn(v);
                        area = list.get(i);
                        initList();
                        areaAdapter.notifyDataSetChanged();
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
