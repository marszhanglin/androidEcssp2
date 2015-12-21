/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package net.evecom.androidecssp.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.webkit.JavascriptInterface;

/**
 * 
 * ���� android 4.2 ֮��汾�ṩ��js���õĺ����������ע�����@JavascriptInterface
 * 
 * @author Mars zhang
 * @created 2015-11-5 ����3:46:45
 */
public final class JsInterface {
    /** MemberVariables */
    public Context context;

    /**
     * 
     * ����
     * 
     * @author Mars zhang
     * @created 2015-11-25 ����2:12:54
     * @param context
     */
    public JsInterface(Context context) {
        this.context = context;
    }

    @JavascriptInterface
    public String show(final String strmsg) {
        System.out.println(strmsg);
        return strmsg;
    }

    @JavascriptInterface
    public String finish(final String strmsg) {
        System.out.println(strmsg);
        new Handler().post(new Runnable() { // UI�߳�
                    @Override
                    public void run() {
                        BaseWebActivity.instance.finish();
                    }
                });
        return strmsg;
    }

    @JavascriptInterface
    public void saveToSP(final String fileName, final String key, final String value) {
        SharedPreferences sp = BaseWebActivity.instance.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

}
