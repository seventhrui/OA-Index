/**
 * StringUtil
 */
package com.example.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Pattern;

import android.util.Log;


public class StringUtils {
	/**
	 * 过滤String中的html标签
	 * @param inputString
	 * @return
	 */
	public static String Html2Text(String inputString) {
		String htmlStr = inputString; // 含html标签的字符串
		String textStr = "";
		java.util.regex.Pattern p_script;
		java.util.regex.Matcher m_script;
		java.util.regex.Pattern p_style;
		java.util.regex.Matcher m_style;
		java.util.regex.Pattern p_html;
		java.util.regex.Matcher m_html;
		java.util.regex.Pattern p_space;
		java.util.regex.Matcher m_space;
		try {
			String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; // 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
			String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; // 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
			String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
			String regEx_space = "[ \\t\\n\\x0B\\f\\r&nbsp;]"; // 定义空白字符的正则表达式
			p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
			m_script = p_script.matcher(htmlStr);
			htmlStr = m_script.replaceAll(""); // 过滤script标签
			p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
			m_style = p_style.matcher(htmlStr);
			htmlStr = m_style.replaceAll(""); // 过滤style标签
			p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
			m_html = p_html.matcher(htmlStr);
			htmlStr = m_html.replaceAll(""); // 过滤html标签
			p_space = Pattern.compile(regEx_space,Pattern.CASE_INSENSITIVE);
			m_space = p_space.matcher(htmlStr);
			htmlStr = m_space.replaceAll("");// 过滤空白字符
			textStr = htmlStr;
		} catch (Exception e) {
			System.err.println("Html2Text: " + e.getMessage());
		}
		return textStr;// 返回文本字符串
	}
	/**
	 * 文件路径转文件名
	 * @param filepath
	 * @return
	 */
	public static String Path2FileName(String filepath){
		String filename="";
		filename=filepath.substring(filepath.lastIndexOf("/")+1, filepath.length());
		return filename;
	}
	
	/**
	 * 按竖线拆分
	 * @param s
	 * @return
	 */
	public static String[] splitStringByLine(String s){
		Log.v("调试", "分割数据");
		return s.split("\\|");
	}
	public static String[] splitStringByNot(String s){
		Log.v("调试", "分割数据");
		return s.split("\\^");
	}
	
	/**
	 * 把inputstream的信息 转化成byte[] 返回
	 * @param is
	 * @return
	 * @throws Exception
	 */
	public static byte[] getStreamBytes(InputStream is) throws Exception{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while( (len = is.read(buffer))!=-1 ){
			baos.write(buffer, 0, len);
		}
		byte[] result = baos.toByteArray();
		is.close();
		baos.close();
		return result;
	}
	/**
	 * 生成随机码--21+length
	 * @param length
	 * @return
	 */
	public static String GenerateGUID(int length) {
		UUID uuid = UUID.randomUUID();
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
		String date = sDateFormat.format(new java.util.Date());
		return date + uuid.toString().substring(0, 7) + randomString(length);
	}
	public static String randomString(int length) {
		Random randGen=null;
		char[] numbersAndLetters = null;
		if (length < 1) {
			return null;
		}
		
		if (randGen == null) {
			randGen = new Random();
			numbersAndLetters = ("0123456789abcdefghijklmnopqrstuvwxyz"
					+ "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();
		}
		char[] randBuffer = new char[length];
		for (int i = 0; i < randBuffer.length; i++) {
			randBuffer[i] = numbersAndLetters[randGen.nextInt(71)];
		}
		return new String(randBuffer);
	}
	/**
	 * 当前时间毫秒
	 * @return
	 */
	public static String TimeString(){
		Date dt= new Date();    
		Long time= dt.getTime();
		return time+"";
	}
}
