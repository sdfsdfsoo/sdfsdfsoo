
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/page/share/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>企业个性库</title>
		<link href="/css/index.css" rel="stylesheet" type="text/css" />
		<link href="/css/userCenter/userCenter.css" rel="stylesheet"
			type="text/css">
		<link href="/js/easyui/themes/icon.css" rel="stylesheet"
			type="text/css" />
		<link href="/js/easyui/themes/default/easyui.css" rel="stylesheet"
			type="text/css" />
		<script src="/js/jquery-1.8.0.min.js" type="text/javascript"></script>
		<script src="/js/jquery.easyui.min.js"></script>
		<script src="/js/easyui/locale/easyui-lang-zh_CN.js"></script>
		<script type="text/javascript"> 
   function setUserCenter(){  
   		document.getElementById('_iframe').src= "/front/user/category_profileTab1";
   }

   function myFormular(){
	   document.getElementById('_iframe').src= "/front/user/formula_myFormular";
   }
   function getSelected(num){  
   			if(num==0){
   				var node = $('#n_tt0').tree('getSelected'); 
   				$('#nodeId').val(node.id);
   	   			$('#nodeText').val(node.text);
   		 		document.getElementById('_iframe').src= "/front/user/category_profileTab2?nodetext="+$('#nodeText').val()+"&nodeid="+$('#nodeId').val();
   			}else if(num==1){
   				var node = $('#n_tt1').tree('getSelected'); 
   				$('#nodeId').val(node.id);
   	   			$('#nodeText').val(node.text);
   		 		document.getElementById('_iframe').src= "/front/user/category_profileTab3?nodetext="+$('#nodeText').val()+"&nodeid="+$('#nodeId').val();
   			}
   			
 	}
 	   function editCategory(){  
           	 var categorytype=$('#categorytype').val();
           	 if(categorytype==0){
   				var selected = $('#n_tt0').tree('getSelected'); 
			}else if(categorytype==1){
   				var selected = $('#n_tt1').tree('getSelected'); 
			}  
              var text = document.getElementById("nodename").value;
              var newname = document.getElementById("newname").value;
 	   		if(selected){
 	   				$.ajax({  
	                     type: "POST",  
	                     url: '/front/user/category_editTreeNode',  
	                     data:{"id":selected.id,"text":text,"newname":newname},  
	                   	  success: function(msg){ 
                    			 var json= jQuery.parseJSON(msg);
                    			 
	                           	 if(categorytype==0){
					   				 $('#n_tt0').tree('update', {
									target: selected.target,
									text: newname
								});
								}else if(categorytype==1){
					   				 $('#n_tt1').tree('update', {
									target: selected.target,
									text: newname
								});
								}
									
	                           	 alert(json.msg);
	                           	 //location.reload();
                    	 }  
	                 });
 	   		
 	   		 }else{  
	                alert("请选择要修改的节点");  
	            } 
 	   			 $('#dlg3').dialog('close');
 	   
 	   }
 	   function deleteNode(){  
             var categorytype=$('#categorytype').val();
           	 if(categorytype==0){
   				var selected = $('#n_tt0').tree('getSelected'); 
			}else if(categorytype==1){
   				var selected = $('#n_tt1').tree('getSelected'); 
			}  
              var text = document.getElementById("nodename").value;
             if (selected){  
	                 $.ajax({  
	                     type: "POST",  
	                     url: '/front/user/category_deleteTreeNode',  
	                     data:{"id":selected.id,"text":text},  
	                   	  success: function(msg){  
                    		 var json= jQuery.parseJSON(msg);
                           	 if(json.result=="success"){
	                           	       if(categorytype==0){
		                         	   $('#n_tt0').tree('remove',selected.target);	
								        }else if(categorytype==1){
		                                $('#n_tt1').tree('remove',selected.target);	
								        }
	                           	    location.reload();	  
                           	 }
                           	 if(json.result=="fail"){
									alert("该节点有子节点，不可删除");
                           	 }
                    	 }  
	                 });
	            }else{  
	                alert("请选择要删除的节点");  
	            } 
        } 
       function confimOrnot(){  
           deleteNode();
           $('#dlg4').dialog('close');
       }
       function confirmPWD(){  
            $('#dlg4').dialog('open');
       }
       function addNode(){  
            var categorytype=$('#categorytype').val();
           	 if(categorytype==0){
   				var selected = $('#n_tt0').tree('getSelected'); 
			}else if(categorytype==1){
   				var selected = $('#n_tt1').tree('getSelected'); 
			}  
            var text = document.getElementById("nodename").value;
             if (selected){  
                 $.ajax({  
                     type: "POST",  
                     url: '/front/user/category_addTreeNode',  
                     data:{"id":selected.id,"text":text,"categorytype":categorytype},  
                     success: function(msg){  
	                        var json= jQuery.parseJSON(msg);
	                        	 alert(json.msg);
	                        	 /*
                       	 	 if(categorytype==0){
					   				$('#n_tt0').tree('append', {
										parent: selected.target,
										data: [{
											text: text
										}]
									}); 
								}else if(categorytype==1){
							   		$('#n_tt1').tree('append', {
										parent: selected.target,
										data: [{
											text: text
										}]
									}); 
								}
									*/
	                        	 location.reload();	
                     }  
                 });  
        }else{  
	         alert("请选择要添加节点的位置！");  
	    }
	  $('#dlg2').dialog('close');
    }    
  function reloadJson(){
    $("#n_tt0").tree({  
            url:'/front/user/category_getTreeJsonAjax0',
            dnd:true,  
            animate: true,
            onContextMenu:function(e,node){
				e.preventDefault();
				if(node.parent=='0'){
					
					$('#n_tt0').tree('select',node.target);
					$('#mmRoot').menu('show',{
						left:e.pageX,
						top:e.pageY
					});
				}
				else{
					$('#n_tt0').tree('select',node.target);
					$('#mm').menu('show',{
						left:e.pageX,
						top:e.pageY
					});
				}
				
		    }  
        }); 
            $("#n_tt1").tree({  
            url:'/front/user/category_getTreeJsonAjax1',
            dnd:true,  
            animate: true,
            onContextMenu:function(e,node){
				e.preventDefault();
				if(node.parent=='0'){
					
					$('#n_tt1').tree('select',node.target);
					$('#mmRoot2').menu('show',{
						left:e.pageX,
						top:e.pageY
					});
				}
				else{
					$('#n_tt1').tree('select',node.target);
					$('#mm2').menu('show',{
						left:e.pageX,
						top:e.pageY
					});
				}
            } 
        });   
  }
  function openDlg(appnoValue){
     $("#_appno").val(appnoValue);
	 $('#dlg').dialog('open');
  }

  function append(){
	  $('#categorytype').val(0);
		$('#dlg2').dialog('open');
  }

  function remove1(){
	  $('#categorytype').val(0);
		 confirmPWD();
  }
  function edit(){
	  $('#categorytype').val(0);
		$('#dlg3').dialog('open');
  }


  
  function append2(){
	  $('#categorytype').val(1);
		$('#dlg2').dialog('open');
  }

  function remove2(){
	  $('#categorytype').val(1);
		 confirmPWD();
  }
  function edit2(){
	  $('#categorytype').val(1);
		$('#dlg3').dialog('open');
  }
</script>
	</head>
	<body onload="reloadJson();" style="width: 100%">
		<%@include file="/WEB-INF/page/front/share/top.jsp"%>
		<div id='mm' class='easyui-menu' style='width: 120px'>
			<div onclick="append()" data-options="iconCls:'icon-add'">
				Append
			</div>
			<div onclick="remove1()" data-options="iconCls:'icon-remove'">
				Remove
			</div>
			<div onclick="edit()" data-options="iconCls:'icon-edit'">
				Edit
			</div>
		</div>
		<div id='mmRoot' class='easyui-menu' style='width: 120px'>
			<div onclick="append()" data-options="iconCls:'icon-add'">
				Append
			</div>
			<div onclick="edit()" data-options="iconCls:'icon-edit'">
				Edit
			</div>
		</div>

		<div id='mm2' class='easyui-menu' style='width: 120px'>
			<div onclick="append2()" data-options="iconCls:'icon-add'">
				Append
			</div>
			<div onclick="remove2()" data-options="iconCls:'icon-remove'">
				Remove
			</div>
			<div onclick="edit2()" data-options="iconCls:'icon-edit'">
				Edit
			</div>
		</div>
		<div id='mmRoot2' class='easyui-menu' style='width: 120px'>
			<div onclick="append2()" data-options="iconCls:'icon-add'">
				Append
			</div>
			<div onclick="edit2()" data-options="iconCls:'icon-edit'">
				Edit
			</div>
		</div>


			<div id="mid" class="base">
				<div id="left" style="width: 185px;">
					<div id="pinlieft" style="width: 185px;">
						<div id="left_title " class="left_ti" style="text-align: center;">
							&nbsp;
							<span>企业个性库</span>&nbsp;
						</div>
						<div class="left_content2" style="padding: 0px; width: 185px">
							
							<div class="panel"
								style="width: 185px; background-color: #FBEC88">
								<div
									class="panel-header accordion-header accordion-header-selected"
									style="height: 16px; width: 210px; border-width: 0 0 1px;">
									<div class="panel-title " id="_userCenter"
										" style="cursor: hand">
										<a href="#"><span onclick="myFormular();"
											style="color: #000000; text-align: center;">我的检索式</span>
										</a>
									</div>
								</div>
							</div>
							<div class="easyui-accordion" style="width: 185px">
								<div title="关注专利信息管理" style="overflow: auto; padding: 10px;"
									data-options="
				selected:false,
				tools:[{
							text:'编辑分类',
							iconCls:'icon-edit',
							handler:function(){
								 $('#categorytype').val(0);
								$('#dlg3').dialog('open');
							}
						},{
							text:'添加分类',
							iconCls:'icon-add',
							handler:function(){
							     $('#categorytype').val(0);
								$('#dlg2').dialog('open');
							}
						},{
							text:'删除分类',
							iconCls:'icon-remove',
							handler:function(){
							 $('#categorytype').val(0);
								 confirmPWD();
							}
						}]">
									<div id="dlg2" closed="true" class="easyui-dialog"
										title="请输入分类名称：" data-options="iconCls:'icon-add'"
										style="width: 200px; height: 150px; padding: 10px">
										<input id="nodename"></input>
										<div id="dlg-buttons" align="center">
											<br />
											<a id="getNodeNameId" href="javascript:void(0)"
												class="easyui-linkbutton" onclick="addNode();">确定</a>
											<a href="javascript:void(0)" class="easyui-linkbutton"
												onclick="javascript:$('#dlg2').dialog('close')">取消</a>
										</div>
									</div>
									<div id="dlg3" closed="true" class="easyui-dialog"
										title="请输入新名称：" data-options="iconCls:'icon-add'"
										style="width: 200px; height: 150px; padding: 10px">
										<input id="newname"></input>
										<div id="dlg-buttons" align="center">
											<br />
											<a id="getNodeNameId" href="javascript:void(0)"
												class="easyui-linkbutton" onclick="editCategory();">确定</a>
											<a href="javascript:void(0)" class="easyui-linkbutton"
												onclick="javascript:$('#dlg3').dialog('close')">取消</a>
										</div>
									</div>
									<div id="dlg4" closed="true" class="easyui-dialog"
										title="确定要删除？" data-options="iconCls:'icon-remove'"
										style="width: 200px; height: 120px; padding: 10px">
										<div id="dlg-buttons" align="center">
											<br />
											<a id="getNodeNameId" href="javascript:void(0)"
												class="easyui-linkbutton" onclick="confimOrnot();">确定</a>
											<a href="javascript:void(0)" class="easyui-linkbutton"
												onclick="javascript:$('#dlg4').dialog('close')">取消</a>
										</div>
									</div>

									<ul ondblclick="getSelected(0);" id="n_tt0" class="easyui-tree"></ul>
								</div>
								<div title="本企业专利管理" style="padding: 10px;"
									data-options="
										selected:true,
										tools: [{
													text:'编辑分类',
													iconCls:'icon-edit',
													handler:function(){
														 $('#categorytype').val(1);
														$('#dlg3').dialog('open');
													}
												},{
													text:'添加分类',
													iconCls:'icon-add',
													handler:function(){
														 $('#categorytype').val(1);
														$('#dlg2').dialog('open');
													}
												},{
													text:'删除分类',
													iconCls:'icon-remove',
													handler:function(){
														  $('#categorytype').val(1);
														 confirmPWD();
													}
												}]"
								>
									<ul ondblclick=" getSelected(1);" id="n_tt1" class="easyui-tree"></ul>
								</div>
								
							</div>
							
						</div>
					</div>
				</div>
				<div id="right" style="width: 808px; min-height: 535px;">
					<!-- display: none -->
					<input type="hidden" id="nodeText" />
					<input type="hidden" id="nodeId" />
					<input type="hidden" id="categorytype" />
					<iframe id="_iframe" width="790px" height="520px" frameborder="0"
						>

					</iframe>


				</div>

			</div>
			<%@include file="/WEB-INF/page/front/share/footer.jsp"%>

	</body>
</html>