package com.koolearn.cloud.tags;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;

import com.koolearn.framework.common.page.ListPager;

public class PagerTag extends TagSupport {
	private static final long serialVersionUID = 2704178827792547902L;
	private ListPager pager;
	private String template;
    private String link;//链接
	public ListPager getPager() {
		return pager;
	}

	public void setPager(ListPager pager) {
		this.pager = pager;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		if(!StringUtils.isNotBlank(template)){
			template = "template/pager.tpl";
		}
		this.template = template;
	}

	@Override
	public int doStartTag() throws JspException {
		String contextPath = super.pageContext.getServletContext().getRealPath("/");
		File file = new File(contextPath+template);
		if(!file.exists()){
			throw new JspException("the template is not found!");
		}
		StringBuffer buffer = new StringBuffer();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
			String line = "";
			while((line = br.readLine())!=null){
				buffer.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspException("the template  file Invalid!");
		}
		String pageDetail = buffer.toString();
		
		String regex = "\\[fristPage\\](.*)\\[/fristPage\\]";
		String fristEle = getRegexStr(regex, pageDetail);
		fristEle = fristEle.replaceAll("\\[link\\]", link.replace("{p}", (0)+""));
		pageDetail = pageDetail.replaceAll(regex, fristEle);
		
		regex = "\\[prePage\\](.*)\\[/prePage\\]";
		String preEle = getRegexStr(regex, pageDetail);
		
		if(pager.getPageNo()<=0){
			preEle = preEle.replaceAll("\\[link\\]", "javascript:;");
		}else{
			preEle = preEle.replaceAll("\\[link\\]", link.replace("{p}", (pager.getPageNo()-1)+""));
		}
		
		pageDetail = pageDetail.replaceAll(regex, preEle);
		double temp = Math.ceil((pager.getTotalRows()*1.0d/pager.getPageSize()));
		int totalPage = (int)temp;
		
		//中间部分
		regex = "\\[pageEle\\](.*)\\[/pageEle\\]";
		String ele = getRegexStr(regex, pageDetail);;
		pageDetail = pageDetail.replaceAll(regex, "");
		
		regex = "\\[currentPage\\](.*)\\[/currentPage\\]";
		String currentEle = getRegexStr(regex, ele);
		ele = ele.replaceAll(regex, "");
		
		
		regex = "\\[pageEach\\](.*)\\[/pageEach\\]";
		String pageEach = getRegexStr(regex, ele);
		ele = ele.replaceAll(regex, "");
		
		//大于5显示点
		if(totalPage>10){
			String eeEle = "";
			if(pager.getPageNo()+9<totalPage){
				int start = pager.getPageNo();
				if(start == 0){
					start = 1;
					for(int i=start;i<=pager.getPageNo()+10;i++){
						if(i==pager.getPageNo()+1){
							eeEle+=currentEle.replaceAll("\\[link\\]", "javascript:;").replaceAll("\\[page\\]", i+"");
						}else{
							eeEle+=pageEach.replaceAll("\\[link\\]", link.replace("{p}",  (i-1)+"")).replaceAll("\\[page\\]", i+"");
						}
					}
				}else{
					for(int i=start;i<=pager.getPageNo()+9;i++){
						if(i==pager.getPageNo()+1){
							eeEle+=currentEle.replaceAll("\\[link\\]", "javascript:;").replaceAll("\\[page\\]", i+"");
						}else{
							eeEle+=pageEach.replaceAll("\\[link\\]", link.replace("{p}",  (i-1)+"")).replaceAll("\\[page\\]", i+"");
						}
					}
				}
			}
			
			if(pager.getPageNo()+1 >totalPage-9){
				for(int i=totalPage-9;i<=totalPage;i++){
					if(i==pager.getPageNo()+1){
						eeEle+=currentEle.replaceAll("\\[link\\]", "javascript:;").replaceAll("\\[page\\]", i+"");
					}else{
 						eeEle+=pageEach.replaceAll("\\[link\\]", link.replace("{p}",  (i-1)+"")).replaceAll("\\[page\\]", i+"");
					}
				}
			}
			
			regex = "(\\[eeEle\\].*\\[/eeEle\\])";
			pageDetail = pageDetail.replaceAll(regex, eeEle);
		}else{
			String eachEle = "";
			for(int i=1;i<=totalPage;i++){
				if(i==pager.getPageNo()+1){
					eachEle+=currentEle.replaceAll("\\[link\\]", "javascript:;").replaceAll("\\[page\\]", i+"");
				}else{
					eachEle+=pageEach.replaceAll("\\[link\\]", link.replace("{p}", (i-1)+"")).replaceAll("\\[page\\]", i+"");
				}
			}
			regex = "(\\[eeEle\\].*\\[/eeEle\\])";
			pageDetail = pageDetail.replaceAll(regex, eachEle);
		}
		
		regex = "\\[nextPage\\](.*)\\[/nextPage\\]";
		String nextEle = getRegexStr(regex, pageDetail);
		if(pager.getPageNo()+1>=totalPage){
			nextEle = nextEle.replaceAll("\\[link\\]", "javascript:;");
		}else{
			nextEle = nextEle.replaceAll("\\[link\\]", link.replace("{p}", (pager.getPageNo()+1)+""));
		}
		pageDetail = pageDetail.replaceAll(regex, nextEle);
		//末页
		regex = "\\[lastPage\\](.*)\\[/lastPage\\]";
		String lastEle = getRegexStr(regex, pageDetail);
		lastEle = lastEle.replaceAll("\\[link\\]", link.replace("{p}", (totalPage-1<0?0:totalPage-1)+""));
		pageDetail = pageDetail.replaceAll(regex, lastEle);
		
		regex = "\\[totalPage\\](.*)\\[/totalPage\\]";
		String totalEle = getRegexStr(regex, pageDetail);
		totalEle = totalEle.replaceAll("\\[page\\]", totalPage+"");
		pageDetail = pageDetail.replaceAll(regex, totalEle);
		
		
		try {
			JspWriter out = pageContext.getOut();       
			out.println(pageDetail);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.doStartTag();
	}
	
	@Override
	public int doEndTag() throws JspException {
		return super.doEndTag();
	}
	
	private String getRegexStr(String regex,String sourceStr){
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(sourceStr);
		while(m.find()){
			return m.group(1).trim();
		}
		return "";
	}
}
