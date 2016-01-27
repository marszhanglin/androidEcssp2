/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package net.evecom.ecssp.mobile.common;

import net.evecom.pub.common.BaseService;
import net.evecom.pub.interceptor.TxProxy;

/**
 * 移动应用-访问日志服务
 * 
 * @author Yancey Qiu
 * @created 2014年11月17日 下午4:50:30
 * @version 2.0
 */
public class MobileLogService extends BaseService {

    /**
     * 服务对象
     * 
     * @author Yancey Qiu
     */
    public static MobileLogService service = TxProxy.newProxy(MobileLogService.class);
}
