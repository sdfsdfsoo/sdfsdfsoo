<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>用户登录</title>
		
		<link href="/css/new/index.css" rel="stylesheet" type="text/css" />
		<script src="/js/jquery-1.8.0.min.js" type="text/javascript"></script>
		<script src="/js/easyui/jquery.easyui.min.js"></script>
		<script src="/js/easyui/locale/easyui-lang-zh_CN.js"></script>
		
		<script type="text/javascript">
	function formSubmit() {
		document.getElementById('loginForm').submit();
	}
	function gotoregister() {
			window.location.href = '/front/user/user_registerUI';
	}
	function getCheckCode(imgObj) {
		imgObj.src = "/front/user/front/user_generateCheckCode?" + Math.random();
	}
	function reloadCheckCode() {
		document.getElementById("checkCodePicture").src = "/front/user/front/user_generateCheckCode?"
				+ Math.random();
	}
</script>
	</head>
	
	<body>
	<div class="logo"><img src="/images/new/logo.png"></div>
	<div class="login_box">
		<form id="loginForm" action="/front/user/front/user_check" method="post">
		    <ul>
		       <li><img src="/images/new/id.png"></li><li><input name="user.username" type="text" id="TextBoxAccount" /></li>
		       <li><img src="/images/new/password.png"></li><li><input name="user.password" type="password"	id="Password" /></li>
		       <li class="code">
			       <img id="checkCodePicture" src="front/user_generateCheckCode" onclick=getCheckCode(this); alt="看不清，换一张" title="看不清，换一张" border="0"/>
			       <input name="checkCode" type="text" id="checkCode" width="20px" alt="看不清，换一张" title="看不清，换一张" />
		       </li>
		       <li>
		       <input type="image" name="" id="ImageButtonLogin" src="/images/new/loginbtn.png" style="border-width: 0px;" onclick="formSubmit();" />
		        <img   src="/images/new/regbtn.png" onclick="gotoregister()"  title="zhuce"	border="0"/>
		        
		       </li>
		    </ul>
		    <ul><li>游客账号为：guest 密码：guest</li></ul>
		</form>
		<br />
		
	</div>
	<div class="foot">建议使用谷歌浏览器<br>
	开发公司：江苏畅远信息科技有限公司  |   技术支持：电话：0511-82016169 传真：0511-88892923 邮箱：zjcy_88@163.com<br>
	地址：镇江市新区丁卯经十五路99号中心研发区C30栋3楼</div>
	</body>
</html>