/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package net.evecom.androidecssp.activity.taskresponse;

import java.io.Serializable;

/**
 * 
 * ����
 * 
 * @author Mars zhang
 * @created 2015-11-25 ����11:24:16
 */
public class TaskInfo implements Serializable {
    /** MemberVariables */
    private String id;
    /** MemberVariables */
    private String taskname;
    /** MemberVariables */
    private String taskcontern;
    /** MemberVariables */
    private String createtime;
    /** MemberVariables */
    private String keyword;
    /** MemberVariables */
    private String taskdept;
    /** MemberVariables */
    private String taskdeptid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaskname() {
        return taskname;
    }

    public void setTaskname(String taskname) {
        this.taskname = taskname;
    }

    public String getTaskcontern() {
        return taskcontern;
    }

    public void setTaskcontern(String taskcontern) {
        this.taskcontern = taskcontern;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getTaskdept() {
        return taskdept;
    }

    public void setTaskdept(String taskdept) {
        this.taskdept = taskdept;
    }

    public String getTaskdeptid() {
        return taskdeptid;
    }

    public void setTaskdeptid(String taskdeptid) {
        this.taskdeptid = taskdeptid;
    }

}
