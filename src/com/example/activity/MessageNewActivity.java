package com.example.activity;

import java.io.File;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import com.example.HandlerCode;
import com.example.StateCode;
import com.example.oa_index.R;
import com.example.beans.LoginConfig;
import com.example.beans.MyMessageBean;
import com.example.db.OADBHelper;
import com.example.fileexplorer.CallbackBundle;
import com.example.fileexplorer.OpenFileDialog;
import com.example.http.HttpHelper;
import com.example.mytree.ContactsActivity;
import com.example.utils.StringUtils;

import android.app.ActionBar;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
/**
 * ����Ϣ
 */
public class MessageNewActivity extends FinalActivity {
	private final static int openfileDialogId = 0;
	private final static int copyfileDialogId = 1;
	private String messageid="";//��Ϣid
	private String myname="";//�û���
	private String messagetitle="";//��Ϣ����
	private String messagecontent="";//��Ϣ����
	private String receivers="";//�ռ�������
	private String receiverids="";//�ռ���id
	private String hasfile="0";//���޸���
	private String filepath="";//����·��
	private String filename="";//������
	private String sendmessagecontent="";//������Ϣ����
    @ViewInject(id=R.id.et_messagereceiver)	EditText et_messagereceiver;//�ռ�����
    @ViewInject(id=R.id.bt_addreceiver,click="onClick_AddReceiver") ImageButton bt_addreceiver;//����ռ���
    @ViewInject(id=R.id.tv_messagesender) TextView tv_messagesender;//������
    @ViewInject(id=R.id.et_messagetitle) EditText et_messagetitle;//��Ϣ����
    @ViewInject(id=R.id.et_newcontent) EditText et_newcontent;//��Ϣ����
    @ViewInject(id=R.id.tv_filename) TextView tv_filename;//������
    @ViewInject(id=R.id.bt_searchfile,click="onClick_SearchFile") Button bt_searchfile;//��Ӹ���
    private ProgressDialog dialog=null;//���ͽ��̶Ի���
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_new);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initView();
    }
	@Override
	protected void onResume() {
		super.onResume();
		Log.v("����Ϣ��ϵ��1",receiverids);
		//����ռ���
		et_messagereceiver.setText(receivers);
	}
	private void initView(){
		ActionBar actionbar=getActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
		
		myname=LoginConfig.getLoginConfig().getMyname();
		tv_messagesender.setText(myname);
	}
	/**
	 * ����ռ���
	 * @param v
	 */
	public void onClick_AddReceiver(View v){
		Intent intent=new Intent();
		intent.setClass(getApplicationContext(), ContactsActivity.class);
		startActivityForResult(intent, 1000);
	}
	/**
	 * ���ļ���Դ����
	 * @param v
	 */
	public void onClick_SearchFile(View v){
		showDialog(openfileDialogId);
	}
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000 && resultCode == 1001)
        {
        	receivers = data.getStringExtra("resultnames");
        	receiverids=data.getStringExtra("receiverids");
            Log.v("����Ϣ��ϵ��2",receiverids);
            et_messagereceiver.setText(receivers);
        }
    }
	
	private Handler handlerSendMessage=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			int whatVal = msg.what;
			switch (whatVal) {
			case HandlerCode.SEND_MESSAGE_BEGIN:
				showDownloadDialog(true);
				sendMyMessage();
				Toast.makeText(getApplicationContext(), "������Ϣ", Toast.LENGTH_SHORT).show();
				break;
			case HandlerCode.SEND_MESSAGE_SUCCESS:
				Toast.makeText(getApplicationContext(), "������Ϣ�ɹ�", Toast.LENGTH_SHORT).show();
				saveMessage(StateCode.MESSAGE_TYPE_SEND);//���ѷ���Ϣ
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
			case HandlerCode.SAVE_MESSAGE_DRAFT:
				saveMessage(StateCode.MESSAGE_TYPE_DRAFT);//��ݸ�
				break;
			}
		}
	};
	/**
	 * ��ȡ��Ϣ����
	 * @return String
	 */
	private String getMessageContent(){
		String contnet="";//���͵���Ϣ����
		hasfile = "0";//�Ƿ��и���
		receivers=et_messagereceiver.getText().toString().trim();//�ռ�������
		messagetitle=et_messagetitle.getText().toString().trim();//��Ϣ����
		messagecontent=et_newcontent.getText().toString().trim();//��Ϣ����
		//���˹ؼ��֡�|��
		messagecontent=messagecontent.replaceAll("\\|", "");
		filename=tv_filename.getText().toString().trim();//������
		if(!filename.equals(""))//����и����� hasfile ��һ
			hasfile="1";
		messageid=StringUtils.GenerateGUID(11);//����32Ϊ����Ϣid
		// �ռ���ID|�ռ�������|����|����|���޸���|��ϢID
		contnet=receiverids+"|"+receivers+"|"+messagetitle+"|"+messagecontent+"|"+hasfile+"|"+messageid;
		return contnet;
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
				Log.v("�½���Ϣ", "��������");
				String url=LoginConfig.getLoginConfig().getServerip();
				String userid=LoginConfig.getLoginConfig().getUserid();
				String myname=LoginConfig.getLoginConfig().getMyname();
				String urlPath = "http://"+url+"/oa/ashx/Ioa.ashx?ot=3&uid="+userid+"&uname="+URLEncoder.encode(myname, "UTF-8");//����ip
				Log.v("�½���Ϣ���͵�ַ", urlPath);
				// ���ӷ������ɹ�֮�󣬽�������
				String data = new HttpHelper(urlPath).doPostString(sendmessagecontent);
				Log.v("�½���Ϣ���ͷ���ֵ",data); 
				if (data.equals("0")) {
					handlerSendMessage.sendEmptyMessage(HandlerCode.SEND_MESSAGE_FAILURE);
				} 
				else if (data.equals("1")){
					handlerSendMessage.sendEmptyMessage(HandlerCode.SEND_MESSAGE_SUCCESS);
				}
			} catch (Exception e) {
				e.printStackTrace();
				handlerSendMessage.sendEmptyMessage(HandlerCode.CONNECTION_TIMEOUT);
			}
			//���ͽ��̶Ի�����ʧ
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
				Log.v("�½���Ϣ�����ļ�", urlPath);
				String data = new HttpHelper(urlPath).uploadFile(file);
				Log.v("�½���Ϣ���ͷ���ֵ",data); 
				if (data.equals("0")) {
					handlerSendMessage.sendEmptyMessage(HandlerCode.SEND_FILE_FAILURE);
				} 
				else if (data.equals("1")){
					handlerSendMessage.sendEmptyMessage(HandlerCode.SEND_FILE_SUCCESS);
				}
			}catch(Exception e){
				handlerSendMessage.sendEmptyMessage(HandlerCode.CONNECTION_TIMEOUT);
			}
			showDownloadDialog(false);
		}
		
	};
	/**
	 * ����ݸ�
	 * @param mmb
	 */
	private void saveMessage(String type){
		getMessageContent();
		MyMessageBean mmb=new MyMessageBean(messageid, "", "", messagetitle, messagecontent, hasfile, filename, "0", type);
		Log.v("��ݸ壺", "mmsid="+messageid+"receivers="+""); 
		OADBHelper.saveMessage(mmb, getApplicationContext());
	}
	/**
	 * ���ؽ��̶Ի���
	 */
	private void showDownloadDialog(boolean b){
		if(b)
			dialog=ProgressDialog.show(MessageNewActivity.this,"���ڷ���...","���Ժ�",true,true);//��ʾ���ؽ��̶Ի���
		else
			dialog.dismiss();//���ؽ��̶Ի�����ʧ
	}
	/**
	 * ��ʾ���س�ʱ
	 */
	private void toastTimeOut(){
		Toast.makeText(getApplicationContext(), R.string.timeout, Toast.LENGTH_LONG).show();
	}
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_message_new, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getTitle().toString().trim().equals("����")) {
			handlerSendMessage.sendEmptyMessage(HandlerCode.SEND_MESSAGE_BEGIN);
		}
		else if(item.getItemId()==R.id.action_save){
			handlerSendMessage.sendEmptyMessage(HandlerCode.SAVE_MESSAGE_DRAFT);
		}
		else if (item.getItemId()==android.R.id.home){
			this.finish();
		}
		return super.onOptionsItemSelected(item);
	}
}
