package com.koolearn.cloud.util.pdf;

import com.koolearn.cloud.util.GlobalConstant;
import com.koolearn.cloud.util.IndexNameUtils;
import com.koolearn.framework.common.utils.PropertiesConfigUtils;
import org.apache.commons.lang.StringUtils;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 剔除文本中常见标签的工具类
 * @author zhengxianyin
 * @date  20150113
 *
 */
public class HtmlCssUtil {

	private static final String regEx_xml = "<xml[^>]*?>[\\s\\S]*?<\\/xml>"; // 定义xml的正则表达式  
	private static final String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式  
    private static final String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式  
    private static final String regEx_html_br= "<br\\s*/>";//定义空格回车换行符
    private static final String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
    private static final String regEx_space = "\\s*|\t|\r|\n";//定义空格回车换行符

    private static final String regEx_w1 = "<w:[^>]*?>[\\s\\S]*?<\\/w:[^>]*?>";//特殊的需求(例如：<w:SaveIfXMLInvalid>false</w:SaveIfXMLInvalid>)
    private static final String regEx_w2 = "<w:[^>]*?>[\\s\\S]*?<\\/w:[^>]*?";//特殊的需求(例如：<w:LidThemeOther>EN-US</w:)
    private static final String regEx_w3 = "<w:[^>]*?>";//特殊的需求(例如：<w:SpaceForUL />)
    private static final String regEx_w4 = "<w:[^>]*?[a-zA-Z]*?[a-zA-Z]$";//特殊的需求(例如：<w:DoNotLea )
    
    private static final String regEx_m1 = "<m:[^>]*?>[\\s\\S]*?<\\/m:[^>]*?>";//特殊的需求(例如：<m:SaveIfXMLInvalid>false</m:SaveIfXMLInvalid>)
    private static final String regEx_m2 = "<m:[^>]*?>[\\s\\S]*?<\\/m:[^>]*?";//特殊的需求(例如：<m:LidThemeOther>EN-US</m:)
    private static final String regEx_m3 = "<m:[^>]*?>";//特殊的需求(例如：<m:SpaceForUL /> )
    private static final String regEx_m4 = "<m:[^>]*?[a-zA-Z]*?[a-zA-Z]$";//特殊的需求(例如：<m:DoNotLea )

    /** 
     * @param htmlStr 
     * @return 
     *  删除Html标签 
     */  
    public static String delHTMLTag(String htmlStr) {
        if(StringUtils.isBlank(htmlStr)) return "";
    	
      	Pattern p_xml = Pattern.compile(regEx_xml, Pattern.CASE_INSENSITIVE);  
        Matcher m_xml = p_xml.matcher(htmlStr);  
        htmlStr = m_xml.replaceAll(""); // 过滤xml标签  
        
        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);  
        Matcher m_script = p_script.matcher(htmlStr);  
        htmlStr = m_script.replaceAll(""); // 过滤script标签  
  
        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);  
        Matcher m_style = p_style.matcher(htmlStr);  
        htmlStr = m_style.replaceAll(""); // 过滤style标签  

        Pattern p_html_br = Pattern.compile(regEx_html_br, Pattern.CASE_INSENSITIVE);
        Matcher m_html_br = p_html_br.matcher(htmlStr);
        htmlStr = m_html_br.replaceAll("\n"); // 处理html标签 换行

        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);  
        Matcher m_html = p_html.matcher(htmlStr);  
        htmlStr = m_html.replaceAll(""); // 过滤html标签  
  
//        Pattern p_space = Pattern.compile(regEx_space, Pattern.CASE_INSENSITIVE);
//        Matcher m_space = p_space.matcher(htmlStr);
//        htmlStr = m_space.replaceAll(""); // 过滤空格回车标签
        htmlStr =parseEntityCharacter(htmlStr);
        return htmlStr.trim(); // 返回文本字符串  
    }

    /**
     * 处理实体
     * @param html
     * @return
     */
    public static String  parseEntityCharacter(String html){
        html= html.replaceAll("&nbsp;"," ").replaceAll("&quot;","\"")
                .replaceAll("&rdquo;","”")
                .replaceAll("&ldquo;","“")
                .replaceAll("&rsquo;","’")
                .replaceAll("&lsquo;","‘")
                .replaceAll("&sbquo;","‚")
                .replaceAll("&bdquo;","„")
                .replaceAll("&prime;","′")
                .replaceAll("&Prime;","″")
                .replaceAll("&sdot;","⋅")
                .replaceAll("&bull;","•")
                .replaceAll("&sup2;","²")
                .replaceAll("&lt;","<")
                .replaceAll("&lsaquo;","‹")
                .replaceAll("&gt;",">")
                .replaceAll("&rsaquo;","›")
                .replaceAll("&le;","≤")
                .replaceAll("&ge;","≥")
                .replaceAll("&oline;","‾")
                .replaceAll("&ndash;","–")
                .replaceAll("&mdash;","—")
                .replaceAll("&radic;","√")
                .replaceAll("&times;","×")
                .replaceAll("&divide;","÷")
                .replaceAll("&there4;","∴")
                .replaceAll("&ne;","≠")
                .replaceAll("&amp;","&")
                .replaceAll("&copy;","©")
                .replaceAll("&trade;","™")
                .replaceAll("&reg;","®")
                .replaceAll("&oplus;","⊕")
                .replaceAll("&otimes;","⊗")
                .replaceAll("&and;","∧")
                .replaceAll("&hellip;","…")
                .replaceAll("&permil;","‰")
        ;
        return html;
    }
    /** 
     * @param htmlStr 
     * @return 
     *  根据ios要求定制剔除Html标签 
     */  
    public static String getTextFromHtml(String htmlStr) {  
    	
    	//特殊处理
    	if(StringUtils.isEmpty(htmlStr)){
    		return "";
    	}
    	
    	Pattern p_space = Pattern.compile(regEx_space, Pattern.CASE_INSENSITIVE);  
        Matcher m_space = p_space.matcher(htmlStr);  
        htmlStr = m_space.replaceAll(""); // 过滤空格回车标签  
        htmlStr = htmlStr.replaceAll("&nbsp;", "");  //不断行的空白格
        htmlStr = htmlStr.trim();  //返回文本字符串 
         
    	Pattern p_w1 = Pattern.compile(regEx_w1, Pattern.CASE_INSENSITIVE);  
        Matcher m_w1 = p_w1.matcher(htmlStr);  
        htmlStr = m_w1.replaceAll(""); // 过滤11 
        
        Pattern p_w2 = Pattern.compile(regEx_w2, Pattern.CASE_INSENSITIVE);  
        Matcher m_w2 = p_w2.matcher(htmlStr);  
        htmlStr = m_w2.replaceAll(""); // 过滤12 
        
        Pattern p_w3 = Pattern.compile(regEx_w3, Pattern.CASE_INSENSITIVE);  
        Matcher m_w3 = p_w3.matcher(htmlStr);  
        htmlStr = m_w3.replaceAll(""); // 过滤13         
        
        Pattern p_w4 = Pattern.compile(regEx_w4, Pattern.CASE_INSENSITIVE);  
        Matcher m_w4 = p_w4.matcher(htmlStr);  
        htmlStr = m_w4.replaceAll(""); // 过滤14 
        
        Pattern p_m1 = Pattern.compile(regEx_m1, Pattern.CASE_INSENSITIVE);  
        Matcher m_m1 = p_m1.matcher(htmlStr);  
        htmlStr = m_m1.replaceAll(""); // 过滤21
        
        Pattern p_m2 = Pattern.compile(regEx_m2, Pattern.CASE_INSENSITIVE);  
        Matcher m_m2 = p_m2.matcher(htmlStr);  
        htmlStr = m_m2.replaceAll(""); // 过滤22
        
        Pattern p_m3 = Pattern.compile(regEx_m3, Pattern.CASE_INSENSITIVE);  
        Matcher m_m3 = p_m3.matcher(htmlStr);  
        htmlStr = m_m3.replaceAll(""); // 过滤23
        
        Pattern p_m4 = Pattern.compile(regEx_m4, Pattern.CASE_INSENSITIVE);  
        Matcher m_m4 = p_m4.matcher(htmlStr);  
        htmlStr = m_m4.replaceAll(""); // 过滤24
        
    	htmlStr = delHTMLTag(htmlStr);
        htmlStr = htmlStr.replaceAll("&nbsp;", "");  //不断行的空白格
        htmlStr = htmlStr.replaceAll("&ldquo;", "");  //左双引号
        htmlStr = htmlStr.replaceAll("&rdquo;", "");  //右双引号              
        
        return htmlStr.trim(); // 返回文本字符串  
    }
    /**
     * 清除一些不需要的html标记
     *
     * @param htmlStr
     *            带有复杂html标记的html语句
     * @return 去除了不需要html标记的语句
     */
    public static String clearFormat(String htmlStr, String docImgPath) {
        // 获取body内容的正则
        String bodyReg = "<BODY .*</BODY>";
        Pattern bodyPattern = Pattern.compile(bodyReg);
        Matcher bodyMatcher = bodyPattern.matcher(htmlStr);
        if (bodyMatcher.find()) {
            // 获取BODY内容，并转化BODY标签为DIV
            htmlStr = bodyMatcher.group().replaceFirst("<BODY", "<DIV")
                    .replaceAll("</BODY>", "</DIV>");
        }
        // 调整图片地址
        htmlStr = htmlStr.replaceAll("<IMG SRC=\"", "<IMG SRC=\"" + docImgPath
                + "/");
        // 把<P></P>转换成</div></div>保留样式
        // content = content.replaceAll("(<P)([^>]*>.*?)(<\\/P>)",
        // "<div$2</div>");
        // 把<P></P>转换成</div></div>并删除样式
        htmlStr = htmlStr.replaceAll("(<P)([^>]*)(>.*?)(<\\/P>)", "<p$3</p>");
        // 删除不需要的标签
        htmlStr = htmlStr
                .replaceAll(
                        "<[/]?(font|FONT|span|SPAN|xml|XML|del|DEL|ins|INS|meta|META|[ovwxpOVWXP]:\\w+)[^>]*?>",
                        "");
        // 删除不需要的属性
        htmlStr = htmlStr
                .replaceAll(
                        "<([^>]*)(?:lang|LANG|class|CLASS|style|STYLE|size|SIZE|face|FACE|[ovwxpOVWXP]:\\w+)=(?:'[^']*'|\"\"[^\"\"]*\"\"|[^>]+)([^>]*)>",
                        "<$1$2>");
        return htmlStr;
    }
    public static String parseImgPath(String htmlStr, String docImgPath){
        // 调整图片地址
        if(StringUtils.isNotBlank(htmlStr)){
            htmlStr = htmlStr.replaceAll("<image src='", "<image src='" + docImgPath);
            htmlStr = htmlStr.replaceAll("<image src=\"", "<image src=\"" + docImgPath);
            htmlStr = htmlStr.replaceAll("<img src='", "<img src='" + docImgPath);
            htmlStr = htmlStr.replaceAll("<img src=\"", "<image src=\"" + docImgPath);
        }
        return htmlStr;
    }
    public static String parseImageUrl(String html){
        try {
            if(StringUtils.isBlank(html)) return "";
            html=html.replace("\"","'");
            String IMGURL_REG ="src=\\'([^\\']+)\\'";
            Matcher matcher = Pattern.compile(IMGURL_REG).matcher(html.toLowerCase()); //将正则编译到模式中，并匹配内容
            while (matcher.find()) {          //尝试查找与该模式匹配的输入序列的下一个子序列
                String url=matcher.group();//src='/upload/office-word/img/exama934f8e4b434467184a1356bf68f51ed.png'
                int begin=url.indexOf("'");
                int end=url.lastIndexOf("'");
                String newUrl=url.substring(begin+1,end);
                if(!newUrl.startsWith("http")){
                    newUrl="http://"+ IndexNameUtils.getValueByKey("exam.host")+newUrl;
                }
                String newSrc="src='"+newUrl+"'";
                html=html.toLowerCase().replace(url,newSrc);
            }
            return html;
        }catch (Exception e){
            e.printStackTrace();
            return html;
        }
    }
    /**
     * 提取图片路径
     * @param html
     * @return
     */
    public static List<String> getImageUrl(String html){
        if(StringUtils.isBlank(html)) return new ArrayList<String>();
        html=html.replace("\"","'");
        String IMGURL_REG ="src=\\'([^\\']+)\\'";
        Matcher matcher = Pattern.compile(IMGURL_REG).matcher(html.toLowerCase()); //将正则编译到模式中，并匹配内容
        List<String> listImgUrl = new ArrayList<String>();         //图片标签集合
        while (matcher.find()) {          //尝试查找与该模式匹配的输入序列的下一个子序列
            String url=matcher.group();//src='/upload/office-word/img/exama934f8e4b434467184a1356bf68f51ed.png'
            int begin=url.indexOf("'");
            int end=url.lastIndexOf("'");
            url=url.substring(begin+1,end);
            if(!url.startsWith("http")){
                url="http://"+ IndexNameUtils.getValueByKey("exam.host")+url;
            }
            if(!isValid(url)){
                System.out.println("试卷下载无效路径："+url);
                //无效路径默认链接
                String clouddomain= PropertiesConfigUtils.getProperty(GlobalConstant.CLOUD_HOST_DOMAIN);
                url=clouddomain+"/image/default.jpg";
            }
            listImgUrl.add(url);
        }
        return listImgUrl;
    }
    /**
     　　* 判断链接是否有效
     　　* 输入链接
     　　* 返回true或者false
     　　*/
    public static boolean isValid(String strLink) {
        URL url;
        try {
            url = new URL(strLink);
            HttpURLConnection connt = (HttpURLConnection)url.openConnection();
            connt.setRequestMethod("HEAD");
            String strMessage = connt.getResponseMessage();
            if (strMessage.compareTo("Not Found") == 0) {
                return false;
            }
            connt.disconnect();
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
