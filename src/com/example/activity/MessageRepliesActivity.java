package com.example.activity;

import com.example.oa_index.R;

import android.app.ActionBar;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
/**
 * ��Ϣ�ظ�
 */
public class MessageRepliesActivity extends FinalActivity {
	private final static int SEND_MESSAGE=0;//������Ϣ
	private String originalsender="";//ԭ������
	private String myname="";//�û���
	private String messagetitle="";//ԭ��Ϣ����
	
    @ViewInject(id=R.id.tv_messagereceiver) TextView tv_messagereceiver;
    @ViewInject(id=R.id.tv_messagesender) TextView tv_messagesender;
    @ViewInject(id=R.id.tv_messagetitle) TextView tv_messagetitle;
    @ViewInject(id=R.id.et_messagecontent) EditText et_messagecontent;
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_replies);
        initView();
       }
   	private void initView(){
		ActionBar actionbar=getActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		originalsender=getIntent().getStringExtra("messagesender");
		myname=getIntent().getStringExtra("myname");
		messagetitle=getIntent().getStringExtra("messagetitle");
		tv_messagereceiver.setText(originalsender);
		tv_messagesender.setText(myname);
		tv_messagetitle.setText("�ظ���"+messagetitle);
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
