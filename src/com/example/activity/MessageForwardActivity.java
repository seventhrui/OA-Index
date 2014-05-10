package com.example.activity;

import com.example.mytree.ContactsActivity;
import com.example.oa_index.R;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
/**
 * ��Ϣת��
 */
public class MessageForwardActivity extends FinalActivity {
	private final static int SEND_MESSAGE=0;//������Ϣ
	private String myname="";
	private String messagetitle="";
	private String originalcontent="";
	private String receivers="";
    @ViewInject(id=R.id.tv_messagereceiver) TextView tv_messagereceiver;
    @ViewInject(id=R.id.et_messagereceiver)	public static EditText et_messagereceiver;
    @ViewInject(id=R.id.bt_addreceiver,click="onClick_AddReceiver") ImageButton bt_addreceiver;
    @ViewInject(id=R.id.tv_messagesender) TextView tv_messagesender;
    @ViewInject(id=R.id.tv_messagetitle) TextView tv_messagetitle;
    @ViewInject(id=R.id.wv_originalcontent) WebView wv_originalcontent;
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_forward);
        initView();
    }
	@Override
	protected void onResume() {
		super.onResume();
		Log.v("��Ϣת����ϵ��",receivers);
		et_messagereceiver.setText(receivers);
	}
	private void initView(){
		ActionBar actionbar=getActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
		
		myname=getIntent().getStringExtra("myname");
		messagetitle=getIntent().getStringExtra("messagetitle");
		originalcontent=getIntent().getStringExtra("messagecontent");
		tv_messagesender.setText(myname);
		tv_messagetitle.setText(messagetitle);
		if(originalcontent.isEmpty())
			originalcontent=" ";
		wv_originalcontent.loadDataWithBaseURL(null, originalcontent, "text/html", "utf-8", null);
	}
	/**
	 * �����ռ���
	 * @param v
	 */
	public void onClick_AddReceiver(View v){
		Intent intent=new Intent();
		intent.setClass(getApplicationContext(), ContactsActivity.class);
		//startActivity(intent);
		startActivityForResult(intent, 1000);
	}
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000 && resultCode == 1001)
        {
        	receivers = data.getStringExtra("result");
            Log.v("��Ϣת����ϵ��2",receivers);
            et_messagereceiver.setText(receivers);
        }
    }
	
	
	private Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			int whatVal = msg.what;
			switch (whatVal) {
			case SEND_MESSAGE:
				Toast.makeText(getApplicationContext(), "������Ϣ", Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_forwardmessage, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getTitle().toString().trim().equals("����")) {
			handler.sendEmptyMessage(SEND_MESSAGE);
		}
		else if (item.getItemId()==android.R.id.home){
			this.finish();
		}
		return super.onOptionsItemSelected(item);
	}
}