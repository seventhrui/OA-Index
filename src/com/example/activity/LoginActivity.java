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
 * 用户登录
 *
 */
public class LoginActivity extends MyBaseActivity {
	@ViewInject(id = R.id.et_loginusernum) EditText et_loginusernum;//用户名输入
	@ViewInject(id = R.id.et_loginpassword) EditText et_loginpasword;// 密码输入
	@ViewInject(id = R.id.cb_rememberpwd) CheckBox cb_rememberpwd;//记住密码
	@ViewInject(id = R.id.tv_loginerror) TextView tv_loginerror;//登陆错误提示
	@ViewInject(id = R.id.bt_gotoindex, click = "onClick_gotoindex") Button bt_gotoindex;//登录按钮
	@ViewInject(id = R.id.bt_Undo, click = "onClick_Undo") Button bt_Undo;//取消按钮
	private String username;//用户名
	private String userpasw;//密码
	private String serverpath;//服务器地址
	private SharedPreferences spusernameandpasw;// 存储用户名和密码
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		//禁止输入法自动弹出
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		initView();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Log.v("服务器地址2",serverpath);
		initView();
	}
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1002 && resultCode == 1003)
        {
        	serverpath = data.getStringExtra("serverpath");
            Log.v("服务器地址1",serverpath);
        }
    }
	private void initView(){
		ActionBar actionbar=getActionBar();
		spusernameandpasw = getSharedPreferences("config", MODE_PRIVATE);
		//用户名
		username=spusernameandpasw.getString("UserNumber", "");
		//用户密码
		userpasw=spusernameandpasw.getString("UserPasswd", "");
		et_loginusernum.setText(username);
		et_loginpasword.setText(userpasw);
		//服务器地址
		serverpath=spusernameandpasw.getString("ServerPath", "");
		
		if(serverpath.equals("")){
			Log.v("服务器地址3",serverpath);
			alertDialog("请确认服务器地址");
		}
		
		LoginConfig.getLoginConfig().setUsername(username);
		LoginConfig.getLoginConfig().setUserid("20121015095350990612c4db3cab4725");
		LoginConfig.getLoginConfig().setServerip(serverpath);
		
		if(!username.equals("")&&!userpasw.equals("")&&!serverpath.equals("")){
			loginCheck();
		}
	}
	/**
	 * 登录
	 * @param v
	 */
	public void onClick_gotoindex(View v) {
		loginCheck();
	}
	/**
	 * 登录校验结果及跳转
	 */
	private void loginCheck(){
		boolean loginresult=true;
		username=et_loginusernum.getText().toString().trim();
		userpasw=et_loginpasword.getText().toString().trim();
		//登录校验
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
	 * 登录校验方法
	 * @param name
	 * @param pswd
	 * @return
	 */
	private boolean loginCheckFun(String name,String pswd,String path){
		if(name.equals("")||pswd.equals("")||path.equals("")){
			Toast.makeText(getApplicationContext(), "用户名/密码/服务器地址不能为空", Toast.LENGTH_LONG).show();
			return false;
		}
		else{
			return true;
		}
	}
	/**
	 * 保存用户名密码
	 */
	private void saveUserData(){
		if(cb_rememberpwd.isChecked()){
			Editor editor=spusernameandpasw.edit();
			editor.putString("UserNumber", username);
			editor.putString("UserPasswd", userpasw);
			editor.commit();
			Toast.makeText(getApplicationContext(), "密码已保存", 0).show();
		}
	}
	/**
	 * 取消
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
		if (item.getTitle().toString().trim().equals("设置")) {
			Intent intent=new Intent();
			intent.setClass(getApplicationContext(), ConfigActivity.class);
			startActivityForResult(intent, 1002);
		}
		return super.onOptionsItemSelected(item);
	}
	public void alertDialog(String message) {
		new AlertDialog.Builder(this)
				.setTitle("提示")
				.setMessage(message)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						setResult(RESULT_OK);// 确定按钮事件
						Intent intent=new Intent();
						intent.setClass(getApplicationContext(), ConfigActivity.class);
						startActivityForResult(intent, 1002);
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// 取消按钮事件
					}
				}).show();
	}
}
