package com.koolearn.cloud.util.hexin;

import com.koolearn.cloud.login.entity.UserEntity;
import com.koolearn.cloud.teacher.entity.School;
import org.apache.commons.codec.digest.DigestUtils;

import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * SHA Secure Hash Algorithm 安全散列算法
 * SHA算法家族目前共有SHA-0、SHA-1、SHA-224、SHA-256、SHA-384和SHA-512五种算法，通常将后四种算法并称为SHA-2算法
 * MessageDigest类支持MD算法的同时也支持SHA算法，几乎涵盖了我们所知的全部SHA系列算法，主要包含SHA-1、SHA-256、SHA-384、SHA-512四种算法。通过BouncyCastle组件，可以支持SHA-224算法
   apache 工具类 DigestUtls
 */
public class SHA1Util {
    public static final String default_ACCESS_KEY="2aa146f7";//合心默认
    public static final String default_ACCESS_SECRET="5540df65f0d4";

    /** 一、使用JDKMessageDigest实现 SHA1 安全加密算法
     *    1）Map<String, Object> maps 4个身份认证参数access_key, timestamp, nonce, signature（必选）
     *    2）access_key和access_secret由小写字母和数字组成，请保密，其中access_secret不直接传输，避免被截获
     *    3）nonce为长度在3-10位的随机字符串，由英文字母组成
     *    4）timestamp为当前unix时间戳，长度为10？
     *         String unixTimestamp=String.valueOf(javaTimestamp).substring(0, 10);
     *         long javaTimestamp=unixTimestamp * 1000
     *    5）signature为access_key、access_secret、nonce、timestamp这4个变量，按字母序排序后组成一个完整字符串，使用sha1方法加密，得到40位的16进制字符串
     *    6）接收一方使用相同的方法计算signature，对比发送一方的signature是否一致。如果一致，认为access_secret正确，即认证成功
     * @param maps 参数key-value map集合
     * @return  signature  数字签名
     * @throws DigestException
     */
    public static String SHA1(Map<String, Object> maps) throws DigestException {
        //获取信息摘要 - 参数字典排序后字符串
        String decrypt = getOrderByLexicographic(maps);//按变量字母序排序后keyvalue组成一个完整字符串
          decrypt = getOrderByLexicographicOfValue(maps);//按参数内容序排序后value组成一个完整字符串
        System.out.println("SHA-1加密字符串："+decrypt);
        try {
            //指定sha1算法(支持 SHA即SHA-1、MD5、SHA-256、SHA-384)
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            //向MessageDigest传送要计算的消息
            digest.update(decrypt.getBytes());
            // 计算消息摘要 ,获取字节数组
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString().toLowerCase();
//            return hexString.toString().toUpperCase();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new DigestException("签名错误！");
        }
    }
    /**二、使用apache 工具类实现
     * DigestUtls类除了MD5算法外，还支持多种SHA系列算法，虽然java6也都支持 ，但是它提供了更为方便的方法
     * SHA-1消息摘要算法,返回字节数组:        DigestUtils.sha1(byte[] data);
     * SHA-1消息摘要算法,返回十六进制字符串 :  DigestUtils.sha1Hex(byte[] data)
     * MessageDigest digest = DigestUtils.getSha1Digest();
     * 支持算法：
     *    md5Hex、sha1Hex、sha256Hex、sha512Hex、sha384Hex
     */
    public static String SHA1Apache(Map<String, Object> maps) throws DigestException {
        //获取信息摘要 - 参数字典排序后字符串
        String decrypt = getOrderByLexicographic(maps);
//        System.out.println("SHA-1加密字符串："+decrypt);
        try {
            return  DigestUtils.sha1Hex(decrypt.getBytes()).toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
            throw new DigestException("签名错误！");
        }
    }

    /**
     * 获取参数的字典排序
     *按变量字母序排序后keyvalue组成一个完整字符串
     * @param maps 参数key-value map集合
     * @return String 排序后的字符串
     */
    private static String getOrderByLexicographic(Map<String, Object> maps) {
        return splitParams(lexicographicOrder(getParamsName(maps)), maps);
    }
    /**
     * 获取参数的字典排序
     *按参数内容序排序后keyvalue组成一个完整字符串
     * @param maps 参数key-value map集合
     * @return String 排序后的字符串
     */
    private static String getOrderByLexicographicOfValue(Map<String, Object> maps) {
        return getParamsOrderValue(maps);
    }

    /**
     * 获取参数名称 key
     *
     * @param maps 参数key-value map集合
     * @return
     */
    private static List<String> getParamsName(Map<String, Object> maps) {
        List<String> paramNames = new ArrayList<String>();
        for (Map.Entry<String, Object> entry : maps.entrySet()) {
            paramNames.add(entry.getKey());
        }
        return paramNames;
    }
    /**
     * 获取参数值字母排序的value
     *
     * @param maps 参数key-value map集合
     * @return
     */
    private static String getParamsOrderValue(Map<String, Object> maps) {
        List<String> paramNames = new ArrayList<String>();
        for (Map.Entry<String, Object> entry : maps.entrySet()) {
            paramNames.add(entry.getValue().toString());
        }
        Collections.sort(paramNames);
        StringBuilder paramStr = new StringBuilder();
        for(String v:paramNames){
            paramStr.append(v);
        }
        return paramStr.toString();
    }

    /**
     * 参数名称按字典排序
     *
     * @param paramNames 参数名称List集合
     * @return 排序后的参数名称List集合
     */
    private static List<String> lexicographicOrder(List<String> paramNames) {
        Collections.sort(paramNames);
        return paramNames;
    }

    /**
     * 拼接排序好的参数名称和参数值
     *
     * @param paramNames 排序后的参数名称集合
     * @param maps       参数key-value map集合
     * @return String 拼接后的字符串
     */
    private static String splitParams(List<String> paramNames, Map<String, Object> maps) {
        StringBuilder paramStr = new StringBuilder();
        for (String paramName : paramNames) {
            paramStr.append(paramName);
            for (Map.Entry<String, Object> entry : maps.entrySet()) {
                if (paramName.equals(entry.getKey())) {
                    paramStr.append(String.valueOf(entry.getValue()));
                }
            }
        }
        return paramStr.toString();
    }
    public static final String access_key="access_key";
    public static final String access_secret="access_secret";
    public static final String timestamp="timestamp";
    public static final String nonce="nonce";
    public static final String signature="signature";
    public static SHA1Entity getSHA1Signature(String accessKey,String accessSecret) throws DigestException {
        SHA1Entity entity=new SHA1Entity();
        entity.setAccessKey(accessKey);
        entity.setTimestamp( String.valueOf(System.currentTimeMillis()).substring(0, 10));//linux时间戳10位，java时间戳13位
        entity.setNonce( randomNonce());
        entity.setAccessSecret(accessSecret);
        Map<String, Object> maps=new HashMap<String, Object>();
        Map<String, String> paramMaps=new HashMap<String, String>();//合心提交参数
        maps.put(SHA1Util.access_key,entity.getAccessKey());
        paramMaps.put(SHA1Util.access_key,entity.getAccessKey());

        maps.put(SHA1Util.timestamp,entity.getTimestamp());
        paramMaps.put(SHA1Util.timestamp,entity.getTimestamp());

        maps.put(SHA1Util.nonce,entity.getNonce());
        paramMaps.put(SHA1Util.nonce,entity.getNonce());

        maps.put(SHA1Util.access_secret,entity.getAccessSecret());//"access_secret" 不能传输
        String signature =SHA1Util.SHA1(maps);
//        String signatureApache =SHA1Util.SHA1Apache(maps);
        maps.put(SHA1Util.signature,signature);
        paramMaps.put(SHA1Util.signature,signature);
        entity.setParamMap(paramMaps);//设置合心接口需要的认证信息参数
        entity.setSignature(signature);
        return  entity;
    }
    public static SHA1Entity getSHA1Signature() throws DigestException {
        SHA1Entity entity=new SHA1Entity();
        entity.setAccessKey(SHA1Util.default_ACCESS_KEY);
        entity.setTimestamp( String.valueOf(System.currentTimeMillis()).substring(0, 10));//linux时间戳10位，java时间戳13位
        entity.setNonce( randomNonce());
        entity.setAccessSecret(SHA1Util.default_ACCESS_SECRET);
        Map<String, Object> maps=new HashMap<String, Object>();
        maps.put(SHA1Util.access_key,entity.getAccessKey());
        maps.put(SHA1Util.timestamp,entity.getTimestamp());
        maps.put(SHA1Util.nonce,entity.getNonce());
        maps.put(SHA1Util.access_secret,entity.getAccessSecret());
        String signature =SHA1Util.SHA1(maps);
        String signatureApache =SHA1Util.SHA1Apache(maps);
        entity.setSignature(signature);
        return  entity;
    }
    public static String randomNonce(){
        String tem="abcdefghigklmnopqrstuvwxyz";
        Random rd=new Random();
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<5;i++){
            sb.append(tem.charAt(rd.nextInt(tem.length())));
        }
        return  sb.toString();
    }
    /**
     * 更加用户生成合心登录的认证信息
     * @param user
     * @return
     */
    public static Map<String,String> getSignature(UserEntity user){
        SHA1Entity shaEntity= null;
        try {
            List<School> schools=user.getFusuiSchoolList();//登录设置(批量加学生时，有老师设置学生学校)
            if(schools!=null&&schools.size()>0){
                School school=schools.get(0);
                shaEntity = SHA1Util.getSHA1Signature(school.getAccessKey(), school.getAccessSecret());
            }
        } catch (DigestException e) {
            e.printStackTrace();
        }
        return shaEntity==null?null:shaEntity.getParamMap();
    }
    public static void main(String[] args) {
        try {
//            SHA1Entity entity=SHA1Util.getSHA1Signature();
//            SHA1Entity entity1=SHA1Util.getSHA1Signature(SHA1Util.ACCESS_KEY,SHA1Util.ACCESS_SECRET);
            Map<String, Object> maps=new HashMap<String, Object>();
            maps.put(SHA1Util.access_key,"75d140f4");
            maps.put(SHA1Util.timestamp,1473659403);
            maps.put(SHA1Util.nonce,"oltkt");
            maps.put(SHA1Util.access_secret,"76abd7b72e10");
            System.out.println(SHA1Util.SHA1(maps));
        } catch (DigestException e) {
            e.printStackTrace();
        }
    }
}