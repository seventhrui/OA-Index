package com.example.db;

import java.util.List;

import net.tsz.afinal.FinalDb;
import android.content.Context;

import com.example.beans.MyMessageBean;
/**
 * ���ݿ���������
 * @author seven
 *
 */
public class OADBHelper {
	/**
	 * ������Ϣ����
	 * @param mmblist
	 * @param context
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
	/**
	 * ������Ϣ����
	 * @param mmb
	 * @param context
	 */
	public static void saveMessage(MyMessageBean mmb,Context context){
		FinalDb db = FinalDb.create(context);
		try{
			db.save(mmb);
		}catch(Exception e){
			
		}
	}
}
