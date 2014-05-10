/**
 * 用户UserBean
 */
package com.example.beans;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;
@Table(name = "oa_users")
public class UserBean {
	@Id(column="userid")//自定义主键名称
	private String userid;//联系人编号
	private String username;//联系人名称
	private String userorga;//联系人所属机构编号
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
		
		return "用户名："+this.username+"用户编号："+this.userid;
	}
	
}
