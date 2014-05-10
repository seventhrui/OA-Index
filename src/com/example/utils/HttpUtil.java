package com.example.utils;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

/**
 * Http������
 */
public class HttpUtil {
	private static String resultstring = "";
	public static String downloadString(String urlPath) {
		FinalHttp fh = new FinalHttp();
		fh.get(urlPath, new AjaxCallBack<String>() {
			
			@Override
			public void onLoading(long count, long current) { // ÿ5�����Զ����ص�һ�Σ�ͨ��progress�Ƿ�ص�onLoading�ͻص�Ƶ��
				resultstring = current + "/" + count;
			}
			@Override
			public void onSuccess(String t) {
				resultstring = t == null ? "null" : t;
			}
		}.progress(true, 5));// ͨ����������onloading��Ƶ��
		return resultstring;
	}
}
