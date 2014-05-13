package com.example.fun;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.oa_index.R;

import android.content.Context;
import android.widget.SimpleAdapter;
/**
 * 开始菜单元素
 *
 */
public class FunAndFunType {
	static ArrayList<HashMap<String, Object>> lstImageItem =null;
	/**
	 * 功能类型列表
	 * @return
	 */
	public static List<String> getFunList(){
		List<String> funlist=new ArrayList<String>();
		funlist.add("通知通告");
		funlist.add("信息沟通");
		funlist.add("加班请假");
		funlist.add("工作写实");
		funlist.add("计划总结");
		funlist.add("绩效考核");
		funlist.add("知识中心");
		return funlist;
	}
	/**
	 * 通知通告gv
	 * @param context
	 * @return
	 */
	public static SimpleAdapter getNotifyFuns(Context context){
		lstImageItem = new ArrayList<HashMap<String, Object>>();
		Integer[] mIds = { R.drawable.ic_launcher,R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher };
		String[] mNames = { "发布通知", "通知草稿", "通知列表", "通知已发" };
		for (int i = 0; i < mIds.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("ItemImage", mIds[i]);// 添加图像资源的ID
			map.put("ItemText", mNames[i]);// 按序号做ItemText
			lstImageItem.add(map);
		}
		SimpleAdapter saImageItems = new SimpleAdapter(context, lstImageItem,
				R.layout.item_grid_startmenu,
				new String[] { "ItemImage", "ItemText" }, new int[] {
						R.id.ItemImage, R.id.ItemText });
		return saImageItems;
	}
	
	/**
	 * 消息沟通gv
	 * @param context
	 * @return
	 */
	public static SimpleAdapter getMessageFuns(Context context){
		lstImageItem = new ArrayList<HashMap<String, Object>>();
		Integer[] mIds = { R.drawable.ic_launcher, R.drawable.ic_launcher,R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher };
		String[] mNames = { "新信息", "已收信息", "已发信息", "信息草稿", "信息回收" };
		for (int i = 0; i < mIds.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("ItemImage", mIds[i]);// 添加图像资源的ID
			map.put("ItemText", mNames[i]);// 按序号做ItemText
			lstImageItem.add(map);
		}
		SimpleAdapter saImageItems = new SimpleAdapter(context, lstImageItem,
				R.layout.item_grid_startmenu,
				new String[] { "ItemImage", "ItemText" }, new int[] {
						R.id.ItemImage, R.id.ItemText });
		return saImageItems;
	}
	
	/**
	 * 加班请假gv
	 * @param context
	 * @return
	 */
	public static SimpleAdapter getOvertimeSickFuns(Context context){
		lstImageItem = new ArrayList<HashMap<String, Object>>();
		Integer[] mIds = { R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher,R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher };
		String[] mNames = { "我要请假", "加班申请", "加班请假审批", "加班请假查阅", "请假调休统计","归档" };
		for (int i = 0; i < mIds.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("ItemImage", mIds[i]);// 添加图像资源的ID
			map.put("ItemText", mNames[i]);// 按序号做ItemText
			lstImageItem.add(map);
		}
		SimpleAdapter saImageItems = new SimpleAdapter(context, lstImageItem,
				R.layout.item_grid_startmenu,
				new String[] { "ItemImage", "ItemText" }, new int[] {
						R.id.ItemImage, R.id.ItemText });
		return saImageItems;
	}
	
	/**
	 * 工作写实gv
	 * @param context
	 * @return
	 */
	public static SimpleAdapter getWorkRecordFuns(Context context){
		lstImageItem = new ArrayList<HashMap<String, Object>>();
		Integer[] mIds = { R.drawable.ic_launcher, R.drawable.ic_launcher,R.drawable.ic_launcher, R.drawable.ic_launcher };
		String[] mNames = { "工作写实编辑", "工作写实查询", "工作写实汇总查询", "工作写实上报"};
		for (int i = 0; i < mIds.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("ItemImage", mIds[i]);// 添加图像资源的ID
			map.put("ItemText", mNames[i]);// 按序号做ItemText
			lstImageItem.add(map);
		}
		SimpleAdapter saImageItems = new SimpleAdapter(context, lstImageItem,
				R.layout.item_grid_startmenu,
				new String[] { "ItemImage", "ItemText" }, new int[] {
						R.id.ItemImage, R.id.ItemText });
		return saImageItems;
	}
	
	/**
	 * 计划总结gv
	 * @param context
	 * @return
	 */
	public static SimpleAdapter getWorkPlanFuns(Context context){
		lstImageItem = new ArrayList<HashMap<String, Object>>();
		Integer[] mIds = { R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher,R.drawable.ic_launcher, R.drawable.ic_launcher };
		String[] mNames = { "计划填写", "总结填写", "计划总结查询", "项目配置","部门项目配置"};
		for (int i = 0; i < mIds.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("ItemImage", mIds[i]);// 添加图像资源的ID
			map.put("ItemText", mNames[i]);// 按序号做ItemText
			lstImageItem.add(map);
		}
		SimpleAdapter saImageItems = new SimpleAdapter(context, lstImageItem,
				R.layout.item_grid_startmenu,
				new String[] { "ItemImage", "ItemText" }, new int[] {
						R.id.ItemImage, R.id.ItemText });
		return saImageItems;
	}
	
	/**
	 * 绩效考核gv
	 * @param context
	 * @return
	 */
	public static SimpleAdapter getWorkPerformanceCheckFuns(Context context){
		lstImageItem = new ArrayList<HashMap<String, Object>>();
		Integer[] mIds = { R.drawable.ic_launcher, R.drawable.ic_launcher };
		String[] mNames = { "员工绩效打分", "绩效考核查询"};
		for (int i = 0; i < mIds.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("ItemImage", mIds[i]);// 添加图像资源的ID
			map.put("ItemText", mNames[i]);// 按序号做ItemText
			lstImageItem.add(map);
		}
		SimpleAdapter saImageItems = new SimpleAdapter(context, lstImageItem,
				R.layout.item_grid_startmenu,
				new String[] { "ItemImage", "ItemText" }, new int[] {
						R.id.ItemImage, R.id.ItemText });
		return saImageItems;
	}
	
	/**
	 * 知识中心gv
	 * @param context
	 * @return
	 */
	public static SimpleAdapter getWorkKnowledgeCenterFuns(Context context){
		lstImageItem = new ArrayList<HashMap<String, Object>>();
		Integer[] mIds = {R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher,R.drawable.ic_launcher, R.drawable.ic_launcher };
		String[] mNames = { "查看知识", "发布新知识","知识目录管理","知识类型管理","知识管理"};
		for (int i = 0; i < mIds.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("ItemImage", mIds[i]);// 添加图像资源的ID
			map.put("ItemText", mNames[i]);// 按序号做ItemText
			lstImageItem.add(map);
		}
		SimpleAdapter saImageItems = new SimpleAdapter(context, lstImageItem,
				R.layout.item_grid_startmenu,
				new String[] { "ItemImage", "ItemText" }, new int[] {
						R.id.ItemImage, R.id.ItemText });
		return saImageItems;
	}
}
