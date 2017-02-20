<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<fe:html title="小学说课-播放页" initSeajs="true" ie="true"
         assets="/project/b-ms-cloud/1.x/css/s-saidclass-play/page.css">
<fe:seaConfig>
    alias:{
    'project/b-ms-cloud/1.x/js/s-saidclass-play/page': 'project/b-ms-cloud/1.x/js/s-saidclass-play/page.js'
    }
</fe:seaConfig>
    <script type="text/javascript">

        var productId = "/teacher/listenCourse/player/${productId}/";
        var oneCategoryId ="/${oneCategoryId}/${subCategoryId}/${threeCategoryId}/${pageNo}";

        seajs.use('project/b-ms-cloud/1.x/js/s-saidclass-play/page',function(init){
            init({
                treeUrl:'/teacher/listenCourse/tree/${productId}',//右侧数据
                treeId:'#treeDemo',//树id
                selectId:'${courseUnit.id}'
            });
        });
    </script>
    <body>
    <!-- 公共的头部 B-->
    <jsp:include page="/WEB-INF/jsp/commons/header.jsp">
        <jsp:param name="user" value="user"/>
        <jsp:param name="nav" value="xxsk"/>
    </jsp:include>

    <!-- 公共的头部 E-->
    <div class="i-main p-saidclass-play">
        <p class="p-title">
            <a href="/teacher/listenCourse/detail/${productId}/${oneCategoryId}/${subCategoryId}/${threeCategoryId}/${pageNo}" title="返回个人中心"></a>
        </p>
        <div class="s-said-cont fc">
            <div class="p-side fl">
                <div class="p-play">
                    ${errorMessage}
                </div>
                <p class="p-txt">讲师：${teachers} ／ 课时：${amout} </p>
            </div>
            <div class="p-cont fr" id="jqcorbox">
                <ul id="treeDemo" class="ztree ztreePlay"></ul>
            </div>
        </div>
    </div>
    <jsp:include page="/footer.jsp"/>
    </body>
</fe:html>
