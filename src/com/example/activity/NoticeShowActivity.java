package com.example.activity;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

import com.example.oa_index.R;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
/**
 * 通知通告详情
 *
 */
public class NoticeShowActivity extends FinalActivity {
	@ViewInject(id=R.id.tv_noticetitle) TextView tv_noticetitle;
    @ViewInject(id=R.id.tv_noticesender) TextView tv_noticesender;
    @ViewInject(id=R.id.tv_noticetime) TextView tv_noticetime;
    @ViewInject(id=R.id.wv_noticecontent) WebView wv_noticecontent;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_show);
    }
}
