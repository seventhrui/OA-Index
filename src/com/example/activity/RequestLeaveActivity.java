package com.example.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.oa_index.R;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
/**
 * 请假条填写
 *
 */
public class RequestLeaveActivity extends FinalActivity {
	@ViewInject(id=R.id.spn_receiver) Spinner spn_receiver;
	@ViewInject(id=R.id.spn_daysoff) Spinner spn_daysoff;
	private List<String> receiverlist = new ArrayList<String>();
	private List<String> daysofflist = new ArrayList<String>();
	private ArrayAdapter<String> receiveradapter;
	private ArrayAdapter<String> daysoffadapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_requestleave);
		//禁止输入法自动弹出
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		initView();
	}
	private void initView(){
		ActionBar actionbar=getActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		
		receiverlist.add("领导一");
		receiverlist.add("领导二");
		receiverlist.add("领导三");
		receiveradapter = new ArrayAdapter<String>(this,R.layout.item_spinner_receiver, receiverlist);
		spn_receiver.setAdapter(receiveradapter);
		spn_receiver.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				String receiver=parent.getItemAtPosition(position).toString();
				Toast.makeText(RequestLeaveActivity.this, "你点击的是:"+receiver, 2000).show();
				Log.v("请假条-选中接收人", receiver);
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		for(int i=0;i<=5;i++){
			daysofflist.add(i+"");
		}
		daysoffadapter = new ArrayAdapter<String>(this,R.layout.item_spinner_receiver, daysofflist);
		spn_daysoff.setAdapter(daysoffadapter);
		spn_daysoff.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				String receiver=parent.getItemAtPosition(position).toString();
				Toast.makeText(RequestLeaveActivity.this, "你点击的是:"+receiver, 2000).show();
				Log.v("请假条-选中调休天数", receiver);
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_forwardmessage, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getTitle().toString().trim().equals("发送")) {
			
		}
		else if (item.getItemId()==android.R.id.home){
			this.finish();
		}
		return super.onOptionsItemSelected(item);
	}
}
