<%@ page import="com.koolearn.cloud.common.serializer.CommonInstence" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix='fmt' %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<fe:html title="运营端-数据字典管理" initSeajs="true" ie="true" defaultHead="<!DOCTYPE html><html>"
         assets="/project/b-ms-cloud/1.x/css/back-end/op-dict/page.css">
<body>
<!-- 公共的头部 B-->
<jsp:include page="/WEB-INF/jsp/commons/opheader.jsp">
    <jsp:param name="nav" value="sjzd"/>
</jsp:include>
<!-- 公共的头部 E-->
<div class="i-main">
    <div class="p-top-tab">
        <a href="javascript:;" class="curr">学校名单管理</a>
    </div>

    <div class="p-filter-box">
        <form action="" id="jp-filter-form">
            <div class="form-line">
                <div class="item">
                    <span>学校名称：</span>
                    <input class="school-name" name="name" type="text">
                </div>
                <input type="hidden" name="pageNo" value="0" id="jp-page-num">
                <a href="javascript:;" id="jp-search-btn" class="search-btn">搜索</a>
            </div>
            <div class="form-line">
                <div class="item">
                    <span>省：</span>
                    <select class="i-sel2" name="province">
                        <option value="">全部</option>
                    </select>
                </div>
                <div class="item">
                    <span>市：</span>
                    <select class="i-sel2" name="city">
                        <option value="">全部</option>
                    </select>
                </div>
                <div class="item">
                    <span>县：</span>
                    <select class="i-sel2" name="county">
                        <option value="">全部</option>
                    </select>
                </div>
                <div class="item">
                    <span>学段：</span>
                    <select class="i-sel2" name="grade">
                        <option value="">全部</option>
                        <option value="<%=CommonInstence.GRADE_TYPE_xx%>">小学</option>
                        <option value="<%=CommonInstence.GRADE_TYPE_cz%>">初中</option>
                        <option value="<%=CommonInstence.GRADE_TYPE_gz%>">高中</option>
                    </select>
                </div>
                <div class="item">
                    <span>状态：</span>
                    <select class="i-sel2" name="schoolStatus">
                        <option value="">全部</option>
                        <option value="<%=CommonInstence.DIC_SCHOOL_SEARCH_STATUS_HASUSER%>">有用户</option>
                        <option value="<%=CommonInstence.DIC_SCHOOL_SEARCH_STATUS_NOUSER%>">无用户</option>
                        <option value="<%=CommonInstence.DIC_SCHOOL_SEARCH_STATUS_SHIELD%>">已屏蔽</option>
                    </select>
                </div>

            </div>
        </form>
    </div>

    <div class="p-new-box">
        <a href="javascript:;" class="new-btn" id="jp-new-btn">添加学校</a>
    </div>

    <div class="p-table-box">
        <table cellpadding="0" cellspacing="0" border="0">
            <thead>
            <tr>
                <th>学校名称</th>
                <th>所在地区</th>
                <th>学校简称</th>
                <th>学校编码</th>
                <th>学段</th>
                <th>添加人</th>
                <th>状态</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
            <!-- <tr data-id="1" data-area="180,181,182" data-range="1,2">
                <td>北京市第十一中学</td>
                <td>北京十一中</td>
                <td>123</td>
                <td>初中,高中</td>
                <td>张三</td>
                <td>有效</td>
                <td>
                    <a href="javascript:;" class="op-btn op-change">修改</a>
                    <a href="javascript:;" class="op-btn op-block">屏蔽</a>
                </td>
            </tr>
            <tr data-id="2" data-area="180,181,183" data-range="1,2">
                <td>北京市第十二中学</td>
                <td>北京十二中</td>
                <td>456</td>
                <td>初中,高中</td>
                <td>张三三</td>
                <td>有效</td>
                <td>
                    <a href="javascript:;" class="op-btn op-change">修改</a>
                    <a href="javascript:;" class="op-btn op-delete">删除</a>
                </td>
            </tr> -->
            </tbody>
        </table>
    </div>

    <div class="p-pager-box"></div>

    <!-- 新增用户/修改用户 弹窗 S -->
    <div class="p-change-dialog" id="jp-change-dialog">
        <form id="jp-change-form">
            <input type="hidden" name="id" id="schoolId" value="" />
            <div class="p-test-form p-test-form-classics p-test-form-block-error jg-validate-block-error">
                <div class="item">
                    <label class="label" for="name">学校名称:</label>
                    <div class="field">
                        <input class="text" type="text" name="name" id="name"/>
                    </div>
                </div>
                <div class="item">
                    <label class="label" for="shorthand">简称:</label>
                    <div class="field">
                        <input class="text" type="text" name="shortname" id="shorthand"/>
                    </div>
                </div>
                <div class="item">
                    <label class="label">所在地区:</label>
                    <div class="field area-field field-rc" id="jp-pop-linkup-box">
                        <label class="label-rc">
                            <select class="i-sel2" name="province">
                                <option value="">省</option>
                            </select>
                        </label>
                        <label class="label-rc">
                            <select class="i-sel2" name="city">
                                <option value="">市</option>
                            </select>
                        </label>
                        <label class="label-rc">
                            <select class="i-sel2" name="county">
                                <option value="">区</option>
                            </select>
                        </label>
                    </div>
                </div>
                <div class="item">
                    <label class="label">学段:</label>
                    <div class="field field-rc">
                        <label for="range1" class="label-rc">
                            <input class="checkbox" name="grades" type="checkbox" id="range1" value="<%=CommonInstence.GRADE_TYPE_xx%>" />
                            <span>小学</span>
                        </label>
                        <label for="range2" class="label-rc">
                            <input class="checkbox" name="grades" type="checkbox" id="range2" value="<%=CommonInstence.GRADE_TYPE_cz%>"/>
                            <span>初中</span>
                        </label>
                        <label for="range3" class="label-rc">
                            <input class="checkbox" name="grades" type="checkbox" id="range3" value="<%=CommonInstence.GRADE_TYPE_gz%>"/>
                            <span>高中</span>
                        </label>
                    </div>
                </div>
                <div class="btn-box">
                    <input type="submit"  class="save-btn" value="保 存">
                </div>
            </div>
        </form>
    </div>
    <!-- 新增用户/修改用户 弹窗 E -->

</div>
<jsp:include page="/footer.jsp"/>

</body>
    <fe:seaConfig>
        alias:{
        'project/b-ms-cloud/1.x/js/back-end/op-dict/page': 'project/b-ms-cloud/1.x/js/back-end/op-dict/page.js'
        }
    </fe:seaConfig>
    <script type="text/javascript">
        seajs.use('project/b-ms-cloud/1.x/js/back-end/op-dict/page',function(init){
            init({
                cityData: '/operation/core/findLocationList',   //省市县数据接口
                block: '/operation/core/blockSchool',  //屏蔽
                delete: '/operation/core/deleteSchool',  //删除
                addSubmit: '/operation/core/addUpdateSchool',   //新增&修改提交
                getPageData: '/operation/core/searchSchoolList',  //获取数据（表格&分页）
                isExist: '/operation/core/isExist'  //判断学校是否存在 true表示存在
            });
        });
    </script>

</fe:html>