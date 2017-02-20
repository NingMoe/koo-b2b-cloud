<%@ taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<fe:html title="学校" initSeajs="true" ie="true"
         assets="/project/b-ms-cloud/1.x/css/back-end/sch-tea-stu/page-school.css">
<body>
<!-- 公共的头部 B-->
<jsp:include page="/WEB-INF/jsp/commons/schoolHeader.jsp" >
    <jsp:param name="user" value="user"/>
    <jsp:param name="nav" value="xx"/>
</jsp:include>
<!-- 公共的头部 E-->
<div class="i-box">
    <div class="p-con-title absolute">云平台权限</div>
    <div class="p-con-box">
        <ul>
            <li><label>最多同时在线人数：</label><span>${schoolPowerDto.maxOnLine}</span></li>
            <li><label>开通日期：</label><span>${schoolPowerDto.beginTime}</span></li>
            <li><label>包含学段：</label><span>${schoolPowerDto.rangeInfo}</span></li>
            <li><label>到期日期：</label><span>${schoolPowerDto.endTime}</span></li>
        </ul>
    </div>
</div>
<div class="i-box">
    <div class="p-con-title ">学校信息</div>
    <div class="p-con-box">
        <a href="#"  id="jp-edit" class="btn fr "> 修改</a>
        <form class="form">
            <input type="hidden" id="schoolId" name="schoolId" value="${schoolPowerDto.schoolId}"/>
            <input type="hidden" id="hadGrade" name="hadGrade" value="${schoolPowerDto.hadGrade}"/>
            <input type="hidden" id="hadUpClasses" name="hadUpClasses" value="${schoolPowerDto.hadUpClasses}"/>
            <div>
                <label  class="len130">学校名称：</label>
                <span>${schoolPowerDto.schoolName}</span>
            </div>
            <div>
                <label  class="len130">管理员：</label>
                <c:forEach items="${schoolPowerDto.managerNameList}" var="managerName"  varStatus="vs">
                    <span>${managerName}</span>
                </c:forEach>
            </div>
            <div>
                <label  class="len130">联系人姓名：</label>
                <div class="field editable off">
                    <input type="text" readonly value="${schoolPowerDto.contacter}" name="contacter"/>
                </div>
            </div>
            <div>
                <label  class="len130">联系人邮箱：</label>
                <div class="field editable off">
                    <input type="text" readonly value="${schoolPowerDto.contacterMail}" name="contacterMail" />
                </div>
            </div>
            <div>
                <label  class="len130">联系人电话：</label>
                <div class="field editable off">
                    <input type="text" readonly value="${schoolPowerDto.contacterMobile}" name="contacterMobile"/>
                </div>
            </div>
            <div>
                <label  class="len130">年级自动升级日期：</label>
                <div class="field editable off tim">
                    <span>${schoolPowerDto.autoUpTime}</span>
                    <input type="text" readonly class="jp-set-tim-input " name="autoUpTime" />
                </div>
            </div>
            <div>
                <label  class="len130">自动毕业年级：</label>
                <div class="field editable off checkbox">
                    <span>${schoolPowerDto.classGraduateLevel}</span>
                    <div class="field off" style="width:400px">
                        <c:forEach items="${schoolPowerDto.rangedLevelMap}" var="rangeMap"  varStatus="ranges" >
                            <span class="group">
                                <label class="group-name"> ${rangeMap.key}：</label>
                                <c:forEach items="${rangeMap.value}" var="levelMap" >
                                    <span>
                                        <c:if test="${ levelMap.value.isCheck == 1 }">
                                            <input type="radio" name="classGraduateLevel-${ranges.index}" class="simulate-radio" value="${levelMap.key}" checked/>
                                            <label class="label">${levelMap.value.levelName }</label>
                                        </c:if>
                                        <c:if test="${ levelMap.value.isCheck != 1 }">
                                            <input type="radio" name="classGraduateLevel-${ranges.index}" class="simulate-radio" value="${levelMap.key}" />
                                            <label class="label">${levelMap.value.levelName }</label>
                                        </c:if>
                                    </span>
                                </c:forEach>
                            </span>
                       </c:forEach>
                    </div>
                </div>
            </div>
            <div>
                <label  class="len130">年级自动毕业日期：</label>
                <div class="field editable off tim">
                    <span>${schoolPowerDto.autoGraduateTime}</span>
                    <input type="text" readonly class="jp-set-tim-input" name="autoGraduateTime"/>
                </div>
            </div>
        </form>
    </div><!--conbox end-->
</div>
<fe:seaConfig>
    alias:{
    'project/b-ms-cloud/1.x/js/back-end/sch-tea-stu/page-school': 'project/b-ms-cloud/1.x/js/back-end/sch-tea-stu/page-school.js'
    }
</fe:seaConfig>
<script type="text/javascript">

    seajs.use('project/b-ms-cloud/1.x/js/back-end/sch-tea-stu/page-school',function(init){
        init({
            actionUrl : "/school/manage/updateSchoolExtendInfo"
        });
    });
</script>
</body>
</fe:html>
