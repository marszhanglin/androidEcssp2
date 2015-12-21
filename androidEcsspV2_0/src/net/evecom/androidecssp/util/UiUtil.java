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
 * 2014-5-28 ����9:05:27 UiUtil������
 * 
 * @author Mars zhang
 * 
 */
public class UiUtil {
    /**
     * 
     * ��̬����ListView�ĸ߶� ���scrollView��Ƕ��listView����ʾȫ��item
     * һ��Adapter��getView�������ص�View�ı�����LinearLayout���
     * ����Ϊֻ��LinearLayout����measure()���� ��ΪListView������Adapter֮��ʹ�� �����������߶Ⱦ�����Ļ�ĸ߶�
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
     * ��ȡ��Ļ���
     * 
     * @return
     */
    public static int getWindowWidth(WindowManager wm) {
        Display display = wm.getDefaultDisplay();

        return display.getWidth();
    }

    /**
     * ��ȡ��Ļ��� ͨ������ķ�ʽ
     * 
     * @return
     */
    public static int getWindowWidthBymDisplayMetrics(WindowManager wm) {
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(mDisplayMetrics);
        return mDisplayMetrics.widthPixels;
    }

    /**
     * ��ȡ��Ļ���
     * 
     * @return
     */
    public static int getWindowHeight(WindowManager wm) {
        Display display = wm.getDefaultDisplay();

        return display.getHeight();
    }

    /**
     * ��ȡ��Ļ��� ͨ������ķ�ʽ
     * 
     * @return
     */
    public static int getWindowHeightBymDisplayMetrics(WindowManager wm) {
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(mDisplayMetrics);
        return mDisplayMetrics.heightPixels;
    }

    /***
     * ��ȡ�ֱ���
     */
    public static String getResolution(WindowManager wm) {
        return getWindowHeight(wm) + "-" + getWindowWidth(wm);
    }

    /**
     * 
     * ���� �첽��<br>
     * ��̨json���ݸ�ʽ��[{"id":"350100","haschildren":"true","name":"������"},{"id":
     * "350900", "haschildren"
     * :"true","name":"������"},{"id":"350200","haschildren":"true","name"
     * :"������"},{"id":"350300","haschildren":"true","name":"������"},{"id":"350700",
     * "haschildren"
     * :"true","name":"��ƽ��"},{"id":"350600","haschildren":"true","name"
     * :"������"},{"id":"350800","haschildren":"true","name":"������"},{"id":"350400",
     * "haschildren"
     * :"true","name":"������"},{"id":"350500","haschildren":"true","name":"Ȫ����"}]
     * 
     * @author Mars zhang
     * @created 2015-12-18 ����12:02:05
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
