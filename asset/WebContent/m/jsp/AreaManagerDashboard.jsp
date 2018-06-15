<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>资产管理系统</title>
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0;" />
<meta content="yes" name="apple-mobile-web-app-capable" />

<link href="../styles/bootstrap.min.css" rel="stylesheet" />
<link href="../styles/bootstrap-responsive.css" rel="stylesheet" />
<link href="../styles/NewGlobal.css" rel="stylesheet" />
<link href="../styles/bootstrap.css" rel="stylesheet" type="text/css" />
<link rel="SHORTCUT ICON" href="../../images/favicon.ico" />

<script type="text/javascript" src="../../js/jquery-3.1.0.min.js"></script>
<script src="../Scripts/bootstrap.min.js"></script>

<style>
body {
	margin: 0 auto;
	min-width: 320px;
	max-width: 414px;
	background-color: #fff;
	color: #666;
	background: #f5f5f5;
	font: 12px/1.5 Tahoma, Arial, "微软雅黑 ", Helvetica, sans-serif;
}

.user {
	float: left;
	height: 26px;
	line-height: 26px;
	padding-left: 10px;
}

.user-img {
	width: 14px;
	height: 14px;
	background-image: url(../../images/user.png);
	float: left;
	margin-top: 6px;
	margin-right: 5px;
}

.user-label {
	float: left;
}

.user-color {
	color: #93d2ef;
}

.user-side {
	width: 2px;
	height: 14px;
	background-image: url(../../images/user-side.png);
	float: left;
	margin-top: 6px;
	margin-left: 10px;
	margin-right: 10px;
}
</style>
<%
		String sName = (String)session.getAttribute("usrname");
		if(null == sName || sName.equals(""))
		{  
			response.sendRedirect("../index.jsp");
   			return;
 		}
		
		String prio = (String)session.getAttribute("prio");
		if(null == prio || prio.equals(""))
		{
			response.sendRedirect("../index.jsp");
			return;
		}
	%>

</head>

<body>
	<div class="header">
		<img alt="蓝思科技" src="../../images/logo.png"
			style="height: 40px; margin: 10px 0px 0px 15px" />
		<div
			style="position: absolute; right: 7px; bottom: 20px; font-size: 16px;">
			<a href="../index.jsp">退出登录</a>
		</div>
	</div>

	<div style="padding: 0 5px 0 0;">

		<ul class="unstyled defaultlist pt20">
			<li class="f"><a href="../UsersManager.html?name=<%=sName %>">
					<h3>用户管理</h3>
					<figure class="jp_icon">
						<img src="../Img/0.png" width="100%">
					</figure>
			</a></li>
			<li class="h"><a href="../DevListManager.html?name=<%=sName %>">
					<h3>终端管理</h3>
					<figure class="jd_icon">
						<img src="../Img/1.png" width="100%">
					</figure>
			</a></li>
		</ul>
		<ul class="unstyled defaultlist">
			<li class="a"><a href="../MyselfManager.jsp?name=<%=sName %>">
					<h3>个人信息</h3>
					<figure>
						<img src="../Img/5.png" width="100%">
					</figure>
			</a></li>

		</ul>

	</div>

	<div id="footer">
		<div class="user">
			<div class="user-img"></div>
			<div class="user-label">
				欢迎您:
				<%=sName%>
				权限：<%=prio %><span class="user-color"></span>
			</div>
			<div class="user-side"></div>
			<div class="user-label">
				<span class="user-color"></span>
			</div>
		</div>
	</div>


</body>
</html>
