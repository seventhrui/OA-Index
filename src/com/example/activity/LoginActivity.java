package com.example.activity;

import com.example.beans.LoginConfig;
import com.example.mybase.MyBaseActivity;
import com.example.oa_index.R;

import net.tsz.afinal.annotation.view.ViewInject;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
/**
 * �û���¼
 *
 */
public class LoginActivity extends MyBaseActivity {
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
		//��ֹ���뷨�Զ�����
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		initView();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Log.v("��������ַ2",serverpath);
		initView();
	}
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1002 && resultCode == 1003)
        {
        	serverpath = data.getStringExtra("serverpath");
            Log.v("��������ַ1",serverpath);
        }
    }
	private void initView(){
		ActionBar actionbar=getActionBar();
		spusernameandpasw = getSharedPreferences("config", MODE_PRIVATE);
		//�û���
		username=spusernameandpasw.getString("UserNumber", "");
		//�û�����
		userpasw=spusernameandpasw.getString("UserPasswd", "");
		et_loginusernum.setText(username);
		et_loginpasword.setText(userpasw);
		//��������ַ
		serverpath=spusernameandpasw.getString("ServerPath", "");
		
		if(serverpath.equals("")){
			Log.v("��������ַ3",serverpath);
			alertDialog("��ȷ�Ϸ�������ַ");
		}
		
		LoginConfig.getLoginConfig().setUsername(username);
		LoginConfig.getLoginConfig().setUserid("20121015095350990612c4db3cab4725");
		LoginConfig.getLoginConfig().setServerip(serverpath);
		
		if(!username.equals("")&&!userpasw.equals("")&&!serverpath.equals("")){
			loginCheck();
		}
	}
	/**
	 * ��¼
	 * @param v
	 */
	public void onClick_gotoindex(View v) {
		loginCheck();
	}
	/**
	 * ��¼У��������ת
	 */
	private void loginCheck(){
		boolean loginresult=true;
		username=et_loginusernum.getText().toString().trim();
		userpasw=et_loginpasword.getText().toString().trim();
		//��¼У��
		loginresult=loginCheckFun(username,userpasw,serverpath);
		if(loginresult){
			saveUserData();
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), MyCenterActivity.class);
			startActivity(intent);
			this.finish();
		}
	}
	/**
	 * ��¼У�鷽��
	 * @param name
	 * @param pswd
	 * @return
	 */
	private boolean loginCheckFun(String name,String pswd,String path){
		if(name.equals("")||pswd.equals("")||path.equals("")){
			Toast.makeText(getApplicationContext(), "�û���/����/��������ַ����Ϊ��", Toast.LENGTH_LONG).show();
			return false;
		}
		else{
			return true;
		}
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
	public void alertDialog(String message) {
		new AlertDialog.Builder(this)
				.setTitle("��ʾ")
				.setMessage(message)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						setResult(RESULT_OK);// ȷ����ť�¼�
						Intent intent=new Intent();
						intent.setClass(getApplicationContext(), ConfigActivity.class);
						startActivityForResult(intent, 1002);
					}
				})
				.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// ȡ����ť�¼�
					}
				}).show();
	}
}
