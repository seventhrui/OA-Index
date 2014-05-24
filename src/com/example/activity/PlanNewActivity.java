package com.example.activity;

import android.os.Bundle;
import android.view.WindowManager;

import com.example.oa_index.R;

import net.tsz.afinal.FinalActivity;

/**
 * 新建计划填写
 * 
 */
public class PlanNewActivity extends FinalActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_plan_new);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}
}
