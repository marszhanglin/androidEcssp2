/*
 * Copyright (c) 2005, 2015, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package net.evecom.ecssp.mobile.contact;

import net.evecom.pub.annotation.Table;
import net.evecom.pub.common.BaseModel;

/**
 * 联系人model
 * 
 * @author Stark Zhou
 * @created 2015-11-13 上午10:15:12
 */
@Table(tableName = "T_DUTY_ADDRESS")
public class ContactModel extends BaseModel<ContactModel> {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;
    /**
     * 数据访问对象
     * 
     * @author Stark Zhou
     */
    public static ContactModel dao = new ContactModel();

}
