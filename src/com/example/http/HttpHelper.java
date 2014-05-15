package com.example.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

import com.example.utils.StringUtils;

import android.util.Log;

/**
 * 
 */
public class HttpHelper {
	private static final String TAG = "uploadFile";
    private static final int TIME_OUT = 10*1000;   //��ʱʱ��
    private static final String CHARSET = "utf-8"; //���ñ���
    
    private static String urlPath;
	public HttpHelper() {

	}

	public HttpHelper(String urlPath) {
		this.urlPath = urlPath;
	}

	/**
	 * doget
	 * 
	 * @return String
	 * @throws Exception
	 */
	public String doGetString() throws Exception {
		String result = "";
		HttpURLConnection conn = (HttpURLConnection) new URL(urlPath)
				.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Charset", "UTF-8");
		// �������ӳ�ʱ
		conn.setConnectTimeout(5 * 1000);
		conn.connect();
		int fileLength = conn.getContentLength();
		if (conn.getResponseCode() == 200 && fileLength > 0) {
			InputStreamReader inputStreamReader = new InputStreamReader(
					conn.getInputStream(), "UTF-8");
			BufferedReader reader = new BufferedReader(inputStreamReader);
			String data1 = null;
			StringBuffer stringBuffer = new StringBuffer();
			while ((data1 = reader.readLine()) != null) {
				stringBuffer.append(data1);
			}
			result = stringBuffer.toString();
		}
		return result;
	}

	/**
	 * dopost
	 * 
	 * @param poststring
	 * @return String
	 * @throws Exception
	 */
	public String doPostString(String poststring) throws Exception {
		String result = "";
		URL url = new URL(urlPath);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5 * 1000);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		String content = poststring;
		byte[] data = content.getBytes();
		conn.setRequestProperty("Content-Length", data.length + "");
		conn.setDoOutput(true);
		conn.getOutputStream().write(data);

		if (conn.getResponseCode() == 200) {
			InputStream is = conn.getInputStream();
			byte[] resultdata = StringUtils.getStreamBytes(is);
			Log.i("----resultdata-----", new String(resultdata));
			result = new String(resultdata);
			Log.i("----redata-----", result + "LLLLLLLLLLL");
		} else
			result = conn.getResponseCode() + "";
		return result;
	}

	public static String uploadFile(File file) {
		String result = null;
		String BOUNDARY = UUID.randomUUID().toString(); // �߽��ʶ �������
		String PREFIX = "--", LINE_END = "\r\n";
		String CONTENT_TYPE = "multipart/form-data"; // ��������

		try {
			URL url = new URL(urlPath);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(10 * 1000);
			conn.setConnectTimeout(10 * 1000);
			conn.setDoInput(true); // ����������
			conn.setDoOutput(true); // ���������
			conn.setUseCaches(false); // ������ʹ�û���
			conn.setRequestMethod("POST"); // ����ʽ
			conn.setRequestProperty("Charset", "utf-8"); // ���ñ���
			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary="
					+ BOUNDARY);

			if (file != null) {
				/**
				 * ���ļ���Ϊ�գ����ļ���װ�����ϴ�
				 */
				DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
				StringBuffer sb = new StringBuffer();
				sb.append(PREFIX);
				sb.append(BOUNDARY);
				sb.append(LINE_END);
				/**
				 * �����ص�ע�⣺ name�����ֵΪ����������Ҫkey ֻ�����key �ſ��Եõ���Ӧ���ļ�
				 * filename���ļ������֣�������׺���� ����:abc.png
				 */
				sb.append("Content-Disposition: form-data; name=\"file\"; filename=\""
						+ file.getName() + "\"" + LINE_END);
				sb.append("Content-Type: application/octet-stream; charset="
						+ CHARSET + LINE_END);
				sb.append(LINE_END);
				dos.write(sb.toString().getBytes());
				InputStream is = new FileInputStream(file);
				byte[] bytes = new byte[1024];
				int len = 0;
				while ((len = is.read(bytes)) != -1) {
					dos.write(bytes, 0, len);
				}
				is.close();
				dos.write(LINE_END.getBytes());
				byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END)
						.getBytes();
				dos.write(end_data);
				dos.flush();
				/**
				 * ��ȡ��Ӧ�� 200=�ɹ� ����Ӧ�ɹ�����ȡ��Ӧ����
				 */
				int res = conn.getResponseCode();
				Log.e(TAG, "response code:" + res);
				Log.e(TAG, "request success");
				InputStream input = conn.getInputStream();
				StringBuffer sb1 = new StringBuffer();
				int ss;
				while ((ss = input.read()) != -1) {
					sb1.append((char) ss);
				}
				result = sb1.toString();
				Log.e(TAG, "result : " + result);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public String getUrlPath() {
		return urlPath;
	}

	public void setUrlPath(String urlPath) {
		this.urlPath = urlPath;
	}
}
