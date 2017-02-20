package com.xiaohao;

import com.koolearn.cloud.mobi.service.CommonHessianService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by haozipu on 2016/7/26.
 */
public class test {
	
	public static void main(String[] args) {

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:provider-exam-test.xml");

        CommonHessianService commonHessianService = applicationContext.getBean(CommonHessianService.class);

    }

}
