<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" import="java.net.InetAddress"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link href="../css/css/fonts.css" rel="stylesheet" />
    <link rel="stylesheet" href="../css/css/bootstrap.min.css" />
    
    <script src="../scripts/jquery-2.1.4.min.js"></script>
    <script src="../zoom/script/canvas-zoom.js"></script>
    <script src="../scripts/jquery.mousewheel.js"></script>
 
    <link rel="SHORTCUT ICON" href="../image/favicon.ico">
    
    <style type="text/css">   
		
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
    <style type="text/css">
    	.smart-green {
			margin-left:auto;
			margin-right:auto;
			max-width: 500px;
			background: #F8F8F8;
			padding: 30px 30px 20px 30px;
			font: 12px Arial, Helvetica, sans-serif;
			color: #666;
			border-radius: 5px;
			-webkit-border-radius: 5px;
			-moz-border-radius: 5px;
		}
		.smart-green h1 {
			font: 24px "Trebuchet MS", Arial, Helvetica, sans-serif;
			padding: 20px 0px 20px 40px;
			display: block;
			margin: -30px -30px 10px -30px;
			color: #FFF;
			background: #9DC45F;
			text-shadow: 1px 1px 1px #949494;
			border-radius: 5px 5px 0px 0px;
			-webkit-border-radius: 5px 5px 0px 0px;
			-moz-border-radius: 5px 5px 0px 0px;
			border-bottom:1px solid #89AF4C;
		}
		.smart-green label {
			display: block;
			margin: 0px 0px 5px;
		}
		.smart-green label>span {
			float: left;
			margin-top: 10px;
			color: #5E5E5E;
		}
		.smart-green input[type="text"], .smart-green select{
			color: #555;
			height: 30px;
			line-height:15px;
			width: 100%;
			padding: 0px 0px 0px 10px;
			margin-top: 2px;
			border: 1px solid #E5E5E5;
			background: #FBFBFB;
			outline: 0;
			-webkit-box-shadow: inset 1px 1px 2px rgba(238, 238, 238, 0.2);
			box-shadow: inset 1px 1px 2px rgba(238, 238, 238, 0.2);
			font: normal 14px/14px Arial, Helvetica, sans-serif;
		}
		.smart-green .button {
			background-color: #9DC45F;
			border-radius: 5px;
			-webkit-border-radius: 5px;
			-moz-border-border-radius: 5px;
			border: none;
			padding: 10px 25px 10px 25px;
			color: #FFF;
			text-shadow: 1px 1px 1px #949494;
		}
		.smart-green .button:hover {
			background-color:#80A24A;
		}
		
		.animated{
		          -webkit-animation-duration:1.4s;
		          animation-duration:1.4s;
		          -webkit-animation-fill-mode:both;
		          animation-fill-mode:both
		      }

       @-webkit-keyframes bounceOutUp {
           0% {
               -webkit-transform: translateY(0);
               transform: translateY(0);
           }

           20% {
               opacity: 1;
               -webkit-transform: translateY(20px);
               transform: translateY(20px);
           }

           100% {
               opacity: 0;
               -webkit-transform: translateY(-2000px);
               transform: translateY(-2000px);
           }
       }

        @keyframes bounceOutUp {
            0% {
                -webkit-transform: translateY(0);
                -ms-transform: translateY(0);
                transform: translateY(0);
            }

            20% {
                opacity: 1;
                -webkit-transform: translateY(20px);
                -ms-transform: translateY(20px);
                transform: translateY(20px);
            }

            100% {
                opacity: 0;
                -webkit-transform: translateY(-2000px);
                -ms-transform: translateY(-2000px);
                transform: translateY(-2000px);
            }
        }

        .bounceOutUp {
            -webkit-animation-name: bounceOutUp;
            animation-name: bounceOutUp;
        }

        @-webkit-keyframes flipInX {
            0% {
                -webkit-transform: perspective(400px) rotateX(90deg);
                transform: perspective(400px) rotateX(90deg);
                opacity: 0;
            }

            40% {
                -webkit-transform: perspective(400px) rotateX(-10deg);
                transform: perspective(400px) rotateX(-10deg);
           }

            70% {
                -webkit-transform: perspective(400px) rotateX(10deg);
                transform: perspective(400px) rotateX(10deg);
            }

            100% {
                -webkit-transform: perspective(400px) rotateX(0deg);
                transform: perspective(400px) rotateX(0deg);
                opacity: 1;
            }
        }

        @keyframes flipInX {
            0% {
                -webkit-transform: perspective(400px) rotateX(90deg);
                -ms-transform: perspective(400px) rotateX(90deg);
                transform: perspective(400px) rotateX(90deg);
                opacity: 0;
            }

            40% {
                -webkit-transform: perspective(400px) rotateX(-10deg);
                -ms-transform: perspective(400px) rotateX(-10deg);
                transform: perspective(400px) rotateX(-10deg);
            }

            70% {
                -webkit-transform: perspective(400px) rotateX(10deg);
                -ms-transform: perspective(400px) rotateX(10deg);
                transform: perspective(400px) rotateX(10deg);
            }

            100% {
                -webkit-transform: perspective(400px) rotateX(0deg);
                -ms-transform: perspective(400px) rotateX(0deg);
                transform: perspective(400px) rotateX(0deg);
                opacity: 1;
            }
        }

        .flipInX {
            -webkit-backface-visibility: visible !important;
            -ms-backface-visibility: visible !important;
            backface-visibility: visible !important;
            -webkit-animation-name: flipInX;
            animation-name: flipInX;
        }


        #dialogBg {
            width:100%;
            height:100%;
            background-color:#000000;
            opacity:.6;
            filter:alpha(opacity=60);
            position:fixed;
            top:0;left:0;
            z-index:9999;
            display:none;
        }
        #dialog {
            width:250px;
            height:160px;
            margin:0 auto;
            display:none;
            background-color:#ffffff;
            position:fixed;
            top:40%;
            left:50%;
            margin:-120px 0 0 -150px;
            z-index:10000;
            border:1px solid #ccc;
            border-radius:10px;
            -webkit-border-radius:10px;
            box-shadow:3px 2px 4px #ccc;
            -webkit-box-shadow:3px 2px 4px #ccc;
        }
        .dialogTop {
            width:90%;
            margin:0 auto;
            border-bottom:1px dotted #ccc;
            letter-spacing:1px;
            padding:10px 0;
            text-align:right;
        }
        .dialogIco {
            width:50px;
            height:50px;
            position:absolute;
            top:-25px;
            left:50%;
            margin-left:-25px;
        }
        .editInfos {
            padding:15px 0;
        }
        
        .navbar-nav > li{
		   margin-right:20px;
		   margin-top:15px;
        }
    </style>
</head>
<body onunload="closeSocket()">
    <div style="position:absolute; top:50%;left:50%;transform:translate(-50%,-50%);">
        <canvas id="indoormap" width="1080" height="1175"></canvas>
    </div>
    
    <div id="dialogBg"></div>
   	<div id="dialog" class="animated">
        <img class="dialogIco" width="50" height="50" src="../images/map.png" alt="" />
        <div class="dialogTop">
             <a href="javascript:;" class="claseDialogBtn">确认</a>
        </div>
        <form action="" id="editForm">
            <ul class="editInfos" style="padding:0;margin:0;list-style:none">
                <li style="width:98%;margin:8px auto auto;text-align: left;padding-left: 10px;padding-right: 10px;">
                    <label style="width:98%;">
                        <font color="#ff0000"></font>请选择地图：
                        <select id="mapSelected"></select>
                    </label>
                </li>
            </ul>
        </form>
    </div>
    
    <div id="iform" style="position: absolute; z-index: 10; top:auto; right: 8px; bottom: 40px; left: ayto; display:none;">
    	<form action="" method="post" class="smart-green">
			<h1>设备信息</h1>
			<label>
			<span>设备号 :</span>
			<input id="deveui" type="text" name="deveui" placeholder="deveui" disabled="true"/>
			</label>
			
			<label>
			<span>状态 :</span>
			<input id="vib" type="text" name="vib" placeholder="vib" disabled="true"/>
			</label>
			<label>
			
			<label>
			<span>电池电量 :</span>
			<input id="battery" type="text" name="battery" placeholder="battery" disabled="true"/>
			</label>
			
			<label>
			<span>信号 :</span>
			<input id="rssi" type="text" name="rssi" placeholder="rssi" disabled="true"/>
			</label>
			
			<span>更新时间 :</span>
			<input id="time" type="text" name="time" placeholder="time" disabled="true"/>
			</label>
			<label>
			<span>&nbsp;</span>
			<input id="btnCls" type="button" class="button" value="关闭" />
			</label>
		</form>
    </div>
    
   
   <script type="text/javascript">
        var sX, sY, osX, osY;
        var mapid;
    	var MAPINFO = [];
    	var devArray = new Array();
    	var pointArray = null;
        var markerInfo = [];
        var totalEui=0;
		var regEui=0;
    	
    	var canvas = document.getElementById("indoormap");
        var context2D = canvas.getContext("2d");
        
        var clsBtn = document.getElementById("btnCls");
  	    clsBtn.onclick = function() {
  	    	$("#iform").css('display','none');
  	    }

        var clientWidth = document.documentElement.clientWidth;
        var clientHeight = document.documentElement.clientHeight;

        var zoomScale = 1;
        if (clientWidth <= clientHeight) {
            zoomScale = clientWidth / 720;
        } else {
            zoomScale = clientHeight / 720;
        }
 
        var div = document.createElement("div");
    	label = document.createElement("label");
    	label.style.backgroundColor = "#394555";
    	label.setAttribute("id", "devinfo");
    	label.style.color = "#fff";
    	label.style.fontSize = "16px";
    	label.style.fontFamily = "微软雅黑";
    	label.style.fontWeight = "normal";
    	div.style.padding = "right";
    	label.paddingLeft = "3px";
    	div.style.paddingTop = "2px";

    	div.appendChild(label);
    	label.innerHTML = "终端信息";
    	document.body.appendChild(div);
    	
        $(document).ready(function () {
	    	 var select = $("select#mapSelected");	
	    	 //获取所有地图名称 
	    	 $.get("../GetMapInfo/doGet.do?username=<%=sName%>", function(str) {
	    		 select.empty();
	    		 select.append(str);
			 });
	         
	    	var w,h,className;
	        function getSrceenWH() {
	            w = $(window).width();
	            h = $(window).height();
	            $('#dialogBg').width(w).height(h);
	        }

	        window.onresize = function() {  
	            getSrceenWH();
	        } 

	        $(window).resize(); 

	        //弹出对话框
	        $(function() {
	        	getSrceenWH();
               //开启地图选择弹窗 
               	className = "flipInX";
               	$('#dialogBg').fadeIn(300);
               	$('#dialog').removeAttr('class').addClass('animated '+className+'').fadeIn();
   	     	
	            //关闭地图选择弹窗 
	           	$('.claseDialogBtn').click(function() {
	                $('#dialogBg').fadeOut(300,function() {
	                    $('#dialog').addClass('bounceOutUp').fadeOut();
	                });
	                
	                //获取地图信息 
	               	mapid = $("#mapSelected").val();
    				
    				request(mapid);//获取地图信息 
 
	                //获取该地图所有的终端信息 
	                $.ajaxSetup({
		  				async: false
		  			});
    				
					$.post("../GetRegisterDevices/doPost.do", {type: "ALL", usrname:mapid}, function(data) {
						devArray = eval("(" + data + ")");
						totalEui = devArray.length;
					});
					
					$.post("../GetRegisterDevices/doPost.do", {type: "REG", usrname: mapid}, function(data) {
						 var obj = eval("(" + data + ")");
						 regEui = obj.length;
					});
					
					document.getElementById("devinfo").innerHTML = "终端信息：设备数量【" + totalEui + "】, 在线数量【" + regEui + "】, 离线数量【" + (totalEui-regEui) + "】";
					
					$.post("../GetRegisterDevices/doPost.do", {type: "POS", usrname: mapid}, function(data) {
						
						var devInfo = JSON.parse(data);
						
						for(i = 0; i < devInfo.devData.length; i++) {
							for(j = 0 ; j < devArray.length; j++) {
								var deveui = devInfo.devData[i].deveui;
								var status = devInfo.devData[i].status;
								
								if(status == "REG") {
									status = "就绪";
								}else {
									status = "离线";
								}
								if(deveui == devArray[j]) {
									var marker = {
				                		deveui: deveui,
				                		mapname: devInfo.devData[i].mapid,
				                		x: devInfo.devData[i].x,
				                		y: devInfo.devData[i].y,
				                		time: devInfo.devData[i].time,
				                		battery: devInfo.devData[i].battery,
				                		status: status,
				                		vib: devInfo.devData[i].vib,
				                		rssi: devInfo.devData[i].rssi,
				                		devType: devInfo.devData[i].devType
				                	}
									markerInfo.push(marker);
								}
							}
		            	}
					});
					
					// 添加导航栏
					var contianer = document.body;
					
					navi = document.createElement("nav");
					navi.setAttribute("class", "navbar");
					navi.style.cursor = "pointer";
					uList = document.createElement("ul");
					uList.setAttribute("class", "nav navbar-nav");
					uList.style.marginTop="-15px";
					
					li1 = document.createElement("li");
					selectdev = document.createElement("select");
					selectdev.style.padding = "2px";
					selectdev.setAttribute("class", "nav-mark-btn");

					for(var x = 0;x < devArray.length;x++) {
						var option = document.createElement("option");
						option.setAttribute("value",x);//设置option属性值

						option.appendChild(document.createTextNode(devArray[x]));
						selectdev.appendChild(option);//向select增加option 
						//select.options[x].disabled = true;
					}
					 
					if(devArray.length > 0)
						selectdev.options[0].selected = true; 
					else {
						 var option = document.createElement("option");
						 option.setAttribute("value",-1); 
						 option.appendChild(document.createTextNode("您的名下无设备"));
						 selectdev.appendChild(option); 
					}

					li1.appendChild(selectdev);	
					uList.appendChild(li1); 

				//显示信息	
					li3 = document.createElement("li");
					img = document.createElement("img");
					img.setAttribute("id", "newImg");
				　　　img.src = "../images/show.png";
					img.style.padding = "2px";
					uList.setAttribute("class", "nav navbar-nav");
					
					img.onclick = function() {
						if(selectdev.value >= 0) {
							var index = selectdev.value;
							for(var i = 0; i < markerInfo.length; i++) {
								if(devArray[index] == markerInfo[i].deveui) {
							$("#iform").css('display','block');
		                        	$("#deveui").val(''+markerInfo[i].deveui);
		                        	$("#vib").val(''+markerInfo[i].vib);
		                        	$("#rssi").val(''+markerInfo[i].rssi);
		                        	$("#battery").val(''+markerInfo[i].battery);
		                        	$("#time").val(''+markerInfo[i].time);
								}
							}
							
						}
						else {
							alert("请选择设备!");
						}
					};

					li3.appendChild(img);
					uList.appendChild(li3);
				
					navi.appendChild(uList);
					contianer.appendChild(navi);
					
	                init();
	            });
	        });      
	    });
        
        
        function request(mapid) {
        	var b=document.createElement("script");
            b.type = "text/javascript"; 
            b.src = "http://192.168.2.151:8080/PersonTracker/jsonp?id=" + mapid + "&type=json&callback=callbackfunction"; 
            //b.src = "http://120.27.104.163/asset/jsonp?id=" + id + "&type=json&callback=callbackfunction"; 
            var headb=document.getElementsByTagName("head")[0];
            headb.appendChild(b);
        }
        
        var callbackfunction = function(jsondata) {
            if(jsondata == "") {
        		alert("Nothing received from map server, please check the map ID!")
        		return;
        	}
            
        	MapData_ = JSON.parse(jsondata);
            var x = MapData_.features[0].properties.x;
            var y = MapData_.features[0].properties.y;
            var width = MapData_.features[0].properties.width;
            var height = MapData_.features[0].properties.height;
            var imageurl = MapData_.features[0].properties.imageurl;
            
            var map_ = {
            	x: x,
        		y: y,
        		width: width,
        		height: height,
        		imageurl: imageurl
            };
            MAPINFO.push(map_);
        };
        
        
        var workerImg = new Image();
        workerImg.onLoad = function() {
            
        }
        workerImg.src = '../images/worker.png';
        
        var managerImg = new Image();
        managerImg.onLoad = function() {
            
        }
        managerImg.src = '../images/manager.png';
        
        var visitorImg = new Image();
        visitorImg.onLoad = function() {
            
        }
        visitorImg.src = '../images/visitor.png';
        
        var Img = new Image();
        Img.onLoad = function() {
            
        }
        Img.src = '../images/marker.png';

        var sX, sY, osX, osY; 

        //绘制地图
        function DrawMapInfo(scaleX, scaleY, offsetX, offsetY) {
        	sX = scaleX;
			sY = scaleY;
			osX = offsetX;
			osY = offsetY;
            for (i = 0; i < MAPINFO.length; i++) {
                DrawBlock(MAPINFO[i].x * scaleX + offsetX, MAPINFO[i].y * scaleY + offsetY, MAPINFO[i].width * scaleX, MAPINFO[i].height * scaleY, MAPINFO[i].imageurl,MAPINFO[i].color);
            }
        }

        //加载地图到canvas
        function DrawBlock(x, y, width, height, imageurl, color) {
            //context2D.fillStyle = color;
            //context2D.fillRect(x, y, width, height);
            //context2D.strokeRect(x, y, width, height);
            var image = new Image();
            image.src = imageurl;
            context2D.drawImage(image, x, y, width, height);
        }

        //终端信息
        function DrawMarkerInfo(scaleX, scaleY, offsetX, offsetY) {
        	for (j = 0; j < markerInfo.length; j++) {
                drawMarkers(markerInfo[j].x * scaleX + offsetX, markerInfo[j].y * scaleY + offsetY, markerInfo[j].deveui, markerInfo[j].status, markerInfo[j].devType);
            }
        }

        //添加终端到地图
        function drawMarkers(x, y, deveui, status, devtype) {
            context2D.font = 10 * zoomScale + "pt Microsoft 楷体";  
            context2D.textAlign = "left";
            context2D.fillStyle = "black";  
            context2D.fillText(""+deveui+[status], x-18, y-20); 
            context2D.fillStyle = 'rgba(255,255,255,0)';
            context2D.beginPath();
            rad = 16;
            context2D.arc(x, y, rad, 0, 2*Math.PI);
            context2D.closePath();
            if(devtype == "工人"){
            	context2D.drawImage(workerImg, x-16, y-16, 32, 32);
            }else if(devtype == "管理者"){
            	context2D.drawImage(managerImg, x-16, y-16, 32, 32);
            }else if(devtype == "资产"){
            	
            }else if(devtype == "访客"){
            	context2D.drawImage(visitorImg, x-16, y-16, 32, 32);
            }else{
            	context2D.drawImage(Img, x-16, y-16, 32, 32);
            }
            context2D.lineWidth = 3;
            context2D.strokeStyle="green";
            context2D.stroke();
           //context2D.closePath();
            //context2D.fill();
        }

        function DrawRangInfo(scaleX, scaleY, offsetX, offsetY) {}

        function closeSocket() {
        	webSocket.close();
        }
        
        Date.prototype.format =function(format) {
	        var o = {
	        "M+" : this.getMonth()+1, //month
	        "d+" : this.getDate(), //day
	        "h+" : this.getHours(), //hour
	        "m+" : this.getMinutes(), //minute
	        "s+" : this.getSeconds(), //second
	        "q+" : Math.floor((this.getMonth()+3)/3),
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
        	webSocket = new WebSocket("ws://192.168.2.151:8080/PersonTracker/NodeQueryWS.do"); 
        else{
        	 webSocket = new WebSocket("ws://120.27.104.163/PersonTracker/NodeQueryWS.do");
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
        					    var status = "就绪";
        						markerInfo[index].deveui = obj.DevEUI;
        						markerInfo[index].x = obj.x;
        						markerInfo[index].y = obj.y;
        						markerInfo[index].time = obj.time;
        						markerInfo[index].battery = obj.battery;
        						markerInfo[index].status = status;
        						markerInfo[index].vib = obj.vib;
        						markerInfo[index].rssi = obj.rssi;
        					break;
        					}
        				}
        			} 
        			break;
        		}
        	}
        	DrawMarkerInfo(sX, sY, osX, osY);
        	
        	$.post("../GetRegisterDevices/doPost.do", {type: "REG", usrname: mapid}, function(data) {
				 	var obj2 = eval("(" + data + ")");
				    regEui = obj2.length;
				});
			document.getElementById("devinfo").innerHTML = "终端信息：设备数量【" + totalEui + "】, 在线数量【" + regEui + "】, 离线数量【" + (totalEui-regEui) + "】";
        }
        
        function onOpen(event) {
        	webSocket.send("NME" + "<%=sName%>");
     	//init();
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
        
     function closeSocket() {
    	    webSocket.close();
    	}
        
    </script>
    
   <script>
		$(document).ready(function () {
	    	setInterval(100);
		});

     	var clientWidth = document.documentElement.clientWidth;
     	var clientHeight = document.documentElement.clientHeight;

     	var zoomScale = 1;
    	var gesturableCanvas = null;
     	if (clientWidth <= clientHeight) {
        	zoomScale = clientWidth / 720;
     	} else {
        	zoomScale = clientHeight / 720;
     	}
     
	 	function init() {
        	gesturableCanvas = new CanvasZoom({
            	canvas: document.getElementById('indoormap'),
            	desktop: true
        	});
	    }

	 
   	    $('#indoormap').bind('mousewheel', function(event, delta) {
        	var dir = delta > 0 ? 'Up' : 'Down';
            var counter = 0;
              
          	if (dir == 'Up') {
          		if (gesturableCanvas.scale.x < 2.5) {
                      gesturableCanvas.scale.x += 0.05;
                      gesturableCanvas.scale.y += 0.05;
                      gesturableCanvas.position.x += -(clientWidth / 60);
                      gesturableCanvas.position.y += -(clientHeight / 60);
                      counter++;
                     
                  } else {
                      gesturableCanvas.scale.x = 2.5;
                      gesturableCanvas.scale.y = 2.5;
                      gesturableCanvas.position.x = -(clientWidth / 2);
                      gesturableCanvas.position.y = -(clientHeight / 2);
                  }
               
          	} else {
          		if (gesturableCanvas.scale.x > 1) {
                      gesturableCanvas.scale.x -= 0.05;
                      gesturableCanvas.scale.y -= 0.05;
                      gesturableCanvas.position.x -= -(clientWidth / 60);
                      gesturableCanvas.position.y -= -(clientHeight / 60);
                      counter++;
                      
                  } else {
                      gesturableCanvas.scale.x = 1;
                      gesturableCanvas.scale.y = 1;
                      gesturableCanvas.position.x = 0;
                      gesturableCanvas.position.y = 0;
                  }
          	}
        }); 

   </script>

</body>
</html>