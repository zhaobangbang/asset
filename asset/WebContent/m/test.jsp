<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0;" />
<meta content="yes" name="apple-mobile-web-app-capable" />
<link href="styles/NewGlobal.css" rel="stylesheet" />

<script type="text/javascript"
	src="http://ajax.microsoft.com/ajax/jquery/jquery-1.4.min.js"></script>
<title>Insert title here</title>

</head>
<body>
	<div class="container width100 pt20">
		enter simulated node position message:
		<form action="../NodeStatusUpdate.do" method="post">
			<p>
				Ring MAC: <input type="text" id="mac" name="mac"
					value="00:4a:77:00:66:ff:fe:23" />
			</p>
			<p>
				mType <input type="text" id="mType" name="mType" />
			</p>
			<p>
				xGPS <input type="text" id="xgps" name="xgps" value="118.32" />
			</p>
			<p>
				yGPS <input type="text" id="ygps" name="ygps" value="32.31" />
			</p>
			<p>
				Datr <input type="text" id="Datr" name="Datr" value="datr" />
			</p>
			<p>
				Rssi <input type="text" id="Rssi" name="Rssi" value="-32" />
			</p>
			<p>
				Freq <input type="text" id="Freq" name="Freq" value="923.4" />
			</p>
			<p>
				Recv <input type="text" id="Recv" name="Recv" value="datr" />
			</p>
			<input type="button" id="datasend" value="Submit" />
		</form>
	</div>
</body>
<script>

$(document).ready(function(){
	$("#datasend").click(function(){
		var testdate = {
				 DevEUI: $(".mac").val(),
				    passwd: $(".text-passwd").val(),
				    tel: $(".text-tel").val(),
				    uid: $(".text-uid").val(),
				    email: $(".text-email").val(),
				    pkey: $(".text-pkey").val(),
				    city: $(".text-city").val()
				};
				     //调用AJAX 
				     $.ajax(
				    		 {
				    			 async: false,
				    			 type: "POST",
				    			 url: "../NodeStatusUpdate.do",
				    			 data: testdate,
				    			// dataType: 'json',
				    			 success: function(data) {
				    				 
				    	
				    			var status = eval("("+data+")");
				    			
				    				 if (status == "OK") {
				    					 
				    					 if($(".jump").css("display")=='none'){//如果show是隐藏的

				    						 $(".jump").css("display","block");//show的display属性设置为block（显示）

				    						 }  
				    		    					 
									}
				    			 }
				    		 });
	});
	});
</script>
</html>