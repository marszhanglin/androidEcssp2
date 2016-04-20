/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package net.evecom.androidecssp.gps.bean;

import java.io.Serializable;

/**
 * 
 * √Ë ˆ
 * 
 * @author Mars zhang
 * @created 2015-11-25 …œŒÁ11:25:08
 */
public class GpsPoint implements Serializable {

    /**
     * √Ë ˆ
     */
    private static final long serialVersionUID = 1L;
    /**
     * √Ë ˆ
     */
    /** MemberVariables */
    private String id;
    /** MemberVariables */
    private String name;
    /** MemberVariables */
    private double gisx;
    /** MemberVariables */
    private double gisy;
    /** MemberVariables */
    private String description;
    /** MemberVariables */
    private int pupResourceId;
    /** MemberVariables */
    private int pointResourceId;
    
    public int getPupResourceId() {
        return pupResourceId;
    }
    public void setPupResourceId(int pupResourceId) {
        this.pupResourceId = pupResourceId;
    }
    public int getPointResourceId() {
        return pointResourceId;
    }
    public void setPointResourceId(int pointResourceId) {
        this.pointResourceId = pointResourceId;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public double getGisx() {
        return gisx;
    }
    public void setGisx(double gisx) {
        this.gisx = gisx;
    }
    public double getGisy() {
        return gisy;
    }
    public void setGisy(double gisy) {
        this.gisy = gisy;
    }
    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
 

    

}
