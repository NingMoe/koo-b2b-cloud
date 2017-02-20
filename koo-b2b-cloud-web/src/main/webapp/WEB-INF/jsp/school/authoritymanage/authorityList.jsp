<%@ taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="com.koolearn.cloud.util.GlobalConstant" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<fe:html title="学校端-用户管理" initSeajs="true" ie="true" defaultHead="<!DOCTYPE html><html>"
         assets="/project/b-ms-cloud/1.x/css/back-end/op-user-manage/page.css">

<fe:seaConfig>
    alias:{
    'project/b-ms-cloud/1.x/js/back-end/sch-user-manage/page': 'project/b-ms-cloud/1.x/js/back-end/sch-user-manage/page.js'
    }
</fe:seaConfig>
<script type="text/javascript">
seajs.use('project/b-ms-cloud/1.x/js/back-end/sch-user-manage/page',function(init){
init({
    cancel: '/school/authority/updateSchoolManageStatus',  //冻结
    reset: '/school/authority/resetManagePassword',  //重置密码
    userSubmit: '/school/authority/addSchoolManager',   //添加新用户提交
    userEdit: '/school/authority/updateManagerInfo',   //修改用户提交
    checkMobile: '/school/authority/isExistManagerMobile',
    checkEmail: '/school/authority/isExistManagerEmail',
    getUserData: '/school/authority/findManagerForUpdate',
    getPageData: '/school/authority/findSchoolManagePage'  //获取数据（表格&分页）



});
});
</script>
</head>
<body>
<!-- 公共的头部 B-->
<jsp:include page="/WEB-INF/jsp/commons/schoolHeader.jsp" >
    <jsp:param name="user" value="user"/>
    <jsp:param name="nav" value="yh"/>
</jsp:include>
<!-- 公共的头部 E-->
<div class="i-main">
<div class="p-new-box">
<a href="#" class="new-btn" id="jp-new-btn">添加新用户</a>
</div>

<div class="p-table-box">
<table cellpadding="0" cellspacing="0" border="0">
<thead>
<tr>
<th>姓名</th>
<th>手机</th>
<th>邮箱</th>
<th>角色</th>
<th>账户状态</th>
<th>创建人</th>
<th>创建时间</th>
<th>操作</th>
</tr>
</thead>
<tbody></tbody>
</table>
</div>

<div class="p-pager-box"></div>

<!-- 新增用户 弹窗 S -->
<div class="p-change-dialog p-change-dialog-sch" id="jp-change-dialog">
<form id="jp-change-form">
<div class="p-test-form p-test-form-classics p-test-form-block-error jg-validate-block-error">
    <div class="item">
        <label class="label" for="managerName">姓名:</label>
        <div class="field">
            <input class="text" type="text" name="managerName" id="managerName"/>
        </div>
    </div>
    <div class="item">
        <label class="label" for="managerMobile">手机:</label>
        <div class="field">
            <input class="text" type="text" name="managerMobile" id="managerMobile"/>
        </div>
    </div>
    <div class="item">
        <label class="label" for="managerEmail">邮箱:</label>
        <div class="field">
            <input class="text" type="text" name="managerEmail" id="managerEmail"/>
        </div>
    </div>
<div class="item">
<label class="label">角色:</label>
<div class="field field-rc">
    <label for="role1" class="label-rc" role>
        <input class="checkbox" name="roleTypeId" type="radio" id="role1" value="20" />
        <span>校领导</span>
    </label>
    <label for="role2" class="label-rc" role>
        <input class="checkbox" name="roleTypeId" type="radio" id="role2" value="30"/>
        <span>年级主任</span>
    </label>
    <label for="role3" class="label-rc" role>
        <input class="checkbox" name="roleTypeId" type="radio" id="role3" value="40"/>
        <span>学科教研组长</span>
    </label>
    <label for="role4" class="label-rc" role>
        <input class="checkbox" name="roleTypeId" type="radio" id="role4" value="50"/>
        <span>年级学科教研组长</span>
    </label>
<div class="select-box range-box">
    <span class="hd">年级：</span>
    <c:forEach items="${ levelMap }" var="levelMap" varStatus="vs">
        <label for="range1${vs.index+1}" class="label-rc">
            <input class="checkbox" name="classesLevelStr" type="checkbox" id="range${vs.index+1}" value="${ levelMap.key}" />
            <span>${ levelMap.value}</span>
        </label>
    </c:forEach>
</div>
<div class="select-box subject-box">
        <span class="hd">学科：</span>
        <c:forEach items="${ subjectMap }" var="subjectMap" varStatus="vs">
            <label for="subject${vs.index+1}" class="label-rc">
                <input class="checkbox" name="subjectIdStr" type="checkbox" id="subject${vs.index+1}" value="${subjectMap.key}" />
                <span>${subjectMap.value}</span>
            </label>
        </c:forEach>
</div>
</div>
</div>
<div class="btn-box">
<input type="submit"  class="save-btn" value="保 存">
</div>
</div>
</form>
</div>
<!-- 新增用户 弹窗 E -->

</div>
</body>
</fe:html>