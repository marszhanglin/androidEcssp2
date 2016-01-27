/*
 * Copyright (c) 2005, 2015, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package net.evecom.ecssp.mobile.regist;

import net.evecom.pub.annotation.Table;
import net.evecom.pub.common.BaseModel;

/**
 * 移动注册用户
 * 
 * @author Stark Zhou
 * @created 2015-11-30 下午2:59:15
 */
@Table(tableName = "T_Mobile_Reg_User")
public class UserMobile extends BaseModel<UserMobile> {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;
    /**
     * 数据访问对象
     * 
     * @author Stark Zhou
     */
    public static UserMobile dao = new UserMobile();

}
