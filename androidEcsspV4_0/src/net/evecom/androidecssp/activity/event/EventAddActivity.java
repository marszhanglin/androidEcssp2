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

import net.evecom.androidecssp.R;
import net.evecom.androidecssp.activity.pub.TreeListActivity;
import net.evecom.androidecssp.activity.pub.imagescan.PictureSelectActivity;
import net.evecom.androidecssp.base.AfnailPictureActivity;
import net.evecom.androidecssp.base.BaseActivity;
import net.evecom.androidecssp.base.BaseModel;
import net.evecom.androidecssp.bean.FileManageBean;
import net.evecom.androidecssp.util.ShareUtil;
import net.evecom.androidecssp.view.gallery.GalleryFlow;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

/**
 * 
 * 描述
 * 
 * @author Mars zhang
 * @created 2015-11-12 上午10:13:17
 */
public class EventAddActivity extends BaseActivity {
    /** MemberVariables */
    private List<FileManageBean> filebeans = new ArrayList<FileManageBean>();
    /** MemberVariables */
    GalleryFlow mGallery = null;
    /** MemberVariables */
    private String saveResult = "";
    /** MemberVariables */
    private TextView nameTV;// event_add_name
    /** MemberVariables */
    private TextView addrTV;// event_add_addr
    /** MemberVariables */
    private TextView sjjbTV;// event_add_sjjb
    /** MemberVariables */
    private TextView clztTV;// event_add_clzt
    /** MemberVariables */
    private TextView bgrTV;// event_add_bgr
    /** MemberVariables */
    private TextView contentTV;// event_add_content
    /** 数据字典 */
    // HashMap<String, String> statevalueKeyMap = new HashMap<String, String>();
    /** 数据字典 */
    HashMap<String, String> levevalueKeyMap = new HashMap<String, String>();
    /** MemberVariables */
    private String eventTypeid = "";
    /** MemberVariables */
    private String eventTypeName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_add_activity);
        initdata();
        init();
    } 

    /**
     * 
     * 描述
     * 
     * @author Mars zhang
     * @created 2015-11-25 下午12:01:28
     */
    private void init() {
        initGallery();
        nameTV = (TextView) findViewById(R.id.event_add_name);
        addrTV = (TextView) findViewById(R.id.event_add_addr);
        sjjbTV = (TextView) findViewById(R.id.event_add_sjjb);
        clztTV = (TextView) findViewById(R.id.event_add_clzt);
        bgrTV = (TextView) findViewById(R.id.event_add_bgr);
        contentTV = (TextView) findViewById(R.id.event_add_content);
    }

    /**
     * 
     * 描述 初始化数据
     * 
     * @author Mars zhang
     * @created 2015-11-23 下午5:32:52
     */
    private void initdata() {
        getDict("SUDDENEVENT_LEVEL", levevalueKeyMap);
        // getDict("EVENT_DEAL_STATUS", statevalueKeyMap);
        clearFilesRecord();
        clearLocation();
    }
    /**
     * 
     * 描述
     * @author Mars zhang
     * @created 2015-12-24 下午4:02:06
     */
    private void clearLocation() {
        SharedPreferences sp = getApplicationContext().getSharedPreferences("GPS", MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("eventlatitude", "" );
        editor.putString("eventlongitude", "");
        editor.commit();
    }

    /**
     * 
     * 描述 初始化界面时清空数据库文件记录
     * 
     * @author Mars zhang
     * @created 2015-11-23 上午9:50:22
     */
    private void clearFilesRecord() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getDb().deleteAll(FileManageBean.class);
            }
        }).start();
    }

    /**
     * 
     * 描述 初始化gallery
     * 
     * @author Mars zhang
     * @created 2015-11-19 上午10:55:25
     */
    private void initGallery() {
        mGallery = (GalleryFlow) findViewById(R.id.event_add_gallery_flow);
        mGallery.setBackgroundColor(Color.parseColor("#ffffff")); // 背景色
        mGallery.setSpacing(90);// 间距
        mGallery.setMaxRotationAngle(20);// 设置倾斜角度
        mGallery.setFadingEdgeLength(10); // 边框渐变宽度
        mGallery.setGravity(Gravity.CENTER_VERTICAL); // 设置对齐方式
        mGallery.setAdapter(new GalleryAdapter());
        mGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), AfnailPictureActivity.class);
                BaseActivity.pushObjData("filebean", filebeans.get(position), intent);
                startActivityForResult(intent, R.layout.afnail_picture_activity);
            }
        });
    }

    /**
     * 
     * 描述 刷新gallery画廊
     * 
     * @author Mars zhang
     * @created 2015-11-19 上午11:13:14
     */
    private void updateGallery() {
        ((GalleryAdapter) mGallery.getAdapter()).notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1: // 图片界面
                String filePath = data.getStringExtra("filePath");
                // manageFileDataAndListView(filePath);
                break;
            case R.layout.afnail_picture_activity: // 图片预览 在预览处不要有删除 耗性能

                break;
            case 3: // 定位界面

                break;
            case R.layout.picture_select_at: // 图片选择跳转
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        filebeans.removeAll(filebeans);
                        filebeans = getDb().findAll(FileManageBean.class);
                        Message message = new Message();
                        message.what = R.layout.picture_select_at;
                        pubHandler.sendMessage(message);
                    }
                }).start();
                break;
            case R.layout.event_name_add_activity: // 事件名称填写跳转
                BaseModel eventinfo = (BaseModel) getData("eventinfo", data);
                if (null != eventinfo) {
                    nameTV.setText(eventinfo.getStr("eventname"));
                    addrTV.setText(eventinfo.getStr("happenaddress"));
                    bgrTV.setText(eventinfo.getStr("reporterperson") + "-" + eventinfo.getStr("reportertel"));
                    contentTV.setText(eventinfo.getStr("eventcontent"));
                }
                break;
            case R.layout.tree_list_at: // 事件名称填写跳转
               /* eventTypeid = data.getStringExtra("eventTypeid");
                eventTypeName = data.getStringExtra("eventTypeName");
                clztTV.setText(eventTypeName);*/
                eventTypeid = data.getStringExtra("nodeid");
                eventTypeName = data.getStringExtra("nodeName");
                clztTV.setText(eventTypeName);
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 公用pubHandler
     */
    public Handler pubHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case R.layout.picture_select_at:
                    updateGallery();
                    break;
                case 1:

                    break;
                default:
                    break;
            }
        };
    };

    /**
     * 保存提交
     */
    private void submit(View view) {
        if (checkifstop(view)) {
            return;
        }
        HashMap<String, String> hashMap = new HashMap<String, String>();

        hashMap.put("infoReception.eventlever", levevalueKeyMap.get(sjjbTV.getText().toString()));
        hashMap.put("infoReception.eventname", nameTV.getText().toString());
        hashMap.put("infoReception.happenaddress", addrTV.getText().toString());
        // hashMap.put("infoReception.eventstatus",// statevalueKeyMap.get(clztTV.getText().toString()));
        hashMap.put("infoReception.eventtype", eventTypeid); 
        hashMap.put("infoReception.belongunitid", 
                ShareUtil.getString(getApplicationContext(), "PASSNAME", "orgid", "")); 

        String bgrstr = bgrTV.getText().toString();
        String bgr[] = bgrstr.split("-");
        if (bgr.length >= 2) {
            hashMap.put("infoReception.reporterperson", bgr[0]);
            hashMap.put("infoReception.reportertel", bgr[1]);
        }
        hashMap.put("infoReception.eventcontent", contentTV.getText().toString());
        hashMap.put("infoReception.gisy", ShareUtil.getString(getApplicationContext(), "GPS", "eventlatitude", ""));
        hashMap.put("infoReception.gisx", ShareUtil.getString(getApplicationContext(), "GPS", "eventlongitude", ""));
        postdata(hashMap);
    }

    /**
     * 
     * 描述 postdata
     * 
     * @author Mars zhang
     * @created 2015-11-10 下午4:13:51
     * @param entity
     */
    private void postdata(final HashMap<String, String> entity) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                try {
                    saveResult = connServerForResultPost("jfs/ecssp/mobile/eventCtr/eventAdd", entity);
                } catch (ClientProtocolException e) {
                    message.what = MESSAGETYPE_02;
                    Log.e("mars", e.getMessage());
                } catch (IOException e) {
                    message.what = MESSAGETYPE_02;
                    Log.e("mars", e.getMessage());
                }
                if (saveResult.length() > 0) {
                    message.what = MESSAGETYPE_01;
                    String eventId = "";
                    try {
                        BaseModel eventInfo = getObjInfo(saveResult);
                        if (null != eventInfo) {
                            eventId = eventInfo.get("id");
                        }
                    } catch (JSONException e) {
                        Log.e("mars", e.getMessage());
                    }
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("eventId", eventId);
                    postImage(map, filebeans, "jfs/ecssp/mobile/eventCtr/eventFileSave");
                } else {
                    message.what = MESSAGETYPE_02;
                }
                Log.v("mars", saveResult);
                saveHandler.sendMessage(message);
            }
        }).start();
    }

    /**
     * 消息处理机制
     */
    private Handler saveHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGETYPE_01:// 文本保存成功 跳转至事件列表 并提交图片资源
                    Intent intent = new Intent(getApplicationContext(), EventListActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case MESSAGETYPE_02:
                    toast("请重新上报事件！", 1);
                    break;
                default:
                    break;
            }
        };
    };

    /**
     * 
     * 描述 nameOnclick
     * 
     * @author Mars zhang
     * @created 2015-11-23 下午2:16:04
     */
    public void nameOnclick(View view) {
        Intent intent = new Intent(instance, EventNameAddActivity.class);
        intent.putExtra("nameTV", nameTV.getText().toString());
        intent.putExtra("addrTV", addrTV.getText().toString());
        String bgrstr = bgrTV.getText().toString();
        String bgr[] = bgrstr.split("-");
        if (bgr.length >= 2) {
            intent.putExtra("bgrnameTV", bgr[0]);
            intent.putExtra("bgrphoneTV", bgr[1]);
        }
        intent.putExtra("contentTV", contentTV.getText().toString());

        startActivityForResult(intent, R.layout.event_name_add_activity);
    }

    /**
     * 
     * 描述 事件级别点击
     * 
     * @author Mars zhang
     * @created 2015-11-23 下午2:11:25
     * @param view
     */
    public void sjjb(View view) {
        final String[] strs = levevalueKeyMap.keySet().toArray(new String[levevalueKeyMap.size()]);
        Dialog dialog = new AlertDialog.Builder(EventAddActivity.this).setIcon(R.drawable.qq_dialog_default_icon)
                .setTitle("请选择级别").setItems(strs, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sjjbTV.setText(strs[which]);
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }

    /**
     * 
     * 描述 处理状态点击
     * 
     * @author Mars zhang
     * @created 2015-11-23 下午2:11:44
     * @param view
     */
    public void clzt(View view) {
        /*
         * final String[] strs = statevalueKeyMap.keySet().toArray(new
         * String[statevalueKeyMap.size()]); Dialog dialog = new
         * AlertDialog.Builder
         * (EventAddActivity.this).setIcon(R.drawable.qq_dialog_default_icon)
         * .setTitle("请选择级别").setItems(strs, new
         * DialogInterface.OnClickListener() {
         * 
         * @Override public void onClick(DialogInterface dialog, int which) {
         * clztTV.setText(strs[which]); dialog.dismiss(); } }).create();
         * dialog.show();
         */
        Intent intent = new Intent(instance, TreeListActivity.class);
        BaseActivity.pushObjData("title", "事件类型", intent);
        BaseActivity.pushObjData("url", "jfs/ecssp/mobile/eventCtr/getAsyEventTypeTree", intent);
        BaseActivity.pushObjData("rootid", "10000", intent);
        BaseActivity.pushObjData("rootname", "事件类型", intent);
        startActivityForResult(intent, R.layout.tree_list_at);
        /*Intent intent = new Intent(instance, EventTypeActivity.class);
        startActivityForResult(intent, R.layout.event_type_at);*/
    }

    public void bc(View view) {
        submit(view);
    }

    /**
     * 
     * 描述 选择文件点击
     * 
     * @author Mars zhang
     * @created 2015-11-23 下午2:14:11
     * @param view
     */
    public void chooseimage(View view) {
        Intent intent = new Intent(instance, PictureSelectActivity.class);
        startActivityForResult(intent, R.layout.picture_select_at);
    }

    /**
     * 校验
     * 
     * @return
     */
    private Boolean checkifstop(View view) {
        if (nameTV.getText().toString().trim().length() == 0) {
            errorAni(view);
            dialogToastNoCall("请输入事件名称！");
            return true;
        } else if (addrTV.getText().toString().trim().length() == 0) {
            errorAni(view);
            dialogToastNoCall("请输入事发地址！");
            return true;
        } else if (sjjbTV.getText().toString().trim().length() == 0) {
            errorAni((View) sjjbTV.getParent());
            errorAni(view);
            dialogToastNoCall("请选择事件级别！");
            return true;
        } else if (clztTV.getText().toString().trim().length() == 0) {
            errorAni((View) clztTV.getParent());
            errorAni(view);
            dialogToastNoCall("请选择突发事件类型！");
            return true;
        } else if (bgrTV.getText().toString().trim().length() == 0) {
            errorAni((View) bgrTV.getParent());
            errorAni(view);
            dialogToastNoCall("请输入报告人！");
            return true;
        } else if (contentTV.getText().toString().trim().length() == 0) {
            errorAni(view);
            dialogToastNoCall("请输入事件描述！");
            return true;
        }
        return false;

    }

    /**
     * 
     * 描述 gallery的适配器
     * 
     * @author Mars zhang
     * @created 2015-11-19 上午11:05:15
     */
    private class GalleryAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return filebeans.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (null == convertView) {
                convertView = new ImageView(instance);
                convertView.setLayoutParams(new Gallery.LayoutParams(160, 240));// 控件的大小
            }
            ImageView imageView = (ImageView) convertView;
            displayImage(imageView, filebeans.get(position).getFileURL());
            imageView.setScaleType(ScaleType.FIT_XY);
            return imageView;
        }
    }

}
