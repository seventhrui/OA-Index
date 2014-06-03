package com.example.adapter;

import java.util.List;

import com.example.beans.MyNoticeBean;
import com.example.oa_index.R;
import com.example.utils.StringUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * ���������б�adapter
 */
public class NoticeListAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater layoutInflater;
	private List<MyNoticeBean> list;

	// ���췽��������list���ݵľ�����һ�����ݵ���Ϣ
	public NoticeListAdapter(Context context, List<MyNoticeBean> list) {
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
					R.layout.item_listview_messagelist, null);

			// tv_message_id ����ȷ����ϢΨһ
			TextView tv_notice_id = (TextView) convertView
					.findViewById(R.id.tv_message_id);
			TextView tv_notice_sender = (TextView) convertView
					.findViewById(R.id.tv_message_sender);
			TextView tv_notice_read = (TextView) convertView
					.findViewById(R.id.tv_message_read);
			TextView tv_notice_title = (TextView) convertView
					.findViewById(R.id.tv_message_title);
			TextView tv_notice_content = (TextView) convertView
					.findViewById(R.id.tv_message_content);

			tv_notice_id.setText(list.get(position).getNotice_id());
			if (list.get(position).getNotice_sender().isEmpty())
				tv_notice_sender.setText("����");
			else
				tv_notice_sender.setText(list.get(position).getNotice_sender());
			if (list.get(position).getNotice_state().equals("0"))
				tv_notice_read.setVisibility(View.VISIBLE);
			tv_notice_title.setText(list.get(position).getNotice_title());
			if (list.get(position).getNotice_content().isEmpty())
				tv_notice_content.setText("������");
			else
				tv_notice_content.setText(StringUtils.Html2Text(list.get(
						position).getNotice_content()));
		}
		return convertView;
	}
}
