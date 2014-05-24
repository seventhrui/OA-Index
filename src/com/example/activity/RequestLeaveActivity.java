package com.example.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.beans.LoginConfig;
import com.example.oa_index.R;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
/**
 * 请假条填写
 *
 */
public class RequestLeaveActivity extends FinalActivity {
	@ViewInject(id=R.id.spn_receiver) Spinner spn_receiver;//接收人
	@ViewInject(id=R.id.spn_daysoff) Spinner spn_daysoff;//调休时间
	@ViewInject(id=R.id.tv_leaveser) TextView tv_leaveser;//请假人
	@ViewInject(id=R.id.et_organization) EditText et_organization;//所属部门
	@ViewInject(id=R.id.et_timea) EditText et_timea;//开始时间
	@ViewInject(id=R.id.et_timeb) EditText et_timeb;//结束时间
	@ViewInject(id=R.id.et_leavefor) EditText et_leavefor;//请假事由
	private List<String> receiverlist = new ArrayList<String>();
	private List<String> daysofflist = new ArrayList<String>();
	private ArrayAdapter<String> receiveradapter;
	private ArrayAdapter<String> daysoffadapter;
	private Calendar c;
	private String date="";
	private String time="";
	private String receiver="";//请假人
	private String leaverser="";//请假人
	private String organization="";//所属部门
	private String timebegain="";//开始时间
	private String timeend="";//结束时间
	private String leavefor="";//请假事由
	private String daysoff="";//调休天数
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_requestleave);
		//禁止输入法自动弹出
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		initView();
		initLinster();
	}
	private void initView(){
		ActionBar actionbar=getActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		
		receiverlist.add("领导一");
		receiverlist.add("领导二");
		receiverlist.add("领导三");
		receiveradapter = new ArrayAdapter<String>(this,R.layout.item_spinner_receiver, receiverlist);
		spn_receiver.setAdapter(receiveradapter);
		
		leaverser=LoginConfig.getLoginConfig().getMyname();
		tv_leaveser.setText(leaverser);
		
		for(int i=0;i<=5;i++){
			daysofflist.add(i+"");
		}
		daysoffadapter = new ArrayAdapter<String>(this,R.layout.item_spinner_receiver, daysofflist);
		spn_daysoff.setAdapter(daysoffadapter);
		
	}
	private void initLinster(){
		spn_receiver.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				String s=parent.getItemAtPosition(position).toString();
				receiver=s;
				Log.v("请假条-选中接收人", receiver);
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		et_timea.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {  
		    @Override  
		    public void onFocusChange(View v, boolean hasFocus) {  
		        if(hasFocus) {
		        	showDialog(2);
		        	showDialog(1);
		        	et_timea.setText(date+time);
				} else {
					et_timea.setText(date+time);
				}
		    }
		});
		et_timeb.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {  
		    @Override  
		    public void onFocusChange(View v, boolean hasFocus) {  
		        if(hasFocus) {
		        	showDialog(2);
		        	showDialog(1);
		        	et_timeb.setText(date+time);
				} else {
					et_timeb.setText(date+time);
				}
		    }
		});
		spn_daysoff.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				String s=parent.getItemAtPosition(position).toString();
				daysoff=s;
				Log.v("请假条-选中调休天数", daysoff);
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
	}
	/**
	 * 日期时间对话框
	 */
	@Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        switch (id) {
        case 1:
            c = Calendar.getInstance();
            dialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker dp, int year,int month, int dayOfMonth) {
                        date=year + "-" + (month+1) + "-" + dayOfMonth + " ";
                    }
                }, 
                c.get(Calendar.YEAR), // 传入年份
                c.get(Calendar.MONTH), // 传入月份
                c.get(Calendar.DAY_OF_MONTH) // 传入天数
            );
            break;
        case 2:
            c=Calendar.getInstance();
            dialog=new TimePickerDialog(
                this, 
                new TimePickerDialog.OnTimeSetListener(){
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        time = hourOfDay+":"+minute;
                    }
                },
                c.get(Calendar.HOUR_OF_DAY),
                c.get(Calendar.MINUTE),
                false
            );
            break;
        }
        return dialog;
    }
	/**
	 * 获取请假条内容
	 * @return
	 */
	private String getRequestleaveContent(){
		String content="";
		//receiver 接收人
		//leaverser 请假人
		//organization 所属部门
		organization=et_organization.getText().toString().trim();
		//timebegain 开始时间
		timebegain=et_timea.getText().toString().trim();
		//timeend 结束时间
		timeend=et_timeb.getText().toString().trim();
		//leavefor 请假事由
		leavefor=et_leavefor.getText().toString().trim();
		//daysoff 调休天数
		
		return content;
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_message_forward, menu);
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
