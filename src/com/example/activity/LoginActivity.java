package com.example.activity;

import com.example.oa_index.R;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
/**
 * �û���¼
 *
 */
public class LoginActivity extends FinalActivity {
	@ViewInject(id = R.id.et_loginusernum) EditText et_loginusernum;//�û�������
	@ViewInject(id = R.id.et_loginpassword) EditText et_loginpasword;// ��������
	@ViewInject(id = R.id.cb_rememberpwd) CheckBox cb_rememberpwd;//��ס����
	@ViewInject(id = R.id.tv_loginerror) TextView tv_loginerror;//��½������ʾ
	@ViewInject(id = R.id.bt_gotoindex, click = "onClick_gotoindex") Button bt_gotoindex;//��¼��ť
	@ViewInject(id = R.id.bt_Undo, click = "onClick_Undo") Button bt_Undo;//ȡ����ť
	private String username;//�û���
	private String userpasw;//����
	private String serverpath;//��������ַ
	private SharedPreferences spusernameandpasw;// �洢�û���������
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initView();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Log.v("��������ַ1",serverpath);
	}
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1002 && resultCode == 1003)
        {
        	serverpath = data.getStringExtra("serverpath");
            Log.v("��������ַ2",serverpath);
        }
    }
	private void initView(){
		ActionBar actionbar=getActionBar();
		spusernameandpasw = getSharedPreferences("config", MODE_PRIVATE);
		et_loginusernum.setText(spusernameandpasw.getString("UserNumber", ""));
		et_loginpasword.setText(spusernameandpasw.getString("UserPasswd", ""));
		serverpath=spusernameandpasw.getString("ServerPath", "");
	}
	/**
	 * ��¼
	 * @param v
	 */
	public void onClick_gotoindex(View v) {
		boolean loginresult=true;
		username=et_loginusernum.getText().toString().trim();
		userpasw=et_loginpasword.getText().toString().trim();
		loginresult=loginCheck(username,userpasw);
		if(loginresult){
			//saveUserData();
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), MyCenterActivity.class);
			startActivity(intent);
		}
	}
	/**
	 * ��½У��
	 * @param name
	 * @param pswd
	 * @return
	 */
	private boolean loginCheck(String name,String pswd){
		return true;
	}
	/**
	 * �����û�������
	 */
	private void saveUserData(){
		if(cb_rememberpwd.isChecked()){
			Editor editor=spusernameandpasw.edit();
			editor.putString("UserNumber", username);
			editor.putString("UserPasswd", userpasw);
			editor.commit();
			Toast.makeText(getApplicationContext(), "�����ѱ���", 0).show();
		}
	}
	/**
	 * ȡ��
	 * @param v
	 */
	public void onClick_Undo(View v){
		this.finish();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_login, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getTitle().toString().trim().equals("����")) {
			Intent intent=new Intent();
			intent.setClass(getApplicationContext(), ConfigActivity.class);
			startActivityForResult(intent, 1002);
		}
		return super.onOptionsItemSelected(item);
	}
}
