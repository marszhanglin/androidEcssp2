/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package net.evecom.androidecssp.bean;

import java.util.Date;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

/**
 * 
 * 2014-7-22下午4:46:54 类FileManageBean
 * 
 * @author Mars zhang
 * 
 */
@Table(name = "File_ManageBean")
public class FileManageBean {
    /** 主键 */
    @Id(column = "FileID")
    private int FileID;
    /** 描述 */
    private String FileDESCRIP;
    /** 定位时间 */
    private Date FileTime;
    /** 路径 */
    private String FileURL;
    /** 文件的类型 图片文件1 视频文件2 音频文件3 */
    private String FileFlag;
    /** 备用字段 */
    private String Fileby1;
    /** 文件名称 */
    private String FileName;

    public int getFileID() {
        return FileID;
    }

    public void setFileID(int file_ID) {
        FileID = file_ID;
    }

    public String getFileDESCRIP() {
        return FileDESCRIP;
    }

    public void setFileDESCRIP(String file_DESCRIP) {
        FileDESCRIP = file_DESCRIP;
    }

    public Date getFileTime() {
        return FileTime;
    }

    public void setFileTime(Date file_Time) {
        FileTime = file_Time;
    }

    public String getFileURL() {
        return FileURL;
    }

    public void setFileURL(String file_URL) {
        FileURL = file_URL;
    }

    public String getFileFlag() {
        return FileFlag;
    }

    public void setFileFlag(String file_Flag) {
        FileFlag = file_Flag;
    }

    public String getFileby1() {
        return Fileby1;
    }

    public void setFileby1(String file_by1) {
        Fileby1 = file_by1;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String file_Name) {
        FileName = file_Name;
    }

}
