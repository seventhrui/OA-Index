package com.example.mytree;
/**
 * 联系人节点资源 <br/>
 * 父节点id
 * 标题
 * 值
 * 图片
 */
public class NodeResource {
	protected String parentId;
	protected String title;
	protected String value;
	protected int iconId;
	protected String curId;

	public NodeResource(String parentId, String curId, String title,
			String value, int iconId) {
		super();
		this.parentId = parentId;
		this.title = title;
		this.value = value;
		this.iconId = iconId;
		this.curId = curId;
	}

	public String getParentId() {
		return parentId;
	}

	public String getTitle() {
		return title;
	}

	public String getValue() {
		return value;
	}

	public int getIconId() {
		return iconId;
	}

	public String getCurId() {
		return curId;
	}

}
