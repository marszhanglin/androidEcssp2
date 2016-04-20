/*
 * Copyright (c) 2005, 2015, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package net.evecom.androidecssp.base;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

/**
 * 
 * √Ë ˆ
 * 
 * @author Mars zhang
 * @created 2015-12-28 …œŒÁ9:43:21
 */
public class BaseService extends Service {
    /** instance */
    public Context instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /** Õ¡Àæ */
    protected void toastInOtherThread(String strMsg, int L1S0) {
        // Toast.makeText(getApplicationContext(), strMsg, L1S0).show();
        System.out.println(strMsg);
    }

}
