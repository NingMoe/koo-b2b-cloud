package com.koolearn.cloud.exam.examcore.util;
import java.text.DecimalFormat;
import  com.koolearn.exam.util.TOL24_MD5;


public class  TestUtil {
	public static final int SERVICE_TYPE_POSTIL = 1; //作文批改服务类型
	public static final int SERVICE_TYPE_SPOKEN = 2; //口语批改服务类型
	
	public static final int STATUS_NEW = 1;			 //新建状态
	public static final int STATUS_CHECK_PASSED = 2; //审核通过
	public static final int STATUS_CHECK_NOTPASS = 3;//审核不通过
	public static final int STATUS_CHECK_INVALID = 4;//作废
	public static final int STATUS_DELETE = 9;		 //删除
	
	public static char transToLetter(int i) {
        char c=(char) (i+65);
        return c;
    }
	
	/**
	 * 保留两位小数
	 * @param i
	 * @return
	 */
	public static float Round2Decimal(float i)
	{
		 DecimalFormat format = new DecimalFormat("#0.00");
         String s = format.format(i); //转换成字符串
         return Float.parseFloat(s);
	}
	
	public static String premissionVisitCode(String str, String type) {
//		TOL24_MD5 md = new TOL24_MD5();
		String tmp = TOL24_MD5.encode(str, type);
		return tmp;
	}
}
