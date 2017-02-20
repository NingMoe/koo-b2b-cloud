<%@ page import="com.koolearn.cloud.util.GlobalConstant" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix='fmt' %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="kl" uri="kl_security" %>

<fe:html title="资源管理-修改资源" initSeajs="true" ie="true" defaultHead="<!DOCTYPE html><html>"
         assets="/project/b-ms-cloud/1.x/css/back-end/op-zyk-upload/page.css
         ,/project/b-ms-cloud/1.x/js/common/tree/css/zTreeStyle.css
         ,/project/b-ms-cloud/1.x/common/webuploader/js/webuploader-min.js
         ,/project/b-ms-cloud/1.x/js/common/tree/tree.js">
<body>
<!-- 公共的头部 B-->
<jsp:include page="/WEB-INF/jsp/commons/opheader.jsp">
    <jsp:param name="nav" value="zy"/>
</jsp:include>
<!-- 公共的头部 E-->
<div class="i-main">
    <div class="p-upload-cont">
        <h2 class="p-up-title">
            <b>修改资源</b>
            <kl:view url="/operation/core/resource/delResource">
                 <a class="p-del" href="javascript:;" id="jp-delete-zy"></a>
            </kl:view>
        </h2>
        <div class="p-conts" id="jp-upload-cont">
            <form id="jp-form" action="#" method="post">
                <!-- 资源id -->
                <input id="jp-resourceid" type="hidden" name="resourceId" value="${resource.id}">
                <!-- 上传文件 -->
                <div class="item p-load">
                    <label class="p-name">文件<i>*</i>：</label>
                    <div class="field p-posit">
                        <input id="jp-file-data" class="p-hid-inpt" type="text" name="fileName" value="11_09.png">
                        <!-- 上传后填充文件名 -->
                        <div class="p-inline-div" id="jp-file-div">
                            <b id="jp-filename" class="p-filename">${resource.fileOldName}</b>
                            <a id="jp-delete" class="p-delete" href="javascript:;"></a>
                        </div>
                        <!-- 上传按钮 -->
                        <div class="p-inline-div p-hide-div" id="jp-file-btn">
                            <a id="jp-local-upload" class="p-upload p-addbtn" href="javascript:;"></a>
                            <em id="jp-known" class="p-known">（上传须知：上传文档 WORD/PPT不能超过10M，MP3/MPG4不能超过100M，PDF不能超过50M，GIF/JPEG/JPG/PNG文件不能超过5M）</em>
                        </div>
                        <div id="thelist" class="uploader-list p-hide-div">
                            <div class="progress progress-striped active">
                                <div class="progress-bar" role="progressbar"></div>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- 上传封面 -->
                <div class="item p-load">
                    <label class="p-name">封面：</label>
                    <div class="field">
                        <!-- 上传后填充文件名 -->
                        <div class="p-inline-div fc p-img-div" id="jp-img-div">
                                <span id="jp-img-name" class="p-imgname fl">
                                    <img src="/data${resource.frontcoverUrl}">
                                </span>
                            <a id="jp-img-delete" class="p-delete fl" href="javascript:;"></a>
                        </div>
                        <!-- 上传按钮 -->
                        <div class="p-inline-div p-hide-div" id="jp-img-btn">
                            <a id="jp-img-upload" class="p-upload p-addbtn" href="javascript:;"></a>
                        </div>
                        <div id="thelistImg" class="uploader-list p-hide-div">
                            <div class="progress progress-striped active">
                                <div class="progress-bar" role="progressbar"></div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="item">
                    <label class="p-name" for="jp-title-inpt">标题<i>*</i>：</label>
                    <div class="field">
                        <input id="jp-title-inpt" class="p-title-inpt" type="text" name="name" value="${resource.name}">
                    </div>
                </div>
                <div class="item">
                    <label class="p-name" for="jp-synopsis-inpt">简介<i>*</i>：</label>
                    <div class="field">
                        <textarea id="jp-synopsis-inpt" name="description">${resource.description}</textarea>
                    </div>
                </div>
                <div class="item">
                    <label class="p-name" for="">学科<i>*</i>：</label>
                    <div class="field p-selects">
                        <select class="i-sel2" id="sel-show1" name="subjectId">
                            <option value="">请选择</option>
                        </select>
                        <label class="p-name">学段<i>*</i>：</label>
                        <select class="i-sel2" id="sel-show2" name="rangeId">
                            <option value="">请选择</option>
                        </select>
                        <label class="label-rc">
                            <input type="checkbox" class="for-checkbox" name="marrow" <c:if test="${resource.marrow==marrowYes}">checked="checked"</c:if>   value="<%=GlobalConstant.RESOURCE_MARROW_YES%>">
                            <b>标为精华</b>
                        </label>
                    </div>
                </div>
                <div class="item" id="jp-zytype-ps">
                    <label class="p-name">资源类型<i>*</i>：</label>
                    <div class="field field-rc p-zytype">
                        <input id="jp-zy-type" class="p-hid-inpt" type="text" name="type" value="${resource.type}">
                        <div class="p-labels" id="jp-labels">
                            <!-- <label for="a" class="label-rc">
                                <input type="radio" class="for-radio" id="a" value="1" checked="checked">
                                <b>课件</b>
                            </label> -->
                        </div>
                    </div>
                </div>
                <div class="item">
                    <label class="p-name">教材目录<i>*</i>：</label>
                    <div class="field">
                        <input id="jp-progr-direc" class="p-hid-inpt" type="text" name="bookVersionIdstr" value="1">
                        <!-- 选择的进度点 B-->
                        <c:forEach items="${resource.bookVersionTagsEntityList}" var="tag">
                            <span class="p-dirs" data-id="${tag.id}">
                                <em>${tag.name}</em>
                                <i class="jp-delet"></i>
                                <input name="bookVersionIds" value="${tag.id}" type="hidden">
                            </span>
                        </c:forEach>
                        <!-- 选择的进度点 E-->
                        <a class="p-addbtn" href="javascript:;" id="jp-progress-add"></a>
                    </div>
                </div>
                <div class="item">
                    <label class="p-name">知识点：</label>
                    <div class="field">
                        <input id="jp-knowl-direc" class="p-hid-inpt" type="text" name="">
                        <!-- 选择的知识点 B-->
                        <c:forEach items="${resource.konwledgeTagsEntityList}" var="tag">
                            <span class="p-dirs" data-id="${tag.id}">
                                <em>${tag.name}</em>
                                <i class="jp-delet"></i>
                                <input name="knowlgedIds" value="${tag.id}" type="hidden">
                            </span>
                        </c:forEach>
                        <!-- 选择的知识点 E-->
                        <a class="p-addbtn" href="javascript:;" id="jp-knowledge-add"></a>
                    </div>
                </div>
                <p class="p-savebtn">
                    <!-- 这个input是为了判断此 保存 为修改页面中的 -->
                    <input type="hidden" id="jp-ifpage">
                    <button class="green-btn green-btn-style" id="jp-sure" type="button">保存</button>
                </p>
                <!-- 添加图片生成 -->
                <div id="jp-image-input">
                    <input id="" name="frontcoverUrl" value="${resource.frontcoverUrl}" type="hidden">
                </div>
            </form>
        </div>
    </div>
</div>
<jsp:include page="/footer.jsp"/>

</body>
    <fe:seaConfig>
        alias:{
        'project/b-ms-cloud/1.x/js/back-end/op-zyk-upload/page': 'project/b-ms-cloud/1.x/js/back-end/op-zyk-upload/page.js'
        }
    </fe:seaConfig>
    <script type="text/javascript">
        seajs.use('project/b-ms-cloud/1.x/js/back-end/op-zyk-upload/page',function(init){
            init({
                fileSwfUrl:'/picture/upload/Uploader.swf',//上传插件兼容ie8/9浏览器flash文件
                fileUrlDo:'/operation/core/resource/uploadFile',//上传url
                fileUuid:'${uuid}',//上传附件
                imgUuid:'${fuuid}',//上传封面
                treeId:"#treeDemo",//树Id
                treeName:"treeDemo",//树name名
                subjectIdUrl : '/operation/core/resource/getAllSubject',//学科
                treeDataUrl : '/operation/core/resource/getTreeData',//获取学段  知识点 和进度点(教材版本／年级等)
                //保存提交请求地址
                submitUrl : '/operation/core/resource/editResource',//编辑资源
                zyTypePhp : '/operation/core/resource/typeList',//资源类型
                returnZylistUrl : '/operation/core/resource/index',//提交后返回资源列表地址
                 deleteZyUrl : '/operation/core/resource/delResource',//删除资源请求地址
                subjectId : ${resource.subjectTagId}, //默认学科
                rangeId : ${resource.stageTagId} //默认学段
            });
        });
    </script>

</fe:html>