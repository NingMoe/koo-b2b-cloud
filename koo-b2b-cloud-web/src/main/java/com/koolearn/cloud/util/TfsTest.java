package com.koolearn.cloud.util;

import com.koolearn.tfs.client.Tfstool;

import java.io.*;
import java.util.UUID;

/**
 * Created by haozipu on 2016/7/27.
 */
public class TfsTest {

    public static void main(String[] args)throws Exception{
        Tfstool tfstool =Tfstool.getInstance();
        File file = new File("provider-exam-test.xml");
        file.createNewFile();
        OutputStream outputStream = new FileOutputStream(file);
        outputStream.write("testddddddddddddddddddddddddddddddddddd".getBytes());
        outputStream.flush();
        outputStream.close();
        InputStream io = new FileInputStream(new File("E:\\sns_源代码.rar"));
        int size =io.available();
        byte[] bb =new byte[size];
        io.read(bb);
        io.close();
        tfstool.writeFile("/"+ UUID.randomUUID().toString(),bb);

        Tfstool too = Tfstool.getInstance();

        boolean b = too.pushFile("E:\\sns_源代码.rar", "/log4j111.44");
        System.out.println(b);


//        Map<String, String> responseMap = new HashMap<String, String>();
//
//        try {
//
//            DecimalFormat myformat = new DecimalFormat("#########0.00");
//
//            String strAmount = String.valueOf(myformat.format(12.63));// 格式化，去掉多余小数
//
//            KooPayParamDto dto = new KooPayParamDto();
//            dto.setAmount(strAmount);
//            dto.setCharset("utf8");
//            dto.setCuryId("100");
//            dto.setNotifyUrl(SystemGlobals.getPreference("hosts.domain") + "/jsp/cart/pay/wechat_app_notify.jsp");// 后台通知接口
//            dto.setOutOrderNo("1111111111");// 订单id
//            dto.setPartnerId("90000008");
//            dto.setPayDesc("新东方网络课堂(" + "orderNo" + ")");
//            dto.setPaySubject("新东方网络课堂(" + "orderNo" + ")");
//            dto.setPayWayId("2000");// 微信支付方式
//            dto.setReturnUrl(SystemGlobals.getPreference("hosts.domain") + "/jsp/cart/pay/wechat_app_notify.jsp");// 页面跳转地址
//            dto.setService("pay_app");
//            dto.setSiteType(KooPayConstant.SITE_TYPE_M);
//            dto.setTradeType("APP");
//            dto.setSignKey("fdned93m48f833dbfd056f81ec988htl");
//            dto.setPayUrl(SystemGlobals.getPreference("hosts.pay"));
//            String url = new KooPayWeixinRedirect().kooPay(null, null, dto);
//
//
//
//            //String s = new .get(url, null);
//
//            String s= RemoteHttpRequestUtil.getHttpresponseData(url,new ArrayList<String>(),new ArrayList<String>(),RemoteHttpRequestUtil.REQUEST_TYPE_GET);
//			/* 2.验证签名 组装app需要的发往微信参数 */
//            KooPayAppResponseDto dtos = KooPayResponseUtil.createAppResponseDto(s);
//            dtos.setSignKey("fdned93m48f833dbfd056f81ec988htl");// 支付中心为业务系统分配的key
//            boolean isSign = KooPayResponseUtil.isAppSignMatching(dtos);// 验证签名
//            boolean isSucess = KooPayResponseUtil.isAppSuccess(dtos);// 验证交互是否成功
//            if (isSign && isSucess) {
//                responseMap.put("appid", "wx4748e6e0e1b6fae2");
//                responseMap.put("partnerid", "1338065001");
//                responseMap.put("prepayid", dtos.getWeixinPrepayId());
//                responseMap.put("package", "Sign=WXPay");
//                responseMap.put("noncestr", WeiXinUtil.randomString());
//                responseMap.put("timestamp", new Date().getTime() + "");
//                responseMap.put("sign", WeiXinUtil.weixinSign(responseMap, "D05pviPfEMMtz0pIuYsRrsyTOWVNv0NQ"));
//                // responseMap.put("prepayidsign",
//                // WeiXinUtil.classSign(dtos.getWeixinPrepayId(),
//                // "D05pviPfEMMtz0pIuYsRrsyTOWVNv0NQ"));
//                responseMap.put("code", "00");
//            } else {
//                responseMap.put("code", "04");
//            }
//        } catch (Exception e) {
//           // log.error(e);
//            responseMap.put("code", "05");
//        } finally {
//            //DBHelper.close(conn);
//        }
//
//        BigDecimal bigDecimal = new BigDecimal(12.68);
//
//        System.out.println(bigDecimal.doubleValue());


    }

}
