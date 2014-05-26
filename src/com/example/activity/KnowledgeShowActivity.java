package com.example.activity;

import java.lang.reflect.Field;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.example.oa_index.R;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
/**
 * 知识详情
 *
 */
public class KnowledgeShowActivity extends FinalActivity {
	@ViewInject(id=R.id.tv_knowledgetitle) TextView tv_knowledgetitle;
	@ViewInject(id=R.id.tv_knowledgetype) TextView tv_knowledgetype;
	@ViewInject(id=R.id.tv_knowledgepublictime) TextView tv_knowledgepublictime;
	@ViewInject(id=R.id.tv_knowledgeissuer) TextView tv_knowledgeissuer;
	@ViewInject(id=R.id.wv_knowledgecontent) WebView wv_knowledgecontent;
	@ViewInject(id=R.id.bt_searchfile,click="onClick_DownLoadFile") Button bt_searchfile;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knowledge_show);
        initView();
	}
	private void initView(){
		ActionBar actionbar=getActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		getOverflowMenu();
		String knowledgetitle=getIntent().getStringExtra("knowledgetitle");
		tv_knowledgetitle.setText(knowledgetitle);
	}
	public void onClick_DownLoadFile(View v){
		
	}
	/**
	 * 三点菜单
	 */
	private void getOverflowMenu() {
	     try {
	        ViewConfiguration config = ViewConfiguration.get(this);
	        Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
	        if(menuKeyField != null) {
	            menuKeyField.setAccessible(true);
	            menuKeyField.setBoolean(config, false);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_notice_show, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getTitle().toString().trim().equals("删除")) {
			this.finish();
		}
		//返回
		else if (item.getItemId()==android.R.id.home){
			this.finish();
		}
		return super.onOptionsItemSelected(item);
	}
}
