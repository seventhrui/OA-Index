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
 * 信息回复
 */
public class MessageRepliesActivity extends FinalActivity {
	private final static int openfileDialogId = 0;
	private final static int copyfileDialogId = 1;
	
	private String originalsender = "";// 收件人（原发件人）
	private String myname = "";// 用户名
	private String messagetitle = "";// 原信息主题
	private String messageid="";//信息id
	private String receiver="";//收件人
	private String receiverid="";//收件人id
	private String messagecontent;//回复内容
	private String originalcontent="";//原内容
	private String sendmessagecontent="";//需要发送的内容
	private String filepath="";//附件路径
	private String filename="";//附件名

	@ViewInject(id = R.id.tv_messagereceiver) TextView tv_messagereceiver;//收件人名
	@ViewInject(id = R.id.tv_messagesender)	TextView tv_messagesender;//发件人名
	@ViewInject(id = R.id.tv_messagetitle) TextView tv_messagetitle;//信息标题
	@ViewInject(id = R.id.et_messagecontent) EditText et_messagecontent;//回复内容
	@ViewInject(id = R.id.wv_originalcontent) WebView wv_originalcontent;//原内容
	@ViewInject(id = R.id.tv_filename) TextView tv_filename;//附件名
	@ViewInject(id = R.id.bt_searchfile,click="onClick_SearchFile") Button bt_searchfile;//添加附件
	private ProgressDialog dialog=null;//发送进程对话框
	
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
		tv_messagetitle.setText("回复：" + messagetitle);
		if(originalcontent.isEmpty())
			originalcontent=" ";
		wv_originalcontent.loadDataWithBaseURL(null, originalcontent, "text/html", "utf-8", null);
	}
	/**
	 * 打开文件资源管理
	 * @param v
	 */
	public void onClick_SearchFile(View v){
		showDialog(openfileDialogId);
	}
	private String getMessageContent(){
		String contnet="";//发送的信息内容
		String hasfile="0";//是否有附件
		receiver=tv_messagereceiver.getText().toString().trim();//收件人名称
		messagetitle=tv_messagetitle.getText().toString().trim();//信息标题
		messagecontent=et_messagecontent.getText().toString().trim();//信息内容
		filename=tv_filename.getText().toString().trim();//附件名
		if(!filename.equals(""))//如果有附件则将 hasfile 置一
			hasfile="1";
		messageid=StringUtils.GenerateGUID(11);//生成32为的信息id
		// 收件人ID|收件人名字|标题|内容|附件|消息ID
		contnet=receiverid+"|"+receiver+"|"+messagetitle+"|"+messagecontent+"\n"+originalcontent+"|"+hasfile+"|"+messageid;
		Log.v("信息回复内容",contnet);
		return contnet;
	}
	private Handler handlersendmessage = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			int whatVal = msg.what;
			switch (whatVal) {
			case HandlerCode.SEND_MESSAGE_BEGIN:
				Toast.makeText(getApplicationContext(), "发送信息", Toast.LENGTH_SHORT).show();
				showDownloadDialog(true);
				sendMyMessage();
				break;
			case HandlerCode.SEND_MESSAGE_SUCCESS:
				Toast.makeText(getApplicationContext(), "发送信息成功", Toast.LENGTH_SHORT).show();
				if(!filename.equals("")){
					showDownloadDialog(true);
					sendMyFile();//发送附件
				}
				break;
			case HandlerCode.SEND_MESSAGE_FAILURE:
				Toast.makeText(getApplicationContext(), "发送信息失败", Toast.LENGTH_SHORT).show();
				break;
			case HandlerCode.SEND_FILE_SUCCESS:
				Toast.makeText(getApplicationContext(), "发送附件成功", Toast.LENGTH_SHORT).show();
				break;
			case HandlerCode.SEND_FILE_FAILURE:
				Toast.makeText(getApplicationContext(), "发送附件失败", Toast.LENGTH_SHORT).show();
				break;
			case HandlerCode.CONNECTION_TIMEOUT:
				Log.v("收件箱", "连接超时");
				toastTimeOut();
				break;
			}
		}
	};
	
	//文件资源管理器窗口
	@Override
	protected Dialog onCreateDialog(int id) {
		if(id==openfileDialogId){
			Map<String, Integer> images = new HashMap<String, Integer>();
			// 下面几句设置各文件类型的图标， 需要你先把图标添加到资源文件夹
			images.put(OpenFileDialog.sRoot, R.drawable.filedialog_root);	// 根目录图标
			images.put(OpenFileDialog.sParent, R.drawable.filedialog_folder_up);	//返回上一层的图标
			images.put(OpenFileDialog.sFolder, R.drawable.filedialog_folder);	//文件夹图标
			images.put("wav", R.drawable.filedialog_wavfile);	//wav文件图标
			images.put(OpenFileDialog.sEmpty, R.drawable.filedialog_file2);
			Dialog dialog = OpenFileDialog.createDialog(1,id, this, "打开文件", new CallbackBundle() {
				@Override
				public void callback(Bundle bundle) {
					filepath = bundle.getString("path");
					tv_filename.setText(StringUtils.Path2FileName(filepath)); // 把文件路径显示在标题上
				}
			}, 
			"",
			images);
			return dialog;
		}
		if(id==copyfileDialogId){
			Map<String, Integer> images = new HashMap<String, Integer>();
			// 下面几句设置各文件类型的图标， 需要你先把图标添加到资源文件夹
			images.put(OpenFileDialog.sRoot, R.drawable.filedialog_root);	// 根目录图标
			images.put(OpenFileDialog.sParent, R.drawable.filedialog_folder_up);	//返回上一层的图标
			images.put(OpenFileDialog.sFolder, R.drawable.filedialog_folder);	//文件夹图标
			Dialog dialog = OpenFileDialog.createDialog(0,id, this, "打开文件", new CallbackBundle() {
				@Override
				public void callback(Bundle bundle) {
					filepath = bundle.getString("path");
					tv_filename.setText(filepath); // 把文件路径显示在标题上
				}
			}, 
			"",
			images);
			return dialog;
		}
		return null;
	}
	/**
	 * 下载进程对话框
	 */
	private void showDownloadDialog(boolean b){
		if(b)
			dialog=ProgressDialog.show(MessageRepliesActivity.this,"正在发送...","请稍后",true,true);//显示下载进程对话框
		else
			dialog.dismiss();//下载进程对话框消失
	}
	/**
	 * 提示下载超时
	 */
	private void toastTimeOut(){
		Toast.makeText(getApplicationContext(), R.string.timeout, Toast.LENGTH_LONG).show();
	}
	/**
	 * 发送信息
	 */
	private void sendMyMessage(){
		new Thread(sendMessages).start();
	}
	/**
	 * 发送消息
	 */
	private Runnable sendMessages = new Runnable() {
		public void run() {
			sendmessagecontent=getMessageContent();
			try {
				Log.v("回复信息", "发送数据");
				String url=LoginConfig.getLoginConfig().getServerip();
				String userid=LoginConfig.getLoginConfig().getUserid();
				String myname=LoginConfig.getLoginConfig().getMyname();
				String urlPath = "http://"+url+"/oa/ashx/Ioa.ashx?ot=3&uid="+userid+"&uname="+URLEncoder.encode(myname, "UTF-8");//内网ip
				Log.v("回复信息地址", urlPath);
				// 连接服务器成功之后，解析数据
				String data = new HttpHelper(urlPath).doPostString(sendmessagecontent);
				Log.v("回复信息返回值",data); 
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
			//下载进程对话框消失
			showDownloadDialog(false);
		}
	};
	/**
	 * 发送文件
	 */
	private void sendMyFile(){
		new Thread(sendFile).start();
	}
	/**
	 * 发送文件
	 */
	private Runnable sendFile=new Runnable() {
		public void run() {
			try{
				File file=new File(filepath);
				String fname=StringUtils.TimeString()+"$"+StringUtils.Path2FileName(filepath);
				String url=LoginConfig.getLoginConfig().getServerip();
				String urlPath="http://"+url+"/oa/ashx/Ioa.ashx?ot=4&mid="+messageid+"&fn="+fname;
				Log.v("回复信息发送文件", urlPath);
				String data = new HttpHelper(urlPath).uploadFile(file);
				Log.v("回复信息发送返回值",data); 
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
		if (item.getTitle().toString().trim().equals("发送")) {
			handlersendmessage.sendEmptyMessage(HandlerCode.SEND_MESSAGE_BEGIN);
			getMessageContent();
		} else if (item.getItemId() == android.R.id.home) {
			this.finish();
		}
		return super.onOptionsItemSelected(item);
	}
}
