/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package net.evecom.ecssp.mobile.common;

import net.evecom.pub.common.BaseService;
import net.evecom.pub.interceptor.TxProxy;

/**
 * 移动应用-服务配置服务
 * 
 * @author Yancey Qiu
 * @created 2014年11月13日 下午6:02:43
 * @version 2.0
 */
public class MobileServiceConfigService extends BaseService {

    /**
     * 服务对象
     * 
     * @author Yancey Qiu
     */
    public static MobileServiceConfigService service = TxProxy.newProxy(MobileServiceConfigService.class);

    /**
     * 根据类名和方法名获取服务配置
     * 
     * @author Yancey Qiu
     * @created 2014年11月13日 下午6:18:39
     * @param cls
     *            类名
     * @param mthd
     *            方法名
     * @return
     */
    public MobileServiceConfig getByClsAndMthd(String cls, String mthd) {

        return MobileServiceConfig.dao.findFirst("SELECT * FROM T_Mobile_ServiceConfig WHERE cls = ? AND mthd = ?",
                cls, mthd);
    }

}
