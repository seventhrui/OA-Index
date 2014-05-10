package com.example.db;

import java.util.List;

import net.tsz.afinal.FinalDb;
import android.content.Context;

import com.example.beans.MyMessageBean;

public class OADBHelper {
	/**
	 * 保存信息数据
	 */
	public static void saveMessages(List<MyMessageBean> mmblist,Context context) {
		FinalDb db = FinalDb.create(context);
		for(MyMessageBean mm:mmblist){
			try{
				db.save(mm);
			}catch(Exception e){
				
			}
		}
	}
}
