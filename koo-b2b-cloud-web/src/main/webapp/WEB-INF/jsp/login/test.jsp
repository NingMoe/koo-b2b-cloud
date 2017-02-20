<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="com.koolearn.framework.common.utils.PropertiesConfigUtils"%>
<%@ page import="com.koolearn.cloud.util.GlobalConstant"%>
<%@taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<fe:html title="登录" initSeajs="true" ie="true" defaultHead="<!DOCTYPE html><html>"
         assets="/project/b-ms-cloud/1.x/css/t-login/page.css">
<body>
<div class="p-header">
    <div class="i-box">
        <a class="p-lg fl" href="#" target="_bank">
            <img src="<%=PropertiesConfigUtils.getProperty("domains.ui") %>/project/b-ms-cloud/1.x/i/i-logo.png">
        </a>
        <b class="p-b fl">中小学教育云平台</b>
    </div>
</div>
<div class="p-login-body">
<div id="uploadForm">
    <input id="file" type="file"/>
    <button id="upload" type="button" onclick="fileUpload();">upload</button>
</div>
</div>
</body>
    <fe:seaConfig>
        alias:{
        'project/b-ms-cloud/1.x/js/t-login/page': 'project/b-ms-cloud/1.x/js/t-login/page.js'
        }
    </fe:seaConfig>
<script type="text/javascript">
function fileUpload() {
	var formData = new FormData();
	formData.append('upFile', $('#file')[0].files[0]);
	$.ajax({
	    url: '/fileUpload',
	    type: 'POST',
	    cache: false,
	    data: formData,
	    processData: false,
	    contentType: false
	}).done(function(res) {
		console.info(11);
	}).fail(function(res) {
		console.info(22);
	});
}

    seajs.use(['project/b-ms-cloud/1.x/js/t-login/page'],function(app){
        var json = {
            "login" :  login
        };
        app.init(json);
    });
</script>
<script type="text/javascript">
    function login(){
    	var mobileEmail = $('#jp-tel').val();
    	var password = $('#jp-pass').val();
    	var random = $('#jp-yzm').val();
    	$.ajax({
    	    url:'/login',
    	    type:'post',
    	    async: false,
    	    dataType:'json',
    	    data:{
    	    	mobileEmail : mobileEmail,
    	        password : password,
    	        random : random
    	    },
    	    success:function(data){
    	        if(data.success==true){
    	        	regLogin(data);
    	        }else{
    	        	//console.info(data.errMsg);
    	        	alert(data.errMsg);
    	        }
    	    }
    	})
    }

    function regLogin(data){
    	var ssoUrl = '${ssoUrl}/quickLogin.do';
    	var userName = data.userName;
    	var password = data.password;
    	$.ajax({
    		url: ssoUrl,
    		async :false,
    		cache:false,
    		dataType: 'jsonp',
    	    data:{
                userName : userName,
                password : password,
                type:"jsonp"
            },
            success:function(data){
            	console.info("regLogin="+data);
            	if(data=='success'){
            		//alert("注册成功后跳转");
            		window.location="/first";
            	}else{
            		if("error.login.timeInterval"==data){
            			alert("十五分钟不同ip不能重复登陆");
            		}else if("error.login.threepassError"==data){
            			alert("连续三次输入密码错误后5分钟封禁");
            		}else if("error.login.userNameIsError"==data){
            			alert("用户名密码错误");
            		}else{
	            		alert("登录失败");
            		}
            	}
            }
    	})
    }
    
    function changeRandom(){
        var randomImg = $("#randomImg");
        randomImg.attr("src", "/random?time="+new Date().getTime());
     }

</script>
</fe:html>

