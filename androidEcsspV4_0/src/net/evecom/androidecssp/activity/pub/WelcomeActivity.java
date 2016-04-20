/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package net.evecom.androidecssp.activity.pub;

import java.io.IOException;
import java.util.HashMap;

import net.evecom.androidecssp.R;
import net.evecom.androidecssp.activity.contact.UserRegistActivity;
import net.evecom.androidecssp.base.BaseActivity;
import net.evecom.androidecssp.base.BaseModel;
import net.evecom.androidecssp.base.ICallback;
import net.evecom.androidecssp.util.UiUtil;
import net.evecom.androidecssp.util.ShareUtil;
import net.tsz.afinal.FinalDb;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.tianditu.android.maps.GeoPoint;
import com.tianditu.android.maps.MapView;
import com.tianditu.android.maps.MyLocationOverlay;

/**
 * 
 * ���� WelcomeActivity
 * 
 * @author Mars zhang
 * @created 2015-11-12 ����10:16:20
 */
public class WelcomeActivity extends BaseActivity {
    /** �û���EditText */
    private EditText userNmaeEditText;
    /** ����EditText */
    private EditText passwordEditText;
    /** ��ס���� */
    private CheckBox jzmmCheckBox;
    /** �Զ����� */
    private CheckBox zddrCheckBox;
    /** welcom_regist_tv */
    private TextView registTV;
    /** SharedPreferences */
    private SharedPreferences passnameSp;
    /** login������ */
    private ProgressDialog loginProgressDialog = null;
    /** loginResult **/
    private String loginResult = "";
    /** dictResult **/
    private String dictResult = "";
    /** �Ƿ��������� ��ֹ�ظ��ύ **/
    private Boolean islogining = false;
    /** �Ƿ���Ҫ������λ���� */
    private Boolean isNeedGpsSet = false;
    /** ��λ�ͻ��� */
    MyLocationOverlay mMyLocation = null;
    /** ��ǰ���� */
    private Location currentLocation = null;
    /** ��λ������ */
    LocationManager locationManager = null;
    /** ���ݿ� */
    FinalDb db = null;
    /** json��ʾ */
    private JSONObject jsonStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_at);
        passnameSp = this.getSharedPreferences("PASSNAME", 0);

        askForOpenGPS();

        initView();

    }

    /**
     * �����gps
     */
    private void askForOpenGPS() {
        boolean gpsEnabled = Settings.Secure.isLocationProviderEnabled(getContentResolver(),
                LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            isNeedGpsSet = true;
            toast("������λ����,����GPS����ѡ��,����λ�޷���������!", 1);
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            startActivityForResult(intent, 0);
        }
    }

    /**
     * GIS����
     */
    private void manageGis() {
        // ���ͼ��� ʵ��ϵͳ��λ�ص��ӿڲ���������������
        mMyLocation = new MyOverlay(this, null);
        // ��λ������
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            toast("��ʼ��λ", 0);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, mMyLocation);
        } else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, mMyLocation);
            toast("��ʼ��վ��λ", 0);
        } else {
            toast("��λδ��", 0);
        }
    }

    /**
     * ���ͼ
     */
    class MyOverlay extends MyLocationOverlay {
        public MyOverlay(Context context, MapView mapView) {
            super(context, mapView);
        }

        /*
         * ������"�ҵ�λ��"�ϵĵ���¼�
         */
        protected boolean dispatchTap() {
            return true;
        }

        @Override
        public void onLocationChanged(Location location) {
            // TODO Auto-generated method stub
            super.onLocationChanged(location);
            if (location != null) {
                currentLocation = location;
                SharedPreferences sp = getApplicationContext().getSharedPreferences("GPS", MODE_PRIVATE);
                // ��������
                Editor editor = sp.edit();
                editor.putString("latitude", "" + location.getLatitude());
                editor.putString("longitude", "" + location.getLongitude());
                editor.commit();
                Log.v("", location.getLatitude() + "-----" + location.getLongitude());
                toast(location.getLatitude() + "-----" + location.getLongitude(), 0);
            }
            GeoPoint point = mMyLocation.getMyLocation();
        }

    }

    /**
     * �����ʼ�� <br>
     * ��ȡ�������ÿؼ�byid <br>
     * �Ƿ��Զ���½����
     */
    private void initView() {
        findbyId();
        iflogin();
    }

    /**
     * ��ȡ�������ÿؼ�byid
     */
    private void findbyId() {
        userNmaeEditText = (EditText) findViewById(R.id.welcome_user_edit);
        passwordEditText = (EditText) findViewById(R.id.welcome_password_edit);
        jzmmCheckBox = (CheckBox) findViewById(R.id.welcom_checkbox_jzmm);
        zddrCheckBox = (CheckBox) findViewById(R.id.welcom_checkbox_zddr);
        registTV = (TextView) findViewById(R.id.welcom_regist_tv);
        registTV.setText(Html.fromHtml("<u>�˺�ע��</u>"));
        registTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                startActivity(new Intent(instance, UserRegistActivity.class));
            }
        });
        listener();
    }

    /**
     * �ؼ�����
     */
    private void listener() {
        // ��ס������
        jzmmCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                Editor editor = passnameSp.edit();
                editor.putString("rembernp", "0");
                if (isChecked) {
                    editor.putString("rembernp", "1");
                } else {
                    editor.putString("rembernp", "0");
                }
                editor.commit();
            }
        });
        // �Զ���½���
        zddrCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                Editor editor = passnameSp.edit();
                editor.putString("autologin", "0");
                if (isChecked) {
                    editor.putString("autologin", "1");
                } else {
                    editor.putString("autologin", "0");
                }
                editor.commit();
            }
        });
    }

    private void iflogin() {
        Editor editor = passnameSp.edit();
        String autologin = ShareUtil.getString(getApplicationContext(), "PASSNAME", "autologin", "0");
        String rembernp = ShareUtil.getString(getApplicationContext(), "PASSNAME", "rembernp", "0");
        String username = ShareUtil.getString(getApplicationContext(), "PASSNAME", "username", "");
        String password = ShareUtil.getString(getApplicationContext(), "PASSNAME", "password", "");
        // ��EditText��ֵ
        userNmaeEditText.setText(username);

        // �ж��Ƿ��
        if (autologin.equals("1")) {
            zddrCheckBox.setChecked(true);
        } else {
            zddrCheckBox.setChecked(false);
        }
        if (rembernp.equals("1")) {
            passwordEditText.setText(password);
            jzmmCheckBox.setChecked(true);
        } else {
            editor.putString("password", "");
            jzmmCheckBox.setChecked(false);
        }
        editor.commit();
        // ��ס�������Զ���½ʱ�ἰ��½����
        if (autologin.equals("1") && rembernp.equals("1") && !isNeedGpsSet) {
            Log.v("mars", "�Զ�����");
            loginsubmit(username, password);
        }
    }

    /**
     * ��½����
     */
    private void loginsubmit(final String username, final String password) {
        if (islogining) {
            return;
        }

        /** �����¼�� */
        final Editor editor = passnameSp.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.commit();

        // �򿪽�����
        loginProgressDialog = ProgressDialog.show(this, "��ʾ", "���ڵ��룬���Ե�...");
        loginProgressDialog.setCancelable(true);

        // ���߳�
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // ��½�߳�ͨ��ʵ�� ���߳��д���
                    Message loginMessage = new Message();
                    HashMap<String, String> hashMap = new HashMap<String, String>();
                    hashMap.put("pwd", password.trim());
                    loginResult = connServerForResultPost("jfs/ecssp/mobile/accessCtr/login", hashMap);

                    // ������ܵ��������� ������Ϣ��ʾ
                    if (loginResult.length() > 0) {
                        try {
                            BaseModel resultObj = getObjInfo(loginResult);
                            Boolean success = resultObj.get("sys_success");
                            if (null != success && success) {
                                loginMessage.what = MESSAGETYPE_01;// �ɹ���½
                                BaseModel organization = getObjInfo(resultObj.get("organization").toString());
                                BaseModel userdata = getObjInfo(resultObj.get("userdata").toString());
                                BaseModel userInfo = getObjInfo(resultObj.get("userInfo").toString());
                                String code = resultObj.get("code").toString();
                                // �洢����--�û���������
                                String username = userNmaeEditText.getText().toString();
                                editor.putString("username", userdata.getStr("loginname"));
                                editor.putString("userid", userdata.getStr("id"));
                                editor.putString("usernameCN", userInfo.getStr("name"));
                                editor.putString("sex", userInfo.get("sex") + "");
                                editor.putString("mobile_In_clound", userInfo.get("mobile") + "");
                                editor.putString("orgid", organization.getStr("id"));
                                editor.putString("orgname", organization.getStr("name"));
                                editor.putString("code", code);
                                editor.commit();
                            } else {
                                loginMessage.what = MESSAGETYPE_02;// ��½ʧ��
                                                                   // �н��յ�����
                            }
                        } catch (JSONException e) {
                            loginMessage.what = MESSAGETYPE_02;// ��½ʧ�� �н��յ�����
                        }
                    } else {
                        loginMessage.what = MESSAGETYPE_03;// ��½ʧ�� û�н��յ�����
                    }
                    loginRequestHandler.sendMessage(loginMessage);

                } catch (ClientProtocolException e) {
                } catch (IOException e) {
                }
            }
        }).start();
    }

    /**
     * �첽��½����GUI����
     */
    private Handler loginRequestHandler = new Handler() {
        // ��дhandlerMessage
        @Override
        public void handleMessage(Message msg) {
            final Editor editor = passnameSp.edit();
            switch (msg.what) {
                case MESSAGETYPE_01:
                    // �رյ�¼������
                    if (null != loginProgressDialog) {
                        loginProgressDialog.dismiss();
                    }

                    Intent intent = new Intent();
                    intent.setClass(WelcomeActivity.this, MainMenuActivity.class);
                    startActivity(intent);
                    WelcomeActivity.this.finish();
                    Log.v("mars", "�ɹ���½");
                    break;
                case MESSAGETYPE_02:
                    // �رյ�¼������
                    if (null != loginProgressDialog) {
                        loginProgressDialog.dismiss();
                    }
                    try {
                        jsonStr = new JSONObject(loginResult);
                        dialogToast(jsonStr.get("sys_err").toString(), new ICallback() {
                            @Override
                            public Object execute() {
                                loginResult = "";
                                return null;
                            }
                        });
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    Log.v("mars", "��½ʧ�� �н��յ�����");
                    break;
                case MESSAGETYPE_03:
                    // �رյ�¼������
                    if (null != loginProgressDialog) {
                        loginProgressDialog.dismiss();
                    }
                    dialogToast("��½ʧ��,�����µ�¼��", new ICallback() {
                        @Override
                        public Object execute() {
                            loginResult = "";
                            // toast("ok", 1);
                            return null;
                        }
                    });
                    Log.v("mars", "��½ʧ��  û�н��յ�����");
                    break;
                default:
                    break;
            }
        }

    };

    /**
     * ��½���
     * 
     * @param view
     */
    public void welcomelogin(View view) {

        String username = userNmaeEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        loginsubmit(username, password);

        /*
         * Intent intent=new Intent(getApplicationContext(),
         * TDTLocation222.class); startActivity(intent);
         */
    }

    @Override
    protected void onPause() {
        if (null != mMyLocation) {
            locationManager.removeUpdates(mMyLocation);
        }
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case 0:
                manageGis();
                break;

            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void test(View view) {
        toast(UiUtil.getResolution(getWindowManager()), 1);
    }

}
