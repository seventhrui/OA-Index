package com.example.db;

import java.util.List;

import net.tsz.afinal.FinalDb;
import android.content.Context;

import com.example.beans.MyMessageBean;
import com.example.beans.MyNoticeBean;
/**
 * 数据库操作相关类
 * @author seven
 *
 */
public class OADBHelper {
	/**
	 * 保存信息数据
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
	 * 保存信息数据
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
	 * 删除信息 By id
	 * @param id
	 * @param context
	 */
	public static void deleteMessage(String id,Context context){
		FinalDb db = FinalDb.create(context);
		db.deleteById(MyMessageBean.class, id);
	}
	/**
	 * 保存通知数据
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
	 * 保存通知通告数据
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
	 * 删除通知 By id
	 * @param id
	 * @param context
	 */
	public static void deleteNotice(String id,Context context){
		FinalDb db = FinalDb.create(context);
		db.deleteById(MyNoticeBean.class, id);
	}
}
