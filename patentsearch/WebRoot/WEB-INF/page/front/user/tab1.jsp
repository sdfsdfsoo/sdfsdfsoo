<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ include file="/WEB-INF/page/share/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户信息维护</title>
	<link rel="stylesheet" type="text/css" href="/js/easyui/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="/js/easyui/themes/icon.css">
		<script type="text/javascript" src="/js/jquery-1.8.0.min.js"></script>
	<script type="text/javascript" src="/js/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript">
	/* 验证邮箱 */
function isEmail(obj){
	var  reg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
	if(!reg.test(obj.value)){
		alert("邮箱格式错误!");
		obj.value="";
		obj.blur();
	}
}
	
	/* 验证手机号码 */
function isphoneNum(obj){
	var  reg = /^(1[3-9][0-9]\d{8})?$/;
	if(!reg.test(obj.value)){
		alert("手机号码格式错误，注意手机号码11位");
		obj.value="";
		obj.blur();
	
	}
}
	
	/* 验证座机号码 */
function isFixedPhone(obj){
	var reg = /^\d{3}-\d{8}|\d{4}-\d{7,8}$/; 
	if(!reg.test(obj.value)){
		alert("固定电话格式错误，格式如0551-82016169或010-82016169");
		obj.value="";
		obj.blur();
		$("#btn_ok").attr("disabled",true);
	}else{
		$("#btn_ok").attr("disabled",null);
	}
}
		 function saveInfo(){ 
		       var name=$('#_name').val();
		       var phone=$('#_phone').val();
		       var mobile=$('#_mobile').val();
		       var address=$('#_address').val();
		       var email=$('#_email').val();
		       $.ajax({
					   type: "POST",
					   url: "/front/user/user_editRegister",
					    data:{"_name":name,"_phone":phone,"_mobile":mobile,"_address":address,"_email":email},  
					   success: function(msg){
						    
					     var json= jQuery.parseJSON(msg);
                        	 alert(json.msg);
					   }
				});
		 }
		 function checkPwd(){    
		       var txtPWD=$('#txtPWD').val();
		       var txtQueRen=$('#txtQueRen').val();
		      if(txtPWD!=txtQueRen){
		      	  alert("两次密码输入不一致，请重新输入！");
		      	  $('#txtQueRen').val("");
		      }
		 }
		 function editpassword(){ 
		       var txtPWD=$('#txtPWD').val();
		       	$.ajax({
					   type: "POST",
					   url: "/front/user/user_editPassword",
					   data: "txtPWD=" + txtPWD,
					   success: function(msg){
						    
					     var json= jQuery.parseJSON(msg);
                        	 alert(json.msg);
					   }
				});
		  		
	           $('#_dlg').dialog('close');    
		 }
	
	</script>
	
</head>
<body onload="loadRemote();">
		<div style="margin:20px 0;"></div>
		<div style="margin-left:20px">
       <div class="easyui-panel" title="用户信息维护" style="width:700px;">
		<div style="padding:10px 60px 20px 60px">
		 
	    <form id="_ff" method="post">
	  
	    	<table cellpadding="5">
	    	<div>
	    					<dt>
								用户类型：
							</dt>
							<dd>
								<input id="rbtYongHuLeiXing_0" type="radio" name="usertype"
									value="5" disabled="disabled" >
								<label for="rbtYongHuLeiXing_0">
									个人
								</label>
								<input id="rbtYongHuLeiXing_1" type="radio" name="usertype"
									value="3" disabled="disabled">
								<label for="rbtYongHuLeiXing_1">
									企业
								</label>
							</dd>
				</div>			
	    		<tr>
	    			<td>用户名:</td>
	    			<td><input class="easyui-validatebox textbox"  type="text" name="username" data-options="required:true" readonly="readonly"></input></td>
	    		</tr>
	    		
	    		<tr>
	    			<td>姓名:</td>
	    			<td><input class="easyui-validatebox textbox" type="text" name="name" id="_name" data-options="required:true"></input></td>
	    		</tr>
	    		<tr>
	    			<td>电话:</td>
	    			<td><input class="textbox" name="phone" id="_phone" onchange="isFixedPhone(this)"></input></td>
	    		</tr>
	    		<tr>
	    			<td>手机:</td>
	    			<td><input class="easyui-validatebox textbox" type="text" name="mobile" id="_mobile" onchange="isphoneNum(this)" data-options="required:true"></input></td>
	    		</tr>
	    		<tr>
	    			<td>通信地址:</td>
	    			<td><input class="easyui-validatebox textbox" type="text" name="address" id="_address" data-options="required:true"></input></td>
	    		</tr>
	    		<tr>
	    			<td>邮箱:</td>
	    			<td><input class="easyui-validatebox textbox" type="text" name="email" id="_email" onchange="isEmail(this)" data-options="required:true"></input></td>
	    		</tr>
	    		
	    	</table>
	    </form>
	    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="saveInfo()">保存修改</a>
	    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="  $('#_dlg').dialog('open');">修改密码</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" onclick="loadRemote()">重置</a>
	    </div>
	</div>
	</div>
	
	<div id="_dlg" closed="true"  class="easyui-dialog" title="修改密码" data-options="iconCls:'icon-ok'" style="width:300px;height:190px;padding:10px" >
				
					<div id="dlg-buttons" align="center">		
					<br/><br/>
								 新密码：&nbsp;&nbsp;<input name="user.password" type="password" id="txtPWD"
									class="textBox easyui-validatebox"
									data-options="validType:'pd',missingMessage:' 6~20位英文、数字或常用符号'"><br/>
								确认密码：<input name="password" type="password" id="txtQueRen" onchange="checkPwd();"
									class="textBox easyui-validatebox" value=""
									data-options="missingMessage:'请再次输入密码'" /><br/><br/><br/>
								<a id="getNodeNameId"  class="easyui-linkbutton" onclick="editpassword();" >确定</a>
								<a href="javascript:void(0)" class="easyui-linkbutton" onclick="javascript:$('#_dlg').dialog('close');">取消</a>
				</div>
	</div>
	<style scoped="scoped">
		.textbox{
			height:20px;
			margin:0;
			padding:0 2px;
			box-sizing:content-box;
		}
	</style>
	<script>
	
		function loadRemote(){
			$('#_ff').form('load', '/front/user/user_loadJson');
		
		}
	</script>
</body>
</html>