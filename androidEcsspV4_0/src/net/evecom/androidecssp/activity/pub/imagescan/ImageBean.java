/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package net.evecom.androidecssp.activity.pub.imagescan;

/**
 * 
 * ���� GridView��ÿ��item�����ݶ���
 * 
 * @author Mars zhang
 * @created 2015-11-25 ����11:26:43
 */
public class ImageBean {
    /**
     * �ļ��еĵ�һ��ͼƬ·��
     */
    private String topImagePath;
    /**
     * �ļ�����
     */
    private String folderName;
    /**
     * �ļ����е�ͼƬ��
     */
    private int imageCounts;

    public String getTopImagePath() {
        return topImagePath;
    }

    public void setTopImagePath(String topImagePath) {
        this.topImagePath = topImagePath;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public int getImageCounts() {
        return imageCounts;
    }

    public void setImageCounts(int imageCounts) {
        this.imageCounts = imageCounts;
    }

}
