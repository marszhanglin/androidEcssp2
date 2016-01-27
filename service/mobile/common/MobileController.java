/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package net.evecom.ecssp.mobile.common;

import java.io.File;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.evecom.ecssp.esb.EsbController;
import net.evecom.org.User;
import net.evecom.pub.annotation.Excluded;
import net.evecom.pub.util.AES;
import net.evecom.pub.util.ToolWeb;
import net.evecom.system.Attachment;

import com.jfinal.core.ActionInvocation;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.upload.UploadFile;

/**
 * 移动应用支撑服务控制对象基类
 * 
 * @author Yancey Qiu
 * @created 2014年11月13日 下午5:12:36
 * @version 2.0
 */
public abstract class MobileController extends EsbController {

    /**
     * 参数名_imei号
     * 
     * @author Yancey Qiu
     */
    public static final String PRM_SYS_IMEI = "sys_imei";

    /**
     * 参数名_加密的授权码
     * 
     * @author Yancey Qiu
     */
    public static final String PRM_SYS_CODE = "sys_code";

    /**
     * 参数名_登录用户的账号
     * 
     * @author Yancey Qiu
     */
    public static final String PRM_SYS_LOGINNAME = "sys_loginName";

    /**
     * 参数名_返回结果
     * 
     * @author Yancey Qiu
     */
    public static final String PRM_RESULT = "result";

    /**
     * 服务配置
     * 
     * @author Yancey Qiu
     */
    private MobileServiceConfig serviceConfig;

    public MobileServiceConfig getServiceConfig() {
        return serviceConfig;
    }

    public void setServiceConfig(MobileServiceConfig serviceConfig) {
        this.serviceConfig = serviceConfig;
    }

    /**
     * 当前用户
     * 
     * @author Yancey Qiu
     */
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    /**
     * 当前用户授权
     * 
     * @author Yancey Qiu
     */
    private MobileUserGrant userGrant;

    public MobileUserGrant getUserGrant() {
        return userGrant;
    }

    public void setUserGrant(MobileUserGrant userGrant) {
        this.userGrant = userGrant;
    }

    /**
     * 接口调用合法性验证
     * 
     * @author Yancey Qiu
     * @created 2014年11月18日 下午8:50:52
     * @param ai
     *            action切面
     * @see net.evecom.ecssp.esb.EsbController#onBeforeInvoke(com.jfinal.core.ActionInvocation)
     */
    @Override
    public void onBeforeInvoke(final ActionInvocation ai) {
        // 接受标准http参数，因此取消总线的原始操作
        // super.onBeforeInvoke(ai);

        // 多次查询数据库启动事务提高性能，不然每次查询数据库连接都关闭，注：当前不存在事务要手动启动
        Db.tx(new IAtom() {

            @Override
            public boolean run() throws SQLException {

                // 验证接口
                serviceConfig = MobileServiceConfigService.service.getByClsAndMthd(ai.getController().getClass()
                        .getName(), ai.getMethodName());

                if (serviceConfig != null && serviceConfig.getStr("disabled").equals("1")) {
                    throw new RuntimeException("接口[" + serviceConfig.getStr("name") + "]已被禁用，请升级软件！");
                }
//                System.out.println(getRequest().getRequestURI() + "----" + getPara(PRM_SYS_LOGINNAME));
                // 如果没有设置排除注释，则验证授权码
                if (ai.getMethod().getAnnotation(Excluded.class) == null) {

                    // 获取当前用户
                    user = MobileUserGrantService.service.getUserByLoginName(getPara(PRM_SYS_LOGINNAME));

                    if (user == null) {
                        throw new RuntimeException("账号[" + getPara(PRM_SYS_LOGINNAME) + "]的用户不存在！");
                    }

                    if ("0".equals(user.getStr("status"))) {
                        throw new RuntimeException("用户[" + user.getStr("name") + "]被禁用，请联系管理员开通！");
                    }
                    String code = getPara(PRM_SYS_CODE);
                    // 获取用户授权
                    userGrant = MobileUserGrantService.service.getByUser(user.getStr("id"), getPara(PRM_SYS_IMEI));

                    if (userGrant == null) {
                        throw new RuntimeException("用户[" + user.getStr("name") + "]未授权，请执行登录操作！");
                    }

                    // 获取解密的授权码(这里前5位是真正的授权码)
                    String decryptedCode = AES.decryptECB(code, userGrant.getStr("pkey"));

                    // 如果解密的授权码为空或不足5位
                    if (decryptedCode == null || decryptedCode.equals("") || decryptedCode.length() < 5) {
                        throw new RuntimeException("用户[" + user.getStr("name") + "]未登录，非法访问！");
                    }

                    // 验证授权码是否合法
                    BigDecimal grantedCode = null;

                    try {
                        grantedCode = new BigDecimal(decryptedCode.substring(0, 5));

                    } catch (Exception e) {
                        throw new RuntimeException("用户[" + user.getStr("name") + "]未登录，非法访问！");
                    }

                    if (grantedCode.compareTo(userGrant.getBigDecimal("grantedCode")) != 0) {
                        throw new RuntimeException("用户[" + user.getStr("name") + "]未登录，非法访问！");
                    }

                    // 如果有设置到期时间且当前时间等于或超过到期时间，则提示异常
                    if (userGrant.getDate("expireTime") != null
                            && userGrant.getDate("expireTime").compareTo(new Date()) <= 0) {
                        throw new RuntimeException("您身份认证已经到期，请重新登录！");
                    }
                }

                return true;
            }
        });
    }

    /**
     * 访问成功处理
     * 
     * @author Yancey Qiu
     * @created 2014年11月18日 下午2:18:19
     * @param ai
     *            action切面
     * @see net.evecom.ecssp.esb.EsbController#onSuccess(com.jfinal.core.ActionInvocation)
     */
    @Override
    public void onSuccess(ActionInvocation ai) {
        super.onSuccess(ai);

        // 保存成功日志，注：当前不存在事务
        // saveMobileLog(ai, null);
    }

    /**
     * 访问失败处理
     * 
     * @author Yancey Qiu
     * @created 2014年11月18日 下午2:19:20
     * @param ai
     *            action切面
     * @param e
     *            异常
     * @see net.evecom.ecssp.esb.EsbController#onError(com.jfinal.core.ActionInvocation,
     *      java.lang.Throwable)
     */
    @Override
    public void onError(ActionInvocation ai, Throwable e) {
        super.onError(ai, e);

        // 保存失败日志，注：当前不存在事务
        saveMobileLog(ai, e);
    }

    /**
     * 保存移动应用-访问日志
     * 
     * @author Yancey Qiu
     * @created 2014年11月18日 上午8:44:24
     * @param ai
     *            action切面
     * @param e
     *            异常
     */
    private void saveMobileLog(ActionInvocation ai, Throwable e) {
        MobileLog log = new MobileLog();
        log.put("svcId", serviceConfig != null ? serviceConfig.getStr("id") : null);
        log.put("svcCls", ai.getController().getClass().getName());
        log.put("svcMthd", ai.getMethodName());
        log.put("svcCode", serviceConfig != null ? serviceConfig.getStr("code") : null);
        log.put("svcName", serviceConfig != null ? serviceConfig.getStr("name") : null);
        log.put("ip", ToolWeb.getIpAddr(ai.getController().getRequest()));
        log.put("grantedId", userGrant != null ? userGrant.getStr("id") : null);
        log.put("userId", user != null ? user.getStr("id") : null);
        log.put("loginName", getPara(PRM_SYS_LOGINNAME));
        log.put("userName", user != null ? user.getStr("name") : null);
        log.put("imei", getPara(PRM_SYS_IMEI));
        log.put("grantedCode", userGrant != null ? userGrant.getBigDecimal("grantedCode") : null);
        log.put("errMsg", e != null ? e.getMessage() : null);

        log.save();
    }

    /**
     * 设置输出报文
     * 
     * @author Yancey Qiu
     * @created 2015年11月9日 上午11:57:54
     * @param obj
     *            返回结果
     */
    public void renderPrmOut(Object obj) {
        Map<String, Object> prmOut = new HashMap<String, Object>();
        prmOut.put(PRM_RESULT, obj);

        renderPrmOut(prmOut);
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
            attachment.set("TYPE","." + type);
            attachment.set("FILEPATH", dirname);
            attachment.set("FILESIZE", file.length());
            attachment.save();
            fileids.append(attachment.get("id") + ",");
        }
        return fileids;
    }

    /**
     * 
     * 描述  D:\otherPath 
     * 
     * @author Mars zhang
     * @created 2015年11月26日 上午11:32:20
     * @return
     */
    protected String getAbsoluteUploadfath() {
        // 文件保存路径
        StringBuilder sb = new StringBuilder();
        //.append(PathKit.getWebRootPath()) E://
        sb.append(PathKit.getWebRootPath()).append(File.separator).append("files").append(File.separator)
                .append("upload").append(File.separator);
        return sb.toString();
    }
    /**
     * 
     * 描述  //files//upload//
     * 
     * @author Mars zhang
     * @created 2015年11月26日 上午11:32:20
     * @return
     */
    protected String getRelativeUploadfath() {
        // 文件保存路径
        StringBuilder sb = new StringBuilder();
        //.append(PathKit.getWebRootPath()) E://
        sb.append(File.separator).append("files").append(File.separator)
                .append("upload").append(File.separator);
        return sb.toString();
    }
}
