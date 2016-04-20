package net.evecom.androidecssp.base;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class BaseService extends Service{

    public Context instance ;
    
    
    @Override
    public void onCreate() {
        super.onCreate();
        instance=this; 
    }
    
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    
    
    
    
    
    /** мак╬ */
    protected void toastInOtherThread(String strMsg, int L1S0) {
//        Toast.makeText(getApplicationContext(), strMsg, L1S0).show();
        System.out.println(strMsg);
    }
    
}
