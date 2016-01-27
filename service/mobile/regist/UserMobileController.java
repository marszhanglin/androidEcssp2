/*
 * Copyright (c) 2005, 2015, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package net.evecom.ecssp.mobile.regist;

import java.sql.Date;
import net.evecom.ecssp.esb.EventInterceptor;
import net.evecom.ecssp.mobile.common.MobileController;
import net.evecom.pub.annotation.Controller;
import net.evecom.pub.annotation.Excluded;
import net.evecom.pub.util.MD5Util;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;

/**
 * 移动用户注册控制类
 * 
 * @author Stark Zhou
 * @created 2015-11-30 下午3:00:15
 */
@Controller(controllerKey = "/jfs/ecssp/mobile/userMobileCtr")
@Before({ EventInterceptor.class, Tx.class })
public class UserMobileController extends MobileController {
    /**
     * 
     * 注册移动用户
     * 
     * @author Stark Zhou
     * @created 2015-12-1 上午9:00:14
     */
    @Excluded
    public void userMobileAdd() {
        UserMobile userMobile = getModel(UserMobile.class);
        boolean valiUser = UserMobileService.service.getMobileUser(userMobile.getStr("loginname"));
        // 判断账号是否已存在
        if (valiUser) {
            // 密码加密
            userMobile.set("pwd", MD5Util.md5("888888"));
            userMobile.set("createtime", new java.sql.Timestamp(new Date(System.currentTimeMillis()).getTime()));
            // 保存用户信息
            userMobile.save();
            renderPrmOut("success");
        } else {
            return;
        }
    }

}
