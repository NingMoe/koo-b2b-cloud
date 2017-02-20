<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<!DOCTYPE html>
<fe:html title="作业-布置作业" ie="true" initSeajs="true"
         assets="/project/b-ms-cloud/1.x/css/t-zy-bzzy/page.css">
    <body>
    <c:if test="${urlType == null}">
        <!-- 公共的头部 B-->
        <jsp:include page="/WEB-INF/jsp/commons/header.jsp">
            <jsp:param name="user" value="user"/>
            <jsp:param name="nav" value="zy"/>
        </jsp:include>
        <!-- 公共的头部 E-->
    </c:if>
    <form id="save">
        <input type="hidden" id="status" name="status" value="1"/>
        <fieldset id="zyArea">
        	<input type="hidden" value="${paper.id}" name="paperId" id="jp-paperid">
            <input type="hidden" value="${paper.paperName}" name="examName" id="jp-examname">
        </fieldset>
        <fieldset id="bjArea">
        </fieldset>
        <div class="i-main fc">
            <div class="p-box i-mgb25">
                <div class="green-tit">
                    选择作业
                  <!--   <a href="/teacher/exam/core/question/jdd?createFrom=2" target="_blank" class="white-btn pdlr4">组题新作业</a> -->
                </div>
                <div class="p-box-ct">
                    <h2 id="jp-bzzy-h2" class="p-bzzy-h2">${paper.paperName}</h2>
                    <p class="p-bzzy-p">
                        <a id="jp-chosepop" href="javascript:;"></a>
                        <span>从我的试卷选择试卷</span>
                    </p>
                </div>
            </div>
            <c:if test="${urlType == 1}">
                <div class="p-box i-mgb4">
                    <div class="green-tit">
                        选择班级
                    </div>
                    <div class="p-box-ct">
                        <c:if test="${empty(classesList)}">
                            没有可选班级，请返回备课列表选择班级
                        </c:if>
                        <c:if test="${!empty(classesList)}">
                        <div class="jp-checkbox-all p-all-checkbox">
                            <input type="checkbox" class="for-checkbox" id="" style="position: absolute; left: -400em;">

                            <label class="input-checkbox-label on" checked-attr="checked"></label>选择全部
                        </div>

                                <ul class="lst p-classes-lst">
                                    <c:forEach items="${classesList}" var="c">
                                    <li>
                                        <dl class="p-class-dl">
                                            <dt class="jp-class-dt"><input type="checkbox"  value="${c.id}" class="for-checkbox" id="${c.id}" checked-attr="" style="position: absolute; left: -400em;">${c.fullName}</dt>
                                            <dd class="jp-class-dd">
                                                <c:forEach items="${c.groupList}" var="cc">
                                                <span class="p-jk"><input type="checkbox" value="${cc.id}"  class="for-checkbox" id="${cc.id}" style="position: absolute; left: -400em;">${cc.fullName}</span>
                                                </c:forEach>
                                            </dd>

                                        </dl>
                                    </li>
                                    </c:forEach>

                                    <%--<li>--%>
                                        <%--<dl class="p-class-dl">--%>
                                            <%--<dt class="jp-class-dt"><input type="checkbox" value="${c.id}" class="for-checkbox" id="${c.id}" style="position: absolute; left: -400em;"><label--%>
                                                    <%--class="input-checkbox-label on"></label>${c.fullName}--%>
                                            <%--</dt>--%>
                                        <%--</dl>--%>
                                    <%--</li>--%>
                                </ul>
                        </c:if>
                    </div>
                </div>
            </c:if>
            <c:if test="${urlType == null}">
                <div class="p-box i-mgb4">
                    <div class="green-tit">
                        选择班级
                    </div>
                    <div class="jp-classes-lst p-box-ct">
                    </div>
                </div>
            </c:if>
            <div class="p-box">
                <c:if test="${urlType == 1}">
                    <div class="btn-area">
                        <a href="javascript:;" id="classRoomSave" class="green-btn">保存</a>
                    </div>
                </c:if>
                <c:if test="${urlType == null}">
                    <div class="p-box-ct">
                        <em class="p-set-tim-tit">设置时间</em>
                        <input type="text" name="startTime" class="jp-set-tim jp-set-tim-star p-set-tim-input" readonly
                               placeholder="设置时间">
                        <span class="p-set-tim-for">至</span>
                        <input type="text" name="endTime" class="jp-set-tim jp-set-tim-end p-set-tim-input" readonly
                               placeholder="截止时间">
                    </div>
                    <div class="btn-area">
                        <a href="javascript:;" class="jp-save-btn green-btn">保存</a>
                        <a href="javascript:;" class="jp-finish-btn orange-btn">布置作业</a>
                    </div>
                </c:if>
            </div>
        </div>
    </form>
    
    <form name="searchPaperForm" id="searchPaperForm">
        <input type="hidden" name="pageNo" id="pageNo" value="0"/>
    <!-- 弹出页面 -->
    <div id="jp-pop-exam" class="p-pop-exam">
        <div class="p-popadd-page">
            <div class="p-addpop-box p-box">
                <button id="jp-close" class="p-close" href="javascript:;">×</button>
                <div id="paperData">

                </div>
            </div>
        </div>
        <div class="p-bg" id="jp-bg"></div>
    </div>
	</form>
    <c:if test="${urlType == null}">
        <jsp:include page="/footer.jsp"></jsp:include>
    </c:if>
    </body>
    <fe:seaConfig>
        alias: {
        'project/b-ms-cloud/1.x/js/t-zy-bzzy/page': 'project/b-ms-cloud/1.x/js/t-zy-bzzy/page.js',
        'project/cms-mix/1.x/common/datetimepicker/datetimepicker': 'project/cms-mix/1.x/common/datetimepicker/datetimepicker.js'
        }
    </fe:seaConfig>
    <script type="text/javascript">
        seajs.use(['project/b-ms-cloud/1.x/js/t-zy-bzzy/page'], function (app) {
            app.init({
                api: {
                    chooseWork: '/project/b-ms-cloud/1.x/json/t-zy-bzzy/chooseWork.php',
                    chooseClass: '/teacher/task/class?classesId=${classesId}',
                    finishLink: '/teacher/task/save',
                    saveLink: '/teacher/task/save',
                    saveLocation: '/teacher/task/list',
                    changeLocationk: '/teacher/task/assign',
                    okLocation: '/teacher/task/list'
                }
            });
        });
    </script>
    <script type="text/javascript">

        $("#classRoomSave").on("click",function(){
            if($("input[name='classesId']").val()==undefined){
                 window._hintPop.hintFun("请选择班级");
                return;
            }

            $.ajax({
                url: "/teacher/task/classRoom/save",
                type: "post",
                dataType:"json",
                data: $("#save").serialize(),
                success: function (data) {
                    if(data.success==true){
                        window.opener.tikuCommit(data.examId,data.examName,data.fullName);
                        window.close();
                    }else{
                         window._hintPop.hintFun("操作失败,请重新尝试")
                    }
                }
            });
        })

        function searchFenye(pageNo) {
            if (!pageNo) {
                pageNo = 0;
            }
            $("#pageNo").val(pageNo);
            $('#searchPaperForm').submit();
        }

        $('#searchPaperForm').submit(function () {
            $.ajax({
                url: '/teacher/task/paper',
                type: 'POST',
                async: false,
                data: $('#searchPaperForm').serialize(),
                success: function (data) {
                    $("#paperData").html(data);
                    $(".p-box .jp-work-list .for-checkbox").radios();
                    $('body,html').animate({ scrollTop: 0 }, 200);
                },
                error: function (data) {
                }
            });
            // 为了防止普通浏览器进行表单提交和产生页面导航（防止页面刷新？）返回false
            return false;
        });
    </script>
</fe:html>

