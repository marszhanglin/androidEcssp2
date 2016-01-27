/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package net.evecom.ecssp.mobile.test;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.evecom.ecapp.event.inforeception.InfoReception;
import net.evecom.ecapp.plan.eventresponse.PlanEventTaskResponse;
import net.evecom.ecapp.plan.eventresponse.TaskResponseService;
import net.evecom.ecapp.plan.planemergency.PlanEventDisproject;
import net.evecom.ecapp.plan.planemergency.PlanEventTask;
import net.evecom.ecssp.mobile.AccessService;
import net.evecom.ecssp.mobile.common.MobileController;
import net.evecom.ecssp.mobile.pub.AndroidService;
import net.evecom.org.Organization;
import net.evecom.org.User;
import net.evecom.org.UserInfo;
import net.evecom.pub.annotation.Controller;
import net.evecom.pub.annotation.Security;
import net.evecom.pub.annotation.SystemLog;
import net.evecom.pub.util.MD5Util;
import net.evecom.system.Attachment;
import net.evecom.system.DictCache;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.upload.UploadFile;

/**
 * 
 * android登录验证
 * 
 * @Before 第一步控制对象拦截，第二步启动事务，这和写的先后顺序有关
 * @author Mars zhang
 * @created 2015年6月2日 上午8:54:59
 */
@Controller(controllerKey = "/jfs/mobile/androidIndex")
@Before({ Tx.class })
public class AndroidLoginController extends MobileController {

    /**
     * 测试
     * 
     * @author Yancey Qiu
     * @created 2014年11月17日 上午9:44:36
     */
    public void post() {
        Map<String, Object> prmOut = new HashMap<String, Object>();
        prmOut.put("name", "测试");
        prmOut.put("now", new java.sql.Timestamp(new Date().getTime()));
        prmOut.put("now2", new Date());

        // 如果业务错误手动抛出异常，让事务回滚
        // if (true)
        // throw new RuntimeException("test err");

        renderPrmOut(prmOut);
    }

    /**
     * 
     * 描述 登录
     * 
     * @author Mars zhang
     * @created 2015年5月21日 上午8:50:57
     */
    public void login() {
        String username = getPara("username");
        String password = getPara("password");
        String imei = getPara("imei");
        System.out.println(username);
        // System.out.println(EncryptUtil.getInstance().AESdecode(password));
        System.out.println(password);
        System.out.println(imei);

        // 验证用户合法性
        User user = AccessService.service.getUserByLoginName(username);

        if (user == null) {
            // throw new RuntimeException("当前用户不存在！");
            renderText("当前用户不存在！");
            return;
        }

        if (!MD5Util.md5(password).equals(user.getStr("pwd"))) {
            // throw new RuntimeException("当前用户密码错误！");
            renderText("当前用户密码错误！");
            return;
        }

        if ("0".equals(user.getStr("status"))) {
            // throw new RuntimeException("当前用户被禁用，请联系管理员开通！");
            renderText("当前用户被禁用，请联系管理员开通！");
            return;
        }
        Map<String, String> dictmap = DictCache.getInstance().getDictKeyValueMap();
        UserInfo userInfo = UserInfo.dao.findById(user.get("id"));
        User user2 = User.dao.findById(user.get("id"));
        Organization organization = Organization.dao.findById(user2.get("orgid"));
        StringBuilder sb = new StringBuilder();
        sb.append("@_2_" + userInfo.getStr("id"));
        sb.append("@_2_" + userInfo.getStr("name"));
        sb.append("@_2_" + dictmap.get("SYSTEM_SEX_" + userInfo.get("sex")));
        sb.append("@_2_" + userInfo.getStr("mobile"));
        sb.append("@_2_" + organization.getStr("id"));
        sb.append("@_2_" + organization.getStr("name"));
        renderText("true@" + sb.toString());
    }

    /**
     * 
     * 获取事件列表
     * 
     * @author Mars zhang
     * @created 2015年6月2日 上午8:54:27
     */
    public void getEnentList() {
        List<InfoReception> infoReceptions = AndroidService.service.geteventLis("1", "15",null);
        renderJson(infoReceptions);

    }

    /**
     * 
     * 获取事件下所有的处置项目
     * 
     * @author Mars zhang
     * @created 2015年5月25日 上午11:11:05
     */
    @Security
    @SystemLog(actionName = "获取事件下所有的处置项目", menuName = "任务处置反馈")
    public void getAllProjectByeventId() {
        String eventId = getPara("eventId");
        List<PlanEventDisproject> planEventDisprojects = TaskResponseService.service.findAllProjectByeventId(eventId);
        // setAttr("planEventDisprojects", planEventDisprojects);
        renderJson(planEventDisprojects);
    }

    /**
     * 
     * 获取事件下所有的处置项目
     * 
     * @author Mars zhang
     * @created 2015年5月25日 上午11:11:05
     */
    @Security
    @SystemLog(actionName = "获取事件下所有的处置项目", menuName = "任务处置反馈")
    public void getTaskByEventIdAndProjectId() {
        String eventId = getPara("eventId");
        String projectId = getPara("projectId");
        // 查询处置项目下的所有任务
        List<PlanEventTask> planEventTasks = TaskResponseService.service.queryAllTaskByEventIdAndProjectId(eventId,
                projectId);
        renderJson(planEventTasks);
    }

    /**
     * 
     * 获取反馈
     * 
     * @author Mars zhang
     * @created 2015年5月25日 上午11:11:05
     */
    @Security
    @SystemLog(actionName = "获取反馈", menuName = "任务处置反馈")
    public void getTaskResponseByTaskId() {
        String taskId = getPara("taskId");
        List<PlanEventTaskResponse> eventTaskResponses = AndroidService.service.getTaskResponseByTaskId(taskId);
        renderJson(eventTaskResponses);
    }

    /**
     * 
     * 保存反馈
     * 
     * @author Mars zhang
     * @created 2015年5月25日 上午11:11:05
     */
    @Security
    @SystemLog(actionName = "获取事件下所有的处置项目", menuName = "任务处置反馈")
    public void taskResponseAdd() {
        String eventid = getPara("eventid");
        String responselevel = getPara("responselevel");
        responselevel = AndroidService.service.getDictValueByKeyAndName("PLAN_EVENTTASK_RESPONSE_LEVEL", responselevel);
        String responsetitle = getPara("responsetitle");
        String remark = getPara("remark");
        String responsedeptid = getPara("responsedeptid");
        String responsename = getPara("responsename");
        String planid = getPara("planid");
        String taskid = getPara("taskid");
        String responsecon = getPara("responsecon");
        String responseid = getPara("responseid");

        PlanEventTaskResponse eventTaskResponse = new PlanEventTaskResponse();
        eventTaskResponse.set("eventid", eventid);
        eventTaskResponse.set("responselevel", responselevel);
        eventTaskResponse.set("responsetitle", responsetitle);
        eventTaskResponse.set("remark", remark);
        eventTaskResponse.set("responsedeptid", responsedeptid);
        eventTaskResponse.set("responsename", responsename);
        eventTaskResponse.set("planid", planid);
        eventTaskResponse.set("taskid", taskid);
        eventTaskResponse.set("responsecon", responsecon);
        eventTaskResponse.set("responseid", responseid);
        eventTaskResponse.set("responsetime", new java.sql.Timestamp(new Date(System.currentTimeMillis()).getTime()));
        eventTaskResponse.save();
        renderJson(eventTaskResponse);
    }

    /**
     * 
     * 保存反馈文件
     * 
     * @author Mars zhang
     * @created 2015年5月25日 上午11:11:05
     */
    @Security
    @SystemLog(actionName = "保存反馈文件", menuName = "任务处置反馈")
    public void taskResponseFileSave() {
        List<UploadFile> upFiles = getFiles("taskresponse");
        if (null == upFiles || upFiles.size() == 0) {
            renderText("failure--" + upFiles.size());
            return;
        }
        String taskresponseId = getPara("taskresponseId");
        StringBuilder fileids = new StringBuilder();
        PlanEventTaskResponse response = PlanEventTaskResponse.dao.findById(taskresponseId);

        for (UploadFile fileitem : upFiles) {
            String oldfilename = fileitem.getFileName();
            // System.out.println(fileitem.getSaveDirectory());
            String type = fileitem.getFileName().split("\\.")[1];
            File file = fileitem.getFile();
            String tempName = System.currentTimeMillis() + "." + type;
            // 防止处重名
            file.renameTo(new File(fileitem.getSaveDirectory() + "\\" + tempName));
            // String fileid=UUID.randomUUID().toString();
            Attachment attachment = new Attachment();
            // attachment.set("ID", fileid);
            attachment.set("FILENAME", oldfilename);
            attachment.set("FILEALIAS", tempName);
            attachment.set("TYPE", type);
            attachment.set("FILEPATH", "\\upload\\taskresponse\\");
            attachment.set("FILESIZE", file.length());
            attachment.save();
            fileids.append(attachment.get("id") + ",");
        }
        response.set("DETAILATTACH", fileids.substring(0, fileids.length() - 1));
        response.update();
        renderText("success--" + upFiles.size());
    }

    /**
     * 
     * 保存事件
     * 
     * @author Mars zhang
     * @created 2015年5月25日 上午11:11:05
     */
    @Security
    @SystemLog(actionName = "保存事件", menuName = "事件上报")
    public void eventAdd() {
        /*
         * String eventlever=getPara("eventlever"); eventlever=
         * AndroidService.service
         * .getDictValueByKeyAndName("SUDDENEVENT_LEVEL",eventlever); String
         * eventname=getPara("eventname"); String
         * happenaddress=getPara("happenaddress"); String
         * eventcontent=getPara("eventcontent"); String
         * belongunitid=getPara("belongunitid"); String areaId="";
         * if(null!=belongunitid&&belongunitid.length()>0){ Organization
         * organization= Organization.dao.findById(belongunitid);
         * if(null!=organization){ areaId=organization.getStr("belongarea"); } }
         * String reporterperson=getPara("reporterperson"); String
         * reportertel=getPara("reportertel"); String
         * eventstatus=getPara("eventstatus"); eventstatus=
         * AndroidService.service
         * .getDictValueByKeyAndName("EVENT_DEAL_STATUS",eventstatus); String
         * gisy=getPara("gisy"); String gisx=getPara("gisx");
         * 
         * //
         * 
         * InfoReception infoReception=new InfoReception();
         * infoReception.set("eventtype", "12000");//事件类型暂时用事故灾难
         * infoReception.set("eventlever", eventlever);
         * infoReception.set("eventname", eventname);
         * infoReception.set("eventarea", areaId);//事件区域用登录者的所属区域
         * infoReception.set("belongunitid", belongunitid);//所属机构用登录者的所属单位
         * infoReception.set("happenaddress", happenaddress);
         * infoReception.set("happendate", new java.sql.Timestamp(new
         * Date(System.currentTimeMillis()).getTime()));
         * infoReception.set("eventcontent", eventcontent);
         * infoReception.set("reporterdeptid", belongunitid);//上报机构用登录者的所属单位
         * infoReception.set("reporterperson", reporterperson);
         * infoReception.set("reporterdate", new java.sql.Timestamp(new
         * Date(System.currentTimeMillis()).getTime()) );
         * infoReception.set("reportertel", reportertel);
         * infoReception.set("eventstatus", eventstatus);
         * infoReception.set("GISX", gisx); infoReception.set("GISY", gisy);
         * infoReception.set("CURSTATUS", "1");
         */
        InfoReception infoReception = getModel(InfoReception.class);
        infoReception.save();
        renderJson(infoReception);
    }

    /**
     * 
     * 更新事件文件
     * 
     * @author Mars zhang
     * @created 2015年5月25日 上午11:11:05
     */
    @Security
    @SystemLog(actionName = "保存事件", menuName = "事件上报")
    public void eventFileSave() {
        List<UploadFile> upFiles = getFiles("event");
        if (null == upFiles || upFiles.size() == 0) {
            renderText("failure--" + upFiles.size());
            return;
        }
        String eventId = getPara("eventId");
        InfoReception infoReception = InfoReception.dao.findById(eventId);
        StringBuilder fileids = saveFiles(upFiles, "event");
        infoReception.set("EVENTANNEX", fileids.substring(0, fileids.length() - 1));
        infoReception.update();
        renderText("success--" + upFiles.size());
    }

    /**
     * 描述 保存并储存文件信息
     * 
     * @author Mars zhang
     * @created 2015年11月10日 下午3:55:33
     * @param upFiles
     * @return
     */
    protected StringBuilder saveFiles(List<UploadFile> upFiles, String dirname) {
        StringBuilder fileids = new StringBuilder();
        for (UploadFile fileitem : upFiles) {
            String oldfilename = fileitem.getFileName();
            String type = fileitem.getFileName().split("\\.")[1];
            File file = fileitem.getFile();
            String tempName = UUID.randomUUID() + "." + type;
            file.renameTo(new File(fileitem.getSaveDirectory() + "\\" + tempName));
            Attachment attachment = new Attachment();
            attachment.set("FILENAME", oldfilename);
            attachment.set("FILEALIAS", tempName);
            attachment.set("TYPE", type);
            attachment.set("FILEPATH", "\\upload\\" + dirname);
            attachment.set("FILESIZE", file.length());
            attachment.save();
            fileids.append(attachment.get("id") + ",");
        }
        return fileids;
    }

    /**
     * 
     * 获取数据字典缓存
     * 
     * @author Mars zhang
     * @created 2015年6月25日 上午8:44:56
     */
    public void gitSysDicts() {
        renderJson(DictCache.getInstance().getDictKeyValueMap());
    }

    /**
     * 
     * 描述 基础信息首页
     * 
     * @author Mars zhang
     * @created 2015年11月5日 上午9:01:49
     */
    public void jqmobileTest() {
        renderJsp("/mobile/base/index.jsp");
    }

    /**
     * 
     * 描述 基础信息首页
     * 
     * @author Mars zhang
     * @created 2015年11月5日 上午9:01:49
     */
    public void jqmobileBlist() {
        System.out.println(getRequest().getRemoteHost());
        renderJsp("/mobile/base/building/building_list.jsp");
    }

}
