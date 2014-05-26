package com.example.activity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.example.oa_index.R;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
/**
 * 知识列表
 *
 */
public class KnowledgeListActivity extends FinalActivity {
	@ViewInject(id=R.id.lv_knowledge,itemClick="onClick_gotoKnowledge") ListView lv_knowledge;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knowledge_list);
        initView();
	}
	private void initView(){
		ActionBar actionbar=getActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		getOverflowMenu();
		List<String> knowtitle=new ArrayList<String>();
		knowtitle.add("知识一");
		knowtitle.add("知识二");
		knowtitle.add("知识三");
		knowtitle.add("知识四");
		knowtitle.add("知识五");
		lv_knowledge.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_expandable_list_item_1, knowtitle));
	}
	public void onClick_gotoKnowledge(AdapterView<?> arg0, View arg1, int arg2,long arg3){
		String knowledge = ((TextView) arg1).getText().toString();
		Intent in = new Intent(getApplicationContext(), KnowledgeShowActivity.class);
		Bundle bundle = new Bundle();
		bundle.putCharSequence("knowledgetitle", knowledge);
		in.putExtras(bundle);
		startActivity(in);
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
		getMenuInflater().inflate(R.menu.menu_message_inboxlist, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getTitle().toString().trim().equals("刷新")) {
		}
		//未读
		else if (item.getTitle().toString().trim().equals("未读")) {
		}
		//已读
		else if (item.getTitle().toString().trim().equals("已读")) {
		}
		//全部
		else if (item.getTitle().toString().trim().equals("全部")) {
		}
		//返回
		else if (item.getItemId()==android.R.id.home){
			this.finish();
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}