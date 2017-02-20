<%@ taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="com.koolearn.cloud.util.GlobalConstant" %>

<fe:html title="学校首页" initSeajs="true" ie="true" defaultHead="<!DOCTYPE html><html>"
         assets="/project/b-ms-cloud/1.x/css/back-end/sch-home/page.css">
<script type="text/javascript" src="/project/b-ms-cloud/1.x/css/back-end/sch-home/page.css"></script>
<script type="text/javascript">

</script>
    <body>
    <jsp:include page="/WEB-INF/jsp/commons/schoolHeader.jsp" >
        <jsp:param name="user" value="user"/>
        <jsp:param name="nav" value="sy"/>
    </jsp:include>
    <!-- 公共的头部 E-->
    <div class="i-main">
        <div class="i-box">
            <div  class="p-head">
                <h1><strong>学校码：${school.code} </strong>
                    <span class="fr"><strong>账号有效期至：${endTime}</strong></span>
                </h1>
            </div>
        </div>
        <div class="i-box dashboard">
            <table>
                <thead>
                <tr>
                    <th>班级</th>
                    <th>老师</th>
                    <th class="brnone">学生</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td><div><strong>${classesNum}</strong></div></td>
                    <td><div class="border"><strong>${teacherNum}</strong></div></td>
                    <td><div><strong>${studentNum}</strong></div></td>
                </tr>
                </tbody>


            </table>
        </div>

        <!-- 公共的底部 B-->
        <div class="i-footer">
            <div class="i-foot-page i-box">
                <div class="i-foot-logo fl">
                    <p class="i-flogo">
                        <a href="#" class="i-a1"></a>
                        <a href="#" class="i-a2"></a>
                    </p>
                    <p class="i-ftxt">新东方在线：新东方旗下网络教育品牌</p>
                </div>
                <div class="i-foot-nav fl">
                    <span class="i-fspn"></span>
                    <p class="i-fnav">
                        <a href="#" target="_bank">关于我们</a>
                        <a href="#" target="_bank">合作伙伴</a>
                        <a href="#" target="_bank">使用帮助</a>
                        <a href="#" target="_bank">版权说明</a>
                        <a href="#" target="_bank">联系我们</a>
                    </p>
                    <p class="i-ftxt">
                        京IPC证050421号 京ICP备05067669号 京公安备110-1081940 网络视听许可证0110531号<br>
                        Copyright(c)2000-2012 koolearn.com Inc. All right reserved. 新东方在线 版权所有
                    </p>
                </div>
            </div>
        </div>
    </div>
        <script type="text/javascript">

        </script>
        <!-- 公共的底部 E-->
    </body>
</fe:html>