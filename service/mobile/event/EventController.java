/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package net.evecom.ecssp.mobile.event;

import java.sql.Date;
import java.util.List;

import net.evecom.ecapp.event.eventtype.EventType;
import net.evecom.ecapp.event.inforeception.EventCopy;
import net.evecom.ecapp.event.inforeception.InfoReception;
import net.evecom.ecapp.resource.resourcetype.ResourceType;
import net.evecom.ecssp.esb.EventInterceptor;
import net.evecom.ecssp.mobile.common.MobileController;
import net.evecom.ecssp.mobile.pub.AndroidService;
import net.evecom.org.Organization;
import net.evecom.org.User;
import net.evecom.org.UserInfo;
import net.evecom.pub.annotation.Controller;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.upload.UploadFile;

/**
 * 
 * 描述 移动端事件控制类
 * 
 * @author Mars zhang
 * @created 2015年11月11日 上午11:55:39
 */
@Controller(controllerKey = "/jfs/ecssp/mobile/eventCtr")
@Before({ EventInterceptor.class, Tx.class })
public class EventController extends MobileController {

    /**
     * 
     * 获取事件列表
     * 
     * @author Mars zhang
     * @created 2015年6月2日 上午8:54:27
     */
    public void getEnentList() {
        String deptid = getPara("deptid");
        List<InfoReception> infoReceptions = AndroidService.service.geteventLis("1", getPara("pagesize") + "",deptid);
        renderJson(infoReceptions);

    }

    /**
     * 
     * 保存事件
     * 
     * @author Mars zhang
     * @created 2015年5月25日 上午11:11:05
     */
    public void eventAdd() {
        InfoReception infoReception = getModel(InfoReception.class);
        infoReception.set("eventstatus", "1");// 处理状态 待处理
        infoReception.set("happendate", new java.sql.Timestamp(new Date(System.currentTimeMillis()).getTime()));
        infoReception.set("reporterdate", new java.sql.Timestamp(new Date(System.currentTimeMillis()).getTime()));
        infoReception.set("CURSTATUS", "1");
        String belongunitid = infoReception.get("belongunitid") + "";
        if (null != belongunitid && belongunitid.length() > 0) {
            Organization organization = Organization.dao.findById(belongunitid);
            if (null != organization) {
                infoReception.set("eventarea", organization.getStr("belongarea"));
            }
        }
        infoReception.save();
        EventCopy eventCopy = new EventCopy();
        eventCopy.set("EVENTID", infoReception.get("id"));
        eventCopy.set("RECEIVEDEPTID", "root");
        eventCopy.set("RECEIVEDEP", "福建省政府");
        eventCopy.save();
        renderJson(infoReception);
    }

    /**
     * 
     * 更新事件文件
     * 
     * @author Mars zhang
     * @created 2015年5月25日 上午11:11:05
     */
    public void eventFileSave() {
        List<UploadFile> upFiles = getFiles(getAbsoluteUploadfath() + "event", 100 * 1024 * 1024);
        if (null == upFiles || upFiles.size() == 0) {
            renderPrmOut(upFiles.size());
            return;
        }
        String eventId = getPara("eventId");
        InfoReception infoReception = InfoReception.dao.findById(eventId);
        StringBuilder fileids = saveFiles(upFiles, getRelativeUploadfath() + "event");
        infoReception.set("EVENTANNEX", fileids.substring(0, fileids.length() - 1));
        infoReception.update();
        renderPrmOut(upFiles.size());
    }

    /**
     * 
     * 描述 signEvent 事件签到 表示该人员已经到达事件现场
     * 
     * @author Mars zhang
     * @created 2015年12月1日 上午10:26:40
     */
    public void signEvent() {
        User mUser = getUser();
        UserInfo mUserInfo = UserInfo.dao.findById(mUser.get("id"));

        MobileScenperson mobileScenperson = new MobileScenperson();
        mobileScenperson.set("PERSONID", mUser.get("id"));
        mobileScenperson.set("EVENTID", getPara("eventid"));
        mobileScenperson.set("PERSONNAME", mUserInfo.get("name"));
        mobileScenperson.set("PERSONPHONE", mUserInfo.get("mobile"));
        mobileScenperson.set("gisx", getPara("gisx"));
        mobileScenperson.set("gisy", getPara("gisy"));
        mobileScenperson.set("orgid", getPara("orgid"));
        
        mobileScenperson.save();
        renderText(mUserInfo.get("name") + "签到成功，当前位置：(" + getPara("gisx") + "," + getPara("gisy") + ")");
    }

    /**
     * 
     * 
     * 描述 查询周边资源
     * 
     * @author Mars zhang
     * @created 2015年12月8日 上午11:39:52
     */
    public void searchResourceAround() {
        double centergisx = Double.parseDouble(getPara("centergisx"));
        double centergisy = Double.parseDouble(getPara("centergisy"));
        double longdiffer = Double.parseDouble(getPara("longdiffer"));
        double latdiffer = -Double.parseDouble(getPara("latdiffer"));
        List<Record> records = AndroidService.service.searchResourceByRecGis(centergisx, centergisy, longdiffer,
                latdiffer, getPara("pagesize"), getPara("resourcename"), getPara("resourcetype"));
        renderJson(records);
        System.out.println("(" + (centergisx - longdiffer) + "," + (centergisy - latdiffer) + ")+-("
                + (longdiffer + longdiffer) + "," + (latdiffer + latdiffer) + ")");

    }

    /**
     * 
     * 描述 获取八大资源类型
     * 
     * @author Mars zhang
     * @created 2015年12月11日 上午9:58:42
     */
    public void getResourceType() {
        List<ResourceType> resourceTypes = AndroidService.service.findChildByParentIds("root");
        renderJson(resourceTypes);
    }

    /**
     * 
     * 描述 事件类型移动端异步树
     * 
     * @author Mars zhang
     * @created 2015年12月16日 下午3:39:17
     */
    public void getAsyEventTypeTree() {
        String currentid = getPara("currentid");
        List<EventType> eventTypes = AndroidService.service.getEventTypesByParentid(currentid);
        renderJson(eventTypes);
    }

}
