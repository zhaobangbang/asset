<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

    <link href="../zoom/css/fonts.css" rel="stylesheet" />
 
    <link rel="stylesheet" href="../css/bootstrap/css/bootstrap.min.css" />
    <link href="../zoom/css/style.css" rel="stylesheet" />
    
    <script src="../scripts/jquery-2.1.4.min.js"></script>
    <script src="../zoom/script/canvas-zoom.js"></script>
    <script src="../scripts/jquery.mousewheel.js"></script>
    
    <script type="text/javascript" src="../jedate/jquery.jedate.js"></script>
    <link type="text/css" rel="stylesheet" href="../jedate/skin/jedate.css">

    <style type="text/css">
    	body{
    		font-family:微软雅黑 ;
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

    <div class="traces" style="position: absolute; z-index: 10; top: 2px; right: auto; bottom: auto; left: 180px;">
        <nav class="navbar" style="cursor:pointer;">
            <ul class="nav navbar-nav" style="margin-top: -15px;">
            	<li style="float:left;">
            		<span>开始日期</span>
            		<input class="datainp wicon" id="datebut1" style="padding:2px; font-size: 16px;" type="text" placeholder="开始日期" value="" readonly>
            	</li>
            	<li style="float:left;">
            		<span>结束日期</span>
            		<input class="datainp wicon" id="datebut2" style="padding:2px; font-size: 16px;" type="text" placeholder="结束日期" value="" readonly>
            	</li>
            	<li style="float:left;">
            		<input id="search" type="button" class="btn btn-info" value="查询" style="padding:2px; font-size: 16px;">
            	</li>
            	<li style="float:left;">
            		<input id="reset" type="button" class="btn btn-info" value="重置" style="padding:2px; font-size: 16px;">
            	</li>
            </ul>
        </nav>
    </div>

    <script type="text/javascript">
        var devArray = new Array();
        var selectdev = null;
        var MAPINFO = [];
        var markerInfo = [];
        
    	var canvas = document.getElementById("indoormap");
        var context2D = canvas.getContext("2d");

        var clientWidth = document.documentElement.clientWidth;
        var clientHeight = document.documentElement.clientHeight;

        var zoomScale = 1;
        if (clientWidth <= clientHeight) {
            zoomScale = clientWidth / 720;
        } else {
            zoomScale = clientHeight / 720;
        }

        var marImg = new Image();
        marImg.onLoad = function() {
            
        }
        marImg.src = '';

        var sX, sY, osX, osY; 
        
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

        //地图信息
        function DrawMapInfo(scaleX, scaleY, offsetX, offsetY) {
        	sX = scaleX;
			sY = scaleY;
			osX = offsetX;
			osY = offsetY;
            for (i = 0; i < MAPINFO.length; i++) {
                DrawBlock(MAPINFO[i].x * scaleX + offsetX, MAPINFO[i].y * scaleY + offsetY, MAPINFO[i].width * scaleX, MAPINFO[i].height * scaleY, MAPINFO[i].imageurl,MAPINFO[i].color);
            }
           // DrawRangInfo(sX, sY, osX, osY);
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
            
        }


        //1.时间段 
		var start = {
		    format: 'YYYY-MM-DD hh:mm:ss',
		    minDate: '2016-07-16 23:59:59', 
		    festival:true,
		    maxDate: $.nowDate(0), 
		    choosefun: function(elem,datas) {
		        end.minDate = datas; 
		    }
		};
		
		var end = {
			format: 'YYYY-MM-DD hh:mm:ss',
		    minDate: $.nowDate(0), //设定最小日期为当前日期
		    festival: true,
		    maxDate: '2099-07-16 23:59:59', //最大日期
		    choosefun: function(elem,datas){
		        start.maxDate = datas; //将结束日的初始值设定为开始日的最大日期
		    }
		};
		
		$("#datebut1").jeDate(start);
		$("#datebut2").jeDate(end);
		
		function testShow(elem) {
	        $.jeDate(elem,{
	            insTrigger: false,
	            isinitVal: true,
	            festival: true,
	            ishmsVal: false,
	            minDate: '2016-12-16 23:59:59',
	            maxDate: $.nowDate(0),
	            format:"hh:mm",
	            zIndex: 3000,
	        })
	    }

        //3.查询事件
        var rangInfo = []; 
        var searchBtn = document.getElementById("search"); 
        searchBtn.onclick = function() {
        	if(selectdev.value >= 0) {
        		var index = selectdev.selectedIndex; 
                var text = selectdev.options[index].text;
                
                var datebut1 = $("#datebut1").val();
    	    	var datebut2 = $("#datebut2").val();
    	    	
    	    	if((datebut1 != "") && (datebut2 != "")) { 
    	    		$.post("../GetHistoryPosition/doPost.do?deveui="+ text + "&datebut1=" + datebut1 +"&datebut2="+ datebut2,function(data) {
    	    		
    	    			for(i = 0; i < data.length; i++) {
    	    				var pos = {
			                	x: data[i].x,
			                	y: data[i].y
			                }
    	    				rangInfo.push(pos);
		            	}
					});
    	    	}else{
    	    		alert("时间不能为空!");
    	    	}
        		
        		DrawRangInfo(sX, sY, osX, osY);
        	}else
				alert("请选择设备!");
        };

        //4.重置事件
        var resetBtn = document.getElementById("reset"); 
        resetBtn.onclick = function() {
        	rangInfo.length = 0;
        	$("#datebut1").val("");
			$("#datebut2").val("");
        };

        //画历史轨迹
        var x1,y1,x2,y2;
        
        function DrawRangInfo(scaleX, scaleY, offsetX, offsetY) {
			for (t = 0; t < rangInfo.length; t++) {
		    	drawTraces(rangInfo[t].x * scaleX + offsetX, rangInfo[t].y * scaleY + offsetY, t);
		    }
        }
        
        function drawTraces(x, y, t) {
			context2D.beginPath();
			context2D.strokeStyle = "blue";
			context2D.lineWidth = "1";

			if(t == 0) {
				x1 = x;
				y1 = y;
				x2 = x1;
				y2 = y1;
				context2D.moveTo(x1, y1);   
				context2D.lineTo(x2, y2);
				
				var image1 = new Image();
	            image1.src = '../images/start.png';
	            context2D.drawImage(image1, x-15, y-15, 64, 64);
			}else {
				x1 = x2;   
				y1 = y2;
				x2 = x;
				y2 = y;
				context2D.moveTo(x1, y1);
				context2D.lineTo(x2, y2);
			}
			
			if(t == rangInfo.length-1) {
	    		var image2 = new Image();
	            image2.src = '../images/end.png';
	            context2D.drawImage(image2, x-15, y-15, 64, 64);
	    	}
			 
			context2D.stroke();
			context2D.closePath();
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