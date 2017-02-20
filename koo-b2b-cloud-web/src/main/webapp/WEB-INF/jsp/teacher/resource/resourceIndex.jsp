<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<fe:html title="资源库" initSeajs="true" ie="true"
         assets="/project/b-ms-cloud/1.x/css/t-zyk/page.css">
<fe:seaConfig>
    alias: {
    'project/b-ms-cloud/1.x/js/t-common-allevel/page': 'project/b-ms-cloud/1.x/js/t-common-allevel/page.js',
    'project/b-ms-cloud/1.x/js/t-zyk/page': 'project/b-ms-cloud/1.x/js/t-zyk/page.js'
    }
</fe:seaConfig>
    <script type="text/javascript">
        seajs.use('project/b-ms-cloud/1.x/js/t-common-allevel/page', function (init) {
            init({
                treeUrl: '/teacher/resource/getTree',//树json
                treeId: "#treeDemo",//树Id
                getreeId: "treeDemo",//树Id
                sureUrl: '/teacher/resource/search',
                //联级
                selProductLine: '/teacher/resource/getSubject',
                selExamSeasonId: '/teacher/resource/getRange',
                selSubjectId: '/teacher/resource/getBookVersion',
                selLastId: '/teacher/resource/getStr',
                obligatoryId: '/teacher/resource/getObligatory',
                subjectId:'${subjectId}',
                rangeId:'${rangeId}',
                bookVersion:'${bookVersion}'
            });
        });
        seajs.use('project/b-ms-cloud/1.x/js/t-zyk/page', function (init) {
            init({});
        });

    </script>
    </head>
    <body>
    <!-- 公共的头部 B-->
    <jsp:include page="/WEB-INF/jsp/commons/header.jsp">
        <jsp:param name="user" value="user"/>
        <jsp:param name="nav" value="zyk"/>
    </jsp:include>
    <!-- 公共的头部 E-->
    <div class="i-main fc jp-main">
        <div class="p-side fl jp-side">
            <p class="p-spns fc">
                <span class="cur"><a href="/teacher/resource/index?urlType=${urlType}" >章节信息</a></span>
                <%--<span><a href="/teacher/resource/knowledgeIndex?urlType=${urlType}" >知识点</a></span>--%>
            </p>

            <form id="jp-form-chos" enctype="multipart/form-data">
                <input type="hidden" name="pageNo" id="pageNo" value="0"/>
                <div class="p-tabpre jp-divs">
                    <div class="tabs cur">
                        <dl class="p-chos p-compu">
                            <dt class="p-chos-dt jp-chos">
                                <span>高中物理、新课标新课标新课标</span>
                                <i></i>
                            </dt>
                            <dd class="p-chos-dd" id="jp-temp">
                                <div class="p-pd12">
                                    <div class="p-psnew">
                                        <p>学科</p>
                                        <p>学段</p>
                                        <p>教材版本</p>
                                    </div>
                                    <div class="p-selnew">
                                        <select class="i-sel1 sel1" id="sel-show" name="subjectId">
                                                <%--<option value="1">小学</option>--%>
                                        </select>
                                        <select class="i-sel1 sel2" id="sel-show1" name="rangeId">
                                                <%--<option value="1">语文</option>--%>
                                        </select>
                                        <select class="i-sel1 sel3" id="sel-show2" name="bookVersion">
                                                <%--<option value="1">人教版</option>--%>
                                        </select>
                                    </div>
                                </div>
                                <a class="p-sure jp-sure" href="javascript:;">确定</a>
                            </dd>
                        </dl>
                        <div class="p-chos p-compu">
                            <select id="obligatoryId" class="i-sel4" name="obligatory">
                                <!-- <option value="1">必修一</option>
                                <option value="2">必修二</option>
                                <option value="3">必修三</option> -->
                            </select>
                        </div>
                        <!-- 章节 -->
                        <div class="p-chapter">
                            <!-- 树 -->
                            <ul id="treeDemo" class="ztree p-pd15"></ul>
                        </div>
                    </div>
                </div>
                <input id="jp-lyId" type="hidden" value="-1" name="source"/>
                <input id="jp-lxId" type="hidden" value="${resourceType}" name="type"/>
                <input id="jp-gsId" type="hidden" value="-1" name="format"/>
                <input id="jp-scId" type="hidden" value="" name="selectType"/>
                <input id="jp-keyId" type="hidden" value="" name="keyTxt"/>
                <input id="jp-tagId" type="hidden" value="" name="tagId"/>
                <input id="jp-section" type="hidden" value="0" name="klbType">
                <input id="urlType" type="hidden" value="${urlType}" name="urlType">

            </form>
        </div>
        <div class="p-cont fr">
            <c:if test="${urlType == 0}">
                <p class="p-updata">
                    <a class="orange-btn fr" href="/teacher/resource/addResourceIndex">上传资源</a>
                </p>
            </c:if>
            <div class="p-search">
                <p class="p-search-p fc">
                    <input id="jp-keytxt" class="fl" type="text" placeholder="输入关键词">
                    <a id="jp-keybtn" class="fr" href="javascript:;"></a>
                </p>
                <dl class="p-search-dl jp-zyk-search">
                    <dt>来源</dt>
                    <dd>
                        <a class="white-btn-tab on " href="javascript:;" data-ly="-1">全部</a>
                        <a class="white-btn-tab" href="javascript:;" data-ly="2">校本库</a>
                        <a class="white-btn-tab" href="javascript:;" data-ly="3">我的资源库</a>
                        <a class="white-btn-tab" href="javascript:;" data-ly="4">公共资源库</a>
                    </dd>
                    <dt>类型</dt>
                    <dd id="jp-type">
                        <a class="white-btn-tab  <c:if test="${resourceType==-1}">on</c:if>" href="javascript:;" data-lx="-1">全部</a>
                        <c:forEach items="${typeList}" var="t">
                            <c:choose>
                                <c:when test="${resourceType==t.value}">
                                    <a class="white-btn-tab on" href="javascript:;" data-lx="${t.value}">${t.name}</a>
                                </c:when>
                                <c:otherwise> <a class="white-btn-tab" href="javascript:;" data-lx="${t.value}">${t.name}</a></c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </dd>
                    <dt>格式</dt>
                    <dd>
                        <a class="white-btn-tab on" href="javascript:;" data-gs="-1">全部</a>
                        <c:forEach items="${formatList}" var="f">
                            <a class="white-btn-tab" href="javascript:;" data-gs="${f.value}">${f.name}</a>
                        </c:forEach>
                    </dd>
                </dl>
                <p class="p-screen radio_area jp-zyk-label">
                    <input type="radio" id="d" class="for-radio" data-sc="-1"><b>全部</b>
                    <input type="radio" id="e" class="for-radio" data-sc="1"><b>过滤使用过的</b>
                    <input type="radio" id="f" class="for-radio" data-sc="2"><b>只选收藏的</b>
                    <input type="radio" id="g" class="for-radio" data-sc="3"><b>只选使用过的</b>
                </p>
            </div>
            <div id="jp-quest">

            </div>
        </div>
    </div>
    <jsp:include page="/footer.jsp"/>
    </body>
    <script>
        function serachFenye(pageNo) {

            if (!pageNo) {
                pageNo = 0;
            }
            $("#pageNo").val(pageNo)
            $.ajax({
                url: "/teacher/resource/search",
                type: "post",
                data: $("#jp-form-chos").getFormData(),
                success: function (data) {
                    $("#jp-quest").html(data);
                    $('body,html').animate({ scrollTop: 0 }, 200);
                }
            });
        }
    </script>
</fe:html>