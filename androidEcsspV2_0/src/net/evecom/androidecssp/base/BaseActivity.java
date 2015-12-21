/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package net.evecom.androidecssp.base;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import net.evecom.androidecssp.R;
import net.evecom.androidecssp.bean.FileManageBean;
import net.evecom.androidecssp.gps.TDTLocation222;
import net.evecom.androidecssp.util.ShareUtil;
import net.mutil.util.HttpUtil;
import net.mutil.util.PhoneUtil;
import net.tsz.afinal.FinalDb;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

/**
 * ������Activity�� 2015-2-15����7:21:37 ��BaseActivity
 * 
 * @author Mars zhang
 * 
 */
public class BaseActivity extends Activity {
    /** ����״̬ */
    protected static final int MESSAGETYPE_01 = 0x0001;
    /** ����״̬ */
    protected static final int MESSAGETYPE_02 = 0x0002;
    /** ����״̬ */
    protected static final int MESSAGETYPE_03 = 0x0003;
    /** ����״̬ */
    protected static final int MESSAGETYPE_04 = 0x0004;
    /** ����״̬ */
    protected static final int MESSAGETYPE_05 = 0x0005;
    /** ����״̬ */
    protected static final int MESSAGETYPE_06 = 0x0006;
    /** �Զ��彻�����ݼ� */
    public static HashMap<Long, Object> contextHashMap = new HashMap<Long, Object>();
    /** ʵ����� */
    public static BaseActivity instance = null;
    /** ��ֹ������ת�������������������־λ if(isdisable)return; */
    protected boolean isdisable = true;
    /** FinalDb */
    private FinalDb db;
    /** view */
    public View view = null;
    /** view */
    public StringBuilder requestCode = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ������������
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        // ��Ҫ����
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        instance = this;
        db = BaseApplication.instence.db;
        requestCode.append("sys_code=");
        requestCode.append(ShareUtil.getString(instance, "PASSNAME", "code", ""));
        requestCode.append("&sys_imei=");
        requestCode.append(PhoneUtil.getInstance().getImei(instance));
        requestCode.append("&sys_loginName=");
        requestCode.append(ShareUtil.getString(instance, "PASSNAME", "username", ""));
    }

    @Override
    protected void onStart() {
        System.out.println(instance.getLocalClassName() + "onstart");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                }
                isdisable = false;
            }
        }).start();
        super.onStart();
    }

    @Override
    protected void onStop() {
        isdisable = true;
        super.onStop();
    }

    /**
     * �������ʾ��Ϣ
     * 
     * @param errorMsg
     */
    protected void DialogToast(String errorMsg, final ICallback callback) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("��ʾ��Ϣ");
        builder1.setIcon(R.drawable.qq_dialog_default_icon);// ͼ��
        builder1.setMessage("" + errorMsg);
        builder1.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
            // @Override
            public void onClick(DialogInterface dialog, int which) {
                if (null != callback) {
                    callback.execute();
                }
            }
        });
        builder1.show();
    }

    /**
     * �������ʾ��Ϣ
     * 
     * @param errorMsg
     */
    protected void DialogPickToast(String title, String msg, String ymsg, String nmsg, final IPickCallback callback) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle(title);
        builder1.setIcon(R.drawable.qq_dialog_default_icon);// ͼ��
        builder1.setMessage(msg);
        builder1.setPositiveButton(ymsg, new DialogInterface.OnClickListener() {
            // @Override
            public void onClick(DialogInterface dialog, int which) {
                if (null != callback) {
                    callback.yes();
                }
            }
        });
        builder1.setNegativeButton(nmsg, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callback.no();
            }
        });
        builder1.show();
    }

    /**
     * �������ʾ��Ϣ
     * 
     * @param errorMsg
     */
    protected void DialogToastNoCall(String errorMsg) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("��ʾ��Ϣ");
        builder1.setIcon(R.drawable.qq_dialog_default_icon);// ͼ��
        builder1.setMessage("" + errorMsg);
        builder1.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
            // @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder1.show();
    }

    /** ��˾ */
    protected void toast(String strMsg, int L1S0) {
        Toast.makeText(getApplicationContext(), strMsg, L1S0).show();
    }

    /** ��˾ */
    protected void toastInOtherThread(String strMsg, int L1S0) {
        Looper.prepare();
        Toast.makeText(getApplicationContext(), strMsg, L1S0).show();
        Looper.loop();
    }

    /**
     * 
     * @param http
     *            get����
     * @return
     * @throws IOException
     * @throws ClientProtocolException
     */
    protected String connServerForResult(String strUrl) throws Exception {
        // HttpGet����
        HttpGet httpRequest = new HttpGet(strUrl);
        String strResult = "";
        // HttpClient����
        BasicHttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 5000);// ���ó�ʱʱ��
        HttpClient httpClient = new DefaultHttpClient(httpParams);
        HttpResponse httpResponse = httpClient.execute(httpRequest);
        if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            // ȡ�÷��ص�����
            strResult = EntityUtils.toString(httpResponse.getEntity());
        }

        return strResult;
    }

    /**
     * 
     * ���� ��ȡ��̨�����ֵ�����
     * 
     * @author Mars zhang
     * @created 2015-11-25 ����2:08:51
     * @param dictkey
     * @param statehashmap
     */
    protected void getDict(final String dictkey, final HashMap<String, String> statehashmap) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HashMap<String, String> entityMap = new HashMap<String, String>();
                    entityMap.put("dictkey", dictkey);
                    String result = connServerForResultPost("jfs/ecssp/mobile/pubCtr/getDictByKey", entityMap);
                    List<BaseModel> baseModels = getObjsInfo(result);
                    for (int i = 0; i < baseModels.size(); i++) {
                        statehashmap.put(baseModels.get(i).get("name") + "", baseModels.get(i).get("dictvalue") + "");
                    }
                } catch (ClientProtocolException e) {
                    Log.v("mars", e.getMessage());
                } catch (IOException e) {
                    Log.v("mars", e.getMessage());
                } catch (JSONException e) {
                    Log.v("mars", e.getMessage());
                }
            }
        }).start();
    }

    /**
     * 
     * ���� ��ȡ�����ֵ��ʽ������ ���Ǹ����ݲ��Ǵ��������ֵ���
     * 
     * @author Mars zhang
     * @created 2015-12-11 ����9:40:22
     * @param postUrl
     * @param dictkey
     * @param statehashmap
     */
    protected void getLikeDict(final String postUrl, final HashMap<String, String> entityMap,
            final HashMap<String, String> statehashmap) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String result = connServerForResultPost(postUrl, entityMap);
                    List<BaseModel> baseModels = getObjsInfo(result);
                    for (int i = 0; i < baseModels.size(); i++) {
                        statehashmap.put(baseModels.get(i).get("name") + "", baseModels.get(i).get("dictvalue") + "");
                    }
                } catch (ClientProtocolException e) {
                    Log.v("mars", e.getMessage());
                } catch (IOException e) {
                    Log.v("mars", e.getMessage());
                } catch (JSONException e) {
                    Log.v("mars", e.getMessage());
                }
            }
        }).start();
    }

    /**
     * 
     * ���� ��֪dictValue dictKey ��TextView����DictName
     * 
     * @author Mars zhang
     * @created 2015-11-25 ����9:50:55
     * @param dictKey
     * @param value
     * @param view
     */
    protected void setDictNameByValueToView(final String dictKey, final String dictValue, final TextView view) {
        final Handler mHandler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case MESSAGETYPE_01:
                        view.setText(msg.getData().getString("dictname"));
                        break;
                    default:
                        break;
                }
            };
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HashMap<String, String> entityMap = new HashMap<String, String>();
                    entityMap.put("dictkey", dictKey);
                    String result = connServerForResultPost("jfs/ecssp/mobile/pubCtr/getDictByKey", entityMap);
                    List<BaseModel> baseModels = getObjsInfo(result);
                    HashMap<String, String> keyValehashmap = new HashMap<String, String>();
                    for (int i = 0; i < baseModels.size(); i++) {
                        keyValehashmap.put(baseModels.get(i).get("dictvalue") + "", baseModels.get(i).get("name") + "");
                    }
                    String dictname = ifnull(keyValehashmap.get(dictValue), "");
                    Message message = new Message();
                    Bundle mbundle = new Bundle();
                    mbundle.putString("dictname", dictname);
                    message.setData(mbundle);
                    message.what = MESSAGETYPE_01;
                    mHandler.sendMessage(message);
                } catch (ClientProtocolException e) {
                    Log.v("mars", e.getMessage());
                } catch (IOException e) {
                    Log.v("mars", e.getMessage());
                } catch (JSONException e) {
                    Log.v("mars", e.getMessage());
                }
            }
        }).start();

    }

    protected String connServerForResultPost(String strUrl, HashMap<String, String> entityMap)
            throws ClientProtocolException, IOException {
        String strResult = "";
        if (null == entityMap) {
            entityMap = new HashMap<String, String>();
        }
        String code = ShareUtil.getString(instance, "PASSNAME", "code", "");
        // code = code.replace("+", "%2B");
        if (code.length() > 0) {
            entityMap.put("sys_code", code);
        }
        entityMap.put("sys_imei", PhoneUtil.getInstance().getImei(instance));
        entityMap.put("sys_loginName", ShareUtil.getString(instance, "PASSNAME", "username", ""));
        strResult = HttpUtil.connServerForResultPost( strUrl, entityMap);
        return strResult;
    }

    /**
     * �ϴ�ͼƬ
     * 
     * @param taskresponseId
     */
    public void postImage(HashMap<String, String> map, List<FileManageBean> fileList, String requestUrl) {
        if (null == map) {
            return;
        }
        if (null == fileList || fileList.size() == 0) {
            return;
        }
        AjaxParams params = new AjaxParams();
        Object[] strings = map.keySet().toArray();
        for (int i = 0; i < strings.length; i++) {
            params.put((String) strings[i], map.get(strings[i]));
        }
        requestUrl += "?" + requestCode.toString();
        for (int i = 0; i < fileList.size(); i++) {
            try {
                params.put("file" + i, new File(fileList.get(i).getFile_URL()));
            } catch (FileNotFoundException e) {
                if (null != e) {
                    e.printStackTrace();
                }
            } // �ϴ��ļ�
        }
        FinalHttp fh = new FinalHttp();
        fh.post(HttpUtil.getPCURL() + requestUrl, params, new AjaxCallBack<String>() {
            @Override
            public void onLoading(long count, long current) {
                Log.v("mars", current + "/" + count);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                Log.v("mars", "�ļ�����ʧ�ܣ����������Ƿ����");
                super.onFailure(t, errorNo, strMsg);
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                Log.v("mars", "�ļ��ϴ��ɹ�:" + t);
            }
        });
    }

    /**
     * 
     * ���� ����
     * 
     * @author Mars zhang
     * @created 2015-11-23 ����4:30:15
     * @param view
     */
    public void fh(View view) {
        if (isdisable)
            return;
        this.finish();
    }

    /**
     * 
     * ���� ��λ���
     * 
     * @author Mars zhang
     * @created 2015-11-23 ����3:34:58
     * @param view
     */
    public void dw(View view) {
        Intent intent = new Intent(instance, TDTLocation222.class);
        startActivityForResult(intent, 1000);

    }

    /**
     * ����json�������� ��baseModel
     * 
     */
    public static BaseModel getObjInfo(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);
        BaseModel baseModel = new BaseModel();
        Iterator<String> iterators = jsonObject.keys();
        for (int j = 0; iterators.hasNext(); j++) {
            String key = iterators.next();
            baseModel.set(key, jsonObject.get(key));
        }
        return baseModel;
    }

    /**
     * ���� json����� ��baseModel
     * 
     */
    public static List<BaseModel> getObjsInfo(String jsonString) throws JSONException {
        List<BaseModel> list = new ArrayList<BaseModel>();
        JSONArray jsonArray = null;
        jsonArray = new JSONArray(jsonString);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            BaseModel baseModel = new BaseModel();
            Iterator<String> iterators = jsonObject.keys();
            for (int j = 0; iterators.hasNext(); j++) {
                String key = iterators.next();
                baseModel.set(key, jsonObject.get(key));
            }
            list.add(baseModel);
        }
        return list;
    }

    /**
     * �������
     * 
     */
    public static Intent pushData(String string, BaseModel baseModel, Intent intent) {
        // Long key = SystemClock.elapsedRealtime();
        Long key = (long) UUID.randomUUID().hashCode();
        intent.putExtra(string, key);
        contextHashMap.put(key, baseModel);
        return intent;
    }

    /**
     * ���Object����
     * 
     */
    public static Intent pushObjData(String string, Object obj, Intent intent) {
        // Long key = SystemClock.elapsedRealtime();
        Long key = (long) UUID.randomUUID().hashCode();
        intent.putExtra(string, key);
        contextHashMap.put(key, obj);
        return intent;
    }

    /**
     * ��ȡ����
     * 
     */
    public static Object getData(String string, Intent intent) {
        Long key = intent.getLongExtra(string, 0L);
        return contextHashMap.get(key);
    }

    /**
     * 
     * ���� չʾͼƬ
     * 
     * @author Mars zhang
     * @created 2015-11-9 ����3:05:16
     */
    public void displayImage(ImageView imageView, String uriStr, HashMap<String, String> params) {
        uriStr += "?" + requestCode.toString();
        if (null != params) {
            Object[] mKeys = params.keySet().toArray();
            for (int i = 0; i < mKeys.length; i++) {
                uriStr += "&" + mKeys[i] + "=" + params.get(mKeys[i]);
            }
        }
        BaseApplication.instence.finalbitmap.display(imageView, uriStr);
    }

    /**
     * 
     * ���� չʾͼƬ
     * 
     * @author Mars zhang
     * @created 2015-11-9 ����3:05:16
     */
    public void displayImage(ImageView imageView, String uriStr) {
        BaseApplication.instence.finalbitmap.display(imageView, uriStr);
    }

    /**
     * 
     * ���� getDb
     * 
     * @author Mars zhang
     * @created 2015-11-30 ����3:01:55
     * @return
     */
    public FinalDb getDb() {
        if (null == db)
            db = FinalDb.create(instance);
        return db;
    };

    /**
     * Ҫ�Ӷ������ȶ��� ����Ҫ��дfinish����
     */
    @Override
    public void finish() {
        super.finish();
        // overridePendingTransition(R.anim.activity_in_heart ,
        // R.anim.activity_out_heart);
    }

    /**
     * Ҫ�Ӷ������ȶ��� ����Ҫ��дstartActivity����
     */
    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        // overridePendingTransition(R.anim.activity_in_heart ,
        // R.anim.activity_out_heart);

    }

    /**
     * 
     * ���� �ǿ��ж�
     * 
     * @author Mars zhang
     * @created 2015-11-25 ����2:09:28
     * @param value
     * @param defaultValue
     * @return
     */
    public String ifnull(Object valueobj, String defaultValue) {
        String value = valueobj + "";
        if (null == value || value.equals("null")) {
            return defaultValue;
        } else {
            return value;
        }

    }

    /** ������ʾ���� */
    public void errorAni(View view) {
        YoYo.with(Techniques.Shake).playOn(view);
    }
    
    /**
     * 
     * ����  ��ʼ�첽������
     * @author Mars zhang
     * @created 2015-12-17 ����9:43:49
     */
    protected void initAsyTree() {
        
    }
    

}