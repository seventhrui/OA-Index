/**
 * 个人中心列表adapter
 */
package com.example.adapter;

import java.util.List;

import com.example.beans.MyMessageBean;
import com.example.oa_index.R;
import com.example.utils.StringUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MessageListAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater layoutInflater;
	private List<MyMessageBean> list;

	// 构造方法，参数list传递的就是这一组数据的信息
	public MessageListAdapter(Context context, List<MyMessageBean> list) {
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
					R.layout.item_list_messagelist, null);
		}
		//tv_message_id 用于确定信息唯一
		TextView tv_message_id = (TextView) convertView
				.findViewById(R.id.tv_message_id);
		TextView tv_message_sender = (TextView) convertView
				.findViewById(R.id.tv_message_sender);
		TextView tv_message_read=(TextView) convertView.findViewById(R.id.tv_message_read);
		TextView tv_message_title = (TextView) convertView
				.findViewById(R.id.tv_message_title);
		TextView tv_message_content = (TextView) convertView
				.findViewById(R.id.tv_message_content);
		
		tv_message_id.setText(list.get(position).getMessage_id());
		if(list.get(position).getMessage_sender().isEmpty())
			tv_message_sender.setText("匿名");
		else
			tv_message_sender.setText(list.get(position).getMessage_sender());
		if(list.get(position).getMessage_state().equals("0"))
			tv_message_read.setVisibility(View.VISIBLE);
		tv_message_title.setText(list.get(position).getMessage_title());
		if(list.get(position).getMessage_content().isEmpty())
			tv_message_content.setText("无内容");
		else
			tv_message_content.setText(StringUtils.Html2Text(list.get(position).getMessage_content()));
		return convertView;
	}
}
