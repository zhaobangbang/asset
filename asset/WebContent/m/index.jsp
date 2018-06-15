<%@ page contentType="text/html; charset=utf-8" language="java"
	import="java.io.*" errorPage=""%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>登陆</title>
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0;" />
<meta content="yes" name="apple-mobile-web-app-capable" />
<link href="styles/bootstrap.min.css" rel="stylesheet" />
<link href="styles/NewGlobal.css" rel="stylesheet" />
<link rel="SHORTCUT ICON" href="../images/favicon.ico" />

<script type="text/javascript" src="../images/login.js"></script>
<script type="text/ecmascript" src="../js/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="Scripts/zepto.js"></script>

<style>
a {
	color: #333;
	text-decoration: none;
}

a:-webkit-any-link {
	color: -webkit-link;
	text-decoration: underline;
	cursor: auto;
}

.quc-tip-error {
	color: red !important;
}

h1 {
	display: block;
	font-size: 2em;
	-webkit-margin-before: 0.67em;
	-webkit-margin-after: 0.67em;
	-webkit-margin-start: 0px;
	-webkit-margin-end: 0px;
	font-weight: bold;
}

input, select, button {
	vertical-align: middle;
}

input {
	-webkit-appearance: textfield;
	background-color: white;
	border-image-source: initial;
	border-image-slice: initial;
	border-image-width: initial;
	border-image-outset: initial;
	border-image-repeat: initial;
	-webkit-rtl-ordering: logical;
	-webkit-user-select: text;
	cursor: auto;
	padding: 1px;
	border-width: 2px;
	/*border-style: inset;*/
	border-color: initial;
}

input, button, textarea, select {
	line-height: 1.2;
	margin: 0;
}

input, button, textarea, select, optgroup, option {
	font-family: inherit;
	font-size: inherit;
	font-style: inherit;
	font-weight: inherit;
}

user agent stylesheet
input, textarea, keygen, select, button {
	text-rendering: auto;
	color: initial;
	letter-spacing: normal;
	word-spacing: normal;
	text-transform: none;
	text-indent: 0px;
	text-shadow: none;
	display: inline-block;
	text-align: start;
	margin: 0em 0em 0em 0em;
	font: 13.3333px Arial;
}

user agent stylesheet
input, textarea, keygen, select, button, meter, progress {
	-webkit-writing-mode: horizontal-tb;
}

.login-content {
	height: 450px;
	position: relative;;
	width: 100%;
}

.login-content .login-bg {
	background-image: url(../images/content-login-bg.png);
	background-size: cover;
	background-position: center center;
	background-repeat: no-repeat;
	height: 450px;
	position: absolute;
	float: center;
	top: 0;
	width: 100%;
}

.login-content .center_content {
	height: 450px;
	margin: 0 auto;
	position: relative;
}

.login-content .content-bg-link {
	display: block;
	height: 100%;
	left: 0;
	position: absolute;
	width: 100%;
	z-index: 5;
}

.login-content .content-layout {
	background: rgba(255, 255, 255, 0.9) !important;
	background: #fff;
	display: none;
	position: absolute;
	float: center;
	width: 100%;
	filter: alpha(opacity = 90);
	z-index: 10;
}

.login-content .content-layout .login {
	overflow: hidden;
	padding: 25px;
}

.login-content .content-layout h1 {
	font-size: 20px;
	margin: 20px 0;
}

.quc-qiuser-panel .quc-wrapper {
	font: 16px "Microsoft YaHei";
	margin-bottom: 20px;
}

.quc-wrapper dd, .quc-wrapper div, .quc-wrapper dl, .quc-wrapper dt,
	.quc-wrapper form, .quc-wrapper i, .quc-wrapper label, .quc-wrapper li,
	.quc-wrapper ol, .quc-wrapper p, .quc-wrapper ul {
	margin: 0;
	padding: 0;
	list-style: none;
	width: auto;
	height: auto;
	box-sizing: content-box;
	-moz-box-sizing: content-box;
	-webkit-box-sizing: content-box;
}

.quc-qiuser-panel .quc-wrapper .quc-tip-wrapper {
	height: 24px;
	padding: 0;
}

.quc-mod-sign-in .quc-tip-wrapper {
	height: 21px;
	line-height: 21px;
	padding: 13px 0 0 105px;
}

.quc-wrapper .quc-tip {
	color: #959393;
}

.quc-qiuser-panel .quc-wrapper .quc-main {
	min-height: 277px;
	padding: 0;
	width: auto;
}

.quc-mod-sign-in .quc-main {
	width: 90%;
	min-height: 190px;
	padding: 0 0 10px 30px;
}

.quc-mod-sign-in p.quc-field-account, .quc-mod-sign-in p.quc-field-password
	{
	background: #fff;
	border: 2px solid #e6e6e6;
	margin-bottom: 18px;
}

.quc-mod-sign-in p.quc-field-account {
	z-index: 10;
}

.quc-mod-sign-in .quc-field {
	margin-bottom: 10px;
}

.quc-fixIe6margin {
	display: inline-block;
}

.quc-mod-sign-in .quc-field-account .quc-label {
	background: url(../images/account-icon.png);
	display: inline-block;
	height: 18px;
	margin: 0 0 0 8px;
	vertical-align: middle;
	width: 18px;
}

.quc-mod-sign-in .quc-label {
	display: inline-block;
	width: 18%;
	margin-right: 10px;
	line-height: 31px;
	font-size: 14px;
	color: #333;
	text-align: right;
}

.quc-mod-sign-in .quc-field-account .quc-input-bg, .quc-mod-sign-in .quc-field-password .quc-input-bg
	{
	display: inline-block;
	height: 48px;
}

.quc-mod-sign-in .quc-field-account .quc-input-bg, .quc-mod-sign-in .quc-field-password .quc-input-bg,
	.quc-mod-sign-in .quc-field-captcha .quc-input-bg {
	background: #fff;
	width: 78%;
}

.quc-mod-sign-in .quc-main .quc-input {
	background: none;
	border: 0;
	font-size: 16px;
	margin: 8px 4px 3px;
	outline: none;
	width: 71%;
}

.quc-wrapper .quc-input {
	width: 77%;
	height: 21px;
	margin: 5px 4px 3px;
	padding: 6px 7px;
	line-height: 20px;
	color: gray;
	outline: 0;
	border: 0;
	background-color: #fff;
	font-family: inherit;
	font-size: 14px;
}

.quc-wrapper .quc-input-bg {
	display: inline-block;
	width: 84%;
	height: 42px;
	vertical-align: middle;
}

.quc-mod-sign-in .quc-field-password .quc-label {
	background: url(../images/password-icon.png);
	display: inline-block;
	height: 18px;
	margin: 0 0 0 8px;
	vertical-align: middle;
	width: 18px;
}

sign-in .quc-field-password .quc-input-bg {
	display: inline-block;
	height: 48px;
}

.quc-mod-sign-in .quc-main .quc-field-keep-alive {
	font-size: 14px;
	margin: 20px 0 10px;
	padding: 0;
}

.quc-clearfix, .quc-clearfix:after {
	clear: both;
}

.quc-clearfix:after {
	content: ".";
	display: block;
	height: 0;
	clear: both;
	visibility: hidden;
}

.quc-clearfix:after {
	content: " ";
	display: block;
	height: 0;
	visibility: hidden;
}

.quc-clearfix, .quc-clearfix:after {
	clear: both;
}

.quc-mod-sign-in .quc-form a.quc-link {
	color: #7a7e80;
}

.quc-wrapper a.quc-link, .quc-wrapper a.quc-link:active {
	color: #0082cb;
}

.quc-wrapper a.forgetPass {
	float: left;
}

.quc-wrapper a {
	text-decoration: none;
}

.quc-mod-sign-in .quc-form a.quc-link {
	color: #7a7e80;
}

.quc-wrapper a.quc-link, .quc-wrapper a.quc-link:active {
	color: #0082cb;
}

.quc-wrapper a.signUpAccount {
	float: right;
}

.quc-wrapper .keepPassword {
	-webkit-appearance: checkbox;
	margin-right: 5px;
}

.quc-wrapper a {
	text-decoration: none;
}

.quc-mod-sign-in p.quc-field-submit {
	margin-top: 20px;
	padding: 0;
}

.quc-mod-sign-in input.quc-submit {
	background: #22ac69;
	border-radius: 5px;
	color: #fff;
	font-size: 14px;
	height: 40px;
	line-height: 40px;
	text-indent: 0;
	width: 100%;
	box-sizing: content-box;
}

body #ft {
	margin-top: 15px;
	padding: 0 0 10px;
	text-align: center;
}

#ft .links a {
	text-decoration: none;
}

#ft .links a:hover {
	color: red;
	text-decoration: underline;
}

#ft, #ft a {
	color: #999;
}

.btn-guest {
	float: right;
	font-family: Tahoma, Arial, "宋体", Helvetica, sans-serif;
	background-color: rgba(0, 0, 0, 0);
	border-color: rgba(0, 0, 0, 0);
	margin-top: -3px;
	font-size: 14px;
	color: #0082cb;
	cursor: pointer;
}

.btn-guest:hover {
	color: #00c0cb;
}

.guest-back-div {
	height: 100%;
	width: 100%;
	position: absolute;
	z-index: 9990;
	background-color: rgba(0, 0, 0, 0.5);
	display: none;
	position: fixed;
}

.guest-back {
	padding: 20px;
	width: 400px;
	height: 265px;
	background-color: #fff;
	margin: 0 auto;
	margin-top: 200px;
}

.div-display-important {
	display: none !important;
}
</style>

<script type="text/javascript"> 
function reloadImage() {
	document.getElementById('btn').disabled = true;
	document.getElementById('identity').src='../IdentityServlet.do?ts=' + new Date().getTime();
}

$(document).ready(function() {	    	
});
</script>

<%
  String sName="";
  String sKey="";

  Cookie[] cookies=request.getCookies();
  String ckExist="";
  if(null != cookies)
  {
     for(int i=0; i<cookies.length; i++)
	 {
	   try{
	    if(cookies[i].getName().equals("lansiuser"))
		{
		   sName=cookies[i].getValue().split("-")[0];
		   sKey=cookies[i].getValue().split("-")[1];
		   sKey = com.lans.infrastructure.util.SecurityUtil.decrypt(sKey, "asset");
		   request.setAttribute("sName", sName);
		   request.setAttribute("sKey", sKey);
		   ckExist = "checked=\"\"";
		}
		}
	   catch(Exception e){
	      cookies[i].setMaxAge(0);
	   }
	 } 
  }
%>

</head>
<body>

	<div class="header">
		<img alt="蓝思科技" src="../images/logo.png"
			style="position: absolute; left: 15px; top: 5px;">
	</div>

	<div class="container width95 pt20">
		<div class="login-content" style="float: center;">
			<div class="login-bg"></div>
			<div class="center_content">
				<div class="content-layout" style="display: block;">
					<div class="login">
						<h1 class="login-title">账号登陆</h1>
						<div class="mod-qiuser-pop quc-qiuser-panel">
							<div class="login-wrapper quc-wrapper quc-page">
								<div class="quc-mod-sign-in quc-mod-normal-sign-in">
									<div class="quc-tip-wrapper">
										<p class="quc-tip"></p>
									</div>
									<div class="quc-main">
										<form class="quc-form" name="qucform" onsubmit="return false">
											<p class="quc-field quc-field-account quc-input-long">
												<span class="quc-fixIe6margin"> <label
													class="quc-label"></label>
												</span> <span class="quc-input-bg"> <input
													class="quc-input quc-input-account" id="name"
													name="account" placeholder="用户名" autocomplete="off"
													value="<%=sName %>"></span>
											</p>
											<p class="quc-field quc-field-password quc-input-long">
												<span class="quc-fixIe6margin"> <label
													class="quc-label"></label>
												</span> <span class="quc-input-bg"> <input
													class="quc-input quc-input-password" id="psw"
													type="password" name="password" maxlength="20"
													placeholder="密码" style="border: 0px;" value="<%=sKey %>"></span>
											</p>
											<p class="quc-field quc-field-keep-alive quc-clearfix">
												<input id="rkey" class="keepPassword quc-link"
													type='checkbox' name='rkey' <%=ckExist%> /> <label
													for="rkey">记住密码</label>
											</p>
										</form>
										<p class="quc-field quc-field-submit">
											<input type="button" value="登录" name="login"
												class="quc-submit quc-button quc-button-sign-in">
										</p>
										<p class="quc-field quc-field-keep-alive quc-clearfix">
											<a class="forgetPass quc-link" href="callbackcode.html"
												target="_blank">找回密码？</a> <a class="signUpAccount quc-link"
												href="register.html" target="_blank">注册帐号</a>
										</p>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>


		<div class="footer">
			<div id="ft">

				<p>Copyright©2016</p>
				<p>All Rights Reserved 南京蓝思信息科技有限公司</p>
				<P>苏ICP备16022777号-1</P>
			</div>
		</div>
		<script type="text/javascript">
	$(document).ready(function(){
		var rolename="";

		$(".quc-submit").click(function(){
			
			fr = document.qucform;
			if(fr.account.value == "")
				{
				$(".quc-tip").addClass("quc-tip-error").html("请输入用户名！");
				fr.account.focus();
				$(".btn-guest").attr("data-istrue",0);
				
				return false;
				}
			if(fr.password.value == "")
			{
				$(".quc-tip").addClass("quc-tip-error").html("请输入密码！");
				$(".btn-guest").attr("data-istrue",0);
			fr.password.focus();
			return false;
			}
			var user = {
			 username: $("#name").val(),
			    password: $("#psw").val(),
			    rkey: $("#rkey").prop("checked")
			};
			     //调用AJAX 
			     $.ajax(
			    		 {
			    			 async: false,
			    			 type: "POST",
			    			 url: "../LoginValidator.do?operate=login",
			    			 data: user,
			    			// dataType: 'json',
			    			 success: function(data) {
			    		    $(".btn-guest").attr("data-istrue",0);
			    			var status = eval("("+data+")");
			    				 if (status == "nouser") {  
			 		                $(".quc-tip").addClass("quc-tip-error").html("用户名错误！");
			 		              
			 		                return false;  
			 		            }
			    				 else if(status== "nokey"){
			    					 $(".quc-tip").addClass("quc-tip-error").html("密码不正确！");
			    					 return false;
			    					 
			    				 }
			    				 
			    				 window.location.href="globalshow.jsp";
							}
			    		 });	 

		});
		$('#psw').bind('keypress', function (event) {
	        if (event.keyCode == "13") {
	        	$(".quc-submit").click();
	        }
	    });
		$('#name').bind('keypress', function (event) {
	        if (event.keyCode == "13") {
	        	$(".quc-submit").click();
	        }
	    });
		

	})
	</script>
</body>
</html>
