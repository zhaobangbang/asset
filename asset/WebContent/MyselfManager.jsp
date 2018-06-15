<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="Expires" content="-1"/>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-control" content="no-cache"/>
<meta http-equiv="Cache" content="no-cache"/>
<link href="css/login2.css" rel="stylesheet" type="text/css" />
<link rel="SHORTCUT ICON" href="images/favicon.ico" />
<style type="text/css">
body, div, td, input {
	font-size: 12px;
	margin: 0px;
}

legend {
	font-weight: bold;
}

select {
	height: 20px;
	width: 300px;
}

.title {
	font-size: 16px;
	padding: 10px;
	margin: 10px;
	width: 80%;
}

.text {
	height: 20px;
	width: 300px;
	border: 1px solid #AAAAAA;
}

.line {
	margin: 2px;
}

.leftDiv {
	width: 110px;
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
	background: #69c url(../images/bg-btn-blue.gif) repeat-x;
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
<script type="text/ecmascript" src="js/jquery-3.1.0.min.js"></script>
<script type="text/ecmascript" src="js/md5.js"></script>
<script type="text/ecmascript" src="js/rsa.js"></script>
<title>用户信息</title>
</head>
<body
	style="text-align: left; font-size: 16px; color: rgb(82, 87, 131);">
	<br />
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
    	
    	var url = "MyselfManager.do?name="+inputName;
    	$.get(url, function(data, status) {
    		var usrInfo = eval("(" + data + ")");
    		$("#oldpasswd").attr("value","");
    		$('#phone').attr("value", usrInfo["phone"]);
    		$('#email').attr("value", usrInfo["email"]);
    		$('#uid').attr("value",usrInfo["uid"]);
    	});
    	
    	//$("#selfConfigForm").attr("action", url);
    	 $("#modify").click(function(){  
    		var pass1 = $("#passwd").val();
    		var pass2 = $("#passwd2").val();
    		var pass0 = $("#oldpasswd").val();
    			
    		if ("" == pass1) {
    			alert("请输入旧密码");
    			return;
    		}
    		if ("" == pass1) {
    			alert("请输入密码");
    			return;
    		}
    	    if ("" == pass2) {
    			alert("请输入确认密码");
    			return false;
    		}
    		if (pass1 != pass2) {
    			alert("请输入一致的密码");
    			return false;
    		}
    		if (pass0 === pass1) {
    			alert("与原密码相同，请重新输入");
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
    			
    		var user = {
    			username: inputName,
    			password: "welcomeVisitLansitec",
    			rkey: ""
    			};
    				     
   		   $.ajax(
   	   	   {
   	   		   async: false,
   	   		   type: "POST",
   	   		   url: "LoginValidator.do?operate=login&vcode=1",
   	   		   data: user,
   	   		   // dataType: 'json',
   	   		   success: function(data) {
   	   		    $(".btn-guest").attr("data-istrue",0);
   	   			var vcode = eval("("+data+")");
   	   	        user.password = $.md5(pass0 + vcode);
	 		    $.ajax(
				{
					async: false,
					type: "POST",
					url: "LoginValidator.do?operate=login",
					data: user,
					// dataType: 'json',
					success: function(data) {
					     $(".btn-guest").attr("data-istrue",0);
					     var status = eval("("+data+")");
					     if(status== "nokey"){
					    	   alert("您输入的旧密码不正确！");
					    	   return;
					    	}
					     else{
					    	 var encrypt=new JSEncrypt();
                             encrypt.setPublicKey(status[0]);
					    	 $.post(url,
					                 {"passwd":encrypt.encrypt($("#passwd").val()),"phone": encrypt.encrypt($("#phone").val()),"uid":encrypt.encrypt($("#uid").val()),"email":encrypt.encrypt($("#email").val())},   //传参  
					                 function(data){    			  
					                    },  
					                "json"    
					        );  
					     }
					}
				});
               }
   	   		 }); 
    	 });
	});
</script>
	<div align="center">
		<form action="" method="post" id="selfConfigForm">
			<div style='width: 65%; margin-top: 8%; color: #fff'>
				<p id="nameDiv" style="font-size: 20px">修改信息</p>
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
						<td><input type="password" id="passwd" class="change-message"
							name="passwd" value="" /></td>
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
							class="change-message" /></td>
					</tr>

				</table>

				<input type="button" name="search" id="modify" value="修改 "
					class="button_blue"
					style="width: 10%; margin-left: 45%; margin-top: 20px">
			</div>


		</form>

	</div>
</body>
</html>