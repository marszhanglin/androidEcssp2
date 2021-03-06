/*
 * Copyright (c) 2005, 2015, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package net.evecom.androidecssp.activity.inform;

import net.evecom.androidecssp.R;
import net.evecom.androidecssp.base.BaseActivity;
import net.evecom.androidecssp.base.BaseModel;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

/**
 * 通知信息
 * 
 * @author Stark Zhou
 * @created 2015-11-12 下午5:00:43
 */
public class InformInfoActivity extends BaseActivity {
    /** informInfo */
    private BaseModel informInfo = null;
    /** content */
    private TextView content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inform_info_at);
        Intent intent = getIntent();
        informInfo = (BaseModel) getData("inform", intent);
        init();
    }

    /**
     * 
     * 初始化联系人信息
     * 
     * @author Stark Zhou
     * @created 2015-11-13 下午3:22:07
     */
    private void init() {
        TextView event = (TextView) findViewById(R.id.inform_event);
        event.setText(ifnull(informInfo.get("eventname") + "", ""));
        TextView level = (TextView) findViewById(R.id.inform_level);
        level.setText(ifnull(informInfo.get("levelname") + "", ""));
        TextView title = (TextView) findViewById(R.id.inform_title);
        title.setText(ifnull(informInfo.get("title") + "", ""));
        // TextView stateText = (TextView) findViewById(R.id.inform_state);
        // stateText.setText(ifnull(informInfo.get("state") + "", ""));
        TextView range = (TextView) findViewById(R.id.inform_range);
        range.setText(ifnull(informInfo.get("influencerange") + "", ""));
        TextView timeRange = (TextView) findViewById(R.id.inform_timerange);
        timeRange.setText(ifnull(informInfo.get("starttime"), "-") + "-" + ifnull(informInfo.get("endtime"), ""));
        TextView org = (TextView) findViewById(R.id.inform_org);
        org.setText(ifnull(informInfo.get("orgname") + "", ""));
        TextView author = (TextView) findViewById(R.id.inform_author);
        author.setText(ifnull(informInfo.get("auth") + "", ""));
        TextView contact = (TextView) findViewById(R.id.inform_contact);
        contact.setText(ifnull(informInfo.get("contact") + "", ""));

        content = (TextView) findViewById(R.id.inform_content);
        //设置滚动条
        content.setMovementMethod(ScrollingMovementMethod.getInstance()); 
        String html=ifnull(informInfo.get("content").toString(),"");
        content.setText(Html.fromHtml(html));
    }

}
