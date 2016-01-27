/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package net.evecom.ecssp.mobile.pub;

import java.util.ArrayList;
import java.util.List;

import net.evecom.ecapp.event.eventtype.EventType;
import net.evecom.ecapp.event.inforeception.InfoReception;
import net.evecom.ecapp.plan.eventresponse.PlanEventTaskResponse;
import net.evecom.ecapp.resource.resourcetype.ResourceType;
import net.evecom.org.Organization;
import net.evecom.org.UserInfo;
import net.evecom.pub.common.BaseService;
import net.evecom.pub.interceptor.TxProxy;
import net.evecom.system.Area;
import net.evecom.system.Attachment;
import net.evecom.system.Dict;

import org.apache.log4j.Logger;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

/**
 * 描述 应急处置下预案响应的服务类
 * 
 * @author Mars zhang
 * @created 2014年10月7日 下午5:54:52
 */
public class AndroidService extends BaseService {

    /**
     * 描述 log
     */
    @SuppressWarnings("unused")
    private static Logger log = Logger.getLogger(AndroidService.class);

    /**
     * 描述 service
     */
    public static AndroidService service = TxProxy.newProxy(AndroidService.class);

    /**
     * 
     * 获取事件
     * 
     * @author Mars zhang
     * @created 2015年6月2日 上午9:01:37
     * @param curstatus
     *            事件状态
     * @return
     */
    public List<InfoReception> geteventLis(String curstatus, String pagesize, String deptid) {
        List<String> mObjs = new ArrayList<String>();
        StringBuilder sqlSb = new StringBuilder();
        sqlSb.append(" SELECT ef.* , et.typename , sa.areaname ");
        sqlSb.append(" FROM t_event_info ef ");
        sqlSb.append(" left join t_event_eventtype et on et.id=ef.eventtype ");
        sqlSb.append(" left join sys_area sa on sa.id=ef.eventarea ");
        if(null!=deptid&&!deptid.equals("null")){
            sqlSb.append(" left join t_event_copyto tec on tec.eventid=ef.id  ");
        }
        sqlSb.append(" WHERE  1=1 ");
        sqlSb.append(" and  ef.CURSTATUS=? ");
        mObjs.add(curstatus);
        if(null!=deptid&&!deptid.equals("null")){
            sqlSb.append(" and  tec.RECEIVEDEPTID=?   ");
            mObjs.add(deptid);
        }
        sqlSb.append(" and rownum <? order by ef.createtime desc "); 
        mObjs.add(pagesize);
        List<InfoReception> infoReceptions = InfoReception.dao.find(sqlSb.toString(),mObjs.toArray());
        return infoReceptions;
    }

    /**
     * 
     * 获取反馈
     * 
     * @author Mars zhang
     * @created 2015年6月2日 上午9:01:37
     * @return
     */
    public List<PlanEventTaskResponse> getTaskResponseByTaskId(String taskId) {
        List<PlanEventTaskResponse> infoReceptions = PlanEventTaskResponse.dao.find("SELECT tr.*,oo.name "
                + " FROM t_plan_event_taskres tr " + " LEFT JOIN org_organization oo  ON oo.id=tr.responsedeptid"
                + " WHERE tr.taskid=? and rownum <=100 order by tr.createtime desc ", new Object[] { taskId });
        return infoReceptions;
    }

    /**
     * 
     * 获取反馈
     * 
     * @author Mars zhang
     * @created 2015年6月2日 上午9:01:37
     * @return
     */
    public String getDictValueByKeyAndName(String key, String name) {
        String dictvalue = Db.queryStr("select dictvalue " + " from sys_dict " + "  where name=?  and dictkey=? ",
                new Object[] { name, key });
        return dictvalue;
    }

    /**
     * 
     * 获取反馈
     * 
     * @author Mars zhang
     * @created 2015年6月2日 上午9:01:37
     * @return
     */
    public List<Dict> getDictByKey(String key) {
        List<Dict> dicts = Dict.dao.find("select * from sys_dict where 1=1" + "  and dictkey=? and type = '1'",
                new Object[] { key });
        return dicts;
    }

    /**
     * 
     * 描述 查询人员列表
     * 
     * @author Mars zhang
     * @created 2015年11月13日 下午4:33:11
     * @param name
     * @param idcard
     * @param sexDict
     * @return
     */
    public List<UserInfo> searchUserlist(String name, String orgname, String sexDict, String pageSize) {
        StringBuilder sbSql = new StringBuilder();
        List<String> pars = new ArrayList<String>();
        if (null == pageSize) {
            pageSize = "15";
        }
        sbSql.append(" select oui.*,sex.name as sexname , oo.name as orgname ,oo.id as orgid from org_userinfo oui ");
        sbSql.append(" left join sys_dict sex on sex.DICTKEY='SYSTEM_SEX' and sex.DICTVALUE=oui.sex ");
        sbSql.append(" left join org_user ou on ou.id=oui.id ");
        sbSql.append(" left join org_organization oo on oo.id=ou.orgid ");
        sbSql.append(" where 1=1 ");
        if (null != name && name.length() > 0) {
            sbSql.append("  and oui.name like '%'||?||'%' ");
            pars.add(name);
        }
        if (null != orgname && orgname.length() > 0) {
            sbSql.append("  and oo.name like '%'||?||'%' ");
            pars.add(orgname);
        }
        if (null != sexDict && sexDict.length() > 0) {
            sbSql.append("  and oui.sex = ? ");
            pars.add(sexDict);
        }
        sbSql.append("and rownum < ?");
        pars.add(pageSize);
        List<UserInfo> userInfos = UserInfo.dao.find(sbSql.toString(), pars.toArray());
        return userInfos;
    }

    /**
     * 描述
     * 
     * @author Mars zhang
     * @created 2015年11月17日 下午3:26:22
     * @param para
     * @return
     */
    public List<Organization> searchOrglist(String name, String pageSize) {
        StringBuilder sbSql = new StringBuilder();
        List<String> pars = new ArrayList<String>();
        if (null == pageSize) {
            pageSize = "15";
        }
        sbSql.append(" select  oo.*  from org_organization oo ");
        sbSql.append(" where 1=1 ");
        if (null != name && name.length() > 0) {
            sbSql.append("  and oo.name like '%'||?||'%' ");
            pars.add(name);
        }
        sbSql.append("and rownum < ?");
        pars.add(pageSize);
        List<Organization> organizations = Organization.dao.find(sbSql.toString(), pars.toArray());
        return organizations;
    }

    /**
     * 描述
     * 
     * @author Mars zhang
     * @created 2015年11月26日 上午9:00:56
     * @param mfileids
     * @return
     */
    public List<Attachment> getImagesByIds(String mfileids) {
        if (mfileids.length() > 0) {
            mfileids = mfileids.replace(",", "','");
        }
        StringBuilder mSqlSb = new StringBuilder();
        mSqlSb.append(" select *  from  SYS_ATTACHMENT sa");
        mSqlSb.append(" where 1=1 ");
        mSqlSb.append(" and sa.id in ('" + mfileids + "') ");
        mSqlSb.append(" and ( sa.type like '%jpg' or  sa.type like '%png' ) ");
        List<Attachment> mAttachments = Attachment.dao.find(mSqlSb.toString());
        return mAttachments;
    }

    /**
     * 
     * 描述
     * 
     * @author Mars zhang
     * @created 2015年12月8日 下午5:22:05
     * @param centergisx
     * @param centergisy
     * @param longdiffer
     * @param latdiffer
     * @return
     */
    public List<Record> searchResourceByRecGis(double centergisx, double centergisy, double longdiffer,
            double latdiffer, String rownum, String resourcename, String resourceType) {
        StringBuilder sqlSb = new StringBuilder();
        sqlSb.append("select *  from view_emergency_plan_resource t  ");
        sqlSb.append(" where 1=1   ");
        sqlSb.append(" and t.gisx<? and t.gisx>?  ");
        sqlSb.append(" and t.gisy<? and t.gisy>?  ");
        sqlSb.append(" and rownum <=?  ");
        sqlSb.append(" and t.name like  '%'||?||'%' ");
        sqlSb.append(" and t.tablename like  ''||?||'%' ");
        List<Record> records = Db.find(sqlSb.toString(), new Object[] { centergisx + longdiffer + "",
                centergisx - longdiffer + "", centergisy + latdiffer + "", centergisy - latdiffer + "", rownum,
                resourcename, resourceType });
        return records;

    }

    /**
     * 
     * 描述 获取资源类型
     * 
     * @author Mars zhang
     * @created 2015年6月30日 上午10:31:56
     * @param id
     * @return
     */
    public List<ResourceType> findChildByParentIds(String id) {
        return ResourceType.dao.find("select t.id,t.name ,t.type as dictvalue from  "
                + ResourceType.dao.getTable().getName() + " t where parentId=? order by id", new Object[] { id });
    }

    /**
     * 
     * 描述 获取事件类型
     * 
     * @author Mars zhang
     * @created 2015年12月16日 下午3:54:58
     * @param parentid
     * @return
     */
    public List<EventType> getEventTypesByParentid(String parentid) {
        StringBuilder sqlSb = new StringBuilder();
        sqlSb.append(" select t.id ,t.typename as name , ");
        sqlSb.append(" (case when (select t.id from T_EVENT_EVENTTYPE b where b.parenttypeid = t.id and rownum =1 ) ");
        sqlSb.append(" is not null then 'true' else 'false' end) as haschildren ");
        sqlSb.append(" from T_EVENT_EVENTTYPE t ");
        sqlSb.append(" where t.parenttypeid = ? ");
        sqlSb.append(" order by haschildren desc ");

        List<EventType> eventTypes = EventType.dao.find(sqlSb.toString(), new Object[] { parentid });
        return eventTypes;

    }

    /**
     * 
     * 描述
     * 
     * @author Mars zhang
     * @created 2015年12月16日 下午3:54:58
     * @param parentid
     * @return
     */
    public List<Organization> getDeptByParentid(String parentid) {
        StringBuilder sqlSb = new StringBuilder();
        sqlSb.append(" select t.id ,t.name , ");
        sqlSb.append(" (case when (select t.id from ORG_ORGANIZATION b where b.parentid = t.id and rownum =1 ) ");
        sqlSb.append(" is not null then 'true' else 'false' end) as haschildren ");
        sqlSb.append(" from ORG_ORGANIZATION t ");
        sqlSb.append(" where t.parentid = ? ");
        sqlSb.append(" order by haschildren desc ");

        List<Organization> organizations = Organization.dao.find(sqlSb.toString(), new Object[] { parentid });
        return organizations;

    }

    /**
     * 
     * 描述
     * 
     * @author Mars zhang
     * @created 2015年12月16日 下午3:54:58
     * @param parentid
     * @return
     */
    public List<Area> getAreaByParentid(String parentid) {
        StringBuilder sqlSb = new StringBuilder();
        sqlSb.append(" select t.id ,t.areaname name , ");
        sqlSb.append(" (case when (select t.id from SYS_AREA b where b.parentid = t.id and rownum =1 ) ");
        sqlSb.append(" is not null then 'true' else 'false' end) as haschildren ");
        sqlSb.append(" from SYS_AREA t ");
        sqlSb.append(" where t.parentid = ? ");
        sqlSb.append(" order by haschildren desc ");

        List<Area> areas = Area.dao.find(sqlSb.toString(), new Object[] { parentid });
        return areas;

    }

}