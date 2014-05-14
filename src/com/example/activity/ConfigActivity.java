package com.example.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.oa_index.R;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
/**
 * ��������ҳ��
 */
public class ConfigActivity extends FinalActivity {
	@ViewInject(id = R.id.et_serverpath) EditText et_serverpath;// ��������ַ
	@ViewInject(id = R.id.bt_saveconfig, click = "onClick_saveConfig") Button bt_saveconfig;// ���水ť
	@ViewInject(id = R.id.bt_logout, click = "onClick_Logout") Button bt_logout;// ȡ����ť
	private String serverpath;
	private SharedPreferences spserverpath;// �洢�û���������

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_config);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		initView();
	}
	
	private void initView() {
		spserverpath = getSharedPreferences("config", MODE_PRIVATE);
		et_serverpath.setText(spserverpath.getString("ServerPath", ""));
	}
	public void onClick_saveConfig(View v){
		serverpath=et_serverpath.getText().toString().trim();
		Editor editor=spserverpath.edit();
		editor.putString("ServerPath", serverpath);
		editor.commit();
		Toast.makeText(getApplicationContext(), "����ɹ�", 0).show();
		Intent intent=new Intent();
		intent.putExtra("serverpath", serverpath);
        setResult(1003, intent);
		this.finish();
	}
	public void onClick_Logout(View v){
		serverpath=et_serverpath.getText().toString().trim();
		Editor editor=spserverpath.edit();
		editor.putString("UserNumber", "");
		editor.putString("UserPasswd", "");
		editor.commit();
		this.finish();
		Intent intent=new Intent();
		intent.setClass(getApplicationContext(), LoginActivity.class);
		startActivity(intent);
	}
}
