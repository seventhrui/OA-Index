package com.example.activity;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;

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

import com.example.beans.LoginConfig;
import com.example.beans.MyNoticeBean;
import com.example.db.OADBHelper;
import com.example.oa_index.R;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalDb;
import net.tsz.afinal.annotation.view.ViewInject;

/**
 * ֪ͨͨ������
 *
 */
public class NoticeShowActivity extends FinalActivity {
	@ViewInject(id=R.id.tv_noticetitle) TextView tv_noticetitle;
    @ViewInject(id=R.id.tv_noticesender) TextView tv_noticesender;
    @ViewInject(id=R.id.tv_noticetime) TextView tv_noticetime;
    @ViewInject(id=R.id.wv_noticecontent) WebView wv_noticecontent;
    @ViewInject(id=R.id.bt_noticefile,click="onClick_DownloadFile") Button bt_noticefile;
    private DownloadManager mgr = null;
    private long lastDownload = -1L;
    private String fileurl="";
    private String filepath="";
    private String noticeid="";//֪ͨid
    private String noticecontent="";//֪ͨ����
    private String filename="";//������
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_show);
        initView();
        showNotice();
    }
    private void initView(){
    	ActionBar actionbar=getActionBar();
    	actionbar.setDisplayHomeAsUpEnabled(true);
    	String noticeid=getIntent().getStringExtra("noticeid");
    	tv_noticetime.setText(noticeid);
    	getOverflowMenu();
    }
    /**
     * �ڿؼ������֪ͨ����
     */
    private void showNotice(){
    	noticeid=getIntent().getStringExtra("noticeid");
    	FinalDb db = FinalDb.create(this);
    	List<MyNoticeBean> noticelist=db.findAllByWhere(MyNoticeBean.class, "notice_id='"+noticeid+"'");
    	for(int i=0;i<noticelist.size();i++){
    		tv_noticetitle.setText(noticelist.get(i).getNotice_title());
    		tv_noticesender.setText(noticelist.get(i).getNotice_sender());
    		tv_noticetime.setText(noticelist.get(i).getNotice_sendtime());
			if(noticelist.get(i).getNotice_hasfile().equals("0"))
				bt_noticefile.setVisibility(View.INVISIBLE);
			else{
				filename=noticelist.get(i).getNotice_filename();
				bt_noticefile.setText(filename);
			}
			noticecontent=noticelist.get(i).getNotice_content();
			if(noticecontent.isEmpty())
				noticecontent=" ";
			wv_noticecontent.loadDataWithBaseURL(null, noticecontent, "text/html", "utf-8", null);
    	}
    	//�������ϢΪδ��������Ϣ״̬Ϊ0�����鿴����Ϣ����Ϣ�޸�Ϊ�Ѷ�
    	if(noticelist.size()!=0){
	    	MyNoticeBean mn=noticelist.get(0);
	    	if(mn.getNotice_state().equals("0")){
		    	mn.setNotice_state("1");
		    	Log.v("֪ͨͨ�����״̬","aaa");
		    	db.update(mn);
	    	}
    	}
    }
    public void onClick_DownloadFile(View v){
    	startDownload(filename);
    }
    /**
     * ���ظ���
     * @param fname
     */
    public void startDownload(String fname) {
		mgr = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
		String url=LoginConfig.getLoginConfig().getServerip();
		fileurl="http://"+url+"/OA/Messages/MessageAttachment/"+fname;
		filepath=Environment.getExternalStorageDirectory().toString()+"/OA/"+fname;
		if(new File(filepath).exists()){
			Intent intent = new Intent(); 
			intent.setAction(android.content.Intent.ACTION_VIEW); 
			File file = new File(Environment.getExternalStorageDirectory().toString()+"/OA/"+fname); 
			intent.setDataAndType(Uri.fromFile(file), "application/*"); 
			startActivity(intent);
		}
		else{
			Toast.makeText(getApplicationContext(), "�ļ���������Ҫ����", Toast.LENGTH_SHORT).show();
			Uri uri = Uri.parse(fileurl);
			Environment.getExternalStoragePublicDirectory("/OA/").mkdirs();
			Request dwreq = new DownloadManager.Request(uri);
			dwreq.setTitle(fname);//�������ر���
			dwreq.setDescription(fname);//����������Ϣ
			dwreq.setDestinationInExternalPublicDir("/OA/",fname);
			dwreq.setNotificationVisibility(0);
			dwreq.setVisibleInDownloadsUi(true);
			dwreq.setShowRunningNotification(true);//�Ƿ���ʾ���ؽ���
			
			Log.v("��Ϣ�����ļ�·����", Environment.getExternalStorageDirectory().toString()+"/OA/"+fname);
			
			lastDownload = mgr.enqueue(dwreq);
		}
	}
    /**
	 * ɾ��֪ͨ
	 * @param mid
	 */
	private void deleteMessage(){
		OADBHelper.deleteNotice(noticeid, getApplicationContext());
		Toast.makeText(getApplicationContext(), "ɾ���ɹ�", Toast.LENGTH_SHORT).show();
		this.finish();
	}
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
		getMenuInflater().inflate(R.menu.menu_notice_show, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getTitle().toString().trim().equals("ɾ��")) {
			deleteMessage();
			this.finish();
		}
		//����
		else if (item.getItemId()==android.R.id.home){
			this.finish();
		}
		return super.onOptionsItemSelected(item);
	}
}
