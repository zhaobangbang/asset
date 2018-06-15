<%@ page contentType="text/html; charset=UTF-8" language="java"	errorPage="" import="java.net.InetAddress"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="Expires" content="-1" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Cache-control" content="no-cache" />
<meta http-equiv="Cache" content="no-cache" />
<link rel="stylesheet" type="text/css" 	href="css/jquery.popuplayer.min.css" />
<script type="text/ecmascript" src="js/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="js/bootstrap.min.js"></script>
<script type="text/javascript" src="js/jquery.popuplayer.min.js"></script>
<link href="css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<link rel="SHORTCUT ICON" href="images/favicon.ico" />

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

.navbar-nav>li {
	margin-right: 5px;
	margin-top: 10px;
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
transform:
 translateY(5px)
 rotate(0deg); 
}
75%
{
transform:
 translateY(5px)
 rotate(-45deg); 
}
100%
{
transform:
 translateY(5px)
 rotate(-45deg);  
}
}
@
keyframes closebottom { 0% {
	transform: translateY(0px) rotate(0deg);
}

25%
{
transform:
 translateY(-5px)
 rotate(0deg); 
}
75%
{
transform:
 translateY(-5px)
 rotate(45deg);  
}
100%
{
transform:
 translateY(-5px)
 rotate(45deg);  
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
	String sName = (String)request.getParameter("usrname");
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
var tt = null;
var outArray = new Array();//室外型终端
var inArray = new Array();//室内型终端
var outinArray = new Array();//室内外型终端
var devArray = new Array();
var pointArray = null;
var overlayArray = null;
var drawLine = new Array();
var markergps;
var ctrPoint = new Array();
var markers = new Array();
var mapsPos = new Array();
var labels = new Array();
var select = null;
var lbutton = null;
var selectMap = null;
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

function uiinit(address, cityName){
	var myGeo = new BMap.Geocoder();
	myGeo.getPoint(address, function(point){
		if (point) {
			var xxP = point.lng;
			var yyP = point.lat;
			var lstX = 0;
			var lstY = 0;

			$.ajaxSetup({
				  async: true
				  });
			$.post("GetRegisterDevices.do", {type: "POS", usrname: "<%=sName%>"}, function(data){
				var result = eval("(" + data + ")");
				var regList = new Array();
				
				for(var pIndex=0; pIndex<result.length;pIndex++)
				{
					var tPoint = new BMap.Point(xxP, yyP + 0.01*pIndex);
					var X = 0;
					var Y = 0;
					
					var devSta;
					ctrPoint[pIndex] = tPoint;
					devStatus[pIndex] = [{'val':result[pIndex].deveui},{'val':'未知'},{'val':'未知'},{'val':'未知'},{'val':'未知'},{'val':'未知'}];
				
					if(result[pIndex].status == "REG")
					{
						devSta = "[就绪]";
						regList.push(result[pIndex].deveui);
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
					
					var myIcon = new BMap.Icon("images/asset.png", new BMap.Size(30,30));
					if(X != 0 && Y != 0)
					{
						var newpoint = new BMap.Point(X,Y);
						
						markers[pIndex] = new BMap.Marker(newpoint, {icon: myIcon});
						ctrPoint[pIndex] = newpoint;//当前位置非默认位置，为最后定位的位置
					}
					else
					{
						markers[pIndex] = new BMap.Marker(tPoint,{icon:myIcon});
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
				
				clock();//获取终端在线信息
				updateDeviceInfo(regList);
			});
			if(null != selectMap && selectMap != undefined){
			        $.get("MapManager.do", {name: "<%=sName%>", mapid:"ALL"}, function(data){
					var result = eval("(" + data + ")");
	                var mapList = new Array();
	                for(var index=0; index < result.length; index++)
	                {
	                	var option = document.createElement("option");
	    				option.setAttribute("value",index);//设置option属性值
	
	    				option.appendChild(document.createTextNode(result[index].mapid));
	    				
	    				selectMap.appendChild(option);//向select增加option 
						
						var leftupPointX = Number(result[index].indoorX);
						var leftupPointY = Number(result[index].indoorY);
						mapsPos[index] = {"mapid":result[index].mapid, "X":leftupPointX,"Y":leftupPointY};
						
						if(leftupPointX != 0 && leftupPointY != 0)
						{
						     var polygon = new BMap.Polygon([ 
		                          new BMap.Point(leftupPointX, leftupPointY),    
		                          new BMap.Point(leftupPointX + 0.001, leftupPointY),
		                          new BMap.Point(leftupPointX + 0.001, leftupPointY - 0.0008),    
		                          new BMap.Point(leftupPointX, leftupPointY - 0.0008),                         			       
								],    
								{strokeColor:"blue", strokeWeight:2, strokeOpacity:0.5, strokeStyle:"dashed", fillColor:"blue", fillOpacity:0.2});    
							bm.addOverlay(polygon);
							
							var marker = new BMap.Marker(new BMap.Point(leftupPointX+0.0004, leftupPointY-0.0004));
							bm.addOverlay(marker);
							var label = new BMap.Label("室内地图编号:" + result[index].mapid, {offset:new BMap.Size(20,-10)});
							marker.setLabel(label);
							
							polygon.pIndex = index;
							polygon.addEventListener("click", function(e){
							    var zoom = 1;
							    if(result[e.currentTarget.pIndex].zoom != undefined && result[e.currentTarget.pIndex].zoom !="")
							    	zoom = Number(result[e.currentTarget.pIndex].zoom);
							    var mapid = result[e.currentTarget.pIndex].mapid;
							    window.open("jsp/Area_IndoorMap.jsp?usrname=<%=sName%>"  + "&mapid=" + mapid  + '&zoom='+ zoom,'_blank','fullscreen=1,location=0');
							});
							marker.pIndex = index;
							marker.leftupPointX = leftupPointX;
							marker.leftupPointY = leftupPointY;
							marker.pX = leftupPointX+0.0004;
							marker.pY = leftupPointY-0.0004;
							marker.addEventListener("click", function(e){
								 var opts = {    
								    	width : 60,     // 信息窗口宽度    
								    	height: 5,     // 信息窗口高度    
								    	title : ""  // 信息窗口标题   
								   }    
							       var infoWindow = new BMap.InfoWindow(result[e.currentTarget.pIndex].indoorTitle, opts);  // 创建信息窗口对象    
							       var posPoint = new BMap.Point(e.currentTarget.pX, e.currentTarget.pY);
							       bm.openInfoWindow(infoWindow, posPoint);      // 打开信息窗口
							       bm.centerAndZoom(new BMap.Point(e.currentTarget.leftupPointX, e.currentTarget.leftupPointY), 18);
							});
							if(0 == index)
							    bm.centerAndZoom(new BMap.Point(leftupPointX, leftupPointY), 12);
						}				
	                }
				});	
			}
		}
	}, cityName);
}

function myFun(result){
	var cityName = result.name;
	var address = cityName;
	
	if(null != devArray[0])
	{
		$.get("DeviceListMgr.do?deveui=" + devArray[0], function(data){
			if(null != data && data != "")
			{
				address = data;
				uiinit(address, cityName);
			}	           
		});
	}
	else{
		uiinit(address, cityName);
	}
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
	
	$.post("GetRegisterDevices.do", {type: "ALL", usrname: "<%=sName%>"}, function(data){
			var allData = eval("(" + data + ")");
			outArray = allData.outdoor;
			inArray = allData.indoor;
			outinArray = allData.outin;
			devArray = outinArray.concat(outArray);//获取室外型终端
			pointArray = new Array();
			overlayArray = new Array();
			for(var devNumber=0; devNumber<devArray.length; devNumber++)
			{
				pointArray.push(new Array());
				devStatus[devNumber] = [{'val':devArray[devNumber]},{'val':'未知'},{'val':'未知'},{'val':'未知'},{'val':'未知'},{'val':'未知'}];
			}
			if(outArray.length > 0 || outinArray.length > 0){
				if(inArray.length > 0 || outinArray.length >0)
					createNav(true, true);
				else
					createNav(true, false);
				
				for(var x = 0;x<devArray.length;x++){
					var option = document.createElement("option");
					option.setAttribute("value",x);//设置option属性值

					option.appendChild(document.createTextNode(devArray[x]));
					select.appendChild(option);//向select增加option 
					//select.options[x].disabled = true;
				}
				select.options[0].selected = true;//设置默认选中第一个
			}
			else
			{
				if(inArray.length > 0)
					createNav(false, true);
			}
			var myCity = new BMap.LocalCity();
			myCity.get(myFun);
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
	
	function createNav(outdoor, indoor){
	    // 添加导航栏	
		navi = document.createElement("nav");
		addEclass(navi, "navbar");
		navi.style.cursor = "pointer";
		navi.style.backgroundColor = "#5bc0de";
		navi.style.height = "auto";
		uList = document.createElement("ul");
		addEclass(uList, "nav navbar-nav");
		uList.style.marginTop="0px";
		
		if(outdoor){
		    //导航栏中选择设备	
			li1 = document.createElement("li");
			select = document.createElement("select");
			select.style.padding = "2px";
			select.setAttribute("class", "nav-mark-btn");
			
			select.onmouseover = function(){
				showTips('选择室外型设备',40,60);
			}
			
			select.onmouseout = function(){
				hideTips();
			}
			
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
				showTips('点击实时轨迹可在当前页面显示设备运动路线。',40,430);
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
				window.location.href="tempalte/manager.html?service_id=118042&ak=SLlCUHnkdc580INpOiI9wRsbi8KYRKUW&owner=<%=sName%>";
			});
		
			li4.appendChild(tbutton);
			li4.appendChild(uList2);		
			uList.appendChild(li4);
		
		// 全屏模式
			li5 = document.createElement("li");
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
					window.open('loraDemo.jsp?usrname=<%=sName%>&type=big','_blank','fullscreen=1');
				 }
				else
					{
					window.location.href="javascript:self.close()";
					}
			};
		  
			li5.appendChild(ebutton);
			uList.appendChild(li5);
		}
		
		if(indoor){
			li1 = document.createElement("li");
			label = document.createElement("label");
			label.style.backgroundColor = "#5bc0de";
			label.style.color = "#fff";
			label.style.fontSize = "16px";
			label.style.fontFamily = "微软雅黑";
			label.style.fontWeight = "normal";
			label.innerHTML = "选择室内地图:"
			li1.appendChild(label);
			uList.appendChild(li1);
			//导航栏中选择设备	
			li2 = document.createElement("li");
			selectMap = document.createElement("select");
			selectMap.style.padding = "2px";
			selectMap.setAttribute("class", "nav-mark-btn");
			
			selectMap.onclick = function(){
				var leftupPointX = Number(mapsPos[selectMap.selectedIndex].X);
				var leftupPointY = Number(mapsPos[selectMap.selectedIndex].Y);
	
				if(leftupPointX != 0 && leftupPointY != 0)
				{
					bm.centerAndZoom(new BMap.Point(leftupPointX, leftupPointY), 18);
				}	
			}
			
			//select.setAttribute("class", "btn-warning");
			li2.appendChild(selectMap);	
			uList.appendChild(li2); 	
		}
		navi.appendChild(uList);
		div.appendChild(navi);	
    // 导航栏添加结束	
    }
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
 	webSocket = new WebSocket("ws://127.0.0.1/asset/NodeQueryWS.do"); 
 else if(webSocketAddr.indexOf("10.31.84.143") >= 0){
	 webSocket = new WebSocket("wss://dev.lansitec.com/asset/NodeQueryWS.do");
 }
 else{
	 webSocket = new WebSocket("ws://www.lansitec.com/asset/NodeQueryWS.do");
}
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
		if(devArray[index] == obj.DevEUI && null != labels[index])
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
						labels[index].setContent(devArray[index] + "[" + "室内" + "]");
		    	}
				//else
				//	labels[index].setContent(devArray[index] + "(" + obj.gps + ")");
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
		    	labels[index].setContent(devArray[index] + "(" + "室外" + ")");
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
		    		var speed = GPSList[i].speed.toFixed(2);
		    		if(speed >= 0)
		    		{
		    			var labelValue = labels[index].content;
		    			labels[index].setContent(devArray[index] + "[Online]" + "[" + speed + "(km/h)]");
		    		}		    		
		    	}
		    	translateCallback(pointA, timeA, index);
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
	webSocket.send("NME" + "<%=sName%>");
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
	$.post("GetRegisterDevices.do", {type: "REG", usrname: "<%=sName%>"}, function(data){
		 var obj = eval("(" + data + ")");
		 if(document.getElementById("devinfo") != null && obj != undefined)
			 updateDeviceInfo(obj);
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
function updateDeviceInfo(devlist)
{
	var totalNum = 0;//设备总数量
	var onlineNum = 0;//在线设备数量
	var regOutNum = 0;//在线室外型设备
	var regInNum = 0;//在线室内型设备
	var regOutInNum = 0;//在线室内外型设备
	
	for(var index=0; index < devlist.length; index++)
	{
		if(outArray.indexOf(devlist[index]) > -1)
				regOutNum++;
		else if(inArray.indexOf(devlist[index]) > -1)
			    regInNum++;
		else if(outinArray.indexOf(devlist[index]) > -1)
			    regOutInNum++;
	}
	
	totalNum = outArray.length + inArray.length + outinArray.length;
	onlineNum = devlist.length;
	
	document.getElementById("devinfo").innerHTML = "设备数量[" + totalNum + "] : "
	                                                                              + "室外型[" + outArray.length + "] 室内型[" + inArray.length + "] 室内外型[" +outinArray.length + "]<br/>" 
	                                                                              + "在线数量[" + onlineNum + "] : "
	                                                                              + "室外型[" + regOutNum + "] 室内型[" + regInNum + "] 室内外型[" + regOutInNum + "]";
}
</script>



</html>
