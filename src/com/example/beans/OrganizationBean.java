/**
 * 组织机构OrganizationBean
 */
package com.example.beans;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

@Table(name = "oa_organization")//设置表名
public class OrganizationBean {
	@Id(column="organizationid")//设置主键
	public String organizationid;//组织机构编号
	public String organizationname;//组织机构名称
	public String organizationgdepth;//组织机构深度
	public String organizationparent;//组织机构父节点编号
	
	public OrganizationBean(String name,String num,String depth,String parent){
		this.organizationname=name;
		this.organizationid=num;
		this.organizationgdepth=depth;
		this.organizationparent=parent;
	}
	
	public String getOrganizationid() {
		return organizationid;
	}

	public void setOrganizationid(String organizationid) {
		this.organizationid = organizationid;
	}

	public String getOrganizationname() {
		return organizationname;
	}

	public void setOrganizationname(String organizationname) {
		this.organizationname = organizationname;
	}

	public String getOrganizationgdepth() {
		return organizationgdepth;
	}

	public void setOrganizationgdepth(String organizationgdepth) {
		this.organizationgdepth = organizationgdepth;
	}

	public String getOrganizationparent() {
		return organizationparent;
	}

	public void setOrganizationparent(String organizationparent) {
		this.organizationparent = organizationparent;
	}

	@Override
	public String toString() {
		
		return "父节点编号："+this.organizationparent+"用户编号："+this.organizationid+"内容"+this.organizationname;
	}
}
