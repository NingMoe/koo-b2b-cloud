<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.koolearn.framework.common.utils.PropertiesConfigUtils" %>
<%@ page import="com.koolearn.cloud.util.GlobalConstant" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix='fmt' %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="koo" uri="http://www.koolearn.com/taglib/koo" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<fe:html title="完善资料" initSeajs="true" ie="true"
         assets="/project/b-ms-cloud/1.x/css/t-zc-wszl/page.css">
<fe:seaConfig>
    alias:{
    'project/b-ms-cloud/1.x/js/t-zc-wszl/page': 'project/b-ms-cloud/1.x/js/t-zc-wszl/page.js'
    }
</fe:seaConfig>
    <script type="text/javascript">
        seajs.use(['project/b-ms-cloud/1.x/js/t-zc-wszl/page'],function(app){
            app.init({
                api:{
                    district:'/teacher/data/findCityList', //查询所有城市
                    county:'/teacher/data/findAreaByCityId', //查询所有城市下的区县
                    school:'/teacher/data/findSchoolList',//查询所有学校
                    subject:'/teacher/data/findRangeList',
                    submits:'/teacher/data/addTeacherInfo', //提交老师完善资料的数据
                    location:"/teacher/choiceSubject/goShowBook" //跳转页面
                }
            });
        });
//加载省份
        $(function (){

        });
        //禁止后退键 作用于Firefox、Opera
        document.onkeypress=banBackSpace;
        //禁止后退键 作用于IE、Chrome
        document.onkeydown=banBackSpace;
        //处理键盘事件 禁止后退键（Backspace）密码或单行、多行文本框除外
        function banBackSpace(e){
            var ev = e || window.event;//获取event对象
            var obj = ev.target || ev.srcElement;//获取事件源
            var t = obj.type || obj.getAttribute('type');//获取事件源类型
            //获取作为判断条件的事件类型
            var vReadOnly = obj.getAttribute('readonly');
            var vEnabled = obj.getAttribute('enabled');
            //处理null值情况
            vReadOnly = (vReadOnly == null) ? false : vReadOnly;
            vEnabled = (vEnabled == null) ? true : vEnabled;
            //当敲Backspace键时，事件源类型为密码或单行、多行文本的，
            //并且readonly属性为true或enabled属性为false的，则退格键失效
            var flag1=(ev.keyCode == 8 && (t=="password" || t=="text" || t=="textarea") && (vReadOnly==true || vEnabled!=true))?true:false;
            //当敲Backspace键时，事件源类型非密码或单行、多行文本的，则退格键失效
            var flag2=(ev.keyCode == 8 && t != "password" && t != "text" && t != "textarea") ?true:false;
            //判断
            if(flag2){
                return false;
            }
            if(flag1){
                return false;
            }
        }
    </script>
</head>
<body>

<div class="i-main fc">
    <input type="hidden" id="jp-gradeId">
    <div class="p-ws-school">
        <div class="jp-choose-txt p-choose-txt">
            <span class="jp-choose-tit jp-city-name p-city-name"></span>
            <span class="jp-choose-tit jp-district-name p-district-name"></span>
            <span class="jp-choose-tit jp-school-name p-school-name"></span>
            <span class="jp-choose-tit jp-subject-tvb"></span>
            <span class="jp-choose-tit jp-subject-name p-subject-name"></span>
        </div>
        <div class="p-choose-tab">
                <span class="jp-tab-sp p-tab-sp on">
                    <span class="p-txt">省／市</span>
                </span>
            <span class="p-line"></span>
                <span class="jp-tab-sp p-tab-sp">
                    <span class="p-txt">县／区</span>
                </span>
            <span class="p-line"></span>
                <span class="jp-tab-sp p-tab-sp">
                    <span class="p-txt">学校</span>
                </span>
            <span class="p-line"></span>
                <span class="jp-tab-sp p-tab-sp">
                    <span class="p-txt">学段<br>学科</span>
                </span>
        </div>
        <div class="p-choose-lst">
            <div class="jp-brother jp-city p-city">
                <div  class="p-link">
                    <c:forEach items="${data}" var="province" >
                        <a href="javascript:;" class="jp-city-link"  type="${province.type}" id="${province.id}" >${province.name}</a>
                    </c:forEach>

                </div>
            </div>
            <div class="jp-brother jp-district p-district" style="display:none">
            </div>
            <div class="jp-brother jp-school p-school" style="display:none">
            </div>
            <div class="jp-brother jp-subject p-subject" style="display:none">

            </div>
        </div>
    </div>
    <div class="jp-btn-box p-btn-box" style="display:none">
        <a href="javascript:;" class="jp-submit green-btn green-btn-style">完成</a>
    </div>
</div>
<jsp:include page="/footer.jsp"></jsp:include>

</body>
</fe:html>