/*
 * Copyright (c) 2005, 2015, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package net.evecom.ecssp.mobile.event;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

import net.evecom.ecapp.event.infomonitoring.EventInidmonitor;
import net.evecom.ecapp.event.inforeception.InfoContinue;
import net.evecom.ecapp.event.inforeception.InfoReception;
import net.evecom.ecapp.event.inforeception.InfoReceptionService;
import net.evecom.ecssp.esb.EventInterceptor;
import net.evecom.ecssp.mobile.common.MobileController;
import net.evecom.pub.annotation.Controller;
import net.evecom.pub.util.ToolDateTime;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.upload.UploadFile;

/**
 * 事件续报控制层
 * 
 * @author Stark Zhou
 * @created 2015-12-21 下午3:32:41
 */
@Controller(controllerKey = "/jfs/ecssp/mobile/eventContinueCtr")
@Before({ EventInterceptor.class, Tx.class })
public class EventContinueController extends MobileController {

    /**
     * 
     * 获取续报事件列表
     * 
     * @author Stark Zhou
     * @created 2015-12-30 下午2:47:22
     */
    public void getEventContinue() {
        String eventId = getPara("eventid");
        List<InfoContinue> eventContinue = InfoReceptionService.service.findInfoContinueById(eventId);
        renderJson(eventContinue);
    }

    /**
     * 
     * 事件续报
     * 
     * @author Stark Zhou
     * @throws ParseException
     * @created 2015-12-30 下午2:47:39
     */
    public void eventContinue() throws ParseException {
        String eventId = getPara("eventid");
        // 根据事件ID获取上报的事件信息
        InfoReception infoReception = InfoReception.dao.findById(eventId);
        InfoContinue infoContinue = getModel(InfoContinue.class);
        infoContinue.set("EVENTID", eventId);
        // 获取续报类别（当续报类别为1的时候(旧事件)，需要修改上报的关键指标值）
        String continueType = infoContinue.getStr("COUTINUETYPE");
        if ("1".equals(continueType)) {
            // 如果为旧事件,保存原事件所属区域和事发时间到续报信息
            infoContinue.set("COUTINUEAREA", infoReception.getStr("EVENTAREA"));
            infoContinue.set("HAPPENDATE", infoReception.get("HAPPENDATE"));
            infoContinue.set("HAPPENADDRESS", infoReception.get("HAPPENADDRESS"));
            infoContinue.set("GISX", infoReception.get("GISX"));
            infoContinue.set("GISY", infoReception.get("GISY"));
        } else {
            String happenStr = getPara("infoContinue.happendate");
            SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            java.util.Date happenDateU = sim.parse(happenStr);
            Timestamp happenDate = java.sql.Timestamp.valueOf(ToolDateTime.format(happenDateU, "yyyy-MM-dd HH:mm:ss"));
            infoContinue.set("HAPPENDATE", happenDate);
        }
        // 日期类型转换
        String reportStr = getPara("infoContinue.reporterdate");
        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date reportDateU = sim.parse(reportStr);
        Timestamp reportDate = java.sql.Timestamp.valueOf(ToolDateTime.format(reportDateU, "yyyy-MM-dd HH:mm:ss"));
        infoContinue.set("REPORTERDATE", reportDate);
        infoContinue.save();
        // 突发事件关键指标更新
        List<EventInidmonitor> listEventInidmonitors = InfoReceptionService.service.findEventInidmonitorById(eventId);
        for (int i = 0; i < listEventInidmonitors.size(); i++) {
            String indivalue = getPara("indivalue" + i);
            EventInidmonitor eventInidmonitor = listEventInidmonitors.get(i);
            eventInidmonitor.set("indivalue", indivalue);
            eventInidmonitor.update();
        }
        // 保存关联指标信息
        EventInidmonitor eventInidmonitor = getModel(EventInidmonitor.class);// 事件关联指标信息
        // 根据事件ID查找关联指标信息
        List<EventInidmonitor> listEventInidmonitor = InfoReceptionService.service.findEventInidmonitorById(eventId);
        if (listEventInidmonitor.size() != 0) {
            for (int i = 0; i < listEventInidmonitor.size(); i++) {
                // 获取监测指标值
                String indicode = getPara("indivalue" + i);
                String eventInId = UUID.randomUUID().toString();
                eventInidmonitor.set("ID", eventInId);
                eventInidmonitor.set("INDIVALUE", indicode);
                eventInidmonitor.set("EVENTID", eventId);
                eventInidmonitor.set("CONTINUEID", infoContinue.getStr("ID"));
                eventInidmonitor.set("INDINAME", listEventInidmonitor.get(i).getStr("INDINAME"));
                eventInidmonitor.set("INDICODE", listEventInidmonitor.get(i).getStr("INDICODE"));
                eventInidmonitor.save();
            }
        }
        renderJson(infoContinue.getStr("id"));
    }

    /**
     * 
     * 获取续报指标
     * 
     * @author Stark Zhou
     * @created 2015-12-30 下午2:47:54
     */
    public void getContinueMonitor() {
        String continueId = getPara("continueid");
        String eventId = getPara("eventid");
        List<EventInidmonitor> continueMonitor = InfoReceptionService.service.findEventInidmonitorById(eventId,
                continueId);
        renderJson(continueMonitor);
    }

    /**
     * 
     * 获取事件指标
     * 
     * @author Stark Zhou
     * @created 2015-12-30 下午2:48:24
     */
    public void getEventMonitor() {
        String eventId = getPara("eventid");
        List<EventInidmonitor> eventMonitor = InfoReceptionService.service.findEventInidmonitorById(eventId);
        renderJson(eventMonitor);
    }

    /**
     * 
     * 续报附件上传
     * 
     * @author Stark Zhou
     * @created 2016-1-4 上午11:28:14
     */
    public void continueFileSave() {
        List<UploadFile> upFiles = getFiles(getAbsoluteUploadfath() + "event", 100 * 1024 * 1024);
        if (null == upFiles || upFiles.size() == 0) {
            renderPrmOut(upFiles.size());
            return;
        }
        String continueId = getPara("continueId");
        InfoContinue eventContinue = InfoContinue.dao.findById(continueId);

        StringBuilder fileids = saveFiles(upFiles, getRelativeUploadfath() + "event");
        eventContinue.set("CONTINUEANNEX", fileids.substring(0, fileids.length() - 1));
        eventContinue.update();
        renderPrmOut(upFiles.size());
    }
}
