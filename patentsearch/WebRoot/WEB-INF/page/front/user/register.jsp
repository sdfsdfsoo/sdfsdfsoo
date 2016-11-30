<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>用户注册</title>

		<link href="/js/easyui/themes/icon.css" rel="stylesheet"
			type="text/css" />
		<link href="/js/easyui/themes/default/easyui.css" rel="stylesheet"
			type="text/css" />
		<link href="/css/index.css" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" type="text/css"
			href="/css/userCenter/register.css">
		<script type="text/javascript" src="/js/jquery-1.8.0.min.js"></script>
		<script src="/js/easyui/jquery.easyui.min.js"></script>
		<script src="/js/easyui/locale/easyui-lang-zh_CN.js"></script>
		<script src="/js/list/jquery.textSearch-1.0.js"></script>
		<script src="/js/myValidate.js"></script>
		<script type="text/javascript">
		function checkpwd(){
			var passwd=document.getElementById('txtPWD').value;
			if(passwd.length<6){
		        	alert("密码长度过短，请重新输入！");
			   		document.getElementById('txtPWD').value="";
			}
		}
		function pwdQueRen(){
				var passwd=document.getElementById('txtPWD').value;
				var passwd2=document.getElementById('txtQueRen').value;
			   if(passwd!=passwd2){
			   		alert("两次输入的密码不一致，请重新输入！");
			   		document.getElementById('txtQueRen').value="";
			   }
			}

		function switchToPerson(obj){
			$("#namefont").html("用户名称");
		}
		function switchToCompany(obj){
			$("#namefont").html("公司名称");
		}
</script>
	</head>
	<body>
		<div style="width: 1000px; margin: 0 auto;">
			<%@include file="/WEB-INF/page/front/share/help.jsp"%>
			<%@include file="/WEB-INF/page/front/share/top.jsp"%>
			<div id="mid" class="base b">
				<div class="registerContent">
					<form method="post" id="_form" action="/front/user/user_register">
					
						<dl class="user">
							<dt>
								用户类型：
							</dt>
							<c:if test="${regUserType=='5'}">
							<dd>
								<input id="rbtYongHuLeiXing_0" type="radio" name="user.userType"
									value="5" checked="checked" onclick=
	switchToPerson(this);
>
								<label for="rbtYongHuLeiXing_0">
									个人
								</label>
								<input id="rbtYongHuLeiXing_1" type="radio" name="user.userType"
									value="3" onclick=
	switchToCompany(this);
>
								<label for="rbtYongHuLeiXing_1">
									企业
								</label>
							</dd>
							</c:if>
								<c:if test="${regUserType!='5'}">
							<dd>
								<input id="rbtYongHuLeiXing_0" type="radio" name="user.userType"
									value="5"  onclick=
	switchToPerson(this);
>
								<label for="rbtYongHuLeiXing_0">
									个人
								</label>
								<input id="rbtYongHuLeiXing_1" type="radio" name="user.userType"
									value="3" checked="checked" onclick=
	switchToCompany(this);
>
								<label for="rbtYongHuLeiXing_1">
									企业
								</label>
							</dd>
							</c:if>
							<dt>
								用户名&nbsp：
							</dt>
							<dd>
								<input name="user.username" type="text" id="user.username"
									 class="textBox easyui-validatebox"
									data-options="required:true,missingMessage:'6~20位英文、数字'"
									onChange="checkUsername()" value="${regUserName }">  <a>*必填</a>
									
							</dd>
							<dd>
							&nbsp
								<label id="checkmessage">
								</label>
								</dd>
							<dt>
								密&nbsp&nbsp码：
							</dt>
							<dd>
								<input name="user.password" type="password" id="txtPWD" onchange="checkpwd()"
									class="textBox easyui-validatebox"
									data-options="required:true,validType:'pd',missingMessage:' 6~20位英文、数字或常用符号'" ></input>  <a>*必填</a>
							</dd>

							<dt>
								确认密码：
							</dt>
							<dd>
								<input name="password" type="password" id="txtQueRen" onchange="pwdQueRen()"
									class="textBox easyui-validatebox" value=""
									data-options="required:true,missingMessage:'请再次输入密码'" />   <a>*必填</a>
							</dd>
							<dt>
								<font id="namefont">用户名称：</font>
							</dt>
							<dd>
								<input name="user.name" type="text" id="txtRealName"
									class="textBox easyui-validatebox"
									data-options="required:true,missingMessage:'请输入名称'" value="${regName }"/>  <a>*必填</a>
							</dd>
						<!--  	<dt>
								电话：
							</dt>
							<dd>
								<input name="user.contactPhone" type="text" id="txtDianHua" onchange="isFixedPhone(this)"
									class="textBox easyui-validatebox"
									data-options="required:true,validType:'phone'，missingMessage:'请输入联系人电话'，errorMessage:'电话格式错误，请重新输入！'" value="${regPhone }"/>   <a>*必填</a>
							</dd>
							-->
							<dt>
								手&nbsp&nbsp机：
							</dt>
							<dd>
								<input name="user.contactCellphone" type="text" id="txtShouJi"  onchange="isphoneNum(this)"
									class="textBox easyui-validatebox"
									data-options="required:true,missingMessage:'请输入联系人手机,注意手机号码11位'"  value="${regCellPhone }"/>  <a>*必填</a>
							</dd>
							<dt>
								通信地址：
							</dt>
							<dd>
								<input name="user.address" type="text" id="txtDiZhi" onchange="isAddress(this)"
									 class="textBox easyui-validatebox" data-options="required:true,missingMessage:'请输入联系人通信地址'"  value="${regAddress }"/>
							</dd>
							<!-- 
							<dt>
								邮箱：
							</dt>
							<dd>
								<input name="user.email" type="text" id="txtYouXiang" onchange="isEmail(this)"
									class="textBox easyui-validatebox"
									data-options="required:true,validType:'email',missingMessage:'请输入联系人邮箱，邮箱务必真实有效',errorMessage:'邮箱地址格式错误，请重新输入！'"  value="${regEmail }"/> <a>*</a>
								<span id="RegularExpressionValidator2"
									style="color: Red; display: none;"></span>
							</dd>
							-->
							<dt>
								&nbsp
							</dt>
							<dd style="text-align: center;">
								<a href="javascript:formSubmitaa()"><img src="/images/userCenter/zhuce_bj2.png" style="border-width: 0px;" /></a>
								&nbsp;
								<a href="/front/user/user_loginUI"><img src="/images/userCenter/Return.jpg" style="border-width: 0px;" /></a>
							</dd>
						</dl>
					</form>
				</div>
			</div>
			<%@include file="/WEB-INF/page/front/share/footer.jsp"%>
		</div>
	</body>
</html>