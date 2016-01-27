/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package net.evecom.ecssp.mobile.pub;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import net.evecom.ecssp.esb.EventInterceptor;
import net.evecom.ecssp.mobile.common.MobileController;
import net.evecom.org.Organization;
import net.evecom.org.UserInfo;
import net.evecom.pub.annotation.Controller;
import net.evecom.system.Area;
import net.evecom.system.Attachment;
import net.evecom.system.Dict;

import com.jfinal.aop.Before;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.tx.Tx;

/**
 * 
 * 描述 移动端事件控制类
 * 
 * @author Mars zhang
 * @created 2015年11月11日 上午11:55:39
 */
@Controller(controllerKey = "/jfs/ecssp/mobile/pubCtr")
@Before({ EventInterceptor.class, Tx.class })
public class PublicController extends MobileController {

    /**
     * 
     * 获取事件列表
     * 
     * @author Mars zhang
     * @created 2015年6月2日 上午8:54:27
     */
    public void getDictByKey() {
        List<Dict> dicts = AndroidService.service.getDictByKey(getPara("dictkey"));
        renderJson(dicts);
    }

    /**
     * 
     * 描述 用户查询列表
     * 
     * @author Mars zhang
     * @created 2015年11月16日 上午10:51:14
     */
    public void searchUserlist() {
        List<UserInfo> infos = AndroidService.service.searchUserlist(getPara("name"), getPara("orgname"),
                getPara("sexDict"), getPara("pagesize"));
        renderJson(infos);
    }

    /**
     * 
     * 描述 组织机构搜索
     * 
     * @author Mars zhang
     * @created 2015年11月17日 下午2:24:57
     */
    public void searchOrglist() {
        List<Organization> infos = AndroidService.service.searchOrglist(getPara("name"), getPara("pagesize"));
        renderJson(infos);
    }
    /**
     * 
     * 描述  
     * @author Mars zhang
     * @created 2015年12月17日 下午4:29:17
     */
    public void  getAsyDeptTree(){
        String  currentid = getPara("currentid");
        List<Organization> organizations = AndroidService.service.getDeptByParentid(currentid);
        renderJson(organizations);
    }
    
    /**
     * 
     * 描述  
     * @author Mars zhang
     * @created 2015年12月17日 下午4:29:17
     */
    public void  getAsyAreaTree(){
        String  currentid = getPara("currentid");
        List<Area> areas = AndroidService.service.getAreaByParentid(currentid);
        renderJson(areas);
    }
    
    
    
    /**
     * 
     * 描述 获取事件对于的图片 返回路径 
     * 
     * @author Mars zhang
     * @created 2015年11月25日 上午10:48:47
     */
    public void getImagesByIds() {
        String mfileids = getPara("fileids");
        List<Attachment> mAttachments = AndroidService.service.getImagesByIds(mfileids);
        List<Attachment> rtnAttachments = new ArrayList<Attachment>();
        for (Attachment attachment : mAttachments) {

            String mfilepath = attachment.get("filepath");
            if (null != mfilepath && mfilepath.length() > 0) {
                // mfilepath = mfilepath.replace("//", "/");
                mfilepath = mfilepath + "/" + attachment.get("filealias");
                attachment.set("filepath", mfilepath);
                rtnAttachments.add(attachment);
            }
        }
        renderJson(rtnAttachments);
    }

    /**
     * 
     * 描述 获取文件流
     * renderFile(new File(file.getAbsolutePath()), file.getName(), getRequest(),getResponse());
     * //下载renderNull();
     * @author Mars zhang
     * @throws IOException 
     * @throws FileNotFoundException 
     * @throws UnsupportedEncodingException 
     * @created 2015年11月30日 上午11:19:51
     */
    public void getImageFlowById() throws UnsupportedEncodingException, FileNotFoundException, IOException {
        HttpServletResponse mhttpServletResponse = getResponse();
        String mfileid = getPara("fileid");
        Attachment attachment = Attachment.dao.findById(mfileid); 
        if (null == attachment) {// 文件不存在
            renderFileFllow(mhttpServletResponse, PathKit.getWebRootPath()
                    + File.separator +"files/default/abc_&&AOsaha啊.png","文件不存在.png");
            renderNull();
            return;
        }
      //获取文件全路径
        String mfilepath = attachment.get("filepath");
        if (null != mfilepath && mfilepath.length() > 0) {
            mfilepath = mfilepath.replace("\\", File.separator);
            mfilepath = PathKit.getWebRootPath() + File.separator + mfilepath + File.separator
                    + attachment.get("filealias");
        }
        File responseFile = new File(mfilepath); 
        if (!responseFile.exists()) {// 文件不存在
            renderFileFllow(mhttpServletResponse, PathKit.getWebRootPath()+ File.separator 
                    +"files/default/abc_&&AOsaha啊.png","文件不存在.png");
            renderNull();
            return;
        } else {
            renderFileFllow(mhttpServletResponse, mfilepath,attachment.get("filename")+"");
            renderNull();
            return;
        }

    }

    /**
     * 描述  返回文件流
     * 
     * @author Mars zhang
     * @created 2015年11月30日 上午11:51:48
     * @param mhttpServletResponse
     * @param mfilepath
     * @throws UnsupportedEncodingException
     * @throws FileNotFoundException
     * @throws IOException
     */
    protected void renderFileFllow(HttpServletResponse mhttpServletResponse, String mfilepath,String fileName)
            throws UnsupportedEncodingException, FileNotFoundException, IOException {
        mhttpServletResponse.setHeader("Content-Type", "image/jped");
        // 设置响应头，控制浏览器下载该文件
        mhttpServletResponse.setHeader("content-disposition",
                "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        // 读取要下载的文件，保存到文件输入流
        FileInputStream in = new FileInputStream(mfilepath);
        // 创建输出流
        OutputStream out = mhttpServletResponse.getOutputStream();
        // 创建缓冲区
        byte buffer[] = new byte[1024];
        int len = 0;
        // 循环将输入流中的内容读取到缓冲区当中
        while ((len = in.read(buffer)) > 0) {
            // 输出缓冲区的内容到浏览器，实现文件下载
            out.write(buffer, 0, len);
        }
        // 关闭文件输入流
        in.close();
        // 关闭输出流
        out.close(); 
    }
    

}
