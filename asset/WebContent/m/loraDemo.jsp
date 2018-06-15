<%@ page contentType="text/html; charset=UTF-8" language="java"
	errorPage=""%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0;" />
<meta content="yes" name="apple-mobile-web-app-capable" />

<link rel="stylesheet" type="text/css"
	href="../css/jquery.datetimepicker.css" />
<link rel="stylesheet" type="text/css"
	href="../css/jquery.popuplayer.min.css" />
<script type="text/ecmascript" src="../js/jquery-3.1.0.min.js"></script>

<script type="text/javascript" src="../js/jquery.popuplayer.min.js"></script>
<script type="text/javascript" src="../js/jquery.datetimepicker.full.js"
	charset="UTF-8"></script>
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

#one, #six, #twelve, #oneday, #timepicker1, #timepicker2 {
	margin-bottom: 10px;
}

.navbar-nav {
	position: fixed;
	margin-top: 5px;
	left: 15px;
	top: 65%;
}

.navbar-nav>li {
	/*top:;*/
	margin-top: 5px;
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

.menu, .linee {
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

.table>tbody>tr>td, .table>tbody>tr>th, .table>tfoot>tr>td, .table>tfoot>tr>th,
	.table>thead>tr>td, .table>thead>tr>th {
	border-color: #5a5a5a;
	color: #003c16;
}

@
keyframes closetop { 0% {
	transform: translateY(5px) rotate(0deg);
}

25%
{
transform
:
 
translateY
(5px)
 
rotate
(0deg);

  
}
75%
{
transform
:
 
translateY
(5px)
 
rotate
(-45deg);

  
}
100%
{
transform
:
 
translateY
(5px)
 
rotate
(-45deg);

  
}
}
@
keyframes closebottom { 0% {
	transform: translateY(0px) rotate(0deg);
}

25%
{
transform
:
 
translateY
(-5px)
 
rotate
(0deg);

  
}
75%
{
transform
:
 
translateY
(-5px)
 
rotate
(45deg);

  
}
100%
{
transform
:
 
translateY
(-5px)
 
rotate
(45deg);

  
}
}
.overmain {
	visibility: visible;
	opacity: 1;
	transition: all 0.400s;
	transition-delay: 0.370s;
	width: 300px;
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
	//if(param.equals(sName))
	//{
	//}
	//else
	//	return;
	//System.out.println(sName + "+" + sType);
%>
<title>智能定位系统</title>
<script type="text/javascript"
	src="http://api.map.baidu.com/api?v=2.0&ak=T3esAl5i0NHA9SAEHF3dRyauqTazR2bf"></script>
</head>

<body onunload="closeSocket()">
	<div id="allmap"></div>

	<!-- 以下是侧边弹出信息框 -->
	<div class="menu-expanded">
		<div id="menu" class="menu" data-open=0></div>
		<div class="main-menu">
			<table class="table table-border" id="infotable"
				style="margin-bottom: 10px;">
				<caption style="padding-left: 90px;; width: 100%; color: #011f0c;">设备信息</caption>
			</table>
			<button class="btn btn-info close-equip-message"
				style="padding: 2px; width: 60px; margin-left: 90px;">关闭</button>
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

var devArray = new Array();
var pointArray = null;
var overlayArray = null;
var drawLine = new Array();
var markergps;
var ctrPoint = new Array();
var markers = new Array();
var labels = new Array();
var select = null;
var lbutton = null;
//var curIndex = null;
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
    	if(0==index)
    		color="red";
    	else if(1==index)
    		color="blue";
    	else
    		color="green";
    	overlayArray[index] = new BMap.Polyline(pointArray[index], {  
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
	select.style.padding = "4px";
	for(var x = 0;x<devArray.length + 1;x++){
		var option = document.createElement("option");
		option.setAttribute("value",x);//设置option属性值
		if(0 == x)
			option.appendChild(document.createTextNode("请选择在线设备"));
		else
			option.appendChild(document.createTextNode(devArray[x-1]));
		select.appendChild(option);//向select增加option 
		select.options[x].disabled = true;
	}
	//select.length = 3;//设置只能选择一个
	select.options[0].selected = true;//设置默认选中第一个
	select.onclick = function(){
		//select.options[0].selected = true;http://localhost:8080/asset/index.jsp
		$.post("../GetRegisterDevices.do", {type: "REG", usrname: "<%=sName%>"}, function(data){
			 var obj = eval("(" + data + ")");
			 if(obj.length == 0)
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
			 	
			}
		});
	};
	
	select.onchange=function(){
		$.post("../GetRegisterDevices.do", {type: "STE", usrname: devArray[select.value - 1]}, function(data){
			var result = eval("(" + data + ")");

			if(result == "LOCATE" || result == "OFFLINE" || result == "DATARCV")
			{
				lbutton.setAttribute("value", "停止定位");
			}
			else if(result == "UNLOCATE" || result == "REG")
			{
				lbutton.setAttribute("value", "开始定位");
			}
			else
				alert("unknow dev status received: " + curBValue);
		});
	};

	//select.setAttribute("class", "btn-warning");
	li1.appendChild(select);	
	uList.appendChild(li1); 

//开始定位	
	li2 = document.createElement("li");
	
	lbutton = document.createElement("input");
	lbutton.setAttribute("type", "button");
	lbutton.setAttribute("value", "开始定位");
	lbutton.style.padding = "4px";
	
	lbutton.setAttribute("class", "btn btn-info");
	lbutton.onclick = function(){
		if(select.value > 0)
		{
			$.post("../GetRegisterDevices.do", {type: "STE", usrname: devArray[select.value - 1]}, function(data){
				var result = eval("(" + data + ")");
				var curBValue = lbutton.getAttribute("value");
				
				if(curBValue == "开始定位")
				{
					if(result == "OFFLINE")
					{
						alert("此设备处于离线状态,请稍候重试!");
					}
					else if(result == "LOCATE" || result == "DATARCV")
					{
						alert("此设备已处于定位状态!");
						lbutton.setAttribute("value", "停止定位");
					}
					else if(result == "UNLOCATE" || result == "REG")
					{
						webSocket.send("BLO"+devArray[select.value - 1]);
						lbutton.setAttribute("value", "停止定位");
						alert("定位命令已发出,请稍候!");
					}
				}
				else if(curBValue == "停止定位")
				{
					if(result == "OFFLINE")
					{
						alert("此设备处于离线状态,请稍候重试!");
					}
					else if(result == "UNLOCATE")
					{
						alert("此设备已处于取消定位状态!");
						lbutton.setAttribute("value","开始定位");
					}
					else if(result == "LOCATE" || result == "DATARCV" || result == "REG")
					{
						webSocket.send("SLO"+devArray[select.value - 1]);
						lbutton.setAttribute("value", "开始定位");
						alert("停止定位命令已发出,请稍候!");
					}

				}
			});
			
		}
		else
		{
			alert("请选择设备!");
		}
	};

	li2.appendChild(lbutton);
	
	uList.appendChild(li2);

//查找设备	
	li3 = document.createElement("li");
	button = document.createElement("input");
	button.setAttribute("type", "button");
	button.setAttribute("value", "查找设备");
	button.style.padding = "4px";
	button.setAttribute("class", "btn btn-info");
	button.onclick = function(){
		if(select.value > 0)
		{
			if(ctrPoint[select.value-1] == null)
				alert("未查询到此设备的数据,请先定位,如已定位请稍候!");
			else
			   bm.setCenter(ctrPoint[select.value - 1]);
		}
		else
		{
			alert("请选择设备!");
		}
	};
	
		
	li3.appendChild(button);
	uList.appendChild(li3);

//轨迹选择	
	li4 = document.createElement("li");
	addEclass(li4, "dropdown");
	
	tbutton = document.createElement("input");
	tbutton.setAttribute("type", "button");
	tbutton.setAttribute("value", "轨迹选择");
	tbutton.style.padding = "4px";
	tbutton.setAttribute("class", "btn btn-info dropdown-toggle");
	tbutton.setAttribute("data-toggle", "dropdown");
	

	b1 = document.createElement("b");
	addEclass(b1, "caret");
	tbutton.appendChild(b1);
	
	uList2 = document.createElement("ul");
	addEclass(uList2, "dropdown-menu");
	
	u2li1 = document.createElement("li");	
	addChi("实时轨迹",u2li1,"a","rtime");
	uList2.appendChild(u2li1);
	
	$(document).on("click","#rtime",function(){
		
		if(select.value > 0){
			
			drawLine[select.value-1] = 1;
			
			drawPolyline(select.value-1);			
	}
	else{
		alert("请选择设备!");
	}

	});

	u2li2 = document.createElement("li");
	addChi("历史轨迹",u2li2,"a","history");
	uList2.appendChild(u2li2);

	$(document).on("click","#history",function(){
		//var id = "00:4a:77:00:66:ff:fe:21";
		//window.location.href="tarck/trackhistory.html?dev="+id;
		
		//$("#tracetime").show(500);
	 	if(select.value > 0){
			var id = devArray[select.value - 1]; 
	 		window.location.href="tarck/trackhistory.html?usr=<%=sName%>&dev="+id;	
			
	} 
	 else{
		alert("请选择设备!");
	} 

	});

	li4.appendChild(tbutton);
	li4.appendChild(uList2);		
	uList.appendChild(li4);

// 全屏模式

	li5 = document.createElement("li");
	var ebutton = document.createElement("input");
	ebutton.setAttribute("type", "button");
	ebutton.style.padding = "4px";
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
			window.open('loraDemo.jsp?usrname=<%=sName%>&type=big','_blank','fullscreen=1');
		 }
		else
			{
			window.location.href="javascript:self.close()";
			}
	};
  
	li5.appendChild(ebutton);
	uList.appendChild(li5);
	
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
	$.ajaxSetup({
		  async: false
		  });
	$.get("../DeviceListMgr.do?deveui=" + devArray[0], function(data){
			if(data != "");
				address = data;
		});
	var myGeo = new BMap.Geocoder();
	myGeo.getPoint(address, function(point){
		if (point) {
			var xxP = point.lng;
			var yyP = point.lat;
			var lstX = 0;
			var lstY = 0;
			
			for(var pIndex=0; pIndex<devArray.length;pIndex++)
			{
				var tPoint = new BMap.Point(xxP, yyP + 0.01*pIndex);
				var X = 0;
				var Y = 0;
				
				var devSta;
				ctrPoint[pIndex] = tPoint;
				devStatus[pIndex] = [{'val':devArray[pIndex]},{'val':'未知'},{'val':'未知'},{'val':'未知'},{'val':'未知'},{'val':'未知'}];

				$.post("../GetRegisterDevices.do", {type: "STE", usrname: devArray[pIndex]}, function(data){
					var result = eval("(" + data + ")");

					if(result == "LOCATE")
						devSta = "(定位中)";
					else if(result == "OFFLINE")
						devSta = "(离线)";
					else if(result == "DATARCV")
						devSta = "(正常)";
					else if(result == "UNLOCATE")
						devSta = "(停止定位)";
					else if(result == "REG")
						devSta = "(就绪)";
					else
						devSta = "";
				});
				
				$.post("../GetRegisterDevices.do", {type: "POS", usrname: devArray[pIndex]}, function(data){
					var result = eval("(" + data + ")");
					
					if(result.X != 0 && result.Y != 0)
					{
						X = result.X;
					    Y = result.Y;
					    lstX = X;
					    lstY = Y;
					}					
				});
				
				if(X != 0 && Y != 0)
				{
					markers[pIndex] = new BMap.Marker(new BMap.Point(X,Y));
				}
				else
				{
					markers[pIndex] = new BMap.Marker(tPoint);
				}
				labels[pIndex] = new BMap.Label(devArray[pIndex]+devSta,{offset:new BMap.Size(20,-10)}); 
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
			if(lstX != 0 && lstY != 0)
			{
				bm.centerAndZoom(new BMap.Point(X,Y));
			}
			else
			{
				bm.centerAndZoom(point, 13);
			}
		}
	}, cityName);
}
var myCity = new BMap.LocalCity();
myCity.get(myFun);

//坐标转换完之后的回调函数
xx = new Array();
yy = new Array();
}

function translateCallback(point,index){
	var curIndex = index;
	//bm.removeOverlay(labels[curIndex]);
	//bm.removeOverlay(markers[curIndex]);
	markers[curIndex].setPosition(point); 
	//bm.addOverlay(markers[curIndex]); 

	//labels[curIndex] = new BMap.Label(devArray[curIndex]+"(正常)",{offset:new BMap.Size(20,-10)}); 
	labels[curIndex].setContent(devArray[curIndex]+"(正常)");
	//markers[curIndex].setLabel(labels[curIndex]); //添加百度label
	ctrPoint[curIndex] = point;
	pointArray[curIndex].push(point);
	if(pointArray[curIndex].length > 10000)
	pointArray[curIndex].pop();
	drawPolyline(curIndex);
	bm.setCenter(point); 
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
 //120.27.104.163  127.0.0.1:8080
 var webSocket = new WebSocket("ws://120.27.104.163/asset/NodeQueryWS.do"); 
 /* 如果出现连接、处理、接收、发送数据失败的时候就会触发onerror事件 */ 
 webSocket.onerror = function(event) { onError(event) }; 
 /* 当websocket创建成功时，即会触发onopen事件 */ 
 webSocket.onopen = function(event) { onOpen(event) }; 
 /* 当客户端收到服务端发来的消息时，会触发onmessage事件，参数evt.data中包含server传输过来的数据 */
 webSocket.onmessage = function(event) { onMessage(event) }; 
 /* 当客户端收到服务端发送的关闭连接的请求时，触发onclose事件 */ 
 webSocket.onclose = function(event) { onClose(event) };

function onMessage(event) 
{ /*收到server的数据后,更新地图*/
	//document.getElementById("messages").innerHTML += event.data+ "\n"; 

    var obj = eval("(" + event.data + ")");	

	for(var index=0; index < devArray.length; index++)
	{  
		if(devArray[index] == obj.DevEUI)
		{	    
			if(obj.msgType == "REG") //0x21
			{ 
				labels[index].setContent(devArray[index] + "(就绪)");
				markers[index].setLabel(labels[index]);
			}
			else if(obj.msgType == "HB") //0x12
		    {
				labels[index].setContent(devArray[index] + obj.gps);
				markers[index].setLabel(labels[index]);
				
			    devStatus[index][0].val = obj.DevEUI;
			    devStatus[index][1].val = obj.vib;
			    devStatus[index][2].val = obj.usb;
			    devStatus[index][3].val = obj.voltage;
			    devStatus[index][4].val = obj.rssi;
			    devStatus[index][5].val = new Date().format("MM-dd hh:mm:ss");
		    }
		    else if(obj.msgType == "LOC") //0x11
		    {
				xx[index] = obj.xGPS;
				yy[index] = obj.yGPS;
				
			    translateCallback(new BMap.Point(obj.xGPS, obj.yGPS), index);
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
	webSocket.send("NME" + "<%=sName%>");
	init();
} 
function onClose(event) 
{ 
	 //document.getElementById("messages").innerHTML += event.data+ "\n";
} 
function onError(event) 
{ 
	alert("连接数据服务器失败,请刷新页面重试!" + event.data);
} 

$.datetimepicker.setLocale('ch');
$('#timepicker1,#timepicker2').datetimepicker({
		
		format:'Y-m-d H:i',
		step:30
	});

var int=self.setInterval("clock()",30000)
function clock()
{
	var nInline = 0;
	$.post("../GetRegisterDevices.do", {type: "REG", usrname: "<%=sName%>"}, function(data){
		 var obj = eval("(" + data + ")");
		 nInline = obj.length;
	});
	
	if(document.getElementById("devinfo") != null)
		document.getElementById("devinfo").innerHTML = "终端信息：设备数量【" + devArray.length + "】, 在线数量【" + nInline + "】, 离线数量【" + (devArray.length - nInline) + "】";
}
</script>



</html>
