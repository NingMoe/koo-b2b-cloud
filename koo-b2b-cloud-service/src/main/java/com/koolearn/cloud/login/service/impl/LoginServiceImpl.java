package com.koolearn.cloud.login.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.koolearn.cloud.common.entity.*;
import com.koolearn.cloud.common.serializer.CommonInstence;
import com.koolearn.cloud.login.dao.LoginDao;
import com.koolearn.cloud.login.dto.UserMobi;
import com.koolearn.cloud.login.entity.UserEntity;
import com.koolearn.cloud.login.service.LoginService;
import com.koolearn.cloud.school.entity.Manager;
import com.koolearn.cloud.school.util.CheckUtil;
import com.koolearn.cloud.teacher.entity.School;
import com.koolearn.cloud.teacher.service.TeacherInfoService;
import com.koolearn.cloud.teacherInfo.dao.ClassNewStatusDao;
import com.koolearn.cloud.teacherInfo.dao.StudentManageDao;
import com.koolearn.cloud.teacherInfo.dao.TeacherAddClassDao;
import com.koolearn.cloud.util.BizException;
import com.koolearn.cloud.util.ConnUtil;
import com.koolearn.cloud.util.GlobalConstant;
import com.koolearn.cloud.util.hexin.HttpUtil;
import com.koolearn.cloud.util.hexin.SHA1Util;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginServiceImpl implements LoginService{
    private Logger log = Logger.getLogger(this.getClass());
	LoginDao loginDao;
    @Autowired
    private StudentManageDao studentManageDao;
    @Autowired
    TeacherInfoService teacherInfoService;
	public void setLoginDao(LoginDao loginDao) {
		this.loginDao = loginDao;
	}
	@Autowired
    private ClassNewStatusDao classNewStatusDao;
    public void setClassNewStatusDao(ClassNewStatusDao classNewStatusDao) {
        this.classNewStatusDao = classNewStatusDao;
    }
    @Autowired
    private TeacherAddClassDao teacherAddClassDao;
    public void setTeacherAddClassDao(TeacherAddClassDao teacherAddClassDao) {
        this.teacherAddClassDao = teacherAddClassDao;
    }

    public TeacherInfoService getTeacherInfoService() {
        return teacherInfoService;
    }

    public void setTeacherInfoService(TeacherInfoService teacherInfoService) {
        this.teacherInfoService = teacherInfoService;
    }

    public TeacherAddClassDao getTeacherAddClassDao() {
        return teacherAddClassDao;
    }

    public StudentManageDao getStudentManageDao() {
        return studentManageDao;
    }

    public void setStudentManageDao(StudentManageDao studentManageDao) {
        this.studentManageDao = studentManageDao;
    }

    /**
	 * 插入用户信息
	 */
	@Override
	public int insertUser(UserEntity ue) {
		return loginDao.insertUser(ue);
	}

	/**
	 * 通过手机、邮箱、用户名查询用户数据，取出sso对应的用户信息 
	 */
	public UserEntity findUser(String mobileEmail) {
		List<UserEntity> li =  loginDao.findUser(mobileEmail);
		if(li!=null&&li.size()>0){
            UserEntity user=li.get(0);
            List<Integer> schoolIdsOfuser=getSchoolIdsByUser(user);
            if(schoolIdsOfuser!=null&&schoolIdsOfuser.size()>0){
                List<School> schools=loginDao.getAllSchoolsOfUser(schoolIdsOfuser);
                user.setSchoolList(schools);//设置用户学校
                List<School> fusuiSchoolList=new ArrayList<School>();
                for(School school:schools){
                    if(school.getLocationId().intValue()==GlobalConstant.FUSUI_LOCATION_ID){
                        fusuiSchoolList.add(school);
                    }
                }
                if(fusuiSchoolList.size()>0){
                    //1.设置用户扶绥学校
                    user.setFusuiSchoolList(fusuiSchoolList);
                    user.setFusui(true);
                }
            }
            List<TeacherBookVersion> plist = teacherInfoService.findTeacherBookVersion(user);
            user.setTeacherBookVersionList(plist);
			return user;
		}else{
			return null;
		}
	}
	
	@Override
	public UserMobi findMobileUser(String userName) {
		UserMobi ue = loginDao.findMobileUser(userName);
		if(ue!=null){//
			UserEntity schClaInfo = loginDao.findSchoolClassInfo(ue.getId());
	        if(schClaInfo!=null){
	            ue.setSchoolId(schClaInfo.getSchoolId());
	            ue.setSchoolName(schClaInfo.getSchoolName());
	            ue.setClassesId(schClaInfo.getClassesId());
	            ue.setClassesName(schClaInfo.getClassesName());
	        }
		}
		//("ue.getSchoolId()="+ue.getSchoolId()+"  ue.getSchoolName()="+ue.getSchoolName()+"  ue.getClassesId()="+ue.getClassesId()+"   ue.getClassesName()="+ue.getClassesName());
		return ue;
	}

    /**
     * 获取老师或学生所在的学校id
     * @param user
     * @return
     */
    public List<Integer> getSchoolIdsByUser(UserEntity user){
        List<Integer> schoolIds=new ArrayList<Integer>();
        if(user.getSchoolId()!=null){
            schoolIds.add(user.getSchoolId());
        }
        if(StringUtils.isNotBlank(user.getSchoolIdStr())){
            String[] idsArr=user.getSchoolIdStr().split(",");
            for (int i=0;i<idsArr.length;i++){
                if(StringUtils.isNotBlank(idsArr[i]) && StringUtils.isNumeric(idsArr[i])){
                    schoolIds.add(Integer.parseInt(idsArr[i]));
                }
            }
        }
        return schoolIds;
    }
	/**
	 * 更新user表数据
	 */
	@Override
	public int updateUserEntity(UserEntity ue) {
		Connection conn = ConnUtil.getTransactionConnection();
		return loginDao.updateUserEntity(conn,ue);
	}


	@Override
	public int insertClassesStudent(ClassesStudent cs, UserEntity update) {
		Connection conn = ConnUtil.getTransactionConnection();
		int count =0;
		ClassesDynamic classesDynamic = new ClassesDynamic();
		try {
			count = loginDao.insertClassesStudent(conn,cs);
            //插入班级动态表
			classesDynamic.setClassesId(cs.getClassesId());
			classesDynamic.setCreateTime(new Date());
			classesDynamic.setDynamicInfo(update.getRealName()+"加入班级");
			classesDynamic.setStatus(ClassesDynamic.STATUS_ZERO);
            int classesDynamicId = classNewStatusDao.insertClassesDynamic( conn , classesDynamic );
            //先查询当前班级下所有老师( 之前刚刚加入该班级的老师不会被下面的sql查询出来*** )
            List< ClassesTeacher> list = teacherAddClassDao.findClassesTeacherByClassesId( classesDynamic.getClassesId()+"");
            // 插入老师动态关联表
            if( null != list && list.size() > 0 ){
                for( int i = 0 ; i< list.size();i++ ){
                    ClassesDynamicTeacher classesDynamicTeacher = makeClassesDynamicTeacher( classesDynamicId , list.get( i ).getTeacherId());
                    int id = classNewStatusDao.insertClassesDynamicTeacher( conn , classesDynamicTeacher );
                }
            }
			loginDao.updateUserEntity(conn,update);
			conn.commit();
		} catch (SQLException e) {
			ConnUtil.rollbackConnection(conn);
			e.printStackTrace();
		}finally{
			ConnUtil.closeConnection(conn);
		}
		return count;
	}

	/**
     * 组装班级动态和老师的关联表对象
     * @return
     */
	private ClassesDynamicTeacher makeClassesDynamicTeacher(int classesDynamicId,Integer teacherId) {
		ClassesDynamicTeacher dynamicTeacher = new ClassesDynamicTeacher();
        dynamicTeacher.setTeacherId( teacherId );
        dynamicTeacher.setClassesDynamicId( classesDynamicId );
        dynamicTeacher.setStatus( ClassesDynamicTeacher.STATUS_ZERO );
		return dynamicTeacher;
	}

    @Override
    public User findUserByUserId(Integer userId) {

        User user = loginDao.findUserByUserId(userId);

        return user;
    }

    /**
	 * 查询学生的班级
	 * @param ue
	 * @return
	 */
	@Override
	public UserEntity findClassesStudent(UserEntity ue) {
		List<UserEntity> list = loginDao.findClassesStudent(ue.getId());
		if(!list.isEmpty()){
			return list.get(0);
		}else{
			return null;
		}
	}

    /**
     * 将sso用户信息入本地库
     * @param user
     * @return
     */
    @Override
    public int addSSOUser(User user) throws Exception {
        Connection conn = null;
        try {
            conn = ConnUtil.getTransactionConnection();
            int id = loginDao.addSSOUser( conn , user );
            conn.commit();
            return id;
        } catch (SQLException e) {
            log.error( "SSO用户信息登录后入库异常 ,用户名称:"+ user.getUserName()   , e );
            ConnUtil.rollbackConnection(conn);
            throw new Exception( "SSO用户信息登录后入库异常 ,用户名称:"+ user.getUserName()+ "," +e.getMessage()   , e );
        } catch (Exception e) {
            log.error( "SSO用户信息登录后入库异常 ,用户名称:"+ user.getUserName() + "," + e.getMessage() ,e );
            ConnUtil.rollbackConnection(conn);
            throw new Exception( "SSO用户信息登录后入库异常 ,用户名称:"+ user.getUserName() + "," + e.getMessage() , e );
        } finally {
            ConnUtil.closeConnection(conn);
        }
    }

    /**
     * 更新用户角色
     * @param userId
     * @param roleType
     * @return
     */
    @Override
    public int addUserRole(Integer userId, Integer roleType) throws Exception {
        Connection conn = null;
        try {
            conn = ConnUtil.getTransactionConnection();
            int num = loginDao.addUserRole( conn , userId , roleType );
            conn.commit();
            return num;
        } catch (SQLException e) {
            log.error( "koolearn用户更新角色类型异常 ,用户id:"+ userId + "," + e.getMessage()  , e );
            ConnUtil.rollbackConnection(conn);
            throw new Exception( "koolearn用户更新角色类型异常 ,用户id:"+ userId + "," + e.getMessage()  , e );
        } catch (Exception e) {
            log.error( "koolearn用户更新角色类型异常 ,用户id:"+ userId + "," + e.getMessage() ,e );
            ConnUtil.rollbackConnection(conn);
            throw new Exception( "koolearn用户更新角色类型异常 ,用户id:"+ userId + "," + e.getMessage(), e );
        } finally {
            ConnUtil.closeConnection(conn);
        }
    }

    /**
     * 根据用户id查询用户信息
     * @param userId
     * @return
     */
    @Override
    public UserEntity findUserById(Integer userId) {
        return loginDao.findUserById( userId , CommonInstence.STATUS_0 );
    }
    public static final String Not_fusui_result="{\"code\": -1, \"message\": \"非扶绥用户\", \"data\": null}";
    /**
     * 合心用户登录
     * @param user
     * @return
     */
    @Override
    public Map<String,String> hexinLogin(UserEntity user) {
        if(user.isFusui()){
            Map<String,String> paramMap=SHA1Util.getSignature(user);
        return paramMap;
        }
        return null;
    }
    public  void hexinParseLog(String url,String param,String result,UserEntity optUser){
        try {
            Map<String,String> nameMap=new HashMap<String, String>();
            nameMap.put("/teacher","编辑老师");
            nameMap.put("/user","编辑学生");
            nameMap.put("/group","编辑班级");
            nameMap.put("/relation","编辑用户与编辑关系");
            String nameKey=url.substring(url.lastIndexOf("/"),url.length());
            JSONObject jb=JSONObject.parseObject(result);
            Integer code=(Integer)jb.get("code");
            if(code.intValue()>0){//code=0表示正常/code>0表示异常 code=4时，表示access_key和access_secret认证失败
            }
            HexinLog hl=new HexinLog();
            hl.setName(nameMap.get(nameKey));
            hl.setCode(code);
            hl.setCreateTime(new Date());
            hl.setUrl(url);
            hl.setParam(param);
            hl.setResult(result);
            hl.setOptUser(optUser.getUserName()+"["+optUser.getUserId()+"]");
            System.out.println("合心接口-参数："+param);
            loginDao.insertHexinLog(hl);
        }catch (Exception e){
            e.printStackTrace();;
        }
    }

    /**
     * 合心老师注册
     * @param user
     * @return
     */
    @Override
    public String hexinAccountTeacher(final UserEntity user) {
        ExecutorService fixedThreadPool = Executors.newSingleThreadExecutor();
        fixedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                String result=Not_fusui_result;
                if(user.isFusui()){
                    user.setType(UserEntity.USER_TYPE_TEACHER);
                    String createUserParam=getAddStuOrTeaCommitJson(user);
                    result=HttpUtil.senPostJson(GlobalConstant.KAOYUE_URL_MAP.get(GlobalConstant.KAOYUE_URL_accoutTeacher),createUserParam);
                    hexinParseLog(GlobalConstant.KAOYUE_URL_MAP.get(GlobalConstant.KAOYUE_URL_accoutTeacher),createUserParam,result,user);
                }
            }
        });
        return "";
    }
    /**
     * 合心学生注册
     * @param user
     * @return
     */
    @Override
    public String hexinAccountStudent(final UserEntity user) {
        ExecutorService fixedThreadPool = Executors.newSingleThreadExecutor();
        fixedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                hexinAccountStudentNoThread(user);
            }
        });
        return "";
    }

    /**
     * 合心批量学生注册--线程调用，内层注册非线程
     * @return
     */
    @Override
    public String hexinAccountStudentList(Classes classes,UserEntity loginUser,List<User> studentList) {
        String result=Not_fusui_result;
        if(loginUser.isFusui()&&studentList!=null&&studentList.size()>0) {
            JSONArray joinUserIdArray =new JSONArray();
            for(User stu:studentList){
                //循环注册学生
                UserEntity stuUser=new UserEntity();
                stuUser.setFusui(loginUser.isFusui());
                stuUser.setSchoolId(loginUser.getSchoolId());//老师不能夸学校，把老师学校id认证信息设给学生
                stuUser.setFusuiSchoolList(loginUser.getFusuiSchoolList());
                stuUser.setSchoolId(loginUser.getSchoolId());
                stuUser.setSchoolIdStr(loginUser.getSchoolIdStr());//获取认证学校

                stuUser.setUserId(stu.getUserId());
                stuUser.setRealName(stu.getRealName());
                stuUser.setMobile(stu.getMobile());
                stuUser.setUserName(stu.getUserName());
                stuUser.setUuid(stu.getUuid());
                stuUser.setStudentCode(stu.getStudentCode());
                hexinAccountStudentNoThread(stuUser);//合心注册学生
                joinUserIdArray.add(stuUser.getUuid());
            }
            //建立班级关联
            classes.setJoinUserIdArray(joinUserIdArray);
            hexinAccountRelationNoThead(loginUser, classes);//合心班级关系请求
        }
        return result;
    }
    /**
     * 合心学生批量或个人注册调用该方法
     * @param user
     * @return
     */

    public String hexinAccountStudentNoThread(  UserEntity user) {
        String result=Not_fusui_result;
        if(user.isFusui()) {
            user.setType(UserEntity.USER_TYPE_STUDENT);
            String createUserParam = getAddStuOrTeaCommitJson(user);
            result=HttpUtil.senPostJson(GlobalConstant.KAOYUE_URL_MAP.get(GlobalConstant.KAOYUE_URL_accoutStudent),createUserParam);
            hexinParseLog(GlobalConstant.KAOYUE_URL_MAP.get(GlobalConstant.KAOYUE_URL_accoutStudent),createUserParam,result,user);
        }
        return result;
    }
    /**
     * 合心添加班级
     * @param user
     * @return
     */
    @Override
    public String hexinAccountGroup(final UserEntity user, final Classes classes) {
        ExecutorService fixedThreadPool = Executors.newSingleThreadExecutor();
        fixedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                String result=Not_fusui_result;
                if(user.isFusui()) {
                    String createClassParam = getCreateclassCommitJson(user, classes);
                    result=HttpUtil.senPostJson(GlobalConstant.KAOYUE_URL_MAP.get(GlobalConstant.KAOYUE_URL_accoutGroup),createClassParam);
                    hexinParseLog(GlobalConstant.KAOYUE_URL_MAP.get(GlobalConstant.KAOYUE_URL_accoutGroup),createClassParam,result,user);
                }
            }
        });
        return "";
    }
    /**
     * 合心添加用户班级关系
     * @param user
     * @return
     */
    @Override
    public String hexinAccountRelation(final UserEntity user, final Classes classes) {
        ExecutorService fixedThreadPool = Executors.newSingleThreadExecutor();
        fixedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                hexinAccountRelationNoThead(user,classes);
            }
        });
        return "";
    }
    /**
     * 学生注册时添加班级--在 一个线程处理
     * @param user
     * @return
     */
    @Override
    public String hexinAccountStudentAndRelation(final UserEntity user, final Classes classes) {
        ExecutorService fixedThreadPool = Executors.newSingleThreadExecutor();
        fixedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                JSONArray joinUserIdArray =new JSONArray();
                joinUserIdArray.add(user.getUuid());
                classes.setJoinUserIdArray(joinUserIdArray);
                hexinAccountStudentNoThread(user);//添加学生
                hexinAccountRelationNoThead(user,classes);//添加班级
            }
        });
        return "";
    }

    /**
     * 同步koolearn用户信息
     * @param yunUser
     */
    @Override
    public void updateKoolearnUserToLocal(UserEntity yunUser) {
        Connection conn = null;
        try {
            conn = ConnUtil.getTransactionConnection();
            loginDao.updateKoolearnUserToLocal( conn ,  yunUser.getId() ,yunUser.getMobile() ,yunUser.getEmail() );
            conn.commit();
        } catch (SQLException e) {
            log.error( "同步koolearn用户信息修改user表异常 "  , e );
            ConnUtil.rollbackConnection(conn);
            e.printStackTrace();
        } catch (BizException e) {
            log.error( "同步koolearn用户信息修改user表异常" + e.getMessage() ,e );
            ConnUtil.rollbackConnection(conn);
        } finally {
            ConnUtil.closeConnection(conn);
        }
    }

    /**
     * 合心添加用户班级关系--非线程
     * @param user
     * @return
     */
    public String hexinAccountRelationNoThead( UserEntity user, Classes classes) {
        String result=Not_fusui_result;
        if(user.isFusui()) {
            if(classes.getJoinUserIdArray()==null ||classes.getJoinUserIdArray().size()<1){
                JSONArray joinUserIdArray =new JSONArray();
                joinUserIdArray.add(user.getUuid());
                classes.setJoinUserIdArray(joinUserIdArray);
            }
            String createRelationParam = getRelationCommitJson(user, classes, classes.getJoinUserIdArray());
            result=HttpUtil.senPostJson(GlobalConstant.KAOYUE_URL_MAP.get(GlobalConstant.KAOYUE_URL_accoutRelation),createRelationParam);
            hexinParseLog(GlobalConstant.KAOYUE_URL_MAP.get(GlobalConstant.KAOYUE_URL_accoutRelation),createRelationParam,result,user);
        }
        return result;
    }
    /**
     * 封装学生或老师注册时提交的参数
     *     扶绥             		云平台
         考号或工号number       学号  student_code , 注：老师或学生个人注册的number 暂时ssoId：63545674
         姓名 name           	姓名  real_name
         登录名uuid              sso   user_name
     * @param user
     * @return
     */
   private  String getAddStuOrTeaCommitJson(UserEntity user){
       Map<String,String> paramMap=SHA1Util.getSignature(user);
       JSONObject userJson=new JSONObject();
       userJson.put("uid",user.getUuid());//sso登录名 user_name(页面登录跳转)
       userJson.put("name",StringUtils.isBlank(user.getRealName())?"【未设置】":user.getRealName());//真实姓名,老师注册时还没设置
       String number=StringUtils.isNotBlank(user.getStudentCode())?user.getStudentCode():"";//user.getUserId()+
       userJson.put("number",number);//注：老师或学生个人注册的number 暂时ssoId：63545674
       userJson.put("phone",user.getMobile());
       List<TeacherBookVersion> tbvList=user.getTeacherBookVersionList();
       if(user.getType().intValue()==UserEntity.USER_TYPE_TEACHER&&tbvList!=null&&tbvList.size()>0){
           //设置老师学科学段 subjects: [学科]（可选， 如“高中数学”）
           JSONArray subjectsArray=new JSONArray();
           for (TeacherBookVersion tbv:tbvList){
               subjectsArray.add(tbv.getRangeName()+tbv.getSubjectName());
           }
            userJson.put("subjects",subjectsArray);
       }
       userJson.put("access_key",paramMap.get("access_key"));
       userJson.put("nonce",paramMap.get("nonce"));
       userJson.put("timestamp",paramMap.get("timestamp"));
       userJson.put("signature",paramMap.get("signature"));
       return userJson.toString();
   }
    /**
     * 封装创建班级提交的参数
     * @param user
     * @return
     */
    private  String getCreateclassCommitJson(UserEntity user,Classes classes){
        Map<String,String> paramMap=SHA1Util.getSignature(user);
        JSONObject userJson=new JSONObject();
        userJson.put("uid",getHexinClassId(classes));
        String classname=classes.getClassName();
        String name=StringUtils.isNotBlank(classname)&&classname.length()>2?classname.substring(0,classname.length()-1):classname;
        userJson.put("name",name);
        userJson.put("grade",classes.getYear());

        userJson.put("access_key",paramMap.get("access_key"));
        userJson.put("nonce",paramMap.get("nonce"));
        userJson.put("timestamp",paramMap.get("timestamp"));
        userJson.put("signature",paramMap.get("signature"));
        return userJson.toString();
    }
    private String getHexinClassId(Classes classes){
        return classes.getClassCode();
    }
    /**
     * 封装学生或老师 加入班级接口参数
     * @param user  当前登录人
     * @return
     */
    private  String getRelationCommitJson(UserEntity user,Classes classes,JSONArray joinUserIdArray){
        Map<String,String> paramMap=SHA1Util.getSignature(user);
        JSONObject relation=new JSONObject();
        relation.put("access_key",paramMap.get("access_key"));
        relation.put("nonce",paramMap.get("nonce"));
        relation.put("timestamp",paramMap.get("timestamp"));
        relation.put("signature",paramMap.get("signature"));
             JSONObject group=new JSONObject();
             group.put("method","add");
             group.put("uid",getHexinClassId(classes));
             group.put("students",joinUserIdArray);
        relation.put("group",group);
        return relation.toString();
    }


    /**
     * 查询用户信息
     * @param userName
     * @param password
     * @return
     */
    @Override
    public Manager findUserByAccout(String userName, String password) {
        Manager manager = null;
        //正则验证userName是邮箱还是电话或者用户名
        try{
            if(CheckUtil.isMobileNO(userName)){
                manager = loginDao.findUserByMobile(userName, password);
            }else if(CheckUtil.isMail(userName)){
                manager = loginDao.findUserByEmail(userName, password);
            }else{
                manager = loginDao.findUserByUserName(userName, password);
            }
        }catch( Exception e ){
            log.error( "查询用户信息异常" +e.getMessage() ,e );
        }
        return manager;
    }

    /**
     * 查询学校是否可用
     */
    public School findSchoolStatus(Integer schoolId){
        return loginDao.findSchoolStatus( schoolId );
    }
    /**
     * 修改密码重置提示状态
     * @param ssoUserId
     */
    public void updateUserPasswordStatus( Integer ssoUserId ) throws Exception {
        Connection conn = null;
        try{
            conn = ConnUtil.getTransactionConnection();
            studentManageDao.updateUserPasswordStatusBySSOId( conn, ssoUserId ,CommonInstence.STATUS_0 );
            conn.commit();
        }catch ( SQLException e ){
            log.error( "修改密码重置提示状态异常，ssoUserId:" + ssoUserId );
            ConnUtil.rollbackConnection(conn);
            throw new  Exception("修改密码重置提示状态异常，ssoUserId:" + ssoUserId + e.getMessage() ,e   );
        }finally {
            ConnUtil.closeConnection(conn);
        }
    }
}
