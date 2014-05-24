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
 * �������д
 *
 */
public class RequestLeaveActivity extends FinalActivity {
	@ViewInject(id=R.id.spn_receiver) Spinner spn_receiver;//������
	@ViewInject(id=R.id.spn_daysoff) Spinner spn_daysoff;//����ʱ��
	@ViewInject(id=R.id.tv_leaveser) TextView tv_leaveser;//�����
	@ViewInject(id=R.id.et_organization) EditText et_organization;//��������
	@ViewInject(id=R.id.et_timea) EditText et_timea;//��ʼʱ��
	@ViewInject(id=R.id.et_timeb) EditText et_timeb;//����ʱ��
	@ViewInject(id=R.id.et_leavefor) EditText et_leavefor;//�������
	private List<String> receiverlist = new ArrayList<String>();
	private List<String> daysofflist = new ArrayList<String>();
	private ArrayAdapter<String> receiveradapter;
	private ArrayAdapter<String> daysoffadapter;
	private Calendar c;
	private String date="";
	private String time="";
	private String receiver="";//�����
	private String leaverser="";//�����
	private String organization="";//��������
	private String timebegain="";//��ʼʱ��
	private String timeend="";//����ʱ��
	private String leavefor="";//�������
	private String daysoff="";//��������
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_requestleave);
		//��ֹ���뷨�Զ�����
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		initView();
		initLinster();
	}
	private void initView(){
		ActionBar actionbar=getActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		
		receiverlist.add("�쵼һ");
		receiverlist.add("�쵼��");
		receiverlist.add("�쵼��");
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
				Log.v("�����-ѡ�н�����", receiver);
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
				Log.v("�����-ѡ�е�������", daysoff);
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
	}
	/**
	 * ����ʱ��Ի���
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
                c.get(Calendar.YEAR), // �������
                c.get(Calendar.MONTH), // �����·�
                c.get(Calendar.DAY_OF_MONTH) // ��������
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
	 * ��ȡ���������
	 * @return
	 */
	private String getRequestleaveContent(){
		String content="";
		//receiver ������
		//leaverser �����
		//organization ��������
		organization=et_organization.getText().toString().trim();
		//timebegain ��ʼʱ��
		timebegain=et_timea.getText().toString().trim();
		//timeend ����ʱ��
		timeend=et_timeb.getText().toString().trim();
		//leavefor �������
		leavefor=et_leavefor.getText().toString().trim();
		//daysoff ��������
		
		return content;
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_message_forward, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getTitle().toString().trim().equals("����")) {
			
		}
		else if (item.getItemId()==android.R.id.home){
			this.finish();
		}
		return super.onOptionsItemSelected(item);
	}
}
