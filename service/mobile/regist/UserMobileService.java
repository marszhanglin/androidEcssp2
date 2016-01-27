/*
 * Copyright (c) 2005, 2015, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package net.evecom.ecssp.mobile.regist;

import java.util.List;

import org.apache.log4j.Logger;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import net.evecom.pub.common.BaseService;
import net.evecom.pub.interceptor.TxProxy;

/**
 * 移动用户注册服务类
 * 
 * @author Stark Zhou
 * @created 2015-11-30 下午2:59:51
 */
public class UserMobileService extends BaseService {
    /**
     * log
     */
    @SuppressWarnings("unused")
    private static Logger log = Logger.getLogger(UserMobileService.class);

    /**
     * service
     */
    public static UserMobileService service = TxProxy.newProxy(UserMobileService.class);

    /**
     * 
     * 是否账号已存在
     * 
     * @author Stark Zhou
     * @created 2016-1-13 上午11:16:27
     * @param name
     * @return
     */
    public boolean getMobileUser(String name) {

        List<Record> rd = Db.find("SELECT t.loginname,t1.loginname FROM T_Mobile_Reg_User t, "
                + " ORG_USER t1 WHERE t.loginname =? or t1.loginname=?", name, name);

        if (rd.size() != 0) {
            return false;
        }
        return true;
    }
}
