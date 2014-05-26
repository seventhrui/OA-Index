package com.example.beans;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

/**
 * 通知通告
 * 0.通知id <br/>
 * 1.发布人 <br/>
 * 2.发布时间  <br/>
 * 3.标题  <br/>
 * 4.内容  <br/>
 * 5.是否有附件  0无 1有<br/>
 * 6.附件名  <br/>
 * 7.是否查看 0未看 1已看<br/>
 * 8.通知类型 1收件 2发件 3草稿
 */
@Table(name = "oa_notices")//设置表名
public class MyNoticeBean {
	@Id(column="notice_id")
	public String notice_id;
	public String notice_sender;
	public String notice_sendtime;
	public String notice_title;
	public String notice_content;
	public String notice_hasfile;
	public String notice_filename;
	public String notice_state;
	public String notice_type;
	public MyNoticeBean(){
		
	}
	public MyNoticeBean(String id,String sender,String sendtime,String title,String content,String hasfile,String filename,String state,String type){
		this.notice_id=id;
		this.notice_sender=sender;
		this.notice_sendtime=sendtime;
		this.notice_title=title;
		this.notice_content=content;
		this.notice_hasfile=hasfile;
		this.notice_filename=filename;
		this.notice_state=state;
		this.notice_type=type;
	}
	public String getNotice_id() {
		return notice_id;
	}
	public void setNotice_id(String notice_id) {
		this.notice_id = notice_id;
	}
	public String getNotice_sender() {
		return notice_sender;
	}
	public void setNotice_sender(String notice_sender) {
		this.notice_sender = notice_sender;
	}
	public String getNotice_sendtime() {
		return notice_sendtime;
	}
	public void setNotice_sendtime(String notice_sendtime) {
		this.notice_sendtime = notice_sendtime;
	}
	public String getNotice_title() {
		return notice_title;
	}
	public void setNotice_title(String notice_title) {
		this.notice_title = notice_title;
	}
	public String getNotice_content() {
		return notice_content;
	}
	public void setNotice_content(String notice_content) {
		this.notice_content = notice_content;
	}
	public String getNotice_hasfile() {
		return notice_hasfile;
	}
	public void setNotice_hasfile(String notice_hasfile) {
		this.notice_hasfile = notice_hasfile;
	}
	public String getNotice_filename() {
		return notice_filename;
	}
	public void setNotice_filename(String notice_filename) {
		this.notice_filename = notice_filename;
	}
	public String getNotice_state() {
		return notice_state;
	}
	public void setNotice_state(String notice_state) {
		this.notice_state = notice_state;
	}
	public String getNotice_type() {
		return notice_type;
	}
	public void setNotice_type(String notice_type) {
		this.notice_type = notice_type;
	}
	
}
