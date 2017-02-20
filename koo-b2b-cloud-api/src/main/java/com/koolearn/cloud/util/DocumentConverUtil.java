package com.koolearn.cloud.util;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * 文档转换工具
 * @author gehaisong
 *
 */
public class DocumentConverUtil {
	/**
	 * 获得文件转换URL. FileUploadUtils.getConvertUrl(....)
	 *   转换id
	 *    1 Conversion 转换请求   2  Query 转换状态查询
	 *  filePath
	 *            需要转换的文件
	 *  desFolder
	 *            转换后的文件存放的位置
	 *  CallBack  回调（回调返回参数： id：转换ID   status：转换状态，1为成功，0为失败    ret 其他返回）
	 *转换接口
	 * http://app2013:8081/StartConvert.ashx?Action=Conversion
	 *    &ID=0010
	 *    &Key=12345678
	 *    &FilePath=\\server\docs\folder\file.docx
	 *    &DesFolder=\\server\pdfdocs\folder\myfile
	 *    &CallBack=xxx
	 *查询状态：
	 *http://app2013:8081/StartConvert.ashx?Action=Query
	 *    &ID=0010
	 *    &Key=12345678
	 * @return
	 */
	 public static String transFile(String convertUrl) throws Exception {
			// 构造HttpClient的实例
			HttpClient httpClient = new HttpClient();
			// 创建GET方法的实例dwa1.koolearn.com:8080
			GetMethod getMethod = new GetMethod(convertUrl);
			// 使用系统提供的默认的恢复策略
			getMethod.getParams().setParameter(
					HttpMethodParams.RETRY_HANDLER,
					new DefaultHttpMethodRetryHandler());
			String msg = "";
			try {
				// 执行getMethod
				int statusCode = httpClient.executeMethod(getMethod);
				// 读取内容
				String responseBody = new String(getMethod.getResponseBody());
				// 处理内容
				if (responseBody.contains("Not Found")) {
					msg = "文档格式不对，不能转换！";
//					bean.setConvStatus(-1);
				}else if (responseBody.contains("Send to Conversion Queue Error")) {
					msg = "文档未能添加至转换队列，请重新上传！";
				}else if (responseBody.contains("Waiting in the Queue")) {
					msg = "转换中！";
				}else if (responseBody.contains("Null Argument")) {
					msg = "参数为空！";
				}else {
					msg = "转换中！";//默认转换中，等候回调时更改状态
//					bean.setConvStatus(2);
				}
			} catch (Exception e) {
//				bean.setConvStatus(-1);
				msg = "网络异常!";
				e.printStackTrace();
			} finally {
				// 释放连接
				getMethod.releaseConnection();
			}
//			dao.update(bean);//更新
			return msg;
	    }
	 public static int doConver(String path) {
			// 创建一个URL对象
			URL url;
			try {

				url = new URL(path);
				// 创建一个Http连接
				HttpURLConnection urlcon = (HttpURLConnection) url.openConnection();
				urlcon.setConnectTimeout(10000);
				urlcon.connect();

				int state = urlcon.getResponseCode();

				System.out.println( "状态："+state);

				return state;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return 0;
			}
		}
public static void main(String[] args) throws Exception {
    String s = "http://owa1.neibu.koolearn.com:8083/StartConvert.ashx";
        String s1 = "?Action=Conversion&Key=cloud-12345&ID=25257&" +
                "FilePath="+URLEncoder.encode("\\\\192.168.102.198\\share/cloud/upload/1/48681422/1460442664718.docx","utf-8") +
                "&DesFolder="+ URLEncoder.encode("\\\\192.168.102.198\\share/cloud/upload/1/0/25257", "utf-8");
        String s2 = "&CallBack='http://192.168.102.198/teacher/resource/converCallBack?t=t'";
        System.out.println(s1+s2);
        DocumentConverUtil.transFile(s+s1+s2);



    String converUrl="http://owa1.neibu.koolearn.com:8081/StartConvert.ashx";
	String prama="?Action=Conversion&Key=12345678&ID=46";
	String filePath="&FilePath=\\\\192.168.102.232\\share/k12/upload/8/46674280/1400228423858.pdf";
	String dir="&DesFolder=\\\\192.168.102.232\\share/k12/upload/8/0/46";
	String call="&CallBack=http://localzxx.koolearn.com:8053/sys/resourceOpt/converCallBack";
	String url="http://owa1.neibu.koolearn.com/:8081/StartConvert.ashx?Action=Conversion&Key=12345678&ID=44&FilePath=\\\\192.168.100.105\\share\\k12\\upload/1/46674090/1400222729484.docx&DesFolder=\\\\192.168.100.105\\share\\k12\\upload\\1\\0\\44&CallBack=http://localzxx.koolearn.com:8053/k12cloud/resourceOpt/converCallBack";
	try {

	} catch (Exception e) {
		e.printStackTrace();
	}
}
}
