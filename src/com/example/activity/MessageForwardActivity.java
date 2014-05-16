package com.example.activity;

import com.example.HandlerCode;
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
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
/**
 * 信息转发
 */
public class MessageForwardActivity extends FinalActivity {
	private String myname="";//发件人
	private String messagetitle="";//信息标题
	private String originalcontent="";//原信息内容
	private String receivers="";//收件人名称
	private String receiverids="";//收件人id
	private String filename="";//附件名
    @ViewInject(id=R.id.tv_messagereceiver) TextView tv_messagereceiver;
    @ViewInject(id=R.id.et_messagereceiver)	public static EditText et_messagereceiver;
    @ViewInject(id=R.id.bt_addreceiver,click="onClick_AddReceiver") ImageButton bt_addreceiver;
    @ViewInject(id=R.id.tv_messagesender) TextView tv_messagesender;
    @ViewInject(id=R.id.tv_messagetitle) TextView tv_messagetitle;
    @ViewInject(id=R.id.wv_originalcontent) WebView wv_originalcontent;
    @ViewInject(id=R.id.tv_filename) TextView tv_filename;
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_forward);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initView();
    }
	@Override
	protected void onResume() {
		super.onResume();
		Log.v("信息转发联系人",receiverids);
		et_messagereceiver.setText(receivers);
	}
	private void initView(){
		ActionBar actionbar=getActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
		
		myname=getIntent().getStringExtra("myname");
		messagetitle=getIntent().getStringExtra("messagetitle");
		originalcontent=getIntent().getStringExtra("messagecontent");
		filename=getIntent().getStringExtra("messagefile");
		tv_messagesender.setText(myname);
		tv_messagetitle.setText(messagetitle);
		if(originalcontent.isEmpty())
			originalcontent=" ";
		wv_originalcontent.loadDataWithBaseURL(null, originalcontent, "text/html", "utf-8", null);
		tv_filename.setText(filename);
	}
	/**
	 * 添加收件人
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
        	receivers = data.getStringExtra("resultnames");
        	receiverids=data.getStringExtra("receiverids");
            Log.v("信息转发联系人2",receiverids);
            et_messagereceiver.setText(receivers);
        }
    }
	
	
	private Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			int whatVal = msg.what;
			switch (whatVal) {
			case HandlerCode.SEND_MESSAGE_BEGIN:
				Toast.makeText(getApplicationContext(), "发送信息", Toast.LENGTH_SHORT).show();
				break;
			}
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
			handler.sendEmptyMessage(HandlerCode.SEND_MESSAGE_BEGIN);
		}
		else if (item.getItemId()==android.R.id.home){
			this.finish();
		}
		return super.onOptionsItemSelected(item);
	}
}
