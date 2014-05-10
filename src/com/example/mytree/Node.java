package com.example.mytree;

import java.util.ArrayList;
import java.util.List;

public class Node {
	private Node parent = null; // ���ڵ�
	private List<Node> childrens = new ArrayList<Node>();// �ӽڵ�
	private String title;// �ڵ���ʾ����
	private String value;// �ڵ���ʾֵ
	private int icon = -1; // icon(R.drawable��id)
	private boolean isChecked = false; // �Ƿ�ѡ��
	private boolean isExpand = true;// �Ƿ�����չ״̬
	private boolean hasCheckBox = true;// �Ƿ��и�ѡ��
	private String parentId = null;
	private String curId = null;
	private boolean isVisiable = true;

	/**
	 * ���ýڵ�ֵ
	 * 
	 * @param parentId
	 *            TODO
	 * @param curId
	 *            TODO
	 */
	public Node(String title, String value, String parentId, String curId,
			int iconId) {
		// TODO Auto-generated constructor stub
		this.title = title;
		this.value = value;
		this.parentId = parentId;
		this.icon = iconId;
		this.curId = curId;
	}

	/**
	 * �õ����ڵ�
	 * 
	 * @return
	 * 
	 */
	public Node getParent() {
		return parent;
	}

	/**
	 * ���ø��ڵ�
	 * 
	 * @param parent
	 * 
	 */
	public void setParent(Node parent) {
		this.parent = parent;
	}

	/**
	 * �õ��ӽڵ�
	 * 
	 * @return
	 * 
	 */
	public List<Node> getChildrens() {
		return childrens;
	}

	/**
	 * �Ƿ���ڵ�
	 * 
	 * @return
	 * 
	 */
	public boolean isRoot() {
		return parent == null ? true : false;
	}

	/**
	 * �Ƿ�����ͼ��
	 * 
	 * @return
	 * 
	 */
	public int getIcon() {
		return icon;
	}

	/**
	 * ����ͼ��
	 * 
	 * @param icon
	 * 
	 */
	public void setIcon(int icon) {
		this.icon = icon;
	}

	/**
	 * �Ƿ�ѡ��
	 * 
	 * @return
	 * 
	 */
	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	/**
	 * �Ƿ���չ��״̬
	 * 
	 * @return
	 * 
	 */
	public boolean isExplaned() {
		return isExpand;
	}

	/**
	 * ����չ��״̬
	 * 
	 * @param isExplaned
	 * 
	 */
	public void setExplaned(boolean isExplaned) {
		this.isExpand = isExplaned;
	}

	/**
	 * �Ƿ��и�ѡ��
	 * 
	 * @return
	 * 
	 */
	public boolean hasCheckBox() {
		return hasCheckBox;
	}

	/**
	 * �����Ƿ��и�ѡ��
	 * 
	 * @param hasCheckBox
	 * 
	 */
	public void setHasCheckBox(boolean hasCheckBox) {
		this.hasCheckBox = hasCheckBox;
	}

	/**
	 * �õ��ڵ����
	 * 
	 * @return
	 * 
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * ���ýڵ����
	 * 
	 * @param title
	 * 
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * �õ��ڵ�ֵ
	 * 
	 * @return
	 * 
	 */
	public String getValue() {
		return value;
	}

	/**
	 * ���ýڵ�ֵ
	 * 
	 * @param value
	 * 
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * ����һ���ӽڵ�
	 * 
	 * @param node
	 * 
	 */
	public void addNode(Node node) {
		if (!childrens.contains(node)) {
			childrens.add(node);
		}
	}

	/**
	 * �Ƴ�һ���ӽڵ�
	 * @param node
	 * 
	 */
	public void removeNode(Node node) {
		if (childrens.contains(node))
			childrens.remove(node);
	}

	/**
	 * �Ƴ�ָ��λ�õ��ӽڵ�
	 * 
	 * @param location
	 * 
	 */
	public void removeNode(int location) {
		childrens.remove(location);
	}

	/**
	 * ��������ӽڵ�
	 * 
	 */
	public void clears() {
		childrens.clear();
	}

	/**
	 * �жϸ����Ľڵ��Ƿ�ǰ�ڵ�ĸ��ڵ�
	 * 
	 * @param node
	 * @return
	 * 
	 */
	public boolean isParent(Node node) {
		if (parent == null)
			return false;
		if (parent.equals(node))
			return true;
		return parent.isParent(node);
	}

	/**
	 * �ݹ��ȡ��ǰ�ڵ㼶��
	 * 
	 * @return
	 * 
	 */
	public int getLevel() {
		return parent == null ? 0 : parent.getLevel() + 1;
	}

	/**
	 * ���ڵ��Ƿ����۵���״̬
	 * @return
	 * 
	 */
	public boolean isParentCollapsed() {
		if (parent == null)
			return false;
		if (!parent.isExplaned())
			return true;
		return parent.isParentCollapsed();
	}

	/**
	 * �Ƿ�Ҷ�ڵ㣨û��չ���¼��ļ��㣩
	 * @return
	 * 
	 */
	public boolean isLeaf() {
		return childrens.size() < 1 ? true : false;
	}

	/**
	 * �����Լ���id
	 * @return
	 **/
	public String getCurId() {
		// TODO Auto-generated method stub
		return curId;
	}

	/**
	 * ���صĸ�id
	 * @return
	 **/
	public String getParentId() {
		// TODO Auto-generated method stub
		return parentId;
	}
}
