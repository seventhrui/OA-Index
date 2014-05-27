package com.example.mytree;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalDb;

import com.example.HandlerCode;
import com.example.beans.LoginConfig;
import com.example.beans.OrganizationBean;
import com.example.beans.UserBean;
import com.example.http.HttpHelper;
import com.example.oa_index.R;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * 联系人树
 */
public class ContactsActivity extends Activity {
	private TreeListView listView;

	private String result = "";
	public static List<OrganizationBean> oblist = new ArrayList<OrganizationBean>();// 组织机构类
	public static List<UserBean> ublist = new ArrayList<UserBean>();// 人员类
	private final static int DOWNLOAD_ORGANANDUSER_BEGIN = 0;// 下载组织机构人员
	private final static int DOWNLOAD_ORGANANDUSER_SUCCESS = 1;// 下载组织机构人员成功
	private final static int DOWNLOAD_ORGANANDUSER_FAILURE = -1;// 下载组织机构人员失败
	private final static int DATABASE_SAVE = 2;// 保存数据
	private FinalDb db;

	RelativeLayout rl;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contacts);
		rl = new RelativeLayout(this);
		rl.setLayoutParams(new LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.FILL_PARENT));
		/*
		 * listView = new TreeListView(this,initNodeTree());
		 * rl.addView(listView); setContentView(rl);
		 */
		initView();
		// handler.sendEmptyMessage(DOWNLOAD_ORGANANDUSER_BEGIN);// 开始下载组织人员
		handler.sendEmptyMessage(HandlerCode.SHOWDATA);
		db = FinalDb.create(this);
	}

	private void initView() {
		ActionBar actionbar = getActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
	}

	/**
	 * 初始化树
	 * 
	 * @return
	 */
	public List<NodeResource> initNodeTree() {
		List<NodeResource> list = new ArrayList<NodeResource>();
		for (OrganizationBean ob : oblist) {
			NodeResource nr = new NodeResource(ob.getOrganizationparent(),
					ob.getOrganizationid(), ob.getOrganizationname(),
					ob.getOrganizationid(), R.drawable.icon_department);
			list.add(nr);
		}
		for (UserBean ub : ublist) {
			NodeResource n2 = new NodeResource(ub.getUserorga(),
					ub.getUsersid(), ub.getUsername(), ub.getUsersid(),
					R.drawable.icon_department);
			list.add(n2);
		}
		return list;
	}

	public List<NodeResource> searchNodeTree() {
		List<NodeResource> list = new ArrayList<NodeResource>();
		List<OrganizationBean> obl = new ArrayList<OrganizationBean>();// 组织机构类
		List<UserBean> ubl = new ArrayList<UserBean>();// 人员类
		obl = db.findAll(OrganizationBean.class);
		ubl = db.findAll(UserBean.class);
		
		for (OrganizationBean ob : obl) {
			NodeResource nr = new NodeResource(ob.getOrganizationparent(),
					ob.getOrganizationid(), ob.getOrganizationname(),
					ob.getOrganizationid(), R.drawable.icon_department);
			list.add(nr);
		}
		
		for (UserBean ub : ubl) {
			NodeResource n2 = new NodeResource(ub.getUserorga(),
					ub.getUsersid(), ub.getUsername(), ub.getUsersid(),
					R.drawable.icon_department);
			list.add(n2);
		}
		return list;
	}

	/**
	 * 联系人列表填充listView
	 */
	private void showlist(List<NodeResource> nrl) {
		if (listView != null)
			listView.removeAllViews();
		listView = new TreeListView(this, nrl);
		rl.addView(listView);
		setContentView(rl);
	}

	/**
	 * 分割字符串
	 */
	private void splitContactsString() {
		Log.v("调试", "分割数据");
		oblist = new ArrayList<OrganizationBean>();// 组织机构类
		ublist = new ArrayList<UserBean>();// 人员类

		String[] list = result.split("\\$");
		String[] l1 = list[0].split("\\|");
		String[] l2 = list[1].split("\\|");

		// 生成组织列表
		for (String s : l1) {
			String[] lo = s.split("\\^");
			oblist.add(new OrganizationBean(lo[0], lo[1], lo[2], lo[3]));
		}
		// 生成人员列表
		for (String s : l2) {
			String[] lo = s.split("\\^");
			ublist.add(new UserBean(lo[0], lo[1], lo[2]));
		}
		handler.sendEmptyMessage(DATABASE_SAVE);
	}

	private Runnable insertInToDatabases = new Runnable() {
		public void run() {
			Log.v("调试", "保存数据");

			for (OrganizationBean ob : oblist) {
				try {
					db.save(ob);
				} catch (Exception e) {

				}
			}
			for (UserBean ub : ublist) {
				try {
					db.save(ub);
				} catch (Exception e) {

				}
			}
			List<UserBean> userList = db.findAll(UserBean.class);// 查询所有的用户
			Log.e("AfinalOrmDemoActivity", "用户数量："
					+ (userList != null ? userList.size() : 0));
			List<OrganizationBean> organList = db
					.findAll(OrganizationBean.class);// 查询所有的用户
			Log.e("AfinalOrmDemoActivity", "组织机构数量："
					+ (organList != null ? organList.size() : 0));
		}
	};

	private void saveData() {
		new Thread(insertInToDatabases).start();
	}

	/**
	 * 下载成员列表
	 */
	private Runnable downloadReceiver = new Runnable() {
		public void run() {
			try {
				Log.v("调试", "下载数据");
				String url = LoginConfig.getLoginConfig().getServerip();
				String urlPath = "http://" + url + "/oa/ashx/Ioa.ashx?ot=1";// 内网ip
				// 连接服务器成功之后，解析数据
				String data = new HttpHelper(urlPath).doGetString();
				if (data.equals("-1")) {
					handler.sendEmptyMessage(DOWNLOAD_ORGANANDUSER_FAILURE);
				} else if (data.equals("0")) {
				} else {
					result = data;
					handler.sendEmptyMessage(DOWNLOAD_ORGANANDUSER_SUCCESS);
				}
			} catch (Exception e) {
				handler.sendEmptyMessage(HandlerCode.CONNECTION_TIMEOUT);
			}
		}
	};

	private void downloadData() {
		new Thread(downloadReceiver).start();
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			int whatVal = msg.what;
			switch (whatVal) {
			case DOWNLOAD_ORGANANDUSER_BEGIN:
				Log.v("hangler", "下载开始");
				downloadData();
				break;
			case DOWNLOAD_ORGANANDUSER_SUCCESS:
				Log.v("hangler", "下载成功");
				splitContactsString();
				break;
			case DOWNLOAD_ORGANANDUSER_FAILURE:
				Log.v("hangler", "下载失败");
				downloadData();
				break;
			case DATABASE_SAVE:
				Log.v("hangler", "保存数据");
				showlist(initNodeTree());
				saveData();
				break;
			case HandlerCode.SHOWDATA:
				showlist(searchNodeTree());
				break;
			case HandlerCode.CONNECTION_TIMEOUT:
				toastTimeOut();
				break;
			}
		}
	};
	
	/**
	 * 提示下载超时
	 */
	private void toastTimeOut(){
		Toast.makeText(getApplicationContext(), R.string.timeout, Toast.LENGTH_LONG).show();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_addreceiver, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getTitle().toString().trim().equals("刷新")) {
			handler.sendEmptyMessage(DOWNLOAD_ORGANANDUSER_BEGIN);
		} else if (item.getTitle().toString().trim().equals("联系人")) {
			List<Node> nodelist = null;
			String receivernames = "";
			String receiverids = "";
			try {
				nodelist = listView.ta.getSelectedNode();
				if (nodelist != null) {
					nodelist = listView.ta.getSelectedNode();
					for (Node n : nodelist) {
						receivernames += n.getTitle() + ",";
						receiverids += n.getValue() + ",";
					}
				}
			} catch (Exception e) {

			}
			if (!receivernames.equals("")) {
				receivernames = receivernames.substring(0,
						receivernames.length() - 1);
				receiverids = receiverids
						.substring(0, receiverids.length() - 1);
				Intent intent = new Intent();
				intent.putExtra("resultnames", receivernames);
				intent.putExtra("receiverids", receiverids);
				setResult(1001, intent);
			}
			this.finish();
		}
		return super.onOptionsItemSelected(item);
	}
}
