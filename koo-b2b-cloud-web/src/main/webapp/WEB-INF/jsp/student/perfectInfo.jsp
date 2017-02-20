<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<%@ page import="com.koolearn.framework.common.utils.PropertiesConfigUtils"%>
<!DOCTYPE html>
<fe:html title="首页 —— 完善资料" ie="true" initSeajs="true" 
assets="/project/b-ms-cloud/1.x/css/t-home-wszl/page.css">
<body>
<!-- 公共的头部 B-->
<div class="p-header">
	<div class="i-box">
		<a class="p-lg fl" href="javascript:;">
            <img src="<%=PropertiesConfigUtils.getProperty("domains.ui") %>/project/b-ms-cloud/1.x/i/i-logo.png">
        </a>
		<b class="p-b fl">中小学教育云平台</b>
	</div>
</div>
<!-- 公共的头部 E-->
<div class="i-main p-courvare">
    <div class="p-contvare">
        <h3>加入班级</h3>
        <div class="login-box">
            <form>
                <div class="p-banji-code p-login-input">
                    <input type="text" placeholder="输入班级码" name="classesCode" value="" id="classesCode">
                    <span class="input-info">必须加入班级才可以使用本系统，请向老师咨询所在班级的班级码，并输入加入班级。</span>
                    <span class="input-error" style="display: none;">您输入的班级码不正确，请重新输入。</span>
                </div>
                <div class="p-student-name p-login-input">
                    <input type="text" placeholder="输入姓名" maxlength="30" name="realName" id="realName" value=""/>
                    <span class="input-info">为了确保您的老师和同学能找到你，请输您的入真实姓名。</span>
                    <span class="input-error" style="display: none;">请输入正确姓名</span>
                </div>
                </form>
                <div class="submit-box">
                    <!-- <a href="javascript:;" class="green-btn">确定</a> -->
                    <input type="button" id="tijiao" class="green-btn" value="确定">
                </div>
        </div>
    </div>
</div>
</body>
    <fe:seaConfig>
        alias:{
        'project/b-ms-cloud/1.x/js/t-home-wszl/page': 'project/b-ms-cloud/1.x/js/t-home-wszl/page.js'
        }
    </fe:seaConfig>
<script type="text/javascript">
        seajs.use('project/b-ms-cloud/1.x/js/t-home-wszl/page',function(init){
            init({
                classesCodeUrl: '/student/classes/exist',//-- "/student/classes/exist"
                sureBtnUrl:'/student/classes/join', //-- '/student/classes/join'
                locationHref:'/student/allSubject/findAllSubjectExam',//-- 跳转页面地址
                addClassUrl:'/student/classHome/checkStudentClasses'
            });
        });
    </script>
</fe:html>
