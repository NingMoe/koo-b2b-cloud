package com.koolearn.cloud.school.schoolclasses.controller;

import com.alibaba.fastjson.JSONObject;
import com.koolearn.cloud.base.controller.BaseController;
import com.koolearn.cloud.common.entity.Classes;
import com.koolearn.cloud.common.serializer.CommonEnum;
import com.koolearn.cloud.common.serializer.CommonInstence;
import com.koolearn.cloud.school.common.SchoolCommonService;
import com.koolearn.cloud.school.dto.ClassPageDto;
import com.koolearn.cloud.school.entity.Manager;
import com.koolearn.cloud.school.entity.SchoolInfo;
import com.koolearn.cloud.school.entity.dto.SubjectDto;
import com.koolearn.cloud.school.entity.dto.TeacherDto;
import com.koolearn.cloud.school.schooclasses.ClassesManageService;
import com.koolearn.cloud.school.teacher.vo.ClassesUploadExcelDto;
import com.koolearn.cloud.teacher.service.StudentManageService;
import com.koolearn.cloud.teacher.service.TeacherAddClassService;
import com.koolearn.cloud.util.DateFormatUtils;
import com.koolearn.cloud.util.KlbTagsUtil;
import com.koolearn.cloud.util.SchoolUploadFileUtil;
import com.koolearn.cloud.util.TeacherCommonUtils;
import com.koolearn.klb.tags.entity.Tags;
import org.apache.commons.lang3.StringUtils;
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
import java.net.FileNameMap;
import java.net.URLConnection;
import java.sql.SQLException;
import java.util.*;


/**
 * Created by fn on 2016/10/31.
 */
@RequestMapping("/school/classes")
@Controller
public class ClassesManageController extends BaseController {

    Logger log = Logger.getLogger( this.getClass() );
    @Autowired
    private ClassesManageService classesManageService;
    @Autowired
    private SchoolCommonService schoolCommonService;
    @Autowired
    private TeacherAddClassService teacherAddClassService;
    @Autowired
    private StudentManageService studentManageService;
    public ClassesManageService getClassesManageService() {
        return classesManageService;
    }

    public void setClassesManageService(ClassesManageService classesManageService) {
        this.classesManageService = classesManageService;
    }

    public SchoolCommonService getSchoolCommonService() {
        return schoolCommonService;
    }

    public void setSchoolCommonService(SchoolCommonService schoolCommonService) {
        this.schoolCommonService = schoolCommonService;
    }

    public TeacherAddClassService getTeacherAddClassService() {
        return teacherAddClassService;
    }

    public void setTeacherAddClassService(TeacherAddClassService teacherAddClassService) {
        this.teacherAddClassService = teacherAddClassService;
    }

    /**
     * 跳转到班级管理页面
     * @param request
     * @param response
     * @return
     * /school/classes/goClassesManage
     */
    @RequestMapping(value = "/goClassesManage"  , method = RequestMethod.GET )
    public String goClassesManage(HttpServletRequest request , HttpServletResponse response ,Manager manager){
        Integer schoolId = manager.getSchoolId();
        Integer userId = manager.getId();
        request.setAttribute( "schoolId" ,schoolId );
        request.setAttribute( "userId" ,userId );
        return "/school/schoolclasses/classManage";
    }

    /**
     * 初始化查询条件：当前学校下的所有学段和年级班级
     * @param request
     * @param response
     * @param manager
     * @return
     * /school/schooclasses/findAllSubjectClasses
     */
    @ResponseBody
    @RequestMapping(value = "/findAllSubjectClasses"  , method = RequestMethod.POST )
    public String findAllSubjectClasses( HttpServletRequest request , HttpServletResponse response ,Manager manager){
        Integer schoolId = manager.getSchoolId();
        Integer userId = manager.getId();
        if(null != schoolId && null != userId ){
            SchoolInfo schoolInfo = schoolCommonService.findAllSchoolBySchoolId( schoolId , userId);
            JSONObject param = (JSONObject) JSONObject.toJSON( schoolInfo );
            log.info("初始化查询条件:"+ param );
            System.out.println( param );
            return param.toString();
        }
        return "";
    }

    /**
     * 班级信息分页查询
     * @return
     * /school/schooclasses/findClassPage
     */
    @ResponseBody
    @RequestMapping(value = "/findClassPage"  , method = RequestMethod.POST )
    public String findClassPage(HttpServletRequest request , HttpServletResponse response ,Manager manager ,
                                @ModelAttribute ClassPageDto classPageDto ){
        if( null != classPageDto ){
            classPageDto.setSchoolId( manager.getSchoolId() );
            if( classPageDto.getCurrentPage() == null ){
                classPageDto.setCurrentPage(0);
            }if( classPageDto.getPageSize() == null ){
                classPageDto.setPageSize( CommonInstence.PAGE_SIZE_20 );
            }
            Map< String , Object > map = classesManageService.findClassPage( classPageDto );
            Map< String , Object > resultMap = new HashMap<String, Object>();
            resultMap.put( "datas" ,map );
            resultMap.put( CommonInstence.STATUS , CommonInstence.CODE_0 );
            JSONObject param = (JSONObject) JSONObject.toJSON( resultMap );
            log.info("班级信息分页查询结果:"+ param );
            System.out.println( "班级信息分页查询结果:" + param );
            return param.toString();
        }
        return "";
    }

    /**
     * 下载导入班级模板
     * @return
     * cloud.trunk.koolearn.com/school/classes/downClassTemplate
     */
    @RequestMapping(value = "/downClassTemplate"  , method = RequestMethod.GET )
    public void downClassTemplate(HttpServletRequest request,  HttpServletResponse response){
        String type = request.getParameter( "type" );//模板类型
        try {
            response.setContentType("text/html;charset=UTF-8");
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        String realName = null;
        if( "c".equals( type )){//班级的模板
            realName = "班级名单导入模板.xlsx";
        }else if( "t".equals( type )){//老师的模板
            realName = "老师名单导入模板.xlsx";
        }
        String codedfilename = null;
        String strDirPath = request.getSession().getServletContext().getRealPath("/");
        String path2 = strDirPath+"\\template\\"+realName;
        File file = new File(path2);
        if( null != file && file.exists() ){
            long fileLength = file.length();
            try {
                //解决IE浏览器中文乱码
                String agent = request.getHeader("USER-AGENT");
                if (null != agent && -1 != agent.indexOf("MSIE") || null != agent  && -1 != agent.indexOf("Trident")) {// ie
                    String name = java.net.URLEncoder.encode(realName, "UTF8");
                    codedfilename = name;
                }else{
                    codedfilename = realName;
                }

                response.setContentType(getType(realName));
                response.setHeader("Content-disposition", "attachment; filename="
                        + new String(codedfilename.getBytes("UTF-8"), "ISO8859-1"));
                response.setHeader("Content-Length", String.valueOf(fileLength));
                bis = new BufferedInputStream(new FileInputStream(file));
                bos = new BufferedOutputStream(response.getOutputStream());
                byte[] buff = new byte[2048];
                int bytesRead;
                while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                    bos.write(buff, 0, bytesRead);
                }
                bis.close();
                bos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 根据文件名获取类型（直线运动的图象.pptx）
     * @param filename
     * @return
     * @throws Exception
     */
    public static  String getType(String filename) throws Exception {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String mimeType = fileNameMap.getContentTypeFor(filename);
        return mimeType;
    }

    /**
     * 批量导入班级信息
     * @param request
     * @param response
     * @return
     * /school/classes/uploadClassExcel
     */
    @ResponseBody
    @RequestMapping(value = "/uploadClassExcel"  , method = RequestMethod.POST )
    public String uploadClassExcel( MultipartFile file, HttpServletRequest request , HttpServletResponse response ,Manager manager){
        if( null != file ){
            //先上传到指定路径 ,然后倒入数据
            String strDirPath = request.getSession().getServletContext().getRealPath("/");
            String uploadPath = strDirPath+"\\downexcel\\";
            //先上传到指定路径
            String extendName = SchoolUploadFileUtil.uploadClassesFile(file, uploadPath);
            //解析指定路径下的excel
            List<ClassesUploadExcelDto> errorList = new ArrayList<ClassesUploadExcelDto>();
            Map< String , Object > map = new HashMap<String, Object>();
            Map< String , Object > lastMap = new HashMap<String, Object>();
            JSONObject param = null;
            try {
                List<Classes> classesList = readXls( uploadPath + extendName ,errorList ,manager.getSchoolId() );
                //删除文件
                File lastFile = new File(uploadPath + extendName);
                if( lastFile.exists()){
                    // TODO 删除文件失败
                    lastFile.delete();
                }
                if( null != classesList && classesList.size() > 0 ){
                   int num = classesManageService.insertClassesLargeInfo( classesList );
                    map.put( CommonInstence.SUCCESS_LINE, num );
                }
                map.put( CommonInstence.ERROR_LINE,errorList.size() );
                map.put( CommonInstence.RESULT,errorList.size() > 0 ? errorList:"{}" );
                lastMap.put( CommonInstence.STATUS , CommonInstence.CODE_0 );
                lastMap.put( CommonInstence.DATA , map );
                param = (JSONObject) JSONObject.toJSON( lastMap );
                log.info("班级批量导入结果:"+ param );
                System.out.println( "班级批量导入异常结果:" + param );
                return param.toString();
            } catch (Exception e) {
                //map.put( CommonInstence.DATA,errorList );
                map.put( CommonInstence.DATA,"{}" );
                map.put( CommonInstence.STATUS , CommonInstence.CODE_1 );
                param = (JSONObject) JSONObject.toJSON( map );
                e.printStackTrace();
                return param.toString();
            }
        }
        return "";
    }

    /**
     * 解析excel
     * @param path
     * @param errorlist :记录异常信息
     * @return
     * @throws java.io.IOException
     */
    private List<Classes> readXls( String path  , List< ClassesUploadExcelDto > errorlist ,Integer schoolId ) throws IOException {
        //TODO 先生成班级编码 ,
        List< String > listCode = teacherAddClassService.findClassesCode();
        List<Classes> list = new ArrayList<Classes>();
        InputStream ins = null;
        Workbook wb = null;
        Classes classes = null;
        //InputStream inputStream = new FileInputStream( path );
        ins=new FileInputStream(new File(path));
        int line = 0;//异常行数
        try {
            wb = WorkbookFactory.create(ins);
            //3.得到Excel工作表对象
            Sheet sheet = wb.getSheetAt(0);
            //总行数
            int trLength = sheet.getLastRowNum();
            for( int i=1 ; i <= trLength ; i++ ){
                line = i;
                int hasErrorNum =0;
                classes = new Classes();
                //得到Excel工作表的行
                Row row1 = sheet.getRow(i);
                //得到Excel工作表指定行的单元格
                Cell classaNameCell = row1.getCell(0);
                Cell yearCell = row1.getCell(1);
                Cell rangeCell = row1.getCell(2);
                Cell typeCell = row1.getCell(3);
                Cell subjectCell = row1.getCell(4);
                ClassesUploadExcelDto classesUploadExcelDto = new ClassesUploadExcelDto();
                StringBuffer errorStr = new StringBuffer();
                //获得每一列中的值
                if( null == classaNameCell ){
                    errorStr.append( "第"+( i+1)+"行" + "班级名称为空," );
                    classesUploadExcelDto.setClassesName( " " );
                    hasErrorNum++;
                }else if( classaNameCell.getCellType() != classaNameCell.CELL_TYPE_STRING ){
                    errorStr.append( "第"+( i+1)+"行" + "班级名称格式有误," );
                    classesUploadExcelDto.setClassesName(getValue( classaNameCell ) );
                    hasErrorNum++;
                }else if(getValue( classaNameCell ).length() > 50 ){
                    errorStr.append( "第"+( i+1)+"行" + "班级名称过长，不要大于50字," );
                    classesUploadExcelDto.setClassesName(getValue( classaNameCell ) );
                    hasErrorNum++;
                } else{
                    String classaName = getValue( classaNameCell );
                    classes.setClassName( classaName );
                    classesUploadExcelDto.setClassesName( classaName );
                }
                if( null == yearCell ){
                    errorStr.append( "第"+( i+1)+"行" + "班级入学年份为空," );
                    classesUploadExcelDto.setYear( " " );
                    hasErrorNum++;
                }else if( yearCell.getCellType() != yearCell.CELL_TYPE_NUMERIC ){
                    errorStr.append( "第"+( i+1)+"行" + "班级入学年份有误," );
                    classesUploadExcelDto.setYear( getValue( yearCell ) );
                    hasErrorNum++;
                }else if( new Integer( getValue( yearCell ).substring( 0,4 ) ) > DateFormatUtils.getYear(new Date()) ){
                    errorStr.append( "第"+( i+1)+"行" + "班级入学年份不要大于当前年份," );
                    classesUploadExcelDto.setYear( getValue( yearCell ).substring( 0,4 ) );
                    hasErrorNum++;
                }else if( new Integer( getValue( yearCell ).substring( 0,4 ) ) < 2008 ){
                    errorStr.append( "第"+( i+1)+"行" + "班级入学年份已低于最低要求," );
                    classesUploadExcelDto.setYear( getValue( yearCell ).substring( 0,4 ) );
                    hasErrorNum++;
                }else{
                    String year = getValue( yearCell );
                    if( year.endsWith(".0")){
                        classes.setYear( new Integer( year.substring( 0,4 ) ) );
                    }else{
                        classes.setYear( new Integer( year ) );
                    }
                    classesUploadExcelDto.setYear( getValue( yearCell ).substring( 0,4 ) );
                }

                Integer subjectId = null;//学科id
                if( null == typeCell ){
                    errorStr.append( "第"+( i+1)+"行" + "班级类型为空," );
                    classesUploadExcelDto.setClassesType( " " );
                    hasErrorNum++;
                }else if( typeCell.getCellType() != typeCell.CELL_TYPE_STRING ){
                    errorStr.append( "第"+( i+1)+"行" + "班级类型有误," );
                    classesUploadExcelDto.setClassesType( getValue( typeCell ) );
                    hasErrorNum++;
                }else if( CommonEnum.getClassTypeEnum.getSource(getValue( typeCell ).trim() ) == null ){
                    errorStr.append( "第"+( i+1)+"行" + "班级类型不在筛选范围内," );
                    classesUploadExcelDto.setClassesType( getValue( typeCell ) );
                    hasErrorNum++;
                } else{
                    classesUploadExcelDto.setClassesType( getValue( typeCell ) );
                    String type = getValue( typeCell );
                    if( "普通班".equals( type )){//行政班
                        classes.setType( CommonInstence.CLASSES_TYPE_0);
                    }else if( "学科班".equals( type )){//学科班
                        classes.setType( CommonInstence.CLASSES_TYPE_1 );
                    }
                    //如果班级类型不为空则继续判断
                    if( null == subjectCell && classes.getType()== CommonInstence.STATUS_1 ){
                        errorStr.append( "第"+( i+1)+"行" + "学科班级的学科不能为空," );
                        classesUploadExcelDto.setSubjectName( getValue( subjectCell ) );
                        hasErrorNum++;
                    }else if( subjectCell != null && classes.getType()== CommonInstence.STATUS_0 ){
                        errorStr.append( "第"+( i+1)+"行" + "普通班级不需要有学科," );
                        classesUploadExcelDto.setSubjectName( getValue( subjectCell ) );
                        hasErrorNum++;
                    }else if( classes.getType()== CommonInstence.STATUS_1 && subjectCell != null &&CommonEnum.getSubjectIdEnum.getSource(getValue( subjectCell ).trim() ) == null ){
                        errorStr.append( "第"+( i+1)+"行" + "班级学科不在筛选范围内," );
                        classesUploadExcelDto.setSubjectName( getValue( subjectCell ) );
                        hasErrorNum++;
                    }else if( classes.getType()== CommonInstence.STATUS_1 && subjectCell != null && subjectCell.getCellType() != subjectCell.CELL_TYPE_STRING ){
                        errorStr.append( "第"+( i+1)+"行" + "班级学科有误," );
                        classesUploadExcelDto.setSubjectName( getValue( subjectCell ) );
                        hasErrorNum++;
                    }else if( classes.getType()== CommonInstence.STATUS_1 && subjectCell != null){
                        String subjectName = getValue( subjectCell );
                        subjectId = CommonEnum.getSubjectIdEnum.getSource(subjectName ).getValue();
                        classes.setSubjectId( subjectId );
                        classes.setSubjectName(subjectName);
                        classesUploadExcelDto.setSubjectName( getValue( subjectCell ) );
                    }
                }

                if( null == rangeCell ){
                    errorStr.append( "第"+( i+1)+"行" + "班级学段为空," );
                    classesUploadExcelDto.setRangeName( " " );
                    hasErrorNum++;
                }else if( rangeCell.getCellType() != rangeCell.CELL_TYPE_STRING ){
                    errorStr.append( "第"+( i+1)+"行" + "班级学段有误," );
                    classesUploadExcelDto.setRangeName( getValue( rangeCell ) );
                    hasErrorNum++;
                }else if( CommonEnum.RangeTypeEnum.getSource(getValue( rangeCell ).trim() ) == null ){
                    errorStr.append( "第"+( i+1)+"行" + "班级学段不在筛选范围内," );
                    classesUploadExcelDto.setRangeName( getValue( rangeCell ) );
                    hasErrorNum++;
                }else{
                    classesUploadExcelDto.setRangeName( getValue( rangeCell ) );
                    if( hasErrorNum == 0 ){
                        String range = getValue( rangeCell );
                        classes.setRangeName( range );
                        //获取学段id
                        if( null != subjectId && classes.getType() == 1 ){
                            List<Tags> rangeTagList = KlbTagsUtil.getInstance().getTagById( subjectId );
                            if( null != rangeTagList ){
                                for( Tags tags:rangeTagList ){
                                    if( tags.getName().equals( range )){
                                        classes.setRangeId( tags.getId() );
                                    }
                                }
                            }
                        }
                    }
                }
                //没有异常数据则记录
                if( hasErrorNum == 0 ){
                    classes.setSchoolId(schoolId);
                    //生成班级编码
                    String classCode = teacherAddClassService.makeClassCode( listCode );
                    listCode.add( classCode );
                    classes.setClassCode( classCode );
                    classes.setCreateTime(new Date());
                    classes.setGraduate(CommonInstence.GRADUATE_0);
                    classes.setStatus( CommonInstence.STATUS_0 );
                    classes.setOk( false );
                    //获取班级全名
                    String fullName = TeacherCommonUtils.getClassesName(classes.getClassName(), classes.getYear(), classes.getRangeName(), 0);
                    classes.setFullName( fullName );
                    classes.setLevel( getLevel( fullName , classes.getClassName() ));
                    list.add( classes );
                }else{
                    classesUploadExcelDto.setErrorInfo( errorStr.toString() );
                    classesUploadExcelDto.setLine( i + 1 );
                    errorlist.add( classesUploadExcelDto );
                }
            }
        } catch (InvalidFormatException e) {
            log.info( "第" + line + "行发生异常" );
            e.printStackTrace();
        }
        ins.close();
        return list;
    }

    public int getLevel( String fullName , String classesName ){
        return CommonEnum.getLevelEnum.getSource( fullName.substring( 0,fullName.length() - classesName.length() )).getValue();
    }

    /**
     * 得到Excel表中的值
     *
     * @param hssfCell
     *            Excel中的每一个格子
     * @return Excel中每一个格子中的值
     */
    @SuppressWarnings("static-access")
    private String getValue(Cell hssfCell) {
        System.out.println(  " 类型是:" +hssfCell.getCellType() );

        if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
            // 返回布尔类型的值
            return String.valueOf(hssfCell.getBooleanCellValue());
        } else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
            // 返回数值类型的值
            return String.valueOf(hssfCell.getNumericCellValue());
        } else {
            // 返回字符串类型的值
            return String.valueOf(hssfCell.getStringCellValue());
        }
    }
    /**
     * 关闭或激活班级
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/closeOrOpenClasses"  , method = RequestMethod.POST )
    public String closeOrOpenClasses(HttpServletRequest request , HttpServletResponse response){
        String operType  = request.getParameter( "type" );
        String classesId = request.getParameter( "classesId" );
        Map< String , Object > map = new HashMap<String, Object>();
        if(StringUtils.isNoneEmpty( classesId )){
            try{
                classesManageService.closeOrOpenClasses( operType , new Integer( classesId ));
                map.put( CommonInstence.STATUS , CommonInstence.CODE_0 );
            }catch( SQLException e ){
                map.put( CommonInstence.STATUS , CommonInstence.CODE_1 );
                map.put( "massege" , " 关闭或激活班级异常" );
            }
        }
        JSONObject param = (JSONObject) JSONObject.toJSON( map );
        log.info("初始化查询条件:"+ param );
        return param.toString();
    }

    /**
     * 跳转到班级下教师列表页面
     * @param request
     * @param response
     * @return
     * /school/classes/goClassTeacherOrStudentList?classesId=243814
     */
    @RequestMapping(value = "/goClassTeacherOrStudentList"  , method = RequestMethod.POST )
    public String goClassTeacherOrStudentList(HttpServletRequest request , HttpServletResponse response,Manager manager,ModelMap modelMap ){
        String classesId = request.getParameter( "classesId" );
        String goType = request.getParameter( "goType" );//跳转页面区分
        request.setAttribute( "classesId" , classesId );
        request.setAttribute( "schoolId" , manager.getSchoolId() );
        //班级基本信息
        if( StringUtils.isNoneEmpty( classesId )){
            Classes classes = teacherAddClassService.findClassesById( new Integer( classesId ) );
            if( null != classes ){
                modelMap.put( "classes" ,classes );
                request.setAttribute( "type" , classes.getType()!=null?classes.getType():0 );
                //TODO　查询班级的毕业日期
                String finishDay = schoolCommonService.findClassFinishDay( manager.getSchoolId()  , classes);
                modelMap.put( "finishDay", finishDay );
            }
        }
        if( "t".equals( goType )){
            return "/school/schoolclasses/classTeacherList";
        }else{
            return "/school/schoolclasses/classStudentList";

        }
    }
    /**
     * 班级下所有学生列表查询
     * @param request
     * @param response
     * @param classPageDto
     * @param modelMap
     * @return
     * /school/classes/findClassesStudentList?classesId=243814
     */
    @ResponseBody
    @RequestMapping(value = "/findClassesStudentList"  , method = RequestMethod.POST )
    public String findClassesStudentList(HttpServletRequest request , HttpServletResponse response,
                                          @ModelAttribute ClassPageDto classPageDto ,ModelMap modelMap ){
        if( null != classPageDto ){
            Integer classesId = classPageDto.getClassesId();
            if( null != classesId ){
                if( null == classPageDto.getCurrentPage() ){
                    classPageDto.setCurrentPage(CommonInstence.PAGE_NO_0);
                }if( null == classPageDto.getPageSize() ){
                    classPageDto.setPageSize( CommonInstence.PAGE_SIZE_20 );
                }
                Map< String , Object > map = classesManageService.findClassStudentPage( classPageDto );
                map.put(CommonInstence.currentPage ,classPageDto.getCurrentPage() );
                Map< String , Object > resultMap = new HashMap<String, Object>();
                resultMap.put( "datas" ,map );
                resultMap.put( CommonInstence.STATUS , CommonInstence.CODE_0  );
                JSONObject param = (JSONObject) JSONObject.toJSON( resultMap );
                log.info("班级下所有学生分页查询结果:"+ param );
                System.out.println( "班级下所有学生分页查询结果:" + param );
                return param.toString();
            }else{
                return "";
            }
        }else{
            return "";
        }
    }
    /**
     * 班级下所有老师列表查询
     * @param request
     * @param response
     * @param classPageDto
     * @param modelMap
     * @return
     * /school/schooclasses/findClassesTeachersList?classesId=243814
     */
    @ResponseBody
    @RequestMapping(value = "/findClassesTeachersList"  , method = RequestMethod.POST )
    public String findClassesTeachersList(HttpServletRequest request , HttpServletResponse response,
                                          @ModelAttribute ClassPageDto classPageDto ,ModelMap modelMap ){
        //String classesId = request.getParameter( "classesId" );
        Map< String , Object > resultMap = new HashMap<String, Object>();
        if( null != classPageDto ){
            Integer classesId = classPageDto.getClassesId();
            if( null != classesId ){
                if( null == classPageDto.getCurrentPage() ){
                    classPageDto.setCurrentPage(CommonInstence.PAGE_NO_0);
                }if( null == classPageDto.getPageSize() ){
                    classPageDto.setPageSize( CommonInstence.PAGE_SIZE_20 );
                }
                Map< String , Object > map = classesManageService.findClassTeacherPage( classPageDto );
                map.put( CommonInstence.currentPage ,classPageDto.getCurrentPage() );
                resultMap.put( "datas" ,map );
                resultMap.put( CommonInstence.STATUS , CommonInstence.CODE_0 );
                JSONObject param = (JSONObject) JSONObject.toJSON( resultMap );
                log.info("班级下所有老师分页查询结果:"+ param );
                System.out.println( "班级下所有老师分页查询结果:" + param );
                return param.toString();
            }else{
                resultMap.put( CommonInstence.STATUS , CommonInstence.CODE_1 );
                JSONObject param = (JSONObject) JSONObject.toJSON( resultMap );
                return param.toString();
            }
        }else{
            return "";
        }
    }

    /**
     * 更改教师与班级的关系（将老师移除本班）
     * @param request
     * @param response
     * @return
     * /school/classes/updateClassesTeacherOrStudentStatus
     */
    @ResponseBody
    @RequestMapping(value = "/updateClassesTeacherOrStudentStatus"  , method = RequestMethod.POST )
    public String updateClassesTeacherOrStudentStatus( HttpServletRequest request , HttpServletResponse response ){
        String userId = request.getParameter( "userId" );
        String classesId = request.getParameter( "classesId" );
        String type = request.getParameter( "type" );//移除对象标识：（1:老师，2：学生）
        Map< String , Object> map = new HashMap<String, Object>();
        if( StringUtils.isNotEmpty( userId ) && StringUtils.isNotEmpty( classesId ) ){
            try{
                classesManageService.updateClassesTeacherOrStudentStatus( new Integer( userId ) , new Integer( classesId) , type );
                map.put( CommonInstence.STATUS , CommonInstence.CODE_0 );
            }catch( Exception e ){
                map.put( CommonInstence.STATUS , CommonInstence.CODE_1 );
                map.put( "message" , "解除老师与班级关系异常" );
            }
        }else if( type == null ){
            map.put( CommonInstence.STATUS , CommonInstence.CODE_1 );
            map.put( "message" , "角色类型标识为空" );
        }else{
            map.put( CommonInstence.STATUS , CommonInstence.CODE_1 );
            map.put( "message" , "班级参数为空" );
        }
        JSONObject param = (JSONObject) JSONObject.toJSON( map );
        System.out.println( "解除老师与班级关系结果:" + param );
        return param.toString();
    }

    /**
     * 初始化所有学科和没有加入该班级的某个学科下的所有老师
     * @return
     * /school/schooclasses/initSubjectAndClassesTeachers
     */
    @ResponseBody
    @RequestMapping(value = "/initSubjectAndClassesTeachers"  , method = RequestMethod.POST )
    public String initSubjectAndClassesTeachers(HttpServletRequest request , HttpServletResponse response ,Manager manager){
        String classesId = request.getParameter( "classesId" );
        List<TeacherDto> teacherDtoList = null;
        Map< String , Object > map = new HashMap<String, Object>();
        if( StringUtils.isNotEmpty( classesId )){
            Integer schoolId = manager.getSchoolId();
            //查询所有学科
            List<SubjectDto> subjectDtoList =  schoolCommonService.findAllSubject();
            map.put( "allSubject" , subjectDtoList );
            //查询某学科下除了当前学科下已加入该班级以外的所有老师
            /*
            teacherDtoList = classesManageService.findAllTeacherBySubjectAndClass( schoolId ,
                    subjectDtoList.get(0).getId() , new Integer( classesId) );
            map.put( "teacherList", teacherDtoList );
            */
            map.put( CommonInstence.STATUS , CommonInstence.CODE_0 );
        }

        JSONObject param = (JSONObject) JSONObject.toJSON( map );
        System.out.println( "班级下所有老师:" + param );
        return param.toString();
    }

    /**
     * 查询某学科下除了当前学科下已加入该班级以外的所有老师( 注意排除前端已选择的老师id)
     * @return
     * /school/schooclasses/findSubjectClassesTeacher
     */
    @ResponseBody
    @RequestMapping(value = "/findSubjectClassesTeacher"  , method = RequestMethod.POST )
    public String findSubjectClassesTeacher(HttpServletRequest request , HttpServletResponse response ,Manager manager){
        String classesId = request.getParameter( "classesId" );
        String subjectId = request.getParameter( "subjectId" );
        Map< String , Object > map = new HashMap<String, Object>();
        List<TeacherDto> teacherDtoList = null;
        if( StringUtils.isNotEmpty( classesId ) && StringUtils.isNotEmpty( subjectId )) {
            Integer schoolId = manager.getSchoolId();
            teacherDtoList = classesManageService.findAllTeacherBySubjectAndClass( schoolId ,
                    new Integer( subjectId ) , new Integer( classesId) );
            map.put( "teacherList", teacherDtoList );
            map.put( CommonInstence.STATUS , CommonInstence.CODE_0 );
        }
        JSONObject param = (JSONObject) JSONObject.toJSON( map );
        System.out.println( "班级下所有老师:" + param );
        return param.toString();
    }

    /**
     * 批量为某班级添加老师
     * @param request
     * @param response
     * @param manager
     * @return
     * /school/classes/addClassesTeacher?teacherIdStr=
     */
    @ResponseBody
    @RequestMapping(value = "/addClassesTeacher"  , method = RequestMethod.POST )
    public String addClassesTeacher(HttpServletRequest request , HttpServletResponse response ,Manager manager,
                                    @RequestParam("teacherIdStr")String teacherIdStr ,@RequestParam("subjectIdStr")String subjectIdStr){
        String classesId = request.getParameter( "classesId" );
        Map< String , Object > map = new HashMap<String, Object>();
        if( StringUtils.isNotEmpty( classesId ) && StringUtils.isNotEmpty( teacherIdStr )&& StringUtils.isNotEmpty( subjectIdStr )){
            Classes classes = teacherAddClassService.findClassesById( new Integer( classesId ));
            if( null != classes ){
                classesManageService.addClassesTeacher( classes , teacherIdStr,subjectIdStr );
            }
        }
        map.put( CommonInstence.STATUS , CommonInstence.CODE_0 );
        JSONObject param = (JSONObject) JSONObject.toJSON( map );
        System.out.println( "班级下所有老师:" + param );
        return param.toString();
    }

    public static void main( String[] args ){
        List< String > list = new ArrayList<String>();

        System.out.println( list.toString() );
    }

    /**
     *
     * @param request
     * @param response
     * @return
     * /school/classes/goUploadTest
     */
    @RequestMapping(value = "/goUploadTest"  , method = RequestMethod.GET )
    public String goUploadTest(HttpServletRequest request , HttpServletResponse response){
        return "/school/testUpload";
    }

    /**
     *重置密码
     * @param request
     * @param manager
     * @return
     * /school/classes/resetTeacherPassword
     */
    @ResponseBody
    @RequestMapping(value = "/resetTeacherPassword", method = RequestMethod.POST)
    public String resetTeacherPassword(HttpServletRequest request ,Manager manager) {
        String teacherId = request.getParameter("ssoUserId");
        Map<String, Object> map = new HashMap<String, Object>();
        if (org.apache.commons.lang.StringUtils.isNotEmpty(teacherId)) {
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
}
