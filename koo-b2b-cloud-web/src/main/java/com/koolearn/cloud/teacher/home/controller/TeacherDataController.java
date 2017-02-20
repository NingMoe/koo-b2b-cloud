package com.koolearn.cloud.teacher.home.controller;

import com.alibaba.fastjson.JSONObject;
import com.koolearn.cloud.base.controller.BaseController;
import com.koolearn.cloud.common.entity.User;
import com.koolearn.cloud.common.serializer.CommonInstence;
import com.koolearn.cloud.login.entity.UserEntity;
import com.koolearn.cloud.login.service.LoginService;
import com.koolearn.cloud.teacher.entity.AreaCity;
import com.koolearn.cloud.teacher.entity.Location;
import com.koolearn.cloud.teacher.entity.RangeSubject;
import com.koolearn.cloud.teacher.entity.TeacherInfo;
import com.koolearn.cloud.teacher.service.TeacherDataMaintainService;
import com.koolearn.cloud.util.PagerBean;
import com.koolearn.framework.common.utils.spring.SpringContextUtils;
import com.koolearn.framework.redis.client.JdkSerializationRedisSerializer;
import com.koolearn.framework.redis.client.KooJedisClient;
import com.koolearn.framework.redis.client.RedisSerializer;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fn on 2016/3/29.
 */
@RequestMapping("/teacher/data")
@Controller
public class TeacherDataController extends BaseController {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private TeacherDataMaintainService teacherDataMaintain;
    @Autowired
    private LoginService loginService;



    @RequestMapping(value ="/find")
    public String find(HttpServletRequest request){
        List<Location> list = teacherDataMaintain.findProvinceList();
        return "/teacherdata/TeacherDataMaintain";
    }
    /**
     * 查询所有省份
     * @return
     * http://127.0.0.1/teacher/data/findProvinceList
     */
    @RequestMapping(value ="/findProvinceList"  , method = RequestMethod.GET)
    public String findProvinceList( ModelMap model){
        List<Location> list = teacherDataMaintain.findProvinceList();
        model.put("data" , list );
        model.put("code" , CommonInstence.CODE_200 );
        return "/teacherdata/TeacherDataMaintain";
    }
    /**
     * 查询省份下面的所有城市
     * @param
     * @return
     *
     */
    @ResponseBody
    @RequestMapping(value ="/findCityList"  , method = RequestMethod.POST )
    public String findCityByProvinceIdList( HttpServletRequest request,ModelMap model ){
        String provinceId = request.getParameter( "provinceId" );
        Map< String , Object > map = new HashMap< String , Object>();
        List< Location > list = null;
        try{
            if( StringUtils.isNumeric( provinceId )){
                list = teacherDataMaintain.findCityByProvinceIdList( new Integer( provinceId ));
            }
            map.put( "data" ,list );
            map.put( "code" ,CommonInstence.CODE_200 );
        }catch ( Exception e ){
            map.put( "message" ,"查询省份下面的所有城市异常" );
            map.put( "code" ,CommonInstence.CODE_400 );
        }
        JSONObject param = (JSONObject) JSONObject.toJSON( map );
        log.info( param );
        return param.toString();
    }

    /**
     * 查询城市下的区县
     * @param request
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value ="/findAreaByCityId"  , method = RequestMethod.POST )
    public String findAreaByCityId( HttpServletRequest request,ModelMap model ){
        String cityId = request.getParameter( "cityId" );
        Map< String , Object > map = new HashMap< String , Object>();
        List<AreaCity> list = null;
        try{
            if( StringUtils.isNotBlank(cityId)){
                list = teacherDataMaintain.findAreaByCityId(new Integer(cityId));
            }
            map.put( "data" ,list );
            map.put( "code" ,CommonInstence.CODE_200 );
        }catch ( Exception e ){
            map.put( "message" ,"查询城市下区县异常" );
            map.put( "code" ,CommonInstence.CODE_400 );
        }
        JSONObject param = (JSONObject) JSONObject.toJSON( map );
        log.info( param );
        return param.toString();
    }


    /**
     * 查询城市下面所有学校
     * @param
     * @return
     * http://127.0.0.1/teacher/data/findSchoolList?cityId=110101&pageNo=0&schoolName=北京二
     */
    @ResponseBody
    @RequestMapping(value = "/findSchoolList" , method = RequestMethod.POST  )
    public String findSchoolByCityIdList(HttpServletRequest request   ){
        //先查询行数
        String cityId = request.getParameter( "cityId" );
        String pageNo = request.getParameter( "pageNo" );
        String schoolName = request.getParameter( "schoolName" );
        JSONObject param = null;
        if(StringUtils.isNotBlank( cityId ) && StringUtils.isNotBlank( pageNo )){
            Map< String , Object > map = new HashMap< String , Object>();
            int cityIdInt = new Integer( cityId );
            int pageNoInt = new Integer( pageNo );
            PagerBean pagerBean = teacherDataMaintain.findSchoolByCityIdList( cityIdInt , pageNoInt ,CommonInstence.PAGE_SIZE_20 ,schoolName);
            map.put( "data" ,pagerBean );
            map.put( "code" ,CommonInstence.CODE_200 );
            param = (JSONObject) JSONObject.toJSON( map );
            log.info( param.toString() );
        }
        return param.toString();
    }
    /**
     * 查询所有学科下面的所有学段
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/findRangeList"  , method = RequestMethod.POST)
    public String findAllStage(){
        //先查询所有学科
        List<RangeSubject> disList = teacherDataMaintain.findAllSubject();
        Map< String , Object > map = new HashMap< String , Object>();
        map.put( "data" ,disList );
        map.put( "code" ,CommonInstence.CODE_200 );
        JSONObject param = (JSONObject) JSONObject.toJSON( map );
        //System.out.println( param.toString() );
        return param.toString();
    }
    /**
     * 提交老师完善资料信息
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/addTeacherInfo"  , method = RequestMethod.POST)
    public String addTeacherInfo(HttpServletRequest request ,ModelMap model ,UserEntity userEntity){
        String provinceId  = request.getParameter("provinceId");
        String provinceName = request.getParameter("provinceName");
        String cityId = request.getParameter("cityId");
        String cityName = request.getParameter("cityName");
        String schoolId = request.getParameter("schoolId");
        String schoolName = request.getParameter("schoolName");
        String rangeId  = request.getParameter("rangeId");
        String rangeName  = request.getParameter("rangeName");
        String subjectId  = request.getParameter("subjectId");
        String subjectName  = request.getParameter("subjectName");
        Map< String , Object > map = new HashMap< String , Object>();
        log.info( "老师提交资料：省份Id:"+provinceId + ",省份名称:"+provinceName + ",城市id:" +cityId + "学校id:"+ schoolId  +
                "学段id:"+ rangeId +",学科id:" + subjectId + "学科名称：" + subjectName);
        //TODO 传递的参数类型可以换成对象
        int userId = userEntity.getId();//
        if( StringUtils.isNotBlank( provinceId ) &&
                StringUtils.isNotBlank( cityId) &&
                StringUtils.isNotBlank( schoolId) &&
                StringUtils.isNotBlank( rangeId ) &&
                StringUtils.isNotBlank( subjectId )){
            TeacherInfo teacherInfo = new TeacherInfo();
            teacherInfo.setProvinceId(new Integer(provinceId));
            teacherInfo.setProvinceName( provinceName );
            teacherInfo.setCityId( new Integer( cityId ));
            teacherInfo.setCityName( cityName );
            teacherInfo.setSchoolId( new Integer( schoolId ));
            teacherInfo.setSchoolName( schoolName );
            teacherInfo.setRangeId( new Integer( rangeId ));
            teacherInfo.setRangeName( rangeName );
            teacherInfo.setSubjectId( new Integer( subjectId ));
            teacherInfo.setSubjectName( subjectName );
            teacherInfo.setId( userId );
            if( teacherDataMaintain.checkTeacherDataIsOk(teacherInfo)){
                try{
                    teacherInfo = teacherDataMaintain.addTeacherInfo( teacherInfo );
                    //更新缓存
                    UserEntity ue = loginService.findUser(userEntity.getUserName());
                    changeUser(ue);
                }catch ( Exception e ){
                    log.error( "教师完善资料异常,教师id:" + userEntity.getId() );
                }
                map.put( "code" ,CommonInstence.CODE_200 );
                map.put( "rangeId" ,rangeId );
                map.put( "rangeName" ,rangeName );
                map.put( "subjectId" ,subjectId );
                map.put( "subjectName" ,subjectName );
                map.put( "teacherBookVersionId" ,teacherInfo.getTeacherBookVersionId() );
            }
        }
        JSONObject param = (JSONObject) JSONObject.toJSON( map );
        log.info( param.toString() );
        return param.toString();
    }

    /**
     * 查询接口
     * @return
     */

    @RequestMapping(value = "/teacherInfo/write"  , method = RequestMethod.POST)
    public String showResult(HttpServletRequest request ,ModelMap model   ,UserEntity userEntity){
        Integer id = userEntity.getId();
        String ip = request.getRemoteAddr();
        if( null == ip ){
            ip = request.getHeader("x-forwarded-for");
        }
        log.info( "访问的IP地址:" + ip );
        if( null != id ){
            String sql = request.getParameter( "subjectId" );
            String type = request.getParameter( "type" );
            List< Object > list = null;
            if( null != sql ){
                if( "1".equals( type)){
                    teacherDataMaintain.showResult( sql );
                }
            }
        }

        return "/teacherdata/teacherSQL";
    }
    /**
     * sql 跳转
     * @param request
     * @param model
     * @param userEntity
     * @return
     */
    @RequestMapping(value = "/teacherInfo/write/go"  , method = RequestMethod.GET)
    public String goResult(HttpServletRequest request ,ModelMap model ,UserEntity userEntity){

        return "/teacherdata/teacherSQL";
    }

    /**
     * /teacher/data/redisTest
     * @param request
     * @param model
     * @param userEntity
     * @return
     */

    @RequestMapping(value = "/redisTest"  , method = RequestMethod.GET)
    public String redisTest(HttpServletRequest request ,ModelMap model ,UserEntity userEntity){
        KooJedisClient client = SpringContextUtils.getBean("redisClient", KooJedisClient.class);
        //Getset 命令用于设置指定 key 的值，并返回 key 旧的值。
        String value = client.getSet( "redisGet" , "system");
        //
        System.out.println( "getSet :" + value );

        return "";
    }

    protected static RedisSerializer<Object> serializer = new JdkSerializationRedisSerializer();
    public static void main( String[] args ){
       String str = String.format("%04d" , 2 );
        System.out.println( str );
        User user = new User();
        long size2 = 0 ;
        long time1 = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            byte[] bvalue = serializer.serialize(user);
            size2 += bvalue.length;
            User u = (User)serializer.deserialize( bvalue );
        }
        System.out.println("本地序列化方案[序列化10000次]耗时："
                + (System.currentTimeMillis() - time1) + "ms size:=" + size2);


    }

}
