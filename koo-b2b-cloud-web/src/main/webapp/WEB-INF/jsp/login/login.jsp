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
    <div class="i-box">
        <form action="" id="jp-login-form" method="post">
            <div class="p-layer">
                <h2>用户登录</h2>
                <ul>
                    <li><div class="p-inp"><input type="text" placeholder="请输入用户名/手机号/注册邮箱" id="jp-tel"/><em></em></div></li>
                    <li><div class="p-inp"><input type="password" placeholder="请输入密码" id="jp-pass"/><em></em></div></li>
                    <li class="fc">
                        <div class="p-inp  fl" style="width: 150px"><input type="text" class="p-yz" id="jp-yzm"/><em></em></div>
                        <div class="fr p-img" id="jp-yzm-box"><img src="/random?uuid=${uuid}" width="103" heigh="40" id="randomImg" onclick="changeRandom();"/></div>
                    </li>
                    <li><a href="javascript:;" class="p-btn" id="jp-sub">登录</a></li>
                </ul>
                <div class="fc p-link">
                    <div class="fl"><a href="javascript:;" onclick="toBackPwd('${ssoUrl}','<%=PropertiesConfigUtils.getProperty(GlobalConstant.CLOUD_HOST_DOMAIN)%>');" target="_blank">忘记密码</a></div>
                    <div class="fr"><a href="/registPage">没有账户，马上注册</a></div>
                </div>
            </div>
        </form>
    </div>
</div>
</body>
    <fe:seaConfig>
        alias:{
        'project/b-ms-cloud/1.x/js/t-login/page': 'project/b-ms-cloud/1.x/js/t-login/page.js'
        }
    </fe:seaConfig>
<script type="text/javascript">
    seajs.use(['project/b-ms-cloud/1.x/js/t-login/page'],function(app){
        var json = {
            "login" :  login,
            'yzmAjaxUrl':'/randomLogin?uuid=${uuid}'//验证码请求地址
        };
        app.init(json);
    });
</script>
<script type="text/javascript">
    function toBackPwd(ssoUrl,cloudDomain){
        //找回密码，跳转到原来系统
        cloudDomain=encodeURIComponent(cloudDomain+'/loginPage');
        location.href=ssoUrl+'/toBackPwd.do?direct_np=1&nextPage='+cloudDomain;
    }
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
    	        random : random,
    	        uuid:'${uuid}'
    	    },
    	    success:function(data){
    	        if(data.success==true){
                    if( data.passwordStatus == 1 ){
                        alert( "请及时修改您的密码" );
                        updateUserPasswordStatus( data.ssoUserId );
                    }
    	        	regLogin(data);
    	        }else{
    	        	changeRandom();
    	        	//console.info(data.errMsg);
    	        	alert(data.errMsg);
    	        }
    	    }
    	})
    }

    //修改密码提示状态
    function updateUserPasswordStatus( ssoUserId ){

        $.ajax({
            url:'/updateUserPasswordStatus',
            type:'post',
            async: false,
            dataType:'json',
            data:{
                ssoUserId : ssoUserId
            },
            success:function(data){

            }
        })
    }

    function regLogin(data){
    	var ssoUrl = '${ssoUrl}/quickLogin.do';
    	var userName = data.userName;
    	var password = data.password;
        var result =data.system;//== 'cloud'
        var type = data.userType;
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
            success:function(dataSso){
            	if(dataSso=='success'&& result == 'koolearn' || ( dataSso=='success'&& result == 'cloud'&& type == 0  )  ){//在koolearn的注册用户登录需要确认身份
                    //alert("注册成功后跳转");
                    window.location="/makeSureUserRole?userId=" + data.userId;
                }else if(dataSso=='success'&& result == 'cloud' ){
                    //alert("跳转");
                    window.location="/first";
                } else{
            		changeRandom();
            		if("error.login.timeInterval"==dataSso){
            			alert("十五分钟不同ip不能重复登陆");
            		}else if("error.login.threepassError"==dataSso){
            			alert("连续三次输入密码错误后5分钟封禁");
            		}else if("error.login.userNameIsError"==dataSso){
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
        randomImg.attr("src", "/random?uuid=${uuid}&time="+new Date().getTime());
     }

</script>
</fe:html>

