package com.koolearn.cloud.common.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

public class ExceptionHandler extends SimpleMappingExceptionResolver {
	public ModelAndView resolveException(
			HttpServletRequest request, 
			HttpServletResponse response, Object handler,
            Exception ex) {
		if(handler!=null){
			Logger log = Logger.getLogger(handler.getClass());
	        log.error(handler,ex);
		}
        ex.printStackTrace();
        return super.resolveException(request, response, handler, ex);
    }
}