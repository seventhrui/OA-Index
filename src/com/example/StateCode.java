package com.example;
/**
 * 状态码
 *
 */
public class StateCode {
	/**
	 * 收件
	 */
	public final static String MESSAGE_TYPE_RECEIVE="1";
	/**
	 * 发件
	 */
	public final static String MESSAGE_TYPE_SEND="2";
	/**
	 * 草稿
	 */
	public final static String MESSAGE_TYPE_DRAFT="3";
	/**
	 * 回收
	 */
	public final static String MESSAGE_TYPE_WASTE="4";
	/**
	 * 全部收件信息
	 */
	public final static int STATE_MESSAGE_ALL=0;
	/**
	 * 已读收件
	 */
	public final static int STATE_MESSAGE_UNREAD=1;
	/**
	 * 未读收件
	 */
	public final static int STATE_MESSAGE_READ=2;
}
