/**
 * �û�UserBean
 */
package com.example.beans;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;
/**
 * userid ��ϵ��id<br/>
 * username ��ϵ������<br/>
 * userorga ��ϵ�������������<br/>
 *
 */
@Table(name = "oa_users")
public class UserBean {
	@Id(column="usersid")//�Զ�����������
	private String usersid;//��ϵ�˱��
	private String username;//��ϵ������
	private String userorga;//��ϵ�������������
	public UserBean(){
		
	}
	public UserBean(String name,String id,String orga){
		this.username=name;
		this.usersid=id;
		this.userorga=orga;
	}
	
	
	public String getUsersid() {
		return usersid;
	}
	public void setUsersid(String userid) {
		this.usersid = userid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUserorga() {
		return userorga;
	}
	public void setUserorga(String userorga) {
		this.userorga = userorga;
	}
	@Override
	public String toString() {
		
		return "�û�����"+this.username+"�û���ţ�"+this.usersid;
	}
	
}
