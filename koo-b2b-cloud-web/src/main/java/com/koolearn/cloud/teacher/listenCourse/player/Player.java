package com.koolearn.cloud.teacher.listenCourse.player;

import com.koolearn.cloud.login.entity.UserEntity;
import com.koolearn.framework.common.utils.spring.SpringContextUtils;
import com.koolearn.library.maintain.course.entity.CourseUnit;
import com.koolearn.library.maintain.libraryManager.dto.LibraryDto;
import com.koolearn.library.maintain.listen.service.ListenService;
import org.springframework.ui.ModelMap;

/**
 * Created by Administrator on 2016/1/5.
 */
public abstract class Player {

    protected String url = "/teacher/listenCourse/listenError";
    protected String errorMessage ="播放异常";
    protected boolean success = true;

    protected ListenService listenService =  (ListenService) SpringContextUtils.getBean("listenService", ListenService.class);
    protected CourseUnit courseUnit;
    protected int productId;
    protected UserEntity user;
    protected LibraryDto library;
    protected ModelMap map;

    public abstract String getUrl();

}
