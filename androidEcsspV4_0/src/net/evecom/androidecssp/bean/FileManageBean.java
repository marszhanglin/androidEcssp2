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
 * 2014-7-22����4:46:54 ��FileManageBean
 * 
 * @author Mars zhang
 * 
 */
@Table(name = "File_ManageBean")
public class FileManageBean {
    /** ���� */
    @Id(column = "FileID")
    private int FileID;
    /** ���� */
    private String FileDESCRIP;
    /** ��λʱ�� */
    private Date FileTime;
    /** ·�� */
    private String FileURL;
    /** �ļ������� ͼƬ�ļ�1 ��Ƶ�ļ�2 ��Ƶ�ļ�3 */
    private String FileFlag;
    /** �����ֶ� */
    private String Fileby1;
    /** �ļ����� */
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
