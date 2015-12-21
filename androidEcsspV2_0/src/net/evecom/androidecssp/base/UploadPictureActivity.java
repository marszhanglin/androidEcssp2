/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package net.evecom.androidecssp.base;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.evecom.androidecssp.R;
import net.evecom.androidecssp.util.FileUtils;
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
import android.widget.Toast;

/**
 * 
 * ����
 * 
 * @author Mars zhang
 * @created 2015-11-25 ����11:22:17
 */
public class UploadPictureActivity extends BaseActivity {
    /** ��Ա���� */
    private Uri photoUri;
    /** ��Ա���� */
    private ImageView imageView;

    /**
     * 
     * ����
     * 
     * @author Mars zhang
     * @created 2015-11-25 ����2:13:04
     * @param savedInstanceState
     * @see net.evecom.androidecssp.base.BaseActivity#onCreate(android.os.Bundle)
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uploadpictrue);
        initView();
    }

    /**
     * 
     * ����
     * 
     * @author Mars zhang
     * @created 2015-11-25 ����2:13:09
     */
    public void initView() {
        // ͼƬ��ͼ
        imageView = (ImageView) findViewById(R.id.uploadpicture_cameraImage);
        SharedPreferences sp = getApplicationContext().getSharedPreferences("Picture", MODE_PRIVATE);
        // ��������
        Editor editor = sp.edit();
        editor.putString("fileName", "");
        editor.commit();
    }

    /**
     * ��ȡ�ļ�·�����ϴ���������
     * 
     * @param v
     */
    public void uploadpicture_up_btn(View v) {
        SharedPreferences sp = getApplicationContext().getSharedPreferences("Picture", MODE_PRIVATE);
        String path = sp.getString("fileName", "none");
        // Toast.makeText(getApplicationContext(), path, 0).show();
        // Intent intent = new Intent(UploadPictureActivity.this,
        // EventAddActivity.class);
        Intent intent = new Intent();
        intent.putExtra("filePath", path);
        setResult(R.layout.uploadpictrue, intent);
        finish();
    }

    /**
     * �����ť����
     * 
     * @param v
     */
    public void uploadpicture_take_btn(View v) {
        cameraMethod();// ����
    }

    /**
     * ����ѡ��ť����
     * 
     * @param v
     */
    public void uploadpicture_pick_btn(View v) {
        Intent intent2 = new Intent(Intent.ACTION_GET_CONTENT);
        intent2.setType("*/*");
        intent2.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent2, "��ѡ��һ���ļ�"), 2);
    }

    /**
     * fh
     * 
     * @param v
     */
    public void fh(View v) {
        System.out.println(isdisable);
        if (isdisable)
            return;
        Intent intent = new Intent();
        intent.putExtra("filePath", "none");
        setResult(R.layout.uploadpictrue, intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) { // ����ֻ����ذ�ťʱ����
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
     * ���๦��
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
            case 1: // ��������ת����
                if (resultCode == RESULT_OK) {
                    String fPath = getRealPathFromURI(photoUri);
                    System.out.println(fPath);
                    SharedPreferences sp = getApplicationContext().getSharedPreferences("Picture", MODE_PRIVATE);
                    // ��������
                    Editor editor = sp.edit();
                    editor.putString("fileName", fPath);
                    editor.commit();
                    // imageView.setImageURI(photoUri);
                    displayImage(imageView, fPath);
                }
                break;

            case 2: // �ӱ���ѡ������ת����
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    String path = FileUtils.getPath(this, uri);
                    if (!isImageFile(path)) {
                        Toast.makeText(this, "��ѡ��Ĳ���ͼƬ����ѡ��һ��ͼƬ", 1).show();
                        return;
                    } else {
                        int xm = 10;
                        File file = new File(path);
                        long fileLength = file.length();
                        if (null != file && fileLength / (1024 * 1024) > xm) {
                            Toast.makeText(this, "��ѡ���ͼƬ�ļ�����10M,������ѡ��ͼƬ�ļ�", 1).show();
                            file = null;
                            return;
                        }
                        file = null;
                    }
                    SharedPreferences sp = getApplicationContext().getSharedPreferences("Picture", MODE_PRIVATE);
                    // ��������
                    Editor editor = sp.edit();
                    editor.putString("fileName", path);
                    editor.commit();
                    displayImage(imageView, path);
                }

                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 2014-4-1 �ж��ļ�·���Ƿ�Ϊ"jpg","png","gif","jpeg"��׺
     * 
     * @param filePath
     *            �ļ�·��
     * @return Boolean �Ƿ�ΪͼƬ·��
     */
    private Boolean isImageFile(String filePath) {
        String[] imageFormatSet = new String[] { "jpg", "png", "gif", "peg" };
        String endPath = filePath.substring(filePath.length() - 3, filePath.length());
        // System.out.println(endPath);
        for (String item : imageFormatSet) {
            if (endPath.equals(item)) {
                return true;
            }
        }
        return false;
    }

    /** ��Uriת�����ļ�·�� --��ʱ */
    private String uri2filePath(Uri uri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, proj, null, null, null);
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(index);
        return path;
    }

    /** ��Uriת�����ļ�·�� */
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