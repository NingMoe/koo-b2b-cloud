<%@ taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<%@ page import="com.koolearn.cloud.util.GlobalConstant" %>
<%@ page import="com.koolearn.cloud.util.IndexNameUtils" %>
<%--
  Created by IntelliJ IDEA.
  User: gehaisong
  Date: 2016/5/31
  Time: 10:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- 公共的底部 B-->
<div class="i-footer">
    <div class="i-foot-page i-box">
        <div class="i-foot-logo fl">
            <p class="i-flogo">
                <a href="#" class="i-a1"> </a>
                <a href="#" class="i-a2"> </a>
            </p>
            <p class="i-ftxt">新东方在线：新东方旗下网络教育品牌</p>
        </div>
        <div class="i-foot-nav fl">
			<span class="i-fspn"> </span>
            <p class="i-fnav">
                <a href="/footercontent.jsp?tag=lxwm" target="_blank">联系我们</a>
                <a href="/footercontent.jsp?tag=bqsm" target="_blank">版权说明</a>
                <a href="/footercontent.jsp?tag=hzhb" target="_blank">合作伙伴</a>
                <a href="/footercontent.jsp?tag=sybz" target="_blank">使用帮助</a>
                <a href="/footercontent.jsp?tag=gywm" target="_blank">关于我们</a>
            </p>
            <p class="i-ftxt">
                京IPC证050421号 京ICP备05067669号 京公安备110-1081940 网络视听许可证0110531号<br>
                Copyright(c)2000-2012 koolearn.com Inc. All right reserved. 新东方在线 版权所有
            </p>
        </div>
    </div>
</div>
<!-- 公共的底部 E-->
<fe:seaConfig>
    alias:{
    'project/b-ms-cloud/1.x/js/i-footer/page': 'project/b-ms-cloud/1.x/js/i-footer/page.js'
    }
</fe:seaConfig>
<script>
    seajs.use('project/b-ms-cloud/1.x/js/i-footer/page',function(init){
        init();
    });
</script>
