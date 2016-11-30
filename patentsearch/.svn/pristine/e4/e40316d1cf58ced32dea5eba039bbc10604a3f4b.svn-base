<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="help">
    <c:if test="${ !empty user}"><span class="Topuser"> 欢迎您：${user.name}  &nbsp;&nbsp;&nbsp;<a href="/front/user/user_logout" target="_parent">退出登录</a></span></c:if>
	<c:if test="${ empty user}"><span class="Topuser"> <a href="/front/user/user_loginUI">请登录</a>或 <a href="/front/user/user_registerUI" >欢迎注册</a></span></c:if>
</div>