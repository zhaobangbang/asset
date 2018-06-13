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
<title>项目管理员</title>
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
	    	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" class="btn" id="find_btn" style="background-color:#7ebfb7;" value="查 询" onclick="triggerQueryJQGrid();"/> 
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
	    
	    
		var fieldSelected;
		var citySelected;
		var companySelected;
		
		
		function rowClicked(rowid, icol, cellCont) {
			var rowData = $("#jqGrid").jqGrid("getRowData",rowid);
			fieldSelected = rowData.field;
			citySelected = rowData.city;
			companySelected = rowData.company;
		}
		
		
		function refreshCompany(mode) {
	                 var companySel = $("select#company");
			$.get("../CompanyManager/doGet.do", function(str) {
	                 companySel.empty();
	                 companySel.append(str);
	                 
	                 if (mode == "edit") {
	                	 companySel.val(companySelected);
	 	             }
                 $.get("../CompanyManager/doGet.do?name=" + companySel.val(), function(str) {
     				company = eval("(" + str + ")");
     				refreshField(company, mode);
                 });
             });
	             }
	                 
		
		function refreshField(companySel,mode) {
	                 var fieldSel = $("select#field");
			$.get("../FieldManager/doGet.do?ComName="+encodeURIComponent(companySel,'UTF-8'), function(str) {
	                 fieldSel.empty();
	                 fieldSel.append(str);
	                 
	                 if (mode == "edit") {
	                	 fieldSel.val(fieldSelected);
	 	            }
                $.get("../FieldManager/doGet.do?name=" + fieldSel.val(), function(str) {
    				field = eval("(" + str + ")");
        			refreshCity(field, mode);
                });
            });
	             }
		
		function refreshCity(fieldSel,mode) {
			$.get("../CityManager/doGet.do?FieldName="+encodeURIComponent(fieldSel,'UTF-8'), function(str) {
               	var citySel = $("select#city");
                citySel.empty();
                citySel.append(str);
	                 
                if (mode == "edit") {
               		citySel.val(citySelected);
	            }
            });
		}
		
		 function validBeforeSubmit(postData, formid) {
			 var qcompany = $("#company").val();
			 var qfield = $("#field").val();

			 var company;
			 var field;
			 
			 $.ajax({
    			 async: false,
    			 type: "GET",
    			 url: "../CompanyManager/doGet.do?name=" + qcompany,
    			 success: function(data) {
     				company = eval("(" + data + ")");
     				postData.company = company;
    			 }
             });
			 
			 $.ajax({
    			 async: false,
    			 type: "GET",
    			 url: "../FieldManager/doGet.do?name=" + qfield,
    			 success: function(data) {
     				field = eval("(" + data + ")");
     				postData.field = field;
    			 }
             });

			 return [true,"success"];	
		 }
		 
	    
		function GetQueryString(name) {
	    	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)","i");
			var r = window.location.search.substr(1).match(reg);
			if (r!=null) return (r[2]); return null;
		}
		var inputName = GetQueryString("name");
		 
		
		$(document).ready(function() {
	    	jQuery("#jqGrid").jqGrid({
				autowidth: false,
			    datatype: "json",
			    mtype:"POST",
			    height: 'auto',
	    		autowidth:true,
	    		colNames:[ '用户ID', '用户名', '密码',"电话","邮箱","注册时间","所属公司","所在城市","所在工地","权限"],
	    		colModel:[
	    			{name:"id",index:"id", width:50,align:"center",editable:false, hidden:true},
	    		    {name:"username",index:"username", width:110,align:"center",sortable:true,editable:true},
	    		    {name:'userkey',index:'userkey', hidden:true, editable:true, editrules: { edithidden: true },align:"center",sortable:true,width:30,edittype:"password"},
	    			{name:"tel",index:"usertel", width:150, align:"center",editable:true},
	    		    {name:"usermail",index:"usermail", width:300, align:"center",editable:true},
	    		    {name:"time",index:"time", width:250, align:"center",editable:false},
	    		    {name:"company",index:"company", width:50, align:"center",sortable:true,editable:true,
    	            	edittype:"select", editoptions:{value:":",
    	        			dataEvents: [{
    	        			            type: 'change', fn: function(e) {
    	        			            	$.get("../CompanyManager/doGet.do?name=" + this.value, function(str) {
    	        			     				company = eval("(" + str + ")");
    	        			     				refreshField(company);
    	        			                 });
    	        			            }
    	        			           }
    	        			      ]}},
	    		    {name:"field",index:"field", width:50, align:"center",sortable:true,editable:true,
    	            	edittype:"select", editoptions:{value:":",
    	        			dataEvents: [{
    	        			            type: 'change', fn: function(e) {
    	        			            	$.get("../FieldManager/doGet.do?name=" + this.value, function(str) {
    	        			    				field = eval("(" + str + ")");
    	        			        			refreshCity(field);
    	        			                }); 
    	        			            }
    	        			           }
    	        			      ]}},
	    		    {name:"city",index:"city",align:"center",editable:true,edittype:"select",editoptions:{value:":"}},
	    		     {name:"prio",index:"prio",width:160,align:"center",editable:true, edittype:"select", 
			             editoptions:{value:"公司级:公司级;城市级:城市级;工地级:工地级"}}
	    			 ],
		    		 url: "../ProjectManagers/doPost.do",  
		    	     editurl:"../ProjectManagers/doPost.do", 
		    	     pager:"#jqGridPager",
		    	     onCellSelect:rowClicked,
		    	     rowNum: 8,  //设置行数
		    	     viewrecords:true,//总记录数
	    	         jsonReader:{
	    	             repeatitems:false
	    	         },
		    	});	

	    	    jQuery("#jqGrid").jqGrid('navGrid', '#jqGridPager',{edittext: '编辑', addtext: '添加', deltext: '删除', search: false},{
	    	        	reloadAfterSubmit : true,
	    	            closeAfterEdit:true,
	    	            closeOnEscape:true,
	    	            beforeSubmit: validBeforeSubmit,
	    	            beforeShowForm: function(formid) {
	    	                refreshCompany("edit");
	    	            }
	    	          },//edit options
	    	          {  
	    	               reloadAfterSubmit : true,//点击“提交”后重新加载表格
	    	               closeAfterAdd:true,
	    	               closeOnEscape:true,
	    	               beforeSubmit: validBeforeSubmit,
	    	               beforeShowForm: function(formid) {
	    	                	refreshCompany("add");
	    	               }
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