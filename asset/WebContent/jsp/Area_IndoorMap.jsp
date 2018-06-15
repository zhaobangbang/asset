<%@ page contentType="text/html; charset=UTF-8" language="java"
	errorPage="" import="java.net.InetAddress"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="Expires" content="-1">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-control" content="no-cache">
<meta http-equiv="Cache" content="no-cache">
<style type="text/css">
html, body, #googleMap {
	width: 100%;
	height: 100%;
	overflow: hidden;
	margin: 0;
}
</style>
<title>室内地图</title>
<script src="../js/jquery-3.1.0.min.js"></script>
<script src="../js/lansIndoorMap.js"></script>
<link rel="stylesheet" type="text/css" href="../css/bootstrap.min.css" />
<link rel="SHORTCUT ICON" href="../images/favicon.ico" />
</head>
<body onload="initialize()">
	<div id="googleMap"></div>
</body>
<%
 	String sName = (String)request.getSession().getAttribute("usrname");
    String mapId = (String)request.getSession().getAttribute("mapid");
    String zoom = (String)request.getSession().getAttribute("zoom");
	if(null == sName || sName.equals(""))
	{  response.sendRedirect("index.jsp");
   		return;
 	}
	sName = (String)request.getParameter("usrname");
	mapId = (String)request.getParameter("mapid");
	zoom = (String)request.getParameter("zoom");
	if(null == mapId){
		mapId = sName;
	}
	if(null == zoom){
		zoom = "1";
	}
%>
<script type="text/javascript">
        var map;
        var beaconmarkers = new Array();
       	var showTracker = 0;
       //	var markers = new Array();
        var newMarker= new Array();
        var infowindow = new Array();
        var image = null;
        var devAndGPSArray = new Array();
        function initialize()
        {   
           	var xGPS = null;
           	var yGPS = null;
           	var deveui = null;
           	var alias = null;
           	var worktype = null;
           	var time = null;
     
        	map = createLansMap("googleMap", {"id":"<%=mapId%>","zoom":"<%=zoom%>"});
        	var grulerDiv = document.createElement('div');  
        	var gruler = new GRulerControl(grulerDiv, map);  
        	grulerDiv.index = 1; 
        	map.controls[google.maps.ControlPosition.TOP_LEFT].push(grulerDiv);        	
        	$.post("../QueryDeviceAndGPS.do", {mapid: "<%=mapId%>"}, function(data){
					devAndGPSArray = eval("(" + data + ")");
					for(var devNumber=0; devNumber<devAndGPSArray.length; devNumber++){
						xGPS = devAndGPSArray[devNumber].xGPS;
						yGPS = devAndGPSArray[devNumber].yGPS;
						worktype = devAndGPSArray[devNumber].worktype;
						if(worktype == "油漆工"){
							image = "../images/youqi.png";
						}else if(worktype == "瓦工"){
							image = "../images/wagong.png";
						}else if(worktype == "水电工"){
							image = "../images/water.png";
						}
						newMarker[devNumber] = new google.maps.Marker({
						    position: map.IndoorPointToLatLng(xGPS, yGPS),
						    label:{text: devAndGPSArray[devNumber].alias},
						    map: map,
						    icon: image
						});
						var tempMarker = newMarker[devNumber];
						google.maps.event.addListener(tempMarker, "click",(function(temMarker,devNumber) {
							return function (){
							      infowindow[devNumber] = new google.maps.InfoWindow({
						          content: "<div>设备：" +devAndGPSArray[devNumber].deveui+"<br>姓名：" +devAndGPSArray[devNumber].alias+"<br>工种：" + devAndGPSArray[devNumber].worktype  +"<br>时间：" + devAndGPSArray[devNumber].time + "</div>"
							        });
							    infowindow[devNumber].open(map, newMarker[devNumber]);
							    }
						    })(tempMarker,devNumber));
			           }
				}); 
        }
       
        var webSocketAddr = "<%=InetAddress.getLocalHost().getHostAddress()%>";
        var webSocket;
        if(webSocketAddr.indexOf("192.168") >= 0)
        	webSocket = new WebSocket("ws://127.0.0.1/asset/NodeQueryWS.do"); 
        else if(webSocketAddr.indexOf("10.31.84.143") >= 0){
       	 webSocket = new WebSocket("wss://dev.lansitec.com/asset/NodeQueryWS.do");
        }
        else{
        	webSocket = new WebSocket("ws://120.27.104.163/asset/NodeQueryWS.do");
       }
     
        webSocket.onerror = function(event) { onError(event) }; 
        webSocket.onopen = function(event) { onOpen(event) }; 
        webSocket.onmessage = function(event) { onMessage(event) }; 
        webSocket.onclose = function(event) { onClose(event) };
       
        function getMarkerIndex()
        {
        	var markers = new Array();
        	this.getIndex = function(deveui){
        		var devIndex = 0;
        		for(devIndex in markers)
        		{
        			if(markers[devIndex] == deveui)
        				return devIndex;
        		}
      
        		markers.push(deveui);
        		return markers.length-1;
        	};
        }
        var markerIndexMgr = new getMarkerIndex();
 
        var trackerListUp = new Array();
        var trackerListDown = new Array();
        var direc = new Array();
        var dtime;
       function onMessage(event) 
       { 
    	   var latlng;
          	   		
           var obj = eval("(" + event.data + ")");	
		   
           if(obj.msgType == "INDOOR" && obj.mapid == "<%=mapId%>")
           {
        	  
        	  devIndex = markerIndexMgr.getIndex(obj.DevEUI);
        	  if(null != newMarker[devIndex])
        	      newMarker[devIndex].setMap(null);
        	  for(var i = 0; i < devAndGPSArray.length; i++) {
        		  if(null !=  newMarker[i] && devAndGPSArray[i].deveui == obj.DevEUI){
        			  newMarker[i].setMap(null);
        		//	  devIndex = i;
        			  break;
        		  }
        	  }
           
		      latlng = map.IndoorPointToLatLng(obj.xGPS, obj.yGPS);	
		      
		      if(obj.DevEUI == "ef010001")
		    	  obj.DevEUI = "004A770211030031";
		      else if(obj.DevEUI == "ef010002")
		    	  obj.DevEUI = "004A770211030032";
		      else  if(obj.DevEUI == "ef010003")
		    	  obj.DevEUI = "004A770211030033";
		      else  if(obj.DevEUI == "ef010004")
		    	  obj.DevEUI = "004A770211030034";
		      else  if(obj.DevEUI == "ef010005")
		    	  obj.DevEUI = "004A770211030035";
		      
		      var lastPoint = latlng;
		      if(direc[devIndex] == undefined)
		    	  direc[devIndex] = 1;
		      if(obj.endPath == 1)
		    	  direc[devIndex] = 0; //上行
		      else if(obj.endPath == 2)
		    	  direc[devIndex] = 1;//下行
		    	  
		      if(showTracker == 1 && trackerListUp[devIndex] != undefined)
		      {
		    	  var trackerPath = trackerListUp[devIndex].getPath();
	 		      trackerPath.push(latlng);  
		    	  /* if(direc[devIndex] == 0)
		    	  {
			    	  var trackerPath = trackerListUp[devIndex].getPath();
			    	  if(trackerPath.getLength() == 0)
		 		    	  trackerPath.push(latlng);  
			    	  else{
			    		  var tLen;
			    		  var oLen = trackerPath.getLength();
			    		  for(tLen = 0; tLen < oLen; tLen++)
				    	  {
				    	  		var tLat = trackerPath.getAt(tLen);
				    	  		if(latlng.lng() >= tLat.lng())
				    	  		{
				    	  			trackerPath.insertAt(tLen, tLat);
				    	  			break;
				    	  		}
				    	  }
			    		  if(tLen == oLen)
			    			  trackerPath.push(latlng);
			    		  
			    		  trackerListUp[devIndex].setPath(trackerPath);
			    		  tLen = trackerPath.getLength()-1;
			    		  lastPoint = trackerPath.getAt(tLen);
			    	  }
					}
			    	else if(direc[devIndex] == 1)
			    	  {
				    	  var trackerPath = trackerListDown[devIndex].getPath();
				    	  if(trackerPath.getLength() == 0)
			 		    	  trackerPath.push(latlng);  
				    	  else{
				    		  var tLen;
				    		  var oLen = trackerPath.getLength();
				    		  for(tLen = 0; tLen < oLen; tLen++)
					    	  {
					    	  		var tLat = trackerPath.getAt(tLen);
					    	  		if(latlng.lng() <= tLat.lng())
					    	  		{
					    	  			trackerPath.insertAt(tLen, tLat);
					    	  			break;
					    	  		}
					    	  }
				    		  if(tLen == oLen)
				    			  trackerPath.push(latlng);
				    		  
				    		  trackerListDown[devIndex].setPath(trackerPath);
				    		  tLen = trackerPath.getLength()-1;
				    		  lastPoint = trackerPath.getAt(tLen);
						} 
		    	  } */
		      }
		      
		      dtime = new Date().format("MM-dd hh:mm:ss");
		      if(obj.worktype == "油漆工"){
					image = "../images/youqi.png";
				}else if(obj.worktype == "瓦工"){
					image = "../images/wagong.png";
				}else if(obj.worktype == "水电工"){
					image = "../images/water.png";
				}
 			  newMarker[devIndex] = new google.maps.Marker({
		    	position:lastPoint,
		    	map:map,
		    	label:{text: obj.alias},
		    	draggable:false,
		    	icon: image
			  });
 			  google.maps.event.addListener(newMarker[devIndex], 'click',
				    function() {
					var infowindow = new google.maps.InfoWindow({
				          content: "<div>设备：" +obj.DevEUI+"<br>姓名：" + obj.alias+"<br>时间：" + dtime + "</div>"
				        });
					infowindow.open(map, newMarker[devIndex]);
				    }
				);
 					/*	map.panTo(latlng); */
          }
       } 
       function onOpen(event) 
       {   
    		webSocket.send("NME" + "<%=mapId%>");
       } 
       function onClose(event) 
       { 
       	 //document.getElementById("messages").innerHTML += event.data+ "\n";
       } 
       function onError(event) 
       { 
    		if(event.data != undefined)
    			alert("连接已断开!" + event.data);
    		else
    			alert("连接已断开!");
       }
       function GRulerControl(container, map) {  		
   		uList = document.createElement("ul");
   		//addClass(uList, "nav navbar-nav");
   		uList.setAttribute("class", "nav navbar-nav");
   		uList.style.marginTop="0px";
   		
   		// “启用/禁用”按钮
   		li1 = document.createElement("li");
   		
   		lbutton = document.createElement("input");
   		lbutton.setAttribute("type", "button");
   		lbutton.setAttribute("value", "显示信标");
   		lbutton.style.padding = "2px";
   		lbutton.setAttribute("class", "btn btn-info");
   		lbutton.onclick = function(){

   					if(lbutton.getAttribute("value") == "显示信标")			
   					{
   						var tName = "<%=sName%>";
   						$.get("../asset/beacon", {usrname: tName}, function(data){
   							var result = eval("(" + data + ")");
   			       			if(result.status == "0")
   			       			{
   			       				var beaconlist = result.list;
   			       				var i;
   								lbutton.setAttribute("value", "隐藏信标");
   								
   			       				for(i in beaconlist)
   			       				{
   			       					if (beaconlist[i].flor == "<%=mapId%>"){
   			      	    	        var sw_lati = map.sw_lati_;
   			      	    	        var sw_long = map.sw_long_;
   			 	    	   
   			      	    	        if(sw_lati == undefined)
   			      	    	        {
   			      	    	        	sw_lati = beaconlist[i].lati;
   			      	    	            sw_long = beaconlist[i].longi;
   			      	    	        }
   			      	    	        else
   			      	    	        {
   	   			 	    	   			sw_lati = sw_lati + (beaconlist[i].lati - sw_lati) * map.zoom_;
   	   			 	    	   			sw_long = sw_long + (beaconlist[i].longi - sw_long) * map.zoom_;	
   			      	    	        }

   			       					
   			 	    	   		    var newpoint = new google.maps.LatLng(sw_lati, sw_long);
   			 	    	   		
   			 	    	   			beaconmarkers[i] = new google.maps.Marker({
   			       			    		position:newpoint,
   			     		    			map:map,
   			     		    			draggable:false,
   			     		    			label: {
   			     		    				color: "blue",
   			     		    				fontSize: "18px",
   			     		    				text: "Major/Minor: " + beaconlist[i].major+"/"+beaconlist[i].minor
   			     		    			},
   			     		    			icon: "../images/ble.png"
   			     					});
   			       					//marker1.enableDragging();
   			       					//marker1.setLabel(new BMap.Label(punchPoint.name,{offset:new BMap.Size(20,-10)}));
   			       					//marker1.addEventListener("dragend", dragendPunch);
   			       					beaconmarkers[i].setMap(map);
   			       					//addPunchMenu(marker1);
   			       				}
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
   	       					beaconmarkers[i].setMap(null); 
   	       					//addPunchMenu(marker1);
   	       				}
	       					lbutton.setAttribute("value", "显示信标");
   					}
   				};//function

   		li1.appendChild(lbutton);
   		uList.appendChild(li1);
   	
     li2 = document.createElement("li");
   		
   		l2button = document.createElement("input");
   		l2button.setAttribute("type", "button");
   		l2button.setAttribute("value", "显示轨迹");
   		l2button.style.padding = "2px";
   		l2button.setAttribute("class", "btn btn-info");
   		l2button.onclick = function(){

   					if(l2button.getAttribute("value") == "显示轨迹")			
   					{
   						showTracker = 1;
   						l2button.setAttribute("value", "隐藏轨迹");
   						
   						$.post("../GetRegisterDevices.do", {type: "ALL", usrname: "<%=mapId%>"}, function(data){
   							var devArray = eval("(" + data + ")");
   							var totalEui = devArray.length;

   							for(var devNumber=0; devNumber<devArray.length; devNumber++)
   							{
   						    	var color1;
   						    	var color2;
   						    	if(0==devNumber % 5)
   						    	{
   						    		color1="red";
   						    		color2="black";
   						    	}
   						    	else if(1==devNumber % 5)
   						    	{
   						    		color1="blue";
   						    		color2="navy";
   						    	}
   						    	else if(2==devNumber % 5)
   						    	{
   						    		color1="green";
   						    		color2="green";
   						    	}
   						    	else if(3==devNumber % 5)
   						    	{
   						    		color1="black";
   						    		color2="black";
   						    	}
   						    	else if(4==devNumber % 5)
   						    	{
   						    		color1="navy";
   						    		color2="navy";
   						    	}
   						    	
   								trackerListUp[devNumber] = new google.maps.Polyline({
     						          strokeColor: color1,
       						          strokeOpacity: 1.0,
       						          strokeWeight: 2
       						        });
   								trackerListUp[devNumber].setMap(map);
   								trackerListDown[devNumber] = new google.maps.Polyline({
   						          strokeColor: color2,
     						          strokeOpacity: 1.0,
     						          strokeWeight: 2
     						        });
 								trackerListDown[devNumber].setMap(map);
   								//devStatus[devNumber] = [{'val':devArray[devNumber]},{'val':'未知'},{'val':'未知'},{'val':'未知'},{'val':'未知'},{'val':'未知'}];
   							}

   						});  			       				
   					}
   					else
   					{
   						showTracker = 0;
   						l2button.setAttribute("value", "显示轨迹");
   	       				for(i in trackerListUp)
   	       				{
   	       					trackerListUp[i].setMap(null); 
   	       					//addPunchMenu(marker1);
   	       				}
   	       			    for(i in trackerListDown)
	       				{
	       					trackerListDown[i].setMap(null);
	       					//addPunchMenu(marker1);
	       				}
   					}
   				};//function

   		li2.appendChild(l2button);
   		uList.appendChild(li2);
   		container.appendChild(uList);
   	   			 
   		//map.getContainer().appendChild(container);
   	}	
    </script>
</html>