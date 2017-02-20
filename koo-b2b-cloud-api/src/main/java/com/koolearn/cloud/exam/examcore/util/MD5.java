package com.koolearn.cloud.exam.examcore.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

public class MD5 {

	private String inStr;
    private MessageDigest md5;
    /**
     * Constructs the MD5 object and sets the string whose MD5 is to be computed.
     *
     * @param inStr the <code>String</code> whose MD5 is to be computed
     */
    public MD5(String inStr)
    {
    this.inStr = inStr;
        try
    {
       this.md5 = MessageDigest.getInstance("MD5");
        }
    catch (Exception e)
    {
       System.out.println(e.toString());
       e.printStackTrace();
        }
    }
    /**
     * Computes the MD5 fingerprint of a string.
     *
     * @return the MD5 digest of the input <code>String</code>
     */
    public String compute()
    {
    byte[] byteArray=null;
    StringBuffer hexValue = new StringBuffer();
    try {
		byteArray=this.inStr.getBytes("UTF-8");
		  byte[] md5Bytes = this.md5.digest(byteArray);
	    for (int i=0; i<md5Bytes.length; i++)
	    {
	       int val = ((int) md5Bytes[i] ) & 0xff; 
	       if (val < 16) hexValue.append("0");
	       hexValue.append(Integer.toHexString(val));
	    }
	} catch (UnsupportedEncodingException e) {
		e.printStackTrace();
	}

  
    return hexValue.toString();
     }
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MD5 md5=new MD5("gasdggasdg123");
	       String postString = md5.compute();
	       System.out.println(postString);
//	           int totalQ = 5; //两个int类型数值
//	           int totalS = 3;
//	        
//	           float f = (float)totalQ / (float)totalS ; //除法求值
//	            
//	           DecimalFormat format = new DecimalFormat("#0.00");
//	            
//	           String s = format.format(f); //转换成字符串
//	            
//	           System.out.print(s);
	}
	
	/**
	 * @param inStr 
	 * @return
	 */
	public static MD5 instance(String inStr){
		return new MD5(inStr);
	}

}
