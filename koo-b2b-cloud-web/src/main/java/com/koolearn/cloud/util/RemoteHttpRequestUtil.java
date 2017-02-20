package com.koolearn.cloud.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class RemoteHttpRequestUtil {
	
	public static final String REQUEST_TYPE_GET = "GET";
	
	public static final String REQUEST_TYPE_POST = "POST";
	
	public static String getHttpresponseData(
			String remoteUrl,
			List<String> paramKey,
			List<String> paramValue,
			String requestType){
		
		String context = null;
		
		if(paramKey.size()!=paramValue.size()){
			throw new RuntimeException("参数错误!");
		}
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		for (int i=0;i<paramKey.size();i++) {
			String key = paramKey.get(i);
			String value = paramValue.get(i);
			nvps.add(new BasicNameValuePair(key,value));
		}
		HttpClient httpclient=new DefaultHttpClient();
		try {
			httpclient.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,20000);
			httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,20000);
			if("POST".equals(requestType)){
				HttpPost httpPost=new HttpPost(remoteUrl);
				httpPost.setEntity(new UrlEncodedFormEntity(nvps,"UTF-8"));
				HttpResponse response=httpclient.execute(httpPost);
				HttpEntity entity =response.getEntity();//获取服务器返回页面的值
				context=EntityUtils.toString(entity,"UTF-8");
				httpPost.abort();
			}else{
				HttpGet httpGet = new HttpGet(remoteUrl);
				for (int i=0;i<paramKey.size();i++) {
					String key = paramKey.get(i);
					String value = paramValue.get(i);
					httpclient.getParams().setParameter(key, value);
				}
				HttpResponse response=httpclient.execute(httpGet);
				HttpEntity entity =response.getEntity();//获取服务器返回页面的值
				context=EntityUtils.toString(entity,"UTF-8");
				httpGet.abort();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			httpclient.getConnectionManager().shutdown();
		}
		return context;
	}
	
	//测试
	public static void main (String[] args){
		//String  reqUrl= "http://apiproduct.neibu.koolearn.com/cms/searchteacher";		
		//String  appKey="v5appkey_hdp_@#$kvf";
		//String  appid="5001";
		
		String  reqUrl= "http://ip.koolearn.com/info";
		
		//String  sign = SignUtil.getSign(appKey, appid); //生成教师接口签名
		
		List<String> key = new ArrayList<String>();
		//key.add("sign");
        //key.add("page");
        //key.add("pagesize");
        //key.add("keyword");
		
		key.add("ips");
        
        List<String> val = new ArrayList<String>();
        //val.add(sign);
        //val.add("1");
       //val.add("5");
        //val.add("刘");
        val.add("221.13.128.0");
        
        String jsonData = RemoteHttpRequestUtil.getHttpresponseData(reqUrl,key,val,RemoteHttpRequestUtil.REQUEST_TYPE_POST);
        System.out.println(jsonData);
       // Map<Object,Object> object = JSONObject.fromObject(jsonData);
        
	}

	public static String getHttpResponseData(String remoteUrl,Map<String, String> map, String requestType) {
		String context = null;
		if(map==null&&map.size()<=0){
			throw new RuntimeException("没有参数!");
		}
		HttpClient httpclient=new DefaultHttpClient();
		try {
			httpclient.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,20000);
			httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,20000);
			if("POST".equals(requestType)){
				List<NameValuePair> nvps = new ArrayList<NameValuePair>();
				for (Iterator<String> i = map.keySet().iterator(); i.hasNext(); ) {
					String key = i.next();
					String value = map.get(key);
					nvps.add(new BasicNameValuePair(key,value));
				}
				HttpPost httpPost=new HttpPost(remoteUrl);
				httpPost.setEntity(new UrlEncodedFormEntity(nvps,"UTF-8"));
				HttpResponse response=httpclient.execute(httpPost);
				HttpEntity entity =response.getEntity();//获取服务器返回页面的值
				context=EntityUtils.toString(entity,"UTF-8");
				httpPost.abort();
			}else{
				HttpGet httpGet = new HttpGet(remoteUrl);
				for (Iterator<String> i = map.keySet().iterator(); i.hasNext(); ) {
					String key = i.next();
					String value = map.get(key);
					httpclient.getParams().setParameter(key, value);
				}
				HttpResponse response=httpclient.execute(httpGet);
				HttpEntity entity =response.getEntity();//获取服务器返回页面的值
				context=EntityUtils.toString(entity,"UTF-8");
				httpGet.abort();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			httpclient.getConnectionManager().shutdown();
		}
		return context;
	}
	
	
	
}
