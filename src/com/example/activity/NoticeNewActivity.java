package com.example.activity;

import java.util.HashMap;
import java.util.Map;

import com.example.oa_index.R;
import com.example.fileexplorer.CallbackBundle;
import com.example.fileexplorer.OpenFileDialog;
import com.example.mytree.ContactsActivity;
import com.example.utils.StringUtils;

import android.app.ActionBar;
import android.app.Dialog;
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
 * �½�֪ͨͨ��
 */
public class NoticeNewActivity extends FinalActivity {
	private final static int openfileDialogId = 0;
	private final static int copyfileDialogId = 1;
	private final static int SEND_NOTICE=0;//����֪ͨ
	private String myname="";//�û���
	private String noticetitle="";//��Ϣ����
	private String noticecontent="";//��Ϣ����
	private String receivers="";//�ռ���
	private String filepath="";//����·��
    @ViewInject(id=R.id.tv_messagereceiver) TextView tv_noticereceiver;
    @ViewInject(id=R.id.et_messagereceiver)	EditText et_noticereceiver;
    @ViewInject(id=R.id.bt_addreceiver,click="onClick_AddReceiver") ImageButton bt_addreceiver;
    @ViewInject(id=R.id.tv_messagesender) TextView tv_noticesender;
    @ViewInject(id=R.id.et_messagetitle) EditText et_noticetitle;
    @ViewInject(id=R.id.et_newcontent) EditText et_noticenewcontent;
    @ViewInject(id=R.id.tv_filename) TextView tv_filename;
    @ViewInject(id=R.id.bt_searchfile,click="onClick_SearchFile") Button bt_searchfile;
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
		Log.v("��֪ͨ��ϵ��1",receivers);
		//����ռ���
		et_noticereceiver.setText(receivers);
	}
	private void initView(){
		ActionBar actionbar=getActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
		
		myname=getIntent().getStringExtra("myname");
		tv_noticesender.setText(myname);
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
        	receivers = data.getStringExtra("result");
            Log.v("��֪ͨ��ϵ��2",receivers);
            et_noticereceiver.setText(receivers);
        }
    }
	
	private Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			int whatVal = msg.what;
			switch (whatVal) {
			case SEND_NOTICE:
				Toast.makeText(getApplicationContext(), "����֪ͨ", Toast.LENGTH_SHORT).show();
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_message_forward, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getTitle().toString().trim().equals("����")) {
			handler.sendEmptyMessage(SEND_NOTICE);
		}
		else if (item.getItemId()==android.R.id.home){
			this.finish();
		}
		return super.onOptionsItemSelected(item);
	}
}
