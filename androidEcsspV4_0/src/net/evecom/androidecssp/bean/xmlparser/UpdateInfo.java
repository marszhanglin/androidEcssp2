/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package net.evecom.androidecssp.bean.xmlparser;

/**
 * 
 * 2014-7-22下午4:44:56 类UpdateInfo app版本信息
 * 
 * @author Mars zhang
 * 
 */
public class UpdateInfo extends net.mutil.util.xmlparser.UpdateInfo {
    /**所属更新项目--应急产品*/
    private String versionDsp = "应急2.0产品";

    public String getVersionDsp() {
        return versionDsp;
    }

    public void setVersionDsp(String versionDsp) {
        this.versionDsp = versionDsp;
    }

    
    

}
