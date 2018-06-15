<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" import="java.net.InetAddress"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" id="viewport_map" content="user-scalable=yes, initial-scale=1, width=device-width, height=device-height" />
<script src="../js/jquery-2.1.4.min.js"></script>
<script src="../js/jquery.mousewheel.js"></script>
<link href="../css/fonts.css" rel="stylesheet" />
<link rel="stylesheet" href="../css/bootstrap.min.css" />
    
<title>Insert title here</title>
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
		String mapid = (String)request.getParameter("mapid");
	%>
</head>
<body onunload="closeSocket()">
	<div id="zoom-btn-container" style="position: absolute; z-index: 10; bottom: auto; right: auto; top: 10px; left: 10px;">
        
    </div>
	<div style="position:fixed; top:50%;left:50%;transform:translate(-50%,-50%);">
        <canvas id="indoormap" width="1080" height="1175"></canvas>
    </div>
    
    <script src="http://www.lansitec.com/asset/js/canvas-zoom.js"></script>
    <script type="text/javascript">
        var map = null;
        var devArray = new Array();
    	var pointArray = null;
        var markerInfo = [];
        var rangInfo = [];
        
        var canvas = document.getElementById("indoormap");
        
        var mapid = "<%=mapid%>"; 
    	initialize();
   	    
   	    function initialize(){
   	    	map = new LansiMap("indoormap",{"id":mapid});
   	    }
   	    
   	    $.ajaxSetup({
			async: false
		});
		
		$.post("../GetRegisterDevices.do", {type: "ALL", usrname:mapid}, function(data) {
			devArray = eval("(" + data + ")");
		});
		
		$.post("../GetRegisterDevices.do", {type: "REG", usrname: mapid}, function(data) {
			 var obj = eval("(" + data + ")");
		});
		
		$.post("../GetRegisterDevices.do", {type: "POS", usrname: mapid}, function(data) {
			var devInfo = JSON.parse(data);
			for(i = 0; i < devInfo.length; i++) {
					var deveui = devInfo[i].deveui;
					var status = devInfo[i].status;
					
					if(status == "REG") {
						status = "就绪";
					}else {
						status = "离线";
					}
					if(devInfo[i].x > 0 && devInfo[i].y > 0)
					{
						var marker = {
	                		deveui: deveui,
	                		map: map,
	                		x: devInfo[i].x,
	                		y: devInfo[i].y,
	                		label: {
	                			text: deveui,
	                			color:'blue',
	                			size: 15
	                		},
	                		icon: "../images/wagong.png"
	                	}
						markerInfo.push(marker);
					}
				}
		});
        
		var  container = document.getElementById("zoom-btn-container");
        uList = document.createElement("ul");
   		uList.setAttribute("class", "nav navbar-nav");
   		uList.style.marginTop="0px";
   		
   		li1 = document.createElement("li");
   		
   		lbutton = document.createElement("input");
   		lbutton.setAttribute("type", "button");
   		lbutton.setAttribute("value", "显示终端");
   		lbutton.style.padding = "2px";
   		lbutton.setAttribute("class", "btn btn-info");
   		lbutton.onclick = function() {
			if(lbutton.getAttribute("value") == "显示终端") {
				lbutton.setAttribute("value", "隐藏终端");
				for(i = 0; i< markerInfo.length; i++) {
					map.addMarker(markerInfo[i]);
					//定义信息框 
					var div = {
						text:"你点击了marker"+i,
						color: "#000",
						size:"16px",
						lineHeight:"60px",
						width:"200px",
						height:"80px",
						bgColor: "#fff",
						borderWidth: "2px",
						borderStyle: "solid",
						borderColor: "#000",
						borderRadius: "4px"
					};

					//注册信息框事件  
					map.ListenClick(markerInfo[i], div);
				}
			}
			else {
				//清空所有终端 
  				map.setMap(null);
  				lbutton.setAttribute("value", "显示终端");
			}
		};
   		
   		li1.appendChild(lbutton);
   		uList.appendChild(li1);
   	
        li2 = document.createElement("li");
   		
   		l2button = document.createElement("input");
   		l2button.setAttribute("type", "button");
   		l2button.setAttribute("value", "显示轨迹");
   		l2button.style.padding = "2px";
   		l2button.setAttribute("class", "btn btn-info");
   		l2button.onclick = function() {
   			if(l2button.getAttribute("value") == "显示轨迹")	{
   				l2button.setAttribute("value", "隐藏轨迹");
   			}
   			else {
				l2button.setAttribute("value", "显示轨迹");
				//清空历史轨迹 
				rangInfo.length = 0;
				map.LineEmpty();
			}
		};
		li2.appendChild(l2button);
   		uList.appendChild(li2);
   		container.appendChild(uList);
    	
		//绘制历史轨迹 
	/*	rangInfo = [
            {x:32,y:113},
            {x:123,y:235},
            {x:349,y:412},
            {x:567,y:781},
            {x:836,y:1003},
            {x:912,y:859}
		];
		map.drawLine(rangInfo);*/
		
		//websocket
		function closeSocket() {
        	webSocket.close();
        }
		
		/* 申请一个WebSocket对象，参数是需要连接的服务器端的地址 */ 
		 var webSocketAddr = "<%=InetAddress.getLocalHost().getHostAddress()%>";
	     var webSocket;
	     if(webSocketAddr.indexOf("192.168") >= 0)
	     	webSocket = new WebSocket("ws://localhost/asset/NodeQueryWS.do"); 
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
	     
	     function onMessage(event) { /*收到server的数据后,更新地图*/  

	        var obj = eval("(" + event.data + ")");

	     	for(var index = 0; index < devArray.length; index++) {
	     		if(devArray[index] == obj.DevEUI) {	
	     			if(obj.msgType == "REG") {
	     				for(index = 0; index < markerInfo.length; index++) {
	     					if(obj.DevEUI == markerInfo[index].deveui) {
	     						//移除marker
	     						map.removeMarker(markerInfo[index]);
	     						if(l2button.getAttribute("value") == "显示轨迹") {
	     							var reline = {
	     									x: markerInfo[index].x,
	     									y: markerInfo[index].y
	     							};
	     							rangInfo.push(reline);
	     							markerInfo[index].deveui = obj.DevEUI;
		     						markerInfo[index].x = obj.x;
		     						markerInfo[index].y = obj.y;
		     						var afterline = {
		     								x: obj.x,
		     								y: obj.y
		     						};
		     						rangInfo.push(reline);
	     						}
	     						map.drawLine(rangInfo);

	     						if(lbutton.getAttribute("value") == "显示终端") {
	     							map.addMarker(markerInfo[index]);
	     						}
	     						break;
	     					}
	     				}
	     			}
	     			break;
	     	    }
	     	}
	     }
	     
	     function onOpen(event) {
	      	webSocket.send("NME" + mapid);
	     } 
	     
	      function onClose(event) { 
	      	 //document.getElementById("messages").innerHTML += event.data+ "\n";
	      } 
	      
	      function onError(event) { 
	      	  if(event.data != undefined)
	      		  alert("连接数据服务器失败,请刷新页面重试!" + event.data);
	      	  else
	      		  alert("连接数据服务器失败,请刷新页面重试!");
	      } 
    </script>
</body>
</html>