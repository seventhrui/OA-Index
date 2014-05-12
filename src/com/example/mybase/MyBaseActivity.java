package com.example.mybase;

import android.view.KeyEvent;
import android.widget.Toast;

import com.example.oa_index.R;

import net.tsz.afinal.FinalActivity;

public class MyBaseActivity extends FinalActivity {

	// 按返回键退出应用
	private long exitTime = 0;// 记录按返回键次数
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), R.string.returnexit, Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				this.finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
