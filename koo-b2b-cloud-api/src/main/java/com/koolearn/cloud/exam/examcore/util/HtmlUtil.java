package com.koolearn.cloud.exam.examcore.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class HtmlUtil {

    private static final Log log = LogFactory.getLog(HtmlUtil.class);


    /**
     * 过滤字符串中的html标记
     *
     * @param inputString
     * @return
     */
    public static String Html2Text(String inputString) {
        String htmlStr = inputString; //含html标签的字符串
        String textStr = "";
        Pattern p_script;
        Matcher m_script;
        Pattern p_style;
        Matcher m_style;
        Pattern p_html;
        Matcher m_html;

        try {
            String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; //定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script> }
            String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; //定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style> }
            String regEx_html = "<[^>]+>"; //定义HTML标签的正则表达式

            p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
            m_script = p_script.matcher(htmlStr);
            htmlStr = m_script.replaceAll(""); //过滤script标签

            p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
            m_style = p_style.matcher(htmlStr);
            htmlStr = m_style.replaceAll(""); //过滤style标签

            p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
            m_html = p_html.matcher(htmlStr);
            htmlStr = m_html.replaceAll(""); //过滤html标签

            textStr = htmlStr;

        } catch (Exception e) {
            System.err.println("Html2Text: " + e.getMessage());
        }

        return textStr;//返回文本字符串
    }
    
    /**
     * 截取length个字符（每个汉字算2个）
     * @param s 原字符串 
     * @param length 截取的字数
     * @param flag true:后面加...，false:不加
     * @return
     * @author wangshouwen - 2012-2-28 下午04:23:19
     */
    public static String subStrUnicodeByte(String s, int length, boolean flag) {
        if (length == 0) {
            return s;
        }			
		int chineseCharacters=0;
		int englishCharacters=0;
		
		String regEx = "[\\u4e00-\\u9fbf]";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(s);
		//逐个字符判断是否为汉字
		for(int i=0;i<s.length();i++){
			matcher = pattern.matcher(String.valueOf(s.charAt(i)));
			if(matcher.find()){
				chineseCharacters++;
			}
			else{
				englishCharacters++;
			}
			//超过截取长度，汉字占两个字符，字母占一个字符
			if((2*chineseCharacters+englishCharacters)>length){
				String str=s.substring(0, i);
				return flag ? (str + "...") : str;
			}
		}
		return s;
    }
    public static String delEscapeString(String s){
    	if(s==null){
    		return null;
    	}
		s=s.replace("&quot;", "");
		s=s.replace("&ldquo;", "");
		s=s.replace("&rdquo;", "");
		s=s.replace("&rdquo;", "");
		s=s.replace("&ensp;", "");
		s=s.replace("&emsp;", "");
		s=s.replace("&nbsp;", "");
		s=s.replace("&lt;", "");
		s=s.replace("&gt;", "");
		s=s.replace("&amp;", "");
		s=s.replace("&quot;", "");
		s=s.replace("&copy;", "");
		s=s.replace("&times;", "");
		s=s.replace("&divide;", "");
		return s;
    }
    public static String replaceQuestionNo4Content(String content,String startNo){
    	return replaceQuestionNo4Content(content,Integer.parseInt(startNo));
    }
    /**
     * 替换文本中填空题本身的序号
     * @param content
     * @param startNo
     * @return
     */
    public static String replaceQuestionNo4Content(String content,int startNo){
    	if(StringUtils.isBlank(content)){
    		return content;
    	}
    	String regEx = "(<span class=\"tkbox\">)(\\(\\d+\\))+(</span>)";
    	String regEx2 = ".*<span class=\"tkbox\">\\(\\d+\\)<\\/span>.*";
    	String oldEx="\\_\\_\\d+\\_\\_";
    	String oldEx2=".*\\_\\_\\d+\\_\\_.*";
    	StringBuilder sb=new StringBuilder(500);
    	if(myMatch(regEx2, content)){
	    	String[] arr=content.split(regEx);
	    	for(int i=0;i<arr.length;i++){
	    		if(i!=0){
	    			sb.append("<span class=\"tkbox\">").append(startNo++).append("</span>");
	    		}
	    		sb.append(arr[i]);
	    	}
    	}else if(myMatch(oldEx2, content)){
    		String[] arr=content.split(oldEx);
        	for(int i=0;i<arr.length;i++){
        		if(i!=0){
        			sb.append("__").append(startNo++).append("__");
        		}
        		sb.append(arr[i]);
        	}
    	}else{
    		return content;
    	}
    	return sb.toString();
    }
    private static boolean myMatch(String reg,String content){
    	 Pattern p = Pattern.compile(reg,Pattern.MULTILINE+Pattern.DOTALL);
    	 Matcher m = p.matcher(content);
    	 boolean b = m.matches();
    	return b;
    }
    public static void main(String[] args) {
		String ss="<p>ada<span class=\"tkbox\">(1)</span>sdsa</p>\r\n" +
				"<p>sd<span class=\"tkbox\">(2)</span>fds</p>" +
				"<p>g<span class=\"tkbox\">(3)</span>fd</p>" +
				"<p>gdsf<span class=\"tkbox\">(4)</span>sdfsdfdsf</p>" +
				"<p>fdg</p>" +
				"<p>fd</p>" +
				"<p>g</p>" +
				"<p>dfh</p>" +
				"<p>gf</p>" +
				"<p>h</p>" +
				"<p>fgh</p>";
		System.out.println(replaceQuestionNo4Content(ss, 5));
		String ss2="Many people wrongly believe that when people reach old age, their families place them in nursing homes. They are left in the __1__ of strangers for the rest of their lives. Their __2__ children visit them only occasionally, but more often, they do not have any __3__ visitors. The truth is that this idea is an unfortunate myth&mdash;an __4__ story. In fact, family members provide over 80 percent of the care __5__ elderly people need. Samuel Prestoon, a sociologist, studied __6__ the American family is changing. He reported that by the time the __7__ American couple reaches 40 years of age, they have more parents than children. __8__ , because people today live longer after an illness than people did years __9__ , family members must provide long term care. More psychologists have found that all caregivers __10__ a common characteristic: All caregivers believe that they are the best __11__ for the job. In other words, they all felt that they __12__ do the job better than anyone else. Social workers __13__ caregivers to find out why they took __14__ the responsibility of caring for an elderly relative. Many caregivers believed they had __15__ to help their relative. Some stated that helping others __16__ them feel more useful. Others hoped that by helping __17__ now, they would deserve care when they became old and __18__ . Caring for the elderly and being taken care of can be a __19__ satisfying experience for everyone who might be __20__ .";
		System.out.println(replaceQuestionNo4Content(ss2, 5));
	}
}
