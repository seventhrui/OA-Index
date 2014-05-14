package com.example.activity;

import java.util.ArrayList;
import java.util.List;

import com.example.oa_index.R;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
/**
 * ֪ʶ�б�
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
		
		List<String> knowtitle=new ArrayList<String>();
		knowtitle.add("֪ʶһ");
		knowtitle.add("֪ʶ��");
		knowtitle.add("֪ʶ��");
		knowtitle.add("֪ʶ��");
		knowtitle.add("֪ʶ��");
		lv_knowledge.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_expandable_list_item_1, knowtitle));
	}
	public void onClick_gotoKnowledge(AdapterView<?> arg0, View arg1, int arg2,long arg3){
		String knowledge = ((TextView) arg1).getText().toString();
		Intent in = new Intent(getApplicationContext(), MessageShowActivity.class);
		Bundle bundle = new Bundle();
		bundle.putCharSequence("knowledgetitle", knowledge);
		in.putExtras(bundle);
		startActivity(in);
	}
}