<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1,maximum-scale=1, user-scalable=no">
    <script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
    <script type="text/javascript" src="../js/grid.locale-cn.js"></script>
    <script type="text/javascript" src="../js/jquery.jqGrid.min.js"></script>

    <link rel="stylesheet" type="text/css" media="screen" href="../css/jquery-ui.css" />
    <link rel="stylesheet" type="text/css" media="screen" href="../css/ui.jqgrid.css" />
    <link rel="SHORTCUT ICON" href="../image/favicon.ico"/>
<title>系统管理员</title>
<style>
	html,body {
		font-size: 75%;
	}
	.name1 {
		display:block; 
	    color:#fff; 
	    font:13px/20px Georgia, "Times New Roman", Times, serif; text-decoration:none;
	 }
    .name2 {
        color:#000; 
        font:13px/20px Georgia, "Times New Roman", Times, serif; text-decoration:none;
        background-color: #e3e1e2;
     }
</style>
</head>
<body>
	<div> 
		<div id="query"> 
	    	<input type="submit" class="btn" style="background-color:#e3e1e2;" value="用户名" disabled="disabled"/>
	    	<input type="text" class="input" id="qusrname" style="width:100px;"/>&nbsp;&nbsp;&nbsp;&nbsp;
	    	<input type="submit" class="btn" style="background-color:#e3e1e2;" value="邮箱" disabled="disabled"/>
	    	<input type="text" class="input" id="qmail" style="width:140px;"/>&nbsp;&nbsp;&nbsp;&nbsp;
	    	<input type="submit" class="btn" id="find_btn" style="background-color:#7ebfb7;" value="查 询" onclick="triggerQueryJQGrid();"/> 
	    	<input type="submit" class="btn" id="clear_btn" style="background-color:#7ebfb7;" value="重置" onclick="triggerClearQueryJQGrid();"/> 
	    </div>
		<p></p>
		<table id="jqGrid"></table>
		<div id="jqGridPager"></div>
	</div>
	
	<script type="text/javascript">
	    //查询 
		function triggerQueryJQGrid() {
			var qUsrname = $("#qusrname").attr("value");
			var qMail = $("#qmail").attr("value");
			
			if ((qUsrname == "") && (qMail == "")) {
				return;
			}
			
			var curPostData = $("#jqGrid").getGridParam("postData");
			if (qUsrname != "") {
				$.extend(curPostData, { username: qUsrname });
			}
			if (qMail != "") {
				$.extend(curPostData, { usermail: qMail });
			}
			
			$("#jqGrid").trigger('reloadGrid');
		}
		
	    //重置 
		function triggerClearQueryJQGrid() {
			
			$("#qusrname").attr("value","");
			$("#qmail").attr("value","");
			
			var curPostData = $("#jqGrid").getGridParam("postData");
			delete curPostData["username"];
			delete curPostData["usermail"];
			$("#jqGrid").trigger('reloadGrid');
		}
	    
		 /**
		 根据用户名首先获得用户权限,根据用户权限重新设置用户表格显示内容.
		 */
		function GetQueryString(name) {
	    	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)","i");
			var r = window.location.search.substr(1).match(reg);
			if (r!=null) return (r[2]); 
			return null;
		}
		var inputName = GetQueryString("name");
		 
		$(document).ready(function() {

	    	jQuery("#jqGrid").jqGrid({
	    	    autowidth: false,
	    	    datatype: "json",
	    	    mtype:"POST",
	    	    height: 'auto',
	    		autowidth:true,
   				colNames:[ '用户ID', '用户名', '密码',"电话","邮箱","注册时间"],
   				colModel:[
   					{name:"id",index:"id",width:50,align:"center",editable:false, hidden:true},
   				    {name:"username",index:"username", width:110,align:"center",sortable:true,editable:true},
   				    {name:"userkey",index:"userkey",align:"center",edittype:"password",hidden:true,editable:true,editrules: { edithidden: true },align:"center",sortable:true,width:30},
   				    {name:"tel",index:"usertel", width:150, align:"center",editable:true},
   				    {name:"usermail",index:"usermail", width:300, align:"center",editable:true},
   				    {name:"time",index:"time", width:250, align:"center",editable:false}
   				 ],
    		   	url: "../AdminManager/doPost.do",  
    	       	editurl:"../AdminManager/doPost.do", 
    	       	pager:"#jqGridPager",
    	        rowNum: 20,  //设置行数
		    	        //onCellSelect:rowClicked,
    	        viewrecords:true,//总记录数
    	        jsonReader:{
    	            repeatitems:false
    	        },
    	    });	

   			jQuery("#jqGrid").jqGrid('navGrid', '#jqGridPager',{edittext: '编辑', addtext: '添加', deltext: '删除', search: false},{
 	        		reloadAfterSubmit : true,
   	            	closeAfterEdit:true,
   	            	closeOnEscape:true,
  	            	},//edit options
    	            {  
    	                reloadAfterSubmit : true,//点击“提交”后重新加载表格
    	                closeAfterAdd:true,
    	            	closeOnEscape:true,
    	            },//add options
    	            {  
    	            	closeOnEscape:true,
    	                reloadAfterSubmit : true,//点击“提交”后重新加载表格
    	            }, //del options
    	            {}, //search option
    	            {}
    	        );
		});
	</script>
</body>
</html>