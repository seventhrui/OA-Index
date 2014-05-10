package com.example.activity;

import java.util.HashMap;

import com.example.fun.FunAndFunType;

import com.example.oa_index.R;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
/**
 * ��ʼ�˵�
 */
public class StartMenuActivity extends FinalActivity implements OnTouchListener {
	private static final int AVTIVITY_NEWMESSAGE=1;//����Ϣ
	private static final int AVTIVITY_INBOXMESSA=2;//�ռ���
	private static final int AVTIVITY_OUTBOXMESS=3;//������
	private static final int AVTIVITY_DRAFTMESSA=4;//�ݸ���
	private static final int AVTIVITY_WASTEMESSA=5;//������
	private static final int SNAP_VELOCITY = 200;//������ʾ������menuʱ����ָ������Ҫ�ﵽ���ٶȡ�
	private int screenWidth;//��Ļ���ֵ��
	private int leftEdge;//menu�����Ի����������Ե��ֵ��menu���ֵĿ��������marginLeft�����ֵ֮�󣬲����ټ��١�
	private int rightEdge = 0;//menu�����Ի��������ұ�Ե��ֵ��Ϊ0����marginLeft����0֮�󣬲������ӡ�
	private int menuPadding = 80;//menu��ȫ��ʾʱ������content�Ŀ��ֵ��
	private LinearLayout.LayoutParams menuParams;//menu���ֵĲ�����ͨ���˲���������leftMargin��ֵ��
	private float xDown;//��¼��ָ����ʱ�ĺ����ꡣ
	private float xMove;//��¼��ָ�ƶ�ʱ�ĺ����ꡣ
	private float xUp;//��¼�ֻ�̧��ʱ�ĺ����ꡣ
	private boolean isMenuVisible;//menu��ǰ����ʾ�������ء�ֻ����ȫ��ʾ������menuʱ�Ż���Ĵ�ֵ�����������д�ֵ��Ч��
	private VelocityTracker mVelocityTracker;//���ڼ�����ָ�������ٶȡ�
	private ActionBar actionbar=null;
	@ViewInject(id=R.id.content) View content;
	@ViewInject(id=R.id.menu) View menu;
	@ViewInject(id=R.id.lv_funtype,itemClick="onClick_ChoiceFunType") ListView lv_funtype;
	@ViewInject(id=R.id.gv_funs,itemClick="onClick_ChoiceFuns") GridView gv_funs;
	private SimpleAdapter gvadapter=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		initValues();
	}

	/**
	 * ��ʼ��һЩ�ؼ������ݡ�������ȡ��Ļ�Ŀ�ȣ���content�����������ÿ�ȣ���menu�����������ÿ�Ⱥ�ƫ�ƾ���ȡ�
	 */
	private void initValues() {
		actionbar=getActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
		WindowManager window = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		screenWidth = window.getDefaultDisplay().getWidth();
		menuParams = (LinearLayout.LayoutParams) menu.getLayoutParams();
		// ��menu�Ŀ������Ϊ��Ļ��ȼ�ȥmenuPadding
		menuParams.width = screenWidth - menuPadding;
		// ���Ե��ֵ��ֵΪmenu��ȵĸ���
		leftEdge = -menuParams.width;
		// menu��leftMargin����Ϊ���Ե��ֵ��������ʼ��ʱmenu�ͱ�Ϊ���ɼ�
		// menuParams.leftMargin = leftEdge;
		// ��content�Ŀ������Ϊ��Ļ���
		content.getLayoutParams().width = screenWidth;
		content.setOnTouchListener(this);
		gvadapter=FunAndFunType.getMessageFuns(getApplicationContext());
		fillFunType();
	}
	/**
	 * ѡ��������
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 */
	public void onClick_ChoiceFunType(AdapterView<?> arg0, View arg1, int arg2,long arg3){
		TextView tvfuntype=(TextView) arg1;
		String funtype=tvfuntype.getText().toString().trim();
		scrollToContent();
		//ˢ��GridView
		((BaseAdapter) gv_funs.getAdapter()).notifyDataSetChanged();
		actionbar.setTitle(funtype);
		gv_funs.setAdapter(getGvAdapter(funtype));
	}
	private SimpleAdapter getGvAdapter(String type){
		SimpleAdapter sladapter=null;
		if(type.equals("֪ͨͨ��")){
			sladapter=FunAndFunType.getNotifyFuns(getApplicationContext());
		}
		else if(type.equals("��Ϣ��ͨ")){
			sladapter=FunAndFunType.getMessageFuns(getApplicationContext());
		}
		else if(type.equals("�Ӱ����")){
			sladapter=FunAndFunType.getOvertimeSickFuns(getApplicationContext());
		}
		else if(type.equals("����дʵ")){
			sladapter=FunAndFunType.getWorkRecordFuns(getApplicationContext());
		}
		else if(type.equals("�ƻ��ܽ�")){
			sladapter=FunAndFunType.getWorkPlanFuns(getApplicationContext());
		}
		else if(type.equals("��Ч����")){
			sladapter=FunAndFunType.getWorkPerformanceCheckFuns(getApplicationContext());
		}
		else if(type.equals("֪ʶ����")){
			sladapter=FunAndFunType.getWorkKnowledgeCenterFuns(getApplicationContext());
		}
		return sladapter;
	}
	/**
	 * ѡ����
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 */
	public void onClick_ChoiceFuns(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
		HashMap<String, Object> item = (HashMap<String, Object>) arg0.getItemAtPosition(arg2);
		String imgName = (String) item.get("ItemText");
		if (imgName.equals("����Ϣ")) {
			handler.sendEmptyMessage(AVTIVITY_NEWMESSAGE);
		} else if (imgName.equals("������Ϣ")) {
			handler.sendEmptyMessage(AVTIVITY_INBOXMESSA);
		} else if (imgName.equals("�ѷ���Ϣ")) {
			handler.sendEmptyMessage(AVTIVITY_OUTBOXMESS);
		} else if (imgName.equals("��Ϣ�ݸ�")) {
			handler.sendEmptyMessage(AVTIVITY_DRAFTMESSA);
		} else if (imgName.equals("��Ϣ����")) {
			handler.sendEmptyMessage(AVTIVITY_WASTEMESSA);
		} else {
			handler.sendEmptyMessage(50);
		}
	}
	private void fillFunType(){
		lv_funtype.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,FunAndFunType.getFunList()));
		gv_funs.setAdapter(gvadapter);
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		createVelocityTracker(event);
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// ��ָ����ʱ����¼����ʱ�ĺ�����
			xDown = event.getRawX();
			break;
		case MotionEvent.ACTION_MOVE:
			// ��ָ�ƶ�ʱ���ԱȰ���ʱ�ĺ����꣬������ƶ��ľ��룬������menu��leftMarginֵ���Ӷ���ʾ������menu
			xMove = event.getRawX();
			int distanceX = (int) (xMove - xDown);
			if (isMenuVisible) {
				menuParams.leftMargin = distanceX;
			} else {
				menuParams.leftMargin = leftEdge + distanceX;
			}
			if (menuParams.leftMargin < leftEdge) {
				menuParams.leftMargin = leftEdge;
			} else if (menuParams.leftMargin > rightEdge) {
				menuParams.leftMargin = rightEdge;
			}
			menu.setLayoutParams(menuParams);
			break;
		case MotionEvent.ACTION_UP:
			// ��ָ̧��ʱ�������жϵ�ǰ���Ƶ���ͼ���Ӷ������ǹ�����menu���棬���ǹ�����content����
			xUp = event.getRawX();
			if (wantToShowMenu()) {
				if (shouldScrollToMenu()) {
					scrollToMenu();
				} else {
					scrollToContent();
				}
			} else if (wantToShowContent()) {
				if (shouldScrollToContent()) {
					scrollToContent();
				} else {
					scrollToMenu();
				}
			}
			recycleVelocityTracker();
			break;
		}
		return true;
	}

	/**
	 * �жϵ�ǰ���Ƶ���ͼ�ǲ�������ʾcontent�������ָ�ƶ��ľ����Ǹ������ҵ�ǰmenu�ǿɼ��ģ�����Ϊ��ǰ��������Ҫ��ʾcontent��
	 * @return ��ǰ��������ʾcontent����true�����򷵻�false��
	 */
	private boolean wantToShowContent() {
		return xUp - xDown < 0 && isMenuVisible;
	}

	/**
	 * �жϵ�ǰ���Ƶ���ͼ�ǲ�������ʾmenu�������ָ�ƶ��ľ������������ҵ�ǰmenu�ǲ��ɼ��ģ�����Ϊ��ǰ��������Ҫ��ʾmenu��
	 * @return ��ǰ��������ʾmenu����true�����򷵻�false��
	 */
	private boolean wantToShowMenu() {
		return xUp - xDown > 0 && !isMenuVisible;
	}

	/**
	 * �ж��Ƿ�Ӧ�ù�����menuչʾ�����������ָ�ƶ����������Ļ��1/2��������ָ�ƶ��ٶȴ���SNAP_VELOCITY��
	 * ����ΪӦ�ù�����menuչʾ������
	 * @return ���Ӧ�ù�����menuչʾ��������true�����򷵻�false��
	 */
	private boolean shouldScrollToMenu() {
		return xUp - xDown > screenWidth / 2 || getScrollVelocity() > SNAP_VELOCITY;
	}

	/**
	 * �ж��Ƿ�Ӧ�ù�����contentչʾ�����������ָ�ƶ��������menuPadding������Ļ��1/2��
	 * ������ָ�ƶ��ٶȴ���SNAP_VELOCITY�� ����ΪӦ�ù�����contentչʾ������
	 * @return ���Ӧ�ù�����contentչʾ��������true�����򷵻�false��
	 */
	private boolean shouldScrollToContent() {
		return xDown - xUp + menuPadding > screenWidth / 2 || getScrollVelocity() > SNAP_VELOCITY;
	}

	/**
	 * ����Ļ������menu���棬�����ٶ��趨Ϊ30.
	 */
	private void scrollToMenu() {
		new ScrollTask().execute(30);
	}

	/**
	 * ����Ļ������content���棬�����ٶ��趨Ϊ-30.
	 */
	private void scrollToContent() {
		new ScrollTask().execute(-30);
	}

	/**
	 * ����VelocityTracker���󣬲�������content����Ļ����¼����뵽VelocityTracker���С�
	 * @param event
	 *            content����Ļ����¼�
	 */
	private void createVelocityTracker(MotionEvent event) {
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);
	}

	/**
	 * ��ȡ��ָ��content���滬�����ٶȡ�
	 * @return �����ٶȣ���ÿ�����ƶ��˶�������ֵΪ��λ��
	 */
	private int getScrollVelocity() {
		mVelocityTracker.computeCurrentVelocity(1000);
		int velocity = (int) mVelocityTracker.getXVelocity();
		return Math.abs(velocity);
	}

	/**
	 * ����VelocityTracker����
	 */
	private void recycleVelocityTracker() {
		mVelocityTracker.recycle();
		mVelocityTracker = null;
	}

	class ScrollTask extends AsyncTask<Integer, Integer, Integer> {
		@Override
		protected Integer doInBackground(Integer... speed) {
			int leftMargin = menuParams.leftMargin;
			// ���ݴ�����ٶ����������棬������������߽���ұ߽�ʱ������ѭ����
			while (true) {
				leftMargin = leftMargin + speed[0];
				if (leftMargin > rightEdge) {
					leftMargin = rightEdge;
					break;
				}
				if (leftMargin < leftEdge) {
					leftMargin = leftEdge;
					break;
				}
				publishProgress(leftMargin);
				// Ϊ��Ҫ�й���Ч��������ÿ��ѭ��ʹ�߳�˯��20���룬�������۲��ܹ���������������
				sleep(20);
			}
			if (speed[0] > 0) {
				isMenuVisible = true;
			} else {
				isMenuVisible = false;
			}
			return leftMargin;
		}

		@Override
		protected void onProgressUpdate(Integer... leftMargin) {
			menuParams.leftMargin = leftMargin[0];
			menu.setLayoutParams(menuParams);
		}

		@Override
		protected void onPostExecute(Integer leftMargin) {
			menuParams.leftMargin = leftMargin;
			menu.setLayoutParams(menuParams);
		}
	}

	/**
	 * ʹ��ǰ�߳�˯��ָ���ĺ�������
	 * @param millis
	 *            ָ����ǰ�߳�˯�߶�ã��Ժ���Ϊ��λ
	 */
	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Intent intent = new Intent();
			switch (msg.what) {
			case AVTIVITY_NEWMESSAGE:
				intent.setClass(getApplicationContext(),MessageNewActivity.class);
				break;
			case AVTIVITY_INBOXMESSA:
				intent.setClass(getApplicationContext(), InboxListAvtivity.class);
				break;
			case AVTIVITY_OUTBOXMESS:
				intent.setClass(getApplicationContext(), OutboxListAvtivity.class);
				break;
			case AVTIVITY_DRAFTMESSA:
				intent.setClass(getApplicationContext(), DraftListAvtivity.class);
				break;
			case AVTIVITY_WASTEMESSA:
				intent.setClass(getApplicationContext(), WasteListAvtivity.class);
				break;
			default:
				intent.setClass(getApplicationContext(), Other.class);
				break;
			}
			startActivity(intent);
		}
	};
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_start, menu);
		return true;
	}
	@Override//ic_menu_revert
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getTitle().toString().trim().equals("�˵�")) {
			actionbar.setTitle("��ʼ�˵�");
			scrollToMenu();
		}
		else if (item.getItemId()==android.R.id.home){
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), MyCenterActivity.class);
			startActivity(intent);
			this.finish();
		}
		return super.onOptionsItemSelected(item);
	}
}
