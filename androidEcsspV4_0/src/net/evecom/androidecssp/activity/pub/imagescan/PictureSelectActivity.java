/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package net.evecom.androidecssp.activity.pub.imagescan;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import net.evecom.androidecssp.R;
import net.evecom.androidecssp.base.BaseActivity;
import net.evecom.androidecssp.bean.FileManageBean;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * 描述 MainActivity
 * 
 * @author Mars zhang
 * @created 2015-11-19 下午4:28:56
 */
public class PictureSelectActivity extends BaseActivity {
    /** MemberVariables */
    private HashMap<String, List<String>> mGruopMap = new HashMap<String, List<String>>();
    /** MemberVariables */
    private List<ImageBean> list = new ArrayList<ImageBean>();
    /** MemberVariables */
    private final static int SCAN_OK = 1;
    /** MemberVariables */
    private ProgressDialog mProgressDialog;
    /** MemberVariables */
    private GroupAdapter adapter;
    /** MemberVariables */
    private GridView mGroupGridView;
    /** 是否在更新gridview */
    private boolean ifupdateImages = false;

    /** 已选文件 */
    private List<FileManageBean> fileBeans = new ArrayList<FileManageBean>();

    /** titleTextView */
    private TextView titleTextView;

    /** MemberVariables */
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SCAN_OK:
                    // 关闭进度条
                    mProgressDialog.dismiss();
                    adapter = new GroupAdapter(PictureSelectActivity.this, list = subGroupOfImage(mGruopMap),
                            mGroupGridView);
                    mGroupGridView.setAdapter(adapter);
                    break;
                default:
                    break;
            }
        }

    };
    /** MemberVariables */
    private Handler updatemHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SCAN_OK:
                    ifupdateImages = false;
                    // 关闭进度条
                    mProgressDialog.dismiss();
                    list = subGroupOfImage(mGruopMap);
                    adapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picture_select_at);

        initView();
        getImages();

        mGroupGridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (moreThan9Image())
                    return;

                /** 该文件夹下有哪些文件 */
                List<String> childList = mGruopMap.get(list.get(position).getFolderName());
                Intent mIntent = new Intent(PictureSelectActivity.this, ShowImageActivity.class);
                BaseActivity.pushObjData("fileBeans", fileBeans, mIntent);
                BaseActivity.pushObjData("data", (ArrayList<String>) childList, mIntent);
                startActivity(mIntent);

            }

        });

    }

    /**
     * 
     * 描述 initView
     * 
     * @author Mars zhang
     * @created 2015-11-20 下午3:08:43
     */
    private void initView() {
        mGroupGridView = (GridView) findViewById(R.id.picture_select_grid);
        titleTextView = (TextView) findViewById(R.id.picture_select_title);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 每次界面显示刷新已选择文件个数
        fileBeans = getDb().findAll(FileManageBean.class);
        titleTextView.setText("图片(" + fileBeans.size() + "/9)");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case R.layout.camera_at: // 拍照返回
                ifupdateImages = true;
                getImages();
                break;

            default:
                break;
        }
    }

    /**
     * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中
     */
    private void getImages() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "暂无外部存储", Toast.LENGTH_SHORT).show();
            return;
        }

        // 显示进度条
        mProgressDialog = ProgressDialog.show(this, null, "正在加载...");

        new Thread(new Runnable() {

            @Override
            public void run() {
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = PictureSelectActivity.this.getContentResolver();

                // 只查询jpeg和png的图片
                Cursor mCursor = mContentResolver.query(mImageUri, null, MediaStore.Images.Media.MIME_TYPE + "=? or "
                        + MediaStore.Images.Media.MIME_TYPE + "=?", new String[] { "image/jpeg", "image/png" },
                        MediaStore.Images.Media.DATE_MODIFIED);
                /** 拍照返回时要清空 然后才可以重新加载gridview */
                mGruopMap.clear();
                while (mCursor.moveToNext()) {
                    // 获取图片的路径
                    String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));

                    // 获取该图片的父路径名
                    String parentName = new File(path).getParentFile().getName();

                    // 根据父路径名将图片放入到mGruopMap中
                    if (!mGruopMap.containsKey(parentName)) {
                        List<String> chileList = new ArrayList<String>();
                        chileList.add(path);
                        mGruopMap.put(parentName, chileList);
                    } else {
                        mGruopMap.get(parentName).add(path);
                    }
                }

                mCursor.close();
                if (ifupdateImages) { // 可以刷新GridView
                    updatemHandler.sendEmptyMessage(SCAN_OK);
                } else { // 可以第一次加载GridView
                    // 通知Handler扫描图片完成
                    mHandler.sendEmptyMessage(SCAN_OK);
                }

            }
        }).start();

    }

    /**
     * 组装分组界面GridView的数据源，因为我们扫描手机的时候将图片信息放在HashMap中 所以需要遍历HashMap将数据组装成List
     * 
     * @param mGruopMap
     * @return
     */
    private List<ImageBean> subGroupOfImage(HashMap<String, List<String>> mGruopMap) {
        if (mGruopMap.size() == 0) {
            return null;
        }
        List<ImageBean> list = new ArrayList<ImageBean>();

        Iterator<Map.Entry<String, List<String>>> it = mGruopMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, List<String>> entry = it.next();
            ImageBean mImageBean = new ImageBean();
            String key = entry.getKey();
            List<String> value = entry.getValue();

            mImageBean.setFolderName(key);
            mImageBean.setImageCounts(value.size());
            mImageBean.setTopImagePath(value.get(0));// 获取该组的第一张图片

            list.add(mImageBean);
        }

        return list;

    }

    /**
     * 
     * 描述 拍照按钮点击
     * 
     * @author Mars zhang
     * @created 2015-11-20 上午10:51:11
     * @param view
     */
    public void camera(View view) {
        if (moreThan9Image())
            return;
        Intent intent = new Intent(instance, CameraActivity.class);
        startActivityForResult(intent, 1);
    }

    /**
     * 
     * 描述 菜单返回键点击
     * 
     * @author Mars zhang
     * @created 2015-11-23 上午8:47:10
     * @see android.app.Activity#onBackPressed()
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(1);
        finish();
    }

    /**
     * 
     * 描述 文件选择过多
     * 
     * @author Mars zhang
     * @created 2015-11-20 下午3:21:35
     */
    private boolean moreThan9Image() {
        if (fileBeans.size() >= 9) {
            toast("你最多只能选择9张照片", 1);
            errorAni(titleTextView);
            return true;
        } else {
            return false;
        }
    }

}
