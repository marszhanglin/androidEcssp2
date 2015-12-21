/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package net.evecom.androidecssp.base;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalDb;
import android.app.Application;

/**
 * 
 * ÃèÊö BaseApplication
 * 
 * @author Mars zhang
 * @created 2015-11-9 ÏÂÎç3:11:24
 */
public class BaseApplication extends Application {
    /** MemberVariables */
    public static FinalBitmap finalbitmap;
    /** MemberVariables */
    public static BaseApplication instence;
    /** MemberVariables */
    public static FinalDb db;

    @Override
    public void onCreate() {
        finalbitmap = FinalBitmap.create(this);
        db = FinalDb.create(this, true);
        super.onCreate();
        instence = this;

    }
}
