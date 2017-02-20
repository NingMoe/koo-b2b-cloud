<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="koo" uri="http://www.koolearn.com/taglib/koo" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<fe:html title="翻转课堂" initSeajs="true" ie="true"
         assets="/project/b-ms-cloud/1.x/css/t-fz/page.css,
         /project/b-ms-cloud/1.x/js/common/formJs/jquery-form.js">
<fe:seaConfig>
    alias: {
    'project/b-ms-cloud/1.x/js/t-fz/page': 'project/b-ms-cloud/1.x/js/t-fz/page.js'
    }
</fe:seaConfig>
    <script type="text/javascript">
        seajs.use(['project/b-ms-cloud/1.x/js/t-fz/page'], function (app) {
            app.init({
                api: {
                    subject:'/teacher/classRoom/getSubject',
                    removeList:'/teacher/classRoom/delete',
                    recall:'/teacher/classRoom/recall',
                    copys:'/teacher/classRoom/copyIndex?id={id}'
                }
            });
        });
    </script>
    <body>
    <!-- 公共的头部 B-->
    <jsp:include page="/WEB-INF/jsp/commons/header.jsp">
        <jsp:param name="user" value="user"/>
        <jsp:param name="nav" value="fzkt"/>
    </jsp:include>
    <!-- 公共的头部 E-->
    <div class="i-main fc" id="">
        <div class="p-hjg">
            <form id="formId" enctype="multipart/form-data" onsubmit="return false;">
                <input type="hidden" name="pageNo" id="pageNo" value="0"/>
                <select id="sel-show" name="subjectId">
                    <option value="-1">全部</option>
                </select>
                <div class="p-search-p fc">
                    <input class="fl" type="text" id="" name="searchValue" placeholder="请输入课堂关键词"/>
                    <a class="jp-search-btn fr" href="javascript:;" onclick="serachFenye(0)"></a>
                </div>
            </form>
        </div>
        <div id="jp-quest"></div>
    </div>
    <jsp:include page="/footer.jsp"/>
    </body>
</fe:html>
<script>

    serachFenye=function(pageNo){
        if(!pageNo){
            pageNo=0;
        }
        $("#pageNo").val(pageNo);
        window.isGetQuestions=true;
        $('#formId').submit();
    }
    $('#formId').submit(function(){
        if(window.isGetQuestions){
            var options = {
                url : '/teacher/classRoom/search' ,
                contentType : "multipart/form-data; charset=utf-8",
                type : 'POST',
                success : function(data) {
                    $("#jp-quest").html(data);
                    $('body,html').animate({ scrollTop: 0 }, 200);
                } ,
                error:function(data){ }

            };
            // 提交表单
            $(this).ajaxSubmit(options);
            // 为了防止普通浏览器进行表单提交和产生页面导航（防止页面刷新？）返回false
            return false;
        }
        return true;
    });


</script>