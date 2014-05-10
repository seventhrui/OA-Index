package com.example.mytree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.example.oa_index.R;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class TreeListView extends ListView {
	private ListView treelist = null;
	TreeAdapter ta = null;
	private List<Node> nodelist=null;
	
	public TreeListView(Context context, List<NodeResource> res) {
		super(context);
		treelist = this;
		treelist.setFocusable(false);
		treelist.setBackgroundColor(0xffffff);
		treelist.setFadingEdgeLength(0);
		treelist.setLayoutParams(new LayoutParams(
				ListView.LayoutParams.FILL_PARENT,
				ListView.LayoutParams.WRAP_CONTENT));
		treelist.setDrawSelectorOnTop(false);
		treelist.setCacheColorHint(0xffffff);
		treelist.setDivider(getResources().getDrawable(R.drawable.divider_list));
		treelist.setDividerHeight(2);
		treelist.setFastScrollEnabled(true);
		treelist.setScrollBarStyle(NO_ID);
		
		treelist.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.d("print", "�����");
				Node node=(Node) ta.getItem(position);
				Toast.makeText(getContext(), node.getValue(),Toast.LENGTH_SHORT).show();
				((TreeAdapter) parent.getAdapter()).ExpandOrCollapse(position);
				nodelist=ta.getSelectedNode();
				for(Node n:nodelist){
					Log.v("ѡ�еĽڵ�", n.getTitle()+","+n.getValue());
				}
			}
		});
		initNode(context, initNodRoot(res), true, -1, -1, 1);
	}

	/**
	 * 
	 * @param context
	 *            ��Ӧ������������
	 * @param root
	 *            �Ѿ��Һ����ĸ��ڵ�
	 * @param hasCheckBox
	 *            �Ƿ��������и�ѡ��
	 * @param tree_ex_id
	 *            չ��iconid -1��ʹ��Ĭ�ϵ�
	 * @param tree_ec_id
	 *            ����iconid -1��ʹ��Ĭ�ϵ�
	 * @param expandLevel
	 *            ��ʼչ���ȼ�
	 * 
	 */
	public List<Node> initNodRoot(List<NodeResource> res) {
		ArrayList<Node> list = new ArrayList<Node>();
		ArrayList<Node> roots = new ArrayList<Node>();
		Map<String, Node> nodemap = new HashMap<String, Node>();
		for (int i = 0; i < res.size(); i++) {
			NodeResource nr = res.get(i);
			Node n = new Node(nr.title, nr.value, nr.parentId, nr.curId,
					nr.iconId);
			nodemap.put(n.getCurId(), n);// ����map��
		}
		Set<String> set = nodemap.keySet();
		Collection<Node> collections = nodemap.values();
		Iterator<Node> iterator = collections.iterator();
		while (iterator.hasNext()) {// ������и��ڵ㵽root��
			Node n = iterator.next();
			if (!set.contains(n.getParentId()))
				roots.add(n);
			list.add(n);
		}
		for (int i = 0; i < list.size(); i++) {
			Node n = list.get(i);
			for (int j = i + 1; j < list.size(); j++) {
				Node m = list.get(j);
				if (m.getParentId().equals(n.getCurId())) {
					n.addNode(m);
					m.setParent(n);
				} else if (m.getCurId().equals(n.getParentId())) {
					m.addNode(n);
					n.setParent(m);
				}
			}
		}
		return roots;
	}

	public void initNode(Context context, List<Node> root, boolean hasCheckBox,
			int tree_ex_id, int tree_ec_id, int expandLevel) {
		ta = new TreeAdapter(context, root);
		// �����������Ƿ���ʾ��ѡ��
		ta.setCheckBox(true);
		// ����չ�����۵�ʱͼ��
		// ta.setCollapseAndExpandIcon(R.drawable.tree_ex, R.drawable.tree_ec);
		int tree_ex_id_ = (tree_ex_id == -1) ? R.drawable.tree_ex : tree_ex_id;
		int tree_ec_id_ = (tree_ec_id == -1) ? R.drawable.tree_ec : tree_ec_id;
		ta.setCollapseAndExpandIcon(tree_ex_id_, tree_ec_id_);
		// ����Ĭ��չ������
		ta.setExpandLevel(expandLevel);
		this.setAdapter(ta);
	}

	/* ���ص�ǰ����ѡ�нڵ��List���� */
	public List<Node> get() {
		return ta.getSelectedNode();
	}

}
