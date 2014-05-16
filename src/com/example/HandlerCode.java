package com.example;
/**
 * 消息id
 *
 */
public class HandlerCode {
	/**
	 * 下载信息
	 */
	public final static int DOWNLOAD_MESSAGE_BEGIN=0;
	/**
	 * 下载信息成功
	 */
	public final static int DOWNLOAD_MESSAGE_SUCCESS=1;
	/**
	 * 下载信息失败
	 */
	public final static int DOWNLOAD_MESSAGE_FAILURE=-1;
	/**
	 * 保存信息数据
	 */
	public final static int DATABASE_MESSAGE_SAVE=2;
	/**
	 * 开始发送信息
	 */
	public final static int SEND_MESSAGE_BEGIN=0;
	/**
	 * 发送信息成功
	 */
	public final static int SEND_MESSAGE_SUCCESS=1;
	/**
	 * 发送信息失败
	 */
	public final static int SEND_MESSAGE_FAILURE=-1;
	/**
	 * 发送文件成功
	 */
	public final static int SEND_FILE_SUCCESS=3;
	/**
	 * 发送文件失败
	 */
	public final static int SEND_FILE_FAILURE=-3;
	/**
	 * 连接超时
	 */
	public final static int CONNECTION_TIMEOUT=5;
	/**
	 * 保存草稿
	 */
	public final static int SAVE_MESSAGE_DRAFT=6;
}
