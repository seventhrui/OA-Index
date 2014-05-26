package com.example.activity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.example.HandlerCode;
import com.example.StateCode;
import com.example.oa_index.R;
import com.example.adapter.NoticeListAdapter;
import com.example.beans.MyNoticeBean;
import com.example.db.OADBHelper;
import com.example.http.HttpHelper;

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
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalDb;
import net.tsz.afinal.annotation.view.ViewInject;
/**
 * 通知通告列表
 */
public class NoticeListActivity extends FinalActivity {
    @ViewInject(id=R.id.tv_inbox) TextView tv_inbox;
    @ViewInject(id=R.id.lv_messages,itemClick="onClick_gotoNotice") ListView lv_notice;//通知通告列表
    
	private String inboxresult="0";
	private List<MyNoticeBean> noticelist=null;//通知list
	private NoticeListAdapter noticelistadapter=null;
	private FinalDb db = null;
	private ProgressDialog dialog=null;//下载进程对话框
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_outboxlist);
        db = FinalDb.create(this);
        initView();
        handlerdealnotice.sendEmptyMessage(HandlerCode.DOWNLOAD_NOTICE_BEGIN);
        //handlersearchnotice.sendEmptyMessage(StateCode.STATE_MESSAGE_ALL);
    }
	
	@Override
	protected void onResume() {
		super.onResume();
		handlerdealnotice.sendEmptyMessage(HandlerCode.DOWNLOAD_NOTICE_BEGIN);
	}
	
	private void initView(){
    	ActionBar actionbar=getActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        getOverflowMenu();
    }

	/**
	 * 跳转通知通告详情
	 * @param v
	 */
	public void onClick_gotoNotice(AdapterView<?> arg0, View arg1, int arg2,long arg3){
		TextView tv_id=(TextView) arg1.findViewById(R.id.tv_message_id);
		String noticeid = tv_id.getText().toString();
		Intent in = new Intent(getApplicationContext(), NoticeShowActivity.class);
		Bundle bundle = new Bundle();  
		bundle.putCharSequence("noticeid", noticeid );
		in.putExtras(bundle);
		startActivity(in);
	}
	/**
	 * 下载通知通告
	 */
	private void downloadNotice(){
		new Thread(downloadNotices).start();
	}
	/**
	 * 下载通知通告
	 */
	private Runnable downloadNotices = new Runnable() {
		public void run() {
			try {
				Log.v("通知通告发件箱", "下载数据");
				String urlPath = "http://192.168.0.143:32768/oa/ashx/Ioa.ashx?ot=2&uid=20121015095350990612c4db3cab4725";//内网ip
				// 连接服务器成功之后，解析数据
				String data = new HttpHelper(urlPath).doGetString();
				if (data.equals("-1")) {
					handlerdealnotice.sendEmptyMessage(HandlerCode.DOWNLOAD_NOTICE_FAILURE);
				} 
				else if (data.equals("0")) {
					handlerdealnotice.sendEmptyMessage(HandlerCode.DOWNLOAD_NOTICE_FAILURE);
				} 
				else {
					inboxresult=data;
					handlerdealnotice.sendEmptyMessage(HandlerCode.DOWNLOAD_NOTICE_SUCCESS);
				}
			} catch (Exception e) {
				e.printStackTrace();
				handlerdealnotice.sendEmptyMessage(HandlerCode.CONNECTION_TIMEOUT);
				handlersearchnotice.sendEmptyMessage(StateCode.STATE_MESSAGE_ALL);
			}
			//下载进程对话框消失
			showDownloadDialog(false);
		}
	};
	/**
	 * 拆分通知通告字符串
	 */
	private List<MyNoticeBean> splitNoticeString(String str){
		Log.v("通知通告发件箱", "分割数据");
		List<MyNoticeBean> mlist=new ArrayList<MyNoticeBean>();
		String[] notices=str.split("\\|");
		for(String s:notices){
			String[] notice=s.split("\\^");
			//MyNoticeBean m=new MyNoticeBean(notice[0],notice[1],notice[2],notice[3],notice[4],notice[5],notice[6],notice[7]);
			//mlist.add(m);
		}
		tv_inbox.setText(notices.length+"");
		return mlist;
	}
	/**
	 * 保存通知通告数据
	 */
	private void saveData(){
		noticelist=splitNoticeString(inboxresult);
		//OADBHelper.saveNotices(noticelist,getApplicationContext());
		handlersearchnotice.sendEmptyMessage(StateCode.STATE_MESSAGE_ALL);
	}
	/**
	 * 填充通知通告
	 */
	private void fillNoticeList(int state){
		List<MyNoticeBean> noticelist=null;
    	if(state==StateCode.STATE_MESSAGE_ALL){
    		noticelist=db.findAll(MyNoticeBean.class,"notice_sendtime");
    	}
    	Log.v("通知通告发件箱数量", noticelist.size()+"");
    	noticelistadapter=new NoticeListAdapter(getApplicationContext(), noticelist);
    	noticelistadapter.notifyDataSetChanged();
		lv_notice.setAdapter(noticelistadapter);
	}
	/**
	 * 下载进程对话框
	 */
	private void showDownloadDialog(boolean b){
		if(b)
			dialog=ProgressDialog.show(NoticeListActivity.this,"正在加载...","请稍后",true,true);//显示下载进程对话框
		else
			dialog.dismiss();//下载进程对话框消失
	}
	/**
	 * 提示下载超时
	 */
	private void toastTimeOut(){
		Toast.makeText(getApplicationContext(), R.string.timeout, Toast.LENGTH_LONG).show();
	}
	//下载信息
	private Handler handlerdealnotice=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			int whatVal = msg.what;
			switch (whatVal) {
			case HandlerCode.DOWNLOAD_NOTICE_BEGIN:
				Log.v("通知通告发件箱", "下载开始");
				showDownloadDialog(true);
				downloadNotice();
				break;
			case HandlerCode.DOWNLOAD_NOTICE_SUCCESS:
				Log.v("通知通告发件箱", "下载成功");
				saveData();
				break;
			case HandlerCode.DOWNLOAD_NOTICE_FAILURE:
				Log.v("通知通告发件箱", "下载失败");
				break;
			case HandlerCode.DATABASE_NOTICE_SAVE:
				Log.v("通知通告发件箱", "保存数据");
				break;
			case HandlerCode.CONNECTION_TIMEOUT:
				Log.v("收件箱", "连接超时");
				toastTimeOut();
				break;
			}
		}
	};
	//查询信息并填充ListView
	private Handler handlersearchnotice=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			int whatVal = msg.what;
			switch (whatVal) {
			case StateCode.STATE_MESSAGE_ALL:
				fillNoticeList(StateCode.STATE_MESSAGE_ALL);
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
		getMenuInflater().inflate(R.menu.menu_message_outboxlist, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getTitle().toString().trim().equals("刷新")) {
			Toast.makeText(getApplicationContext(), item.getTitle()+"", Toast.LENGTH_SHORT).show();
			handlerdealnotice.sendEmptyMessage(HandlerCode.DOWNLOAD_NOTICE_BEGIN);
		}
		//返回
		else if (item.getItemId()==android.R.id.home){
			this.finish();
		}
		return super.onOptionsItemSelected(item);
	}
}
