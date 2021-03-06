/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package net.evecom.androidecssp.activity.pub.imagescan;

import java.util.HashMap;
import java.util.List;

import net.evecom.androidecssp.R;
import net.evecom.androidecssp.activity.pub.imagescan.MyImageView.OnMeasureListener;
import net.evecom.androidecssp.activity.pub.imagescan.NativeImageLoader.NativeImageCallBack;
import net.evecom.androidecssp.bean.FileManageBean;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.ImageView;

import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * 
 * 描述
 * 
 * @author Mars zhang
 * @created 2015-11-25 上午11:26:34
 */
public class ChildAdapter extends BaseAdapter {
    /** MemberVariables */
    private Point mPoint = new Point(0, 0);// 用来封装ImageView的宽和高的对象
    /**
     * 用来存储图片的选中情况
     */
    private HashMap<Integer, Boolean> mSelectMap = new HashMap<Integer, Boolean>();
    /** MemberVariables */
    private GridView mGridView;
    /** MemberVariables */
    private List<String> list;
    /** MemberVariables */
    protected LayoutInflater mInflater;
    /** MemberVariables */
    private List<FileManageBean> fileBeans;
    /** 操作数据库 */
    private ShowImageActivity context;
    /** checkbox不触发setOnCheckedChangeListener */
    private String prePath = "";
    /** MemberVariables */
    private boolean istimeout = false;

    // private boolean issetOnCheckedChangeListenerDisable =false;
    /**
     * 
     * 描述
     * 
     * @author Mars zhang
     * @created 2015-11-25 下午12:04:26
     * @param context
     * @param list
     * @param fileBeans
     * @param mGridView
     */
    public ChildAdapter(ShowImageActivity context, List<String> list, List<FileManageBean> fileBeans,
            GridView mGridView) {
        this.list = list;
        this.mGridView = mGridView;
        this.fileBeans = fileBeans;
        this.context = context;
        mInflater = LayoutInflater.from(context);
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < fileBeans.size(); j++) {
                if (null != list.get(i) && list.get(i).equals(fileBeans.get(j).getFileURL())) {
                    mSelectMap.put(i, true);
                }
            }
        }
        ;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        final String path = list.get(position);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.picture_select_grid_child_item, null);
            viewHolder = new ViewHolder();
            viewHolder.mImageView = (MyImageView) convertView.findViewById(R.id.child_image);
            viewHolder.mCheckBox = (CheckBox) convertView.findViewById(R.id.child_checkbox);

            // 用来监听ImageView的宽和高
            viewHolder.mImageView.setOnMeasureListener(new OnMeasureListener() {

                @Override
                public void onMeasureSize(int width, int height) {
                    mPoint.set(width, height);
                }
            });

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.mImageView.setImageResource(R.drawable.friends_sends_pictures_no);
        }
        viewHolder.mImageView.setTag(path);
        viewHolder.mCheckBox.setChecked(mSelectMap.containsKey(position) ?
                mSelectMap.get(position) : false);
        // 单选点击
        viewHolder.mCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                if (context.moreThan9Image(fileBeans, isChecked)) {// >=9
                    if (isChecked) {// 选择点击 改变样式为false并返回
                        // issetOnCheckedChangeListenerDisable=true;
                        viewHolder.mCheckBox.setChecked(false);
                        return;
                    } else {// 取消点击
                    }
                }

                new Thread(new Runnable() {
                    public void run() {
                        /*
                         * new Thread( new Runnable() { public void run() { try
                         * { Thread.sleep(1000); istimeout=true; } catch
                         * (InterruptedException e) { } } }).start(); //连续重复判断
                         * if
                         * (prePath.equals(path)&&!istimeout)return;prePath=path
                         * +""+isChecked; istimeout=false;
                         */
                        if (isChecked) {// 选择点击
                            FileManageBean fileitembean = new FileManageBean();
                            fileitembean.setFileURL(path);
                            context.getDb().save(fileitembean);
                            fileBeans.add(fileitembean);
                        } else {// 取消点击
                            context.getDb().deleteByWhere(FileManageBean.class, "File_URL=\"" + path + "\"");
                            if (fileBeans.size() > 0)
                                fileBeans.remove(0);
                        }
                        titlehandler.sendEmptyMessage(0);
                    }
                }).start();

                // 如果是未选中的CheckBox,则添加动画
                if (!mSelectMap.containsKey(position) || !mSelectMap.get(position)) {
                    addAnimation(viewHolder.mCheckBox);
                }
                mSelectMap.put(position, isChecked);
            }
        });

        // 利用NativeImageLoader类加载本地图片
        Bitmap bitmap = NativeImageLoader.getInstance().loadNativeImage(path, mPoint, new NativeImageCallBack() {

            @Override
            public void onImageLoader(Bitmap bitmap, String path) {
                ImageView mImageView = (ImageView) mGridView.findViewWithTag(path);
                if (bitmap != null && mImageView != null) {
                    mImageView.setImageBitmap(bitmap);
                }
            }
        });

        if (bitmap != null) {
            viewHolder.mImageView.setImageBitmap(bitmap);
        } else {
            viewHolder.mImageView.setImageResource(R.drawable.friends_sends_pictures_no);
        }

        return convertView;
    }

    /**
     * 给CheckBox加点击动画，利用开源库nineoldandroids设置动画
     * 
     * @param view
     */
    private void addAnimation(View view) {
        float[] vaules = new float[] { 0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 1.0f, 1.1f, 1.2f, 1.3f, 1.25f, 1.2f, 1.15f, 1.1f,
                1.0f };
        AnimatorSet set = new AnimatorSet();
        set.playTogether(ObjectAnimator.ofFloat(view, "scaleX", vaules), ObjectAnimator.ofFloat(view,
                "scaleY", vaules));
        set.setDuration(150);
        set.start();
    }

    /**
     * 获取选中的Item的position
     * 
     * @return
     */
    /*
     * public List<Integer> getSelectItems(){ List<Integer> list = new
     * ArrayList<Integer>(); for(Iterator<Map.Entry<Integer, Boolean>> it =
     * mSelectMap.entrySet().iterator(); it.hasNext();){ Map.Entry<Integer,
     * Boolean> entry = it.next(); if(entry.getValue()){
     * list.add(entry.getKey()); } }
     * 
     * return list; }
     */
    /**
     * 
     * 描述
     * 
     * @author Mars zhang
     * @created 2015-11-25 下午12:04:39
     */
    public static class ViewHolder {
        /** MemberVariables */
        public MyImageView mImageView;
        /** MemberVariables */
        public CheckBox mCheckBox;
    }

    /** MemberVariables */
    Handler titlehandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            context.titleTextView.setText("图片(" + fileBeans.size() + "/9)");
        };
    };

}
