/**
 * 收件箱列表adapter
 */
package com.example.adapter;

import java.util.List;

import com.example.beans.MyCenterBeans;
import com.example.oa_index.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyCenterListAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater layoutInflater;
	private List<MyCenterBeans> list;

	// 构造方法，参数list传递的就是这一组数据的信息
	public MyCenterListAdapter(Context context, List<MyCenterBeans> list) {
		this.context = context;
		layoutInflater = LayoutInflater.from(context);
		this.list = list;
	}

	@Override
	public int getCount() {
		return this.list != null ? this.list.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return this.list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = layoutInflater.inflate(
					R.layout.item_listview_mycenter, null);
		}
		// tv_message_id 用于确定信息唯一
		TextView tv_mycenter_funtype = (TextView) convertView
				.findViewById(R.id.tv_mycenter_funtype);
		TextView tv_mycenter_unread = (TextView) convertView
				.findViewById(R.id.tv_mycenter_unread);

		tv_mycenter_funtype.setText(list.get(position).getMycenter_type());
		if(list.get(position).getMycenter_unread()>0){
			tv_mycenter_unread.setVisibility(View.VISIBLE);
			tv_mycenter_unread.setText(list.get(position).getMycenter_unread()+"");
		}
		return convertView;
	}
}
