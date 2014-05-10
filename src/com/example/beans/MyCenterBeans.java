/**
 * 个人中心<br/>
 * 功能<br/>
 * 记录总数<br/>
 * 未看条数<br/>
 */
package com.example.beans;

public class MyCenterBeans {
	private String mycenter_type;
	private int mycenter_unread;
	public MyCenterBeans(){
		
	}
	public MyCenterBeans(String type,int unread){
		this.mycenter_type=type;
		this.mycenter_unread=unread;
	}
	public String getMycenter_type() {
		return mycenter_type;
	}
	public void setMycenter_type(String mycenter_type) {
		this.mycenter_type = mycenter_type;
	}
	public int getMycenter_unread() {
		return mycenter_unread;
	}
	public void setMycenter_unread(int mycenter_unread) {
		this.mycenter_unread = mycenter_unread;
	}
	
}
