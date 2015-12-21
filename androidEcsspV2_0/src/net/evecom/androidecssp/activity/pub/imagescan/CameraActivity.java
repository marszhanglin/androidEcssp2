/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package net.evecom.androidecssp.activity.pub.imagescan;

import java.text.SimpleDateFormat;
import java.util.Date;

import net.evecom.androidecssp.R;
import net.evecom.androidecssp.base.BaseActivity;
import net.evecom.androidecssp.bean.FileManageBean;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Video.Media;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * 
 * 2014-7-22下午6:04:23 类UploadPictureActivity
 * 
 * @author Mars zhang
 * 
 */
public class CameraActivity extends BaseActivity {
    /** 成员变量 */
    private Uri photoUri;
    /** 成员变量 */
    private ImageView imageView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_at);

        initView();
    }

    /**
     * 
     * 描述
     * 
     * @author Mars zhang
     * @created 2015-11-25 下午12:03:11
     */
    public void initView() {
        // 图片视图
        imageView = (ImageView) findViewById(R.id.uploadpicture_cameraImage);
        SharedPreferences sp = getApplicationContext().getSharedPreferences("Picture", MODE_PRIVATE);
        // 存入数据
        Editor editor = sp.edit();
        editor.putString("fileName", "");
        editor.commit();
    }

    /**
     * 获取文件路径并上传至服务器
     * 
     * @param v
     */
    public void addpicture(View v) {
        SharedPreferences sp = getApplicationContext().getSharedPreferences("Picture", MODE_PRIVATE);
        final String path = sp.getString("fileName", "none");
        new Thread(new Runnable() {
            @Override
            public void run() {
                FileManageBean fileManageBean = new FileManageBean();
                fileManageBean.setFile_URL(path);
                getDb().save(fileManageBean);
            }
        }).start();
        Intent intent = new Intent();
        setResult(R.layout.uploadpictrue, intent);
        finish();
    }

    /**
     * 相机按钮监听
     * 
     * @param v
     */
    public void uploadpicture_take_btn(View v) {
        cameraMethod();// 拍照
    }

    /**
     * fh
     * 
     * @param v
     */
    public void fh(View v) {
        if (isdisable)
            return;
        Intent intent = new Intent();
        intent.putExtra("filePath", "none");
        setResult(R.layout.uploadpictrue, intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) { // 点击手机返回按钮时操作
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent intent = new Intent();
            intent.putExtra("filePath", "none");
            setResult(R.layout.uploadpictrue, intent);
            finish();
            return true;
        }
        return false;
    }

    /**
     * 照相功能
     */
    private void cameraMethod() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        String filename = timeStampFormat.format(new Date());
        ContentValues values = new ContentValues();
        values.put(Media.TITLE, filename);
        photoUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(intent, 1);
    }

    @SuppressLint("NewApi")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1: // 从拍照跳转回来
                if (resultCode == RESULT_OK) {
                    String fPath = getRealPathFromURI(photoUri);
                    System.out.println(fPath);
                    SharedPreferences sp = getApplicationContext().getSharedPreferences("Picture", MODE_PRIVATE);
                    // 存入数据
                    Editor editor = sp.edit();
                    editor.putString("fileName", fPath);
                    editor.commit();
                    // imageView.setImageURI(photoUri);
                    displayImage(imageView, fPath);
                }
                break;

            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /** 把Uri转化成文件路径 */
    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            ;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

}
