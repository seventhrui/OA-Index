package com.example.fun;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.oa_index.R;

import android.content.Context;
import android.widget.SimpleAdapter;
/**
 * ��ʼ�˵�Ԫ��
 *
 */
public class FunAndFunType {
	static ArrayList<HashMap<String, Object>> lstImageItem =null;
	/**
	 * ���������б�
	 * @return
	 */
	public static List<String> getFunList(){
		List<String> funlist=new ArrayList<String>();
		funlist.add("֪ͨͨ��");
		funlist.add("��Ϣ��ͨ");
		funlist.add("�Ӱ����");
		funlist.add("����дʵ");
		funlist.add("�ƻ��ܽ�");
		funlist.add("��Ч����");
		funlist.add("֪ʶ����");
		return funlist;
	}
	/**
	 * ֪ͨͨ��gv
	 * @param context
	 * @return
	 */
	public static SimpleAdapter getNotifyFuns(Context context){
		lstImageItem = new ArrayList<HashMap<String, Object>>();
		Integer[] mIds = { R.drawable.notice_xj,R.drawable.notice_cg, R.drawable.notice_lb, R.drawable.notice_yf };
		String[] mNames = { "����֪ͨ", "֪ͨ�ݸ�", "֪ͨ�б�", "֪ͨ�ѷ�" };
		for (int i = 0; i < mIds.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("ItemImage", mIds[i]);// ���ͼ����Դ��ID
			map.put("ItemText", mNames[i]);// �������ItemText
			lstImageItem.add(map);
		}
		SimpleAdapter saImageItems = new SimpleAdapter(context, lstImageItem,
				R.layout.item_grid_startmenu,
				new String[] { "ItemImage", "ItemText" }, new int[] {
						R.id.ItemImage, R.id.ItemText });
		return saImageItems;
	}
	
	/**
	 * ��Ϣ��ͨgv
	 * @param context
	 * @return
	 */
	public static SimpleAdapter getMessageFuns(Context context){
		lstImageItem = new ArrayList<HashMap<String, Object>>();
		Integer[] mIds = { R.drawable.message_xj, R.drawable.message_ys,R.drawable.message_yf, R.drawable.message_cg, R.drawable.message_hs };
		String[] mNames = { "����Ϣ", "������Ϣ", "�ѷ���Ϣ", "��Ϣ�ݸ�", "��Ϣ����" };
		for (int i = 0; i < mIds.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("ItemImage", mIds[i]);// ���ͼ����Դ��ID
			map.put("ItemText", mNames[i]);// �������ItemText
			lstImageItem.add(map);
		}
		SimpleAdapter saImageItems = new SimpleAdapter(context, lstImageItem,
				R.layout.item_grid_startmenu,
				new String[] { "ItemImage", "ItemText" }, new int[] {
						R.id.ItemImage, R.id.ItemText });
		return saImageItems;
	}
	
	/**
	 * �Ӱ����gv
	 * @param context
	 * @return
	 */
	public static SimpleAdapter getOvertimeSickFuns(Context context){
		lstImageItem = new ArrayList<HashMap<String, Object>>();
		Integer[] mIds = { R.drawable.timesick_qj, R.drawable.timesick_jb, R.drawable.timesick_sp,R.drawable.timesick_cy, R.drawable.timesick_tj, R.drawable.timesick_gd };
		String[] mNames = { "��Ҫ���", "�Ӱ�����", "�Ӱ��������", "�Ӱ���ٲ���", "��ٵ���ͳ��","�鵵" };
		for (int i = 0; i < mIds.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("ItemImage", mIds[i]);// ���ͼ����Դ��ID
			map.put("ItemText", mNames[i]);// �������ItemText
			lstImageItem.add(map);
		}
		SimpleAdapter saImageItems = new SimpleAdapter(context, lstImageItem,
				R.layout.item_grid_startmenu,
				new String[] { "ItemImage", "ItemText" }, new int[] {
						R.id.ItemImage, R.id.ItemText });
		return saImageItems;
	}
	
	/**
	 * ����дʵgv
	 * @param context
	 * @return
	 */
	public static SimpleAdapter getWorkRecordFuns(Context context){
		lstImageItem = new ArrayList<HashMap<String, Object>>();
		Integer[] mIds = { R.drawable.workrecord_bj, R.drawable.workrecord_xc,R.drawable.workrecord_hzxc, R.drawable.workrecord_sb };
		String[] mNames = { "����дʵ�༭", "����дʵ��ѯ", "����дʵ���ܲ�ѯ", "����дʵ�ϱ�"};
		for (int i = 0; i < mIds.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("ItemImage", mIds[i]);// ���ͼ����Դ��ID
			map.put("ItemText", mNames[i]);// �������ItemText
			lstImageItem.add(map);
		}
		SimpleAdapter saImageItems = new SimpleAdapter(context, lstImageItem,
				R.layout.item_grid_startmenu,
				new String[] { "ItemImage", "ItemText" }, new int[] {
						R.id.ItemImage, R.id.ItemText });
		return saImageItems;
	}
	
	/**
	 * �ƻ��ܽ�gv
	 * @param context
	 * @return
	 */
	public static SimpleAdapter getWorkPlanFuns(Context context){
		lstImageItem = new ArrayList<HashMap<String, Object>>();
		Integer[] mIds = { R.drawable.workplan_jh, R.drawable.workplan_zj, R.drawable.workplan_cx,R.drawable.workplan_xmpz, R.drawable.workplan_bmpz };
		String[] mNames = { "�ƻ���д", "�ܽ���д", "�ƻ��ܽ��ѯ", "��Ŀ����","������Ŀ����"};
		for (int i = 0; i < mIds.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("ItemImage", mIds[i]);// ���ͼ����Դ��ID
			map.put("ItemText", mNames[i]);// �������ItemText
			lstImageItem.add(map);
		}
		SimpleAdapter saImageItems = new SimpleAdapter(context, lstImageItem,
				R.layout.item_grid_startmenu,
				new String[] { "ItemImage", "ItemText" }, new int[] {
						R.id.ItemImage, R.id.ItemText });
		return saImageItems;
	}
	
	/**
	 * ��Ч����gv
	 * @param context
	 * @return
	 */
	public static SimpleAdapter getWorkPerformanceCheckFuns(Context context){
		lstImageItem = new ArrayList<HashMap<String, Object>>();
		Integer[] mIds = { R.drawable.workcheck_df, R.drawable.workcheck_xc };
		String[] mNames = { "Ա����Ч���", "��Ч���˲�ѯ"};
		for (int i = 0; i < mIds.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("ItemImage", mIds[i]);// ���ͼ����Դ��ID
			map.put("ItemText", mNames[i]);// �������ItemText
			lstImageItem.add(map);
		}
		SimpleAdapter saImageItems = new SimpleAdapter(context, lstImageItem,
				R.layout.item_grid_startmenu,
				new String[] { "ItemImage", "ItemText" }, new int[] {
						R.id.ItemImage, R.id.ItemText });
		return saImageItems;
	}
	
	/**
	 * ֪ʶ����gv
	 * @param context
	 * @return
	 */
	public static SimpleAdapter getWorkKnowledgeCenterFuns(Context context){
		lstImageItem = new ArrayList<HashMap<String, Object>>();
		Integer[] mIds = {R.drawable.knowledge_ck, R.drawable.knowledge_fb, R.drawable.knowledge_mlgl,R.drawable.knowledge_lxck, R.drawable.knowledge_gl };
		String[] mNames = { "�鿴֪ʶ", "������֪ʶ","֪ʶĿ¼����","֪ʶ���͹���","֪ʶ����"};
		for (int i = 0; i < mIds.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("ItemImage", mIds[i]);// ���ͼ����Դ��ID
			map.put("ItemText", mNames[i]);// �������ItemText
			lstImageItem.add(map);
		}
		SimpleAdapter saImageItems = new SimpleAdapter(context, lstImageItem,
				R.layout.item_grid_startmenu,
				new String[] { "ItemImage", "ItemText" }, new int[] {
						R.id.ItemImage, R.id.ItemText });
		return saImageItems;
	}
}
