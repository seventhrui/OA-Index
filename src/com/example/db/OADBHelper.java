package com.example.db;

import java.util.List;

import net.tsz.afinal.FinalDb;
import android.content.Context;

import com.example.beans.MyMessageBean;
import com.example.beans.MyNoticeBean;
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
	/**
	 * ɾ����Ϣ By id
	 * @param id
	 * @param context
	 */
	public static void deleteMessage(String id,Context context){
		FinalDb db = FinalDb.create(context);
		db.deleteById(MyMessageBean.class, id);
	}
	/**
	 * ����֪ͨ����
	 * @param mmblist
	 * @param context
	 */
	public static void saveNotices(List<MyNoticeBean> mnblist,Context context) {
		FinalDb db = FinalDb.create(context);
		for(MyNoticeBean mn:mnblist){
			try{
				db.save(mn);
			}catch(Exception e){
				
			}
		}
	}
	/**
	 * ����֪ͨͨ������
	 * @param mmb
	 * @param context
	 */
	public static void saveNotice(MyNoticeBean mnb,Context context){
		FinalDb db = FinalDb.create(context);
		try{
			db.save(mnb);
		}catch(Exception e){
			
		}
	}
	/**
	 * ɾ��֪ͨ By id
	 * @param id
	 * @param context
	 */
	public static void deleteNotice(String id,Context context){
		FinalDb db = FinalDb.create(context);
		db.deleteById(MyNoticeBean.class, id);
	}
}
