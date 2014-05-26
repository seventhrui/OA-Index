package com.example.activity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.example.ActivityCode;
import com.example.HandlerCode;
import com.example.StateCode;
import com.example.adapter.MyCenterListAdapter;
import com.example.beans.LoginConfig;
import com.example.beans.MyCenterBeans;
import com.example.beans.MyMessageBean;
import com.example.db.OADBHelper;
import com.example.http.HttpHelper;
import com.example.mybase.MyBaseActivity;
import com.example.oa_index.R;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import net.tsz.afinal.FinalDb;
import net.tsz.afinal.annotation.view.ViewInject;
/**
 * OA开始界面 个人中心
 * 消息沟通
 * 通知通告
 * 工作任务
 * 工作提醒
 * 最新知识
 */
public class MyCenterActivity extends MyBaseActivity {
	
	@ViewInject(id=R.id.lv_mycenter,itemClick="onClick_gotopart") ListView lv_mycenter;//个人中心列表
	private MyCenterListAdapter mcladapter=null;//个人中心列表适配器
	List<MyCenterBeans> mcblist=null;//个人中心Beans
	private ProgressDialog dialog=null;//下载进程对话框
	private String inboxresult="0";
	private List<MyMessageBean> messagelist=null;//信息list
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycenter);
        handlerDownload.sendEmptyMessage(HandlerCode.DOWNLOAD_MESSAGE_BEGIN);
        initView();
    }
	public void initView(){
		ActionBar actionbar=getActionBar();
		getOverflowMenu();
		fillMyCenter();
	}
	private void fillMyCenter(){
		mcblist=new ArrayList<MyCenterBeans>();
		mcblist.add(new MyCenterBeans("消息沟通", 0));
		mcblist.add(new MyCenterBeans("通知通告", 0));
		mcblist.add(new MyCenterBeans("工作任务", 0));
		mcblist.add(new MyCenterBeans("工作提醒", 0));
		mcblist.add(new MyCenterBeans("最新知识", 0));
		mcladapter=new MyCenterListAdapter(getApplicationContext(), mcblist);
		mcladapter.notifyDataSetChanged();
    	lv_mycenter.setAdapter(mcladapter);
	}
	public void onClick_gotopart(AdapterView<?> arg0, View arg1, int arg2,long arg3){
		TextView tv_id=(TextView) arg1.findViewById(R.id.tv_mycenter_funtype);
		String type=tv_id.getText().toString().trim();
		if (type.equals("消息沟通")) {
			handler.sendEmptyMessage(ActivityCode.COMMECTION);
		} else if (type.equals("通知通告")) {
			handler.sendEmptyMessage(ActivityCode.NOTIFICATION);
		} else if (type.equals("工作任务")) {
			handler.sendEmptyMessage(ActivityCode.WORKTASK);
		} else if (type.equals("工作提醒")) {
			handler.sendEmptyMessage(ActivityCode.WORKREMINDER);
		} else if (type.equals("最新知识")) {
			handler.sendEmptyMessage(ActivityCode.KNOWLEDGE);
		}
	}
	/**
	 * 下载信息
	 */
	private void downloadMessage(){
		new Thread(downloadMessages).start();
	}
	/**
	 * 下载信息
	 */
	private Runnable downloadMessages = new Runnable() {
		public void run() {
			try {
				Log.v("个人中心", "下载数据");
				String url=LoginConfig.getLoginConfig().getServerip();
				String userid=LoginConfig.getLoginConfig().getUserid();
				String urlPath = "http://"+url+"/oa/ashx/Ioa.ashx?ot=2&uid="+userid;//内网ip
				Log.v("个人中心", urlPath);
				// 连接服务器成功之后，解析数据
				String data = new HttpHelper(urlPath).doGetString();
				Log.v("个人中心下载返回数据", data);
				if (data.equals("-1")) {
					handlerDownload.sendEmptyMessage(HandlerCode.DOWNLOAD_MESSAGE_FAILURE);
				} 
				else if (data.equals("0")) {
				} 
				else {
					inboxresult=data;
					handlerDownload.sendEmptyMessage(HandlerCode.DOWNLOAD_MESSAGE_SUCCESS);
				}
			} catch (Exception e) {
				e.printStackTrace();
				handlerDownload.sendEmptyMessage(HandlerCode.CONNECTION_TIMEOUT);
			}
			//下载进程对话框消失
			showDownloadDialog(false);
		}
	};
	/**
	 * 拆分信息字符串
	 */
	private List<MyMessageBean> splitMessageString(String str){
		Log.v("个人中心", "分割数据");
		List<MyMessageBean> mlist=new ArrayList<MyMessageBean>();
		String[] messages=str.split("\\|");
		for(String s:messages){
			String[] message=s.split("\\^");
			MyMessageBean m=new MyMessageBean(message[0],message[1],message[2],message[3],message[4],message[5],message[6],message[7],StateCode.MESSAGE_TYPE_RECEIVE);
			mlist.add(m);
		}
		return mlist;
	}
	private void saveData(){
		messagelist=splitMessageString(inboxresult);
		OADBHelper.saveMessages(messagelist,getApplicationContext());
		handlerDownload.sendEmptyMessage(HandlerCode.DATABASE_MESSAGE_SAVE);
	}
	/**
	 * 从数据库中获取数据填充个人中心列表（eg:消息沟通     1）
	 */
	private void getMyCenterData(){
		mcblist=new ArrayList<MyCenterBeans>();
		FinalDb db = FinalDb.create(this);
		//未读  消息沟通 数量
		int messcount=db.findAllByWhere(MyMessageBean.class,"message_state='"+0+"' AND "+"message_type='"+StateCode.MESSAGE_TYPE_RECEIVE+"'").size();
		mcblist.add(new MyCenterBeans("消息沟通", messcount));
		mcblist.add(new MyCenterBeans("通知通告", 0));
		mcblist.add(new MyCenterBeans("工作任务", 0));
		mcblist.add(new MyCenterBeans("工作提醒", 0));
		mcblist.add(new MyCenterBeans("最新知识", 0));
	}
	/**
	 * 下载进程对话框
	 */
	private void showDownloadDialog(boolean b){
		if(b)
			dialog=ProgressDialog.show(MyCenterActivity.this,"正在加载...","请稍后",true,true);//显示下载进程对话框
		else
			dialog.dismiss();//下载进程对话框消失
	}
	/**
	 * 提示下载超时
	 */
	private void toastTimeOut(){
		Toast.makeText(getApplicationContext(), R.string.timeout, Toast.LENGTH_LONG).show();
	}
	private Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Intent intent=new Intent();
			switch (msg.what) {
			case ActivityCode.COMMECTION:
				intent.setClass(getApplicationContext(), MessageInboxListAvtivity.class);
				break;
			case ActivityCode.NOTIFICATION:
				intent.setClass(getApplicationContext(), NoticeListActivity.class);
				break;
			case ActivityCode.WORKTASK:
				intent.setClass(getApplicationContext(), Other.class);
				break;
			case ActivityCode.WORKREMINDER:
				intent.setClass(getApplicationContext(), Other.class);
				break;
			case ActivityCode.KNOWLEDGE:
				intent.setClass(getApplicationContext(), KnowledgeListActivity.class);
				break;
			}
			startActivity(intent);
		}
	};
	private Handler handlerDownload=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case HandlerCode.DOWNLOAD_MESSAGE_BEGIN:
				Log.v("个人中心", "下载开始");
				showDownloadDialog(true);
				downloadMessage();
				break;
			case HandlerCode.DOWNLOAD_MESSAGE_SUCCESS:
				Log.v("个人中心", "下载成功");
				//更新列表
				saveData();
				break;
			case HandlerCode.DOWNLOAD_MESSAGE_FAILURE:
				Log.v("个人中心", "下载失败");
				downloadMessage();
				break;
			case HandlerCode.DATABASE_MESSAGE_SAVE:
				Log.v("个人中心", "保存数据");
				getMyCenterData();
				mcladapter=new MyCenterListAdapter(getApplicationContext(), mcblist);;
				mcladapter.notifyDataSetChanged();
				lv_mycenter.setAdapter(mcladapter);
				break;
			case HandlerCode.CONNECTION_TIMEOUT:
				Log.v("收件箱", "连接超时");
				toastTimeOut();
				break;
			}
		}
	};
	
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
		getMenuInflater().inflate(R.menu.menu_mycenter, menu);
		return true;
	}
	@Override//
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getTitle().toString().trim().equals("刷新")) {
			handlerDownload.sendEmptyMessage(HandlerCode.DOWNLOAD_MESSAGE_BEGIN);
		}
		else if (item.getTitle().toString().trim().equals("开始菜单")){
			this.finish();
			Intent intent=new Intent();
			intent.setClass(getApplicationContext(), StartMenuActivity.class);
			startActivity(intent);
		}
		else if (item.getTitle().toString().trim().equals("设置")){
			this.finish();
			Intent intent=new Intent();
			intent.setClass(getApplicationContext(), ConfigActivity.class);
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}
}
