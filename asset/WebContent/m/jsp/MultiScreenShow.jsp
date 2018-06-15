<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0;" />
<meta content="yes" name="apple-mobile-web-app-capable" />
<link href="../styles/NewGlobal.css" rel="stylesheet" />
<title>用户终端多屏显示</title>

<style type="text/css">
* {
	margin: 0;
	padding: 0;
}

body {
	margin: 0 auto;
	min-width: 320px;
	max-width: 480px;
	background-color: #fff;
	color: #666;
	background: #f5f5f5;
	font: 12px/1.5 Tahoma, Arial, "微软雅黑 ", Helvetica, sans-serif;
}

.header {
	background: #ddd;
	height: 120px;
}

.footerCon {
	width: 320px;
	margin: 0 auto;
}

.footerCon .dropDiv {
	background: #fff;
	margin: 10px 0 0 0;
	float: left;
}

.footerCon .fpmodel {
	display: none;
	float: right;
	width: 160px;
}

.footerCon .saveBtn {
	margin: 10px 0 0 10px;
	padding: 2px 10px;
	border: 1px solid #CCC;
	cursor: pointer;
}

.clearFix:after {
	content: ’’;
	display: block;
	height: 0;
	overflow: hidden;
	clear: both;
}

.footer {
	height: 40px;
	background: #ABABAB;
	position: fixed;
	bottom: 0px;
	width: 100%;
}

.footer .ulTab {
	list-style-type: none;
	width: 200px;
	overflow: hidden;
	float: left;
}

.footer .ulTab li {
	float: left;
	height: 16px;
	padding: 12px 15px;
	cursor: pointer;
}

.footer .ulTab li.currentLi {
	background: #fff;
}

.tabImg {
	width: 18px;
	height: 14px;
	border: 1px solid #707070;
	background: #fff;
}

.tabImg td {
	width: 9px;
	height: 5px;
	border: 1px solid #707070;
}

.divImg {
	border-width: 2px;
	height: 12px;
}

.td3Img td {
	height: 3px;
}

.currentLi .tabImg, .currentLi .tabImg td {
	border-color: #1e7be4;
}

.topbarDiv {
	position: absolute;
	left: 0;
	top: 0;
	right: 0;
	border: 1px solid #dedede;
	z-index: 1;
	height: 25px;
	padding: 3px;
	background: #61C0FA;
	display: none;
}

.changeBtn {
	cursor: pointer;
	padding: 2px 10px;
	float: left;
}

.dropDiv, .footer .dropDiv {
	position: relative;
	width: 100px;
	z-index: 100;
}

.dropDiv .curSrc, .footer .dropDiv .curSrc {
	display: inline-block;
	height: 20px;
	line-height: 20px;
	padding: 0 2px;
}

.dropDiv ul, .footer .dropDiv ul {
	position: absolute;
	left: -1px;
	top: 20px;
	background: #fff;
	width: 100px;
	border: 1px solid #1E7BE4;
	display: none;
}

.dropDiv ul li, .footer .dropDiv ul li {
	line-height: 20px;
	padding: 0 2px;
}

.dropDiv ul li.currentSrc, .footer .dropDiv ul li.currentSrc {
	background: #1E7BE4;
	color: #fff;
	cursor: pointer;
}

.dropDiv ul li:hover, .footer .dropDiv ul li:hover {
	background: #AEC9F3;
	color: #fff;
	cursor: pointer;
}

.optionsWrap {
	float: right;
}

.optionsWrap a {
	font-family: 'MicroSoft YaHei';
	color: #fff;
	text-decoration: none;
	float: left;
}

.optionsWrap a:hover {
	color: #fdd;
	cursor: pointer;
}

.btnBig {
	width: 50px;
	height: 30px;
	text-align: center;
}

.btnSmall {
	width: 50px;
	height: 30px;
	text-align: center;
}

.btnCls {
	width: 50px;
	height: 30px;
	text-align: center;
}
/*分屏模块布局*/
.container {
	position: relative;
}

.fp-box {
	position: absolute;
	border: 1px solid #000;
	background: #fff;
}

.fp-1-1 {
	top: 0;
	left: 0;
	right: 0;
	bottom: 0;
}

.fp-2-1 {
	top: 0;
	left: 0;
	right: 50%;
	bottom: 0;
}

.fp-2-2 {
	top: 0;
	right: 0px;
	bottom: 0;
	width: 50%;
}

.fp-3-1 {
	top: 0;
	left: 0;
	right: 50%;
	bottom: 0;
}

.fp-3-2 {
	top: 0;
	right: 0;
	width: 50%;
	height: 50%;
}

.fp-3-3 {
	top: 50%;
	right: 0;
	bottom: 0;
	width: 50%;
}

.fp-4-1 {
	top: 0;
	left: 0;
	right: 50%;
	height: 50%;
}

.fp-4-2 {
	top: 50%;
	left: 0;
	right: 50%;
	bottom: 0;
}

.fp-4-3 {
	top: 0;
	right: 0;
	width: 50%;
	height: 50%;
}

.fp-4-4 {
	top: 50%;
	right: 0;
	bottom: 0;
	width: 50%;
}

.fp-5-1 {
	top: 0;
	left: 0;
	right: 300px;
	bottom: 252px;
}

.fp-5-2 {
	top: 0px;
	width: 300px;
	right: 0;
	bottom: 252px;
}

.fp-5-3 {
	height: 250px;
	left: 0;
	width: 30%;
	bottom: 0;
}

.fp-5-4 {
	height: 250px;
	left: 30%;
	width: 30%;
	bottom: 0;
}

.fp-5-5 {
	height: 250px;
	left: 60%;
	bottom: 0;
	right: 0;
}

.fp-6-1 {
	top: 0;
	left: 0;
	right: 300px;
	bottom: 252px;
}

.fp-6-2 {
	top: 0;
	width: 300px;
	right: 0;
	height: 250px;
}

.fp-6-3 {
	top: 250px;
	width: 300px;
	right: 0;
	bottom: 252px;
}

.fp-6-4 {
	height: 250px;
	left: 0;
	width: 30%;
	bottom: 0;
}

.fp-6-5 {
	height: 250px;
	left: 30%;
	width: 30%;
	bottom: 0;
}

.fp-6-6 {
	height: 250px;
	left: 60%;
	bottom: 0;
	right: 0;
}
</style>
<%
		String sdevnum = request.getParameter("devs");
		int idevnum = Integer.parseInt(sdevnum);
		String[] deveui = new String[4];
		for(int devindex=0; devindex<4; devindex++)
		{
			deveui[devindex] = "";
		}
		for(int devindex=0; devindex<idevnum; devindex++)
		{
			deveui[devindex] = request.getParameter("dev"+devindex);
		}
	%>
</head>
<body>
	<div class="container width100 pt20">
		<div class="container" id="displayArea">
			<!-- 分屏内容区 -->
		</div>
	</div>
	<script type="text/javascript" src="../../js/jquery-1.7.2.min.js"></script>
	<script type="text/javascript" src="../../js/splitScreen.js"></script>
	<script type="text/javascript">
			//分屏数据
			var iframes = [
					{
						id:'frames_1',
						frameName:'<%=deveui[0]%>',
						src:'SingleScreenShow.jsp?deveui=' + '<%=deveui[0]%>'
					},
					{
						id:'frames_1',
						frameName:'<%=deveui[1]%>',
						src:'SingleScreenShow.jsp?deveui=' + '<%=deveui[1]%>'
					},
					{
						id:'frames_1',
						frameName:'<%=deveui[2]%>',
						src:'SingleScreenShow.jsp?deveui=' + '<%=deveui[2]%>'
					},
					{
						id:'frames_1',
						frameName:'<%=deveui[3]%>',
						src:'SingleScreenShow.jsp?deveui=' + '<%=deveui[3]%>'
					}
				];
			window.onload = function(){
				Panel.resize();
			}
			window.onresize = function(){
				Panel.resize();
			}

			//初始化分屏
			var splitScreen = new splitScreen($('#displayArea'),iframes);
			
			//监听removeSettingCls自定义事件
			splitScreen._on('removeSettingCls',function(){
				splitScreen.toggleTopbar(function(){
						$('.ulTab li[data-fp="setting"]').removeClass('currentLi');
					});
			});
			//分屏切换
			function changeModel(fpmodel){
				if(fpmodel !="setting"){
					splitScreen.screenMode(fpmodel,function(){
						//$(obj).addClass('currentLi').siblings().removeClass('currentLi');
					});
				}else{
					splitScreen.toggleTopbar(function(){
						//$(obj).toggleClass('currentLi');
					});
				}
			}
			
			changeModel(<%=sdevnum%>);
	</script>
</body>
</html>