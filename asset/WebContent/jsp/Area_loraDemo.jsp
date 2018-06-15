<%@ page contentType="text/html; charset=UTF-8" language="java" errorPage="" import="java.net.InetAddress"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="Expires" content="-1">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-control" content="no-cache">
<meta http-equiv="Cache" content="no-cache">
<link rel="stylesheet" type="text/css" href="../css/jquery.popuplayer.min.css" />
<script type="text/ecmascript" src="../js/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="../js/bootstrap.min.js"></script>
<script type="text/javascript" src="../js/jquery.popuplayer.min.js"></script>
<link href="../css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<link rel="SHORTCUT ICON" href="../images/favicon.ico" />

<style type="text/css">
body, html, #allmap {
	width: 100%;
	height: 100%;
	overflow: hidden;
	margin: 0;
}

#l-map {
	height: 100%;
	width: 78%;
	float: left;
	border-right: 2px solid #bcbcbc;
}

#r-result {
	height: 100%;
	width: 20%;
	float: left;
}
#tracetime {
	display: none;
	position: absolute;
	top: 150px;
	left: 500px;
	background-color: rgb(230, 244, 250);
	width: 400px;
	font-size: 14px;
	font-family: "微软雅黑";
	color: #337ab7;
}
#tracetime h1 {
	font-size: 24px;
	text-align: center;
}

.timeform {
	text-align: center;
	padding: 10px;
	margin-bottom: 10px;
}
.timeform label {
	font-size: 15px;
	margin-right: 5px;
}
#one,#six,#twelve,#oneday,#timepicker1,#timepicker2
{
	margin-bottom: 10px;
}
.navbar-nav>li {
	margin-right: 20px;
	margin-top: 15px;
}
.btn-info {
	background-color: rgba(6, 131, 187, 0.6);
	border-color: rgba(6, 131, 187, 0.6);
}
.btn-info:hover {
	background-color: #3F304F;
	border-color: #3F304F;
}
/*以下是侧边收缩栏样式*/
.menu-expanded {
	font-size: 14px;
	background-color: transparent;
	position: fixed;
	width: 250px;
	height: 320px;
	top: 50%;
	right: 0px;
	margin-top: -180px;
	overflow: hidden !important;
	z-index: 5;
}

.menu {
	float: right;
	margin: 50% 15px 0 0;
	height: 30px;
	width: 30px;
	border-radius: 45%;
	background-color: #ffb6ba;
	border: none;
	transition: all 0.40s ease-out;
	box-shadow: 5px 5px 5px #423737;
}

.main-menu {
	visibility: hidden;
	position: absolute;
	/*   right: 30px; */
	left: 15px;
	top: 15px;
	opacity: 0;
	transition: all 0.300s;
	transition-delay: 0s;
}
.menu,
.linee {
	cursor: pointer;
}

.over {
	transform: scale(100);
	transition: all 0.350s ease-in;
	cursor: default;
	background-color: rgba(160, 208, 228, 0.9);
}

#infotable tr:first-child {
	border-top: 2px solid #000;
}

.table>tbody>tr>td, .table>tbody>tr>th, .table>tfoot>tr>td, .table>tfoot>tr>th, .table>thead>tr>td, .table>thead>tr>th{
	border-color: #5a5a5a;
	color: #003c16;
}

 
@keyframes closetop {
  0% {
	transform: translateY(5px) rotate(0deg);
}
  25% {
    transform: translateY(5px) rotate(0deg);
}
  75% {
    transform: translateY(5px) rotate(-45deg);
}
  100% {
    transform: translateY(5px) rotate(-45deg);
}
}
 
@keyframes closebottom {
  0% {
	transform: translateY(0px) rotate(0deg);
}
  25% {
    transform: translateY(-5px) rotate(0deg);
}
  75% {
    transform: translateY(-5px) rotate(45deg);
}
  100% {
    transform: translateY(-5px) rotate(45deg);
}
}

.overmain {
	visibility: visible;
	opacity: 1;
	transition: all 0.400s;
	transition-delay: 0.370s;
	width: 300px;
}
.huawei {
	background: url(../images/huawei.jpg);
	background-position: 0 0;
	background-size: 100% 99.9%;
}
.childDiv {
	position: fixed;
	width: 52%;
	height: 50%;
	top: 71%;
	right: 10%;
	margin-top: -180px;
	overflow: hidden !important;
	z-index: 5;
}
.guest-back-div {
	top: 0;
	height: 100%;
	width: 100%;
	z-index: 7;
	background-color: rgba(0, 0, 0, 0.5);
	display: none;
	position: fixed;
}
.guest-back {
	padding: 20px;
	width: 400px;
	height: 320px;
	background-color: #fff;
	margin: 0 auto;
	margin-top: 160px;
}
.guest-role-select {
	margin-left: 10%;
	margin-right: 10%;
	width: 80%;
	display: inline-block;
	height: 45px;
	text-align: center;
	cursor: pointer;
	line-height: 45px;
	background-color: #84d3ff;
	margin-top: 15px;
	font-family: Tahoma, Arial, "宋体", Helvetica, sans-serif;
	font-size: 14px;
	color: #1f4768;
	border: 1px solid #84d3ff;
}
.guest-role-select-unable {
	margin-left: 10%;
	margin-right: 10%;
	width: 80%;
	display: inline-block;
	height: 45px;
	text-align: center;
	cursor: pointer;
	line-height: 45px;
	background-color: #d4d4d4;
	margin-top: 15px;
	font-family: Tahoma, Arial, "宋体", Helvetica, sans-serif;
	font-size: 14px;
	color: #1f4768;
	border: 1px solid #d4d4d4;
	cursor: no-drop;
}
.guest-role-select:hover {
	background-color: #a8e0ff;
	border: 1px solid #a8e0ff;
}

.guest-role-select.active {
	background-color: #e7f0f5;
	border: 1px solid #84d3ff;
}
</style>
<%
	String sName = (String)request.getSession().getAttribute("usrname");

	if(null == sName || sName.equals(""))
	{  response.sendRedirect("index.jsp");
   		return;
 	}
	sName = (String)request.getParameter("usrname");
	String sType = (String)request.getParameter("type");
%>
<title>智能定位系统</title>
<script type="text/javascript"
	src="https://api.map.baidu.com/api?v=2.0&ak=T3esAl5i0NHA9SAEHF3dRyauqTazR2bf"></script>
</head>

<body onunload="closeSocket()">
	<div id="allmap"></div>
	<!-- 以下是侧边弹出信息框 -->
	<div class="menu-expanded">
		<div id="menu" class="menu" data-open=0></div>
		<div class="main-menu">
  			<table class="table table-border" id="infotable" style="margin-bottom:10px;">
				<caption style="padding-left: 90px;; width: 100%; color: #011f0c;">设备信息</caption>
			</table>
  			<button class="btn btn-info close-equip-message" style="padding:2px;width:60px;margin-left:90px;">关闭</button>		
		</div>
	</div>

	<div class="guest-back-div">
		<div class="guest-back">
			<button class="guest-role-select" value="某公司工地_C1栋_1层">某公司工地_C1栋_1层</button>
			<button class="guest-role-select" value="某公司工地_C1栋_2层">某公司工地_C1栋_2层</button>
			<button class="guest-role-select" value="某公司工地_C3栋_1层">某公司工地_C3栋_1层</button>
			<button class="guest-role-select" value="某公司工地_C3栋_2层">某公司工地_C3栋_2层</button>
		</div>
	</div>
</body>

<script type="text/javascript"> 
/* 以下是控制信息框弹出*/

$(document).ready(function(){
$('.menu , .linee').on('click', function() {
		 var dataopen = Number($(".menu").attr("data-open"));
		 if(dataopen == 0){
			 $('.menu').addClass('over')
			 $('.main-menu').addClass('overmain')
			 $(".menu").attr("data-open",1);
		 }
	});
	
	$('.close-equip-message').on('click', function(){
		  $('.menu').removeClass('over')
		  $('.main-menu').removeClass('overmain')
		  $(".menu").attr("data-open",0)
		  curSelIndex = -1;
	})
	
	$(".guest-role-select").on("click",function(){
		var selectmap_id = null;

		$(this).addClass("active");
		selectName = $(this).attr("value");
		
		if(selectName == "某公司工地_C1栋_1层"){
			selectmap_id = "Huawei_C1_1"
		    window.open('Area_IndoorMap.jsp?usrname=<%=sName%>&mapid='+selectmap_id,'_blank','fullscreen=1,location=0');
		}else if(selectName == "某公司工地_C1栋_2层"){
			selectmap_id = "Huawei_C1_2"
			window.open('Area_IndoorMap.jsp?usrname=<%=sName%>&mapid='+selectmap_id,'_blank','fullscreen=1,location=0');
		}else if(selectName == "某公司工地_C3栋_1层"){
			selectmap_id = "Huawei_C3_1"
				window.open('Area_IndoorMap.jsp?usrname=<%=sName%>&mapid='+selectmap_id,'_blank','fullscreen=1,location=0');
		}else if(selectName == "某公司工地_C3栋_2层"){
			selectmap_id = "Huawei_C3_2"
				window.open('Area_IndoorMap.jsp?usrname=<%=sName%>&mapid='+selectmap_id,'_blank','fullscreen=1,location=0');
		}
		
		$(".guest-role-select").removeClass("active");
	});
		
	$(".guest-back-div").on("click",function(){
		$(".guest-back-div").css("display","none");
		$("body").css("overflow-y","auto");
	});
	

})

var trName = [{"name":"编号"},{"name":"状态"},{"name":"电源"},{"name":"电量"},{"name":"信号"},{"name":"时间"}]
var devStatus = new Array();
var curSelIndex = -1;//当前选择的设备的index

/*以下是添加删除表格*/
function addTable(name,temp){
  for (var i = 0; i < temp.length; i++) {
    var x=document.getElementById('infotable').insertRow(i)
    var y=x.insertCell(0)
    var z=x.insertCell(1)
    y.innerHTML=name[i].name
    z.innerHTML=temp[i].val
}
}
function deleteTable()
{
  var tab = document.getElementById("infotable");
  var rows = tab.rows.length;
  for (var i = rows-1; i >= 0; i--) {
  tab.deleteRow(i);
  }
}
/*end*/
var bm;
var convertor;
var xx = null;
var yy = null;
var tt = null;

var devArray = new Array();
var pointArray = null;
var overlayArray = null;
var drawLine = new Array();
var markergps;
var ctrPoint = new Array();
var markers = new Array();
var beaconmarkers = new Array();
var labels = new Array();
var select = null;
var lbutton = null;
//var curIndex = null;
//index indicates the dev, startpoint indicates the draw start point of the dev.
//clear indicates whether clear the old overlay.
function drawPolyline(index) {  
    if (pointArray[index]==null || pointArray[index].length<=1) {  
        return;  
    }
    if(overlayArray[index] != null)
    {
    	bm.removeOverlay(overlayArray[index]);
    }
    
    if(drawLine[index] == 1)
    {
    	var color;
    	if(0==index % 4)
    		color="red";
    	else if(1==index % 4)
    		color="blue";
    	else if(2==index % 4)
    		color="black";
    	else if(3==index % 4)
    		color="green";
    	
    	var points = new Array();
    	for(var i=0 ; i<pointArray[index].length; i++)
    		points.push(pointArray[index][i].p1);
    		
    	overlayArray[index] = new BMap.Polyline(points, {  
        	strokeColor : color,  
        	strokeWeight : 3,  
        	strokeOpacity : 0.5  
    	});
    	bm.addOverlay(overlayArray[index]); // 画线  
    }
}
function DeviceInfoControl(){    
    // 设置默认停靠位置和偏移量  
    this.defaultAnchor = BMAP_ANCHOR_TOP_RIGHT;    
    this.defaultOffset = new BMap.Size(0, 0);    
}    
// 通过JavaScript的prototype属性继承于BMap.Control   
DeviceInfoControl.prototype = new BMap.Control();
DeviceInfoControl.prototype.initialize = function(map){    
	// 创建一个DOM元素   
	var div = document.createElement("div");
	label = document.createElement("label");
	label.style.backgroundColor = "#5bc0de";
	label.setAttribute("id", "devinfo");
	label.style.color = "#fff";
	label.style.fontSize = "16px";
	label.style.fontFamily = "微软雅黑";
	label.style.fontWeight = "normal";

	div.appendChild(label);
	label.innerHTML = "终端信息";
	map.getContainer().appendChild(div); 
	return div;
}

function showTips(content, height, left){
	  var windowWidth  = $(window).width(); 
	  var tipsDiv = '<div class="x">' + content + '</div>';
	   
	  $( 'body' ).append( tipsDiv ); 
	
	  $( 'div.x' ).css({ 
		  'top' : height + 'px', 
	      'left' : left + 'px',
	      'position'  : 'absolute',
	      'background': '#ff9',  
	      'border' : '1px solid #F96',
	      'border-top-left-radius': 1.5 +'em',
	      'padding': '10px',
	      'width'     : 160 +'px',  
	      'height'    : 'auto'
	  }).show();    
	}
function hideTips(){
	$( 'div.x' ).remove();
}

function DevSerControl(){    
    // 设置默认停靠位置和偏移量  
    this.defaultAnchor = BMAP_ANCHOR_TOP_LEFT;    
    this.defaultOffset = new BMap.Size(60, 2);    
}    
// 通过JavaScript的prototype属性继承于BMap.Control   
DevSerControl.prototype = new BMap.Control();
DevSerControl.prototype.initialize = function(map){    
	// 创建一个DOM元素   
	var div = document.createElement("div");
	
	$.ajaxSetup({
		  async: false
		  });
	$.post("../GetRegisterDevices.do", {type: "ALL", usrname: "<%=sName%>"}, function(data){
			devArray = eval("(" + data + ")");
			pointArray = new Array();
			overlayArray = new Array();
			for(var devNumber=0; devNumber<devArray.length; devNumber++)
			{
				pointArray.push(new Array());
				devStatus[devNumber] = [{'val':devArray[devNumber]},{'val':'未知'},{'val':'未知'},{'val':'未知'},{'val':'未知'},{'val':'未知'}];
			}

			document.getElementById("devinfo").innerHTML = "终端信息：设备数量【" + devArray.length + "】, 在线数量【0】, 离线数量【" + (devArray.length) + "】";
		});
	
	function addChi(str,father,chi,idstr){                                     //给父元素标签添加子元素，并设置文本值,并添加id名
	   var List = document.createElement(chi);
	   List.appendChild(document.createTextNode(str));
	   List.setAttribute("id", idstr);
	   father.appendChild(List);
	}
	function addEclass(ele,str){                                     //给元素添加类的方法
		ele.setAttribute("class", str);
	}
	
	
// 添加导航栏	
	navi = document.createElement("nav");
	addEclass(navi, "navbar");
	navi.style.cursor = "pointer";
	uList = document.createElement("ul");
	addEclass(uList, "nav navbar-nav");
	uList.style.marginTop="-20px";
	
//导航栏中选择设备	
	li1 = document.createElement("li");
	select = document.createElement("select");
	select.style.padding = "2px";

	select.onmouseover = function(){
		showTips('用户名下设备列表',40,60);
	}
	
	select.onmouseout = function(){
		hideTips();
	}
	for(var x = 0;x<devArray.length;x++){
		var option = document.createElement("option");
		option.setAttribute("value",x);//设置option属性值
		
		var showText = devArray[x];
		
	      if(showText == "ef010001")
	    	  showText = "004A770211030031";
	      else if(showText == "ef010002")
	    	  showText = "004A770211030032";
	      else  if(showText == "ef010003")
	    	  showText = "004A770211030033";
	      else  if(showText == "ef010004")
	    	  showText = "004A770211030034";
	      else  if(showText == "ef010005")
	    	  showText = "004A770211030035";
	      
        option.appendChild(document.createTextNode(showText));
		select.appendChild(option);//向select增加option 
		//select.options[x].disabled = true;
	}
	//select.length = 3;//设置只能选择一个
	if(devArray.length > 0)
	 select.options[0].selected = true;//设置默认选中第一个
	 else{
		 var option = document.createElement("option");
		 option.setAttribute("value",-1);//设置option属性值
		 option.appendChild(document.createTextNode("您的名下无设备"));
		 select.appendChild(option);//向select增加option 
	}
	select.onclick = function(){
		//select.options[0].selected = true;
		var totalEui=0;
		var regEui=0;
		$.ajaxSetup({
		  async: false
		  });
	$.post("../GetRegisterDevices.do", {type: "ALL", usrname: "<%=sName%>"}, function(data){
			devArray = eval("(" + data + ")");
			totalEui = devArray.length;
			pointArray = new Array();
			overlayArray = new Array();
			for(var devNumber=0; devNumber<devArray.length; devNumber++)
			{
				pointArray.push(new Array());
				//devStatus[devNumber] = [{'val':devArray[devNumber]},{'val':'未知'},{'val':'未知'},{'val':'未知'},{'val':'未知'},{'val':'未知'}];
			}

		});
		$.post("../GetRegisterDevices.do", {type: "REG", usrname: "<%=sName%>"}, function(data){
			 var obj = eval("(" + data + ")");
			 regEui = obj.length;
			 /* if(obj.length == 0)
			 {
				for(var index = 0; index < devArray.length; index++)
			 	{
					 if(select.options[index+1].disabled == false)
				 		select.options[index+1].disabled = true;
			 	}
			 }
			 else
			 {
				for(var index=0; index < devArray.length; index++)
				{
					 var objIndex;
					 for(objIndex = 0; objIndex < obj.length; objIndex++)
					 {
						if(devArray[index] == obj[objIndex])
						{
							select.options[index+1].disabled = false;
							break;
						}
					 }
					 if(objIndex == obj.length)
						 select.options[index+1].disabled = true;
		        }
			} */
		});
			 	
		document.getElementById("devinfo").innerHTML = "终端信息：设备数量【" + totalEui + "】, 在线数量【" + regEui + "】, 离线数量【" + (totalEui-regEui) + "】";
	};
	select.onchange = function(){
		hbutton.setAttribute("value", "隐藏其它");
		for(i in markers)
       	{
       		markers[i].show();
       		if(overlayArray[i] != null)
       		  overlayArray[i].show();
       	}
	};
	//select.setAttribute("class", "btn-warning");
	li1.appendChild(select);	
	uList.appendChild(li1); 

//开始定位	
	/* li2 = document.createElement("li");
	
	lbutton = document.createElement("input");
	lbutton.setAttribute("type", "button");
	lbutton.setAttribute("value", "定位");
	lbutton.style.padding = "2px";
	lbutton.setAttribute("class", "btn btn-info");
	lbutton.onmouseover = function(){
		showTips('二级定位描述',40,180);
	}
	li2.onmouseout = function(){
		hideTips();
	}
	lbutton.onclick = function(){
		if(select.value > 0)
		{
			var req = {
					// usrname: inputName,
					 DEVEUI: devArray[select.value]
					};
			$.ajax(
					{
						async: false,
						type: "POST",
						url: "../DevPositionReq.do",
						data: req,
						success: function(data) {
						    var status = eval("("+data+")");
						    if(status != "")
						    	alert(status);
						   }		
			       });
			
		}
		else
		{
			alert("请选择设备!");
		}
	};

	li2.appendChild(lbutton);
	
	uList.appendChild(li2); */

//查找设备	
	li3 = document.createElement("li");
	button = document.createElement("input");
	button.setAttribute("type", "button");
	button.setAttribute("value", "查找设备");
	button.style.padding = "2px";
	button.setAttribute("class", "btn btn-info");
	button.onmouseover = function(){
		showTips('查找选择的设备，并显示在地图中间。',40,260);
	}
	button.onmouseout = function(){
		hideTips();
	}
	button.onclick = function(){
		if(select.value >= 0)
		{
			if(ctrPoint[select.value] == null)
				alert("未查询到此设备的数据,请先定位,如已定位请稍候!");
			else
			   bm.setCenter(ctrPoint[select.value]);
		}
		else
		{
			alert("请选择设备!");
		}
	};
	
		
	li3.appendChild(button);
	uList.appendChild(li3);
	//隐藏/显示终端
	li31 = document.createElement("li");
	
	hbutton = document.createElement("input");
	hbutton.setAttribute("type", "button");
	hbutton.setAttribute("value", "隐藏其它");
	hbutton.style.padding = "2px";
	hbutton.setAttribute("class", "btn btn-info");
	hbutton.onmouseover = function(){
		showTips('设备显示叠加时，隐藏其它设备。',40,330);
	}
	hbutton.onmouseout = function(){
		hideTips();
	}
	hbutton.onclick = function(){
		if(select.value >= 0)
		{
			if(hbutton.getAttribute("value") == "隐藏其它")			
			{					
				hbutton.setAttribute("value", "显示其它");
							
				for(i in markers)
		       	{
		       		if(i != select.value)
		       		{
		       			markers[i].hide();
		       			if(overlayArray[i] != null)
		       				overlayArray[i].hide();
		       		}	
		       	}
			}
			else
			{
				hbutton.setAttribute("value", "隐藏其它");
				for(i in markers)
		       	{
		       		if(i != select.value)
		       		{
		       			markers[i].show();
		       			if(overlayArray[i] != null)
		       				overlayArray[i].show();
		       		}	
		       	}
			}
		}
		else
		{
			alert("请选择设备!");
		}
	}//function
	li31.appendChild(hbutton);
	
	uList.appendChild(li31);
//轨迹选择	
	li4 = document.createElement("li");
	addEclass(li4, "dropdown");
	
	tbutton = document.createElement("input");
	tbutton.setAttribute("type", "button");
	tbutton.setAttribute("value", "轨迹选择");
	tbutton.style.padding = "2px";
	tbutton.setAttribute("class", "btn btn-info dropdown-toggle");
	tbutton.setAttribute("data-toggle", "dropdown");
	tbutton.onmouseover = function(){
		showTips('点击实时轨迹可在当前页面显示设备[室外]运动路线。',40,430);
	}
	tbutton.onmouseout = function(){
		hideTips();
	}

	b1 = document.createElement("b");
	addEclass(b1, "caret");
	tbutton.appendChild(b1);
	
	uList2 = document.createElement("ul");
	addEclass(uList2, "dropdown-menu");
	
	u2li1 = document.createElement("li");	
	addChi("实时轨迹",u2li1,"a","rtime");
	uList2.appendChild(u2li1);
	
	$(document).on("click","#rtime",function(){
		
		if(select.value >= 0){
			
			drawLine[select.value] = 1;
			
			drawPolyline(select.value);			
	}
	else{
		alert("请选择设备!");
	}
	
	});

	u2li2 = document.createElement("li");
	addChi("历史轨迹",u2li2,"a","history");
	uList2.appendChild(u2li2);

	$(document).on("click","#history",function(){
		window.location.href="../tempalte/manager.html?service_id=118042&ak=SLlCUHnkdc580INpOiI9wRsbi8KYRKUW&owner=<%=sName%>";
	});

	li4.appendChild(tbutton);	
	li4.appendChild(uList2);		
	uList.appendChild(li4);
//显示Beacon分布
<%-- 	li5 = document.createElement("li");
	
	lbutton = document.createElement("input");
	lbutton.setAttribute("type", "button");
	lbutton.setAttribute("value", "显示信标");
	lbutton.style.padding = "2px";
	lbutton.setAttribute("class", "btn btn-info");
	lbutton.onclick = function(){

				if(lbutton.getAttribute("value") == "显示信标")			
				{
					$.get("../asset/beacon", {usrname: "<%=sName%>"}, function(data){
						var result = eval("(" + data + ")");
		       			if(result.status == "0")
		       			{
		       				var beaconlist = result.list;
		       				var i;
							lbutton.setAttribute("value", "隐藏信标");
							
		       				for(i in beaconlist)
		       				{
		       					var myIcon = new BMap.Icon("../images/flag.ico", new BMap.Size(32,32));
		       					var newpoint = new BMap.Point(beaconlist[i].longi, beaconlist[i].lati);
		       					beaconmarkers[i] = new BMap.Marker(newpoint,{icon:myIcon});  // 创建标注
		       					beaconmarkers[i].setLabel(new BMap.Label(beaconlist[i].major+","+beaconlist[i].minor,{offset:new BMap.Size(20,-10)}));
		       					//marker1.enableDragging();
		       					//marker1.setLabel(new BMap.Label(punchPoint.name,{offset:new BMap.Size(20,-10)}));
		       					//marker1.addEventListener("dragend", dragendPunch);
		       					map.addOverlay(beaconmarkers[i]);
		       					map.centerAndZoom(newpoint, 19);
		       					//addPunchMenu(marker1);
		       				}
		       			}
		       			else
		       			{
		       				alert("操作失败:" + result.message);
		       			}
					});
				}
				else
				{
       				for(i in beaconmarkers)
       				{
       					map.removeOverlay(beaconmarkers[i]); 
       					lbutton.setAttribute("value", "显示信标");
       					//addPunchMenu(marker1);
       				}
				}
			}//function

	li5.appendChild(lbutton);
	
	uList.appendChild(li5); --%>

// 全屏模式
	li6 = document.createElement("li");
	var ebutton = document.createElement("input");
	ebutton.setAttribute("type", "button");
	ebutton.style.padding = "2px";
	var screenType = "<%=sType%>";
	if(screenType == "big")
	   ebutton.setAttribute("value", "退出全屏");
	else
	   ebutton.setAttribute("value", "全屏")
	ebutton.setAttribute("class", "btn btn-info");
	ebutton.onclick = function(){
		if(ebutton.getAttribute("value") == "全屏")
		 {		
			//window.location.href = "loraDemo.jsp?usrname=<%=sName%>&type=big";
			window.open('Area_loraDemo.jsp?usrname=<%=sName%>&type=big','_blank','fullscreen=1');
		 }
		else
			{
			window.location.href="javascript:self.close()";
			}
	};
  
	li6.appendChild(ebutton);
	uList.appendChild(li6);
	
	navi.appendChild(uList);
	div.appendChild(navi);
	
// 导航栏添加结束	

	

	 // 绑定事件，点击一次放大两级    
   
	 // 添加DOM元素到地图中   
	map.getContainer().appendChild(div);    
	 // 将DOM元素返回  
	return div; 
}
function init()
{
	if("<%=sName%>" == "huawei"){
		$(".menu-expanded").css('display','none');
		var div = document.getElementById("allmap");
		
		div.setAttribute("class", "huawei");
	     
	    var childDiv = document.createElement("div");
	    childDiv.setAttribute("class", "childDiv");
	　　　　
	    document.body.appendChild(childDiv);
	    
	    $('.childDiv').on('click', function() {
	    	$(".guest-back-div").css("display","block");
	    	$("body").css("overflow-y","hidden");
	    }); 

	}else{
	var xxL = 118.80290300615; //Lansitec
	var yyL = 31.982098073871; 

//GPS坐标
var gpsPoint = new BMap.Point(xxL,yyL); 

//地图初始化
bm = new BMap.Map("allmap",{enableMapClick:false}); 
convertor = new BMap.Convertor();
bm.centerAndZoom(gpsPoint, 14); 
bm.addControl(new BMap.MapTypeControl({anchor: BMAP_ANCHOR_BOTTOM_RIGHT}));
bm.addControl(new BMap.NavigationControl());
bm.addControl(new BMap.ScaleControl());
bm.addControl(new DeviceInfoControl());
bm.addControl(new DevSerControl());

//bm.setCurrentCity("南京");
bm.enableScrollWheelZoom(true);

function myFun(result){
	var cityName = result.name;
	var address = cityName;
	
	if(null != devArray[0])
	{
	$.ajaxSetup({
		  async: false
		  });
		$.get("../DeviceListMgr.do?deveui=" + devArray[0], function(data){
			if(null != data && data != "");
				address = data;
		});
	}
	var myGeo = new BMap.Geocoder();
	myGeo.getPoint(address, function(point){
		if (point) {
			var xxP = point.lng;
			var yyP = point.lat;
			var lstX = 0;
			var lstY = 0;
			var onlineNum = 0;
			$.ajaxSetup({
				  async: true
				  });
			$.post("../GetRegisterDevices.do", {type: "POS", usrname: "<%=sName%>"}, function(data){
				var result = eval("(" + data + ")");
				
				for(var pIndex=0; pIndex<result.length;pIndex++)
			{
				var tPoint = new BMap.Point(xxP+ 0.01*pIndex, yyP + 0.01*pIndex);
				var X = 0;
				var Y = 0;
				
				var devSta;
				ctrPoint[pIndex] = tPoint;
					devStatus[pIndex] = [{'val':result[pIndex].deveui},{'val':'未知'},{'val':'未知'},{'val':'未知'},{'val':'未知'},{'val':'未知'}];

					if(result[pIndex].status == "REG")
					{
						devSta = "[就绪]";
						onlineNum++;
					}
					else
						devSta = "[离线]";
					
					if(result[pIndex].X != 0 && result[pIndex].Y != 0)
					{
/* 						if(result[pIndex].X > 1000)//百度室内平面坐标
						{
					       	var projection = new BMap.MercatorProjection();
					       	var point = projection.pointToLngLat(new BMap.Pixel(result[pIndex].X,result[pIndex].Y));
					       	
							X = point.lng;
							Y = point.lat;	
						}	 */					
						if(result[pIndex].X > 70 && result[pIndex].X < 180)
						{
							X = result[pIndex].X;
					    	Y = result[pIndex].Y;
						}

					    lstX = X;
					    lstY = Y;
					}
					
					if(result[pIndex].rssi != 0)
					{
					    devStatus[pIndex][1].val = result[pIndex].vib;
					    devStatus[pIndex][2].val = result[pIndex].usb;
					    devStatus[pIndex][3].val = result[pIndex].voltage;
					    devStatus[pIndex][4].val = result[pIndex].rssi;
					    devStatus[pIndex][5].val = result[pIndex].time;
					}
				
				if(X != 0 && Y != 0)
				{
					var newpoint = new BMap.Point(X,Y);
					markers[pIndex] = new BMap.Marker(newpoint);
					ctrPoint[pIndex] = newpoint;//当前位置非默认位置，为最后定位的位置
				}
				else
				{
					markers[pIndex] = new BMap.Marker(tPoint);
				}
					
					labels[pIndex] = new BMap.Label(result[pIndex].deveui+devSta,{offset:new BMap.Size(20,-10)}); 
				markers[pIndex].setLabel(labels[pIndex]); //添加GPS标注
					markers[pIndex].thatIndex = pIndex;
					markers[pIndex].addEventListener("click", function(e){
						console.log(e.currentTarget.thatIndex)
						$('.menu').addClass('over');
					    $('.main-menu').addClass('overmain'); // 开启侧边信息窗
					    deleteTable();
					    addTable(trName,devStatus[e.currentTarget.thatIndex]);
					    curSelIndex = e.currentTarget.thatIndex;
						});
				bm.addOverlay(markers[pIndex]);
			}
			document.getElementById("devinfo").innerHTML = "终端信息：设备数量【" + devArray.length + "】, 在线数量【" + onlineNum + "】, 离线数量【" + (devArray.length-onlineNum) + "】";
			});
			$.get("../MapManager.do", {type: "pos", name: "<%=sName%>"}, function(data){
				var result = eval("(" + data + ")");
				var leftupPointX = Number(result.indoorX);
				var leftupPointY = Number(result.indoorY);
				if(leftupPointX > 0.1)
				{
				     var polygon = new BMap.Polygon([ 
                          new BMap.Point(leftupPointX, leftupPointY),    
                          new BMap.Point(leftupPointX + 0.0005, leftupPointY),
                          new BMap.Point(leftupPointX + 0.0005, leftupPointY - 0.0004),    
                          new BMap.Point(leftupPointX, leftupPointY - 0.0004),                         			       
						],    
						{strokeColor:"blue", strokeWeight:2, strokeOpacity:0.5, strokeStyle:"dashed", fillColor:"blue", fillOpacity:0.2});    
					bm.addOverlay(polygon);
					polygon.addEventListener("dblclick", function(e){
					    var zoom = 1;
					    if(result.zoom != undefined && result.zoom !="")
					    	zoom = Number(result.zoom);
						window.open('Area_IndoorMap.jsp?usrname=<%=sName%>&zoom='+ zoom,'_blank','fullscreen=1,location=0');
					});
					polygon.addEventListener("click", function(e){
						 var opts = {    
						    	width : 60,     // 信息窗口宽度    
						    	height: 10,     // 信息窗口高度    
						    	title : ""  // 信息窗口标题   
						   }    
					var infoWindow = new BMap.InfoWindow(result.indoorTitle, opts);  // 创建信息窗口对象    
					bm.openInfoWindow(infoWindow, new BMap.Point(leftupPointX, leftupPointY));      // 打开信息窗口
					});
					bm.centerAndZoom(new BMap.Point(leftupPointX, leftupPointY), 18);
				}
			else{
				if("<%=sName%>" == "yk")
		        {//特殊处理，在百度地图上加logo水印			    
			     ///////////////////////////////////////////////
			     function ComplexCustomOverlay(point, text, mouseoverText){
			         this._point = point;
			         this._text = text;
			         this._overText = mouseoverText;
			       }
			       ComplexCustomOverlay.prototype = new BMap.Overlay();
			       ComplexCustomOverlay.prototype.initialize = function(map){
			         this._map = map;
			         var div = this._div = document.createElement("div");
			         div.style.position = "absolute";
			         div.style.zIndex = BMap.Overlay.getZIndex(this._point.lat);
			         div.style.backgroundColor = "#EE5D5B";
			         div.style.border = "1px solid #BC3B3A";
			         div.style.color = "white";
			         div.style.width = "150px";
			         div.style.height = "192px";
			         div.style.padding = "0px";
			         div.style.lineHeight = "0px";
			         div.style.whiteSpace = "nowrap";
			       //  div.style.MozUserSelect = "none";
			         div.style.fontSize = "12px";
			         div.style.background = "url(../images/bdtd.png) no-repeat";
			        
			        var infoWindow;
			         div.onmouseover = function(){
			        	 this.style.cursor = "crosshair";
			        	 var opts = {    
	 			    			 width : 60,     // 信息窗口宽度    
	 			    			 height: 10,     // 信息窗口高度    
	 			    			 title : ""  // 信息窗口标题   
	 			    			}    
	 			    		    infoWindow = new BMap.InfoWindow("兖矿集团室内定位展示", opts);  // 创建信息窗口对象    
	 			    			bm.openInfoWindow(infoWindow, new BMap.Point(116.95482,35.437655));      // 打开信息窗口
			         }

			         div.onmouseout = function(){
			           this.style.cursor = "pointer";
			           infoWindow.close();
			         }
					
			         div.onclick = function(){
					     	window.open('Area_IndoorMap.jsp?usrname=<%=sName%>','_blank','fullscreen=1,location=0'); 
			         }
			         bm.getPanes().labelPane.appendChild(div);
			         
			         return div;
			       }
			       ComplexCustomOverlay.prototype.draw = function(){
			         var map = this._map;
			         var pixel = map.pointToOverlayPixel(this._point);
			         this._div.style.left = pixel.x  + "px";
			         this._div.style.top  = pixel.y  + "px";
			       }
			           
			       var myCompOverlay = new ComplexCustomOverlay(new BMap.Point(116.95482,35.437655), "兖矿集团","室内定位");

			       bm.addOverlay(myCompOverlay);
			     
			     //////////////////////////////////////////////
			    bm.centerAndZoom(new BMap.Point(116.95482,35.437655), 19);
					//bm.setCenter(new BMap.Point(118.79253, 31.98120));
		    }
				else if("<%=sName%>" == "ztedemo")
			    {//特殊处理，在百度地图上做室内应用展示
				     var polygon = new BMap.Polygon([
				         				       new BMap.Point(120.439462,31.326889),
				         				       new BMap.Point(120.439962,31.326889),
				         				       new BMap.Point(120.439962,31.326489),  
				         				       new BMap.Point(120.439462,31.326489),	       
				         				    ],    
				     {strokeColor:"blue", strokeWeight:1, strokeOpacity:0.2, strokeStyle:"dashed", fillColor:"blue", fillOpacity:0.2});    
				     bm.addOverlay(polygon);
					     polygon.addEventListener("mouseover", function(e){
					    	 var opts = {    
					    			 width : 60,     // 信息窗口宽度    
					    			 height: 20,     // 信息窗口高度    
					    			 title : ""  // 信息窗口标题   
					    			}    
					    			var infoWindow = new BMap.InfoWindow("ZTE定位测试区域", opts);  // 创建信息窗口对象    
					    			bm.openInfoWindow(infoWindow, new BMap.Point(120.439462,31.326889));      // 打开信息窗口
						    });

				    bm.centerAndZoom(new BMap.Point(120.439462,31.326889), 19);
				    
							
		       				for(var i=0 ;i < 2; i++)
		       				{
		       					var beaconNum = 0;
		       				    if(0==i)
		       				    	beaconNum = 24;
		       				    else 
		       				    	beaconNum = 31;
	                           var showTxt = "信标" + beaconNum;
	                           
		      	    	        var sw_lati = 31.326689; //beaconlist[i].lati ;
		      	    	        var sw_long = 120.439462 + 0.0005*i ;//beaconlist[i].longi;
			       					
			 	    	   		var newpoint = new BMap.Point(sw_long,sw_lati);
			 	    	   		
			 	    	   		var markers1 = new BMap.Marker(newpoint);
														
							   var label = new BMap.Label(showTxt,{offset:new BMap.Size(20,-10)}); 
							   markers1.setLabel(label); //添加GPS标注
							   bm.addOverlay(markers1);
			       			}
			
					//	});
					//bm.setCenter(new BMap.Point(118.80173, 31.98220));
			    }
			else
			{
				if(lstX != 0 && lstY != 0)
				{
					bm.centerAndZoom(new BMap.Point(lstX,lstY));
					bm.setCenter(new BMap.Point(lstX,lstY));
				}
				else
				{
					bm.centerAndZoom(point, 15);
					bm.setCenter(point);
				}
			}
		  }
		});
		}
	}, cityName);
}

}
var myCity = new BMap.LocalCity();
myCity.get(myFun);

//坐标转换完之后的回调函数
xx = new Array();
yy = new Array();
tt = new Array();

for(var devNumber=0; devNumber<devArray.length; devNumber++)
{
	pointArray[devNumber] = new Array();
}
}

//百度坐标处理函数
function translateCallback(point, time, index){
	var len = point.length;
	
	ctrPoint[index] = point[len-1];
	
	var p = new Array();
	for(var i=0; i<len ;i++)
	{
		var mp = {p1:point[i],p2:time[i]};
		p.push(mp);
	}
	
	var paLen = pointArray[index].length;
	if(paLen == 0 || time[0] >= pointArray[index][paLen-1].p2)
	{
		for(var i=0; i<p.length;i++)
			pointArray[index].push(p[i]);
	}
	else
	{
		for(var j=0; j<len; j++)
		{
			for(var i=0; i<paLen-1; i++)
			{
				if(p[j].p2 >= pointArray[index][paLen-2-i].p2)
				{
					//p.unshift(paLen-1-i, 0);
					pointArray[index].splice(paLen-1-i,0,p[j]);
					//Array.prototype.splice.apply(pointArray[index], p);
					break;
				}
				else if(i == paLen-2)
				{
					//p.unshift(0, 0);
					pointArray[index].splice(0,0,p[j]);
					//Array.prototype.splice.apply(pointArray[index], p);				
				}
			}
		}
	}
	//pointArray[curIndex].push(p);
	if(pointArray[index].length > 5000)
		pointArray[index].shift();
	drawPolyline(index);
	markers[index].setPosition(pointArray[index][pointArray[index].length-1].p1); 
	bm.setCenter(pointArray[index][pointArray[index].length-1].p1); 
	//alert("转化为百度坐标为："+point.points[0].lng +"," + point.points[0].lat); 
}

function closeSocket(){
	webSocket.close();
}

Date.prototype.format =function(format)
{
var o = {
"M+" : this.getMonth()+1, //month
"d+" : this.getDate(), //day
"h+" : this.getHours(), //hour
"m+" : this.getMinutes(), //minute
"s+" : this.getSeconds(), //second
"q+" : Math.floor((this.getMonth()+3)/3), //quarter
"S" : this.getMilliseconds() //millisecond
}
if(/(y+)/.test(format)) format=format.replace(RegExp.$1,
(this.getFullYear()+"").substr(4- RegExp.$1.length));
for(var k in o)if(new RegExp("("+ k +")").test(format))
format = format.replace(RegExp.$1,
RegExp.$1.length==1? o[k] :
("00"+ o[k]).substr((""+ o[k]).length));
return format;
}


/* 申请一个WebSocket对象，参数是需要连接的服务器端的地址 */ 
 var webSocketAddr = "<%=InetAddress.getLocalHost().getHostAddress()%>";
 var webSocket;
 if(webSocketAddr.indexOf("192.168") >= 0)
 	webSocket = new WebSocket("ws://127.0.0.1:8080/asset/NodeQueryWS.do"); 
 else if(webSocketAddr.indexOf("10.31.84.143") >= 0){
	 webSocket = new WebSocket("wss://dev.lansitec.com/asset/NodeQueryWS.do");
 }
 else{
	 webSocket = new WebSocket("ws://120.27.104.163/asset/NodeQueryWS.do");
}
 /* 如果出现连接、处理、接收、发送数据失败的时候就会触发onerror事件 */ 
 webSocket.onerror = function(event) { onError(event) }; 
 /* 当websocket创建成功时，即会触发onopen事件 */ 
 webSocket.onopen = function(event) { onOpen(event) }; 
 /* 当客户端收到服务端发来的消息时，会触发onmessage事件，参数evt.data中包含server传输过来的数据 */
 webSocket.onmessage = function(event) { onMessage(event) }; 
 /* 当客户端收到服务端发送的关闭连接的请求时，触发onclose事件 */ 
 webSocket.onclose = function(event) { onClose(event) };

var sContent =
		"<h4 style='margin:0 0 5px 0;padding:0.2em 0'><dt>白糖管理员信息</dt></h4>" + 
		"<div class='content' style='width:240px;'>" +
        "<img src='../images/xkf.jpg' width='130' height='104' title='许奎峰' class='right'/>" +
        "<dl id='dl'><dd> 姓名：许奎峰</dd><dd>性别：男</dd><dd>职务：仓库管理员</dd><dd> " +
        "管辖范围：1-16号仓库</dd><dd>所属区域：广西荣桂仓储钦州分公司</dd><dd>有效期：2018年5月20日</dd></dl>" +
        "</div>";
        
var infoWindow = new BMap.InfoWindow(sContent);


function markerChange(index)
{
	markers[index].addEventListener("click", (function(index){
		return function()
		{
			$('.menu').addClass('over');
	  		$('.main-menu').addClass('overmain'); // 开启侧边信息窗
	  		deleteTable();
	  		addTable(trName,devStatus[index]);
	  		if(devArray[index] == "004a770211030003" || devArray[index] == "004a770211030005")
	  		{
	  		   this.openInfoWindow(infoWindow);
	  		   //图片加载完毕重绘infowindow
	  		   var img = document.getElementById('imgDemo');
	  		   if(null != img)
	  		   {
	  			   document.getElementById('imgDemo').onload = function (){
	  			  	 infoWindow.redraw();   //防止在网速较慢，图片未加载时，生成的信息框高度比图片的总高度小，导致图片部分被隐藏
	  		   		};
	  		   }
	  		}
	  		curSelIndex = index;
		}
	})(index));	 
}
function onMessage(event) 
{ /*收到server的数据后,更新地图*/
	//document.getElementById("messages").innerHTML += event.data+ "\n"; 

    var obj = eval("(" + event.data + ")");	

	for(var index=0; index < devArray.length; index++)
	{  
		if(devArray[index] == obj.DevEUI && labels[index] != undefined)
		{	    
			if(obj.msgType == "REG") 
			{ 
				labels[index].setContent(devArray[index] + "[就绪]");
				markers[index].setLabel(labels[index]);
			}
			else if(obj.msgType == "HB") 
		    {
				if(obj.gps == "无GPS信号")
				{
					var labelValue = labels[index].content;
				    if(labelValue.indexOf("室内:") < 0)
						labels[index].setContent(devArray[index] + "[室内]");
		    	}
				else
					labels[index].setContent(devArray[index] + "[心跳]");
				markers[index].setLabel(labels[index]);
				
			    devStatus[index][0].val = obj.DevEUI;
			    if(obj.usrdata == undefined)
			        devStatus[index][1].val = obj.vib;
			    else
			    	devStatus[index][1].val = "行走" + obj.usrdata + "步";
			    
			    devStatus[index][2].val = obj.usb;
			    devStatus[index][3].val = obj.voltage;
			    devStatus[index][4].val = obj.rssi;
			    devStatus[index][5].val = new Date().format("MM-dd hh:mm:ss");
		    }
		    else if(obj.msgType == "LOC")
		    {
				xx[index] = obj.xGPS;
				yy[index] = obj.yGPS;
				tt[index] = obj.time;
				
		    	var pointA = new Array();
		    	var timeA = new Array();
		    	
		    	pointA.push(new BMap.Point(obj.xGPS, obj.yGPS));
		    	timeA.push(obj.time);
		    	labels[index].setContent(devArray[index] + "[" + "室外" + "]");
			    translateCallback(pointA, timeA, index);
		    }
		    else if(obj.msgType == "HIS")
		    {
		    	var GPSList = obj.GPS;
		    	var i = 0;
		    	var pointA = new Array();
		    	var timeA = new Array();
		    	
		    	for(i in GPSList)
		    	{
		    		pointA.push(new BMap.Point(GPSList[i].xGPS, GPSList[i].yGPS));
		    		timeA.push(GPSList[i].time);
		    	}

		    	translateCallback(pointA, timeA, index);
		    }
		    else if(obj.msgType == "INDOOR")
		    {
		    	if("<%=sName%>" == "winter")
		    	{
		    		if(obj.minor != undefined)
		    		{
		    	       labels[index].setContent(devArray[index] + "[室内]");
		    	       var myIcon = new BMap.Icon("https://api.map.baidu.com/img/markers.png", new BMap.Size(23, 25), {
                         offset: new BMap.Size(10, 25),
                         imageOffset: new BMap.Size(0, 0 - 10 * 25)
                        });
				       bm.removeOverlay(markers[index]);
				       var newPoint;
				       if(obj.minor == 15)
	      	    	        newPoint = new BMap.Point(120.803089, 31.27893);
     	    	        else{
							newPoint = new BMap.Point(120.803089 + 0.0005, 31.27893);
						}
		    	       markers[index] = new BMap.Marker(newPoint, {icon: myIcon});
		    	       markers[index].setLabel(labels[index]);
		    	       bm.addOverlay(markers[index]);		    	
		    		}
		    	}
		    	else if("<%=sName%>" == "ztedemo")
		    	{
		    		if(obj.minor != undefined)
		    		{
		    	       labels[index].setContent(devArray[index] + "[室内]");
		    	       var myIcon = new BMap.Icon("https://api.map.baidu.com/img/markers.png", new BMap.Size(23, 25), {
                         offset: new BMap.Size(10, 25),
                         imageOffset: new BMap.Size(0, 0 - 10 * 25)
                        });
				       bm.removeOverlay(markers[index]);
				       var newPoint;
				       if(obj.minor == 31)
	      	    	        newPoint = new BMap.Point(120.439462,31.326589);
     	    	        else{
							newPoint = new BMap.Point(120.439462 + 0.0005, 31.326589);
						}
				       ctrPoint[index] = newPoint;
		    	       markers[index] = new BMap.Marker(newPoint, {icon: myIcon});
		    	       markers[index].setLabel(labels[index]);
		    	       bm.addOverlay(markers[index]);		    	
		    		}
		    	}
		    	else{
		    		labels[index].setContent(devArray[index] + "[室内]");
					markers[index].setLabel(labels[index]);
		    	}
		    } 
			if(index == curSelIndex)
			{
				$('.menu').addClass('over');
				$('.main-menu').addClass('overmain'); // 开启侧边信息窗
				deleteTable();
				addTable(trName,devStatus[index]);
			}
			break;
		}
	}
} 

function onOpen(event) 
{   
	//document.getElementById("messages").innerHTML += "<%=sName%>";
	//document.getElementById("messages").innerHTML += "成功建立连接..."+ "\n"; 
	if("<%=sName%>" != "yk")
    {
	    webSocket.send("NME" + "<%=sName%>");
    }
	init();
} 
function onClose(event) 
{ 
	 //document.getElementById("messages").innerHTML += event.data+ "\n";
} 
function onError(event) 
{ 
	if(event.data != undefined)
		alert("连接数据服务器失败,请刷新页面重试!" + event.data);
	else
		alert("连接数据服务器失败,请刷新页面重试!");
} 
var count=0;
var int=self.setInterval("clock()",30000)
function clock()
{
	var nInline = 0;
	$.post("../GetRegisterDevices.do", {type: "REG", usrname: "<%=sName%>"}, function(data){
		 var obj = eval("(" + data + ")");
		 nInline = obj.length;
		 if(document.getElementById("devinfo") != null)
				document.getElementById("devinfo").innerHTML = "终端信息：设备数量【" + devArray.length + "】, 在线数量【" + nInline + "】, 离线数量【" + (devArray.length - nInline) + "】";			
	});
	
	webSocket.send("HBT");
	//test code
	/*
	drawLine[1] = 1;
	var pointA = new Array();
	var timeA = new Array();
    count++;
	pointA.push(new BMap.Point(118.3241+count*0.0005, 32.2351));
	pointA.push(new BMap.Point(118.3244+count*0.0005, 32.2354));
	timeA.push(12345-count*5);
	timeA.push(12348-count*5);
	
	translateCallback(pointA, timeA, 1);
	*/
}
</script>



</html>
