/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package net.evecom.androidecssp.activity.taskresponse;

import java.io.Serializable;

/**
 * 
 * 描述
 * 
 * @author Mars zhang
 * @created 2015-11-25 上午11:24:38
 */
public class TaskResponseInfo implements Serializable {
    /** MemberVariables */
    private String id;
    /** MemberVariables */
    private String responsetitle;
    /** MemberVariables */
    private String responsecon;
    /** MemberVariables */
    private String createtime;
    /** MemberVariables */
    private String remark;
    /** MemberVariables */
    private String name;
    /** MemberVariables */
    private String detailattach;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResponsetitle() {
        return responsetitle;
    }

    public void setResponsetitle(String responsetitle) {
        this.responsetitle = responsetitle;
    }

    public String getResponsecon() {
        return responsecon;
    }

    public void setResponsecon(String responsecon) {
        this.responsecon = responsecon;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetailattach() {
        return detailattach;
    }

    public void setDetailattach(String detailattach) {
        this.detailattach = detailattach;
    }

}
/*
 * { "eventid": "d44c4fea0fb048e9b5567488e182d199", "responselevel": "2",
 * "createtime": "2015-05-30 19:32:12", "responsetitle": "设置警戒点对警戒范围内进行戒严",
 * "relationid": null, "remark": "设置警戒点对警戒范围内进行戒严", "dataflg": null, "createby":
 * null, "responsedeptid": "340b8284b60448a8938633ffb2e092ed", "updateby": null,
 * "responsename": "张三", "name": "省公安厅", "responsetime": "2015-05-13 19:32:05",
 * "detailattach": null, "planid": "1faa0daa10254580b40470e6e145694e", "id":
 * "f8bc8766835a4b61b6ed23a1a79e9225", "state": null, "updatetime": null,
 * "projectid": null, "taskid": "3427290d13d64e52b92c76d373353d67",
 * "responsecon": "设置警戒点对警戒范围内进行戒严", "responseid":
 * "c28d1f5cba824f5296dba0e49532467f" }
 */