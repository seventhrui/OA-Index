package com.example.mybase;

import android.view.KeyEvent;
import android.widget.Toast;

import com.example.oa_index.R;

import net.tsz.afinal.FinalActivity;

public class MyBaseActivity extends FinalActivity {

	// �����ؼ��˳�Ӧ��
	private long exitTime = 0;// ��¼�����ؼ�����
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
