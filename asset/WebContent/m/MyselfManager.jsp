<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0;" />
<meta content="yes" name="apple-mobile-web-app-capable" />
<link href="../css/login2.css" rel="stylesheet" type="text/css" />
<link rel="SHORTCUT ICON" href="../images/favicon.ico" />
<link href="styles/NewGlobal.css" rel="stylesheet" />
<script type="text/javascript"
	src="http://ajax.microsoft.com/ajax/jquery/jquery-1.4.min.js"></script>

<title>个人信息</title>

<style type="text/css">
body {
	margin: 0 auto;
	min-width: 320px;
	max-width: 420px;
	background-color: #fff;
	color: #666;
	background: #f5f5f5;
	font: 12px/1.5 Tahoma, Arial, "微软雅黑 ", Helvetica, sans-serif;
}

div, td, input {
	font-size: 12px;
	margin: 0px;
}

legend {
	font-weight: bold;
}

select {
	height: 20px;
	width: 80%;
}

.title {
	font-size: 16px;
	padding: 10px;
	margin: 10px;
	width: 80%;
}

.text {
	height: 20px;
	width: 80%;
	border: 1px solid #AAAAAA;
}

.line {
	margin: 2px;
}

.leftDiv {
	width: 38%;
	float: left;
	height: 22px;
	line-height: 22px;
	font-weight: bold;
}

.rightDiv {
	height: 22px;
	line-height: 22px;
}

.button {
	color: #fff;
	font-weight: bold;
	font-size: 11px;
	text-align: center;
	padding: .17em 0 .2em .17em;
	border-style: solid;
	border-width: 1px;
	border-color: #9cf #159 #159 #9cf;
	background: #69c url(../../images/bg-btn-blue.gif) repeat-x;
}

.btn-info {
	background-color: rgba(101, 150, 172, 0.6);
	border-color: rgba(101, 150, 172, 0.6);
}

.btn-info:hover {
	background-color: #3F304F;
	border-color: #3F304F;
}

.change-message {
	width: 60%;
	height: 30px;
	margin: 10px;
	font-size: 16px;
	background-color: rgba(255, 255, 255, 0);
	border: 1px solid rgba(255, 255, 255, 0.8);
	border-radius: 5px;
	color: #fff;
}

.change-message:focus {
	background-color: rgba(255, 255, 255, 1);
	color: #000;
}
</style>

</head>

<body>
	<%
	String sName = request.getParameter("name");
	if(null == sName)
		sName = "";
%>
	<script type="text/javascript">
	$(document).ready(function(){
		var inputName = "<%=sName%>";
		console.log(inputName);
    	$('#nameDiv').html("修改"+inputName+"信息");
    	
    	var url = "../MyselfManager.do?name="+inputName;
    	$.get(url, function(data, status) {
    		var usrInfo = eval("(" + data + ")");
    		$("#oldpasswd").attr("value","");
    		$('#phone').attr("value", usrInfo["phone"]);
    		$('#email').attr("value", usrInfo["email"]);
    		$('#uid').attr("value",usrInfo["uid"]);
    	});
    	
    	$("#selfConfigForm").attr("action", url);
	});
	
	function checkInput() {
		var inputName = "<%=sName%>";
		var pass1 = $("#passwd").val();
		var pass2 = $("#passwd2").val();
		var pass0 = $("#oldpasswd").val();
		
		if ("" == pass1) {
			alert("请输入旧密码");
			return false;
		}
		if ("" == pass1) {
			alert("请输入密码");
			return false;
		}
		if ("" == pass2) {
			alert("请输入确认密码");
			return false;
		}
		if (pass1 != pass2) {
			alert("请输入一致的密码");
			return false;
		}
		var newPhone = $("#phone").val();
		if ("" == newPhone) {
			alert("请输入有效的电话号码");
			return false;
		}
		var newMail = $("#email").val();
		if ("" == newMail) {
			alert("请输入有效的邮箱");
			return false;
		}
		
		var oldpsistrue = true;
		var user = {
				    username: inputName,
				    password: pass0,
				    rkey: ""
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
				    	if(status== "nokey"){
				    	   alert("您输入的旧密码不正确！");
				    	   oldpsistrue = false;
				    	   return false;
				    	}
						}
				     });
		if(!oldpsistrue){
			return false;
		}
		
		if (pass0 === pass1) {
			alert("与原密码相同，请重新输入");
			return false;
		}
		 alert("保存成功");
		
		return true;
	}
	
	
</script>

	<div class="header">
		<a href="#" onClick="javascript:history.go(-1);" class="home"> <span
			class="header-icon header-icon-home"><img
				src="Img/icon_home.png"></span> <span class="header-name">主页</span>
		</a>
		<div
			style="text-align: center; color: #fff; position: absolute; top: 15px; right: 37%; font-size: 20px;">个人信息</div>
		<a href="#" onClick="javascript:history.go(-1);" class="back"> <span
			class="header-icon header-icon-return"><img
				src="Img/icon_return.png"></span> <span class="header-name">返回</span>
		</a>
	</div>

	<div class="container width95 pt10" style="float: center;">

		<div style="background: #ADD8E6">

			<form action="../MyselfManager.do" method="post" id="selfConfigForm">
				<div style='width: 90%; color: #000;'>

					<p id="nameDiv"
						style="font-size: 20px; text-align: center; margin-top: 10px;">修改信息</p>
					<table align="center" width="100%">
						<tr style="width: 100%;">
							<td align="right"
								style="width: 40%; font-size: 18px; margin-top: 10px; margin-bottom: 10px">原密码：</td>
							<td><input type="password" id="oldpasswd"
								class="change-message" name="oldpasswd" value="" /></td>
						</tr>
						<tr style="width: 100%;">
							<td align="right"
								style="width: 40%; font-size: 18px; margin-top: 10px; margin-bottom: 10px">新密码：</td>
							<td><input type="password" id="passwd"
								class="change-message" name="passwd" value="" /></td>
						</tr>
						<tr style="width: 100%;">
							<td align="right"
								style="width: 40%; font-size: 18px; margin-top: 10px; margin-bottom: 10px">确认密码：</td>
							<td><input type="password" id="passwd2" name="passwd2"
								value="" class="change-message" /></td>
						</tr>
						<tr>
							<td align="right"
								style="width: 40%; font-size: 18px; margin-top: 10px; margin-bottom: 10px">电话：</td>
							<td><input type="text" id="phone" name="phone" value=""
								class="change-message" /></td>
						</tr>
						<tr>
							<td align="right"
								style="width: 40%; font-size: 18px; margin-top: 10px; margin-bottom: 10px">身份证：</td>
							<td><input type="text" id="uid" name="uid" value=""
								class="change-message" /></td>
						</tr>
						<tr>
							<td align="right"
								style="width: 40%; font-size: 18px; margin-top: 10px; margin-bottom: 10px">电子邮箱：</td>
							<td><input type="text" id="email" name="email" value=""
								class="change-message" style="width: 100%" /></td>
						</tr>

					</table>
					</br> <input type="submit" name="search" value="修改 " class="button_blue"
						style="width: 30%; margin-left: 40%; margin-top: 20px"
						onclick="return checkInput();">
				</div>
				</br>

			</form>
		</div>

	</div>
</body>
</html>