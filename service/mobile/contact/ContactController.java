/*
 * Copyright (c) 2005, 2015, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package net.evecom.ecssp.mobile.contact;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.evecom.ecapp.duty.contacts.DutyContacts;
import net.evecom.ecapp.duty.contacts.DutyGroup;
import net.evecom.ecssp.esb.EventInterceptor;
import net.evecom.ecssp.mobile.common.MobileController;
import net.evecom.ecssp.mobile.pub.AndroidService;
import net.evecom.org.Organization;
import net.evecom.pub.annotation.Controller;
import net.evecom.pub.util.SqlFilter;
import net.evecom.system.Dict;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;

/**
 * 联系人管理控制类
 * 
 * @author Stark Zhou
 * @created 2015-11-13 上午9:15:10
 */
@Controller(controllerKey = "/jfs/ecssp/mobile/contactCtr")
@Before({ EventInterceptor.class, Tx.class })
public class ContactController extends MobileController {
    /**
     * contacts
     */
    private List<DutyContacts> contacts;

    /**
     * 
     * 获取未分组联系人
     * 
     * @author Stark Zhou
     * @created 2015-12-30 下午7:00:35
     */
    public void getEmptyGroup() {
        String searchStr = getPara("searchStr");
        contacts = ContactService.service.getEmptyGroup(searchStr);
        getContactType();
        renderJson(contacts);
    }

    /**
     * 
     * 获取联系人列表
     * 
     * @author Stark Zhou
     * @created 2015-11-13 上午9:32:11
     */
    public void getContactList() {
        String searchStr = getPara("searchStr");
        SqlFilter filter = new SqlFilter(getRequest());
        // 如果查询条件不为空
        if (searchStr != null && searchStr.equals("") == false && searchStr.equals("null") == false) {
            filter.addFilter("QUERY_t#quicksearch_S_LK", searchStr);
        }
        contacts = ContactService.service.getContact(filter);
        getContactType();
        renderJson(contacts);
    }

    /**
     * 
     * 按组获取联系人
     * 
     * @author Stark Zhou
     * @created 2015-12-30 下午2:49:51
     */
    public void getContactByGrp() {
        String groupId = getPara("groupid");
        String searchStr = getPara("searchStr");
        contacts = ContactService.service.getContactByGrp(groupId, searchStr);
        getContactType();
        renderJson(contacts);
    }

    /**
     * 
     * 按父亲获取分组
     * 
     * @author Stark Zhou
     * @created 2015-12-30 下午2:50:11
     */
    public void getGroupByParentId() {
        List<DutyGroup> groups = new ArrayList<DutyGroup>();
        String groupid = getPara("groupid");
        groups = ContactService.service.getGroupByParentId(groupid);
        renderJson(groups);
    }

    /**
     * 
     * 1,123456;2,123456;格式的联系方式,分解成电话和手机号，
     * callsMap格式为{“手机”，“123456”}，{“固话”，“123456”}
     * 
     * @author Stark Zhou
     * @created 2016-1-4 下午3:02:01
     */
    public void getContactType() {
        String[] callGrp = new String[] {};
        String[] callNo = new String[] {};
        // 获取通讯方式字典
        List<Dict> dict = ContactService.service.getContactTypeDict();
        for (int l = 0; l < contacts.size(); l++) {
            // 获取一种通讯方式
            callGrp = contacts.get(l).get("callnos").toString().split(";");
            for (int i = 0; i < callGrp.length; i++) {
                callNo = callGrp[i].split(",");
                for (int j = 0; j < dict.size(); j++) {
                    // 如果字典的value等于逗号前的数字
                    if (callNo[0].equals(dict.get(j).get("value").toString())) {
                        // 把通讯方式的名称和号码加入到联系人list中
                        contacts.get(l).put(dict.get(j).get("name").toString(), callNo[1]);
                    }
                }
            }
        }
    }

    /**
     * 
     * 删除联系人
     * 
     * @author Stark Zhou
     * @created 2015-11-17 上午9:05:44
     */
    public void delContact() {
        String contactId = getPara("id");
        ContactService.service.delContact(contactId);
        renderJson("delete success");
    }

    /**
     * 
     * 获取分组列表
     * 
     * @author Stark Zhou
     * @created 2015-11-16 下午3:13:38
     */
    public void getGroupList() {
        String userId = getPara("userid");
        renderJson(ContactService.service.getGroupList(userId));
    }

    /**
     * 
     * 新增联系人
     * 
     * @author Stark Zhou
     * @created 2015-11-16 上午10:31:56
     */
    public void addContact() {
        ContactModel contacts = getModel(ContactModel.class);

        contacts.set("createtime", new java.sql.Timestamp(new Date(System.currentTimeMillis()).getTime()));
        contacts.save();
        // renderPrmOut("");
        renderJson(contacts);
    }

}
