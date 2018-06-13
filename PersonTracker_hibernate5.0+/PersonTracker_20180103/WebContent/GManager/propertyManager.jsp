<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
    <script type="text/javascript" src="../js/grid.locale-cn.js"></script>
    <script type="text/javascript" src="../js/jquery.jqGrid.src.js"></script>
    <script type="text/javascript" src="../js/jquery-ui.min.js"></script>
    <link rel="stylesheet" type="text/css" media="screen" href="../css/jquery-ui.css" />
    <link rel="stylesheet" type="text/css" media="screen" href="../css/ui.jqgrid.css" />  
    <link rel="stylesheet" type="text/css" href="../css/jquery.my-modal.1.1.winStyle.css" />
    <link rel="SHORTCUT ICON" href="../image/favicon.ico"/>
    <!--<link rel="stylesheet" href="../css1/reset.css" />-->
    <link rel="stylesheet" href="../css/style2.css" />  
<title>资产管理</title>
<style>
	html,body {
		font-size: 75%;
	}
</style>
</head>
<body>
	<div> 
		<div id="query"> 
	    	<input type="submit" class="btn" style="background-color:#e3e1e2;" value="资产名称" disabled="disabled"/>
	    	<input type="text" class="input" id="name" style="width:140px;"/> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	    	<input type="submit" class="btn" id="find_btn" style="background-color:#7ebfb7;" value="查 询" onclick="triggerQueryJQGrid();"/> 
	    	<input type="submit" class="btn" id="clear_btn" style="background-color:#7ebfb7;" value="重置" onclick="triggerClearQueryJQGrid();"/> 
	    	&nbsp;&nbsp;&nbsp;&nbsp;
    	   <button style="background-color:#7ebfb7;" value="添加" onclick="addWin();">添加</button>
	    </div>
		<p></p>
		<table id="jqGridPro"></table>
		<div id="jqGridPagerPro"></div>
	</div>
	
	<div class="m-modal">
		<div class="m-modal-dialog">
			<div class="m-top">
				<h4 class="m-modal-title">
					资产基本信息
				</h4>
				<span class="m-modal-close">&times;</span>
			</div>
			<div class="m-middle">
				<p>&nbsp;资产名称:&nbsp;<input id="editName" type="text" /></p>
				<p>&nbsp;状态:&nbsp;<select id="editStatus">
							<option value=""> </option>
						 	<option value="正常">正常</option>
						 	<option value="维修">维修</option>
						 	<option value="闲置">闲置</option>
						</select>
				</p>
				<p>&nbsp;所属工地:&nbsp;<select id="editFid">
						</select>
				</p>
				<p>&nbsp;资产类型:&nbsp;<select id="editType">
							<option value=""> </option>
						 	<option value="挖掘机">挖掘机</option>
						 	<option value="搅拌机">搅拌机</option>
						</select>
				</p>
				<p id="IdDev">&nbsp;设备号:&nbsp;<input id="editDeveui" type="text"></p>
				<p id="IdPurchase" class="calendarWarp">&nbsp;采购时间:&nbsp;
					<input type="text" name="date" class='ECalendar' id="editPurchase"/>
				</p>
				
				<p id="IdPurchase">&nbsp;备注:&nbsp;<input id="editMemo" type="text"></p>
			</div>
			<div class="m-bottom">
				<button id="add" class="m-btn-add" onclick="addOper();">确定</button>
				<button id="edit" class="m-btn-edit" onclick="editOper();">编辑</button>
				<button id="del" class="m-btn-del" onclick="delOper();">删除</button>
				<button class="m-btn-cancel">取消</button>
			</div>
		</div>
	</div>
	
	<script type="text/javascript" src="../js/jquery.my-modal.1.1.js"></script>
	<script src="../js/Ecalendar.jquery.min.js"></script>
	<script type="text/javascript">
		$(function(){
			$("#editPurchase").ECalendar({
				 type:"date",
				 skin:"#248",
				 offset:[0,2]
			});
		})
	</script>
	<script type="text/javascript">
		var oper = null;
		var f_sn = null;
		var id = null;
		var fid_name = null;
	
		function GetQueryString(name) {
	    	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)","i");
	    	var r = window.location.search.substr(1).match(reg);
	    	if (r!=null) return (r[2]); return null;
	   }
		
	    //根据用户名获取名下工地
		var select = $("select##editFid");	
 	    $.get("../FieldManager/doGet.do?username="+GetQueryString("name"), function(str) {
    		 select.empty();
    		 select.append(str);
		});
 	    
		/*用于触发按钮查询*/	
	   function triggerQueryJQGrid() {
		    var property = $("#name").val();
			if (property == "") {
				return;
			}
			
			var curPostData = $("#jqGridPro").getGridParam("postData");
			$("#jqGridPro").jqGrid('setGridParam',{
				url:"../AssetInfoManagement/doPost.do?usrname="+ GetQueryString("name") + "&name=" + property,
				page:1
			}).trigger('reloadGrid');
		}
		
	   /*用于触发重置查询*/ 
	   function triggerClearQueryJQGrid() {
			 $("#name").val("");
			
			var curPostData = $("#jqGridPro").getGridParam("postData");
			delete curPostData["name"];
			
			$("#jqGridPro").jqGrid('setGridParam',{
				url:"../AssetInfoManagement/doPost.do?usrname="+GetQueryString("name")
			}).trigger('reloadGrid'); 

	   }
	   
	   //根据工地名称获取工地sn
	   function fidSn() {
		   var f_name = $("#editFid").val();
		   $.ajax({
	   			 async: false,
	   			 type: "GET",
				 url: "../FieldManager/doGet.do?name=" + f_name,
	   			 success: function(data) {
	 				f_sn = eval("(" + data + ")");
	   			 }
		     });
	   }
	   
	   var m1 = null;
	   /*用于触发添加事件*/
 	   function addWin() {
 		  m1 = new MyModal.modal(function() {
 			  
		  });
 		
 		  $("#IdDev").css('display','none');
 		  $("#add").css('display','block');
		  $("#del").css('display','none');
		  $("#edit").css('display','none');
 		  
 		  $("#editName").val("");
 		  $("#editType").val("");
 		  $("#editStatus").val("");
 		  $("#editPurchase").val("");
 		  $("#editMemo").val("");
 		  
		  m1.show();
 	   }
	   
	   //用于触发确定事件 
	   function addOper() {
		   fidSn();
			 oper = "add";
			 
			 var AddInfo = {
				 assetname: $("#editName").val(),
				 f_sn: f_sn,
				 a_type: $("#editType").val(),
				 a_status: $("#editStatus").val(),
				 purchase: $("#editPurchase").val(),
				 memo: $("#editMemo").val(),
				 oper: oper
			 };
			 
			 $.ajax({
				async: false,
				type: "POST",
				url: "../AssetInfoManagement/doPost.do?usrname="+GetQueryString("name"),
				data: AddInfo,
				success: function(data) {
					        var status = data.status;
						    if (status == "ok") {				     					 			    					 
						    	alert("数据添加成功！");	    					
							}else{
								alert("数据添加失败！" + status);
							}
				   		}		
		      });
			 m1.hide();
			 
			  $("#jqGridPro").jqGrid('setGridParam',{
	 		  	url:"../AssetInfoManagement/doPost.do?usrname="+GetQueryString("name")
	 		  }).trigger('reloadGrid');
	   }
	   /*function validBeforeSubmit(postData, formid) {
			 var qfield = $("#field").val();

			 var field;
			 
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
		}*/
	   
	   /*function refreshField(mode) {
			var fieldSel = $("select#field");			
			var qsn = snSelected;
			
			$.get("../FieldManager/doGet.do?username="+ GetQueryString("name"), function(str) {
				fieldSel.empty();
				fieldSel.append(str);
		        	
				if (mode == "edit" && qsn != undefined) {
		        	$.get("../FieldManager/doGet.do?sn=" + qsn, function(str) {
	        		    fieldSelected = str;
	        		    fieldSel.val(fieldSelected);
				    });
		        }
			});				
		}
	   
	   function refreshType(mode) {
			var typeSel = $("select#type");
			if(mode == "edit"){
				typeSel.val(typeSelected);
			}
		}
	   
	   function refreshStatus(mode) {
		   var statusSel = $("select#status");
		   if(mode == "edit"){
			   statusSel.val(statusSelected);
		   } 
	   }*/
		
		
		//var fieldSelected;
		//var snSelected;
		//var typeSelected;
		//var statusSelected;
		
		function rowClicked(rowid, icol, cellCont) {
			var rowData = $("#jqGridPro").jqGrid("getRowData",rowid);
			a_sn = rowData.sn;
			id = rowData.id;
			//根据资产sn获取工地名称 
			$.ajax({
				 async: false,
				 type: "GET",
				 url: "../FieldManager/doGet.do?a_sn=" + a_sn,
				 success: function(data) {
	  				fid_name = data;
				 }
	        });
			
			$("#del").css('display','block');	
			$("#edit").css('display','block');
			$("#add").css('display','none');
			$("#IdDev").css('display','block');
			
			$("#editName").val(rowData.name);
			var e_dev = rowData.deveui;
			e_dev = e_dev.substr(12);
			$("#editDeveui").val(e_dev);
			$("#editPurchase").val(rowData.purchase);
			$("#editMemo").val(rowData.memo);
			
			var select1 = document.getElementById("editFid");
			for(var i = 0; i < select1.options.length; i++) {  
			    if(select1.options[i].innerHTML == fid_name){  
			        select1.options[i].selected = true;  
			        break;  
			    }  
			}  
			
			var select2 = document.getElementById("editType");
			for(var i = 0; i < select2.options.length; i++) {  
			    if(select2.options[i].innerHTML == rowData.type){  
			        select2.options[i].selected = true;  
			        break;  
			    }  
			}  
			
			var select3 = document.getElementById("editStatus");
			for(var i = 0; i < select3.options.length; i++) {  
			    if(select3.options[i].innerHTML == rowData.status){  
			        select3.options[i].selected = true;  
			        break;  
			    }  
			}  

			m1 = new MyModal.modal(function() {
			
			});
			
			m1.show();
		}
		
		/*用于触发编辑事件*/
		function editOper() {
			fidSn();
			oper = "edit";
			var EditInfo = {
				 assetname: $("#editName").val(),
				 f_sn: f_sn,
				 a_type: $("#editType").val(),
				 a_status: $("#editStatus").val(),
				 purchase: $("#editPurchase").val(),
				 deveui: $("#editDeveui").val(),
				 memo: $("#editMemo").val(),
				 a_id: id,
				 oper: oper
			};
				 
			 $.ajax({
				async: false,
				type: "POST",
				url: "../AssetInfoManagement/doPost.do?usrname="+ GetQueryString("name"),
				data: EditInfo,
				success: function(data) {
					        var status = data.status;
						    if (status == "ok") {				    					 			    					 
						    	alert("数据更新成功！");	    					
							}else{
								alert("数据更新失败！" + status);
							}
				   		}		
		      });
			 
			 m1.hide();
			 
			 $("#jqGridPro").jqGrid('setGridParam',{
	 		  	url:"../AssetInfoManagement/doPost.do?usrname="+GetQueryString("name")
	 		 }).trigger('reloadGrid'); 
		}
		
		//用于触发删除事件
		function delOper() {
			oper = "del";
			var DelInfo = {
				id: id,
				oper: oper
			};
			$.ajax({
				async: false,
				type: "POST",
				url: "../AssetInfoManagement/doPost.do?usrname="+ GetQueryString("name"),
				data: DelInfo,
				success: function(data) {
					        var status = data.status;
						    if (status == "ok") {				    					 			    					 
						    	alert("数据删除成功！");	    					
							}else{
								alert("删除失败！" + status);
							}
				   		}		
		      });
			m1.hide();
			 $("#jqGridPro").jqGrid('setGridParam',{
	 		  	url:"../AssetInfoManagement/doPost.do?usrname="+GetQueryString("name")
	 		 }).trigger('reloadGrid'); 
		}
	   
	   $(document).ready(function() {
		   var inputName = GetQueryString("name");
		   jQuery("#jqGridPro").jqGrid({
	          datatype: "json",
	    	  mtype:"POST",
	    	  autowidth: true,
	    	  height: 'auto',
   	          colNames:[ '资产ID', '资产名称','sn','资产类型','采购时间','设备号','状态','备注'],  
   	          colModel:[
   				 {name:"id",index:"id", width:50,align:"center",editable:false, hidden:true},
   	             {name:"name",index:"name", width:150, align:"center",sortable:true,editable:true},
   	          	 {name:"sn",index:"sn",align:"center",editable:true,hidden:true},
              	 {name:"type",index:"type",width: 50, align:"center",editable:true},
              	 {name:'purchase',index:'purchase', align:"center",width:150,editable:true,
    	            	formatter:function(cellvalue, options, row){
       	            		var year = cellvalue.year;
       	            		var month = cellvalue.monthValue;
       	            		var day = cellvalue.dayOfMonth;
       	            		if(month < 10){
       	            			month = "0" + month;
       	            		}
       	            		if(day < 10){
       	            			day = "0" + day;
	              		}
       	            		var str = "-";
       	            		return year + str + month + str + day;
              	 	}
              	 },
              	 {name:"deveui",index:"deveui",width: 50, align:"center",editable:true},
              	 {name:"status",index:"status",width: 50, align:"center",editable:true},
   	             {name:"memo",index:"memo", width:200, align:"center",editable:true}
   	         ],
	    	 url: "../AssetInfoManagement/doPost.do?usrname="+inputName, 
	    	 editurl:"../AssetInfoManagement/doPost.do?usrname="+inputName, 
	    	 pager:"#jqGridPagerPro",
	    	 rowNum: 20,  //设置行数
	    	 viewrecords:true,//总记录数
	    	 onCellSelect:rowClicked,
	    	 jsonReader:{
	             repeatitems:false
	         },
	     });
	 });
  </script>
</body>
</html>