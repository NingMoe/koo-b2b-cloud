package com.koolearn.cloud.operation.controller;

import com.koolearn.cloud.base.controller.BaseController;
import com.koolearn.framework.common.utils.PropertiesConfigUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.servlet.http.HttpSession;

@Controller
public class OperationLogoutController extends BaseController {

    /**
     * security退出
     * @param session
     * @return
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:"+PropertiesConfigUtils.getProperty("casDomain")+"/logout";
    }


}
