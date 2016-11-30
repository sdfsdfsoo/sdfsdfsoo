<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/WEB-INF/page/share/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<title>专利检索分析系统</title>
<link href="/css/new/style.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="/js/jquery-1.8.0.min.js"></script>
<link href="/js/artdialog2/ui-dialog.css" rel="stylesheet"
			type="text/css" />
		<script type="text/javascript" src="/js/artdialog2/dialog-min.js"></script>
</head>
<body>

<script type="text/javascript">
	function updateCellPhone(value){
		$("#cellPhoneSpan").html("<input id='cellPhoneInput' value='"+value+"'/>");
		$("#updateCellPhoneHref").hide();
		$("#confirmCellPhoneHref").show();
	}


	function confirmCellPhone(){


		var mobile = $('#cellPhoneInput').val();
		var myreg = /^1\d{10}$|^(0\d{2,3}-?|\(0\d{2,3}\))?[1-9]\d{4,7}(-\d{1,8})?$/;
	    if(mobile.length==0)
	    {
	       alert('请输入手机号码或固定电话！');
	       $('#cellPhoneInput').val("");
	       return false;
	    }    
	    if(mobile.length!=11 && !myreg.test(mobile))
	    {
	        alert('请输入有效的手机号码或固定电话！');
	        $('#cellPhoneInput').val("");
	        return false;
	    }
		
		$.ajax({
			type:'post',
			data:{
			mobile:$("#cellPhoneInput").val()
			},
			url:'user_updateUser',
			success:function(data){
				location.reload();
			}
		});
		
		$("#cellPhoneSpan").html($("#cellPhoneInput").val());
		$("#updateCellPhoneHref").show();
		$("#confirmCellPhoneHref").hide();
	}

	function updateName(value){
		$("#nameSpan").html("<input id='nameInput' value='"+value+"'/>");
		$("#updateNameHref").hide();
		$("#confirmNameHref").show();
	}

	function confirmName(){

		if($("#nameInput").val()==""){
			alert("请填写新的名称");
			return false;
		}
		
		$.ajax({
			type:'post',
			data:{
			name:$("#nameInput").val()
			},
			url:'user_updateUser',
			success:function(data){
				location.reload();
			}
		});
		
		$("#nameSpan").html($("#nameInput").val());
		$("#updateNameHref").show();
		$("#confirmNameHref").hide();
	}

	function updateAddress(value){
		$("#addressSpan").html("<input id='addressInput' value='"+value+"'/>");
		$("#updateAddressHref").hide();
		$("#confirmAddressHref").show();
	}

	function confirmAddress(){

		if($("#addressInput").val()==""){
			alert("请填写新的地址");
			return false;
		}
		
		$.ajax({
			type:'post',
			data:{
			address:$("#addressInput").val()
			},
			url:'user_updateUser',
			success:function(data){
				location.reload();
			}
		});
		
		$("#addressSpan").html($("#addressInput").val());
		$("#updateAddressHref").show();
		$("#confirmAddressHref").hide();
	}

	function updateContactPhone(value){
		$("#contactPhoneSpan").html("<input id='contactPhoneInput' value='"+value+"'/>");
		$("#updateContactPhoneHref").hide();
		$("#confirmContactPhoneHref").show();
	}

	function confirmContactPhone(){

		if($("#contactPhoneInput").val()==""){
			alert("请填写新的电话号码");
			return false;
		}
		
		$.ajax({
			type:'post',
			data:{
			phone:$("#contactPhoneInput").val()
			},
			url:'user_updateUser',
			success:function(data){
				location.reload();
			}
		});
		
		$("#contactPhoneSpan").html($("#contactPhoneInput").val());
		$("#updateContactPhoneHref").show();
		$("#confirmContactPhoneHref").hide();
	}

	function updateEmail(value){
		$("#emailSpan").html("<input id='emailInput' value='"+value+"'/>");
		$("#updateEmailHref").hide();
		$("#confirmEmailHref").show();
	}

	function confirmEmail(){

		if($("#emailInput").val()==""){
			alert("请填写新的电话号码");
			return false;
		}
		
		$.ajax({
			type:'post',
			data:{
			email:$("#emailInput").val()
			},
			url:'user_updateUser',
			success:function(data){
				location.reload();
			}
		});
		
		$("#emailSpan").html($("#emailInput").val());
		$("#updateEmailHref").show();
		$("#confirmEmailHref").hide();
	}

	function updatePwd(){
		 var d = dialog({
			title: '更改密码',
		    button: [
		        {
		            value: '确定',
		            callback: function () {
		        		$.ajax({
							type:'post',
							data:{
		        				txtPWD:$("#newpwd").val()
							},
							url:'user_editPassword',
							success:function(data){

							}
		        		});
		            }
		        },
		        {
		            value: '取消',
		            callback: function () {
		        		d.close().remove();
		            }
		        }
		    ],
		    content:
			    '新的密码：<input type="text" name="newpwd" id="newpwd">'
		});
		d.showModal();
	}
	
</script>

<%@include file="/WEB-INF/page/front/share/top.jsp"%>
<div class="yhzx">
   <div class="yhzx_title"><a href="javascript:updatePwd()">修改密码</a>用户信息</div>
   <div class="yhzx_yhxx">
      <ul>
         <li class="right18"><span></span>用户名：${user.username }</li>
         <li>
         	<span>
         		<a id="updateCellPhoneHref" href="javascript:updateCellPhone('${user.contactCellphone }')">修改</a>
         		<a id="confirmCellPhoneHref" style="display:none" href="javascript:confirmCellPhone()">确认修改</a>
         	</span>
        	手　机：
        	<font id="cellPhoneSpan">${user.contactCellphone }</font>
         </li>
      </ul>
      <ul>
         <li  class="right18">
         	<span>
         		<a id="updateNameHref" href="javascript:updateName('${user.name }')">修改</a>
         		<a id="confirmNameHref" style="display:none" href="javascript:confirmName()">确认修改</a>
         	</span>
         	公司名：
         	<font id="nameSpan">${user.name }</font>
         	
         </li>
         <li>
         	<span>
         		<a id="updateAddressHref" href="javascript:updateAddress('${user.address }')">修改</a>
         		<a id="confirmAddressHref" style="display:none" href="javascript:confirmAddress()">确认修改</a>
         	</span>地　址：
         	<font id="addressSpan">${user.address }</font>
         	
         </li>
      </ul><%--
      <ul>
         <li class="right18">
         
         	<span>
         		<a id="updateContactPhoneHref" href="javascript:updateContactPhone('${user.contactPhone }')">修改</a>
         		<a id="confirmContactPhoneHref" style="display:none" href="javascript:confirmContactPhone()">确认修改</a>
         	</span>
         	电　话：
         	<font id="contactPhoneSpan">${user.contactPhone }</font>
         	
		</li>
         <li>
         
         	<span>
         		<a id="updateEmailHref" href="javascript:updateEmail('${user.email }')">修改</a>
         		<a id="confirmEmailHref" style="display:none" href="javascript:confirmEmail()">确认修改</a>
         	</span>
         	邮　箱：
         	<font id="emailSpan">${user.email }</font>
         </li>
      </ul>
   --%></div>
  <div class="yhzx_title marb20">信息中心</div>
   <div class="yhzx_del"><span><input type="checkbox"></span><input type="button" value="删除所选"></div>
   <div class="yhzx_xxzx">
      <dl>
         <dt><input type="checkbox"></dt>
         <dd><span>2016-02-01</span>一周专利监控报告</dd>
      </dl>
   </div>
</div>
<div class="foot">建议使用谷歌浏览器<br>
开发公司：江苏畅远信息科技有限公司  |   技术支持：电话：0511-82016169 传真：0511-88892923 邮箱：zjcy_88@163.com<br>
地址：镇江市新区丁卯经十五路99号中心研发区C30栋3楼</div>
</body>
</html>