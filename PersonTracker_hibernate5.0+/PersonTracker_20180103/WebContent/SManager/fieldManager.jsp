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
	<title>工地管理</title>
	<style>
		html,body {
			font-size: 75%;
		}
	</style>
</head>
<body>
	<table id="jqGridField"></table>
	<div id="jqGridPagerField"></div>
	
	<script type="text/javascript">
		function GetQueryString(name) {
			var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)","i");
		    var r = window.location.search.substr(1).match(reg);
		    if (r!=null) return (r[2]); 
		    	return null;
		}
		var inputName = GetQueryString("name");
		
		function refreshCompany(mode) {
			var companySel = $("select#company");			
			var qsn = snSelected;
			
			$.get("../CompanyManager/doGet.do", function(str) {
				companySel.empty();
				companySel.append(str);
		        	
		        if (mode == "edit" && qsn != undefined) {
		        	$.get("../CompanyManager/doGet.do?sn=" + qsn, function(str) {
		        		companySelected = str;
			   			companySel.val(companySelected);
				    });
		        }
			});				
		}
		
		function refreshCity(mode) {
			var citySel = $("select#city");			
			$.get("../CityManager/doGet.do", function(str) {
				citySel.empty();
		        citySel.append(str);
		        	
		        if (mode == "edit") {
		        	citySel.val(citySelected);
		        }
			});				
		}
		
		function validBeforeSubmit(postData, formid) {
			var qcompany = $("#company").val();

			var company;
			 
			$.ajax({
   			 async: false,
   			 type: "GET",
   			 url: "../CompanyManager/doGet.do?name=" + qcompany,
   			 success: function(data) {
    				company = eval("(" + data + ")");
    				postData.company = company;
   			 }
            });
			return [true,"success"];
		 }
		
		var citySelected;
		var companySelected;
		var snSelected;
		
		function rowClicked(rowid, icol, cellCont) {
			var rowData = $("#jqGridField").jqGrid("getRowData",rowid);
			snSelected = rowData.sn;
			companySelected = rowData.company;
			citySelected = rowData.city;
		}
		
		
		jQuery("#jqGridField").jqGrid({
	        url:"../FieldManager/doPost.do",
	        datatype: "json",
	        mtype:"POST",
	        height: 'auto',
    		autowidth:true,
	        colNames:[ '工地ID', '工地名称','sn','所属公司','所在城市','网关数量','终端数量'], 
	        colModel:[
				{name:"id",index:"id", width:50,align:"center",editable:false, hidden:true},
	            {name:"name",index:"name", width:300, align:"center",editable:true},
	            {name:"sn",index:"sn", width:300, align:"center",editable:false,hidden:true},
	            {name:"company",index:"company",align:"center",editable:true,hidden:true,editrules: { edithidden: true },edittype:"select",editoptions:{value:":"}},
	            {name:"city",index:"city",align:"center",editable:true,edittype:"select",editoptions:{value:":"}},
	            {name:"gwnumber",index:"gwnumber", width:300, align:"center",editable:false},
	            {name:"motenumber",index:"motenumber", width:300, align:"center",editable:false}
	        ],
	        pager:"#jqGridPagerField",
	        onCellSelect:rowClicked,
	        rowNum: 8,  //设置行数
	        viewrecords:true,//总记录数
	        caption: "工地信息",
	        jsonReader:{
	            repeatitems:false
	            },
	        editurl:"../FieldManager/doPost.do"
	    });
		
		jQuery("#jqGridField").jqGrid('navGrid', '#jqGridPagerField',
    			{edittext: '编辑', addtext: '添加', deltext: '删除', search:false

            	},
	            {
	            	reloadAfterSubmit : true,
	            	closeOnEscape:true,
	            	closeAfterEdit:true,
	            	beforeSubmit: validBeforeSubmit,
	            	beforeShowForm: function(formid) {
	            		refreshCompany("edit");
		                refreshCity("edit");
		            }
	            },//edit options
	            {  
	                reloadAfterSubmit : true,//点击“提交”后重新加载表格
	            	closeOnEscape:true,
	                closeAfterAdd:true,
	                beforeSubmit: validBeforeSubmit,
	                beforeShowForm: function(formid) {
	                	refreshCompany("add");
		                refreshCity("add");
		            }
	            },//add options
	            {  
	            	closeOnEscape:true,
	                reloadAfterSubmit : true,//点击“提交”后重新加载表格
	            }, //del options
	            {}, //search option
	            {}
	    );
	</script>
</body>
</html>