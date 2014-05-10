/**
 * ��֯����OrganizationBean
 */
package com.example.beans;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

@Table(name = "oa_organization")//���ñ���
public class OrganizationBean {
	@Id(column="organizationid")//��������
	public String organizationid;//��֯�������
	public String organizationname;//��֯��������
	public String organizationgdepth;//��֯�������
	public String organizationparent;//��֯�������ڵ���
	
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
		
		return "���ڵ��ţ�"+this.organizationparent+"�û���ţ�"+this.organizationid+"����"+this.organizationname;
	}
}
