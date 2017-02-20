package com.koolearn.cloud.teacher.home.controller;

import com.koolearn.cloud.base.controller.BaseController;
import com.koolearn.cloud.teacher.service.SchedulerClassesUpService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by fn on 2016/7/5.
 */
@RequestMapping("/shool")
@Controller
public class SchedulerClassesUpController extends BaseController {

    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private SchedulerClassesUpService schedulerClassesUpService;

    //http://cloud.trunk.koolearn.com/shool/uplevel
    @RequestMapping(value = "/uplevel", method = RequestMethod.GET)
    public void classesUpLevel(){
        Integer schoolId = 0;
         schedulerClassesUpService.classesUpLevel(  );
    }





}
