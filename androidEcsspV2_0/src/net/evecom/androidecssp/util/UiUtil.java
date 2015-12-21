/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package net.evecom.androidecssp.util;

import java.util.ArrayList;
import java.util.HashMap;

import net.mutil.util.PhoneUtil;
import net.mutil.view.aystree.AsyTreeAdapter;
import net.mutil.view.aystree.AsyTreeData;
import net.mutil.view.aystree.AsyTreeItemOnClick;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * 2014-5-28 上午9:05:27 UiUtil工具类
 * 
 * @author Mars zhang
 * 
 */
public class UiUtil {
    /**
     * 
     * 动态设置ListView的高度 解决scrollView内嵌套listView不显示全部item
     * 一是Adapter中getView方法返回的View的必须由LinearLayout组成
     * ，因为只有LinearLayout才有measure()方法 在为ListView设置了Adapter之后使用 但是他的最大高度就是屏幕的高度
     * 
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        if (listView == null)
            return;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    /**
     * 获取屏幕宽度
     * 
     * @return
     */
    public static int getWindowWidth(WindowManager wm) {
        Display display = wm.getDefaultDisplay();

        return display.getWidth();
    }

    /**
     * 获取屏幕宽度 通过矩阵的方式
     * 
     * @return
     */
    public static int getWindowWidthBymDisplayMetrics(WindowManager wm) {
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(mDisplayMetrics);
        return mDisplayMetrics.widthPixels;
    }

    /**
     * 获取屏幕宽度
     * 
     * @return
     */
    public static int getWindowHeight(WindowManager wm) {
        Display display = wm.getDefaultDisplay();

        return display.getHeight();
    }

    /**
     * 获取屏幕宽度 通过矩阵的方式
     * 
     * @return
     */
    public static int getWindowHeightBymDisplayMetrics(WindowManager wm) {
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(mDisplayMetrics);
        return mDisplayMetrics.heightPixels;
    }

    /***
     * 获取分辨率
     */
    public static String getResolution(WindowManager wm) {
        return getWindowHeight(wm) + "-" + getWindowWidth(wm);
    }

    /**
     * 
     * 描述 异步树<br>
     * 后台json数据格式：[{"id":"350100","haschildren":"true","name":"福州市"},{"id":
     * "350900", "haschildren"
     * :"true","name":"宁德市"},{"id":"350200","haschildren":"true","name"
     * :"厦门市"},{"id":"350300","haschildren":"true","name":"莆田市"},{"id":"350700",
     * "haschildren"
     * :"true","name":"南平市"},{"id":"350600","haschildren":"true","name"
     * :"漳州市"},{"id":"350800","haschildren":"true","name":"龙岩市"},{"id":"350400",
     * "haschildren"
     * :"true","name":"三明市"},{"id":"350500","haschildren":"true","name":"泉州市"}]
     * 
     * @author Mars zhang
     * @created 2015-12-18 下午12:02:05
     * @param rootName
     * @param rootId
     * @param postUrl
     * @param postmap
     * @param itemLayoutRes
     * @param itemImageId
     * @param itemTextId
     * @param drawableid
     * @param nochilddrawableid
     * @param indentionBase
     * @param itemclickCallBack
     * @param treeListView
     * @param context
     */
    public static void initTree(String rootName, String rootId, String postUrl, HashMap<String, String> postmap,
            int itemLayoutRes, int itemImageId, int itemTextId, int drawableid, int nochilddrawableid,
            int indentionBase, AsyTreeAdapter.AsyTreeItemclick itemclickCallBack, ListView treeListView, Context context) {

        String code = ShareUtil.getString(context, "PASSNAME", "code", "");
        // code = code.replace("+", "%2B");
        if (code.length() > 0) {
            postmap.put("sys_code", code);
        }
        postmap.put("sys_imei", PhoneUtil.getInstance().getImei(context));
        postmap.put("sys_loginName", ShareUtil.getString(context, "PASSNAME", "username", ""));

        ArrayList<AsyTreeData> topNodes = new ArrayList<AsyTreeData>();
        ArrayList<AsyTreeData> allNodes = new ArrayList<AsyTreeData>();
        AsyTreeData root = new AsyTreeData(rootName, 0, rootId, "", true, false);
        topNodes.add(root);
        allNodes.add(root);
        AsyTreeAdapter asyTreeAdapter = new AsyTreeAdapter(topNodes, allNodes, context, postUrl, postmap);
        asyTreeAdapter.setLayoutResIds(itemLayoutRes, itemImageId, itemTextId, drawableid, nochilddrawableid);
        asyTreeAdapter.setIndentionBase(indentionBase);
        asyTreeAdapter.setAsyTreeItemclick(itemclickCallBack);
        treeListView.setAdapter(asyTreeAdapter);
        treeListView.setOnItemClickListener(new AsyTreeItemOnClick(asyTreeAdapter));
    }
}
