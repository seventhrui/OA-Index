package com.example.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.oa_index.R;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
/**
 * ÖªÊ¶ÏêÇé
 *
 */
public class KnowledgeShowActivity extends FinalActivity {
	@ViewInject(id=R.id.tv_knowledgetitle) TextView tv_knowledgetitle;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knowledge_show);
        initView();
	}
	private void initView(){
		String knowledgetitle=getIntent().getStringExtra("knowledgetitle");
		tv_knowledgetitle.setText(knowledgetitle);
	}
}
