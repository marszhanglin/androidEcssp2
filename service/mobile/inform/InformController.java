/*
 * Copyright (c) 2005, 2015, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package net.evecom.ecssp.mobile.inform;

import java.util.List;

import net.evecom.ecapp.infoissue.info.Info;
import net.evecom.ecssp.esb.EventInterceptor;
import net.evecom.ecssp.mobile.common.MobileController;
import net.evecom.pub.annotation.Controller;
import net.evecom.pub.util.SqlFilter;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;

/**
 * 描述
 * 
 * @author Stark Zhou
 * @created 2015-12-17 上午11:44:12
 */
@Controller(controllerKey = "/jfs/ecssp/mobile/informCtr")
@Before({ EventInterceptor.class, Tx.class })
public class InformController extends MobileController {
    /**
     * 获取通知列表
     */
    public void getInfromList() {
        String searchStr = getPara("searchStr");
        String userId = getPara("userid");
        SqlFilter filter = new SqlFilter();
        if (searchStr != null && searchStr != "") {
            filter.addFilter("QUERY_t#title_S_LK", searchStr);
        }
        filter.addFilter("QUERY_t2#id_S_EQ", userId);
        List<Info> informs = InformService.service.getInformList(filter);
        renderJson(informs);
    }

}
