package com.example.utils;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

/**
 * Http工具类
 */
public class HttpUtil {
	private static String resultstring = "";
	public static String downloadString(String urlPath) {
		FinalHttp fh = new FinalHttp();
		fh.get(urlPath, new AjaxCallBack<String>() {
			
			@Override
			public void onLoading(long count, long current) { // 每5秒钟自动被回调一次，通过progress是否回调onLoading和回调频率
				resultstring = current + "/" + count;
			}
			@Override
			public void onSuccess(String t) {
				resultstring = t == null ? "null" : t;
			}
		}.progress(true, 5));// 通过这里设置onloading的频率
		return resultstring;
	}
}
