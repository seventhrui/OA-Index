/**
 * 
 */
package com.example.http;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpHelper {
	private String urlPath;
	public HttpHelper(){
		
	}
	public HttpHelper(String urlPath){
		this.urlPath=urlPath;
	}
	public String readParse() throws Exception {
		String result = "";
		HttpURLConnection conn = (HttpURLConnection) new URL(urlPath)
				.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Charset", "UTF-8");
		// 设置连接超时
		conn.setConnectTimeout(10 * 1000);
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
	public String getUrlPath() {
		return urlPath;
	}
	public void setUrlPath(String urlPath) {
		this.urlPath = urlPath;
	}
}
