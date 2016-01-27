/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package net.evecom.ecssp.mobile;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import net.evecom.ecssp.mobile.common.MobileUserGrant;
import net.evecom.ecssp.mobile.common.MobileUserGrantService;
import net.evecom.org.User;
import net.evecom.pub.common.BaseService;
import net.evecom.pub.interceptor.TxProxy;
import net.evecom.pub.util.StringUtil;

/**
 * 权限访问服务
 * 
 * @author Yancey Qiu
 * @created 2014年11月19日 下午5:26:33
 * @version 2.0
 */
public class AccessService extends BaseService {

    /**
     * 服务对象
     * 
     * @author Yancey Qiu
     */
    public static AccessService service = TxProxy.newProxy(AccessService.class);

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
     * 产生权限码
     * 
     * @author Yancey Qiu
     * @created 2014年11月24日 上午9:33:21
     * @return
     */
    private int genGrantedCode() {
        int max = 99999;
        int min = 10000;
        Random random = new Random();

        return random.nextInt(max) % (max - min + 1) + min;
    }

    /**
     * 产生用户授权
     * 
     * @author Yancey Qiu
     * @created 2014年11月24日 上午10:30:13
     * @param userId
     *            用户id
     * @param imei
     *            IMEI号
     * @return
     */
    public MobileUserGrant genUserGrant(String userId, String imei) {

        // 获取用户授权
        MobileUserGrant userGrant = MobileUserGrantService.service.getByUser(userId, imei);
        boolean exists = userGrant != null;

        // 如果用户授权不存在，则新增用户授权
        if (!exists) {
            userGrant = new MobileUserGrant();
        }

        // 到期时间为当前时间增加一年
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1);
        userGrant.set("expireTime", new Timestamp(calendar.getTime().getTime()));

        userGrant.set("userId", userId);
        userGrant.set("imei", imei);
        userGrant.set("grantedCode", new BigDecimal(genGrantedCode()));
        userGrant.set("pkey", StringUtil.getUuidByJdk(true).substring(0, 16));

        // 如果用户授权存在，则修改用户授权
        if (exists) {
            userGrant.update();
        }
        // 否则新增用户授权
        else {
            userGrant.save();
        }

        return userGrant;
    }
}
