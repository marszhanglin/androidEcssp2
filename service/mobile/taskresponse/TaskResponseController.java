/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package net.evecom.ecssp.mobile.taskresponse;

import java.util.Date;
import java.util.List;

import net.evecom.ecapp.plan.eventresponse.PlanEventTaskResponse;
import net.evecom.ecapp.plan.eventresponse.TaskResponseService;
import net.evecom.ecapp.plan.planemergency.PlanEventDisproject;
import net.evecom.ecapp.plan.planemergency.PlanEventTask;
import net.evecom.ecssp.esb.EventInterceptor;
import net.evecom.ecssp.mobile.common.MobileController;
import net.evecom.ecssp.mobile.pub.AndroidService;
import net.evecom.pub.annotation.Controller;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.upload.UploadFile;

/**
 * 
 * 描述 事件反馈类
 * 
 * @author Mars zhang
 * @created 2015年11月11日 上午11:56:42
 */
@Controller(controllerKey = "/jfs/ecssp/mobile/taskresponseCtr")
@Before({ EventInterceptor.class, Tx.class })
public class TaskResponseController extends MobileController {

    /**
     * 
     * 获取事件下所有的处置项目
     * 
     * @author Mars zhang
     * @created 2015年5月25日 上午11:11:05
     */
    public void getAllProjectByeventId() {
        String eventId = getPara("eventId");
        List<PlanEventDisproject> planEventDisprojects = TaskResponseService.service.findAllProjectByeventId(eventId);
        renderJson(planEventDisprojects);
    }

    /**
     * 
     * 获取事件下所有的任务
     * 
     * @author Mars zhang
     * @created 2015年5月25日 上午11:11:05
     */
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
     * 获取 组织下所有的任务
     * 
     * @author Mars zhang
     * @created 2015年5月25日 上午11:11:05
     */
    public void getTaskByDeptIdAndUserId() {
        String deptid = getPara("deptid");
        String userid = getPara("userid");
        // 查询处置项目下的所有任务
        List<PlanEventTask> planEventTasks = TaskResponseService.service.getTaskByDeptIdAndUserId(deptid, userid);
        renderJson(planEventTasks);
    }

    /**
     * 
     * 获取反馈
     * 
     * @author Mars zhang
     * @created 2015年5月25日 上午11:11:05
     */
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
    public void taskResponseAdd() {
        PlanEventTaskResponse eventTaskResponse = getModel(PlanEventTaskResponse.class);
        eventTaskResponse.set("responsetime", new java.sql.Timestamp(new Date(System.currentTimeMillis()).getTime()));
        eventTaskResponse.set(
                "responselevel",
                AndroidService.service.getDictValueByKeyAndName("PLAN_EVENTTASK_RESPONSE_LEVEL",
                        eventTaskResponse.get("responselevel") + ""));
        
        // 获取处置项目id并更新反馈的处置项目
        List<String> projectids = Db.query(" select  projectid   from t_plan_event_task t "
                + " start with t.id = ? "
                + " connect by  t.id = prior t.projectid  ",
                new Object[] { eventTaskResponse.get("taskid") });
        eventTaskResponse.set("projectid", projectids.get(projectids.size()-1));
        
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
/*    public void taskResponseFileSave() {
        List<UploadFile> upFiles = getFiles(getAbsoluteUploadfath() + "taskresponse", 100 * 1024 * 1024);
        if (null == upFiles || upFiles.size() == 0) {
            renderPrmOut(upFiles.size());
            return;
        }
        String taskresponseId = getPara("taskresponseId");
        PlanEventTaskResponse response = PlanEventTaskResponse.dao.findById(taskresponseId);
        StringBuilder fileids = saveFiles(upFiles, getRelativeUploadfath() + "taskresponse");
        response.set("DETAILATTACH", fileids.substring(0, fileids.length() - 1));
        response.update();
        renderPrmOut(upFiles.size());
    }*/
    
    
    /**
     * 
     * 保存反馈文件
     * 
     * @author Mars zhang
     * @created 2015年5月25日 上午11:11:05
     */
    public void taskResponseFileSave() {
        List<UploadFile> upFiles = getFiles(getAbsoluteUploadfath() + "taskresponse", 100 * 1024 * 1024);
        if (null == upFiles || upFiles.size() == 0) {
            renderText("error");
            return;
        }
        StringBuilder fileids = saveFiles(upFiles, getRelativeUploadfath() + "taskresponse");
        renderText(fileids.substring(0, fileids.length() - 1));
    }

    /**
     * 
     * 描述
     * 
     * @author Mars zhang
     * @created 2015年11月17日 上午10:11:13
     */
    public void taskadd() {
        PlanEventTask planEventTask = getModel(PlanEventTask.class);
        planEventTask.set("state", "3");// 任务状态 默认 未进行
        planEventTask.save();
        renderPrmOut(planEventTask);
    }
}
