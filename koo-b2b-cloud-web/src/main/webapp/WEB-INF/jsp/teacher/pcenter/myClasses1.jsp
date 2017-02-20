<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<!DOCTYPE html>
<fe:html title="个人中心-我的班级" ie="true" initSeajs="true" 
assets="/project/b-ms-cloud/1.x/css/t-personal-grade/page.css">
<body>
<!-- 公共的头部 B-->
<jsp:include page="/WEB-INF/jsp/commons/header.jsp" >
    <jsp:param name="user" value="user"/>
    <jsp:param name="nav" value="sy"/>
</jsp:include>
<!-- 公共的头部 E-->
<div class="i-main">
    <h2 class="p-user-header">我的班级<a href="/teacher/info/index" title="返回个人中心"></a></h2>
    <div class="p-zy-box">
        <table>
            <thead>
            <tr>
                <td width="5%">序号</td>
                <td width="12%">班级名称</td>
                <td width="12%">入学年份</td>
                <td width="11%">学段</td>
                <td width="12%">班级类型</td>
                <td width="12%">学生人数</td>
                <td width="12%">所任学科</td>
                <td width="12%">班级编码</td>
                <td width="12%">班级状态</td>
            </tr>
            </thead>
            <tbody>
            <!-- 默认0：type=0 行政班   type=1 学科班、type=3班级小组 -->
            <!--graduate 毕业标识：默认0：0未毕业   1 毕业 -->
            <c:forEach items="${clist}" var="c" varStatus="stat">
            <tr>
                <td>${stat.index+1}</td>
                <td><a href="/teacher/addStudent/showStudentInfo?classesId=${c.id}">${c.fullName}</a></td>
                <td>${c.year}</td>
                <td rangeId="${c.rangeId}">${c.rangeName}</td>
                <td>
                	<c:choose>
                		<c:when test="${c.type==0}">行政班</c:when>
                		<c:when test="${c.type==1}">学科班</c:when>
                		<c:otherwise></c:otherwise>
                	</c:choose>
                </td>
                <td>${c.studentNum}</td>
                <td>
                <c:forEach items="${c.teacherBookVersionList}" var="book" varStatus="stat">
                	${book.subjectName}<c:if test="${!stat.last}">、</c:if>
                </c:forEach>
                </td>
                <td>${c.classCode}</td>
                <td class="p-type">
					<c:choose>
                		<c:when test="${c.graduate==0}">未毕业</c:when>
                		<c:when test="${c.graduate==1}">已毕业</c:when>
                		<c:otherwise></c:otherwise>
                	</c:choose>
				</td>
            </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>
</body>
    <fe:seaConfig>
        alias:{
        'project/b-ms-cloud/1.x/js/t-personal-grade/page': 'project/b-ms-cloud/1.x/js/t-personal-grade/page.js'
        }
    </fe:seaConfig>
<script type="text/javascript">
    seajs.use(['project/b-ms-cloud/1.x/js/t-personal-grade/page'],function(app){
        app.init();
    });
</script>
</fe:html>
