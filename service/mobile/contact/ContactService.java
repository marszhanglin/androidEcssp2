/*
 * Copyright (c) 2005, 2015, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package net.evecom.ecssp.mobile.contact;

import net.evecom.system.Dict;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import net.evecom.ecapp.duty.contacts.DutyContacts;
import net.evecom.ecapp.duty.contacts.DutyGroup;
import net.evecom.pub.common.BaseService;
import net.evecom.pub.interceptor.TxProxy;
import net.evecom.pub.util.SqlFilter;

/**
 * 联系人管理服务类
 * 
 * @author Stark Zhou
 * @created 2015-11-13 上午9:14:51
 */
public class ContactService extends BaseService {
    /**
     * 描述 log
     */
    @SuppressWarnings("unused")
    private static Logger log = Logger.getLogger(ContactService.class);

    /**
     * 描述 service
     */
    public static ContactService service = TxProxy.newProxy(ContactService.class);

    public List<DutyContacts> getEmptyGroup(String s) {
        String searchStr = "%" + s + "%";
        List<String> str = new ArrayList<String>();
        String sql = "select t.*,t1.name as dept,sd.name as sexname from t_duty_contacts t left join "
                + "org_organization t1 on t1.id=t.orgid left join "
                + "sys_dict sd on sd.dictkey='SYSTEM_SEX' and sd.dictvalue=t.sex "
                + "where t.id  not  in (select t2.contactsid from t_contacts_group_relation t2 ) ";
        if (s != null && s.equals("") == false && s.equals("null") == false) {
            sql = sql + "and t.quicksearch like ?";
            str.add(searchStr);
        }
        List<DutyContacts> contact = DutyContacts.dao.find(sql, str.toArray());
        return contact;
    }

    /**
     * 
     * 获取联系人列表
     * 
     * @author Stark Zhou
     * @created 2015-11-13 上午9:21:23
     * @return
     */
    public List<DutyContacts> getContactByGrp(String group, String searchStr) {
        StringBuilder sql = new StringBuilder();
        List<String> s = new ArrayList<String>();
        s.add(group);
        sql.append("select t3.*,t1.name as groupname,t4.name as dept,sd.name as sexname from ");
        sql.append("(select * from t_duty_group start with id=? connect by prior id=parentid) t1 ");
        sql.append("left join t_contacts_group_relation t2 on t1.id=t2.groupid ");
        sql.append("left join t_duty_contacts t3 on t3.id=t2.contactsid ");
        sql.append("left join sys_dict sd on sd.dictkey='SYSTEM_SEX' and sd.dictvalue=t3.sex ");
        sql.append("left join org_organization t4 on t4.id=t3.orgid where rownum<100 and t3.id is not null ");
        if (searchStr != null && searchStr.equals("") == false && searchStr.equals("null") == false) {
            sql.append(" and t3.quicksearch like ?  ");
            searchStr = "%" + searchStr + "%";
            s.add(searchStr);
        }
        sql.append("order by t3.firstchar ");
        List<DutyContacts> contact = DutyContacts.dao.find(sql.toString(), s.toArray());
        return contact;
    }

    /**
     * 
     * 获取联系人
     * 
     * @author Stark Zhou
     * @created 2015-12-30 下午2:52:22
     * @param filter
     * @return
     */
    public List<DutyContacts> getContact(SqlFilter filter) {
        String sql = "select t.*,t3.name as groupname,t1.name as dept,sd.name as sexname from t_duty_contacts t "
                + "left join org_organization t1 on t.orgid=t1.id "
                + "left join t_contacts_group_relation t2 on t.id=t2.contactsid "
                + "left join sys_dict sd on sd.dictkey='SYSTEM_SEX' and sd.dictvalue=t.sex "
                + "left join t_duty_group t3 on t3.id=t2.groupid where rownum<100 " + filter.getWhereAndOrderSql()
                + " order by t.firstchar ";
        List<DutyContacts> contact = DutyContacts.dao.find(sql, filter.getParams().toArray());
        return contact;
    }

    /**
     * 
     * 获取分组列表
     * 
     * @author Stark Zhou
     * @created 2015-11-16 下午3:25:22
     * @return
     */

    public List<DutyGroup> getGroupByParentId(String s) {
        StringBuilder sql = new StringBuilder();
        sql.append("select t.* from t_duty_group t ");
        if (s.equals("0") || s == "0") {
            sql.append(" where t.parentid is null ");
        } else {
            sql.append(" where t.parentid= '" + s + "'");
        }
        sql.append(" and t.ownerid is null ");
        return DutyGroup.dao.find(sql.toString());
    }

    /**
     * 
     * 描述
     * 
     * @author Stark Zhou
     * @created 2015-12-30 下午2:52:45
     * @param userid
     * @param s
     * @return
     */
    public List<DutyGroup> getNormalGroup(String userid, String s) {
        StringBuilder sql = new StringBuilder();
        sql.append("select t.id,t.name ,(case when t.isleaf='0' ");
        sql.append("then 'false' else 'false' end) as haschildren from t_duty_group t ");
        if (s.equals("1") || s == "1") {
            sql.append(" where t.parentid is null ");
        } else {
            sql.append(" where t.parentid= '" + s + "'");
        }
        sql.append(" and (t.ownerid=? or t.ispublic='1') order by haschildren desc ");
        return DutyGroup.dao.find(sql.toString(), userid);
    }

    /**
     * 
     * 描述
     * 
     * @author Stark Zhou
     * @created 2015-12-30 下午2:52:53
     * @param id
     * @return
     */
    public List<Record> getGroupList(String id) {
        List<Record> groups = Db.find("SELECT * from (SELECT t1.* FROM t_duty_group t1  "
                + "where t1.ownerid is null and t1.rank='1' order by t1.code) union all "
                + "select * from (select t.* from t_duty_group t where (t.ownerid=? or t.ispublic='1')  "
                + "and t.rank='1' and t.ownerid is not null order by t.code ) ", id);
        return groups;
    }

    /**
     * 
     * 描述
     * 
     * @author Stark Zhou
     * @created 2015-12-30 下午2:53:00
     * @return
     */
    public List<Dict> getContactTypeDict() {
        List<Dict> contactType = Dict.dao.find("select t.name as name,t.dictvalue as value from sys_dict t "
                + "where t.dictkey='DUTY_CONTACTS_TYPE' and t.type='1' ");
        return contactType;
    }

    /**
     * 
     * 删除联系人
     * 
     * @author Stark Zhou
     * @created 2015-11-17 上午9:09:17
     * @param id
     */
    public void delContact(String id) {
        ContactModel.dao.deleteById(id);
        // Db.deleteById("t_duty_address", id);
    }
}
