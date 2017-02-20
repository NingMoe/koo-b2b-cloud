<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="koo" uri="http://www.koolearn.com/taglib/koo" %>
<div class="p-quest">
    <c:if test="${empty sourceList}"><div class="i-no-result">没有查询到相关数据</div></c:if>
    <c:forEach items="${sourceList}" var="source">
        <div class="p-quest-temp">
            <c:if test="${source.marrow != null && source.marrow == 2}" >
                <span class="goods-ico p-fine"></span><!-- 精选图标 -->
            </c:if>
            <dl class="p-learn fc">
                <dt class="fl">
                    <c:choose>
                        <c:when test="${not empty source.frontcoverUrl}">
                            <img src="/data${source.frontcoverUrl}">
                        </c:when>
                        <c:when test="${empty source.frontcoverUrl && (source.format==1||source.format==8)}">
                            <img src="/data/cloud/image/defaultFrontcover_WORD_PDF.jpg">
                        </c:when>
                        <c:when test="${empty source.frontcoverUrl && (source.format==2)}">
                            <img src="/data/cloud/image/defaultFrontcover_PPT.jpg">
                        </c:when>
                        <c:when test="${empty source.frontcoverUrl && (source.format==4)}">
                            <img src="/data/cloud/image/defaultFrontcover_MP4.jpg">
                        </c:when>
                        <c:when test="${empty source.frontcoverUrl && (source.format==5)}">
                            <img src="/data/cloud/image/defaultFrontcover_MP3.jpg">
                        </c:when>
                        <c:otherwise>
                            <img src="/data/cloud/image/defaultFrontcover.png">
                        </c:otherwise>
                    </c:choose>
                        <%--<c:when test="${empty source.frontcoverUrl &&(source.format==1||source.format==2||source.format==8)}">--%>
                            <%--<img src="/data${source.fileConverPath}/index_1.png">--%>
                        <%--</c:when>--%>
                </dt>
                <dd class="fl">
                    <p>
                        <a href="/teacher/resource/preview/reader/${source.id}" target="_blank">${source.name}</a>
                    </p>
                    <p>类 型：${source.typeName}</p>
                    <p>来 源：<c:if test="${source.source != 1}">${source.userStr}</c:if>
                        <c:if test="${source.source == 1}">新东方教育云</c:if></p>
                    <p>进度点：${source.bookVersionNames}</p>
                </dd>
            </dl>
            <dl class="p-parsi">
                <dt>
                <p class="p-parsi-btn">
                    <a class="collect-btn jp-collect <c:if test="${source.collection == true}">cur</c:if> " href="javascript:;" onclick="collection(${source.id})"></a>
                    <c:if test="${urlType != 0}">
                        <a class="white-btn w68" href="javascript:;" id="selectResource"  onclick="use(${source.id},'${source.name}','${source.typeName}')" >选择</a>
                    </c:if>
                </p>
                <p class="p-parsi-kldge">
                    <span>使用：${source.useTimes} </span>
                    <%--<span><c:if test="${source.knowledgeNames != null}">知识点：${source.knowledgeNames}</c:if></span>--%>
                    <span><c:if test="${source.knowledgeSingleName != null}">知识点：${source.knowledgeSingleName}</c:if></span>
                </p>
                </dt>
            </dl>
        </div>
    </c:forEach>
</div>
<koo:pager name="listPager" iteration="true" link="javaScript:serachFenye({p});" ></koo:pager>
<!-- 分页 注:此分页只是一个暂时的样式-->
<script type="text/javascript">
    function collection(sourceId){
        setTimeout(function(){
            $.ajax({
                url:"/teacher/resource/collection/"+sourceId,    //请求的url地址
                dataType:"json",   //返回格式为json
                async:true,//请求是否异步，默认为异步，这也是ajax重要特性
                //  data:{"id":sourceId},    //参数值
                type:"post",   //请求方式
                success:function(data){

                },
                error:function(){
                    window._hintPop.hintFun("收藏失败!")
                }
            })
        }, 1000);

    }

    function use(sourceId,sourceName,sourceType){
        $('.white-btn').removeAttr("onclick");
        $.ajax({
            url:"/teacher/resource/use/"+sourceId,    //请求的url地址
            dataType:"json",   //返回格式为json
            async:true,//请求是否异步，默认为异步，这也是ajax重要特性
            //  data:{"id":sourceId},    //参数值
            type:"post",   //请求方式
            success:function(data){
                if(data){
                    window.opener.zyCommit(sourceId,sourceName,sourceType);
                    window.close();
                }
            },
            error:function(){
                 window._hintPop.hintFun("选择失败!")
            }
        });
    }


</script>
