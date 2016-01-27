/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package net.evecom.ecssp.mobile;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.HashMap;

import net.evecom.ecssp.esb.EventInterceptor;
import net.evecom.ecssp.mobile.common.MobileController;
import net.evecom.ecssp.mobile.common.MobileUserGrant;
import net.evecom.org.Organization;
import net.evecom.org.User;
import net.evecom.org.UserInfo;
import net.evecom.pub.annotation.Controller;
import net.evecom.pub.annotation.Excluded;
import net.evecom.pub.util.AES;
import net.evecom.pub.util.MD5Util;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;

/**
 * 权限访问控制对象
 * 
 * @author Yancey Qiu
 * @created 2014年11月19日 下午5:14:21
 * @version 2.0
 */
@Controller(controllerKey = "/jfs/ecssp/mobile/accessCtr")
@Before({ EventInterceptor.class, Tx.class })
public class AccessController extends MobileController {

    /**
     * 登录 @Excluded 不进行系统级的用户验证
     * 
     * @author Yancey Qiu
     * @created 2014年11月19日 下午5:16:23
     */
    @Excluded
    public void login() {

        // 获取参数
        String imei = getPara(PRM_SYS_IMEI);
        String loginName = getPara(PRM_SYS_LOGINNAME);
        String pwd = getPara("pwd");

        // 验证用户合法性
        User user = AccessService.service.getUserByLoginName(loginName);

        if (user == null) {
            throw new RuntimeException("当前用户不存在！");
        }

        if (!MD5Util.md5(pwd).equals(user.getStr("pwd"))) {
            throw new RuntimeException("当前用户密码错误！");
        }

        if ("0".equals(user.getStr("status"))) {
            throw new RuntimeException("当前用户被禁用，请联系管理员开通！");
        }

        // 产生用户授权
        MobileUserGrant userGrant = AccessService.service.genUserGrant(user.getStr("id"), imei);

        // 设置日志需要记录的数据
        setUser(user);
        setUserGrant(userGrant);

        // 设置返回的加密授权码
        BigDecimal grantedCode = userGrant.getBigDecimal("grantedCode");
        String pkey = userGrant.getStr("pkey");

        String code = grantedCode.toString() + userGrant.getStr("id");
        code = AES.encryptECB(code, pkey);
        try {
            code = java.net.URLEncoder.encode(code, "utf-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        UserInfo userInfo = UserInfo.dao.findById(user.get("id"));
        User userdata = User.dao.findById(user.get("id"));
        Organization organization = Organization.dao.findById(userdata.get("orgid"));

        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("userInfo", userInfo);
        hashMap.put("userdata", userdata);
        hashMap.put("organization", organization);
        hashMap.put("code", code);
        renderPrmOut(hashMap);
    }

}
