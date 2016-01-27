/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package net.evecom.ecssp.mobile.event;

import net.evecom.pub.annotation.Table;
import net.evecom.pub.common.BaseModel;

/**
 * 
 * 移动应用-现场人员
 * 
 * @author Mars zhang
 * @created 2015年6月1日 下午2:31:08
 */
@Table(tableName = "T_MOBILE_SCENPERSON")
public class MobileScenperson extends BaseModel<MobileScenperson> {

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
    public static MobileScenperson dao = new MobileScenperson();
}
