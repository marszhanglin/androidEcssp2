/*
 * Copyright (c) 2005, 2015, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package net.evecom.ecssp.mobile.inform;

import java.util.List;

import org.apache.log4j.Logger;

import net.evecom.pub.common.BaseService;
import net.evecom.pub.interceptor.TxProxy;
import net.evecom.pub.util.SqlFilter;
import net.evecom.ecapp.infoissue.info.Info;

/**
 * 描述
 * 
 * @author Stark Zhou
 * @created 2015-12-17 上午11:43:28
 */
public class InformService extends BaseService {
    /**
     * 描述 log
     */
    @SuppressWarnings("unused")
    private static Logger log = Logger.getLogger(InformService.class);

    /**
     * 描述 service
     */
    public static InformService service = TxProxy.newProxy(InformService.class);

    /**
     * 
     * 获取通知列表
     * 
     * @author Stark Zhou
     * @created 2015-12-17 上午11:55:32
     * @param filter
     * @return
     */
    public List<Info> getInformList(SqlFilter filter) {
        String sql = "select t.* ,t3.name as levelname,so.name as orgname,ei.eventname "
                + "as eventname from t_infoissue_info t left join t_infoissue_issuetype t1 on t.typeid=t1.id  "
                + "left join t_infoissue_issuetype t3 on t3.code=t.levels  "
                + "left join org_user t2 on (t2.orgid in t.recorgids) "
                + "left join org_organization so on t.orgid=so.id " + "left join t_event_info ei on ei.id=t.eventid "
                + "where t1.id='014' " + filter.getWhereAndOrderSql();
        List<Info> informs = Info.dao.find(sql, filter.getParams().toArray());
        return informs;
    }

}
