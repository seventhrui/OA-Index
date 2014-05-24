package com.example.activity;

import java.io.File;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
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
import net.tsz.afinal.FinalDb;
import net.tsz.afinal.annotation.view.ViewInject;
/**
 * 信息草稿
 */
public class MessageDraftShowActivity extends FinalActivity {
	private final static int openfileDialogId = 0;
	private final static int copyfileDialogId = 1;
	private String messageid="";//信息id
	private String myname="";//用户名
	private String messagetitle="";//信息主题
	private String messagecontent="";//信息内容
	private String receivers="";//收件人名称
	private String receiverids="";//收件人id
	private String hasfile="0";//有无附件
	private String filepath="";//附件路径
	private String filename="";//附件名
	private String sendmessagecontent="";//发送消息内容
    @ViewInject(id=R.id.et_messagereceiver)	EditText et_messagereceiver;//收件人名
    @ViewInject(id=R.id.bt_addreceiver,click="onClick_AddReceiver") ImageButton bt_addreceiver;//添加收件人
    @ViewInject(id=R.id.tv_messagesender) TextView tv_messagesender;//发件人
    @ViewInject(id=R.id.et_messagetitle) EditText et_messagetitle;//信息标题
    @ViewInject(id=R.id.et_newcontent) EditText et_newcontent;//信息内容
    @ViewInject(id=R.id.tv_filename) TextView tv_filename;//附件名
    @ViewInject(id=R.id.bt_searchfile,click="onClick_SearchFile") Button bt_searchfile;//添加附件
    private ProgressDialog dialog=null;//发送进程对话框
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
		Log.v("信息草稿联系人1",receiverids);
		//填充收件人
		et_messagereceiver.setText(receivers);
	}
	private void initView(){
		ActionBar actionbar=getActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
		
		myname=LoginConfig.getLoginConfig().getMyname();
		tv_messagesender.setText(myname);
		showMessage();
	}
	/**
     * 在控件中添加信息详情
     */
    private void showMessage(){
    	messageid=getIntent().getStringExtra("messageid");
    	FinalDb db = FinalDb.create(this);
    	List<MyMessageBean> messlist=db.findAllByWhere(MyMessageBean.class, "message_id='"+messageid+"'");
    	for(int i=0;i<messlist.size();i++){
    		et_messagetitle.setText(messlist.get(i).getMessage_title());
			if(messlist.get(i).getMessage_sender() != null){
				tv_messagesender.setText(messlist.get(i).getMessage_sender().trim());
			}
			else
				tv_messagesender.setText("");
    		if(messlist.get(i).getMessage_hasfile().equals("0"))
    			tv_filename.setVisibility(View.INVISIBLE);
			else{
				filename=messlist.get(i).getMessage_filename();
				tv_filename.setText(filename);
			}
			messagecontent=messlist.get(i).getMessage_content();
			if(messagecontent.isEmpty())
				messagecontent=" ";
			et_newcontent.setText(messagecontent);
    	}
    }
	/**
	 * 添加收件人
	 * @param v
	 */
	public void onClick_AddReceiver(View v){
		Intent intent=new Intent();
		intent.setClass(getApplicationContext(), ContactsActivity.class);
		startActivityForResult(intent, 1000);
	}
	/**
	 * 打开文件资源管理
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
            Log.v("信息草稿联系人2",receiverids);
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
				Toast.makeText(getApplicationContext(), "发送信息", Toast.LENGTH_SHORT).show();
				break;
			case HandlerCode.SEND_MESSAGE_SUCCESS:
				Toast.makeText(getApplicationContext(), "发送信息成功", Toast.LENGTH_SHORT).show();
				saveMessage(StateCode.MESSAGE_TYPE_SEND);//存已发信息
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
				Log.v("信息草稿", "连接超时");
				toastTimeOut();
				break;
			case HandlerCode.DELETE:
				deleteMessage();//删除草稿
				break;
			}
		}
	};
	/**
	 * 删除草稿
	 * @param mid
	 */
	private void deleteMessage(){
		OADBHelper.deleteMessage(messageid, getApplicationContext());
		Toast.makeText(getApplicationContext(), "删除成功", Toast.LENGTH_SHORT).show();
		this.finish();
	}
	/**
	 * 获取信息内容
	 * @return String
	 */
	private String getMessageContent(){
		String contnet="";//发送的信息内容
		hasfile = "0";//是否有附件
		receivers=et_messagereceiver.getText().toString().trim();//收件人名称
		messagetitle=et_messagetitle.getText().toString().trim();//信息标题
		messagecontent=et_newcontent.getText().toString().trim();//信息内容
		//过滤关键字“|”
		messagecontent=messagecontent.replaceAll("\\|", "");
		filename=tv_filename.getText().toString().trim();//附件名
		if(!filename.equals(""))//如果有附件则将 hasfile 置一
			hasfile="1";
		//messageid=StringUtils.GenerateGUID(11);//生成32为的信息id
		// 收件人ID|收件人名字|标题|内容|有无附件|消息ID
		contnet=receiverids+"|"+receivers+"|"+messagetitle+"|"+messagecontent+"|"+hasfile+"|"+messageid;
		return contnet;
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
				Log.v("信息草稿", "发送数据");
				String url=LoginConfig.getLoginConfig().getServerip();
				String userid=LoginConfig.getLoginConfig().getUserid();
				String myname=LoginConfig.getLoginConfig().getMyname();
				String urlPath = "http://"+url+"/oa/ashx/Ioa.ashx?ot=3&uid="+userid+"&uname="+URLEncoder.encode(myname, "UTF-8");//内网ip
				Log.v("信息草稿发送地址", urlPath);
				// 连接服务器成功之后，解析数据
				String data = new HttpHelper(urlPath).doPostString(sendmessagecontent);
				Log.v("信息草稿发送返回值",data); 
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
			//发送进程对话框消失
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
				Log.v("信息草稿发送文件", urlPath);
				String data = new HttpHelper(urlPath).uploadFile(file);
				Log.v("信息草稿发送返回值",data); 
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
	 * 保存信息
	 * @param mmb
	 */
	private void saveMessage(String type){
		getMessageContent();
		MyMessageBean mmb=new MyMessageBean(messageid, LoginConfig.getLoginConfig().getMyname(), "", messagetitle, messagecontent, hasfile, filename, "0", type);
		Log.v("草稿：", "mmsid="+messageid+"receivers="+""); 
		OADBHelper.saveMessage(mmb, getApplicationContext());
	}
	/**
	 * 下载进程对话框
	 */
	private void showDownloadDialog(boolean b){
		if(b)
			dialog=ProgressDialog.show(MessageDraftShowActivity.this,"正在发送...","请稍后",true,true);//显示下载进程对话框
		else
			dialog.dismiss();//下载进程对话框消失
	}
	/**
	 * 提示下载超时
	 */
	private void toastTimeOut(){
		Toast.makeText(getApplicationContext(), R.string.timeout, Toast.LENGTH_LONG).show();
	}
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_message_deaftshow, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getTitle().toString().trim().equals("发送")) {
			handlerSendMessage.sendEmptyMessage(HandlerCode.SEND_MESSAGE_BEGIN);
		}
		else if (item.getTitle().toString().trim().equals("删除")) {
			handlerSendMessage.sendEmptyMessage(HandlerCode.DELETE);
		}
		else if (item.getItemId()==android.R.id.home){
			this.finish();
		}
		return super.onOptionsItemSelected(item);
	}
}
