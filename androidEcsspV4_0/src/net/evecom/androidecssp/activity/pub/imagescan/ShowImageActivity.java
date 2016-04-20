/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package net.evecom.androidecssp.activity.pub.imagescan;

import java.util.List;

import net.evecom.androidecssp.R;
import net.evecom.androidecssp.base.BaseActivity;
import net.evecom.androidecssp.bean.FileManageBean;
import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

/**
 * 
 * ���� ShowImageActivity
 * 
 * @author Mars zhang
 * @created 2015-11-19 ����5:18:49
 */
public class ShowImageActivity extends BaseActivity {
    /** MemberVariables */
    private GridView mGridView;
    /** MemberVariables */
    private List<String> list;
    /** MemberVariables */
    private ChildAdapter adapter;
    /** MemberVariables */
    private List<FileManageBean> fileBeans;
    /** MemberVariables */
    public TextView titleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picture_select_child_at);

        mGridView = (GridView) findViewById(R.id.child_grid);
        titleTextView = (TextView) findViewById(R.id.picture_select_child_title);
        Intent intent = getIntent();
        fileBeans = (List<FileManageBean>) getData("fileBeans", intent);
        list = (List<String>) getData("data", intent);

        adapter = new ChildAdapter(this, list, fileBeans, mGridView);
        mGridView.setAdapter(adapter);
        // Long t=System.currentTimeMillis();
        // for(int i=0 ;i<100;i++){
        // FileManageBean bean =new FileManageBean();
        // bean.setFile_URL(i+"");
        // getDb().save(bean);
        // }
        // System.out.println(System.currentTimeMillis()-t);

        // mGridView.setOnItemClickListener(new
        // AdapterView.OnItemClickListener() {
        // @Override
        // public void onItemClick(AdapterView<?> parent, View view, int
        // position, long id) {
        //
        // }
        // });

    }

    /*
     * @Override public void onBackPressed() { Toast.makeText(this, "ѡ�� " +
     * adapter.getSelectItems().size() + " item", Toast.LENGTH_LONG).show();
     * super.onBackPressed(); }
     */

    /**
     * 
     * ���� �ļ�ѡ�����
     * 
     * @author Mars zhang
     * @created 2015-11-20 ����3:21:35
     */
    public boolean moreThan9Image(List<FileManageBean> fileBeans, boolean isani) {
        if (fileBeans.size() >= 9) {// ��9����
            if (isani) {// �����ټ���
                toast("�����ֻ��ѡ��9����Ƭ", 1);
                errorAni(titleTextView);
            }
            return true;
        } else {
            return false;
        }
    }

}
