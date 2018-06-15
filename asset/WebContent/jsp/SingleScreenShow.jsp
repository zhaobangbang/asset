<%@ page contentType="text/html; charset=UTF-8" language="java"
	errorPage="" import="java.net.InetAddress"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<script type="text/ecmascript" src="../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../images/login.js"></script>
<link href="../css/login2.css" rel="stylesheet" type="text/css" />
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
</style>
<%
	String sName = (String)request.getSession().getAttribute("usrname");

	if(null == sName || sName.equals(""))
	{  response.sendRedirect("../index.jsp");
   		return;
 	}
	
	String deveui = (String)request.getParameter("deveui");
%>
<title>LORA DEMO</title>
<script type="text/javascript"
	src="https://api.map.baidu.com/api?v=2.0&ak=T3esAl5i0NHA9SAEHF3dRyauqTazR2bf"></script>
</head>

<body onunload="closeSocket()">
	<div id="allmap"></div>
</body>
<script type="text/javascript"> 
var bm;
var convertor;
var deveui = "<%=deveui%>";
var usrname = "<%=sName%>";
var pointArray = new Array();
var overlayArray = null;
var markers = null;
var labelgps = null;
var lbutton = null;
var traceTxt = null;
var lastTime = null;
var lastPoint = null;//The last postion of the device.
var wendu = 0; //最后一次得到的温度值
var shidu = 0; //最后一次得到的湿度值

//绘制轨迹
function drawPolyline() {  
    if (pointArray==null || pointArray.length<=1) {  
        return;  
    }
    if(overlayArray != null)
    {
    	bm.removeOverlay(overlayArray);
    }
 
    overlayArray = new BMap.Polyline(pointArray, {  
        	strokeColor : "red",  
        	strokeWeight : 3,  
        	strokeOpacity : 0.5  
    	});
    bm.addOverlay(overlayArray); // 画线  
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
 
	lbutton = document.createElement("input");
	lbutton.setAttribute("type", "button");
	lbutton.setAttribute("value", "开始定位");
	//button.setAttribute("style", "margin-left:30px;margin-top:10px;width:50px;");
	lbutton.setAttribute("class", "button_blue");
	lbutton.onclick = function(){		
		{
			var req = {
					// usrname: inputName,
					 DEVEUI: deveui
					};
			$.ajax(
					{
						async: false,
						type: "POST",
						url: "../DevPositionReq.do",
						data: req,
						success: function(data) {
						    var status = eval("("+data+")");
						    alert(status);
						   }		
			       });
		}
	};

	div.appendChild(lbutton);
	if(usrname == "yjs")
	{
		div.appendChild(document.createElement("br"));
		div.appendChild(document.createElement("br"));
		div.appendChild(document.createElement("br"));
		traceTxt = document.createElement("textarea");
		traceTxt.setAttribute("id","traceTxt");
		traceTxt.setAttribute("style", "font-size:12px;width:230px;height:300px");
		div.appendChild(traceTxt);
	}

	// 设置样式    
	div.style.cursor = "pointer";    
	div.style.border = "0px solid blue";    
	div.style.backgroundColor = "blue";
	div.style.color = "blue";
	div.style.filter="alpha(opacity=80)";

	 // 绑定事件，点击一次放大两级    
   
	 // 添加DOM元素到地图中   
	map.getContainer().appendChild(div);    
	 // 将DOM元素返回  
	return div; 
}
function init()
{
	//var xxL = 118.80290300615; //Lansitec
	//var yyL = 31.982098073871; 

	//地图初始化
	bm = new BMap.Map("allmap"); 
	convertor = new BMap.Convertor();

	bm.addControl(new BMap.MapTypeControl({anchor: BMAP_ANCHOR_BOTTOM_RIGHT}));
	bm.addControl(new BMap.NavigationControl());
	bm.addControl(new BMap.ScaleControl());
	bm.addControl(new DevSerControl());
	bm.enableScrollWheelZoom(true);

function myFun(result){
	var cityName = result.name;
	var address = cityName;
	$.ajaxSetup({
		  async: false
		  });
	$.get("../DeviceListMgr.do?deveui=" + deveui, function(data){
			if(data != "");
				address = data;
		});
	var myGeo = new BMap.Geocoder();
	myGeo.getPoint(address, function(point){
		if (point) {
			lastPoint = point;
			bm.centerAndZoom(point, 13);
			markers = new BMap.Marker(point);
			labelgps = new BMap.Label(deveui + "(离线)",{offset:new BMap.Size(20,-10)}); 
			markers.setLabel(labelgps); //添加GPS标注
			bm.addOverlay(markers);
		}else{
			alert("您选择地址没有解析到结果!");
		}
	}, cityName);
}


	var myCity = new BMap.LocalCity();
	myCity.get(myFun);
}

function translateCallback(point, datr, rssi, freq, wd, sd, recv){
	bm.removeOverlay(labelgps);
	bm.removeOverlay(markers);
	markers = new BMap.Marker(point); 
	bm.addOverlay(markers); 
	var opts = {
	  width : 200,     // 信息窗口宽度
	  height: 150,     // 信息窗口高度
	  title : "设备详细信息:" , // 信息窗口标题
	}

	var infoWindow;
	infoWindow = new BMap.InfoWindow("地址:" + deveui + "<br>速率:" + datr + "<br>信号:" + rssi + "dbm<br>频率:" + freq + "MHz<br>温度:" + wd + "摄氏度<br>湿度:" + sd + "%<br>更新:" + recv, opts);  // 创建信息窗口对象 
	markers.addEventListener("click", function(){          
		bm.openInfoWindow(infoWindow,point); //开启信息窗口
	});
	labelgps = new BMap.Label(deveui+"(正常)",{offset:new BMap.Size(20,-10)}); 
	markers.setLabel(labelgps); //添加百度label
	pointArray.push(point);
	if(pointArray.length > 10000)
		pointArray.pop();

	drawPolyline();
	bm.setCenter(point); 
	lastPoint = point;
	//alert("转化为百度坐标为："+point.points[0].lng +"," + point.points[0].lat); 
}
function closeSocket(){
	webSocket.close();
}
/* 申请一个WebSocket对象，参数是需要连接的服务器端的地址 */ 
 var webSocketAddr = "<%=InetAddress.getLocalHost().getHostAddress()%>";
 var webSocket;
 if(webSocketAddr.indexOf("192.168") >= 0)
 	webSocket = new WebSocket("ws://127.0.0.1:8080/asset/NodeQueryWS.do"); 
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
 
function onMessage(event) 
{ /*收到server的数据后,更新地图*/
	//document.getElementById("messages").innerHTML += event.data+ "\n"; 
    var obj = eval("(" + event.data + ")");	
    var result;
    
	if(deveui == obj.DevEUI)
	{
		var curBValue = lbutton.getAttribute("value");
		result = obj.State;
		
		if(curBValue == "开始定位")
		{
			if(result == "LOCATE" || result == "DATARCV")
			{
				lbutton.setAttribute("value", "停止定位");
			}
		}
		else if(curBValue == "停止定位")
		{
			if(result == "UNLOCATE" || result == "REG" || result == "OFFLINE")
			{
				lbutton.setAttribute("value","开始定位");
			}
		}
		
		if(obj.msgType == "01")
		{
			bm.removeOverlay(labelgps);
			labelgps = new BMap.Label(deveui + "(已注册)",{offset:new BMap.Size(20,-10)}); 
			markers.setLabel(labelgps); //添加GPS标注
		}
		else if(obj.msgType == "33") //0x21
		{
			bm.removeOverlay(labelgps);
			labelgps = new BMap.Label(deveui + "(就绪)",{offset:new BMap.Size(20,-10)}); 
			markers.setLabel(labelgps); //添加GPS标注			
		}
		else if(obj.msgType == "18") //0x12
		{
			wendu = obj.Wd;
			shidu = obj.Sd;
			
			var opts = {
					  width : 200,     // 信息窗口宽度
					  height: 150,     // 信息窗口高度
					  title : "设备详细信息:" , // 信息窗口标题
					}

			var infoWindow = new BMap.InfoWindow("地址:" + deveui + "<br>速率:" + obj.Datr + "<br>信号:" + obj.Rssi + "dbm<br>频率:" + obj.Freq + "MHz<br>温度:" + obj.Wd + "摄氏度<br>湿度:" + obj.Sd + "%<br>更新:" + obj.Recv, opts);  // 创建信息窗口对象 
			markers.addEventListener("click", function(){          
					bm.openInfoWindow(infoWindow,lastPoint); //开启信息窗口
				});
			bm.removeOverlay(labelgps);
			
			if(result == "UNLOCATE")
				labelgps = new BMap.Label(deveui + "(取消定位)",{offset:new BMap.Size(20,-10)}); 
			else
				labelgps = new BMap.Label(deveui + "(定位中)",{offset:new BMap.Size(20,-10)}); 
				
			markers.setLabel(labelgps); //添加信息标注			
		}
		else if(obj.msgType == "17") //0x11
		{
			var realWd, realSd;
			if(obj.Wd == null || obj.Sd == null)
			{//如果不包含温湿度,则用上次的值.
				realWd = wendu;
				realSd = shidu;
			}
			else
			{
				realWd = obj.Wd;
				realSd = obj.Sd;
				wendu = realWd;
				shidu = realSd;
			}
			translateCallback(new BMap.Point(obj.xGPS, obj.yGPS), obj.Datr, obj.Rssi, obj.Freq, realWd, realSd, obj.Recv);
		}
		else if(obj.msgType == "TIMEOUT")
		{
			bm.removeOverlay(labelgps);
			markers.setLabel(new BMap.Label(deveui + "(离线)",{offset:new BMap.Size(20,-10)})); //添加GPS标注
		}
		else if(obj.msgType == "DEBUG")
		{
			if(document.getElementById("traceTxt").value.split("\n").length > 100) //清空数据
				document.getElementById("traceTxt").value = "";
			var curValue = document.getElementById("traceTxt").value;
			var time = new Date();
			var delta;
			if(lastTime == null)
				delta = 0;
			else
				delta = time - lastTime;
			lastTime = time;
			
			document.getElementById("traceTxt").value = obj.TRACE + "\r\n***距离上次数据:"+delta/1000+"秒***\r\n" + curValue;
		}
	}	
}

function onOpen(event) 
{   
	if(usrname == "yjs")
		webSocket.send("DBG" + deveui);
	else
		webSocket.send("DEV" + deveui);

	init();
} 
function onClose(event) 
{ 
	 //document.getElementById("messages").innerHTML += event.data+ "\n";
} 
function onError(event) 
{ 
	alert("连接数据服务器失败,请刷新页面重试!");
} 

</script>



</html>
