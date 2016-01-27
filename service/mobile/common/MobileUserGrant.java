/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package net.evecom.ecssp.mobile.common;

import net.evecom.pub.annotation.Table;
import net.evecom.pub.common.BaseModel;

/**
 * 移动应用-用户授权
 * 
 * @author Yancey Qiu
 * @created 2014年11月14日 下午12:03:15
 * @version 2.0
 */
@Table(tableName = "T_Mobile_UserGrant")
public class MobileUserGrant extends BaseModel<MobileUserGrant> {

    /**
     * 
     * @author Yancey Qiu
     */
    private static final long serialVersionUID = 1L;

    /**
     * 数据访问对象
     * 
     * @author Yancey Qiu
     */
    public static MobileUserGrant dao = new MobileUserGrant();
}
