package com.koolearn.cloud.school.teacher.controller;

import com.alibaba.fastjson.JSONObject;
import com.koolearn.cloud.base.controller.BaseController;
import com.koolearn.cloud.common.entity.User;
import com.koolearn.cloud.common.serializer.CommonInstence;
import com.koolearn.cloud.school.common.SchoolCommonService;
import com.koolearn.cloud.school.dto.TeacherPageDto;
import com.koolearn.cloud.school.entity.Manager;
import com.koolearn.cloud.school.entity.SchoolInfo;
import com.koolearn.cloud.school.teacher.TeacherManageService;
import com.koolearn.cloud.school.teacher.vo.TeacherPageResultDto;
import com.koolearn.cloud.school.teacher.vo.TeacherUploadExcelDto;
import com.koolearn.cloud.teacher.service.StudentManageService;
import com.koolearn.cloud.util.CheckUtil;
import com.koolearn.library.maintain.remote.service.InterfaceCloudService;
import com.koolearn.sso.dto.RegistDTO;
import com.koolearn.sso.dto.RegisterRet;
import com.koolearn.sso.service.IOpenService;
import com.koolearn.util.SystemGlobals;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by fn on 2016/11/3.
 */
@RequestMapping("/school/teacher")
@Controller
public class TeacherManageController extends BaseController {

    Logger log = Logger.getLogger( this.getClass() );
    @Autowired
    private TeacherManageService teacherManageService;
    @Autowired
    private SchoolCommonService schoolCommonService;
    @Autowired
    private StudentManageService studentManageService;
    @Autowired
    private IOpenService iopenService;
    @Autowired
    InterfaceCloudService interfaceCloudService;
    /**
     * 跳转到教师管理页面
     * @param request
     * @param response
     * @return
     * /school/teacher/goTeacherManage
     */
    @RequestMapping(value = "/goTeacherManage"  , method = RequestMethod.GET )
    public String goTeacherManage(HttpServletRequest request , HttpServletResponse response ,Manager manager,ModelMap modelMap){
        return "/school/teacher/teacherManage";
    }
    /**
     * 教师分页查询的查询条件（学段，学科）
     * @param request
     * @param response
     * @param manager
     * @return
     * /school/teacher/findTeacherCondition
     */
    @ResponseBody
    @RequestMapping(value = "/findTeacherCondition"  , method = RequestMethod.POST )
    public String findTeacherCondition(HttpServletRequest request , HttpServletResponse response ,Manager manager ){
        Integer schoolId = manager.getSchoolId();
        Integer userId = manager.getId();
        if(null != schoolId && null != userId ){
            SchoolInfo schoolInfo = schoolCommonService.findAllSchoolRangeBySchoolId(schoolId, userId);
            Map< String , Object > map = new HashMap<String, Object>();
            map.put( "data" , schoolInfo);
            JSONObject param = (JSONObject) JSONObject.toJSON( map );
            log.info("教师信息分页条件结果:"+ param );
            return param.toString();
        }else{
            return "";
        }
    }

    /**
     * 分页查询教师信息
     * @param request
     * @param response
     * @param manager
     * @return
     * /school/teacher/findTeacherList
     */
    @ResponseBody
    @RequestMapping(value = "/findTeacherList"  , method = RequestMethod.POST )
    public String findTeacherList(HttpServletRequest request , HttpServletResponse response ,Manager manager,
                                  @ModelAttribute TeacherPageDto teacherPageDto){
        if( null != teacherPageDto ) {
            teacherPageDto.setSchoolId(manager.getSchoolId());
            if (teacherPageDto.getCurrentPage() == null) {
                teacherPageDto.setCurrentPage(0);
            }
            if (teacherPageDto.getPageSize() == null) {
                teacherPageDto.setPageSize(CommonInstence.PAGE_SIZE_20);
            }
            Map<String, Object> map = teacherManageService.findClassPage( teacherPageDto );
            Map< String , Object > resultMap = new HashMap<String, Object>();
            resultMap.put( "datas" ,map );
            map.put( CommonInstence.STATUS , CommonInstence.CODE_0 );
            JSONObject param = (JSONObject) JSONObject.toJSON( resultMap );
            log.info("教师信息分页查询结果:"+ param );
            System.out.println( "教师信息分页查询结果:" + param );
            return param.toString();
        }
        return "";
    }
    /**
     * 批量导入教师信息
     * @param request
     * @param response
     * @return
     * /school/teacher/uploadTeacherExcel
     */
    @ResponseBody
    @RequestMapping(value = "/uploadTeacherExcel"  , method = RequestMethod.POST )
    public String uploadTeacherExcel( MultipartFile file, HttpServletRequest request , HttpServletResponse response ,Manager manager){
        if( null != file ){
            //解析指定路径下的excel
            List< TeacherUploadExcelDto > TeacherUploadExcelErrorList = new ArrayList<TeacherUploadExcelDto>();
            JSONObject param = null;
            Map< String , Object > map = new HashMap<String, Object>();
            Map< String , Object > lastMap = new HashMap<String, Object>();
            try {
                //解析excel，查找异常数据及拼装正确数据
                List<User> userList = readForCheckTeacherXls( file  ,TeacherUploadExcelErrorList,  manager.getManagerName() );
                //生成教师账号
                if( null != userList && userList.size() > 0 ){
                    userList = teacherManageService.makeTeacherUserName( userList ,manager.getSchoolId() );
                    //入库
                    userList = addSSOAndLocalDb( userList ,manager.getSchoolId() , manager.getManagerName());
                    map.put( CommonInstence.SUCCESS_LINE , userList.size() );
                }
                map.put( CommonInstence.ERROR_LINE,TeacherUploadExcelErrorList.size() );
                map.put( CommonInstence.RESULT,TeacherUploadExcelErrorList );
                lastMap.put( CommonInstence.STATUS , CommonInstence.CODE_0 );
                lastMap.put( CommonInstence.DATA , map );
                param = (JSONObject) JSONObject.toJSON( lastMap );
                log.info("班级导入数据结果:" + param);
                return param.toString();
            }catch (Exception e) {
                e.printStackTrace();
                map.put( "list",TeacherUploadExcelErrorList );
                param = (JSONObject) JSONObject.toJSON( map );
                map.put( CommonInstence.STATUS , CommonInstence.CODE_1 );
                log.error( "班级导入数据异常" + e.getMessage() , e );
                return param.toString();
            }
        }
        return "";
    }

    /**
     * 注册SSO用户和本地云用户
     * @param userList
     * @param schoolId
     * @return
     */
    public List<User > addSSOAndLocalDb(List<User> userList ,Integer schoolId ,String operateName) throws Exception {
        List<User> userListAppand = new ArrayList<User>();
        if( userList.size() > 0 ){
            int num = userList.size() / CommonInstence.PAGE_SIZE_20 ;
            int last = userList.size() % CommonInstence.PAGE_SIZE_20;
            for( int i = 0 ; i< num;i++ ){
                List< User > ssoList = userList.subList( i* CommonInstence.PAGE_SIZE_20 , i* CommonInstence.PAGE_SIZE_20 + CommonInstence.PAGE_SIZE_20 );
                System.out.println( "第" + i +"次，截取大小:" +ssoList.size() );
                Long nowTime = System.currentTimeMillis();
                //注册SSO用户
                ssoList = insertSSOUser(ssoList);
                Long nowTime2 = System.currentTimeMillis();
                System.out.println( "调用SSO耗时:" + ( nowTime2 - nowTime )/1000 + "秒");
                //注册本地用户
                ssoList = teacherManageService.insertTeacherExcelToDb( ssoList , new Integer( schoolId ));
                userListAppand.addAll( ssoList );
                try {
                    Thread.sleep( 300 );
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if( last > 0 ){
                List< User > ssoList = userList.subList( num * CommonInstence.PAGE_SIZE_20 ,  num * CommonInstence.PAGE_SIZE_20 +last );
                Long nowTime = System.currentTimeMillis();
                ssoList = insertSSOUser(ssoList);
                Long nowTime2 = System.currentTimeMillis();
                System.out.println( "调用SSO耗时:" + ( nowTime2 - nowTime )/1000 + "秒");
                ssoList = teacherManageService.insertTeacherExcelToDb( ssoList , new Integer( schoolId ));
                userListAppand.addAll( ssoList );
            }
            //建立数图用户
            try{//数据库libirary库的customer和lib_user表
                for( User user : userListAppand ) {
                    interfaceCloudService.saveLibUser( user.getUserId() , Integer.valueOf(SystemGlobals.getPreference("libraryId")));
                }
            }catch( Exception e ){
                schoolCommonService.insertErrorLog( "数图:saveLibUser" ,
                                                    "创建数图用户接口异常,"+ e.getMessage(),
                                                    "操作人"+ operateName );
            }
        }
        return userList;
    }

    public List<User> insertSSOUser(List<User> userList  ) throws Exception {
        try {
            List< RegistDTO > registDTOList = new LinkedList<RegistDTO>(  );
            for( int i =0;i<userList.size();i++ ) {
                RegistDTO registDTO = new RegistDTO();
                registDTO.setUserName(userList.get(i).getUserName());
                registDTO.setPassword(CommonInstence.RESET_PASSWORD);
                registDTO.setRole( CommonInstence.TEACHER_TYPE_1 );
                registDTO.setChannel(CommonInstence.USER_CHANNEL);//云平台的渠道
                registDTO.setEmail(userList.get(i).getEmail());
                registDTO.setMobile(userList.get(i).getMobile());
                registDTOList.add(registDTO);
            }
            List<RegisterRet> registerRetList = iopenService.batchRegisterUser( registDTOList );
            if( null != registerRetList ){
                for( int i = 0 ;i < registDTOList.size();i++ ){
                    userList.get( i ).setUserId( registerRetList.get( i ).getUserId() );
                }
            }
        } catch (Exception e) {
            schoolCommonService.insertErrorLog( "SSO:batchRegisterUser接口" ,
                                                "批量创建教师调SSO接口异常,"+ e.getMessage(),
                                                "");
            log.error( "批量添加SSO用户接口信息异常" + "," + e.getMessage() ,e );
            e.printStackTrace();
            throw new Exception( "批量添加SSO用户接口信息异常" + "," + e.getMessage() , e );
        }
        return userList;
    }

    /**
     * 验证教师的excel数据正确性
     * @param file
     * @param teacherUploadExcelErrorList
     * @param creator
     * @return
     * @throws IOException
     */
    public List<User> readForCheckTeacherXls( MultipartFile file,  List< TeacherUploadExcelDto > teacherUploadExcelErrorList ,String creator ) throws IOException {
        List< User > userList = new ArrayList<User>();
        Workbook wb = null;
        int line = 0;//异常行数
        try {
            //wb = WorkbookFactory.create(ins);
            wb = WorkbookFactory.create( file.getInputStream() );
            //3.得到Excel工作表对象
            Sheet sheet = wb.getSheetAt(0);
            //总行数
            int trLength = sheet.getLastRowNum();
            User user = null;
            for (int i = 1; i <= trLength; i++) {
                TeacherUploadExcelDto errorDto = new TeacherUploadExcelDto();
                line = i;
                int hasErrorNum = 0;
                user = new User();
                //得到Excel工作表的行
                Row row = sheet.getRow(i);
                //得到Excel工作表指定行的单元格
                Cell realNameCell = row.getCell(0);
                Cell mobileCell = row.getCell(1);
                Cell emailCell = row.getCell(2);

                StringBuffer errorStr = new StringBuffer();
                //姓名判断
                if( null == realNameCell ){
                    errorStr.append( "第"+( i+1)+"行" + "教师姓名为空," ) ;
                    errorDto.setLine( i + 1);
                    errorDto.setRealName( "" );
                    hasErrorNum++;
                }else if( realNameCell.getCellType() != realNameCell.CELL_TYPE_STRING ){
                    errorStr.append( "第"+( i+1)+"行" + "教师姓名格式有误," );
                    errorDto.setRealName( getValue( realNameCell ) );
                    hasErrorNum++;
                }else if( getValue( realNameCell ).length() > 30 ){
                    errorStr.append( "第"+( i+1)+"行" + "教师姓名过长，不要大于50字," );
                    errorDto.setRealName( getValue( realNameCell ) );
                    hasErrorNum++;
                }else{
                    String realName = getValue( realNameCell );
                    user.setRealName( realName );
                    errorDto.setRealName( getValue( realNameCell ) );
                }

                //邮箱判断
                try{
                    if( null == emailCell ){
                        errorStr.append( "第"+( i+1)+"行" + "教师邮箱为空," ) ;
                        errorDto.setLine( i + 1);
                        errorDto.setEmail( "" );
                        hasErrorNum++;
                    }else if( isExistMail(getValue( emailCell )) ){
                        errorStr.append("第" + (i + 1) + "行" + "教师邮箱" + getValue(emailCell) + "已被使用,");
                        errorDto.setEmail( getValue( emailCell ) );
                        hasErrorNum++;
                    }else if( emailCell.getCellType() != emailCell.CELL_TYPE_STRING ){
                        errorStr.append( "第"+( i+1)+"行" + "教师邮箱格式有误," );
                        errorDto.setEmail( getValue( emailCell ) );
                        hasErrorNum++;
                    }else if( getValue( emailCell ).length() > 50 ){
                        errorStr.append( "第"+( i+1)+"行" + "教师邮箱过长，不要大于50字," );
                        errorDto.setEmail( getValue( emailCell ) );
                        hasErrorNum++;
                    }else if( !CheckUtil.isMail( getValue( emailCell ) ) ){
                        errorStr.append( "第"+( i+1)+"行" + "教师邮箱格式不正确," );
                        errorDto.setEmail( getValue( emailCell ) );
                        hasErrorNum++;
                    }else{
                        errorDto.setEmail( getValue( emailCell ) );
                        String email = getValue( emailCell );
                        user.setEmail( email );
                    }
                }catch ( Exception e ){}
                //电话判断
                try{
                    System.out.println( new DecimalFormat("#").format(mobileCell.getNumericCellValue()) );
                    if( null == mobileCell ){
                        errorStr.append( "第"+( i+1)+"行" + "教师电话为空," ) ;
                        errorDto.setLine(i + 1);
                        errorDto.setMobile( "" );
                        hasErrorNum++;
                    }else if( isExistMobile( new DecimalFormat("#").format(mobileCell.getNumericCellValue())) ){
                        errorStr.append("第" + (i + 1) + "行" + "教师电话" + new DecimalFormat("#").format(mobileCell.getNumericCellValue()) + "已被使用,");
                        errorDto.setMobile( new DecimalFormat("#").format(mobileCell.getNumericCellValue()) );
                        hasErrorNum++;
                    }else if( mobileCell.getCellType() != mobileCell.CELL_TYPE_NUMERIC ){
                        errorStr.append( "第"+( i+1)+"行" + "教师电话格式有误," );
                        errorDto.setMobile( new DecimalFormat("#").format(mobileCell.getNumericCellValue()) );
                        hasErrorNum++;
                    }else if( new DecimalFormat("#").format(mobileCell.getNumericCellValue()).length() > 11 ){//格式化数字过长时的科学计数法
                        errorStr.append( "第"+( i+1)+"行" + "教师电话过长," );
                        errorDto.setMobile( new DecimalFormat("#").format(mobileCell.getNumericCellValue()) );
                        hasErrorNum++;
                    }else if( !CheckUtil.isMobileNO( new DecimalFormat("#").format(mobileCell.getNumericCellValue()) ) ){
                        errorStr.append( "第"+( i+1)+"行" + "教师电话格式不正确," );
                        errorDto.setMobile( new DecimalFormat("#").format(mobileCell.getNumericCellValue()) );
                        hasErrorNum++;
                    }else{
                        String mobile = new DecimalFormat("#").format(mobileCell.getNumericCellValue());
                        user.setMobile( mobile );
                        errorDto.setEmail( getValue( emailCell ) );
                    }
                }catch( Exception e ){}
                errorDto.setErrorInfo( errorStr.toString() );
                if( hasErrorNum > 0 ){
                    errorDto.setLine( i + 1 );
                    teacherUploadExcelErrorList.add( errorDto );
                }else{
                    user.setCreator( creator );
                    userList.add( user );
                }
            }
        }catch (InvalidFormatException e) {
            log.error("第" + line + "行发生异常");
            e.printStackTrace();
        }catch (FileNotFoundException e) {
            log.error( "批量导入教师发生异常" );
            e.printStackTrace();
        }catch (IOException e) {
            log.error( "批量导入教师发生异常" );
            e.printStackTrace();
        }
        return userList;
    }
    /**
     * 验证邮箱是否重复
     * @param request
     * @param email
     * @return
     * /school/teacher/checkIsExistMail?email=fusui@126.com
     */
    @ResponseBody
    @RequestMapping(value = "/checkIsExistMail"  , method = RequestMethod.POST )
    public String checkIsExistMail(HttpServletRequest request ,@RequestParam("email")String email ,Manager manager ){
        Map< String , Object > map = new HashMap<String, Object>();
        if(StringUtils.isNotEmpty( email ) && null != manager ){
            if( isExistMail( email )){
                map.put( "code" , CommonInstence.CODE_400 );
                map.put( "message" , "邮箱已存在" );
            }else{
                if( email.length() > 30 ){
                    map.put( CommonInstence.STATUS , CommonInstence.CODE_1 );
                    map.put( "message" , "邮箱过长" );
                }else if( !CheckUtil.isMail( email)){
                    map.put( CommonInstence.STATUS , CommonInstence.CODE_1 );
                    map.put( "message" , "邮箱格式不正确" );
                }else{
                    map.put( CommonInstence.STATUS , CommonInstence.CODE_0 );
                }
            }
        }
        JSONObject param = (JSONObject) JSONObject.toJSON( map );
        log.info("验证邮箱结果:"+ param );
        return param.toString();
    }

    /**
     * 验证手机是否重复
     * @param request
     * @param mobile
     * @return
     * /school/teacher/checkIsExistMobile?mobile=13289898888
     */
    @ResponseBody
    @RequestMapping(value = "/checkIsExistMobile"  , method = RequestMethod.POST )
    public String checkIsExistMobile(HttpServletRequest request ,@RequestParam("mobile")String mobile ){
        Map< String , Object > map = new HashMap<String, Object>();
        if(StringUtils.isNotEmpty( mobile )){
            if( isExistMobile( mobile )){
                map.put( CommonInstence.STATUS , CommonInstence.CODE_1 );
                map.put( "message" , "电话已存在" );
            }else{
                if( !CheckUtil.isMobileNO(mobile )){
                    map.put( CommonInstence.STATUS , CommonInstence.CODE_1 );
                    map.put( "message" , "电话格式有误" );
                }else{
                    map.put( CommonInstence.STATUS , CommonInstence.CODE_0 );
                }
            }
        }
        JSONObject param = (JSONObject) JSONObject.toJSON( map );
        log.info("验证电话结果:"+ param );
        return param.toString();
    }

    /**
     * 添加教师信息
     * @return
     * /school/teacher/addTeacherBaseInfo?realName=test&mobile=13100909989&email=test231@126.com
     */
    @ResponseBody
    @RequestMapping(value = "/addTeacherBaseInfo"  , method = RequestMethod.POST )
    public String addTeacherBaseInfo( HttpServletRequest request ,@ModelAttribute User user,Manager manager ){
        Map< String , Object > map = new HashMap<String, Object>();
        if( null != user && StringUtils.isNotEmpty( user.getMobile() )&&
                StringUtils.isNotEmpty( user.getEmail()) && StringUtils.isNotEmpty( user.getRealName() )){
            try {
                teacherManageService.addTeacherBaseInfo(user, manager.getSchoolId());
            } catch (Exception e) {
                e.printStackTrace();
            }
            map.put( CommonInstence.STATUS , CommonInstence.CODE_0 );
        }else{
            map.put( CommonInstence.STATUS , CommonInstence.CODE_1 );
            map.put( "message" , "参数有误" );
        }
        JSONObject param = (JSONObject) JSONObject.toJSON( map );
        log.info("添加教师结果:"+ param );
        return param.toString();
    }

    /**
     * 查询教师信息
     * @return
     * /school/teacher/findTeacherInfoForUpdate?userId=243805
     */
    @ResponseBody
    @RequestMapping(value = "/findTeacherInfoForUpdate"  , method = RequestMethod.POST )
    public String findTeacherInfoForUpdate( HttpServletRequest request ,@RequestParam("id")Integer userId,Manager manager ){
        Map< String , Object > map = new HashMap<String, Object>();
        if( null != userId  ){
            try {
                TeacherPageResultDto teacherPageResultDto = teacherManageService.findTeacherInfoForUpdate( userId );
                map.put( CommonInstence.STATUS , CommonInstence.CODE_0 );
                map.put( CommonInstence.DATA , teacherPageResultDto );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            map.put( CommonInstence.STATUS , CommonInstence.CODE_1 );
            map.put( "message" , "参数有误" );
        }
        JSONObject param = (JSONObject) JSONObject.toJSON( map );
        log.info("查询教师结果:"+ param );
        return param.toString();
    }
    /**
     * 更新教师信息
     * @return
     * /school/teacher/updateTeacherBaseInfo?id=244180&mobile=13111111111&email=1111@126.com&realName=test
     */
    @ResponseBody
    @RequestMapping(value = "/updateTeacherBaseInfo"  , method = RequestMethod.POST )
    public String updateTeacherBaseInfo( HttpServletRequest request ,@ModelAttribute User user,Manager manager ){
        Map< String , Object > map = new HashMap<String, Object>();
        if( null != user && StringUtils.isNotEmpty( user.getMobile() ) && user.getId() != null &&
                StringUtils.isNotEmpty( user.getEmail()) && StringUtils.isNotEmpty( user.getRealName() )){
            try {
                user.setUpdater( manager.getManagerName() );
                teacherManageService.updateTeacherBaseInfo(user);
            } catch (Exception e) {
                map.put( CommonInstence.STATUS , CommonInstence.CODE_1 );
                map.put( "message" , "更新失败" );
                e.printStackTrace();
            }
            map.put( CommonInstence.STATUS , CommonInstence.CODE_0 );
        }else{
            map.put( CommonInstence.STATUS , CommonInstence.CODE_1 );
            map.put( "message" , "参数有误" );
        }
        JSONObject param = (JSONObject) JSONObject.toJSON( map );
        log.info("更新教师信息结果:"+ param );
        return param.toString();
    }
    /**
     * 更新教师或学生状态（ 冻结，解冻 ）
     * @return
     * /school/teacher/updateTeacherOrStudentStatus?userId=244180&status=10
     */
    @ResponseBody
    @RequestMapping(value = "/updateTeacherOrStudentStatus"  , method = RequestMethod.POST )
    public String updateTeacherOrStudentStatus( HttpServletRequest request ,
                                                @RequestParam("status")String status,
                                                @RequestParam("userId")String userId ,Manager manager ){
        Map< String , Object > map = new HashMap<String, Object>();
        if(StringUtils.isNotEmpty( status ) && StringUtils.isNotEmpty( userId )){
            try {
                teacherManageService.updateTeacherOrStudentStatus(new Integer(userId), new Integer(status), manager.getManagerName());
            } catch (Exception e) {
                map.put( "code" , CommonInstence.CODE_400 );
                map.put( "message" , "更新失败" );
                e.printStackTrace();
            }
            map.put( CommonInstence.STATUS , CommonInstence.CODE_0 );
        }else{
            map.put( CommonInstence.STATUS , CommonInstence.CODE_1 );
            map.put( "message" , "参数有误" );
        }
        JSONObject param = (JSONObject) JSONObject.toJSON( map );
        log.info("更新教师状态结果:"+ param );
        return param.toString();
    }
    /**
     * 重置教师密码
     * @param request
     * @return 127.0.0.1/studentManage/resetStudentPassword?ssoUserId=12121
     * /school/teacher/resetTeacherPassword?ssoUserId=63545206
     */
    @ResponseBody
    @RequestMapping(value = "/resetTeacherPassword", method = RequestMethod.POST )
    public String resetTeacherPassword(HttpServletRequest request) {
        String teacherId = request.getParameter("ssoUserId");
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotEmpty(teacherId)) {
            try {
                studentManageService.resetStudentPassword(teacherId);
                map.put( CommonInstence.STATUS , CommonInstence.CODE_0 );
            } catch (Exception e) {
                map.put( CommonInstence.STATUS , CommonInstence.CODE_1 );
                map.put("message", CommonInstence.MESSAGE_SSO);
            }
        }
        JSONObject param = (JSONObject) JSONObject.toJSON(map);
        log.info(param);
        return param.toString();
    }


    /**
     * 判断邮箱是否存在
     * @param email
     * @return
     */
    public boolean isExistMail( String email ){
        return teacherManageService.isExistMail( email );
    }
    /**
     * 判断电话是否存在
     * @param mobile
     * @return
     */
    public boolean isExistMobile( String mobile ){
        return teacherManageService.isExistMobile(mobile);
    }

    @SuppressWarnings("static-access")
    public String getValue(Cell hssfCell) {
        if (hssfCell.getCellType() == hssfCell.CELL_TYPE_STRING) {
            return String.valueOf(hssfCell.getStringCellValue());
        } else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
            // 返回数值类型的值
            return String.valueOf(hssfCell.getNumericCellValue());
        } else {
            return "";
        }
    }









}
