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
 * ֪ͨͨ���б�
 */
public class NoticeListActivity extends FinalActivity {
    @ViewInject(id=R.id.tv_inbox) TextView tv_inbox;
    @ViewInject(id=R.id.lv_messages,itemClick="onClick_gotoNotice") ListView lv_notice;//֪ͨͨ���б�
    
	private String inboxresult="0";
	private List<MyNoticeBean> noticelist=null;//֪ͨlist
	private NoticeListAdapter noticelistadapter=null;
	private FinalDb db = null;
	private ProgressDialog dialog=null;//���ؽ��̶Ի���
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
	 * ��ת֪ͨͨ������
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
	 * ����֪ͨͨ��
	 */
	private void downloadNotice(){
		new Thread(downloadNotices).start();
	}
	/**
	 * ����֪ͨͨ��
	 */
	private Runnable downloadNotices = new Runnable() {
		public void run() {
			try {
				Log.v("֪ͨͨ�淢����", "��������");
				String urlPath = "http://192.168.0.143:32768/oa/ashx/Ioa.ashx?ot=2&uid=20121015095350990612c4db3cab4725";//����ip
				// ���ӷ������ɹ�֮�󣬽�������
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
			//���ؽ��̶Ի�����ʧ
			showDownloadDialog(false);
		}
	};
	/**
	 * ���֪ͨͨ���ַ���
	 */
	private List<MyNoticeBean> splitNoticeString(String str){
		Log.v("֪ͨͨ�淢����", "�ָ�����");
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
	 * ����֪ͨͨ������
	 */
	private void saveData(){
		noticelist=splitNoticeString(inboxresult);
		//OADBHelper.saveNotices(noticelist,getApplicationContext());
		handlersearchnotice.sendEmptyMessage(StateCode.STATE_MESSAGE_ALL);
	}
	/**
	 * ���֪ͨͨ��
	 */
	private void fillNoticeList(int state){
		List<MyNoticeBean> noticelist=null;
    	if(state==StateCode.STATE_MESSAGE_ALL){
    		noticelist=db.findAll(MyNoticeBean.class,"notice_sendtime");
    	}
    	Log.v("֪ͨͨ�淢��������", noticelist.size()+"");
    	noticelistadapter=new NoticeListAdapter(getApplicationContext(), noticelist);
    	noticelistadapter.notifyDataSetChanged();
		lv_notice.setAdapter(noticelistadapter);
	}
	/**
	 * ���ؽ��̶Ի���
	 */
	private void showDownloadDialog(boolean b){
		if(b)
			dialog=ProgressDialog.show(NoticeListActivity.this,"���ڼ���...","���Ժ�",true,true);//��ʾ���ؽ��̶Ի���
		else
			dialog.dismiss();//���ؽ��̶Ի�����ʧ
	}
	/**
	 * ��ʾ���س�ʱ
	 */
	private void toastTimeOut(){
		Toast.makeText(getApplicationContext(), R.string.timeout, Toast.LENGTH_LONG).show();
	}
	//������Ϣ
	private Handler handlerdealnotice=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			int whatVal = msg.what;
			switch (whatVal) {
			case HandlerCode.DOWNLOAD_NOTICE_BEGIN:
				Log.v("֪ͨͨ�淢����", "���ؿ�ʼ");
				showDownloadDialog(true);
				downloadNotice();
				break;
			case HandlerCode.DOWNLOAD_NOTICE_SUCCESS:
				Log.v("֪ͨͨ�淢����", "���سɹ�");
				saveData();
				break;
			case HandlerCode.DOWNLOAD_NOTICE_FAILURE:
				Log.v("֪ͨͨ�淢����", "����ʧ��");
				break;
			case HandlerCode.DATABASE_NOTICE_SAVE:
				Log.v("֪ͨͨ�淢����", "��������");
				break;
			case HandlerCode.CONNECTION_TIMEOUT:
				Log.v("�ռ���", "���ӳ�ʱ");
				toastTimeOut();
				break;
			}
		}
	};
	//��ѯ��Ϣ�����ListView
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
	 * ����˵�
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
		if (item.getTitle().toString().trim().equals("ˢ��")) {
			Toast.makeText(getApplicationContext(), item.getTitle()+"", Toast.LENGTH_SHORT).show();
			handlerdealnotice.sendEmptyMessage(HandlerCode.DOWNLOAD_NOTICE_BEGIN);
		}
		//����
		else if (item.getItemId()==android.R.id.home){
			this.finish();
		}
		return super.onOptionsItemSelected(item);
	}
}
