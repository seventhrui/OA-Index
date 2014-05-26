package com.example;
/**
 * 消息id
 *
 */
public class HandlerCode {
	/**
	 * 下载信息
	 */
	public final static int DOWNLOAD_MESSAGE_BEGIN=10;
	/**
	 * 下载信息成功
	 */
	public final static int DOWNLOAD_MESSAGE_SUCCESS=11;
	/**
	 * 下载信息失败
	 */
	public final static int DOWNLOAD_MESSAGE_FAILURE=-11;
	/**
	 * 保存信息数据
	 */
	public final static int DATABASE_MESSAGE_SAVE=12;
	/**
	 * 开始发送信息
	 */
	public final static int SEND_MESSAGE_BEGIN=20;
	/**
	 * 发送信息成功
	 */
	public final static int SEND_MESSAGE_SUCCESS=21;
	/**
	 * 发送信息失败
	 */
	public final static int SEND_MESSAGE_FAILURE=-21;
	/**
	 * 发送文件成功
	 */
	public final static int SEND_FILE_SUCCESS=23;
	/**
	 * 发送文件失败
	 */
	public final static int SEND_FILE_FAILURE=-23;
	/**
	 * 连接超时
	 */
	public final static int CONNECTION_TIMEOUT=5;
	/**
	 * 保存草稿
	 */
	public final static int SAVE_MESSAGE_DRAFT=26;
	
	/**
	 * 下载通知通告
	 */
	public final static int DOWNLOAD_NOTICE_BEGIN=30;
	/**
	 * 下载通知通告成功
	 */
	public final static int DOWNLOAD_NOTICE_SUCCESS=31;
	/**
	 * 下载通知通告失败
	 */
	public final static int DOWNLOAD_NOTICE_FAILURE=-31;
	/**
	 * 保存通知通告数据
	 */
	public final static int DATABASE_NOTICE_SAVE=32;
	/**
	 * 发送通知通告开始
	 */
	public final static int SEND_NOTICE_BEGIN=33;
	/**
	 * 发送通知通告成功
	 */
	public final static int SEND_NOTICE_SUCCESS=34;
	/**
	 * 发送通知通告失败
	 */
	public final static int SEND_NOTICE_FAILURE=35;
	
	/**
	 * 删除
	 */
	public final static int DELETE=7;
	/**
	 * 展示数据
	 */
	public final static int SHOWDATA=8;
}
