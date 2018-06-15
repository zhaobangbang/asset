<%@ page language="java" contentType="text/html; charset=utf-8" 	pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="Expires" content="-1"/>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-control" content="no-cache"/>
<meta http-equiv="Cache" content="no-cache"/>
<meta name="viewport"
	content="width=device-width, initial-scale=1,maximum-scale=1, user-scalable=no">
<title>资产定位管理</title>
<link href="css/bootstrap/css/bootstrap.min.css" title=""
	rel="stylesheet">
<link rel="SHORTCUT ICON" href="images/favicon.ico">
<link title="" href="css/style.css" rel="stylesheet" type="text/css">
<link title="blue" href="css/dermadefault.css" rel="stylesheet"
	type="text/css">
<link href="css/templatecss.css" rel="stylesheet" title=""
	type="text/css">
<script src="js/jquery-1.11.1.min.js" type="text/javascript"></script>
<script src="js/bootstrap.min.js" type="text/javascript"></script>
<%
		String sName = (String)session.getAttribute("usrname");
		if(null == sName || sName.equals(""))
		{  
			response.sendRedirect("index.jsp");
   			return;
 		}
		
		String prio = (String)session.getAttribute("prio");
		if(null == prio || prio.equals(""))
		{
			response.sendRedirect("index.jsp");
			return;
		}
	%>
</head>

<body>
	<nav class="nav navbar-default navbar-mystyle navbar-fixed-top">
		<div class="navbar-header">
			<img src="images/LOGO_PNG_02.png"
				style="border-style: none; vertical-align: top; height: 100%; float: left; height: 51px;">
		</div>
		<div class="collapse navbar-collapse">
			<ul class="nav navbar-nav pull-right">
				<li class="li-border"><a href="index.jsp"
					style="color: #fff; font-size: 16px;"> <span
						class="glyphicon glyphicon-log-out"
						style="margin-right: 10px; color: #fff;"></span>退出登录
				</a></li>
			</ul>
		</div>
	</nav>
	<div class="down-main">
		<div class="left-main left-full">
			<div class="sidebar-fold">
				<span class="glyphicon glyphicon-menu-hamburger"></span>
			</div>
			<div class="subNavBox">
				<div class="sBox">
					<ul class="navContent" style="display: block">
						<li>
							<div class="showtitle" style="width: 100px;">
								<img src="images/leftimg.png">用户管理
							</div> <a target="trackList" href="UsersManager.html?name=<%=sName %>">
								<span class="sublist-icon glyphicon glyphicon-user"></span> <span
								class="sub-title">用户管理</span>
						</a>
						</li>
						<li>
							<div class="showtitle" style="width: 100px;">
								<img src="images/leftimg.png">终端管理
							</div> <a target="trackList"
							href="DevListManager.html?name=<%=sName %>"> <span
								class="sublist-icon glyphicon glyphicon-modal-window"></span> <span
								class="sub-title">终端管理</span></a>
						</li>
						<li>
							<div class="showtitle" style="width: 100px; display: none;">
								<img src="images/leftimg.png">信标管理
							</div> <a target="trackList"
							href="jsp/System_BeaconManager.html?name=<%=sName %>"> <span
								class="sublist-icon glyphicon glyphicon-headphones"></span> <span
								class="sub-title">信标管理</span></a>
						</li>
						<li>
							<div class="showtitle" style="width: 100px; display: none;">
								<img src="images/leftimg.png">地图管理
							</div> <a target="trackList"
							href="jsp/System_MapManager.html?name=<%=sName %>"> <span
								class="sublist-icon glyphicon glyphicon-flag"> </span><span
								class="sub-title">地图管理</span></a>
						</li>
						<li>
							<div class="showtitle" style="width: 100px; display: none;">
								<img src="images/leftimg.png">网关管理
							</div> <a target="trackList" href="GateWayManager.html"> <span
								class="sublist-icon glyphicon glyphicon-inbox"></span> <span
								class="sub-title">网关管理</span></a>
						</li>
						<li>
							<div class="showtitle" style="width: 100px; display: none;">
								<img src="images/leftimg.png">区域管理
							</div> <a target="trackList" href="CityManager.html?name=<%=sName %>">
								<span class="sublist-icon glyphicon glyphicon-globe"> </span><span
								class="sub-title">区域管理</span>
						</a>
						</li>
						<li>
							<div class="showtitle" style="width: 100px; display: none;">
								<img src="images/leftimg.png">个人信息
							</div> <a target="trackList" href="MyselfManager.jsp?name=<%=sName %>">
								<span class="sublist-icon glyphicon glyphicon-envelope">
							</span><span class="sub-title">个人信息</span>
						</a>
						</li>
						<li>
							<div class="showtitle" style="width: 100px; display: none;">
								<img src="images/leftimg.png">注册码管理
							</div> <a target="trackList" href="RegisterCode.html"> <span
								class="sublist-icon glyphicon glyphicon-qrcode"></span> <span
								class="sub-title">注册码管理</span></a>
						</li>
						<li>
							<div class="showtitle" style="width: 100px; display: none;">
								<img src="images/leftimg.png">系统管理
							</div> <a target="trackList" href="html/warningmgr/UsersLoginInfo.html">
								<span class="sublist-icon glyphicon glyphicon-cog"> </span><span
								class="sub-title">系统管理</span>
						</a>
						</li>
					</ul>
				</div>
			</div>
		</div>
		<div class="right-product my-index right-full">
			<iframe width="100%" scrolling="auto" height="100%"
				frameborder="false" allowtransparency="true"
				style="border: medium none;"
				src="UsersManager.html?name=<%=sName %>" id="rightMain"
				name="trackList"></iframe>
		</div>
		<div id="footer">
			<div class="user">
				<div class="user-img"></div>
				<div class="user-label">
					欢迎您:
					<%=sName%>&nbsp;权限：<%=prio%><span class="user-color"></span>
				</div>
			</div>
		</div>
	</div>


	<script type="text/javascript">
$(".subNav").click(function(){				
	if($(this).find("span:first-child").attr('class')=="title-icon glyphicon glyphicon-chevron-down")
	{
		$(this).find("span:first-child").removeClass("glyphicon-chevron-down");
	    $(this).find("span:first-child").addClass("glyphicon-chevron-up");
	    $(this).removeClass("sublist-down");
		$(this).addClass("sublist-up");
        }
	else
	{
		$(this).find("span:first-child").removeClass("glyphicon-chevron-up");
		$(this).find("span:first-child").addClass("glyphicon-chevron-down");
		$(this).removeClass("sublist-up");
		$(this).addClass("sublist-down");
        }
$(this).next(".navContent").slideToggle(300).siblings(".navContent").slideUp(300);
})

$(".left-main .sidebar-fold").click(function(){

if($(this).parent().attr('class')=="left-main left-full")
{
$(this).parent().removeClass("left-full");
$(this).parent().addClass("left-off");

$(this).parent().parent().find(".right-product").removeClass("right-full");
$(this).parent().parent().find(".right-product").addClass("right-off");

        }
else
{
$(this).parent().removeClass("left-off");
$(this).parent().addClass("left-full");

$(this).parent().parent().find(".right-product").removeClass("right-off");
$(this).parent().parent().find(".right-product").addClass("right-full");

        }
})	

$(".sBox ul li").mouseenter(function(){
if($(this).find("span:last-child").css("display")=="none")
{$(this).find("div").show();}
}).mouseleave(function(){
	$(this).find("div").hide();
})	

</script>

</body>
</html>
