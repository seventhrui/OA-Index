package com.example.activity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.example.adapter.MyCenterListAdapter;
import com.example.beans.LoginConfig;
import com.example.beans.MyCenterBeans;
import com.example.beans.MyMessageBean;
import com.example.db.OADBHelper;
import com.example.http.HttpHelper;
import com.example.mybase.MyBaseActivity;
import com.example.oa_index.R;
import com.example.utils.HttpUtil;

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
 * OA��ʼ���� ��������
 * ��Ϣ��ͨ
 * ֪ͨͨ��
 * ��������
 * ��������
 * ����֪ʶ
 */
public class MyCenterActivity extends MyBaseActivity {
	private static final int COMMECTION=1;//��Ϣ��ͨ
	private static final int NOTIFICATION=2;//֪ͨͨ��
	private static final int WORKTASK=3;//��������
	private static final int WORKREMINDER=4;//��������
	private static final int KNOWLEDGE=5;//����֪ʶ
	private final static int DOWNLOAD_MESSAGE_BEGIN=0;//������Ϣ
	private final static int DOWNLOAD_MESSAGE_SUCCESS=1;//������Ϣ�ɹ�
	private final static int DOWNLOAD_MESSAGE_FAILURE=-1;//������Ϣʧ��
	private final static int CONNECTION_TIMEOUT=3;//���ӳ�ʱ
	private final static int DATABASE_MESSAGE_SAVE=2;//������Ϣ����
	@ViewInject(id=R.id.lv_mycenter,itemClick="onClick_gotopart") ListView lv_mycenter;//���������б�
	private MyCenterListAdapter mcladapter=null;//���������б�������
	List<MyCenterBeans> mcblist=null;//��������Beans
	private ProgressDialog dialog=null;//���ؽ��̶Ի���
	private String inboxresult="0";
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycenter);
        handlerDownload.sendEmptyMessage(DOWNLOAD_MESSAGE_BEGIN);
        initView();
    }
	public void initView(){
		ActionBar actionbar=getActionBar();
		//actionbar.setDisplayHomeAsUpEnabled(true);
		getOverflowMenu();
		fillMyCenter();
	}
	private void fillMyCenter(){
		mcblist=new ArrayList<MyCenterBeans>();
		mcblist.add(new MyCenterBeans("��Ϣ��ͨ", 0));
		mcblist.add(new MyCenterBeans("֪ͨͨ��", 0));
		mcblist.add(new MyCenterBeans("��������", 0));
		mcblist.add(new MyCenterBeans("��������", 0));
		mcblist.add(new MyCenterBeans("����֪ʶ", 0));
		mcladapter=new MyCenterListAdapter(getApplicationContext(), mcblist);
		mcladapter.notifyDataSetChanged();
    	lv_mycenter.setAdapter(mcladapter);
	}
	public void onClick_gotopart(AdapterView<?> arg0, View arg1, int arg2,long arg3){
		TextView tv_id=(TextView) arg1.findViewById(R.id.tv_mycenter_funtype);
		String type=tv_id.getText().toString().trim();
		if (type.equals("��Ϣ��ͨ")) {
			handler.sendEmptyMessage(COMMECTION);
		} else if (type.equals("֪ͨͨ��")) {
			handler.sendEmptyMessage(NOTIFICATION);
		} else if (type.equals("��������")) {
			handler.sendEmptyMessage(WORKTASK);
		} else if (type.equals("��������")) {
			handler.sendEmptyMessage(WORKREMINDER);
		} else if (type.equals("����֪ʶ")) {
			handler.sendEmptyMessage(KNOWLEDGE);
		}
	}
	/**
	 * finalhttp������Ϣ(����)
	 */
	public void getMessageString() {
		showDownloadDialog(true);
		String url=LoginConfig.getLoginConfig().getServerip();
		String userid=LoginConfig.getLoginConfig().getUserid();
		//String urlPath = "http://192.168.0.143:32768/oa/ashx/Ioa.ashx?ot=2&uid=20121015095350990612c4db3cab4725";//����ip
		String urlPath = "http://"+url+"/oa/ashx/Ioa.ashx?ot=2&uid="+userid;//����ip
		Log.v("��������--��Ϣip",urlPath);
		
		String resultstring="";
		resultstring=HttpUtil.downloadString(urlPath);
		Log.v("��������--��Ϣ",resultstring+",");
		if(!resultstring.isEmpty()){
			List<MyMessageBean> mmblist=splitMessageString(resultstring);
			OADBHelper.saveMessages(mmblist,getApplicationContext());//������Ϣ
			handlerDownload.sendEmptyMessage(DOWNLOAD_MESSAGE_SUCCESS);
		}
		else{
			/**
			 * ��ʾ���س�ʱ
			 */
			Toast.makeText(getApplicationContext(), R.string.timeout, Toast.LENGTH_LONG).show();
		}
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
				Log.v("��������", "��������");
				String url=LoginConfig.getLoginConfig().getServerip();
				String userid=LoginConfig.getLoginConfig().getUserid();
				//String urlPath = "http://192.168.0.143:32768/oa/ashx/Ioa.ashx?ot=2&uid=20121015095350990612c4db3cab4725";//����ip
				String urlPath = "http://"+url+"/oa/ashx/Ioa.ashx?ot=2&uid="+userid;//����ip
				Log.v("��������", urlPath);
				// ���ӷ������ɹ�֮�󣬽�������
				String data = new HttpHelper(urlPath).readParse();
				if (data.equals("-1")) {
					handlerDownload.sendEmptyMessage(DOWNLOAD_MESSAGE_FAILURE);
				} 
				else if (data.equals("0")) {
				} 
				else {
					inboxresult=data;
					handlerDownload.sendEmptyMessage(DOWNLOAD_MESSAGE_SUCCESS);
				}
			} catch (Exception e) {
				e.printStackTrace();
				handlerDownload.sendEmptyMessage(CONNECTION_TIMEOUT);
			}
			//���ؽ��̶Ի�����ʧ
			showDownloadDialog(false);
		}
	};
	/**
	 * �����Ϣ�ַ���
	 */
	private List<MyMessageBean> splitMessageString(String str){
		Log.v("��������", "�ָ�����");
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
	 * �����ݿ��л�ȡ���������������б�eg:��Ϣ��ͨ     1��
	 */
	private void getMyCenterData(){
		mcblist=new ArrayList<MyCenterBeans>();
		FinalDb db = FinalDb.create(this);
		//δ��  ��Ϣ��ͨ ����
		int messcount=db.findAllByWhere(MyMessageBean.class,"message_state='"+0+"'").size();
		mcblist.add(new MyCenterBeans("��Ϣ��ͨ", messcount));
		mcblist.add(new MyCenterBeans("֪ͨͨ��", 0));
		mcblist.add(new MyCenterBeans("��������", 0));
		mcblist.add(new MyCenterBeans("��������", 0));
		mcblist.add(new MyCenterBeans("����֪ʶ", 0));
		showDownloadDialog(false);
	}
	/**
	 * ���ؽ��̶Ի���
	 */
	private void showDownloadDialog(boolean b){
		if(b)
			dialog=ProgressDialog.show(MyCenterActivity.this,"���ڼ���...","���Ժ�",true,true);//��ʾ���ؽ��̶Ի���
		else
			dialog.dismiss();//���ؽ��̶Ի�����ʧ
	}
	/**
	 * ��ʾ���س�ʱ
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
			case COMMECTION:
				intent.setClass(getApplicationContext(), MessageInboxListAvtivity.class);
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
			case DOWNLOAD_MESSAGE_BEGIN:
				Log.v("��������", "���ؿ�ʼ");
				//getMessageString();
				showDownloadDialog(true);
				downloadMessage();
				break;
			case DOWNLOAD_MESSAGE_SUCCESS:
				Log.v("��������", "���سɹ�");
				//�����б�
				getMyCenterData();
				mcladapter=new MyCenterListAdapter(getApplicationContext(), mcblist);;
				mcladapter.notifyDataSetChanged();
				lv_mycenter.setAdapter(mcladapter);
				break;
			case DOWNLOAD_MESSAGE_FAILURE:
				Log.v("��������", "����ʧ��");
				//getMessageString();
				downloadMessage();
				break;
			case DATABASE_MESSAGE_SAVE:
				Log.v("��������", "��������");
				break;
			case CONNECTION_TIMEOUT:
				Log.v("�ռ���", "���ӳ�ʱ");
				toastTimeOut();
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
		getMenuInflater().inflate(R.menu.menu_mycenter, menu);
		return true;
	}
	@Override//
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getTitle().toString().trim().equals("ˢ��")) {
			handlerDownload.sendEmptyMessage(DOWNLOAD_MESSAGE_BEGIN);
		}
		else if (item.getTitle().toString().trim().equals("��ʼ�˵�")){
			this.finish();
			Intent intent=new Intent();
			intent.setClass(getApplicationContext(), StartMenuActivity.class);
			startActivity(intent);
		}
		else if (item.getTitle().toString().trim().equals("����")){
			this.finish();
			Intent intent=new Intent();
			intent.setClass(getApplicationContext(), ConfigActivity.class);
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}
}
