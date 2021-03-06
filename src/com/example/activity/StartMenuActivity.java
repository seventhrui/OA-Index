package com.example.activity;

import java.util.HashMap;

import com.example.ActivityCode;
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
 * 开始菜单
 */
public class StartMenuActivity extends FinalActivity implements OnTouchListener {
	
	private static final int SNAP_VELOCITY = 200;//滚动显示和隐藏menu时，手指滑动需要达到的速度。
	private int screenWidth;//屏幕宽度值。
	private int leftEdge;//menu最多可以滑动到的左边缘。值由menu布局的宽度来定，marginLeft到达此值之后，不能再减少。
	private int rightEdge = 0;//menu最多可以滑动到的右边缘。值恒为0，即marginLeft到达0之后，不能增加。
	private int menuPadding = 80;//menu完全显示时，留给content的宽度值。
	private LinearLayout.LayoutParams menuParams;//menu布局的参数，通过此参数来更改leftMargin的值。
	private float xDown;//记录手指按下时的横坐标。
	private float xMove;//记录手指移动时的横坐标。
	private float xUp;//记录手机抬起时的横坐标。
	private boolean isMenuVisible;//menu当前是显示还是隐藏。只有完全显示或隐藏menu时才会更改此值，滑动过程中此值无效。
	private VelocityTracker mVelocityTracker;//用于计算手指滑动的速度。
	private ActionBar actionbar=null;
	@ViewInject(id=R.id.content) View content;
	@ViewInject(id=R.id.menu) View menu;
	@ViewInject(id=R.id.lv_funtype,itemClick="onClick_ChoiceFunType") ListView lv_funtype;
	@ViewInject(id=R.id.gv_funs,itemClick="onClick_ChoiceFuns") GridView gv_funs;
	private SimpleAdapter gvadapter=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_startmenu);
		initValues();
	}

	/**
	 * 初始化一些关键性数据。包括获取屏幕的宽度，给content布局重新设置宽度，给menu布局重新设置宽度和偏移距离等。
	 */
	private void initValues() {
		actionbar=getActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
		WindowManager window = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		screenWidth = window.getDefaultDisplay().getWidth();
		menuParams = (LinearLayout.LayoutParams) menu.getLayoutParams();
		// 将menu的宽度设置为屏幕宽度减去menuPadding
		menuParams.width = screenWidth - menuPadding;
		// 左边缘的值赋值为menu宽度的负数
		leftEdge = -menuParams.width;
		// menu的leftMargin设置为左边缘的值，这样初始化时menu就变为不可见
		//menuParams.leftMargin = leftEdge;
		// 将content的宽度设置为屏幕宽度
		content.getLayoutParams().width = screenWidth;
		content.setOnTouchListener(this);
		gvadapter=FunAndFunType.getMessageFuns(getApplicationContext());
		fillFunType();
	}
	/**
	 * 选择功能类型
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 */
	public void onClick_ChoiceFunType(AdapterView<?> arg0, View arg1, int arg2,long arg3){
		TextView tvfuntype=(TextView) arg1;
		String funtype=tvfuntype.getText().toString().trim();
		scrollToContent();
		//刷新GridView
		((BaseAdapter) gv_funs.getAdapter()).notifyDataSetChanged();
		actionbar.setTitle(funtype);
		gv_funs.setAdapter(getGvAdapter(funtype));
	}
	private SimpleAdapter getGvAdapter(String type){
		SimpleAdapter sladapter=null;
		if(type.equals("通知通告")){
			sladapter=FunAndFunType.getNotifyFuns(getApplicationContext());
			actionbar.setIcon(R.drawable.notice_);
		}
		else if(type.equals("信息沟通")){
			sladapter=FunAndFunType.getMessageFuns(getApplicationContext());
			actionbar.setIcon(R.drawable.message_);
		}
		else if(type.equals("加班请假")){
			sladapter=FunAndFunType.getOvertimeSickFuns(getApplicationContext());
			actionbar.setIcon(R.drawable.timesick_);
		}
		else if(type.equals("工作写实")){
			sladapter=FunAndFunType.getWorkRecordFuns(getApplicationContext());
			actionbar.setIcon(R.drawable.workrecord_);
		}
		else if(type.equals("计划总结")){
			sladapter=FunAndFunType.getWorkPlanFuns(getApplicationContext());
			actionbar.setIcon(R.drawable.workplan_);
		}
		else if(type.equals("绩效考核")){
			sladapter=FunAndFunType.getWorkPerformanceCheckFuns(getApplicationContext());
			actionbar.setIcon(R.drawable.workcheck_);
		}
		else if(type.equals("知识中心")){
			sladapter=FunAndFunType.getWorkKnowledgeCenterFuns(getApplicationContext());
			actionbar.setIcon(R.drawable.knowledge_);
		}
		return sladapter;
	}
	/**
	 * 选择功能
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 */
	public void onClick_ChoiceFuns(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
		HashMap<String, Object> item = (HashMap<String, Object>) arg0.getItemAtPosition(arg2);
		String imgName = (String) item.get("ItemText");
		if (imgName.equals("发布通知")) {
			handler.sendEmptyMessage(ActivityCode.ACTIVITY_NOTICENEW);
		} else if (imgName.equals("通知草稿")){
			handler.sendEmptyMessage(ActivityCode.ACTIVITY_NOTICDRAFT);
		} else if (imgName.equals("通知列表")){
			handler.sendEmptyMessage(ActivityCode.ACTIVITY_NOTICELIST);
		} else if (imgName.equals("通知已发")){
			handler.sendEmptyMessage(ActivityCode.ACTIVITY_NOTIOUTBOX);
		} else if (imgName.equals("新信息")) {
			handler.sendEmptyMessage(ActivityCode.ACTIVITY_MESSAGENEW);
		} else if (imgName.equals("已收信息")) {
			handler.sendEmptyMessage(ActivityCode.ACTIVITY_MESSAINBOX);
		} else if (imgName.equals("已发信息")) {
			handler.sendEmptyMessage(ActivityCode.ACTIVITY_MESSOUTBOX);
		} else if (imgName.equals("信息草稿")) {
			handler.sendEmptyMessage(ActivityCode.ACTIVITY_MESSADRAFT);
		} else if (imgName.equals("信息回收")) {
			handler.sendEmptyMessage(ActivityCode.ACTIVITY_MESSAWASTE);
		} else if (imgName.equals("我要请假")){
			handler.sendEmptyMessage(ActivityCode.ACTIVITY_REQUELEAVE);
		} else if (imgName.equals("计划填写")){
			handler.sendEmptyMessage(ActivityCode.ACTIVITY_PLANNEW);
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
			// 手指按下时，记录按下时的横坐标
			xDown = event.getRawX();
			break;
		case MotionEvent.ACTION_MOVE:
			// 手指移动时，对比按下时的横坐标，计算出移动的距离，来调整menu的leftMargin值，从而显示和隐藏menu
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
			// 手指抬起时，进行判断当前手势的意图，从而决定是滚动到menu界面，还是滚动到content界面
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
	 * 判断当前手势的意图是不是想显示content。如果手指移动的距离是负数，且当前menu是可见的，则认为当前手势是想要显示content。
	 * @return 当前手势想显示content返回true，否则返回false。
	 */
	private boolean wantToShowContent() {
		return xUp - xDown < 0 && isMenuVisible;
	}

	/**
	 * 判断当前手势的意图是不是想显示menu。如果手指移动的距离是正数，且当前menu是不可见的，则认为当前手势是想要显示menu。
	 * @return 当前手势想显示menu返回true，否则返回false。
	 */
	private boolean wantToShowMenu() {
		return xUp - xDown > 0 && !isMenuVisible;
	}

	/**
	 * 判断是否应该滚动将menu展示出来。如果手指移动距离大于屏幕的1/2，或者手指移动速度大于SNAP_VELOCITY，
	 * 就认为应该滚动将menu展示出来。
	 * @return 如果应该滚动将menu展示出来返回true，否则返回false。
	 */
	private boolean shouldScrollToMenu() {
		return xUp - xDown > screenWidth / 2 || getScrollVelocity() > SNAP_VELOCITY;
	}

	/**
	 * 判断是否应该滚动将content展示出来。如果手指移动距离加上menuPadding大于屏幕的1/2，
	 * 或者手指移动速度大于SNAP_VELOCITY， 就认为应该滚动将content展示出来。
	 * @return 如果应该滚动将content展示出来返回true，否则返回false。
	 */
	private boolean shouldScrollToContent() {
		return xDown - xUp + menuPadding > screenWidth / 2 || getScrollVelocity() > SNAP_VELOCITY;
	}

	/**
	 * 将屏幕滚动到menu界面，滚动速度设定为30.
	 */
	private void scrollToMenu() {
		actionbar.setIcon(R.drawable.startmenu);
		new ScrollTask().execute(30);
	}

	/**
	 * 将屏幕滚动到content界面，滚动速度设定为-30.
	 */
	private void scrollToContent() {
		new ScrollTask().execute(-30);
	}

	/**
	 * 创建VelocityTracker对象，并将触摸content界面的滑动事件加入到VelocityTracker当中。
	 * @param event
	 *            content界面的滑动事件
	 */
	private void createVelocityTracker(MotionEvent event) {
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);
	}

	/**
	 * 获取手指在content界面滑动的速度。
	 * @return 滑动速度，以每秒钟移动了多少像素值为单位。
	 */
	private int getScrollVelocity() {
		mVelocityTracker.computeCurrentVelocity(1000);
		int velocity = (int) mVelocityTracker.getXVelocity();
		return Math.abs(velocity);
	}

	/**
	 * 回收VelocityTracker对象。
	 */
	private void recycleVelocityTracker() {
		mVelocityTracker.recycle();
		mVelocityTracker = null;
	}

	class ScrollTask extends AsyncTask<Integer, Integer, Integer> {
		@Override
		protected Integer doInBackground(Integer... speed) {
			int leftMargin = menuParams.leftMargin;
			// 根据传入的速度来滚动界面，当滚动到达左边界或右边界时，跳出循环。
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
				// 为了要有滚动效果产生，每次循环使线程睡眠20毫秒，这样肉眼才能够看到滚动动画。
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
	 * 使当前线程睡眠指定的毫秒数。
	 * @param millis
	 *            指定当前线程睡眠多久，以毫秒为单位
	 */
	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 跳转Activity
	 */
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Intent intent = new Intent();
			switch (msg.what) {
			case ActivityCode.ACTIVITY_NOTICENEW:
				intent.setClass(getApplicationContext(), NoticeNewActivity.class);
				break;
			case ActivityCode.ACTIVITY_NOTICDRAFT:
				intent.setClass(getApplicationContext(),NoticeDraftListActivity.class);
				break;
			case ActivityCode.ACTIVITY_NOTICELIST:
				intent.setClass(getApplicationContext(), NoticeListActivity.class);
				break;
			case ActivityCode.ACTIVITY_NOTIOUTBOX:
				intent.setClass(getApplicationContext(), NoticeOutboxListActivity.class);
				break;
			case ActivityCode.ACTIVITY_MESSAGENEW:
				intent.setClass(getApplicationContext(),MessageNewActivity.class);
				break;
			case ActivityCode.ACTIVITY_MESSAINBOX:
				intent.setClass(getApplicationContext(), MessageInboxListAvtivity.class);
				break;
			case ActivityCode.ACTIVITY_MESSOUTBOX:
				intent.setClass(getApplicationContext(), MessageOutboxListAvtivity.class);
				break;
			case ActivityCode.ACTIVITY_MESSADRAFT:
				intent.setClass(getApplicationContext(), MessageDraftListAvtivity.class);
				break;
			case ActivityCode.ACTIVITY_MESSAWASTE:
				intent.setClass(getApplicationContext(), MseeageWasteListAvtivity.class);
				break;
			case ActivityCode.ACTIVITY_REQUELEAVE:
				intent.setClass(getApplicationContext(), RequestLeaveActivity.class);
				break;
			case ActivityCode.ACTIVITY_PLANNEW:
				intent.setClass(getApplicationContext(), PlanNewActivity.class);
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
		if (item.getTitle().toString().trim().equals("菜单")) {
			actionbar.setTitle("开始菜单");
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
