package com.koolearn.cloud.util;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

/**
 * Created by fn on 2016/7/20.
 */
public class SendMainUtil {

    /**
     *
     * @param errorMessage ：邮件内容
     * @param toMail      ：接收邮件地址
     */
    public static void sendMain( String errorMessage ,String toMail ){
        String title = "验证邮箱";
        String fromAddr = "Eservice@koolearn.com";
        String fromName = "新东方在线";
        String toAddr = toMail;
        String random = SsoUtil.randomEmailCode();
        String host = "pubmail.koolearn.com";
        try {
            Properties props = new Properties();
            // 设置smtp服务器地址
            props.put("mail.smtp.host", host);
            System.setProperty("mail.mime.charset", "UTF-8");
            props.put("mail.smtp.auth", "false");
            // 5秒抛出
            props.put("mail.smtp.timeout", "5000");
            props.put("mail.smtp.port", "25");
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.ssl.enable", false);
            // 创建新邮件
            Session session = Session.getDefaultInstance(props, null);
            // 创建过程对象
            MimeMessage message = new MimeMessage(session);
            if (fromName != null && !"".equals(fromName)) {
                message.setFrom(new InternetAddress(fromAddr, fromName));
            } else {
                message.setFrom(new InternetAddress(fromAddr));
            }
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddr));
            // 设置主题
            message.setSubject(title);
            // 如果非附件发送，则只要简单设置内容
            message.setContent( errorMessage , "text/html;charset=UTF-8");
            message.setSentDate(new Date());
            message.saveChanges();
            Transport.send(message, message.getAllRecipients());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
