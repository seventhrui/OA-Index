/**
 * �û�UserBean
 */
package com.example.beans;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;
@Table(name = "oa_users")
public class UserBean {
	@Id(column="userid")//�Զ�����������
	private String userid;//��ϵ�˱��
	private String username;//��ϵ������
	private String userorga;//��ϵ�������������
	public UserBean(){
		
	}
	public UserBean(String name,String id,String orga){
		this.username=name;
		this.userid=id;
		this.userorga=orga;
	}
	
	
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
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
		
		return "�û�����"+this.username+"�û���ţ�"+this.userid;
	}
	
}
