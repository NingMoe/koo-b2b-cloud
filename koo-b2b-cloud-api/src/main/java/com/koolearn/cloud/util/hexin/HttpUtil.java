package com.koolearn.cloud.util.hexin;

/**
 * Created by gehaisong on 2016/9/8.
 */
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.DigestException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator
 *
 */
public class HttpUtil {
    /**
     * 使用Get方式获取数据
     *
     * @param url
     *            URL包括参数，http://HOST/XX?XX=XX&XXX=XXX
     * @param charset
     * @return
     */
    public static String sendGet(String url, String charset) {
        String result = "";
        BufferedReader in = null;
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(), charset));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    /**
     * POST请求，字符串形式数据
     * @param url 请求地址
     * @param param 请求数据
     * @param charset 编码方式
     */
    public static String sendPostUrl(String url, String param, String charset) {

        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    conn.getInputStream(), charset));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }
    /**
     * POST请求，发送Map形式数据
     * @param url 请求地址
     * @param param 请求数据
     * @param charset 编码方式
     */
    public static String sendPost(String url, Map<String, String> param,
                                  String charset) {

        StringBuffer buffer = new StringBuffer();
        if (param != null && !param.isEmpty()) {
            for (Map.Entry<String, String> entry : param.entrySet()) {
                buffer.append(entry.getKey()).append("=")
                        .append(URLEncoder.encode(entry.getValue()))
                        .append("&");

            }
        }
        buffer.deleteCharAt(buffer.length() - 1);

        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(buffer);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    conn.getInputStream(), charset));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }
    /**
     * 发送HttpPost请求
     * 统一使用json格式，http返回的Content-Type为application/json
     * @param url  服务地址
     * @param paramsJson 提交的数据： {"access_key":"75d140f4","name":"ghsfs000","nonce":"fminy","number":"ghsfs000","phone":"12546897856","signature":"ecceae426e15a04bc45a1d41c1a537f1f36d35e9","subjects":["初中数学","高中数学"],"timestamp":"1473734485","uid":"63545623"}
     * @return {"code": 0, "message": null, "data": null}
     *      状态码code=0表示正常返回，此时message为null，data为返回值
            状态码code>0表示异常，此时data为null，message为异常信息
            其中当code=4时，表示access_key和access_secret认证失败
     */
    public static String senPostJson(String url, String paramsJson) {
            PrintWriter out = null;
            BufferedReader in = null;
            String result = "";
        try {
            URL realurl = new URL(url);// 创建连接
            HttpURLConnection connection = (HttpURLConnection) realurl
                    .openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestMethod("POST"); // 设置请求方式
            connection.setRequestProperty("Accept", "application/json"); // 设置接收数据的格式
            connection.setRequestProperty("Content-Type", "application/json"); // 设置发送数据的格式
            connection.connect();
            out = new PrintWriter(connection.getOutputStream());
            out.append(paramsJson);
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            // 使用finally块来关闭输出流、输入流
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }
    /************合心接口***************************/
    public static void main(String[] args) {
        try {
            SHA1Entity shaEntity=SHA1Util.getSHA1Signature(SHA1Util.default_ACCESS_KEY,SHA1Util.default_ACCESS_SECRET);
            Map<String,String> param=new HashMap<String, String>();
            param.put("uid","");
            param.put("redirect","/t");
            param.put("access_key",shaEntity.getAccessKey());
            param.put("timestamp",shaEntity.getTimestamp());
            param.put("nonce",shaEntity.getNonce());
            param.put("signature",shaEntity.getSignature());
            //http://kaoyue.koolearn.com/api/open/login?access_key=abcd1234&timestamp=1469414300&nonce=abc&signature=XXX&redirect=/t
//            String result=HttpUtil.sendPost("http://kaoyue.koolearn.com/api/open/login",param,"utf-8");
//            System.out.println(result);
//            System.out.println(StringUtils.isNumeric("334579"));
        } catch (DigestException e) {
            e.printStackTrace();
        }
    }
}