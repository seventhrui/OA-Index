package com.example.activity;

import java.util.ArrayList;
import java.util.List;

import com.example.adapter.MyCenterListAdapter;
import com.example.beans.MyCenterBeans;
import com.example.beans.MyMessageBean;
import com.example.db.OADBHelper;
import com.example.oa_index.R;
import com.example.utils.HttpUtil;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import net.tsz.afinal.FinalActivity;
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
public class MyCenterActivity extends FinalActivity {
	private static final int COMMECTION=1;//消息沟通
	private static final int NOTIFICATION=2;//通知通告
	private static final int WORKTASK=3;//工作任务
	private static final int WORKREMINDER=4;//工作提醒
	private static final int KNOWLEDGE=5;//最新知识
	private final static int DOWNLOAD_MESSAGE_BEGIN=0;//下载信息
	private final static int DOWNLOAD_MESSAGE_SUCCESS=1;//下载信息成功
	private final static int DOWNLOAD_MESSAGE_FAILURE=-1;//下载信息失败
	private final static int DATABASE_MESSAGE_SAVE=2;//保存信息数据
	@ViewInject(id=R.id.lv_mycenter,itemClick="onClick_gotopart") ListView lv_mycenter;//个人中心列表
	private MyCenterListAdapter mcladapter=null;//个人中心列表适配器
	List<MyCenterBeans> mcblist=null;//个人中心Beans
	private ActionBar actionbar=null;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycenter);
        handlerDownload.sendEmptyMessage(DOWNLOAD_MESSAGE_BEGIN);
        initView();
    }
	public void initView(){
		actionbar=getActionBar();
		fillMyCenter();
	}
	private void fillMyCenter(){
		//List<MyCenterBeans> mcblist=new ArrayList<MyCenterBeans>();
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
			handler.sendEmptyMessage(COMMECTION);
		} else if (type.equals("通知通告")) {
			handler.sendEmptyMessage(NOTIFICATION);
		} else if (type.equals("工作任务")) {
			handler.sendEmptyMessage(WORKTASK);
		} else if (type.equals("工作提醒")) {
			handler.sendEmptyMessage(WORKREMINDER);
		} else if (type.equals("最新知识")) {
			handler.sendEmptyMessage(KNOWLEDGE);
		}
	}
	/**
	 * 下载信息
	 */
	public void getMessageString() {
		String urlPath = "http://192.168.0.143:32768/oa/ashx/Ioa.ashx?ot=2&uid=20121015095350990612c4db3cab4725";
		String resultstring="";
		resultstring=HttpUtil.downloadString(urlPath);
		Log.v("个人中心--信息",resultstring+",");
		if(!resultstring.isEmpty()){
			List<MyMessageBean> mmblist=splitMessageString(resultstring);
			OADBHelper.saveMessages(mmblist,getApplicationContext());//保存信息
			handlerDownload.sendEmptyMessage(DOWNLOAD_MESSAGE_SUCCESS);
		}
	}
	/**
	 * 拆分信息字符串
	 */
	private List<MyMessageBean> splitMessageString(String str){
		Log.v("个人中心", "分割数据");
		List<MyMessageBean> mlist=new ArrayList<MyMessageBean>();
		String[] messages=str.split("\\|");
		for(String s:messages){
			String[] message=s.split("\\^");
			MyMessageBean m=new MyMessageBean(message[0],message[1],message[2],message[3],message[4],message[5],message[6],message[7]);
			mlist.add(m);
		}
		return mlist;
	}
	/**
	 * 从数据库中获取数据填充个人中心列表（eg:消息沟通     1）
	 */
	private void getMyCenterData(){
		mcblist=new ArrayList<MyCenterBeans>();
		FinalDb db = FinalDb.create(this);
		int messcount=db.findAllByWhere(MyMessageBean.class,"message_state='"+0+"'").size();
		mcblist.add(new MyCenterBeans("消息沟通", messcount));
		mcblist.add(new MyCenterBeans("通知通告", 0));
		mcblist.add(new MyCenterBeans("工作任务", 0));
		mcblist.add(new MyCenterBeans("工作提醒", 0));
		mcblist.add(new MyCenterBeans("最新知识", 0));
	}
	private Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Intent intent=new Intent();
			switch (msg.what) {
			case COMMECTION:
				intent.setClass(getApplicationContext(), InboxListAvtivity.class);
				break;
			case NOTIFICATION:
				intent.setClass(getApplicationContext(), Other.class);
				break;
			case WORKTASK:
				intent.setClass(getApplicationContext(), Other.class);
				break;
			case WORKREMINDER:
				intent.setClass(getApplicationContext(), Other.class);
				break;
			case KNOWLEDGE:
				intent.setClass(getApplicationContext(), Other.class);
				break;
			}
			startActivity(intent);
		}
	};
	private Handler handlerDownload=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Intent intent=new Intent();
			switch (msg.what) {
			case DOWNLOAD_MESSAGE_BEGIN:
				Log.v("个人中心", "下载开始");
				getMessageString();
				break;
			case DOWNLOAD_MESSAGE_SUCCESS:
				Log.v("个人中心", "下载成功");
				//更新列表
				getMyCenterData();
				mcladapter=new MyCenterListAdapter(getApplicationContext(), mcblist);;
				mcladapter.notifyDataSetChanged();
				lv_mycenter.setAdapter(mcladapter);
				break;
			case DOWNLOAD_MESSAGE_FAILURE:
				Log.v("个人中心", "下载失败");
				getMessageString();
				break;
			case DATABASE_MESSAGE_SAVE:
				Log.v("个人中心", "保存数据");
				break;
			}
		}
	};
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_mycenter, menu);
		return true;
	}
	@Override//
	public boolean onOptionsItemSelected(MenuItem item) {
		Toast.makeText(getApplicationContext(), item.getTitle().toString().trim(), Toast.LENGTH_SHORT).show();
		if (item.getTitle().toString().trim().equals("刷新")) {
			Toast.makeText(getApplicationContext(), item.getTitle()+"", Toast.LENGTH_SHORT).show();
			handlerDownload.sendEmptyMessage(DOWNLOAD_MESSAGE_BEGIN);
		}
		else if (item.getTitle().toString().trim().equals("菜单")){
			this.finish();
			Intent intent=new Intent();
			intent.setClass(getApplicationContext(), StartMenuActivity.class);
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}
}
