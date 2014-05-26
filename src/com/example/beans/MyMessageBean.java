package com.example.beans;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

/**
 * ��Ϣ MyMessageBean
 * 0.��Ϣid <br/>
 * 1.������ <br/>
 * 2.����ʱ��  <br/>
 * 3.����  <br/>
 * 4.����  <br/>
 * 5.�Ƿ��и���  0�� 1��<br/>
 * 6.������  <br/>
 * 7.�Ƿ�鿴 0δ�� 1�ѿ�<br/>
 * 8.��Ϣ���� 1�ռ� 2���� 3�ݸ�
 */
@Table(name = "oa_messages")//���ñ���
public class MyMessageBean {
	@Id(column="message_id")
	public String message_id;
	public String message_sender;
	public String message_sendtime;
	public String message_title;
	public String message_content;
	public String message_hasfile;
	public String message_filename;
	public String message_state;
	public String message_type;
	public MyMessageBean(){
		
	}
	public MyMessageBean(String id,String sender,String sendtime,String title,String content,String hasfile,String filename,String state,String type){
		this.message_id=id;
		this.message_sender=sender;
		this.message_sendtime=sendtime;
		this.message_title=title;
		this.message_content=content;
		this.message_hasfile=hasfile;
		this.message_filename=filename;
		this.message_state=state;
		this.message_type=type;
	}
	public String getMessage_id() {
		return message_id;
	}
	public void setMessage_id(String message_id) {
		this.message_id = message_id;
	}
	public String getMessage_sender() {
		return message_sender;
	}
	public void setMessage_sender(String message_addresser) {
		this.message_sender = message_addresser;
	}
	public String getMessage_sendtime() {
		return message_sendtime;
	}
	public void setMessage_sendtime(String message_sendtime) {
		this.message_sendtime = message_sendtime;
	}
	public String getMessage_title() {
		return message_title;
	}
	public void setMessage_title(String message_title) {
		this.message_title = message_title;
	}
	public String getMessage_content() {
		return message_content;
	}
	public void setMessage_content(String message_content) {
		this.message_content = message_content;
	}
	public String getMessage_hasfile() {
		return message_hasfile;
	}
	public void setMessage_hasfile(String message_hasfile) {
		this.message_hasfile = message_hasfile;
	}
	public String getMessage_filename() {
		return message_filename;
	}
	public void setMessage_filename(String message_filename) {
		this.message_filename = message_filename;
	}
	public String getMessage_state() {
		return message_state;
	}
	public void setMessage_state(String message_state) {
		this.message_state = message_state;
	}
	public String getMessage_type() {
		return message_type;
	}
	public void setMessage_type(String message_type) {
		this.message_type = message_type;
	}
	
}
