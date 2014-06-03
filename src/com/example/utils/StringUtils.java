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
	 * ����String�е�html��ǩ
	 * @param inputString
	 * @return
	 */
	public static String Html2Text(String inputString) {
		String htmlStr = inputString; // ��html��ǩ���ַ���
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
			String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; // ����script��������ʽ{��<script[^>]*?>[\\s\\S]*?<\\/script>
			String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; // ����style��������ʽ{��<style[^>]*?>[\\s\\S]*?<\\/style>
			String regEx_html = "<[^>]+>"; // ����HTML��ǩ��������ʽ
			String regEx_space = "[ \\t\\n\\x0B\\f\\r&nbsp;]"; // ����հ��ַ���������ʽ
			p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
			m_script = p_script.matcher(htmlStr);
			htmlStr = m_script.replaceAll(""); // ����script��ǩ
			p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
			m_style = p_style.matcher(htmlStr);
			htmlStr = m_style.replaceAll(""); // ����style��ǩ
			p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
			m_html = p_html.matcher(htmlStr);
			htmlStr = m_html.replaceAll(""); // ����html��ǩ
			p_space = Pattern.compile(regEx_space,Pattern.CASE_INSENSITIVE);
			m_space = p_space.matcher(htmlStr);
			htmlStr = m_space.replaceAll("");// ���˿հ��ַ�
			textStr = htmlStr;
		} catch (Exception e) {
			System.err.println("Html2Text: " + e.getMessage());
		}
		return textStr;// �����ı��ַ���
	}
	/**
	 * �ļ�·��ת�ļ���
	 * @param filepath
	 * @return
	 */
	public static String Path2FileName(String filepath){
		String filename="";
		filename=filepath.substring(filepath.lastIndexOf("/")+1, filepath.length());
		return filename;
	}
	
	/**
	 * �����߲��
	 * @param s
	 * @return
	 */
	public static String[] splitStringByLine(String s){
		Log.v("����", "�ָ�����");
		return s.split("\\|");
	}
	public static String[] splitStringByNot(String s){
		Log.v("����", "�ָ�����");
		return s.split("\\^");
	}
	
	/**
	 * ��inputstream����Ϣ ת����byte[] ����
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
	 * ���������--21+length
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
	 * ��ǰʱ�����
	 * @return
	 */
	public static String TimeString(){
		Date dt= new Date();    
		Long time= dt.getTime();
		return time+"";
	}
}
