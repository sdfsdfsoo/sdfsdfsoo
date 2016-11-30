<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户用心</title>
<link  href="/css/userCenter/userCenter.css" rel="stylesheet" type="text/css" >
<link href="/js/easyui/themes/icon.css" rel="stylesheet" type="text/css" />
<link href="/js/easyui/themes/default/easyui.css" rel="stylesheet" type="text/css" />
<link href="/css/index.css" rel="stylesheet" type="text/css" />
<script src="/js/jquery-1.8.0.min.js" type="text/javascript" ></script>
<script src="/js/easyui/jquery.easyui.min.js"></script>
<script src="/js/easyui/locale/easyui-lang-zh_CN.js"></script>
		<script type="text/javascript">

		var searchPatentNo="";
		var searchPatentName="";
		var searchApd="";
		var searchLegalState="";
		
		function payYearFee(id){
			$.ajax({
				type:'post',
				data:{
					id:id
				},
				url:'/front/user/category_payYearFee',
				success:function(data){
					location.reload();
				}
			});
		}
		
		var allSelectedStateItem=false;
		     function operaterLook(value,row,index){
		    	 var html;
             	if(row.searchscope==1){
             		html="<a target=_blank href='/front/search/table_patentDetailUI?searchscope=DocDB&enDescriptionItem.pubnr="+row.appno+"'>查看详情</a> &nbsp;&nbsp;<a  href='javascript:void(0);' onClick='loadFirst("+row.id+");' >置顶</a>"
             	}else {
             		if(row.yearFeeDate!=null&&row.yearFeeDate!=""){
               		  html="<a target=_blank href='javascript:payYearFee("+ row.id +")'>已缴纳</a>&nbsp;<a target=_blank href='/front/search/table_patentDetailUI?cnDescriptionItem.appno="+row.appno+"'>查看详情</a> &nbsp;<a  href='javascript:void(0);' onClick='loadFirst("+row.id+");' >置顶</a>"
                	}
             		else{
             		  html="<a target=_blank href='/front/search/table_patentDetailUI?cnDescriptionItem.appno="+row.appno+"'>查看详情</a> &nbsp;<a  href='javascript:void(0);' onClick='loadFirst("+row.id+");' >置顶</a>"
                     	
             		}
             	}
				  return html;
            }
		     function loadFirst(rowId){
		     $('#dg').datagrid({
                    url:'/front/user/category_getPatentStoreInfo',
                    pagination:true,
                    checkbox:true,
                    height:530,
                    fitColumns:true,
                    multiSort:false,
                      queryParams: {
						id: '${tabmid}',
					   text: '${tabtext}',
					   rowId: rowId,
					   searchPatentNo:searchPatentNo,
					   searchPatentName:searchPatentName,
					   searchApd:searchApd,
					   searchLegalState:searchLegalState
					},
                    columns:[[
                        {field:'ck',checkbox:true},
                        {field:'appno',title:'专利申请号',width:8,sortable:true},
                        {field:'appname',title:'专利名称',width:14},
                        {field:'apd',title:'申请日',width:8,sortable:true},
                        {field:'appl',title:'申请人',width:14,sortable:true},
                       // {field:'legalStatus',title:'法律状态',width:10},
                        {field:'operation',title:'操作',width:25,formatter:operaterLook}
                    ]]
				
                });
		     }
            
		     function searchButton(){
					searchPatentNo = $("#searchPatentNo").val();
					searchPatentName = $("#searchPatentName").val();
					searchApd = $("#searchApd").val();
					searchLegalState = $("#searchLegalState").val();

					 $('#dg').datagrid({
		                    url:'/front/user/category_getPatentStoreInfo',
		                    pagination:true,
		                    checkbox:true,
		                    height:530,
		                    fitColumns:true,
		                    multiSort:false,
		                    sortName:'id',
		                    sortOrder:'asc',
		                      queryParams: {
								id: '${tabmid}',
							   text: '${tabtext}',
							   searchPatentNo:searchPatentNo,
							   searchPatentName:searchPatentName,
							   searchApd:searchApd,
							   searchLegalState:searchLegalState
							},
		                    columns:[[
		                        {field:'ck',checkbox:true},
		                        {field:'appno',title:'专利申请号',width:8,sortable:true},
		                        {field:'appname',title:'专利名称',width:14},
		                        {field:'apd',title:'申请日',width:8,sortable:true},
		                        {field:'appl',title:'申请人',width:14,sortable:true},
		                       // {field:'legalStatus',title:'法律状态',width:10},
		                        {field:'legalstateold',title:'法律状态',width:14,sortable:true},
		                        {field:'operation',title:'操作',width:14,formatter:operaterLook}
		                    ]],
		                    onRowContextMenu:function(e,rowIndex,rowData){
								e.preventDefault();
								$(this).datagrid("clearSelections");
								$(this).datagrid("selectRow",rowIndex);

								$("#_appno").val(rowData['appno']);
					          	$("#_title").val(rowData['appname']);
					          	$("#_apd").val(rowData['apd']);
					          	$("#_appl").val(rowData['appl']);
								
								
								$('#menu').menu('show',{
									left:e.pageX,
									top:e.pageY
								});
							}
						
		                });
				 }
			     
     $(function (){
                $('#dg').datagrid({
                    url:'/front/user/category_getPatentStoreInfo',
                    pagination:true,
                    checkbox:true,
                    height:530,
                    fitColumns:true,
                    multiSort:false,
                    sortName:'id',
                    sortOrder:'asc',
                      queryParams: {
						id: '${tabmid}',
					   text: '${tabtext}',
					   searchPatentNo:searchPatentNo,
					   searchPatentName:searchPatentName,
					   searchApd:searchApd,
					   searchLegalState:searchLegalState
					},
                    columns:[[
                        {field:'ck',checkbox:true},
                        {field:'appno',title:'专利申请号',width:8,sortable:true},
                        {field:'appname',title:'专利名称',width:14},
                        {field:'apd',title:'申请日',width:8,sortable:true},
                        {field:'appl',title:'申请人',width:14,sortable:true},
                       // {field:'legalStatus',title:'法律状态',width:10},
                        {field:'legalstateold',title:'法律状态',width:10,sortable:true},
                        {field:'yearFeeDate',title:'年费缴纳日',width:10,sortable:true,
                            formatter:function(value,rowData,rowIndex){
                    			return value;
                        	}

                        },
                        {field:'days',title:'距离缴纳日',sortable:true,
                        	formatter:function(value,rowData,rowIndex){
                        	var yearFeeDate = new Date(rowData.yearFeeDate);
                        	var now=new Date();
	                        	if(rowData.yearFeeDate!=null&&rowData.yearFeeDate!=""){
		                			return parseInt((yearFeeDate-now)/1000/60/60/24);
	                        	}
	                    	}
                    	},
                        {field:'operation',title:'操作',width:14,formatter:operaterLook}
                    ]],
                    onRowContextMenu:function(e,rowIndex,rowData){
						e.preventDefault();
						$(this).datagrid("clearSelections");
						$(this).datagrid("selectRow",rowIndex);

						$("#_appno").val(rowData['appno']);
			          	$("#_title").val(rowData['appname']);
			          	$("#_apd").val(rowData['apd']);
			          	$("#_appl").val(rowData['appl']);
						
						
						$('#menu').menu('show',{
							left:e.pageX,
							top:e.pageY
						});
					},
					rowStyler:function(index,row){
						var yearFeeDate = new Date(row.yearFeeDate);
						var yearFeeYear = yearFeeDate.getFullYear();
						var yearFeeM=yearFeeDate.getMonth()+1;

						var now = new Date();
						var nowYear = now.getFullYear();
						var nowM=now.getMonth()+1;

						var months = 12*(yearFeeYear-nowYear)+(yearFeeM-nowM);
						if(months<3&&months>0){
							return 'background-color:rgb(242, 188, 188)';
						}
						if(months<0){
							return 'background-color:rgb(255, 232, 159)';
						}
					}
				
                });
                
			$('#button7').linkbutton(
					{
						iconCls : 'icon-patent_compare',
						text : '分析',
						/**
						onClick : function() {
							window.location = "/front/search/table_loadingMessageOwn";
							//  window.location="/front/search/table_analysisUI?searchFormula="+searchFormula;

						}
						*/
					});
					
					
			
     $('#button2').linkbutton({
        iconCls: 'icon-item_export',
		text:'专利著录项批量导出(Excel)',
		onClick: function(){
	  	    var ids="";
	        var checkedids=$('#dg').datagrid('getChecked'); 
	        if(checkedids.length){
		        for (i=0; i<checkedids.length; i++){
					ids+=checkedids[i]['appno']+",";       
			    } 
			    ids=ids.substring(0,ids.length-1);  
				 
		   		$('#dlg6').dialog('open');
		    } else {
		    	$.messager.alert('专利选择提示','请在专利著录项批量导出(Excel)操作之前，完成对专利的选择!','信息');
		    }
		}
	});
	
	$('#button3').linkbutton({
        iconCls: 'icon-pdf_download',
		text:'专利全文批量下载(PDF)',
		onClick: function(){
	  	    var ids="";
	        var checkedids=$('#dg').datagrid('getChecked'); 
	        if(checkedids.length){
		        for (i=0; i<checkedids.length; i++){
					ids+=checkedids[i]['appno']+",";       
			    } 
			    ids=ids.substring(0,ids.length-1);  
				 
		       // parent.location="/front/search/table_batchDownloadPdfs?appnos="+ids;
		       window.open("/front/search/table_batchDownloadPdfs?appnos="+ids);
		    } else {
		    	$.messager.alert('专利选择提示','请在专利全文批量下载(PDF)操作之前，完成对专利的选择!','信息');
		    }
		}
	});
	
	$('#button5').linkbutton({
        iconCls: 'icon-patent_compare',
		text:'批量专利对比',
		onClick: function(){
  	    var ids="";
        var checkedids=$('#dg').datagrid('getChecked'); 
        if(checkedids.length){
	        for (i=0; i<checkedids.length; i++){
				ids+=checkedids[i]['appno']+",";       
		    } 
		    ids=ids.substring(0,ids.length-1);  
			 
	        parent.location="/front/user/analysis_analysisUI?appnos="+ids;
	    } else {
	    	 $.messager.alert('专利选择提示','请在专利对比操作之前，完成对专利的选择!','信息');
	    }
	    /**
		if(ids=='on'||ids==''){
			$.messager.alert('专利选择提示','请在专利对比操作之前，完成对专利的选择!','信息');
		}else{
			window.location.href = "/front/user/analysis_analysisUI?searchscope=${searchscope}&appnos="+ids;
		}
		*/
		
		}
	});
});

  
            
  function analysisPatent(){
  	  var ids="";
      var checkedids=$('#dg').datagrid('getChecked'); 
      if(checkedids.length){
	       for (i=0; i<checkedids.length; i++){
				ids+=checkedids[i]['appno']+",";       
		   } 
		   ids=ids.substring(0,ids.length-1);  
			 
	       parent.location="/front/search/table_analysisUIOwn?ids="+ids;
	   } else {
	       parent.location="/front/search/table_analysisUIOwn?id="+${tabmid};
	   }	
  }
            
            
  function deletePatent(){

	  if(isMove=='1'){
		  var ids="";
	      var checkedids=$('#dg').datagrid('getChecked'); 
	      if(checkedids.length){
		        for (i=0; i<checkedids.length; i++){
					ids+=checkedids[i]['id']+",";       
				 } 
				 ids=ids.substring(0,ids.length-1);  
				    $.ajax({  
	                     type: "POST",  
	                     url: '/front/user/category_delPatentStoreInfo',  
	                      data: "ids=" + ids,
	                     success: function(msg){  
	                         var json= jQuery.parseJSON(msg);
	                            for (i=0; i<checkedids.length; i++){
	                        		 var index = $('#dg').datagrid('getRowIndex', checkedids[i]);
	                        		 $('#dg').datagrid('deleteRow',index);
	                        	}
	                         //$.messager.alert('专利提示',json.msg,'信息');
	                          
	                     }  
	                 });
				 
		   }else{
		   	 $.messager.alert('专利选择提示','请在删除专利之前，完成对专利的选择!','信息');
		   }
	  }
	  else{
		  if(window.confirm('你确定删除吗?')){

		      var ids="";
		      var checkedids=$('#dg').datagrid('getChecked'); 
		      if(checkedids.length){
			        for (i=0; i<checkedids.length; i++){
						ids+=checkedids[i]['id']+",";       
					 } 
					 ids=ids.substring(0,ids.length-1);  
					    $.ajax({  
		                     type: "POST",  
		                     url: '/front/user/category_delPatentStoreInfo',  
		                      data: "ids=" + ids,
		                     success: function(msg){  
		                         var json= jQuery.parseJSON(msg);
		                            for (i=0; i<checkedids.length; i++){
		                        		 var index = $('#dg').datagrid('getRowIndex', checkedids[i]);
		                        		 $('#dg').datagrid('deleteRow',index);
		                        	}
		                         $.messager.alert('专利提示',json.msg,'信息');
		                         location.reload();
		                          
		                     }  
		                 });
					 
			   }else{
			   	 $.messager.alert('专利选择提示','请在删除专利之前，完成对专利的选择!','信息');
			   }
		  }
		  else{
				return false;
		  }
	  }
	  
	  	  
	}
	
	function selectAllItem(){
 	 var items="";
	 var itemsObject= $("[name='items']");
      if(!allSelectedStateItem){
              $("[name='items']").attr("checked",'true');
            
              for (i=0; i<itemsObject.length; i++){
				                if (itemsObject[i].type=="checkbox"){    
				            	  items+=itemsObject[i].value+",";       
				                }
				        }  
			 document.getElementById('items').value=items.substring(0,items.length-1);
			 allSelectedStateItem=true;
      }else{
         $("[name='items']").removeAttr("checked");
         document.getElementById('items').value="";
         allSelectedStateItem=false;
      }
	}
	
	
	 function toexcelWithSelectedItems(){
  		var ids=""
  		if(document.getElementById('idValue').value=="jly"){
	  	    var ids="";
	        var checkedids=$('#dg').datagrid('getChecked'); 
	        if(checkedids.length){
		        for (i=0; i<checkedids.length; i++){
					ids+=checkedids[i]['appno']+",";       
			    } 
			    ids=ids.substring(0,ids.length-1);  
		    }
  		}else{
  			ids= document.getElementById('idValue').value;
	  		document.getElementById('idValue').value="jly";
  		}
  		var items= document.getElementById('items').value;
		window.location.href = "/front/search/table_toExcel?searchscope=${searchscope}&appnos="+ids+"&items="+items;
		
		$("[name='ids']").removeAttr("checked");
        allSelectedState=false;
        
		$('#dlg6').dialog('close');
  	}

	  	function moveTo(){
	  		$('#dlg3').dialog('open');
	  		document.getElementById("isMove").value=1;
	  	}
	  	function copyTo(){
	  		$('#dlg3').dialog('open');
	  		document.getElementById("isMove").value=0;
	  	}
	  	
	    function openDlg(categorytype){
    		$("#categorytype").val(categorytype);
    		if($("#categorytype").val()==0){
    		    $("#n_tt0").tree({  
		            url:'/front/user/category_getTreeJsonAjax0',
		            dnd:true,  
		            animate: true    
		        }); 
    		  $('#dlg0').dialog('open');
    		}else if($("#categorytype").val()==1){
    		      $("#n_tt1").tree({  
			            url:'/front/user/category_getTreeJsonAjax1',
			            dnd:true,
			            animate: true    
			        });  
    		  $('#dlg1').dialog('open');
    		}
    		  $('#dlg3').dialog('close');
    		
    }

	    function addPatentIn(){
		  var appnoValue=document.getElementById("_appno").value;
		  var title=document.getElementById("_title").value;
		  var apd=document.getElementById("_apd").value;
		  var appl=document.getElementById("_appl").value;
		  var isMove=document.getElementById("isMove").value;
		  var text = "";
		  var  categorytype =$("#categorytype").val();
       	  if(categorytype==0){	
        		  var selected = $('#n_tt0').tree('getSelected');  
          }else if(categorytype==1){
        		  var selected = $('#n_tt1').tree('getSelected');  
          }

          if(isMove=='1'){
        	  deletePatent();
			//删除原有纪录
          }
          else if(isMove=='0'){
			//不做任何操作
          }
			  
		  $("#_appno").val("");
          $.ajax({
              type: "POST",  
              url: '/front/user/category_addPatentIn',  
              data:{"appnoValue":appnoValue,"id":selected.id,"text":text,"title":title,"apd":apd,"appl":appl,"searchscope":"${searchscope}"},  
              success: function(msg){  
                   var json= jQuery.parseJSON(msg);
               		 alert(json.msg);
              }  
          }); 
          
          if($("#categorytype").val()==0){
		   		$('#dlg0').dialog('close');
          }else if($("#categorytype").val()==1){
		   	    $('#dlg1').dialog('close');
          }
	}
        </script>

</head>
<body>
<input type="hidden" id="categorytype" />
<input type="hidden" id="isMove" />
<div id="dlg3" closed="true" class="easyui-dialog"
								title="请选择要加入的库：" data-options="iconCls:'icon-add'"
								style="width: 200px; height: 150px; padding: 10px">
								<div id="dlg-buttons" align="center">
									<br />
									<input id="categoryType1" value="关注专利库" type="button"
										onclick="openDlg(0);"></input>
									<br />
									<br />
									<input id="categoryType2" value="本企业专利库" type="button"
										onclick="openDlg(1);"></input>
								</div>
							</div>
							
	<div id="dlg0" class="easyui-dialog" closed="true" title="企业个性库"
									style="width: 300px; height: 300px; padding: 10px"
									data-options="
								iconCls: 'icon-save',
								
								buttons: [{
									text:'确定',
									iconCls:'icon-ok',
									handler:function(){
										addPatentIn();
									}
								},{
									text:'取消',
									handler:function(){
										$('#dlg0').dialog('close');
									}
								}]
							">
		<input id="_appno" type="hidden" />
		<input id="_title" type="hidden" />
		<input id="_apd" type="hidden" />
		<input id="_appl" type="hidden" />
		<ul id="n_tt0" class="easyui-tree"></ul>
	</div>
		<div id="dlg1" class="easyui-dialog" closed="true" title="企业个性库"
		style="width: 300px; height: 300px; padding: 10px"
		data-options="
		iconCls: 'icon-save',
		
		buttons: [{
			text:'确定',
			iconCls:'icon-ok',
			handler:function(){
				addPatentIn();
			}
		},{
			text:'取消',
			handler:function(){
				$('#dlg1').dialog('close');
			}
		}]
	">
		<input id="_appno" type="hidden" />
		<input id="_title" type="hidden" />
		<input id="_apd" type="hidden" />
		<input id="_appl" type="hidden" />
		<ul id="n_tt1" class="easyui-tree"></ul>
	</div>
	
	<div id='menu' class="easyui-menu" style="width:50px;display:none">
		<div onclick="moveTo()">移动至其他文件夹</div>
		<div onclick="copyTo()">复制到其他文件夹</div>
	</div>
	
				<div id="_dataGridDiv2" style="margin:20px 0;">

<div style="width:600px">
				<form>
					<table>
						<tr>
							<td>专利号:</td>
							<td><input type="text" id="searchPatentNo" name="searchPatentNo"/></td>
							<td>专利名称:</td>
							<td><input type="text" id="searchPatentName" name="searchPatentName" /></td>
							<td></td>
						</tr>
						<tr>
							<td>申请日:</td>
							<td><input type="text" id="searchApd" name="searchApd"/></td>
							<td>法律状态:</td>
							<td><input type="text" id="searchLegalState" name="searchLegalState"/></td>
							<td><input type="button" onclick="searchButton()" value="搜索"/></td>
							
						</tr>
					</table>
				</form>
			</div>

					               <table id='dg' toolbar="#toolbar"></table>
                           <div id="toolbar">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="deletePatent()">删除所选专利</a>
        <a id="button7"   target="_blank" onclick="analysisPatent()">分析</a>
        <a id="button5">批量专利对比</a>
		<a id="button2">批量著录项导出(Excel)</a>
		<a id="button3">批量专利全文下载(PDF)</a>
		
		<input type="hidden" id="idValue" value="jly" />
		<div id="dlg6" closed="true" class="easyui-dialog" title="请选择需要导出的著录项：" data-options="iconCls:'icon-item_export'" style="width:450px;height:280px;padding:10px">
				<div id="dlg-buttons" align="left">
									<!-- ///////////////////////////////////////////////////////////// -->
									<div id="ExportCFG" style="min-height: 100px;">
										<ul>
											<li>
												<label>
													<input type="checkbox" id="chkAppNo" value="appno"
														disabled="disabled" checked="checked" >
													申请号
												</label>
											</li>
											<li>
												<label>
													<input type="checkbox" id="chkAppDate" value="appdate" name="items" onchange="changeItem()">
													申请日
												</label>
											</li>
									<!--  	<li>
												<label>
													<input type="checkbox" id="chkPubNo" value="pubno" name="items" onchange="changeItem()">
													公开（公告）号
												</label>
											</li>     -->
											<li>
												<label>
													<input type="checkbox" id="chkTitle" value="title"
														disabled="disabled" checked="checked">
													名称
												</label>
											</li>
											<li>
												<label>
													<input type="checkbox" id="chkIPC" value="ipc" name="items" onchange="changeItem()">
													分类号
												</label>
											</li>
										<!-- <li>
												<label>
													<input type="checkbox" id="chkMIPC" value="mipc" name="items" onchange="changeItem()">
													主分类号
												</label>
											</li>    -->
											<li>
												<label>
													<input type="checkbox" id="chkPubDate" value="pubdate" name="items" onchange="changeItem()">
													公开（公告）日
												</label>
											</li>
								<!--  			<li>
												<label>
													<input type="checkbox" id="chkAT" value="at" name="items" onchange="changeItem()">
													代理人
												</label>
											</li>   -->
											<li>
												<label>
													<input type="checkbox" id="chkAG" value="agent" name="items" onchange="changeItem()">
													专利代理机构
												</label>
											</li>
										<!-- 	<li>
												<label>
													<input type="checkbox" id="chkabs" value="abs" name="items" onchange="changeItem()">
													摘要
												</label>
											</li>  -->
											<li>
												<label>
													<input type="checkbox" id="chkCL" value="claim" name="items"  onchange="changeItem()">
													主权项
												</label>
											</li>
											<li>
												<label>
													<input  type="checkbox" id="chkCY" value="countryNC" name="items" onchange="changeItem()">
													国省代码
												</label>
											</li>
											<li>
												<label>
													<input  type="checkbox" id="chkPA" value="appl" name="items" onchange="changeItem()">
													申请（专利权）人
												</label>
											</li>
											<li>
												<label>
													<input  type="checkbox" id="chkIN" value="inventor" name="items" onchange="changeItem()">
													发明（设计）人
												</label>
											</li>
									<!--  	<li>
												<label>
													<input type="checkbox" id="chkPR" value="pr" name="items" onchange="changeItem()">
													优先权
												</label>
											</li>  -->
											<li>
												<label>
													<input type="checkbox" id="chkdz" value="address" name="items"  onchange="changeItem()">
													地址
												</label>
											</li>
											<li>
												<label>
													<input type="checkbox" id="chkflzt" value="flzt" name="items" onchange="changeItem()">
													法律状态
												</label>
											</li>
											<li>
												<label>
												<a> &nbsp;</a>
												</label>
											</li>
											<li>
												<label>
													<input onclick="selectAllItem()" type="checkbox" name="itemsAll" id="items">全选
													<a id="getNodeNameId" href="javascript:void(0)" class="easyui-linkbutton" onclick="toexcelWithSelectedItems();" style="background:#00ff00"  >导出(Excel)</a>
												</label>
											</li>
										</ul>
									</div>
									<!-- ///////////////////////////////////////////////////////////// -->
				</div>
	</div>
    </div> 
	
</body>
</html>