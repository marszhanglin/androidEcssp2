/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package net.evecom.ecssp.mobile.test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.evecom.ecssp.esb.EventInterceptor;
import net.evecom.ecssp.mobile.common.MobileController;
import net.evecom.pub.annotation.Controller;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;

/**
 * 移动应用支撑服务测试控制对象
 * 
 * @Before第一步控制对象拦截，第二步启动事务，这和写的先后顺序有关
 * 
 * @author Yancey Qiu
 * @created 2014年11月17日 上午9:39:53
 * @version 2.0
 */
@Controller(controllerKey = "/jfs/ecssp/mobile/test/testMobileCtr")
@Before({ EventInterceptor.class, Tx.class })
public class TestMobileController extends MobileController {

    /**
     * 测试
     * 
     * @author Yancey Qiu
     * @created 2014年11月17日 上午9:44:36
     */
    public void post() {
        Map<String, Object> prmOut = new HashMap<String, Object>();
        prmOut.put("name", "测试");
        prmOut.put("now", new java.sql.Timestamp(new Date().getTime()));
        prmOut.put("now2", new Date());

        // 如果业务错误手动抛出异常，让事务回滚
        // if (true)
        // throw new RuntimeException("test err");

        renderPrmOut(prmOut);
    }
}
