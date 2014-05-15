package com.example.activity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.example.oa_index.R;
import com.example.adapter.MessageListAdapter;
import com.example.beans.LoginConfig;
import com.example.beans.MyMessageBean;
import com.example.db.OADBHelper;
import com.example.http.HttpHelper;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
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
 * ��Ϣ�ݸ�
 */
public class MessageDraftListAvtivity extends FinalActivity {
    @ViewInject(id=R.id.tv_inbox) TextView tv_inbox;
    @ViewInject(id=R.id.lv_messages,itemClick="onClick_gotoMessage") ListView lv_messages;
    private final static int DOWNLOAD_MESSAGE_BEGIN=0;//������Ϣ
	private final static int DOWNLOAD_MESSAGE_SUCCESS=1;//������Ϣ�ɹ�
	private final static int DOWNLOAD_MESSAGE_FAILURE=-1;//������Ϣʧ��
	private final static int DATABASE_MESSAGE_SAVE=2;//������Ϣ����
	private final static int CONNECTION_TIMEOUT=3;//���ӳ�ʱ
	private final static int STATE_MESSAGE_ALL=0;//ȫ��
	private String inboxresult="0";
	private List<MyMessageBean> messagelist=null;//��Ϣlist
	private MessageListAdapter mesladapter=null;//��Ϣlist������
	private FinalDb db = null;
	private ProgressDialog dialog=null;//���ؽ��̶Ի���
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_outboxlist);
        db = FinalDb.create(this);
        initView();
        //������Ϣ
        handlerdealmessage.sendEmptyMessage(DOWNLOAD_MESSAGE_BEGIN);
        //handlersearchmessage.sendEmptyMessage(STATE_MESSAGE_ALL);
    }
	private void initView(){
    	ActionBar actionbar=getActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        getOverflowMenu();
    }

	/**
	 * ��ת������Ϣ
	 * @param v
	 */
	public void onClick_gotoMessage(AdapterView<?> arg0, View arg1, int arg2,long arg3){
		TextView tv_id=(TextView) arg1.findViewById(R.id.tv_message_id);
		String messageid = tv_id.getText().toString();
		Intent in = new Intent(getApplicationContext(), MessageShowActivity.class);
		Bundle bundle = new Bundle();  
		bundle.putCharSequence("messageid", messageid );
		in.putExtras(bundle);
		startActivity(in);
	}
	/**
	 * ������Ϣ
	 */
	private void downloadMessage(){
		new Thread(downloadMessages).start();
	}
	/**
	 * ������Ϣ
	 */
	private Runnable downloadMessages = new Runnable() {
		public void run() {
			try {
				Log.v("��Ϣ�ݸ���", "��������");
				String url=LoginConfig.getLoginConfig().getServerip();
				String userid=LoginConfig.getLoginConfig().getUserid();
				//String urlPath = "http://192.168.0.143:32768/oa/ashx/Ioa.ashx?ot=2&uid=20121015095350990612c4db3cab4725";//����ip
				String urlPath = "http://"+url+"/oa/ashx/Ioa.ashx?ot=2&uid="+userid;//����ip
				Log.v("��Ϣ�ݸ��ַ", urlPath);
				// ���ӷ������ɹ�֮�󣬽�������
				String data = new HttpHelper(urlPath).doGetString();
				if (data.equals("-1")) {
					tv_inbox.setText("-1");
					handlerdealmessage.sendEmptyMessage(DOWNLOAD_MESSAGE_FAILURE);
				} 
				else if (data.equals("0")) {
					tv_inbox.setText("0");
				} 
				else {
					inboxresult=data;
					handlerdealmessage.sendEmptyMessage(DOWNLOAD_MESSAGE_SUCCESS);
				}
			} catch (Exception e) {
				e.printStackTrace();
				handlerdealmessage.sendEmptyMessage(CONNECTION_TIMEOUT);
				handlersearchmessage.sendEmptyMessage(STATE_MESSAGE_ALL);
			}
			//���ؽ��̶Ի�����ʧ
			showDownloadDialog(false);
			//dialog.dismiss();
		}
	};
	/**
	 * �����Ϣ�ַ���
	 */
	private List<MyMessageBean> splitMessageString(String str){
		Log.v("��Ϣ�ݸ���", "�ָ�����");
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
	 * ��������
	 */
	private void saveData(){
		messagelist=splitMessageString(inboxresult);
		OADBHelper.saveMessages(messagelist,getApplicationContext());
		handlersearchmessage.sendEmptyMessage(STATE_MESSAGE_ALL);
	}
	/**
	 * �����Ϣ
	 */
	private void fillMessageList(int state){
		List<MyMessageBean> messlist=null;
    	messlist=db.findAll(MyMessageBean.class,"message_sendtime");
    	Log.v("��Ϣ�ݸ�������", messlist.size()+"");
    	mesladapter=new MessageListAdapter(getApplicationContext(), messlist);
    	mesladapter.notifyDataSetChanged();
		lv_messages.setAdapter(mesladapter);
	}
	/**
	 * ���ؽ��̶Ի���
	 */
	private void showDownloadDialog(boolean b){
		if(b)
			dialog=ProgressDialog.show(MessageDraftListAvtivity.this,"���ڼ���...","���Ժ�",true,true);//��ʾ���ؽ��̶Ի���
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
	private Handler handlerdealmessage=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			int whatVal = msg.what;
			switch (whatVal) {
			case DOWNLOAD_MESSAGE_BEGIN:
				Log.v("��Ϣ�ݸ���", "���ؿ�ʼ");
				showDownloadDialog(true);
				downloadMessage();
				break;
			case DOWNLOAD_MESSAGE_SUCCESS:
				Log.v("��Ϣ�ݸ���", "���سɹ�");
				saveData();
				break;
			case DOWNLOAD_MESSAGE_FAILURE:
				Log.v("��Ϣ�ݸ���", "����ʧ��");
				downloadMessage();
				break;
			case DATABASE_MESSAGE_SAVE:
				Log.v("��Ϣ�ݸ���", "��������");
				break;
			case CONNECTION_TIMEOUT:
				Log.v("��Ϣ�ݸ���", "���ӳ�ʱ");
				toastTimeOut();
				break;
			}
		}
	};
	//��ѯ��Ϣ�����ListView
	private Handler handlersearchmessage=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			int whatVal = msg.what;
			switch (whatVal) {
			case STATE_MESSAGE_ALL:
				fillMessageList(STATE_MESSAGE_ALL);
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
			handlerdealmessage.sendEmptyMessage(DOWNLOAD_MESSAGE_BEGIN);
		}
		//����
		else if (item.getItemId()==android.R.id.home){
			this.finish();
		}
		return super.onOptionsItemSelected(item);
	}
	//MyCenterActivity.dialog.dismiss();
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
