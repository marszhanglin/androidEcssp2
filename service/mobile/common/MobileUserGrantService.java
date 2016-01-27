/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package net.evecom.ecssp.mobile.common;

import java.util.ArrayList;
import java.util.List;

import net.evecom.org.User;
import net.evecom.pub.common.BaseService;
import net.evecom.pub.interceptor.TxProxy;

/**
 * 移动应用-用户授权服务
 * 
 * @author Yancey Qiu
 * @created 2014年11月14日 下午12:04:58
 * @version 2.0
 */
public class MobileUserGrantService extends BaseService {

    /**
     * 服务对象
     * 
     * @author Yancey Qiu
     */
    public static MobileUserGrantService service = TxProxy.newProxy(MobileUserGrantService.class);

    /**
     * 根据登录名获取用户
     * 
     * @author Yancey Qiu
     * @created 2014年11月14日 下午2:18:25
     * @param loginName
     *            登录名
     * @return
     */
    public User getUserByLoginName(String loginName) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT u.id, u.loginName, u.status, u.pwd, i.name FROM Org_User u ");
        sql.append("LEFT JOIN Org_UserInfo i ON u.id = i.id ");
        sql.append("WHERE u.loginName = ? ");

        return User.dao.findFirst(sql.toString(), loginName);
    }

    /**
     * 根据用户获取用户授权
     * 
     * @author Yancey Qiu
     * @created 2014年11月14日 下午4:53:16
     * @param userId
     *            用户Id
     * @param imei
     *            imei号
     * @return
     */
    public MobileUserGrant getByUser(String userId, String imei) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM T_Mobile_UserGrant WHERE userId = ? ");

        List<Object> params = new ArrayList<Object>();
        params.add(userId);

        if (imei == null || imei.equals("")) {
            sql.append("AND imei IS NULL ");
        } else {
            sql.append("AND imei = ? ");
            params.add(imei);
        }

        return MobileUserGrant.dao.findFirst(sql.toString(), params.toArray());
    }

}
