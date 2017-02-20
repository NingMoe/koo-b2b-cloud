<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<%@ page import="com.koolearn.framework.common.utils.PropertiesConfigUtils"%>
<fe:html title="注册" initSeajs="true" ie="true" defaultHead="<!DOCTYPE html><html>"
assets="/project/b-ms-cloud/1.x/css/t-register/page.css">
<body>
<div class="p-header">
    <div class="i-box">
        <a class="p-lg fl" href="#" target="_bank">
            <img src="<%=PropertiesConfigUtils.getProperty("domains.ui") %>/project/b-ms-cloud/1.x/i/i-logo.png">
        </a>
        <b class="p-b fl">中小学教育云平台</b>
    </div>
</div>
<div class="p-login-body p-fusui">
    <div class="i-box">
        <div class="p-layer">
            <h2 class="fc" id="jp-tab">
                <span class="fl s" data-type="1">我是老师</span>
                <span class="fr" data-type="2">我是学生</span>
            </h2>
            <div id="jp-box"></div>
            <div class="fc p-link">
                <div class="fl"><a href="javascript:;" id="jp-agree">用户协议</a></div>
                <div class="fr"><a href="/index">有账户，马上登录</a></div>
            </div>
        </div>
    </div>
</div>
</body>
</fe:html>
<fe:seaConfig>
    alias:{
    'project/b-ms-cloud/1.x/js/t-register/page': 'project/b-ms-cloud/1.x/js/t-register/page.js'
    }
</fe:seaConfig>
<script type="text/javascript">
    seajs.use(['project/b-ms-cloud/1.x/js/t-register/page'],function(app){
        var urlJson = {
            "ssoUser" : verifyAccountInDatabase,
            "ssoTel" : verifyMobileInDatabase,
            "ssoEmail" : verifyEmailInDatabase,
            "stuUrl" : "/project/b-ms-cloud/1.x/json/t-ajax/ajax.php?b=1",
            "terUrl" : "/project/b-ms-cloud/1.x/json/t-ajax/ajax.php?b=2",
            "yzmImgUrl" : "/random?uuid=${uuid}&type=2",
            'yzmStuFn' : changeRandom,
            'yzmTerFn': terChangeRandom,
            'register': register,
            'yzmAjaxUrl':'/randomRegister?uuid=${uuid}'//验证码请求地址
        };
        app.init(urlJson);
    });
</script>
<script type="text/javascript">
function changeRandom(){
    var randomImg = $("#jp-yzm-box img");
    randomImg.attr("src", "/random?uuid=${uuid}&type=2&time="+new Date().getTime());
 }
 
function terChangeRandom(){
	var mobile = $("#jp-tel").val();
	$.ajax({
		type:"post",
		url:"/sendRegisterCode",
		data:{"mobile":mobile, "msgInterval":60, "imageCode":""},//60秒
		success:function(msg){
			msg = eval('(' + msg + ')')
			if(msg == '0'){
				alert("验证码已发送,由于运营商原因，手机短信可能会有延迟，请耐心等待");
			}else if(msg == '7202'){
				alert('同一个手机号码一天只可接收三次手机验证码');
			}else if(msg == '7004'){
				alert('手机号非法');
			}else if(msg == '7201'){
				alert('请于上次获取验证码一分钟之后再获取');
			}else if(msg == 'imageCodeError'){
				alert('图片验证码填写不正确');
			}else if(msg == '7203'){
				alert('短信发送出错啦，请过一会再试');
			}else{
				alert('短信验证码发送失败，重新获取');
			}
		}
	});
}

function regLogin(da){
	ssoUrl = '${ssoUrl}/quickLogin.do';
	var userName = $('#jp-user').val();
	var password = $('#jp-pass').val();
	$.ajax({
		url: ssoUrl,
		async :false,
		cache:false,
		type:'post',
		dataType: 'jsonp',
	    data:{
            userName : userName,
            password : password,
            type:"jsonp"
        },
        success:function(data){
        	if(data=='success'){
        		window.location="/first";
        	}else{
        		window.location="/loginPage";
        	}
        }
	})
}

function register()
{
	var mobile = $('#jp-tel').val();
	var userName = $('#jp-user').val();
	var password = $('#jp-pass').val();
	var random = $('#jp-yzm').val();
	var role = $('#jp-tab span.s').data('type');
	$.ajax({
	    url:'/regist',
	    type:'post',
	    async: false,
	    dataType:'json',
	    data:{
	        userName : userName,
	        password : password,
	        role : role,
	        random : random,
	        mobile : mobile,
	        uuid:'${uuid}'
	    },
	    success:function(data){
	        if(data.success==true){
	        	regLogin(data);
	        }else{
	        	alert(data.errMsg);
	        }
	        changeRandom();
	    }
	})
}
</script>
<script type="text/javascript">
    //用户名是否已注册
    function verifyAccountInDatabase(url,async,datas){
    	var username = $("#jp-user").val();
    	url = "${ssoUrl}/userNameExists.do?username="+username;
    	if ( url.indexOf("?") == -1 ) {
            url += "?type=jsonp";
        }
        else {
            url += "&type=jsonp";
        }
    	var d = $.Deferred();
    	var status = false;
        $.ajax({
            url:url,
            dataType:'jsonp',
            type: "post",
            cache:false,
            async:false,
            success:function(data){
                if (data == "true") {//用户名已存在
                	status = false;
                } else {
                	status = true;
                }
                d.resolve(status);
            },
            error:function() {
            	status = false;
            	d.reject(status);
            }
        });
        return d.promise();
    }

    //手机号码是否已注册
    function verifyMobileInDatabase(url,async){
    	var mobile = $("#jp-tel").val();
    	url = "${ssoUrl}/mobileExists.do?mobile="+mobile;
        if ( url.indexOf("?") == -1 ) {
            url += "?type=jsonp";
        }
        else {
            url += "&type=jsonp";
        }
        var d = $.Deferred();
        var status = false;
        $.ajax({
            url:url,
            dataType:'jsonp',
            cache:false,
            async:false,
            success:function(data){
                if (data == "true"){
                	status = false;
                }else{
                	status = true;
                }
                d.resolve(status);
            },
            error: function() {
            	status = false;
            	d.reject(status);
            }
        });
        return d.promise();
    }

    //邮箱是否已注册
    function verifyEmailInDatabase(url, async) {
    	var email = $("#jp-tel").val();
    	url = "${ssoUrl}/existsEmail.do?email="+email;
        if ( url.indexOf("?") == -1 ) {
            url += "?type=jsonp";
        }
        else {
            url += "&type=jsonp";
        }
        var d = $.Deferred();
    	var status = false;
        $.ajax({
            url:url,
            dataType:'jsonp',
            cache:false,
            async:false,
            success:function(data) {
                if ( data == "true" ) {
                	status = false;
                } else if(data == "error"){
                	status = false;
                } else {
                	status = true;
                }
                d.resolve(status);
            },
            error: function() {
            	status = false;
            	d.reject(status);
            }
        });
        return d.promise();
    }
</script>