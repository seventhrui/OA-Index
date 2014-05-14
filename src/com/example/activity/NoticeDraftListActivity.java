package com.example.activity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.example.oa_index.R;
import com.example.adapter.MessageListAdapter;
import com.example.beans.MyMessageBean;
import com.example.db.OADBHelper;
import com.example.http.HttpHelper;

import android.app.ActionBar;
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
 * ֪ͨͨ��ݸ�
 */
public class NoticeDraftListActivity extends FinalActivity {
    @ViewInject(id=R.id.tv_inbox) TextView tv_inbox;
    @ViewInject(id=R.id.lv_messages,itemClick="onClick_gotoMessage") ListView lv_notice;
    private final static int DOWNLOAD_NOTICE_BEGIN=0;//����֪ͨͨ��
	private final static int DOWNLOAD_NOTICE_SUCCESS=1;//����֪ͨͨ��ɹ�
	private final static int DOWNLOAD_NOTICE_FAILURE=-1;//����֪ͨͨ��ʧ��
	private final static int DATABASE_NOTICE_SAVE=2;//����֪ͨͨ������
	private final static int STATE_MESSAGE_ALL=0;//ȫ��
	private String inboxresult="0";
	private List<MyMessageBean> noticelist=null;//��Ϣlist
	private MessageListAdapter noticelistadapter=null;
	private FinalDb db = null;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_outboxlist);
        handlersearchnotice.sendEmptyMessage(STATE_MESSAGE_ALL);
        db = FinalDb.create(this);
        initView();
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
				String data = new HttpHelper(urlPath).readParse();
				if (data.equals("-1")) {
					tv_inbox.setText("-1");
					handlerdealnotice.sendEmptyMessage(DOWNLOAD_NOTICE_FAILURE);
				} 
				else if (data.equals("0")) {
					tv_inbox.setText("0");
				} 
				else {
					inboxresult=data;
					handlerdealnotice.sendEmptyMessage(DOWNLOAD_NOTICE_SUCCESS);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	/**
	 * ���֪ͨͨ���ַ���
	 */
	private List<MyMessageBean> splitNoticeString(String str){
		Log.v("֪ͨͨ�淢����", "�ָ�����");
		List<MyMessageBean> mlist=new ArrayList<MyMessageBean>();
		String[] messages=str.split("\\|");
		for(String s:messages){
			String[] message=s.split("\\^");
			MyMessageBean m=new MyMessageBean(message[0],message[1],message[2],message[3],message[4],message[5],message[6],message[7]);
			mlist.add(m);
		}
		tv_inbox.setText(messages.length+"");
		return mlist;
	}
	/**
	 * ����֪ͨͨ������
	 */
	private void saveData(){
		noticelist=splitNoticeString(inboxresult);
		OADBHelper.saveMessages(noticelist,getApplicationContext());
		handlersearchnotice.sendEmptyMessage(STATE_MESSAGE_ALL);
	}
	/**
	 * ���֪ͨͨ��
	 */
	private void fillNoticeList(int state){
		List<MyMessageBean> messlist=null;
    	if(state==STATE_MESSAGE_ALL){
    		messlist=db.findAll(MyMessageBean.class,"message_sendtime");
    	}
    	Log.v("֪ͨͨ�淢��������", messlist.size()+"");
    	noticelistadapter=new MessageListAdapter(getApplicationContext(), messlist);
    	noticelistadapter.notifyDataSetChanged();
		lv_notice.setAdapter(noticelistadapter);
	}
	//������Ϣ
	private Handler handlerdealnotice=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			int whatVal = msg.what;
			switch (whatVal) {
			case DOWNLOAD_NOTICE_BEGIN:
				Log.v("֪ͨͨ�淢����", "���ؿ�ʼ");
				downloadNotice();
				break;
			case DOWNLOAD_NOTICE_SUCCESS:
				Log.v("֪ͨͨ�淢����", "���سɹ�");
				saveData();
				break;
			case DOWNLOAD_NOTICE_FAILURE:
				Log.v("֪ͨͨ�淢����", "����ʧ��");
				downloadNotice();
				break;
			case DATABASE_NOTICE_SAVE:
				Log.v("֪ͨͨ�淢����", "��������");
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
			case STATE_MESSAGE_ALL:
				fillNoticeList(STATE_MESSAGE_ALL);
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
		getMenuInflater().inflate(R.menu.menu_outboxlist, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getTitle().toString().trim().equals("ˢ��")) {
			Toast.makeText(getApplicationContext(), item.getTitle()+"", Toast.LENGTH_SHORT).show();
			handlerdealnotice.sendEmptyMessage(DOWNLOAD_NOTICE_BEGIN);
		}
		//����
		else if (item.getItemId()==android.R.id.home){
			this.finish();
		}
		return super.onOptionsItemSelected(item);
	}
}
