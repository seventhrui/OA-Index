package com.example.activity;

import java.io.File;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import com.example.HandlerCode;
import com.example.beans.LoginConfig;
import com.example.fileexplorer.CallbackBundle;
import com.example.fileexplorer.OpenFileDialog;
import com.example.http.HttpHelper;
import com.example.oa_index.R;
import com.example.utils.StringUtils;

import android.app.ActionBar;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

/**
 * ��Ϣ�ظ�
 */
public class MessageRepliesActivity extends FinalActivity {
	private final static int openfileDialogId = 0;
	private final static int copyfileDialogId = 1;
	
	private String originalsender = "";// �ռ��ˣ�ԭ�����ˣ�
	private String myname = "";// �û���
	private String messagetitle = "";// ԭ��Ϣ����
	private String messageid="";//��Ϣid
	private String receiver="";//�ռ���
	private String receiverid="";//�ռ���id
	private String messagecontent;//�ظ�����
	private String originalcontent="";//ԭ����
	private String sendmessagecontent="";//��Ҫ���͵�����
	private String filepath="";//����·��
	private String filename="";//������

	@ViewInject(id = R.id.tv_messagereceiver) TextView tv_messagereceiver;//�ռ�����
	@ViewInject(id = R.id.tv_messagesender)	TextView tv_messagesender;//��������
	@ViewInject(id = R.id.tv_messagetitle) TextView tv_messagetitle;//��Ϣ����
	@ViewInject(id = R.id.et_messagecontent) EditText et_messagecontent;//�ظ�����
	@ViewInject(id = R.id.wv_originalcontent) WebView wv_originalcontent;//ԭ����
	@ViewInject(id = R.id.tv_filename) TextView tv_filename;//������
	@ViewInject(id = R.id.bt_searchfile,click="onClick_SearchFile") Button bt_searchfile;//��Ӹ���
	private ProgressDialog dialog=null;//���ͽ��̶Ի���
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_replies);
		initView();
	}

	private void initView() {
		ActionBar actionbar = getActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		originalsender = getIntent().getStringExtra("messagesender");
		myname = getIntent().getStringExtra("myname");
		messagetitle = getIntent().getStringExtra("messagetitle");
		originalcontent=getIntent().getStringExtra("messagecontent");
		tv_messagereceiver.setText(originalsender);
		tv_messagesender.setText(myname);
		tv_messagetitle.setText("�ظ���" + messagetitle);
		if(originalcontent.isEmpty())
			originalcontent=" ";
		wv_originalcontent.loadDataWithBaseURL(null, originalcontent, "text/html", "utf-8", null);
	}
	/**
	 * ���ļ���Դ����
	 * @param v
	 */
	public void onClick_SearchFile(View v){
		showDialog(openfileDialogId);
	}
	private String getMessageContent(){
		String contnet="";//���͵���Ϣ����
		String hasfile="0";//�Ƿ��и���
		receiver=tv_messagereceiver.getText().toString().trim();//�ռ�������
		messagetitle=tv_messagetitle.getText().toString().trim();//��Ϣ����
		messagecontent=et_messagecontent.getText().toString().trim();//��Ϣ����
		filename=tv_filename.getText().toString().trim();//������
		if(!filename.equals(""))//����и����� hasfile ��һ
			hasfile="1";
		messageid=StringUtils.GenerateGUID(11);//����32Ϊ����Ϣid
		// �ռ���ID|�ռ�������|����|����|����|��ϢID
		contnet=receiverid+"|"+receiver+"|"+messagetitle+"|"+messagecontent+"\n"+originalcontent+"|"+hasfile+"|"+messageid;
		Log.v("��Ϣ�ظ�����",contnet);
		return contnet;
	}
	private Handler handlersendmessage = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			int whatVal = msg.what;
			switch (whatVal) {
			case HandlerCode.SEND_MESSAGE_BEGIN:
				Toast.makeText(getApplicationContext(), "������Ϣ", Toast.LENGTH_SHORT).show();
				showDownloadDialog(true);
				sendMyMessage();
				break;
			case HandlerCode.SEND_MESSAGE_SUCCESS:
				Toast.makeText(getApplicationContext(), "������Ϣ�ɹ�", Toast.LENGTH_SHORT).show();
				if(!filename.equals("")){
					showDownloadDialog(true);
					sendMyFile();//���͸���
				}
				break;
			case HandlerCode.SEND_MESSAGE_FAILURE:
				Toast.makeText(getApplicationContext(), "������Ϣʧ��", Toast.LENGTH_SHORT).show();
				break;
			case HandlerCode.SEND_FILE_SUCCESS:
				Toast.makeText(getApplicationContext(), "���͸����ɹ�", Toast.LENGTH_SHORT).show();
				break;
			case HandlerCode.SEND_FILE_FAILURE:
				Toast.makeText(getApplicationContext(), "���͸���ʧ��", Toast.LENGTH_SHORT).show();
				break;
			case HandlerCode.CONNECTION_TIMEOUT:
				Log.v("�ռ���", "���ӳ�ʱ");
				toastTimeOut();
				break;
			}
		}
	};
	
	//�ļ���Դ����������
	@Override
	protected Dialog onCreateDialog(int id) {
		if(id==openfileDialogId){
			Map<String, Integer> images = new HashMap<String, Integer>();
			// ���漸�����ø��ļ����͵�ͼ�꣬ ��Ҫ���Ȱ�ͼ����ӵ���Դ�ļ���
			images.put(OpenFileDialog.sRoot, R.drawable.filedialog_root);	// ��Ŀ¼ͼ��
			images.put(OpenFileDialog.sParent, R.drawable.filedialog_folder_up);	//������һ���ͼ��
			images.put(OpenFileDialog.sFolder, R.drawable.filedialog_folder);	//�ļ���ͼ��
			images.put("wav", R.drawable.filedialog_wavfile);	//wav�ļ�ͼ��
			images.put(OpenFileDialog.sEmpty, R.drawable.filedialog_file2);
			Dialog dialog = OpenFileDialog.createDialog(1,id, this, "���ļ�", new CallbackBundle() {
				@Override
				public void callback(Bundle bundle) {
					filepath = bundle.getString("path");
					tv_filename.setText(StringUtils.Path2FileName(filepath)); // ���ļ�·����ʾ�ڱ�����
				}
			}, 
			"",
			images);
			return dialog;
		}
		if(id==copyfileDialogId){
			Map<String, Integer> images = new HashMap<String, Integer>();
			// ���漸�����ø��ļ����͵�ͼ�꣬ ��Ҫ���Ȱ�ͼ����ӵ���Դ�ļ���
			images.put(OpenFileDialog.sRoot, R.drawable.filedialog_root);	// ��Ŀ¼ͼ��
			images.put(OpenFileDialog.sParent, R.drawable.filedialog_folder_up);	//������һ���ͼ��
			images.put(OpenFileDialog.sFolder, R.drawable.filedialog_folder);	//�ļ���ͼ��
			Dialog dialog = OpenFileDialog.createDialog(0,id, this, "���ļ�", new CallbackBundle() {
				@Override
				public void callback(Bundle bundle) {
					filepath = bundle.getString("path");
					tv_filename.setText(filepath); // ���ļ�·����ʾ�ڱ�����
				}
			}, 
			"",
			images);
			return dialog;
		}
		return null;
	}
	/**
	 * ���ؽ��̶Ի���
	 */
	private void showDownloadDialog(boolean b){
		if(b)
			dialog=ProgressDialog.show(MessageRepliesActivity.this,"���ڷ���...","���Ժ�",true,true);//��ʾ���ؽ��̶Ի���
		else
			dialog.dismiss();//���ؽ��̶Ի�����ʧ
	}
	/**
	 * ��ʾ���س�ʱ
	 */
	private void toastTimeOut(){
		Toast.makeText(getApplicationContext(), R.string.timeout, Toast.LENGTH_LONG).show();
	}
	/**
	 * ������Ϣ
	 */
	private void sendMyMessage(){
		new Thread(sendMessages).start();
	}
	/**
	 * ������Ϣ
	 */
	private Runnable sendMessages = new Runnable() {
		public void run() {
			sendmessagecontent=getMessageContent();
			try {
				Log.v("�ظ���Ϣ", "��������");
				String url=LoginConfig.getLoginConfig().getServerip();
				String userid=LoginConfig.getLoginConfig().getUserid();
				String myname=LoginConfig.getLoginConfig().getMyname();
				String urlPath = "http://"+url+"/oa/ashx/Ioa.ashx?ot=3&uid="+userid+"&uname="+URLEncoder.encode(myname, "UTF-8");//����ip
				Log.v("�ظ���Ϣ��ַ", urlPath);
				// ���ӷ������ɹ�֮�󣬽�������
				String data = new HttpHelper(urlPath).doPostString(sendmessagecontent);
				Log.v("�ظ���Ϣ����ֵ",data); 
				if (data.equals("0")) {
					handlersendmessage.sendEmptyMessage(HandlerCode.SEND_MESSAGE_FAILURE);
				} 
				else if (data.equals("1")){
					handlersendmessage.sendEmptyMessage(HandlerCode.SEND_MESSAGE_SUCCESS);
				}
			} catch (Exception e) {
				e.printStackTrace();
				handlersendmessage.sendEmptyMessage(HandlerCode.CONNECTION_TIMEOUT);
			}
			//���ؽ��̶Ի�����ʧ
			showDownloadDialog(false);
		}
	};
	/**
	 * �����ļ�
	 */
	private void sendMyFile(){
		new Thread(sendFile).start();
	}
	/**
	 * �����ļ�
	 */
	private Runnable sendFile=new Runnable() {
		public void run() {
			try{
				File file=new File(filepath);
				String fname=StringUtils.TimeString()+"$"+StringUtils.Path2FileName(filepath);
				String url=LoginConfig.getLoginConfig().getServerip();
				String urlPath="http://"+url+"/oa/ashx/Ioa.ashx?ot=4&mid="+messageid+"&fn="+fname;
				Log.v("�ظ���Ϣ�����ļ�", urlPath);
				String data = new HttpHelper(urlPath).uploadFile(file);
				Log.v("�ظ���Ϣ���ͷ���ֵ",data); 
				if (data.equals("0")) {
					handlersendmessage.sendEmptyMessage(HandlerCode.SEND_FILE_FAILURE);
				} 
				else if (data.equals("1")){
					handlersendmessage.sendEmptyMessage(HandlerCode.SEND_FILE_SUCCESS);
				}
			}catch(Exception e){
				handlersendmessage.sendEmptyMessage(HandlerCode.CONNECTION_TIMEOUT);
			}
			showDownloadDialog(false);
		}
		
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_message_forward, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getTitle().toString().trim().equals("����")) {
			handlersendmessage.sendEmptyMessage(HandlerCode.SEND_MESSAGE_BEGIN);
			getMessageContent();
		} else if (item.getItemId() == android.R.id.home) {
			this.finish();
		}
		return super.onOptionsItemSelected(item);
	}
}
