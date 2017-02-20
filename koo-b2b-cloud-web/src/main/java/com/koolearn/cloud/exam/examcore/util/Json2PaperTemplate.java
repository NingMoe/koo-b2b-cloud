package com.koolearn.cloud.exam.examcore.util;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.io.FileUtils;

import com.alibaba.fastjson.JSON;
import com.koolearn.cloud.exam.examcore.paper.dto.PaperTemplateDto;

public class Json2PaperTemplate {
	public static void main(String[] args) throws URISyntaxException, IOException {
		URI uri=Json2PaperTemplate.class.getResource("./ielts.json").toURI();
		String str=FileUtils.readFileToString(new File(uri));
		PaperTemplateDto dto=JSON.parseObject(str, PaperTemplateDto.class);
		System.out.println(str);
	}
	public static PaperTemplateDto convert(String fileName){
		URI uri;
		String str=null;
		try {
			uri = Json2PaperTemplate.class.getResource("./"+fileName).toURI();
			str=FileUtils.readFileToString(new File(uri),"utf-8");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		str=str.replaceAll("\\n", "").replaceAll("\\r", "").replaceAll("\\t", "");
		PaperTemplateDto dto=JSON.parseObject(str, PaperTemplateDto.class);
		return dto;
	}
}
