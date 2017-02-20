package com.koolearn.cloud.teacher.resource.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 */
@Controller
public class OutInterfaceController{

    /**
     * 上传头像接口提供
     * @return
     */
    @RequestMapping(value="/ajax/uploadcrossdomain",method= RequestMethod.POST)
    private String uploadcrossdomain(HttpServletResponse response, String callback, int status, String message){
        String script = "<script type=\"text/javascript\">parent['"+callback+"']({status:'"+status+"',message:'"+message+"'});</script>";
        response.setContentType("text/html; charset=utf-8");
        response.setCharacterEncoding("utf-8");

        try {
            PrintWriter pwriter = response.getWriter();
            pwriter.print(script);
            pwriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
