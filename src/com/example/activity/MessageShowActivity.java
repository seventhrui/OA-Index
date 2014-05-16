package com.example.activity;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;

import com.example.oa_index.R;
import com.example.beans.MyMessageBean;

import android.app.ActionBar;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalDb;
import net.tsz.afinal.annotation.view.ViewInject;
/**
 * 信息详情
 */
public class MessageShowActivity extends FinalActivity {
	private String myname="";//用户名
	private String messagecontent="";//信息内容
	
	@ViewInject(id=R.id.bt_messagefile,click="onClick_DownloadFile") Button bt_messagefile;
    @ViewInject(id=R.id.tv_messagetitle) TextView tv_messagetitle;
    @ViewInject(id=R.id.tv_messagesender) TextView tv_messagesender;
    @ViewInject(id=R.id.tv_messagetime) TextView tv_messagetime;
    @ViewInject(id=R.id.wv_messagecontent) WebView wv_messagecontent;
    private DownloadManager mgr = null;
    private long lastDownload = -1L;
    private String fileurl="";
    private String filename="";
	private String filepath="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_show);
        initView();
        showMessage();
    }
    private void initView(){
    	myname=getIntent().getStringExtra("myname");
    	ActionBar actionbar=getActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        getOverflowMenu();
    }
    /**
     * 在控件中添加信息详情
     */
    private void showMessage(){
    	String messageid=getIntent().getStringExtra("messageid");
    	FinalDb db = FinalDb.create(this);
    	List<MyMessageBean> messlist=db.findAllByWhere(MyMessageBean.class, "message_id='"+messageid+"'");
    	for(int i=0;i<messlist.size();i++){
    		tv_messagetitle.setText(messlist.get(i).getMessage_title());
			tv_messagesender.setText(messlist.get(i).getMessage_sender());
			tv_messagetime.setText(messlist.get(i).getMessage_sendtime());
			if(messlist.get(i).getMessage_hasfile().equals("0"))
				bt_messagefile.setVisibility(View.INVISIBLE);
			else{
				filename=messlist.get(i).getMessage_filename();
				bt_messagefile.setText(filename);
			}
			messagecontent=messlist.get(i).getMessage_content();
			if(messagecontent.isEmpty())
				messagecontent=" ";
			wv_messagecontent.loadDataWithBaseURL(null, messagecontent, "text/html", "utf-8", null);
    	}
    	//如果该信息为未读（即信息状态为0），查看完信息后将信息修改为已读
    	MyMessageBean mm=messlist.get(0);
    	if(mm.getMessage_state().equals("0")){
	    	mm.setMessage_state("1");
	    	Log.v("收件箱更新状态","aaa");
	    	db.update(mm);
    	}
    	/*//从InboxListAvtivity.messagelist中获取信息列表
    	String messageid=getIntent().getStringExtra("messageid");
    	List<MyMessage> messlist =InboxListAvtivity.messagelist;
    	for(MyMessage m:messlist){
    		if(messageid.trim().equals(m.getMessage_id())){
    			Log.v("选中的id", messageid);
    			tv_messagetitle.setText(m.getMessage_title());
    			tv_messagesender.setText(m.getMessage_sender());
    			tv_messagetime.setText(m.getMessage_sendtime());
    			if(m.getMessage_hasfile().equals("0"))
    				bt_messagefile.setVisibility(View.INVISIBLE);
    			else{
    				filename=m.getMessage_filename();
    				bt_messagefile.setText(filename);
    			}
    			String data=m.getMessage_content();
    			if(data.isEmpty())
    				data=" ";
    			wv_messagecontent.loadDataWithBaseURL(null, data, "text/html", "utf-8", null);
    		}
    	}
    	*/
    }
    /**
     * 下载附件
     * @param v
     */
    public void onClick_DownloadFile(View v){
    	startDownload(filename);
    }
    /**
     * 下载附件
     * @param fname
     */
    public void startDownload(String fname) {
		mgr = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
		fileurl="http://192.168.0.143:32768/OA/Messages/MessageAttachment/"+fname;
		filepath=Environment.getExternalStorageDirectory().toString()+"/OA/"+fname;
		if(new File(filepath).exists()){
			Intent intent = new Intent(); 
			intent.setAction(android.content.Intent.ACTION_VIEW); 
			File file = new File(Environment.getExternalStorageDirectory().toString()+"/OA/"+fname); 
			intent.setDataAndType(Uri.fromFile(file), "application/*"); 
			startActivity(intent);
		}
		else{
			Toast.makeText(getApplicationContext(), "文件不存在需要下载", Toast.LENGTH_SHORT).show();
			Uri uri = Uri.parse(fileurl);
			Environment.getExternalStoragePublicDirectory("/OA/").mkdirs();
			Request dwreq = new DownloadManager.Request(uri);
			dwreq.setTitle(fname);//设置下载标题
			dwreq.setDescription(fname);//设置描述信息
			dwreq.setDestinationInExternalPublicDir("/OA/",fname);
			dwreq.setNotificationVisibility(0);
			dwreq.setVisibleInDownloadsUi(true);
			dwreq.setShowRunningNotification(true);//是否显示下载进度
			
			Log.v("信息详情文件路径：", Environment.getExternalStorageDirectory().toString()+"/OA/"+fname);
			
			lastDownload = mgr.enqueue(dwreq);
		}
	}
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
		getMenuInflater().inflate(R.menu.menu_message_show, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		//回复信息
		if (item.getTitle().toString().trim().equals("回复")) {
			Toast.makeText(getApplicationContext(), item.getTitle()+"", Toast.LENGTH_SHORT).show();
			Intent intent=new Intent();
			intent.setClass(getApplicationContext(), MessageRepliesActivity.class);
			Bundle bundle=new Bundle();
			bundle.putString("myname", myname);
			bundle.putString("messagesender", tv_messagesender.getText().toString().trim());
			bundle.putString("messagetitle", tv_messagetitle.getText().toString().trim());
			bundle.putString("messagecontent", messagecontent);
			intent.putExtras(bundle);
			startActivity(intent);
		}
		//转发信息
		else if (item.getTitle().toString().trim().equals("转发")) {
			Toast.makeText(getApplicationContext(), item.getTitle()+"", Toast.LENGTH_SHORT).show();
			Intent intent=new Intent();
			intent.setClass(getApplicationContext(), MessageForwardActivity.class);
			Bundle bundle=new Bundle();
			bundle.putString("myname", myname);
			bundle.putString("messagetitle", tv_messagetitle.getText().toString().trim());
			bundle.putString("messagecontent", messagecontent);
			bundle.putString("messagefile", filename);
			intent.putExtras(bundle);
			startActivity(intent);
		}
		//删除信息
		else if (item.getTitle().toString().trim().equals("删除")) {
			Toast.makeText(getApplicationContext(), item.getTitle()+"", Toast.LENGTH_SHORT).show();
		}
		//返回
		else if (item.getItemId()==android.R.id.home){
			this.finish();
		}
		return super.onOptionsItemSelected(item);
	}

}
