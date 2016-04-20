/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package net.evecom.androidecssp.activity.pub;

import java.util.ArrayList;
import java.util.List;

import net.evecom.androidecssp.R;
import net.evecom.androidecssp.base.BaseActivity;
import net.evecom.androidecssp.viewpager.RotateDownTransformer;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

/**
 * 
 * ���� EmergencyNotification
 * 
 * @author Mars zhang
 * @created 2015-11-12 ����10:13:00
 */
public class EmergencyNotification extends BaseActivity {
    /** MemberVariables */
    private ViewPager viewpager;
    /** MemberVariables */
    private int[] imagesId = { R.drawable.emergency_info_3,
            R.drawable.emergency_info_33, R.drawable.emergency_info_34 };
    /** MemberVariables */
    private List<ImageView> imageViews = new ArrayList<ImageView>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emergency_info_activity);

        // 1��
        viewpager = (ViewPager) findViewById(R.id.my_viewpager_id);
        // 4�����Զ���DepthPageTransformer
        // viewpager.setPageTransformer(true, new DepthPageTransformer());
        viewpager.setPageTransformer(true, new RotateDownTransformer());
        // 2������������
        viewpager.setAdapter(new PagerAdapter() {
            @Override
            // ��ʼ��item �����ﲼ��ÿ��page�Ĳ������������ӿؼ�
            public Object instantiateItem(ViewGroup container, int position) {
                // ������ÿ��ҳ��������һ��ͼƬ Ҳ���Բ���ͼƬswitch�ж���ÿ��pageҪ��ʲô
                ImageView imageView = new ImageView(EmergencyNotification.this);
                imageView.setImageResource(imagesId[position]);
                // ����ͼƬ����
                imageView.setScaleType(ScaleType.CENTER_CROP);

                // ��imageView�ӵ�container�� �������һ��page��ֹһ���ؼ���ô��
                container.addView(imageView);
                // ��imageView�ӵ�list��
                imageViews.add(imageView);
                // ����imageView
                return imageView;
            }

            @Override
            // destroyItem
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(imageViews.get(position));
            }

            @Override
            // �ж� ÿ��adapter�������
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            // �����ж���ҳ
            public int getCount() {
                return imagesId.length;
            }
        });

    }

    /**
     * 
     * ���� call
     * 
     * @author Mars zhang
     * @created 2015-11-17 ����4:46:30
     * @param view
     */
    public void call(View view) { 
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:18950478288"));
        startActivity(intent);
        finish();
    }
}